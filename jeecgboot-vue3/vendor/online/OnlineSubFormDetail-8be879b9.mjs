var f = (t, i, m) => new Promise((e, a) => {
  var s = (r) => {
    try {
      n(m.next(r));
    } catch (o) {
      a(o);
    }
  }, p = (r) => {
    try {
      n(m.throw(r));
    } catch (o) {
      a(o);
    }
  }, n = (r) => r.done ? e(r.value) : Promise.resolve(r.value).then(s, p);
  n((m = m.apply(t, i)).next());
});
import { useMessage as h } from "/@/hooks/web/useMessage";
import { ref as u, watch as d, resolveComponent as F, openBlock as S, createBlock as v } from "vue";
import { BasicForm as D } from "/@/components/Form/index";
import { defHttp as y } from "/@/utils/http/axios";
import { m as w, g as $ } from "./useExtendComponent-bb98e568.mjs";
import { Loading as x } from "/@/components/Loading";
import B from "./DetailForm-c592b8d8.mjs";
import { _ as I } from "./index-9e1e1e53.mjs";
import "/@/components/Form/src/componentMap";
import "/@/utils/propTypes";
import "@ant-design/icons-vue";
import "/@/components/Modal";
import "lodash-es";
import "./constant-fa63bd66.mjs";
import "/@/utils/common/compUtils";
import "/@/components/Form/src/jeecg/components/JUpload";
import "/@/utils/is";
import "/@/views/system/user/user.api";
import "/@/store/modules/user";
import "/@/utils";
import "/@/utils/desform/customExpression";
import "/@/store/modules/permission";
import "/@/utils/dict/JDictSelectUtil";
import "/@/components/Table";
import "/@/hooks/system/useListPage";
import "vue-router";
import "/@/components/Form/src/utils/Area";
import "/@/components/Preview/index";
import "./LinkTableListPiece-e016b8e6.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/utils/auth";
import "/@/api/common/api";
import "/@/hooks/web/useAppInject";
import "/@/assets/images/placeholderImage.png";
import "./OnlineSelectCascade-d631ed72.mjs";
import "./JModalTip-a927f85d.mjs";
import "ant-design-vue";
import "@vueuse/core";
import "/@/utils/dict";
import "/@/utils/dict/index";
import "/@/components/Markdown";
const O = "/online/cgform/api/subform", P = {
  name: "OnlineSubFormDetail",
  components: {
    BasicForm: D,
    Loading: x,
    DetailForm: B
  },
  props: {
    properties: {
      type: Object,
      required: !0
    },
    mainId: {
      type: String,
      default: ""
    },
    table: {
      type: String,
      default: ""
    },
    formTemplate: {
      type: Number,
      default: 1
    }
  },
  emits: ["formChange"],
  setup(t) {
    const i = u(!1);
    h();
    const m = u(""), e = u({}), { detailFormSchemas: a, createFormSchemas: s, formSpan: p } = w(t);
    d(
      () => t.table,
      () => {
        m.value = t.table;
      },
      { immediate: !0 }
    ), d(
      () => t.properties,
      () => {
        i.value = !1, s(t.properties), i.value = !0;
      },
      { deep: !0, immediate: !0 }
    ), d(
      () => t.mainId,
      () => {
        setTimeout(() => {
          n();
        }, 100);
      },
      { immediate: !0 }
    );
    function n() {
      return f(this, null, function* () {
        yield $(i), e.value = {};
        const { table: o, mainId: l } = t;
        !o || !l || (e.value = (yield r(o, l)) || {});
      });
    }
    function r(o, l) {
      return f(this, null, function* () {
        let _ = `${O}/${o}/${l}`;
        return new Promise((b, g) => {
          y.get({ url: _ }, { isTransformResponse: !1 }).then((c) => {
            c.success ? b(c.result) : g(c.message);
          });
        }).catch((b) => Promise.resolve({}));
      });
    }
    return {
      detailFormSchemas: a,
      subFormData: e,
      formSpan: p
    };
  }
};
function R(t, i, m, e, a, s) {
  const p = F("detail-form");
  return S(), v(p, {
    schemas: e.detailFormSchemas,
    data: e.subFormData,
    span: e.formSpan
  }, null, 8, ["schemas", "data", "span"]);
}
const St = /* @__PURE__ */ I(P, [["render", R]]);
export {
  St as default
};
