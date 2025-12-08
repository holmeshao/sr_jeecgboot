package org.jeecg.modules.tools.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeecg.modules.tools.dto.ExcelMergeResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Excel 合并服务
 * 
 * 支持功能：
 * - 多个 Excel 文件合并
 * - ZIP 包内 Excel 文件合并
 * - 可选跳过表头
 * - 可选添加来源列
 * - 可选去重
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Slf4j
@Service
public class ExcelMergeService {
    
    // 支持的 Excel 文件扩展名
    private static final Set<String> EXCEL_EXTENSIONS = new HashSet<>(Arrays.asList("xlsx", "xls"));
    
    /**
     * 合并多个 Excel 文件
     * 
     * @param files 上传的文件列表（可以是 Excel 文件或 ZIP 包）
     * @param skipHeader 是否跳过每个文件的表头（第一行）
     * @param addSourceColumn 是否添加来源列（记录原文件名）
     * @param deduplicateColumn 去重列索引（-1 表示不去重）
     * @return 合并后的 Excel 字节数组和结果信息
     */
    public MergeOutput merge(MultipartFile[] files, boolean skipHeader, 
                             boolean addSourceColumn, int deduplicateColumn) {
        
        log.info("开始合并Excel文件: 文件数={}, skipHeader={}, addSourceColumn={}, deduplicateColumn={}", 
                 files.length, skipHeader, addSourceColumn, deduplicateColumn);
        
        ExcelMergeResult result = new ExcelMergeResult();
        List<ExcelFileData> allFileData = new ArrayList<>();
        
        // 1. 解析所有文件
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                result.addError("未知文件", "文件名为空");
                continue;
            }
            
            try {
                if (fileName.toLowerCase().endsWith(".zip")) {
                    // 处理 ZIP 包
                    List<ExcelFileData> zipData = parseZipFile(file, result);
                    allFileData.addAll(zipData);
                } else if (isExcelFile(fileName)) {
                    // 处理单个 Excel 文件
                    ExcelFileData data = parseExcelFile(file.getInputStream(), fileName);
                    if (data != null) {
                        allFileData.add(data);
                        result.incrementSuccess();
                    } else {
                        result.addError(fileName, "解析失败，文件可能为空或格式错误");
                    }
                } else {
                    result.addError(fileName, "不支持的文件格式，仅支持 .xlsx, .xls, .zip");
                }
            } catch (Exception e) {
                log.error("处理文件失败: {}", fileName, e);
                result.addError(fileName, "处理异常: " + e.getMessage());
            }
        }
        
        result.setTotalFiles(result.getSuccessFiles() + result.getFailedFiles());
        
        if (allFileData.isEmpty()) {
            result.setSuccess(false);
            result.setMessage("没有有效的数据可以合并");
            return new MergeOutput(null, result);
        }
        
        // 2. 合并数据
        try {
            byte[] mergedData = mergeExcelData(allFileData, skipHeader, addSourceColumn, deduplicateColumn, result);
            result.setSuccess(true);
            result.setMessage(String.format("合并完成：共%d个文件，成功%d个，失败%d个，共%d条数据", 
                result.getTotalFiles(), result.getSuccessFiles(), result.getFailedFiles(), result.getTotalRows()));
            
            return new MergeOutput(mergedData, result);
        } catch (Exception e) {
            log.error("合并Excel数据失败", e);
            result.setSuccess(false);
            result.setMessage("合并失败: " + e.getMessage());
            return new MergeOutput(null, result);
        }
    }
    
    /**
     * 解析 ZIP 文件中的所有 Excel
     */
    private List<ExcelFileData> parseZipFile(MultipartFile zipFile, ExcelMergeResult result) throws Exception {
        List<ExcelFileData> dataList = new ArrayList<>();
        
        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                
                // 跳过目录和隐藏文件
                if (entry.isDirectory() || entryName.startsWith("__MACOSX") || entryName.startsWith(".")) {
                    continue;
                }
                
                // 获取实际文件名（去掉路径）
                String fileName = entryName;
                if (entryName.contains("/")) {
                    fileName = entryName.substring(entryName.lastIndexOf("/") + 1);
                }
                
                if (isExcelFile(fileName)) {
                    try {
                        // 读取 ZIP 条目内容到内存
                        byte[] content = readZipEntry(zis);
                        ExcelFileData data = parseExcelBytes(content, fileName);
                        if (data != null) {
                            dataList.add(data);
                            result.incrementSuccess();
                        } else {
                            result.addError(fileName, "ZIP内文件解析失败");
                        }
                    } catch (Exception e) {
                        result.addError(fileName, "ZIP内文件处理异常: " + e.getMessage());
                    }
                }
                zis.closeEntry();
            }
        }
        
        return dataList;
    }
    
    /**
     * 读取 ZIP 条目内容
     */
    private byte[] readZipEntry(ZipInputStream zis) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        while ((len = zis.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }
    
    /**
     * 解析 Excel 文件
     */
    private ExcelFileData parseExcelFile(InputStream is, String fileName) {
        try (Workbook workbook = WorkbookFactory.create(is)) {
            return extractData(workbook, fileName);
        } catch (Exception e) {
            log.error("解析Excel文件失败: {}", fileName, e);
            return null;
        }
    }
    
    /**
     * 从字节数组解析 Excel
     */
    private ExcelFileData parseExcelBytes(byte[] content, String fileName) {
        try (Workbook workbook = WorkbookFactory.create(new java.io.ByteArrayInputStream(content))) {
            return extractData(workbook, fileName);
        } catch (Exception e) {
            log.error("解析Excel字节数据失败: {}", fileName, e);
            return null;
        }
    }
    
    /**
     * 提取 Workbook 数据
     */
    private ExcelFileData extractData(Workbook workbook, String fileName) {
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
            return null;
        }
        
        ExcelFileData data = new ExcelFileData();
        data.setFileName(fileName);
        
        List<List<Object>> rows = new ArrayList<>();
        int lastRowNum = sheet.getLastRowNum();
        
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            
            List<Object> rowData = new ArrayList<>();
            int lastCellNum = row.getLastCellNum();
            
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                rowData.add(getCellValue(cell));
            }
            
            // 跳过完全空行
            if (!isEmptyRow(rowData)) {
                rows.add(rowData);
            }
        }
        
        data.setRows(rows);
        return data;
    }
    
    /**
     * 获取单元格值
     */
    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                // 避免科学计数法
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue) && !Double.isInfinite(numValue)) {
                    return (long) numValue;
                }
                return numValue;
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return cell.getNumericCellValue();
                    } catch (Exception e2) {
                        return "";
                    }
                }
            case BLANK:
            default:
                return "";
        }
    }
    
    /**
     * 判断是否为空行
     */
    private boolean isEmptyRow(List<Object> row) {
        for (Object cell : row) {
            if (cell != null && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 合并 Excel 数据
     */
    private byte[] mergeExcelData(List<ExcelFileData> allData, boolean skipHeader, 
                                   boolean addSourceColumn, int deduplicateColumn,
                                   ExcelMergeResult result) throws Exception {
        
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("合并数据");
            
            int currentRowNum = 0;
            boolean headerWritten = false;
            Set<String> deduplicateSet = new HashSet<>();
            List<Object> headerRow = null;
            
            for (ExcelFileData fileData : allData) {
                List<List<Object>> rows = fileData.getRows();
                if (rows.isEmpty()) {
                    continue;
                }
                
                int startIndex = 0;
                
                // 处理表头
                if (!headerWritten && !rows.isEmpty()) {
                    headerRow = new ArrayList<>(rows.get(0));
                    if (addSourceColumn) {
                        headerRow.add("来源文件");
                    }
                    writeRow(sheet, currentRowNum++, headerRow, workbook, true);
                    headerWritten = true;
                    startIndex = 1;
                } else if (skipHeader && !rows.isEmpty()) {
                    startIndex = 1;
                }
                
                // 写入数据行
                for (int i = startIndex; i < rows.size(); i++) {
                    List<Object> rowData = new ArrayList<>(rows.get(i));
                    
                    // 去重检查
                    if (deduplicateColumn >= 0 && deduplicateColumn < rowData.size()) {
                        String key = String.valueOf(rowData.get(deduplicateColumn));
                        if (deduplicateSet.contains(key)) {
                            continue; // 跳过重复行
                        }
                        deduplicateSet.add(key);
                    }
                    
                    // 添加来源列
                    if (addSourceColumn) {
                        rowData.add(fileData.getFileName());
                    }
                    
                    writeRow(sheet, currentRowNum++, rowData, workbook, false);
                    result.addRows(1);
                }
            }
            
            // 自动调整列宽
            if (headerRow != null) {
                for (int i = 0; i < headerRow.size(); i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            
            // 输出到字节数组
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        }
    }
    
    /**
     * 写入行数据
     */
    private void writeRow(Sheet sheet, int rowNum, List<Object> rowData, Workbook workbook, boolean isHeader) {
        Row row = sheet.createRow(rowNum);
        
        CellStyle style = null;
        if (isHeader) {
            style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        
        for (int i = 0; i < rowData.size(); i++) {
            Cell cell = row.createCell(i);
            Object value = rowData.get(i);
            
            if (value == null) {
                cell.setCellValue("");
            } else if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if (value instanceof Date) {
                cell.setCellValue((Date) value);
            } else {
                cell.setCellValue(value.toString());
            }
            
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }
    
    /**
     * 判断是否为 Excel 文件
     */
    private boolean isExcelFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        String lowerName = fileName.toLowerCase();
        return lowerName.endsWith(".xlsx") || lowerName.endsWith(".xls");
    }
    
    // ========== 内部类 ==========
    
    /**
     * Excel 文件数据
     */
    @lombok.Data
    private static class ExcelFileData {
        private String fileName;
        private List<List<Object>> rows = new ArrayList<>();
    }
    
    /**
     * 合并输出结果
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class MergeOutput {
        private byte[] data;
        private ExcelMergeResult result;
    }
}
