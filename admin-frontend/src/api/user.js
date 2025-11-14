import request from '@/utils/request'

export function getUserPage(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export function updateUserStatus(userId, status) {
  return request({
    url: `/user/status/${userId}`,
    method: 'put',
    params: { status }
  })
}
