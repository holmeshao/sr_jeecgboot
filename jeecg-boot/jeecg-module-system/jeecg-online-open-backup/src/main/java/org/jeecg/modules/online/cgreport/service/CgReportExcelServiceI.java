/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.poi.hssf.usermodel.HSSFWorkbook
 */
package org.jeecg.modules.online.cgreport.service;

import java.util.Collection;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface CgReportExcelServiceI {
    public HSSFWorkbook exportExcel(String var1, Collection<?> var2, Collection<?> var3);
}

