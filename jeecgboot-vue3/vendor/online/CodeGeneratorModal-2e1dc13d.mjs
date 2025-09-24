var E = (t, o, c) => new Promise((F, p) => {
  var T = (n) => {
    try {
      l(c.next(n));
    } catch (f) {
      p(f);
    }
  }, d = (n) => {
    try {
      l(c.throw(n));
    } catch (f) {
      p(f);
    }
  }, l = (n) => n.done ? F(n.value) : Promise.resolve(n.value).then(T, d);
  l((c = c.apply(t, o)).next());
});
import { defineComponent as ie, ref as M, computed as G, reactive as P, nextTick as ue, resolveComponent as i, openBlock as L, createElementBlock as me, Fragment as ce, createVNode as u, withCtx as r, createElementVNode as J, createTextVNode as V, createBlock as I, createCommentVNode as $, mergeProps as pe } from "vue";
import { defHttp as H } from "/@/utils/http/axios";
import { BasicForm as de, useForm as fe } from "/@/components/Form";
import { BasicModal as ge, useModalInner as Fe, useModal as U } from "/@/components/Modal";
import { JVxeTypes as R } from "/@/components/jeecg/JVxeTable/types";
import { b as be } from "./useSchemas-b074f3a1.mjs";
import { underLine2CamelCase as q } from "/@/utils/common/compUtils";
import Ce from "./CodeFileListModal-a924902a.mjs";
import _e from "./FileSelectModal-ffc69d4a.mjs";
import { message as ve } from "ant-design-vue";
import { useDesign as Me } from "/@/hooks/web/useDesign";
import { _ as Ne } from "./index-9e1e1e53.mjs";
import "@ant-design/icons-vue";
import "/@/hooks/web/usePermission";
import "/@/utils/helper/validator";
import "./CodeFileViewModal-405e2b58.mjs";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "/@/hooks/web/useMessage";
import "vue-router";
import "/@/utils/file/download";
const ye = ie({
  name: "CodeGenerator",
  components: { BasicForm: de, BasicModal: ge, FileSelectModal: _e, CodeFileListModal: Ce },
  emits: ["register"],
  setup(t) {
    const o = "JEECG_ONL_PROJECT_PATH", c = "JEECG_ONL_PROJECT_NAME", F = M(!0), p = M(), T = G(() => F.value ? 800 : 1200), d = M("代码生成"), l = M(!1), { prefixCls: n } = Me("code-generator-modal"), f = n, N = M(""), h = P({
      projectPath: "",
      packageStyle: "service",
      jspMode: "",
      jformType: "1",
      tableName_tmp: "",
      ftlDescription: "",
      entityName: "",
      codeTypes: "controller,service,dao,mapper,entity,vue"
    }), a = P({}), y = M([]), b = P({
      dataSource: [],
      columns: [
        {
          title: "子表名",
          key: "tableName",
          type: R.input,
          disabled: !0,
          validateRules: [{ required: !0, message: "请输入${title}" }]
        },
        {
          title: "子表实体",
          key: "entityName",
          type: R.input,
          validateRules: [{ required: !0, message: "请输入${title}" }]
        },
        {
          title: "功能说明",
          key: "ftlDescription",
          type: R.input,
          validateRules: [{ required: !0, message: "请输入${title}" }]
        }
      ]
    }), B = G(() => b.dataSource.length > 0), { formSchemas: C } = be(
      t,
      {
        onProjectPathChange: se,
        onProjectPathSearch: oe,
        jspModeOptions: y
      },
      F
    ), [S, { resetFields: j, setFieldsValue: k, validate: K }] = fe({
      schemas: C,
      showActionButtonGroup: !1,
      labelAlign: "right"
    }), [x, { closeModal: O }] = Fe((e) => E(this, null, function* () {
      yield j(), N.value = e.code, l.value = !1, b.dataSource = [], y.value = [], ne(), Object.assign(a, h), W();
    }));
    function W() {
      return E(this, null, function* () {
        let { main: e, sub: s, jspModeList: _, projectPath: m } = yield H.get({
          url: "/online/cgform/head/tableInfo",
          params: { code: N.value }
        }), v = [];
        for (let g of _) {
          const { code: D, note: re } = g;
          D == "many" || v.push({
            label: re,
            value: D
          });
        }
        y.value = v, e.isTree == "Y" ? a.jspMode = "tree" : v.find((g) => g.value === e.themeTemplate) ? a.jspMode = e.themeTemplate : a.jspMode = v[0].value, F.value = e.tableType == 1, d.value = "代码生成【" + e.tableName + "】", a.projectPath || (a.projectPath = m, window.localStorage.setItem(o, m));
        const w = localStorage.getItem(c);
        w && (a.entityPackage = w), a.jformType = e.tableType + "", a.tableName_tmp = e.tableName, a.ftlDescription = e.tableTxt;
        let A = q(e.tableName);
        a.entityName = A.substring(0, 1).toUpperCase() + A.substring(1), yield ue(), k(a), s && s.length > 0 && (b.dataSource = s.map((g) => ({
          tableName: g.tableName,
          entityName: le(g.tableName),
          ftlDescription: g.tableTxt
        })));
      });
    }
    const [z, { openModal: Y }] = U();
    function Q() {
      return E(this, null, function* () {
        try {
          const e = yield K();
          let s = Object.assign({}, e, { code: N.value, tableName: e.tableName_tmp });
          if (B.value) {
            if (yield p.value.validateTable())
              return;
            s.subList = p.value.getTableData();
          }
          l.value = !0;
          let _ = yield X(s);
          Y(!0, {
            codeList: _.codeList,
            pathKey: _.pathKey,
            tableName: e.tableName_tmp
          }), O(), localStorage.setItem(c, e.entityPackage);
        } catch (e) {
        } finally {
          l.value = !1;
        }
      });
    }
    function X(e) {
      return new Promise((s, _) => {
        H.post({ url: "/online/cgform/api/codeGenerate", params: e }, { isTransformResponse: !1 }).then((m) => {
          if (m.success) {
            let v = m.result, w = m.message;
            s({
              codeList: v,
              pathKey: w
            });
          } else
            ve.error(m.message), _(m.message);
        });
      });
    }
    function Z() {
      O();
    }
    const [ee, te] = U();
    function oe() {
      te.openModal(!0, {});
    }
    function ae(e) {
      window.localStorage.setItem(o, e), k({ projectPath: e });
    }
    function le(e) {
      let s = q(e);
      return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    function ne() {
      let e = window.localStorage.getItem(o);
      e && (h.projectPath = e);
    }
    function se(e) {
      e.target.value && window.localStorage.setItem(o, e.target.value);
    }
    return {
      title: d,
      modalWidth: T,
      confirmLoading: l,
      subTable: b,
      showSubTable: B,
      onSubmit: Q,
      onCancel: Z,
      onFileSelect: ae,
      registerFileSelectModal: ee,
      subTableRef: p,
      registerForm: S,
      registerModal: x,
      registerCodeFileListModal: z,
      wrapClassName: f
    };
  }
});
function Te(t, o, c, F, p, T) {
  const d = i("a-radio"), l = i("a-tooltip"), n = i("a-radio-group"), f = i("BasicForm"), N = i("JVxeTable"), h = i("a-card"), a = i("a-spin"), y = i("BasicModal"), b = i("FileSelectModal"), B = i("code-file-list-modal");
  return L(), me(ce, null, [
    u(y, {
      onRegister: t.registerModal,
      title: t.title,
      width: t.modalWidth,
      confirmLoading: t.confirmLoading,
      okText: "开始生成",
      cancelText: "取消",
      onOk: t.onSubmit,
      onCancel: t.onCancel,
      wrapClassName: t.wrapClassName
    }, {
      default: r(() => [
        u(a, { spinning: t.confirmLoading }, {
          default: r(() => [
            u(f, { onRegister: t.registerForm }, {
              pageCode: r(({ model: C, field: S }) => [
                u(n, {
                  value: C[S],
                  "onUpdate:value": (j) => C[S] = j
                }, {
                  default: r(() => [
                    u(l, { placement: "top" }, {
                      title: r(() => o[0] || (o[0] = [
                        J("span", null, "深度封装表单，用户只需定义字段json即可渲染表单，优点简单便捷，缺点扩展有难度", -1)
                      ])),
                      default: r(() => [
                        u(d, { value: "vue3" }, {
                          default: r(() => o[1] || (o[1] = [
                            V("封装表单(BasicForm)")
                          ])),
                          _: 1
                        })
                      ]),
                      _: 1
                    }),
                    u(l, { placement: "top" }, {
                      title: r(() => o[2] || (o[2] = [
                        J("span", null, "antd的原生表单，所有字段都需要硬编码，缺点编码繁琐，优点扩展容易", -1)
                      ])),
                      default: r(() => [
                        C.jspMode == "innerTable" || C.jspMode == "tab" ? $("", !0) : (L(), I(d, {
                          key: 0,
                          value: "vue3Native"
                        }, {
                          default: r(() => o[3] || (o[3] = [
                            V("原生表单(a-form)")
                          ])),
                          _: 1
                        }))
                      ]),
                      _: 2
                    }, 1024)
                  ]),
                  _: 2
                }, 1032, ["value", "onUpdate:value"])
              ]),
              _: 1
            }, 8, ["onRegister"]),
            t.showSubTable ? (L(), I(h, {
              key: 0,
              title: "子表信息",
              size: "small"
            }, {
              default: r(() => [
                u(N, pe({
                  ref: "subTableRef",
                  rowNumber: "",
                  maxHeight: 580
                }, t.subTable), null, 16)
              ]),
              _: 1
            })) : $("", !0)
          ]),
          _: 1
        }, 8, ["spinning"])
      ]),
      _: 1
    }, 8, ["onRegister", "title", "width", "confirmLoading", "onOk", "onCancel", "wrapClassName"]),
    u(b, {
      onRegister: t.registerFileSelectModal,
      onSelect: t.onFileSelect
    }, null, 8, ["onRegister", "onSelect"]),
    u(B, { onRegister: t.registerCodeFileListModal }, null, 8, ["onRegister"])
  ], 64);
}
const Ke = /* @__PURE__ */ Ne(ye, [["render", Te]]);
export {
  Ke as default
};
