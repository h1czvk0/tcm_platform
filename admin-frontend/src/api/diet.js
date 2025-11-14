import request from '@/utils/request'

export function getDietPage(params) {
  return request({
    url: '/diet/page',
    method: 'get',
    params
  })
}

export function addDiet(data) {
  return request({
    url: '/diet/add',
    method: 'post',
    data
  })
}

export function updateDiet(data) {
  return request({
    url: '/diet/update',
    method: 'put',
    data
  })
}

export function deleteDiet(dietId) {
  return request({
    url: `/diet/${dietId}`,
    method: 'delete'
  })
}
