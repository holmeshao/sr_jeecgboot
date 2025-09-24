var re = Object.defineProperty;
var j = Object.getOwnPropertySymbols;
var ie = Object.prototype.hasOwnProperty, ae = Object.prototype.propertyIsEnumerable;
var N = (i, a, r) => a in i ? re(i, a, { enumerable: !0, configurable: !0, writable: !0, value: r }) : i[a] = r, V = (i, a) => {
  for (var r in a || (a = {}))
    ie.call(a, r) && N(i, r, a[r]);
  if (j)
    for (var r of j(a))
      ae.call(a, r) && N(i, r, a[r]);
  return i;
};
var k = (i, a, r) => new Promise((t, p) => {
  var c = (m) => {
    try {
      b(r.next(m));
    } catch (g) {
      p(g);
    }
  }, s = (m) => {
    try {
      b(r.throw(m));
    } catch (g) {
      p(g);
    }
  }, b = (m) => m.done ? t(m.value) : Promise.resolve(m.value).then(c, s);
  b((r = r.apply(i, a)).next());
});
import { useMessage as le } from "/@/hooks/web/useMessage";
import { nextTick as me, ref as d, reactive as R, watch as se, resolveComponent as h, openBlock as f, createElementBlock as x, createVNode as w, createCommentVNode as I, createBlock as F, withCtx as B, Fragment as ce, renderList as ue, normalizeStyle as fe, renderSlot as pe } from "vue";
import { Loading as de } from "/@/components/Loading";
import { getToken as he } from "/@/utils/auth";
import { goJmReportViewPage as be } from "/@/utils";
import { PrinterOutlined as ge } from "@ant-design/icons-vue";
import _e from "./DetailForm-c592b8d8.mjs";
import ye from "./OnlineSubFormDetail-8be879b9.mjs";
import { m as Se, u as Te } from "./useExtendComponent-bb98e568.mjs";
import { defHttp as xe } from "/@/utils/http/axios";
import { E as Ce } from "./constant-fa63bd66.mjs";
import { useAppInject as ke } from "/@/hooks/web/useAppInject";
import { _ as we } from "./index-9e1e1e53.mjs";
import "/@/utils/propTypes";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/dict/index";
import "/@/api/common/api";
import "/@/components/Form/src/utils/Area";
import "/@/utils/common/compUtils";
import "/@/components/Preview/index";
import "/@/components/Markdown";
import "/@/components/Form/index";
import "/@/components/Form/src/componentMap";
import "/@/components/Modal";
import "lodash-es";
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
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
function Fe() {
  const i = {}, a = {
    setFieldsValue: "<m> 设置表单控件的值",
    getFieldsValue: "<m> 获取表单控件的值",
    sh: "<p> 表单控件的显示隐藏状态"
  }, r = new Proxy(a, {
    get(c, s) {
      return Reflect.get(i, s);
    }
  });
  function t(c, s) {
    i[c] = s;
  }
  function p(c) {
    Object.keys(c).map((s) => {
      i[s] = c[s];
    });
  }
  return t("$nextTick", me), t("addObject2Context", t), { onlineFormDetailContext: r, addObject2Context: t, resetContext: p };
}
const Pe = {
  name: "OnlineFormDetail",
  components: {
    DetailForm: _e,
    Loading: de,
    PrinterOutlined: ge,
    OnlineSubFormDetail: ye
  },
  props: {
    id: {
      type: String,
      default: ""
    },
    formTemplate: {
      type: Number,
      default: 1
    },
    disabled: {
      type: Boolean,
      default: !1
    },
    isTree: {
      type: Boolean,
      default: !1
    },
    pidField: {
      type: String,
      default: ""
    },
    submitTip: {
      type: Boolean,
      default: !0
    },
    showSub: {
      type: Boolean,
      default: !0
    },
    themeTemplate: {
      type: String,
      default: ""
    }
  },
  emits: ["success", "rendered"],
  setup(i, { emit: a }) {
    const { createMessage: r } = le(), { getIsMobile: t } = ke(), p = d(""), c = d(!0), s = d(!1), b = d(1), m = d({}), g = d(t.value ? "auto" : 300), P = d(340), O = d(!t.value);
    let _ = {};
    const y = R({}), l = R({
      reportPrintShow: 0,
      reportPrintUrl: "",
      joinQuery: 0,
      modelFullscreen: 0,
      modalMinWidth: ""
    }), { detailFormSchemas: S, hasSubTable: M, subTabInfo: H, refMap: L, showStatus: v, subDataSource: D, createFormSchemas: U, formSpan: z } = Se(i);
    function A(e) {
      let o = { reportPrintShow: 0, reportPrintUrl: "", joinQuery: 0, modelFullscreen: 1, modalMinWidth: "" };
      e && (o = JSON.parse(e)), Object.keys(o).map((n) => {
        l[n] = o[n];
      });
    }
    const { onlineFormDetailContext: E, resetContext: K } = Fe();
    let { EnhanceJS: C, initCgEnhanceJs: Q } = Te(E, !1);
    function W(e) {
      return k(this, null, function* () {
        b.value = e.head.tableType, p.value = e.head.tableName, c.value = e.head.tableType == 1, A(e.head.extConfigJson), U(e.schema.properties), C = Q(e.enhanceJs), a("rendered", l);
      });
    }
    function X(e, o) {
      return k(this, null, function* () {
        yield Y(o), J(!0);
      });
    }
    function q(e) {
      let o = `/online/cgform/api/detail/${i.id}/${e}`;
      return new Promise((n, u) => {
        xe.get({ url: o }, { isTransformResponse: !1 }).then((T) => {
          T.success ? n(T.result) : (u(), r.warning(T.message));
        }).catch(() => {
          u();
        });
      });
    }
    function J(e) {
      Object.keys(v).map((o) => {
        v[o] = e;
      });
    }
    function G() {
      J(!1), setTimeout(() => {
        J(!0);
      }, 300);
    }
    function Y(e) {
      return k(this, null, function* () {
        _ = yield q(e.id), S.value.filter((n) => n.hidden).forEach((n) => n.hidden = !1), Object.keys(y).forEach(function(n) {
          delete y[n];
        }), ne({ buttonCode: "loaded" }), m.value = V({}, _), Z(_);
      });
    }
    function Z(e) {
      e || (e = {});
      let o = Object.keys(D.value);
      if (o && o.length > 0) {
        let n = {};
        for (let u of o)
          n[u] = e[u] || [];
        D.value = n;
      }
    }
    function $(e) {
      return "online_" + e + ":";
    }
    function ee() {
      let e = l.reportPrintUrl, o = m.value;
      if (o) {
        let n = o.id, u = he();
        be(e, n, u);
      }
    }
    function te(e) {
      let o = m.value;
      return oe(o, e);
    }
    function oe(e, o) {
      if (e) {
        let n = e[o];
        return !n && n !== 0 && (n = e[o.toLowerCase()], !n && n !== 0 && (n = e[o.toUpperCase()])), n;
      }
      return "";
    }
    function ne({ buttonCode: e }) {
      C && C[e] && C[e].call(E, E);
    }
    return se(y, (e) => {
      Object.entries(e).forEach(([o, n]) => {
        if (n == !1) {
          const u = S.value.find((T) => T.field === o);
          u && (u.hidden = !0);
        }
      });
    }), K({
      setFieldsValue: (e) => {
        Object.entries(e).forEach(([o, n]) => {
          _[o] = n;
        });
      },
      getFieldsValue: () => V({}, _),
      sh: y
    }), {
      detailFormSchemas: S,
      formData: m,
      formSpan: z,
      //主表
      tableName: p,
      loading: s,
      //子表
      hasSubTable: M,
      subTabInfo: H,
      subFormHeight: g,
      subTableHeight: P,
      refMap: L,
      onTabChange: G,
      //一对多子表
      subDataSource: D,
      getSubTableAuthPre: $,
      //父组件调用
      show: X,
      createRootProperties: W,
      // 扩展配置
      onOpenReportPrint: ee,
      onlineExtConfigJson: l,
      getSubTableForeignKeyValue: te,
      showStatus: v,
      ERP: Ce,
      rowNumber: O
    };
  }
}, Oe = ["id"], ve = {
  key: 0,
  style: { "text-align": "right", position: "absolute", top: "15px", right: "20px", "z-index": "999" }
}, De = { key: 1 };
function Ee(i, a, r, t, p, c) {
  const s = h("PrinterOutlined"), b = h("detail-form"), m = h("online-sub-form-detail"), g = h("JVxeTable"), P = h("a-spin"), O = h("a-tab-pane"), _ = h("a-tabs"), y = h("Loading");
  return f(), x("div", {
    id: t.tableName + "_form"
  }, [
    t.formData.id && t.onlineExtConfigJson.reportPrintShow ? (f(), x("div", ve, [
      w(s, {
        title: "打印",
        onClick: t.onOpenReportPrint,
        style: { "font-size": "16px" }
      }, null, 8, ["onClick"])
    ])) : I("", !0),
    w(b, {
      schemas: t.detailFormSchemas,
      data: t.formData,
      span: t.formSpan
    }, null, 8, ["schemas", "data", "span"]),
    r.themeTemplate !== t.ERP && t.hasSubTable && r.showSub ? (f(), F(_, {
      key: 1,
      onChange: t.onTabChange
    }, {
      default: B(() => [
        (f(!0), x(ce, null, ue(t.subTabInfo, (l, S) => (f(), F(O, {
          tab: l.describe,
          key: S + "",
          forceRender: !0
        }, {
          default: B(() => [
            l.relationType == 1 ? (f(), x("div", {
              key: 0,
              style: fe({ "overflow-y": "auto", "overflow-x": "hidden", "max-height": t.subFormHeight + "px" })
            }, [
              w(m, {
                table: l.key,
                "form-template": r.formTemplate,
                "main-id": t.getSubTableForeignKeyValue(l.foreignKey),
                properties: l.properties
              }, null, 8, ["table", "form-template", "main-id", "properties"])
            ], 4)) : (f(), x("div", De, [
              t.showStatus[l.key] ? (f(), F(g, {
                key: 0,
                ref_for: !0,
                ref: t.refMap[l.key],
                "keep-source": "",
                "row-number": t.rowNumber,
                "row-selection": "",
                height: t.subTableHeight,
                disabled: !0,
                columns: l.columns,
                dataSource: t.subDataSource[l.key],
                authPre: t.getSubTableAuthPre(l.key)
              }, null, 8, ["row-number", "height", "columns", "dataSource", "authPre"])) : (f(), F(P, {
                key: 1,
                spinning: !0
              }))
            ]))
          ]),
          _: 2
        }, 1032, ["tab"]))), 128))
      ]),
      _: 1
    }, 8, ["onChange"])) : I("", !0),
    w(y, {
      loading: t.loading,
      absolute: !1
    }, null, 8, ["loading"]),
    pe(i.$slots, "bottom")
  ], 8, Oe);
}
const xt = /* @__PURE__ */ we(Pe, [["render", Ee]]);
export {
  xt as default
};
