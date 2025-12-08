<template>
  <div class="excel-merge-page">
    <a-card title="Excel 文件合并" :bordered="false">
      <template #extra>
        <a-space>
          <a-button @click="showHelp = true">
            <QuestionCircleOutlined />
            使用帮助
          </a-button>
        </a-space>
      </template>

      <!-- 上传区域 -->
      <div class="upload-section">
        <a-upload-dragger
          v-model:fileList="fileList"
          :multiple="true"
          :beforeUpload="beforeUpload"
          :accept="'.xlsx,.xls,.zip'"
          :showUploadList="false"
        >
          <p class="ant-upload-drag-icon">
            <InboxOutlined />
          </p>
          <p class="ant-upload-text">点击或拖拽文件到此区域</p>
          <p class="ant-upload-hint">
            支持 .xlsx、.xls 文件，或包含 Excel 文件的 .zip 压缩包<br />
            单次最多 500 个文件，总大小不超过 100MB
          </p>
        </a-upload-dragger>
      </div>

      <!-- 文件列表 -->
      <div v-if="fileList.length > 0" class="file-list-section">
        <div class="file-list-header">
          <span>已选择 {{ fileList.length }} 个文件</span>
          <a-button type="link" danger size="small" @click="clearFiles">
            <DeleteOutlined />
            清空
          </a-button>
        </div>
        
        <div class="file-list">
          <a-tag 
            v-for="(file, index) in displayFiles" 
            :key="index"
            closable
            @close="removeFile(index)"
            :color="getFileColor(file.name)"
          >
            <FileExcelOutlined v-if="isExcelFile(file.name)" />
            <FileZipOutlined v-else />
            {{ file.name }}
            <span class="file-size">({{ formatFileSize(file.size) }})</span>
          </a-tag>
          <span v-if="fileList.length > 10" class="more-files">
            ... 还有 {{ fileList.length - 10 }} 个文件
          </span>
        </div>
        
        <div class="file-stats">
          总大小：{{ formatFileSize(totalSize) }}
        </div>
      </div>

      <!-- 合并选项 -->
      <a-divider>合并选项</a-divider>
      
      <a-form layout="inline" class="merge-options">
        <a-form-item label="跳过表头">
          <a-switch v-model:checked="options.skipHeader" />
          <a-tooltip title="合并时跳过每个文件的第一行（表头行），只保留第一个文件的表头">
            <QuestionCircleOutlined class="option-tip" />
          </a-tooltip>
        </a-form-item>
        
        <a-form-item label="添加来源列">
          <a-switch v-model:checked="options.addSourceColumn" />
          <a-tooltip title="在合并后的数据末尾添加一列，记录每行数据的来源文件名">
            <QuestionCircleOutlined class="option-tip" />
          </a-tooltip>
        </a-form-item>
        
        <a-form-item label="去重列">
          <a-input-number 
            v-model:value="options.deduplicateColumn" 
            :min="-1" 
            :max="100"
            placeholder="列索引"
            style="width: 100px"
          />
          <a-tooltip title="按指定列（从0开始）去除重复数据，-1表示不去重">
            <QuestionCircleOutlined class="option-tip" />
          </a-tooltip>
        </a-form-item>
      </a-form>

      <!-- 操作按钮 -->
      <div class="action-section">
        <a-space size="large">
          <a-button 
            type="primary" 
            size="large"
            :loading="merging"
            :disabled="fileList.length === 0"
            @click="handleMerge"
          >
            <MergeCellsOutlined />
            {{ merging ? '合并中...' : '开始合并' }}
          </a-button>
          
          <a-button 
            size="large"
            :loading="previewing"
            :disabled="fileList.length === 0"
            @click="handlePreview"
          >
            <EyeOutlined />
            预览检查
          </a-button>
        </a-space>
      </div>

      <!-- 结果展示 -->
      <div v-if="mergeResult" class="result-section">
        <a-alert
          :type="mergeResult.success ? 'success' : 'error'"
          :message="mergeResult.success ? '合并完成' : '合并失败'"
          show-icon
        >
          <template #description>
            <div class="result-detail">
              <p>{{ mergeResult.message }}</p>
              <div v-if="mergeResult.success" class="result-stats">
                <a-statistic-countdown 
                  v-if="false"
                  title="处理文件" 
                  :value="mergeResult.totalFiles" 
                />
                <a-row :gutter="16">
                  <a-col :span="6">
                    <a-statistic title="总文件数" :value="mergeResult.totalFiles" />
                  </a-col>
                  <a-col :span="6">
                    <a-statistic title="成功" :value="mergeResult.successFiles" :value-style="{ color: '#3f8600' }" />
                  </a-col>
                  <a-col :span="6">
                    <a-statistic title="失败" :value="mergeResult.failedFiles" :value-style="{ color: mergeResult.failedFiles > 0 ? '#cf1322' : undefined }" />
                  </a-col>
                  <a-col :span="6">
                    <a-statistic title="数据行数" :value="mergeResult.totalRows" />
                  </a-col>
                </a-row>
              </div>
              
              <!-- 错误详情 -->
              <div v-if="mergeResult.errors && mergeResult.errors.length > 0" class="error-list">
                <a-collapse>
                  <a-collapse-panel header="查看失败详情">
                    <a-list size="small" :dataSource="mergeResult.errors">
                      <template #renderItem="{ item }">
                        <a-list-item>
                          <a-list-item-meta>
                            <template #title>
                              <span class="error-file">{{ item.fileName }}</span>
                            </template>
                            <template #description>
                              {{ item.reason }}
                            </template>
                          </a-list-item-meta>
                        </a-list-item>
                      </template>
                    </a-list>
                  </a-collapse-panel>
                </a-collapse>
              </div>
            </div>
          </template>
        </a-alert>
      </div>
    </a-card>

    <!-- 帮助弹窗 -->
    <a-modal v-model:open="showHelp" title="使用帮助" :footer="null" width="600px">
      <a-typography>
        <a-typography-title :level="5">功能说明</a-typography-title>
        <a-typography-paragraph>
          将多个 Excel 文件合并为一个文件，适用于收集分散的数据（如问卷、表格模板填写后的回收）。
        </a-typography-paragraph>
        
        <a-typography-title :level="5">支持的文件格式</a-typography-title>
        <a-typography-paragraph>
          <ul>
            <li><strong>.xlsx</strong> - Excel 2007+ 格式</li>
            <li><strong>.xls</strong> - Excel 97-2003 格式</li>
            <li><strong>.zip</strong> - 包含 Excel 文件的压缩包</li>
          </ul>
        </a-typography-paragraph>
        
        <a-typography-title :level="5">合并选项说明</a-typography-title>
        <a-typography-paragraph>
          <ul>
            <li><strong>跳过表头</strong>：开启后，只保留第一个文件的表头，其他文件的第一行会被跳过</li>
            <li><strong>添加来源列</strong>：在合并结果的最后一列添加文件名，方便追溯数据来源</li>
            <li><strong>去重列</strong>：按指定列的值去除重复行，列索引从0开始，-1表示不去重</li>
          </ul>
        </a-typography-paragraph>
        
        <a-typography-title :level="5">使用限制</a-typography-title>
        <a-typography-paragraph>
          <ul>
            <li>单次最多上传 500 个文件</li>
            <li>文件总大小不超过 100MB</li>
            <li>只读取每个文件的第一个工作表</li>
          </ul>
        </a-typography-paragraph>
      </a-typography>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { message } from 'ant-design-vue';
import {
  InboxOutlined,
  DeleteOutlined,
  FileExcelOutlined,
  FileZipOutlined,
  QuestionCircleOutlined,
  MergeCellsOutlined,
  EyeOutlined,
} from '@ant-design/icons-vue';
import { defHttp } from '/@/utils/http/axios';
import type { UploadProps } from 'ant-design-vue';

// 文件列表
const fileList = ref<File[]>([]);
const showHelp = ref(false);
const merging = ref(false);
const previewing = ref(false);

// 合并选项
const options = ref({
  skipHeader: true,
  addSourceColumn: false,
  deduplicateColumn: -1,
});

// 合并结果
const mergeResult = ref<any>(null);

// 计算属性
const totalSize = computed(() => {
  return fileList.value.reduce((sum, file) => sum + file.size, 0);
});

const displayFiles = computed(() => {
  return fileList.value.slice(0, 10);
});

// 方法
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  // 检查文件类型
  const isValid = isExcelFile(file.name) || file.name.toLowerCase().endsWith('.zip');
  if (!isValid) {
    message.error(`${file.name} 不是支持的文件格式`);
    return false;
  }
  
  // 添加到文件列表
  fileList.value.push(file);
  return false; // 阻止自动上传
};

const isExcelFile = (name: string) => {
  const lower = name.toLowerCase();
  return lower.endsWith('.xlsx') || lower.endsWith('.xls');
};

const getFileColor = (name: string) => {
  if (name.toLowerCase().endsWith('.zip')) return 'purple';
  if (name.toLowerCase().endsWith('.xlsx')) return 'green';
  return 'blue';
};

const formatFileSize = (bytes: number) => {
  if (bytes < 1024) return bytes + ' B';
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
  return (bytes / (1024 * 1024)).toFixed(2) + ' MB';
};

const removeFile = (index: number) => {
  fileList.value.splice(index, 1);
};

const clearFiles = () => {
  fileList.value = [];
  mergeResult.value = null;
};

// 预览检查
const handlePreview = async () => {
  if (fileList.value.length === 0) {
    message.warning('请先选择文件');
    return;
  }
  
  previewing.value = true;
  mergeResult.value = null;
  
  try {
    const formData = new FormData();
    fileList.value.forEach((file) => {
      formData.append('files', file);
    });
    formData.append('skipHeader', String(options.value.skipHeader));
    formData.append('addSourceColumn', String(options.value.addSourceColumn));
    formData.append('deduplicateColumn', String(options.value.deduplicateColumn));
    
    const result = await defHttp.post({
      url: '/tools/excel/merge/preview',
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    
    mergeResult.value = result;
    
  } catch (error: any) {
    message.error(error.message || '预览失败');
  } finally {
    previewing.value = false;
  }
};

// 执行合并
const handleMerge = async () => {
  if (fileList.value.length === 0) {
    message.warning('请先选择文件');
    return;
  }
  
  merging.value = true;
  mergeResult.value = null;
  
  try {
    const formData = new FormData();
    fileList.value.forEach((file) => {
      formData.append('files', file);
    });
    formData.append('skipHeader', String(options.value.skipHeader));
    formData.append('addSourceColumn', String(options.value.addSourceColumn));
    formData.append('deduplicateColumn', String(options.value.deduplicateColumn));
    
    // 使用原生 fetch 处理文件下载
    const response = await fetch('/jeecg-boot/tools/excel/merge', {
      method: 'POST',
      body: formData,
      headers: {
        'X-Access-Token': localStorage.getItem('ACCESS_TOKEN') || '',
      },
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || '合并失败');
    }
    
    // 检查是否返回错误 JSON
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      const errorData = await response.json();
      if (!errorData.success) {
        throw new Error(errorData.message || '合并失败');
      }
    }
    
    // 获取结果信息
    const mergeResultHeader = response.headers.get('X-Merge-Result');
    const totalFiles = response.headers.get('X-Total-Files');
    const successFiles = response.headers.get('X-Success-Files');
    const failedFiles = response.headers.get('X-Failed-Files');
    const totalRows = response.headers.get('X-Total-Rows');
    
    mergeResult.value = {
      success: true,
      message: mergeResultHeader ? decodeURIComponent(mergeResultHeader) : '合并成功',
      totalFiles: parseInt(totalFiles || '0'),
      successFiles: parseInt(successFiles || '0'),
      failedFiles: parseInt(failedFiles || '0'),
      totalRows: parseInt(totalRows || '0'),
    };
    
    // 下载文件
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    
    // 从响应头获取文件名
    const disposition = response.headers.get('content-disposition');
    let filename = '合并结果.xlsx';
    if (disposition) {
      const filenameMatch = disposition.match(/filename\*=UTF-8''(.+)/);
      if (filenameMatch) {
        filename = decodeURIComponent(filenameMatch[1]);
      }
    }
    
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
    
    message.success('合并完成，文件已下载');
    
  } catch (error: any) {
    message.error(error.message || '合并失败');
    mergeResult.value = {
      success: false,
      message: error.message || '合并失败',
    };
  } finally {
    merging.value = false;
  }
};
</script>

<style lang="less" scoped>
.excel-merge-page {
  padding: 16px;
  
  .upload-section {
    margin-bottom: 24px;
  }
  
  .file-list-section {
    background: #fafafa;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 16px;
    
    .file-list-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      font-weight: 500;
    }
    
    .file-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      
      .ant-tag {
        display: flex;
        align-items: center;
        gap: 4px;
        
        .file-size {
          font-size: 12px;
          color: #999;
        }
      }
      
      .more-files {
        color: #999;
        font-size: 12px;
        line-height: 22px;
      }
    }
    
    .file-stats {
      margin-top: 12px;
      color: #666;
      font-size: 13px;
    }
  }
  
  .merge-options {
    margin-bottom: 24px;
    
    .option-tip {
      margin-left: 4px;
      color: #999;
      cursor: help;
    }
  }
  
  .action-section {
    text-align: center;
    margin: 32px 0;
  }
  
  .result-section {
    margin-top: 24px;
    
    .result-detail {
      .result-stats {
        margin-top: 16px;
      }
      
      .error-list {
        margin-top: 16px;
        
        .error-file {
          color: #cf1322;
        }
      }
    }
  }
}
</style>
