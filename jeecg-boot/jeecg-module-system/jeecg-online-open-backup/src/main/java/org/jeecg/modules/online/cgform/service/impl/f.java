/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  org.apache.ibatis.session.ExecutorType
 *  org.apache.ibatis.session.SqlSession
 *  org.jeecg.common.util.SpringContextUtils
 *  org.mybatis.spring.SqlSessionTemplate
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.online.cgform.converter.b;
import org.jeecg.modules.online.cgform.d.c;
import org.jeecg.modules.online.cgform.d.j;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.enums.EnhanceDataEnum;
import org.jeecg.modules.online.cgform.mapper.OnlCgformFieldMapper;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlCgformSqlService;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="onlCgformSqlServiceImpl")
public class f
implements IOnlCgformSqlService {
    private static final Logger a = LoggerFactory.getLogger(f.class);
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Override
    public void saveBatchOnlineTable(OnlCgformHead head, List<OnlCgformField> fieldList, List<Map<String, Object>> dataList) throws BusinessException {
        int n = 0;
        try (SqlSession sqlSession = null;){
            b.a(2, dataList, fieldList);
            sqlSession = this.sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
            OnlCgformFieldMapper onlCgformFieldMapper = (OnlCgformFieldMapper)sqlSession.getMapper(OnlCgformFieldMapper.class);
            int n2 = 1000;
            if (n2 >= dataList.size()) {
                int n3 = 0;
                while (n3 < dataList.size()) {
                    String string = JSON.toJSONString(dataList.get(n3));
                    this.a(string, head, fieldList, onlCgformFieldMapper);
                    ++n3;
                    ++n;
                }
            } else {
                int n4 = 0;
                while (n4 < dataList.size()) {
                    String string = JSON.toJSONString(dataList.get(n4));
                    this.a(string, head, fieldList, onlCgformFieldMapper);
                    if (n4 % n2 == 0) {
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    ++n4;
                    ++n;
                }
            }
            sqlSession.commit();
        }
    }

    private String a(Throwable throwable) {
        if (throwable.getCause() != null) {
            return this.a(throwable.getCause());
        }
        return throwable.getMessage();
    }

    @Override
    public void saveOrUpdateSubData(String subDataJsonStr, OnlCgformHead head, List<OnlCgformField> subFiledList) throws BusinessException {
        OnlCgformFieldMapper onlCgformFieldMapper = (OnlCgformFieldMapper)SpringContextUtils.getBean(OnlCgformFieldMapper.class);
        this.a(subDataJsonStr, head, subFiledList, onlCgformFieldMapper);
    }

    @Override
    public Map<String, String> saveOnlineImportDataWithValidate(OnlCgformHead head, List<OnlCgformField> fieldList, List<Map<String, Object>> dataList) {
        StringBuffer stringBuffer = new StringBuffer();
        j j2 = new j(fieldList);
        OnlCgformFieldMapper onlCgformFieldMapper = (OnlCgformFieldMapper)SpringContextUtils.getBean(OnlCgformFieldMapper.class);
        int n = 0;
        int n2 = 0;
        b.a(2, dataList, fieldList);
        int n3 = dataList.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            String string = JSON.toJSONString(dataList.get(i2));
            String string2 = j2.a(string, ++n);
            if (string2 == null) {
                try {
                    this.a(string, head, fieldList, onlCgformFieldMapper);
                }
                catch (Exception exception) {
                    ++n2;
                    String string3 = null;
                    string3 = exception.getCause() != null ? this.a(exception.getCause().getMessage()) : this.a(exception.getMessage());
                    String string4 = j.b(string3, n);
                    stringBuffer.append(string4);
                }
                continue;
            }
            ++n2;
            stringBuffer.append(string2);
        }
        HashMap<String, String> hashMap = new HashMap<String, String>(5);
        hashMap.put("error", stringBuffer.toString());
        hashMap.put("tip", j.a(n3, n2));
        return hashMap;
    }

    private void a(String string, OnlCgformHead onlCgformHead, List<OnlCgformField> list, OnlCgformFieldMapper onlCgformFieldMapper) throws BusinessException {
        JSONObject jSONObject = JSONObject.parseObject((String)string);
        EnhanceDataEnum enhanceDataEnum = this.onlCgformHeadService.executeEnhanceImport(onlCgformHead, jSONObject);
        String string2 = onlCgformHead.getTableName();
        if (EnhanceDataEnum.INSERT == enhanceDataEnum) {
            Map<String, Object> map = c.a(string2, list, jSONObject);
            onlCgformFieldMapper.executeInsertSQL(map);
        } else if (EnhanceDataEnum.UPDATE == enhanceDataEnum) {
            Map<String, Object> map = c.b(string2, list, jSONObject);
            onlCgformFieldMapper.executeUpdatetSQL(map);
        } else if (EnhanceDataEnum.ABANDON == enhanceDataEnum) {
            // empty if block
        }
    }

    private String a(String string) {
        String string2 = "^Duplicate entry \\'(.*)\\' for key .*$";
        Pattern pattern = Pattern.compile(string2);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return "\u91cd\u590d\u6570\u636e" + matcher.group(1);
        }
        return string;
    }
}

