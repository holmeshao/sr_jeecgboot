var Te = Object.defineProperty, Oe = Object.defineProperties;
var xe = Object.getOwnPropertyDescriptors;
var he = Object.getOwnPropertySymbols;
var Be = Object.prototype.hasOwnProperty, Me = Object.prototype.propertyIsEnumerable;
var Ce = (y, o, S) => o in y ? Te(y, o, { enumerable: !0, configurable: !0, writable: !0, value: S }) : y[o] = S, X = (y, o) => {
  for (var S in o || (o = {}))
    Be.call(o, S) && Ce(y, S, o[S]);
  if (he)
    for (var S of he(o))
      Me.call(o, S) && Ce(y, S, o[S]);
  return y;
}, Z = (y, o) => Oe(y, xe(o));
import { ref as B, reactive as Se, watch as se, toRaw as we, computed as Pe, resolveComponent as v, openBlock as O, createElementBlock as ve, Fragment as ye, createElementVNode as D, createBlock as N, withCtx as c, createVNode as d, toDisplayString as de, Teleport as Ve, createTextVNode as H, createCommentVNode as Ie, normalizeClass as _e, withDirectives as Fe, vShow as be, normalizeStyle as Ne, renderList as ke } from "vue";
import { useModalInner as Re, BasicModal as Ae, useModal as Qe } from "/@/components/Modal";
import { randomString as fe } from "/@/utils/common/compUtils";
import { useMessage as De } from "/@/hooks/web/useMessage";
import { Modal as je, Divider as qe, Popconfirm as Le, Empty as Ye } from "ant-design-vue";
import { createLocalStorage as Je } from "/@/utils/cache";
import { useRoute as He } from "vue-router";
import { i as Ue, F as Ke, j as ge } from "./useExtendComponent-bb98e568.mjs";
import { cloneDeep as pe } from "lodash-es";
import { _ as ze } from "./SuperQueryValComponent.vue_vue_type_script_lang-8fe34917.mjs";
import { MinusCircleOutlined as We, PlusOutlined as Ge, FileTextOutlined as Xe, CloseCircleOutlined as Ze, AppstoreTwoTone as $e } from "@ant-design/icons-vue";
import { useConditionFilter as et } from "/@/utils/index";
import { useDesign as tt } from "/@/hooks/web/useDesign";
import { _ as lt } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "/@/components/Form/index";
import "/@/utils/http/axios";
import "./constant-fa63bd66.mjs";
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
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const nt = {
  password: "text",
  file: "text",
  image: "text",
  textarea: "text",
  umeditor: "text",
  markdown: "text",
  checkbox: "list_multi",
  radio: "list"
}, ot = "JSuperQuerySaved_";
function at(y) {
  const { linkTableCard2Select: o } = Ue(), { createMessage: S } = De(), l = B(), k = Se({
    values: []
  }), R = B("and"), V = B(!1), [$, { setModalProps: T }] = Re(() => {
    T({ confirmLoading: !1 });
  }), A = Object.assign({}, { link_down: "text" }, nt);
  function j() {
  }
  function ee() {
  }
  function U(t, n, i) {
    i.val = n;
  }
  const M = B({}), Y = B([]), I = (t) => {
    const { properties: n = {} } = t;
    Object.entries(n).forEach(([i, a]) => {
      a.view === "table" && I(a), ["link_down"].includes(a.originView || a.view) && delete n[i];
    });
  };
  function q(t) {
    var r;
    I(t);
    let { allFields: n, treeData: i } = re(t);
    M.value = n;
    const a = (r = t.properties) != null ? r : {}, m = [], p = t.table;
    if (Object.entries(a).forEach(([h, e]) => {
      e.view === "table" && m.push(h);
    }), m.length) {
      let h = [];
      h = i.filter((e) => !m.includes(e.value));
      for (let e = 0, u = i.length; e < u; e++) {
        const f = i[e];
        m.includes(f.value) || (i.splice(e, 1), e--, u--);
      }
      i.unshift({ title: "主表", value: p, disabled: !0, order: 200, children: h, view: "table" });
    }
    Y.value = i;
  }
  function K(t) {
    let n = {
      field: void 0,
      rule: "eq",
      val: "",
      key: fe(16)
    };
    t === !1 ? (k.values = [], k.values.push(n)) : t === !0 ? k.values.length == 0 && k.values.push(n) : k.values.splice(++t, 0, n);
  }
  function te(t) {
    let n = we(k.values), i = -1;
    for (let a = 0; a < n.length; a++)
      if (t.key == n[a].key) {
        i = a;
        break;
      }
    i != -1 && k.values.splice(i, 1);
  }
  const le = {
    field: "val",
    label: "测试",
    component: "Input"
  };
  function ne(t, n) {
    var h, e, u;
    let a = M.value[t.field];
    if (!a)
      return le;
    A[a.view] && (a.view = A[a.view]);
    let m = Ke.createFormSchema(t.field, a);
    m.noChange(), m.asSearchForm(), m.updateField(t.field + n);
    const p = (f) => {
      t.val = f[t.field];
    };
    m.setFunctionForFieldValue(p);
    let r = m.getFormItemSchema();
    if (["empty", "not_empty"].includes(t.rule) && (r.componentProps = Z(X({}, r.componentProps), { disabled: !0 })), o(r), r.component === "LinkTableSelect") {
      let f = (h = r.componentProps) != null ? h : {};
      r.componentProps = Z(X({}, f), { editBtnShow: !1 });
    }
    if (r && r.component === "InputNumber" && (t.curLineAlign = "start"), (r == null ? void 0 : r.component) === "JTreeSelect") {
      let f = r.componentProps;
      f ? f.getPopupContainer = () => document.body : r.componentProps = { getPopupContainer: () => document.body };
    }
    if ((r == null ? void 0 : r.component) === "JSwitch") {
      const f = (e = r.componentProps) != null ? e : {};
      r.componentProps = Z(X({}, f), { query: !0 });
    }
    if ((r == null ? void 0 : r.component) === "JSelectUser") {
      const f = (u = r.componentProps) != null ? u : {};
      r.componentProps = Z(X({}, f), { showButton: !1 });
    }
    return r;
  }
  const J = B(""), Q = Je(), E = Se({
    visible: !1,
    title: "",
    content: "",
    saveCode: ""
  }), oe = B(!1), z = He();
  y.isCustomSave ? se(y.saveSearchData, () => {
    F.value = y.saveSearchData;
  }) : se(() => z.fullPath, (t) => {
    W();
  });
  const F = B([]);
  se(() => F.value, (t) => {
    let n = [];
    t && t.length > 0 && t.map((i) => {
      let a = fe(16);
      n.push({
        title: i.title,
        slots: { icon: "custom" },
        value: a
      });
    }), J.value = n;
  }, { immediate: !0, deep: !0 });
  function W() {
    if (y.isCustomSave)
      F.value = pe(y.saveSearchData);
    else {
      let t = ot + z.fullPath;
      E.saveCode = t;
      let n = Q.get(t);
      n && n instanceof Array && (F.value = n);
    }
  }
  W();
  function G() {
    let t = L();
    if (!t) {
      S.warning("空条件不能保存");
      return;
    }
    let n = JSON.stringify(t);
    ae(n);
  }
  function ae(t) {
    E.visible = !0, E.title = "", E.content = t;
  }
  function ie() {
    let { title: t, content: n, saveCode: i } = E, a = P(t);
    const m = (p) => {
      const r = pe(F.value);
      V.value = !0, p ? r.splice(a, 1, {
        content: n,
        title: t,
        type: R.value
      }) : r.push({
        content: n,
        title: t,
        type: R.value
      });
      const h = () => {
        E.visible = !1, S.success("保存成功"), F.value = r, V.value = !1;
      };
      y.isCustomSave ? y.save(r, 0).then(() => {
        h();
      }).catch((e) => {
        V.value = !1;
      }) : (Q.set(i, r, 2592e3), h());
    };
    a >= 0 ? je.confirm({
      title: "提示",
      content: `${t} 已存在，是否覆盖？`,
      okText: "确认",
      cancelText: "取消",
      onOk: () => {
        m(1);
      }
    }) : m(0);
  }
  function P(t) {
    let n = F.value, i = -1;
    for (let a = 0; a < n.length; a++)
      if (n[a].title == t) {
        i = a;
        break;
      }
    return i;
  }
  function L(t = !1) {
    var m;
    let n = k.values;
    if (!n || n.length == 0)
      return !1;
    let i = [], a = M.value;
    for (let p of n) {
      let r = ["empty", "not_empty"].includes(p.rule);
      if (p.field && (r || p.val || p.val === 0) && p.rule) {
        let h = a[p.field], e = (m = h == null ? void 0 : h.formatValue) != null ? m : (_) => _, u = we(p.val);
        u instanceof Array ? u = u.map((_) => e(_)).join(",") : u = e(u);
        let w = {
          field: s(p),
          rule: p.rule,
          val: u,
          fileType: p.fileType
        };
        if (t === !0) {
          let _ = a[p.field];
          _ && (w.type = _.view, w.dbType = _.type);
        }
        i.push(w);
      }
    }
    return i.length == 0 ? !1 : i;
  }
  function s(t) {
    let n = t.field;
    return n.indexOf("@") > 0 && (n = n.replace("@", ",")), n;
  }
  function x(t, { node: n }) {
    let i = n.dataRef.title, a = F.value.filter((m) => m.title == i);
    if (a && a.length > 0) {
      let { content: m, type: p } = a[0], r = JSON.parse(m), h = [];
      for (let e of r)
        e.field = e.field.replace(",", "@"), h.push(Object.assign({}, { key: fe(16) }, e));
      k.values = h, R.value = p;
    }
  }
  function b(t) {
    let n = P(t);
    if (n >= 0)
      if (y.isCustomSave) {
        const i = pe(F.value);
        i.splice(n, 1), y.save(i, 1).then(() => {
          F.value = i;
        }).catch((a) => {
        });
      } else
        F.value.splice(n, 1), Q.set(E.saveCode, F.value);
  }
  function re(t) {
    let n = {}, i = 1, a = [];
    return t.properties && (t = t.properties), Object.keys(t).map((m) => {
      let p = t[m];
      if (p.view == "table") {
        let r = p.properties || p.fields, h = i * 100, e = {
          title: p.title,
          value: m,
          disabled: !0,
          children: [],
          order: h,
          // update-begin--author:liaozhiyang---date:20240306---for：【TV360X-461】字段类型是string，则默认模糊查询
          fieldType: p.type
          // update-end--author:liaozhiyang---date:20240306---for：【TV360X-461】字段类型是string，则默认模糊查询
        };
        Object.keys(r).map((u) => {
          let f = r[u];
          f.order = h + f.order;
          let w = m + "@" + u;
          n[w] = f, e.children.push({
            title: f.title,
            value: w,
            isLeaf: !0,
            order: f.order,
            // update-begin--author:liaozhiyang---date:20240306---for：【TV360X-461】字段类型是string，则默认模糊查询
            fieldType: f.type,
            view: f.view,
            originView: f.view
            // update-end--author:liaozhiyang---date:20240306---for：【TV360X-461】字段类型是string，则默认模糊查询
          });
        }), ue(e), a.push(e), i++;
      } else {
        let r = m;
        n[r] = p, a.push({
          title: p.title,
          value: r,
          isLeaf: !0,
          order: p.order,
          // update-begin--author:liaozhiyang---date:20240306---for：【TV360X-461】字段类型是string，则默认模糊查询
          fieldType: p.type,
          view: p.view,
          originView: p.view
          // update-end--author:liaozhiyang---date:20240306---for：【TV360X-461】字段类型是string，则默认模糊查询
        });
      }
    }), ue(a), { allFields: n, treeData: a };
  }
  function ue(t) {
    (t.children || t).sort(function(i, a) {
      return i.order - a.order;
    });
  }
  function me(t) {
    const { params: n, matchType: i } = t;
    if (n) {
      let a = [];
      for (let m of n)
        a.push(Object.assign({}, { key: fe(16) }, m));
      k.values = a, i.value = i;
    }
  }
  return {
    formRef: l,
    init: q,
    dynamicRowValues: k,
    matchType: R,
    registerModal: $,
    handleSubmit: j,
    handleCancel: ee,
    handleSave: G,
    doSaveQueryInfo: ie,
    saveInfo: E,
    saveTreeData: J,
    handleRemoveSaveInfo: b,
    handleTreeSelect: x,
    fieldTreeData: Y,
    addOne: K,
    removeOne: te,
    setFormModel: U,
    getSchema: ne,
    loading: oe,
    getQueryInfo: L,
    initDefaultValues: me,
    saveModalLoading: V,
    fieldProperties: M
  };
}
const it = {
  name: "OnlineSuperQuery",
  props: {
    config: {
      type: Object,
      default: []
    },
    status: {
      type: Boolean,
      default: !1
    },
    online: {
      type: Boolean,
      default: !1
    },
    // update-begin--author:liaozhiyang---date:20240514---for：【issues/6205】高级查询组件增加保存条件自定义存储方式
    isCustomSave: {
      type: Boolean,
      default: !1
    },
    saveSearchData: {
      type: Array,
      default: () => []
    },
    save: {
      type: Function
    },
    // update-end--author:liaozhiyang---date:20240514---for：【issues/6205】高级查询组件增加保存条件自定义存储方式
    queryBtnCfg: {
      type: Object,
      default: () => ({
        buttonName: "高级查询",
        buttonIcon: "ant-design:filter-outlined"
      })
    }
  },
  components: {
    BasicModal: Ae,
    MinusCircleOutlined: We,
    PlusOutlined: Ge,
    OnlineSuperQueryValComponent: ze,
    FileTextOutlined: Xe,
    CloseCircleOutlined: Ze,
    AppstoreTwoTone: $e,
    Divider: qe,
    Popconfirm: Le
  },
  emits: ["search"],
  setup(y, { emit: o }) {
    const [S, l] = Qe(), { createMessage: k } = De(), R = B({}), V = B(180), { prefixCls: $ } = tt("super-query"), T = `${$}-tree-popup`, A = B(!0);
    let j = null;
    const { filterCondition: ee } = et(), U = (e) => {
      var f, w;
      const u = (_, C) => {
        var ce;
        const g = (ce = _.find((Ee) => Ee.value === C)) != null ? ce : {};
        return ee({ view: g.originView || g.view, fieldType: g.fieldType });
      };
      if (((f = e.field) == null ? void 0 : f.indexOf("@")) == -1 || !e.field)
        return u(P.value, e.field);
      {
        const _ = e.field.split("@")[0], C = P.value.find((g) => g.value === _);
        if ((w = C == null ? void 0 : C.children) != null && w.length)
          return u(C.children, e.field);
      }
    };
    function M() {
      l.closeModal();
    }
    function Y() {
      if (y.online === !0) {
        let e = ue(!0);
        e && e.length > 0 ? (I(e), o("search", e, E.value), M(), j = e) : k.warning("空条件无法查询！");
      } else {
        let e = ue(!0);
        if (e && e.length > 0) {
          let u = q(e);
          o("search", u);
        } else
          k.warning("空条件无法查询！");
      }
      A.value = !0;
    }
    const I = (e) => {
      e.forEach((u) => {
        const f = u.val;
        if (u.type === "date" && typeof f == "string" && f != "") {
          const w = n.value[u.field];
          if (w) {
            let _ = w.fieldExtendJson;
            if (_ && (_ = JSON.parse(_), _.picker && _.picker !== "default")) {
              const C = _.picker;
              C === "year" ? u.val = ge(f).set("month", 0).set("date", 1).format("YYYY-MM-DD") : C === "month" ? u.val = ge(f).set("date", 1).format("YYYY-MM-DD") : C === "week" && (u.val = ge(f).startOf("week").format("YYYY-MM-DD"));
            }
          }
        }
      });
    };
    function q(e) {
      let u = [];
      for (let w of e) {
        let _ = w.field, C = w.val;
        C instanceof Array && (C = C.join(",")), u.push(Z(X({}, w), {
          field: _,
          val: C
        }));
      }
      return u.length > 0 ? i.value = !0 : i.value = !1, {
        superQueryMatchType: E.value,
        superQueryParams: encodeURI(JSON.stringify(u))
      };
    }
    function K() {
      let e = q([]);
      o("search", e);
    }
    function te() {
      Q.values = [], L(!1);
      let e = q([]);
      j = null, A.value = !0, o("search", e);
    }
    const le = (e) => {
      if (e) {
        const u = document.documentElement.clientHeight - 165;
        R.value = { maxHeight: `${u}px` }, V.value = u * 0.62;
      } else
        R.value = {}, V.value = 180;
    }, {
      formRef: ne,
      init: J,
      dynamicRowValues: Q,
      matchType: E,
      registerModal: oe,
      handleSave: z,
      doSaveQueryInfo: F,
      saveInfo: W,
      saveTreeData: G,
      handleTreeSelect: ae,
      handleRemoveSaveInfo: ie,
      fieldTreeData: P,
      addOne: L,
      removeOne: s,
      setFormModel: x,
      getSchema: b,
      loading: re,
      getQueryInfo: ue,
      initDefaultValues: me,
      saveModalLoading: t,
      fieldProperties: n
    } = at(y), i = B(!1);
    se(() => y.status, (e) => {
      i.value = e;
    }, { immediate: !0 });
    function a() {
      i.value && j && (Q.values = pe(j)), l.openModal(), A.value = !0, L(!0);
    }
    function m() {
      return document.getElementsByClassName("jee-super-query-form")[0];
    }
    function p(e) {
    }
    function r(e) {
      var f, w;
      e.val = "";
      const u = (_, C) => {
        const g = _.find((ce) => ce.value === C);
        (g == null ? void 0 : g.fieldType) === "string" && ["text"].includes((g == null ? void 0 : g.originView) || (g == null ? void 0 : g.view)) ? e.rule = "like" : ["file", "image", "password"].includes((g == null ? void 0 : g.originView) || (g == null ? void 0 : g.view)) ? e.rule = "empty" : e.rule = "eq";
      };
      if (((f = e.field) == null ? void 0 : f.indexOf("@")) == -1)
        u(P.value, e.field);
      else {
        const _ = e.field.split("@")[0], C = P.value.find((g) => g.value === _);
        (w = C == null ? void 0 : C.children) != null && w.length && u(C.children, e.field);
      }
    }
    se(() => y.config, (e) => {
      e && J(e);
    }, { immediate: !0 });
    const h = Pe(() => P.value.find((u) => u.children) ? `${T} containTable` : `${T} noTable`);
    return {
      formRef: ne,
      registerFormModal: S,
      init: J,
      handleChangeField: r,
      dynamicRowValues: Q,
      matchType: E,
      historyCollapsed: A,
      registerModal: oe,
      handleSubmit: Y,
      handleCancel: M,
      handleSave: z,
      handleReset: te,
      doSaveQueryInfo: F,
      saveInfo: W,
      saveTreeData: G,
      handleTreeSelect: ae,
      handleRemoveSaveInfo: ie,
      fieldTreeData: P,
      addOne: L,
      removeOne: s,
      setFormModel: x,
      getSchema: b,
      loading: re,
      onFinish: p,
      getPopupContainer: m,
      superQueryFlag: i,
      handleOpen: a,
      initDefaultValues: me,
      simpleImage: Ye.PRESENTED_IMAGE_SIMPLE,
      saveModalLoading: t,
      queryFormStyle: R,
      handleFullScreen: le,
      fieldTreeSelectHeight: V,
      getTreePopupClass: h,
      handleStop: K,
      getQueryCondition: U
    };
  }
};
const rt = { class: "j-super-query-button" }, ut = { style: { float: "left" } }, st = { slot: "description" }, ct = ["title"], dt = { style: { height: "80px", "line-height": "75px", width: "100%", "text-align": "center" } };
function ft(y, o, S, l, k, R) {
  const V = v("divider"), $ = v("AppstoreTwoTone"), T = v("a-button"), A = v("a-button-group"), j = v("a-tooltip"), ee = v("a-divider"), U = v("a-empty"), M = v("a-select-option"), Y = v("a-select"), I = v("a-form-item"), q = v("a-col"), K = v("a-row"), te = v("a-tree-select"), le = v("online-super-query-val-component"), ne = v("PlusOutlined"), J = v("MinusCircleOutlined"), Q = v("a-space"), E = v("a-form"), oe = v("close-circle-outlined"), z = v("a-popconfirm"), F = v("file-text-outlined"), W = v("a-tree"), G = v("Icon"), ae = v("a-card"), ie = v("BasicModal"), P = v("a-input"), L = v("a-modal");
  return O(), ve(ye, null, [
    D("div", rt, [
      l.superQueryFlag ? (O(), N(j, {
        key: 0,
        mouseLeaveDelay: 0.2
      }, {
        title: c(() => [
          o[7] || (o[7] = D("span", null, "执行查询中...", -1)),
          d(V, {
            type: "vertical",
            style: { "background-color": "#fff" }
          }),
          D("a", {
            onClick: o[0] || (o[0] = (...s) => l.handleStop && l.handleStop(...s))
          }, "取消查询")
        ]),
        default: c(() => [
          d(A, null, {
            default: c(() => [
              d(T, {
                type: "primary",
                onClick: l.handleOpen
              }, {
                default: c(() => [
                  d($, { spin: !0 }),
                  D("span", null, de(S.queryBtnCfg.buttonName), 1)
                ]),
                _: 1
              }, 8, ["onClick"])
            ]),
            _: 1
          })
        ]),
        _: 1
      })) : (O(), N(T, {
        key: 1,
        type: "primary",
        preIcon: S.queryBtnCfg.buttonIcon,
        onClick: l.handleOpen
      }, {
        default: c(() => [
          D("span", null, de(S.queryBtnCfg.buttonName), 1)
        ]),
        _: 1
      }, 8, ["preIcon", "onClick"]))
    ]),
    (O(), N(Ve, { to: "body" }, [
      d(ie, {
        title: S.queryBtnCfg.buttonName + "构造器",
        "wrap-class-name": "j-super-query-modal",
        canFullscreen: !0,
        width: 850,
        onRegister: l.registerFormModal,
        onOk: l.handleSubmit,
        onFullScreen: l.handleFullScreen
      }, {
        footer: c(() => [
          D("div", ut, [
            d(T, {
              loading: l.loading,
              onClick: l.handleReset
            }, {
              default: c(() => o[8] || (o[8] = [
                H("清空")
              ])),
              _: 1
            }, 8, ["loading", "onClick"]),
            d(T, {
              loading: l.loading,
              onClick: l.handleSave
            }, {
              default: c(() => o[9] || (o[9] = [
                H("保存查询")
              ])),
              _: 1
            }, 8, ["loading", "onClick"])
          ]),
          d(T, {
            key: "submit",
            type: "primary",
            onClick: l.handleSubmit
          }, {
            default: c(() => o[10] || (o[10] = [
              H("执行查询")
            ])),
            _: 1
          }, 8, ["onClick"]),
          d(T, {
            key: "back",
            onClick: l.handleCancel
          }, {
            default: c(() => o[11] || (o[11] = [
              H("关闭")
            ])),
            _: 1
          }, 8, ["onClick"])
        ]),
        default: c(() => [
          l.dynamicRowValues.values.length == 0 ? (O(), N(U, { key: 0 }, {
            default: c(() => [
              D("div", st, [
                o[12] || (o[12] = D("span", null, "没有任何查询条件", -1)),
                d(ee, { type: "vertical" }),
                D("a", {
                  onClick: o[1] || (o[1] = (s) => l.addOne(-1))
                }, "点击新增")
              ])
            ]),
            _: 1
          })) : Ie("", !0),
          d(K, {
            class: _e("j-super-query-modal-content")
          }, {
            default: c(() => [
              d(q, {
                sm: 24,
                md: 24
              }, {
                default: c(() => [
                  Fe(d(K, null, {
                    default: c(() => [
                      d(q, {
                        md: 12,
                        xs: 24
                      }, {
                        default: c(() => [
                          d(I, {
                            label: "匹配模式",
                            labelCol: { md: 6, xs: 24 },
                            wrapperCol: { md: 18, xs: 24 },
                            style: { width: "100%" }
                          }, {
                            default: c(() => [
                              d(Y, {
                                value: l.matchType,
                                "onUpdate:value": o[2] || (o[2] = (s) => l.matchType = s),
                                getPopupContainer: (s) => s == null ? void 0 : s.parentNode,
                                style: { width: "100%" }
                              }, {
                                default: c(() => [
                                  d(M, { value: "and" }, {
                                    default: c(() => o[13] || (o[13] = [
                                      H("AND（所有条件匹配）")
                                    ])),
                                    _: 1
                                  }),
                                  d(M, { value: "or" }, {
                                    default: c(() => o[14] || (o[14] = [
                                      H("OR（任意一个匹配）")
                                    ])),
                                    _: 1
                                  })
                                ]),
                                _: 1
                              }, 8, ["value", "getPopupContainer"])
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      })
                    ]),
                    _: 1
                  }, 512), [
                    [be, l.dynamicRowValues.values.length > 0]
                  ]),
                  Fe(d(E, {
                    ref: "formRef",
                    class: _e("jee-super-query-form"),
                    model: l.dynamicRowValues,
                    onFinish: l.onFinish,
                    style: Ne(l.queryFormStyle)
                  }, {
                    default: c(() => [
                      (O(!0), ve(ye, null, ke(l.dynamicRowValues.values, (s, x) => (O(), N(Q, {
                        key: s.key,
                        style: { display: "flex", "margin-bottom": "8px" },
                        align: s.curLineAlign ? s.align : "baseline"
                      }, {
                        default: c(() => [
                          d(I, {
                            class: "field-clos",
                            name: ["values", x, "field"]
                          }, {
                            default: c(() => [
                              d(te, {
                                popupClassName: l.getTreePopupClass,
                                style: { width: "100%" },
                                placeholder: "请选择字段",
                                value: s.field,
                                "onUpdate:value": (b) => s.field = b,
                                "show-search": "",
                                "tree-node-filter-prop": "title",
                                "allow-clear": "",
                                "tree-default-expand-all": "",
                                "dropdown-style": { maxHeight: `${l.fieldTreeSelectHeight}px`, overflow: "auto" },
                                listHeight: l.fieldTreeSelectHeight - 20,
                                onChange: (b) => l.handleChangeField(s),
                                "tree-data": l.fieldTreeData
                              }, null, 8, ["popupClassName", "value", "onUpdate:value", "dropdown-style", "listHeight", "onChange", "tree-data"])
                            ]),
                            _: 2
                          }, 1032, ["name"]),
                          d(I, {
                            class: "rule-clos",
                            name: ["values", x, "rule"]
                          }, {
                            default: c(() => [
                              d(Y, {
                                style: { width: "100%" },
                                placeholder: "请选择匹配规则",
                                value: s.rule,
                                "onUpdate:value": (b) => s.rule = b
                              }, {
                                default: c(() => [
                                  (O(!0), ve(ye, null, ke(l.getQueryCondition(s), (b) => (O(), N(M, {
                                    value: b.value,
                                    key: b.value
                                  }, {
                                    default: c(() => [
                                      H(de(b.label), 1)
                                    ]),
                                    _: 2
                                  }, 1032, ["value"]))), 128))
                                ]),
                                _: 2
                              }, 1032, ["value", "onUpdate:value"])
                            ]),
                            _: 2
                          }, 1032, ["name"]),
                          d(I, {
                            class: "component-clos",
                            name: ["values", x, "val"]
                          }, {
                            default: c(() => [
                              d(le, {
                                style: { width: "100%" },
                                schema: l.getSchema(s, x),
                                formModel: s,
                                setFormModel: (b, re) => {
                                  l.setFormModel(b, re, s);
                                },
                                onSubmit: l.handleSubmit
                              }, null, 8, ["schema", "formModel", "setFormModel", "onSubmit"])
                            ]),
                            _: 2
                          }, 1032, ["name"]),
                          d(I, null, {
                            default: c(() => [
                              d(T, {
                                onClick: (b) => l.addOne(x),
                                style: { "margin-right": "6px" }
                              }, {
                                default: c(() => [
                                  d(ne)
                                ]),
                                _: 2
                              }, 1032, ["onClick"]),
                              d(T, {
                                onClick: (b) => l.removeOne(s)
                              }, {
                                default: c(() => [
                                  d(J)
                                ]),
                                _: 2
                              }, 1032, ["onClick"])
                            ]),
                            _: 2
                          }, 1024)
                        ]),
                        _: 2
                      }, 1032, ["align"]))), 128))
                    ]),
                    _: 1
                  }, 8, ["model", "onFinish", "style"]), [
                    [be, l.dynamicRowValues.values.length > 0]
                  ])
                ]),
                _: 1
              })
            ]),
            _: 1
          }),
          d(ae, {
            class: _e(["j-super-query-history-card", { collapsed: l.historyCollapsed }]),
            bordered: !1
          }, {
            title: c(() => o[15] || (o[15] = [
              D("div", null, "保存的查询", -1)
            ])),
            default: c(() => [
              l.saveTreeData.length === 0 ? (O(), N(U, {
                key: 0,
                class: "j-super-query-history-empty",
                image: l.simpleImage,
                description: "没有保存的查询"
              }, null, 8, ["image"])) : (O(), N(W, {
                key: 1,
                class: "j-super-query-history-tree",
                treeData: l.saveTreeData,
                selectedKeys: [],
                "show-icon": !0,
                onSelect: l.handleTreeSelect
              }, {
                title: c(({ title: s }) => [
                  D("div", null, [
                    D("span", { title: s }, de(s.length > 10 ? s.substring(0, 10) + "..." : s), 9, ct),
                    d(z, {
                      title: "确定删除吗？",
                      onConfirm: (x) => l.handleRemoveSaveInfo(s)
                    }, {
                      default: c(() => [
                        D("span", {
                          class: "icon-cancle",
                          onClick: o[3] || (o[3] = (x) => x.stopPropagation())
                        }, [
                          d(oe)
                        ])
                      ]),
                      _: 2
                    }, 1032, ["onConfirm"])
                  ])
                ]),
                custom: c(() => [
                  d(F)
                ]),
                _: 1
              }, 8, ["treeData", "onSelect"])),
              D("div", {
                class: "collapse-box",
                onClick: o[4] || (o[4] = (s) => l.historyCollapsed = !l.historyCollapsed)
              }, [
                l.historyCollapsed ? (O(), N(G, {
                  key: 0,
                  icon: "ant-design:caret-left"
                })) : (O(), N(G, {
                  key: 1,
                  icon: "ant-design:caret-right"
                }))
              ])
            ]),
            _: 1
          }, 8, ["class"])
        ]),
        _: 1
      }, 8, ["title", "onRegister", "onOk", "onFullScreen"])
    ])),
    d(L, {
      title: "请输入保存的名称",
      open: l.saveInfo.visible,
      onCancel: o[6] || (o[6] = (s) => l.saveInfo.visible = !1),
      onOk: l.doSaveQueryInfo,
      confirmLoading: l.saveModalLoading
    }, {
      default: c(() => [
        D("div", dt, [
          d(P, {
            value: l.saveInfo.title,
            "onUpdate:value": o[5] || (o[5] = (s) => l.saveInfo.title = s),
            style: { width: "90%" },
            placeholder: "请输入保存的名称"
          }, null, 8, ["value"])
        ])
      ]),
      _: 1
    }, 8, ["open", "onOk", "confirmLoading"])
  ], 64);
}
const tl = /* @__PURE__ */ lt(it, [["render", ft], ["__scopeId", "data-v-c52e7338"]]);
export {
  tl as default
};
