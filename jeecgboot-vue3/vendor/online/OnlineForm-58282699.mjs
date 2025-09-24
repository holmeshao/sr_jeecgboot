var Qt = Object.defineProperty, Xt = Object.defineProperties;
var Zt = Object.getOwnPropertyDescriptors;
var Me = Object.getOwnPropertySymbols;
var $t = Object.prototype.hasOwnProperty, en = Object.prototype.propertyIsEnumerable;
var De = (u, p, s) => p in u ? Qt(u, p, { enumerable: !0, configurable: !0, writable: !0, value: s }) : u[p] = s, w = (u, p) => {
  for (var s in p || (p = {}))
    $t.call(p, s) && De(u, s, p[s]);
  if (Me)
    for (var s of Me(p))
      en.call(p, s) && De(u, s, p[s]);
  return u;
}, ce = (u, p) => Xt(u, Zt(p));
var _ = (u, p, s) => new Promise((a, R) => {
  var E = (F) => {
    try {
      O(s.next(F));
    } catch (S) {
      R(S);
    }
  }, B = (F) => {
    try {
      O(s.throw(F));
    } catch (S) {
      R(S);
    }
  }, O = (F) => F.done ? a(F.value) : Promise.resolve(F.value).then(E, B);
  O((s = s.apply(u, p)).next());
});
import { useMessage as tn } from "/@/hooks/web/useMessage";
import { ref as v, inject as nn, reactive as ln, watch as on, computed as an, resolveComponent as D, openBlock as V, createElementBlock as te, normalizeClass as rn, createVNode as H, createBlock as ne, withCtx as G, Fragment as sn, renderList as un, normalizeStyle as cn, createElementVNode as Ve, toDisplayString as Be, createCommentVNode as fe, renderSlot as fn, unref as Ie, nextTick as je, toRaw as J } from "vue";
import { BasicForm as dn, useForm as mn } from "/@/components/Form/index";
import { c as pn, O as gn, d as hn, e as bn, u as yn, l as Y, k as vn, g as Fn, V as z, S as Sn, f as wn, h as Ne, j as de } from "./useExtendComponent-bb98e568.mjs";
import { defHttp as le } from "/@/utils/http/axios";
import { pick as kn, omit as Je, debounce as Tn, cloneDeep as Cn } from "lodash-es";
import { sleep as _n, goJmReportViewPage as On } from "/@/utils";
import { Loading as Pn } from "/@/components/Loading";
import "/@/components/jeecg/JVxeTable/types";
import { getToken as An } from "/@/utils/auth";
import { PrinterOutlined as Rn } from "@ant-design/icons-vue";
import "/@/hooks/core/useContext";
import "/@/utils/mitt";
import { useModal as En } from "/@/components/Modal";
import { u as xn, G as Mn } from "./useCustomHook-acb00837.mjs";
import { E as oe } from "./constant-fa63bd66.mjs";
import { useAppInject as Dn } from "/@/hooks/web/useAppInject";
import { isArray as Vn } from "/@/utils/is";
import { usePermissionStore as Bn } from "/@/store/modules/permission";
import "./OnlineForm.vue_vue_type_style_index_0_scoped_3f26e7bd_lang-4ed993c7.mjs";
import { _ as In } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "/@/utils/common/compUtils";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils/desform/customExpression";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/api/common/api";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "/@/utils/cache";
const me = {
  optPre: "/online/cgform/api/form/",
  urlButtonAction: "/online/cgform/api/doButton"
}, jn = {
  name: "OnlineForm",
  components: {
    BasicForm: dn,
    Loading: Pn,
    OnlineSubForm: pn,
    PrinterOutlined: Rn,
    OnlinePopModal: gn
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
    modalClass: {
      type: String,
      default: ""
    },
    themeTemplate: {
      type: String,
      default: ""
    },
    // update-begin--author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
    // erp风格会传来所有子表数据
    subTableSource: {
      default: () => ({})
    },
    // update-end-author:liaozhiyang---date:20231128---for：【QQYUN-7260】erp主表编辑时保存子表记录
    // -update-begin--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
    // 流程会传taskId
    taskId: {
      type: String
    },
    // -update-end--author:liaozhiyang---date:20240613---for：【TV360X-1000】流程一对多走流程的接口
    cgBIBtnMap: Object,
    buttonSwitch: Object
  },
  emits: ["success", "rendered", "close"],
  setup(u, { emit: p }) {
    const { createMessage: s } = tn(), a = v(null), R = v(!0), E = v(!1), B = v(1), O = v(""), F = v(!1), S = v(!1), { getIsMobile: I } = Dn(), Q = v(!1), L = nn("foreignkey", { value: {} }), ae = v(!I.value), c = v(null);
    let U = null;
    const y = ln({
      reportPrintShow: 0,
      reportPrintUrl: "",
      joinQuery: 0,
      modelFullscreen: 0,
      modalMinWidth: "",
      commentStatus: 0
    }), {
      onlineFormContext: b,
      resetContext: Le,
      getSubAddBtnCfg: Ue,
      getSubRemoveBtnCfg: Ke,
      getSubOpenAddBtnCfg: Ye,
      getSubOpenEditBtnCfg: We
    } = hn(u), {
      formSchemas: X,
      defaultValueFields: W,
      changeDataIfArray2String: pe,
      tableName: x,
      dbData: M,
      checkOnlyFieldValue: qe,
      hasSubTable: He,
      subTabInfo: k,
      refMap: P,
      subDataSource: Z,
      baseColProps: Ge,
      createFormSchemas: ze,
      linkDownList: Un,
      fieldDisplayStatus: K,
      labelCol: Qe,
      wrapperCol: Xe,
      labelWidth: Ze
    } = bn(u, a);
    let { EnhanceJS: f, initCgEnhanceJs: $e } = yn(b, !1);
    on(
      [k, I],
      () => {
        I.value && k.value.length && k.value.forEach((e) => {
          e.relationType != 1 && e.columns.forEach((t) => {
            t.width = 100;
          });
        });
      },
      { immediate: !0 }
    );
    const { executeJsEnhanced: et } = xn({}, b), [tt, { setProps: nt, validate: ge, resetFields: he, clearValidate: lt, setFieldsValue: A, updateSchema: q, getFieldsValue: $, scrollToField: be }] = mn({
      schemas: X,
      showActionButtonGroup: !1,
      baseColProps: Ge,
      // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
      labelWidth: Ze,
      // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
      // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      labelCol: Qe,
      wrapperCol: Xe
      // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
    }), ye = v(!1);
    function ot() {
      let e = u.disabled;
      ye.value = e, nt({ disabled: e });
    }
    function at(e, t, n) {
      return _(this, null, function* () {
        yield it(), O.value = "", yield he(), setTimeout(() => {
          lt();
        }, 0), M.value = "";
        let l = Ie(e);
        S.value = l, ct(), l ? yield Fe(t) : Se(), je(() => {
          var o;
          !l && n && A(n), rt(), ie("js", "loaded"), ot(), (o = c.value) != null && o.length && (c.value[0].scrollTop = 0);
        });
      });
    }
    function it() {
      return _(this, null, function* () {
        if (u.isTree === !0) {
          let e = u.pidField, t = X.value;
          t && t.length > 0 && t.filter((l) => l.field === e).length > 0 && (yield q({
            field: e,
            componentProps: {
              reload: (/* @__PURE__ */ new Date()).getTime(),
              // update-begin--author:liaozhiyang---date:20240529---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
              hiddenNodeKey: ""
              // update-end--author:liaozhiyang---date:20240529---for：【TV360X-87】树表编辑时不可选自己及子孙节点当父节点
            }
          }));
        }
      });
    }
    const j = {
      keys: [],
      map: /* @__PURE__ */ new Map(),
      calcFn: /* @__PURE__ */ new Map()
    };
    function rt() {
      let e = J(W[x.value]);
      Ie(S) === !1 && Y(e, (n) => {
        A(n);
      });
      const t = vn(e);
      j.keys = [...t.keys()], j.map = t, j.calcFn.clear();
    }
    function st(e, t) {
      if (j.keys.includes(e)) {
        let n = j.calcFn.get(e);
        typeof n != "function" && (n = Tn(() => {
          let l = J(W[x.value]);
          if (Array.isArray(l) && l.length > 0) {
            const o = j.map.get(e);
            l = l.filter((i) => o.includes(i.field));
          } else
            l = [];
          if (l.length > 0) {
            let o = $();
            Y(l, (i) => A(i), o);
          }
        }, 150), j.calcFn.set(e, n)), n(t);
      }
    }
    function ve(e, t) {
      let n = J(W[e.key]);
      Y(n, (l) => {
        const { row: o, target: i } = t;
        let d = [{ rowKey: o.id, values: w({}, l) }];
        i.setValues(d);
      });
    }
    function Fe(e) {
      return _(this, null, function* () {
        let t = yield dt(e.id);
        M.value = Object.assign({}, e, t);
        let n = ft.value, l = kn(t, ...n);
        u.disabled && Object.keys(l).map((o) => {
          !l[o] && l[o] !== 0 && l[o] !== "0" && delete l[o];
        }), yield A(l), ut(e.id), Se(t);
      });
    }
    function Se(e) {
      e || (e = {});
      let t = Object.keys(Z.value);
      if (t && t.length > 0) {
        let n = {};
        for (let l of t)
          n[l] = e[l] || [];
        Z.value = n;
      }
    }
    function ut(e) {
      var t;
      if (u.isTree === !0) {
        const { schema: n } = U, l = (t = n.properties) != null ? t : {}, o = Object.entries(l);
        if (o.length) {
          const i = o.find(([d, m]) => m.view === "sel_tree" && m.pidComponent != null);
          if (i) {
            const d = i[0];
            X.value.find((r) => r.field == d) && q({
              field: d,
              componentProps: {
                hiddenNodeKey: e
              }
            });
          }
        }
      }
    }
    function ct() {
      var e;
      (e = k.value) == null || e.forEach((t) => {
        t.relationType == 1 && P[t.key].value && P[t.key].value[0].resetFields();
      });
    }
    let ft = an(() => {
      let e = X.value, t = [];
      for (let n of e)
        t.push(n.field);
      return t;
    });
    function dt(e) {
      let t = `${me.optPre}${u.id}/${e}`;
      return new Promise((n, l) => {
        le.get({ url: t }, { isTransformResponse: !1 }).then((o) => {
          o.success ? n(o.result) : (l(), s.warning(o.message));
        }).catch(() => {
          l();
        });
      });
    }
    function mt(e) {
      return _(this, null, function* () {
        B.value = e.head.tableType, x.value = e.head.tableName, R.value = e.head.tableType == 1, ht(e.head.extConfigJson), ze(e.schema.properties, e.schema.required, qe, y), f = $e(e.enhanceJs), p("rendered", y);
        let t = yield Fn(a);
        t.$formValueChange = (n, l, o) => {
          Mt(n, l), o && A(o), pt(n, l, o), st(n, l);
        }, f && f.setup && Pe(f.setup), U = e;
      });
    }
    function pt(e, t, n) {
      b.changEvent(e, t, n);
    }
    function gt(e) {
      b.addObject2Context("changEvent", e);
    }
    function ht(e) {
      let t = { reportPrintShow: 0, reportPrintUrl: "", joinQuery: 0, modelFullscreen: 0, modalMinWidth: "", commentStatus: 0, formLabelLength: null };
      e && (t = JSON.parse(e), I.value && (t.commentStatus = 0)), Q.value = !!t.formLabelLength, Object.keys(t).map((n) => {
        y[n] = t[n];
      });
    }
    function bt() {
      F.value = !0, we();
    }
    function we() {
      R.value === !0 ? wt() : yt();
    }
    function yt() {
      vt().then((e) => {
        Te(e);
      });
    }
    function vt() {
      let e = {};
      return new Promise((t, n) => {
        ge().then(
          (l) => t(l),
          ({ errorFields: l, values: o }) => {
            n({
              // update-begin--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
              errorFields: l,
              values: o,
              // update-end--author:liaozhiyang---date:20240617---for：【TV360X-496】使用数值类型，金额校验，控件默认值得出的是小数导致校验过不去给提示
              code: z,
              key: x.value,
              // 滚动到未通过校验的字段上
              scrollToField: () => l[0] && be(l[0].name, { behavior: "smooth", block: "center" })
            });
          }
        );
      }).then((t) => (Object.assign(e, pe(t)), u.themeTemplate === oe ? Promise.resolve({}) : St())).then((t) => (Object.assign(e, t), Promise.resolve(e))).catch((t) => ((t === z || (t == null ? void 0 : t.code) === z) && (ke(t.errorFields, t.values, R.value, t.key).then((n) => {
        n || s.warning("校验未通过");
      }), t.key && (Ft(t.key), t.scrollToField && setTimeout(() => t.scrollToField(), 150))), Promise.reject(null)));
    }
    function Ft(e) {
      let t = k.value;
      for (let n = 0; n < t.length; n++)
        if (e == t[n].key) {
          let l = n + "";
          if (ee.value === l)
            break;
          if (ee.value = l, t[n].relationType === 0) {
            let o = T(e);
            _n(300, () => o == null ? void 0 : o.validateTable());
          }
          break;
        }
    }
    function St() {
      return new Promise((e, t) => _(this, null, function* () {
        let n = {};
        try {
          let l = k.value;
          for (let o = 0; o < l.length; o++) {
            let i = l[o].key, d = T(i);
            if (l[o].relationType == 1)
              try {
                let m = yield d.getAll();
                n[i] = [], n[i].push(m);
              } catch (m) {
                return t(w({ code: z, key: i }, m));
              }
            else {
              if (yield d.fullValidateTable())
                return t({ code: z, key: i });
              n[i] = d.getTableData();
            }
          }
        } catch (l) {
          t(l);
        }
        e(n);
      }));
    }
    function wt() {
      return _(this, null, function* () {
        try {
          let e = yield ge();
          e = Object.assign({}, M.value, e), e = pe(e), E.value = !0, Te(e);
        } catch (e) {
          Array.isArray(e == null ? void 0 : e.errorFields) && e.errorFields[0] && (be(e.errorFields[0].name, { behavior: "smooth", block: "center" }), ke(e.errorFields, e.values, R.value));
        } finally {
          E.value = !1, p("close");
        }
      });
    }
    function ke(e, t, n, l = null) {
      return _(this, null, function* () {
        var i;
        let o = !1;
        if (e != null && e.length) {
          const d = (i = U.schema) != null ? i : {}, { properties: m = {} } = d, r = e[0].name[0];
          let h;
          if (n || l === x.value)
            h = m[r];
          else {
            const g = m[l], { properties: C = {} } = g;
            h = C[r];
          }
          h.type === "number" && h.view === "number" && h.defVal && (yield Y(
            [
              {
                field: r,
                type: h.type,
                value: h.defVal,
                view: h.view
              }
            ],
            (g) => {
              g[r] === t[r] && (s.warning(`${h.title}的默认值是：${t[r]}，导致校验通不过，需要正确配置默认值！`), o = !0);
            }
          ));
        }
        return o;
      });
    }
    function Te(e) {
      u.themeTemplate === oe && S.value && Object.keys(u.subTableSource).length && (e = w(w({}, e), u.subTableSource)), Nt(xe, e).then(() => {
        Tt(e);
      }).catch((t) => {
        s.warning(t);
      });
    }
    const kt = (e) => {
      const { schema: t } = U, { properties: n } = t, l = (o, i) => {
        Object.entries(o).forEach(([d, m]) => {
          var h;
          const r = i[d];
          if (r) {
            if (r.view === "tab" && Vn(m)) {
              if (r.properties && m.forEach((g) => {
                l(g, r.properties);
              }), (h = r.columns) != null && h.length) {
                const g = Cn(r.columns.filter((C) => C.type === "date" && C.fieldExtendJson));
                if (g.length) {
                  const C = {};
                  g.forEach((N) => {
                    C[N.key] = {
                      view: "date",
                      fieldExtendJson: N.fieldExtendJson
                    };
                  }), m.forEach((N) => {
                    l(N, C);
                  });
                }
              }
            } else if (r.view === "date" && typeof m == "string" && m !== "") {
              let g = r.fieldExtendJson;
              g && (g = JSON.parse(g), g.picker && g.picker !== "default" && (g.picker === "year" ? o[d] = de(m).set("month", 0).set("date", 1).format("YYYY-MM-DD") : g.picker === "month" ? o[d] = de(m).set("date", 1).format("YYYY-MM-DD") : g.picker === "week" && (o[d] = de(m).startOf("week").format("YYYY-MM-DD"))));
            }
          }
        });
      };
      l(e, n);
    };
    function Tt(e) {
      Object.keys(e).map((o) => {
        Array.isArray(e[o]) && e[o].length == 0 && (e[o] = "");
      }), kt(e);
      let t = O.value, n = `${me.optPre}${u.id}?tabletype=${B.value}`;
      t && (n = `${t}?tabletype=${B.value}`), F.value === !0 && (e[Sn] = 1), L.value.field && L.value.value && (e[L.value.field] = L.value.value);
      let l = S.value === !0 ? "put" : "post";
      le.request({ url: n, method: l, params: e }, { isTransformResponse: !1 }).then((o) => {
        o.success ? (o.result && (e[wn] = o.result), p("success", e), u.submitTip === !0 && s.success(o.message)) : s.warning(o.message);
      }).finally(() => {
        E.value = !1, p("close");
      });
    }
    function Ct(e, t, n) {
      t && n ? n.vxeProps ? n.setValues([
        {
          rowKey: t,
          values: e
        }
      ]) : n.setValues(e) : A(e);
    }
    function _t(e, t) {
      let n = {};
      n[e] = t, A(n);
    }
    const ee = v("0"), Ce = v(I.value ? "auto" : 500), _e = v(340);
    function Ot(e) {
      if (S.value === !0) {
        let t = M.value;
        return Pt(t, e);
      }
      return "";
    }
    function Pt(e, t) {
      if (e) {
        let n = e[t];
        return !n && n !== 0 && (n = e[t.toLowerCase()], !n && n !== 0 && (n = e[t.toUpperCase()])), n;
      }
      return "";
    }
    function At(e, t) {
      if (f && f[t + "_onlChange"]) {
        let n = f[t + "_onlChange"](), l = Object.keys(e)[0];
        if (n[l]) {
          let i = T(t).getFormEvent(), d = w({
            column: { key: l },
            value: e[l]
          }, i);
          n[l].call(b, b, d);
        }
      }
    }
    function Rt(e, t) {
      if (f && f[t + "_onlChange"]) {
        let n = f[t + "_onlChange"](b);
        if (e.column === "all") {
          let l = Object.keys(n);
          if (l.length > 0)
            for (let o of l)
              n[o].call(b, b, e);
        } else {
          let l = e.column.key || e.col.key;
          n[l] && e.row && e.row.id && n[l].call(b, b, e);
        }
      }
    }
    function Et(e, t) {
      var n;
      if (f && f[t + "_onlChange"]) {
        let l = f[t + "_onlChange"](b), o = Object.keys(l);
        if (o.length > 0)
          for (let i of o)
            (n = l[i]) == null || n.call(b, b, ce(w({}, e), { row: e.deleteRows }));
      }
    }
    function xt(e, t) {
      t.isModalData || ve(e, t);
    }
    function Oe(e) {
      return "online_" + e + ":";
    }
    function Mt(e, t) {
      return _(this, null, function* () {
        if (!f || !f.onlChange || !e)
          return !1;
        let n = f.onlChange();
        n[e] && setTimeout(() => _(this, null, function* () {
          let o = {
            row: yield $(),
            column: { key: e },
            value: t
          };
          n[e].call(b, b, o);
        }), 0);
      });
    }
    function Pe(e) {
      let n = e.toLocaleString().match(Mn);
      if (n.length > 1) {
        let l = n[1];
        et(l);
      }
    }
    function ie(e, t) {
      if (e == "js") {
        let n = t + "_hook";
        f && f[t] ? f[t].call(b, b) : f && f[n] && Pe(f[n]);
      } else if (e == "action") {
        let n = M.value, l = {
          formId: u.id,
          buttonCode: t,
          dataId: n.id,
          uiFormData: Object.assign({}, n)
        };
        le.post(
          {
            url: `${me.urlButtonAction}`,
            params: l
          },
          { isTransformResponse: !1 }
        ).then((o) => {
          o.success ? s.success("处理完成!") : s.warning("处理失败!");
        });
      }
    }
    function Ae(e) {
      let t = T(e), n = [...t.getNewDataWithId(), ...Z.value[e]];
      if (!n || n.length == 0)
        return !1;
      let l = [];
      for (let o of n)
        l.push(o.id);
      t.removeRowsById(l);
    }
    function Re(e, t) {
      if (!t)
        return !1;
      let n = T(e);
      typeof t == "object" ? n.addRows(t, !0) : this.$message.error("添加子表数据,参数不识别!");
    }
    function Dt(e, t) {
      Ae(e), Re(e, t);
    }
    function Vt(e, t) {
      !t && t.length <= 0 && (t = []), t.map((n) => {
        n.hasOwnProperty("label") || (n.label = n.text);
      }), q({
        field: e,
        componentProps: {
          options: t
        }
      });
    }
    function Bt({ field: e, dict: t, label: n, type: l, subTableName: o }) {
      var d, m;
      const i = t.split(",").map((r) => encodeURIComponent(r)).join(",");
      if (l == "subTable") {
        const r = k.value.find((h) => h.key === o);
        if (r) {
          const h = r.columns.findIndex((g) => g.key === e);
          h !== -1 && le.get({
            url: `/sys/dict/loadDict/${i}`,
            params: { keyword: "", pageSize: 1e3 }
          }).then((g) => {
            const C = t.split(","), N = { customOptions: !0, dictTable: C[0], dictCode: C[2], dictText: C[1], options: g };
            n && (N.title = n), r.columns[h] = w(w({}, r.columns[h]), N), window.findSubTableInfo = r;
          });
        }
      } else if (l == "subForm") {
        if ((m = (d = P[o]) == null ? void 0 : d.value) != null && m[0]) {
          const r = {
            field: e,
            componentProps: {
              dict: i
            }
          };
          n && (r.label = n), P[o].value[0].updateSchema(r);
        }
      } else {
        const r = {
          field: e,
          componentProps: {
            dict: i
          }
        };
        n && (r.label = n), q(r);
      }
    }
    function It(e, t, n) {
      const l = k.value.find((o) => o.key === e);
      if (l) {
        !n && n.length <= 0 && (n = []), n.map((i) => {
          i.hasOwnProperty("label") || (i.label = i.text);
        });
        const o = l.columns.findIndex((i) => i.key === t);
        o !== -1 && (l.columns[o] = ce(w({}, l.columns[o]), { options: n, dictCode: "" }));
      }
    }
    function jt(e, t, n) {
      var l, o;
      (o = (l = P[e]) == null ? void 0 : l.value) != null && o[0] && (!n && n.length <= 0 && (n = []), n.map((i) => {
        i.hasOwnProperty("label") || (i.label = i.text);
      }), P[e].value[0].updateSchema({
        field: t,
        componentProps: {
          dictCode: "",
          options: n
        }
      }));
    }
    function Nt(e, t) {
      return f && f.beforeSubmit ? f.beforeSubmit(e, t) : Promise.resolve();
    }
    function Jt(e, t) {
      let n = J(K);
      Object.keys(n).map((l) => {
        l.endsWith("_load") || l.endsWith("_disabled") || (K[l] = !0);
      }), e && e.length > 0 ? Object.keys(n).map((l) => {
        !l.endsWith("_load") && e.indexOf(l) < 0 && (K[l] = !1);
      }) : t && t.length > 0 && Object.keys(n).map((l) => {
        t.indexOf(l) >= 0 && (K[l] = !1);
      });
    }
    function Lt(e, t) {
      return _(this, null, function* () {
        O.value = t, yield he(), M.value = "", S.value = !0, yield Fe(e), yield je(() => {
          ie("js", "loaded");
        });
      });
    }
    function T(e) {
      let t = P[e].value;
      if (t instanceof Array && (t = t[0]), !t) {
        s.warning("子表ref找不到:" + e);
        return;
      }
      return t;
    }
    function Ut() {
      let e = y.reportPrintUrl, t = M.value.id, n = An();
      On(e, t, n);
    }
    const [Kt, { openModal: Ee }] = En(), re = v(""), se = v(""), ue = v(!0);
    function Yt(e) {
      re.value = e.id, se.value = e.key, ue.value = !1, Ee(!0, { isUpdate: !1, tableType: "3" });
    }
    function Wt(e) {
      let n = T(e.key).getSelectedData();
      if (n.length != 1) {
        s.warning("请选择一条数据");
        return;
      }
      re.value = e.id, se.value = e.key, ue.value = !1, Ee(!0, { isUpdate: !0, record: n[0] });
    }
    function qt(e) {
      const t = e[Ne];
      let n = Je(e, [Ne]);
      if (n.id) {
        let l = Je(w({}, n), "id"), o = [{ rowKey: n.id, values: l }];
        T(t).setValues(o);
      } else
        T(t).addRows(n, { isOnlineJS: !1, setActive: !1, emitChange: !0, isModalData: !0 });
    }
    function Ht() {
      if (u.themeTemplate === oe)
        return;
      let e = k.value;
      if (e && e.length > 0) {
        for (let t of e)
          if (t.relationType != 1) {
            let n = T(t.key);
            n && n.clearSelection();
          }
      }
    }
    function Gt() {
      let e = $(), t = J(W[x.value]);
      Y(t, (n) => {
        A(n);
      }, e);
    }
    function zt(e, t) {
      let n = k.value;
      if (n && n.length > 0) {
        let l = n.filter((o) => o.key === e);
        if (l.length == 0)
          return;
        if (l[0].relationType == 1)
          T(e).executeFillRule();
        else {
          let o = J(W[e]), i = J(t.row);
          Y(o, (d) => {
            const { row: m, target: r } = t;
            let h = [{ rowKey: m.id, values: w({}, d) }];
            r.setValues(h);
          }, i);
        }
      }
    }
    let xe = {
      tableName: x,
      loading: E,
      subActiveKey: ee,
      onlineFormRef: a,
      getFieldsValue: $,
      setFieldsValue: A,
      submitFlowFlag: F,
      subFormHeight: Ce,
      subTableHeight: _e,
      refMap: P,
      triggleChangeValues: Ct,
      triggleChangeValue: _t,
      sh: K,
      clearSubRows: Ae,
      addSubRows: Re,
      clearThenAddRows: Dt,
      changeOptions: Vt,
      isUpdate: S,
      getSubTableInstance: T,
      updateSchema: q,
      executeMainFillRule: Gt,
      executeSubFillRule: zt,
      // update-begin--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
      changeSubTableOptions: It,
      changeSubFormbleOptions: jt,
      // update-end--author:liaozhiyang---date:20240313---for：【QQYUN-8350】js增强根据主表限制子表options
      changeRemoteOptions: Bt,
      changEvent: () => {
      },
      onlineFormValueChange: gt,
      // update-begin--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
      submitFormAndFlow: bt
      // update-end--author:liaozhiyang---date:20240705---for：【TV360X-1754】js增强-提交表单并且发起流程
    };
    return Le(xe), {
      //主表
      tableName: x,
      onlineFormRef: a,
      registerForm: tt,
      loading: E,
      //子表
      subActiveKey: ee,
      hasSubTable: He,
      subTabInfo: k,
      refMap: P,
      //一对一子表
      subFormHeight: Ce,
      getSubTableForeignKeyValue: Ot,
      isUpdate: S,
      handleSubFormChange: At,
      //一对多子表
      subTableHeight: _e,
      onlineFormDisabled: ye,
      subDataSource: Z,
      getSubTableAuthPre: Oe,
      handleAdded: xt,
      handleSubTableDefaultValue: ve,
      handleValueChange: Rt,
      openSubFormModalForAdd: Yt,
      openSubFormModalForEdit: Wt,
      getBtnAuth: (e, t) => {
        const n = Oe(t);
        let o = Bn().getOnlineSubTableAuth(n);
        return o != null && o.length ? !o.find((d) => d === e) : !0;
      },
      handleRemoved: Et,
      //父组件调用
      show: at,
      createRootProperties: mt,
      handleSubmit: we,
      sh: K,
      handleCgButtonClick: ie,
      handleCustomFormSh: Jt,
      handleCustomFormEdit: Lt,
      //跳转
      dbData: M,
      onOpenReportPrint: Ut,
      onlineExtConfigJson: y,
      //一对多子表弹窗操作数据
      registerPopModal: Kt,
      popTableId: re,
      popTableName: se,
      getPopFormData: qt,
      popModalRequest: ue,
      onCloseModal: Ht,
      ERP: oe,
      rowNumber: ae,
      isSetFormLabelLength: Q,
      subFormWrapRef: c,
      getSubAddBtnCfg: Ue,
      getSubRemoveBtnCfg: Ke,
      getSubOpenAddBtnCfg: Ye,
      getSubOpenEditBtnCfg: We
    };
  }
}, Nn = ["id"], Jn = { key: 1 };
function Ln(u, p, s, a, R, E) {
  const B = D("BasicForm"), O = D("online-sub-form"), F = D("a-button"), S = D("JVxeTable"), I = D("a-tab-pane"), Q = D("a-tabs"), L = D("Loading"), ae = D("online-pop-modal");
  return V(), te("div", {
    id: a.tableName + "_form",
    class: rn(["onlineFormWrap", [`formTemplate_${s.formTemplate}`]])
  }, [
    H(B, {
      ref: "onlineFormRef",
      onRegister: a.registerForm,
      name: "online-form_" + a.tableName
    }, null, 8, ["onRegister", "name"]),
    s.themeTemplate !== a.ERP && a.hasSubTable ? (V(), ne(Q, {
      key: 0,
      activeKey: a.subActiveKey,
      "onUpdate:activeKey": p[0] || (p[0] = (c) => a.subActiveKey = c)
    }, {
      default: G(() => [
        (V(!0), te(sn, null, un(a.subTabInfo, (c, U) => (V(), ne(I, {
          tab: c.describe,
          key: U + "",
          forceRender: !0
        }, {
          default: G(() => [
            c.relationType == 1 ? (V(), te("div", {
              key: 0,
              ref_for: !0,
              ref: "subFormWrapRef",
              style: cn({ "overflow-y": "auto", "overflow-x": "hidden", "max-height": a.subFormHeight + "px" })
            }, [
              H(O, {
                ref_for: !0,
                ref: a.refMap[c.key],
                table: c.key,
                disabled: a.onlineFormDisabled,
                "form-template": s.formTemplate,
                "main-id": a.getSubTableForeignKeyValue(c.foreignKey),
                properties: c.properties,
                "required-fields": c.requiredFields,
                "is-update": a.isUpdate,
                onFormChange: (y) => a.handleSubFormChange(y, c.key)
              }, null, 8, ["table", "disabled", "form-template", "main-id", "properties", "required-fields", "is-update", "onFormChange"])
            ], 4)) : (V(), te("div", Jn, [
              H(S, {
                ref_for: !0,
                ref: a.refMap[c.key],
                toolbar: "",
                "keep-source": "",
                "row-number": a.rowNumber,
                "row-selection": "",
                height: (
                  // 【VUEN-803】一对多子表固定340高度，修复自定义列组件被遮挡的问题
                  a.subTableHeight
                ),
                disabled: a.onlineFormDisabled,
                columns: c.columns,
                dataSource: a.subDataSource[c.key],
                addBtnCfg: a.getSubAddBtnCfg,
                removeBtnCfg: a.getSubRemoveBtnCfg,
                onValueChange: (y) => a.handleValueChange(y, c.key),
                onRemoved: (y) => a.handleRemoved(y, c.key),
                authPre: a.getSubTableAuthPre(c.key),
                onAdded: (y) => a.handleAdded(c, y),
                onExecuteFillRule: (y) => a.handleSubTableDefaultValue(c, y)
              }, {
                toolbarSuffix: G(() => [
                  !a.onlineFormDisabled && a.getSubOpenAddBtnCfg.enabled && a.getBtnAuth("add", c.key) ? (V(), ne(F, {
                    key: 0,
                    type: "primary",
                    preIcon: a.getSubOpenAddBtnCfg.buttonIcon,
                    onClick: (y) => a.openSubFormModalForAdd(c)
                  }, {
                    default: G(() => [
                      Ve("span", null, Be(a.getSubOpenAddBtnCfg.buttonName), 1)
                    ]),
                    _: 2
                  }, 1032, ["preIcon", "onClick"])) : fe("", !0),
                  !a.onlineFormDisabled && a.getSubOpenEditBtnCfg.enabled && a.getBtnAuth("update", c.key) ? (V(), ne(F, {
                    key: 1,
                    type: "primary",
                    preIcon: a.getSubOpenEditBtnCfg.buttonIcon,
                    onClick: (y) => a.openSubFormModalForEdit(c)
                  }, {
                    default: G(() => [
                      Ve("span", null, Be(a.getSubOpenEditBtnCfg.buttonName), 1)
                    ]),
                    _: 2
                  }, 1032, ["preIcon", "onClick"])) : fe("", !0)
                ]),
                _: 2
              }, 1032, ["row-number", "height", "disabled", "columns", "dataSource", "addBtnCfg", "removeBtnCfg", "onValueChange", "onRemoved", "authPre", "onAdded", "onExecuteFillRule"])
            ]))
          ]),
          _: 2
        }, 1032, ["tab"]))), 128))
      ]),
      _: 1
    }, 8, ["activeKey"])) : fe("", !0),
    H(L, {
      loading: a.loading,
      absolute: !1
    }, null, 8, ["loading"]),
    fn(u.$slots, "bottom", {}, void 0, !0),
    H(ae, {
      formTableType: "3",
      request: a.popModalRequest,
      id: a.popTableId,
      onRegister: a.registerPopModal,
      onSuccess: a.getPopFormData,
      taskId: s.taskId,
      tableName: a.popTableName,
      topTip: "",
      isVxeTableData: ""
    }, null, 8, ["request", "id", "onRegister", "onSuccess", "taskId", "tableName"])
  ], 10, Nn);
}
const Dl = /* @__PURE__ */ In(jn, [["render", Ln], ["__scopeId", "data-v-3f26e7bd"]]);
export {
  Dl as default
};
