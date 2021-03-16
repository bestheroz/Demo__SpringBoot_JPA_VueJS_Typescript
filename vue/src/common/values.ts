import {
  CodeEntity,
  MemberEntity,
  MemberMenuEntity,
  MenuEntity,
} from "@/common/entities";
import dayjs from "dayjs";
export function defaultUser(): {
  id: number | null;
  userId: string | null;
  name: string | null;
  authority: string | null;
  theme: string;
} {
  return {
    id: null,
    userId: null,
    name: null,
    authority: null,
    theme: "light",
  };
}

export function defaultMemberEntity(): MemberEntity {
  return {
    userId: null,
    name: null,
    loginFailCnt: null,
    expired: dayjs().add(1, "years").endOf("day"),
    available: null,
    theme: null,
    authority: null,
    token: null,
  };
}

export function defaultMenuEntity(): MenuEntity {
  return {
    name: null,
    type: "G",
    parentId: null,
    authority: null,
    displayOrder: null,
    icon: null,
    url: null,
  };
}
export function defaultCodeEntity(): CodeEntity {
  return {
    type: null,
    value: null,
    name: null,
    available: null,
    displayOrder: null,
    authority: null,
  };
}
export function defaultMemberMenuEntity(): MemberMenuEntity {
  return {
    authority: null,
    menuId: null,
    name: null,
    type: null,
    parentId: null,
    displayOrder: null,
    icon: null,
    url: null,
  };
}
