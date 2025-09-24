var Y = Object.defineProperty;
var K = Object.getOwnPropertySymbols;
var Z = Object.prototype.hasOwnProperty, $ = Object.prototype.propertyIsEnumerable;
var N = (i, r, n) => r in i ? Y(i, r, { enumerable: !0, configurable: !0, writable: !0, value: n }) : i[r] = n, V = (i, r) => {
  for (var n in r || (r = {}))
    Z.call(r, n) && N(i, n, r[n]);
  if (K)
    for (var n of K(r))
      $.call(r, n) && N(i, n, r[n]);
  return i;
};
var v = (i, r, n) => new Promise((o, b) => {
  var S = (l) => {
    try {
      c(n.next(l));
    } catch (f) {
      b(f);
    }
  }, h = (l) => {
    try {
      c(n.throw(l));
    } catch (f) {
      b(f);
    }
  }, c = (l) => l.done ? o(l.value) : Promise.resolve(l.value).then(S, h);
  c((n = n.apply(i, r)).next());
});
import { useMessage as ee } from "/@/hooks/web/useMessage";
import { ref as s, watch as te, reactive as oe, resolveComponent as u, openBlock as p, createElementBlock as _, createVNode as y, createCommentVNode as I, withCtx as B, createBlock as w, Fragment as ne, renderList as re, normalizeStyle as ae, renderSlot as ie } from "vue";
import { Loading as le } from "/@/components/Loading";
import { getToken as me } from "/@/utils/auth";
import { goJmReportViewPage as se } from "/@/utils";
import { PrinterOutlined as pe } from "@ant-design/icons-vue";
import ue from "./DetailForm-c592b8d8.mjs";
import ce from "./OnlineSubFormDetail-8be879b9.mjs";
import { m as fe } from "./useExtendComponent-bb98e568.mjs";
import { defHttp as de } from "/@/utils/http/axios";
import { E as be, T as ge } from "./constant-fa63bd66.mjs";
import { useAppInject as he } from "/@/hooks/web/useAppInject";
import { _ as _e } from "./index-9e1e1e53.mjs";
import "/@/utils/propTypes";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/dict/index";
import "/@/api/common/api";
import "/@/components/Form/src/utils/Area";
import "/@/utils/common/compUtils";
import "/@/components/Preview/index";
import "/@/components/Markdown";
import "/@/components/Form/src/componentMap";
import "/@/components/Modal";
import "/@/components/Form/index";
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
const ye = {
  name: "OnlineTabFormDetail",
  components: {
    DetailForm: ue,
    Loading: le,
    PrinterOutlined: pe,
    OnlineSubFormDetail: ce
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
    },
    tabIndex: {
      type: String,
      default: ""
    }
  },
  emits: ["success", "rendered"],
  setup(i, { emit: r }) {
    const { createMessage: n } = ee(), { getIsMobile: o } = he(), b = s(""), S = s(!0), h = s(!1), c = s(1), l = s({}), f = s("auto"), k = s(340), T = s("0"), x = s(!o.value);
    te(
      () => i.tabIndex,
      (e, t) => {
        T.value = e, t && J();
      },
      {
        immediate: !0
      }
    );
    const g = oe({
      reportPrintShow: 0,
      reportPrintUrl: "",
      joinQuery: 0,
      modelFullscreen: 0,
      modalMinWidth: ""
    }), { detailFormSchemas: m, hasSubTable: P, subTabInfo: A, refMap: E, showStatus: F, subDataSource: C, createFormSchemas: M, formSpan: R } = fe(i);
    function j(e) {
      let t = { reportPrintShow: 0, reportPrintUrl: "", joinQuery: 0, modelFullscreen: 1, modalMinWidth: "" };
      e && (t = JSON.parse(e)), Object.keys(t).map((a) => {
        g[a] = t[a];
      });
    }
    function H(e) {
      return v(this, null, function* () {
        c.value = e.head.tableType, b.value = e.head.tableName, S.value = e.head.tableType == 1, j(e.head.extConfigJson), M(e.schema.properties), r("rendered", g);
      });
    }
    function L(e, t) {
      return v(this, null, function* () {
        yield z(t), D(!0);
      });
    }
    function U(e) {
      let t = `/online/cgform/api/detail/${i.id}/${e}`;
      return new Promise((a, d) => {
        de.get({ url: t }, { isTransformResponse: !1 }).then((O) => {
          O.success ? a(O.result) : (d(), n.warning(O.message));
        }).catch(() => {
          d();
        });
      });
    }
    function D(e) {
      Object.keys(F).map((t) => {
        F[t] = e;
      });
    }
    function J() {
      D(!1), setTimeout(() => {
        D(!0);
      }, 300);
    }
    function z(e) {
      return v(this, null, function* () {
        let t = yield U(e.id);
        l.value = V({}, t), Q(t);
      });
    }
    function Q(e) {
      e || (e = {});
      let t = Object.keys(C.value);
      if (t && t.length > 0) {
        let a = {};
        for (let d of t)
          a[d] = e[d] || [];
        C.value = a;
      }
    }
    function W(e) {
      return "online_" + e + ":";
    }
    function q() {
      let e = g.reportPrintUrl, t = l.value;
      if (t) {
        let a = t.id, d = me();
        se(e, a, d);
      }
    }
    function G(e) {
      let t = l.value;
      return X(t, e);
    }
    function X(e, t) {
      if (e) {
        let a = e[t];
        return !a && a !== 0 && (a = e[t.toLowerCase()], !a && a !== 0 && (a = e[t.toUpperCase()])), a;
      }
      return "";
    }
    return {
      detailFormSchemas: m,
      formData: l,
      formSpan: R,
      //主表
      tableName: b,
      loading: h,
      //子表
      hasSubTable: P,
      subTabInfo: A,
      subFormHeight: f,
      subTableHeight: k,
      refMap: E,
      onTabChange: J,
      //一对多子表
      subDataSource: C,
      getSubTableAuthPre: W,
      //父组件调用
      show: L,
      createRootProperties: H,
      // 扩展配置
      onOpenReportPrint: q,
      onlineExtConfigJson: g,
      getSubTableForeignKeyValue: G,
      showStatus: F,
      ERP: be,
      TAB: ge,
      subActiveKey: T,
      rowNumber: x
    };
  }
};
const Se = ["id"], Te = {
  key: 0,
  style: { "text-align": "right", position: "absolute", top: "15px", right: "20px", "z-index": "999" }
}, ve = { key: 1 };
function we(i, r, n, o, b, S) {
  const h = u("PrinterOutlined"), c = u("detail-form"), l = u("a-tab-pane"), f = u("online-sub-form-detail"), k = u("JVxeTable"), T = u("a-spin"), x = u("a-tabs"), g = u("Loading");
  return p(), _("div", {
    id: o.tableName + "_form"
  }, [
    o.formData.id && o.onlineExtConfigJson.reportPrintShow ? (p(), _("div", Te, [
      y(h, {
        title: "打印",
        onClick: o.onOpenReportPrint,
        style: { "font-size": "16px" }
      }, null, 8, ["onClick"])
    ])) : I("", !0),
    y(x, {
      class: "tabTheme",
      onChange: o.onTabChange,
      activeKey: o.subActiveKey,
      "onUpdate:activeKey": r[0] || (r[0] = (m) => o.subActiveKey = m)
    }, {
      default: B(() => [
        (p(), w(l, {
          tab: "主表",
          key: "-1"
        }, {
          default: B(() => [
            y(c, {
              schemas: o.detailFormSchemas,
              data: o.formData,
              span: o.formSpan
            }, null, 8, ["schemas", "data", "span"])
          ]),
          _: 1
        })),
        o.hasSubTable && n.showSub ? (p(!0), _(ne, { key: 0 }, re(o.subTabInfo, (m, P) => (p(), w(l, {
          tab: m.describe,
          key: P + "",
          forceRender: !0
        }, {
          default: B(() => [
            m.relationType == 1 ? (p(), _("div", {
              key: 0,
              style: ae({ "overflow-y": "auto", "overflow-x": "hidden", "max-height": o.subFormHeight + "px" })
            }, [
              y(f, {
                table: m.key,
                "form-template": n.formTemplate,
                "main-id": o.getSubTableForeignKeyValue(m.foreignKey),
                properties: m.properties
              }, null, 8, ["table", "form-template", "main-id", "properties"])
            ], 4)) : (p(), _("div", ve, [
              o.showStatus[m.key] ? (p(), w(k, {
                key: 0,
                ref_for: !0,
                ref: o.refMap[m.key],
                toolbar: "",
                "keep-source": "",
                "row-number": o.rowNumber,
                "row-selection": "",
                height: o.subTableHeight,
                disabled: !0,
                columns: m.columns,
                dataSource: o.subDataSource[m.key],
                authPre: o.getSubTableAuthPre(m.key)
              }, null, 8, ["row-number", "height", "columns", "dataSource", "authPre"])) : (p(), w(T, {
                key: 1,
                spinning: !0
              }))
            ]))
          ]),
          _: 2
        }, 1032, ["tab"]))), 128)) : I("", !0)
      ]),
      _: 1
    }, 8, ["onChange", "activeKey"]),
    y(g, {
      loading: o.loading,
      absolute: !1
    }, null, 8, ["loading"]),
    ie(i.$slots, "bottom", {}, void 0, !0)
  ], 8, Se);
}
const ft = /* @__PURE__ */ _e(ye, [["render", we], ["__scopeId", "data-v-60a1e2da"]]);
export {
  ft as default
};
