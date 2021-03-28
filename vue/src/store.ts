import Vue from "vue";
import Vuex, { ActionContext } from "vuex";
import createPersistedState from "vuex-persistedstate";
import { SelectItem } from "@/common/types";
import { axiosInstance, getApi, postApi } from "@/utils/apis";
// eslint-disable-next-line camelcase
import jwt_decode from "jwt-decode";
import axios from "axios";
import envs from "@/constants/envs";
import router from "@/router";
import { defaultUser } from "@/common/values";
import type { AuthorityEntity, MemberEntity } from "@/common/entities";
import { errorPage } from "@/utils/errors";

Vue.use(Vuex);

/* eslint-disable @typescript-eslint/no-explicit-any */
const user = {
  state: {
    user: defaultUser(),
    accessToken: null,
    refreshToken: null,
  },
  getters: {
    user: (state: any) => {
      return {
        id: state.user.id,
        userId: state.user.userId,
        name: state.user.name,
        authorityId: state.user.authorityId,
        theme: state.user.theme || "light",
      };
    },
    loggedIn: (state: any): boolean => {
      return !!state.user.id && !!state.accessToken && !!state.refreshToken;
    },
    theme: (state: any): string => {
      return state.user.theme;
    },
    accessToken: (state: any): string | null => {
      return state.accessToken;
    },
    refreshToken: (state: any): string | null => {
      return state.refreshToken;
    },
  },
  mutations: {
    setUser(state: any, user: MemberEntity): void {
      state.user = user;
    },
    setTheme(state: any, theme: string): void {
      state.user.theme = theme;
    },
    setAccessToken(state: any, accessToken: string): void {
      const jwt = jwt_decode<{
        exp: number;
        userId: string;
        userVO: string;
      }>(accessToken);
      console.log(JSON.parse(jwt.userVO));
      state.user = JSON.parse(jwt.userVO);
      state.accessToken = accessToken;
    },
    setRefreshToken(state: any, refreshToken: string): void {
      state.refreshToken = refreshToken;
    },
  },
  actions: {
    toggleTheme: async function ({
      commit,
      getters,
    }: ActionContext<any, any>): Promise<void> {
      let theme;
      if (getters.theme === "dark") {
        theme = "light";
      } else {
        theme = "dark";
      }
      await postApi<{
        theme: string;
      }>(
        "members/mine/changeTheme/",
        {
          theme: theme,
        },
        false,
      );
      commit("setTheme", theme);
    },
    clearUser({ commit }: ActionContext<any, any>): void {
      commit("setUser", defaultUser());
    },
    saveToken(
      { commit }: ActionContext<any, any>,
      payload: { accessToken: string; refreshToken: string },
    ): void {
      commit("setAccessToken", payload.accessToken);
      commit("setRefreshToken", payload.refreshToken);
    },
    async reissueAccessToken({
      commit,
      getters,
      dispatch,
    }: ActionContext<any, any>): Promise<void> {
      try {
        const response = await axios
          .create({
            baseURL: envs.API_HOST,
            headers: {
              contentType: "application/json",
              Authorization: getters.accessToken,
              AuthorizationR: getters.refreshToken,
            },
          })
          .get("api/auth/refreshToken");
        await commit("setAccessToken", response?.data?.data);
      } catch (e) {
        if (
          e.response?.status === 401 ||
          e.message === "Invalid token specified"
        ) {
          await dispatch("needLogin");
        } else if ([403, 404, 500].includes(e.response?.status)) {
          await errorPage(e.response?.status);
        } else {
          await errorPage(500);
        }
      }
    },
  },
};

const authority = {
  state: {
    authority: null,
    selected: null,
  },
  getters: {
    authority: (state: any): AuthorityEntity => {
      return state.authority || [];
    },
    selected: (state: any): number | null => {
      return state.selected || null;
    },
  },
  mutations: {
    setAuthority(state: any, authority: AuthorityEntity): void {
      state.authority = authority;
    },
    setSelected(state: any, selected: number): void {
      state.selected = selected;
    },
  },
  actions: {
    async initAuthority({
      commit,
      getters,
    }: ActionContext<any, any>): Promise<void> {
      const response = await getApi<AuthorityEntity>(
        `auth/${getters.user.authorityId}`,
      );
      commit("setAuthority", response?.data);
    },
    clearAuthority({ commit }: ActionContext<any, any>): void {
      commit("setAuthority", null);
    },
    setMenuSelected(
      { commit }: ActionContext<any, any>,
      selected: number,
    ): void {
      commit("setSelected", selected);
    },
    clearMenuSelected({ commit }: ActionContext<any, any>): void {
      commit("setSelected", null);
    },
  },
};

const codes = {
  state: {
    memberCodes: null,
  },
  getters: {
    memberCodes: (state: any): SelectItem[] => {
      return state.memberCodes || [];
    },
  },
  mutations: {
    setMemberCodes(state: any, memberCodes: SelectItem[]): void {
      state.memberCodes = memberCodes;
    },
  },
  actions: {
    async initMemberCodes({ commit }: ActionContext<any, any>): Promise<void> {
      const response = await getApi<SelectItem[]>("members/codes");
      commit("setMemberCodes", response?.data);
    },
    clearCache({ commit }: ActionContext<any, any>): void {
      commit("setMemberCodes", null);
    },
  },
};
const command = {
  state: {},
  mutations: {},
  actions: {
    async needLogin(): Promise<void> {
      if (router.currentRoute.path !== "/login") {
        await router.push("/login?login=need");
      }
    },
    async logout(): Promise<void> {
      try {
        axiosInstance.delete(`${envs.API_HOST}api/auth/logout`).then();
      } catch (e) {
        console.error(e);
      }
      await router.push("/login").then();
    },
  },
};
/* eslint-enable @typescript-eslint/no-explicit-any */
export default new Vuex.Store({
  strict: true,
  modules: {
    user,
    authority,
    codes,
    command,
  },
  plugins: [createPersistedState()],
});
