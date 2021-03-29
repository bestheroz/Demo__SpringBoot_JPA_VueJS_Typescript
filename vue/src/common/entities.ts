import { DateTime } from "@/common/types";

export interface MemberEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  userId: string | null;
  password?: string | null;
  name: string | null;
  loginFailCnt: number;
  expired: DateTime | null;
  available: boolean;
  theme: string | null;
  authorityId: number | null;
  token: string | null;
}

export interface MenuEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  name: string | null;
  type: string | null;
  parentId: number | null;
  displayOrder: number | null;
  icon: string | null;
  url: string | null;
}

export interface AuthorityItemEntity {
  id?: number | null;
  displayOrder: number | null;
  menu: MenuEntity | null;
  typesJson: string[];
}
export interface AuthorityEntity {
  id?: number | null;
  createdBy?: string | null;
  created?: DateTime | null;
  updatedBy?: string | null;
  updated?: DateTime | null;
  code: string | null;
  name: string | null;
  items: AuthorityItemEntity[];
}

export interface CodeEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  type: string | null;
  value: string | null;
  name: string | null;
  available: boolean;
  displayOrder: number | null;
  authorityId: number | null;
}
