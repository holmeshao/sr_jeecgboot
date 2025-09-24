import { defineStore as t } from "pinia";
import "/@/store";
const h = t({
  id: "cgform-state",
  state: () => ({
    changedTables: []
  }),
  getters: {},
  actions: {
    /**
     * 检查是否同步过数据库
     * @param tableId 表id
     */
    checkIsChanged(e) {
      return this.changedTables.includes(e);
    },
    addChangedTable(e) {
      this.changedTables.push(e);
    },
    removeChangedTable(e) {
      const s = this.changedTables.findIndex((a) => a === e);
      s !== -1 && this.changedTables.splice(s, 1);
    }
  }
});
export {
  h as u
};
