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
  loginFailCnt: number | null;
  expired: DateTime | null;
  available: boolean | null;
  theme: string | null;
  authority: number | null;
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
  authority: number | null;
  displayOrder: number | null;
  icon: string | null;
  url: string | null;
}

export interface MemberMenuEntity {
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

export interface CodeEntity {
  id?: number | null;
  created?: DateTime | null;
  createdBy?: string | null;
  updated?: DateTime | null;
  updatedBy?: string | null;
  type: string | null;
  value: string | null;
  name: string | null;
  available: boolean | null;
  displayOrder: number | null;
  authority: number | null;
}
