import axios from 'axios'
import store from '@/store'
import { Message } from 'element-ui'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 15000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从Vuex或localStorage获取token
    const token = store.state.token || localStorage.getItem('token')
    
    if (token) {
      // 添加Authorization请求头
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回的状态码不是200，则当做错误处理
    if (res.code !== 200) {
      Message({
        message: res.message || '请求失败',
        type: 'error',
        duration: 3000
      })
      
      // 3003: Token缺失, 3004: Token过期, 2001: 管理员未找到
      if (res.code === 3003 || res.code === 3004 || res.code === 2001) {
        // 清除token并跳转到登录页
        store.commit('SET_TOKEN', '')
        localStorage.removeItem('token')
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error)
    
    Message({
      message: error.message || '系统异常，请联系管理员',
      type: 'error',
      duration: 3000
    })
    
    return Promise.reject(error)
  }
)

export default service
