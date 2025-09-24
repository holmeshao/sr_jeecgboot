var x = (Q, m, c) => new Promise((M, R) => {
  var y = (p) => {
    try {
      v(c.next(p));
    } catch (g) {
      R(g);
    }
  }, _ = (p) => {
    try {
      v(c.throw(p));
    } catch (g) {
      R(g);
    }
  }, v = (p) => p.done ? M(p.value) : Promise.resolve(p.value).then(y, _);
  v((c = c.apply(Q, m)).next());
});
import { reactive as Ge, ref as P, watch as Xe, resolveComponent as w, openBlock as n, createElementBlock as f, unref as e, createBlock as s, createCommentVNode as u, withDirectives as J, createVNode as b, vShow as $, normalizeClass as Ye, withCtx as r, createElementVNode as S, toDisplayString as I, Fragment as F, renderList as z, createTextVNode as H, mergeProps as Ze, toHandlers as eo, resolveDynamicComponent as oo, normalizeProps as to, guardReactiveProps as no, nextTick as ro } from "vue";
import { BasicTable as ao, TableAction as io } from "/@/components/Table";
import { useMessage as lo } from "/@/hooks/web/useMessage";
import so from "./OnlineAutoModal-95f46901.mjs";
import co from "./OnlineCustomModal-c8b1e780.mjs";
import j from "./OnlineDetailModal-5b412bb9.mjs";
import uo from "/@/components/Form/src/jeecg/components/JImportModal.vue";
import { u as po, a as mo } from "./useListButton-98908683.mjs";
import { u as go, a as fo, O as bo, g as yo } from "./useExtendComponent-bb98e568.mjs";
import _o from "./OnlineQueryForm-9248341f.mjs";
import Co from "./SuperQuery-46032e66.mjs";
import { u as ho } from "./useOnlinePopEvent-687070b7.mjs";
import Io from "./OnlCgformInnerSubTable-6b6fa15a.mjs";
import { I as ko } from "./constant-fa63bd66.mjs";
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
import "/@/components/Table/src/const";
const So = { class: "p-2" }, vo = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, To = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, wo = ["src", "onClick"], Ro = ["innerHTML", "onClick"], Bo = ["innerHTML"], xo = ["title"], Gt = {
  __name: "OnlCgformInnerTableList",
  setup(Q) {
    const m = Ge({
      tabNav: [],
      tabIndex: "0"
    }), c = P([]), M = P(null), { createMessage: R } = lo(), {
      ID: y,
      onlineTableContext: _,
      onlineQueryFormOuter: v,
      loading: p,
      reload: g,
      dataSource: V,
      pagination: U,
      handleSpecialConfig: W,
      getColumnList: G,
      handleChangeInTable: X,
      loadData: Y,
      superQueryButtonRef: O,
      superQueryStatus: Z,
      handleSuperQuery: ee,
      onlineExtConfigJson: q,
      handleFormConfig: oe,
      registerCustomModal: te,
      tableReloading: E
    } = po();
    if (!y.value)
      throw R.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    let { initCgEnhanceJs: ne } = go(_);
    const {
      buttonSwitch: k,
      cgBIBtnMap: i,
      getQueryButtonCfg: re,
      getResetButtonCfg: ae,
      getFormConfirmButtonCfg: ie,
      cgTopButtonList: L,
      importUrl: le,
      registerModal: se,
      handleAdd: ce,
      handleBatchDelete: ue,
      registerImportModal: pe,
      onImportExcel: de,
      onExportExcel: me,
      cgButtonJsHandler: ge,
      cgButtonActionHandler: fe,
      getDropDownActions: be,
      getActions: ye,
      initButtonList: _e,
      initButtonSwitch: Ce,
      registerDetailModal: he,
      registerBpmModal: Mo
    } = mo(_, q), N = P(!1);
    function Ie() {
      return x(this, null, function* () {
        try {
          N.value = !0, yield me();
        } finally {
          setTimeout(() => N.value = !1, 1500);
        }
      });
    }
    const {
      columns: ke,
      actionColumn: Se,
      selectedKeys: ve,
      rowSelection: Te,
      enableScrollBar: we,
      tableScroll: Re,
      downloadRowFile: Be,
      getImgView: xe,
      getPcaText: A,
      getFormatDate: Me,
      handleColumnResult: Oe,
      hrefComponent: B,
      viewOnlineCellImage: Ee,
      hrefMainTableId: Le,
      registerOnlineHrefModal: Ne,
      registerPopModal: De,
      openPopModal: Pe,
      onlinePopModalRef: Fe,
      popTableId: K,
      handleClickFieldHref: He
    } = fo(_, q);
    Xe(
      y,
      () => {
        Qe();
      },
      { immediate: !0 }
    );
    function Qe() {
      return x(this, null, function* () {
        p.value = !0;
        let t = yield G(ko);
        qe(t), yield Y(), p.value = !1, _.execButtonEnhance("setup");
      });
    }
    function qe(t) {
      let l = ne(t.enhanceJs);
      _.EnhanceJS = l, _e(t.cgButtonList), Ce(t.hideColumns), Oe(t), W(t);
    }
    function Ae(t) {
      _.queryParam = t, g({ mode: "search" });
    }
    function Ke(t) {
      return x(this, null, function* () {
        yield yo(O), O.value.init(t);
      });
    }
    function Je(t) {
      K.value = t.id;
      let l = {
        title: t.describe
      };
      t.record && t.record.id && (l.record = t.record, l.isUpdate = !0), Pe(!0, l);
    }
    ho(Je);
    const $e = (t) => {
      oe(t);
      const { schema: l } = t, { properties: D } = l, d = [];
      Object.entries(D).forEach(([T, C]) => {
        C.view == "tab" && d.push({ tableName: T, tableTxt: C.describe, id: C.id, order: C.order });
      }), d.sort((T, C) => T.order - C.order), m.tabNav = d;
    }, ze = (t, l) => {
      c.value = [], t && (c.value = [l.id], M.value = l);
    }, je = (t) => {
      g(t), Ve();
    }, Ve = () => {
      if (c.value.length) {
        const t = m.tabIndex;
        m.tabIndex = "-1", ro(() => {
          m.tabIndex = t;
        });
      }
    };
    return (t, l) => {
      const D = w("a-skeleton"), d = w("a-button"), T = w("a-tab-pane"), C = w("a-tabs"), Ue = w("a-modal");
      return n(), f("div", So, [
        e(E) ? (n(), s(D, {
          key: 0,
          active: ""
        })) : u("", !0),
        J(b(_o, {
          ref_key: "onlineQueryFormOuter",
          ref: v,
          id: e(y),
          queryBtnCfg: e(re),
          resetBtnCfg: e(ae),
          onSearch: Ae,
          onLoaded: Ke
        }, null, 8, ["id", "queryBtnCfg", "resetBtnCfg"]), [
          [$, !e(E)]
        ]),
        e(E) ? u("", !0) : (n(), s(e(ao), {
          key: 1,
          ref: "onlineTable",
          rowKey: "jeecg_row_key",
          canResize: !0,
          bordered: !0,
          showIndexColumn: !1,
          loading: e(p),
          columns: e(ke),
          dataSource: e(V),
          pagination: e(U),
          rowSelection: e(Te),
          actionColumn: e(Se),
          showTableSetting: !0,
          clickToRowSelect: !1,
          scroll: e(Re),
          onTableRedo: e(g),
          class: Ye({ "j-table-force-nowrap": e(we) }),
          onChange: e(X),
          expandedRowKeys: c.value,
          onExpand: ze
        }, {
          tableTitle: r(() => [
            e(k).add && e(i).add.enabled ? (n(), s(d, {
              key: 0,
              type: "primary",
              preIcon: e(i).add.buttonIcon,
              onClick: e(ce)
            }, {
              default: r(() => [
                S("span", null, I(e(i).add.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])) : u("", !0),
            e(k).import && e(i).import.enabled ? (n(), s(d, {
              key: 1,
              type: "primary",
              preIcon: e(i).import.buttonIcon,
              onClick: e(de)
            }, {
              default: r(() => [
                S("span", null, I(e(i).import.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])) : u("", !0),
            e(k).export && e(i).export.enabled ? (n(), s(d, {
              key: 2,
              type: "primary",
              preIcon: e(i).export.buttonIcon,
              loading: N.value,
              onClick: Ie
            }, {
              default: r(() => [
                S("span", null, I(e(i).export.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "loading"])) : u("", !0),
            e(L) && e(L).length > 0 ? (n(!0), f(F, { key: 3 }, z(e(L), (o, a) => (n(), f(F, null, [
              o.optType == "js" ? (n(), s(d, {
                key: "cgbtn" + a,
                onClick: (h) => e(ge)(o.buttonCode),
                type: "primary",
                preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
              }, {
                default: r(() => [
                  H(I(o.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"])) : o.optType == "action" ? (n(), s(d, {
                key: "cgbtn" + a,
                onClick: (h) => e(fe)(o.buttonCode),
                type: "primary",
                preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
              }, {
                default: r(() => [
                  H(I(o.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"])) : u("", !0)
            ], 64))), 256)) : u("", !0),
            e(k).batch_delete && e(i).batch_delete.enabled ? J((n(), s(d, {
              key: 4,
              preIcon: e(i).batch_delete.buttonIcon,
              onClick: e(ue)
            }, {
              default: r(() => [
                S("span", null, I(e(i).batch_delete.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])), [
              [$, e(ve).length > 0]
            ]) : u("", !0),
            e(k).super_query && e(i).super_query.enabled ? (n(), s(Co, {
              key: 5,
              ref_key: "superQueryButtonRef",
              ref: O,
              online: "",
              status: e(Z),
              queryBtnCfg: e(i).super_query,
              onSearch: e(ee)
            }, null, 8, ["status", "queryBtnCfg", "onSearch"])) : u("", !0)
          ]),
          expandedRowRender: r(({ record: o }) => [
            c.value[0] && o.id == c.value[0] ? (n(), s(C, {
              key: 0,
              activeKey: m.tabIndex,
              "onUpdate:activeKey": l[0] || (l[0] = (a) => m.tabIndex = a)
            }, {
              default: r(() => [
                c.value.length ? (n(!0), f(F, { key: 0 }, z(m.tabNav, (a, h) => (n(), s(T, {
                  tab: a.tableTxt,
                  key: h + ""
                }, {
                  default: r(() => [
                    m.tabIndex == h ? (n(), s(Io, {
                      key: 0,
                      subTableId: a.id,
                      mTableSelectedRcordId: c.value[0],
                      subTableName: a.tableName
                    }, null, 8, ["subTableId", "mTableSelectedRcordId", "subTableName"])) : u("", !0)
                  ]),
                  _: 2
                }, 1032, ["tab"]))), 128)) : u("", !0)
              ]),
              _: 1
            }, 8, ["activeKey"])) : u("", !0)
          ]),
          fileSlot: r(({ text: o, record: a, column: h }) => [
            o ? (n(), s(d, {
              key: 1,
              ghost: !0,
              type: "primary",
              preIcon: "ant-design:download",
              size: "small",
              onClick: (We) => e(Be)(o, a, h, e(y))
            }, {
              default: r(() => l[1] || (l[1] = [
                H(" 下载 ")
              ])),
              _: 2
            }, 1032, ["onClick"])) : (n(), f("span", vo, "无文件"))
          ]),
          imgSlot: r(({ text: o }) => [
            o ? (n(), f("img", {
              key: 1,
              src: e(xe)(o),
              alt: "图片不存在",
              class: "online-cell-image",
              onClick: (a) => e(Ee)(o)
            }, null, 8, wo)) : (n(), f("span", To, "无图片"))
          ]),
          htmlSlot: r(({ text: o, column: a, record: h }) => [
            a.fieldHref ? (n(), f("a", {
              key: 0,
              innerHTML: o,
              onClick: (We) => e(He)(a.fieldHref, h)
            }, null, 8, Ro)) : (n(), f("div", {
              key: 1,
              innerHTML: o
            }, null, 8, Bo))
          ]),
          pcaSlot: r(({ text: o }) => [
            S("div", {
              title: e(A)(o)
            }, I(e(A)(o)), 9, xo)
          ]),
          dateSlot: r(({ text: o, column: a }) => [
            S("span", null, I(e(Me)(o, a)), 1)
          ]),
          action: r(({ record: o }) => [
            b(e(io), {
              actions: e(ye)(o),
              dropDownActions: e(be)(o)
            }, null, 8, ["actions", "dropDownActions"])
          ]),
          _: 1
        }, 8, ["loading", "columns", "dataSource", "pagination", "rowSelection", "actionColumn", "scroll", "onTableRedo", "class", "onChange", "expandedRowKeys"])),
        b(so, {
          onRegister: e(se),
          id: e(y),
          cgBIBtnMap: e(i),
          buttonSwitch: e(k),
          confirmBtnCfg: e(ie),
          onSuccess: je,
          onFormConfig: $e
        }, null, 8, ["onRegister", "id", "cgBIBtnMap", "buttonSwitch", "confirmBtnCfg"]),
        b(j, {
          id: e(y),
          onRegister: e(he)
        }, null, 8, ["id", "onRegister"]),
        b(uo, {
          onRegister: e(pe),
          url: e(le)(),
          onOk: e(g),
          online: ""
        }, null, 8, ["onRegister", "url", "onOk"]),
        b(Ue, Ze(e(B).model, eo(e(B).on)), {
          default: r(() => [
            (n(), s(oo(e(B).is), to(no(e(B).params)), null, 16))
          ]),
          _: 1
        }, 16),
        b(co, {
          onRegister: e(te),
          onSuccess: e(g)
        }, null, 8, ["onRegister", "onSuccess"]),
        b(j, {
          id: e(Le),
          onRegister: e(Ne),
          defaultFullscreen: !1
        }, null, 8, ["id", "onRegister"]),
        b(bo, {
          ref_key: "onlinePopModalRef",
          ref: Fe,
          id: e(K),
          onRegister: e(De),
          onSuccess: e(g),
          request: "",
          topTip: ""
        }, null, 8, ["id", "onRegister", "onSuccess"])
      ]);
    };
  }
};
export {
  Gt as default
};
