package org.jeecg.modules.tools.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.tools.dto.ExcelMergeResult;
import org.jeecg.modules.tools.service.ExcelMergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Excel 工具控制器
 * 
 * 提供 Excel 文件处理相关的工具接口
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Slf4j
@RestController
@RequestMapping("/tools/excel")
@Tag(name = "工具箱-Excel工具")
public class ExcelToolController {
    
    @Autowired
    private ExcelMergeService excelMergeService;
    
    /**
     * 合并多个 Excel 文件
     * 
     * 支持：
     * - 直接上传多个 .xlsx/.xls 文件
     * - 上传包含多个 Excel 文件的 .zip 压缩包
     * - 混合上传（Excel 文件 + ZIP 包）
     * 
     * @param files 上传的文件
     * @param skipHeader 是否跳过每个文件的表头（首行），默认 true
     * @param addSourceColumn 是否添加来源列（记录原文件名），默认 false
     * @param deduplicateColumn 去重列索引（从0开始，-1表示不去重），默认 -1
     * @return 合并后的 Excel 文件
     */
    @AutoLog(value = "工具箱-Excel合并")
    @Operation(summary = "合并Excel文件", description = "将多个Excel文件合并为一个")
    @PostMapping(value = "/merge", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> mergeExcel(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "skipHeader", defaultValue = "true") boolean skipHeader,
            @RequestParam(value = "addSourceColumn", defaultValue = "false") boolean addSourceColumn,
            @RequestParam(value = "deduplicateColumn", defaultValue = "-1") int deduplicateColumn) {
        
        log.info("Excel合并请求: 文件数={}, skipHeader={}, addSourceColumn={}, deduplicateColumn={}", 
                 files.length, skipHeader, addSourceColumn, deduplicateColumn);
        
        // 参数校验
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body(Result.error("请上传至少一个文件"));
        }
        
        // 限制文件数量
        if (files.length > 500) {
            return ResponseEntity.badRequest().body(Result.error("单次最多支持500个文件"));
        }
        
        // 检查总大小（限制 100MB）
        long totalSize = 0;
        for (MultipartFile file : files) {
            totalSize += file.getSize();
        }
        if (totalSize > 100 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Result.error("文件总大小不能超过100MB"));
        }
        
        try {
            // 执行合并
            ExcelMergeService.MergeOutput output = excelMergeService.merge(
                files, skipHeader, addSourceColumn, deduplicateColumn);
            
            ExcelMergeResult result = output.getResult();
            
            if (!result.isSuccess() || output.getData() == null) {
                return ResponseEntity.ok(Result.error(result.getMessage()));
            }
            
            // 生成文件名
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "合并结果_" + timestamp + ".xlsx";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20");
            
            // 返回文件流
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                    .header("X-Merge-Result", URLEncoder.encode(result.getMessage(), StandardCharsets.UTF_8.toString()))
                    .header("X-Total-Files", String.valueOf(result.getTotalFiles()))
                    .header("X-Success-Files", String.valueOf(result.getSuccessFiles()))
                    .header("X-Failed-Files", String.valueOf(result.getFailedFiles()))
                    .header("X-Total-Rows", String.valueOf(result.getTotalRows()))
                    .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, 
                            "X-Merge-Result,X-Total-Files,X-Success-Files,X-Failed-Files,X-Total-Rows")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(output.getData());
                    
        } catch (Exception e) {
            log.error("Excel合并失败", e);
            return ResponseEntity.internalServerError().body(Result.error("合并失败: " + e.getMessage()));
        }
    }
    
    /**
     * 仅预览合并结果（不下载文件）
     * 
     * 用于前端先检查文件是否能正常解析
     */
    @AutoLog(value = "工具箱-Excel合并预览")
    @Operation(summary = "预览合并结果", description = "检查文件并返回预览信息，不生成合并文件")
    @PostMapping(value = "/merge/preview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<ExcelMergeResult> previewMerge(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "skipHeader", defaultValue = "true") boolean skipHeader,
            @RequestParam(value = "addSourceColumn", defaultValue = "false") boolean addSourceColumn,
            @RequestParam(value = "deduplicateColumn", defaultValue = "-1") int deduplicateColumn) {
        
        if (files == null || files.length == 0) {
            return Result.error("请上传至少一个文件");
        }
        
        try {
            ExcelMergeService.MergeOutput output = excelMergeService.merge(
                files, skipHeader, addSourceColumn, deduplicateColumn);
            
            return Result.OK(output.getResult());
        } catch (Exception e) {
            log.error("Excel合并预览失败", e);
            return Result.error("预览失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取工具信息
     */
    @Operation(summary = "获取Excel工具信息")
    @GetMapping("/info")
    public Result<?> getInfo() {
        return Result.OK(new java.util.HashMap<String, Object>() {{
            put("name", "Excel工具箱");
            put("version", "1.0.0");
            put("tools", new String[] {
                "Excel合并 - 将多个Excel文件合并为一个",
                // 预留扩展
                // "Excel拆分 - 按条件将一个Excel拆分为多个",
                // "Excel去重 - 按指定列去除重复数据",
            });
            put("limits", new java.util.HashMap<String, Object>() {{
                put("maxFiles", 500);
                put("maxTotalSize", "100MB");
                put("supportedFormats", new String[] {".xlsx", ".xls", ".zip"});
            }});
        }});
    }
}
