/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jeecg.common.system.vo.DictModel
 *  org.jeecg.common.util.oConvertUtils
 */
package org.jeecg.modules.online.cgform.converter.a;

import java.util.List;
import java.util.Map;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.converter.FieldCommentConverter;

public class b
implements FieldCommentConverter {
    protected String a;
    protected List<DictModel> b;

    public String getFiled() {
        return this.a;
    }

    public void setFiled(String filed) {
        this.a = filed;
    }

    public List<DictModel> getDictList() {
        return this.b;
    }

    public void setDictList(List<DictModel> dictList) {
        this.b = dictList;
    }

    @Override
    public String converterToVal(String txt) {
        if (oConvertUtils.isNotEmpty((Object)txt)) {
            for (DictModel dictModel : this.b) {
                if (!dictModel.getText().equals(txt)) continue;
                return dictModel.getValue();
            }
        }
        return null;
    }

    @Override
    public String converterToTxt(String val) {
        if (oConvertUtils.isNotEmpty((Object)val)) {
            for (DictModel dictModel : this.b) {
                if (dictModel.getValue() == null || !dictModel.getValue().equals(val)) continue;
                return dictModel.getText();
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getConfig() {
        return null;
    }
}

