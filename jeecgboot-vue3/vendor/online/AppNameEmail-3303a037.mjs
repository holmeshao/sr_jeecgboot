var h = (E, x, m) => new Promise((o, r) => {
  var v = (i) => {
    try {
      c(m.next(i));
    } catch (d) {
      r(d);
    }
  }, _ = (i) => {
    try {
      c(m.throw(i));
    } catch (d) {
      r(d);
    }
  }, c = (i) => i.done ? o(i.value) : Promise.resolve(i.value).then(v, _);
  c((m = m.apply(E, x)).next());
});
import { defineComponent as q, reactive as L, ref as p, computed as g, unref as t, resolveComponent as y, openBlock as $, createElementBlock as j, createVNode as l, createElementVNode as n, withCtx as k, normalizeClass as w, toDisplayString as z, createTextVNode as H, toRaw as G } from "vue";
import J from "./AppLoginHeader-4432c584.mjs";
import { useI18n as K } from "/@/hooks/web/useI18n";
import { register as O } from "/@/api/sys/user";
import { useMessage as P } from "/@/hooks/web/useMessage";
import { _ as Q } from "./index-9e1e1e53.mjs";
import "/@/assets/images/logo.png";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const W = { class: "name-email-subject" }, X = { class: "name-email-content" }, Y = /* @__PURE__ */ q({
  __name: "AppNameEmail",
  emits: ["login-account", "bind-third-account"],
  setup(E, { expose: x, emit: m }) {
    const o = L({
      realname: "",
      email: ""
    }), { t: r } = K(), v = p(), _ = p(), c = p(), i = g(() => [{ required: !0, message: "请填写邮箱", trigger: "change" }, { type: "email", message: "请填写正确的邮箱" }]), d = g(() => [{ required: !0, message: "请填写昵称", trigger: "change" }]), M = g(() => ({
      realname: t(d),
      email: t(i)
    })), u = p(""), F = g(() => o.realname || t(u) === "realname" ? "current-active" : ""), S = g(() => o.email != "" || t(u) === "email" ? "current-active" : ""), a = p({}), N = m, { notification: C, createErrorModal: Z } = P(), R = p(!1);
    function b(s) {
      u.value = s, s === "realname" ? v.value.focus() : _.value.focus();
    }
    function A() {
      u.value = "";
    }
    function T() {
      return h(this, null, function* () {
        c.value.validateFields().then((s) => h(this, null, function* () {
          if (t(a).bindThirdAccount)
            N("bind-third-account", {
              username: t(a).phone,
              email: s.email,
              realname: s.realname,
              password: t(a).password,
              phone: t(a).phone,
              smscode: t(a).smscode
            });
          else
            try {
              R.value = !0;
              const e = yield O(
                G({
                  username: t(a).phone,
                  email: s.email,
                  realname: s.realname,
                  password: t(a).password,
                  phone: t(a).phone,
                  smscode: t(a).smscode
                })
              );
              e && e.data.success ? (N("login-account", { account: t(a).phone, password: t(a).password }), C.success({
                message: void 0,
                description: e.data.message || r("sys.api.registerMsg"),
                duration: 3
              })) : C.warning({
                message: r("sys.api.errorTip"),
                description: e.data.message || r("sys.api.networkExceptionMsg"),
                duration: 3
              });
            } catch (e) {
              C.error({
                message: r("sys.api.errorTip"),
                description: e.message || r("sys.api.networkExceptionMsg"),
                duration: 3
              });
            } finally {
              R.value = !1;
            }
        }));
      });
    }
    function U(s) {
      a.value = s;
    }
    return x({
      setRegisterData: U
    }), (s, e) => {
      const B = y("a-input"), I = y("a-form-item"), D = y("a-button"), V = y("a-form");
      return $(), j("div", W, [
        l(J),
        e[5] || (e[5] = n("div", { class: "flex-row align-items-center margin-top40" }, [
          n("div", { class: "register-title" }, " 请填写昵称和邮箱 ")
        ], -1)),
        e[6] || (e[6] = n("div", { class: "name-email-desc align-items-center" }, [
          n("span", { style: { color: "#9e9e9e" } }, "请填写昵称和邮箱，方便大家与您联系")
        ], -1)),
        n("div", X, [
          l(V, {
            ref_key: "formRef",
            ref: c,
            model: o,
            rules: M.value
          }, {
            default: k(() => [
              n("div", {
                class: w(["content-item", F.value]),
                onClick: e[1] || (e[1] = (f) => b("realname"))
              }, [
                l(I, { name: "realname" }, {
                  default: k(() => [
                    l(B, {
                      ref_key: "realNameRef",
                      ref: v,
                      value: o.realname,
                      "onUpdate:value": e[0] || (e[0] = (f) => o.realname = f),
                      style: { height: "40px" },
                      onBlur: A
                    }, null, 8, ["value"]),
                    n("div", {
                      class: w(["form-title", u.value === "username" ? "active-title" : ""])
                    }, " 昵称 ", 2)
                  ]),
                  _: 1
                })
              ], 2),
              n("div", {
                class: w(["content-item", S.value]),
                onClick: e[3] || (e[3] = (f) => b("email"))
              }, [
                l(I, { name: "email" }, {
                  default: k(() => [
                    l(B, {
                      ref_key: "emailRef",
                      ref: _,
                      value: o.email,
                      "onUpdate:value": e[2] || (e[2] = (f) => o.email = f),
                      style: { height: "40px" },
                      onBlur: A
                    }, null, 8, ["value"]),
                    n("div", {
                      class: w(["form-title", u.value === "email" ? "active-title" : ""])
                    }, z(t(r)("sys.login.email")), 3)
                  ]),
                  _: 1
                })
              ], 2),
              n("div", {
                class: "pointer",
                onClick: T
              }, [
                l(D, {
                  type: "primary",
                  class: "next-step",
                  loading: R.value
                }, {
                  default: k(() => e[4] || (e[4] = [
                    H("完成")
                  ])),
                  _: 1
                }, 8, ["loading"])
              ])
            ]),
            _: 1
          }, 8, ["model", "rules"])
        ])
      ]);
    };
  }
});
const ce = /* @__PURE__ */ Q(Y, [["__scopeId", "data-v-f3b40873"]]);
export {
  ce as default
};
