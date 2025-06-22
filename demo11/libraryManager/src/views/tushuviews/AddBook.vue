<template>
  <el-dialog
    title="添加图书"
    v-model="dialogVisible"
    width="500px"
    :modal-append-to-body="true"
    :destroy-on-close="true"
    :style="{ zIndex: 9999 }"
    @close="resetForm"
  >
    <el-form :model="form" ref="addForm" label-width="80px">
      <el-form-item label="书名" :rules="[{ required: true, message: '请输入书名', trigger: 'blur' }]">
        <el-input v-model="form.title"></el-input>
      </el-form-item>

      <el-form-item label="作者" :rules="[{ required: true, message: '请输入作者', trigger: 'blur' }]">
        <el-input v-model="form.author"></el-input>
      </el-form-item>

      <el-form-item label="分类">
        <el-input v-model="form.category"></el-input>
      </el-form-item>

      <el-form-item label="出版社">
        <el-input v-model="form.publisher"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAdd">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'AddBook',
  data() {
    return {
      dialogVisible: false,
      form: {
        title: '',
        author: '',
        category: '',
        publisher: ''
      }
    }
  },
  methods: {
    openDialog() {
      this.dialogVisible = true
      console.log('调用了 openDialog 方法， dialogVisible 为：', this.dialogVisible);
    },
    resetForm() {
      this.form = {
        title: '',
        author: '',
        category: '',
        publisher: ''
      }
      this.$refs.addForm?.clearValidate()
    },
    submitAdd() {
      if (!this.form.title || !this.form.author) {
      this.$message.warning('书名和作者不能为空')
      return
    }

this.$api.post('api/books', this.form).then(res => {
  console.log('添加图书返回:', res);
  // 判断返回是否是对象且有id，表示添加成功
  if (res && res.id) {
    this.$message.success('添加成功');
    this.dialogVisible = false;
    this.$emit('refresh'); // 通知父组件刷新列表
    this.resetForm();
  } else {
    this.$message.error('添加失败，返回数据格式异常');
  }
}).catch(() => {
  this.$message.error('请求失败');
});


    }
  }
}
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
