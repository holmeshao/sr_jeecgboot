import { E as e } from "./cgform.data-0ca62d09.mjs";
function i(s) {
  let o = {};
  if (s.extConfigJson)
    try {
      o = JSON.parse(s.extConfigJson);
    } catch (t) {
    }
  return Object.assign({}, e, o, {
    isDesForm: s.isDesForm || "N",
    desFormCode: s.desFormCode || ""
  });
}
export {
  i as p
};
