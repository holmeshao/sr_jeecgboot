var S = (s, f, m) => new Promise((y, k) => {
  var g = (r) => {
    try {
      h(m.next(r));
    } catch (u) {
      k(u);
    }
  }, w = (r) => {
    try {
      h(m.throw(r));
    } catch (u) {
      k(u);
    }
  }, h = (r) => r.done ? y(r.value) : Promise.resolve(r.value).then(g, w);
  h((m = m.apply(s, f)).next());
});
import { defineComponent as V, ref as p, watch as q, resolveComponent as L, openBlock as E, createElementBlock as U, createVNode as b, withCtx as C, Fragment as P, createTextVNode as v, createBlock as x, toDisplayString as R } from "vue";
import { BasicTable as K, useTable as M } from "/@/components/Table";
import { a as O, b as _, c as j, d as G, e as A } from "./auth.api-53df4c33.mjs";
import { a as H } from "./auth.data-626c5083.mjs";
import { _ as J } from "./index-9e1e1e53.mjs";
import "/@/utils/http/axios";
import "/@/utils/index";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const Q = V({
  name: "AuthFieldConfig",
  components: { BasicTable: K },
  props: {
    headId: {
      type: String,
      default: "",
      required: !0
    }
  },
  emits: ["update:authFields"],
  setup(s, { emit: f }) {
    const m = p(""), [y, { reload: k, getTableRef: g, setPagination: w }] = M({
      api: $,
      rowKey: "code",
      bordered: !0,
      columns: H,
      showIndexColumn: !1
    }), h = p(!1), r = p(!1), u = p(!1), i = p(!1), c = p(!1), I = p(!1), F = p(!1);
    q(
      () => s.headId,
      (l) => {
        m.value = l.split("?")[0], g().value && w({ current: 1, pageSize: 10 }), k().catch(() => null);
      },
      { immediate: !0 }
    );
    function $(l) {
      return S(this, null, function* () {
        const e = ["id"];
        let n = yield O(m.value, l), t = [], d = [];
        return n.forEach((o) => {
          e.indexOf(o.code) < 0 && ((o.isShowForm == 1 || o.isShowList == 1) && d.push(o), o.dbIsPersist == 1 && t.push({
            text: o.title,
            value: o.code,
            // -update-begin--author:liaozhiyang---date:20240617---for：【TV360X-201】权限管理条件根据控件过滤
            view: o.fieldShowType,
            dbType: o.dbType
            // -update-end--author:liaozhiyang---date:20240617---for：【TV360X-201】权限管理条件根据控件过滤
          }));
        }), f("update:authFields", t), T(l.pageNo, l.pageSize, d), d;
      });
    }
    function N(l, e) {
      return S(this, null, function* () {
        yield _({
          cgformId: m.value,
          code: e.code,
          status: l ? 1 : 0
        }), e.formEditable || e.formShow || e.listShow || (e.formEditable = !0, e.formShow = !0, e.listShow = !0), e.status = Math.abs(e.status - 1), B();
      });
    }
    function z(l, e, n) {
      return S(this, null, function* () {
        let t = l.target.checked;
        yield j({
          cgformId: m.value,
          code: e.code,
          switchFlag: n,
          listShow: t,
          formShow: t,
          formEditable: t
        }), n == 1 ? e.listShow = t : n == 2 ? e.formShow = t : n == 3 && (e.formEditable = t), e.listShow === !1 && e.formShow === !1 && e.formEditable === !1 && (e.status = 0), B();
      });
    }
    function T(l, e, n) {
      const t = [];
      if (n != null && n.length) {
        const d = l * e > n.length ? n.length : l * e;
        for (let o = l * e - e; o < d; o++) {
          const a = n[o];
          t.push(a);
        }
      }
      t.length ? (h.value = !0, r.value = !0, u.value = !0, t.forEach((d) => {
        h.value && d.status == 0 && (h.value = !1), r.value && d.listShow == !1 && (r.value = !1), u.value && (d.formEditable == !1 || d.formShow == !1) && (u.value = !1);
      }), r.value == !0 ? F.value = !1 : t.find((o) => o.listShow) ? F.value = !0 : F.value = !1, u.value == !0 ? I.value = !1 : t.find((o) => o.formEditable || o.formShow) ? I.value = !0 : I.value = !1) : (h.value = !1, r.value = !1, u.value = !1);
    }
    const B = () => {
      const { current: l, pageSize: e } = g().value.getPaginationRef(), n = g().value.getDataSource();
      T(l, e, n);
    }, D = (l, e) => {
      const n = [], t = g().value.getDataSource();
      if (t != null && t.length) {
        const d = l * e > t.length ? t.length : l * e;
        for (let o = l * e - e; o < d; o++) {
          const a = t[o];
          n.push(a);
        }
      }
      return n;
    };
    return { registerTable: y, onUpdateStatus: N, onCheckboxChange: z, handleChangeSwitch: (l) => S(this, null, function* () {
      c.value = !0, h.value = l;
      const { current: e, pageSize: n } = g().value.getPaginationRef(), t = D(e, n);
      let d = t.map((a) => ({ cgformId: a.cgformId, code: a.code, status: l ? 1 : 0 }));
      i.value = !0, yield G(d), t.forEach((a) => {
        l ? a.status = 1 : a.status = 0, a.formEditable || a.formShow || a.listShow || (a.formEditable = !0, a.formShow = !0, a.listShow = !0);
      }), i.value = !1, c.value = !1;
      const o = g().value.getDataSource();
      T(e, n, o);
    }), allSwitch: h, allFormControl: u, allListControl: r, allSloading: i, handleTableChange: (l) => {
    }, handleChangeList: (l) => S(this, null, function* () {
      c.value = !0;
      const e = l.target.checked;
      r.value = e;
      const { current: n, pageSize: t } = g().value.getPaginationRef(), d = D(n, t);
      let o = d.map((a) => ({ cgformId: a.cgformId, code: a.code, switchFlag: 1, listShow: !!e }));
      yield A(o), d.forEach((a) => {
        a.listShow = !!e, a.listShow === !1 && a.formShow === !1 && a.formEditable === !1 && (a.status = 0, h.value = !1);
      }), e && (F.value = !1), c.value = !1;
    }), handleChangeForm: (l) => S(this, null, function* () {
      c.value = !0;
      const e = l.target.checked;
      u.value = e;
      const { current: n, pageSize: t } = g().value.getPaginationRef(), d = D(n, t), o = [
        ...d.map((a) => ({ cgformId: a.cgformId, code: a.code, switchFlag: 4, formShow: !!e, formEditable: !!e }))
      ];
      yield A(o), d.forEach((a) => {
        a.formEditable = !!e, a.formShow = !!e, a.listShow === !1 && a.formShow === !1 && a.formEditable === !1 && (a.status = 0, h.value = !1);
      }), e && (I.value = !1), c.value = !1;
    }), tableLoading: c, formIndeterminate: I, listIndeterminate: F };
  }
});
const W = { class: "auth-field-config" };
function X(s, f, m, y, k, g) {
  const w = L("a-switch"), h = L("a-checkbox"), r = L("BasicTable");
  return E(), U("div", W, [
    b(r, {
      onRegister: s.registerTable,
      onChange: s.handleTableChange,
      loading: s.tableLoading
    }, {
      headerCell: C(({ column: u }) => [
        u.dataIndex === "switch" ? (E(), U(P, { key: 0 }, [
          b(w, {
            loading: s.allSloading,
            checked: s.allSwitch,
            "onUpdate:checked": f[0] || (f[0] = (i) => s.allSwitch = i),
            size: "small",
            onChange: s.handleChangeSwitch
          }, null, 8, ["loading", "checked", "onChange"]),
          f[3] || (f[3] = v("启用 "))
        ], 64)) : u.dataIndex === "list" ? (E(), x(h, {
          key: 1,
          indeterminate: s.listIndeterminate,
          checked: s.allListControl,
          "onUpdate:checked": f[1] || (f[1] = (i) => s.allListControl = i),
          disabled: !s.allSwitch,
          onChange: s.handleChangeList
        }, {
          default: C(() => [
            v(R(u.customTitle), 1)
          ]),
          _: 2
        }, 1032, ["indeterminate", "checked", "disabled", "onChange"])) : u.dataIndex === "form" ? (E(), x(h, {
          key: 2,
          indeterminate: s.formIndeterminate,
          checked: s.allFormControl,
          "onUpdate:checked": f[2] || (f[2] = (i) => s.allFormControl = i),
          disabled: !s.allSwitch,
          onChange: s.handleChangeForm
        }, {
          default: C(() => [
            v(R(u.customTitle), 1)
          ]),
          _: 2
        }, 1032, ["indeterminate", "checked", "disabled", "onChange"])) : (E(), U(P, { key: 3 }, [
          v(R(u.customTitle), 1)
        ], 64))
      ]),
      switch: C(({ text: u, record: i }) => [
        b(w, {
          size: "small",
          checked: i.status === 1,
          onChange: (c) => s.onUpdateStatus(c, i)
        }, null, 8, ["checked", "onChange"])
      ]),
      list: C(({ text: u, record: i }) => [
        b(h, {
          checked: i.listShow,
          disabled: i.status === 0,
          onChange: (c) => s.onCheckboxChange(c, i, 1)
        }, {
          default: C(() => f[4] || (f[4] = [
            v(" 可见 ")
          ])),
          _: 2
        }, 1032, ["checked", "disabled", "onChange"])
      ]),
      form: C(({ text: u, record: i }) => [
        b(h, {
          checked: i.formShow,
          disabled: i.status === 0,
          onChange: (c) => s.onCheckboxChange(c, i, 2)
        }, {
          default: C(() => f[5] || (f[5] = [
            v(" 可见 ")
          ])),
          _: 2
        }, 1032, ["checked", "disabled", "onChange"]),
        b(h, {
          checked: i.formEditable,
          disabled: i.status === 0,
          onChange: (c) => s.onCheckboxChange(c, i, 3)
        }, {
          default: C(() => f[6] || (f[6] = [
            v(" 可编辑 ")
          ])),
          _: 2
        }, 1032, ["checked", "disabled", "onChange"])
      ]),
      _: 1
    }, 8, ["onRegister", "onChange", "loading"])
  ]);
}
const ce = /* @__PURE__ */ J(Q, [["render", X], ["__scopeId", "data-v-c48da65c"]]);
export {
  ce as default
};
