/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.poi.xssf.usermodel.XSSFWorkbook
 *  org.jeecgframework.poi.excel.entity.params.ExcelExportEntity
 */
package org.jeecg.modules.online.cgform.service;

import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.model.e;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;

public interface IOnlineJoinQueryService {
    public Map<String, Object> pageList(OnlCgformHead var1, Map<String, Object> var2, boolean var3);

    public Map<String, Object> pageList(OnlCgformHead var1, Map<String, Object> var2);

    public e getQueryInfo(OnlCgformHead var1, Map<String, Object> var2, boolean var3);

    public e getQueryInfo(OnlCgformHead var1, Map<String, Object> var2, boolean var3, boolean var4);

    public XSSFWorkbook handleOnlineExport(OnlCgformHead var1, Map<String, Object> var2);

    public void addAllSubTableDate(String var1, Map<String, Object> var2, List<Map<String, Object>> var3, List<ExcelExportEntity> var4, boolean var5);
}

