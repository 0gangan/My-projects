<template>
  <div>
    <el-button type="primary" @click="showAddDialog">添加借阅信息</el-button>
    <el-button @click="loadBorrows" style="margin-left: 10px">刷新</el-button>

    <el-table :data="borrowList" style="width: 100%; margin-top: 0px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="bookId" label="图书ID" />
      <el-table-column prop="bookTitle" label="图书名" />
      <el-table-column prop="borrowTime" label="借出时间" width="200" />
      <el-table-column prop="returnTime" label="归还时间" width="200" />
      <el-table-column prop="returned" label="是否归还">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.returned">是</el-tag>
          <el-tag type="warning" v-else>否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteBorrow(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
    style="margin-top: 20px; text-align: right"
    background
    layout="prev, pager, next, jumper"
    :current-page="page"
    :page-size="size"
    :total="total"
    @current-change="handlePageChange"
    />

    <borrow-info ref="borrowRef" @refresh="loadBorrows" />
  </div>
</template>

 <script>
import BorrowInfo from './BorrowInfo.vue'

export default {
  name: 'Borrow',
  components: { BorrowInfo },
  data() {
    return {
      borrowList: [],
      page: 1,
      size: 10,
      total: 0
    }
  },
  methods: {
    loadBorrows() {
      this.$api.get('api/borrow', {
        page: this.page - 1,
        size: this.size
      }).then(res => {
        this.borrowList = res.content
        this.total = res.totalElements // 设置总条数用于分页组件
      }).catch(() => {
        this.$message.error('加载借阅信息失败')
      })
    },
    showAddDialog() {
      this.$refs.borrowRef.openDialog()
    },
    showEditDialog(row) {
      this.$refs.borrowRef.openDialog(row)
    },
    deleteBorrow(row) {
      this.$confirm(`确定要删除编号为 ${row.id} 的借阅记录？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$api.delete(`api/borrow/${row.id}`)
          .then(() => {
            this.$message.success('删除成功')
            this.loadBorrows()
          })
          .catch(() => {
            this.$message.error('删除失败')
          })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    handleSizeChange(newSize) {
      this.size = newSize
      this.loadBorrows()
    },
    handlePageChange(newPage) {
      this.page = newPage
      this.loadBorrows()
    }
  },
  mounted() {
    this.loadBorrows()
  }
}
</script> 



