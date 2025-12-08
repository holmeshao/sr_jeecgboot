<template>
  <div class="tools-index-page">
    <a-page-header title="工具箱" sub-title="常用数据处理工具集">
      <template #extra>
        <a-tag color="blue">v1.0.0</a-tag>
      </template>
    </a-page-header>

    <div class="tools-grid">
      <!-- Excel 工具 -->
      <a-card hoverable class="tool-card" @click="goTo('/tools/excel/merge')">
        <template #cover>
          <div class="tool-icon excel">
            <FileExcelOutlined />
          </div>
        </template>
        <a-card-meta title="Excel 合并">
          <template #description>
            将多个 Excel 文件合并为一个，支持 ZIP 包批量处理
          </template>
        </a-card-meta>
        <template #actions>
          <span><ArrowRightOutlined /> 立即使用</span>
        </template>
      </a-card>

      <!-- 预留：Excel 拆分 -->
      <a-card class="tool-card coming-soon">
        <template #cover>
          <div class="tool-icon excel disabled">
            <SplitCellsOutlined />
          </div>
        </template>
        <a-card-meta title="Excel 拆分">
          <template #description>
            将一个 Excel 按条件拆分为多个文件
          </template>
        </a-card-meta>
        <template #actions>
          <a-tag>即将推出</a-tag>
        </template>
      </a-card>

      <!-- 预留：数据去重 -->
      <a-card class="tool-card coming-soon">
        <template #cover>
          <div class="tool-icon data disabled">
            <CopyOutlined />
          </div>
        </template>
        <a-card-meta title="数据去重">
          <template #description>
            按指定列去除 Excel 中的重复数据
          </template>
        </a-card-meta>
        <template #actions>
          <a-tag>即将推出</a-tag>
        </template>
      </a-card>

      <!-- 预留：格式转换 -->
      <a-card class="tool-card coming-soon">
        <template #cover>
          <div class="tool-icon convert disabled">
            <SwapOutlined />
          </div>
        </template>
        <a-card-meta title="格式转换">
          <template #description>
            Excel、CSV、JSON 格式互转
          </template>
        </a-card-meta>
        <template #actions>
          <a-tag>即将推出</a-tag>
        </template>
      </a-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import {
  FileExcelOutlined,
  SplitCellsOutlined,
  CopyOutlined,
  SwapOutlined,
  ArrowRightOutlined,
} from '@ant-design/icons-vue';

const router = useRouter();

const goTo = (path: string) => {
  router.push(path);
};
</script>

<style lang="less" scoped>
.tools-index-page {
  padding: 16px;
  
  .tools-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 24px;
    margin-top: 24px;
  }
  
  .tool-card {
    cursor: pointer;
    transition: all 0.3s;
    
    &:hover:not(.coming-soon) {
      transform: translateY(-4px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
    
    &.coming-soon {
      cursor: not-allowed;
      opacity: 0.6;
    }
    
    .tool-icon {
      height: 120px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 48px;
      
      &.excel {
        background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
        color: white;
      }
      
      &.data {
        background: linear-gradient(135deg, #1890ff 0%, #40a9ff 100%);
        color: white;
      }
      
      &.convert {
        background: linear-gradient(135deg, #722ed1 0%, #9254de 100%);
        color: white;
      }
      
      &.disabled {
        filter: grayscale(100%);
      }
    }
  }
}
</style>
