<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索药膳名称"
          style="width: 300px"
          @keyup.enter.native="handleSearch">
        </el-input>
        <el-select v-model="difficulty" placeholder="难度筛选" clearable style="width: 150px">
          <el-option label="简单" value="EASY"></el-option>
          <el-option label="中等" value="MEDIUM"></el-option>
          <el-option label="困难" value="HARD"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">添加药膳</el-button>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="dietId" label="ID" width="80"></el-table-column>
        <el-table-column prop="dietName" label="药膳名称" width="150"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="efficacy" label="功效" show-overflow-tooltip></el-table-column>
        <el-table-column prop="cookingTime" label="烹饪时间(分)" width="110"></el-table-column>
        <el-table-column prop="difficultyLevel" label="难度" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.difficultyLevel === 'EASY'" type="success">简单</el-tag>
            <el-tag v-else-if="scope.row.difficultyLevel === 'MEDIUM'" type="warning">中等</el-tag>
            <el-tag v-else-if="scope.row.difficultyLevel === 'HARD'" type="danger">困难</el-tag>
          </template>
        </el-table-column>
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
      width="700px">
      <el-form :model="form" :rules="formRules" ref="form" label-width="100px">
        <el-form-item label="药膳名称" prop="dietName">
          <el-input v-model="form.dietName"></el-input>
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="form.description" rows="3"></el-input>
        </el-form-item>
        
        <el-form-item label="功效" prop="efficacy">
          <el-input type="textarea" v-model="form.efficacy" rows="2"></el-input>
        </el-form-item>
        
        <el-form-item label="食材列表" prop="ingredients">
          <el-input 
            type="textarea" 
            v-model="form.ingredientsText" 
            rows="3"
            placeholder='每行一个食材，例如：羊肉500g'>
          </el-input>
          <span style="font-size: 12px; color: #999">提示：每行输入一个食材</span>
        </el-form-item>
        
        <el-form-item label="制作步骤" prop="preparationSteps">
          <el-input 
            type="textarea" 
            v-model="form.stepsText" 
            rows="4"
            placeholder='每行一个步骤，例如：羊肉洗净切块，焯水去血沫'>
          </el-input>
          <span style="font-size: 12px; color: #999">提示：每行输入一个步骤</span>
        </el-form-item>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="烹饪时间" prop="cookingTime">
              <el-input-number v-model="form.cookingTime" :min="1" :max="500"></el-input-number>
              <span style="margin-left: 10px">分钟</span>
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="难度等级" prop="difficultyLevel">
              <el-select v-model="form.difficultyLevel">
                <el-option label="简单" value="EASY"></el-option>
                <el-option label="中等" value="MEDIUM"></el-option>
                <el-option label="困难" value="HARD"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="推荐季节" prop="season">
          <el-input v-model="form.season" placeholder="例如：四季、春季、冬季"></el-input>
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
import { getDietPage, addDiet, updateDiet, deleteDiet } from '@/api/diet'

export default {
  name: 'Diet',
  data() {
    return {
      searchKeyword: '',
      difficulty: '',
      tableData: [],
      loading: false,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        ingredientsText: '',
        stepsText: ''
      },
      formRules: {
        dietName: [{ required: true, message: '请输入药膳名称', trigger: 'blur' }],
        difficultyLevel: [{ required: true, message: '请选择难度等级', trigger: 'change' }]
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
        const res = await getDietPage({
          current: this.pagination.current,
          size: this.pagination.size,
          keyword: this.searchKeyword,
          difficulty: this.difficulty
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
    
    handleAdd() {
      this.dialogTitle = '添加药膳'
      this.form = {
        ingredientsText: '',
        stepsText: ''
      }
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.dialogTitle = '编辑药膳'
      this.form = { ...row }
      
      // 将JSON格式转为文本格式
      if (row.ingredients) {
        try {
          const ingredientsArray = JSON.parse(row.ingredients)
          this.form.ingredientsText = ingredientsArray.join('\n')
        } catch (e) {
          this.form.ingredientsText = row.ingredients
        }
      }
      
      if (row.preparationSteps) {
        try {
          const stepsArray = JSON.parse(row.preparationSteps)
          this.form.stepsText = stepsArray.join('\n')
        } catch (e) {
          this.form.stepsText = row.preparationSteps
        }
      }
      
      this.dialogVisible = true
    },
    
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        // 将文本格式转为JSON格式
        const submitData = { ...this.form }
        
        if (this.form.ingredientsText) {
          const ingredientsArray = this.form.ingredientsText.split('\n').filter(item => item.trim())
          submitData.ingredients = JSON.stringify(ingredientsArray)
        }
        
        if (this.form.stepsText) {
          const stepsArray = this.form.stepsText.split('\n').filter(item => item.trim())
          submitData.preparationSteps = JSON.stringify(stepsArray)
        }
        
        delete submitData.ingredientsText
        delete submitData.stepsText
        
        this.submitLoading = true
        try {
          if (submitData.dietId) {
            await updateDiet(submitData)
            this.$message.success('更新成功')
          } else {
            await addDiet(submitData)
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
      this.$confirm('确认删除该药膳?', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await deleteDiet(row.dietId)
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
