import { defineComponent as f, ref as b, resolveComponent as g, openBlock as h, createBlock as y, mergeProps as $ } from "vue";
import { JVxeTypes as o } from "/@/components/jeecg/JVxeTable/types";
import { u as T } from "./useTableSync-075826a1.mjs";
import { _ as v } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "lodash-es";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const x = f({
  name: "IndexTable",
  components: {},
  props: {
    actionButton: {
      type: Boolean,
      default: !0,
      required: !1
    }
  },
  setup() {
    const e = b([
      {
        title: "索引名称",
        key: "indexName",
        width: 330,
        type: o.input,
        defaultValue: "",
        placeholder: "请输入${title}",
        validateRules: [
          { required: !0, message: "${title}不能为空" },
          // update-begin--author:liaozhiyang---date:20240807---for：【TV360X-1974】online索引名称增加校验
          {
            pattern: /^[a-zA-Z][a-zA-Z0-9_-]*$/,
            message: "命名规则：只能由字母、数字、下划线组成；必须以字母开头"
          }
          // update-end--author:liaozhiyang---date:20240807---for：【TV360X-1974】online索引名称增加校验
        ]
      },
      {
        title: "索引栏位",
        key: "indexField",
        width: 330,
        type: o.selectMultiple,
        options: [],
        defaultValue: "",
        placeholder: "请选择${title}",
        validateRules: [{ required: !0, message: "请选择${title}" }]
      },
      {
        title: "索引类型",
        key: "indexType",
        width: 330,
        type: o.select,
        options: [
          { title: "normal", value: "normal" },
          { title: "unique", value: "unique" }
        ],
        defaultValue: "normal",
        placeholder: "请选择${title}",
        validateRules: [{ required: !0, message: "请选择${title}" }]
      }
    ]), l = T(e), { tableRef: r, loading: i, dataSource: n, tableHeight: u, tableProps: t, setDataSource: d, validateData: p } = l;
    function m(c) {
      let s = [];
      c.value.tableRef.getTableData().forEach((a) => {
        a.dbFieldName && s.push({
          title: a.dbFieldTxt,
          value: a.dbFieldName
        });
      }), e.value[1].options = s;
    }
    return { tableRef: r, loading: i, dataSource: n, columns: e, tableHeight: u, tableProps: t, syncTable: m, setDataSource: d, validateData: p };
  }
});
function S(e, l, r, i, n, u) {
  const t = g("JVxeTable");
  return h(), y(t, $({
    ref: "tableRef",
    rowNumber: "",
    rowSelection: "",
    dragSort: "",
    keyboardEdit: "",
    sortKey: "orderNum",
    maxHeight: e.tableHeight.normal,
    loading: e.loading,
    columns: e.columns,
    dataSource: e.dataSource,
    toolbar: e.actionButton
  }, e.tableProps), null, 16, ["maxHeight", "loading", "columns", "dataSource", "toolbar"]);
}
const z = /* @__PURE__ */ v(x, [["render", S]]);
export {
  z as default
};
