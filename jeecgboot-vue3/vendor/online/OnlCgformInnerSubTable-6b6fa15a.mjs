var I = (h, g, p) => new Promise((a, n) => {
  var f = (l) => {
    try {
      b(p.next(l));
    } catch (_) {
      n(_);
    }
  }, c = (l) => {
    try {
      b(p.throw(l));
    } catch (_) {
      n(_);
    }
  }, b = (l) => l.done ? a(l.value) : Promise.resolve(l.value).then(f, c);
  b((p = p.apply(h, g)).next());
});
import { watch as re, resolveComponent as M, openBlock as r, createElementBlock as d, Fragment as ae, unref as e, createBlock as S, normalizeClass as se, withCtx as m, createTextVNode as me, createElementVNode as w, toDisplayString as v, createCommentVNode as pe, createVNode as u, mergeProps as ce, toHandlers as de, resolveDynamicComponent as ue, normalizeProps as ge, guardReactiveProps as fe } from "vue";
import { BasicTable as be } from "/@/components/Table";
import { useMessage as _e } from "/@/hooks/web/useMessage";
import Te from "./OnlineCustomModal-c8b1e780.mjs";
import O from "./OnlineDetailModal-5b412bb9.mjs";
import he from "/@/components/Form/src/jeecg/components/JImportModal.vue";
import { u as Ce, a as Se } from "./useListButton-98908683.mjs";
import { u as Re, a as ke, O as ye } from "./useExtendComponent-bb98e568.mjs";
import { u as Ie } from "./useOnlinePopEvent-687070b7.mjs";
import { I as Me } from "./constant-fa63bd66.mjs";
import "/@/components/Table/src/const";
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
import "/@/components/jeecg/comment/CommentPanel.vue";
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
const we = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, ve = {
  key: 0,
  style: { "font-size": "12px", "font-style": "italic" }
}, Oe = ["src", "onClick"], Be = ["innerHTML", "onClick"], Pe = ["innerHTML"], Ee = ["title"], Uo = {
  __name: "OnlCgformInnerSubTable",
  props: ["subTableId", "subTableName", "mTableSelectedRcordId"],
  setup(h) {
    const g = h, { createMessage: p } = _e(), {
      ID: a,
      onlineTableContext: n,
      loading: f,
      reload: c,
      dataSource: b,
      handleSpecialConfig: l,
      getColumnList: _,
      handleChangeInTable: B,
      loadData: P,
      onlineExtConfigJson: R,
      registerCustomModal: E,
      tableReloading: L
    } = Ce({ code: g.subTableId });
    n.isInnerSubTable = !0, n.innerSubTableName = g.subTableName, n.innerSubTableId = a.value, n.mTableSelectedRcordId = g.mTableSelectedRcordId, a.value || p.warning("地址错误, 配置ID不存在!");
    let { initCgEnhanceJs: H } = Re(n);
    const {
      importUrl: N,
      registerImportModal: x,
      initButtonList: D,
      initButtonSwitch: z,
      registerDetailModal: F,
      registerBpmModal: Le
    } = Se(n, R), {
      columns: J,
      enableScrollBar: V,
      tableScroll: $,
      downloadRowFile: A,
      getImgView: K,
      getPcaText: k,
      getFormatDate: j,
      handleColumnResult: U,
      hrefComponent: T,
      viewOnlineCellImage: q,
      hrefMainTableId: G,
      registerOnlineHrefModal: Q,
      registerPopModal: W,
      openPopModal: X,
      onlinePopModalRef: Y,
      popTableId: y,
      handleClickFieldHref: Z
    } = ke(n, R);
    re(
      a,
      () => {
        ee();
      },
      { immediate: !0 }
    );
    function ee() {
      return I(this, null, function* () {
        var i;
        f.value = !0;
        let o = yield _(Me);
        oe(o), (i = o.foreignKeys) != null && i.length && (n.innerSubTableFk = o.foreignKeys[0].field), yield P(), f.value = !1, n.execButtonEnhance("setup");
      });
    }
    function oe(o) {
      let i = H(o.enhanceJs);
      n.EnhanceJS = i, D(o.cgButtonList), z(o.hideColumns), U(o), l(o);
    }
    function te(o) {
      y.value = o.id;
      let i = {
        title: o.describe
      };
      o.record && o.record.id && (i.record = o.record, i.isUpdate = !0), X(!0, i);
    }
    return Ie(te), (o, i) => {
      const ne = M("a-button"), ie = M("a-modal");
      return r(), d(ae, null, [
        e(L) ? pe("", !0) : (r(), S(e(be), {
          key: 0,
          ref: "onlineTable",
          rowKey: "jeecg_row_key",
          canResize: !0,
          bordered: !0,
          showIndexColumn: !1,
          loading: e(f),
          columns: e(J),
          dataSource: e(b),
          pagination: !1,
          showActionColumn: !1,
          showTableSetting: !1,
          clickToRowSelect: !1,
          scroll: e($),
          onTableRedo: e(c),
          class: se({ "j-table-force-nowrap": e(V) }),
          onChange: e(B)
        }, {
          fileSlot: m(({ text: t, record: s, column: C }) => [
            t ? (r(), S(ne, {
              key: 1,
              ghost: !0,
              type: "primary",
              preIcon: "ant-design:download",
              size: "small",
              onClick: (le) => e(A)(t, s, C, e(a))
            }, {
              default: m(() => i[0] || (i[0] = [
                me(" 下载 ")
              ])),
              _: 2
            }, 1032, ["onClick"])) : (r(), d("span", we, "无文件"))
          ]),
          imgSlot: m(({ text: t }) => [
            t ? (r(), d("img", {
              key: 1,
              src: e(K)(t),
              alt: "图片不存在",
              class: "online-cell-image",
              onClick: (s) => e(q)(t)
            }, null, 8, Oe)) : (r(), d("span", ve, "无图片"))
          ]),
          htmlSlot: m(({ text: t, column: s, record: C }) => [
            s.fieldHref ? (r(), d("a", {
              key: 0,
              innerHTML: t,
              onClick: (le) => e(Z)(s.fieldHref, C)
            }, null, 8, Be)) : (r(), d("div", {
              key: 1,
              innerHTML: t
            }, null, 8, Pe))
          ]),
          pcaSlot: m(({ text: t }) => [
            w("div", {
              title: e(k)(t)
            }, v(e(k)(t)), 9, Ee)
          ]),
          dateSlot: m(({ text: t, column: s }) => [
            w("span", null, v(e(j)(t, s)), 1)
          ]),
          _: 1
        }, 8, ["loading", "columns", "dataSource", "scroll", "onTableRedo", "class", "onChange"])),
        u(O, {
          id: e(a),
          onRegister: e(F)
        }, null, 8, ["id", "onRegister"]),
        u(he, {
          onRegister: e(x),
          url: e(N)(),
          onOk: e(c),
          online: ""
        }, null, 8, ["onRegister", "url", "onOk"]),
        u(ie, ce(e(T).model, de(e(T).on)), {
          default: m(() => [
            (r(), S(ue(e(T).is), ge(fe(e(T).params)), null, 16))
          ]),
          _: 1
        }, 16),
        u(Te, {
          onRegister: e(E),
          onSuccess: e(c)
        }, null, 8, ["onRegister", "onSuccess"]),
        u(O, {
          id: e(G),
          onRegister: e(Q),
          defaultFullscreen: !1
        }, null, 8, ["id", "onRegister"]),
        u(ye, {
          ref_key: "onlinePopModalRef",
          ref: Y,
          id: e(y),
          onRegister: e(W),
          onSuccess: e(c),
          request: "",
          topTip: ""
        }, null, 8, ["id", "onRegister", "onSuccess"])
      ], 64);
    };
  }
};
export {
  Uo as default
};
