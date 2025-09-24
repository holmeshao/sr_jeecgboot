var T = (s, _, p) => new Promise((c, g) => {
  var C = (l) => {
    try {
      m(p.next(l));
    } catch (i) {
      g(i);
    }
  }, u = (l) => {
    try {
      m(p.throw(l));
    } catch (i) {
      g(i);
    }
  }, m = (l) => l.done ? c(l.value) : Promise.resolve(l.value).then(C, u);
  m((p = p.apply(s, _)).next());
});
import { defineComponent as ae, ref as r, reactive as J, computed as R, unref as U, watch as ne, resolveComponent as k, openBlock as f, createElementBlock as b, createElementVNode as t, normalizeClass as x, createVNode as a, Fragment as h, createTextVNode as S, createCommentVNode as j, withCtx as v } from "vue";
import oe from "./AppLoginHeader-4432c584.mjs";
import { useI18n as se } from "/@/hooks/web/useI18n";
import { defHttp as q } from "/@/utils/http/axios";
import { useMessage as ie } from "/@/hooks/web/useMessage";
import { router as P } from "/@/router";
import { PageEnum as V } from "/@/enums/pageEnum";
import { useUserStore as re } from "/@/store/modules/user";
import { _ as ue } from "./index-9e1e1e53.mjs";
import "/@/assets/images/logo.png";
import "/@/components/jeecg/OnLine/JPopupOnlReport.vue";
import "vue-router";
const me = (s) => q.post({ url: "/sys/tenant/saveTenantJoinUser", params: s }, { isTransformResponse: !1 }), ve = (s) => q.post({ url: "/sys/tenant/joinTenantByHouseNumber", params: s }, { isTransformResponse: !1 }), pe = { class: "organ-box" }, ce = {
  class: "align-items-center",
  style: { "margin-top": "20px" }
}, de = {
  class: "align-items-center",
  style: { "margin-top": "20px" }
}, fe = { class: "content-item" }, be = { class: "content-item" }, ge = { class: "content-item" }, ye = { class: "content-item" }, ke = /* @__PURE__ */ ae({
  __name: "AppTenant",
  props: {
    tenantType: { type: String, default: "" }
  },
  emits: ["success"],
  setup(s, { emit: _ }) {
    const p = s, { createMessage: c } = ie(), g = r(), C = r(), u = r("home"), m = J({
      houseNumber: ""
    }), l = J({
      name: "",
      trade: void 0,
      companySize: void 0,
      position: void 0,
      department: void 0
    }), i = r(""), M = R(() => m.houseNumber != "" || U(i) === "houseNumber" ? "current-active" : ""), L = R(() => l.name != "" || U(i) === "name" ? "current-active" : ""), w = r(), I = r(), $ = {
      houseNumber: [{ required: !0, message: "请输入组织门牌号" }]
    };
    se();
    const K = R(() => Y("请输入组织名称")), D = R(() => ({
      name: U(K)
    })), G = r([
      { label: "信息传输、软件和信息技术服务业", value: "1" },
      { label: "制造业", value: "2" },
      { label: "租赁和商务服务业", value: "3" },
      { label: "教育", value: "4" },
      { label: "金融业", value: "5" },
      { label: "建筑业", value: "6" },
      { label: "科学研究和技术服务业", value: "7" },
      { label: "批发和零售业", value: "8" },
      { label: "住宿和餐饮业", value: "9" },
      { label: "电子商务", value: "10" },
      { label: "线下零售与服务业", value: "11" },
      { label: "文化、体育和娱乐业", value: "12" },
      { label: "房地产业", value: "13" },
      { label: "交通运输、仓储和邮政业", value: "14" },
      { label: "卫生和社会工作", value: "15" },
      { label: "公共管理、社会保障和社会组织", value: "16" },
      { label: "电力、热力、燃气及水生产和供应业", value: "17" },
      { label: "水利、环境和公共设施管理业", value: "18" },
      { label: "居民服务、修理和其他服务业", value: "19" },
      { label: "政府机构", value: "20" },
      { label: "农、林、牧、渔业", value: "21" },
      { label: "采矿业", value: "22" },
      { label: "国际组织", value: "23" },
      { label: "其他", value: "24" }
    ]), Q = r([
      { label: "20人以下", value: "1" },
      { label: "21-99人", value: "2" },
      { label: "100-499人", value: "3" },
      { label: "500-999人", value: "4" },
      { label: "1000-9999人", value: "5" },
      { label: "10000人以上", value: "6" }
    ]), W = r([
      { label: "总裁/总经理/CEO", value: "1" },
      { label: "副总裁/副总经理/VP", value: "2" },
      { label: "总监/主管/经理", value: "3" },
      { label: "员工/专员/执行", value: "4" },
      { label: "其他", value: "5" }
    ]), X = r([
      { label: "总经办", value: "1" },
      { label: "技术/IT/研发", value: "2" },
      { label: "产品/设计", value: "3" },
      { label: "销售/市场/运营", value: "4" },
      { label: "人事/财务/行政", value: "5" },
      { label: "资源/仓储/采购", value: "6" },
      { label: "其他", value: "7" }
    ]), O = _, y = re();
    function Y(n) {
      return [
        {
          required: !0,
          message: n,
          trigger: "change"
        }
      ];
    }
    function Z() {
      u.value = "join";
    }
    function ee() {
      u.value = "create";
    }
    function B(n) {
      i.value = n, n === "houseNumber" ? w.value.focus() : I.value.focus();
    }
    function E() {
      i.value = "";
    }
    function te() {
      return T(this, null, function* () {
        g.value.validateFields().then((n) => T(this, null, function* () {
          n.status = "1", yield me(n).then((e) => {
            e.success ? (c.success(e.message), y.setTenant(e.result), P.replace(y.getUserInfo && y.getUserInfo.homePath || V.BASE_HOME), O("success")) : c.warning(e.message);
          });
        }));
      });
    }
    function le() {
      C.value.validateFields().then((n) => T(this, null, function* () {
        yield ve(n).then((e) => {
          e.success ? (c.success(e.message), p.tenantType || P.replace(y.getUserInfo && y.getUserInfo.homePath || V.BASE_HOME), O("success")) : c.warning(e.message);
        }).catch((e) => {
          c.warning(e.message);
        });
      }));
    }
    function H() {
      u.value = "home";
    }
    return ne(
      () => p.tenantType,
      (n) => {
        n && (u.value = n);
      },
      { immediate: !0 }
    ), (n, e) => {
      const z = k("Icon"), A = k("a-input"), d = k("a-form-item"), F = k("a-form"), N = k("a-select");
      return f(), b("div", pe, [
        t("div", {
          class: x(["organ-subject", s.tenantType !== "" ? "subject-margin" : ""])
        }, [
          a(oe, { showLocalePicker: !1 }),
          u.value === "home" ? (f(), b(h, { key: 0 }, [
            e[10] || (e[10] = t("div", {
              class: "flex-row align-items-center",
              style: { "margin-top": "20px" }
            }, [
              t("div", { class: "organ-title" }, " 创建或者加入组织 ")
            ], -1)),
            t("div", { class: "organ-desc align-items-center" }, [
              t("div", {
                class: "content-box pointer",
                onClick: Z
              }, e[8] || (e[8] = [
                t("div", { class: "organ-title-desc" }, [
                  S(" 加入"),
                  t("span", { style: { color: "rgb(141, 198, 216)" } }, "已有"),
                  S("组织 ")
                ], -1),
                t("div", null, " 如果被告知要使用，或有同事已经在用，请选择此项。 ", -1)
              ])),
              t("div", {
                class: "content-box pointer",
                style: { "margin-top": "35px" },
                onClick: ee
              }, e[9] || (e[9] = [
                t("div", { class: "organ-title-desc" }, [
                  S(" 创建"),
                  t("span", { style: { color: "rgb(74, 79, 175)" } }, "新的"),
                  S("组织 ")
                ], -1),
                t("div", null, " 如果想为企业或者组织创建账号，请选择此项。 ", -1)
              ]))
            ])
          ], 64)) : u.value === "join" ? (f(), b(h, { key: 1 }, [
            s.tenantType === "" ? (f(), b("div", {
              key: 0,
              class: "margin-top40 pointer",
              style: { display: "flex" },
              onClick: H
            }, [
              a(z, {
                icon: "ant-design:arrow-left-outlined",
                style: { color: "#757575", "margin-top": "2px" }
              }),
              e[11] || (e[11] = t("span", { style: { "margin-left": "10px" } }, "返回", -1))
            ])) : j("", !0),
            t("div", ce, [
              e[13] || (e[13] = t("div", { class: "organ-title" }, " 请填写组织门牌号 ", -1)),
              e[14] || (e[14] = t("div", { class: "organ-join-desc" }, [
                t("span", { style: { color: "#9e9e9e" } }, "组织门牌号可以通过管理员获取")
              ], -1)),
              a(F, {
                ref_key: "joinRef",
                ref: C,
                model: m,
                rules: $
              }, {
                default: v(() => [
                  t("div", {
                    class: x(["content-item", M.value]),
                    onClick: e[1] || (e[1] = (o) => B("houseNumber"))
                  }, [
                    a(d, { name: "houseNumber" }, {
                      default: v(() => [
                        a(A, {
                          ref_key: "hoseNumberRef",
                          ref: w,
                          value: m.houseNumber,
                          "onUpdate:value": e[0] || (e[0] = (o) => m.houseNumber = o),
                          style: { height: "40px" },
                          onBlur: E
                        }, null, 8, ["value"]),
                        t("div", {
                          class: x(["form-title", i.value === "houseNumber" ? "active-title" : ""])
                        }, " 示例: J9A2K8R ", 2)
                      ]),
                      _: 1
                    })
                  ], 2),
                  e[12] || (e[12] = t("div", { class: "tenant-number" }, [
                    t("a", {
                      href: "http://help.qiaoqiaoyun.com/org/new.html",
                      target: "_blank"
                    }, "没有组织门牌号？")
                  ], -1)),
                  t("div", {
                    class: "create-btn pointer",
                    onClick: le
                  }, " 加入 ")
                ]),
                _: 1
              }, 8, ["model"])
            ])
          ], 64)) : u.value === "create" ? (f(), b(h, { key: 2 }, [
            s.tenantType === "" ? (f(), b("div", {
              key: 0,
              class: "margin-top40 pointer",
              style: { display: "flex" },
              onClick: H
            }, [
              a(z, {
                icon: "ant-design:arrow-left-outlined",
                style: { color: "#757575", "margin-top": "2px" }
              }),
              e[15] || (e[15] = t("span", { style: { "margin-left": "10px" } }, "返回", -1))
            ])) : j("", !0),
            t("div", de, [
              e[20] || (e[20] = t("div", { class: "organ-title" }, " 创建组织 ", -1)),
              e[21] || (e[21] = t("div", { class: "organ-join-desc" }, [
                t("span", { style: { color: "#9e9e9e" } }, "您当前账号默认成为组织的管理员")
              ], -1)),
              a(F, {
                ref_key: "createRef",
                ref: g,
                model: l,
                rules: D.value
              }, {
                default: v(() => [
                  t("div", {
                    class: x(["content-item", L.value]),
                    onClick: e[3] || (e[3] = (o) => B("name"))
                  }, [
                    a(d, { name: "name" }, {
                      default: v(() => [
                        a(A, {
                          ref_key: "nameRef",
                          ref: I,
                          value: l.name,
                          "onUpdate:value": e[2] || (e[2] = (o) => l.name = o),
                          style: { height: "40px", "font-size": "13px", color: "rgba(0, 0, 0, 0.65)" },
                          onBlur: E
                        }, null, 8, ["value"]),
                        t("div", {
                          class: x(["form-title", i.value === "name" ? "active-title" : ""])
                        }, " 组织名称 ", 2)
                      ]),
                      _: 1
                    })
                  ], 2),
                  t("div", fe, [
                    a(d, { name: "trade" }, {
                      default: v(() => [
                        a(N, {
                          placeholder: "请选择",
                          style: { height: "40px" },
                          options: G.value,
                          value: l.trade,
                          "onUpdate:value": e[4] || (e[4] = (o) => l.trade = o)
                        }, null, 8, ["options", "value"]),
                        e[16] || (e[16] = t("div", { class: "active-form-title" }, " 行业 ", -1))
                      ]),
                      _: 1
                    })
                  ]),
                  t("div", be, [
                    a(d, { name: "companySize" }, {
                      default: v(() => [
                        a(N, {
                          placeholder: "请选择",
                          style: { height: "40px" },
                          options: Q.value,
                          value: l.companySize,
                          "onUpdate:value": e[5] || (e[5] = (o) => l.companySize = o)
                        }, null, 8, ["options", "value"]),
                        e[17] || (e[17] = t("div", { class: "active-form-title" }, " 规模 ", -1))
                      ]),
                      _: 1
                    })
                  ]),
                  t("div", ge, [
                    a(d, { name: "position" }, {
                      default: v(() => [
                        a(N, {
                          placeholder: "请选择",
                          style: { height: "40px" },
                          options: W.value,
                          value: l.position,
                          "onUpdate:value": e[6] || (e[6] = (o) => l.position = o)
                        }, null, 8, ["options", "value"]),
                        e[18] || (e[18] = t("div", { class: "active-form-title" }, " 您的职级 ", -1))
                      ]),
                      _: 1
                    })
                  ]),
                  t("div", ye, [
                    a(d, { name: "department" }, {
                      default: v(() => [
                        a(N, {
                          placeholder: "请选择",
                          style: { height: "40px" },
                          options: X.value,
                          value: l.department,
                          "onUpdate:value": e[7] || (e[7] = (o) => l.department = o)
                        }, null, 8, ["options", "value"]),
                        e[19] || (e[19] = t("div", { class: "active-form-title" }, " 您的部门 ", -1))
                      ]),
                      _: 1
                    })
                  ]),
                  t("div", {
                    class: "create-btn pointer",
                    onClick: te
                  }, " 创建 ")
                ]),
                _: 1
              }, 8, ["model", "rules"])
            ])
          ], 64)) : j("", !0)
        ], 2)
      ]);
    };
  }
});
const Be = /* @__PURE__ */ ue(ke, [["__scopeId", "data-v-5bf68799"]]);
export {
  Be as default
};
