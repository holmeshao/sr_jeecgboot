var T = (E, v, l) => new Promise((m, B) => {
  var h = (c) => {
    try {
      k(l.next(c));
    } catch (I) {
      B(I);
    }
  }, d = (c) => {
    try {
      k(l.throw(c));
    } catch (I) {
      B(I);
    }
  }, k = (c) => c.done ? m(c.value) : Promise.resolve(c.value).then(h, d);
  k((l = l.apply(E, v)).next());
});
import { defineComponent as Ae, ref as Ne, watch as Qe, resolveComponent as L, openBlock as t, createElementBlock as f, unref as e, createBlock as a, createCommentVNode as p, withDirectives as q, createVNode as u, vShow as A, normalizeClass as Je, withCtx as i, createElementVNode as b, toDisplayString as y, Fragment as N, renderList as ze, createTextVNode as D, mergeProps as $e, toHandlers as Ve, resolveDynamicComponent as je, normalizeProps as Ke, guardReactiveProps as Ue } from "vue";
import { BasicTable as We, TableAction as Ge } from "/@/components/Table";
import { useMessage as Xe } from "/@/hooks/web/useMessage";
import Ye from "./OnlineCustomModal-c8b1e780.mjs";
import Ze from "./OnlineTabAutoModal-c9ca3c24.mjs";
import Q from "./OnlineTabDetailModal-fd4c9a72.mjs";
import eo from "/@/components/Form/src/jeecg/components/JImportModal.vue";
import { u as oo, a as to } from "./useListButton-98908683.mjs";
import { u as no, a as ro, O as io, g as lo } from "./useExtendComponent-bb98e568.mjs";
import ao from "./OnlineQueryForm-9248341f.mjs";
import so from "./SuperQuery-46032e66.mjs";
import { u as co } from "./useOnlinePopEvent-687070b7.mjs";
import { T as po } from "./constant-fa63bd66.mjs";
import "/@/components/Modal";
import "/@/utils/http/axios";
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
import "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
import "./OnlineTabForm-1940e88b.mjs";
import "/@/components/jeecg/comment/CommentPanel.vue";
import "./OnlineTabFormDetail-2aa67564.mjs";
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
const uo = { class: "p-2" }, mo = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, go = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, fo = ["src", "onClick"], yo = ["innerHTML", "onClick"], Co = ["innerHTML"], _o = ["title"], qt = /* @__PURE__ */ Ae({
  __name: "OnlCgformTabList",
  setup(E) {
    const { createMessage: v } = Xe(), {
      ID: l,
      onlineTableContext: m,
      onlineQueryFormOuter: B,
      loading: h,
      reload: d,
      dataSource: k,
      pagination: c,
      handleSpecialConfig: I,
      getColumnList: J,
      handleChangeInTable: z,
      loadData: $,
      superQueryButtonRef: R,
      superQueryStatus: V,
      handleSuperQuery: j,
      onlineExtConfigJson: F,
      handleFormConfig: K,
      registerCustomModal: U,
      tableReloading: M
    } = oo();
    if (!l.value)
      throw v.warning("地址错误, 配置ID不存在!"), new Error("地址错误, 配置ID不存在!");
    let { initCgEnhanceJs: W } = no(m);
    const {
      buttonSwitch: _,
      cgBIBtnMap: r,
      getQueryButtonCfg: G,
      getResetButtonCfg: X,
      getFormConfirmButtonCfg: Y,
      cgTopButtonList: O,
      importUrl: Z,
      registerModal: ee,
      handleAdd: oe,
      handleBatchDelete: te,
      registerImportModal: ne,
      onImportExcel: re,
      onExportExcel: ie,
      cgButtonJsHandler: le,
      cgButtonActionHandler: ae,
      getDropDownActions: se,
      getActions: ce,
      initButtonList: pe,
      initButtonSwitch: ue,
      registerDetailModal: me,
      registerBpmModal: bo
    } = to(m, F), x = Ne(!1);
    function de() {
      return T(this, null, function* () {
        try {
          x.value = !0, yield ie();
        } finally {
          setTimeout(() => x.value = !1, 1500);
        }
      });
    }
    const {
      columns: ge,
      actionColumn: fe,
      selectedKeys: ye,
      rowSelection: Ce,
      enableScrollBar: _e,
      tableScroll: be,
      downloadRowFile: he,
      getImgView: ke,
      getPcaText: P,
      getFormatDate: Ie,
      handleColumnResult: Se,
      hrefComponent: w,
      viewOnlineCellImage: Be,
      hrefMainTableId: we,
      registerOnlineHrefModal: Te,
      registerPopModal: ve,
      openPopModal: Re,
      onlinePopModalRef: Me,
      popTableId: H,
      handleClickFieldHref: Oe
    } = ro(m, F);
    Qe(
      l,
      () => {
        xe();
      },
      { immediate: !0 }
    );
    function xe() {
      return T(this, null, function* () {
        h.value = !0;
        let n = yield J(po);
        Le(n), yield $(), h.value = !1, m.execButtonEnhance("setup");
      });
    }
    function Le(n) {
      let g = W(n.enhanceJs);
      m.EnhanceJS = g, pe(n.cgButtonList), ue(n.hideColumns), Se(n), I(n);
    }
    function De(n) {
      m.queryParam = n, d({ mode: "search" });
    }
    function Ee(n) {
      return T(this, null, function* () {
        yield lo(R), R.value.init(n);
      });
    }
    function Fe(n) {
      H.value = n.id;
      let g = {
        title: n.describe
      };
      n.record && n.record.id && (g.record = n.record, g.isUpdate = !0), Re(!0, g);
    }
    return co(Fe), (n, g) => {
      const Pe = L("a-skeleton"), C = L("a-button"), He = L("a-modal");
      return t(), f("div", uo, [
        e(M) ? (t(), a(Pe, {
          key: 0,
          active: ""
        })) : p("", !0),
        q(u(ao, {
          ref_key: "onlineQueryFormOuter",
          ref: B,
          id: e(l),
          queryBtnCfg: e(G),
          resetBtnCfg: e(X),
          onSearch: De,
          onLoaded: Ee
        }, null, 8, ["id", "queryBtnCfg", "resetBtnCfg"]), [
          [A, !e(M)]
        ]),
        e(M) ? p("", !0) : (t(), a(e(We), {
          key: 1,
          ref: "onlineTable",
          rowKey: "jeecg_row_key",
          canResize: !0,
          bordered: !0,
          showIndexColumn: !1,
          loading: e(h),
          columns: e(ge),
          dataSource: e(k),
          pagination: e(c),
          rowSelection: e(Ce),
          actionColumn: e(fe),
          showTableSetting: !0,
          clickToRowSelect: !1,
          scroll: e(be),
          onTableRedo: e(d),
          class: Je({ "j-table-force-nowrap": e(_e) }),
          onChange: e(z)
        }, {
          tableTitle: i(() => [
            e(_).add && e(r).add.enabled ? (t(), a(C, {
              key: 0,
              type: "primary",
              preIcon: e(r).add.buttonIcon,
              onClick: e(oe)
            }, {
              default: i(() => [
                b("span", null, y(e(r).add.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])) : p("", !0),
            e(_).import && e(r).import.enabled ? (t(), a(C, {
              key: 1,
              type: "primary",
              preIcon: e(r).import.buttonIcon,
              onClick: e(re)
            }, {
              default: i(() => [
                b("span", null, y(e(r).import.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])) : p("", !0),
            e(_).export && e(r).export.enabled ? (t(), a(C, {
              key: 2,
              type: "primary",
              preIcon: e(r).export.buttonIcon,
              loading: x.value,
              onClick: de
            }, {
              default: i(() => [
                b("span", null, y(e(r).export.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "loading"])) : p("", !0),
            e(O) && e(O).length > 0 ? (t(!0), f(N, { key: 3 }, ze(e(O), (o, s) => (t(), f(N, null, [
              o.optType == "js" ? (t(), a(C, {
                key: "cgbtn" + s,
                onClick: (S) => e(le)(o.buttonCode),
                type: "primary",
                preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
              }, {
                default: i(() => [
                  D(y(o.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"])) : o.optType == "action" ? (t(), a(C, {
                key: "cgbtn" + s,
                onClick: (S) => e(ae)(o.buttonCode),
                type: "primary",
                preIcon: o.buttonIcon ? "ant-design:" + o.buttonIcon : ""
              }, {
                default: i(() => [
                  D(y(o.buttonName), 1)
                ]),
                _: 2
              }, 1032, ["onClick", "preIcon"])) : p("", !0)
            ], 64))), 256)) : p("", !0),
            e(_).batch_delete && e(r).batch_delete.enabled ? q((t(), a(C, {
              key: 4,
              preIcon: e(r).batch_delete.buttonIcon,
              onClick: e(te)
            }, {
              default: i(() => [
                b("span", null, y(e(r).batch_delete.buttonName), 1)
              ]),
              _: 1
            }, 8, ["preIcon", "onClick"])), [
              [A, e(ye).length > 0]
            ]) : p("", !0),
            e(_).super_query && e(r).super_query.enabled ? (t(), a(so, {
              key: 5,
              ref_key: "superQueryButtonRef",
              ref: R,
              online: "",
              status: e(V),
              queryBtnCfg: e(r).super_query,
              onSearch: e(j)
            }, null, 8, ["status", "queryBtnCfg", "onSearch"])) : p("", !0)
          ]),
          fileSlot: i(({ text: o, record: s, column: S }) => [
            o ? (t(), a(C, {
              key: 1,
              ghost: !0,
              type: "primary",
              preIcon: "ant-design:download",
              size: "small",
              onClick: (qe) => e(he)(o, s, S, e(l))
            }, {
              default: i(() => g[0] || (g[0] = [
                D(" 下载 ")
              ])),
              _: 2
            }, 1032, ["onClick"])) : (t(), f("span", mo, "无文件"))
          ]),
          imgSlot: i(({ text: o }) => [
            o ? (t(), f("img", {
              key: 1,
              src: e(ke)(o),
              alt: "图片不存在",
              class: "online-cell-image",
              onClick: (s) => e(Be)(o)
            }, null, 8, fo)) : (t(), f("span", go, "无图片"))
          ]),
          htmlSlot: i(({ text: o, column: s, record: S }) => [
            s.fieldHref ? (t(), f("a", {
              key: 0,
              innerHTML: o,
              onClick: (qe) => e(Oe)(s.fieldHref, S)
            }, null, 8, yo)) : (t(), f("div", {
              key: 1,
              innerHTML: o
            }, null, 8, Co))
          ]),
          pcaSlot: i(({ text: o }) => [
            b("div", {
              title: e(P)(o)
            }, y(e(P)(o)), 9, _o)
          ]),
          dateSlot: i(({ text: o, column: s }) => [
            b("span", null, y(e(Ie)(o, s)), 1)
          ]),
          action: i(({ record: o }) => [
            u(e(Ge), {
              actions: e(ce)(o),
              dropDownActions: e(se)(o)
            }, null, 8, ["actions", "dropDownActions"])
          ]),
          _: 1
        }, 8, ["loading", "columns", "dataSource", "pagination", "rowSelection", "actionColumn", "scroll", "onTableRedo", "class", "onChange"])),
        u(Ze, {
          onRegister: e(ee),
          id: e(l),
          cgBIBtnMap: e(r),
          buttonSwitch: e(_),
          confirmBtnCfg: e(Y),
          onSuccess: e(d),
          onFormConfig: e(K)
        }, null, 8, ["onRegister", "id", "cgBIBtnMap", "buttonSwitch", "confirmBtnCfg", "onSuccess", "onFormConfig"]),
        u(Q, {
          id: e(l),
          onRegister: e(me)
        }, null, 8, ["id", "onRegister"]),
        u(eo, {
          onRegister: e(ne),
          url: e(Z)(),
          onOk: e(d),
          online: ""
        }, null, 8, ["onRegister", "url", "onOk"]),
        u(He, $e(e(w).model, Ve(e(w).on)), {
          default: i(() => [
            (t(), a(je(e(w).is), Ke(Ue(e(w).params)), null, 16))
          ]),
          _: 1
        }, 16),
        u(Ye, {
          onRegister: e(U),
          onSuccess: e(d)
        }, null, 8, ["onRegister", "onSuccess"]),
        u(Q, {
          id: e(we),
          onRegister: e(Te),
          defaultFullscreen: !1
        }, null, 8, ["id", "onRegister"]),
        u(io, {
          ref_key: "onlinePopModalRef",
          ref: Me,
          id: e(H),
          onRegister: e(ve),
          onSuccess: e(d),
          request: "",
          topTip: ""
        }, null, 8, ["id", "onRegister", "onSuccess"])
      ]);
    };
  }
});
export {
  qt as default
};
