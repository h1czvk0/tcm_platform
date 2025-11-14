<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索用户名/昵称/手机号"
          style="width: 300px"
          @keyup.enter.native="handleSearch">
        </el-input>
        <el-select v-model="status" placeholder="状态筛选" clearable style="width: 150px">
          <el-option label="正常" value="ACTIVE"></el-option>
          <el-option label="冻结" value="FROZEN"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="userId" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="120"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.gender === 'M'">男</span>
            <span v-else-if="scope.row.gender === 'F'">女</span>
            <span v-else>未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80"></el-table-column>
        <el-table-column prop="registrationDate" label="注册时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.registrationDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastLoginDate" label="最后登录" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.lastLoginDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="accountStatus" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.accountStatus === 'ACTIVE'" type="success">正常</el-tag>
            <el-tag v-else-if="scope.row.accountStatus === 'FROZEN'" type="danger">冻结</el-tag>
            <el-tag v-else type="info">已删除</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button 
              size="mini" 
              :type="scope.row.accountStatus === 'ACTIVE' ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)">
              {{ scope.row.accountStatus === 'ACTIVE' ? '冻结' : '解冻' }}
            </el-button>
            <el-button size="mini" type="primary" @click="handleView(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @current-change="handlePageChange"
        :current-page="pagination.current"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; text-align: right">
      </el-pagination>
    </el-card>
    
    <!-- 用户详情对话框 -->
    <el-dialog 
      title="用户详情" 
      :visible.sync="detailVisible"
      width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ currentUser.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentUser.username }}</el-descriptions-item>
        <el-descriptions-item label="昵称">{{ currentUser.nickname }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ currentUser.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <span v-if="currentUser.gender === 'M'">男</span>
          <span v-else-if="currentUser.gender === 'F'">女</span>
          <span v-else>未知</span>
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{ currentUser.age || '-' }}</el-descriptions-item>
        <el-descriptions-item label="注册时间" :span="2">
          {{ formatDate(currentUser.registrationDate) }}
        </el-descriptions-item>
        <el-descriptions-item label="最后登录" :span="2">
          {{ formatDate(currentUser.lastLoginDate) }}
        </el-descriptions-item>
        <el-descriptions-item label="账户状态" :span="2">
          <el-tag v-if="currentUser.accountStatus === 'ACTIVE'" type="success">正常</el-tag>
          <el-tag v-else-if="currentUser.accountStatus === 'FROZEN'" type="danger">冻结</el-tag>
          <el-tag v-else type="info">已删除</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="微信OpenID" :span="2">
          {{ currentUser.wechatOpenid || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getUserPage, updateUserStatus } from '@/api/user'

export default {
  name: 'User',
  data() {
    return {
      searchKeyword: '',
      status: '',
      tableData: [],
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      detailVisible: false,
      currentUser: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getUserPage({
          current: this.pagination.current,
          size: this.pagination.size,
          keyword: this.searchKeyword,
          status: this.status
        })
        this.tableData = res.data.records
        this.pagination.total = res.data.total
      } catch (error) {
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    handlePageChange(page) {
      this.pagination.current = page
      this.loadData()
    },
    
    handleView(row) {
      this.currentUser = row
      this.detailVisible = true
    },
    
    handleToggleStatus(row) {
      const newStatus = row.accountStatus === 'ACTIVE' ? 'FROZEN' : 'ACTIVE'
      const action = newStatus === 'FROZEN' ? '冻结' : '解冻'
      
      this.$confirm(`确认${action}该用户?`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await updateUserStatus(row.userId, newStatus)
          this.$message.success(`${action}成功`)
          this.loadData()
        } catch (error) {
          console.error(error)
        }
      })
    },
    
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}
</style>
