<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <code-type ref="refCodeType" @selected="onCodeTypeSelected" />
      </v-col>
      <v-col cols="12">
        <code-list ref="refCodeList" :type="type" @created="onCreated" />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import CodeList from "@/views/admin/code/CodeList.vue";
import CodeType from "@/views/admin/code/CodeType.vue";
import { Code } from "@/common/models";

@Component({
  name: "CodeWrapper",
  components: {
    CodeType,
    CodeList,
  },
})
export default class extends Vue {
  @Ref() readonly refCodeList!: CodeList;
  @Ref() readonly refCodeType!: CodeType;

  type = "";

  protected mounted(): void {
    this.refCodeList.getList();
  }

  protected onCreated(value: Code): void {
    this.refCodeType.onCreate(value);
  }

  protected onCodeTypeSelected(val: string): void {
    this.type = val;
  }
}
</script>
