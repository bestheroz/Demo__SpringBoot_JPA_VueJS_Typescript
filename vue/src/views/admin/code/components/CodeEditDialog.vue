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
                    v-model="item.type"
                    label="*그룹 코드"
                    :disabled="!isNew"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="item.available"
                  :label="item.available | getSwitchLabel"
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
                    v-model="item.value"
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
                    v-model="item.name"
                    label="상세 코드명"
                    :counter="100"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="권한"
                  rules="required"
                >
                  <v-select
                    v-if="AUTHORITY"
                    v-model.number="item.authority"
                    :items="
                      AUTHORITY.map((code) => {
                        return { value: parseInt(code.value), text: code.text };
                      })
                    "
                    label="*권한"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="정렬순서"
                  rules="required|numeric"
                >
                  <v-text-field
                    v-model="item.displayOrder"
                    label="*정렬순서"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
            </v-row>
          </ValidationObserver>
        </v-card-text>
        <created-updated-bar
          :created-date-time="item.created"
          :updated-date-time="item.updated"
          v-if="!isNew"
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
import { getCodesApi, postApi, putApi } from "@/utils/apis";
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
  @VModel({ required: true }) item!: CodeEntity;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  AUTHORITY: SelectItem[] = [];
  loading = false;

  get isNew(): boolean {
    return !this.item.id;
  }

  protected async created(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
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
    const response = await postApi<CodeEntity>("admin/codes/", this.item);
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("created", response.data);
    }
  }

  protected async put(): Promise<void> {
    this.loading = true;
    const response = await putApi<CodeEntity>(
      `admin/codes/${this.item.id}/`,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      window.localStorage.removeItem(`code__${this.item.id}`);
      this.$emit("updated", response.data);
    }
  }
}
</script>
