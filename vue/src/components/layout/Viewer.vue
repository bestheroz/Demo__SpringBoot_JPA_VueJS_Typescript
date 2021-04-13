<template>
  <v-main :title="null">
    <v-system-bar
      v-if="title"
      :color="$vuetify.theme.dark ? '#1E1E1E' : '#FFFFFF'"
      :height="40"
      class="pt-4 pl-4"
    >
      <v-icon :size="28" style="top: -1px" v-text="icon" v-if="icon" />
      <v-card-title v-text="title" />
    </v-system-bar>
    <v-container fluid class="pa-0">
      <router-view />
    </v-container>
  </v-main>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import type { Drawer } from "@/common/types";
import { getVariableApi } from "@/utils/apis";
import { defaultMenu } from "@/common/values";
import { errorPage } from "@/utils/errors";
import _ from "lodash";

@Component({ name: "Viewer" })
export default class extends Vue {
  icon = "";

  get title(): string {
    if (this.$route.fullPath === "/index") {
      return "";
    }
    if (this.$store.getters.authority.items?.length > 0) {
      return (this.findThisPage().name || "").split("(팝업)").join("");
    }
    return "";
  }

  @Watch("title")
  protected async watchTitle(val: string): Promise<void> {
    document.title = ((await getVariableApi("title")) || "") + `:: ${val}`;
  }

  protected findThisPage(): Drawer {
    if (this.$route.name) {
      return defaultMenu();
    }
    const result: Drawer | undefined = (_.flattenDeep(
      this.$store.getters.drawers
        .map((d: Drawer) => {
          return _.isEmpty(d.children)
            ? d
            : {
                ...d,
                children: d.children?.map((c) => {
                  return { ...c, icon: d.icon };
                }),
              };
        })
        .map((d: Drawer) => (_.isEmpty(d.children) ? d : d.children)),
    ) as Drawer[]).find((d: Drawer) => {
      return d.url === this.$route.fullPath;
    });
    if (!result) {
      errorPage(403);
      return defaultMenu();
    }
    this.icon = result?.icon || "mdi-file-document-outline";
    return result || defaultMenu();
  }
}
</script>
