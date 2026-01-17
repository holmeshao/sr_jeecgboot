/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.poi.hssf.usermodel.HSSFCellStyle
 *  org.apache.poi.hssf.usermodel.HSSFRichTextString
 *  org.apache.poi.hssf.usermodel.HSSFSheet
 *  org.apache.poi.hssf.usermodel.HSSFWorkbook
 *  org.apache.poi.ss.usermodel.BorderStyle
 *  org.apache.poi.ss.usermodel.Cell
 *  org.apache.poi.ss.usermodel.CellStyle
 *  org.apache.poi.ss.usermodel.FillPatternType
 *  org.apache.poi.ss.usermodel.HorizontalAlignment
 *  org.apache.poi.ss.usermodel.IndexedColors
 *  org.apache.poi.ss.usermodel.RichTextString
 *  org.apache.poi.ss.usermodel.Row
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 */
package org.jeecg.modules.online.cgreport.service.a;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.jeecg.modules.online.cgreport.service.CgReportExcelServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service(value="cgReportExcelService")
public class a
implements CgReportExcelServiceI {
    private static final Logger a = LoggerFactory.getLogger(a.class);

    @Override
    public HSSFWorkbook exportExcel(String title, Collection<?> titleSet, Collection<?> dataSet) {
        HSSFWorkbook hSSFWorkbook = null;
        try {
            Object object;
            if (titleSet == null || titleSet.size() == 0) {
                throw new Exception("\u8bfb\u53d6\u8868\u5934\u5931\u8d25\uff01");
            }
            if (title == null) {
                title = "";
            }
            hSSFWorkbook = new HSSFWorkbook();
            HSSFSheet hSSFSheet = hSSFWorkbook.createSheet(title);
            int n = 0;
            int n2 = 0;
            Row row = hSSFSheet.createRow(n);
            row.setHeight((short)450);
            HSSFCellStyle hSSFCellStyle = this.a(hSSFWorkbook);
            List list = (List)titleSet;
            Iterator<?> iterator = dataSet.iterator();
            for (Map map : list) {
                String string = (String)map.get("field_txt");
                Object object2 = row.createCell(n2);
                object = new HSSFRichTextString(string);
                object2.setCellValue((RichTextString)object);
                object2.setCellStyle((CellStyle)hSSFCellStyle);
                ++n2;
            }
            HSSFCellStyle hSSFCellStyle2 = this.c(hSSFWorkbook);
            while (iterator.hasNext()) {
                Map map;
                n2 = 0;
                row = hSSFSheet.createRow(++n);
                map = (Map)iterator.next();
                for (Object object2 : list) {
                    object = (String)object2.get("field_name");
                    String string = map.get(object) == null ? "" : map.get(object).toString();
                    Cell cell = row.createCell(n2);
                    HSSFRichTextString hSSFRichTextString = new HSSFRichTextString(string);
                    cell.setCellStyle((CellStyle)hSSFCellStyle2);
                    cell.setCellValue((RichTextString)hSSFRichTextString);
                    ++n2;
                }
            }
            for (int i2 = 0; i2 < list.size(); ++i2) {
                hSSFSheet.autoSizeColumn(i2);
            }
        }
        catch (Exception exception) {
            a.error(exception.getMessage(), (Throwable)exception);
        }
        return hSSFWorkbook;
    }

    private HSSFCellStyle a(HSSFWorkbook hSSFWorkbook) {
        HSSFCellStyle hSSFCellStyle = hSSFWorkbook.createCellStyle();
        hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
        hSSFCellStyle.setBorderRight(BorderStyle.THIN);
        hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
        hSSFCellStyle.setBorderTop(BorderStyle.THIN);
        hSSFCellStyle.setAlignment(HorizontalAlignment.CENTER);
        hSSFCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        hSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return hSSFCellStyle;
    }

    private void a(int n, int n2, HSSFWorkbook hSSFWorkbook) {
        HSSFSheet hSSFSheet = hSSFWorkbook.getSheetAt(0);
        HSSFCellStyle hSSFCellStyle = this.c(hSSFWorkbook);
        for (int i2 = 1; i2 <= n; ++i2) {
            Row row = hSSFSheet.createRow(i2);
            for (int i3 = 0; i3 < n2; ++i3) {
                row.createCell(i3).setCellStyle((CellStyle)hSSFCellStyle);
            }
        }
    }

    private HSSFCellStyle b(HSSFWorkbook hSSFWorkbook) {
        HSSFCellStyle hSSFCellStyle = hSSFWorkbook.createCellStyle();
        hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
        hSSFCellStyle.setBorderRight(BorderStyle.THIN);
        hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
        hSSFCellStyle.setBorderTop(BorderStyle.THIN);
        hSSFCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.index);
        hSSFCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return hSSFCellStyle;
    }

    private HSSFCellStyle c(HSSFWorkbook hSSFWorkbook) {
        HSSFCellStyle hSSFCellStyle = hSSFWorkbook.createCellStyle();
        hSSFCellStyle.setBorderLeft(BorderStyle.THIN);
        hSSFCellStyle.setBorderRight(BorderStyle.THIN);
        hSSFCellStyle.setBorderBottom(BorderStyle.THIN);
        hSSFCellStyle.setBorderTop(BorderStyle.THIN);
        return hSSFCellStyle;
    }
}

