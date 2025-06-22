<template>
  <el-dialog
    title="编辑图书"
    v-model="dialogVisible"
    width="500px"
    :modal-append-to-body="true"
    :destroy-on-close="true"
    :style="{ zIndex: 9999 }"
    @close="resetForm"
  >
    <el-form :model="form" ref="editForm" label-width="80px">
      <el-form-item label="书名" :rules="[{ required: true, message: '请输入书名', trigger: 'blur' }]">
        <el-input v-model="form.title"></el-input>
      </el-form-item>

      <el-form-item label="作者" :rules="[{ required: true, message: '请输入作者', trigger: 'blur' }]">
        <el-input v-model="form.author"></el-input>
      </el-form-item>

<el-form-item label="分类">
  <el-cascader
    v-model="form.categoryPath"
    :options="categoryTree"
    :props="{
      label: 'name',
      value: 'name',
      children: 'children',
      emitPath: false  // 只使用最后一个值（名字）
    }"
    clearable
    placeholder="请选择分类"
  />
</el-form-item>



      <el-form-item label="出版社">
        <el-input v-model="form.publisher"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEdit">保存</el-button>
    </span>
  </el-dialog>
</template>


<!-- <script>
export default {
  name: 'EditBook',
  data() {
    return {
      dialogVisible: false,
      form: {
        id: null,
        title: '',
        author: '',
        category: '',
        publisher: ''
      }
    }
  },
  methods: {
    openDialog(book) {
      // 传入要编辑的图书对象，赋值给form
      this.form = { ...book }
      this.dialogVisible = true
    },
    resetForm() {
      this.form = {
        id: null,
        title: '',
        author: '',
        category: '',
        publisher: ''
      }
      this.$refs.editForm?.clearValidate()
    },
    submitEdit() {
      if (!this.form.title || !this.form.author) {
        this.$message.warning('书名和作者不能为空')
        return
      }

      // PUT 请求，URL 里带 id，body 是 form 的其他字段
      const { id, title, author, category, publisher } = this.form
      this.$api.put(`api/books/${id}`, { title, author, category, publisher })
      .then(res => {
        console.log('编辑图书返回:', res)
        if (res && res.id) {
          this.$message.success('编辑成功')
          this.dialogVisible = false
          this.$emit('refresh')
          this.resetForm()
        } else {
          this.$message.error('编辑失败，返回数据格式异常')
        }
      })
      .catch(() => {
        this.$message.error('请求失败')
      })
    },

  }
}
</script> -->
<script>
  export default {
  name: 'EditBook',
  data() {
    return {
      dialogVisible: false,
      form: {
        id: null,
        title: '',
        author: '',
        categoryPath: '', // 存分类名（字符串）
        publisher: ''
      },
      categoryTree: [],
    }
  },
  methods: {
    openDialog(book) {
      this.form = {
        id: book.id,
        title: book.title,
        author: book.author,
        categoryPath: book.category || '', // 直接是分类名字符串
        publisher: book.publisher
      }
      this.dialogVisible = true
    },
    fetchCategories() {
      this.$api.get('api/bookCategory/tree').then(res => {
        this.categoryTree = res || []
      })
    },
    submitEdit() {
      if (!this.form.title || !this.form.author) {
        this.$message.warning('书名和作者不能为空')
        return
      }

      const payload = {
        title: this.form.title,
        author: this.form.author,
        publisher: this.form.publisher,
        category: this.form.categoryPath || ''
      }

      this.$api.put(`api/books/${this.form.id}`, payload)
        .then(res => {
          if (res && res.id) {
            this.$message.success('编辑成功')
            this.dialogVisible = false
            this.$emit('refresh')
            this.resetForm()
          } else {
            this.$message.error('编辑失败，返回数据格式异常')
          }
        })
        .catch(() => {
          this.$message.error('请求失败')
        })
    },
    resetForm() {
      this.form = {
        id: null,
        title: '',
        author: '',
        categoryPath: '',
        publisher: ''
      }
      this.$refs.editForm?.clearValidate()
    }
  },
  mounted() {
    this.fetchCategories()
  }
}

</script>
<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
