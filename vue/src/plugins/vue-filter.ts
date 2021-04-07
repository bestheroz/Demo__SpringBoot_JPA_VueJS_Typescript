import Vue from "vue";
import { DateTime, SelectItem } from "@/common/types";
import { getTextOfSelectItem } from "@/utils/codes";
import {
  formatDate,
  formatDatetime,
  getEllipseText,
  getMemberNm,
  getSwitchLabel,
} from "@/utils/formatter";

Vue.filter(
  "formatDatetime",
  function (value: DateTime | undefined | null): string {
    return formatDatetime(value);
  },
);
Vue.filter("formatDate", function (value: DateTime | undefined | null): string {
  return formatDate(value);
});
Vue.filter(
  "formatMemberNm",
  function (value: string | undefined | null): string {
    return getMemberNm(value);
  },
);
Vue.filter(
  "getCodeText",
  function (value: string, codes: SelectItem[] | null): string {
    return getTextOfSelectItem(codes, value);
  },
);
Vue.filter("getEllipseText", function (text: string, length: number): string {
  return getEllipseText(text, length);
});
Vue.filter("getSwitchLabel", function (yn: boolean, prefix?: string): string {
  return getSwitchLabel(yn, prefix);
});
