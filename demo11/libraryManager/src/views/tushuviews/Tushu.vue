<template>
  <div>
    <el-button type="primary" @click="showAddDialog">添加图书</el-button>
    <!-- 新增的刷新按钮 -->
    <el-button type="success" style="margin-left: 10px" @click="loadBooks">刷新列表</el-button>

    <el-table :data="bookList" style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="category" label="分类" />
      <el-table-column prop="publisher" label="出版社" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteBook(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <AddBook ref="addBookRef" @refresh="loadBooks" />
    <EditBook ref="editBookRef" @refresh="loadBooks" />
  </div>
</template>

<script>
import AddBook from './AddBook.vue'
import EditBook from './EditBook.vue'

export default {
  name: 'Tushu',
  components: { AddBook, EditBook },
  data() {
    return {
      bookList: []
    }
  },
  methods: {
    loadBooks() {
      this.$api.get('api/books', null)
        .then(res => {
          console.log('后端返回数据:', res)
          if (Array.isArray(res)) {
            this.bookList = res
          } else {
            this.$message.error('返回数据格式错误')
          }
        })
        .catch(() => {
          this.$message.error('请求图书列表失败')
        })
    },
    showAddDialog() {
      this.$refs.addBookRef.openDialog()
    },
    showEditDialog(row) {
      this.$refs.editBookRef.openDialog(row)
    },
    deleteBook(row) {
      this.$confirm(`此操作将永久删除《${row.title}》，是否继续？`, '提示', { 
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
          this.$api.delete(`api/books/${row.id}`)
          .then(() => {
            this.$message.success('删除成功')
            this.loadBooks()
          })
          .catch(() => {
            this.$message.error('删除请求失败')
          })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    }
  },
  mounted() {
    this.loadBooks()
  }
}
</script>
