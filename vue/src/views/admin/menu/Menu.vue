<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <menu-list />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import MenuList from "@/views/admin/menu/MenuList.vue";

@Component({
  name: "Menu",
  components: {
    MenuList,
  },
})
export default class extends Vue {}
</script>
