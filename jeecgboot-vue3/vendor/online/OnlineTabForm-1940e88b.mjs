var At = Object.defineProperty, Bt = Object.defineProperties;
var Vt = Object.getOwnPropertyDescriptors;
var ve = Object.getOwnPropertySymbols;
var xt = Object.prototype.hasOwnProperty, Mt = Object.prototype.propertyIsEnumerable;
var Fe = (i, u, r) => u in i ? At(i, u, { enumerable: !0, configurable: !0, writable: !0, value: r }) : i[u] = r, P = (i, u) => {
  for (var r in u || (u = {}))
    xt.call(u, r) && Fe(i, r, u[r]);
  if (ve)
    for (var r of ve(u))
      Mt.call(u, r) && Fe(i, r, u[r]);
  return i;
}, Se = (i, u) => Bt(i, Vt(u));
var v = (i, u, r) => new Promise((a, A) => {
  var S = (p) => {
    try {
      y(r.next(p));
    } catch (m) {
      A(m);
    }
  }, k = (p) => {
    try {
      y(r.throw(p));
    } catch (m) {
      A(m);
    }
  }, y = (p) => p.done ? a(p.value) : Promise.resolve(p.value).then(S, k);
  y((r = r.apply(i, u)).next());
});
import { useMessage as Dt } from "/@/hooks/web/useMessage";
import { ref as g, reactive as Et, computed as It, watch as jt, resolveComponent as T, openBlock as w, createElementBlock as J, normalizeClass as Nt, createVNode as V, withCtx as x, createBlock as U, Fragment as Lt, renderList as Jt, normalizeStyle as Ut, createElementVNode as _e, toDisplayString as Ce, createCommentVNode as ee, renderSlot as qt, unref as Te, nextTick as we, toRaw as M } from "vue";
import { BasicForm as Kt, useForm as Wt } from "/@/components/Form/index";
import { c as Ht, O as Gt, d as zt, e as Qt, u as Yt, l as q, g as Xt, V as I, S as Zt, f as $t, h as ke } from "./useExtendComponent-bb98e568.mjs";
import { defHttp as te } from "/@/utils/http/axios";
import { pick as eo, omit as Oe } from "lodash-es";
import { sleep as to, goJmReportViewPage as oo } from "/@/utils";
import { Loading as lo } from "/@/components/Loading";
import { getToken as no } from "/@/utils/auth";
import { PrinterOutlined as ao } from "@ant-design/icons-vue";
import { useModal as ro } from "/@/components/Modal";
import { u as io, G as so } from "./useCustomHook-acb00837.mjs";
import { useAppInject as uo } from "/@/hooks/web/useAppInject";
import { _ as co } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "./constant-fa63bd66.mjs";
import "/@/utils/common/compUtils";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/utils/is";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/api/common/api";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "/@/utils/cache";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const oe = {
  optPre: "/online/cgform/api/form/",
  urlButtonAction: "/online/cgform/api/doButton"
}, fo = {
  name: "OnlineTabForm",
  components: {
    BasicForm: Kt,
    Loading: lo,
    OnlineSubForm: Ht,
    PrinterOutlined: ao,
    OnlinePopModal: Gt
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
    tabIndex: {
      type: String,
      default: ""
    },
    cgBIBtnMap: Object,
    buttonSwitch: Object
  },
  emits: ["success", "rendered", "toggleTab"],
  setup(i, { emit: u }) {
    const { createMessage: r } = Dt(), a = g(null), A = g(!0), S = g(!1), k = g(1), y = g(""), p = g(!1), m = g(!1), { getIsMobile: K } = uo(), W = g(!K.value), O = Et({
      reportPrintShow: 0,
      reportPrintUrl: "",
      joinQuery: 0,
      modelFullscreen: 0,
      modalMinWidth: "",
      commentStatus: 0
    }), {
      onlineFormContext: f,
      resetContext: c,
      getSubAddBtnCfg: H,
      getSubRemoveBtnCfg: b,
      getSubOpenAddBtnCfg: Re,
      getSubOpenEditBtnCfg: Pe
    } = zt(i), {
      formSchemas: G,
      defaultValueFields: j,
      changeDataIfArray2String: le,
      tableName: R,
      dbData: _,
      checkOnlyFieldValue: Ae,
      hasSubTable: Be,
      subTabInfo: D,
      refMap: z,
      subDataSource: N,
      baseColProps: Ve,
      createFormSchemas: xe,
      fieldDisplayStatus: E,
      labelCol: Me,
      wrapperCol: De,
      labelWidth: Ee
    } = Qt(i, a);
    let { EnhanceJS: s, initCgEnhanceJs: Ie } = Yt(f, !1);
    const { executeJsEnhanced: je } = io({}, f), [Ne, { setProps: Le, validate: ne, resetFields: ae, clearValidate: Je, setFieldsValue: C, updateSchema: Q, getFieldsValue: Y, scrollToField: re }] = Wt({
      schemas: G,
      showActionButtonGroup: !1,
      baseColProps: Ve,
      // update-begin--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
      labelWidth: Ee,
      // update-end--author:liaozhiyang---date:20240329---for：【QQYUN-7872】online表单label较长优化
      // update-begin--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
      labelCol: Me,
      wrapperCol: De
      // update-end--author:liaozhiyang---date:20240105---for：【QQYUN-7499】多列风格富文本、markdown增加独占一行功能
    }), ie = g(!1);
    function Ue() {
      let e = i.disabled;
      ie.value = e, Le({ disabled: e });
    }
    function qe(e, t, o) {
      return v(this, null, function* () {
        yield Ke(), y.value = "", yield ae(), setTimeout(() => {
          Je();
        }, 0), _.value = "";
        let l = Te(e);
        m.value = l, l ? yield ue(t) : ce(), we(() => {
          !l && o && C(o), We(), X("js", "loaded"), Ue();
        });
      });
    }
    function Ke() {
      return v(this, null, function* () {
        if (i.isTree === !0) {
          let e = i.pidField, t = G.value;
          t && t.length > 0 && t.filter((l) => l.field === e).length > 0 && (yield Q({
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
    function We() {
      if (Te(m) === !1) {
        let e = M(j[R.value]);
        q(e, (t) => {
          C(t);
        });
      }
    }
    function se(e, t) {
      let o = M(j[e.key]);
      q(o, (l) => {
        const { row: n, target: d } = t;
        let F = [{ rowKey: n.id, values: P({}, l) }];
        d.setValues(F);
      });
    }
    function ue(e) {
      return v(this, null, function* () {
        let t = yield Ge(e.id);
        _.value = Object.assign({}, e, t);
        let o = He.value, l = eo(t, ...o);
        i.disabled && Object.keys(l).map((n) => {
          !l[n] && l[n] !== 0 && l[n] !== "0" && delete l[n];
        }), yield C(l), ce(t);
      });
    }
    function ce(e) {
      e || (e = {});
      let t = Object.keys(N.value);
      if (t && t.length > 0) {
        let o = {};
        for (let l of t)
          o[l] = e[l] || [];
        N.value = o;
      }
    }
    let He = It(() => {
      let e = G.value, t = [];
      for (let o of e)
        t.push(o.field);
      return t;
    });
    function Ge(e) {
      let t = `${oe.optPre}${i.id}/${e}`;
      return new Promise((o, l) => {
        te.get({ url: t }, { isTransformResponse: !1 }).then((n) => {
          n.success ? o(n.result) : (l(), r.warning(n.message));
        }).catch(() => {
          l();
        });
      });
    }
    function ze(e) {
      return v(this, null, function* () {
        k.value = e.head.tableType, R.value = e.head.tableName, A.value = e.head.tableType == 1, Xe(e.head.extConfigJson), xe(e.schema.properties, e.schema.required, Ae, O), s = Ie(e.enhanceJs), u("rendered", O);
        let t = yield Xt(a);
        t.$formValueChange = (o, l, n) => {
          gt(o, l), n && C(n), Qe(o, l, n);
        }, s && s.setup && ge(s.setup);
      });
    }
    function Qe(e, t, o) {
      f.changEvent(e, t, o);
    }
    function Ye(e) {
      f.addObject2Context("changEvent", e);
    }
    function Xe(e) {
      let t = { reportPrintShow: 0, reportPrintUrl: "", joinQuery: 0, modelFullscreen: 0, modalMinWidth: "", commentStatus: 0, formLabelLength: null };
      e && (t = JSON.parse(e)), Object.keys(t).map((o) => {
        O[o] = t[o];
      });
    }
    function Ze() {
      A.value === !0 ? lt() : $e();
    }
    function $e() {
      et().then((e) => {
        fe(e);
      });
    }
    function et() {
      let e = {};
      return new Promise((t, o) => {
        ne().then(
          (l) => t(l),
          ({ errorFields: l }) => {
            o({
              code: I,
              key: R.value,
              // 滚动到未通过校验的字段上
              scrollToField: () => l[0] && re(l[0].name, { behavior: "smooth", block: "center" })
            });
          }
        );
      }).then((t) => (Object.assign(e, le(t)), ot())).then((t) => (Object.assign(e, t), Promise.resolve(e))).catch((t) => ((t === I || (t == null ? void 0 : t.code) === I) && (r.warning("校验未通过"), t.key && (tt(t.key), t.scrollToField && setTimeout(() => t.scrollToField(), 150))), Promise.reject(null)));
    }
    function tt(e) {
      if (e === R.value) {
        u("toggleTab", "-1");
        return;
      }
      let t = D.value;
      for (let o = 0; o < t.length; o++)
        if (e == t[o].key) {
          let l = o + "";
          if (L.value === l)
            break;
          if (u("toggleTab", l), t[o].relationType === 0) {
            let n = h(e);
            to(300, () => n == null ? void 0 : n.validateTable());
          }
          break;
        }
    }
    function ot() {
      return new Promise((e, t) => v(this, null, function* () {
        let o = {};
        try {
          let l = D.value;
          for (let n = 0; n < l.length; n++) {
            let d = l[n].key, F = h(d);
            if (l[n].relationType == 1)
              try {
                let B = yield F.getAll();
                o[d] = [], o[d].push(B);
              } catch (B) {
                return t(P({ code: I, key: d }, B));
              }
            else {
              if (yield F.fullValidateTable())
                return t({ code: I, key: d });
              o[d] = F.getTableData();
            }
          }
        } catch (l) {
          t(l);
        }
        e(o);
      }));
    }
    function lt() {
      return v(this, null, function* () {
        try {
          let e = yield ne();
          e = Object.assign({}, _.value, e), e = le(e), S.value = !0, fe(e);
        } catch (e) {
          Array.isArray(e == null ? void 0 : e.errorFields) && e.errorFields[0] && re(e.errorFields[0].name, { behavior: "smooth", block: "center" });
        } finally {
          S.value = !1;
        }
      });
    }
    function fe(e) {
      ht(ye, e).then(() => {
        nt(e);
      }).catch((t) => {
        r.warning(t);
      });
    }
    function nt(e) {
      Object.keys(e).map((n) => {
        Array.isArray(e[n]) && e[n].length == 0 && (e[n] = "");
      });
      let t = y.value, o = `${oe.optPre}${i.id}?tabletype=${k.value}`;
      t && (o = `${t}?tabletype=${k.value}`), p.value === !0 && (e[Zt] = 1);
      let l = m.value === !0 ? "put" : "post";
      te.request({ url: o, method: l, params: e }, { isTransformResponse: !1 }).then((n) => {
        n.success ? (n.result && (e[$t] = n.result), u("success", e), i.submitTip === !0 && r.success(n.message)) : r.warning(n.message);
      }).finally(() => {
        S.value = !1;
      });
    }
    function at(e, t, o) {
      t && o ? o.vxeProps ? o.setValues([
        {
          rowKey: t,
          values: e
        }
      ]) : o.setValues(e) : C(e);
    }
    function rt(e, t) {
      let o = {};
      o[e] = t, C(o);
    }
    const L = g("0"), de = g("auto"), me = g(340);
    function it(e) {
      if (m.value === !0) {
        let t = _.value;
        return st(t, e);
      }
      return "";
    }
    jt(
      () => i.tabIndex,
      (e) => {
        L.value = e;
      },
      {
        immediate: !0
      }
    );
    function st(e, t) {
      if (e) {
        let o = e[t];
        return !o && o !== 0 && (o = e[t.toLowerCase()], !o && o !== 0 && (o = e[t.toUpperCase()])), o;
      }
      return "";
    }
    function ut(e, t) {
      if (s && s[t + "_onlChange"]) {
        let o = s[t + "_onlChange"](), l = Object.keys(e)[0];
        if (o[l]) {
          let d = h(t).getFormEvent(), F = P({
            column: { key: l },
            value: e[l]
          }, d);
          o[l].call(f, f, F);
        }
      }
    }
    function ct(e, t) {
      if (s && s[t + "_onlChange"]) {
        let o = s[t + "_onlChange"](f);
        if (e.column === "all") {
          let l = Object.keys(o);
          if (l.length > 0)
            for (let n of l)
              o[n].call(f, f, e);
        } else {
          let l = e.column.key || e.col.key;
          o[l] && e.row && e.row.id && o[l].call(f, f, e);
        }
      }
    }
    function ft(e, t) {
      var o;
      if (s && s[t + "_onlChange"]) {
        let l = s[t + "_onlChange"](f), n = Object.keys(l);
        if (n.length > 0)
          for (let d of n)
            (o = l[d]) == null || o.call(f, f, Se(P({}, e), { row: e.deleteRows }));
      }
    }
    function dt(e, t) {
      t.isModalData || se(e, t);
    }
    function mt(e) {
      return "online_" + e + ":";
    }
    function gt(e, t) {
      return v(this, null, function* () {
        if (!s || !s.onlChange || !e)
          return !1;
        let o = s.onlChange();
        o[e] && setTimeout(() => v(this, null, function* () {
          let n = {
            row: yield Y(),
            column: { key: e },
            value: t
          };
          o[e].call(f, f, n);
        }), 0);
      });
    }
    function ge(e) {
      let o = e.toLocaleString().match(so);
      if (o.length > 1) {
        let l = o[1];
        je(l);
      }
    }
    function X(e, t) {
      if (e == "js") {
        let o = t + "_hook";
        s && s[t] ? s[t].call(f, f) : s && s[o] && ge(s[o]);
      } else if (e == "action") {
        let o = _.value, l = {
          formId: i.id,
          buttonCode: t,
          dataId: o.id,
          uiFormData: Object.assign({}, o)
        };
        te.post(
          {
            url: `${oe.urlButtonAction}`,
            params: l
          },
          { isTransformResponse: !1 }
        ).then((n) => {
          n.success ? r.success("处理完成!") : r.warning("处理失败!");
        });
      }
    }
    function be(e) {
      let t = h(e), o = [...t.getNewDataWithId(), ...N.value[e]];
      if (!o || o.length == 0)
        return !1;
      let l = [];
      for (let n of o)
        l.push(n.id);
      t.removeRowsById(l);
    }
    function pe(e, t) {
      if (!t)
        return !1;
      let o = h(e);
      typeof t == "object" ? o.addRows(t, !0) : this.$message.error("添加子表数据,参数不识别!");
    }
    function bt(e, t) {
      be(e), pe(e, t);
    }
    function pt(e, t) {
      !t && t.length <= 0 && (t = []), t.map((o) => {
        o.hasOwnProperty("label") || (o.label = o.text);
      }), Q({
        field: e,
        componentProps: {
          options: t
        }
      });
    }
    function ht(e, t) {
      return s && s.beforeSubmit ? s.beforeSubmit(e, t) : Promise.resolve();
    }
    function yt(e, t) {
      let o = M(E);
      e && e.length > 0 ? Object.keys(o).map((l) => {
        !l.endsWith("_load") && e.indexOf(l) < 0 && (E[l] = !1);
      }) : t && t.length > 0 && Object.keys(o).map((l) => {
        t.indexOf(l) >= 0 && (E[l] = !1);
      });
    }
    function vt(e, t) {
      return v(this, null, function* () {
        y.value = t, yield ae(), _.value = "", m.value = !0, yield ue(e), yield we(() => {
          X("js", "loaded");
        });
      });
    }
    function h(e) {
      let t = z[e].value;
      if (t instanceof Array && (t = t[0]), !t) {
        r.warning("子表ref找不到:" + e);
        return;
      }
      return t;
    }
    function Ft() {
      let e = O.reportPrintUrl, t = _.value.id, o = no();
      oo(e, t, o);
    }
    const [St, { openModal: he }] = ro(), Z = g(""), $ = g(!0);
    function _t(e) {
      Z.value = e.id, $.value = !1, he(!0, { isUpdate: !1, tableType: "3" });
    }
    function Ct(e) {
      let o = h(e.key).getSelectedData();
      if (o.length != 1) {
        r.warning("请选择一条数据");
        return;
      }
      Z.value = e.id, $.value = !1, he(!0, { isUpdate: !0, record: o[0] });
    }
    function Tt(e) {
      const t = e[ke];
      let o = Oe(e, [ke]);
      if (o.id) {
        let l = Oe(P({}, o), "id"), n = [{ rowKey: o.id, values: l }];
        h(t).setValues(n);
      } else
        h(t).addRows(o, { isOnlineJS: !1, setActive: !1, emitChange: !0, isModalData: !0 });
    }
    function wt() {
      let e = D.value;
      if (e && e.length > 0) {
        for (let t of e)
          if (t.relationType != 1) {
            let o = h(t.key);
            o && o.clearSelection();
          }
      }
    }
    function kt() {
      let e = Y(), t = M(j[R.value]);
      q(
        t,
        (o) => {
          C(o);
        },
        e
      );
    }
    function Ot(e, t) {
      let o = D.value;
      if (o && o.length > 0) {
        let l = o.filter((n) => n.key === e);
        if (l.length == 0)
          return;
        if (l[0].relationType == 1)
          h(e).executeFillRule();
        else {
          let n = M(j[e]), d = M(t.row);
          q(
            n,
            (F) => {
              const { row: B, target: Rt } = t;
              let Pt = [{ rowKey: B.id, values: P({}, F) }];
              Rt.setValues(Pt);
            },
            d
          );
        }
      }
    }
    let ye = {
      tableName: R,
      loading: S,
      subActiveKey: L,
      onlineFormRef: a,
      getFieldsValue: Y,
      setFieldsValue: C,
      submitFlowFlag: p,
      subFormHeight: de,
      subTableHeight: me,
      refMap: z,
      triggleChangeValues: at,
      triggleChangeValue: rt,
      sh: E,
      clearSubRows: be,
      addSubRows: pe,
      clearThenAddRows: bt,
      changeOptions: pt,
      isUpdate: m,
      getSubTableInstance: h,
      updateSchema: Q,
      executeMainFillRule: kt,
      executeSubFillRule: Ot,
      changEvent: () => {
      },
      onlineFormValueChange: Ye
    };
    return c(ye), {
      //主表
      tableName: R,
      onlineFormRef: a,
      registerForm: Ne,
      loading: S,
      //子表
      subActiveKey: L,
      hasSubTable: Be,
      subTabInfo: D,
      refMap: z,
      //一对一子表
      subFormHeight: de,
      getSubTableForeignKeyValue: it,
      isUpdate: m,
      handleSubFormChange: ut,
      //一对多子表
      subTableHeight: me,
      onlineFormDisabled: ie,
      subDataSource: N,
      getSubTableAuthPre: mt,
      handleAdded: dt,
      handleSubTableDefaultValue: se,
      handleValueChange: ct,
      openSubFormModalForAdd: _t,
      openSubFormModalForEdit: Ct,
      handleRemoved: ft,
      //父组件调用
      show: qe,
      createRootProperties: ze,
      handleSubmit: Ze,
      sh: E,
      handleCgButtonClick: X,
      handleCustomFormSh: yt,
      handleCustomFormEdit: vt,
      //跳转
      dbData: _,
      onOpenReportPrint: Ft,
      onlineExtConfigJson: O,
      //一对多子表弹窗操作数据
      registerPopModal: St,
      popTableName: Z,
      getPopFormData: Tt,
      popModalRequest: $,
      onCloseModal: wt,
      rowNumber: W,
      getSubAddBtnCfg: H,
      getSubRemoveBtnCfg: b,
      getSubOpenAddBtnCfg: Re,
      getSubOpenEditBtnCfg: Pe
    };
  }
}, mo = ["id"], go = { key: 1 };
function bo(i, u, r, a, A, S) {
  const k = T("BasicForm"), y = T("a-tab-pane"), p = T("online-sub-form"), m = T("a-button"), K = T("JVxeTable"), W = T("a-tabs"), O = T("Loading"), f = T("online-pop-modal");
  return w(), J("div", {
    id: a.tableName + "_form",
    class: Nt(["onlineFormWrap", [`formTemplate_${r.formTemplate}`]])
  }, [
    V(W, {
      class: "tabTheme",
      activeKey: a.subActiveKey,
      "onUpdate:activeKey": u[0] || (u[0] = (c) => a.subActiveKey = c)
    }, {
      default: x(() => [
        (w(), U(y, {
          tab: "主表",
          key: "-1"
        }, {
          default: x(() => [
            V(k, {
              ref: "onlineFormRef",
              onRegister: a.registerForm
            }, null, 8, ["onRegister"])
          ]),
          _: 1
        })),
        a.hasSubTable ? (w(!0), J(Lt, { key: 0 }, Jt(a.subTabInfo, (c, H) => (w(), U(y, {
          tab: c.describe,
          key: H + "",
          forceRender: !0
        }, {
          default: x(() => [
            c.relationType == 1 ? (w(), J("div", {
              key: 0,
              style: Ut({ "overflow-y": "auto", "overflow-x": "hidden", "max-height": a.subFormHeight + "px" })
            }, [
              V(p, {
                ref_for: !0,
                ref: a.refMap[c.key],
                table: c.key,
                disabled: a.onlineFormDisabled,
                "form-template": r.formTemplate,
                "main-id": a.getSubTableForeignKeyValue(c.foreignKey),
                properties: c.properties,
                "required-fields": c.requiredFields,
                "is-update": a.isUpdate,
                onFormChange: (b) => a.handleSubFormChange(b, c.key)
              }, null, 8, ["table", "disabled", "form-template", "main-id", "properties", "required-fields", "is-update", "onFormChange"])
            ], 4)) : (w(), J("div", go, [
              V(K, {
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
                onValueChange: (b) => a.handleValueChange(b, c.key),
                onRemoved: (b) => a.handleRemoved(b, c.key),
                authPre: a.getSubTableAuthPre(c.key),
                onAdded: (b) => a.handleAdded(c, b),
                onExecuteFillRule: (b) => a.handleSubTableDefaultValue(c, b)
              }, {
                toolbarSuffix: x(() => [
                  !a.onlineFormDisabled && a.getSubOpenAddBtnCfg.enabled ? (w(), U(m, {
                    key: 0,
                    type: "primary",
                    preIcon: a.getSubOpenAddBtnCfg.buttonIcon,
                    onClick: (b) => a.openSubFormModalForAdd(c)
                  }, {
                    default: x(() => [
                      _e("span", null, Ce(a.getSubOpenAddBtnCfg.buttonName), 1)
                    ]),
                    _: 2
                  }, 1032, ["preIcon", "onClick"])) : ee("", !0),
                  !a.onlineFormDisabled && a.getSubOpenEditBtnCfg.enabled ? (w(), U(m, {
                    key: 1,
                    type: "primary",
                    preIcon: a.getSubOpenEditBtnCfg.buttonIcon,
                    onClick: (b) => a.openSubFormModalForEdit(c)
                  }, {
                    default: x(() => [
                      _e("span", null, Ce(a.getSubOpenEditBtnCfg.buttonName), 1)
                    ]),
                    _: 2
                  }, 1032, ["preIcon", "onClick"])) : ee("", !0)
                ]),
                _: 2
              }, 1032, ["row-number", "height", "disabled", "columns", "dataSource", "addBtnCfg", "removeBtnCfg", "onValueChange", "onRemoved", "authPre", "onAdded", "onExecuteFillRule"])
            ]))
          ]),
          _: 2
        }, 1032, ["tab"]))), 128)) : ee("", !0)
      ]),
      _: 1
    }, 8, ["activeKey"]),
    V(O, {
      loading: a.loading,
      absolute: !1
    }, null, 8, ["loading"]),
    qt(i.$slots, "bottom", {}, void 0, !0),
    V(f, {
      formTableType: "3",
      request: a.popModalRequest,
      id: a.popTableName,
      onRegister: a.registerPopModal,
      onSuccess: a.getPopFormData,
      topTip: "",
      isVxeTableData: ""
    }, null, 8, ["request", "id", "onRegister", "onSuccess"])
  ], 10, mo);
}
const ol = /* @__PURE__ */ co(fo, [["render", bo], ["__scopeId", "data-v-ef315dba"]]);
export {
  ol as default
};
