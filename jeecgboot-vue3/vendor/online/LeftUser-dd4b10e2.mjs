var m = (r, n, o) => new Promise((i, s) => {
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
  l((o = o.apply(r, n)).next());
});
import { defineComponent as g, resolveComponent as w, openBlock as b, createBlock as h, withCtx as S, createElementVNode as T } from "vue";
import { defHttp as _ } from "/@/utils/http/axios";
import { BasicTable as C } from "/@/components/Table";
import { useListPage as x } from "/@/hooks/system/useListPage";
import { _ as B } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const R = g({
  name: "LeftUser",
  components: { BasicTable: C },
  emits: ["select"],
  setup(r, { emit: n }) {
    const { tableContext: o, createMessage: i } = x({
      tableProps: {
        api: l,
        rowKey: "id",
        size: "small",
        bordered: !0,
        columns: [
          { title: "账号", dataIndex: "username", width: 200 },
          { title: "姓名", dataIndex: "realname", width: 200 }
        ],
        rowSelection: {
          type: "radio",
          onChange(t) {
            t.length > 0 && n("select", t[0]);
          }
        },
        formConfig: {
          schemas: [
            {
              label: "账号",
              field: "username",
              component: "JInput",
              componentProps: {
                placeholder: "输入账号"
              }
            },
            {
              label: "姓名",
              field: "realname",
              component: "JInput",
              componentProps: {
                placeholder: "输入姓名"
              }
            }
          ]
        },
        canResize: !1,
        clickToRowSelect: !0,
        showActionColumn: !1,
        showTableSetting: !1
      }
    }), [s, { clearSelectedRowKeys: c }, { rowSelection: a }] = o;
    function l(t) {
      return m(this, null, function* () {
        let { code: p, success: f, result: u, message: d } = yield _.get(
          {
            url: "/sys/user/list",
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
function $(r, n, o, i, s, c) {
  const a = w("BasicTable");
  return b(), h(a, {
    onRegister: r.registerTable,
    rowSelection: r.rowSelection
  }, {
    tableTop: S(() => n[0] || (n[0] = [
      T("span", null, null, -1)
    ])),
    _: 1
  }, 8, ["onRegister", "rowSelection"]);
}
const A = /* @__PURE__ */ B(R, [["render", $]]);
export {
  A as default
};
