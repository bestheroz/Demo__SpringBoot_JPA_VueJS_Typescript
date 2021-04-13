<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <v-card flat :loading="loading">
          <v-card-text>
            <v-chip-group
              v-model.number="authorityId"
              column
              active-class="accent"
              mandatory
              dense
            >
              <v-chip
                v-for="item in AUTHORITY"
                :value="item.value"
                filter
                outlined
                :key="item.value"
              >
                {{ item.text }}
              </v-chip>
            </v-chip-group>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12">
        <authority-list
          ref="refAuthorityList"
          v-model="selectedItem"
          v-if="selectedItem"
          @click:reload="getList"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue, Watch } from "vue-property-decorator";
import { SelectItem } from "@/common/types";
import AuthorityList from "@/views/admin/member/menu/components/AuthorityList.vue";
import { getApi } from "@/utils/apis";
import { Authority } from "@/common/models";

@Component({
  name: "Authority",
  components: {
    AuthorityList,
  },
})
export default class extends Vue {
  items: Authority[] = [];
  authorityId: number | null = null;
  loading = false;
  selectedItem: Authority | null = null;

  @Ref() readonly refAuthorityList!: AuthorityList;

  get AUTHORITY(): SelectItem<number>[] {
    return this.items
      .filter((i) => i.code !== "SUPER")
      .map((i) => {
        return { value: i.id || 0, text: i.name };
      });
  }

  @Watch("authorityId")
  protected watchAuthority(val: number | null): void {
    this.selectedItem = this.items.find((item) => item.id === val) || null;
    if (this.selectedItem) {
      const findIndex = this.items.findIndex(
        (item) => item.id === this.selectedItem?.id,
      );
      this.items = [
        ...this.items.slice(0, findIndex),
        this.selectedItem,
        ...this.items.slice(findIndex + 1),
      ];
    }
  }

  protected mounted(): void {
    this.getList().then(() => {
      this.refAuthorityList.triggerRefreshed();
    });
  }

  protected async getList(): Promise<void> {
    this.loading = true;
    const response = await getApi<Authority[]>("admin/authorities/");
    this.items = (response.data || []).map((item) => {
      return { ...item, items: item.items || [] };
    });
    this.loading = false;
  }
}
</script>
