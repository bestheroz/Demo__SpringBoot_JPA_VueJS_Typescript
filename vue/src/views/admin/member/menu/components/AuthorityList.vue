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
                        active-class="primary"
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
                v-model="selected"
                column
                multiple
                active-class="accent"
                dense
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
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";

@Component({
  name: "AuthorityList",
  components: { ButtonIconTooltip, RefreshDataBar, ButtonSet, draggable },
})
export default class extends Vue {
  @VModel({ required: true }) item!: AuthorityEntity;
  @Ref() readonly refRefreshDataBar!: RefreshDataBar;

  menus: MenuEntity[] = [];
  selected: MenuEntity[] = [];
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
  }

  @Watch("selected")
  watchSelected(selected: MenuEntity[]): void {
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
    console.log(this.item.items);
  }

  protected async saveItems(): Promise<void> {
    this.saving = true;
    console.log(this.item);
    const response = await postApi<AuthorityEntity>(
      `admin/authorities/${this.item.code}`,
      this.item,
    );
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initDrawers");
      this.item = response.data;
    }
  }
}
</script>
