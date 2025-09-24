var ie = Object.defineProperty;
var A = Object.getOwnPropertySymbols;
var le = Object.prototype.hasOwnProperty, ne = Object.prototype.propertyIsEnumerable;
var W = (a, s, d) => s in a ? ie(a, s, { enumerable: !0, configurable: !0, writable: !0, value: d }) : a[s] = d, R = (a, s) => {
  for (var d in s || (s = {}))
    le.call(s, d) && W(a, d, s[d]);
  if (A)
    for (var d of A(s))
      ne.call(s, d) && W(a, d, s[d]);
  return a;
};
var _ = (a, s, d) => new Promise((k, b) => {
  var L = (m) => {
    try {
      w(d.next(m));
    } catch (D) {
      b(D);
    }
  }, x = (m) => {
    try {
      w(d.throw(m));
    } catch (D) {
      b(D);
    }
  }, w = (m) => m.done ? k(m.value) : Promise.resolve(m.value).then(L, x);
  w((d = d.apply(a, s)).next());
});
import { ref as ae, computed as re, watch as oe, defineComponent as se, resolveComponent as T, openBlock as f, createElementBlock as u, normalizeClass as j, createVNode as $, withCtx as q, Fragment as S, renderList as V, createBlock as K, createElementVNode as p, toDisplayString as B } from "vue";
import { propTypes as J } from "/@/utils/propTypes";
import { getDictItemsByCode as de } from "/@/utils/dict";
import { filterMultiDictText as O, filterDictText as ce } from "/@/utils/dict/JDictSelectUtil";
import { initDictOptions as H } from "/@/utils/dict/index";
import { loadDictItem as fe, queryDepartTreeSync as ue, getUserList as pe } from "/@/api/common/api";
import { defHttp as me } from "/@/utils/http/axios";
import { getAreaTextByCode as _e } from "/@/components/Form/src/utils/Area";
import { getFileAccessHttpUrl as he } from "/@/utils/common/compUtils";
import { createImgPreview as ge } from "/@/components/Preview/index";
import { useMessage as ve } from "/@/hooks/web/useMessage";
import { DownloadOutlined as we, EyeOutlined as ye, PaperClipOutlined as ke } from "@ant-design/icons-vue";
import { L as De } from "./useExtendComponent-bb98e568.mjs";
import { MarkdownViewer as Fe } from "/@/components/Markdown";
import { getWeekMonthQuarterYear as Ce } from "/@/utils";
import { _ as be } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/components/Modal";
import "/@/components/Form/index";
import "lodash-es";
import "./constant-fa63bd66.mjs";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/utils/is";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/utils/auth";
import "/@/hooks/web/useAppInject";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "/@/components/Loading";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
function xe(a) {
  const s = {}, d = [], k = ae({}), { createMessage: b } = ve(), L = re(() => a.containerClass ? `jeecg-detail-form ${a.containerClass}` : "jeecg-detail-form");
  oe(
    () => a.data,
    (e) => _(this, null, function* () {
      if (e) {
        let n = a.schemas, l = {};
        if (n && n.length > 0)
          for (let i of n) {
            let t = i.field;
            try {
              l[t] = yield x(i);
            } catch (o) {
            }
          }
        k.value = l;
      }
    }),
    { deep: !0, immediate: !0 }
  );
  function x(e) {
    return _(this, null, function* () {
      let n = a.data;
      if (n) {
        let l = n[e.field];
        if (!l && l !== "0" && l !== 0)
          return "";
        let i = l, t = e.view;
        if (t == "list" || t == "radio" || t == "checkbox" || t == "list_multi")
          i = yield w(e, n);
        else if (t == "sel_search")
          i = yield D(e, n);
        else if (t == "cat_tree")
          i = yield E(e, n);
        else if (t == "link_table")
          i = yield ee(e, n);
        else if (t == "sel_depart")
          i = yield c(e, n);
        else if (t == "sel_user")
          i = yield F(e, n);
        else if (t == "pca")
          i = _e(l);
        else if (t == "link_down")
          i = yield v(e, n);
        else if (t == "sel_tree")
          i = yield h(e, n);
        else if (t == "switch")
          i = yield I(e, n);
        else if (t == "image" || t == "file")
          i = Y(e, n);
        else if (t == "popup_dict") {
          const o = n[`${e.field}_dictText`];
          o !== void 0 && (i = o);
        } else if (d.indexOf(e.field) >= 0) {
          let o = s[e.field];
          o && o.length > 0 && (i = O(o, l));
        }
        return i;
      }
      return "";
    });
  }
  function w(e, n) {
    return _(this, null, function* () {
      let l = m(e), i = n[e.field];
      if (!l)
        return i;
      let t = de(l);
      if (t && t.length > 0)
        return O(t, i);
      {
        let o = [];
        if (s[l] ? o = s[l] : o = (yield H(l)) || [], o && o.length > 0)
          return s[l] = o, O(o, i);
      }
      return "";
    });
  }
  function m(e) {
    let n = "", { dictCode: l, dictTable: i, dictText: t } = e;
    return i ? n = encodeURI(`${i},${t},${l}`) : n = l, n;
  }
  function D(e, n) {
    return _(this, null, function* () {
      let l = m(e), i = n[e.field];
      if (!i)
        return "";
      let t = [];
      return s[l + i] ? t = s[l + i] : t = (yield me.get({ url: `/sys/dict/loadDictItem/${l}`, params: { key: i } })) || [], t && t.length > 0 ? (s[l + i] = t, t.join(",")) : "";
    });
  }
  function E(e, n) {
    return _(this, null, function* () {
      let l = n[e.field];
      if (!l)
        return "";
      let i = (yield fe({ ids: l })) || [];
      return i && i.length > 0 ? i.join(",") : "";
    });
  }
  function c(e, n) {
    return _(this, null, function* () {
      let l = n[e.field];
      if (!l)
        return "";
      let i = r(e), t = i.store || "id", o = i.text || "departName", g = (yield ue({ ids: l, primaryKey: t })) || [];
      if (g && g.length > 0) {
        let y = [];
        for (let C of g)
          C[o] ? y.push(C[o]) : y.push(C.title);
        return y.join(",");
      }
      return "";
    });
  }
  function F(e, n) {
    return _(this, null, function* () {
      let l = n[e.field];
      if (!l)
        return "";
      let i = r(e), o = {
        [i.store || "username"]: l
      }, y = ((yield pe(o)) || {}).records || [];
      if (y && y.length > 0) {
        let C = [], M = i.text || "realname";
        for (let N of y)
          C.push(N[M]);
        return C.join(",");
      }
      return "";
    });
  }
  function r(e) {
    let n = {}, { fieldExtendJson: l } = e;
    if (l && typeof l == "string")
      try {
        let i = JSON.parse(l);
        n = R({}, i);
      } catch (i) {
      }
    return n;
  }
  function v(e, n) {
    return _(this, null, function* () {
      let { dictTable: l, field: i } = e, t = [];
      if (s[i])
        t = s[i];
      else if (l) {
        let o = JSON.parse(l);
        if (o) {
          let { table: g, txt: y, key: C, linkField: M } = o, N = `${g},${y},${C}`;
          if (t = [...(yield H(N)) || []], t && t.length > 0 && (s[i] = t, M)) {
            let te = M.split(",");
            for (let U of te)
              s[U] = t, d.push(U);
          }
        }
      }
      if (t && t.length > 0) {
        let o = n[i];
        return O(t, o);
      }
      return "";
    });
  }
  function h(e, n) {
    return _(this, null, function* () {
      let { dict: l, field: i } = e, t = [];
      if (s[i] ? t = s[i] : l && (t = yield H(l)), t && t.length > 0) {
        let o = n[i];
        return O(t, o);
      }
      return "";
    });
  }
  function I(e, n) {
    return _(this, null, function* () {
      let { fieldExtendJson: l, field: i } = e, t = ["Y", "N"];
      l && (t = JSON.parse(l));
      let o = [
        { value: t[0], text: "是" },
        { value: t[1], text: "否" },
        { value: t[0] + "", text: "是" },
        { value: t[1] + "", text: "否" }
      ], g = n[i];
      return ce(o, g);
    });
  }
  function P(e) {
    return e.span ? e.span : a.span;
  }
  function Y(e, n) {
    let l = n[e.field];
    if (!l)
      return [];
    let i = l.split(","), t = [];
    for (let o of i) {
      let g = he(o) || "";
      g && t.push(g);
    }
    return t;
  }
  function z(e) {
    e && window.open(e);
  }
  function Q(e) {
    let n = k.value[e];
    if (!n || n.length == 0) {
      b.warning("无图片!");
      return;
    }
    ge({ imageList: n });
  }
  function G(e) {
    return e ? e.substring(e.lastIndexOf("/") + 1) : "";
  }
  const X = ["file", "image", "markdown", "umeditor"];
  function Z(e) {
    return X.indexOf(e.view) >= 0 ? a.span == 12 ? "span12" : a.span == 8 ? "span8" : a.span == 6 ? "span6" : "span24" : "";
  }
  function ee(e, n) {
    return _(this, null, function* () {
      let l = n[e.field];
      return r(e).showType == "select" ? l ? n[e.field + "_dictText"] : "" : l ? n[e.field] : "";
    });
  }
  return {
    formContainerClass: L,
    detailFormData: k,
    getItemSpan: P,
    handleDownloadFile: z,
    handleViewImage: Q,
    getFilename: G,
    getLabelWidthClass: Z
  };
}
const Te = se({
  name: "DetailForm",
  components: {
    DownloadOutlined: we,
    EyeOutlined: ye,
    PaperClipOutlined: ke,
    LinkTableCard: De,
    MarkdownViewer: Fe
  },
  props: {
    span: J.number.def(24),
    //表单配置
    schemas: J.array.def([]),
    //表单数据
    data: J.object.def({}),
    containerClass: J.string.def("")
  },
  setup(a) {
    const { formContainerClass: s, detailFormData: d, getItemSpan: k, handleDownloadFile: b, handleViewImage: L, getFilename: x, getLabelWidthClass: w } = xe(a);
    return {
      formContainerClass: s,
      detailFormData: d,
      getItemSpan: k,
      handleDownloadFile: b,
      handleViewImage: L,
      getFilename: x,
      getLabelWidthClass: w,
      filterLable: (c) => {
        if (c.fieldExtendJson) {
          const F = JSON.parse(c.fieldExtendJson);
          if (F.labelLength && c.label.length > 4)
            return c.label.substr(0, F.labelLength);
        }
        return c.label;
      },
      filter: (c, F, r) => {
        if (F == "date" && typeof c == "string") {
          if (!c)
            return "";
          let v = r.fieldExtendJson;
          return v && (v = JSON.parse(v), v.picker && v.picker != "default") ? Ce(c)[v.picker] : c.split(" ").shift();
        } else
          return c;
      },
      textareaLineBreak: (c) => (c && c.includes(`
`) && (c = c.replace(/\n/g, "<br>")), c)
    };
  }
});
const Le = {
  key: 1,
  class: "detail-item"
}, $e = ["title"], Ie = {
  key: 0,
  class: "item-content"
}, Oe = ["innerHTML"], Ee = ["innerHTML"], Me = {
  key: 3,
  class: "item-content",
  style: { display: "block", "padding-top": "10px" }
}, Se = { key: 0 }, Je = {
  key: 4,
  class: "item-content"
}, Ne = { class: "ant-upload-list ant-upload-list-picture-card" }, je = {
  class: "ant-upload-list-picture-card-container",
  style: { "margin-top": "8px" }
}, Ve = {
  class: "ant-upload-list-item ant-upload-list-item-done ant-upload-list-item-list-type-picture-card",
  "data-has-actions": "true"
}, Be = { class: "ant-upload-list-item-info" }, He = ["src", "onClick"], Ue = { class: "ant-upload-list-item-actions" }, Ae = {
  key: 5,
  class: "item-content"
}, We = { class: "ant-upload-list ant-upload-list-text" }, Re = { class: "" }, qe = { class: "ant-upload-list-item ant-upload-list-item-done ant-upload-list-item-list-type-text" }, Ke = { class: "ant-upload-list-item-info" }, Pe = { class: "ant-upload-span" }, Ye = { class: "ant-upload-text-icon" }, ze = ["href"], Qe = { class: "ant-upload-list-item-card-actions" }, Ge = {
  key: 6,
  class: "item-content"
};
function Xe(a, s, d, k, b, L) {
  const x = T("MarkdownViewer"), w = T("link-table-card"), m = T("download-outlined"), D = T("eye-outlined"), E = T("paper-clip-outlined"), c = T("a-col"), F = T("a-row");
  return f(), u("div", {
    class: j(a.formContainerClass)
  }, [
    $(F, null, {
      default: q(() => [
        (f(!0), u(S, null, V(a.schemas, (r, v) => (f(), K(c, {
          key: v,
          span: a.getItemSpan(r)
        }, {
          default: q(() => [
            r.hidden ? (f(), u(S, { key: 0 }, [], 64)) : (f(), u("div", Le, [
              p("div", {
                class: j(["item-title", a.getLabelWidthClass(r)]),
                title: r.label
              }, B(a.filterLable(r)) + "： ", 11, $e),
              r.view === "markdown" ? (f(), u("div", Ie, [
                $(x, {
                  value: a.detailFormData[r.field],
                  "onUpdate:value": (h) => a.detailFormData[r.field] = h,
                  placeholder: ""
                }, null, 8, ["value", "onUpdate:value"])
              ])) : r.isHtml ? (f(), u("div", {
                key: 1,
                class: j(["item-content", r.view]),
                innerHTML: a.detailFormData[r.field]
              }, null, 10, Oe)) : r.view == "textarea" ? (f(), u("div", {
                key: 2,
                class: "item-content",
                innerHTML: a.textareaLineBreak(a.detailFormData[r.field])
              }, null, 8, Ee)) : r.isCard ? (f(), u("div", Me, [
                a.detailFormData[r.field] ? (f(), K(w, {
                  key: 1,
                  disabled: "",
                  detail: "",
                  value: a.detailFormData[r.field],
                  valueField: r.dictCode,
                  textField: r.dictText,
                  tableName: r.dictTable,
                  multi: r.multi
                }, null, 8, ["value", "valueField", "textField", "tableName", "multi"])) : (f(), u("span", Se))
              ])) : r.isImage ? (f(), u("div", Je, [
                p("div", Ne, [
                  (f(!0), u(S, null, V(a.detailFormData[r.field], (h) => (f(), u("div", je, [
                    p("span", null, [
                      p("div", Ve, [
                        p("div", Be, [
                          p("img", {
                            src: h,
                            alt: "图片不存在",
                            class: "ant-upload-list-item-image",
                            onClick: (I) => a.handleViewImage(r.field)
                          }, null, 8, He)
                        ]),
                        p("span", Ue, [
                          $(m, {
                            onClick: (I) => a.handleDownloadFile(h)
                          }, null, 8, ["onClick"]),
                          $(D, {
                            onClick: (I) => a.handleViewImage(r.field)
                          }, null, 8, ["onClick"])
                        ])
                      ])
                    ])
                  ]))), 256))
                ])
              ])) : r.isFile ? (f(), u("div", Ae, [
                p("div", We, [
                  (f(!0), u(S, null, V(a.detailFormData[r.field], (h) => (f(), u("div", Re, [
                    p("span", null, [
                      p("div", qe, [
                        p("div", Ke, [
                          p("span", Pe, [
                            p("div", Ye, [
                              $(E)
                            ]),
                            p("a", {
                              href: h,
                              target: "_blank",
                              rel: "noopener noreferrer",
                              class: "ant-upload-list-item-name"
                            }, B(a.getFilename(h)), 9, ze),
                            p("span", Qe, [
                              $(m, {
                                onClick: (I) => a.handleDownloadFile(h)
                              }, null, 8, ["onClick"])
                            ])
                          ])
                        ])
                      ])
                    ])
                  ]))), 256))
                ])
              ])) : (f(), u("div", Ge, B(a.filter(a.detailFormData[r.field], r.view, r)), 1))
            ]))
          ]),
          _: 2
        }, 1032, ["span"]))), 128))
      ]),
      _: 1
    })
  ], 2);
}
const Ut = /* @__PURE__ */ be(Te, [["render", Xe], ["__scopeId", "data-v-05ca0a61"]]);
export {
  Ut as default
};
