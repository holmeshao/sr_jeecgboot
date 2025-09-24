import { createContext as i } from "/@/hooks/core/useContext";
import r from "/@/utils/mitt";
import "vue";
const m = Symbol(), p = "openpopmodal";
function u(t) {
  const n = r();
  function o(e) {
    t(e);
  }
  n.on(p, o), a({
    onlineEmitter: n
  });
}
function a(t) {
  return i(t, m, { readonly: !1, native: !0 });
}
export {
  u
};
