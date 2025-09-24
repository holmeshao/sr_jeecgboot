var h = (i, p, o) => new Promise((t, n) => {
  var u = (a) => {
    try {
      e(o.next(a));
    } catch (s) {
      n(s);
    }
  }, c = (a) => {
    try {
      e(o.throw(a));
    } catch (s) {
      n(s);
    }
  }, e = (a) => a.done ? t(a.value) : Promise.resolve(a.value).then(u, c);
  e((o = o.apply(i, p)).next());
});
import { useDebounceFn as O } from "@vueuse/core";
import { defHttp as P } from "/@/utils/http/axios";
import { useMessage as k } from "/@/hooks/web/useMessage";
import { ref as m, watch as S, resolveComponent as _, openBlock as g, createBlock as y, withCtx as w, createElementBlock as B, Fragment as D, renderList as H, createTextVNode as I, toDisplayString as q } from "vue";
import { _ as L } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const { createMessage: N } = k(), T = {
  name: "JOnlineSearchSelect",
  props: {
    placeholder: {
      type: String,
      default: "",
      required: !1
    },
    value: {
      type: String,
      required: !1
    },
    // online CgReport item id
    fieldId: {
      type: String,
      required: !0
    }
  },
  emits: ["update:value"],
  setup(i, { emit: p }) {
    let o = m(""), t = m([]), n = !0, u = !1, c = "";
    const e = m(1);
    S(
      () => i.value,
      (l) => {
        l ? o.value = l : o.value = void 0;
      },
      { immediate: !0 }
    ), S(
      () => i.fieldId,
      () => {
        v();
      },
      { immediate: !0 }
    );
    const a = O((l) => {
      c = l, e.value = 1, n = !0, s(l);
    }, 800);
    function s(l = "") {
      return h(this, null, function* () {
        let d = {
          keyword: l,
          fieldId: i.fieldId,
          pageSize: 10,
          pageNo: e.value
        }, f = "/online/cgreport/api/getReportDictList";
        yield P.get({ url: f, params: d }, { isTransformResponse: !1 }).then((r) => {
          r.success ? r.result && r.result.length > 0 ? (e.value == 1 ? t.value = [...r.result] : t.value.push(...r.result), e.value++) : (e.value == 1 && (t.value = []), n = !1) : N.warning(r.message);
        }).catch(() => {
          e.value != 1 && e.value--;
        });
      });
    }
    function C(l) {
      p("update:value", l), (!l || l == "") && v();
    }
    function v() {
      t.value = [], e.value = 1, n = !0, c = "", s();
    }
    return {
      selectOptions: t,
      handleSearch: a,
      handleChange: C,
      selected: o,
      handlePopupScroll: (l) => h(this, null, function* () {
        const { target: d } = l, { scrollTop: f, scrollHeight: r, clientHeight: x } = d;
        !u && n && f + x >= r - 10 && (u = !0, s(c).finally(() => {
          u = !1;
        }));
      })
    };
  }
};
function F(i, p, o, t, n, u) {
  const c = _("a-select-option"), e = _("a-select");
  return g(), y(e, {
    value: t.selected,
    placeholder: o.placeholder,
    "show-search": "",
    "default-active-first-option": !1,
    "show-arrow": !0,
    "filter-option": !1,
    "not-found-content": null,
    onSearch: t.handleSearch,
    onChange: t.handleChange,
    onPopupScroll: t.handlePopupScroll,
    allowClear: ""
  }, {
    default: w(() => [
      (g(!0), B(D, null, H(t.selectOptions, (a) => (g(), y(c, {
        key: a.value
      }, {
        default: w(() => [
          I(q(a.text), 1)
        ]),
        _: 2
      }, 1024))), 128))
    ]),
    _: 1
  }, 8, ["value", "placeholder", "onSearch", "onChange", "onPopupScroll"]);
}
const G = /* @__PURE__ */ L(T, [["render", F]]);
export {
  G as default
};
