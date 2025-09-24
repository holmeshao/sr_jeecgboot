var h = (i, u, c) => new Promise((p, m) => {
  var g = (o) => {
    try {
      r(c.next(o));
    } catch (l) {
      m(l);
    }
  }, f = (o) => {
    try {
      r(c.throw(o));
    } catch (l) {
      m(l);
    }
  }, r = (o) => o.done ? p(o.value) : Promise.resolve(o.value).then(g, f);
  r((c = c.apply(i, u)).next());
});
import { defineComponent as S, ref as T, watch as A, resolveComponent as w, openBlock as E, createElementBlock as $, createVNode as v, withCtx as x, createTextVNode as q } from "vue";
import { cloneDeep as F } from "lodash-es";
import { BasicTable as j, useTable as z } from "/@/components/Table";
import { f as O, g as U, h as V } from "./auth.api-53df4c33.mjs";
import { b as K, c as P } from "./auth.data-626c5083.mjs";
import { _ as G } from "./index-9e1e1e53.mjs";
import "/@/utils/http/axios";
import "/@/utils/index";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const H = S({
  name: "AuthButtonConfig",
  components: { BasicTable: j },
  props: {
    headId: {
      type: String,
      default: "",
      required: !0
    },
    // 1单表 2主表 3附表
    tableType: {
      type: Number,
      default: 1
    }
  },
  setup(i) {
    const u = T(""), c = T(2), p = T(3), m = T(5), [g, { reload: f, getTableRef: r, setPagination: o }] = z({
      api: B,
      rowKey: "code",
      bordered: !0,
      columns: K,
      showIndexColumn: !1
    });
    A(
      () => i.headId,
      (t) => {
        u.value = t.split("?")[0], r().value && o({ current: 1, pageSize: 10 }), f().catch(() => null);
      },
      { immediate: !0 }
    );
    const l = (t) => {
      const e = F(P);
      if (t.mainRelationType != null && t.mainThemeTemplate != null && i.tableType == 3) {
        let a = [];
        switch (t.mainThemeTemplate) {
          case "normal":
          case "innerTable":
          case "tab":
            t.mainRelationType == 1 ? a = [] : a = e.filter((n) => ["add", "update", "batch_delete"].includes(n.code));
            break;
          case "erp":
            a = e.filter((n) => !["super_query"].includes(n.code));
            break;
        }
        return a;
      } else
        return e;
    };
    function B(t) {
      return h(this, null, function* () {
        let e = yield O(u.value, t), { authList: a, buttonList: n } = e, d = [];
        const y = l(e);
        for (let s of y) {
          const b = n.findIndex((C) => C.buttonCode === s.code), _ = {};
          b !== -1 && (_.title = n[b].buttonName, n.splice(b, 1));
          let N = {
            status: 0,
            page: p.value
          }, R = a.find((C) => C.code == s.code);
          Object.assign(s, N, R, _), d.push(s);
        }
        if (i.tableType == 3) {
          const s = d.findIndex((b) => b.code === "super_query");
          s != -1 && d.splice(s, 1);
        }
        return I(a, n, d);
      });
    }
    function I(t, e, a) {
      for (let n of e) {
        let d = t.find((s) => s.code == n.buttonCode), y = {
          code: n.buttonCode,
          title: n.buttonName,
          status: 0,
          page: n.buttonStyle == "form" ? m.value : p.value
        };
        a.push(Object.assign(y, d));
      }
      return a;
    }
    function k(t, e) {
      return h(this, null, function* () {
        t ? L(e) : D(e);
      });
    }
    function L(t) {
      return h(this, null, function* () {
        let e = yield U({
          id: t.id,
          code: t.code,
          page: t.page,
          cgformId: u.value,
          type: c.value,
          control: 5,
          status: 1
        });
        t.id = e.id, t.status = 1;
      });
    }
    function D(t) {
      return h(this, null, function* () {
        yield V(t.id), t.status = 0;
      });
    }
    return { registerTable: g, onUpdateStatus: k };
  }
}), J = { class: "auth-field-config" };
function M(i, u, c, p, m, g) {
  const f = w("a-switch"), r = w("BasicTable");
  return E(), $("div", J, [
    v(r, { onRegister: i.registerTable }, {
      switch: x(({ text: o, record: l }) => [
        v(f, {
          size: "small",
          checked: l.status === 1,
          onChange: (B) => i.onUpdateStatus(B, l)
        }, null, 8, ["checked", "onChange"])
      ]),
      control: x(() => u[0] || (u[0] = [
        q(" 可见 ")
      ])),
      _: 1
    }, 8, ["onRegister"])
  ]);
}
const ut = /* @__PURE__ */ G(H, [["render", M]]);
export {
  ut as default
};
