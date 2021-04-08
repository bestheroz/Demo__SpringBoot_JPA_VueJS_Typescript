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
          <v-col cols="5">
            <v-list dense>
              <draggable
                tag="div"
                v-model="vModel.items"
                v-bind="dragOptions"
                handle=".drag-handle"
                @end="onDraggableEnd"
              >
                <transition-group type="transition" name="flip-list">
                  <v-list-item
                    :key="item.displayOrder"
                    v-for="item in vModel.items"
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
                    </v-list-item-content>
                    <br />
                    <v-chip-group
                      multiple
                      dense
                      v-model="item.typesJson"
                      active-class="accent"
                    >
                      <v-chip :value="AUTHORITY_TYPE.VIEW" disabled>
                        <v-icon>mdi-eye</v-icon>
                      </v-chip>
                      <v-chip
                        filter
                        outlined
                        :value="AUTHORITY_TYPE.WRITE"
                        :disabled="vModel.code === 'SUPER'"
                      >
                        <v-icon>mdi-content-save-outline</v-icon>
                      </v-chip>
                      <v-chip
                        filter
                        outlined
                        :value="AUTHORITY_TYPE.DELETE"
                        :disabled="vModel.code === 'SUPER'"
                      >
                        <v-icon>mdi-delete-outline</v-icon>
                      </v-chip>
                    </v-chip-group>
                  </v-list-item>
                </transition-group>
              </draggable>
            </v-list>
          </v-col>
          <v-col cols="7">
            <v-card-text class="py-0 elevation-1">
              <v-chip-group
                v-model="selectedChips"
                column
                multiple
                active-class="accent"
                dense
                @change="onChangeSelectedChip"
              >
                <v-chip
                  v-for="item in menus"
                  :value="item"
                  :key="item.id"
                  filter
                  outlined
                  :disabled="vModel.code === 'SUPER'"
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
import { defaultAuthorityItemEntity, defaultMenuEntity } from "@/common/values";
import type { AuthorityEntity, MenuEntity } from "@/common/entities";
import RefreshDataBar from "@/components/history/RefreshDataBar.vue";
import { AUTHORITY_TYPE } from "@/common/selections";

@Component({
  name: "AuthorityList",
  components: { RefreshDataBar, ButtonSet, draggable },
})
export default class extends Vue {
  @VModel({ required: true }) vModel!: AuthorityEntity;
  @Ref() readonly refRefreshDataBar!: RefreshDataBar;

  menus: MenuEntity[] = [];
  selectedChips: MenuEntity[] = [];
  loading = false;
  saving = false;
  drag = false;

  readonly AUTHORITY_TYPE = AUTHORITY_TYPE;

  get dragOptions(): { animation: number } {
    return {
      animation: 200,
    };
  }

  protected async created(): Promise<void> {
    const response = await getApi<MenuEntity[]>("admin/menus/");
    this.menus = response?.data || [];
    this.watchItem(this.vModel);
  }

  @Watch("vModel")
  protected watchItem(val: AuthorityEntity): void {
    if (val.code === "SUPER") {
      this.selectedChips = this.menus;
    } else {
      this.selectedChips = val.items.map(
        (item) =>
          this.menus.find((menu) => menu.id === item.menu?.id) ||
          defaultMenuEntity(),
      );
    }
    this.onChangeSelectedChip(this.selectedChips);
  }

  public triggerRefreshed(): void {
    this.refRefreshDataBar.triggerRefreshed();
  }
  protected onDraggableEnd(): void {
    this.vModel.items = this.vModel.items.map((item, index) => {
      return { ...item, displayOrder: index + 1 };
    });
  }

  protected onChangeSelectedChip(selectedChips: MenuEntity[]): void {
    this.vModel.items = selectedChips.map((select, index) => {
      return {
        ...(this.vModel.items.find((item) => item.menu.id === select.id) || {
          ...defaultAuthorityItemEntity(),
          menu: select,
          typesJson:
            this.vModel.code === "SUPER"
              ? [
                  AUTHORITY_TYPE.VIEW,
                  AUTHORITY_TYPE.WRITE,
                  AUTHORITY_TYPE.DELETE,
                ]
              : [AUTHORITY_TYPE.VIEW],
        }),
        displayOrder: index + 1,
      };
    });
  }

  protected async saveItems(): Promise<void> {
    this.saving = true;
    const response = await postApi<AuthorityEntity>(
      `admin/authorities/${this.vModel.code}`,
      this.vModel,
    );
    this.saving = false;
    if (response?.code?.startsWith("S") && response.data) {
      await this.$store.dispatch("initAuthority");
      this.vModel = response.data;
    }
  }
}
</script>
