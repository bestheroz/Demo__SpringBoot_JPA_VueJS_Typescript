<template>
  <div>
    <v-system-bar
      v-if="isProduction ? diff > 60 : true"
      class="pl-4"
      :color="color"
    >
      <v-icon size="16" style="vertical-align: initial">
        mdi-clock-check-outline
      </v-icon>
      데이타 {{ text }} 지남
      <button-icon-tooltip
        text="새로고침"
        icon="mdi-reload"
        class="pl-1"
        x-small
        @click="$emit('reload')"
      />
    </v-system-bar>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import type { DateTime } from "@/common/types";
import dayjs from "dayjs";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";

@Component({
  name: "RefreshDataBar",
  components: { ButtonIconTooltip },
})
export default class extends Vue {
  datetime: DateTime | null = null;

  interval: number | null = null;
  now: DateTime = new Date();

  protected beforeDestroy(): void {
    this.interval && clearInterval(this.interval);
    this.interval = null;
    this.$nextTick(() => {
      this.interval && clearInterval(this.interval);
      this.interval = null;
    });
  }

  get diff(): number {
    if (!this.datetime) {
      return 0;
    }
    return dayjs(this.now).diff(this.datetime, "second");
  }

  get text(): string {
    if (this.diff > 3600) {
      return `${Math.floor(this.diff / 3600)}시간 ${Math.floor(
        (this.diff % 3600) / 60,
      )}분 ${Math.floor(this.diff % 60)}초`;
    } else {
      return `${Math.floor((this.diff % 3600) / 60)}분 ${Math.floor(
        this.diff % 60,
      )}초`;
    }
  }

  get color(): string | null {
    if (this.diff < 300) {
      return null;
    } else if (this.diff < 600) {
      return "info";
    } else if (this.diff < 1200) {
      return "warning";
    } else {
      return "error";
    }
  }

  get isProduction(): boolean {
    return process.env.NODE_ENV === "production";
  }

  protected async created(): Promise<void> {
    setTimeout(
      () => {
        this.interval = window.setInterval(() => {
          this.now = new Date();
        }, 1000);
      },
      this.isProduction ? 59_000 : 0,
    );
  }

  public triggerRefreshed(): void {
    this.datetime = new Date();
  }
}
</script>
