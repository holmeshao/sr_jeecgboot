import { ref as i, unref as l } from "vue";
import { useRoute as a } from "vue-router";
import { useUserStore as f } from "/@/store/modules/user";
import { message as k } from "ant-design-vue";
function g() {
  const n = a(), e = i(n.query.token), o = i(""), t = f();
  l(e) || (t.getToken ? e.value = t.getToken : r());
  function r() {
    const c = k.loading("获取token中...", 0);
    setTimeout(() => {
      o.value = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTAwMTc2NzUsInVzZXJuYW1lIjoiYWRtaW4ifQ.0q5ywkLF144SaqumsSgEXO5ERtqaYp8jqouIUxxhELc", u(), s(), c();
    }, 3e3);
  }
  function u() {
    t.setToken(o.value);
  }
  function s() {
    window.location.replace(`${n.fullPath}?token=${o.value}`);
  }
  return { token: e };
}
export {
  g as u
};
