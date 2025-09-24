var T = (x, A, f) => new Promise((E, o) => {
  var n = (c) => {
    try {
      g(f.next(c));
    } catch (b) {
      o(b);
    }
  }, k = (c) => {
    try {
      g(f.throw(c));
    } catch (b) {
      o(b);
    }
  }, g = (c) => c.done ? E(c.value) : Promise.resolve(c.value).then(n, k);
  g((f = f.apply(x, A)).next());
});
import { defineComponent as ge, reactive as ve, ref as a, computed as u, unref as s, resolveComponent as h, openBlock as w, createElementBlock as F, Fragment as _e, withDirectives as q, createElementVNode as i, createVNode as l, toDisplayString as _, withCtx as p, normalizeClass as y, createBlock as z, createTextVNode as H, createCommentVNode as G, vShow as Z } from "vue";
import ye from "./AppLoginHeader-4432c584.mjs";
import { useI18n as be } from "/@/hooks/web/useI18n";
import { useMessage as J } from "/@/hooks/web/useMessage";
import { getCaptcha as he, phoneVerify as we } from "/@/api/sys/user";
import { SmsEnum as ke } from "/@/views/sys/login/useLogin";
import Ce from "./AppNameEmail-3303a037.mjs";
import Re from "/@/components/jeecg/captcha/CaptchaModal.vue";
import { useModal as Pe } from "/@/components/Modal";
import { ExceptionEnum as Te } from "/@/enums/exceptionEnum";
import { _ as xe } from "./index-9e1e1e53.mjs";
import "/@/assets/images/logo.png";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const Ae = { class: "register-box" }, Ee = { class: "register-subject" }, Me = { class: "flex-row align-items-center margin-top40" }, Ie = { class: "register-title" }, Ue = { class: "register-content" }, Be = { class: "register-rule" }, Ne = {
  key: 0,
  class: "line"
}, Se = /* @__PURE__ */ ge({
  __name: "AppRegister",
  props: {
    bindThirdAccount: { type: Boolean, default: !1 }
  },
  emits: ["return-login", "login-account", "bind-third-account"],
  setup(x, { expose: A, emit: f }) {
    const { createMessage: E } = J(), { t: o } = be(), n = ve({
      mobile: "",
      sms: "",
      regPassword: "",
      policy: !0
    }), k = a(), g = a(), c = a(), b = a(), M = a(!1), d = a(""), K = u(() => n.mobile != "" || s(d) === "mobile" ? "current-active" : "");
    u(() => n.username != "" || s(d) === "username" ? "current-active" : "");
    const Q = u(() => n.sms != "" || s(d) === "sms" ? "current-active" : ""), W = u(() => n.regPassword != "" || s(d) === "regPassword" ? "current-active" : ""), I = f, U = a(!0), v = a(60), C = a(null), B = a(), X = u(() => re(o("sys.login.mobilePlaceholder"))), Y = u(() => ne(o("sys.login.smsPlaceholder"))), ee = u(() => [{ required: !0, validator: ie }, { pattern: /^(?=.*[0-9])(?=.*[a-zA-Z])(.{8,20})$/, message: "8-20位，需包含字母和数字" }]), te = u(() => ({
      mobile: s(X),
      sms: s(Y),
      regPassword: s(ee)
    })), { notification: L, createErrorModal: De } = J(), O = u(() => o("component.countdown.normalText")), V = u(() => o("component.countdown.sendText", [s(v)])), R = a("register"), $ = a(), N = x, [oe, { openModal: se }] = Pe();
    function ne(t) {
      return [
        {
          required: !0,
          message: t,
          trigger: "change"
        }
      ];
    }
    function re(t) {
      return [
        {
          required: !0,
          message: t,
          trigger: "change"
        },
        {
          pattern: /^1[3456789]\d{9}$/,
          message: o("sys.login.mobileCorrectPlaceholder")
        }
      ];
    }
    function S(t) {
      d.value = t, t === "mobile" ? k.value.focus() : t === "sms" ? c.value.focus() : t === "username" ? b.value.focus() : g.value.focus();
    }
    function D() {
      d.value = "";
    }
    function j() {
      return T(this, null, function* () {
        if (!n.mobile) {
          E.warn(o("sys.login.mobilePlaceholder"));
          return;
        }
        (yield he({ mobile: n.mobile, smsmode: ke.REGISTER }).catch((e) => {
          e.code === Te.PHONE_SMS_FAIL_CODE && se(!0, {});
        })) && (s(C) || (v.value = 60, U.value = !1, C.value = setInterval(() => {
          s(v) > 0 && s(v) <= 60 ? v.value = v.value - 1 : (U.value = !0, clearInterval(s(C)), C.value = null);
        }, 1e3)));
      });
    }
    function ie(t, e) {
      return e === "" ? Promise.reject(o("sys.login.passwordPlaceholder")) : Promise.resolve();
    }
    function le() {
      I("return-login");
    }
    function ae() {
      return T(this, null, function* () {
        B.value.validateFields().then((t) => T(this, null, function* () {
          M.value = !0, yield we({ phone: t.mobile, smscode: t.sms }).then((e) => {
            if (e.success)
              e.result && e.result.username && L.warning({
                message: o("sys.api.errorTip"),
                description: "手机号已注册",
                duration: 3
              });
            else if (e.message === "用户信息不存在") {
              let m = { password: t.regPassword, phone: t.mobile, smscode: t.sms, bindThirdAccount: !1, thirdUserUuid: "", thirdType: "" };
              R.value = "email", setTimeout(() => {
                m.bindThirdAccount = N.bindThirdAccount, $.value.setRegisterData(m);
              }, 300);
            } else
              L.warning({
                message: o("sys.api.errorTip"),
                description: e.message || o("sys.api.networkExceptionMsg"),
                duration: 3
              });
          }).finally(() => {
            M.value = !1;
          });
        }));
      });
    }
    function ce() {
      R.value = "register", Object.assign(n, { mobile: "", sms: "", regPassword: "", policy: !0 }), B.value.clearValidate();
    }
    function ue(t) {
      I("login-account", t);
    }
    function de(t) {
      I("bind-third-account", t);
    }
    return A({
      clearValidate: ce
    }), (t, e) => {
      const m = h("a-input"), P = h("a-form-item"), me = h("a-checkbox"), pe = h("a-button"), fe = h("a-form");
      return w(), F(_e, null, [
        q(i("div", Ae, [
          i("div", Ee, [
            l(ye),
            i("div", Me, [
              i("div", Ie, _(s(o)("sys.login.signUpFormTitle")), 1)
            ]),
            i("div", Ue, [
              l(fe, {
                ref_key: "registerRef",
                ref: B,
                model: n,
                rules: te.value
              }, {
                default: p(() => [
                  i("div", {
                    class: y(["content-item", K.value]),
                    onClick: e[1] || (e[1] = (r) => S("mobile"))
                  }, [
                    l(P, { name: "mobile" }, {
                      default: p(() => [
                        l(m, {
                          ref_key: "phoneRef",
                          ref: k,
                          value: n.mobile,
                          "onUpdate:value": e[0] || (e[0] = (r) => n.mobile = r),
                          style: { height: "40px" },
                          onBlur: D
                        }, null, 8, ["value"]),
                        i("div", {
                          class: y(["form-title", d.value === "mobile" ? "active-title" : ""])
                        }, _(s(o)("sys.login.mobile")), 3)
                      ]),
                      _: 1
                    })
                  ], 2),
                  i("div", {
                    class: y(["content-item", Q.value])
                  }, [
                    l(P, {
                      name: "sms",
                      onClick: e[3] || (e[3] = (r) => S("sms"))
                    }, {
                      default: p(() => [
                        l(m, {
                          ref_key: "smscodeRef",
                          ref: c,
                          maxLength: 6,
                          value: n.sms,
                          "onUpdate:value": e[2] || (e[2] = (r) => n.sms = r),
                          style: { height: "40px" },
                          onBlur: D
                        }, null, 8, ["value"]),
                        i("div", {
                          class: y(["form-title", d.value === "sms" ? "active-title" : ""])
                        }, _(s(o)("sys.login.smsCode")), 3)
                      ]),
                      _: 1
                    }),
                    U.value ? (w(), z(m, {
                      key: 0,
                      type: "button",
                      class: "aui-code-line pointer",
                      bordered: !1,
                      onClick: j,
                      value: O.value,
                      "onUpdate:value": e[4] || (e[4] = (r) => O.value = r)
                    }, null, 8, ["value"])) : (w(), z(m, {
                      key: 1,
                      type: "button",
                      class: "aui-code-line disabled-btn",
                      bordered: !1,
                      value: V.value,
                      "onUpdate:value": e[5] || (e[5] = (r) => V.value = r)
                    }, null, 8, ["value"]))
                  ], 2),
                  i("div", {
                    class: y(["content-item", W.value]),
                    onClick: e[7] || (e[7] = (r) => S("regPassword"))
                  }, [
                    l(P, { name: "regPassword" }, {
                      default: p(() => [
                        l(m, {
                          ref_key: "pwdRef",
                          ref: g,
                          type: "password",
                          value: n.regPassword,
                          "onUpdate:value": e[6] || (e[6] = (r) => n.regPassword = r),
                          style: { height: "40px" },
                          onBlur: D,
                          autocomplete: "new-password"
                        }, null, 8, ["value"]),
                        i("div", {
                          class: y(["form-title", d.value === "regPassword" ? "active-title" : ""])
                        }, " 8-20位，需包含字母和数字 ", 2)
                      ]),
                      _: 1
                    })
                  ], 2),
                  i("p", Be, [
                    l(P, { name: "policy" }, {
                      default: p(() => [
                        l(me, {
                          checked: n.policy,
                          "onUpdate:checked": e[8] || (e[8] = (r) => n.policy = r)
                        }, {
                          default: p(() => [
                            H(_(s(o)("sys.login.policy")), 1)
                          ]),
                          _: 1
                        }, 8, ["checked"])
                      ]),
                      _: 1
                    })
                  ]),
                  i("div", null, [
                    l(pe, {
                      type: "primary",
                      loading: M.value,
                      class: "registr-btn pointer",
                      onClick: ae
                    }, {
                      default: p(() => [
                        H(_(s(o)("sys.login.nextStep")), 1)
                      ]),
                      _: 1
                    }, 8, ["loading"])
                  ]),
                  N.bindThirdAccount ? G("", !0) : (w(), F("div", Ne)),
                  N.bindThirdAccount ? G("", !0) : (w(), F("span", {
                    key: 1,
                    class: "to-login pointer",
                    onClick: le
                  }, _(s(o)("sys.exception.backLogin")), 1))
                ]),
                _: 1
              }, 8, ["model", "rules"])
            ])
          ])
        ], 512), [
          [Z, R.value === "register"]
        ]),
        q(i("div", null, [
          l(Ce, {
            ref_key: "emailRef",
            ref: $,
            onLoginAccount: ue,
            onBindThirdAccount: de
          }, null, 512)
        ], 512), [
          [Z, R.value === "email"]
        ]),
        l(Re, {
          onRegister: s(oe),
          onOk: j
        }, null, 8, ["onRegister"])
      ], 64);
    };
  }
});
const Xe = /* @__PURE__ */ xe(Se, [["__scopeId", "data-v-288dafe5"]]);
export {
  Xe as default
};
