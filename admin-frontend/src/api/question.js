import request from '@/utils/request'

export function getQuestionPage(params) {
  return request({
    url: '/question/page',
    method: 'get',
    params
  })
}

export function addQuestion(data) {
  return request({
    url: '/question/add',
    method: 'post',
    data
  })
}

export function updateQuestion(data) {
  return request({
    url: '/question/update',
    method: 'put',
    data
  })
}

export function deleteQuestion(questionId) {
  return request({
    url: `/question/${questionId}`,
    method: 'delete'
  })
}
