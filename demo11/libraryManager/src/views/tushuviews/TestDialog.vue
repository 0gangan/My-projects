<template>
  <div>
    <el-button type="primary" @click="dialogVisible = true">打开弹窗</el-button>

    <el-dialog
      v-model:visible="dialogVisible"
      title="测试弹窗"
      width="30%"
    >
      <span>这是一个测试弹窗。</span>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const dialogVisible = ref(false)
</script>
