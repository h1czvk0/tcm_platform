<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索中药名称"
          style="width: 300px"
          @keyup.enter.native="handleSearch">
        </el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">添加中药</el-button>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="medicineId" label="ID" width="80"></el-table-column>
        <el-table-column prop="medicineName" label="中药名称" width="120"></el-table-column>
        <el-table-column prop="nature" label="性质" width="80"></el-table-column>
        <el-table-column prop="flavor" label="味道" width="100"></el-table-column>
        <el-table-column prop="functions" label="功效" show-overflow-tooltip></el-table-column>
        <el-table-column prop="viewsCount" label="浏览" width="80"></el-table-column>
        <el-table-column prop="collectionCount" label="收藏" width="80"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
    
    <!-- 添加/编辑对话框 -->
    <el-dialog 
      :title="dialogTitle" 
      :visible.sync="dialogVisible"
      width="600px">
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="中药名称" prop="medicineName">
          <el-input v-model="form.medicineName"></el-input>
        </el-form-item>
        <el-form-item label="别名" prop="medicineAlias">
          <el-input v-model="form.medicineAlias"></el-input>
        </el-form-item>
        <el-form-item label="性质" prop="nature">
          <el-select v-model="form.nature">
            <el-option label="温" value="温"></el-option>
            <el-option label="热" value="热"></el-option>
            <el-option label="凉" value="凉"></el-option>
            <el-option label="寒" value="寒"></el-option>
            <el-option label="平" value="平"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="味道" prop="flavor">
          <el-input v-model="form.flavor"></el-input>
        </el-form-item>
        <el-form-item label="归经" prop="meridians">
          <el-input v-model="form.meridians"></el-input>
        </el-form-item>
        <el-form-item label="功效" prop="functions">
          <el-input type="textarea" v-model="form.functions" rows="3"></el-input>
        </el-form-item>
        <el-form-item label="用法用量" prop="usage">
          <el-input type="textarea" v-model="form.usage" rows="2"></el-input>
        </el-form-item>
        <el-form-item label="禁忌" prop="contraindications">
          <el-input type="textarea" v-model="form.contraindications" rows="2"></el-input>
        </el-form-item>
        <el-form-item label="图片URL" prop="imageUrl">
          <el-input v-model="form.imageUrl"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getMedicinePage, addMedicine, updateMedicine, deleteMedicine } from '@/api/medicine'

export default {
  name: 'Medicine',
  data() {
    return {
      searchKeyword: '',
      tableData: [],
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {},
      formRules: {
        medicineName: [{ required: true, message: '请输入中药名称', trigger: 'blur' }],
        nature: [{ required: true, message: '请选择性质', trigger: 'change' }]
      },
      submitLoading: false
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getMedicinePage({
          current: this.pagination.current,
          size: this.pagination.size,
          keyword: this.searchKeyword,
          nature: this.selectedNature
        })
      if (res.code === 200) {
        this.tableData = res.data.records || []
        this.pagination.total = res.data.total || 0
      } else {
        this.$message.error(res.message || '加载数据失败')
      }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('系统异常，请联系管理员')
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
    
    handleAdd() {
      this.dialogTitle = '添加中药'
      this.form = {}
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.dialogTitle = '编辑中药'
      this.form = { ...row }
      this.dialogVisible = true
    },
    
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        this.submitLoading = true
        try {
          if (this.form.medicineId) {
            await updateMedicine(this.form)
            this.$message.success('更新成功')
          } else {
            await addMedicine(this.form)
            this.$message.success('添加成功')
          }
          this.dialogVisible = false
          this.loadData()
        } catch (error) {
          console.error(error)
        } finally {
          this.submitLoading = false
        }
      })
    },
    
    handleDelete(row) {
      this.$confirm('确认删除该中药?', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await deleteMedicine(row.medicineId)
          this.$message.success('删除成功')
          this.loadData()
        } catch (error) {
          console.error(error)
        }
      })
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
