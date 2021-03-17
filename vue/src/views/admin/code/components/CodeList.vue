<template>
  <div>
    <button-set
      :loading="saving"
      add-button
      @click:add="showAddDialog"
      delete-button
      :delete-disabled="!selected || selected.length === 0"
      @click:delete="remove"
      reload-button
      @click:reload="getList"
    />
    <v-card flat>
      <v-card-text class="py-0">
        <refresh-data-bar ref="refRefreshDataBar" @reload="getList" />
        <v-data-table
          v-model="selected"
          must-sort
          fixed-header
          :loading="loading"
          :headers="headers"
          :items="filteredItems"
          :sort-by="sortBy"
          :sort-desc="sortDesc"
          item-key="value"
          single-select
          show-select
          dense
          :footer-props="envs.FOOTER_PROPS_MAX_100"
        >
          <template #header>
            <data-table-client-side-filter
              :headers="headers"
              :output.sync="filteredItems"
              :input="items"
            />
          </template>
          <template #[`item.value`]="{ item }">
            <span class="text--anchor" @click="showEditDialog(item)">
              {{ item.value }}
            </span>
          </template>
          <template #[`item.available`]="{ item }">
            <span style="display: inline-flex">
              <v-checkbox
                readonly
                :input-value="item.available"
                :ripple="false"
                dense
                hide-details
                class="mt-0"
              />
            </span>
          </template>
          <template v-if="AUTHORITY" #[`item.authority`]="{ item }">
            {{ item.authority | getCodeText(AUTHORITY) }}
          </template>
          <template #[`item.updatedBy`]="{ item }">
            {{ item.updatedBy | formatMemberNm }}
          </template>
          <template #[`item.updated`]="{ item }">
            {{ item.updated | formatDatetime }}
          </template>
        </v-data-table>
      </v-card-text>
      <code-edit-dialog
        v-model="editItem"
        :dialog.sync="dialog"
        @created="onCreated"
        @updated="onUpdated"
        v-if="dialog"
      />
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Ref, Vue, Watch } from "vue-property-decorator";
import type { DataTableHeader, SelectItem } from "@/common/types";
import { deleteApi, getApi, getCodesApi } from "@/utils/apis";
import envs from "@/constants/envs";
import DataTableClientSideFilter from "@/components/datatable/DataTableClientSideFilter.vue";
import qs from "querystring";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import CodeEditDialog from "@/views/admin/code/components/CodeEditDialog.vue";
import { confirmDelete } from "@/utils/alerts";
import { defaultCodeEntity } from "@/common/values";
import type { CodeEntity } from "@/common/entities";
import _ from "lodash";
import RefreshDataBar from "@/components/history/RefreshDataBar.vue";

@Component({
  name: "CodeList",
  components: {
    RefreshDataBar,
    CodeEditDialog,
    ButtonSet,
    DataTableClientSideFilter,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly type!: string;
  @Ref() readonly refRefreshDataBar!: RefreshDataBar;

  readonly envs: typeof envs = envs;

  selected: CodeEntity[] = [];
  AUTHORITY: SelectItem[] = [];
  saving = false;
  loading = false;
  sortBy: string[] = ["displayOrder"];
  sortDesc: boolean[] = [false];
  items: CodeEntity[] = [];
  filteredItems: CodeEntity[] = [];
  dialog = false;
  editItem: CodeEntity = defaultCodeEntity();

  get headers(): DataTableHeader[] {
    return [
      {
        text: "코드",
        align: "start",
        value: "value",
      },
      {
        text: "코드명",
        align: "start",
        value: "name",
      },
      {
        text: "사용 가능",
        align: "center",
        value: "available",
        filterType: "switch",
        width: "6rem",
      },
      {
        text: "출력 순서",
        align: "end",
        value: "displayOrder",
        width: "6rem",
      },
      {
        text: "권한(~부터 볼수 있음)",
        align: "center",
        value: "authority",
        filterType: "select",
        filterSelectItem: this.AUTHORITY,
        width: "11rem",
      },
      {
        text: "작업 일시",
        align: "center",
        value: "updated",
        filterable: false,
        width: "10rem",
      },
      {
        text: "작업자",
        align: "start",
        value: "updatedBy",
        filterable: false,
        width: "7rem",
      },
    ];
  }

  protected async created(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  @Watch("type")
  public async getList(): Promise<void> {
    this.selected = [];
    this.items = [];
    if (this.type) {
      this.loading = true;
      const response = await getApi<CodeEntity[]>(
        `admin/codes/?${qs.stringify({ type: this.type })}`,
      );
      this.loading = false;
      this.items = response?.data || [];
    }
    this.refRefreshDataBar.triggerRefreshed();
  }

  @Emit("created")
  protected onCreated(value: CodeEntity): CodeEntity {
    this.items = [value, ...this.items];
    return value;
  }

  protected onUpdated(value: CodeEntity): void {
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
    this.editItem = { ...defaultCodeEntity(), type: this.type };
    this.dialog = true;
  }

  protected showEditDialog(value: CodeEntity): void {
    this.editItem = _.cloneDeep(value);
    this.dialog = true;
  }

  protected async remove(): Promise<void> {
    const result = await confirmDelete();
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<CodeEntity>(
        `admin/codes/${this.selected[0].id}/`,
      );
      this.saving = false;
      if (response?.code?.startsWith("S")) {
        window.localStorage.removeItem(`code__${this.selected[0].id}`);
        this.items = this.items.filter(
          (item) => item.id !== (response.data?.id || 0),
        );
      }
    }
  }
}
</script>
