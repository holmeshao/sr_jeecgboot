var ze = Object.defineProperty, Xe = Object.defineProperties;
var We = Object.getOwnPropertyDescriptors;
var re = Object.getOwnPropertySymbols;
var Ze = Object.prototype.hasOwnProperty, ea = Object.prototype.propertyIsEnumerable;
var ue = (a, l, c) => l in a ? ze(a, l, { enumerable: !0, configurable: !0, writable: !0, value: c }) : a[l] = c, O = (a, l) => {
  for (var c in l || (l = {}))
    Ze.call(l, c) && ue(a, c, l[c]);
  if (re)
    for (var c of re(l))
      ea.call(l, c) && ue(a, c, l[c]);
  return a;
}, de = (a, l) => Xe(a, We(l));
var D = (a, l, c) => new Promise((E, h) => {
  var g = (T) => {
    try {
      F(c.next(T));
    } catch (p) {
      h(p);
    }
  }, w = (T) => {
    try {
      F(c.throw(T));
    } catch (p) {
      h(p);
    }
  }, F = (T) => T.done ? E(T.value) : Promise.resolve(T.value).then(g, w);
  F((c = c.apply(a, l)).next());
});
import { defineComponent as aa, ref as y, computed as ce, provide as be, reactive as ta, nextTick as Z, toRaw as la, resolveComponent as m, openBlock as _, createBlock as ee, mergeProps as na, withCtx as b, createElementVNode as P, createVNode as u, createTextVNode as B, createElementBlock as ae, Fragment as oa, renderList as ia, toDisplayString as sa, createCommentVNode as te } from "vue";
import { Icon as ra } from "/@/components/Icon";
import { BasicModal as ua, useModalInner as da, useModal as ca } from "/@/components/Modal";
import { BasicForm as ba, useForm as fa } from "/@/components/Form/index";
import { useMessage as ma } from "/@/hooks/web/useMessage";
import { u as pa } from "./useSchemas-b074f3a1.mjs";
import ga from "./DBAttributeTable-1a45c7b7.mjs";
import Ta from "./PageAttributeTable-66e7b485.mjs";
import ya from "./CheckDictTable-8a938e3a.mjs";
import va from "./ForeignKeyTable-92decaea.mjs";
import ha from "./IndexTable-2ded2014.mjs";
import Fa from "./QueryTable-65d3f54f.mjs";
import Ca from "./ExtendConfigModal-7d70f362.mjs";
import { u as Da, E as wa, a as Ba, V as M } from "./cgform.data-0ca62d09.mjs";
import { defHttp as v } from "/@/utils/http/axios";
import { simpleDebounce as ka } from "/@/utils/common/compUtils";
import { u as Ia } from "./useOnlineTest-e4bd8be3.mjs";
import { buildUUID as Ra } from "/@/utils/uuid";
import { sleep as V } from "/@/utils";
import { g as Sa } from "./useExtendComponent-bb98e568.mjs";
import { p as _a } from "./utils-9fce7606.mjs";
import { _ as Ea } from "./index-9e1e1e53.mjs";
const dt = (a) => v.get({ url: "/online/cgform/head/list", params: a }), ct = (a) => fe(a, 0), bt = (a) => v.delete(
  { url: "/online/cgform/head/removeRecord", params: { id: a } },
  { joinParamsToUrl: !0 }
), ft = (a) => fe(a, 1), mt = (a) => v.delete(
  { url: "/online/cgform/head/delete", params: { id: a } },
  { joinParamsToUrl: !0 }
);
function fe(a, l) {
  return v.delete(
    {
      url: "/online/cgform/head/deleteBatch",
      params: {
        ids: a.join(","),
        flag: l
      }
    },
    { joinParamsToUrl: !0 }
  );
}
const pt = (a, l) => v.post({ url: `/online/cgform/api/doDbSynch/${a}/${l}`, timeout: 12e3, timeoutErrorMessage: "同步数据库超时，已自动刷新" }), gt = (a) => v.post({ url: `/online/cgform/head/copyOnline?code=${a}` }), Tt = (a, l, c) => v.get({ url: `/online/cgform/head/copyOnlineTable/${a}`, params: O({ tableName: l }, c) }), q = {
  // 查询表字段 e3e3NcxzbUiGa53YYVXxWc8ADo5ISgQGx/gaZwERF91oAryDlivjqBv3wqRArgChupi+Y/Gg/swwGEyL0PuVFg==
  doQueryField: (a, l) => v.get({ url: "/online/cgform/field/listByHeadId", params: O({ headId: a }, l) }),
  // 查询表index配置
  doQueryIndexes: (a, l) => v.get({ url: "/online/cgform/index/listByHeadId", params: O({ headId: a }, l) }),
  // 新增或修改
  doSaveOrUpdate: (a, l) => l ? v.put({ url: "/online/cgform/api/editAll", params: a }) : v.post({ url: "/online/cgform/api/addAll", params: a }),
  //只是修改表配置不改字段
  editHead: (a) => v.put({ url: "/online/cgform/head/edit", params: a })
}, Aa = aa({
  name: "CgformModal",
  components: {
    BasicModal: ua,
    BasicForm: ba,
    DBAttributeTable: ga,
    PageAttributeTable: Ta,
    CheckDictTable: ya,
    ForeignKeyTable: va,
    IndexTable: ha,
    QueryTable: Fa,
    ExtendConfigModal: Ca,
    Icon: ra
  },
  emits: ["success", "register"],
  props: {
    actionButton: {
      type: Boolean,
      default: !0,
      required: !1
    }
  },
  setup(a, { emit: l }) {
    const { createMessage: c } = ma(), E = y(), h = y(!1);
    let g = {};
    const w = ce(() => h.value ? "编辑" : "新增"), F = y(!0), T = y(!1), p = y("dbTable"), A = y(!0), d = {
      dbTable: y(),
      pageTable: y(),
      checkTable: y(),
      fkTable: y(),
      idxTable: y(),
      queryTable: y()
    }, K = ce(() => {
      var e, t;
      return (t = (e = E.value) == null ? void 0 : e.fullScreenRef) != null ? t : !1;
    });
    be("tables", d), be("fullScreenRef", K);
    const N = { value: {} }, k = y(""), { formSchemas: J } = pa(a, N, {
      onTableTypeChange: ke,
      onIsTreeChange: Ie,
      ifShowOfSubTableStr: () => ne
    }), [H, R] = fa({
      schemas: J,
      showActionButtonGroup: !1,
      labelAlign: "right"
    }), { resetFields: j, setFieldsValue: L, validate: G } = R, [Y, { closeModal: z }] = da((e) => {
      var t;
      h.value = (t = e == null ? void 0 : e.isUpdate) != null ? t : !1, h.value ? oe(e == null ? void 0 : e.record) : he();
    }), $ = y("");
    let s = ta({});
    const X = ka(() => Re(), 150);
    let le = [], ne = !1, x = !1, I = [];
    // workflow config states
    const wfProcessKey = y("");
    const wfUiMode = y("SPLIT");
    const wfUiSchema = y("");
    const wfLoading = y(!1);
    // 高级配置（整体JSON，存表头 extConfigJson.workflow.configJson）
    const wfAdvancedJson = y("");
    // 字段批量权限
    const wfFieldRows = y([]);
    function wfLoadFields() {
      try {
        const pt = d.pageTable.value;
        if (!pt || !pt.tableRef) {
          c.warning("请先切到‘页面属性’加载字段");
          return;
        }
        const rows = pt.tableRef.getTableData();
        wfFieldRows.value = (rows || []).filter((r) => r && r.id && r.dbFieldName !== "id");
        c.success("已加载字段");
      } catch (e) {
        c.warning("无法获取字段列表");
      }
    }
    function wfOpenField(row) {
      try {
        const pt = d.pageTable.value;
        if (!pt || !pt.openConfig) {
          c.warning("页面属性未就绪");
          return;
        }
        pt.openConfig({ row, rowId: row.id });
      } catch (e) {
        c.warning("打开字段配置失败");
      }
    }
    // 默认兜底配置（写入表头 extConfigJson.workflow）
    const wfDefaultUiMode = y("SPLIT");
    const wfDefaultUiSchema = y("");
    const wfTemplateNodeId = y("");
    function wfApplyTemplateStartEditable() {
      if (!wfFieldRows.value || wfFieldRows.value.length === 0) {
        c.warning("请先点击‘加载字段’");
        return;
      }
      if (!wfTemplateNodeId.value) {
        c.warning("请填写模板节点ID");
        return;
      }
      try {
        const pt = d.pageTable.value;
        const tpl = { workflow: { default: { editable: [wfTemplateNodeId.value] } } };
        for (const row of wfFieldRows.value) {
          pt.handleExtJson(tpl, row.id);
        }
        c.success("已应用模板到全部字段");
      } catch (e) {
        c.warning("应用模板失败");
      }
    }
    function wfClearAllFieldExtJson() {
      if (!wfFieldRows.value || wfFieldRows.value.length === 0) {
        c.warning("请先点击‘加载字段’");
        return;
      }
      try {
        const pt = d.pageTable.value;
        for (const row of wfFieldRows.value) {
          pt.handleExtJson({}, row.id);
        }
        c.success("已清空全部字段的权限JSON");
      } catch (e) {
        c.warning("清空失败");
      }
    }
    function wfDefaultLoad() {
      if (!g.id) {
        c.warning("请先保存表头");
        return;
      }
      try {
        s.workflow || (s.workflow = {});
        wfDefaultUiMode.value = s.workflow.defaultUiMode || "SPLIT";
        const defSchema = s.workflow.defaultUiSchema;
        wfDefaultUiSchema.value = defSchema ? (typeof defSchema === "string" ? defSchema : JSON.stringify(defSchema, null, 2)) : "";
        c.success("已读取默认配置");
      } catch (e) {
      }
    }
    function wfDefaultSave() {
      return D(this, null, function* () {
        if (!g.id) {
          c.warning("请先保存表头");
          return;
        }
        let parsed = null;
        if (wfDefaultUiSchema.value) {
          try {
            parsed = JSON.parse(wfDefaultUiSchema.value);
          } catch (e) {
            c.warning("默认UI Schema JSON格式不正确");
            return;
          }
        }
        s.workflow || (s.workflow = {});
        s.workflow.defaultUiMode = wfDefaultUiMode.value;
        s.workflow.defaultUiSchema = parsed || "";
        const raw = la(s);
        yield q.editHead({ id: g.id, extConfigJson: JSON.stringify(raw) });
        c.success("默认配置已保存");
      });
    }
    // 高级配置：读/写/校验/格式化（大JSON，含 nodes/variables/buttons/uiSchema 等）
    // 将对象的第一层 key 做映射转换
    function wfMapFirstLevelKeys(obj, mapFn) {
      if (!obj || typeof obj !== "object") return obj;
      const out = {};
      Object.keys(obj).forEach((k) => {
        const nk = mapFn(k) || k;
        out[nk] = obj[k];
      });
      return out;
    }
    // 获取流程节点的 名称<->ID 映射
    function wfFetchNodeMaps() {
      return D(this, null, function* () {
        const tn = $.value || (g == null ? void 0 : g.tableName) || "";
        if (!tn || !wfProcessKey.value) return { name2id: {}, id2name: {} };
        try {
          const list = yield v.get({ url: "/workflow/onlineForm/node/formNodes", params: { tableName: tn, processKey: wfProcessKey.value } });
          const arr = Array.isArray(list) ? list : (list && (list.records || list.data) ? (list.records || list.data) : []);
          const name2id = {}, id2name = {};
          (arr || []).forEach((n) => {
            const id = n.nodeId || n.node_id || n.id;
            const name = n.nodeName || n.node_name || n.name;
            if (id && name) {
              name2id[name] = id;
              id2name[id] = name;
            }
          });
          return { name2id, id2name };
        } catch (e) {
          return { name2id: {}, id2name: {} };
        }
      });
    }
    function wfAdvLoad() {
      if (!g.id) {
        c.warning("请先保存表头");
        return;
      }
      try {
        s.workflow || (s.workflow = {});
        const adv = s.workflow.configJson;
        // 如果能拿到节点映射，则把ID转成名称，方便编辑
        const applyPretty = (cfg, maps) => {
          try {
            if (!cfg || !cfg.workflow) return cfg;
            const id2name = (maps && maps.id2name) || {};
            const toName = (k) => id2name[k] || k;
            const w = Object.assign({}, cfg.workflow);
            if (w.nodes && typeof w.nodes === "object") w.nodes = wfMapFirstLevelKeys(w.nodes, toName);
            if (w.variables && typeof w.variables === "object") w.variables = wfMapFirstLevelKeys(w.variables, toName);
            if (w.uiSchema && typeof w.uiSchema === "object") w.uiSchema = wfMapFirstLevelKeys(w.uiSchema, toName);
            return Object.assign({}, cfg, { workflow: w });
          } catch (_) { return cfg; }
        };
        if (adv) {
          const parsed = typeof adv === "string" ? JSON.parse(adv) : adv;
          wfFetchNodeMaps().then((maps) => {
            const pretty = applyPretty(parsed, maps);
            wfAdvancedJson.value = JSON.stringify(pretty, null, 2);
            c.success("已读取高级配置");
          });
        } else {
          wfAdvancedJson.value = "";
          c.success("已读取高级配置");
        }
      } catch (e) {}
    }
    function wfAdvSave() {
      return D(this, null, function* () {
        if (!g.id) {
          c.warning("请先保存表头");
          return;
        }
        let parsed = null;
        if (wfAdvancedJson.value) {
          try {
            parsed = JSON.parse(wfAdvancedJson.value);
          } catch (e) {
            c.warning("高级配置 JSON 格式不正确");
            return;
          }
        }
        // 将名称转回节点ID再保存
        const resolveById = (cfg, maps) => {
          if (!cfg || !cfg.workflow) return cfg;
          const name2id = (maps && maps.name2id) || {};
          const toId = (k) => name2id[k] || k;
          const w = Object.assign({}, cfg.workflow);
          if (w.nodes && typeof w.nodes === "object") w.nodes = wfMapFirstLevelKeys(w.nodes, toId);
          if (w.variables && typeof w.variables === "object") w.variables = wfMapFirstLevelKeys(w.variables, toId);
          if (w.uiSchema && typeof w.uiSchema === "object") w.uiSchema = wfMapFirstLevelKeys(w.uiSchema, toId);
          return Object.assign({}, cfg, { workflow: w });
        };
        let finalCfg = parsed || "";
        if (parsed) {
          try {
            const maps = yield wfFetchNodeMaps();
            finalCfg = resolveById(parsed, maps);
          } catch (e) {
            finalCfg = parsed;
          }
        }
        s.workflow || (s.workflow = {});
        s.workflow.configJson = finalCfg || "";
        const raw = la(s);
        // 1) 先把高级配置写入表头 extConfigJson
        yield q.editHead({ id: g.id, extConfigJson: JSON.stringify(raw) });
        // 2) 同步关键项到工作流配置表（启用、启动模式、uiMode、processKey），避免只配JSON不生效
        try {
          const wf = (finalCfg && finalCfg.workflow) ? finalCfg.workflow : {};
          const pdKey = wf.processDefinitionKey || wf.process_key || wf.key || wf.process;
          const uiModeSync = wf.uiMode || wf.ui_mode || "SPLIT";
          const startModeSync = wf.startMode || wf.workflowStartMode || wf.start_mode || "MANUAL";
          const enabledSync = (wf.enabled === false || wf.workflowEnabled === 0) ? 0 : 1;
          if (pdKey) {
            const list = yield v.get({ url: "/workflow/onlineForm/config/list", params: { cgformHeadId: g.id, processDefinitionKey: pdKey, pageNo: 1, pageSize: 1 } });
            const recs = (list && (list.records || list)) ? (list.records || list) : [];
            let cfgId = (Array.isArray(recs) && recs.length > 0) ? recs[0].id : null;
            const payload = { id: cfgId, cgformHeadId: g.id, processDefinitionKey: pdKey, uiMode: uiModeSync, workflowStartMode: startModeSync, workflowEnabled: enabledSync };
            if (cfgId) yield v.put({ url: "/workflow/onlineForm/config/edit", data: payload });
            else yield v.post({ url: "/workflow/onlineForm/config/add", data: payload });
          }
        } catch (e) {}
        c.success("高级配置已保存");
      });
    }
    function wfAdvValidateJson() {
      try {
        wfAdvancedJson.value && JSON.parse(wfAdvancedJson.value);
        c.success("JSON 校验通过");
      } catch (e) {
        c.warning("JSON 格式不正确");
      }
    }
    function wfAdvFormatJson() {
      try {
        const obj = wfAdvancedJson.value ? JSON.parse(wfAdvancedJson.value) : {};
        wfAdvancedJson.value = JSON.stringify(obj, null, 2);
        c.success("已格式化");
      } catch (e) {
        c.warning("JSON 格式不正确");
      }
    }
    // 节点 formKey 绑定
    const wfNodeLoading = y(!1);
    const wfNodes = y([]);
    function wfLoadNodes() {
      return D(this, null, function* () {
        if (!g.id) {
          c.warning("请先保存表头");
          return;
        }
        if (!wfProcessKey.value) {
          c.warning("请先填写流程定义Key");
          return;
        }
        wfNodeLoading.value = !0;
        try {
          const tn = $.value || (g == null ? void 0 : g.tableName) || "";
          const list = yield v.get({ url: "/workflow/onlineForm/node/formNodes", params: { tableName: tn, processKey: wfProcessKey.value } });
          wfNodes.value = Array.isArray(list) ? list : (list && (list.records || list.data) ? (list.records || list.data) : []);
          c.success("已加载节点");
        } finally {
          wfNodeLoading.value = !1;
        }
      });
    }
    function wfSaveNode(e) {
      return D(this, null, function* () {
        if (!e)
          return;
        if (!e.nodeId || !e.formKey) {
          c.warning("请填写节点ID与表单Key");
          return;
        }
        const payload = { id: e.id, formId: g.id, processDefinitionKey: wfProcessKey.value, nodeId: e.nodeId, nodeName: e.nodeName, formKey: e.formKey };
        if (e.id) {
          yield v.put({ url: "/workflow/onlineForm/node/edit", params: payload });
        } else {
          const resp = yield v.post({ url: "/workflow/onlineForm/node/add", params: payload });
          if (resp && resp.id)
            e.id = resp.id;
        }
        c.success("已保存");
      });
    }
    function wfAddNode() {
      wfNodes.value = [].concat(wfNodes.value || [], [{ id: null, nodeId: "", nodeName: "", formKey: "" }]);
    }
    function wfDeleteNode(e) {
      return D(this, null, function* () {
        if (!e)
          return;
        if (e.id) {
          yield v.delete({ url: "/workflow/onlineForm/node/delete", params: { id: e.id } }, { joinParamsToUrl: !0 });
        }
        wfNodes.value = (wfNodes.value || []).filter((n) => n !== e);
        c.success("已删除");
      });
    }
    function wfFillNodeFormKey() {
      const tn = $.value || (g == null ? void 0 : g.tableName) || "";
      if (!tn) {
        c.warning("当前表名为空，无法填充");
        return;
      }
      const list = wfNodes.value || [];
      list.forEach((n) => n.formKey = tn);
      wfNodes.value = list;
      c.success("已将表单Key填充为当前表名");
     }
    const { aiTestMode: me, aiTestTable: pe, aiTableList: ge, initVirtualData: Te, tableJsonGetHelper: ye, refreshCacheTableName: ve } = Ia();
    function he() {
      oe({});
    }
    function oe(e) {
      return D(this, null, function* () {
        var t;
        if (F.value = !1, p.value = "dbTable", yield j(), g = Object.assign({}, e), we(g), k.value = "", ye(g), De(g), L(g), $.value = g.tableName, V(1, () => A.value = !1), h.value)
          (t = d.dbTable.value) == null || t.setDataSource([]), yield Fe(g.id), yield Ce(g.id), Sa(d.pageTable).then(() => {
            d.pageTable.value.changePageType(g.tableType == 3);
          });
        else {
          let { initialData: n, tempIds: o } = Da();
          yield ie(n, !0), le = o;
        }
      });
    }
    function Fe(e) {
      return D(this, null, function* () {
        T.value = !0;
        try {
          let t = yield q.doQueryField(e);
          T.value = !1, yield ie(t);
        } finally {
          T.value = !1;
        }
      });
    }
    function Ce(e) {
      return D(this, null, function* () {
        let t = yield q.doQueryIndexes(e);
        d.idxTable.value.setDataSource(t);
      });
    }
    function De(e) {
      let t = _a(e);
      s = Object.assign({}, wa, t, {
        isDesForm: e.isDesForm || "N",
        desFormCode: e.desFormCode || ""
      }), N.value = s;
    }
    function we(e) {
      x = e.isTree == "Y", ne = e.tableType === 2;
    }
    function ie(e, t) {
      return D(this, null, function* () {
        const { dbTable: n, pageTable: o, checkTable: r, fkTable: i, queryTable: f } = d;
        n.value || (yield Z(), yield V(1)), n.value.setDataSource(e, t), setTimeout(() => {
          o.value.setDataSource(e, t), r.value.setDataSource(e, t), i.value.setDataSource(e, t), f.value.setDataSource(e, t);
        }, 10);
      });
    }
    function Be(e) {
      if (["pageTable", "checkTable", "fkTable", "idxTable", "queryTable"].indexOf(e) !== -1) {
        const t = d.dbTable, n = d[e];
        t.value.tableRef.resetScrollTop(), n.value.syncTable(t);
      }
    }
    function ke(e) {
      e === 1 && L({ themeTemplate: "normal" }), d.pageTable.value.changePageType(e == 3);
    }
    function Ie(e) {
      e === "Y" ? Le() : $e();
    }
    function W() {
      X();
    }
    function Re() {
      return D(this, null, function* () {
        let { dbTable: e, pageTable: t, checkTable: n, fkTable: o, queryTable: r } = d;
        yield t.value.syncTable(e), yield n.value.syncTable(e), yield o.value.syncTable(e), yield r.value.syncTable(e);
      });
    }
    function Se() {
      W();
    }
    function _e() {
      W();
    }
    function Ee(e) {
      let { oldIndex: t, newIndex: n } = e;
      Ne(t, n);
    }
    function Ae(e) {
      return D(this, null, function* () {
        let { insertIndex: t, row: n } = e, { pageTable: o, checkTable: r, fkTable: i, queryTable: f } = d;
        o.value.tableRef.insertRows(n, t), r.value.tableRef.insertRows(n, t), i.value.tableRef.insertRows(n, t), f.value.tableRef.insertRows(n, t);
      });
    }
    function Ne(e, t) {
      let { pageTable: n, checkTable: o, fkTable: r, queryTable: i } = d;
      n.value.tableRef.rowResort(e, t), o.value.tableRef.rowResort(e, t), r.value.tableRef.rowResort(e, t), i.value.tableRef.rowResort(e, t);
    }
    function Oe(e) {
      d.pageTable.value.syncFieldShowType(e.row);
    }
    function Pe(e) {
      d.pageTable.value.syncIsQuery(e.row);
    }
    function Me(e) {
      d.checkTable.value.syncFieldMustInput(e.row);
    }
    function je(e) {
      d.pageTable.value.enableQuery(e);
    }
    function Le() {
      if (!x) {
        let { dbTable: e, pageTable: t, checkTable: n } = d, o = Ba();
        o = o.filter((r) => !e.value.tableRef.getTableData().map((f) => f.dbFieldName).includes(r.dbFieldName)), I = [], o.forEach((r) => {
          let i = Ra() + "__tempId";
          I.push(i), r.id = i;
        }), e.value.tableRef.addRows(o, { setActive: !1 }), t.value.tableRef.addRows(o, { setActive: !1 }), n.value.tableRef.addRows(o, { setActive: !1 }), Z(() => W()), x = !0;
      }
      Z(() => {
        R.setFieldsValue({
          treeIdField: "has_child",
          treeParentIdField: "pid"
        });
      });
    }
    function $e() {
      if (I && I.length > 0) {
        let { dbTable: e } = d;
        e.value.tableDeleteLines(I), I = [], x = !1;
      }
    }
    function xe() {
      let e = {};
      return new Promise((t, n) => {
        G().then(
          (o) => t({ values: o }),
          () => n(M)
        );
      }).then((t) => (Object.assign(e, t), Qe())).then((t) => {
        Object.assign(e, t);
        let n = Ue(e);
        return Ve(n);
      }).catch((t) => (t === M || (t == null ? void 0 : t.code) === M ? c.warning("校验未通过") : t != null && t.msg && c.warning(t.msg), Promise.reject(null)));
    }
    function Qe() {
      return new Promise((e, t) => D(this, null, function* () {
        let n = Object.keys(d), o = {};
        for (let r = 0; r < n.length; r++) {
          let i = n[r], f = d[i];
          try {
            o[i] = yield f.value.validateData(i);
          } catch (C) {
            C.code === M && (p.value = C.activeKey), t(C);
            return;
          }
        }
        e(o);
      }));
    }
    function Ue(e) {
      let t = {
        head: {},
        fields: [],
        indexs: [],
        deleteFieldIds: [],
        deleteIndexIds: []
      };
      return t.head = Object.assign(g, e.values), t.head.isDesForm = s.isDesForm, t.head.desFormCode = s.desFormCode, delete s.isDesForm, delete s.desFormCode, t.head.extConfigJson = JSON.stringify(s), e.dbTable.tableData.forEach((n, o) => {
        let r = n.id, i = Object.assign({}, n), f = e.pageTable.tableData[o];
        i = Object.assign(f, i);
        let C = e.checkTable.tableData[o];
        i = Object.assign(C, i);
        let Q = e.fkTable.tableData[o];
        i = Object.assign(Q, i);
        let U = e.queryTable.tableData[o];
        i = Object.assign(U, i), r == null || r === "" ? delete i.id : i.id = r, [].concat(le, I).includes(i.id) && delete i.id, t.fields.push(i);
      }), t.deleteFieldIds = e.dbTable.deleteIds, t.indexs = e.idxTable.tableData, t.deleteIndexIds = e.idxTable.deleteIds, t;
    }
    function Ve(e) {
      // 取消“外键只允许配置一个”的前端限制：直接通过校验
      return new Promise((t, n) => {
        t(e);
      });
    }
    function qe() {
      F.value = !0, xe().then(
        (e) => D(this, null, function* () {
          var t;
          if (e.fields && e.fields.length > 0)
            for (let n of e.fields)
              n.dbFieldName = n.dbFieldName.toLowerCase().trim();
          (t = e.head) != null && t.tableName && (e.head.tableName = e.head.tableName.toLowerCase().trim()), yield q.doSaveOrUpdate(e, h.value), ve($.value, e.head.tableName), l("success"), V(1, () => se());
        }),
        (e) => {
        }
      ).finally(() => {
        F.value = !1;
      });
    }
    const [Ke, Je] = ca();
    function He(e) {
      return D(this, null, function* () {
        if (N.value = e, e.joinQuery == 0 && R.validateFields(["themeTemplate"]), s = e, h.value == !0) {
          let t = la(s);
          const n = {
            id: g.id,
            extConfigJson: JSON.stringify(t)
          };
          yield q.editHead(n), l("success");
        }
      });
    }
    function Ge() {
      Je.openModal(!0, {
        extConfigJson: s
      });
    }
    function se() {
      A.value = !0, V(1, () => z());
    }
    // ----- workflow config helpers -----
    function wfValidateJson() {
      try {
        wfUiSchema.value && JSON.parse(wfUiSchema.value);
        c.success("JSON 校验通过");
      } catch (e) {
        c.warning("JSON 格式不正确");
      }
    }
    function wfFormatJson() {
      try {
        const obj = wfUiSchema.value ? JSON.parse(wfUiSchema.value) : {};
        wfUiSchema.value = JSON.stringify(obj, null, 2);
        c.success("已格式化");
      } catch (e) {
        c.warning("JSON 格式不正确");
      }
    }
    function wfCanOperate() {
      if (!g.id) {
        c.warning("请先保存表头");
        return !1;
      }
      if (!wfProcessKey.value) {
        c.warning("请先填写流程定义Key");
        return !1;
      }
      return !0;
    }
    function wfLoad() {
      return D(this, null, function* () {
        if (!wfCanOperate())
          return;
        wfLoading.value = !0;
        try {
          const m1 = yield v.get({ url: "/workflow/config/uiMode", params: { cgformHeadId: g.id, processDefinitionKey: wfProcessKey.value } });
          m1 && (wfUiMode.value = m1.uiMode || m1.ui_mode || wfUiMode.value);
          const m2 = yield v.get({ url: "/workflow/config/uiSchema", params: { cgformHeadId: g.id, processDefinitionKey: wfProcessKey.value } });
          wfUiSchema.value = (m2 && (m2.uiSchemaJson || m2.ui_schema_json)) || "";
          c.success("已读取工作流配置");
        } finally {
          wfLoading.value = !1;
        }
      });
    }
    function wfSave() {
      return D(this, null, function* () {
        if (!wfCanOperate())
          return;
        try {
          wfUiSchema.value && JSON.parse(wfUiSchema.value);
        } catch (e) {
          c.warning("JSON 格式不正确");
          return;
        }
        wfLoading.value = !0;
        try {
          // 先保存/更新 uiMode 到 onlineForm config，再写 uiSchema
          try {
            const list = yield v.get({ url: "/workflow/onlineForm/config/list", params: { cgformHeadId: g.id, processDefinitionKey: wfProcessKey.value, pageNo: 1, pageSize: 1 } });
            const recs = (list && (list.records || list)) ? (list.records || list) : [];
            let cfgId = (Array.isArray(recs) && recs.length > 0) ? recs[0].id : null;
            const payload = { id: cfgId, cgformHeadId: g.id, processDefinitionKey: wfProcessKey.value, uiMode: wfUiMode.value };
            if (cfgId) yield v.put({ url: "/workflow/onlineForm/config/edit", data: payload });
            else yield v.post({ url: "/workflow/onlineForm/config/add", data: payload });
          } catch (e) {}
          yield v.post({ url: "/workflow/config/uiSchema", data: { cgformHeadId: g.id, processDefinitionKey: wfProcessKey.value, uiSchemaJson: wfUiSchema.value } });
          c.success("保存成功");
        } finally {
          wfLoading.value = !1;
        }
      });
    }
    const Ye = () => {
      const e = k.value.trim();
      if (e.length) {
        const n = d[p.value].value.tableRef.getXTable(), o = n.getTableData().fullData, r = o.findIndex((f) => e === f.dbFieldName || e === f.dbFieldTxt);
        let i = -1;
        if (r == -1 ? i = o.findIndex((C) => C.dbFieldName.includes(k.value) || C.dbFieldTxt.includes(k.value)) : i = r, i != -1) {
          const f = o[i];
          n.scrollToRow(f).then(() => {
            const { refTableBody: C } = n.getRefMaps(), Q = C.value, U = Q ? Q.$el : null;
            if (U) {
              const S = U.querySelector(`[rowid="${n.getRowid(f)}"]`);
              S && (S.classList.add("customHighlight"), setTimeout(() => {
                S == null || S.classList.remove("customHighlight");
              }, 1e3));
            }
          });
        } else
          c.warning("没搜到相关字段名称或字段备注~");
      } else
        c.warning("请输入字段名称或字段备注~");
    };
    return de(O({}, d), {
      modalRef: E,
      title: w,
      confirmLoading: F,
      tableLoading: T,
      activeKey: p,
      onCancel: se,
      extConfigJson: s,
      formAction: R,
      hideTabs: A,
      onSubmit: qe,
      onTabsChange: Be,
      onTableAdded: Se,
      onTableRemoved: _e,
      onTableDragged: Ee,
      onTableInserted: Ae,
      onTableSyncDbType: Oe,
      onTableQuery: je,
      onOpenExtConfig: Ge,
      onExtConfigOk: He,
      // workflow
      wfProcessKey: wfProcessKey,
      wfUiMode: wfUiMode,
      wfUiSchema: wfUiSchema,
      wfLoading: wfLoading,
        wfAdvancedJson: wfAdvancedJson,
      onWfLoad: wfLoad,
      onWfSave: wfSave,
      onWfValidateJson: wfValidateJson,
      onWfFormatJson: wfFormatJson,
        onWfAdvLoad: wfAdvLoad,
        onWfAdvSave: wfAdvSave,
        onWfAdvValidateJson: wfAdvValidateJson,
        onWfAdvFormatJson: wfAdvFormatJson,
      // fields batch
      wfFieldRows: wfFieldRows,
      onWfLoadFields: wfLoadFields,
      onWfOpenField: wfOpenField,
      // defaults
      wfDefaultUiMode: wfDefaultUiMode,
      wfDefaultUiSchema: wfDefaultUiSchema,
      onWfDefaultLoad: wfDefaultLoad,
      onWfDefaultSave: wfDefaultSave,
      // nodes binding
      wfNodeLoading: wfNodeLoading,
      wfNodes: wfNodes,
      onWfLoadNodes: wfLoadNodes,
      onWfSaveNode: wfSaveNode,
      onWfAddNode: wfAddNode,
      onWfDeleteNode: wfDeleteNode,
      onWfFillNodeFormKey: wfFillNodeFormKey,
      wfTemplateNodeId: wfTemplateNodeId,
      onWfApplyTemplateStartEditable: wfApplyTemplateStartEditable,
      onWfClearAllFieldExtJson: wfClearAllFieldExtJson,
      registerForm: H,
      registerModal: Y,
      registerExtendConfigModal: Ke,
      // hook OnlineTest
      aiTestMode: me,
      aiTestTable: pe,
      aiTableList: ge,
      initVirtualData: Te,
      onTableSyncDbIsPersist: Pe,
      onTableSyncDbIsNull: Me,
      isUpdate: h,
      positioning: k,
      handlePositioning: Ye
    });
  }
});
const Na = { style: { flex: "1", "text-align": "right" } }, Oa = { class: "footer-area" }, Pa = { class: "rightArea" }, Ma = { class: "leftArea" }, ja = { key: 0 }, La = {
  key: 1,
  class: "positioning-area"
};
function $a(a, l, c, E, h, g) {
  const w = m("a-button"), F = m("BasicForm"), T = m("DBAttributeTable"), p = m("a-tab-pane"), A = m("PageAttributeTable"), d = m("CheckDictTable"), K = m("ForeignKeyTable"), N = m("IndexTable"), k = m("Icon"), J = m("a-tooltip"), H = m("QueryTable"), R = m("a-tabs"), j = m("a-spin"), L = m("a-select-option"), G = m("a-select"), Y = m("a-input"), z = m("ExtendConfigModal"), $ = m("BasicModal"), Q = m("a-textarea"), te2 = m("a-card"), ne2 = m("a-space"), ee2 = m("a-row"), ae2 = m("a-col"), oe2 = m("a-divider");
  return _(), ee($, na({
    ref: "modalRef",
    title: a.title,
    width: 1200,
    maskClosable: !1,
    defaultFullscreen: !0,
    confirmLoading: a.confirmLoading
  }, a.$attrs, {
    onCancel: a.onCancel,
    onRegister: a.registerModal
  }), {
    footer: b(() => [
      P("div", Oa, [
        P("div", Pa, [
          u(w, { onClick: a.onCancel }, {
            default: b(() => l[6] || (l[6] = [
              B("关闭")
            ])),
            _: 1
          }, 8, ["onClick"]),
          u(w, {
            type: "primary",
            loading: a.confirmLoading,
            preIcon: "ant-design:save",
            onClick: a.onSubmit
          }, {
            default: b(() => l[7] || (l[7] = [
              B("保存")
            ])),
            _: 1
          }, 8, ["loading", "onClick"])
        ]),
        P("div", Ma, [
          a.aiTestMode && !a.isUpdate ? (_(), ae("div", ja, [
            u(G, {
              value: a.aiTestTable,
              "onUpdate:value": l[1] || (l[1] = (s) => a.aiTestTable = s),
              placeholder: "请选择测试的数据模型",
              getPopupContainer: (s) => s == null ? void 0 : s.parentElement,
              style: { width: "300px", margin: "0 10px 0 0", "text-align": "left" }
            }, {
              default: b(() => [
                (_(!0), ae(oa, null, ia(a.aiTableList, (s, X) => (_(), ee(L, {
                  key: X,
                  value: s.name
                }, {
                  default: b(() => [
                    B(sa(s.title + "（" + s.name + "）"), 1)
                  ]),
                  _: 2
                }, 1032, ["value"]))), 128))
              ]),
              _: 1
            }, 8, ["value", "getPopupContainer"]),
            u(w, {
              type: "primary",
              ghost: "",
              onClick: a.initVirtualData
            }, {
              default: b(() => l[8] || (l[8] = [
                B("生成数据>>")
              ])),
              _: 1
            }, 8, ["onClick"])
          ])) : te("", !0),
          a.isUpdate ? (_(), ae("div", La, [
            u(Y, {
              value: a.positioning,
              "onUpdate:value": l[2] || (l[2] = (s) => a.positioning = s),
              placeholder: "请输入字段名称或字段备注",
              allowClear: "",
              onPressEnter: a.handlePositioning
            }, null, 8, ["value", "onPressEnter"]),
            u(w, {
              type: "primary",
              ghost: "",
              onClick: a.handlePositioning
            }, {
              default: b(() => l[9] || (l[9] = [
                B("定位")
              ])),
              _: 1
            }, 8, ["onClick"])
          ])) : te("", !0)
        ])
      ])
    ]),
    default: b(() => [
      u(j, {
        wrapperClassName: "p-2",
        spinning: a.confirmLoading
      }, {
        default: b(() => [
          u(F, { onRegister: a.registerForm }, {
            extConfigButton: b(() => [
              P("div", Na, [
                u(w, {
                  preIcon: "ant-design:setting",
                  onClick: a.onOpenExtConfig
                }, {
                  default: b(() => l[3] || (l[3] = [
                    B("扩展配置")
                  ])),
                  _: 1
                }, 8, ["onClick"])
              ])
            ]),
            _: 1
          }, 8, ["onRegister"]),
          u(j, {
            spinning: a.tableLoading || a.hideTabs
          }, {
            default: b(() => [
              a.hideTabs ? te("", !0) : (_(), ee(R, {
                key: 0,
                activeKey: a.activeKey,
                "onUpdate:activeKey": l[0] || (l[0] = (s) => a.activeKey = s),
                animated: "",
                onChange: a.onTabsChange
              }, {
                default: b(() => [
                  u(p, {
                    tab: "数据库属性",
                    key: "dbTable",
                    forceRender: ""
                  }, {
                    default: b(() => [
                      u(T, {
                        ref: "dbTable",
                        actionButton: a.actionButton,
                        onAdded: a.onTableAdded,
                        onRemoved: a.onTableRemoved,
                        onDragged: a.onTableDragged,
                        onInserted: a.onTableInserted,
                        onSyncDbType: a.onTableSyncDbType,
                        onSyncDbIsPersist: a.onTableSyncDbIsPersist,
                        onSyncDbIsNull: a.onTableSyncDbIsNull
                      }, null, 8, ["actionButton", "onAdded", "onRemoved", "onDragged", "onInserted", "onSyncDbType", "onSyncDbIsPersist", "onSyncDbIsNull"])
                    ]),
                    _: 1
                  }),
                  u(p, {
                    tab: "页面属性",
                    key: "pageTable",
                    forceRender: ""
                  }, {
                    default: b(() => [
                      u(A, { ref: "pageTable" }, null, 512)
                    ]),
                    _: 1
                  }),
                  u(p, {
                    tab: "校验字段",
                    key: "checkTable",
                    forceRender: ""
                  }, {
                    default: b(() => [
                      u(d, { ref: "checkTable" }, null, 512)
                    ]),
                    _: 1
                  }),
                  u(p, {
                    tab: "外键",
                    key: "fkTable",
                    forceRender: ""
                  }, {
                    default: b(() => [
                      u(K, {
                        ref: "fkTable",
                        actionButton: a.actionButton
                      }, null, 8, ["actionButton"])
                    ]),
                    _: 1
                  }),
                  u(p, {
                    tab: "索引",
                    key: "idxTable",
                    forceRender: ""
                  }, {
                    default: b(() => [
                      u(N, {
                        ref: "idxTable",
                        actionButton: a.actionButton
                      }, null, 8, ["actionButton"])
                    ]),
                    _: 1
                  }),
                  u(p, {
                    key: "queryTable",
                    forceRender: ""
                  }, {
                    tab: b(() => [
                      P("span", null, [
                        l[5] || (l[5] = B(" 个性查询配置 ")),
                        u(J, null, {
                          title: b(() => l[4] || (l[4] = [
                            B("允许自定义，查询表单字段控件类型！")
                          ])),
                          default: b(() => [
                            u(k, { icon: "bx:help-circle" })
                          ]),
                          _: 1
                        })
                      ])
                    ]),
                    default: b(() => [
                      u(H, {
                        ref: "queryTable",
                        onQuery: a.onTableQuery
                      }, null, 8, ["onQuery"])
                    ]),
                    _: 1
                  }),
                  u(p, {
                    tab: "工作流配置",
                    key: "workflowConfig",
                    forceRender: ""
                  }, {
                    default: b(() => [
                      u(j, { spinning: a.wfLoading }, {
                        default: b(() => [
                          u(te2, { title: "基本设置", size: "small", bordered: !1 }, {
                            default: b(() => [
                              u(ee2, { gutter: 16 }, {
                                default: b(() => [
                                  u(ae2, { span: 8 }, {
                                    default: b(() => [
                                      u(Y, {
                                        value: a.wfProcessKey,
                                        "onUpdate:value": l[10] || (l[10] = (s) => a.wfProcessKey = s),
                                        placeholder: "流程定义Key(processDefinitionKey)"
                                      }, null, 8, ["value"])
                                    ]),
                                    _: 1
                                  }),
                                  u(ae2, { span: 8 }, {
                                    default: b(() => [
                                      u(G, {
                                        value: a.wfUiMode,
                                        "onUpdate:value": l[11] || (l[11] = (s) => a.wfUiMode = s),
                                        style: { width: "100%" }
                                      }, {
                                        default: b(() => [
                                          u(L, { value: "SPLIT" }, { default: b(() => [l[12] || (l[12] = [B("分离模式(SPLIT)")]) ]), _: 1 }),
                                          u(L, { value: "INTEGRATED" }, { default: b(() => [l[13] || (l[13] = [B("融合模式(INTEGRATED)")]) ]), _: 1 })
                                        ]),
                                        _: 1
                                      }, 8, ["value"])
                                    ]),
                                    _: 1
                                  }),
                                  u(ae2, { span: 8 }, {
                                    default: b(() => [
                                      u(ne2, null, {
                                        default: b(() => [
                                          u(w, { onClick: a.onWfLoad }, { default: b(() => l[14] || (l[14] = [B("读取配置")]) ), _: 1 }, 8, ["onClick"]),
                                          u(w, { type: "primary", onClick: a.onWfSave }, { default: b(() => l[15] || (l[15] = [B("保存配置")]) ), _: 1 }, 8, ["onClick"])
                                        ]),
                                        _: 1
                                      })
                                    ]),
                                    _: 1
                                  })
                                ]),
                                _: 1
                              }),
                              u(oe2),
                              u(te2, { title: "字段工作流权限（批量入口）", size: "small", bordered: !1 }, {
                                default: b(() => [
                                  u(ne2, null, { default: b(() => [
                                    u(w, { onClick: a.onWfLoadFields }, { default: b(() => l[19] || (l[19] = [B("加载字段")]) ), _: 1 }, 8, ["onClick"]),
                                    u(Y, { value: a.wfTemplateNodeId, "onUpdate:value": l[24] || (l[24] = (s) => a.wfTemplateNodeId = s), style: { width: "240px", marginLeft: "8px" }, placeholder: "模板节点ID(如 start/leader_review)" }, null, 8, ["value"]),
                                    u(w, { onClick: a.onWfApplyTemplateStartEditable }, { default: b(() => [B("一键：起始节点可编辑")]), _: 1 }),
                                    u(w, { danger: "", onClick: a.onWfClearAllFieldExtJson }, { default: b(() => [B("清空全部字段权限")]), _: 1 })
                                  ]), _: 1 }),
                                  P("div", null, [
                                    (_(!0), ae(oa, null, ia(a.wfFieldRows, (s) => (_(), ae("div", { key: s.id, class: "wf-field-item", style: { padding: "4px 0" } }, [
                                      P("span", null, sa(s.dbFieldTxt + " (" + s.dbFieldName + ")"), 1),
                                      u(w, { size: "small", style: { marginLeft: "8px" }, onClick: (r0) => a.onWfOpenField(s) }, { default: b(() => [B("编辑权限JSON")]), _: 2 }, 1032, ["onClick"])
                                    ]))), 128))
                                  ])
                                ]),
                                _: 1
                              }),
                              u(oe2),
                  u(te2, { title: "节点 UI Schema(JSON)", size: "small", bordered: !1 }, {
                                default: b(() => [
                                  u(Q, {
                                    value: a.wfUiSchema,
                                    "onUpdate:value": l[16] || (l[16] = (s) => a.wfUiSchema = s),
                                    autoSize: { minRows: 12 },
                                    placeholder: "粘贴/编辑节点扩展字段与附件的 Schema(JSON)"
                                  }, null, 8, ["value"]),
                                  u(ne2, { style: { marginTop: "8px" } }, {
                                    default: b(() => [
                                      u(w, { onClick: a.onWfValidateJson }, { default: b(() => l[17] || (l[17] = [B("校验JSON")]) ), _: 1 }, 8, ["onClick"]),
                                      u(w, { onClick: a.onWfFormatJson }, { default: b(() => l[18] || (l[18] = [B("格式化")]) ), _: 1 }, 8, ["onClick"])
                                    ]),
                                    _: 1
                                  })
                                ]),
                                _: 1
                              }),
                              u(oe2),
                  u(te2, { title: "高级配置 JSON（全量）", size: "small", bordered: !1 }, {
                    default: b(() => [
                      u(Q, {
                        value: a.wfAdvancedJson,
                        "onUpdate:value": (s) => a.wfAdvancedJson = s,
                        autoSize: { minRows: 12 },
                        placeholder: "一次性维护 nodes/variables/buttons/uiSchema 等（与文档一致）"
                      }, null, 8, ["value"]),
                      u(ne2, { style: { marginTop: "8px" } }, {
                        default: b(() => [
                          u(w, { onClick: a.onWfAdvLoad }, { default: b(() => [B("读取高级配置")]), _: 1 }),
                          u(w, { type: "primary", onClick: a.onWfAdvSave }, { default: b(() => [B("保存高级配置")]), _: 1 }),
                          u(w, { onClick: a.onWfAdvValidateJson }, { default: b(() => [B("校验JSON")]), _: 1 }),
                          u(w, { onClick: a.onWfAdvFormatJson }, { default: b(() => [B("格式化")]), _: 1 })
                        ]),
                        _: 1
                      })
                    ]),
                    _: 1
                  }),
                  u(oe2),
                              u(te2, { title: "节点表单Key绑定（权威：存配置表）", size: "small", bordered: !1 }, {
                                default: b(() => [
                                  u(ne2, null, { default: b(() => [
                                    u(w, { onClick: a.onWfLoadNodes }, { default: b(() => [B("加载节点")]), _: 1 }, 8, ["onClick"]),
                                    u(w, { onClick: a.onWfAddNode }, { default: b(() => [B("新增行")]), _: 1 })
                                  ]), _: 1 }),
                                  u(ne2, { style: { margin: "8px 0" } }, { default: b(() => [
                                    u(w, { onClick: a.onWfFillNodeFormKey }, { default: b(() => [B("一键使用当前表名填充表单Key")]), _: 1 })
                                  ]), _: 1 }),
                                  u(j, { spinning: a.wfNodeLoading }, {
                                    default: b(() => [
                                      P("div", null, [
                                        (_(!0), ae(oa, null, ia(a.wfNodes, (s, idx) => (_(), ae("div", { key: s.id || idx, style: { display: "flex", gap: "8px", padding: "4px 0" } }, [
                                          u(Y, { value: s.nodeId, "onUpdate:value": (r0) => s.nodeId = r0, placeholder: "节点ID(TaskDefinitionKey)", style: { width: "200px" } }, null, 8, ["value","onUpdate:value"]),
                                          u(Y, { value: s.nodeName, "onUpdate:value": (r1) => s.nodeName = r1, placeholder: "节点名称(可选)", style: { width: "180px" } }, null, 8, ["value","onUpdate:value"]),
                                          u(Y, { value: s.formKey, "onUpdate:value": (r2) => s.formKey = r2, placeholder: "表单Key", style: { width: "240px" } }, null, 8, ["value","onUpdate:value"]),
                                          u(w, { type: "primary", size: "small", onClick: () => a.onWfSaveNode(s) }, { default: b(() => [B("保存")]), _: 1 }),
                                          u(w, { danger: "", size: "small", onClick: () => a.onWfDeleteNode(s) }, { default: b(() => [B("删除")]), _: 1 })
                                        ]))), 128))
                                      ])
                                    ]),
                                    _: 1
                                  })
                                ]),
                                _: 1
                              }),
                              
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }, 8, ["spinning"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["activeKey", "onChange"]))
            ]),
            _: 1
          }, 8, ["spinning"])
        ]),
        _: 1
      }, 8, ["spinning"]),
      u(z, {
        onRegister: a.registerExtendConfigModal,
        parentForm: a.formAction,
        onOk: a.onExtConfigOk
      }, null, 8, ["onRegister", "parentForm", "onOk"])
    ]),
    _: 1
  }, 16, ["title", "confirmLoading", "onCancel", "onRegister"]);
}
const xa = /* @__PURE__ */ Ea(Aa, [["render", $a], ["__scopeId", "data-v-44d8bb3b"]]), yt = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: xa
}, Symbol.toStringTag, { value: "Module" }));
export {
  xa as C,
  pt as a,
  Tt as b,
  mt as c,
  gt as d,
  bt as e,
  ct as f,
  ft as g,
  yt as h,
  dt as l
};
