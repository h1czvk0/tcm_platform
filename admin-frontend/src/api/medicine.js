import request from '@/utils/request'

export function getMedicinePage(params) {
  return request({
    url: '/medicine/page',
    method: 'get',
    params
  })
}

export function getMedicineDetail(medicineId) {
  return request({
    url: `/medicine/${medicineId}`,
    method: 'get'
  })
}

export function addMedicine(data) {
  return request({
    url: '/medicine/add',
    method: 'post',
    data
  })
}

export function updateMedicine(data) {
  return request({
    url: '/medicine/update',
    method: 'put',
    data
  })
}

export function deleteMedicine(medicineId) {
  return request({
    url: `/medicine/${medicineId}`,
    method: 'delete'
  })
}
