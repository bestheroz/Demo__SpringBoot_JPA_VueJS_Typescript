<template>
  <div>
    <button-set
      :loading="saving"
      reload-button
      @click:reload="getList"
      add-button
      @click:add="showAddDialog"
      save-button
      save-text="순서저장"
      @click:save="saveItems"
    />
    <v-card flat>
      <v-card-text>
        <v-list dense>
          <draggable
            tag="div"
            v-model="items"
            v-bind="dragOptions"
            handle=".drag-handle"
          >
            <transition-group type="transition" name="flip-list">
              <v-list-item
                :key="item.id"
                v-for="item in items"
                class="elevation-1"
                dense
                @dblclick="showEditDialog(item)"
              >
                <v-list-item-icon>
                  <v-icon v-text="item.icon" />
                </v-list-item-icon>
                <v-list-item-content style="display: inline-block" class="py-0">
                  <v-icon color="primary" class="drag-handle">
                    mdi-sort
                  </v-icon>
                  {{ item.name }}
                </v-list-item-content>
                <v-list-item-content style="display: inline-block" class="py-0">
                  {{ item.url }}
                </v-list-item-content>
                <v-list-item-action style="display: inline-block" class="my-0">
                  <v-icon color="error" @click="onDelete(item)">
                    mdi-minus
                  </v-icon>
                </v-list-item-action>
              </v-list-item>
            </transition-group>
          </draggable>
        </v-list>
      </v-card-text>
    </v-card>
    <menu-edit-dialog
      v-model="editItem"
      :dialog.sync="dialog"
      @created="onCreated"
      @updated="onUpdated"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { deleteApi, getApi, postApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import MenuEditDialog from "@/views/admin/menu/components/MenuEditDialog.vue";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import draggable from "vuedraggable";
import { defaultMenuEntity } from "@/common/values";
import _ from "lodash";
import type { MenuEntity } from "@/common/entities";

interface MenuVO extends MenuEntity {
  children: MenuEntity[];
}

@Component({
  name: "MenuList",
  components: { ButtonSet, MenuEditDialog, draggable },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;
  items: MenuEntity[] = [];
  loading = false;
  saving = false;
  drag = false;

  editItem: MenuEntity = defaultMenuEntity();
  dialog = false;

  get dragOptions(): { animation: number } {
    return {
      animation: 200,
    };
  }

  protected mounted(): void {
    this.getList();
  }

  public async getList(): Promise<void> {
    this.items = [];
    this.loading = true;
    const response = await getApi<MenuVO[]>("admin/menus/");
    this.loading = false;
    this.items = response?.data || [];
  }

  protected onCreated(value: MenuEntity): void {
    this.items = [value, ...this.items];
  }

  protected onUpdated(value: MenuEntity): void {
    const findIndex = this.items.findIndex(
      (item) => item.id === this.editItem.id,
    );
    this.items = [
      ...this.items.slice(0, findIndex),
      value,
      ...this.items.slice(findIndex + 1),
    ];
  }

  protected showAddDialog(): void {
    this.editItem = defaultMenuEntity();
    this.dialog = true;
  }
  protected showEditDialog(value: MenuEntity): void {
    this.editItem = _.cloneDeep(value);
    this.dialog = true;
  }

  protected async onDelete(value: MenuEntity): Promise<void> {
    const result = await confirmDelete();
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<MenuEntity>(`admin/menus/${value.id}/`);
      this.saving = false;
      if (response?.code?.startsWith("S")) {
        await this.$store.dispatch("initDrawers");
        this.items = this.items.filter(
          (item) => item.id !== (response.data?.id || 0),
        );
      }
    }
  }

  protected async saveItems(): Promise<void> {
    let parentId = 0;
    this.saving = true;
    const response = await postApi<MenuEntity[]>(
      "admin/menus/save",
      this.items.map((item, index) => {
        if (item.type === "G") {
          parentId = item.id || 0;
        }
        return {
          ...item,
          displayOrder: index + 1,
          parentId: item.type === "G" ? 0 : parentId,
        };
      }),
    );
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initDrawers");
      this.items = response.data || [];
    }
  }
}
</script>
