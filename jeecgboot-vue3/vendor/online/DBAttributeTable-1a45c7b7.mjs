var Y = Object.defineProperty, $ = Object.defineProperties;
var k = Object.getOwnPropertyDescriptors;
var b = Object.getOwnPropertySymbols;
var K = Object.prototype.hasOwnProperty, W = Object.prototype.propertyIsEnumerable;
var c = (e, a, d) => a in e ? Y(e, a, { enumerable: !0, configurable: !0, writable: !0, value: d }) : e[a] = d, D = (e, a) => {
  for (var d in a || (a = {}))
    K.call(a, d) && c(e, d, a[d]);
  if (b)
    for (var d of b(a))
      W.call(a, d) && c(e, d, a[d]);
  return e;
}, S = (e, a) => $(e, k(a));
import { defineComponent as X, getCurrentInstance as Q, ref as f, resolveComponent as q, openBlock as J, createBlock as Z, mergeProps as z } from "vue";
import { JVxeTypes as E } from "/@/components/jeecg/JVxeTable/types";
import { u as j } from "./useTableSync-075826a1.mjs";
import { _ as ee } from "./index-9e1e1e53.mjs";
import "./cgform.data-0ca62d09.mjs";
import "/@/utils/dict";
import "/@/utils/dict/JDictSelectUtil";
import "/@/utils/uuid";
import "lodash-es";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const te = [
  "ADD",
  "ALL",
  "ALTER",
  "ANALYZE",
  "AND",
  "AS",
  "ASC",
  "ASENSITIVE",
  "BEFORE",
  "BETWEEN",
  "BIGINT",
  "BINARY",
  "BLOB",
  "BOTH",
  "BY",
  "CALL",
  "CASCADE",
  "CASE",
  "CHANGE",
  "CHAR",
  "CHARACTER",
  "CHECK",
  "COLLATE",
  "COLUMN",
  "CONDITION",
  "CONNECTION",
  "CONSTRAINT",
  "CONTINUE",
  "CONVERT",
  "CREATE",
  "CROSS",
  "CURRENT_DATE",
  "CURRENT_TIME",
  "CURRENT_TIMESTAMP",
  "CURRENT_USER",
  "CURSOR",
  "DATABASE",
  "DATABASES",
  "DAY_HOUR",
  "DAY_MICROSECOND",
  "DAY_MINUTE",
  "DAY_SECOND",
  "DEC",
  "DECIMAL",
  "DECLARE",
  "DEFAULT",
  "DELAYED",
  "DELETE",
  "DESC",
  "DESCRIBE",
  "DETERMINISTIC",
  "DISTINCT",
  "DISTINCTROW",
  "DIV",
  "DOUBLE",
  "DROP",
  "DUAL",
  "EACH",
  "ELSE",
  "ELSEIF",
  "ENCLOSED",
  "ESCAPED",
  "EXISTS",
  "EXIT",
  "EXPLAIN",
  "FALSE",
  "FETCH",
  "FLOAT",
  "FLOAT4",
  "FLOAT8",
  "FOR",
  "FORCE",
  "FOREIGN",
  "FROM",
  "FULLTEXT",
  "GOTO",
  "GRANT",
  "GROUP",
  "HAVING",
  "HIGH_PRIORITY",
  "HOUR_MICROSECOND",
  "HOUR_MINUTE",
  "HOUR_SECOND",
  "IF",
  "IGNORE",
  "IN",
  "INDEX",
  "INFILE",
  "INNER",
  "INOUT",
  "INSENSITIVE",
  "INSERT",
  "INT",
  "INT1",
  "INT2",
  "INT3",
  "INT4",
  "INT8",
  "INTEGER",
  "INTERVAL",
  "INTO",
  "IS",
  "ITERATE",
  "JOIN",
  "KEY",
  "KEYS",
  "KILL",
  "LABEL",
  "LEADING",
  "LEAVE",
  "LEFT",
  "LIKE",
  "LIMIT",
  "LINEAR",
  "LINES",
  "LOAD",
  "LOCALTIME",
  "LOCALTIMESTAMP",
  "LOCK",
  "LONG",
  "LONGBLOB",
  "LONGTEXT",
  "LOOP",
  "LOW_PRIORITY",
  "MATCH",
  "MEDIUMBLOB",
  "MEDIUMINT",
  "MEDIUMTEXT",
  "MIDDLEINT",
  "MINUTE_MICROSECOND",
  "MINUTE_SECOND",
  "MOD",
  "MODIFIES",
  "NATURAL",
  "NOT",
  "NO_WRITE_TO_BINLOG",
  "NULL",
  "NUMERIC",
  "ON",
  "OPTIMIZE",
  "OPTION",
  "OPTIONALLY",
  "OR",
  "ORDER",
  "OUT",
  "OUTER",
  "OUTFILE",
  "PRECISION",
  "PRIMARY",
  "PROCEDURE",
  "PURGE",
  "RAID0",
  "RANGE",
  "READ",
  "READS",
  "REAL",
  "REFERENCES",
  "REGEXP",
  "RELEASE",
  "RENAME",
  "REPEAT",
  "REPLACE",
  "REQUIRE",
  "RESTRICT",
  "RETURN",
  "REVOKE",
  "RIGHT",
  "RLIKE",
  "SCHEMA",
  "SCHEMAS",
  "SECOND_MICROSECOND",
  "SELECT",
  "SENSITIVE",
  "SEPARATOR",
  "SET",
  "SHOW",
  "SMALLINT",
  "SPATIAL",
  "SPECIFIC",
  "SQL",
  "SQLEXCEPTION",
  "SQLSTATE",
  "SQLWARNING",
  "SQL_BIG_RESULT",
  "SQL_CALC_FOUND_ROWS",
  "SQL_SMALL_RESULT",
  "SSL",
  "STARTING",
  "STRAIGHT_JOIN",
  "TABLE",
  "TERMINATED",
  "THEN",
  "TINYBLOB",
  "TINYINT",
  "TINYTEXT",
  "TO",
  "TRAILING",
  "TRIGGER",
  "TRUE",
  "UNDO",
  "UNION",
  "UNIQUE",
  "UNLOCK",
  "UNSIGNED",
  "UPDATE",
  "USAGE",
  "USE",
  "USING",
  "UTC_DATE",
  "UTC_TIME",
  "UTC_TIMESTAMP",
  "VALUES",
  "VARBINARY",
  "VARCHAR",
  "VARCHARACTER",
  "VARYING",
  "WHEN",
  "WHERE",
  "WHILE",
  "WITH",
  "WRITE",
  "X509",
  "XOR",
  "YEAR_MONTH",
  "ZEROFILL"
], ae = X({
  name: "DBAttributeTable",
  props: {
    actionButton: { type: Boolean, default: !0 }
  },
  emits: ["added", "removed", "inserted", "dragged", "syncDbType", "syncDbIsPersist", "syncDbIsNull"],
  setup(e, { emit: a }) {
    const d = Q(), R = f(!1), N = f([
      {
        title: "字段名称",
        key: "dbFieldName",
        width: 140,
        type: E.input,
        defaultValue: "",
        placeholder: "请输入${title}",
        validateRules: [
          { required: !0, message: "${title}不能为空" },
          {
            pattern: /^[a-zA-Z]{1}(?!_)[a-zA-Z0-9_\\$]+$/,
            message: "命名规则：只能由字母、数字、下划线、$符号组成；必须以字母开头；不能以单个字母加下滑线开头"
          },
          { unique: !0, message: "${title}不能重复" },
          {
            handler({ cellValue: t }, i) {
              te.includes(t.toUpperCase()) ? i(!1, t + "是关键字，不能作为字段名称使用！") : i(!0);
            }
          },
          { handler: O },
          // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
          {
            handler({ cellValue: t }, i) {
              t.length > 32 ? i(!1, "字段名最长32个字符") : i(!0);
            }
          }
          // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
        ],
        disabled: !e.actionButton
      },
      {
        title: "字段备注",
        key: "dbFieldTxt",
        width: 140,
        type: E.input,
        defaultValue: "",
        placeholder: "请输入${title}",
        validateRules: [
          { required: !0, message: "${title}不能为空" },
          // update-begin--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
          {
            handler({ cellValue: t }, i) {
              t.length > 200 ? i(!1, "字段名最长200个字") : i(!0);
            }
          }
          // update-end--author:liaozhiyang---date:20240603---for：【TV360X-631】表名字段名表描述字段备注长度校验
        ]
      },
      {
        title: "字段长度",
        key: "dbLength",
        width: 120,
        type: E.inputNumber,
        defaultValue: 32,
        placeholder: "请输入${title}",
        validateRules: [{ required: !0, message: "${title}不能为空" }],
        disabled: !e.actionButton
      },
      {
        title: "小数点",
        key: "dbPointLength",
        width: 100,
        type: E.inputNumber,
        defaultValue: 0,
        placeholder: "请输入${title}",
        validateRules: [{ required: !0, message: "${title}不能为空" }],
        disabled: !e.actionButton
      },
      {
        title: "默认值",
        key: "dbDefaultVal",
        width: 140,
        type: E.input,
        defaultValue: "",
        disabled: !e.actionButton
      },
      {
        title: "字段类型",
        key: "dbType",
        width: 140,
        type: E.select,
        options: [
          { title: "String", value: "string" },
          { title: "Integer", value: "int" },
          { title: "Double", value: "double" },
          { title: "Date", value: "Date" },
          { title: "Datetime", value: "Datetime" },
          { title: "BigDecimal", value: "BigDecimal" },
          { title: "Text", value: "Text" },
          { title: "Blob", value: "Blob" }
        ],
        defaultValue: "string",
        placeholder: "请选择${title}",
        disabled: !e.actionButton,
        validateRules: [{ required: !0, message: "请选择${title}" }, { handler: x }]
      },
      {
        title: "主键",
        key: "dbIsKey",
        width: 80,
        type: E.checkbox,
        align: "center",
        customValue: ["1", "0"],
        defaultChecked: !1,
        disabled: !e.actionButton
      },
      {
        title: "允许空值",
        key: "dbIsNull",
        width: 80,
        type: E.checkbox,
        customValue: ["1", "0"],
        defaultChecked: !0,
        disabled: !e.actionButton
      },
      {
        title: "同步数据库",
        key: "dbIsPersist",
        minWidth: 80,
        type: E.checkbox,
        customValue: ["1", "0"],
        defaultChecked: !0,
        disabled: !e.actionButton
      },
      { title: "orderNum", key: "orderNum", type: E.hidden }
    ]);
    let s = [];
    const I = j(N), { tableRef: A, loading: L, dataSource: g, tableHeight: m, tableProps: h, setDataSource: p, validateData: y } = I;
    function U() {
      a("added", d);
    }
    function B(t) {
      s = s.concat(t.deleteRows.map((i) => i.id)), a("removed", S(D({}, t), { removeIds: s, target: d }));
    }
    function _(t) {
      a("dragged", {
        oldIndex: t.oldIndex,
        newIndex: t.newIndex,
        target: d
      });
    }
    function M(t) {
      a("inserted", S(D({}, t), { target: d }));
    }
    function v() {
      return s;
    }
    function F(t) {
      let { type: i, row: l, col: o, value: n, target: u, oldValue: r } = t;
      i === E.select && o.key === "dbType" ? ((n === "Date" || n === "Datetime") && a("syncDbType", { row: l, value: n, target: d }), (n !== "Date" || n !== "Datetime") && (r == "Date" || r == "Datetime") && a("syncDbType", { row: l, value: n, target: d }), n === "Blob" || n === "Text" || n === "Date" ? u.setValues([{ rowKey: l.id, values: { dbLength: "0" } }]) : n === "string" ? u.setValues([{ rowKey: l.id, values: { dbLength: "32" } }]) : n === "int" || n === "double" || n === "BigDecimal" ? u.setValues([{ rowKey: l.id, values: { dbLength: "10" } }]) : l.dbLength === "0" && u.setValues([{ rowKey: l.id, values: { dbLength: "32" } }])) : o.key === "dbIsPersist" ? a("syncDbIsPersist", { row: l, value: n, target: d }) : o.key === "dbIsNull" && a("syncDbIsNull", { row: l, value: n, target: d });
    }
    function V(t, i, l) {
      var o;
      if (t === "has_child")
        return !0;
      if (t === "id") {
        const { tables: n } = I, r = ((o = n.dbTable.value.tableRef.getTableData()) != null ? o : []).findIndex((T) => T.dbFieldName === "id");
        if ((r === -1 ? 0 : r) === l)
          return !0;
      }
      return !1;
    }
    function P(t) {
      A.value.pushRows(t), R.value || a("added", d);
    }
    function H(t) {
      return A.value.removeRowsById(t);
    }
    function G() {
      R.value = !0, L.value = !0;
    }
    function w() {
      R.value = !1, L.value = !1, a("added", d);
    }
    function O({ cellValue: t, row: i }, l) {
      const { tables: o } = I;
      if (o) {
        let u = o.dbTable.value.tableRef.dataSource.filter((T) => T.id === i.id);
        (!u || u.length <= 0) && l(!0);
        let r = u[0].dbFieldName;
        r == t && l(!0);
        let C = o.idxTable.value.tableRef.getTableData();
        for (let T of C)
          T.indexField.split(",").indexOf(r) >= 0 && l(!1, "当前字段存在索引配置，请先删除索引再修改字段");
      }
      l(!0);
    }
    function x({ cellValue: t, row: i }, l) {
      i.dbType == "int" && i.dbPointLength > 0 && l(!1, "设置了小数点不可设置integer类型"), l(!0);
    }
    return {
      tableRef: A,
      loading: L,
      columns: N,
      dataSource: g,
      setDataSource: p,
      addBatchBegin: G,
      addBatchEnd: w,
      tableAddLine: P,
      tableHeight: m,
      tableProps: h,
      tableDeleteLines: H,
      handleAdded: U,
      handleRemoved: B,
      handleDragged: _,
      handleInserted: M,
      handleValueChange: F,
      handleDisabledDbFieldName: V,
      validateData: y,
      getRemoveIds: v,
      validateExistIndex: O
    };
  }
});
function de(e, a, d, R, N, s) {
  const I = q("JVxeTable");
  return J(), Z(I, z({
    class: "dBAttributeTable",
    ref: "tableRef",
    rowNumber: "",
    rowSelection: "",
    dragSort: "",
    notAllowDrag: [{ key: "dbFieldName", value: "id" }],
    keyboardEdit: "",
    sortKey: "orderNum",
    addButtonSettings: "",
    loading: e.loading,
    columns: e.columns,
    dataSource: e.dataSource,
    toolbar: e.actionButton,
    maxHeight: e.tableHeight.normal,
    disabledRows: { dbFieldName: e.handleDisabledDbFieldName }
  }, e.tableProps, {
    onAdded: e.handleAdded,
    onRemoved: e.handleRemoved,
    onDragged: e.handleDragged,
    onInserted: e.handleInserted,
    onValueChange: e.handleValueChange
  }), null, 16, ["loading", "columns", "dataSource", "toolbar", "maxHeight", "disabledRows", "onAdded", "onRemoved", "onDragged", "onInserted", "onValueChange"]);
}
const Se = /* @__PURE__ */ ee(ae, [["render", de], ["__scopeId", "data-v-7723c158"]]);
export {
  Se as default
};
