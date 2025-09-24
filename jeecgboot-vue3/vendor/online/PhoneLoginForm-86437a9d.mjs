var y = (M, T, d) => new Promise((t, s) => {
  var p = (i) => {
    try {
      n(d.next(i));
    } catch (u) {
      s(u);
    }
  }, g = (i) => {
    try {
      n(d.throw(i));
    } catch (u) {
      s(u);
    }
  }, n = (i) => i.done ? t(i.value) : Promise.resolve(i.value).then(p, g);
  n((d = d.apply(M, T)).next());
});
import { defineComponent as ee, ref as r, reactive as A, computed as c, unref as o, resolveComponent as C, openBlock as h, createElementBlock as V, Fragment as oe, createVNode as m, withKeys as te, withCtx as k, createElementVNode as v, normalizeClass as R, toDisplayString as I, createBlock as H, createTextVNode as ne, createCommentVNode as le, toRaw as se } from "vue";
import { useUserStore as ie } from "/@/store/modules/user";
import { SmsEnum as ae } from "/@/views/sys/login/useLogin";
import { useI18n as re } from "/@/hooks/web/useI18n";
import { getCaptcha as ue } from "/@/api/sys/user";
import { useMessage as K } from "/@/hooks/web/useMessage";
import ce from "/@/components/jeecg/captcha/CaptchaModal.vue";
import { useModal as me } from "/@/components/Modal";
import { ExceptionEnum as de } from "/@/enums/exceptionEnum";
import { _ as fe } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const pe = /* @__PURE__ */ ee({
  __name: "PhoneLoginForm",
  props: { bindThirdAccount: { type: Boolean, default: !1 } },
  emits: ["login", "login-success", "bind-third-phone"],
  setup(M, { emit: T }) {
    const { createMessage: d } = K(), { t } = re(), s = r(""), p = r(!1), g = r();
    A({
      randCodeImage: "",
      requestCodeSuccess: !1,
      checkKey: -1
    });
    const n = A({
      mobile: "",
      sms: ""
    }), i = ie(), u = T;
    r(!1);
    const q = c(() => n.mobile != "" || o(s) === "mobile" ? "current-active" : ""), $ = c(() => n.sms != "" || o(s) === "sms" ? "current-active" : ""), { notification: z, createErrorModal: ve } = K(), x = r(!0), f = r(60), b = r(null), E = r(), S = r(), G = c(() => P(t("sys.login.accountPlaceholder"))), W = c(() => P(t("sys.login.smsPlaceholder"))), j = c(() => ({
      mobile: o(G),
      sms: o(W)
    })), L = c(() => t("component.countdown.normalText")), w = c(() => t("component.countdown.sendText", [o(f)])), F = M, [J, { openModal: Q }] = me();
    function P(l) {
      return [
        {
          required: !0,
          message: l,
          trigger: "change"
        }
      ];
    }
    function B(l) {
      s.value = l, l === "sms" ? E.value.focus() : S.value.focus();
    }
    function N() {
      s.value = "";
    }
    function O() {
      return y(this, null, function* () {
        g.value.validateFields().then((l) => y(this, null, function* () {
          if (F.bindThirdAccount)
            u("bind-third-phone", l);
          else
            try {
              p.value = !0;
              const { userInfo: e } = yield i.phoneLogin(
                se({
                  mobile: l.mobile,
                  captcha: l.sms,
                  mode: "none",
                  //不要默认的错误提示
                  goHome: !1
                })
              );
              e && u("login-success", e.realname);
            } catch (e) {
              z.error({
                message: t("sys.api.errorTip"),
                description: e.message || t("sys.api.networkExceptionMsg"),
                duration: 3
              });
            } finally {
              p.value = !1;
            }
        }));
      });
    }
    function U() {
      return y(this, null, function* () {
        if (!n.mobile) {
          d.warn(t("sys.login.mobilePlaceholder"));
          return;
        }
        (yield ue({ mobile: n.mobile, smsmode: ae.FORGET_PASSWORD }).catch((e) => {
          e.code === de.PHONE_SMS_FAIL_CODE && Q(!0, {});
        })) && (o(b) || (f.value = 60, x.value = !1, b.value = setInterval(() => {
          o(f) > 0 && o(f) <= 60 ? f.value = f.value - 1 : (x.value = !0, clearInterval(o(b)), b.value = null);
        }, 1e3)));
      });
    }
    function X() {
      u("login", "accountLogin");
    }
    return (l, e) => {
      const _ = C("a-input"), D = C("a-form-item"), Y = C("a-button"), Z = C("a-form");
      return h(), V(oe, null, [
        m(Z, {
          ref_key: "loginPhoneRef",
          ref: g,
          model: n,
          rules: j.value,
          onKeyup: te(O, ["enter", "native"])
        }, {
          default: k(() => [
            v("div", {
              class: R(["content-item", q.value]),
              onClick: e[1] || (e[1] = (a) => B("mobile"))
            }, [
              m(D, { name: "mobile" }, {
                default: k(() => [
                  m(_, {
                    ref_key: "mobileRef",
                    ref: S,
                    value: n.mobile,
                    "onUpdate:value": e[0] || (e[0] = (a) => n.mobile = a),
                    style: { height: "40px" },
                    onBlur: N
                  }, null, 8, ["value"]),
                  v("div", {
                    class: R(["form-title", s.value === "mobile" ? "active-title" : ""])
                  }, I(o(t)("sys.login.mobile")), 3)
                ]),
                _: 1
              })
            ], 2),
            v("div", {
              class: R(["content-item", $.value])
            }, [
              m(D, {
                name: "sms",
                onClick: e[3] || (e[3] = (a) => B("sms"))
              }, {
                default: k(() => [
                  m(_, {
                    ref_key: "smsCodeRef",
                    ref: E,
                    maxLength: 6,
                    value: n.sms,
                    "onUpdate:value": e[2] || (e[2] = (a) => n.sms = a),
                    style: { height: "40px" },
                    onBlur: N
                  }, null, 8, ["value"]),
                  v("div", {
                    class: R(["form-title", s.value === "sms" ? "active-title" : ""])
                  }, I(o(t)("sys.login.smsCode")), 3)
                ]),
                _: 1
              }),
              x.value ? (h(), H(_, {
                key: 0,
                type: "button",
                class: "aui-code-line pointer",
                bordered: !1,
                onClick: U,
                value: L.value,
                "onUpdate:value": e[4] || (e[4] = (a) => L.value = a)
              }, null, 8, ["value"])) : (h(), H(_, {
                key: 1,
                type: "button",
                class: "aui-code-line disabled-btn",
                bordered: !1,
                value: w.value,
                "onUpdate:value": e[5] || (e[5] = (a) => w.value = a)
              }, null, 8, ["value"]))
            ], 2),
            v("div", null, [
              m(Y, {
                type: "primary",
                onClick: O,
                loading: p.value,
                class: "login-btn"
              }, {
                default: k(() => [
                  ne(I(o(t)("sys.login.loginButton")), 1)
                ]),
                _: 1
              }, 8, ["loading"])
            ]),
            F.bindThirdAccount ? le("", !0) : (h(), V("div", {
              key: 0,
              class: "phone-login-btn pointer",
              onClick: X
            }, I(o(t)("sys.login.backSignIn")), 1))
          ]),
          _: 1
        }, 8, ["model", "rules"]),
        m(ce, {
          onRegister: o(J),
          onOk: U
        }, null, 8, ["onRegister"])
      ], 64);
    };
  }
});
const Se = /* @__PURE__ */ fe(pe, [["__scopeId", "data-v-471134ff"]]);
export {
  Se as default
};
