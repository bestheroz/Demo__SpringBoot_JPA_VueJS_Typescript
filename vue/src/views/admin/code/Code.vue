<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <code-group @selected="onCodeGroupSelected" />
      </v-col>
      <v-col cols="12">
        <code-list ref="codeList" :type="type" />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import CodeList from "@/views/admin/code/components/CodeList.vue";
import CodeGroup from "@/views/admin/code/components/CodeGroup.vue";

@Component({
  name: "CodeWrapper",
  components: {
    CodeGroup,
    CodeList,
  },
})
export default class extends Vue {
  @Ref() readonly codeList!: CodeList;

  type: string | null = null;

  protected mounted(): void {
    this.codeList.getList();
  }

  protected onCodeGroupSelected(val: string): void {
    this.type = val;
  }
}
</script>
