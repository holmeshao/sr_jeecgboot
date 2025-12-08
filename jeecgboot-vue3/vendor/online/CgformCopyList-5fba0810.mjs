import { defineComponent as B, provide as _, watch as k, resolveComponent as e, openBlock as M, createElementBlock as S, Fragment as v, createElementVNode as I, normalizeClass as q, createVNode as t, withCtx as i, createTextVNode as m, createCommentVNode as y } from "vue";
import { BasicTable as L, TableAction as N } from "/@/components/Table";
import { C as V } from "./CgformModal-c4a4e0c2.mjs";
import F from "./DbToOnlineModal-f28ff0a3.mjs";
import O from "./CustomButtonList-c453b654.mjs";
import $ from "./EnhanceJsModal-dc4f9ade.mjs";
import P from "./EnhanceJavaModal-d5a93f2a.mjs";
import z from "./EnhanceSqlModal-984f045d.mjs";
import Q from "./AuthManagerDrawer-32556109.mjs";
import Y from "./AuthSetterModal-364f1f67.mjs";
import Z from "./CgformAddressModal-1131274c.mjs";  // [Fix] 添加缺失的 CgformAddressModal
import { C as j, u as G } from "./useCgformList-f3cb9156.mjs";
import { _ as H } from "./index-9e1e1e53.mjs";
import "/@/components/Icon";
import "/@/components/Modal";
import "/@/components/Form/index";
import "/@/hooks/web/useMessage";
import "./useSchemas-b074f3a1.mjs";
import "ant-design-vue";
import "@ant-design/icons-vue";
import "/@/utils/common/compUtils";
import "/@/hooks/web/usePermission";
import "/@/utils/helper/validator";
import "./DBAttributeTable-1a45c7b7.mjs";
import "/@/components/jeecg/JVxeTable/types";
import "./useTableSync-075826a1.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "lodash-es";
import "./PageAttributeTable-66e7b485.mjs";
import "./LinkTableConfigModal-7eeb3e58.mjs";
import "/@/utils/http/axios";
import "./LinkTableFieldConfigModal-b078fcef.mjs";
import "./FieldExtendJsonModal-bf04d70e.mjs";
import "./SetSwitchOptions-f914bc17.mjs";
import "/@/utils/is";
import "./constant-fa63bd66.mjs";
import "./CheckDictTable-8a938e3a.mjs";
import "/@/components/jeecg/JPrompt";
import "/@/hooks/web/useDesign";
import "./ForeignKeyTable-92decaea.mjs";
import "./IndexTable-2ded2014.mjs";
import "./QueryTable-65d3f54f.mjs";
import "./ExtendConfigModal-7d70f362.mjs";
import "/@/components/Form";
import "./useOnlineTest-e4bd8be3.mjs";
import "/@/utils";
import "./useExtendComponent-bb98e568.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/hooks/system/useListPage";
import "vue-router";
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
import "./utils-9fce7606.mjs";
import "./BuiltInButtonList.vue_vue_type_script_setup_true_lang-07d0b7d0.mjs";
import "./EnhanceJsHistory-8ddb0657.mjs";
import "/@/utils/dateUtil";
import "/@/store";
import "pinia";
import "/@/utils/cache";
import "./enhance.api-138e6826.mjs";
import "./enhance.data-6601ff44.mjs";
import "/@/components/Drawer";
import "./AuthFieldConfig-f1e224cc.mjs";
import "./auth.api-53df4c33.mjs";
import "./auth.data-626c5083.mjs";
import "/@/utils/index";
import "./AuthButtonConfig-d5bffca0.mjs";
import "./AuthDataConfig-d3b7afa4.mjs";
import "./LeftRole-b0e0b496.mjs";
import "./LeftDepart-52cb6743.mjs";
import "./LeftUser-dd4b10e2.mjs";
import "./AuthFieldTree-5cc0da05.mjs";
import "./AuthButtonTree-b0bd6c40.mjs";
import "./AuthDataTree-f14a98d9.mjs";
import "./cgformState-d9f8ec42.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
const K = B({
  name: "CgformCopyList",
  components: {
    BasicTable: L,
    TableAction: N,
    CgformModal: V,
    DbToOnlineModal: F,
    CustomButtonList: O,
    EnhanceJsModal: $,
    EnhanceJavaModal: P,
    EnhanceSqlModal: z,
    AuthManagerDrawer: Q,
    AuthSetterModal: Y,
    CgformAddressModal: Z  // [Fix] 注册 CgformAddressModal
  },
  setup() {
    const o = j.copy;
    _("cgformPageType", o);
    const {
      router: r,
      pageContext: E,
      getTableAction: A,
      getDropDownAction: b,
      onShowCustomButton: w,
      onShowEnhanceJs: a,
      onShowEnhanceSql: p,
      onShowEnhanceJava: l,
      registerCustomButtonModal: s,
      registerEnhanceJsModal: c,
      registerEnhanceSqlModal: u,
      registerEnhanceJavaModal: g,
      registerAuthManagerDrawer: d,
      registerAuthSetterModal: h,
      registerCgformModal: f,
      registerDbToOnlineModal: C,
      registerAddressModal: x  // [Fix] 提取 registerAddressModal
    } = G({
      pageType: o,
      designScope: "online-cgform-list",
      columns: [
        { title: "视图表名", dataIndex: "tableName" },
        { title: "视图表描述", dataIndex: "tableTxt" },
        { title: "原表版本", dataIndex: "copyVersion" },
        { title: "视图版本", dataIndex: "tableVersion" }
      ],
      formSchemas: [{ label: "表名", field: "tableName", component: "JInput" }]
    }), { prefixCls: n, tableContext: R } = E, [D, { reload: T }, { rowSelection: J }] = R;
    return k(r.currentRoute, () => T()), {
      prefixCls: n,
      reload: T,
      rowSelection: J,
      getTableAction: A,
      getDropDownAction: b,
      onShowCustomButton: w,
      onShowEnhanceJs: a,
      onShowEnhanceSql: p,
      onShowEnhanceJava: l,
      registerCustomButtonModal: s,
      registerEnhanceJsModal: c,
      registerEnhanceSqlModal: u,
      registerEnhanceJavaModal: g,
      registerAuthManagerDrawer: d,
      registerAuthSetterModal: h,
      registerTable: D,
      registerCgformModal: f,
      registerDbToOnlineModal: C,
      registerAddressModal: x  // [Fix] 暴露 registerAddressModal
    };
  }
}), U = {
  key: 0,
  style: { color: "limegreen" }
}, W = {
  key: 1,
  style: { color: "red" }
};
function X(o, r, E, A, b, w) {
  const a = e("a-button"), p = e("TableAction"), l = e("BasicTable"), s = e("CgformModal"), c = e("EnhanceJsModal"), u = e("EnhanceJavaModal"), g = e("EnhanceSqlModal"), d = e("DbToOnlineModal"), h = e("CustomButtonList"), f = e("AuthManagerDrawer"), C = e("AuthSetterModal"), x = e("CgformAddressModal");  // [Fix]
  return M(), S(v, null, [
    I("div", {
      class: q(o.prefixCls)
    }, [
      t(l, {
        onRegister: o.registerTable,
        rowSelection: o.rowSelection
      }, {
        tableTitle: i(() => [
          t(a, {
            onClick: o.onShowCustomButton,
            type: "primary",
            preIcon: "ant-design:highlight"
          }, {
            default: i(() => r[0] || (r[0] = [
              m("自定义按钮")
            ])),
            _: 1
          }, 8, ["onClick"]),
          t(a, {
            onClick: o.onShowEnhanceJs,
            type: "primary",
            preIcon: "ant-design:strikethrough"
          }, {
            default: i(() => r[1] || (r[1] = [
              m("JS增强")
            ])),
            _: 1
          }, 8, ["onClick"]),
          t(a, {
            onClick: o.onShowEnhanceSql,
            type: "primary",
            preIcon: "ant-design:filter"
          }, {
            default: i(() => r[2] || (r[2] = [
              m("SQL增强")
            ])),
            _: 1
          }, 8, ["onClick"]),
          t(a, {
            onClick: o.onShowEnhanceJava,
            type: "primary",
            preIcon: "ant-design:tool"
          }, {
            default: i(() => r[3] || (r[3] = [
              m("Java增强")
            ])),
            _: 1
          }, 8, ["onClick"])
        ]),
        dbSync: i(({ text: n }) => [
          n === "Y" ? (M(), S("span", U, "已同步")) : y("", !0),
          n === "N" ? (M(), S("span", W, "未同步")) : y("", !0)
        ]),
        action: i(({ record: n }) => [
          t(p, {
            actions: o.getTableAction(n),
            dropDownActions: o.getDropDownAction(n)
          }, null, 8, ["actions", "dropDownActions"])
        ]),
        _: 1
      }, 8, ["onRegister", "rowSelection"])
    ], 2),
    t(s, {
      onRegister: o.registerCgformModal,
      actionButton: !1,
      onSuccess: o.reload
    }, null, 8, ["onRegister", "onSuccess"]),
    t(c, { onRegister: o.registerEnhanceJsModal }, null, 8, ["onRegister"]),
    t(u, { onRegister: o.registerEnhanceJavaModal }, null, 8, ["onRegister"]),
    t(g, { onRegister: o.registerEnhanceSqlModal }, null, 8, ["onRegister"]),
    t(d, {
      onRegister: o.registerDbToOnlineModal,
      onSuccess: o.reload
    }, null, 8, ["onRegister", "onSuccess"]),
    t(h, { onRegister: o.registerCustomButtonModal }, null, 8, ["onRegister"]),
    t(f, { onRegister: o.registerAuthManagerDrawer }, null, 8, ["onRegister"]),
    t(C, { onRegister: o.registerAuthSetterModal }, null, 8, ["onRegister"]),
    t(x, { onRegister: o.registerAddressModal }, null, 8, ["onRegister"])  // [Fix] 渲染 CgformAddressModal
  ], 64);
}
const Ft = /* @__PURE__ */ H(K, [["render", X]]);
export {
  Ft as default
};
