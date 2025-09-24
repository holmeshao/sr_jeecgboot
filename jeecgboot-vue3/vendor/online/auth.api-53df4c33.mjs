import { defHttp as o } from "/@/utils/http/axios";
const l = (t, a) => o.get({ url: `/online/cgform/api/authColumn/${t}`, params: a }), r = (t) => o.put({ url: "/online/cgform/api/authColumn", params: t }), s = (t) => o.post({ url: "/online/cgform/api/authColumn", params: t }), i = (t) => o.put({ url: "/online/cgform/api/authColumn/batch", params: t }), c = (t) => o.post({ url: "/online/cgform/api/authColumn/batch", params: t }), h = (t, a) => o.get({ url: `/online/cgform/api/authButton/${t}`, params: a }), p = (t) => o.post({ url: "/online/cgform/api/authButton", params: t }), g = (t, a) => o.put({ url: `/online/cgform/api/authButton/${t}`, params: a }), m = (t, a) => o.get({ url: `/online/cgform/api/authData/${t}`, params: a }), $ = (t) => o.put({ url: "/online/cgform/api/authData", params: t }), d = (t, a) => a ? o.put({ url: "/online/cgform/api/authData", params: t }) : o.post({ url: "/online/cgform/api/authData", params: t }), f = (t, a) => o.delete({ url: `/online/cgform/api/authData/${t}`, params: a }), D = (t, a, e) => {
  let u = `/online/cgform/api/authPage/${t}/${a}`;
  return o.get({ url: u, params: e });
}, A = (t, a) => {
  let e = `/online/cgform/api/validAuthData/${t}`;
  return o.get({ url: e, params: a });
}, b = (t, a, e) => {
  let u = `/online/cgform/api/authPage/${t}/${a}`;
  return o.get({ url: u, params: e });
}, B = (t) => o.get({ url: "/online/cgform/api/roleAuth", params: t }), C = (t, a, e) => {
  let u = `/online/cgform/api/roleColumnAuth/${t}/${a}`;
  return o.post({ url: u, params: e });
}, F = (t, a, e) => {
  let u = `/online/cgform/api/roleDataAuth/${t}/${a}`;
  return o.post({ url: u, params: e });
}, L = (t, a, e) => {
  let u = `/online/cgform/api/roleButtonAuth/${t}/${a}`;
  return o.post({ url: u, params: e }, { successMessageMode: "none", isTransformResponse: !1 });
};
export {
  l as a,
  r as b,
  s as c,
  i as d,
  c as e,
  h as f,
  p as g,
  g as h,
  m as i,
  d as j,
  $ as k,
  f as l,
  D as m,
  B as n,
  b as o,
  L as p,
  A as q,
  F as r,
  C as s
};
