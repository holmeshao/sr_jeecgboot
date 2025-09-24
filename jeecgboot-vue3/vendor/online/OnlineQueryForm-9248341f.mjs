var ke = Object.defineProperty, Se = Object.defineProperties;
var De = Object.getOwnPropertyDescriptors;
var ee = Object.getOwnPropertySymbols;
var Ye = Object.prototype.hasOwnProperty, Oe = Object.prototype.propertyIsEnumerable;
var te = (h, f, l) => f in h ? ke(h, f, { enumerable: !0, configurable: !0, writable: !0, value: l }) : h[f] = l, v = (h, f) => {
  for (var l in f || (f = {}))
    Ye.call(f, l) && te(h, l, f[l]);
  if (ee)
    for (var l of ee(f))
      Oe.call(f, l) && te(h, l, f[l]);
  return h;
}, q = (h, f) => Se(h, De(f));
var P = (h, f, l) => new Promise((d, b) => {
  var B = (_) => {
    try {
      F(l.next(_));
    } catch (D) {
      b(D);
    }
  }, j = (_) => {
    try {
      F(l.throw(_));
    } catch (D) {
      b(D);
    }
  }, F = (_) => _.done ? d(_.value) : Promise.resolve(_.value).then(B, j);
  F((l = l.apply(h, f)).next());
});
import { BasicForm as Fe, useForm as xe } from "/@/components/Form/index";
import { ref as C, reactive as ve, watch as A, toRaw as G, resolveComponent as M, openBlock as R, createElementBlock as oe, createVNode as N, withCtx as I, mergeProps as re, createElementVNode as H, createBlock as ne, toDisplayString as W, createCommentVNode as Q, createTextVNode as Pe } from "vue";
import { defHttp as Ce } from "/@/utils/http/axios";
import { useMessage as Ie } from "/@/hooks/web/useMessage";
import { i as Ve, g as je, n as ae, o as Me, F as K, p as Ne, q as Le, r as se, j as V } from "./useExtendComponent-bb98e568.mjs";
import { L as Be } from "./constant-fa63bd66.mjs";
import "/@/utils/common/compUtils";
import Ee from "/@/components/Form/src/jeecg/components/JRangeNumber.vue";
import { _ as Je } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "@ant-design/icons-vue";
import "/@/components/Modal";
import "lodash-es";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/utils/is";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/utils/auth";
import "/@/api/common/api";
import "/@/hooks/web/useAppInject";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "/@/components/Loading";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
const qe = {
  name: "OnlineQueryForm",
  components: {
    BasicForm: Fe,
    JRangeNumber: Ee
  },
  props: {
    id: {
      type: String,
      default: ""
    },
    queryBtnCfg: {
      type: Object,
      default: () => ({
        enabled: !0,
        buttonName: "查询",
        buttonIcon: "ant-design:search"
      })
    },
    resetBtnCfg: {
      type: Object,
      default: () => ({
        enabled: !0,
        buttonName: "重置",
        buttonIcon: "ant-design:reload"
      })
    }
  },
  emits: ["search", "loaded"],
  setup(h, { emit: f }) {
    const l = "/online/cgform/api/getQueryInfoVue3/", d = C(null), b = C([]), B = C({ xs: 24, sm: 24, md: 12, lg: 6, xl: 6 }), j = C(!1), F = C(!1), _ = C({}), D = C([]), { createMessage: E } = Ie(), { linkTableCard2Select: T } = Ve(), J = C(80), c = ve({
      config: {},
      cache: {},
      param: {},
      status: !1
    });
    A(
      () => c.status,
      (e) => P(this, null, function* () {
        const { config: t, cache: o, param: r } = G(c);
        let n = Object.assign({}, t, o, r);
        yield le(n);
      }),
      { immediate: !0, deep: !0 }
    );
    function y(e, t) {
      return P(this, null, function* () {
        c.cache = v({}, e), c.param = v({}, t), c.status = !c.status;
      });
    }
    A(
      () => h.id,
      (e) => {
        e ? ie() : b.value = [];
      },
      { immediate: !0 }
    );
    function w(e) {
      return P(this, null, function* () {
        var a, g, k, Y;
        let t = [], o = {}, r = Object.keys(e), n = -1;
        for (let p of r) {
          const i = e[p];
          p === "sys_org_code" && (i.fieldExtendJson || (i.fieldExtendJson = '{"store":"orgCode"}'));
          let u = i.view;
          if (i.originView = i.view, ae[u] && (i.view = ae[u]), yield Me(p, i, o), i.mode == "group" && (u == "date" || u == "datetime" || u == "number" || u == "time")) {
            let x = K.createSlotFormSchema(p, i);
            t.push(x);
          } else if (i.view === Ne) {
            let x = Le(i, p);
            for (let O of x) {
              let Z = K.createFormSchema(O.key, O), $ = se(t, O.key);
              $ == -1 ? t.push(Z) : t[$] = Z;
            }
          } else if (se(t, p) == -1) {
            let O = K.createFormSchema(p, i);
            t.push(O);
          }
          let S = i.fieldExtendJson;
          S && (S = JSON.parse(S), S.labelLength && (n > -1 ? n = S.labelLength > n ? S.labelLength : n : n = S.labelLength));
        }
        n == -1 ? n = Be : t.forEach((p) => {
          p.labelLength = n;
        }), t.sort(function(p, i) {
          return p.order - i.order;
        });
        let s = [];
        t.length > 2 && (j.value = !0);
        let m = [];
        for (let p = 0; p < t.length; p++) {
          let i = t[p];
          i.setFormRef(d), i.noChange(), i.asSearchForm(), p > 1 && (m.push(i.field), i.isHidden());
          let u = i.getFormItemSchema();
          if (i.slot == "groupDatetime" && t.length <= 3 && (u.colProps = { xs: 24, sm: 24, md: 12, lg: 8, xl: 8 }), u.component === "JSwitch") {
            const O = (a = u.componentProps) != null ? a : {};
            u.componentProps = q(v({}, O), { query: !0 });
          }
          if (T(u), u.component === "LinkTableSelect") {
            let O = (g = u.componentProps) != null ? g : {};
            u.componentProps = q(v({}, O), { editBtnShow: !1 });
          }
          const S = (k = u.componentProps) != null ? k : {};
          S.getPopupContainer || (u.componentProps = q(v({}, S), { getPopupContainer: () => document.body }));
          const x = (Y = e[u.field]) != null ? Y : {};
          x.mode == "like" && x.view === "text" && x.originView === "text" && (u.component = "JInput"), s.push(u);
        }
        D.value = m, b.value = s, c.config = v({}, o), c.status = !c.status, setTimeout(() => {
          const p = n * 14 + n + 24;
          J.value = p;
        }, 0);
      });
    }
    const L = (e) => {
      const t = e.properties;
      t && Object.entries(t).forEach(([o, r]) => {
        const n = r;
        if (["date_year", "date_month", "date_week", "date_quarter"].includes(n.view)) {
          const s = n.fieldExtendJson ? JSON.parse(n.fieldExtendJson) : {};
          s.picker = n.view.split("_")[1], n.fieldExtendJson = JSON.stringify(s), n.view = "date";
        }
      });
    };
    function ie() {
      return P(this, null, function* () {
        let e = yield ue();
        L(e);
        let t = me(e);
        f("loaded", e);
        let { formProperties: o, hasField: r } = ce(t, e);
        if (r == !1) {
          b.value = [];
          return;
        }
        yield w(o);
      });
    }
    function le(e) {
      return P(this, null, function* () {
        yield je(d);
        const t = he(e);
        yield z(t), Object.keys(t).length > 0 && U();
      });
    }
    function ce(e, t) {
      const { searchFieldList: o, joinQuery: r, table: n } = t;
      let s = !1, m = {};
      return e && Object.keys(e).map((a) => {
        o.indexOf(a) >= 0 && (r == !0 ? a.indexOf("@") < 0 ? (m[n + "@" + a] = e[a], s = !0) : (m[a] = e[a], s = !0) : a.indexOf("@") < 0 && (m[a] = e[a], s = !0));
      }), {
        formProperties: m,
        hasField: s
      };
    }
    function me(e) {
      const { properties: t, searchFieldList: o, joinQuery: r, table: n } = e;
      let s = {}, m = 1;
      return Object.keys(t).map((a) => {
        let g = t[a];
        if (g.view == "table") {
          let k = g.properties, Y = m * 100;
          Object.keys(k).map((p) => {
            let i = k[p];
            i.order = Y + Number(i.order);
            let u = a + "@" + p;
            s[u] = i;
          }), m++;
        } else
          g.order = Number(g.order), s[a] = g;
      }), s;
    }
    function ue() {
      let e = `${l}${h.id}`;
      return new Promise((t) => {
        Ce.get({ url: e }, { isTransformResponse: !1 }).then((o) => {
          o.success ? t(o.result) : (t(!1), E.warning(o.message));
        }).catch(() => {
          E.warning("获取查询条件失败!"), t(!1);
        });
      });
    }
    const [fe, { resetFields: pe, setFieldsValue: z, updateSchema: de, getFieldsValue: ge }] = xe({
      name: "online-query-form",
      schemas: b,
      showActionButtonGroup: !1,
      baseColProps: B,
      autoSubmitOnEnter: !0,
      labelWidth: J,
      wrapperCol: null,
      submitFunc() {
        U();
      }
      /* labelCol: ONL_QUERY_LABEL_COL,
      wrapperCol: ONL_QUERY_WRAPPER_COL*/
    });
    function U() {
      let e = ge();
      be(e), ye(e);
      let t = Object.assign({}, G(c.param), we(e));
      f("search", t, !0);
    }
    const he = (e) => {
      const t = v({}, e), o = b.value.filter((r) => ["groupTime", "groupDatetime", "groupNumber", "groupDate"].includes(r.slot));
      return o.length && Object.keys(t).forEach((r) => {
        let n;
        if (o.find((m) => m.field === r ? (n = r, !0) : !1)) {
          const m = t[n];
          if (typeof m == "string") {
            const a = m.split(",");
            t[n] = [...a];
          }
        }
      }), t;
    }, ye = (e) => {
      if (e) {
        const t = b.value.filter((o) => ["groupTime", "groupDatetime", "groupDate", "groupNumber"].includes(o.slot));
        t.length && Object.keys(e).forEach((o) => {
          let r;
          if (t.find((s) => s.field === o ? (r = o, !0) : !1)) {
            const s = e[r];
            if (typeof s == "string") {
              const m = s.split(",");
              e[`${r}_begin`] = m[0], e[`${r}_end`] = m[1], delete e[r];
            }
          }
        });
      }
    }, be = (e) => {
      const t = b.value.filter((o) => {
        var r;
        return ((r = o.componentProps) == null ? void 0 : r.picker) && o.componentProps.picker != "default";
      });
      t.length && Object.keys(e).forEach((o) => {
        let r;
        const n = t.find((s) => s.field === o || `${s.field}_begin` === o || `${s.field}_end` === o ? (r = o, !0) : !1);
        if (n) {
          const s = e[r];
          if (s) {
            const m = (a, g, k) => {
              const Y = n.componentProps.picker;
              Y === "year" ? k ? e[g] = V(a).endOf("year").format("YYYY-MM-DD") : e[g] = V(a).startOf("year").format("YYYY-MM-DD") : Y === "month" ? k ? e[g] = V(a).endOf("month").format("YYYY-MM-DD") : e[g] = V(a).startOf("month").format("YYYY-MM-DD") : Y === "week" ? k ? e[g] = V(a).endOf("week").format("YYYY-MM-DD") : e[g] = V(a).startOf("week").format("YYYY-MM-DD") : Y === "quarter" && (k ? e[g] = V(a).endOf("quarter").format("YYYY-MM-DD") : e[g] = V(a).startOf("quarter").format("YYYY-MM-DD"));
            };
            if ((n == null ? void 0 : n.slot) === "groupDate") {
              const a = s.split(",");
              m(a[0], `${r}_begin`, !1), m(a[1], `${r}_end`, !0), delete e[r];
            } else
              m(s, r, !1);
          }
        }
      });
    };
    function X() {
      return P(this, null, function* () {
        yield pe();
        const { config: e, param: t } = G(c);
        let o = Object.assign({}, e, t);
        return Object.keys(o).length > 0 && (yield z(o)), o;
      });
    }
    function _e() {
      return P(this, null, function* () {
        const e = yield X();
        f("search", e, !1);
      });
    }
    function we(e) {
      return Object.keys(e).map((t) => {
        e[t] && e[t] instanceof Array && (e[t] = e[t].join(","));
      }), e;
    }
    return A(
      () => F.value,
      (e) => {
        let t = D.value;
        if (t && t.length > 0) {
          let o = [];
          for (let r of t)
            o.push({
              field: r,
              show: e
            });
          de(o);
        }
      },
      { immediate: !1 }
    ), {
      onlineQueryFormRef: d,
      registerForm: fe,
      initDefaultValues: y,
      toggleButtonShow: j,
      toggleSearchStatus: F,
      doSearch: U,
      resetSearch: _e,
      queryParams: _,
      formSchemas: b,
      clearSearch: X,
      getGroupDatePlaceholder: (e) => {
        let t = ["开始日期", "结束日期"];
        if (e != null && e.picker)
          switch (e == null ? void 0 : e.picker) {
            case "year":
              t = ["开始年份", "结束年份"];
              break;
            case "month":
              t = ["开始月份", "结束月份"];
              break;
            case "week":
              t = ["开始周", "结束周"];
              break;
            case "quarter":
              t = ["开始季度", "结束季度"];
              break;
            default:
              t = ["开始日期", "结束日期"];
          }
        return t;
      }
    };
  }
}, Re = {
  key: 0,
  class: "jeecg-basic-table-form-container online-query-form p-0"
}, Qe = {
  style: { float: "left", overflow: "hidden", "margin-left": "10px" },
  class: "table-page-search-submitButtons"
};
function Te(h, f, l, d, b, B) {
  const j = M("a-range-picker"), F = M("a-time-range-picker"), _ = M("JRangeNumber"), D = M("a-button"), E = M("a-icon"), T = M("a-col"), J = M("BasicForm");
  return d.formSchemas && d.formSchemas.length > 0 ? (R(), oe("div", Re, [
    N(J, {
      ref: "onlineQueryFormRef",
      onRegister: d.registerForm
    }, {
      groupDate: I(({ model: c, field: y, schema: w }) => [
        N(j, re({
          style: { width: "100%" },
          value: c[y],
          "onUpdate:value": (L) => c[y] = L
        }, w.componentProps, {
          placeholder: d.getGroupDatePlaceholder(w.componentProps),
          valueFormat: "YYYY-MM-DD"
        }), null, 16, ["value", "onUpdate:value", "placeholder"])
      ]),
      groupDatetime: I(({ model: c, field: y }) => [
        N(j, {
          style: { width: "100%" },
          value: c[y],
          "onUpdate:value": (w) => c[y] = w,
          "show-time": !0,
          valueFormat: "YYYY-MM-DD HH:mm:ss"
        }, null, 8, ["value", "onUpdate:value"])
      ]),
      groupTime: I(({ model: c, field: y }) => [
        N(F, {
          style: { width: "100%" },
          value: c[y],
          "onUpdate:value": (w) => c[y] = w,
          "value-format": "HH:mm:ss"
        }, null, 8, ["value", "onUpdate:value"])
      ]),
      groupNumber: I(({ model: c, field: y, schema: w }) => [
        N(_, re({
          value: c[y],
          "onUpdate:value": (L) => c[y] = L
        }, w.componentProps), null, 16, ["value", "onUpdate:value"])
      ]),
      formFooter: I(() => [
        N(T, {
          md: 6,
          sm: 8
        }, {
          default: I(() => [
            H("span", Qe, [
              l.queryBtnCfg.enabled ? (R(), ne(D, {
                key: 0,
                type: "primary",
                preIcon: l.queryBtnCfg.buttonIcon,
                onClick: d.doSearch
              }, {
                default: I(() => [
                  H("span", null, W(l.queryBtnCfg.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "onClick"])) : Q("", !0),
              l.resetBtnCfg.enabled ? (R(), ne(D, {
                key: 1,
                type: "primary",
                preIcon: l.resetBtnCfg.buttonIcon,
                style: { "margin-left": "8px" },
                onClick: d.resetSearch
              }, {
                default: I(() => [
                  H("span", null, W(l.resetBtnCfg.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "onClick"])) : Q("", !0),
              d.toggleButtonShow ? (R(), oe("a", {
                key: 2,
                onClick: f[0] || (f[0] = (c) => d.toggleSearchStatus = !d.toggleSearchStatus),
                style: { "margin-left": "8px" }
              }, [
                Pe(W(d.toggleSearchStatus ? "收起" : "展开") + " ", 1),
                N(E, {
                  type: d.toggleSearchStatus ? "up" : "down"
                }, null, 8, ["type"])
              ])) : Q("", !0)
            ])
          ]),
          _: 1
        })
      ]),
      _: 1
    }, 8, ["onRegister"])
  ])) : Q("", !0);
}
const Ct = /* @__PURE__ */ Je(qe, [["render", Te], ["__scopeId", "data-v-04f16c27"]]);
export {
  Ct as default
};
