<template>
  <el-container class="main-layout">
    <el-aside width="200px">
      <div class="logo">TCM管理后台</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#fff"
        active-text-color="#409EFF">
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-home"></i>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/medicine">
          <i class="el-icon-medicine-box"></i>
          <span>中药管理</span>
        </el-menu-item>
        <el-menu-item index="/diet">
          <i class="el-icon-food"></i>
          <span>药膳管理</span>
        </el-menu-item>
        <el-menu-item index="/question">
          <i class="el-icon-edit"></i>
          <span>题库管理</span>
        </el-menu-item>
        <el-menu-item index="/user">
          <i class="el-icon-user"></i>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-right">
          <span>{{ userInfo.username }}</span>
          <el-button type="text" @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'MainLayout',
  computed: {
    userInfo() {
      return this.$store.state.userInfo
    }
  },
  methods: {
    handleLogout() {
      this.$confirm('确认退出登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('logout')
        this.$router.push('/login')
      })
    }
  }
}
</script>

<style scoped>
.main-layout {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #2b3a4a;
}

.el-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
