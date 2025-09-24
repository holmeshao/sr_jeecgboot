var U = (L, y, _) => new Promise((k, C) => {
  var s = (l) => {
    try {
      p(_.next(l));
    } catch (i) {
      C(i);
    }
  }, r = (l) => {
    try {
      p(_.throw(l));
    } catch (i) {
      C(i);
    }
  }, p = (l) => l.done ? k(l.value) : Promise.resolve(l.value).then(s, r);
  p((_ = _.apply(L, y)).next());
});
import { defineComponent as J, ref as m, reactive as A, computed as S, unref as n, onMounted as Q, resolveComponent as f, openBlock as B, createBlock as W, withKeys as X, withCtx as d, createElementVNode as t, createVNode as c, normalizeClass as F, toDisplayString as g, createElementBlock as D, createTextVNode as E, toRaw as Y } from "vue";
import { useUserStore as Z } from "/@/store/modules/user";
import { useFormRules as ee, useFormValid as oe } from "/@/views/sys/login/useLogin";
import { useI18n as te } from "/@/hooks/web/useI18n";
import { getCodeInfo as ne } from "/@/api/sys/user";
import { useMessage as se } from "/@/hooks/web/useMessage";
import ie from "/@/assets/images/checkcode.png";
import { _ as ae } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const ce = { class: "content-item current-active" }, re = { class: "code-image" }, le = ["src"], ue = ["src"], de = { class: "forget-pwd" }, pe = {
  style: { float: "left" },
  class: "pointer forget-login-pwd"
}, me = {
  class: "remember-password pointer",
  style: { float: "right" }
}, fe = /* @__PURE__ */ J({
  __name: "AccountLoginForm",
  emits: ["login", "forget-pwd", "login-success"],
  setup(L, { expose: y, emit: _ }) {
    const k = m(), C = m();
    m();
    const { t: s } = te(), r = m(""), p = m(!1), l = m(), i = A({
      randCodeImage: "",
      requestCodeSuccess: !1,
      checkKey: -1
    }), o = A({
      account: "",
      password: "",
      inputCode: ""
    }), { getFormRules: N } = ee(o), { validForm: T } = oe(l), V = Z(), R = m(!1);
    S(() => o.account != "" || n(r) === "account" ? "current-active" : ""), S(() => o.password != "" || n(r) === "password" ? "current-active" : ""), S(() => o.inputCode != "" || n(r) === "inputCode" ? "current-active" : "");
    const { notification: q, createErrorModal: ge } = se(), w = _;
    function h(a) {
      r.value = a;
    }
    function x() {
      r.value = "";
    }
    function K() {
      return U(this, null, function* () {
        let a = yield T();
        if (a)
          try {
            p.value = !0;
            const { userInfo: e } = yield V.login(
              Y({
                password: a.password,
                username: a.account,
                captcha: a.inputCode,
                checkKey: i.checkKey,
                mode: "none",
                //不要默认的错误提示
                goHome: !1
                //不跳转到首页
              })
            );
            e && w("login-success", e.realname);
          } catch (e) {
            q.error({
              message: s("sys.api.errorTip"),
              description: e.message || s("sys.api.networkExceptionMsg"),
              duration: 3
            }), v();
          } finally {
            p.value = !1;
          }
      });
    }
    function v() {
      o.inputCode = "", i.checkKey = (/* @__PURE__ */ new Date()).getTime() + Math.random().toString(36).slice(-4), ne(i.checkKey).then((a) => {
        i.randCodeImage = a, i.requestCodeSuccess = !0;
      });
    }
    function $() {
      w("login", "phoneLogin");
    }
    function P() {
      w("forget-pwd");
    }
    function j(a) {
      Object.assign(o, a), v();
    }
    return Q(() => {
      v();
    }), y({
      setAccountData: j
    }), (a, e) => {
      const b = f("a-input"), I = f("a-form-item"), M = f("a-col"), z = f("a-row"), H = f("a-checkbox"), O = f("a-button"), G = f("a-form");
      return B(), W(G, {
        id: "loginContentForm",
        ref_key: "loginRef",
        ref: l,
        model: o,
        rules: n(N),
        onKeyup: X(K, ["enter", "native"])
      }, {
        default: d(() => [
          t("div", {
            class: "content-item current-active",
            onClick: e[1] || (e[1] = (u) => h("account"))
          }, [
            c(I, { name: "account" }, {
              default: d(() => [
                c(b, {
                  ref_key: "accountRef",
                  ref: k,
                  id: "accountLogin",
                  value: o.account,
                  "onUpdate:value": e[0] || (e[0] = (u) => o.account = u),
                  style: { height: "40px" },
                  onBlur: x
                }, null, 8, ["value"]),
                t("div", {
                  class: F(["form-title", r.value === "account" ? "active-title" : ""])
                }, g(n(s)("sys.login.userName")), 3)
              ]),
              _: 1
            })
          ]),
          t("div", {
            class: "content-item current-active",
            onClick: e[3] || (e[3] = (u) => h("password"))
          }, [
            c(I, { name: "password" }, {
              default: d(() => [
                c(b, {
                  id: "pwdLogin",
                  ref: "",
                  type: "password",
                  value: o.password,
                  "onUpdate:value": e[2] || (e[2] = (u) => o.password = u),
                  style: { height: "40px" },
                  onBlur: x
                }, null, 8, ["value"]),
                t("div", {
                  class: F(["form-title", r.value === "password" ? "active-title" : ""])
                }, g(n(s)("sys.login.password")), 3)
              ]),
              _: 1
            })
          ]),
          t("div", ce, [
            c(z, { span: 24 }, {
              default: d(() => [
                c(M, { span: 15 }, {
                  default: d(() => [
                    c(I, {
                      name: "inputCode",
                      onClick: e[5] || (e[5] = (u) => h("inputCode"))
                    }, {
                      default: d(() => [
                        c(b, {
                          ref_key: "codeRef",
                          ref: C,
                          value: o.inputCode,
                          "onUpdate:value": e[4] || (e[4] = (u) => o.inputCode = u),
                          style: { height: "40px" },
                          onBlur: x
                        }, null, 8, ["value"]),
                        t("div", {
                          class: F(["form-title", r.value === "inputCode" ? "active-title" : ""])
                        }, g(n(s)("sys.login.inputCode")), 3)
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }),
                c(M, { span: 8 }, {
                  default: d(() => [
                    t("div", re, [
                      i.requestCodeSuccess ? (B(), D("img", {
                        key: 0,
                        class: "pointer",
                        style: { "margin-top": "2px", "max-width": "initial" },
                        src: i.randCodeImage,
                        onClick: v
                      }, null, 8, le)) : (B(), D("img", {
                        key: 1,
                        style: { "margin-top": "2px", "max-width": "initial" },
                        src: n(ie),
                        onClick: v
                      }, null, 8, ue))
                    ])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          t("div", de, [
            t("div", pe, [
              t("span", {
                style: { color: "#757575" },
                onClick: P
              }, g(n(s)("sys.login.forgetPassword")), 1)
            ]),
            t("div", me, [
              c(H, {
                checked: R.value,
                "onUpdate:checked": e[6] || (e[6] = (u) => R.value = u)
              }, {
                default: d(() => [
                  E(g(n(s)("sys.login.rememberMe")), 1)
                ]),
                _: 1
              }, 8, ["checked"])
            ])
          ]),
          t("div", null, [
            c(O, {
              type: "primary",
              onClick: K,
              loading: p.value,
              class: "login-btn"
            }, {
              default: d(() => [
                E(g(n(s)("sys.login.loginButton")), 1)
              ]),
              _: 1
            }, 8, ["loading"])
          ]),
          t("div", {
            class: "phone-login-btn pointer",
            onClick: $
          }, g(n(s)("sys.login.mobileSignInFormTitle")), 1)
        ]),
        _: 1
      }, 8, ["model", "rules"]);
    };
  }
});
const Be = /* @__PURE__ */ ae(fe, [["__scopeId", "data-v-ffc0558b"]]);
export {
  Be as default
};
