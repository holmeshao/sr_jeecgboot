var F = (e, f, p) => new Promise((i, S) => {
  var w = (r) => {
    try {
      h(p.next(r));
    } catch (C) {
      S(C);
    }
  }, m = (r) => {
    try {
      h(p.throw(r));
    } catch (C) {
      S(C);
    }
  }, h = (r) => r.done ? i(r.value) : Promise.resolve(r.value).then(w, m);
  h((p = p.apply(e, f)).next());
});
import { BasicTable as je, TableAction as Ue } from "/@/components/Table";
import { useMessage as _e } from "/@/hooks/web/useMessage";
import We from "./OnlineAutoModal-95f46901.mjs";
import Ge from "./OnlineDetailModal-5b412bb9.mjs";
import Xe from "./OnlineCustomModal-c8b1e780.mjs";
import { ref as P, watch as Ye, resolveComponent as u, openBlock as a, createElementBlock as c, createBlock as d, createCommentVNode as g, withDirectives as V, createVNode as B, vShow as z, normalizeClass as Ze, withCtx as s, createElementVNode as R, toDisplayString as b, Fragment as j, renderList as xe, createTextVNode as Q, mergeProps as en, toHandlers as nn, resolveDynamicComponent as on, normalizeProps as tn, guardReactiveProps as ln } from "vue";
import an from "/@/components/Form/src/jeecg/components/JImportModal.vue";
import { u as rn, a as sn } from "./useListButton-98908683.mjs";
import { u as un, a as dn, g as pn } from "./useExtendComponent-bb98e568.mjs";
import { defHttp as mn } from "/@/utils/http/axios";
import cn from "./OnlineQueryForm-9248341f.mjs";
import gn from "./SuperQuery-46032e66.mjs";
import { b as U } from "./constant-fa63bd66.mjs";
import { _ as fn } from "./index-9e1e1e53.mjs";
import "/@/components/Modal";
import "./OnlineForm-58282699.mjs";
import "/@/components/Form/index";
import "lodash-es";
import "/@/utils";
import "/@/components/Loading";
import "/@/components/jeecg/JVxeTable/types";
import "/@/utils/auth";
import "@ant-design/icons-vue";
import "/@/hooks/core/useContext";
import "/@/utils/mitt";
import "./useCustomHook-acb00837.mjs";
import "/@/utils/cache";
import "/@/utils/common/compUtils";
import "/@/store/modules/user";
import "/@/hooks/web/useAppInject";
import "/@/utils/is";
import "/@/store/modules/permission";
import "./OnlineForm.vue_vue_type_style_index_0_scoped_3f26e7bd_lang-4ed993c7.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/views/system/user/user.api";
import "/@/utils/desform/customExpression";
import "/@/utils/dict/JDictSelectUtil";
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
import "/@/components/jeecg/comment/CommentPanel.vue";
import "./OnlineFormDetail-fc087725.mjs";
import "./DetailForm-c592b8d8.mjs";
import "/@/utils/dict";
import "/@/utils/dict/index";
import "/@/components/Markdown";
import "./OnlineSubFormDetail-8be879b9.mjs";
import "/@/router";
import "/@/hooks/core/onMountedOrActivated";
import "/@/store/modules/multipleTab";
import "./cgformState-d9f8ec42.mjs";
import "pinia";
import "/@/store";
import "/@/hooks/system/useMethods";
import "/@/components/Form/src/jeecg/components/JRangeNumber.vue";
import "./SuperQueryValComponent.vue_vue_type_script_lang-8fe34917.mjs";
import "/@/utils/index";
import "/@/hooks/web/useDesign";
const hn = {
  name: "DefaultOnlineList",
  components: {
    BasicTable: je,
    TableAction: Ue,
    OnlineAutoModal: We,
    JImportModal: an,
    OnlineQueryForm: cn,
    OnlineSuperQuery: gn,
    OnlineCustomModal: Xe,
    OnlineDetailModal: Ge
  },
  setup() {
    const { createMessage: e } = _e(), f = P(), {
      ID: p,
      onlineTableContext: i,
      onlineQueryFormOuter: S,
      loading: w,
      reload: m,
      dataSource: h,
      pagination: r,
      handleSpecialConfig: C,
      getColumnList: v,
      handleChangeInTable: D,
      loadData: E,
      superQueryButtonRef: T,
      superQueryStatus: L,
      handleSuperQuery: A,
      registerCustomModal: O,
      getTreeDataByResult: n,
      expandedRowKeys: l,
      handleExpandedRowsChange: y,
      tableReloading: H,
      onlineExtConfigJson: $,
      handleFormConfig: _
    } = rn();
    if (!p.value)
      throw e.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    i.isTree(!0);
    let { initCgEnhanceJs: W } = un(i);
    const {
      buttonSwitch: G,
      cgLinkButtonList: X,
      cgBIBtnMap: Y,
      getQueryButtonCfg: Z,
      getResetButtonCfg: x,
      getFormConfirmButtonCfg: ee,
      cgTopButtonList: ne,
      importUrl: oe,
      registerModal: te,
      handleAdd: J,
      handleEdit: ie,
      handleBatchDelete: le,
      registerImportModal: ae,
      onImportExcel: re,
      onExportExcel: se,
      cgButtonJsHandler: ue,
      cgButtonActionHandler: de,
      cgButtonLinkHandler: pe,
      handleSubmitFlow: me,
      getDropDownActions: ce,
      getActions: ge,
      initButtonList: fe,
      initButtonSwitch: he,
      registerDetailModal: Ce
    } = sn(i, $), {
      columns: ye,
      actionColumn: Be,
      selectedKeys: be,
      rowSelection: we,
      enableScrollBar: Ie,
      tableScroll: Se,
      downloadRowFile: Te,
      getImgView: ke,
      getPcaText: Re,
      getFormatDate: Me,
      handleColumnResult: Fe,
      hrefComponent: ve,
      viewOnlineCellImage: De,
      handleClickFieldHref: Ee
    } = dn(i, $);
    Ye(
      p,
      () => {
        Le();
      },
      { immediate: !0 }
    );
    function Le() {
      return F(this, null, function* () {
        w.value = !0;
        let o = yield v(U);
        Ae(o), yield E(), w.value = !1, i.execButtonEnhance("setup");
      });
    }
    function Ae(o) {
      let t = W(o.enhanceJs);
      i.EnhanceJS = t, fe(o.cgButtonList), he(o.hideColumns), Fe(o), C(o), i.hasChildrenField = o.hasChildrenField, i.pidField = o.pidField;
    }
    function Oe(o, t) {
      i.queryParam = o, t === !0 ? m({ mode: "search" }) : N();
    }
    function He(o) {
      return F(this, null, function* () {
        yield pn(T), T.value.init(o);
      });
    }
    function qe(o, t) {
      let I = l.value;
      if (o) {
        if (Qe(t.id), t.children.length > 0 && t.children[0].isLoading === !0) {
          let k = i.hasChildrenField;
          const { sortField: Pe, sortType: Ve } = i;
          let q = Object.assign({}, { column: Pe, order: Ve });
          q[i.pidField] = t.id, q[k] = t[k];
          let ze = `${i.onlineUrl.getTreeData}${i.ID}`;
          mn.get({ url: ze, params: q }, { isTransformResponse: !1 }).then((M) => {
            M.success ? Number(M.result.total) > 0 ? t.children = n(M.result.records) : (t.children = "", t.hasChildrenField = "0") : e.warning(M.message);
          }).catch(() => {
            e.warning("加载子节点失败!");
          });
        }
      } else {
        let k = I.indexOf(t.id);
        k >= 0 && (l.value = I.splice(k, 1));
      }
    }
    function Qe(o) {
      let t = l.value;
      t && t.indexOf(o) < 0 && t.push(o), l.value = t;
    }
    function N() {
      return F(this, null, function* () {
        i.isTree() === !0 && (l.value = [], f.value.collapseAll()), m();
      });
    }
    function $e(o) {
      if (K.value === !0) {
        let t = o[i.pidField];
        if (t) {
          let I = l.value;
          I.indexOf(t) < 0 && I.push(t), l.value = I;
        }
      }
      m();
    }
    const Je = (o) => ({
      label: "添加下级",
      onClick: Ne.bind(null, o)
    }), K = P(!1);
    function Ne(o) {
      K.value = !0;
      let t = {
        [i.pidField]: o.id
      };
      J(t);
    }
    function Ke(o) {
      let t = ce(o, { themeTemplate: U });
      return t.unshift(Je(o)), t;
    }
    return {
      ID: p,
      // 查询区域
      onlineQueryFormOuter: S,
      queryWithCondition: Oe,
      onQueryFormLoaded: He,
      reload: m,
      //高级查询
      superQueryButtonRef: T,
      superQueryStatus: L,
      handleSuperQuery: A,
      // table区域
      loading: w,
      columns: ye,
      actionColumn: Be,
      dataSource: h,
      pagination: r,
      rowSelection: we,
      selectedKeys: be,
      tableScroll: Se,
      enableScrollBar: Ie,
      handleChangeInTable: D,
      //按钮
      buttonSwitch: G,
      handleAdd: J,
      handleEdit: ie,
      onImportExcel: re,
      onExportExcel: se,
      cgBIBtnMap: Y,
      getQueryButtonCfg: Z,
      getResetButtonCfg: x,
      getFormConfirmButtonCfg: ee,
      cgTopButtonList: ne,
      cgLinkButtonList: X,
      cgButtonJsHandler: ue,
      cgButtonActionHandler: de,
      cgButtonLinkHandler: pe,
      handleBatchDelete: le,
      // table-slot
      downloadRowFile: Te,
      getImgView: ke,
      getPcaText: Re,
      getFormatDate: Me,
      // 操作列
      getActions: ge,
      getTreeDropDownActions: Ke,
      // 弹窗
      registerModal: te,
      registerCustomModal: O,
      registerImportModal: ae,
      importUrl: oe,
      handleFormConfig: _,
      //其他
      tableReloading: H,
      handleSubmitFlow: me,
      hrefComponent: ve,
      viewOnlineCellImage: De,
      //树特定的配置
      onlineTreeTableRef: f,
      handlerFormSuccess: $e,
      searchReset: N,
      handleExpand: qe,
      expandedRowKeys: l,
      handleExpandedRowsChange: y,
      registerDetailModal: Ce,
      handleClickFieldHref: Ee
    };
  }
  // 1引入了loadsh   console.log(that.simpleDateFormat(new Date().getTime(),'yyyy-MM-dd'));
  // 2. value的问题
  // 3. 变量位置改变后需要 重写api
  // 1添加按钮的时候 预留出样式对象 然后js增强中设置样式对象
  // 2直接设置css字符串 然后通过js document 往head里面增加css片段 全局生效
  // TODO 清空高级查询
  // TODO 积木报表打印地址
  // const reportPrintUrl = ref('')
};
const Cn = { class: "p-2" }, yn = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, Bn = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, bn = ["src", "onClick"], wn = ["innerHTML", "onClick"], In = ["innerHTML"], Sn = ["title"];
function Tn(e, f, p, i, S, w) {
  const m = u("a-skeleton"), h = u("online-query-form"), r = u("a-button"), C = u("online-super-query"), v = u("TableAction"), D = u("BasicTable"), E = u("OnlineAutoModal"), T = u("JImportModal"), L = u("a-modal"), A = u("online-custom-modal"), O = u("online-detail-modal");
  return a(), c("div", Cn, [
    e.tableReloading ? (a(), d(m, {
      key: 0,
      active: ""
    })) : g("", !0),
    V(B(h, {
      ref: "onlineQueryFormOuter",
      id: e.ID,
      queryBtnCfg: e.getQueryButtonCfg,
      resetBtnCfg: e.getResetButtonCfg,
      onSearch: e.queryWithCondition,
      onLoaded: e.onQueryFormLoaded
    }, null, 8, ["id", "queryBtnCfg", "resetBtnCfg", "onSearch", "onLoaded"]), [
      [z, !e.tableReloading]
    ]),
    e.tableReloading ? g("", !0) : (a(), d(D, {
      key: 1,
      ref: "onlineTreeTableRef",
      isTreeTable: !0,
      expandedRowKeys: e.expandedRowKeys,
      onExpandedRowsChange: e.handleExpandedRowsChange,
      onExpand: e.handleExpand,
      rowKey: "jeecg_row_key",
      canResize: !0,
      bordered: !0,
      showIndexColumn: !1,
      loading: e.loading,
      columns: e.columns,
      dataSource: e.dataSource,
      pagination: e.pagination,
      rowSelection: e.rowSelection,
      actionColumn: e.actionColumn,
      showTableSetting: !0,
      clickToRowSelect: !1,
      scroll: e.tableScroll,
      onTableRedo: e.reload,
      class: Ze({ "j-table-force-nowrap": e.enableScrollBar }),
      onChange: e.handleChangeInTable
    }, {
      tableTitle: s(() => [
        e.buttonSwitch.add && e.cgBIBtnMap.add.enabled ? (a(), d(r, {
          key: 0,
          type: "primary",
          preIcon: e.cgBIBtnMap.add.buttonIcon,
          onClick: e.handleAdd
        }, {
          default: s(() => [
            R("span", null, b(e.cgBIBtnMap.add.buttonName), 1)
          ]),
          _: 1
        }, 8, ["preIcon", "onClick"])) : g("", !0),
        e.buttonSwitch.export && e.cgBIBtnMap.export.enabled ? (a(), d(r, {
          key: 1,
          type: "primary",
          preIcon: e.cgBIBtnMap.export.buttonIcon,
          onClick: e.onExportExcel
        }, {
          default: s(() => [
            R("span", null, b(e.cgBIBtnMap.export.buttonName), 1)
          ]),
          _: 1
        }, 8, ["preIcon", "onClick"])) : g("", !0),
        e.cgTopButtonList && e.cgTopButtonList.length > 0 ? (a(!0), c(j, { key: 2 }, xe(e.cgTopButtonList, (n, l) => (a(), c(j, null, [
          n.optType == "js" ? (a(), d(r, {
            key: "cgbtn" + l,
            onClick: (y) => e.cgButtonJsHandler(n.buttonCode),
            type: "primary",
            preIcon: n.buttonIcon ? "ant-design:" + n.buttonIcon : ""
          }, {
            default: s(() => [
              Q(b(n.buttonName), 1)
            ]),
            _: 2
          }, 1032, ["onClick", "preIcon"])) : n.optType == "action" ? (a(), d(r, {
            key: "cgbtn" + l,
            onClick: (y) => e.cgButtonActionHandler(n.buttonCode),
            type: "primary",
            preIcon: n.buttonIcon ? "ant-design:" + n.buttonIcon : ""
          }, {
            default: s(() => [
              Q(b(n.buttonName), 1)
            ]),
            _: 2
          }, 1032, ["onClick", "preIcon"])) : g("", !0)
        ], 64))), 256)) : g("", !0),
        e.buttonSwitch.batch_delete && e.cgBIBtnMap.batch_delete.enabled ? V((a(), d(r, {
          key: 3,
          preIcon: e.cgBIBtnMap.batch_delete.buttonIcon,
          onClick: e.handleBatchDelete
        }, {
          default: s(() => [
            R("span", null, b(e.cgBIBtnMap.batch_delete.buttonName), 1)
          ]),
          _: 1
        }, 8, ["preIcon", "onClick"])), [
          [z, e.selectedKeys.length > 0]
        ]) : g("", !0),
        e.buttonSwitch.super_query && e.cgBIBtnMap.super_query.enabled ? (a(), d(C, {
          key: 4,
          ref: "superQueryButtonRef",
          online: "",
          status: e.superQueryStatus,
          queryBtnCfg: e.cgBIBtnMap.super_query,
          onSearch: e.handleSuperQuery
        }, null, 8, ["status", "queryBtnCfg", "onSearch"])) : g("", !0)
      ]),
      fileSlot: s(({ text: n, record: l, column: y }) => [
        n ? (a(), d(r, {
          key: 1,
          ghost: !0,
          type: "primary",
          preIcon: "ant-design:download",
          size: "small",
          onClick: (H) => e.downloadRowFile(n, l, y, e.ID)
        }, {
          default: s(() => f[0] || (f[0] = [
            Q(" 下载 ")
          ])),
          _: 2
        }, 1032, ["onClick"])) : (a(), c("span", yn, "无文件"))
      ]),
      imgSlot: s(({ text: n }) => [
        n ? (a(), c("img", {
          key: 1,
          src: e.getImgView(n),
          alt: "图片不存在",
          class: "online-cell-image",
          onClick: (l) => e.viewOnlineCellImage(n)
        }, null, 8, bn)) : (a(), c("span", Bn, "无图片"))
      ]),
      htmlSlot: s(({ text: n, column: l, record: y }) => [
        l.fieldHref ? (a(), c("a", {
          key: 0,
          innerHTML: n,
          onClick: (H) => e.handleClickFieldHref(l.fieldHref, y)
        }, null, 8, wn)) : (a(), c("div", {
          key: 1,
          innerHTML: n
        }, null, 8, In))
      ]),
      pcaSlot: s(({ text: n }) => [
        R("div", {
          title: e.getPcaText(n)
        }, b(e.getPcaText(n)), 9, Sn)
      ]),
      dateSlot: s(({ text: n, column: l }) => [
        R("span", null, b(e.getFormatDate(n, l)), 1)
      ]),
      action: s(({ record: n }) => [
        B(v, {
          actions: e.getActions(n),
          dropDownActions: e.getTreeDropDownActions(n)
        }, null, 8, ["actions", "dropDownActions"])
      ]),
      _: 1
    }, 8, ["expandedRowKeys", "onExpandedRowsChange", "onExpand", "loading", "columns", "dataSource", "pagination", "rowSelection", "actionColumn", "scroll", "onTableRedo", "class", "onChange"])),
    B(E, {
      onRegister: e.registerModal,
      id: e.ID,
      cgBIBtnMap: e.cgBIBtnMap,
      buttonSwitch: e.buttonSwitch,
      confirmBtnCfg: e.getFormConfirmButtonCfg,
      onSuccess: e.handlerFormSuccess,
      onFormConfig: e.handleFormConfig
    }, null, 8, ["onRegister", "id", "cgBIBtnMap", "buttonSwitch", "confirmBtnCfg", "onSuccess", "onFormConfig"]),
    B(T, {
      onRegister: e.registerImportModal,
      url: e.importUrl(),
      onOk: e.reload,
      online: ""
    }, null, 8, ["onRegister", "url", "onOk"]),
    B(L, en(e.hrefComponent.model, nn(e.hrefComponent.on)), {
      default: s(() => [
        (a(), d(on(e.hrefComponent.is), tn(ln(e.hrefComponent.params)), null, 16))
      ]),
      _: 1
    }, 16),
    B(A, {
      onRegister: e.registerCustomModal,
      onSuccess: e.reload
    }, null, 8, ["onRegister", "onSuccess"]),
    B(O, {
      id: e.ID,
      onRegister: e.registerDetailModal
    }, null, 8, ["id", "onRegister"])
  ]);
}
const Vo = /* @__PURE__ */ fn(hn, [["render", Tn]]);
export {
  Vo as default
};
