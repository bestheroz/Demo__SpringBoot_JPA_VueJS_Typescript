import {
  CodeEntity,
  MemberEntity,
  AuthorityEntity,
  MenuEntity,
  AuthorityItemEntity,
  CodeAuthorityEntity,
} from "@/common/entities";
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

export function defaultMemberEntity(): MemberEntity {
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

export function defaultMenuEntity(): MenuEntity {
  return {
    name: "",
    type: "G",
    parentId: 99999,
    displayOrder: 99999,
    icon: null,
    url: null,
  };
}
export function defaultCodeEntity(): CodeEntity {
  return {
    type: "",
    value: "",
    name: "",
    available: false,
    displayOrder: 0,
    authorities: [],
  };
}
export function defaultAuthorityItemEntity(): AuthorityItemEntity {
  return {
    menu: defaultMenuEntity(),
    displayOrder: 0,
    typesJson: [AUTHORITY_TYPE.VIEW],
  };
}
export function defaultAuthorityEntity(): AuthorityEntity {
  return {
    code: "",
    name: "",
    items: [],
  };
}
export function defaultCodeAuthorityEntity(): CodeAuthorityEntity {
  return {
    authorityId: 0,
  };
}
