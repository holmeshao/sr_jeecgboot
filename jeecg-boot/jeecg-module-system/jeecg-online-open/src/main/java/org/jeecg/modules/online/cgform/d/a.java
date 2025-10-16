/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.beans.BeanUtils
 */
package org.jeecg.modules.online.cgform.d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jeecg.modules.online.cgform.entity.OnlCgformButton;
import org.springframework.beans.BeanUtils;

public class a {
    private static final List<OnlCgformButton> a = new ArrayList<OnlCgformButton>();
    private static final Set<String> b = new HashSet<String>();

    private static void a(String string, String string2, String string3, int n) {
        OnlCgformButton onlCgformButton = new OnlCgformButton();
        onlCgformButton.setButtonCode(string);
        onlCgformButton.setButtonName(string2);
        onlCgformButton.setButtonIcon(string3);
        onlCgformButton.setOrderNum(n);
        onlCgformButton.setButtonStyle("built-in");
        onlCgformButton.setButtonStatus("1");
        a.add(onlCgformButton);
        b.add(string);
    }

    public static Set<String> getButtonCodeSet() {
        return b;
    }

    public static List<OnlCgformButton> a(String string, List<OnlCgformButton> list) {
        ArrayList<OnlCgformButton> arrayList = new ArrayList<OnlCgformButton>();
        for (OnlCgformButton onlCgformButton : a) {
            OnlCgformButton onlCgformButton3 = list.stream().filter(onlCgformButton2 -> onlCgformButton.getButtonCode().equals(onlCgformButton2.getButtonCode())).findFirst().orElse(null);
            if (onlCgformButton3 == null) {
                OnlCgformButton onlCgformButton4 = new OnlCgformButton();
                BeanUtils.copyProperties((Object)onlCgformButton, (Object)onlCgformButton4);
                onlCgformButton4.setCgformHeadId(string);
                arrayList.add(onlCgformButton4);
                continue;
            }
            onlCgformButton3.setOrderNum(onlCgformButton.getOrderNum());
            arrayList.add(onlCgformButton3);
        }
        arrayList.sort(Comparator.comparing(OnlCgformButton::getOrderNum));
        return arrayList;
    }

    static {
        int n = 0;
        org.jeecg.modules.online.cgform.d.a.a("add", "\u65b0\u589e", "plus-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("edit", "\u7f16\u8f91", "", ++n);
        org.jeecg.modules.online.cgform.d.a.a("detail", "\u8be6\u60c5", "", ++n);
        org.jeecg.modules.online.cgform.d.a.a("delete", "\u5220\u9664", "", ++n);
        org.jeecg.modules.online.cgform.d.a.a("batch_delete", "\u6279\u91cf\u5220\u9664", "delete-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("import", "\u5bfc\u5165", "import-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("export", "\u5bfc\u51fa", "export-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("query", "\u67e5\u8be2", "search", ++n);
        org.jeecg.modules.online.cgform.d.a.a("reset", "\u91cd\u7f6e", "reload", ++n);
        org.jeecg.modules.online.cgform.d.a.a("bpm", "\u63d0\u4ea4\u6d41\u7a0b", "", ++n);
        org.jeecg.modules.online.cgform.d.a.a("super_query", "\u9ad8\u7ea7\u67e5\u8be2", "filter-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("form_confirm", "\u786e\u5b9a", "", ++n);
        org.jeecg.modules.online.cgform.d.a.a("form_sub_add", "\u65b0\u589e", "plus-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("form_sub_batch_delete", "\u5220\u9664", "minus-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("form_sub_open_add", "\u65b0\u589e", "expand-alt-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("form_sub_open_edit", "", "form-outlined", ++n);
        org.jeecg.modules.online.cgform.d.a.a("aigc_mock_data", "\u751f\u6210\u6d4b\u8bd5\u6570\u636e", "robot-love-outline", ++n);
    }
}

