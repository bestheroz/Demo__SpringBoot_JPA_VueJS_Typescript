<template>
  <div>
    <div v-if="row">
      <v-system-bar lights-out v-if="createdDateTimeString">
        <v-spacer />
        <span v-if="createdDateTimeString">
          Created
          <v-icon size="16" style="vertical-align: initial">
            mdi-clock-outline
          </v-icon>
          {{ createdDateTimeString }}
        </span>
        <v-spacer />
      </v-system-bar>
      <v-system-bar dark lights-out v-if="updatedDateTimeString">
        <v-spacer />
        <span v-if="updatedDateTimeString">
          Updated
          <v-icon size="16" style="vertical-align: initial">
            mdi-clock-check-outline
          </v-icon>
          {{ updatedDateTimeString }}
        </span>
        <v-spacer />
      </v-system-bar>
    </div>
    <div v-else>
      <v-system-bar
        lights-out
        v-if="createdDateTimeString || updatedDateTimeString"
      >
        <v-spacer />
        <span v-if="createdDateTimeString">
          Created
          <v-icon size="16" style="vertical-align: initial">
            mdi-clock-outline
          </v-icon>
          {{ createdDateTimeString }}
        </span>
        <v-spacer />
        <span v-if="updatedDateTimeString">
          Updated
          <v-icon size="16" style="vertical-align: initial">
            mdi-clock-check-outline
          </v-icon>
          {{ updatedDateTimeString }}
        </span>
        <v-spacer />
      </v-system-bar>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import { DateTime } from "@/common/types";
import { formatDatetime } from "@/utils/formatter";

@Component({
  name: "CreatedUpdatedBar",
  components: {},
})
export default class extends Vue {
  @Prop() readonly createdDateTime!: DateTime;
  @Prop() readonly updatedDateTime!: DateTime;
  @Prop({ type: Boolean }) readonly row!: boolean;

  get createdDateTimeString(): string {
    if (!this.createdDateTime) {
      return "";
    }
    return formatDatetime(this.createdDateTime);
  }
  get updatedDateTimeString(): string {
    if (!this.updatedDateTime) {
      return "";
    }
    return formatDatetime(this.updatedDateTime);
  }
}
</script>
