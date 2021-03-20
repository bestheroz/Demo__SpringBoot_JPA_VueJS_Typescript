<template>
  <div>
    <ValidationObserver ref="observer">
      <ValidationProvider :name="label" :rules="rules" v-slot="{ errors }">
        <v-combobox
          v-model="values"
          :label="label"
          multiple
          hide-no-data
          chips
          clearable
          @change="onChange"
          :error-messages="errors"
        >
          <template v-slot:selection="data">
            <v-chip
              :key="JSON.stringify(data.item)"
              v-bind="data.attrs"
              :input-value="data.selected"
              close
              outlined
              @click:close="data.parent.selectItem(data.item)"
            >
              <v-avatar
                class="accent"
                left
                v-text="data.item.slice(0, 1).toUpperCase()"
              />
              {{ data.item }}
            </v-chip>
          </template>
        </v-combobox>
      </ValidationProvider>
    </ValidationObserver>
  </div>
</template>

<script lang="ts">
import {
  Component,
  Prop,
  Ref,
  VModel,
  Vue,
  Watch,
} from "vue-property-decorator";
import { ValidationObserver } from "vee-validate";

@Component({
  name: "MultipleValueToString",
  components: {},
})
export default class extends Vue {
  @VModel({ required: true }) output!: string;
  @Prop() readonly label!: string;
  @Prop({ default: "," }) readonly separator!: string;
  @Prop({ type: Boolean }) readonly required!: boolean;
  @Prop({ type: Boolean }) readonly ruleEmail!: boolean;

  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  values: string[] = [];

  get rules(): string {
    return [
      `${this.required ? "required" : ""}`,
      `${this.ruleEmail ? "email" : ""}`,
    ]
      .map((item) => item)
      .join("|");
  }

  @Watch("output", { immediate: true })
  watchOutput(val: string): void {
    if (val) {
      this.values = val.split(this.separator);
    }
  }

  protected onChange() {
    this.output =
      this.values.length > 0 ? this.values.join(this.separator) : undefined;
  }

  public async validateForm(): Promise<boolean> {
    return await this.observer.validate();
  }
  public resetForm(): void {
    this.observer.reset();
  }
}
</script>
