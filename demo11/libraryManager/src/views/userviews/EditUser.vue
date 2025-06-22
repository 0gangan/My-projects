<template>
    <div>   
    <el-dialog
        title="修改用户信息"
        v-model="dialogFormVisible"
        :before-close="clear"
        width=30%
        draggable>  
        <el-form :model="form" :rules="rules" style="text-align:left" ref="form" >
            <el-form-item label="用户Id " :label-width="formLabelWidth" prop="id" >         
                <el-input name="id" type="text" v-model="form.id" readonly disabled></el-input>
            </el-form-item> 
            <el-form-item label="用户名称" :label-width="formLabelWidth" prop="username">
                <el-input type="text" name="username" v-model="form.username"  ></el-input>
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
    name: 'EditUser',
    components:{},
    data () {
      return {
        dialogFormVisible: false,        
        form: {
           id:'',                             
           username:'',          
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
           id:'',           
           username:'',          
           phone:'', 
           usertype:'',       
        }
        this.options = []
        this.selvalue = ''
      },
      changeusertype(){
        this.form.usertype = this.selvalue
      },

      onSubmit () {        
        let data = {
                id: this.form.id,
                usertype:this.form.usertype,                         
                username:this.form.username,
                phone:this.form.phone, 
            }  
            this.$api.post('user/updateuser', data)
                .then((res) => {                        
                    if (res.code === 200) {
                        this.$emit("onReload")
                        this.dialogFormVisible = false
                    } else {
                        this.$message({
                            type: 'info',
                            message: '修改用户信息失败'
                        });   
                    }        
                }) 
        },     
      
  },    
}
</script>

