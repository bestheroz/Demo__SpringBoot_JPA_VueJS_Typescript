import { DateTime } from "@/common/types";

export interface Member {
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

export interface Menu {
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

export interface AuthorityItem {
  id?: number | null;
  displayOrder: number;
  menu: Menu;
  typesJson: string[];
}
export interface Authority {
  id?: number;
  createdBy?: string;
  created?: DateTime;
  updatedBy?: string;
  updated?: DateTime;
  code: string;
  name: string;
  items: AuthorityItem[];
}

export interface CodeAuthority {
  id?: number;
  createdBy?: string;
  created?: DateTime;
  updatedBy?: string;
  updated?: DateTime;
  authorityId: number;
}

export interface Code {
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
  authorities: CodeAuthority[];
}
