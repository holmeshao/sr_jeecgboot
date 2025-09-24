var L = (P, x, u) => new Promise((a, d) => {
  var s = (c) => {
    try {
      g(u.next(c));
    } catch (l) {
      d(l);
    }
  }, w = (c) => {
    try {
      g(u.throw(c));
    } catch (l) {
      d(l);
    }
  }, g = (c) => c.done ? a(c.value) : Promise.resolve(c.value).then(s, w);
  g((u = u.apply(P, x)).next());
});
import { defineComponent as Q, ref as r, reactive as X, onMounted as Y, resolveComponent as T, openBlock as S, createElementBlock as F, Fragment as Z, createElementVNode as e, createVNode as t, toDisplayString as R, unref as p, withDirectives as m, vShow as f, createCommentVNode as U, withCtx as v, withModifiers as oo, createTextVNode as eo, toRaw as no, nextTick as to } from "vue";
import io from "./AppLoginHeader-4432c584.mjs";
import lo from "./AppRegister-79a62c59.mjs";
import ro from "./AccountLoginForm-84cbd344.mjs";
import so from "./PhoneLoginForm-86437a9d.mjs";
import co from "./AppForgetPassword-52443f23.mjs";
import { useI18n as ao } from "/@/hooks/web/useI18n";
import { createFromIconfontCN as uo, WechatFilled as go } from "@ant-design/icons-vue";
import { useMessage as po } from "/@/hooks/web/useMessage";
import { useUserStore as mo } from "/@/store/modules/user";
import fo from "./AppTenant-f0bef7ca.mjs";
import { router as V } from "/@/router";
import vo from "./AppThirdForm-fd8b4604.mjs";
import { useLocale as _o } from "/@/locales/useLocale";
import { PageEnum as ho } from "/@/enums/pageEnum";
import { _ as yo } from "./index-9e1e1e53.mjs";
import "/@/assets/images/logo.png";
import "/@/api/sys/user";
import "/@/views/sys/login/useLogin";
import "./AppNameEmail-3303a037.mjs";
import "/@/components/jeecg/captcha/CaptchaModal.vue";
import "/@/components/Modal";
import "/@/enums/exceptionEnum";
import "/@/assets/images/checkcode.png";
import "/@/utils/http/axios";
import "ant-design-vue";
import "/@/components/CountDown";
import "/@/hooks/setting";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const Lo = {
  key: 0,
  class: "login-box"
}, To = { class: "login-subject" }, wo = { class: "flex-row align-items-center margin-top10" }, Ao = { class: "login-title" }, bo = { class: "content-box" }, ko = {
  key: 0,
  class: "text-center"
}, Co = { class: "login-other" }, Ro = { style: { width: "100%", display: "flex" } }, xo = { class: "aui-third-login" }, Io = { class: "login-language" }, So = /* @__PURE__ */ Q({
  __name: "AppLogin",
  setup(P) {
    const { changeLocale: x, getLocale: u } = _o(), a = r(), d = mo(), s = r("accountLogin");
    uo({
      scriptUrl: "//at.alicdn.com/t/font_2316098_umqusozousr.js"
    });
    const w = r(), g = r(), c = r(), { t: l } = ao(), i = r("login");
    r(), X({
      phone: "",
      smscode: ""
    });
    const { notification: D, createErrorModal: Fo } = po(), A = r("zh_CN"), b = r(!1), _ = r(!1);
    function H() {
      i.value = "register", setTimeout(() => {
        w.value.clearValidate();
      }, 300);
    }
    function E() {
      i.value = "login", s.value = "accountLogin";
    }
    function N(o) {
      i.value = "login", s.value = "accountLogin", setTimeout(() => {
        g.value.setAccountData(o);
      }, 300);
    }
    function B(o) {
      s.value = o;
    }
    function $() {
      i.value = "forgetPwd";
    }
    function I(o) {
      return L(this, null, function* () {
        var h, y;
        const n = no(d.getLoginInfo), { tenantList: k } = n;
        if (!k || k.length === 0)
          i.value = "tenant";
        else {
          D.success({
            message: l("sys.login.loginSuccessTitle"),
            description: `${l("sys.login.loginSuccessDesc")}: ${o}`,
            duration: 3
          });
          let C = (y = (h = V.currentRoute.value) == null ? void 0 : h.query) == null ? void 0 : y.redirect;
          if (C) {
            window.open(decodeURIComponent(C), "_self");
            return;
          }
          yield V.replace(d.getUserInfo && d.getUserInfo.homePath || ho.BASE_HOME), M();
        }
      });
    }
    function M() {
      window.location.reload();
    }
    function j(o) {
      a.value.onThirdLogin(o);
    }
    function q(o) {
      return L(this, null, function* () {
        yield x(o);
      });
    }
    function O({ key: o }) {
      A.value = o, p(u) !== o && q(o);
    }
    function W(o) {
      to(() => {
        o.loginType != "thirdLogin" && (b.value = !0, a.value.hideBindThirdAccount()), o.loginType === "login" && (s.value = "phoneLogin"), i.value = o.loginType;
      });
    }
    function G(o) {
      return L(this, null, function* () {
        _.value = !0, yield a.value.bindThirdAccount(o), _.value = !1;
      });
    }
    function J(o) {
      return L(this, null, function* () {
        _.value = !0, yield a.value.createAccountBindThird(o), _.value = !1;
      });
    }
    return Y(() => {
      let o = document.getElementById("app");
      o.style.height = "auto";
      let n = document.getElementsByTagName("body")[0].style;
      n.backgroundColor = "#f2f5f7";
    }), (o, n) => {
      const k = T("a-spin"), h = T("Icon"), y = T("a-menu-item"), C = T("a-menu"), K = T("a-dropdown");
      return S(), F(Z, null, [
        i.value === "login" ? (S(), F("div", Lo, [
          e("div", To, [
            t(io),
            e("div", wo, [
              e("div", Ao, R(s.value === "accountLogin" ? p(l)("sys.login.signInFormTitle") : p(l)("sys.login.mobileSignInFormTitle")), 1)
            ]),
            e("div", bo, [
              m(e("div", null, [
                t(ro, {
                  ref_key: "accountLoginRef",
                  ref: g,
                  onLogin: B,
                  onForgetPwd: $,
                  onLoginSuccess: I
                }, null, 512)
              ], 512), [
                [f, s.value === "accountLogin"]
              ]),
              m(e("div", null, [
                t(so, {
                  onLogin: B,
                  onLoginSuccess: I,
                  bindThirdAccount: b.value,
                  onBindThirdPhone: G
                }, null, 8, ["bindThirdAccount"])
              ], 512), [
                [f, s.value === "phoneLogin"]
              ]),
              b.value ? U("", !0) : (S(), F("div", ko, [
                e("div", Co, R(p(l)("sys.login.otherSignIn")), 1),
                e("div", Ro, [
                  e("div", xo, [
                    e("a", {
                      title: "微信",
                      onClick: n[0] || (n[0] = (z) => j("wechat_open"))
                    }, [
                      t(p(go), { style: { color: "rgb(75, 176, 79)" } })
                    ])
                  ])
                ]),
                n[3] || (n[3] = e("div", { class: "line" }, null, -1)),
                e("div", {
                  class: "register-account pointer",
                  onClick: H
                }, R(p(l)("sys.login.registerButton")), 1)
              ]))
            ])
          ])
        ])) : U("", !0),
        m(e("div", null, [
          t(k, { spinning: _.value }, {
            default: v(() => [
              t(lo, {
                ref_key: "appRegisterRef",
                ref: w,
                onReturnLogin: E,
                bindThirdAccount: b.value,
                onLoginAccount: N,
                onBindThirdAccount: J
              }, null, 8, ["bindThirdAccount"])
            ]),
            _: 1
          }, 8, ["spinning"])
        ], 512), [
          [f, i.value === "register"]
        ]),
        m(e("div", null, [
          t(co, {
            ref_key: "appForgetPwdRef",
            ref: c,
            onReturnLogin: E,
            onLoginAccount: N
          }, null, 512)
        ], 512), [
          [f, i.value === "forgetPwd"]
        ]),
        m(e("div", null, [
          t(fo, { onSuccess: M })
        ], 512), [
          [f, i.value === "tenant"]
        ]),
        m(e("div", Io, [
          t(h, {
            icon: "ant-design:global-outlined",
            style: { "font-size": "13px", color: "#9e9e9e" }
          }),
          t(K, {
            trigger: ["click"],
            placement: "top"
          }, {
            overlay: v(() => [
              t(C, {
                value: A.value,
                "onUpdate:value": n[2] || (n[2] = (z) => A.value = z),
                onClick: O
              }, {
                default: v(() => [
                  t(y, { key: "zh_CN" }, {
                    default: v(() => n[4] || (n[4] = [
                      e("span", null, "简体中文", -1)
                    ])),
                    _: 1
                  }),
                  t(y, { key: "en" }, {
                    default: v(() => n[5] || (n[5] = [
                      e("span", null, "English", -1)
                    ])),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["value"])
            ]),
            default: v(() => [
              e("span", {
                class: "language-drop pointer",
                onClick: n[1] || (n[1] = oo(() => {
                }, ["prevent"]))
              }, [
                eo(R(A.value === "zh_CN" ? "CN" : "EN") + " ", 1),
                t(h, {
                  icon: "ant-design:down-outlined",
                  style: { color: "#9e9e9e", "font-size": "13px" }
                })
              ])
            ]),
            _: 1
          })
        ], 512), [
          [f, i.value !== "thirdLogin"]
        ]),
        t(vo, {
          ref_key: "thirdModalRef",
          ref: a,
          onType: W,
          onLoginSuccess: I
        }, null, 512)
      ], 64);
    };
  }
});
const ae = /* @__PURE__ */ yo(So, [["__scopeId", "data-v-1103e34e"]]);
export {
  ae as default
};
