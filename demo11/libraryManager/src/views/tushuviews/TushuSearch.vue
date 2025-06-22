<template>
  <div>
    <el-form :inline="true" label-width="60px" class="search-form" @submit.native.prevent="searchBooks">
      <el-form-item label="书名">
        <el-input v-model="filters.title" placeholder="输入书名"></el-input>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="filters.author" placeholder="输入作者"></el-input>
      </el-form-item>
      <el-form-item label="分类">
        <el-input v-model="filters.category" placeholder="输入分类"></el-input>
      </el-form-item>
      <el-form-item label="出版社">
        <el-input v-model="filters.publisher" placeholder="输入出版社"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchBooks">搜索</el-button>
        <el-button @click="resetFilters">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="bookList" style="width: 100%; margin-top: 20px">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="category" label="分类" />
      <el-table-column prop="publisher" label="出版社" />
    </el-table>

    <el-empty v-if="bookList.length === 0" description="无搜索结果"></el-empty>
  </div>
</template>

<script>
export default {
  name: 'SearchBook',
  data() {
    return {
      filters: {
        title: '',
        author: '',
        category: '',
        publisher: ''
      },
      bookList: []
    }
  },
  methods: {
    async searchBooks() {
      try {
        // 调用全局 $api，传递filters，过滤空字段
        const params = {}
        for (const key in this.filters) {
          if (this.filters[key].trim() !== '') {
            params[key] = this.filters[key].trim()
          }
        }
        const res = await this.$api.get('api/books/search', params)
        if (Array.isArray(res)) {
          this.bookList = res
        } else {
          this.$message.error('返回数据格式错误')
        }
      } catch (err) {
        this.$message.error('搜索请求失败')
      }
    },
    resetFilters() {
      this.filters = {
        title: '',
        author: '',
        category: '',
        publisher: ''
      }
      this.bookList = []
    }
  }
}
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}
</style>
