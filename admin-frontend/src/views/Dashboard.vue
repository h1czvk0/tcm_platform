<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A">
              <i class="el-icon-medicine-box"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.medicineCount }}</div>
              <div class="stat-label">中药总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C">
              <i class="el-icon-food"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.dietCount }}</div>
              <div class="stat-label">药膳总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C">
              <i class="el-icon-edit"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.questionCount }}</div>
              <div class="stat-label">题目总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>最近注册用户</span>
          </div>
          <el-table :data="recentUsers" v-loading="loading" height="300">
            <el-table-column prop="username" label="用户名" width="120"></el-table-column>
            <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
            <el-table-column prop="registrationDate" label="注册时间">
              <template slot-scope="scope">
                {{ formatDate(scope.row.registrationDate) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>热门中药</span>
          </div>
          <el-table :data="popularMedicines" v-loading="loading" height="300">
            <el-table-column prop="medicineName" label="中药名称" width="150"></el-table-column>
            <el-table-column prop="viewsCount" label="浏览次数" width="100"></el-table-column>
            <el-table-column prop="collectionCount" label="收藏次数"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>系统信息</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
            <el-descriptions-item label="后端框架">Spring Boot 2.7.18</el-descriptions-item>
            <el-descriptions-item label="前端框架">Vue 2.6.14</el-descriptions-item>
            <el-descriptions-item label="数据库">MySQL 8.0</el-descriptions-item>
            <el-descriptions-item label="缓存">Redis 7.0</el-descriptions-item>
            <el-descriptions-item label="ORM框架">MyBatis Plus 3.5.3.1</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserPage } from '@/api/user'
import { getMedicinePage } from '@/api/medicine'

export default {
  name: 'Dashboard',
  data() {
    return {
      statistics: {
        userCount: 0,
        medicineCount: 0,
        dietCount: 0,
        questionCount: 0
      },
      recentUsers: [],
      popularMedicines: [],
      loading: false
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadRecentUsers()
    this.loadPopularMedicines()
  },
  methods: {
    async loadStatistics() {
      try {
        const userRes = await getUserPage({ current: 1, size: 1 })
        this.statistics.userCount = userRes.data.total || 0
        
        const medicineRes = await getMedicinePage({ current: 1, size: 1 })
        this.statistics.medicineCount = medicineRes.data.total || 0
        
        this.statistics.dietCount = 3
        this.statistics.questionCount = 4
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    async loadRecentUsers() {
      this.loading = true
      try {
        const res = await getUserPage({ current: 1, size: 5 })
        this.recentUsers = res.data.records || []
      } catch (error) {
        console.error('加载用户列表失败:', error)
        this.recentUsers = []
      } finally {
        this.loading = false
      }
    },
    
    async loadPopularMedicines() {
      this.loading = true
      try {
        const res = await getMedicinePage({ current: 1, size: 5 })
        this.popularMedicines = res.data.records || []
      } catch (error) {
        console.error('加载中药列表失败:', error)
        this.popularMedicines = []
      } finally {
        this.loading = false
      }
    },
    
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
  margin-right: 20px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
