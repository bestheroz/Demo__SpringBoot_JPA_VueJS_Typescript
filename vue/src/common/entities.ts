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
  menu: MenuEntity;
  typesJson: string[];
}
export interface AuthorityEntity {
  id?: number;
  createdBy?: string;
  created?: DateTime;
  updatedBy?: string;
  updated?: DateTime;
  code: string;
  name: string;
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
