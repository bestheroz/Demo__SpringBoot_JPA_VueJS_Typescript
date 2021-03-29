<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <v-card flat>
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
          v-model="selectedItem"
          v-if="selectedItem"
          @click:reload="getList"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { SelectItem } from "@/common/types";
import AuthorityList from "@/views/admin/member/menu/components/AuthorityList.vue";
import { getApi } from "@/utils/apis";
import { AuthorityEntity } from "@/common/entities";

@Component({
  name: "Authority",
  components: {
    AuthorityList,
  },
})
export default class extends Vue {
  items: AuthorityEntity[] = [];
  authorityId: number | null = null;
  loading = false;
  selectedItem: AuthorityEntity | null = null;

  get AUTHORITY(): SelectItem[] {
    return this.items.map((item) => {
      return { value: item.id, text: item.name };
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

  protected created(): void {
    this.getList().then();
  }

  protected async getList(): Promise<void> {
    this.loading = true;
    const response = await getApi<AuthorityEntity[]>("admin/authorities/");
    this.items = (response.data || []).map((item) => {
      return { ...item, items: item.items || [] };
    });
    this.loading = false;
  }
}
</script>
