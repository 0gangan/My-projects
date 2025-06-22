<template>
     <el-card class="login-form-layout">
         <el-form
             autocomplete="on"
             :model="loginForm"
             :rules="loginFormRules"
             ref="loginForm"
             label-position="left"
             >  
            <h2 class="login-title color-main">用户注册</h2>
             <el-form-item prop="username"> 
                <el-input
                     prefix-icon="UserFilled"
                     name="username"
                     type="text"
                     v-model.trim="loginForm.username"
                     autocomplete="on"
                     placeholder="请输入用户名"
                 >  
                </el-input>     
             </el-form-item>
             <el-form-item prop="password">
                 <el-input
                     prefix-icon="Lock"
                     name="password"
                     type="password"    
                     v-model.trim="loginForm.password"
                     autocomplete="on"
                     placeholder="请输入密码"
                     >  
                 </el-input>
             </el-form-item>
             <el-form-item prop="usertype">
                 <el-radio-group v-model="loginForm.usertype">
                     <el-radio-button value="reader">读者</el-radio-button>
                     <el-radio-button value="admin">图书管理员</el-radio-button>
                 </el-radio-group>
             </el-form-item>
             <el-form-item style="margin-bottom: 60px">
                 <el-button
                    style="width: 45%"     
                     type="primary"
                     @click="register('loginForm')">
                     注册
                 </el-button>
                 <el-button
                     style="width: 45%"     
                     type="primary"
                     @click="handleLogin()">
                     登录
                 </el-button>
             </el-form-item>
         </el-form>
     </el-card> 
</template>
<script>
export default {
     name: "login",
     data() {   
         return {
             loginForm: {
                 username: "",   
                 password: "",
                 usertype: "reader"
             },
             loginFormRules:{
                 username:[{required:true,message:'请填写用户名',trigger:'change'}],
                password:[{required:true,message:'请填写密码',trigger:'change'}]
                },  
             }
         },
    methods:{
         handleLogin(){
             alert("转入登录页面")
             this.$router.push({path: '/login'}) 
             },
         register(formName){    
            this.$refs[formName].validate((valid)=>{
                 if(valid){
                     alert("开始注册")
                     let data = {
                         password: this.loginForm.password,
                         username: this.loginForm.username,
                         usertype: this.loginForm.usertype
                    }   
                     this.$api.post('auth/add/user', data).then((res) => {  
                     if (res.code === 200) {
                            alert("转入登录页面")
                             this.$router.push({path: '/login'}) 
                     } else {

                     }
                 })
                 }
                 else{
                     return false
                 }
             })
        }
     } 
}
</script>
<style>
.login-form-layout {  
  left: 0;
  right: 0;
  width: 360px;
  height: 300px;
  margin: 40px auto;
  border-top: 10px solid #409eff;
}
.login-title {
  text-align: center;
}
.login-center-layout {
  background: #409eff;
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 100%;
  margin-top: 200px;
}
</style>