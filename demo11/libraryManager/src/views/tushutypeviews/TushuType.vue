
<template>
  <div>
    <el-row justify="space-between" align="middle" style="margin-bottom: 16px;">
      <el-col>
        <el-button type="primary" @click="fetchCategories">刷新分类</el-button>
      </el-col>
      <el-col>
        <el-button @click="openAddDialog">新增分类</el-button>
        <el-button
          type="warning"
          :disabled="!selectedNode"
          @click="openEditDialog"
          style="margin-left: 8px"
        >
          修改分类
        </el-button>
        <el-button
          type="danger"
          :disabled="!selectedNode"
          @click="handleDelete"
          style="margin-left: 8px"
        >
          删除分类
        </el-button>
      </el-col>
    </el-row>

    <el-tree
      :data="categoryTree"
      :props="defaultProps"
      node-key="id"
      highlight-current
      default-expand-all
      @node-click="handleNodeClick"
      style="border: 1px solid #ebeef5; padding: 12px; border-radius: 4px;"
    >
      <template #default="{ node, data }">
        <span>{{ data.name }}</span>
      </template>
    </el-tree>

    <!-- 新增分类弹窗 -->
    <el-dialog
      title="新增分类"
      v-model="dialogAddVisible"
      width="400px"
      :z-index="9999"
    >
      <el-form :model="formAdd" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="formAdd.name" autocomplete="off" />
        </el-form-item>
<el-form-item label="父分类">
  <el-tree-select
    v-model="formAdd.parentId"
    :data="categoryTree"
    :props="defaultProps"
    placeholder="请选择父分类（可不选）"
    clearable
    default-expand-all
    check-strictly
    style="width: 100%;"
  />
</el-form-item>



      </el-form>
      <template #footer>
        <el-button @click="dialogAddVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAdd">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改分类弹窗 -->
    <el-dialog
      title="修改分类"
      v-model="dialogEditVisible"
      width="400px"
      :z-index="9999"
    >
      <el-form :model="formEdit" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="formEdit.name" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogEditVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import api from '@/utils/api'
import { ElMessage } from 'element-plus'

// 表单数据
const formAdd = reactive({
  name: '',
  parentId: null
})
const formEdit = reactive({
  id: null,
  name: ''
})

// 弹窗状态
const dialogAddVisible = ref(false)
const dialogEditVisible = ref(false)

// 控制新增弹窗
function openAddDialog() {
  dialogAddVisible.value = true
  formAdd.name = ''
  formAdd.parentId = null
}

// 控制修改弹窗
function openEditDialog() {
  if (selectedNode.value) {
    formEdit.id = selectedNode.value.id
    formEdit.name = selectedNode.value.name
    dialogEditVisible.value = true
  }
}

// 分类数据
const categoryTree = ref([])
const defaultProps = {
  children: 'children',
  label: 'name',
  value: 'id'
}

// 当前选中节点
const selectedNode = ref(null)

function handleNodeClick(data) {
  selectedNode.value = data
  formEdit.id = data.id
  formEdit.name = data.name
}

// 包装分类数据，添加“无父分类”作为顶层可选项
function buildCategoryTreeWithRoot(categories) {
  return [
    {
      id: null,
      name: '无父分类',
      children: []
    },
    ...categories
  ]
}

// 获取分类列表
async function fetchCategories() {
  try {
    const res = await api.get('api/bookCategory/tree')
    if (Array.isArray(res)) {
      categoryTree.value = buildCategoryTreeWithRoot(res)
    }
  } catch (err) {
    ElMessage.error('加载分类失败')
  }
}

// 新增分类
async function handleAdd() {
  if (!formAdd.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }

  const postData = {
    name: formAdd.name.trim(),
    parent: formAdd.parentId !== null ? { id: formAdd.parentId } : null
  }

  try {
    await api.post('api/bookCategory', postData)
    ElMessage.success('新增成功')
    dialogAddVisible.value = false
    formAdd.name = ''
    formAdd.parentId = null
    await fetchCategories()
  } catch (error) {
    ElMessage.error('新增分类失败')
  }
}

// 修改分类
async function handleEdit() {
  if (!formEdit.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }
  try {
    await api.put(`api/bookCategory/${formEdit.id}`, {
      name: formEdit.name.trim()
    })
    ElMessage.success('修改成功')
    dialogEditVisible.value = false
    await fetchCategories()
  } catch (error) {
    ElMessage.error('修改分类失败')
  }
}

// 删除分类
async function handleDelete() {
  try {
    await api.delete(`api/bookCategory/${selectedNode.value.id}`)
    ElMessage.success('删除成功')
    selectedNode.value = null
    await fetchCategories()
  } catch (err) {
    ElMessage.error('删除失败')
  }
}

// 初始化加载
onMounted(() => {
  fetchCategories()
})
</script>


<style>
.el-popper {
  z-index: 10000 !important;
}
</style>
