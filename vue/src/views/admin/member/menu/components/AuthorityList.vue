<template>
  <div>
    <button-set
      :loading="saving"
      reload-button
      @click:reload="$emit('click:reload')"
      save-button
      @click:save="saveItems"
    />
    <v-card flat :loading="loading">
      <v-card-text>
        <refresh-data-bar
          ref="refRefreshDataBar"
          @reload="$emit('click:reload')"
        />
        <v-row dense>
          <v-col cols="3">
            <v-list dense>
              <draggable
                tag="div"
                v-model="item.items"
                v-bind="dragOptions"
                handle=".drag-handle"
                @end="onDraggableEnd"
              >
                <transition-group type="transition" name="flip-list">
                  <v-list-item
                    :key="item.displayOrder"
                    v-for="item in item.items"
                    class="elevation-1"
                    dense
                  >
                    <v-list-item-icon>
                      <v-icon v-text="item.menu.icon" />
                    </v-list-item-icon>
                    <v-list-item-content
                      style="display: inline-block"
                      class="py-0"
                    >
                      <v-icon color="primary" class="drag-handle">
                        mdi-sort
                      </v-icon>
                      {{ item.menu.name }}
                      <br />
                      <v-chip-group
                        multiple
                        dense
                        v-model="item.typesJson"
                        active-class="accent"
                      >
                        <v-chip value="VIEW" disabled>
                          <v-icon>mdi-eye</v-icon>
                        </v-chip>
                        <v-chip filter outlined value="WRITE">
                          <v-icon>mdi-content-save-outline</v-icon>
                        </v-chip>
                        <v-chip filter outlined value="DELETE">
                          <v-icon>mdi-delete-outline</v-icon>
                        </v-chip>
                      </v-chip-group>
                    </v-list-item-content>
                  </v-list-item>
                </transition-group>
              </draggable>
            </v-list>
          </v-col>
          <v-col cols="9">
            <v-card-text class="py-0 elevation-1">
              <v-chip-group
                v-model="selectedChips"
                column
                multiple
                active-class="accent"
                dense
                @change="onChangeSelected"
              >
                <v-chip
                  v-for="item in menus"
                  :value="item"
                  :key="item.id"
                  filter
                  outlined
                >
                  <v-icon v-text="item.icon" v-if="item.icon" />
                  {{ item.name }}
                </v-chip>
              </v-chip-group>
            </v-card-text>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Ref, VModel, Vue, Watch } from "vue-property-decorator";
import { getApi, postApi } from "@/utils/apis";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import draggable from "vuedraggable";
import { defaultAuthorityItemEntity } from "@/common/values";
import type { AuthorityEntity, MenuEntity } from "@/common/entities";
import RefreshDataBar from "@/components/history/RefreshDataBar.vue";

@Component({
  name: "AuthorityList",
  components: { RefreshDataBar, ButtonSet, draggable },
})
export default class extends Vue {
  @VModel({ required: true }) item!: AuthorityEntity;
  @Ref() readonly refRefreshDataBar!: RefreshDataBar;

  menus: MenuEntity[] = [];
  selectedChips: MenuEntity[] = [];
  loading = false;
  saving = false;
  drag = false;

  get dragOptions(): { animation: number } {
    return {
      animation: 200,
    };
  }

  protected async created(): Promise<void> {
    const response = await getApi<MenuEntity[]>("admin/menus/");
    this.menus = response?.data || [];
    this.watchItem(this.item);
  }

  @Watch("item")
  protected watchItem(val: AuthorityEntity): void {
    this.selectedChips = val.items.map((item) =>
      this.menus.find((menu) => menu.id === item.menu.id),
    );
  }

  protected onDraggableEnd(): void {
    console.log("onDraggableEnd");
    this.item.items = this.item.items.map((item, index) => {
      return { ...item, displayOrder: index + 1 };
    });
  }

  protected onChangeSelected(selected: MenuEntity[]): void {
    this.item.items = selected.map((select, index) => {
      const find = this.item.items.find((item) => item.id === select.id);
      if (find) {
        return { ...find, displayOrder: index + 1 };
      } else {
        return {
          ...defaultAuthorityItemEntity(),
          menu: select,
          displayOrder: index + 1,
        };
      }
    });
  }

  protected async saveItems(): Promise<void> {
    this.saving = true;
    const response = await postApi<AuthorityEntity>(
      `admin/authorities/${this.item.code}`,
      this.item,
    );
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initAuthority");
      this.item = response.data;
    }
  }
}
</script>
