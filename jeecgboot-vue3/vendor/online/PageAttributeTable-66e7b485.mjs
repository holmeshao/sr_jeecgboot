var Z = Object.defineProperty, ee = Object.defineProperties;
var te = Object.getOwnPropertyDescriptors;
var J = Object.getOwnPropertySymbols;
var le = Object.prototype.hasOwnProperty, ie = Object.prototype.propertyIsEnumerable;
var N = (i, u, s) => u in i ? Z(i, u, { enumerable: !0, configurable: !0, writable: !0, value: s }) : i[u] = s, E = (i, u) => {
  for (var s in u || (u = {}))
    le.call(u, s) && N(i, s, u[s]);
  if (J)
    for (var s of J(u))
      ie.call(u, s) && N(i, s, u[s]);
  return i;
}, I = (i, u) => ee(i, te(u));
import { defineComponent as ae, ref as oe, resolveComponent as g, openBlock as O, createElementBlock as se, Fragment as de, createVNode as T, mergeProps as ne, withCtx as P, createBlock as ue, createTextVNode as re, createCommentVNode as fe } from "vue";
import { JVxeTypes as r } from "/@/components/jeecg/JVxeTable/types";
import { u as ce } from "./useTableSync-075826a1.mjs";
import me from "./LinkTableConfigModal-7eeb3e58.mjs";
import be from "./LinkTableFieldConfigModal-b078fcef.mjs";
import pe from "./FieldExtendJsonModal-bf04d70e.mjs";
import { useModal as S } from "/@/components/Modal";
import { _ as ye } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "lodash-es";
import "/@/components/Form/index";
import "/@/utils/http/axios";
import "/@/hooks/web/useMessage";
import "./SetSwitchOptions-f914bc17.mjs";
import "/@/utils/is";
import "./constant-fa63bd66.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const H = [
  { title: "文本框", value: "text" },
  { title: "密码", value: "password" },
  { title: "下拉框", value: "list" },
  { title: "单选框", value: "radio" },
  { title: "多选框", value: "checkbox" },
  { title: "开关", value: "switch" },
  { title: "日期(年月日)", value: "date" },
  { title: "日期(年月日时分秒)", value: "datetime" },
  { title: "时间(HH:mm:ss)", value: "time" },
  { title: "文件", value: "file" },
  { title: "图片", value: "image" },
  { title: "多行文本", value: "textarea" },
  { title: "富文本", value: "umeditor" },
  { title: "MarkDown", value: "markdown" },
  { title: "用户选择", value: "sel_user" },
  { title: "部门选择", value: "sel_depart" },
  { title: "关联记录", value: "link_table" },
  { title: "他表字段", value: "link_table_field" },
  { title: "省市区组件", value: "pca" },
  { title: "Popup弹框", value: "popup" },
  // update-begin--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
  { title: "Popup字典", value: "popup_dict" },
  // update-end--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
  { title: "下拉多选框", value: "list_multi" },
  { title: "下拉搜索框", value: "sel_search" },
  { title: "分类字典树", value: "cat_tree" },
  { title: "自定义树控件", value: "sel_tree" },
  { title: "联动组件", value: "link_down" }
], ge = [
  { title: "文本框", value: "text" },
  { title: "单选框", value: "radio" },
  { title: "开关", value: "switch" },
  { title: "日期(yyyy-MM-dd)", value: "date" },
  { title: "日期（yyyy-MM-dd HH:mm:ss）", value: "datetime" },
  { title: "时间（HH:mm:ss）", value: "time" },
  { title: "文件", value: "file" },
  { title: "图片", value: "image" },
  { title: "下拉框", value: "list" },
  { title: "下拉多选框", value: "list_multi" },
  { title: "下拉搜索框", value: "sel_search" },
  { title: "popup弹出框", value: "popup" },
  // update-begin--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
  // { title: 'Popup字典', value: 'popup_dict' },
  // update-end--author:liaozhiyang---date:20240130---for：【QQYUN-7961】popupDict字典
  { title: "部门选择", value: "sel_depart" },
  { title: "用户选择", value: "sel_user" },
  { title: "省市区组件", value: "pca" },
  { title: "多行文本", value: "textarea" }
], he = ae({
  name: "PageAttributeTable",
  components: {
    LinkTableConfigModal: me,
    LinkTableFieldConfigModal: be,
    FieldExtendJsonModal: pe
  },
  setup() {
    const i = oe([
      { title: "字段名称", key: "dbFieldName", width: 100 },
      { title: "字段备注", key: "dbFieldTxt", width: 150 },
      {
        title: "表单显示",
        key: "isShowForm",
        width: 80,
        type: r.checkbox,
        align: "center",
        customValue: ["1", "0"],
        defaultChecked: !0
      },
      {
        title: "列表显示",
        key: "isShowList",
        width: 80,
        type: r.checkbox,
        align: "center",
        customValue: ["1", "0"],
        defaultChecked: !0
      },
      {
        title: "是否排序",
        key: "sortFlag",
        width: 80,
        type: r.checkbox,
        align: "center",
        customValue: ["1", "0"],
        defaultChecked: !1,
        // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-291】没勾选同步数据库禁用排序功能
        props: {
          isDisabledCell({ row: e, column: t }) {
            let { dbTable: l } = c;
            const a = l.value.tableRef.getTableData({ rowIds: [e.id] })[0];
            return (a == null ? void 0 : a.dbIsPersist) == "0";
          }
        }
        // update-end--author:liaozhiyang---date:20240528---for：【TV360X-291】没勾选同步数据库禁用排序功能
      },
      {
        title: "是否只读",
        key: "isReadOnly",
        width: 80,
        type: r.checkbox,
        align: "center",
        customValue: ["1", "0"],
        defaultChecked: !1
      },
      {
        title: "控件类型",
        key: "fieldShowType",
        width: 170,
        type: r.select,
        options: H,
        defaultValue: "text",
        placeholder: "请选择${title}",
        validateRules: [{ required: !0, message: "请选择${title}" }, { handler: V }]
      },
      {
        title: "控件长度",
        key: "fieldLength",
        width: 100,
        titleHelp: { message: "此长度只对子表列字段宽度有效！" },
        type: r.inputNumber,
        defaultValue: 120,
        placeholder: "请输入控件长度"
      },
      {
        title: "是否查询",
        key: "isQuery",
        width: 80,
        type: r.checkbox,
        align: "center",
        customValue: ["1", "0"],
        defaultChecked: !1,
        // update-begin--author:liaozhiyang---date:20240528---for：【TV360X-423】未勾选同步数据库禁用查询
        props: {
          isDisabledCell({ row: e, column: t }) {
            let { dbTable: l } = c;
            const a = l.value.tableRef.getTableData({ rowIds: [e.id] })[0];
            return (a == null ? void 0 : a.dbIsPersist) == "0";
          }
        }
        // update-end--author:liaozhiyang---date:20240528---for：【TV360X-423】未勾选同步数据库禁用查询
      },
      {
        title: "查询类型",
        key: "queryMode",
        width: 120,
        type: r.select,
        options: [
          { title: "普通查询", value: "single" },
          { title: "模糊查询", value: "like" },
          { title: "范围查询", value: "group" }
        ],
        defaultValue: "single",
        placeholder: "请选择${title}",
        validateRules: [{ handler: U }],
        props: {
          isDisabledCell({ row: e, column: t }) {
          }
        }
      },
      {
        title: "控件默认值",
        key: "fieldDefaultValue",
        width: 140,
        type: r.textarea,
        props: {
          allowClear: !0
        },
        defaultValue: ""
      },
      {
        title: "定义转换器",
        key: "converter",
        width: 150,
        type: r.input,
        defaultValue: ""
      },
      {
        title: "扩展参数",
        key: "fieldExtendJson",
        width: 120,
        type: r.textarea,
        defaultValue: ""
      },
      {
        title: "更多配置",
        minWidth: 100,
        key: "fieldConfig",
        type: r.slot,
        slotName: "fieldConfig"
      }
    ]), u = ce(i), { tableRef: s, tables: c } = u;
    function V({ cellValue: e, row: t }, l) {
      let { dbTable: a } = c, o = a.value.tableRef.getTableData({ rowIds: [t.id] })[0].dbType;
      e === "time" && o !== "string" ? l(!1, "当控件类型为时间时,数据库属性里的字段类型必须是String！") : e === "date" && o !== "Date" && o !== "Datetime" ? l(!1, "当控件类型为日期时，数据库属性里的字段类型必须是Date或Datetime！") : e === "datetime" && o !== "Datetime" ? l(!1, "当控件类型为datetime时，数据库属性里的字段类型必须是Datetime！") : l(!0);
    }
    function C(e) {
      let t = "text";
      e.dbType === "Datetime" ? t = "datetime" : e.dbType === "Date" && (t = "date"), s.value.setValues([
        {
          rowKey: e.id,
          values: { fieldShowType: t }
        }
      ]);
    }
    function w(e) {
      e.dbIsPersist === "0" && s.value.setValues([
        {
          rowKey: e.id,
          values: { isQuery: "0", sortFlag: "0" }
        }
      ]);
    }
    function k(e) {
      for (let t of i.value)
        if (t.key == "fieldShowType") {
          t.options = e ? ge : H;
          break;
        }
    }
    function F(e) {
      s.value.setValues([
        {
          rowKey: e,
          values: { isQuery: "1" }
        }
      ]);
    }
    const [_, { openModal: x }] = S(), [h, { openModal: M }] = S(), [K, { openModal: L }] = S();
    function $(e) {
      return e.row.dbFieldName != "id";
    }
    function B(e) {
      e.row.fieldShowType.indexOf("link_table") >= 0 ? R(e) : z(e);
    }
    function R(e) {
      let { row: t } = e, { checkTable: l } = c;
      if (l) {
        let a = l.value.tableRef.getTableData({ rowIds: [t.id] })[0];
        if (e.row.fieldShowType == "link_table") {
          let o = A(t, a);
          x(!0, {
            record: o,
            fieldName: t.dbFieldName
          });
        } else if (e.row.fieldShowType == "link_table_field") {
          let o = Q(t, a);
          M(!0, o);
        }
      }
    }
    function Q(e, t) {
      let a = s.value.getTableData().filter((b) => b.fieldShowType == "link_table"), o = {};
      if (a && a.length > 0) {
        let b = c.checkTable.value.tableRef.getTableData();
        for (let v of a) {
          let y = b.filter((p) => p.dbFieldName == v.dbFieldName);
          if (y && y.length > 0) {
            let p = y[0];
            o[p.dbFieldName] = {
              title: v.dbFieldTxt,
              table: p.dictTable,
              fields: p.dictText
            };
          }
        }
      }
      const { dictTable: m, dictText: f } = t, { id: n, dbFieldTxt: d } = e;
      return {
        record: {
          rowKey: n,
          dbFieldTxt: d,
          dictText: f,
          dictTable: m
        },
        tableAndFieldsMap: o
      };
    }
    function A(e, t) {
      const { id: l, dbFieldTxt: a, fieldExtendJson: o } = e, { dictTable: m, dictText: f } = t;
      let n = {
        rowKey: l,
        dbFieldTxt: a,
        dictTable: m
      };
      if (f) {
        let d = f.split(",");
        n.titleField = d[0], d.length > 1 && (n.otherFields = f.substring(f.indexOf(",") + 1));
      } else
        n.titleField = "", n.otherFields = "";
      if (o)
        try {
          let d = JSON.parse(o);
          d.multiSelect ? n.multiSelect = d.multiSelect : n.multiSelect = !1, d.isListReadOnly ? n.isListReadOnly = d.isListReadOnly : n.isListReadOnly = !1, d.showType ? n.showType = d.showType : n.showType = "card", d.imageField ? n.imageField = d.imageField : n.imageField = "";
        } catch (d) {
        }
      return n;
    }
    function q(e) {
      const { multiSelect: t, showType: l, imageField: a, fieldName: o, isListReadOnly: m } = e;
      let f = { showType: l, multiSelect: t, imageField: a, isListReadOnly: m }, n = [{ rowKey: e.rowKey, values: { fieldExtendJson: JSON.stringify(f), dbFieldTxt: e.dbFieldTxt } }];
      s.value.setValues(n);
      let { checkTable: d, dbTable: D } = c;
      if (D) {
        let b = [{ rowKey: e.rowKey, values: { dbFieldTxt: e.dbFieldTxt } }];
        D.value.tableRef.setValues(b);
      }
      if (d) {
        let b = e.titleField;
        e.otherFields && (b += "," + e.otherFields);
        const { dictTable: v, dictField: y } = e;
        let p = {
          dictTable: v,
          dictField: y,
          dictText: b,
          dbFieldName: o
        }, Y = [{ rowKey: e.rowKey, values: p }];
        d.value.tableRef.setValues(Y);
      }
    }
    function j(e) {
      const { dbFieldTxt: t, dictTable: l, dictText: a, rowKey: o } = e;
      let m = [{ rowKey: o, values: { dbFieldTxt: t } }];
      s.value.setValues(m);
      let { checkTable: f, dbTable: n } = c;
      if (n) {
        let d = [{ rowKey: o, values: { dbFieldTxt: t, dbIsPersist: "0" } }];
        n.value.tableRef.setValues(d);
      }
      if (f) {
        let d = [{ rowKey: o, values: { dictTable: l, dictText: a } }];
        f.value.tableRef.setValues(d);
      }
    }
    function z(e) {
      let t = e.row.fieldExtendJson || "", l = e.rowId, a = e.row.fieldShowType || "", o = e.row.sortFlag || "0";
      const m = e.row.dbType;
      L(!0, {
        jsonStr: t,
        fieldShowType: a,
        sortFlag: o,
        id: l,
        dbType: m
      });
    }
    function W(e, t) {
      let l;
      e && Object.keys(e).length > 0 ? l = [{ rowKey: t, values: { fieldExtendJson: JSON.stringify(e) } }] : l = [{ rowKey: t, values: { fieldExtendJson: "" } }], s.value.setValues(l);
    }
    const G = (e) => {
    };
    function U({ cellValue: e, row: t }, l) {
      let { dbTable: a } = c;
      const o = a.value.tableRef.getTableData({ rowIds: [t.id] })[0].dbType;
      e === "group" ? ["double", "int", "BigDecimal", "Date", "Datetime"].includes(o) || t.fieldShowType === "time" ? l(!0) : l(!1, "范围查询，只支持数据库属里的字段类型为：Integer、Double、BigDecimal及Date、Datetime 或 控件类型是时间(HH:mm:ss)") : e === "like" ? ["string"].includes(o) && t.fieldShowType === "text" ? l(!0) : l(!1, "模糊查询，只支持控件类型是文本框且数据库属里的字段类型是String") : l(!0);
    }
    const X = ({ oldValue: e, row: t }) => {
      t.dbType != "string" && e == "string" && s.value.getTableData({ rowIds: [t.id] })[0].queryMode == "like" && s.value.setValues([
        {
          rowKey: t.id,
          values: { queryMode: "single" }
        }
      ]);
    };
    return I(E({}, u), {
      columns: i,
      enableQuery: F,
      syncFieldShowType: C,
      changePageType: k,
      showConfigButton: $,
      showFieldConfig: R,
      registerExtJsonModal: K,
      handleExtJson: W,
      openConfig: B,
      registerModal: _,
      handleConfigData: q,
      registerFieldModal: h,
      handleFieldConfigData: j,
      syncIsQuery: w,
      handleValueChange: G,
      syncQueryMode: X
    });
  }
});
function ve(i, u, s, c, V, C) {
  const w = g("a-button"), k = g("JVxeTable"), F = g("link-table-config-modal"), _ = g("link-table-field-config-modal"), x = g("FieldExtendJsonModal");
  return O(), se(de, null, [
    T(k, ne({
      ref: "tableRef",
      "row-class-name": "online-config-page",
      rowNumber: "",
      keyboardEdit: "",
      maxHeight: i.tableHeight.noToolbar,
      loading: i.loading,
      columns: i.columns,
      dataSource: i.dataSource,
      disabledRows: { dbFieldName: ["id", "has_child"] },
      onValueChange: i.handleValueChange
    }, i.tableProps), {
      fieldConfig: P((h) => [
        i.showConfigButton(h) ? (O(), ue(w, {
          key: 0,
          type: "primary",
          size: "small",
          ghost: "",
          onClick: (M) => i.openConfig(h)
        }, {
          default: P(() => u[0] || (u[0] = [
            re("更多配置")
          ])),
          _: 2
        }, 1032, ["onClick"])) : fe("", !0)
      ]),
      _: 1
    }, 16, ["maxHeight", "loading", "columns", "dataSource", "onValueChange"]),
    T(F, {
      onRegister: i.registerModal,
      onSuccess: i.handleConfigData
    }, null, 8, ["onRegister", "onSuccess"]),
    T(_, {
      onRegister: i.registerFieldModal,
      onSuccess: i.handleFieldConfigData
    }, null, 8, ["onRegister", "onSuccess"]),
    T(x, {
      onRegister: i.registerExtJsonModal,
      onSuccess: i.handleExtJson
    }, null, 8, ["onRegister", "onSuccess"])
  ], 64);
}
const Be = /* @__PURE__ */ ye(he, [["render", ve], ["__scopeId", "data-v-519eccff"]]);
export {
  Be as default
};
