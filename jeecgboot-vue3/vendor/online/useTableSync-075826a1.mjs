var w = (f, m, e) => new Promise((d, u) => {
  var n = (t) => {
    try {
      s(e.next(t));
    } catch (b) {
      u(b);
    }
  }, T = (t) => {
    try {
      s(e.throw(t));
    } catch (b) {
      u(b);
    }
  }, s = (t) => t.done ? d(t.value) : Promise.resolve(t.value).then(n, T);
  s((e = e.apply(f, m)).next());
});
import { inject as h, ref as y, computed as g, nextTick as v } from "vue";
import { V as N } from "./cgform.data-0ca62d09.mjs";
import { pick as R } from "lodash-es";
function L(f) {
  const m = h("tables"), e = h("fullScreenRef"), d = y(), u = y(!1), n = y([]), T = g(() => ({
    // 正常表格高度
    normal: e != null && e.value ? 430 : 260,
    // 没有 toolbar 的表格高度
    noToolbar: e != null && e.value ? 480 : 320
  })), s = g(() => ["id"].concat(f.value.map((l) => l.key))), t = g(() => ({
    // 针对Online表单对虚拟滚动做出优化
    // 虚拟滚动配置，y轴（行数）大于xx条数据时启用虚拟滚动
    // update-begin--author:liaozhiyang---date:20231025---for：【QQYUN-6808】online编辑字段多了卡顿
    scrollY: {
      enabled: !0,
      gt: 15
    },
    // 列数
    scrollX: {
      enabled: !0,
      gt: 20
    }
    // update-begin--author:liaozhiyang---date:20231025---for：【QQYUN-6808】online编辑字段多了卡顿
  }));
  function b(l) {
    return w(this, null, function* () {
      let a = d.value;
      if (yield a.fullValidateTable())
        throw { code: N, activeKey: l };
      let D = a.getTableData().map((c) => R(c, s.value)), F = a.getDeleteData().map((c) => c.id);
      return { tableData: D, deleteIds: F };
    });
  }
  function k(l, a = !1) {
    return w(this, null, function* () {
      a ? (n.value = [], yield v(), yield d.value.addOrInsert(l, 0, null, { setActive: !1 }), yield v(), d.value.recalcDisableRows()) : (n.value = l, yield v(), d.value.recalcDisableRows());
    });
  }
  function E(l) {
    let a = d.value, x = l.value.tableRef, D = l.value.getRemoveIds(), F = x.getXTable().internalData.tableFullData, c = a.getXTable().internalData.tableFullData;
    return F.forEach((r) => {
      let p = !1;
      if (c.forEach((o) => {
        if (r.id === o.id) {
          let i = o.dbFieldName, I = o.dbFieldTxt;
          (r.dbFieldName !== i || r.dbFieldTxt !== I) && a.setValues([
            {
              rowKey: o.id,
              values: {
                dbFieldName: r.dbFieldName,
                dbFieldTxt: r.dbFieldTxt
              }
            }
          ]), p = !0;
        } else
          D.forEach((i) => {
            i === o.id && (setTimeout(() => {
              a.removeRowsById(i);
            }, 0), p = !0);
          });
      }), !p) {
        let o = Object.assign({}, r);
        f.value.forEach((i) => {
          i.key !== "dbFieldName" && i.key !== "dbFieldTxt" && (o[i.key] = i.defaultValue);
        }), a.addRows(o);
      }
    }), v();
  }
  return { tables: m, tableRef: d, loading: u, dataSource: n, columnKeys: s, tableHeight: T, tableProps: t, syncTable: E, validateData: b, setDataSource: k };
}
export {
  L as u
};
