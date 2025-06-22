
<template>
  <div>
    <el-button type="primary" @click="loadBorrows" style="margin-bottom: 15px">刷新</el-button>
    <el-button type="success" @click="filterVisible = true" style="margin-left: 10px">筛选</el-button>

    <el-table :data="borrowList" style="width: 100%; margin-top: 15px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="bookTitle" label="图书名" />
      <el-table-column prop="borrowTime" label="借出时间" width="180" />
      <el-table-column prop="returnTime" label="归还时间" width="180" />
      <el-table-column label="是否归还">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.returned">是</el-tag>
          <el-tag type="warning" v-else>否</el-tag>
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

    <el-dialog title="筛选借阅记录" v-model="filterVisible" width="35%">
      <el-form label-width="80px">
        <el-form-item label="图书名">
          <el-input v-model="filterBookTitle" placeholder="请输入图书名称" clearable />
        </el-form-item>
        <el-form-item label="是否归还">
          <el-select v-model="filterReturned" placeholder="请选择" clearable>
            <el-option label="已归还" :value="true" />
            <el-option label="未归还" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item label="借阅时间">
          <el-date-picker
            v-model="filterBorrowTimeRange"
            type="daterange"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            unlink-panels
            clearable
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="resetFilter">重置</el-button>
        <el-button @click="filterVisible = false">取消</el-button>
        <el-button type="primary" @click="applyFilter">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ReaderBorrow',
  data() {
    return {
      borrowList: [],
      page: 1,
      size: 10,
      total: 0,
      filterVisible: false,
      filterBookTitle: '',
      filterReturned: null, // true, false 或 null（不筛选）
      filterBorrowTimeRange: [] // [startDate, endDate]
    };
  },
  methods: {
loadBorrows() {
  const userId = sessionStorage.getItem("userid"); // 从 sessionStorage 获取当前用户ID
  if (!userId) {
    this.$message.error('未获取到用户ID');
    return;
  }

  this.$api.get(`api/borrow/user/${userId}`, {
    page: this.page - 1,
    size: this.size,
    bookTitle: this.filterBookTitle || undefined,
    returned: this.filterReturned !== '' ? this.filterReturned : undefined
  }).then(res => {
    this.borrowList = res.content;
    this.total = res.totalElements;
  }).catch(() => {
    this.$message.error('加载借阅信息失败');
  });
}
,
    handlePageChange(newPage) {
      this.page = newPage;
      this.loadBorrows();
    },
    applyFilter() {
      this.page = 1; // 筛选时重置页码
      this.filterVisible = false;
      this.loadBorrows();
    },
    resetFilter() {
      this.filterBookTitle = '';
      this.filterReturned = null;
      this.filterBorrowTimeRange = [];
    }
  },
  mounted() {
    this.loadBorrows();
  }
}; 
</script>
