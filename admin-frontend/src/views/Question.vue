<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索题目内容"
          style="width: 300px"
          @keyup.enter.native="handleSearch">
        </el-input>
        <el-select v-model="category" placeholder="分类筛选" clearable style="width: 150px">
          <el-option label="中药功效" value="中药功效"></el-option>
          <el-option label="中药配伍" value="中药配伍"></el-option>
          <el-option label="中药归经" value="中药归经"></el-option>
          <el-option label="药膳知识" value="药膳知识"></el-option>
        </el-select>
        <el-select v-model="difficulty" placeholder="难度筛选" clearable style="width: 150px">
          <el-option label="简单" value="EASY"></el-option>
          <el-option label="中等" value="MEDIUM"></el-option>
          <el-option label="困难" value="HARD"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">添加题目</el-button>
      </div>
      
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="questionId" label="ID" width="80"></el-table-column>
        <el-table-column prop="questionContent" label="题目内容" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="questionType" label="题型" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.questionType === 'SINGLE'" type="primary">单选</el-tag>
            <el-tag v-else-if="scope.row.questionType === 'MULTIPLE'" type="success">多选</el-tag>
            <el-tag v-else-if="scope.row.questionType === 'JUDGE'" type="warning">判断</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="100"></el-table-column>
        <el-table-column prop="difficulty" label="难度" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.difficulty === 'EASY'" type="success">简单</el-tag>
            <el-tag v-else-if="scope.row.difficulty === 'MEDIUM'" type="warning">中等</el-tag>
            <el-tag v-else-if="scope.row.difficulty === 'HARD'" type="danger">困难</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="correctAnswer" label="正确答案" width="100"></el-table-column>
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
        <el-form-item label="题目内容" prop="questionContent">
          <el-input type="textarea" v-model="form.questionContent" rows="3"></el-input>
        </el-form-item>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="题型" prop="questionType">
              <el-select v-model="form.questionType" @change="handleTypeChange">
                <el-option label="单选" value="SINGLE"></el-option>
                <el-option label="多选" value="MULTIPLE"></el-option>
                <el-option label="判断" value="JUDGE"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-input v-model="form.category"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="难度等级" prop="difficulty">
          <el-radio-group v-model="form.difficulty">
            <el-radio label="EASY">简单</el-radio>
            <el-radio label="MEDIUM">中等</el-radio>
            <el-radio label="HARD">困难</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <div v-if="form.questionType !== 'JUDGE'">
          <el-form-item label="选项A" prop="optionA">
            <el-input v-model="form.optionA"></el-input>
          </el-form-item>
          
          <el-form-item label="选项B" prop="optionB">
            <el-input v-model="form.optionB"></el-input>
          </el-form-item>
          
          <el-form-item label="选项C" prop="optionC">
            <el-input v-model="form.optionC"></el-input>
          </el-form-item>
          
          <el-form-item label="选项D" prop="optionD">
            <el-input v-model="form.optionD"></el-input>
          </el-form-item>
        </div>
        
        <el-form-item label="正确答案" prop="correctAnswer">
          <el-input 
            v-model="form.correctAnswer" 
            :placeholder="form.questionType === 'JUDGE' ? '输入A或B' : form.questionType === 'MULTIPLE' ? '输入多个选项，如ABD' : '输入单个选项，如A'">
          </el-input>
        </el-form-item>
        
        <el-form-item label="答案解析" prop="explanation">
          <el-input type="textarea" v-model="form.explanation" rows="3"></el-input>
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
import { getQuestionPage, addQuestion, updateQuestion, deleteQuestion } from '@/api/question'

export default {
  name: 'Question',
  data() {
    return {
      searchKeyword: '',
      category: '',
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
      form: {},
      formRules: {
        questionContent: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
        questionType: [{ required: true, message: '请选择题型', trigger: 'change' }],
        correctAnswer: [{ required: true, message: '请输入正确答案', trigger: 'blur' }]
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
        const res = await getQuestionPage({
          current: this.pagination.current,
          size: this.pagination.size,
          keyword: this.searchKeyword,
          category: this.category,
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
      this.dialogTitle = '添加题目'
      this.form = {
        questionType: 'SINGLE',
        difficulty: 'MEDIUM'
      }
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.dialogTitle = '编辑题目'
      this.form = { ...row }
      this.dialogVisible = true
    },
    
    handleTypeChange(type) {
      if (type === 'JUDGE') {
        this.form.optionA = '正确'
        this.form.optionB = '错误'
        this.form.optionC = null
        this.form.optionD = null
      }
    },
    
    handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        
        this.submitLoading = true
        try {
          if (this.form.questionId) {
            await updateQuestion(this.form)
            this.$message.success('更新成功')
          } else {
            await addQuestion(this.form)
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
      this.$confirm('确认删除该题目?', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await deleteQuestion(row.questionId)
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
