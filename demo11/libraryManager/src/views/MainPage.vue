<template>
    <div class="box"> 
      <div :style="leftstyle">    
            <el-checkbox-button v-model="isCollapse" > 
              <Location style="width:1em;height:1em;"/>      
            </el-checkbox-button>    
        <el-menu default-active="5" class="el-menu-vertical-demo"  @select="handleSelect" @open="handleOpen" @close="handleClose" :collapse="isCollapse">
          <el-sub-menu index="1" v-if="usertype=='admin'">
            <template #title>
              <User style="width:1em;height:1em;margin-right:5px"/>
              <span slot="title"><span>用户管理</span></span>
            </template>
              <el-menu-item index="1-1"><span>用户信息管理</span></el-menu-item>
            </el-sub-menu> 
          <el-sub-menu index="2" v-if="usertype=='admin'">
            <template #title>
              <Setting style="width:1em;height:1em;margin-right:5px"/>
              <span slot="title"><span>图书分类</span></span>
            </template>
              <el-menu-item index="2-1"><span>图书分类管理</span></el-menu-item>               
          </el-sub-menu>  
    
          <el-sub-menu index="3" v-if="usertype=='admin'">
            <template #title>
              <Document style="width:1em;height:1em;margin-right:5px"/>
              <span slot="title"><span>图书管理</span></span>
            </template>
              <el-menu-item index="3-1"><span>图书管理</span></el-menu-item> 
              <el-menu-item index="3-2"><span>图书检索</span></el-menu-item>                       
          </el-sub-menu> 
    
          <el-sub-menu index="4" v-if="usertype=='admin'">
            <template #title>
              <Document style="width:1em;height:1em;margin-right:5px"/>
              <span slot="title"><span>图书借阅</span></span>
            </template>
            <el-menu-item index="4-1"><span>图书借阅管理</span></el-menu-item>
         </el-sub-menu>
         <el-menu-item index="5" v-if="usertype=='reader'">
            <Setting style="width:1em;height:1em;margin-right:5px"/>    
            <span slot="title">借阅信息</span>
          </el-menu-item>
          <el-menu-item index="6" v-if="usertype=='reader'">
            <Setting style="width:1em;height:1em;margin-right:5px"/>    
            <span slot="title">图书检索</span>
          </el-menu-item>
        </el-menu>
        </div>
        <div class="right">      
          <el-tabs v-model="editableTabsValue" type="card"  closable @tab-remove="removeTab">
            <el-tab-pane
              v-for="(item, index) in editableTabs"
                :key="item.name"
                :label="item.title"
                :name="item.name"
              > 
              <UserInfo v-if="item.content == '1-1'"></UserInfo>              
              <TushuType v-if="item.content == '2-1'"></TushuType> 
              <Tushu v-if="item.content == '3-1'"></Tushu> 
              <TushuSearch v-if="item.content == '3-2'"></TushuSearch>
              <Borrow v-if="item.content == '4-1'"></Borrow> 
              <ReaderBorrow v-if="item.content == '5'"></ReaderBorrow>
              <TushuSearch v-if="item.content == '6'"></TushuSearch>             
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </template>
     
    <script >
    import UserInfo from "./userviews/UserInfo.vue"
    import TushuType from "./tushutypeviews/TushuType.vue"
    import Tushu from "./tushuviews/Tushu.vue"
    import TushuSearch from "./tushuviews/TushuSearch.vue"
    import Borrow from "./borrowviews/Borrow.vue"
    import BorrowInfo from "./borrowviews/BorrowInfo.vue"
    import ReaderBorrow from "./borrowviews/ReaderBorrow.vue"

    // 这里可以添加脚本逻辑，但对于一个简单的菜单，不需要额外的代码
    export default {
        name:"MainPage",
        components:{UserInfo,TushuType,Tushu,TushuSearch,Borrow,BorrowInfo,ReaderBorrow,},
        data() {
          return {
            isCollapse: false,
            leftstyle: "left",        
            editableTabsValue: '0',
            editableTabs: [],
            tabIndex: 0,
            usertypeinfo:'',
            usertype:'',
          };
        },
        methods: {
          handleOpen(key, keyPath) {
            console.log(key, keyPath);
          },
          handleClose(key, keyPath) {
            console.log(key, keyPath);
          },  
          handleSelect(key){         
             var tabname = ""
             var tabcontent = key
             switch(key){
                case "1-1":
                  tabname = "用户管理"                     
                  break;
                case "2-1":
                   tabname = "图书分类管理"
                break;
                case "3-1":
                   tabname = "图书管理"
                break;
                case "3-2":
                  tabname = "图书检索"
                  break;
                case "4-1":
                  tabname = "图书借阅管理"
                  break;               
                case "5":
                  tabname = "读者借阅信息"
                  break;
                case "6":
                  tabname = "图书检索"
                  break;
             }
            // let newTabName = ++this.tabIndex + '';
            let tabs = this.editableTabs;
            let isexist = false
            tabs.forEach((tab, index) => {
                if (tab.name === tabname) {
                  isexist = true
                  this.editableTabsValue = tabname;
                }
            });
            if (isexist === false){
              this.editableTabs.push({
                title: tabname,
                name: tabname,
                content: tabcontent
              });
              this.editableTabsValue = tabname;
            }
          },
          addTab(targetName) {
            let newTabName = ++this.tabIndex + '';
            this.editableTabs.push({
              title: 'New Tab',
              name: newTabName,
              content: 'New Tab content'
            });
            this.editableTabsValue = newTabName;
          },
          removeTab(targetName) {
            let tabs = this.editableTabs;
            let activeName = this.editableTabsValue;
            if (activeName === targetName) {
              tabs.forEach((tab, index) => {
                if (tab.name === targetName) {
                  let nextTab = tabs[index + 1] || tabs[index - 1];
                  if (nextTab) {
                    activeName = nextTab.name;
                  }
                }
              });
            }        
            this.editableTabsValue = activeName;
            this.editableTabs = tabs.filter(tab => tab.name !== targetName);
          }, 
        
        },
      
        mounted() {
          console.log('mounted triggered');
          this.usertype = sessionStorage.getItem('usertype');
          console.log('usertype:', this.usertype);
        }
      }
    
    </script>
     
    <style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }
    span {
      font-size:12px;
    }
    .box {
        width: 100vw;/*1340px;*/
        height:100vh;/* 620px;*/
        display: flex;
    }
    .left {    
        width: 200px; 
        height:100%;
        font-size:12px; 
        background-color:rgb(250, 250, 250); 
        border:1px solid rgb(245, 245, 245) ;
        border-radius: 10px;     
    }
    
    .right {
        flex: 1;    
        height: 100%;
        font-size:8px; 
        background-color: rgb(255, 255, 255);    
        display: flex;    
        border:1px solid rgb(245, 245, 245) ;
        border-radius: 10px; 
    }
    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 180px;
        min-height: 320px;
        max-height: 420px;
        margin-right:5px;
      }
    .tac {
      text-align: center;
    }
    </style> 
