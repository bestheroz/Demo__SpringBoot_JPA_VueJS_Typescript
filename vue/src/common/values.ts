import {
  Code,
  Member,
  Authority,
  Menu,
  AuthorityItem,
  CodeAuthority,
} from "@/common/models";
import dayjs from "dayjs";
import { AUTHORITY_TYPE } from "@/common/selections";

export function defaultUser(): {
  id: number;
  userId: string;
  name: string;
  authorityId: number;
  theme: string;
} {
  return {
    id: 0,
    userId: "",
    name: "",
    authorityId: 0,
    theme: "light",
  };
}

export function defaultMember(): Member {
  return {
    userId: "",
    name: "",
    loginFailCnt: 0,
    expired: dayjs().add(1, "years").endOf("day"),
    available: false,
    theme: "",
    authorityId: 0,
    token: "",
  };
}

export function defaultMenu(): Menu {
  return {
    name: "",
    type: "G",
    parentId: 99999,
    displayOrder: 99999,
    icon: null,
    url: null,
  };
}
export function defaultCode(): Code {
  return {
    type: "",
    value: "",
    name: "",
    available: false,
    displayOrder: 0,
    authorities: [],
  };
}
export function defaultAuthorityItem(): AuthorityItem {
  return {
    menu: defaultMenu(),
    displayOrder: 0,
    typesJson: [AUTHORITY_TYPE.VIEW],
  };
}
export function defaultAuthority(): Authority {
  return {
    code: "",
    name: "",
    items: [],
  };
}
export function defaultCodeAuthority(): CodeAuthority {
  return {
    authorityId: 0,
  };
}
