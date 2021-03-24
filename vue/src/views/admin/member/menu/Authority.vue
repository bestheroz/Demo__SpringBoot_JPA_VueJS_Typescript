<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <v-card flat>
          <v-card-text>
            <v-chip-group
              v-model="authority"
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
import { Component, Vue } from "vue-property-decorator";
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
  authority: string | null = null;
  AUTHORITY: SelectItem[] = [];

  get selectedItem(): AuthorityEntity | null {
    return this.items.find((item) => item.code === this.authority) || null;
  }

  set selectedItem(value: AuthorityEntity): void {
    const findIndex = this.items.findIndex((item) => item.id === value.id);
    this.items = [
      ...this.items.slice(0, findIndex),
      value,
      ...this.items.slice(findIndex + 1),
    ];
  }

  protected async created(): Promise<void> {
    this.getList().then();
  }

  protected async getList(): Promise<void> {
    this.loading = true;
    const response = await getApi<AuthorityEntity[]>("admin/authorities/");
    this.items = response.data || [];
    this.loading = false;
    this.AUTHORITY = (response?.data || []).map((item) => {
      return { value: item.code, text: item.name };
    });
  }
}
</script>
