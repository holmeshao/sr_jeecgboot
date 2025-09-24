var Pe = Object.defineProperty, Oe = Object.defineProperties;
var Te = Object.getOwnPropertyDescriptors;
var he = Object.getOwnPropertySymbols;
var Re = Object.prototype.hasOwnProperty, ke = Object.prototype.propertyIsEnumerable;
var be = (l, m, s) => m in l ? Pe(l, m, { enumerable: !0, configurable: !0, writable: !0, value: s }) : l[m] = s, B = (l, m) => {
  for (var s in m || (m = {}))
    Re.call(m, s) && be(l, s, m[s]);
  if (he)
    for (var s of he(m))
      ke.call(m, s) && be(l, s, m[s]);
  return l;
}, ee = (l, m) => Oe(l, Te(m));
var L = (l, m, s) => new Promise((M, T) => {
  var R = (S) => {
    try {
      v(s.next(S));
    } catch (O) {
      T(O);
    }
  }, g = (S) => {
    try {
      v(s.throw(S));
    } catch (O) {
      T(O);
    }
  }, v = (S) => S.done ? M(S.value) : Promise.resolve(S.value).then(R, g);
  v((s = s.apply(l, m)).next());
});
import { E as te, s as Fe } from "./useExtendComponent-bb98e568.mjs";
import { useRoute as Be } from "vue-router";
import { router as ye } from "/@/router";
import { ref as P, provide as Ie, onBeforeUnmount as _e, toRaw as I, nextTick as Se, reactive as re, computed as Me } from "vue";
import { defHttp as _ } from "/@/utils/http/axios";
import { useMessage as De } from "/@/hooks/web/useMessage";
import { filterObj as Ee } from "/@/utils/common/compUtils";
import { u as Ne, G as Ce } from "./useCustomHook-acb00837.mjs";
import { onMountedOrActivated as $e } from "/@/hooks/core/onMountedOrActivated";
import { useModal as H } from "/@/components/Modal";
import { E as ae } from "./constant-fa63bd66.mjs";
import { useMultipleTabStore as Ue } from "/@/store/modules/multipleTab";
import { u as Qe } from "./cgformState-d9f8ec42.mjs";
import { pick as Ve } from "lodash-es";
import { Modal as Ke } from "ant-design-vue";
import { useMethods as je } from "/@/hooks/system/useMethods";
import { getToken as Le } from "/@/utils/auth";
import { goJmReportViewPage as ze } from "/@/utils";
const He = {
  acceptHrefParams: "<p> 跳转时获取的参数信息",
  currentPage: "<p> 当前页数",
  currentTableName: "<p> 当前表名",
  description: "<p> 当前表描述",
  hasChildrenField: "<p> 是否有子节点的字段名，仅树形表单下有效",
  isDesForm: "<p> xx",
  isTree: "<m> 是否是树形表单 ",
  loadData: "<m> 加载列表数据",
  pageSize: "<p> 每一页显示条数",
  queryParam: "<p> 查询条件对象，每次点击查询后才会更新此数据",
  selectedRowKeys: "<p> 选中的行的id数组",
  sortField: "<p> 排序字段",
  sortType: "<p> 排序规则",
  total: "<p> 总页数",
  foreignKeyValue: "<p> Erp一对多子表外键选中对应主表字段的值",
  isErpSubTable: "<p> 是否Erp一对多子表",
  foreignKeyField: "<p> Erp一对多子表外键字段",
  themeTemplate: "<p> 主题模板",
  isInnerSubTable: "<p> 是否内嵌一对多子表",
  innerSubTableId: "<p>内嵌一对多子表ID",
  innerSubTableName: "<p> 内嵌一对多子表名",
  mTableSelectedRcordId: "<p>内嵌主表展开行的id",
  innerSubTableFk: "<p>内嵌子表的外键字段"
}, Je = {
  getColumns: "/online/cgform/api/getColumns/",
  getQueryInfo: "/online/cgform/api/getQueryInfo/",
  getData: "/online/cgform/api/getData/",
  getTreeData: "/online/cgform/api/getTreeData/",
  optPre: "/online/cgform/api/form/",
  buttonAction: "/online/cgform/api/doButton",
  exportXls: "/online/cgform/api/exportXlsOld/",
  importXls: "/online/cgform/api/importXls/",
  startProcess: "/act/process/extActProcess/startMutilProcess",
  getErpColumns: "/online/cgform/api/getErpColumns/",
  // 内嵌主题一对多子表数据请求接口
  list: "/online/cgform/api/subform/list/"
};
let Ae = {
  sortField: "id",
  sortType: "asc",
  currentPage: 1,
  pageSize: 10,
  total: 0,
  selectedRowKeys: [],
  queryParam: {},
  acceptHrefParams: {},
  description: "",
  currentTableName: "",
  isDesForm: !1,
  desFormCode: "",
  cache: !1,
  isTree: !1,
  hasChildrenField: ""
};
const we = {
  current: 1,
  pageSize: 10,
  pageSizeOptions: ["10", "20", "30"],
  showTotal: (l, m) => m[0] + "-" + m[1] + " 共" + l + "条",
  showQuickJumper: !0,
  showSizeChanger: !0,
  total: 0
}, { createMessage: z, createErrorModal: qe } = De();
function dt(l = {}) {
  var x;
  const m = (x = l.code) != null ? x : "", s = P(m);
  Ie("tableId", s);
  const M = Be(), T = P(), R = P(), g = P(!1), v = P([]), S = P(!0), O = P(), J = Qe(), ne = Ue();
  let h = {};
  const b = {
    execButtonEnhance: function(r, n) {
      if (i[te][r])
        if (Fe === r)
          k(r);
        else {
          let u = I(n);
          return i[te][r].call(i, i, u);
        }
      else if (i[te][r + "_hook"])
        if (n) {
          let u = I(n);
          k(r + "_hook", u);
        } else
          k(r + "_hook");
    },
    /**
     * get 是否是树形表单
     * @param status 如果有值 则视为set方法
     */
    isTree: function(r) {
      return typeof r == "boolean" ? (i.isTreeTable = r, r) : i.isTreeTable;
    }
  };
  function k(r, n) {
    let p = i[te][r].toLocaleString().match(Ce);
    if (p.length > 1) {
      let f = p[1];
      w(f, n);
    }
  }
  const i = new Proxy(He, {
    get(r, n) {
      if (typeof b[n] == "function")
        return b[n];
      {
        let u = h[s.value];
        return u == null ? u : Reflect.get(u, n);
      }
    },
    set(r, n, u) {
      let p = le();
      return Reflect.set(typeof u == "function" ? b : p, n, u);
    },
    deleteProperty(r, n) {
      return n === s.value ? (delete h[n], !0) : !1;
    }
  }), { executeJsEnhanced: w } = Ne({}, i);
  function U() {
    let r = M.params.id;
    return r || (r = ""), r;
  }
  $e(({ type: r }) => {
    !m && Z(), r === "activated" && J.checkIsChanged(s.value) && ne.refreshPage(ye), s.value && J.removeChangedTable(s.value);
  }), _e(() => {
    delete h[s.value];
  });
  function le() {
    let r = h[s.value];
    if (!r) {
      let n = Object.assign({}, Ae, { onlineUrl: Je });
      r = JSON.parse(JSON.stringify(n)), l.themeTemplate == ae && (r.pageSize = 5), h[s.value] = r;
    }
    return r;
  }
  function ie() {
    let r = {}, n = M.query;
    n && (Object.keys(n).map((u) => {
      r[u] = n[u];
    }), i.acceptHrefParams = r);
  }
  function oe(r = "") {
    let n;
    return r == ae ? n = `${i.onlineUrl.getErpColumns}${s.value}` : n = `${i.onlineUrl.getColumns}${s.value}`, new Promise((u, p) => {
      _.get(
        {
          url: n
        },
        { isTransformResponse: !1 }
      ).then((f) => {
        f.success ? u(f.result) : (z.warning(f.message), p());
      }).catch(() => {
        p();
      });
    });
  }
  function N(r = {}) {
    const { delNum: n } = r;
    return new Promise((u, p) => {
      if (n != null) {
        const { total: t, pageSize: a, current: o } = g.value, c = Math.ceil(t / a);
        o === c && (g.value.current = Math.ceil((t - n) / a));
      }
      let f = A(), e = `${i.onlineUrl.getData}${s.value}`;
      i.isTree() === !0 ? e = `${i.onlineUrl.getTreeData}${s.value}` : i.isInnerSubTable === !0 && (e = `${i.onlineUrl.getData}${i.innerSubTableId}`, f = { pageSize: -521 }, i.innerSubTableFk && i.mTableSelectedRcordId && (f[i.innerSubTableFk] = i.mTableSelectedRcordId)), i.isErpSubTable === !0 && (f[i.foreignKeyField] = i.foreignKeyValue, f.tabletype = 3, delete f.hasQuery), _.get({ url: e, params: f }, { isTransformResponse: !1 }).then((t) => {
        t.success ? (se(t.result), u(!0)) : (t.message === "NO_DB_SYNC" ? qe({
          title: "数据库未同步",
          content: "请先同步数据库再查看此页面！",
          // 点击确定后自动返回上一页
          onOk: () => ye.back()
        }) : z.warning(t.message), p(!1));
      }).catch(() => {
        let t = "请求列表数据异常!";
        z.warning(t), p(!1);
      });
    });
  }
  function A() {
    const { sortField: r, sortType: n, acceptHrefParams: u, queryParam: p } = i;
    let f = {};
    i.isTree(), f.hasQuery = "true";
    let e = Object.assign({}, f, u, p, { column: r, order: n });
    g.value ? (e.pageNo = g.value.current, e.pageSize = g.value.pageSize) : e.pageSize = -521;
    let t = pe();
    return e.superQueryMatchType = t.matchType || "", e.superQueryParams = t.params || "", Ee(e);
  }
  function se(r) {
    let n = 0;
    Number(r.total) > 0 ? (i.isTree() === !0 ? (v.value = V(r.records), Se(() => {
      fe(v.value);
    })) : v.value = r.records, n = Number(r.total)) : v.value = [], g.value && (g.value = ee(B({}, g.value), { total: n }));
  }
  function q(r, n, u) {
    u && u.order ? (i.sortField = u.field, i.sortType = u.order == "ascend" ? "asc" : "desc") : (i.sortField = "id", i.sortType = "asc"), g.value && (g.value = r), N();
  }
  function X(r) {
    i.description = r.description, i.currentTableName = r.currentTableName, i.isDesForm = r.isDesForm, i.desFormCode = r.desFormCode, i.ID = s.value;
    let { acceptHrefParams: n, queryParam: u, superQuery: p, currentPage: f, pageSize: e } = i;
    if (ie(), u ? T.value && T.value.initDefaultValues(u, n) : i.queryParam = {}, p ? R.value && R.value.initDefaultValues(p) : i.superQuery = { params: "", matchType: "" }, r.paginationFlag == "Y") {
      let t = we.pageSizeOptions;
      l.themeTemplate == ae && (t = ["5", "10", "30"]), g.value = ee(B({}, we), { current: f, pageSize: e, pageSizeOptions: t });
    } else
      g.value = !1;
  }
  function ue() {
    return L(this, null, function* () {
      S.value = !0, yield Se(), S.value = !1;
    });
  }
  const Q = {
    loadData: N,
    getLoadDataParams: A,
    reloadTable: ue
  };
  Object.keys(Q).map((r) => {
    i[r] = Q[r];
  });
  let G = P(!1);
  function ce() {
    return L(this, arguments, function* (r = {}) {
      g.value && (g.value = ee(B({}, g.value), { current: r.mode == "search" || !g.value.current ? 1 : g.value.current })), l.themeTemplate !== ae && i.clearSelectedRow(), yield N();
    });
  }
  function V(r) {
    if (r)
      return r.map((n) => {
        let u = i.hasChildrenField;
        if (n[u] == "1") {
          let p = { id: n.id + "_loadChild", name: "loading...", isLoading: !0 };
          p.jeecg_row_key = p.id, n.children = [p];
        }
        return n;
      });
  }
  const K = P([]);
  function j(r) {
    K.value = r;
  }
  function fe(r) {
    let n = K.value;
    if (n.length > 0) {
      const { sortField: u, sortType: p, pidField: f } = i;
      let e = Object.assign({}, { column: u, order: p });
      e.hasQuery = "in";
      let t = Object.assign({});
      t.rule = "in", t.type = "text", t.val = n.join(","), t.field = f, t = [t], e.superQueryParams = encodeURI(JSON.stringify(t)), e.superQueryMatchType = "and", e.batchFlag = "true";
      let a = `${i.onlineUrl.getTreeData}${s.value}`;
      _.get({ url: a, params: e }, { isTransformResponse: !1 }).then((o) => {
        if (o.success && o.result.records && o.result.records.length > 0) {
          let c = o.result.records;
          const d = /* @__PURE__ */ new Map();
          for (let D of c) {
            let E = D[f];
            if (n.join(",").includes(E)) {
              let $ = d.get(E);
              $ == null && ($ = []), $.push(D), d.set(E, $);
            }
          }
          let y = d, F = (D) => {
            D && D.forEach((E) => {
              n.includes(E.id) && (E.children = V(y.get(E.id)), F(E.children));
            });
          };
          F(r);
        }
      }).catch(() => {
        let o = "loadDataByExpandedRows请求列表数据异常!";
        z.warning(o);
      });
    } else
      return Promise.resolve();
  }
  function pe() {
    if (!i.superQuery)
      return {};
    const {
      superQuery: { params: r, matchType: n },
      currentTableName: u
    } = i;
    let p = u + "@", f = [];
    if (r.length > 0)
      for (let t of r) {
        let a = B({}, t), o = a.field;
        o.startsWith(p) && (a.field = o.replace(p, "")), f.push(a);
      }
    let e = f.length > 0 ? JSON.stringify(f) : "";
    return {
      params: encodeURIComponent(e),
      matchType: n
    };
  }
  const C = P(!1);
  function W(r, n) {
    i.superQuery = {
      params: r,
      matchType: n
    }, r.length == 0 || r.length == null ? C.value = !1 : C.value = !0, g.value.current = 1, N();
  }
  const [de, { openModal: Y }] = H();
  function me(r) {
    if (r || (r = {}), !r.row) {
      let n = i.selectedRows;
      if (!n || n.length == 0 || n.length > 1) {
        z.warning("请选择一条数据");
        return;
      }
      r.row = n[0];
    }
    r.code = s.value, Y(!0, r);
  }
  i.openCustomModal = me;
  function Z() {
    let r = U();
    s.value = r;
  }
  !m && !s.value && Z();
  function ge(r) {
    let n = r.head.extConfigJson;
    n && (O.value = JSON.parse(n));
  }
  return B({
    ID: s,
    onlineQueryFormOuter: T,
    superQueryButtonRef: R,
    loading: G,
    reload: ce,
    dataSource: v,
    pagination: g,
    tableReloading: S,
    handleSpecialConfig: X,
    onlineTableContext: i,
    handleChangeInTable: q,
    getColumnList: oe,
    getTreeDataByResult: V,
    expandedRowKeys: K,
    handleExpandedRowsChange: j,
    onlineExtConfigJson: O,
    handleFormConfig: ge,
    superQueryStatus: C,
    handleSuperQuery: W,
    registerCustomModal: de
  }, Q);
}
const ve = "onl_";
function mt(l, m, s = {}) {
  const M = {
    add: !0,
    addSub: !0,
    // edit = 编辑按钮的code
    edit: !0,
    // update = 编辑按钮的老code
    update: !0,
    delete: !0,
    batch_delete: !0,
    import: !0,
    export: !0,
    detail: !0,
    query: !0,
    reset: !0,
    super_query: !0,
    bpm: !0,
    form_confirm: !0,
    // 子表新增
    form_sub_add: !0,
    // 子表删除
    form_sub_batch_delete: !0,
    // 子表新增
    form_sub_open_add: !0,
    // 子表编辑
    form_sub_open_edit: !0
  }, [T, { openModal: R }] = H(), [g, { openModal: v }] = H(), [S, { openModal: O }] = H(), [J, { openModal: ne }] = H(), { createMessage: h } = De(), b = re(M), k = re([]), i = re([]), w = re({}), U = (e) => Me(() => b[e] === !0 ? w[e] : { enabled: !1 }), le = U("query"), ie = U("reset"), oe = U("form_confirm");
  function N(e) {
    if (k.length = 0, i.length = 0, e && e.length > 0)
      for (let t = 0; t < e.length; t++) {
        let a = Ve(e[t], "buttonCode", "buttonName", "buttonStyle", "optType", "exp", "buttonIcon", "buttonStatus", "enabled");
        a.buttonStyle == "button" ? i.push(a) : a.buttonStyle == "link" ? k.push(a) : a.buttonStyle == "built-in" && (a.buttonIcon && (a.buttonIcon = "ant-design:" + a.buttonIcon), a.enabled = a.buttonStatus === "1", w[a.buttonCode] = a);
      }
  }
  function A(e) {
    Object.keys(b).forEach((t) => {
      b[t] = !0;
    }), e && e.length > 0 && Object.keys(b).forEach((t) => {
      e.indexOf(t) >= 0 && (b[t] = !1);
    });
  }
  function se(e) {
    let t = { isUpdate: !1 };
    e && (t.param = e), R(!0, t);
  }
  function q(e) {
    l.beforeEdit(e).then(() => {
      R(!0, {
        isUpdate: !0,
        record: e
      });
    }).catch((t) => {
      h.warning(t);
    });
  }
  const X = (e) => ({
    label: w.delete.buttonName,
    ifShow: () => w.delete.enabled,
    popConfirm: {
      title: "是否删除？",
      confirm: ue.bind(null, e)
    }
  });
  function ue(e) {
    l.beforeDelete(e).then(() => {
      W(e.id, !1);
    }).catch((t) => {
      h.warning(t);
    });
  }
  function Q(e) {
    let t = j(e), a = t && (t == "1" || t == "3" || t == "4") || !t;
    return I(b.edit) === !0 && I(b.update) === !0 && a ? [
      {
        label: w.edit.buttonName,
        ifShow: () => w.edit.enabled,
        onClick: (o) => {
          s.editClickCallback && s.editClickCallback(e.id, o), q(e);
        }
      }
    ] : [];
  }
  function G(e) {
    return {
      label: w.bpm.buttonName,
      ifShow: () => w.bpm.enabled,
      popConfirm: {
        title: "确认提交流程吗？",
        confirm: C.bind(null, e)
      }
    };
  }
  function ce(e) {
    return {
      label: "审批进度",
      onClick: V.bind(null, e)
    };
  }
  function V(e) {
    const { currentTableName: t } = l;
    let a = t;
    t.includes("$") && (a = t.split("$")[0]);
    let o = ve + a, c = e.id;
    ne(!0, {
      flowCode: o,
      dataId: c
    });
  }
  function K(e, t = {}) {
    let a = [];
    if (I(b.detail) === !0 && a.push({
      label: w.detail.buttonName,
      ifShow: () => w.detail.enabled,
      onClick: fe.bind(null, e)
    }), l.hasBpmStatus === !0 && I(b.bpm) === !0) {
      let y = j(e);
      !y || y == "1" ? a.push(G(e)) : a.push(ce(e));
    }
    if (m.value) {
      let { reportPrintShow: y, reportPrintUrl: F } = m.value;
      y && F && a.push({
        label: "打印",
        onClick() {
          let D = F, E = e.id, $ = Le();
          ze(D, E, $);
        }
      });
    }
    let o = j(e), c = o && o == "1" || !o;
    I(b.delete) === !0 && c && a.push(X(e));
    let d = k;
    if (d && d.length > 0)
      for (let y of d)
        p(y.exp || "", e) === !0 && a.push({
          label: y.buttonName,
          onClick: Y.bind(null, e, y.buttonCode, y.optType)
        });
    return a;
  }
  function j(e) {
    const t = "bpm_status";
    let a = e[t];
    return a || (a = e[t.toUpperCase()]), a;
  }
  function fe(e) {
    O(!0, {
      isUpdate: !0,
      disableSubmit: !0,
      record: e
    });
  }
  function pe(e) {
    const {
      currentTableName: t,
      onlineUrl: { startProcess: a }
    } = l;
    let o = t;
    t.includes("$") && (o = t.split("$")[0]);
    let c = {
      url: a,
      params: {
        flowCode: ve + o,
        id: e.id,
        // TODO 流程表单没有
        formUrl: "modules/bpm/task/form/OnlineFormDetail",
        formUrlMobile: "check/onlineForm/detail"
      }
    }, d = { isTransformResponse: !1 };
    return new Promise((y, F) => {
      _.post(c, d).then((D) => {
        D.success ? (y(D), h.success(D.message)) : (F(), h.warning(D.message));
      });
    });
  }
  function C(e) {
    return L(this, null, function* () {
      yield pe(e), l.loadData();
    });
  }
  function W(e, t = !0) {
    let a = `${l.onlineUrl.optPre}${l.ID}/${e}`;
    return l.isErpSubTable === !0 && (a = `${a}?tabletype=3`), new Promise((o, c) => {
      _.delete(
        {
          url: a
        },
        { isTransformResponse: !1 }
      ).then((d) => {
        d.success ? (h.success(d.message), l.loadData({ delNum: e.split(",").length }), t || s.singleDelCallback && s.singleDelCallback(e), o(!0)) : (h.warning(d.message), c());
      });
    });
  }
  function de() {
    let e = l.selectedRowKeys;
    if (e.length <= 0)
      return h.warning("请选择一条记录！"), !1;
    {
      let t = [];
      e.forEach(function(o) {
        let c = o;
        c && c.endsWith("_loadChild") && (c = c.replace("_loadChild", "")), t.indexOf(c) < 0 && t.push(c);
      });
      let a = t.join(",");
      Ke.confirm({
        title: "确认删除",
        content: "是否删除选中数据",
        okText: "确认",
        cancelText: "取消",
        onOk: () => L(this, null, function* () {
          yield W(a), l.clearSelectedRow();
        })
      });
    }
  }
  function Y(e, t, a) {
    if (a == "js")
      l.execButtonEnhance(t, e);
    else if (a == "action") {
      let o = {
        formId: l.ID,
        buttonCode: t,
        dataId: e.id
      }, c = `${l.onlineUrl.buttonAction}`;
      _.post(
        {
          url: c,
          params: o
        },
        { isTransformResponse: !1 }
      ).then((d) => {
        d.success ? (l.loadData(), h.success("处理完成!")) : h.warning(d.message);
      });
    }
  }
  function me(e) {
    l.execButtonEnhance(e);
  }
  function Z(e) {
    let t = l.selectedRowKeys;
    if (!t || t.length == 0)
      return h.warning("请先选中一条记录"), !1;
    let a = t.join(","), o = {
      formId: l.ID,
      buttonCode: e,
      dataId: a
    }, c = `${l.onlineUrl.buttonAction}`;
    _.post(
      {
        url: c,
        params: o
      },
      { isTransformResponse: !1 }
    ).then((d) => {
      d.success ? (l.loadData(), l.clearSelectedRow(), h.success("处理完成!")) : h.warning(d.message);
    });
  }
  function ge() {
    l.foreignKeyField && l.foreignKeyValue ? v(!0, {
      [l.foreignKeyField]: l.foreignKeyValue
    }) : v(!0);
  }
  const x = () => {
    let e = `${l.onlineUrl.importXls}${l.ID}`;
    return l.isErpSubTable === !0 && (e = `${e}?tabletype=3`), e;
  }, { handleExportXlsx: r } = je();
  function n() {
    let e = l.getLoadDataParams(), t = l.selectedRowKeys;
    t && t.length > 0 && (e.selections = t.join(","));
    let a = {};
    l.isErpSubTable === !0 && (a = { tabletype: 3 }, l.foreignKeyField && l.foreignKeyValue && (e[l.foreignKeyField] = l.foreignKeyValue));
    let o = JSON.stringify(Ee(e)), c = `${l.onlineUrl.exportXls}${l.ID}`;
    const d = l.description;
    return r(d, c, B({ paramsStr: o }, a));
  }
  function u(e, t) {
    const a = [];
    e.split("||").forEach((c) => {
      const d = [];
      c.trim().split("&&").forEach((y) => {
        d.push(f(y.trim(), t));
      }), a.push(d.join("&&"));
    });
    const o = a.join("||");
    return new Function(`return ${o}`)();
  }
  function p(e, t) {
    return !e || e == "" ? !0 : e.indexOf("||") == -1 && e.indexOf("&&") == -1 ? f(e, t) : u(e, t);
  }
  function f(e, t) {
    if (!e || e == "")
      return !0;
    let a = e.split("#"), o = t[a[0]], c = a[1].toLowerCase();
    return c === "eq" ? o == a[2] : c === "ne" ? o != a[2] : c === "empty" ? a[2] === "true" ? !o || o == "" : o && o.length > 0 : c === "in" ? a[2].split(",").indexOf(String(o)) >= 0 : !1;
  }
  return {
    buttonSwitch: b,
    cgLinkButtonList: k,
    cgBIBtnMap: w,
    getQueryButtonCfg: le,
    getResetButtonCfg: ie,
    getFormConfirmButtonCfg: oe,
    cgTopButtonList: i,
    importUrl: x,
    registerModal: T,
    handleAdd: se,
    handleEdit: q,
    handleBatchDelete: de,
    registerImportModal: g,
    onImportExcel: ge,
    onExportExcel: n,
    getDropDownActions: K,
    getActions: Q,
    cgButtonJsHandler: me,
    cgButtonActionHandler: Z,
    cgButtonLinkHandler: Y,
    initButtonList: N,
    initButtonSwitch: A,
    getDeleteButton: X,
    handleSubmitFlow: C,
    getSubmitFlowButton: G,
    registerDetailModal: S,
    registerBpmModal: J,
    openDetailModal: O
  };
}
export {
  mt as a,
  dt as u
};
