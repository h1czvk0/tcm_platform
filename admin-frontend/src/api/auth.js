import request from '@/utils/request'

export function adminLogin(data) {
  return request({
    url: '/auth/admin/login',
    method: 'post',
    data
  })
}
