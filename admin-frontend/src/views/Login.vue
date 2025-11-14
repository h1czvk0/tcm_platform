<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="title">中医药推广平台管理后台</div>
      
      <el-form :model="loginForm" :rules="rules" ref="loginForm">
        <el-form-item prop="adminName">
          <el-input 
            v-model="loginForm.adminName" 
            placeholder="请输入管理员账号"
            prefix-icon="el-icon-user">
          </el-input>
        </el-form-item>
        
        <el-form-item prop="adminPassword">
          <el-input 
            v-model="loginForm.adminPassword" 
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            @keyup.enter.native="handleLogin">
          </el-input>
        </el-form-item>
        
        <el-button 
          type="primary" 
          style="width: 100%"
          :loading="loading"
          @click="handleLogin">
          登录
        </el-button>
      </el-form>
      
      <div class="tips">
        默认账号: admin / 密码: admin123
      </div>
    </el-card>
  </div>
</template>

<script>
import { adminLogin } from '@/api/auth'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        adminName: 'admin',
        adminPassword: 'admin123'
      },
      rules: {
        adminName: [
          { required: true, message: '请输入管理员账号', trigger: 'blur' }
        ],
        adminPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (!valid) return
        
        this.loading = true
        try {
          const res = await adminLogin(this.loginForm)
          const { token, userId, username, userType } = res.data
          
          this.$store.dispatch('login', {
            token,
            userInfo: { userId, username, userType }
          })
          
          this.$message.success('登录成功')
          this.$router.push('/')
        } catch (error) {
          console.error(error)
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 20px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.tips {
  margin-top: 20px;
  text-align: center;
  color: #999;
  font-size: 12px;
}
</style>
