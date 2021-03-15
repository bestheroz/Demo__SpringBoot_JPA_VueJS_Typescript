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
      excel-button
      @click:excel="excel"
    />
    <v-card flat>
      <v-card-text class="pb-0">
        <v-data-table
          v-model="selected"
          must-sort
          fixed-header
          :loading="loading"
          :headers="headers"
          :items="items"
          :options.sync="pagination"
          item-key="id"
          single-select
          show-select
          dense
          :height="height"
          :footer-props="envs.FOOTER_PROPS_MAX_100"
        >
          <template #header>
            <data-table-filter
              :headers="headers"
              :filter.sync="datatableFilter"
            />
          </template>
          <template #[`item.id`]="{ item }">
            <a
              class="text--anchor"
              @click="showEditDialog({ ...item, password: undefined })"
            >
              {{ item.id }}
            </a>
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
          <template #[`item.expired`]="{ item }">
            {{ item.expired | formatDatetime }}
          </template>
          <template #[`item.updated`]="{ item }">
            {{ item.updated | formatDatetime }}
          </template>
          <template #[`item.updatedBy`]="{ item }">
            {{ item.updatedBy | formatMemberNm }}
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <member-edit-dialog
      v-model="editItem"
      :dialog.sync="dialog"
      @created="onCreated"
      @updated="onUpdated"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import type {
  DataTableHeader,
  PageResult,
  Pagination,
  SelectItem,
} from "@/common/types";
import { deleteApi, getApi, getCodesApi, getExcelApi } from "@/utils/apis";
import envs from "@/constants/envs";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import MemberEditDialog from "@/views/admin/member/components/MemberEditDialog.vue";
import { confirmDelete } from "@/utils/alerts";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import qs from "qs";
import { defaultMemberEntity } from "@/common/values";
import type { MemberEntity } from "@/common/entities";
import _ from "lodash";

@Component({
  name: "MemberList",
  components: {
    DataTableFilter,
    MemberEditDialog,
    ButtonSet,
  },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;
  readonly envs: typeof envs = envs;
  selected: MemberEntity[] = [];
  pagination: Pagination = {
    page: 1,
    sortBy: ["authority"],
    sortDesc: [true],
    itemsPerPage: 20,
  };

  items: MemberEntity[] = [];
  loading = false;
  saving = false;
  editItem: MemberEntity = defaultMemberEntity();
  AUTHORITY: SelectItem[] = [];
  dialog = false;
  datatableFilter: { [p: string]: string | number } = {};

  get headers(): DataTableHeader[] {
    return [
      {
        text: "사용자아이디",
        align: "start",
        value: "id",
      },
      {
        text: "사용자명",
        align: "start",
        value: "name",
      },
      {
        text: "권한",
        align: "center",
        value: "authority",
        filterType: "select",
        filterSelectItem: this.AUTHORITY,
        width: "8rem",
      },
      {
        text: "만료일",
        align: "center",
        value: "expired",
        width: "10rem",
      },
      {
        text: "사용 가능",
        align: "center",
        value: "available",
        filterType: "switch",
        width: "6rem",
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

  get queryString(): string {
    return qs.stringify({
      filter: this.datatableFilter,
      ...this.pagination,
    });
  }

  protected async created(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  @Watch("queryString")
  public async getList(): Promise<void> {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<PageResult<MemberEntity>>(
      `admin/members/?${this.queryString}`,
    );
    this.loading = false;
    this.items = response?.data?.content || [];
  }

  protected onCreated(value: MemberEntity): void {
    this.items = [value, ...this.items];
  }

  protected onUpdated(value: MemberEntity): void {
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
    this.editItem = defaultMemberEntity();
    this.dialog = true;
  }

  protected showEditDialog(value: MemberEntity): void {
    this.editItem = _.cloneDeep(value);
    this.dialog = true;
  }

  protected async remove(): Promise<void> {
    const result = await confirmDelete();
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<MemberEntity>(
        `admin/members/${this.selected[0].id}/`,
      );
      this.saving = false;
      if (response?.code?.startsWith("S")) {
        await this.$store.dispatch("initMemberCodes");
        this.items = this.items.filter(
          (item) => item.id !== (response.data?.id || 0),
        );
      }
    }
  }

  protected async excel(): Promise<void> {
    this.saving = true;
    await getExcelApi("admin/members/download/excel");
    this.saving = false;
  }
}
</script>
