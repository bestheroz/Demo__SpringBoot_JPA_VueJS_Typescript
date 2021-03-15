<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <code-group-list
          ref="groupNameList"
          height="25vh"
          @select-row="onSelectRow"
        />
      </v-col>
      <v-col cols="12">
        <v-divider />
      </v-col>
      <v-col cols="12">
        <code-list :group-name="groupName" />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import CodeList from "@/views/admin/code/components/CodeList.vue";
import CodeGroupList from "@/views/admin/code/components/CodeGroupList.vue";
import { defaultTableCodeGroupEntity } from "@/common/values";
import type { TableCodeGroupEntity } from "@/common/entities";

@Component({
  name: "Code",
  components: {
    CodeGroupList,
    CodeList,
  },
})
export default class extends Vue {
  @Ref() readonly groupNameList!: CodeGroupList;
  selected: TableCodeGroupEntity = defaultTableCodeGroupEntity();

  get groupName(): string {
    return this.selected?.name || "";
  }

  protected mounted(): void {
    this.groupNameList.getList();
  }

  onSelectRow(val: TableCodeGroupEntity): void {
    this.selected = val;
  }
}
</script>
