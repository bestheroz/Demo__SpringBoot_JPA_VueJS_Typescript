<template>
  <div>
    <v-dialog v-model="syncedDialog" max-width="100%" width="60vw">
      <v-card>
        <dialog-title :is-new="isNew" prefix="메뉴">
          <template #buttons>
            <button-icon-tooltip
              icon="mdi-window-close"
              text="닫기"
              @click="syncedDialog = false"
              top
            />
          </template>
        </dialog-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row dense>
              <v-col cols="12" md="3">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="메뉴명"
                  rules="required|max:50"
                >
                  <v-text-field
                    v-model="item.name"
                    label="*메뉴명"
                    :counter="50"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="3">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="타입"
                  rules="required"
                >
                  <v-select
                    v-model="item.type"
                    :items="MenuTypeItems"
                    label="*타입"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="6" v-if="item.type !== 'G'">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="링크 URL"
                  rules="max:255"
                >
                  <v-text-field
                    v-model="item.url"
                    label="링크 URL"
                    :counter="255"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col v-if="item.type === 'G'" cols="12" md="4">
                <v-text-field
                  v-model="item.icon"
                  label="메뉴 아이콘"
                  append-icon="mdi-dock-window"
                  @click:append="linkIconSite"
                />
              </v-col>
              <v-col v-if="item.type === 'G'" cols="12" md="1">
                <v-icon v-text="item.icon" size="3.5rem" />
              </v-col>
            </v-row>
          </ValidationObserver>
        </v-card-text>
        <created-updated-bar
          :created-date-time="item.created"
          :updated-date-time="item.updated"
          v-if="item.created || item.updated"
        />
        <dialog-action-button
          :loading="saving"
          @click:save="save"
          @click:close="syncedDialog = false"
        />
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Ref, VModel, Vue } from "vue-property-decorator";
import { postApi, putApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import DialogTitle from "@/components/title/DialogTitle.vue";
import DialogActionButton from "@/components/button/DialogActionButton.vue";
import type { Menu } from "@/common/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import { MenuTypeItems } from "@/common/selections";

@Component({
  name: "MenuEditDialog",
  components: {
    CreatedUpdatedBar,
    DialogActionButton,
    DialogTitle,
    ButtonIconTooltip,
  },
})
export default class extends Vue {
  @VModel({ required: true }) item!: Menu;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  readonly ENDPOINT_URL = "admin/menus/";
  saving = false;
  MenuTypeItems = MenuTypeItems;

  get isNew(): boolean {
    return !this.item.id;
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.update();
  }

  protected async create(): Promise<void> {
    this.saving = true;
    const response = await postApi<Menu>(this.ENDPOINT_URL, this.item);
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initAuthority");
      this.syncedDialog = false;
      this.$emit("created", response.data);
    }
  }

  protected async update(): Promise<void> {
    this.saving = true;
    const response = await putApi<Menu>(
      `${this.ENDPOINT_URL}${this.item.id}/`,
      this.item,
    );
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initAuthority");
      this.syncedDialog = false;
      this.$emit("updated", response.data);
    }
  }

  protected linkIconSite(): void {
    window.open("https://materialdesignicons.com/cdn/5.8.55/", "_blank");
  }
}
</script>
