var J = Object.defineProperty, E = Object.defineProperties;
var $ = Object.getOwnPropertyDescriptors;
var f = Object.getOwnPropertySymbols;
var v = Object.prototype.hasOwnProperty, y = Object.prototype.propertyIsEnumerable;
var d = (n, t, e) => t in n ? J(n, t, { enumerable: !0, configurable: !0, writable: !0, value: e }) : n[t] = e, g = (n, t) => {
  for (var e in t || (t = {}))
    v.call(t, e) && d(n, e, t[e]);
  if (f)
    for (var e of f(t))
      y.call(t, e) && d(n, e, t[e]);
  return n;
}, m = (n, t) => E(n, $(t));
var u = (n, t, e) => new Promise((a, r) => {
  var l = (o) => {
    try {
      i(e.next(o));
    } catch (h) {
      r(h);
    }
  }, c = (o) => {
    try {
      i(e.throw(o));
    } catch (h) {
      r(h);
    }
  }, i = (o) => o.done ? a(o.value) : Promise.resolve(o.value).then(l, c);
  i((e = e.apply(n, t)).next());
});
import { defHttp as s } from "/@/utils/http/axios";
import { isArray as p } from "/@/utils/is";
function b(n, t, e) {
  return u(this, null, function* () {
    let { success: a, result: r } = yield s.get(
      {
        url: "/online/cgform/head/enhanceJs/" + n,
        params: m(g({}, e), {
          type: t
        })
      },
      { isTransformResponse: !1 }
    );
    return a || (r = { cgJs: "" }), r;
  });
}
const q = (n, t, e) => {
  let a = `/online/cgform/head/enhanceJs/${n}`;
  return e ? s.put({ url: a, params: t }, { successMessageMode: "none" }) : s.post({ url: a, params: t }, { successMessageMode: "none" });
};
function w(n, t) {
  return u(this, null, function* () {
    let e = yield s.get({ url: "/online/cgform/head/enhanceButton/" + n }, { isTransformResponse: !1 }), a = [];
    e.success && p(e.result) && (a = e.result.filter((c) => c.optType == "action"));
    let r = `/online/cgform/head/enhanceJava/${n}`, l = yield s.get({ url: r, params: t });
    return { btnList: a, dataSource: l };
  });
}
function R(n) {
  return s.delete(
    {
      url: "/online/cgform/head/deleteBatchEnhanceJava",
      params: {
        ids: n.join(",")
      }
    },
    { joinParamsToUrl: !0 }
  );
}
const j = (n, t, e) => {
  let a = `/online/cgform/head/enhanceJava/${n}`;
  return e ? s.put({ url: a, params: t }) : s.post({ url: a, params: t });
};
function M(n, t) {
  return u(this, null, function* () {
    let e = yield s.get({ url: "/online/cgform/head/enhanceButton/" + n }, { isTransformResponse: !1 }), a = [];
    e.success && p(e.result) && (a = e.result.filter((c) => c.optType == "action"));
    let r = `/online/cgform/head/enhanceSql/${n}`, l = yield s.get({ url: r, params: t });
    return { btnList: a, dataSource: l };
  });
}
function C(n) {
  return s.delete(
    {
      url: "/online/cgform/head/deletebatchEnhanceSql",
      params: {
        ids: n.join(",")
      }
    },
    { joinParamsToUrl: !0 }
  );
}
const D = (n, t, e) => {
  let a = `/online/cgform/head/enhanceSql/${n}`;
  return e ? s.put({ url: a, params: t }) : s.post({ url: a, params: t });
};
export {
  w as a,
  j as b,
  M as c,
  R as d,
  D as e,
  C as f,
  b as g,
  q as s
};
