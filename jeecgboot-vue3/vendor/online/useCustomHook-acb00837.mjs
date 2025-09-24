import * as a from "vue";
import * as p from "/@/utils/cache";
import * as _ from "/@/utils/http/axios";
import * as x from "/@/hooks/web/useMessage";
import { randomString as E } from "/@/utils/common/compUtils";
import * as S from "/@/store/modules/user";
const O = {
  vue: a,
  "@": {
    hooks: {
      // 调用示例：@/hooks/useMessage
      useMessage: x,
      useUserStore: S
    },
    utils: {
      // 调用示例：@/utils/axios
      axios: _,
      cache: p
    }
  }
};
function I(i, s) {
  const m = Object.assign({}, O, i);
  function u(t) {
    if (t != null && t != "") {
      let o = t.toString().split("/"), e = m[o[0]];
      for (let n = 1; n < o.length; n++)
        e = e[o[n]];
      return e;
    }
    return null;
  }
  function c() {
  }
  function f(t, o) {
    let n = "__export_" + E(6);
    if (o) {
      const r = `return function (row, customImport, ${n}) {"use strict"; ${t}}`;
      new Function(r)().call(s, o, u, c);
    } else {
      const r = `return function (customImport, ${n}) {"use strict"; ${t}}`;
      new Function(r)().call(s, u, c);
    }
  }
  return {
    executeJsEnhanced: f
  };
}
const g = /(?:\/\*[\s\S]*?\*\/|\/\/.*?\r?\n|[^{])+\{([\s\S]*)\}$/;
export {
  g as G,
  I as u
};
