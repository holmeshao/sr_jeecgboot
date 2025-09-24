var D = (T, M, c) => new Promise((E, p) => {
  var O = (n) => {
    try {
      m(c.next(n));
    } catch (C) {
      p(C);
    }
  }, F = (n) => {
    try {
      m(c.throw(n));
    } catch (C) {
      p(C);
    }
  }, m = (n) => n.done ? E(n.value) : Promise.resolve(n.value).then(O, F);
  m((c = c.apply(T, M)).next());
});
import { ref as H, provide as Qe, watch as U, resolveComponent as N, openBlock as l, createElementBlock as g, Fragment as A, unref as e, createBlock as s, createCommentVNode as d, withDirectives as W, vShow as G, normalizeClass as je, withCtx as i, createElementVNode as k, toDisplayString as f, renderList as Ue, createTextVNode as K, createVNode as b, mergeProps as We, toHandlers as Ge, resolveDynamicComponent as Xe, normalizeProps as Ye, guardReactiveProps as Ze } from "vue";
import { BasicTable as eo, TableAction as oo } from "/@/components/Table";
import { useMessage as to } from "/@/hooks/web/useMessage";
import no from "./OnlineAutoModal-95f46901.mjs";
import lo from "./OnlineCustomModal-c8b1e780.mjs";
import X from "./OnlineDetailModal-5b412bb9.mjs";
import io from "/@/components/Form/src/jeecg/components/JImportModal.vue";
import { u as ro, a as ao } from "./useListButton-98908683.mjs";
import { u as co, a as so, O as uo } from "./useExtendComponent-bb98e568.mjs";
import po from "./OnlineQueryForm-9248341f.mjs";
import { u as mo } from "./useOnlinePopEvent-687070b7.mjs";
import { E as go, a as Y } from "./constant-fa63bd66.mjs";
import { cloneDeep as fo } from "lodash-es";
import "/@/components/Modal";
import "./OnlineForm-58282699.mjs";
import "/@/components/Form/index";
import "/@/utils/http/axios";
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
import "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
import "/@/components/jeecg/comment/CommentPanel.vue";
import "./OnlineFormDetail-fc087725.mjs";
import "./DetailForm-c592b8d8.mjs";
import "/@/utils/propTypes";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/dict/index";
import "/@/api/common/api";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "/@/components/Markdown";
import "./OnlineSubFormDetail-8be879b9.mjs";
import "/@/router";
import "/@/hooks/core/onMountedOrActivated";
import "/@/store/modules/multipleTab";
import "./cgformState-d9f8ec42.mjs";
import "pinia";
import "/@/store";
import "ant-design-vue";
import "/@/hooks/system/useMethods";
import "/@/components/Form/src/componentMap";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/views/system/user/user.api";
import "/@/utils/desform/customExpression";
import "/@/hooks/system/useListPage";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "@vueuse/core";
import "/@/components/Form/src/jeecg/components/JRangeNumber.vue";
const bo = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, yo = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, Co = ["src", "onClick"], ho = ["innerHTML", "onClick"], ko = ["innerHTML"], So = ["title"], $t = {
  __name: "OnlCgformErpSubTable",
  props: ["data", "mainTableSelectedRowRcord"],
  emits: ["getSource"],
  setup(T, { emit: M }) {
    const c = T, E = M, p = H(!1), O = {
      cacheKey: `online_erp_subTable_${c.data.currentTableName}`
    }, { createMessage: F } = to(), {
      ID: m,
      onlineTableContext: n,
      onlineQueryFormOuter: C,
      loading: B,
      reload: h,
      dataSource: S,
      pagination: $,
      handleSpecialConfig: Z,
      getColumnList: _o,
      handleChangeInTable: ee,
      loadData: oe,
      onlineExtConfigJson: J,
      handleFormConfig: te,
      registerCustomModal: ne,
      tableReloading: L
    } = ro({ code: c.data.code, themeTemplate: go });
    n.isErpSubTable = !0;
    let { initCgEnhanceJs: le } = co(n);
    const {
      buttonSwitch: _,
      cgLinkButtonList: vo,
      cgBIBtnMap: r,
      getQueryButtonCfg: ie,
      getResetButtonCfg: re,
      getFormConfirmButtonCfg: ae,
      cgTopButtonList: x,
      importUrl: ce,
      registerModal: se,
      handleAdd: ue,
      handleEdit: To,
      handleBatchDelete: pe,
      registerImportModal: de,
      onImportExcel: me,
      onExportExcel: ge,
      cgButtonJsHandler: fe,
      cgButtonActionHandler: be,
      cgButtonLinkHandler: Bo,
      handleSubmitFlow: Io,
      getDropDownActions: ye,
      getActions: Ce,
      initButtonList: he,
      initButtonSwitch: ke,
      registerDetailModal: Se,
      registerBpmModal: Ro
    } = ao(n, J), P = H(!1);
    function _e() {
      return D(this, null, function* () {
        try {
          P.value = !0, yield ge();
        } finally {
          setTimeout(() => P.value = !1, 1500);
        }
      });
    }
    const {
      columns: ve,
      actionColumn: Te,
      selectedKeys: z,
      rowSelection: Be,
      enableScrollBar: Ie,
      tableScroll: Re,
      downloadRowFile: we,
      getImgView: Me,
      getPcaText: V,
      getFormatDate: Ee,
      handleColumnResult: Oe,
      hrefComponent: I,
      viewOnlineCellImage: Fe,
      hrefMainTableId: Le,
      registerOnlineHrefModal: xe,
      registerPopModal: Pe,
      openPopModal: De,
      onlinePopModalRef: He,
      popTableId: q,
      handleClickFieldHref: Ne
    } = so(n, J);
    Ae(fo(c.data));
    const Q = H(null), R = c.data.foreignKeys;
    let j;
    if (R != null && R.length) {
      const t = R[0], a = t.field;
      j = t.key, n.foreignKeyField = a;
    } else
      n.foreignKeyField = null, n.foreignKeyValue = null;
    Qe("foreignkey", Q), U(
      () => c.mainTableSelectedRowRcord,
      (t) => {
        var a;
        if ($.value.current = 1, z.value = [], (a = C.value) == null || a.clearSearch(), t) {
          if (n.foreignKeyField) {
            const w = t[j];
            n.foreignKeyValue = w, Q.value = { field: n.foreignKeyField, value: w };
          }
          p.value = !0, B.value = !0, oe().finally(() => {
            B.value = !1;
          });
        } else
          p.value = !1, S.value = [];
      },
      {
        immediate: !0
      }
    ), U(
      () => S.value,
      () => {
        E("getSource", c.data.currentTableName, S.value);
      },
      { immediate: !0 }
    );
    function Ae(t) {
      return D(this, null, function* () {
        Ke(t), B.value = !1, n.execButtonEnhance("setup");
      });
    }
    function Ke(t) {
      let a = le(t.enhanceJs);
      n.EnhanceJS = a, he(t.cgButtonList), ke(t.hideColumns), Oe(t), Z(t);
    }
    function $e(t) {
      n.queryParam = t, h({ mode: "search" });
    }
    function Je(t) {
      q.value = t.id;
      let a = {
        title: t.describe
      };
      t.record && t.record.id && (a.record = t.record, a.isUpdate = !0), De(!0, a);
    }
    mo(Je);
    const ze = () => {
      c.data.relationType == 1 && S.value.length ? F.warning("一对一的表只能新增一条数据") : ue();
    };
    return (t, a) => {
      const w = N("a-skeleton"), y = N("a-button"), Ve = N("a-modal");
      return l(), g(A, null, [
        e(L) ? (l(), s(w, {
          key: 0,
          active: ""
        })) : d("", !0),
        T.mainTableSelectedRowRcord ? W((l(), s(po, {
          key: 1,
          ref_key: "onlineQueryFormOuter",
          ref: C,
          id: e(m),
          queryBtnCfg: e(ie),
          resetBtnCfg: e(re),
          onSearch: $e
        }, null, 8, ["id", "queryBtnCfg", "resetBtnCfg"])), [
          [G, !e(L)]
        ]) : d("", !0),
        e(L) ? d("", !0) : (l(), s(e(eo), {
          key: 2,
          ref: "onlineTable",
          rowKey: "jeecg_row_key",
          canResize: !0,
          bordered: !0,
          showIndexColumn: !1,
          loading: e(B),
          columns: e(ve),
          dataSource: e(S),
          pagination: e($),
          rowSelection: e(Be),
          actionColumn: e(Te),
          showTableSetting: !0,
          clickToRowSelect: !1,
          scroll: e(Re),
          onTableRedo: e(h),
          class: je({ "j-table-force-nowrap": e(Ie) }),
          tableSetting: O,
          onChange: e(ee),
          minHeight: 300
        }, {
          tableTitle: i(() => [
            p.value && e(_).add && e(r).add.enabled ? (l(), s(y, {
              key: 0,
              type: "primary",
              preIcon: e(r).add.buttonIcon,
              onClick: ze
            }, {
              default: i(() => [
                k("span", null, f(e(r).add.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon"])) : d("", !0),
            p.value && e(_).import && e(r).import.enabled ? (l(), s(y, {
              key: 1,
              type: "primary",
              preIcon: e(r).import.buttonIcon,
              onClick: e(me)
            }, {
              default: i(() => [
                k("span", null, f(e(r).import.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])) : d("", !0),
            p.value && e(_).export && e(r).export.enabled ? (l(), s(y, {
              key: 2,
              type: "primary",
              preIcon: e(r).export.buttonIcon,
              loading: P.value,
              onClick: _e
            }, {
              default: i(() => [
                k("span", null, f(e(r).export.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "loading"])) : d("", !0),
            p.value && e(x) && e(x).length > 0 ? (l(!0), g(A, { key: 3 }, Ue(e(x), (o, u) => (l(), g(A, null, [
              o.optType == "js" ? (l(), s(y, {
                key: "cgbtn" + u,
                onClick: (v) => e(fe)(o.buttonCode),
                type: "primary",
                preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
              }, {
                default: i(() => [
                  K(f(o.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"])) : o.optType == "action" ? (l(), s(y, {
                key: "cgbtn" + u,
                onClick: (v) => e(be)(o.buttonCode),
                type: "primary",
                preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
              }, {
                default: i(() => [
                  K(f(o.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"])) : d("", !0)
            ], 64))), 256)) : d("", !0),
            e(_).batch_delete && e(r).batch_delete.enabled ? W((l(), s(y, {
              key: 4,
              preIcon: e(r).batch_delete.buttonIcon,
              onClick: e(pe)
            }, {
              default: i(() => [
                k("span", null, f(e(r).batch_delete.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])), [
              [G, e(z).length > 0]
            ]) : d("", !0)
          ]),
          fileSlot: i(({ text: o, record: u, column: v }) => [
            o ? (l(), s(y, {
              key: 1,
              ghost: !0,
              type: "primary",
              preIcon: "ant-design:download",
              size: "small",
              onClick: (qe) => e(we)(o, u, v, e(m))
            }, {
              default: i(() => a[0] || (a[0] = [
                K(" 下载 ")
              ])),
              _: 2
            }, 1032, ["onClick"])) : (l(), g("span", bo, "无文件"))
          ]),
          imgSlot: i(({ text: o }) => [
            o ? (l(), g("img", {
              key: 1,
              src: e(Me)(o),
              alt: "图片不存在",
              class: "online-cell-image",
              onClick: (u) => e(Fe)(o)
            }, null, 8, Co)) : (l(), g("span", yo, "无图片"))
          ]),
          htmlSlot: i(({ text: o, column: u, record: v }) => [
            u.fieldHref ? (l(), g("a", {
              key: 0,
              innerHTML: o,
              onClick: (qe) => e(Ne)(u.fieldHref, v)
            }, null, 8, ho)) : (l(), g("div", {
              key: 1,
              innerHTML: o
            }, null, 8, ko))
          ]),
          pcaSlot: i(({ text: o }) => [
            k("div", {
              title: e(V)(o)
            }, f(e(V)(o)), 9, So)
          ]),
          dateSlot: i(({ text: o, column: u }) => [
            k("span", null, f(e(Ee)(o, u)), 1)
          ]),
          action: i(({ record: o }) => [
            b(e(oo), {
              actions: e(Ce)(o),
              dropDownActions: e(ye)(o)
            }, null, 8, ["actions", "dropDownActions"])
          ]),
          _: 1
        }, 8, ["loading", "columns", "dataSource", "pagination", "rowSelection", "actionColumn", "scroll", "onTableRedo", "class", "onChange"])),
        b(no, {
          onRegister: e(se),
          id: e(m),
          source: e(Y),
          cgBIBtnMap: e(r),
          buttonSwitch: e(_),
          confirmBtnCfg: e(ae),
          onSuccess: e(h),
          onFormConfig: e(te)
        }, null, 8, ["onRegister", "id", "source", "cgBIBtnMap", "buttonSwitch", "confirmBtnCfg", "onSuccess", "onFormConfig"]),
        b(X, {
          source: e(Y),
          id: e(m),
          onRegister: e(Se)
        }, null, 8, ["source", "id", "onRegister"]),
        b(io, {
          onRegister: e(de),
          url: e(ce)(),
          onOk: e(h),
          online: ""
        }, null, 8, ["onRegister", "url", "onOk"]),
        b(Ve, We(e(I).model, Ge(e(I).on)), {
          default: i(() => [
            (l(), s(Xe(e(I).is), Ye(Ze(e(I).params)), null, 16))
          ]),
          _: 1
        }, 16),
        b(lo, {
          onRegister: e(ne),
          onSuccess: e(h)
        }, null, 8, ["onRegister", "onSuccess"]),
        b(X, {
          id: e(Le),
          onRegister: e(xe),
          defaultFullscreen: !1
        }, null, 8, ["id", "onRegister"]),
        b(uo, {
          ref_key: "onlinePopModalRef",
          ref: He,
          id: e(q),
          onRegister: e(Pe),
          onSuccess: e(h),
          request: "",
          topTip: ""
        }, null, 8, ["id", "onRegister", "onSuccess"])
      ], 64);
    };
  }
};
export {
  $t as default
};
