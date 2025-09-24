import { ref as D, onMounted as Yt, defineComponent as Kt, resolveComponent as S, openBlock as tt, createElementBlock as ut, Fragment as Xt, createElementVNode as pt, normalizeClass as q, createVNode as v, mergeProps as Qt, withCtx as j, createTextVNode as O, createBlock as Ut, createCommentVNode as dt } from "vue";
import { BasicTable as Zt, TableAction as te } from "/@/components/Table";
import { C as ee } from "./CgformModal-c4a4e0c2.mjs";
import ne from "./DbToOnlineModal-f28ff0a3.mjs";
import oe from "./CodeGeneratorModal-2e1dc13d.mjs";
import ie from "./CustomButtonList-c453b654.mjs";
import re from "./EnhanceJsModal-dc4f9ade.mjs";
import se from "./EnhanceJavaModal-d5a93f2a.mjs";
import ae from "./EnhanceSqlModal-984f045d.mjs";
import le from "./AuthManagerDrawer-32556109.mjs";
import ce from "./AuthSetterModal-364f1f67.mjs";
import ue from "./AiModal-26903538.mjs";
import pe from "./CgformAddressModal-1131274c.mjs";
import { u as de, C as he } from "./useCgformList-f3cb9156.mjs";
import { c as me, s as fe } from "./cgform.data-0ca62d09.mjs";
import { useDesign as ge } from "/@/hooks/web/useDesign";
import { _ as be } from "./index-9e1e1e53.mjs";
import "/@/components/Icon";
import "/@/components/Modal";
import "/@/components/Form/index";
import "/@/hooks/web/useMessage";
import "./useSchemas-b074f3a1.mjs";
import "ant-design-vue";
import "@ant-design/icons-vue";
import "/@/utils/common/compUtils";
import "/@/hooks/web/usePermission";
import "/@/utils/helper/validator";
import "./DBAttributeTable-1a45c7b7.mjs";
import "/@/components/jeecg/JVxeTable/types";
import "./useTableSync-075826a1.mjs";
import "lodash-es";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
import "./PageAttributeTable-66e7b485.mjs";
import "./LinkTableConfigModal-7eeb3e58.mjs";
import "/@/utils/http/axios";
import "./LinkTableFieldConfigModal-b078fcef.mjs";
import "./FieldExtendJsonModal-bf04d70e.mjs";
import "./SetSwitchOptions-f914bc17.mjs";
import "/@/utils/is";
import "./constant-fa63bd66.mjs";
import "./CheckDictTable-8a938e3a.mjs";
import "/@/components/jeecg/JPrompt";
import "./ForeignKeyTable-92decaea.mjs";
import "./IndexTable-2ded2014.mjs";
import "./QueryTable-65d3f54f.mjs";
import "./ExtendConfigModal-7d70f362.mjs";
import "/@/components/Form";
import "./useOnlineTest-e4bd8be3.mjs";
import "/@/utils";
import "./useExtendComponent-bb98e568.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/hooks/system/useListPage";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/utils/auth";
import "/@/api/common/api";
import "/@/hooks/web/useAppInject";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "/@/components/Loading";
import "./JModalTip-a927f85d.mjs";
import "@vueuse/core";
import "./utils-9fce7606.mjs";
import "./CodeFileListModal-a924902a.mjs";
import "./CodeFileViewModal-405e2b58.mjs";
import "/@/utils/file/download";
import "./FileSelectModal-ffc69d4a.mjs";
import "./BuiltInButtonList.vue_vue_type_script_setup_true_lang-07d0b7d0.mjs";
import "./EnhanceJsHistory-8ddb0657.mjs";
import "/@/utils/dateUtil";
import "/@/store";
import "pinia";
import "/@/utils/cache";
import "./enhance.api-138e6826.mjs";
import "./enhance.data-6601ff44.mjs";
import "/@/components/Drawer";
import "./AuthFieldConfig-f1e224cc.mjs";
import "./auth.api-53df4c33.mjs";
import "./auth.data-626c5083.mjs";
import "/@/utils/index";
import "./AuthButtonConfig-d5bffca0.mjs";
import "./AuthDataConfig-d3b7afa4.mjs";
import "./LeftRole-b0e0b496.mjs";
import "./LeftDepart-52cb6743.mjs";
import "./LeftUser-dd4b10e2.mjs";
import "./AuthFieldTree-5cc0da05.mjs";
import "./AuthButtonTree-b0bd6c40.mjs";
import "./AuthDataTree-f14a98d9.mjs";
import "/@/hooks/web/useCopyToClipboard";
import "./cgformState-d9f8ec42.mjs";
/*!
 * Intro.js v7.2.0
 * https://introjs.com
 *
 * Copyright (C) 2012-2023 Afshin Mehrabani (@afshinmeh).
 * https://introjs.com
 *
 * Date: Mon, 14 Aug 2023 19:47:14 GMT
 */
function it(t) {
  return it = typeof Symbol == "function" && typeof Symbol.iterator == "symbol" ? function(e) {
    return typeof e;
  } : function(e) {
    return e && typeof Symbol == "function" && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e;
  }, it(t);
}
function f(t, e, n, o) {
  return new (n || (n = Promise))(function(i, r) {
    function s(p) {
      try {
        c(o.next(p));
      } catch (l) {
        r(l);
      }
    }
    function a(p) {
      try {
        c(o.throw(p));
      } catch (l) {
        r(l);
      }
    }
    function c(p) {
      var l;
      p.done ? i(p.value) : (l = p.value, l instanceof n ? l : new n(function(u) {
        u(l);
      })).then(s, a);
    }
    c((o = o.apply(t, e || [])).next());
  });
}
function g(t, e) {
  var n, o, i, r, s = { label: 0, sent: function() {
    if (1 & i[0])
      throw i[1];
    return i[1];
  }, trys: [], ops: [] };
  return r = { next: a(0), throw: a(1), return: a(2) }, typeof Symbol == "function" && (r[Symbol.iterator] = function() {
    return this;
  }), r;
  function a(c) {
    return function(p) {
      return function(l) {
        if (n)
          throw new TypeError("Generator is already executing.");
        for (; r && (r = 0, l[0] && (s = 0)), s; )
          try {
            if (n = 1, o && (i = 2 & l[0] ? o.return : l[0] ? o.throw || ((i = o.return) && i.call(o), 0) : o.next) && !(i = i.call(o, l[1])).done)
              return i;
            switch (o = 0, i && (l = [2 & l[0], i.value]), l[0]) {
              case 0:
              case 1:
                i = l;
                break;
              case 4:
                return s.label++, { value: l[1], done: !1 };
              case 5:
                s.label++, o = l[1], l = [0];
                continue;
              case 7:
                l = s.ops.pop(), s.trys.pop();
                continue;
              default:
                if (i = s.trys, !((i = i.length > 0 && i[i.length - 1]) || l[0] !== 6 && l[0] !== 2)) {
                  s = 0;
                  continue;
                }
                if (l[0] === 3 && (!i || l[1] > i[0] && l[1] < i[3])) {
                  s.label = l[1];
                  break;
                }
                if (l[0] === 6 && s.label < i[1]) {
                  s.label = i[1], i = l;
                  break;
                }
                if (i && s.label < i[2]) {
                  s.label = i[2], s.ops.push(l);
                  break;
                }
                i[2] && s.ops.pop(), s.trys.pop();
                continue;
            }
            l = e.call(t, s);
          } catch (u) {
            l = [6, u], o = 0;
          } finally {
            n = i = 0;
          }
        if (5 & l[0])
          throw l[1];
        return { value: l[0] ? l[1] : void 0, done: !0 };
      }([c, p]);
    };
  }
}
function _t(t, e, n) {
  var o, i = ((o = {})[t] = e, o.path = "/", o.expires = void 0, o);
  if (n) {
    var r = /* @__PURE__ */ new Date();
    r.setTime(r.getTime() + 24 * n * 60 * 60 * 1e3), i.expires = r.toUTCString();
  }
  var s = [];
  for (var a in i)
    s.push("".concat(a, "=").concat(i[a]));
  return document.cookie = s.join("; "), Et(t);
}
function Et(t) {
  return (e = {}, document.cookie.split(";").forEach(function(n) {
    var o = n.split("="), i = o[0], r = o[1];
    e[i.trim()] = r;
  }), e)[t];
  var e;
}
function ve(t, e) {
  e ? _t(t._options.dontShowAgainCookie, "true", t._options.dontShowAgainCookieDays) : _t(t._options.dontShowAgainCookie, "", -1);
}
var et, mt = (et = {}, function(t, e) {
  return e === void 0 && (e = "introjs-stamp"), et[e] = et[e] || 0, t[e] === void 0 && (t[e] = et[e]++), t[e];
}), M = new (function() {
  function t() {
    this.events_key = "introjs_event";
  }
  return t.prototype._id = function(e, n, o) {
    return e + mt(n) + (o ? "_".concat(mt(o)) : "");
  }, t.prototype.on = function(e, n, o, i, r) {
    var s = this._id(n, o, i), a = function(c) {
      return o(i || e, c || window.event);
    };
    "addEventListener" in e ? e.addEventListener(n, a, r) : "attachEvent" in e && e.attachEvent("on".concat(n), a), e[this.events_key] = e[this.events_key] || {}, e[this.events_key][s] = a;
  }, t.prototype.off = function(e, n, o, i, r) {
    var s = this._id(n, o, i), a = e[this.events_key] && e[this.events_key][s];
    a && ("removeEventListener" in e ? e.removeEventListener(n, a, r) : "detachEvent" in e && e.detachEvent("on".concat(n), a), e[this.events_key][s] = null);
  }, t;
}())(), w = function(t) {
  return typeof t == "function";
};
function I(t, e) {
  if (t instanceof SVGElement) {
    var n = t.getAttribute("class") || "";
    n.match(e) || t.setAttribute("class", "".concat(n, " ").concat(e));
  } else if (t.classList !== void 0)
    for (var o = 0, i = e.split(" "); o < i.length; o++) {
      var r = i[o];
      t.classList.add(r);
    }
  else
    t.className.match(e) || (t.className += " ".concat(e));
}
function gt(t, e) {
  var n = "";
  return "currentStyle" in t ? n = t.currentStyle[e] : document.defaultView && document.defaultView.getComputedStyle && (n = document.defaultView.getComputedStyle(t, null).getPropertyValue(e)), n && n.toLowerCase ? n.toLowerCase() : n;
}
function Ct(t, e) {
  if (t) {
    var n = function(o) {
      var i = window.getComputedStyle(o), r = i.position === "absolute", s = /(auto|scroll)/;
      if (i.position === "fixed")
        return document.body;
      for (var a = o; a = a.parentElement; )
        if (i = window.getComputedStyle(a), (!r || i.position !== "static") && s.test(i.overflow + i.overflowY + i.overflowX))
          return a;
      return document.body;
    }(e);
    n !== document.body && (n.scrollTop = e.offsetTop - n.offsetTop);
  }
}
function bt() {
  if (window.innerWidth !== void 0)
    return { width: window.innerWidth, height: window.innerHeight };
  var t = document.documentElement;
  return { width: t.clientWidth, height: t.clientHeight };
}
function St(t, e, n, o, i) {
  var r;
  if (e !== "off" && t && (r = e === "tooltip" ? i.getBoundingClientRect() : o.getBoundingClientRect(), !function(a) {
    var c = a.getBoundingClientRect();
    return c.top >= 0 && c.left >= 0 && c.bottom + 80 <= window.innerHeight && c.right <= window.innerWidth;
  }(o))) {
    var s = bt().height;
    r.bottom - (r.bottom - r.top) < 0 || o.clientHeight > s ? window.scrollBy(0, r.top - (s / 2 - r.height / 2) - n) : window.scrollBy(0, r.top - (s / 2 - r.height / 2) + n);
  }
}
function U(t) {
  t.setAttribute("role", "button"), t.tabIndex = 0;
}
function at(t) {
  var e = t.parentElement;
  return !(!e || e.nodeName === "HTML") && (gt(t, "position") === "fixed" || at(e));
}
function Y(t, e) {
  var n = document.body, o = document.documentElement, i = window.pageYOffset || o.scrollTop || n.scrollTop, r = window.pageXOffset || o.scrollLeft || n.scrollLeft;
  e = e || n;
  var s = t.getBoundingClientRect(), a = e.getBoundingClientRect(), c = gt(e, "position"), p = { width: s.width, height: s.height };
  return e.tagName.toLowerCase() !== "body" && c === "relative" || c === "sticky" ? Object.assign(p, { top: s.top - a.top, left: s.left - a.left }) : at(t) ? Object.assign(p, { top: s.top, left: s.left }) : Object.assign(p, { top: s.top + i, left: s.left + r });
}
function vt(t, e) {
  if (t instanceof SVGElement) {
    var n = t.getAttribute("class") || "";
    t.setAttribute("class", n.replace(e, "").replace(/^\s+|\s+$/g, ""));
  } else
    t.className = t.className.replace(e, "").replace(/^\s+|\s+$/g, "");
}
function H(t, e) {
  var n = "";
  if (t.style.cssText && (n += t.style.cssText), typeof e == "string")
    n += e;
  else
    for (var o in e)
      n += "".concat(o, ":").concat(e[o], ";");
  t.style.cssText = n;
}
function R(t, e, n) {
  if (n && e) {
    var o = Y(e.element, t._targetElement), i = t._options.helperElementPadding;
    e.element instanceof Element && at(e.element) ? I(n, "introjs-fixedTooltip") : vt(n, "introjs-fixedTooltip"), e.position === "floating" && (i = 0), H(n, { width: "".concat(o.width + i, "px"), height: "".concat(o.height + i, "px"), top: "".concat(o.top - i / 2, "px"), left: "".concat(o.left - i / 2, "px") });
  }
}
function nt(t, e, n, o, i) {
  return t.left + e + n.width > o.width ? (i.style.left = "".concat(o.width - n.width - t.left, "px"), !1) : (i.style.left = "".concat(e, "px"), !0);
}
function ot(t, e, n, o) {
  return t.left + t.width - e - n.width < 0 ? (o.style.left = "".concat(-t.left, "px"), !1) : (o.style.right = "".concat(e, "px"), !0);
}
function B(t, e) {
  t.includes(e) && t.splice(t.indexOf(e), 1);
}
function ye(t, e, n, o) {
  var i = t.slice(), r = bt(), s = Y(n).height + 10, a = Y(n).width + 20, c = e.getBoundingClientRect(), p = "floating";
  if (c.bottom + s > r.height && B(i, "bottom"), c.top - s < 0 && B(i, "top"), c.right + a > r.width && B(i, "right"), c.left - a < 0 && B(i, "left"), o && (o = o.split("-")[0]), i.length && (p = i[0], i.includes(o) && (p = o)), p === "top" || p === "bottom") {
    var l = void 0, u = [];
    p === "top" ? (l = "top-middle-aligned", u = ["top-left-aligned", "top-middle-aligned", "top-right-aligned"]) : (l = "bottom-middle-aligned", u = ["bottom-left-aligned", "bottom-middle-aligned", "bottom-right-aligned"]), p = function(m, b, y, C) {
      var _ = b / 2, E = Math.min(y, window.screen.width);
      return E - m < b && (B(C, "top-left-aligned"), B(C, "bottom-left-aligned")), (m < _ || E - m < _) && (B(C, "top-middle-aligned"), B(C, "bottom-middle-aligned")), m < b && (B(C, "top-right-aligned"), B(C, "bottom-right-aligned")), C.length ? C[0] : null;
    }(c.left, a, r.width, u) || l;
  }
  return p;
}
function rt(t, e, n, o, i) {
  if (i === void 0 && (i = !1), e) {
    var r, s, a, c, p = "";
    n.style.top = "", n.style.right = "", n.style.bottom = "", n.style.left = "", n.style.marginLeft = "", n.style.marginTop = "", o.style.display = "inherit", p = typeof e.tooltipClass == "string" ? e.tooltipClass : t._options.tooltipClass, n.className = ["introjs-tooltip", p].filter(Boolean).join(" "), n.setAttribute("role", "dialog"), (c = e.position) !== "floating" && t._options.autoPosition && (c = ye(t._options.positionPrecedence, e.element, n, c)), s = Y(e.element), r = Y(n), a = bt(), I(n, "introjs-".concat(c));
    var l = s.width / 2 - r.width / 2;
    switch (c) {
      case "top-right-aligned":
        o.className = "introjs-arrow bottom-right";
        var u = 0;
        ot(s, u, r, n), n.style.bottom = "".concat(s.height + 20, "px");
        break;
      case "top-middle-aligned":
        o.className = "introjs-arrow bottom-middle", i && (l += 5), ot(s, l, r, n) && (n.style.right = "", nt(s, l, r, a, n)), n.style.bottom = "".concat(s.height + 20, "px");
        break;
      case "top-left-aligned":
      case "top":
        o.className = "introjs-arrow bottom", nt(s, i ? 0 : 15, r, a, n), n.style.bottom = "".concat(s.height + 20, "px");
        break;
      case "right":
        n.style.left = "".concat(s.width + 20, "px"), s.top + r.height > a.height ? (o.className = "introjs-arrow left-bottom", n.style.top = "-".concat(r.height - s.height - 20, "px")) : o.className = "introjs-arrow left";
        break;
      case "left":
        i || t._options.showStepNumbers !== !0 || (n.style.top = "15px"), s.top + r.height > a.height ? (n.style.top = "-".concat(r.height - s.height - 20, "px"), o.className = "introjs-arrow right-bottom") : o.className = "introjs-arrow right", n.style.right = "".concat(s.width + 20, "px");
        break;
      case "floating":
        o.style.display = "none", n.style.left = "50%", n.style.top = "50%", n.style.marginLeft = "-".concat(r.width / 2, "px"), n.style.marginTop = "-".concat(r.height / 2, "px");
        break;
      case "bottom-right-aligned":
        o.className = "introjs-arrow top-right", ot(s, u = 0, r, n), n.style.top = "".concat(s.height + 20, "px");
        break;
      case "bottom-middle-aligned":
        o.className = "introjs-arrow top-middle", i && (l += 5), ot(s, l, r, n) && (n.style.right = "", nt(s, l, r, a, n)), n.style.top = "".concat(s.height + 20, "px");
        break;
      default:
        o.className = "introjs-arrow top", nt(s, 0, r, a, n), n.style.top = "".concat(s.height + 20, "px");
    }
  }
}
function Tt() {
  for (var t = 0, e = Array.from(document.querySelectorAll(".introjs-showElement")); t < e.length; t++)
    vt(e[t], /introjs-[a-zA-Z]+/g);
}
function h(t, e) {
  var n = document.createElement(t);
  e = e || {};
  var o = /^(?:role|data-|aria-)/;
  for (var i in e) {
    var r = e[i];
    i === "style" && typeof r != "function" ? H(n, r) : typeof r == "string" && i.match(o) ? n.setAttribute(i, r) : n[i] = r;
  }
  return n;
}
function At(t, e, n) {
  if (n === void 0 && (n = !1), n) {
    var o = e.style.opacity || "1";
    H(e, { opacity: "0" }), window.setTimeout(function() {
      H(e, { opacity: o });
    }, 10);
  }
  t.appendChild(e);
}
function Nt(t, e) {
  return (t + 1) / e * 100;
}
function xt(t, e) {
  var n = h("div", { className: "introjs-bullets" });
  t._options.showBullets === !1 && (n.style.display = "none");
  var o = h("ul");
  o.setAttribute("role", "tablist");
  for (var i = function() {
    var p = this.getAttribute("data-step-number");
    p != null && t.goToStep(parseInt(p, 10));
  }, r = 0; r < t._introItems.length; r++) {
    var s = t._introItems[r].step, a = h("li"), c = h("a");
    a.setAttribute("role", "presentation"), c.setAttribute("role", "tab"), c.onclick = i, r === e.step - 1 && (c.className = "active"), U(c), c.innerHTML = "&nbsp;", c.setAttribute("data-step-number", s.toString()), a.appendChild(c), o.appendChild(a);
  }
  return n.appendChild(o), n;
}
function Bt(t, e, n) {
  var o = t.querySelector(".introjs-progress .introjs-progressbar");
  if (o) {
    var i = Nt(e, n);
    o.style.cssText = "width:".concat(i, "%;"), o.setAttribute("aria-valuenow", i.toString());
  }
}
function It(t, e) {
  return f(this, void 0, void 0, function() {
    var n, o, i, r, s, a, c, p, l, u, m, b, y, C, _, E, N, L, x, P, V, z, T, W, X = this;
    return g(this, function(J) {
      switch (J.label) {
        case 0:
          return w(t._introChangeCallback) ? [4, t._introChangeCallback.call(t, e.element)] : [3, 2];
        case 1:
          J.sent(), J.label = 2;
        case 2:
          return n = document.querySelector(".introjs-helperLayer"), o = document.querySelector(".introjs-tooltipReferenceLayer"), i = "introjs-helperLayer", typeof e.highlightClass == "string" && (i += " ".concat(e.highlightClass)), typeof t._options.highlightClass == "string" && (i += " ".concat(t._options.highlightClass)), n !== null && o !== null ? (c = o.querySelector(".introjs-helperNumberLayer"), p = o.querySelector(".introjs-tooltiptext"), l = o.querySelector(".introjs-tooltip-title"), u = o.querySelector(".introjs-arrow"), m = o.querySelector(".introjs-tooltip"), a = o.querySelector(".introjs-skipbutton"), s = o.querySelector(".introjs-prevbutton"), r = o.querySelector(".introjs-nextbutton"), n.className = i, m.style.opacity = "0", m.style.display = "none", Ct(t._options.scrollToElement, e.element), R(t, e, n), R(t, e, o), Tt(), t._lastShowElementTimer && window.clearTimeout(t._lastShowElementTimer), t._lastShowElementTimer = window.setTimeout(function() {
            c !== null && (c.innerHTML = "".concat(e.step, " ").concat(t._options.stepNumbersOfLabel, " ").concat(t._introItems.length)), p.innerHTML = e.intro || "", l.innerHTML = e.title || "", m.style.display = "block", rt(t, e, m, u), function(d, A, k) {
              if (d) {
                var $ = A.querySelector(".introjs-bullets li > a.active"), Z = A.querySelector('.introjs-bullets li > a[data-step-number="'.concat(k.step, '"]'));
                $ && Z && ($.className = "", Z.className = "active");
              }
            }(t._options.showBullets, o, e), Bt(o, t._currentStep, t._introItems.length), m.style.opacity = "1", (r != null && /introjs-donebutton/gi.test(r.className) || r != null) && r.focus(), St(t._options.scrollToElement, e.scrollTo, t._options.scrollPadding, e.element, p);
          }, 350)) : (b = h("div", { className: i }), y = h("div", { className: "introjs-tooltipReferenceLayer" }), C = h("div", { className: "introjs-arrow" }), _ = h("div", { className: "introjs-tooltip" }), E = h("div", { className: "introjs-tooltiptext" }), N = h("div", { className: "introjs-tooltip-header" }), L = h("h1", { className: "introjs-tooltip-title" }), x = h("div"), H(b, { "box-shadow": "0 0 1px 2px rgba(33, 33, 33, 0.8), rgba(33, 33, 33, ".concat(t._options.overlayOpacity.toString(), ") 0 0 0 5000px") }), Ct(t._options.scrollToElement, e.element), R(t, e, b), R(t, e, y), At(t._targetElement, b, !0), At(t._targetElement, y), E.innerHTML = e.intro, L.innerHTML = e.title, x.className = "introjs-tooltipbuttons", t._options.showButtons === !1 && (x.style.display = "none"), N.appendChild(L), _.appendChild(N), _.appendChild(E), t._options.dontShowAgain && (P = h("div", { className: "introjs-dontShowAgain" }), (V = h("input", { type: "checkbox", id: "introjs-dontShowAgain", name: "introjs-dontShowAgain" })).onchange = function(d) {
            t.setDontShowAgain(d.target.checked);
          }, (z = h("label", { htmlFor: "introjs-dontShowAgain" })).innerText = t._options.dontShowAgainLabel, P.appendChild(V), P.appendChild(z), _.appendChild(P)), _.appendChild(xt(t, e)), _.appendChild(function(d) {
            var A = h("div");
            A.className = "introjs-progress", d._options.showProgress === !1 && (A.style.display = "none");
            var k = h("div", { className: "introjs-progressbar" });
            d._options.progressBarAdditionalClass && (k.className += " " + d._options.progressBarAdditionalClass);
            var $ = Nt(d._currentStep, d._introItems.length);
            return k.setAttribute("role", "progress"), k.setAttribute("aria-valuemin", "0"), k.setAttribute("aria-valuemax", "100"), k.setAttribute("aria-valuenow", $.toString()), k.style.cssText = "width:".concat($, "%;"), A.appendChild(k), A;
          }(t)), T = h("div"), t._options.showStepNumbers === !0 && (T.className = "introjs-helperNumberLayer", T.innerHTML = "".concat(e.step, " ").concat(t._options.stepNumbersOfLabel, " ").concat(t._introItems.length), _.appendChild(T)), _.appendChild(C), y.appendChild(_), (r = h("a")).onclick = function() {
            return f(X, void 0, void 0, function() {
              return g(this, function(d) {
                switch (d.label) {
                  case 0:
                    return t._introItems.length - 1 === t._currentStep ? [3, 2] : [4, F(t)];
                  case 1:
                    return d.sent(), [3, 6];
                  case 2:
                    return /introjs-donebutton/gi.test(r.className) ? w(t._introCompleteCallback) ? [4, t._introCompleteCallback.call(t, t._currentStep, "done")] : [3, 4] : [3, 6];
                  case 3:
                    d.sent(), d.label = 4;
                  case 4:
                    return [4, G(t, t._targetElement)];
                  case 5:
                    d.sent(), d.label = 6;
                  case 6:
                    return [2];
                }
              });
            });
          }, U(r), r.innerHTML = t._options.nextLabel, (s = h("a")).onclick = function() {
            return f(X, void 0, void 0, function() {
              return g(this, function(d) {
                switch (d.label) {
                  case 0:
                    return t._currentStep > 0 ? [4, st(t)] : [3, 2];
                  case 1:
                    d.sent(), d.label = 2;
                  case 2:
                    return [2];
                }
              });
            });
          }, U(s), s.innerHTML = t._options.prevLabel, U(a = h("a", { className: "introjs-skipbutton" })), a.innerHTML = t._options.skipLabel, a.onclick = function() {
            return f(X, void 0, void 0, function() {
              return g(this, function(d) {
                switch (d.label) {
                  case 0:
                    return t._introItems.length - 1 === t._currentStep && w(t._introCompleteCallback) ? [4, t._introCompleteCallback.call(t, t._currentStep, "skip")] : [3, 2];
                  case 1:
                    d.sent(), d.label = 2;
                  case 2:
                    return w(t._introSkipCallback) ? [4, t._introSkipCallback.call(t, t._currentStep)] : [3, 4];
                  case 3:
                    d.sent(), d.label = 4;
                  case 4:
                    return [4, G(t, t._targetElement)];
                  case 5:
                    return d.sent(), [2];
                }
              });
            });
          }, N.appendChild(a), t._introItems.length > 1 && x.appendChild(s), x.appendChild(r), _.appendChild(x), rt(t, e, _, C), St(t._options.scrollToElement, e.scrollTo, t._options.scrollPadding, e.element, _)), (W = t._targetElement.querySelector(".introjs-disableInteraction")) && W.parentNode && W.parentNode.removeChild(W), e.disableInteraction && function(d, A) {
            var k = document.querySelector(".introjs-disableInteraction");
            k === null && (k = h("div", { className: "introjs-disableInteraction" }), d._targetElement.appendChild(k)), R(d, A, k);
          }(t, e), t._currentStep === 0 && t._introItems.length > 1 ? (r != null && (r.className = "".concat(t._options.buttonClass, " introjs-nextbutton"), r.innerHTML = t._options.nextLabel), t._options.hidePrev === !0 ? (s != null && (s.className = "".concat(t._options.buttonClass, " introjs-prevbutton introjs-hidden")), r != null && I(r, "introjs-fullbutton")) : s != null && (s.className = "".concat(t._options.buttonClass, " introjs-prevbutton introjs-disabled"))) : t._introItems.length - 1 === t._currentStep || t._introItems.length === 1 ? (s != null && (s.className = "".concat(t._options.buttonClass, " introjs-prevbutton")), t._options.hideNext === !0 ? (r != null && (r.className = "".concat(t._options.buttonClass, " introjs-nextbutton introjs-hidden")), s != null && I(s, "introjs-fullbutton")) : r != null && (t._options.nextToDone === !0 ? (r.innerHTML = t._options.doneLabel, I(r, "".concat(t._options.buttonClass, " introjs-nextbutton introjs-donebutton"))) : r.className = "".concat(t._options.buttonClass, " introjs-nextbutton introjs-disabled"))) : (s != null && (s.className = "".concat(t._options.buttonClass, " introjs-prevbutton")), r != null && (r.className = "".concat(t._options.buttonClass, " introjs-nextbutton"), r.innerHTML = t._options.nextLabel)), s != null && s.setAttribute("role", "button"), r != null && r.setAttribute("role", "button"), a != null && a.setAttribute("role", "button"), r != null && r.focus(), function(d) {
            I(d, "introjs-showElement");
            var A = gt(d, "position");
            A !== "absolute" && A !== "relative" && A !== "sticky" && A !== "fixed" && I(d, "introjs-relativePosition");
          }(e.element), w(t._introAfterChangeCallback) ? [4, t._introAfterChangeCallback.call(t, e.element)] : [3, 4];
        case 3:
          J.sent(), J.label = 4;
        case 4:
          return [2];
      }
    });
  });
}
function we(t, e) {
  return f(this, void 0, void 0, function() {
    return g(this, function(n) {
      switch (n.label) {
        case 0:
          return t._currentStep = e - 2, t._introItems === void 0 ? [3, 2] : [4, F(t)];
        case 1:
          n.sent(), n.label = 2;
        case 2:
          return [2];
      }
    });
  });
}
function _e(t, e) {
  return f(this, void 0, void 0, function() {
    return g(this, function(n) {
      switch (n.label) {
        case 0:
          return t._currentStepNumber = e, t._introItems === void 0 ? [3, 2] : [4, F(t)];
        case 1:
          n.sent(), n.label = 2;
        case 2:
          return [2];
      }
    });
  });
}
function F(t) {
  return f(this, void 0, void 0, function() {
    var e, n, o;
    return g(this, function(i) {
      switch (i.label) {
        case 0:
          if (t._direction = "forward", t._currentStepNumber !== void 0)
            for (e = 0; e < t._introItems.length; e++)
              t._introItems[e].step === t._currentStepNumber && (t._currentStep = e - 1, t._currentStepNumber = void 0);
          return t._currentStep === -1 ? t._currentStep = 0 : ++t._currentStep, n = t._introItems[t._currentStep], o = !0, w(t._introBeforeChangeCallback) ? [4, t._introBeforeChangeCallback.call(t, n && n.element, t._currentStep, t._direction)] : [3, 2];
        case 1:
          o = i.sent(), i.label = 2;
        case 2:
          return o === !1 ? (--t._currentStep, [2, !1]) : t._introItems.length <= t._currentStep ? w(t._introCompleteCallback) ? [4, t._introCompleteCallback.call(t, t._currentStep, "end")] : [3, 4] : [3, 6];
        case 3:
          i.sent(), i.label = 4;
        case 4:
          return [4, G(t, t._targetElement)];
        case 5:
          return i.sent(), [2, !1];
        case 6:
          return [4, It(t, n)];
        case 7:
          return i.sent(), [2, !0];
      }
    });
  });
}
function st(t) {
  return f(this, void 0, void 0, function() {
    var e, n;
    return g(this, function(o) {
      switch (o.label) {
        case 0:
          return t._direction = "backward", t._currentStep <= 0 ? [2, !1] : (--t._currentStep, e = t._introItems[t._currentStep], n = !0, w(t._introBeforeChangeCallback) ? [4, t._introBeforeChangeCallback.call(t, e && e.element, t._currentStep, t._direction)] : [3, 2]);
        case 1:
          n = o.sent(), o.label = 2;
        case 2:
          return n === !1 ? (++t._currentStep, [2, !1]) : [4, It(t, e)];
        case 3:
          return o.sent(), [2, !0];
      }
    });
  });
}
function Mt(t, e) {
  return f(this, void 0, void 0, function() {
    var n, o;
    return g(this, function(i) {
      switch (i.label) {
        case 0:
          return (n = e.code === void 0 ? e.which : e.code) === null && (n = e.charCode === null ? e.keyCode : e.charCode), n !== "Escape" && n !== 27 || t._options.exitOnEsc !== !0 ? [3, 2] : [4, G(t, t._targetElement)];
        case 1:
          return i.sent(), [3, 16];
        case 2:
          return n !== "ArrowLeft" && n !== 37 ? [3, 4] : [4, st(t)];
        case 3:
          return i.sent(), [3, 16];
        case 4:
          return n !== "ArrowRight" && n !== 39 ? [3, 6] : [4, F(t)];
        case 5:
          return i.sent(), [3, 16];
        case 6:
          return n !== "Enter" && n !== "NumpadEnter" && n !== 13 ? [3, 16] : (o = e.target || e.srcElement) && o.className.match("introjs-prevbutton") ? [4, st(t)] : [3, 8];
        case 7:
          return i.sent(), [3, 15];
        case 8:
          return o && o.className.match("introjs-skipbutton") ? t._introItems.length - 1 === t._currentStep && w(t._introCompleteCallback) ? [4, t._introCompleteCallback.call(t, t._currentStep, "skip")] : [3, 10] : [3, 12];
        case 9:
          i.sent(), i.label = 10;
        case 10:
          return [4, G(t, t._targetElement)];
        case 11:
          return i.sent(), [3, 15];
        case 12:
          return o && o.getAttribute("data-step-number") ? (o.click(), [3, 15]) : [3, 13];
        case 13:
          return [4, F(t)];
        case 14:
          i.sent(), i.label = 15;
        case 15:
          e.preventDefault ? e.preventDefault() : e.returnValue = !1, i.label = 16;
        case 16:
          return [2];
      }
    });
  });
}
function yt(t) {
  if (t === null || it(t) !== "object" || "nodeType" in t)
    return t;
  var e = {};
  for (var n in t)
    "jQuery" in window && t[n] instanceof window.jQuery ? e[n] = t[n] : e[n] = yt(t[n]);
  return e;
}
function K(t) {
  var e = document.querySelector(".introjs-hints");
  return e ? Array.from(e.querySelectorAll(t)) : [];
}
function wt(t, e) {
  return f(this, void 0, void 0, function() {
    var n;
    return g(this, function(o) {
      switch (o.label) {
        case 0:
          return n = K('.introjs-hint[data-step="'.concat(e, '"]'))[0], lt(), n && I(n, "introjs-hidehint"), w(t._hintCloseCallback) ? [4, t._hintCloseCallback.call(t, e)] : [3, 2];
        case 1:
          o.sent(), o.label = 2;
        case 2:
          return [2];
      }
    });
  });
}
function Ce(t) {
  return f(this, void 0, void 0, function() {
    var e, n, o, i, r;
    return g(this, function(s) {
      switch (s.label) {
        case 0:
          e = K(".introjs-hint"), n = 0, o = e, s.label = 1;
        case 1:
          return n < o.length ? (i = o[n], (r = i.getAttribute("data-step")) ? [4, wt(t, parseInt(r, 10))] : [3, 3]) : [3, 4];
        case 2:
          s.sent(), s.label = 3;
        case 3:
          return n++, [3, 1];
        case 4:
          return [2];
      }
    });
  });
}
function Se(t) {
  return f(this, void 0, void 0, function() {
    var e, n, o, i, r;
    return g(this, function(s) {
      switch (s.label) {
        case 0:
          if (!(e = K(".introjs-hint")) || !e.length)
            return [3, 1];
          for (n = 0, o = e; n < o.length; n++)
            i = o[n], (r = i.getAttribute("data-step")) && Lt(parseInt(r, 10));
          return [3, 3];
        case 1:
          return [4, Pt(t, t._targetElement)];
        case 2:
          s.sent(), s.label = 3;
        case 3:
          return [2];
      }
    });
  });
}
function Lt(t) {
  var e = K('.introjs-hint[data-step="'.concat(t, '"]'))[0];
  e && vt(e, /introjs-hidehint/g);
}
function kt(t) {
  var e = K('.introjs-hint[data-step="'.concat(t, '"]'))[0];
  e && e.parentNode && e.parentNode.removeChild(e);
}
function Ae(t) {
  return f(this, void 0, void 0, function() {
    var e, n, o, i, r, s, a;
    return g(this, function(c) {
      switch (c.label) {
        case 0:
          for ((e = document.querySelector(".introjs-hints")) === null && (e = h("div", { className: "introjs-hints" })), n = function(m) {
            return function(b) {
              var y = b || window.event;
              y && y.stopPropagation && y.stopPropagation(), y && y.cancelBubble !== null && (y.cancelBubble = !0), Rt(t, m);
            };
          }, o = 0; o < t._hintItems.length; o++) {
            if (i = t._hintItems[o], document.querySelector('.introjs-hint[data-step="'.concat(o, '"]')))
              return [2];
            U(r = h("a", { className: "introjs-hint" })), r.onclick = n(o), i.hintAnimation || I(r, "introjs-hint-no-anim"), at(i.element) && I(r, "introjs-fixedhint"), s = h("div", { className: "introjs-hint-dot" }), a = h("div", { className: "introjs-hint-pulse" }), r.appendChild(s), r.appendChild(a), r.setAttribute("data-step", o.toString()), i.hintTargetElement = i.element, i.element = r, qt(i.hintPosition, r, i.hintTargetElement), e.appendChild(r);
          }
          return document.body.appendChild(e), w(t._hintsAddedCallback) ? [4, t._hintsAddedCallback.call(t)] : [3, 2];
        case 1:
          c.sent(), c.label = 2;
        case 2:
          return t._options.hintAutoRefreshInterval >= 0 && (t._hintsAutoRefreshFunction = (p = function() {
            return ct(t);
          }, l = t._options.hintAutoRefreshInterval, function() {
            for (var m = [], b = 0; b < arguments.length; b++)
              m[b] = arguments[b];
            window.clearTimeout(u), u = window.setTimeout(function() {
              p(m);
            }, l);
          }), M.on(window, "scroll", t._hintsAutoRefreshFunction, t, !0)), [2];
      }
      var p, l, u;
    });
  });
}
function qt(t, e, n) {
  if (n !== void 0) {
    var o = Y(n), i = 20, r = 20;
    switch (t) {
      default:
      case "top-left":
        e.style.left = "".concat(o.left, "px"), e.style.top = "".concat(o.top, "px");
        break;
      case "top-right":
        e.style.left = "".concat(o.left + o.width - i, "px"), e.style.top = "".concat(o.top, "px");
        break;
      case "bottom-left":
        e.style.left = "".concat(o.left, "px"), e.style.top = "".concat(o.top + o.height - r, "px");
        break;
      case "bottom-right":
        e.style.left = "".concat(o.left + o.width - i, "px"), e.style.top = "".concat(o.top + o.height - r, "px");
        break;
      case "middle-left":
        e.style.left = "".concat(o.left, "px"), e.style.top = "".concat(o.top + (o.height - r) / 2, "px");
        break;
      case "middle-right":
        e.style.left = "".concat(o.left + o.width - i, "px"), e.style.top = "".concat(o.top + (o.height - r) / 2, "px");
        break;
      case "middle-middle":
        e.style.left = "".concat(o.left + (o.width - i) / 2, "px"), e.style.top = "".concat(o.top + (o.height - r) / 2, "px");
        break;
      case "bottom-middle":
        e.style.left = "".concat(o.left + (o.width - i) / 2, "px"), e.style.top = "".concat(o.top + o.height - r, "px");
        break;
      case "top-middle":
        e.style.left = "".concat(o.left + (o.width - i) / 2, "px"), e.style.top = "".concat(o.top, "px");
    }
  }
}
function Rt(t, e) {
  return f(this, void 0, void 0, function() {
    var n, o, i, r, s, a, c, p, l, u, m;
    return g(this, function(b) {
      switch (b.label) {
        case 0:
          return n = document.querySelector('.introjs-hint[data-step="'.concat(e, '"]')), o = t._hintItems[e], w(t._hintClickCallback) ? [4, t._hintClickCallback.call(t, n, o, e)] : [3, 2];
        case 1:
          b.sent(), b.label = 2;
        case 2:
          return (i = lt()) !== void 0 && parseInt(i, 10) === e || (r = h("div", { className: "introjs-tooltip" }), s = h("div"), a = h("div"), c = h("div"), r.onclick = function(y) {
            y.stopPropagation ? y.stopPropagation() : y.cancelBubble = !0;
          }, s.className = "introjs-tooltiptext", (p = h("p")).innerHTML = o.hint || "", s.appendChild(p), t._options.hintShowButton && ((l = h("a")).className = t._options.buttonClass, l.setAttribute("role", "button"), l.innerHTML = t._options.hintButtonLabel, l.onclick = function() {
            return wt(t, e);
          }, s.appendChild(l)), a.className = "introjs-arrow", r.appendChild(a), r.appendChild(s), u = n.getAttribute("data-step") || "", t._currentStep = parseInt(u, 10), m = t._hintItems[t._currentStep], c.className = "introjs-tooltipReferenceLayer introjs-hintReference", c.setAttribute("data-step", u), R(t, m, c), c.appendChild(r), document.body.appendChild(c), rt(t, m, r, a, !0)), [2];
      }
    });
  });
}
function lt() {
  var t = document.querySelector(".introjs-hintReference");
  if (t && t.parentNode) {
    var e = t.getAttribute("data-step");
    return e ? (t.parentNode.removeChild(t), e) : void 0;
  }
}
function Pt(t, e) {
  return f(this, void 0, void 0, function() {
    var n, o, i, r, s, a, c, p, l, u;
    return g(this, function(m) {
      switch (m.label) {
        case 0:
          if (t._hintItems = [], t._options.hints && t._options.hints.length > 0)
            for (n = 0, o = t._options.hints; n < o.length; n++)
              i = o[n], typeof (r = yt(i)).element == "string" && (r.element = document.querySelector(r.element)), r.hintPosition = r.hintPosition || t._options.hintPosition, r.hintAnimation = r.hintAnimation || t._options.hintAnimation, r.element !== null && t._hintItems.push(r);
          else {
            if (!(s = Array.from(e.querySelectorAll("*[data-hint]"))) || !s.length)
              return [2, !1];
            for (a = 0, c = s; a < c.length; a++)
              p = c[a], l = p.getAttribute("data-hint-animation"), u = t._options.hintAnimation, l && (u = l === "true"), t._hintItems.push({ element: p, hint: p.getAttribute("data-hint") || "", hintPosition: p.getAttribute("data-hint-position") || t._options.hintPosition, hintAnimation: u, tooltipClass: p.getAttribute("data-tooltip-class") || void 0, position: p.getAttribute("data-position") || t._options.tooltipPosition });
          }
          return [4, Ae(t)];
        case 1:
          return m.sent(), M.on(document, "click", lt, t, !1), M.on(window, "resize", ct, t, !0), [2, !0];
      }
    });
  });
}
function ct(t) {
  for (var e = 0, n = t._hintItems; e < n.length; e++) {
    var o = n[e], i = o.hintTargetElement;
    qt(o.hintPosition, o.element, i);
  }
}
function Dt(t, e) {
  var n = Array.from(e.querySelectorAll("*[data-intro]")), o = [];
  if (t._options.steps && t._options.steps.length)
    for (var i = 0, r = t._options.steps; i < r.length; i++) {
      var s = yt(m = r[i]);
      if (s.step = o.length + 1, s.title = s.title || "", typeof s.element == "string" && (s.element = document.querySelector(s.element) || void 0), s.element === void 0 || s.element === null) {
        var a = document.querySelector(".introjsFloatingElement");
        a === null && (a = h("div", { className: "introjsFloatingElement" }), document.body.appendChild(a)), s.element = a, s.position = "floating";
      }
      s.position = s.position || t._options.tooltipPosition, s.scrollTo = s.scrollTo || t._options.scrollTo, s.disableInteraction === void 0 && (s.disableInteraction = t._options.disableInteraction), s.element !== null && o.push(s);
    }
  else {
    var c = void 0;
    if (n.length < 1)
      return [];
    for (var p = 0, l = n; p < l.length; p++) {
      var u = l[p];
      if ((!t._options.group || u.getAttribute("data-intro-group") === t._options.group) && u.style.display !== "none") {
        var m = parseInt(u.getAttribute("data-step") || "", 10);
        c = t._options.disableInteraction, u.hasAttribute("data-disable-interaction") && (c = !!u.getAttribute("data-disable-interaction")), m > 0 && (o[m - 1] = { step: m, element: u, title: u.getAttribute("data-title") || "", intro: u.getAttribute("data-intro") || "", tooltipClass: u.getAttribute("data-tooltip-class") || void 0, highlightClass: u.getAttribute("data-highlight-class") || void 0, position: u.getAttribute("data-position") || t._options.tooltipPosition, scrollTo: u.getAttribute("data-scroll-to") || t._options.scrollTo, disableInteraction: c });
      }
    }
    for (var b = 0, y = 0, C = n; y < C.length; y++)
      if (u = C[y], (!t._options.group || u.getAttribute("data-intro-group") === t._options.group) && u.getAttribute("data-step") === null) {
        for (; o[b] !== void 0; )
          b++;
        c = u.hasAttribute("data-disable-interaction") ? !!u.getAttribute("data-disable-interaction") : t._options.disableInteraction, o[b] = { element: u, title: u.getAttribute("data-title") || "", intro: u.getAttribute("data-intro") || "", step: b + 1, tooltipClass: u.getAttribute("data-tooltip-class") || void 0, highlightClass: u.getAttribute("data-highlight-class") || void 0, position: u.getAttribute("data-position") || t._options.tooltipPosition, scrollTo: u.getAttribute("data-scroll-to") || t._options.scrollTo, disableInteraction: c };
      }
  }
  for (var _ = [], E = 0; E < o.length; E++)
    o[E] && _.push(o[E]);
  return (o = _).sort(function(N, L) {
    return N.step - L.step;
  }), o;
}
function Ot(t, e) {
  var n = t._currentStep;
  if (n != null && n != -1) {
    var o = t._introItems[n], i = document.querySelector(".introjs-tooltipReferenceLayer"), r = document.querySelector(".introjs-helperLayer"), s = document.querySelector(".introjs-disableInteraction");
    R(t, o, r), R(t, o, i), R(t, o, s), e && (t._introItems = Dt(t, t._targetElement), function(p, l) {
      if (p._options.showBullets) {
        var u = document.querySelector(".introjs-bullets");
        u && u.parentNode && u.parentNode.replaceChild(xt(p, l), u);
      }
    }(t, o), Bt(i, n, t._introItems.length));
    var a = document.querySelector(".introjs-arrow"), c = document.querySelector(".introjs-tooltip");
    return c && a && rt(t, t._introItems[n], c, a), ct(t), t;
  }
}
function Ht(t) {
  Ot(t);
}
function Q(t, e) {
  if (e === void 0 && (e = !1), t && t.parentElement) {
    var n = t.parentElement;
    e ? (H(t, { opacity: "0" }), window.setTimeout(function() {
      try {
        n.removeChild(t);
      } catch (o) {
      }
    }, 500)) : n.removeChild(t);
  }
}
function G(t, e, n) {
  return n === void 0 && (n = !1), f(this, void 0, void 0, function() {
    var o, i, r, s;
    return g(this, function(a) {
      switch (a.label) {
        case 0:
          return o = !0, t._introBeforeExitCallback === void 0 ? [3, 2] : [4, t._introBeforeExitCallback.call(t, e)];
        case 1:
          o = a.sent(), a.label = 2;
        case 2:
          if (!n && o === !1)
            return [2];
          if ((i = Array.from(e.querySelectorAll(".introjs-overlay"))) && i.length)
            for (r = 0, s = i; r < s.length; r++)
              Q(s[r]);
          return Q(e.querySelector(".introjs-helperLayer"), !0), Q(e.querySelector(".introjs-tooltipReferenceLayer")), Q(e.querySelector(".introjs-disableInteraction")), Q(document.querySelector(".introjsFloatingElement")), Tt(), M.off(window, "keydown", Mt, t, !0), M.off(window, "resize", Ht, t, !0), w(t._introExitCallback) ? [4, t._introExitCallback.call(t)] : [3, 4];
        case 3:
          a.sent(), a.label = 4;
        case 4:
          return t._currentStep = -1, [2];
      }
    });
  });
}
function ke(t, e) {
  return f(this, void 0, void 0, function() {
    var n;
    return g(this, function(o) {
      switch (o.label) {
        case 0:
          return t.isActive() ? w(t._introStartCallback) ? [4, t._introStartCallback.call(t, e)] : [3, 2] : [2, !1];
        case 1:
          o.sent(), o.label = 2;
        case 2:
          return (n = Dt(t, e)).length === 0 ? [2, !1] : (t._introItems = n, function(i, r) {
            var s = this, a = h("div", { className: "introjs-overlay" });
            H(a, { top: 0, bottom: 0, left: 0, right: 0, position: "fixed" }), r.appendChild(a), i._options.exitOnOverlayClick === !0 && (H(a, { cursor: "pointer" }), a.onclick = function() {
              return f(s, void 0, void 0, function() {
                return g(this, function(c) {
                  switch (c.label) {
                    case 0:
                      return [4, G(i, r)];
                    case 1:
                      return c.sent(), [2];
                  }
                });
              });
            });
          }(t, e), [4, F(t)]);
        case 3:
          o.sent(), e.addEventListener, t._options.keyboardNavigation && M.on(window, "keydown", Mt, t, !0), M.on(window, "resize", Ht, t, !0), o.label = 4;
        case 4:
          return [2, !1];
      }
    });
  });
}
function jt(t, e, n) {
  return t[e] = n, t;
}
var ht = function() {
  function t(e) {
    this._currentStep = -1, this._introItems = [], this._hintItems = [], this._targetElement = e, this._options = { steps: [], hints: [], isActive: !0, nextLabel: "Next", prevLabel: "Back", skipLabel: "×", doneLabel: "Done", hidePrev: !1, hideNext: !1, nextToDone: !0, tooltipPosition: "bottom", tooltipClass: "", group: "", highlightClass: "", exitOnEsc: !0, exitOnOverlayClick: !0, showStepNumbers: !1, stepNumbersOfLabel: "of", keyboardNavigation: !0, showButtons: !0, showBullets: !0, showProgress: !1, scrollToElement: !0, scrollTo: "element", scrollPadding: 30, overlayOpacity: 0.5, autoPosition: !0, positionPrecedence: ["bottom", "top", "right", "left"], disableInteraction: !1, dontShowAgain: !1, dontShowAgainLabel: "Don't show this again", dontShowAgainCookie: "introjs-dontShowAgain", dontShowAgainCookieDays: 365, helperElementPadding: 10, hintPosition: "top-middle", hintButtonLabel: "Got it", hintShowButton: !0, hintAutoRefreshInterval: 10, hintAnimation: !0, buttonClass: "introjs-button", progressBarAdditionalClass: !1 };
  }
  return t.prototype.isActive = function() {
    return (!this._options.dontShowAgain || (e = Et(this._options.dontShowAgainCookie)) === "" || e !== "true") && this._options.isActive;
    var e;
  }, t.prototype.clone = function() {
    return new t(this._targetElement);
  }, t.prototype.setOption = function(e, n) {
    return this._options = jt(this._options, e, n), this;
  }, t.prototype.setOptions = function(e) {
    return this._options = function(n, o) {
      for (var i = 0, r = Object.entries(o); i < r.length; i++) {
        var s = r[i];
        n = jt(n, s[0], s[1]);
      }
      return n;
    }(this._options, e), this;
  }, t.prototype.start = function() {
    return f(this, void 0, void 0, function() {
      return g(this, function(e) {
        switch (e.label) {
          case 0:
            return [4, ke(this, this._targetElement)];
          case 1:
            return e.sent(), [2, this];
        }
      });
    });
  }, t.prototype.goToStep = function(e) {
    return f(this, void 0, void 0, function() {
      return g(this, function(n) {
        switch (n.label) {
          case 0:
            return [4, we(this, e)];
          case 1:
            return n.sent(), [2, this];
        }
      });
    });
  }, t.prototype.addStep = function(e) {
    return this._options.steps || (this._options.steps = []), this._options.steps.push(e), this;
  }, t.prototype.addSteps = function(e) {
    if (!e.length)
      return this;
    for (var n = 0; n < e.length; n++)
      this.addStep(e[n]);
    return this;
  }, t.prototype.goToStepNumber = function(e) {
    return f(this, void 0, void 0, function() {
      return g(this, function(n) {
        switch (n.label) {
          case 0:
            return [4, _e(this, e)];
          case 1:
            return n.sent(), [2, this];
        }
      });
    });
  }, t.prototype.nextStep = function() {
    return f(this, void 0, void 0, function() {
      return g(this, function(e) {
        switch (e.label) {
          case 0:
            return [4, F(this)];
          case 1:
            return e.sent(), [2, this];
        }
      });
    });
  }, t.prototype.previousStep = function() {
    return f(this, void 0, void 0, function() {
      return g(this, function(e) {
        switch (e.label) {
          case 0:
            return [4, st(this)];
          case 1:
            return e.sent(), [2, this];
        }
      });
    });
  }, t.prototype.currentStep = function() {
    return this._currentStep;
  }, t.prototype.exit = function(e) {
    return f(this, void 0, void 0, function() {
      return g(this, function(n) {
        switch (n.label) {
          case 0:
            return [4, G(this, this._targetElement, e)];
          case 1:
            return n.sent(), [2, this];
        }
      });
    });
  }, t.prototype.refresh = function(e) {
    return Ot(this, e), this;
  }, t.prototype.setDontShowAgain = function(e) {
    return ve(this, e), this;
  }, t.prototype.onbeforechange = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onbeforechange was not a function");
    return this._introBeforeChangeCallback = e, this;
  }, t.prototype.onchange = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onchange was not a function.");
    return this._introChangeCallback = e, this;
  }, t.prototype.onafterchange = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onafterchange was not a function");
    return this._introAfterChangeCallback = e, this;
  }, t.prototype.oncomplete = function(e) {
    if (!w(e))
      throw new Error("Provided callback for oncomplete was not a function.");
    return this._introCompleteCallback = e, this;
  }, t.prototype.onhintsadded = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onhintsadded was not a function.");
    return this._hintsAddedCallback = e, this;
  }, t.prototype.onhintclick = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onhintclick was not a function.");
    return this._hintClickCallback = e, this;
  }, t.prototype.onhintclose = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onhintclose was not a function.");
    return this._hintCloseCallback = e, this;
  }, t.prototype.onstart = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onstart was not a function.");
    return this._introStartCallback = e, this;
  }, t.prototype.onexit = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onexit was not a function.");
    return this._introExitCallback = e, this;
  }, t.prototype.onskip = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onskip was not a function.");
    return this._introSkipCallback = e, this;
  }, t.prototype.onbeforeexit = function(e) {
    if (!w(e))
      throw new Error("Provided callback for onbeforeexit was not a function.");
    return this._introBeforeExitCallback = e, this;
  }, t.prototype.addHints = function() {
    return f(this, void 0, void 0, function() {
      return g(this, function(e) {
        switch (e.label) {
          case 0:
            return [4, Pt(this, this._targetElement)];
          case 1:
            return e.sent(), [2, this];
        }
      });
    });
  }, t.prototype.hideHint = function(e) {
    return f(this, void 0, void 0, function() {
      return g(this, function(n) {
        switch (n.label) {
          case 0:
            return [4, wt(this, e)];
          case 1:
            return n.sent(), [2, this];
        }
      });
    });
  }, t.prototype.hideHints = function() {
    return f(this, void 0, void 0, function() {
      return g(this, function(e) {
        switch (e.label) {
          case 0:
            return [4, Ce(this)];
          case 1:
            return e.sent(), [2, this];
        }
      });
    });
  }, t.prototype.showHint = function(e) {
    return Lt(e), this;
  }, t.prototype.showHints = function() {
    return f(this, void 0, void 0, function() {
      return g(this, function(e) {
        switch (e.label) {
          case 0:
            return [4, Se(this)];
          case 1:
            return e.sent(), [2, this];
        }
      });
    });
  }, t.prototype.removeHints = function() {
    return function(e) {
      for (var n = 0, o = K(".introjs-hint"); n < o.length; n++) {
        var i = o[n].getAttribute("data-step");
        i && kt(parseInt(i, 10));
      }
      M.off(document, "click", lt, e, !1), M.off(window, "resize", ct, e, !0), e._hintsAutoRefreshFunction && M.off(window, "scroll", e._hintsAutoRefreshFunction, e, !0);
    }(this), this;
  }, t.prototype.removeHint = function(e) {
    return kt(e), this;
  }, t.prototype.showHintDialog = function(e) {
    return f(this, void 0, void 0, function() {
      return g(this, function(n) {
        switch (n.label) {
          case 0:
            return [4, Rt(this, e)];
          case 1:
            return n.sent(), [2, this];
        }
      });
    });
  }, t;
}(), ft = function t(e) {
  var n;
  if (it(e) === "object")
    n = new ht(e);
  else if (typeof e == "string") {
    var o = document.querySelector(e);
    if (!o)
      throw new Error("There is no element with given selector.");
    n = new ht(o);
  } else
    n = new ht(document.body);
  return t.instances[mt(n, "introjs-instance")] = n, n;
};
ft.version = "7.2.0", ft.instances = {};
const je = () => {
  const { prefixVar: t } = ge(""), e = D(`${t}-online-aiCreateTable`), n = D(`${t}-online-newAddBtn`), o = D(`${t}-online-customBtn`), i = D(`${t}-online-enhanceJsBtn`), r = D(`${t}-online-enhanceSqlBtn`), s = D(`${t}-online-enhanceJavaBtn`), a = D(`${t}-online-exportDbBtn`), c = D(`${t}-online-codeGenerator`), p = `${t}-online-guide`, l = () => {
    let u = ft();
    u.setOptions({
      nextLabel: "下一步",
      prevLabel: "上一步",
      //skipLabel: '跳过',
      doneLabel: "完成",
      steps: [
        {
          title: "第一步",
          element: document.querySelector(`.${n.value}`),
          intro: "点击<strong>新增</strong>按钮，新建一个表。"
        },
        {
          title: "第二步",
          intro: '在列表中找到刚才新建数据，在操作列点击<strong>"更多"</strong>，选择<strong>"同步数据库"</strong>。'
        },
        {
          title: "第三步",
          intro: '在列表中找到刚才新建数据，在操作列点击<strong>"更多"</strong>，选择<strong>"功能测试"</strong>。'
        },
        {
          title: "AI建表",
          element: document.querySelector(`.${e.value}`),
          intro: "输入修饰词即可通过AI创建工作表"
        },
        {
          title: "代码生成",
          element: document.querySelector(`.${c.value}`),
          intro: "选中一条记录，通过代码生成可将已配置好的表单，一键生成前后端代码，复杂需求可在此基础上进行二次开发。"
        },
        {
          title: "自定义按钮",
          element: document.querySelector(`.${o.value}`),
          intro: '选中一条记录，点击自定义按钮，配置按钮相关信息即可在当前记录的<strong>"功能测试"</strong>页面新增一个按钮'
        },
        {
          title: "JS强增",
          element: document.querySelector(`.${i.value}`),
          intro: '选中一条记录，通过js增强可为<strong>"自定义按钮"</strong>添加不同操作，可操作列表和表单数据等，也可以添加表单前置事件。'
        },
        {
          title: "SQL增强",
          element: document.querySelector(`.${r.value}`),
          intro: "选中一条记录，通过增强SQL，可以关联修改业务数据。"
        },
        {
          title: "java增强",
          element: document.querySelector(`.${s.value}`),
          intro: "选中一条记录，通过Java增强可在表单的增加、修改、和删除数据时实现额外的功能，类似spring中的AOP切面编程。"
        },
        {
          title: "导入数据库表",
          element: document.querySelector(`.${a.value}`),
          intro: "可将已有数据库中的表，直接导入生成表单。"
        }
      ]
    }), u.start();
  };
  return Yt(() => {
    localStorage.getItem(p) || setTimeout(() => {
      l(), localStorage.setItem(p, "1");
    }, 2e3);
  }), {
    newAddBtn: n,
    customBtn: o,
    enhanceJsBtn: i,
    enhanceSqlBtn: r,
    exportDbBtn: a,
    enhanceJavaBtn: s,
    codeGeneratorBtn: c,
    aiCreateTable: e
  };
}, Ee = Kt({
  name: "CgformIndex",
  components: {
    BasicTable: Zt,
    TableAction: te,
    CgformModal: ee,
    DbToOnlineModal: ne,
    CodeGeneratorModal: oe,
    CustomButtonList: ie,
    EnhanceJsModal: re,
    EnhanceJavaModal: se,
    EnhanceSqlModal: ae,
    AuthManagerDrawer: le,
    AuthSetterModal: ce,
    AiModal: ue,
    CgformAddressModal: pe
  },
  setup() {
    const {
      pageContext: t,
      onAdd: e,
      onAiCreateTable: n,
      onSuccess: o,
      onCreateAiTable: i,
      onDeleteBatch: r,
      onImportDbTable: s,
      onGenerateCode: a,
      onShowCustomButton: c,
      onShowEnhanceJs: p,
      onShowEnhanceSql: l,
      onShowEnhanceJava: u,
      getTableAction: m,
      getDropDownAction: b,
      registerAuthManagerDrawer: y,
      registerAuthSetterModal: C,
      registerCustomButtonModal: _,
      registerEnhanceJsModal: E,
      registerEnhanceSqlModal: N,
      registerEnhanceJavaModal: L,
      registerCgformModal: x,
      registerDbToOnlineModal: P,
      registerCodeGeneratorModal: V,
      registerAiToOnlineModal: z,
      registerAddressModal: T,
      tableRef: W
    } = de({
      pageType: he.normal,
      designScope: "online-cgform-list",
      columns: me,
      formSchemas: fe
    }), { prefixCls: X, tableContext: J } = t, [d, { reload: A }, { rowSelection: k, selectedRowKeys: $ }] = J, {
      newAddBtn: Z,
      customBtn: Jt,
      enhanceJsBtn: $t,
      enhanceSqlBtn: Ft,
      exportDbBtn: Gt,
      enhanceJavaBtn: Vt,
      codeGeneratorBtn: zt,
      aiCreateTable: Wt
    } = je();
    return {
      prefixCls: X,
      reload: A,
      rowSelection: k,
      selectedRowKeys: $,
      onAdd: e,
      onAiCreateTable: n,
      onSuccess: o,
      onDeleteBatch: r,
      onImportDbTable: s,
      onGenerateCode: a,
      onShowCustomButton: c,
      onShowEnhanceJs: p,
      onShowEnhanceSql: l,
      onShowEnhanceJava: u,
      onCreateAiTable: i,
      getTableAction: m,
      getDropDownAction: b,
      registerAuthManagerDrawer: y,
      registerAuthSetterModal: C,
      registerCustomButtonModal: _,
      registerEnhanceJsModal: E,
      registerEnhanceSqlModal: N,
      registerEnhanceJavaModal: L,
      registerTable: d,
      registerCgformModal: x,
      registerDbToOnlineModal: P,
      registerCodeGeneratorModal: V,
      registerAiToOnlineModal: z,
      registerAddressModal: T,
      newAddBtn: Z,
      customBtn: Jt,
      enhanceJsBtn: $t,
      enhanceSqlBtn: Ft,
      enhanceJavaBtn: Vt,
      exportDbBtn: Gt,
      codeGeneratorBtn: zt,
      aiCreateTable: Wt,
      tableRef: W
    };
  }
}), Te = {
  key: 0,
  style: { color: "limegreen" }
}, Ne = {
  key: 1,
  style: { color: "red" }
};
function xe(t, e, n, o, i, r) {
  const s = S("a-button"), a = S("a-icon"), c = S("a-menu-item"), p = S("a-menu"), l = S("a-dropdown"), u = S("TableAction"), m = S("BasicTable"), b = S("CgformModal"), y = S("DbToOnlineModal"), C = S("CodeGeneratorModal"), _ = S("CustomButtonList"), E = S("EnhanceJsModal"), N = S("EnhanceJavaModal"), L = S("EnhanceSqlModal"), x = S("AuthManagerDrawer"), P = S("AuthSetterModal"), V = S("AiModal"), z = S("CgformAddressModal");
  return tt(), ut(Xt, null, [
    pt("div", {
      class: q(t.prefixCls)
    }, [
      v(m, Qt({
        ref: "tableRef",
        onRegister: t.registerTable,
        rowSelection: t.rowSelection
      }, t.$attrs), {
        tableTitle: j(() => [
          v(s, {
            class: q(t.newAddBtn),
            onClick: t.onAdd,
            type: "primary",
            preIcon: "ant-design:plus"
          }, {
            default: j(() => e[0] || (e[0] = [
              O("新增")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          v(s, {
            class: q(t.aiCreateTable),
            onClick: t.onAiCreateTable,
            type: "primary",
            preIcon: "ant-design:plus"
          }, {
            default: j(() => e[1] || (e[1] = [
              O("AI建表")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          v(s, {
            class: q(t.customBtn),
            onClick: t.onShowCustomButton,
            type: "primary",
            preIcon: "ant-design:highlight"
          }, {
            default: j(() => e[2] || (e[2] = [
              O("自定义按钮")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          v(s, {
            class: q(t.enhanceJsBtn),
            onClick: t.onShowEnhanceJs,
            type: "primary",
            preIcon: "ant-design:strikethrough"
          }, {
            default: j(() => e[3] || (e[3] = [
              O("JS增强")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          v(s, {
            class: q(t.enhanceSqlBtn),
            onClick: t.onShowEnhanceSql,
            type: "primary",
            preIcon: "ant-design:filter"
          }, {
            default: j(() => e[4] || (e[4] = [
              O(" SQL增强 ")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          v(s, {
            class: q(t.enhanceJavaBtn),
            onClick: t.onShowEnhanceJava,
            type: "primary",
            preIcon: "ant-design:tool"
          }, {
            default: j(() => e[5] || (e[5] = [
              O("JAVA增强")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          v(s, {
            class: q(t.exportDbBtn),
            onClick: t.onImportDbTable,
            type: "primary",
            preIcon: "ant-design:database"
          }, {
            default: j(() => e[6] || (e[6] = [
              O("导入数据库表")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          v(s, {
            class: q(t.codeGeneratorBtn),
            onClick: t.onGenerateCode,
            type: "primary",
            preIcon: "bx:bx-code-alt"
          }, {
            default: j(() => e[7] || (e[7] = [
              O(" 代码生成 ")
            ])),
            _: 1
          }, 8, ["class", "onClick"]),
          t.selectedRowKeys.length > 0 ? (tt(), Ut(l, { key: 0 }, {
            overlay: j(() => [
              v(p, null, {
                default: j(() => [
                  v(c, {
                    key: "1",
                    onClick: t.onDeleteBatch
                  }, {
                    default: j(() => [
                      v(a, { type: "delete" }),
                      e[8] || (e[8] = pt("span", null, "删除", -1))
                    ]),
                    _: 1
                  }, 8, ["onClick"])
                ]),
                _: 1
              })
            ]),
            default: j(() => [
              v(s, null, {
                default: j(() => [
                  e[9] || (e[9] = pt("span", null, "批量操作", -1)),
                  v(a, { type: "down" })
                ]),
                _: 1
              })
            ]),
            _: 1
          })) : dt("", !0)
        ]),
        dbSync: j(({ text: T }) => [
          T === "Y" ? (tt(), ut("span", Te, "已同步")) : dt("", !0),
          T === "N" ? (tt(), ut("span", Ne, "未同步")) : dt("", !0)
        ]),
        action: j(({ record: T }) => [
          v(u, {
            actions: t.getTableAction(T),
            dropDownActions: t.getDropDownAction(T)
          }, null, 8, ["actions", "dropDownActions"])
        ]),
        _: 1
      }, 16, ["onRegister", "rowSelection"])
    ], 2),
    v(b, {
      onRegister: t.registerCgformModal,
      onSuccess: t.onSuccess
    }, null, 8, ["onRegister", "onSuccess"]),
    v(y, {
      onRegister: t.registerDbToOnlineModal,
      onSuccess: t.onSuccess
    }, null, 8, ["onRegister", "onSuccess"]),
    v(C, { onRegister: t.registerCodeGeneratorModal }, null, 8, ["onRegister"]),
    v(_, { onRegister: t.registerCustomButtonModal }, null, 8, ["onRegister"]),
    v(E, { onRegister: t.registerEnhanceJsModal }, null, 8, ["onRegister"]),
    v(N, { onRegister: t.registerEnhanceJavaModal }, null, 8, ["onRegister"]),
    v(L, { onRegister: t.registerEnhanceSqlModal }, null, 8, ["onRegister"]),
    v(x, { onRegister: t.registerAuthManagerDrawer }, null, 8, ["onRegister"]),
    v(P, { onRegister: t.registerAuthSetterModal }, null, 8, ["onRegister"]),
    v(V, {
      onRegister: t.registerAiToOnlineModal,
      onSuccess: t.onCreateAiTable
    }, null, 8, ["onRegister", "onSuccess"]),
    v(z, { onRegister: t.registerAddressModal }, null, 8, ["onRegister"])
  ], 64);
}
const jo = /* @__PURE__ */ be(Ee, [["render", xe]]);
export {
  jo as default
};
