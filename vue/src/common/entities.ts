import { DateTime } from "@/common/types";

export interface TableMemberEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  userId: string | null;
  password?: string | null;
  name: string | null;
  loginFailCnt: number | null;
  expired: DateTime | null;
  available: boolean | null;
  theme: string | null;
  authority: number | null;
  token: string | null;
}

export interface TableMenuEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  name: string | null;
  type: string | null;
  parentId: number | null;
  authority: number | null;
  displayOrder: number | null;
  icon: string | null;
  url: string | null;
}

export interface TableCodeGroupEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  codeGroup: string | null;
  name: string | null;
}

export interface TableCodeEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  codeGroup: string | null;
  code: string | null;
  name: string | null;
  available: boolean | null;
  displayOrder: number | null;
  authority: number | null;
}
export interface TableMemberMenuEntity {
  id?: number | null;
  createdBy?: string | null;
  created?: DateTime | null;
  updatedBy?: string | null;
  updated?: DateTime | null;
  authority: number | null;
  menuId?: number | null;
  name: string | null;
  type: string | null;
  parentId: number | null;
  displayOrder: number | null;
  icon: string | null;
  url: string | null;
}
