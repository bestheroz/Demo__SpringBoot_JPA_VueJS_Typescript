import { SelectItem } from "@/common/types";

export const AUTHORITY_TYPE = {
  VIEW: "VIEW",
  WRITE: "WRITE",
  DELETE: "DELETE",
};
export const AuthorityTypeItems: SelectItem[] = [
  { value: "VIEW", text: "보기" },
  { value: "WRITE", text: "쓰기" },
  { value: "DELETE", text: "삭제" },
];
export const MenuTypeItems: SelectItem[] = [
  { value: "G", text: "페이지 그룹" },
  { value: "P", text: "페이지" },
  { value: "W", text: "팝업" },
];
export const MENU_TYPE = {
  G: "G",
  P: "P",
  W: "W",
};
