<template>
    <div class="box">
        <div class="left">        
            <div style="flex:1">
                <img src="@/assets/1.jpeg" alr="" style="width:100%;height:100vh;">
            </div>        
        </div>
        <div class="right">    
            <div class="logindiv" >           
                <el-card class="login-form-layout">                 
                    <h1 class="login-title">用户登录</h1>
                    <hr style="margin:5px 0px 10px ;"/>           
                    <el-form
                        autocomplete="on"
                        :model="loginForm"
                        ref="loginForm"
                        label-position="left"
                    >                
                    <el-form-item prop="username" >                    
                        <el-input 
                            prefix-icon="User"                                               
                            name="username"
                            type="text"
                            v-model="loginForm.username"
                            autocomplete="on"
                            placeholder="请输入用户名">                                              
                        </el-input>                
                    </el-form-item>
                    
                    <el-form-item prop="password">                                  
                    <el-input 
                        prefix-icon="Lock"  
                        type="password" 
                        name="password"                                     
                        v-model="loginForm.password"
                        autocomplete="on"
                        placeholder="请输入密码"
                        show-password>                   
                    </el-input>                
                    </el-form-item>              
                    <el-form-item >
                        <el-button 
                            style="width:100%;font-size:12px"                       
                            type="primary"
                            @click ="handleLogin">
                            登&nbsp;&nbsp;录
                        </el-button>
                    </el-form-item>                            
                </el-form>            
            </el-card>            
            </div>
        </div>
    </div>
    </template>
    <script>
    export default {
        name: 'Login',
        data() {    
            return {
                loginForm: {
                    username: window.localStorage.getItem('username'),  
                    password: window.localStorage.getItem(window.localStorage.getItem('username'))  //"456789"
                },
            };
        },
        methods:{
            handleLogin(){            
                if((this.loginForm.username.trim() !== '' 
                  && this.loginForm.username !== undefined)
                  &&(this.loginForm.password.trim() !== '' 
                  && this.loginForm.password !== undefined)){               
                    let data = {
                        username: this.loginForm.username,
                        password: this.loginForm.password
                    }                
                    this.$api.post('auth/login', data)
                    .then((res) => {                                         
                        if (res.code === 200) {
                            //alert(res.data.regionCode)  
                            window.localStorage.setItem('username',this.loginForm.username),  
                            window.localStorage.setItem(this.loginForm.username,this.loginForm.password)
                            sessionStorage.setItem('token',res.data.regionCode)
                            sessionStorage.setItem('usertype',res.data.usertype)  
                            sessionStorage.setItem('userid', res.data.id); 
                            this.$router.push({  
                                path: '/'                                                                 
                            });  
                        } else {
                            if(res.code === 500){
                               alert("用户不存在或密码错误!")
                            }
                        }        
                    })
                }
            }
        }
    }
    </script>
    <style scoped>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }
    .box {
        width: 100vw;/*1340px;*/
        height: 100vh;/*620px;*/
        display: flex;  
    }
    .left {
        width: 60%;/*820px;*/
        height: 100%;/*620px;*/
        background-color:lightpink;
    }
    .right {       
        width: 40%;/*520px;*/
        height: 100%;
        background-color: rgb(250, 250, 250);  
        display: flex;
        justify-content: center;
        align-items:center;      
    }
    .logindiv{
        width:50%;
        height:50%;    
    }
    .login-form-layout { 
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