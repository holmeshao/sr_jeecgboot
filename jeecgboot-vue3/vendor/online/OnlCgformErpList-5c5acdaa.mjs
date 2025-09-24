var E = (J, S, _) => new Promise((w, R) => {
  var O = (c) => {
    try {
      T(_.next(c));
    } catch (i) {
      R(i);
    }
  }, M = (c) => {
    try {
      T(_.throw(c));
    } catch (i) {
      R(i);
    }
  }, T = (c) => c.done ? w(c.value) : Promise.resolve(c.value).then(O, M);
  T((_ = _.apply(J, S)).next());
});
import { ref as v, watch as N, resolveComponent as I, openBlock as r, createElementBlock as g, createElementVNode as C, unref as e, createBlock as s, createCommentVNode as p, withDirectives as U, createVNode as d, vShow as G, normalizeClass as Ze, withCtx as l, toDisplayString as b, Fragment as Q, renderList as W, createTextVNode as q, mergeProps as eo, toHandlers as oo, resolveDynamicComponent as to, normalizeProps as no, guardReactiveProps as ro } from "vue";
import { BasicTable as lo, TableAction as io } from "/@/components/Table";
import { useMessage as ao } from "/@/hooks/web/useMessage";
import co from "./OnlineAutoModal-95f46901.mjs";
import so from "./OnlineCustomModal-c8b1e780.mjs";
import X from "./OnlineDetailModal-5b412bb9.mjs";
import uo from "/@/components/Form/src/jeecg/components/JImportModal.vue";
import { u as po, a as mo } from "./useListButton-98908683.mjs";
import { u as go, a as fo, O as yo, g as bo } from "./useExtendComponent-bb98e568.mjs";
import _o from "./OnlineQueryForm-9248341f.mjs";
import Co from "./SuperQuery-46032e66.mjs";
import { u as ho } from "./useOnlinePopEvent-687070b7.mjs";
import ko from "./OnlCgformErpSubTable-e1b88312.mjs";
import { E as Y } from "./constant-fa63bd66.mjs";
import { cloneDeep as vo } from "lodash-es";
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
import "./SuperQueryValComponent.vue_vue_type_script_lang-8fe34917.mjs";
import "/@/utils/index";
import "/@/hooks/web/useDesign";
const So = { class: "p-2 cgformErpList" }, wo = { class: "content" }, Ro = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, To = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, Bo = ["src", "onClick"], Io = ["innerHTML", "onClick"], Mo = ["innerHTML"], Lo = ["title"], en = {
  __name: "OnlCgformErpList",
  setup(J) {
    const S = v([]), _ = v(0), w = v(null), R = v({}), { createMessage: O } = ao(), M = v({}), T = (t, n) => {
      M.value[t] = n;
    }, {
      ID: c,
      onlineTableContext: i,
      onlineQueryFormOuter: Z,
      loading: x,
      reload: h,
      dataSource: D,
      pagination: ee,
      handleSpecialConfig: oe,
      getColumnList: te,
      handleChangeInTable: ne,
      loadData: re,
      superQueryButtonRef: P,
      superQueryStatus: le,
      handleSuperQuery: ie,
      onlineExtConfigJson: $,
      handleFormConfig: ae,
      registerCustomModal: ce,
      tableReloading: A
    } = po({ themeTemplate: Y });
    if (N(D, (t) => {
      f.value.length > 0 && (f.value = f.value.filter((n) => t.some((y) => y.jeecg_row_key === n)));
    }), !c.value)
      throw O.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    let { initCgEnhanceJs: se } = go(i);
    const {
      buttonSwitch: k,
      cgLinkButtonList: Eo,
      cgBIBtnMap: a,
      getQueryButtonCfg: ue,
      getResetButtonCfg: pe,
      getFormConfirmButtonCfg: de,
      cgTopButtonList: F,
      importUrl: me,
      registerModal: ge,
      handleAdd: fe,
      handleEdit: Oo,
      handleBatchDelete: ye,
      registerImportModal: be,
      onImportExcel: _e,
      onExportExcel: Ce,
      cgButtonJsHandler: he,
      cgButtonActionHandler: ke,
      cgButtonLinkHandler: xo,
      handleSubmitFlow: Do,
      getDropDownActions: ve,
      getActions: Se,
      initButtonList: we,
      initButtonSwitch: Re,
      registerDetailModal: Te,
      registerBpmModal: Po
    } = mo(i, $, {
      singleDelCallback: (t) => {
        Array.isArray(i.selectedRowKeys) && i.selectedRowKeys.includes(t) && i.clearSelectedRow();
      },
      editClickCallback: (t, n) => {
        Array.isArray(i.selectedRowKeys) && i.selectedRowKeys.includes(t) && n.stopPropagation();
      }
    }), H = v(!1);
    function Be() {
      return E(this, null, function* () {
        try {
          H.value = !0, yield Ce();
        } finally {
          setTimeout(() => H.value = !1, 1500);
        }
      });
    }
    const {
      columns: Ie,
      actionColumn: Me,
      selectedKeys: f,
      rowSelection: Le,
      enableScrollBar: Ee,
      tableScroll: Oe,
      downloadRowFile: xe,
      getImgView: De,
      getPcaText: j,
      getFormatDate: Pe,
      handleColumnResult: Ae,
      hrefComponent: L,
      viewOnlineCellImage: Fe,
      hrefMainTableId: He,
      registerOnlineHrefModal: Ke,
      registerPopModal: Ne,
      openPopModal: Qe,
      onlinePopModalRef: qe,
      popTableId: z,
      handleClickFieldHref: Je
    } = fo(i, $);
    N(
      f,
      (t) => {
        var n;
        (n = f.value) != null && n.length ? w.value = D.value.find((y) => y.id === f.value[0]) : w.value = null;
      },
      {
        immediate: !0
      }
    ), N(
      c,
      () => {
        $e();
      },
      { immediate: !0 }
    );
    function $e() {
      return E(this, null, function* () {
        x.value = !0;
        let t = yield te(Y);
        je(t.main), S.value = t.subList, yield re(), x.value = !1, i.execButtonEnhance("setup");
      });
    }
    function je(t) {
      let n = se(t.enhanceJs);
      i.EnhanceJS = n, we(t.cgButtonList), Re(t.hideColumns), Ae(t, "radio"), oe(t), R.value = {
        cacheKey: `online_erp_mainTable_${t.currentTableName}`
      };
    }
    function ze(t) {
      i.queryParam = t, h({ mode: "search" });
    }
    function Ve() {
      return E(this, arguments, function* (t = {}) {
        yield bo(P);
        const n = vo(t), { properties: y = {} } = n;
        Object.entries(y).forEach(([m, K]) => {
          K.view == "table" && delete y[m];
        }), P.value.init(n);
      });
    }
    function Ue(t) {
      z.value = t.id;
      let n = {
        title: t.describe
      };
      t.record && t.record.id && (n.record = t.record, n.isUpdate = !0), Qe(!0, n);
    }
    ho(Ue);
    const Ge = (t, n) => {
      ie(t, n), f.value = [];
    };
    return (t, n) => {
      var V;
      const y = I("a-skeleton"), m = I("a-button"), K = I("a-tab-pane"), We = I("a-tabs"), Xe = I("a-modal");
      return r(), g("div", So, [
        C("div", wo, [
          e(A) ? (r(), s(y, {
            key: 0,
            active: ""
          })) : p("", !0),
          U(d(_o, {
            ref_key: "onlineQueryFormOuter",
            ref: Z,
            id: e(c),
            queryBtnCfg: e(ue),
            resetBtnCfg: e(pe),
            onSearch: ze,
            onLoaded: Ve
          }, null, 8, ["id", "queryBtnCfg", "resetBtnCfg"]), [
            [G, !e(A)]
          ]),
          e(A) ? p("", !0) : (r(), s(e(lo), {
            key: 1,
            ref: "onlineTable",
            rowKey: "jeecg_row_key",
            canResize: !0,
            bordered: !0,
            showIndexColumn: !1,
            loading: e(x),
            columns: e(Ie),
            dataSource: e(D),
            pagination: e(ee),
            rowSelection: e(Le),
            actionColumn: e(Me),
            showTableSetting: !0,
            clickToRowSelect: !0,
            scroll: e(Oe),
            onTableRedo: e(h),
            tableSetting: R.value,
            class: Ze({ "j-table-force-nowrap": e(Ee) }),
            onChange: e(ne)
          }, {
            tableTitle: l(() => [
              e(k).add && e(a).add.enabled ? (r(), s(m, {
                key: 0,
                type: "primary",
                preIcon: e(a).add.buttonIcon,
                onClick: e(fe)
              }, {
                default: l(() => [
                  C("span", null, b(e(a).add.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "onClick"])) : p("", !0),
              e(k).import && e(a).import.enabled ? (r(), s(m, {
                key: 1,
                type: "primary",
                preIcon: e(a).import.buttonIcon,
                onClick: e(_e)
              }, {
                default: l(() => [
                  C("span", null, b(e(a).import.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "onClick"])) : p("", !0),
              e(k).export && e(a).export.enabled ? (r(), s(m, {
                key: 2,
                type: "primary",
                preIcon: e(a).export.buttonIcon,
                loading: H.value,
                onClick: Be
              }, {
                default: l(() => [
                  C("span", null, b(e(a).export.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "loading"])) : p("", !0),
              e(F) && e(F).length > 0 ? (r(!0), g(Q, { key: 3 }, W(e(F), (o, u) => (r(), g(Q, null, [
                o.optType == "js" ? (r(), s(m, {
                  key: "cgbtn" + u,
                  onClick: (B) => e(he)(o.buttonCode),
                  type: "primary",
                  preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
                }, {
                  default: l(() => [
                    q(b(o.buttonName), 1)
                  ]),
                  _: 2
                }, 1032, ["onClick", "preIcon"])) : o.optType == "action" ? (r(), s(m, {
                  key: "cgbtn" + u,
                  onClick: (B) => e(ke)(o.buttonCode),
                  type: "primary",
                  preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
                }, {
                  default: l(() => [
                    q(b(o.buttonName), 1)
                  ]),
                  _: 2
                }, 1032, ["onClick", "preIcon"])) : p("", !0)
              ], 64))), 256)) : p("", !0),
              e(k).batch_delete && e(a).batch_delete.enabled ? U((r(), s(m, {
                key: 4,
                preIcon: e(a).batch_delete.buttonIcon,
                onClick: e(ye)
              }, {
                default: l(() => [
                  C("span", null, b(e(a).batch_delete.buttonName), 1)
                ]),
                _: 1
              }, 8, ["preIcon", "onClick"])), [
                [G, e(f).length > 0]
              ]) : p("", !0),
              e(k).super_query && e(a).super_query.enabled ? (r(), s(Co, {
                key: 5,
                ref_key: "superQueryButtonRef",
                ref: P,
                online: "",
                status: e(le),
                queryBtnCfg: e(a).super_query,
                onSearch: Ge
              }, null, 8, ["status", "queryBtnCfg"])) : p("", !0)
            ]),
            fileSlot: l(({ text: o, record: u, column: B }) => [
              o ? (r(), s(m, {
                key: 1,
                ghost: !0,
                type: "primary",
                preIcon: "ant-design:download",
                size: "small",
                onClick: (Ye) => e(xe)(o, u, B, e(c))
              }, {
                default: l(() => n[1] || (n[1] = [
                  q(" 下载 ")
                ])),
                _: 2
              }, 1032, ["onClick"])) : (r(), g("span", Ro, "无文件"))
            ]),
            imgSlot: l(({ text: o }) => [
              o ? (r(), g("img", {
                key: 1,
                src: e(De)(o),
                alt: "图片不存在",
                class: "online-cell-image",
                onClick: (u) => e(Fe)(o)
              }, null, 8, Bo)) : (r(), g("span", To, "无图片"))
            ]),
            htmlSlot: l(({ text: o, column: u, record: B }) => [
              u.fieldHref ? (r(), g("a", {
                key: 0,
                innerHTML: o,
                onClick: (Ye) => e(Je)(u.fieldHref, B)
              }, null, 8, Io)) : (r(), g("div", {
                key: 1,
                innerHTML: o
              }, null, 8, Mo))
            ]),
            pcaSlot: l(({ text: o }) => [
              C("div", {
                title: e(j)(o)
              }, b(e(j)(o)), 9, Lo)
            ]),
            dateSlot: l(({ text: o, column: u }) => [
              C("span", null, b(e(Pe)(o, u)), 1)
            ]),
            action: l(({ record: o }) => [
              d(e(io), {
                actions: e(Se)(o),
                dropDownActions: e(ve)(o)
              }, null, 8, ["actions", "dropDownActions"])
            ]),
            _: 1
          }, 8, ["loading", "columns", "dataSource", "pagination", "rowSelection", "actionColumn", "scroll", "onTableRedo", "tableSetting", "class", "onChange"])),
          (V = S.value) != null && V.length ? (r(), s(We, {
            key: 2,
            animated: "",
            activeKey: _.value,
            "onUpdate:activeKey": n[0] || (n[0] = (o) => _.value = o),
            style: { margin: "10px" }
          }, {
            default: l(() => [
              (r(!0), g(Q, null, W(S.value, (o, u) => (r(), s(K, {
                tab: o.description,
                key: u,
                forceRender: ""
              }, {
                default: l(() => [
                  d(ko, {
                    data: o,
                    mainTableSelectedRowRcord: w.value,
                    onGetSource: T
                  }, null, 8, ["data", "mainTableSelectedRowRcord"])
                ]),
                _: 2
              }, 1032, ["tab"]))), 128))
            ]),
            _: 1
          }, 8, ["activeKey"])) : p("", !0),
          d(co, {
            onRegister: e(ge),
            id: e(c),
            subTableSource: M.value,
            cgBIBtnMap: e(a),
            buttonSwitch: e(k),
            confirmBtnCfg: e(de),
            onSuccess: e(h),
            onFormConfig: e(ae)
          }, null, 8, ["onRegister", "id", "subTableSource", "cgBIBtnMap", "buttonSwitch", "confirmBtnCfg", "onSuccess", "onFormConfig"]),
          d(X, {
            id: e(c),
            onRegister: e(Te)
          }, null, 8, ["id", "onRegister"]),
          d(uo, {
            onRegister: e(be),
            url: e(me)(),
            onOk: e(h),
            online: ""
          }, null, 8, ["onRegister", "url", "onOk"]),
          d(Xe, eo(e(L).model, oo(e(L).on)), {
            default: l(() => [
              (r(), s(to(e(L).is), no(ro(e(L).params)), null, 16))
            ]),
            _: 1
          }, 16),
          d(so, {
            onRegister: e(ce),
            onSuccess: e(h)
          }, null, 8, ["onRegister", "onSuccess"]),
          d(X, {
            id: e(He),
            onRegister: e(Ke),
            defaultFullscreen: !1
          }, null, 8, ["id", "onRegister"]),
          d(yo, {
            ref_key: "onlinePopModalRef",
            ref: qe,
            id: e(z),
            onRegister: e(Ne),
            onSuccess: e(h),
            request: "",
            topTip: ""
          }, null, 8, ["id", "onRegister", "onSuccess"])
        ])
      ]);
    };
  }
};
export {
  en as default
};
