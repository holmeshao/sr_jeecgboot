package org.jeecg.modules.tools.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 合并结果 DTO
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Data
public class ExcelMergeResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 是否成功
     */
    private boolean success = true;
    
    /**
     * 总文件数
     */
    private int totalFiles;
    
    /**
     * 成功处理的文件数
     */
    private int successFiles;
    
    /**
     * 失败的文件数
     */
    private int failedFiles;
    
    /**
     * 合并后的总数据行数（不含表头）
     */
    private int totalRows;
    
    /**
     * 失败文件详情
     */
    private List<FileError> errors = new ArrayList<>();
    
    /**
     * 处理消息
     */
    private String message;
    
    /**
     * 文件错误信息
     */
    @Data
    public static class FileError implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 文件名
         */
        private String fileName;
        
        /**
         * 错误原因
         */
        private String reason;
        
        public FileError() {}
        
        public FileError(String fileName, String reason) {
            this.fileName = fileName;
            this.reason = reason;
        }
    }
    
    // ========== 便捷方法 ==========
    
    public void addError(String fileName, String reason) {
        this.errors.add(new FileError(fileName, reason));
        this.failedFiles++;
    }
    
    public void incrementSuccess() {
        this.successFiles++;
    }
    
    public void addRows(int rows) {
        this.totalRows += rows;
    }
    
    /**
     * 构建成功结果
     */
    public static ExcelMergeResult success(int totalFiles, int successFiles, int totalRows) {
        ExcelMergeResult result = new ExcelMergeResult();
        result.setSuccess(true);
        result.setTotalFiles(totalFiles);
        result.setSuccessFiles(successFiles);
        result.setFailedFiles(totalFiles - successFiles);
        result.setTotalRows(totalRows);
        result.setMessage(String.format("合并完成：共%d个文件，成功%d个，共%d条数据", 
            totalFiles, successFiles, totalRows));
        return result;
    }
    
    /**
     * 构建失败结果
     */
    public static ExcelMergeResult fail(String message) {
        ExcelMergeResult result = new ExcelMergeResult();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}
