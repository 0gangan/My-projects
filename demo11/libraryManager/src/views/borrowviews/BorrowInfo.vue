<template>
  <el-dialog :title="form.id ? '编辑借阅信息' : '添加借阅信息'" v-model="dialogVisible" width="500px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="用户ID">
        <el-input v-model="form.userId" />
      </el-form-item>
      <el-form-item label="图书ID">
        <el-input v-model="form.bookId" />
      </el-form-item>
      <el-form-item label="借出时间" width="200">
        <el-date-picker
          v-model="form.borrowTime"
          type="datetime"
          placeholder="选择借出时间"
          value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="归还时间" width="200">
        <el-date-picker
          v-model="form.returnTime"
          type="datetime"
          placeholder="选择归还时间"
          value-format="YYYY-MM-DDTHH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="是否归还">
        <el-switch v-model="form.returned" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<script>
export default {
  name: 'BorrowInfo',
  data() {
    return {
      dialogVisible: false,
      form: {
        id: null,
        userId: sessionStorage.getItem('userid') || '',
        bookId: '',
        borrowTime: '',
        returnTime: '',
        returned: false
      }
    };
  },
  methods: {
    getEmptyForm() {
      return {
        id: null,
        userId: sessionStorage.getItem('userid') || '',
        bookId: '',
        borrowTime: '',
        returnTime: '',
        returned: false
      };
    },
    openDialog(row) {
      this.form = row ? { ...row } : this.getEmptyForm();
      this.dialogVisible = true;
    },
    save() {
      const method = this.form.id ? 'put' : 'post';
      const url = this.form.id ? `api/borrow/${this.form.id}` : 'api/borrow';

      this.$api[method](url, this.form)
        .then(() => {
          this.$message.success('保存成功');
          this.dialogVisible = false;
          this.$emit('refresh');
        })
        .catch(() => {
          this.$message.error('保存失败');
        });
    }
  }
};
</script>

