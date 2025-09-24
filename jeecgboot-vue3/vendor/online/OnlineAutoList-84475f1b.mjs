var R = (e, m, l) => new Promise((w, f) => {
  var b = (t) => {
    try {
      B(l.next(t));
    } catch (C) {
      f(C);
    }
  }, I = (t) => {
    try {
      B(l.throw(t));
    } catch (C) {
      f(C);
    }
  }, B = (t) => t.done ? w(t.value) : Promise.resolve(t.value).then(b, I);
  B((l = l.apply(e, m)).next());
});
import { ref as He, watch as Pe, resolveComponent as p, openBlock as n, createElementBlock as c, normalizeClass as Q, createBlock as u, createCommentVNode as s, withDirectives as N, createVNode as d, vShow as $, withCtx as i, createElementVNode as y, toDisplayString as g, Fragment as J, renderList as qe, createTextVNode as H, mergeProps as Qe, toHandlers as Ne, resolveDynamicComponent as $e, normalizeProps as Je, guardReactiveProps as ze } from "vue";
import { BasicTable as Ve, TableAction as je } from "/@/components/Table";
import { useMessage as Ke } from "/@/hooks/web/useMessage";
import Ue from "./OnlineAutoModal-95f46901.mjs";
import We from "./OnlineCustomModal-c8b1e780.mjs";
import Ge from "./OnlineDetailModal-5b412bb9.mjs";
import Xe from "/@/components/Form/src/jeecg/components/JImportModal.vue";
import { u as Ye, a as Ze } from "./useListButton-98908683.mjs";
import { O as _e, u as xe, a as eo, g as oo } from "./useExtendComponent-bb98e568.mjs";
import no from "./OnlineQueryForm-9248341f.mjs";
import to from "./SuperQuery-46032e66.mjs";
import { u as ro } from "./useOnlinePopEvent-687070b7.mjs";
import { N as io } from "./constant-fa63bd66.mjs";
import { _ as lo } from "./index-9e1e1e53.mjs";
import "/@/components/Modal";
import "./OnlineForm-58282699.mjs";
import "/@/components/Form/index";
import "/@/utils/http/axios";
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
const ao = {
  name: "OnlineAutoList",
  components: {
    BasicTable: Ve,
    TableAction: je,
    OnlineAutoModal: Ue,
    JImportModal: Xe,
    OnlineQueryForm: no,
    OnlineSuperQuery: to,
    OnlineCustomModal: We,
    OnlineDetailModal: Ge,
    OnlinePopModal: _e
  },
  setup() {
    const { createMessage: e } = Ke(), {
      ID: m,
      onlineTableContext: l,
      onlineQueryFormOuter: w,
      loading: f,
      reload: b,
      dataSource: I,
      pagination: B,
      handleSpecialConfig: t,
      getColumnList: C,
      handleChangeInTable: v,
      loadData: L,
      superQueryButtonRef: M,
      superQueryStatus: T,
      handleSuperQuery: D,
      onlineExtConfigJson: S,
      handleFormConfig: E,
      registerCustomModal: O,
      tableReloading: o
    } = Ye();
    if (!m.value)
      throw e.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    let { initCgEnhanceJs: a } = xe(l);
    const {
      buttonSwitch: h,
      cgLinkButtonList: F,
      cgBIBtnMap: z,
      getQueryButtonCfg: V,
      getResetButtonCfg: j,
      getFormConfirmButtonCfg: K,
      cgTopButtonList: U,
      importUrl: W,
      registerModal: G,
      handleAdd: X,
      handleEdit: Y,
      handleBatchDelete: Z,
      registerImportModal: _,
      onImportExcel: x,
      onExportExcel: P,
      cgButtonJsHandler: ee,
      cgButtonActionHandler: oe,
      cgButtonLinkHandler: ne,
      handleSubmitFlow: te,
      getDropDownActions: re,
      getActions: ie,
      initButtonList: le,
      initButtonSwitch: ae,
      registerDetailModal: pe,
      registerBpmModal: ue
    } = Ze(l, S), A = He(!1);
    function se() {
      return R(this, null, function* () {
        try {
          A.value = !0, yield P();
        } finally {
          setTimeout(() => A.value = !1, 1500);
        }
      });
    }
    const {
      columns: de,
      actionColumn: me,
      selectedKeys: ce,
      rowSelection: ge,
      enableScrollBar: fe,
      tableScroll: Be,
      downloadRowFile: Ce,
      getImgView: he,
      getPcaText: ye,
      getFormatDate: be,
      handleColumnResult: Ie,
      hrefComponent: Me,
      viewOnlineCellImage: Se,
      hrefMainTableId: ke,
      registerOnlineHrefModal: we,
      registerPopModal: Te,
      openPopModal: Re,
      onlinePopModalRef: ve,
      popTableId: q,
      handleClickFieldHref: Le
    } = eo(l, S);
    Pe(
      m,
      () => {
        De();
      },
      { immediate: !0 }
    );
    function De() {
      return R(this, null, function* () {
        f.value = !0;
        let r = yield C(io);
        Ee(r), yield L(), f.value = !1, l.execButtonEnhance("setup");
      });
    }
    function Ee(r) {
      let k = a(r.enhanceJs);
      l.EnhanceJS = k, le(r.cgButtonList), ae(r.hideColumns), Ie(r), t(r);
    }
    function Oe(r) {
      l.queryParam = r, b({ mode: "search" });
    }
    function Fe(r) {
      return R(this, null, function* () {
        yield oo(M), M.value.init(r);
      });
    }
    function Ae(r) {
      q.value = r.id;
      let k = {
        title: r.describe
      };
      r.record && r.record.id && (k.record = r.record, k.isUpdate = !0), Re(!0, k);
    }
    return ro(Ae), {
      ID: m,
      // 查询区域
      onlineQueryFormOuter: w,
      queryWithCondition: Oe,
      onQueryFormLoaded: Fe,
      reload: b,
      //高级查询
      superQueryButtonRef: M,
      superQueryStatus: T,
      handleSuperQuery: D,
      // table区域
      loading: f,
      columns: de,
      dataSource: I,
      pagination: B,
      actionColumn: me,
      rowSelection: ge,
      selectedKeys: ce,
      tableScroll: Be,
      enableScrollBar: fe,
      handleChangeInTable: v,
      //按钮
      buttonSwitch: h,
      handleAdd: X,
      handleEdit: Y,
      onImportExcel: x,
      onExportExcel: P,
      exportLoading: A,
      onExportExcelOverride: se,
      cgBIBtnMap: z,
      getQueryButtonCfg: V,
      getResetButtonCfg: j,
      getFormConfirmButtonCfg: K,
      cgTopButtonList: U,
      cgLinkButtonList: F,
      cgButtonJsHandler: ee,
      cgButtonActionHandler: oe,
      cgButtonLinkHandler: ne,
      handleBatchDelete: Z,
      // table-slot
      downloadRowFile: Ce,
      getImgView: he,
      getPcaText: ye,
      getFormatDate: be,
      // 操作列
      getActions: ie,
      getDropDownActions: re,
      // 弹窗
      registerModal: G,
      registerCustomModal: O,
      registerImportModal: _,
      registerDetailModal: pe,
      importUrl: W,
      handleFormConfig: E,
      onlinePopModalRef: ve,
      //其他
      tableReloading: o,
      handleSubmitFlow: te,
      hrefComponent: Me,
      viewOnlineCellImage: Se,
      hrefMainTableId: ke,
      onlineExtConfigJson: S,
      registerOnlineHrefModal: we,
      registerPopModal: Te,
      popTableId: q,
      registerBpmModal: ue,
      handleClickFieldHref: Le
    };
  }
  // 1引入了loadsh   console.log(that.simpleDateFormat(new Date().getTime(),'yyyy-MM-dd'));
  // 2. value的问题
  // 3. 变量位置改变后需要 重写api
  // 1添加按钮的时候 预留出样式对象 然后js增强中设置样式对象
  // 2直接设置css字符串 然后通过js document 往head里面增加css片段 全局生效
};
const po = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, uo = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, so = ["src", "onClick"], mo = ["innerHTML", "onClick"], co = ["innerHTML"], go = ["title"];
function fo(e, m, l, w, f, b) {
  const I = p("a-skeleton"), B = p("online-query-form"), t = p("a-button"), C = p("online-super-query"), v = p("TableAction"), L = p("BasicTable"), M = p("OnlineAutoModal"), T = p("online-detail-modal"), D = p("JImportModal"), S = p("a-modal"), E = p("online-custom-modal"), O = p("online-pop-modal");
  return n(), c("div", {
    class: Q(["p-2", `online-list-${e.ID}`])
  }, [
    e.tableReloading ? (n(), u(I, {
      key: 0,
      active: ""
    })) : s("", !0),
    N(d(B, {
      ref: "onlineQueryFormOuter",
      id: e.ID,
      queryBtnCfg: e.getQueryButtonCfg,
      resetBtnCfg: e.getResetButtonCfg,
      onSearch: e.queryWithCondition,
      onLoaded: e.onQueryFormLoaded
    }, null, 8, ["id", "queryBtnCfg", "resetBtnCfg", "onSearch", "onLoaded"]), [
      [$, !e.tableReloading]
    ]),
    e.tableReloading ? s("", !0) : (n(), u(L, {
      key: 1,
      ref: "onlineTable",
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
      class: Q({ "j-table-force-nowrap": e.enableScrollBar }),
      onChange: e.handleChangeInTable
    }, {
      tableTitle: i(() => [
        e.buttonSwitch.add && e.cgBIBtnMap.add.enabled ? (n(), u(t, {
          key: 0,
          type: "primary",
          preIcon: e.cgBIBtnMap.add.buttonIcon,
          onClick: e.handleAdd
        }, {
          default: i(() => [
            y("span", null, g(e.cgBIBtnMap.add.buttonName), 1)
          ]),
          _: 1
        }, 8, ["preIcon", "onClick"])) : s("", !0),
        e.buttonSwitch.import && e.cgBIBtnMap.import.enabled ? (n(), u(t, {
          key: 1,
          type: "primary",
          preIcon: e.cgBIBtnMap.import.buttonIcon,
          onClick: e.onImportExcel
        }, {
          default: i(() => [
            y("span", null, g(e.cgBIBtnMap.import.buttonName), 1)
          ]),
          _: 1
        }, 8, ["preIcon", "onClick"])) : s("", !0),
        e.buttonSwitch.export && e.cgBIBtnMap.export.enabled ? (n(), u(t, {
          key: 2,
          type: "primary",
          preIcon: e.cgBIBtnMap.export.buttonIcon,
          loading: e.exportLoading,
          onClick: e.onExportExcelOverride
        }, {
          default: i(() => [
            y("span", null, g(e.cgBIBtnMap.export.buttonName), 1)
          ]),
          _: 1
        }, 8, ["preIcon", "loading", "onClick"])) : s("", !0),
        e.cgTopButtonList && e.cgTopButtonList.length > 0 ? (n(!0), c(J, { key: 3 }, qe(e.cgTopButtonList, (o, a) => (n(), c(J, null, [
          o.optType == "js" ? (n(), u(t, {
            key: "cgbtn" + a,
            onClick: (h) => e.cgButtonJsHandler(o.buttonCode),
            type: "primary",
            preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
          }, {
            default: i(() => [
              H(g(o.buttonName), 1)
            ]),
            _: 2
          }, 1032, ["onClick", "preIcon"])) : o.optType == "action" ? (n(), u(t, {
            key: "cgbtn" + a,
            onClick: (h) => e.cgButtonActionHandler(o.buttonCode),
            type: "primary",
            preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
          }, {
            default: i(() => [
              H(g(o.buttonName), 1)
            ]),
            _: 2
          }, 1032, ["onClick", "preIcon"])) : s("", !0)
        ], 64))), 256)) : s("", !0),
        e.buttonSwitch.batch_delete && e.cgBIBtnMap.batch_delete.enabled ? N((n(), u(t, {
          key: 4,
          preIcon: e.cgBIBtnMap.batch_delete.buttonIcon,
          onClick: e.handleBatchDelete
        }, {
          default: i(() => [
            y("span", null, g(e.cgBIBtnMap.batch_delete.buttonName), 1)
          ]),
          _: 1
        }, 8, ["preIcon", "onClick"])), [
          [$, e.selectedKeys.length > 0]
        ]) : s("", !0),
        e.buttonSwitch.super_query && e.cgBIBtnMap.super_query.enabled ? (n(), u(C, {
          key: 5,
          ref: "superQueryButtonRef",
          online: "",
          status: e.superQueryStatus,
          queryBtnCfg: e.cgBIBtnMap.super_query,
          onSearch: e.handleSuperQuery
        }, null, 8, ["status", "queryBtnCfg", "onSearch"])) : s("", !0)
      ]),
      fileSlot: i(({ text: o, record: a, column: h }) => [
        o ? (n(), u(t, {
          key: 1,
          ghost: !0,
          type: "primary",
          preIcon: "ant-design:download",
          size: "small",
          onClick: (F) => e.downloadRowFile(o, a, h, e.ID)
        }, {
          default: i(() => m[0] || (m[0] = [
            H(" 下载 ")
          ])),
          _: 2
        }, 1032, ["onClick"])) : (n(), c("span", po, "无文件"))
      ]),
      imgSlot: i(({ text: o }) => [
        o ? (n(), c("img", {
          key: 1,
          src: e.getImgView(o),
          alt: "图片不存在",
          class: "online-cell-image",
          onClick: (a) => e.viewOnlineCellImage(o)
        }, null, 8, so)) : (n(), c("span", uo, "无图片"))
      ]),
      htmlSlot: i(({ text: o, column: a, record: h }) => [
        a.fieldHref ? (n(), c("a", {
          key: 0,
          innerHTML: o,
          onClick: (F) => e.handleClickFieldHref(a.fieldHref, h)
        }, null, 8, mo)) : (n(), c("div", {
          key: 1,
          innerHTML: o
        }, null, 8, co))
      ]),
      pcaSlot: i(({ text: o }) => [
        y("div", {
          title: e.getPcaText(o)
        }, g(e.getPcaText(o)), 9, go)
      ]),
      dateSlot: i(({ text: o, column: a }) => [
        y("span", null, g(e.getFormatDate(o, a)), 1)
      ]),
      action: i(({ record: o }) => [
        d(v, {
          actions: e.getActions(o),
          dropDownActions: e.getDropDownActions(o)
        }, null, 8, ["actions", "dropDownActions"])
      ]),
      _: 1
    }, 8, ["loading", "columns", "dataSource", "pagination", "rowSelection", "actionColumn", "scroll", "onTableRedo", "class", "onChange"])),
    d(M, {
      onRegister: e.registerModal,
      id: e.ID,
      cgBIBtnMap: e.cgBIBtnMap,
      buttonSwitch: e.buttonSwitch,
      confirmBtnCfg: e.getFormConfirmButtonCfg,
      onSuccess: e.reload,
      onFormConfig: e.handleFormConfig
    }, null, 8, ["onRegister", "id", "cgBIBtnMap", "buttonSwitch", "confirmBtnCfg", "onSuccess", "onFormConfig"]),
    d(T, {
      id: e.ID,
      onRegister: e.registerDetailModal
    }, null, 8, ["id", "onRegister"]),
    d(D, {
      onRegister: e.registerImportModal,
      url: e.importUrl(),
      onOk: e.reload,
      online: ""
    }, null, 8, ["onRegister", "url", "onOk"]),
    d(S, Qe(e.hrefComponent.model, Ne(e.hrefComponent.on)), {
      default: i(() => [
        (n(), u($e(e.hrefComponent.is), Je(ze(e.hrefComponent.params)), null, 16))
      ]),
      _: 1
    }, 16),
    d(E, {
      onRegister: e.registerCustomModal,
      onSuccess: e.reload
    }, null, 8, ["onRegister", "onSuccess"]),
    d(T, {
      id: e.hrefMainTableId,
      onRegister: e.registerOnlineHrefModal,
      defaultFullscreen: !1
    }, null, 8, ["id", "onRegister"]),
    d(O, {
      ref: "onlinePopModalRef",
      id: e.popTableId,
      onRegister: e.registerPopModal,
      onSuccess: e.reload,
      request: "",
      topTip: ""
    }, null, 8, ["id", "onRegister", "onSuccess"])
  ], 2);
}
const Hn = /* @__PURE__ */ lo(ao, [["render", fo]]);
export {
  Hn as default
};
