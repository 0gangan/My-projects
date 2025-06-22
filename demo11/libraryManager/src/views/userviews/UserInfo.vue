<template>
    <el-container style="width:1096px;">
      <el-header style="height:60px; border-bottom: 1px solid #eee;">  
        <div style="float:left;width:240px;height:50px;font-size: 24px;margin-top:5px">用户信息管理</div>
        <div style="float:right;width:80px;height:30px;text-align:bottom;margin-top:15px;margin-right:45px">
           <el-button
              size="mini" 
              @click="adduser()">
              <Plus style="width:1em;height:1em;margin-right:5px"></Plus>
              <span style="vertical-align:middle">增加用户</span></el-button>
        </div>
      </el-header>
      <el-main>       
      <el-table
        :data=tableData
        style="width: 100%"
        max-height="680" border> 
        <el-table-column
          prop="id"
          label="用户ID"
          min-width="8%"
          align="center">
        </el-table-column>       
        <el-table-column
          prop="username"
          label="用户名称"
          min-width="12%"
          align="center">
        </el-table-column>

        <el-table-column
          prop="usertype"
          label="用户类型"
          min-width="12%"
          align="center">                 
        </el-table-column>
        <el-table-column
          prop="phone"
          label="电话号码"
          min-width="18%"
          align="center">
        </el-table-column>         
         <el-table-column min-width="26%" align="center" label="操作">         
          <template #default="{ row }">              
            <el-button
              size="mini"
              type="primary" link
              @click="edituser(row)">
              <Edit style="width:1em;height:1em;margin-right:5px"></Edit>
              <span style="vertical-align:middle">修改用户信息</span>
              </el-button>              
            <el-button
              size="mini" link
              type="danger"
              @click="deleteuser(row)">
              <Delete style="width:1em;height:1em;margin-right:5px"></Delete>
              <span style="vertical-align:middle">删除用户信息</span>
              </el-button>
          </template>
        </el-table-column>
      </el-table>      
      </el-main>  
      <AddUser @onReload = "getalluser" ref="adduser"></AddUser> 
      <EditUser @onReload = "getalluser" ref="edituser"></EditUser>      
    </el-container>     
</template>
<script>
import AddUser from "./AddUser.vue"
import EditUser from "./EditUser.vue"
//import EditPassword from "./EditPassword.vue"
export default {
  name: 'UserInfo',
  components:{ AddUser,EditUser},
  data() {
    return { 
        tableData: [],   
        options:[{"id":1,"usertype":"admin"},
                 {"id":2,"usertype":"reader"}
        ],            
      }        
   },
   methods: {        
        getalluser(){            
            this.$api.post('user/getusers', null).then((res) => {                        
                if (res.code === 200) {
                    //alert(res.data[0].id)     
                    this.tableData = res.data
                } else {
                    alert('获取用户信息数据失败!')
                }
            })              
        },      
        
        adduser(){
            this.$refs.adduser.dialogFormVisible = true
            this.$refs.adduser.options = this.options
        },
        edituser(row){            
            this.$refs.edituser.dialogFormVisible = true
            this.$refs.edituser.form.id = row.id; 
            this.$refs.edituser.form.username =  row.username;
            this.$refs.edituser.form.phone =  row.phone
            this.$refs.edituser.form.usertype =  row.usertype   
            this.$refs.edituser.options = this.options         
        },
       
        deleteuser(row){
            this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$message({
                    type: 'success',
                    message: '确认删除!'
                 });
                let data = {
                    id: row.id,            
                 }        
                this.$api.post('user/deleteuser', data)
                .then(res => { 
                    if (res.code === 200) {
                        //alert(res.data)                   
                        this.getalluser()                 
                    }              
                })              
            }).catch(() => {
                this.$message({
                type: 'info',
                message: '已取消删除'
                })          
            }) 
        }         
    },
    mounted: function () {     
        this.getalluser()       
    } ,
}
</script>
<style scoped>
</style>