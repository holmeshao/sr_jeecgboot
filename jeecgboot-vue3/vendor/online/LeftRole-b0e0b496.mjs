var m = (n, r, o) => new Promise((i, s) => {
  var c = (e) => {
    try {
      l(o.next(e));
    } catch (t) {
      s(t);
    }
  }, a = (e) => {
    try {
      l(o.throw(e));
    } catch (t) {
      s(t);
    }
  }, l = (e) => e.done ? i(e.value) : Promise.resolve(e.value).then(c, a);
  l((o = o.apply(n, r)).next());
});
import { defineComponent as g, resolveComponent as w, openBlock as b, createBlock as S, withCtx as T, createElementVNode as _ } from "vue";
import { defHttp as C } from "/@/utils/http/axios";
import { BasicTable as R } from "/@/components/Table";
import { useListPage as h } from "/@/hooks/system/useListPage";
import { _ as x } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const B = g({
  name: "LeftRole",
  components: { BasicTable: R },
  emits: ["select"],
  setup(n, { emit: r }) {
    const { tableContext: o, createMessage: i } = h({
      tableProps: {
        api: l,
        rowKey: "id",
        size: "small",
        bordered: !0,
        columns: [
          { title: "角色编码", align: "center", dataIndex: "roleCode" },
          { title: "角色名称", align: "center", dataIndex: "roleName" }
        ],
        rowSelection: {
          type: "radio",
          onChange(t) {
            t.length > 0 && r("select", t[0]);
          }
        },
        canResize: !1,
        clickToRowSelect: !0,
        useSearchForm: !1,
        showActionColumn: !1,
        showTableSetting: !1
      }
    }), [s, { clearSelectedRowKeys: c }, { rowSelection: a }] = o;
    function l(t) {
      return m(this, null, function* () {
        let { code: p, success: f, result: u, message: d } = yield C.get(
          {
            url: "/sys/role/list",
            params: t
          },
          { isTransformResponse: !1 }
        );
        return f ? u : (p === 510 && i.warning(d), {});
      });
    }
    function e() {
      c();
    }
    return { rowSelection: a, registerTable: s, clearSelected: e };
  }
});
function $(n, r, o, i, s, c) {
  const a = w("BasicTable");
  return b(), S(a, {
    onRegister: n.registerTable,
    rowSelection: n.rowSelection
  }, {
    tableTop: T(() => r[0] || (r[0] = [
      _("span", null, null, -1)
    ])),
    _: 1
  }, 8, ["onRegister", "rowSelection"]);
}
const D = /* @__PURE__ */ x(B, [["render", $]]);
export {
  D as default
};
