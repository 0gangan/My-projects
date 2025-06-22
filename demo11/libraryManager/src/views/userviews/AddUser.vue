<template>
    <div>   
    <el-dialog
        title="增加用户"
        v-model="dialogFormVisible"
        :before-close="clear"
        width=30%
        draggable> 
        <el-form :model="form" :rules="rules" style="text-align:left" ref="form" >
            <el-form-item label="用户名称" :label-width="formLabelWidth" prop="username">
                <el-input type="text" name="username" v-model="form.username" autocomplete="off" placeholder="请输入用户名称" ></el-input>
            </el-form-item>
  
            <el-form-item label="密码 " :label-width="formLabelWidth" prop="password" >         
                <el-input name="password" type="password" v-model="form.password" autocomplete="off" placeholder="请输入密码" ></el-input>
            </el-form-item>

            <el-form-item label="电话号码 " :label-width="formLabelWidth" prop="phone" >         
                <el-input name="phone" type="text" v-model="form.phone" autocomplete="off" placeholder="请输入电话号码" ></el-input>
            </el-form-item>

            <el-form-item label="用户类型" :label-width="formLabelWidth" prop="usertype">
                <el-input type="text" name="usertype" v-model="form.usertype" autocomplete="off" placeholder="请输入用户类型" ></el-input>
                <div class="flex flex-wrap gap-4 items-center">
                   <el-select
                    v-model="selvalue"
                    placeholder="Select"
                    size="large"
                    @change="changeusertype()"
                    style="width: 240px">
                        <el-option
                        v-for="item in options"
                        :key="item.id"
                        :label="item.usertype"
                        :value="item.usertype"
                   />
                  </el-select>
                  </div>
            </el-form-item> 
        </el-form>
        
        <div slot="footer" class="dialog-footer" align="right">
          <el-button @click="dialogFormVisible = false">取 消</el-button>       
          <el-button type="primary" @click="onSubmit()">确 定</el-button>
        </div>        
    </el-dialog>
    </div>
</template>
<script>
export default {
    name: 'AddUser',
    components:{},
    data () {
      return {
        dialogFormVisible: false,        
        form: {                             
           username:'',
           password:'', 
           phone:'', 
           usertype:'',            
        },
        selvalue:'',
        options:[],
        formLabelWidth: '120px',       
      }
    },
    methods: {
      clear () {       
        this.form = {            
           username:'',
           password:'', 
           phone:'', 
           usertype:'',                         
        }
      },
      changeusertype(){
        this.form.usertype = this.selvalue
      },

      onSubmit () {        
          if((this.form.username.trim() !== '' 
              && this.form.username !== undefined) 
              && (this.form.password !== '' 
              && this.form.password !== undefined)
              && (this.form.phone !== '' 
              && this.form.phone !== undefined)
              && (this.form.usertype !== '' 
              && this.form.usertype !== undefined))
          {   
            //alert("用户账号:"+ this.form.username ) 
            this.$confirm('此操作将增加用户: '+ this.form.username +' , 是否继续?', '提示', 
              {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              })
              .then(() => {
                  this.$message({
                      type: 'success',
                      message: '确认增加username用户!'
                  });
                  var _this = this                 
                  let data = {
                      username: this.form.username,
                      password: this.form.password,
                      usertype: this.form.usertype,
                      phone: this.form.phone
                  }                    
                  this.$api.post('auth/add/user', data)
                      .then((res) => {                        
                          if (res.code === 200) {
                              this.$emit("onReload")
                              this.dialogFormVisible = false
                          } else {
                              if(res.code ===0)
                                alert(res.msg)
                              this.$message({
                                  type: 'info',
                                  message: '增加用户失败'
                              });   
                          }        
                      })
              })                               
          }
      }
  },    
}
</script>

<style scoped>
  .el-icon-circle-plus-outline {
    margin: 50px 0 0 20px;
    font-size: 100px;
    float: left;
    cursor: pointer;
  } 
</style>