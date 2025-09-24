var h = (O, M, P) => new Promise((U, k) => {
  var s = (m) => {
    try {
      g(P.next(m));
    } catch (y) {
      k(y);
    }
  }, d = (m) => {
    try {
      g(P.throw(m));
    } catch (y) {
      k(y);
    }
  }, g = (m) => m.done ? U(m.value) : Promise.resolve(m.value).then(s, d);
  g((P = P.apply(O, M)).next());
});
import { defineComponent as ue, reactive as de, ref as u, computed as c, unref as o, resolveComponent as T, openBlock as B, createElementBlock as ce, Fragment as me, createElementVNode as t, createVNode as a, toDisplayString as v, withDirectives as q, withCtx as w, normalizeClass as f, createBlock as z, vShow as G, toRaw as K } from "vue";
import pe from "./AppLoginHeader-4432c584.mjs";
import { useI18n as fe } from "/@/hooks/web/useI18n";
import { useMessage as W } from "/@/hooks/web/useMessage";
import { getCaptcha as ve, phoneVerify as ge, passwordChange as we } from "/@/api/sys/user";
import { SmsEnum as Pe } from "/@/views/sys/login/useLogin";
import ye from "/@/components/jeecg/captcha/CaptchaModal.vue";
import { useModal as _e } from "/@/components/Modal";
import { ExceptionEnum as he } from "/@/enums/exceptionEnum";
import { _ as Ce } from "./index-9e1e1e53.mjs";
import "/@/assets/images/logo.png";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const ke = { class: "forget-pwd-box" }, Re = { class: "forget-pwd-subject" }, be = { class: "flex-row align-items-center margin-top40" }, xe = { class: "register-title" }, Fe = { class: "register-content" }, Me = { class: "active-form-title" }, Ue = /* @__PURE__ */ ue({
  __name: "AppForgetPassword",
  emits: ["return-login", "login-account"],
  setup(O, { emit: M }) {
    const [P, { openModal: U }] = _e(), { createMessage: k } = W(), { t: s } = fe(), d = de({
      phone: "",
      smscode: "",
      forgetPassword: "",
      policy: !0
    }), g = u(), m = u(), y = u(), N = u(), A = u(), R = u("vailPhone"), p = u(""), J = c(() => E(s("sys.login.mobilePlaceholder"))), Q = c(() => E(s("sys.login.smsPlaceholder"))), X = c(() => E(s("sys.login.passwordPlaceholder"))), Y = c(() => ({
      phone: o(J),
      smscode: o(Q)
    })), Z = c(() => ({
      forgetPassword: o(X),
      confirmPassword: [{ validator: se(o(l).forgetPassword), trigger: "change" }]
    })), { notification: ee, createErrorModal: oe } = W(), D = c(() => s("component.countdown.normalText")), L = c(() => s("component.countdown.sendText", [o(_)])), se = (n) => (e, i) => h(this, null, function* () {
      return i ? i !== n ? Promise.reject(s("sys.login.diffPwd")) : Promise.resolve() : Promise.reject(s("sys.login.passwordPlaceholder"));
    });
    function E(n) {
      return [
        {
          required: !0,
          message: n,
          trigger: "change"
        }
      ];
    }
    const $ = c(() => d.phone != "" || o(p) === "phone" ? "current-active" : ""), te = c(() => d.smscode != "" || o(p) === "smscode" ? "current-active" : ""), ne = c(() => o(l).forgetPassword != "" || o(p) === "forgetPassword" ? "current-active" : ""), re = c(() => o(l).confirmPassword != "" || o(p) === "confirmPassword" ? "current-active" : ""), j = M, I = u(!0), _ = u(60), b = u(null), S = u(), l = u([]);
    function x(n) {
      p.value = n, n === "phone" ? g.value.focus() : n === "smscode" ? y.value.focus() : n === "confirmPassword" ? A.value.focus() : m.value.focus();
    }
    function F() {
      p.value = "";
    }
    function V() {
      return h(this, null, function* () {
        if (!d.phone) {
          k.warn(s("sys.login.mobilePlaceholder"));
          return;
        }
        (yield ve({ mobile: d.phone, smsmode: Pe.FORGET_PASSWORD }).catch((e) => {
          e.code === he.PHONE_SMS_FAIL_CODE && U(!0, {});
        })) && (o(b) || (_.value = 60, I.value = !1, b.value = setInterval(() => {
          o(_) > 0 && o(_) <= 60 ? _.value = _.value - 1 : (I.value = !0, clearInterval(o(b)), b.value = null);
        }, 1e3)));
      });
    }
    function le() {
      S.value.validateFields().then((n) => h(this, null, function* () {
        const e = yield ge(
          K({
            phone: n.phone,
            smscode: n.smscode
          })
        );
        if (e.success) {
          let i = {
            username: e.result.username,
            phone: n.phone,
            smscode: e.result.smscode
          };
          l.value = i, R.value = "vailPwd";
        } else
          ee.error({
            message: s("sys.api.errorTip"),
            description: e.message || s("sys.api.networkExceptionMsg"),
            duration: 3
          });
      }));
    }
    function ae() {
      return h(this, null, function* () {
        N.value.validateFields().then((n) => h(this, null, function* () {
          const e = yield we(
            K({
              username: n.username,
              password: n.forgetPassword,
              smscode: o(l).smscode,
              phone: o(l).phone
            })
          );
          e.success ? (j("login-account", { account: n.username, password: n.forgetPassword }), S.value.resetFields(), l.value = {}, R.value = "vailPhone") : oe({
            title: s("sys.api.errorTip"),
            content: e.message || s("sys.api.networkExceptionMsg")
          });
        }));
      });
    }
    function ie() {
      j("return-login");
    }
    return (n, e) => {
      const i = T("a-input"), C = T("a-form-item"), H = T("a-form");
      return B(), ce(me, null, [
        t("div", ke, [
          t("div", Re, [
            a(pe),
            t("div", be, [
              t("div", xe, v(o(s)("sys.login.forgetFormTitle")), 1)
            ]),
            t("div", Fe, [
              q(a(H, {
                ref_key: "phoneUpdateRef",
                ref: S,
                model: d,
                rules: Y.value
              }, {
                default: w(() => [
                  t("div", {
                    class: f(["content-item", $.value]),
                    onClick: e[1] || (e[1] = (r) => x("phone"))
                  }, [
                    a(C, { name: "phone" }, {
                      default: w(() => [
                        a(i, {
                          ref_key: "phoneRef",
                          ref: g,
                          value: d.phone,
                          "onUpdate:value": e[0] || (e[0] = (r) => d.phone = r),
                          style: { height: "40px" },
                          onBlur: F
                        }, null, 8, ["value"]),
                        t("div", {
                          class: f(["form-title", p.value === "phone" ? "active-title" : ""])
                        }, v(o(s)("sys.login.mobile")), 3)
                      ]),
                      _: 1
                    })
                  ], 2),
                  t("div", {
                    class: f(["content-item", te.value])
                  }, [
                    a(C, {
                      name: "smscode",
                      onClick: e[3] || (e[3] = (r) => x("smscode"))
                    }, {
                      default: w(() => [
                        a(i, {
                          ref_key: "smscodeRef",
                          ref: y,
                          maxLength: 6,
                          value: d.smscode,
                          "onUpdate:value": e[2] || (e[2] = (r) => d.smscode = r),
                          style: { height: "40px" },
                          onBlur: F
                        }, null, 8, ["value"]),
                        t("div", {
                          class: f(["form-title", p.value === "smscode" ? "active-title" : ""])
                        }, v(o(s)("sys.login.smsCode")), 3)
                      ]),
                      _: 1
                    }),
                    I.value ? (B(), z(i, {
                      key: 0,
                      type: "button",
                      class: "aui-code-line pointer",
                      bordered: !1,
                      onClick: V,
                      value: D.value,
                      "onUpdate:value": e[4] || (e[4] = (r) => D.value = r)
                    }, null, 8, ["value"])) : (B(), z(i, {
                      key: 1,
                      type: "button",
                      class: "aui-code-line disabled-btn",
                      bordered: !1,
                      value: L.value,
                      "onUpdate:value": e[5] || (e[5] = (r) => L.value = r)
                    }, null, 8, ["value"]))
                  ], 2),
                  t("div", {
                    class: "forget-btn pointer",
                    onClick: le
                  }, [
                    t("span", null, v(o(s)("sys.login.nextStep")), 1)
                  ]),
                  e[11] || (e[11] = t("div", { class: "line" }, null, -1))
                ]),
                _: 1
              }, 8, ["model", "rules"]), [
                [G, R.value === "vailPhone"]
              ]),
              q(a(H, {
                ref_key: "pwdUpdateRef",
                ref: N,
                model: l.value,
                rules: Z.value
              }, {
                default: w(() => [
                  t("div", {
                    class: f(["content-item", $.value])
                  }, [
                    a(C, { name: "username" }, {
                      default: w(() => [
                        a(i, {
                          ref_key: "phoneRef",
                          ref: g,
                          value: l.value.username,
                          "onUpdate:value": e[6] || (e[6] = (r) => l.value.username = r),
                          style: { height: "40px" },
                          disabled: ""
                        }, null, 8, ["value"]),
                        t("div", Me, v(o(s)("sys.login.userName")), 1)
                      ]),
                      _: 1
                    })
                  ], 2),
                  t("div", {
                    class: f(["content-item", ne.value]),
                    onClick: e[8] || (e[8] = (r) => x("forgetPassword"))
                  }, [
                    a(C, { name: "forgetPassword" }, {
                      default: w(() => [
                        a(i, {
                          ref_key: "pwdRef",
                          ref: m,
                          type: "password",
                          value: l.value.forgetPassword,
                          "onUpdate:value": e[7] || (e[7] = (r) => l.value.forgetPassword = r),
                          style: { height: "40px" },
                          onBlur: F
                        }, null, 8, ["value"]),
                        t("div", {
                          class: f(["form-title", p.value === "password" ? "active-title" : ""])
                        }, v(o(s)("sys.login.password")), 3)
                      ]),
                      _: 1
                    })
                  ], 2),
                  t("div", {
                    class: f(["content-item", re.value]),
                    onClick: e[10] || (e[10] = (r) => x("confirmPassword"))
                  }, [
                    a(C, { name: "confirmPassword" }, {
                      default: w(() => [
                        a(i, {
                          ref_key: "conPwdRef",
                          ref: A,
                          type: "password",
                          value: l.value.confirmPassword,
                          "onUpdate:value": e[9] || (e[9] = (r) => l.value.confirmPassword = r),
                          style: { height: "40px" },
                          onBlur: F
                        }, null, 8, ["value"]),
                        t("div", {
                          class: f(["form-title", p.value === "password" ? "active-title" : ""])
                        }, v(o(s)("sys.login.confirmPassword")), 3)
                      ]),
                      _: 1
                    })
                  ], 2),
                  t("div", {
                    class: "forget-btn pointer",
                    onClick: ae
                  }, e[12] || (e[12] = [
                    t("span", null, "完成", -1)
                  ])),
                  e[13] || (e[13] = t("div", { class: "line" }, null, -1))
                ]),
                _: 1
              }, 8, ["model", "rules"]), [
                [G, R.value === "vailPwd"]
              ]),
              t("span", {
                class: "to-login pointer",
                onClick: ie
              }, v(o(s)("sys.exception.backLogin")), 1)
            ])
          ])
        ]),
        a(ye, {
          onRegister: o(P),
          onOk: V
        }, null, 8, ["onRegister"])
      ], 64);
    };
  }
});
const qe = /* @__PURE__ */ Ce(Ue, [["__scopeId", "data-v-b3303144"]]);
export {
  qe as default
};
