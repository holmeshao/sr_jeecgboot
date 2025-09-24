var O = Object.defineProperty;
var F = Object.getOwnPropertySymbols;
var R = Object.prototype.hasOwnProperty, q = Object.prototype.propertyIsEnumerable;
var E = (t, e, n) => e in t ? O(t, e, { enumerable: !0, configurable: !0, writable: !0, value: n }) : t[e] = n, _ = (t, e) => {
  for (var n in e || (e = {}))
    R.call(e, n) && E(t, n, e[n]);
  if (F)
    for (var n of F(e))
      q.call(e, n) && E(t, n, e[n]);
  return t;
};
var B = (t, e, n) => new Promise((m, g) => {
  var h = (s) => {
    try {
      f(n.next(s));
    } catch (r) {
      g(r);
    }
  }, d = (s) => {
    try {
      f(n.throw(s));
    } catch (r) {
      g(r);
    }
  }, f = (s) => s.done ? m(s.value) : Promise.resolve(s.value).then(h, d);
  f((n = n.apply(t, e)).next());
});
import { ref as c, unref as a, defineComponent as z, resolveComponent as G, withDirectives as Q, openBlock as J, createElementBlock as K, createElementVNode as p, createVNode as k, withCtx as L, createTextVNode as I, vShow as W } from "vue";
import { Form as $, Input as X } from "ant-design-vue";
import { CountdownInput as Y } from "/@/components/CountDown";
import { defHttp as D } from "/@/utils/http/axios";
import { useGlobSetting as Z } from "/@/hooks/setting";
import { useMessage as ee } from "/@/hooks/web/useMessage";
import { useUserStore as ne } from "/@/store/modules/user";
import { useI18n as te } from "/@/hooks/web/useI18n";
import { QuestionCircleFilled as oe } from "@ant-design/icons-vue";
import { _ as ie } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
function se(t) {
  const { createMessage: e, notification: n } = ee(), { t: m } = te(), g = Z(), h = ne(), d = c(""), f = c({}), s = c(!1), r = c(!1), b = c(""), T = c(!1), v = c(""), y = c("");
  function S(o) {
    let i = `${g.uploadUrl}/sys/thirdLogin/render/${o}`;
    window.open(
      i,
      `login ${o}`,
      "height=500, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no"
    ), d.value = o, f.value = {}, s.value = !1;
    let l = function(j) {
      let u = j.data;
      if (typeof u == "string")
        if (u === "登录失败")
          e.warning(u);
        else if (u.includes("绑定手机号")) {
          r.value = !0;
          let H = u.split(",");
          b.value = H[1], t("type", { loginType: "thirdLogin" });
        } else
          w(u);
      else
        typeof u == "object" ? u.isObj === !0 && (T.value = !0, f.value = _({}, u)) : e.warning("不识别的信息传递");
      window.removeEventListener("message", a(l), !1);
    };
    window.addEventListener("message", l, !1);
  }
  function w(o) {
    a(s) === !1 && (s.value = !0, h.ThirdLogin({ token: o, thirdType: a(d) }).then((i) => {
      i && i.userInfo ? n.success({
        message: m("sys.login.loginSuccessTitle"),
        description: `${m("sys.login.loginSuccessDesc")}: ${i.userInfo.realname}`,
        duration: 3
      }) : U(i);
    }));
  }
  function U(o) {
    n.error({
      message: "登录失败",
      description: ((o.response || {}).data || {}).message || o.message || "请求出现错误，请稍后再试",
      duration: 4
    });
  }
  function A() {
    a(v) || C("请输入手机号"), a(y) || C("请输入验证码");
    let o = {
      mobile: a(v),
      captcha: a(y),
      thirdUserUuid: a(b)
    };
    D.post({ url: "/sys/thirdLogin/bindingThirdPhone", params: o }, { isTransformResponse: !1 }).then((i) => {
      i.success ? (r.value = !1, w(i.result)) : e.warning(i.message);
    }).catch((i) => {
      e.warning(i.message);
    });
  }
  function C(o) {
    n.error({
      message: "登录失败",
      description: o,
      duration: 4
    });
  }
  function x() {
    t("type", { loginType: "login" });
  }
  function P() {
    t("type", { loginType: "register" });
  }
  function M() {
    r.value = !1;
  }
  function N(o) {
    return B(this, null, function* () {
      let i = _({}, o);
      i.thirdUserUuid = a(b), yield D.put({ url: "/sys/thirdLogin/registerBindThirdAccount", params: i }, { isTransformResponse: !1 }).then((l) => {
        l.success ? (r.value = !1, w(l.result)) : e.warning(l.message);
      }).catch((l) => {
        e.warning(l.message);
      });
    });
  }
  function V(o) {
    v.value = o.mobile, y.value = o.sms, A();
  }
  return {
    thirdConfirmShow: T,
    bindingAccount: r,
    thirdHandleOk: A,
    thirdPhone: v,
    thirdCaptcha: y,
    onThirdLogin: S,
    loginAccountClick: x,
    registerAccountClick: P,
    hideBindThirdAccount: M,
    bindThirdAccount: V,
    createAccountBindThird: N
  };
}
const re = $.Item, ue = X.Password, ae = z({
  name: "AppThirdForm",
  components: { FormItem: re, Form: $, InputPassword: ue, CountdownInput: Y, QuestionCircleFilled: oe },
  setup(t, { emit: e }) {
    return _({}, se(e));
  }
});
const le = { class: "third-account" }, ce = { class: "content" }, de = { class: "enter-x bind-btn" }, pe = {
  class: "enter-x bind-btn",
  style: { "margin-top": "20px" }
};
function fe(t, e, n, m, g, h) {
  const d = G("a-button");
  return Q((J(), K("div", le, [
    p("div", ce, [
      e[2] || (e[2] = p("div", { class: "bind-title" }, [
        p("span", null, "还未绑定敲敲云账号")
      ], -1)),
      e[3] || (e[3] = p("div", { class: "bind-title-desc" }, [
        p("span", null, "请选择绑定已有帐户，或创建新帐号")
      ], -1)),
      p("div", de, [
        k(d, {
          type: "primary",
          onClick: t.loginAccountClick
        }, {
          default: L(() => e[0] || (e[0] = [
            I("登录并绑定")
          ])),
          _: 1
        }, 8, ["onClick"])
      ]),
      p("div", pe, [
        k(d, {
          type: "primary",
          onClick: t.registerAccountClick
        }, {
          default: L(() => e[1] || (e[1] = [
            I("注册新账号")
          ])),
          _: 1
        }, 8, ["onClick"])
      ])
    ])
  ], 512)), [
    [W, t.bindingAccount]
  ]);
}
const Be = /* @__PURE__ */ ie(ae, [["render", fe], ["__scopeId", "data-v-c98a927f"]]);
export {
  Be as default
};
