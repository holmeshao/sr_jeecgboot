var i = (t, o, e) => new Promise((f, a) => {
  var m = (r) => {
    try {
      n(e.next(r));
    } catch (c) {
      a(c);
    }
  }, l = (r) => {
    try {
      n(e.throw(r));
    } catch (c) {
      a(c);
    }
  }, n = (r) => r.done ? f(r.value) : Promise.resolve(r.value).then(m, l);
  n((e = e.apply(t, o)).next());
});
import { defineStore as d } from "pinia";
import { useUserStoreWithOut as u } from "/@/store/modules/user";
import { removeCacheByDynKey as h } from "/@/utils/auth";
import { TOKEN_KEY as g } from "/@/enums/cacheEnum";
import { getUserInfo as R } from "/@/api/sys/user";
const s = u(), S = d({
  id: "online-cgform-share",
  state: () => ({
    cgformRecord: null,
    dataRecord: null
  }),
  getters: {
    getCgformRecord() {
      return this.cgformRecord;
    },
    getDataRecord() {
      return this.dataRecord;
    }
  },
  actions: {
    // 检查 url 参数是否携带token
    checkUrlToken() {
      return i(this, null, function* () {
        const t = new URLSearchParams(window.location.search).get("token"), o = t != null && t.length > 0;
        if (o) {
          s.setToken(t);
          try {
            const e = yield R();
            if (e != null && e.userInfo)
              s.setUserInfo(e.userInfo), e.sysAllDictItems && s.setAllDictItems(e.sysAllDictItems);
            else
              throw new Error("token无效");
            return !0;
          } catch (e) {
            return s.setToken(""), h(g), !1;
          }
        }
        return o;
      });
    },
    setCgformRecord(t) {
      this.cgformRecord = t;
    },
    setDataRecord(t) {
      this.dataRecord = t;
    }
  }
});
export {
  S as u
};
