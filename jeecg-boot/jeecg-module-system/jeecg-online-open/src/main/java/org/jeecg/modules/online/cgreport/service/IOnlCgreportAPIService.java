/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.poi.ss.usermodel.Workbook
 */
package org.jeecg.modules.online.cgreport.service;

import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;

public interface IOnlCgreportAPIService {
    public Map<String, Object> getDataById(String var1, Map<String, Object> var2);

    public Map<String, Object> getDataByCode(String var1, Map<String, Object> var2);

    public Map<String, Object> getData(String var1, String var2, Map<String, Object> var3);

    public Map<String, Object> executeSelectSqlRoute(String var1, String var2, Map<String, Object> var3, String var4) throws Exception;

    public Workbook getReportWorkbook(String var1, Map<String, Object> var2);
}

