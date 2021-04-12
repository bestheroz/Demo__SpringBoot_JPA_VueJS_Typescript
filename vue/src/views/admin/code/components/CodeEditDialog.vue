<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="100%" width="60vw">
      <v-card>
        <dialog-title :is-new="isNew" prefix="코드">
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
            <v-row>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="그룹 코드"
                  rules="required"
                >
                  <v-text-field
                    v-model="vModel.type"
                    label="*그룹 코드"
                    :disabled="!isNew"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="vModel.available"
                  :label="vModel.available | getSwitchLabel"
                />
              </v-col>
              <v-col cols="0" md="4" />
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="상세 코드"
                  rules="max:50|required"
                >
                  <v-text-field
                    v-model="vModel.value"
                    label="*상세 코드"
                    :counter="50"
                    :error-messages="errors"
                    :disabled="!isNew"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="상세 코드명"
                  rules="max:100"
                >
                  <v-text-field
                    v-model="vModel.name"
                    label="상세 코드명"
                    :counter="100"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <v-autocomplete
                  v-model="authorities"
                  :items="AUTHORITY"
                  label="*권한"
                  multiple
                  @change="onChangeAuthorities"
                />
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="정렬순서"
                  rules="required|numeric"
                >
                  <v-text-field
                    v-model="vModel.displayOrder"
                    label="*정렬순서"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
            </v-row>
          </ValidationObserver>
        </v-card-text>
        <created-updated-bar
          :created-date-time="vModel.created"
          :updated-date-time="vModel.updated"
          v-if="vModel.created || vModel.updated"
        />
        <dialog-action-button
          :loading="loading"
          @click:save="save"
          @click:close="syncedDialog = false"
        />
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Ref, VModel, Vue } from "vue-property-decorator";
import type { SelectItem } from "@/common/types";
import { getApi, postApi, putApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogTitle from "@/components/title/DialogTitle.vue";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import DialogActionButton from "@/components/button/DialogActionButton.vue";
import type { CodeEntity } from "@/common/entities";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";

@Component({
  name: "CodeEditDialog",
  components: {
    CreatedUpdatedBar,
    DialogActionButton,
    ButtonIconTooltip,
    DialogTitle,
  },
})
export default class extends Vue {
  @VModel({ required: true }) vModel!: CodeEntity;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  AUTHORITY: SelectItem<number>[] = [];
  authorities: number[] = [];
  loading = false;

  get isNew(): boolean {
    return !this.vModel.id;
  }

  protected async created(): Promise<void> {
    const response = await getApi<SelectItem<number>[]>("auth/codes");
    this.AUTHORITY = (response.data || []).filter((a) => a.value !== 1);
    this.authorities = this.vModel.authorities.map((a) => a.authorityId);
  }

  protected onChangeAuthorities(): void {
    this.vModel.authorities = this.authorities.map((a) => {
      return (
        this.vModel.authorities.find((v) => v.authorityId === a) || {
          authorityId: a,
        }
      );
    });
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.put();
  }

  protected async create(): Promise<void> {
    this.loading = true;
    const response = await postApi<CodeEntity>("admin/codes/", this.vModel);
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("created", response.data);
    }
  }

  protected async put(): Promise<void> {
    this.loading = true;
    const response = await putApi<CodeEntity>(
      `admin/codes/${this.vModel.id}/`,
      this.vModel,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      window.localStorage.removeItem(`code__${this.vModel.id}`);
      this.$emit("updated", response.data);
    }
  }
}
</script>
