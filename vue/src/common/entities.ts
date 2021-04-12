import { DateTime } from "@/common/types";

export interface MemberEntity {
  id?: number;
  createdBy?: string;
  created?: DateTime;
  updatedBy?: string;
  updated?: DateTime;
  userId: string;
  password?: string;
  name: string;
  loginFailCnt: number;
  expired: DateTime;
  available: boolean;
  theme: string;
  authorityId: number;
  token: string;
}

export interface MenuEntity {
  id?: number;
  createdBy?: string;
  created?: DateTime;
  updatedBy?: string;
  updated?: DateTime;
  name: string;
  type: string;
  parentId: number;
  displayOrder: number;
  icon: string | null;
  url: string | null;
}

export interface AuthorityItemEntity {
  id?: number | null;
  displayOrder: number;
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

export interface CodeAuthorityEntity {
  id?: number;
  createdBy?: string;
  created?: DateTime;
  updatedBy?: string;
  updated?: DateTime;
  authorityId: number;
}

export interface CodeEntity {
  id?: number;
  createdBy?: string;
  created?: DateTime;
  updatedBy?: string;
  updated?: DateTime;
  type: string;
  value: string;
  name: string;
  available: boolean;
  displayOrder: number;
  authorities: CodeAuthorityEntity[];
}
