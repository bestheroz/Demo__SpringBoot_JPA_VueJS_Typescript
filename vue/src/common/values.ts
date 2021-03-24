import {
  CodeEntity,
  MemberEntity,
  AuthorityEntity,
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
    loginFailCnt: 0,
    expired: dayjs().add(1, "years").endOf("day"),
    available: false,
    theme: null,
    authority: null,
    token: null,
  };
}

export function defaultMenuEntity(): MenuEntity {
  return {
    name: null,
    type: "G",
    parentId: 99999,
    displayOrder: 99999,
    icon: null,
    url: null,
  };
}
export function defaultCodeEntity(): CodeEntity {
  return {
    type: null,
    value: null,
    name: null,
    available: false,
    displayOrder: null,
    authority: null,
  };
}
export function defaultAuthorityEntity(): AuthorityEntity {
  return {
    name: null,
    data: [],
  };
}
