import { defineComponent as i, resolveComponent as p, openBlock as a, createElementBlock as c, createVNode as l } from "vue";
import { BasicForm as d, useForm as u } from "/@/components/Form/index";
import { defHttp as f } from "/@/utils/http/axios";
import { pick as _ } from "lodash-es";
import { _ as g } from "./index-9e1e1e53.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
const h = () => [
  {
    field: "name",
    component: "Input",
    label: "名称"
  },
  {
    field: "age",
    component: "InputNumber",
    label: "年龄",
    componentProps: {
      style: {
        width: "100%"
      }
    }
  },
  {
    field: "sex",
    label: "性别",
    component: "JDictSelectTag",
    componentProps: {
      dictCode: "sex"
    }
  },
  {
    field: "birthday",
    component: "DatePicker",
    label: "生日",
    componentProps: {
      valueFormat: "YYYY-MM-DD",
      style: {
        width: "100%"
      }
    }
  },
  {
    field: "email",
    component: "Input",
    label: "邮箱"
  }
], b = i({
  components: { BasicForm: d },
  props: ["id"],
  setup(e) {
    const [t, { setFieldsValue: r }] = u({
      schemas: h(),
      showActionButtonGroup: !1,
      baseColProps: { span: 24 }
    });
    let n = { id: e.id };
    return f.get({ url: "/test/jeecgDemo/queryById", params: n }, { isTransformResponse: !1 }).then((o) => {
      if (o.success) {
        let s = _(o.result, "name", "age", "birthday", "sex", "email");
        r(s);
      }
    }), {
      registerForm: t
    };
  }
}), F = { style: { margin: "50px auto", width: "800px" } };
function x(e, t, r, n, o, s) {
  const m = p("BasicForm");
  return a(), c("div", F, [
    l(m, { onRegister: e.registerForm }, null, 8, ["onRegister"])
  ]);
}
const I = /* @__PURE__ */ g(b, [["render", x]]);
export {
  I as default
};
