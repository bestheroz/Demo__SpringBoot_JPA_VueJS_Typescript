import dayjs from "dayjs";
import type { MenuEntity } from "@/common/entities";

export type DateTime = dayjs.ConfigType;

export interface SelectItem {
  value: string;
  text: string;
}
/* eslint-disable @typescript-eslint/no-explicit-any */
export interface DataTableHeader {
  text: string;
  value: string;
  align?: "start" | "center" | "end";
  sortable?: boolean | null;
  filterable?: boolean | null;
  divider?: boolean | null;
  class?: string | string[];
  width?: string | number;
  filter?: (value: any, search: string, item: any) => boolean;
  sort?: (a: any, b: any) => number;
  // 아래는 filter 를 위한 property 추가
  filterType?: "input" | "select" | "switch";
  filterSelectItem?: SelectItem[] | null;
  filterDefaultValue?: string | null;
}
/* eslint-enable @typescript-eslint/no-explicit-any */

export interface Pagination {
  page: number;
  sortBy: string[];
  sortDesc: boolean[];
  itemsPerPage: number; // -1 for All
}

export interface PageResult<T> {
  content: T[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: {
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    sort: { empty: boolean; sorted: boolean; unsorted: boolean };
    unpaged: boolean;
  };
  sort: { empty: boolean; sorted: boolean; unsorted: boolean };
  totalElements: number;
  totalPages: number;
}

export interface DrawerItem extends MenuEntity {
  children?: DrawerItem[];
}
