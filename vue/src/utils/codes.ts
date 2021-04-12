import { SelectItem } from "@/common/types";

export function getTextOfSelectItem<T = string>(
  codes: SelectItem<T>[] | null,
  value: T,
  defaultText?: string,
): string {
  return (
    (codes || []).find((item) => item.value === value)?.text ||
    defaultText ||
    value + "" ||
    ""
  );
}
