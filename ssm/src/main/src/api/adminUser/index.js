import axios from '@/api'

/******************* 列表 *******************/
export const getTableData = (page,limit,keywords) => {
	return axios.request({
		url:'/sys/user/list?page=' + page + '&limit=' + limit + keywords,
		method:'get',
	});
}


/******************* 添加 *******************/
export const add = (data) => {
	return axios.request({
		url:'/sys/user/save',
		method:'post',
		data:data
	});
}

/******************* 编辑 *******************/
export const edit = (data) => {
	return axios.request({
		url:'/sys/user/update',
		method:'post',
		data:data
	});
}

/******************* 重置密码 *******************/
export const password = (id) => {
	return axios.request({
		url:'/sys/user/password/'+id,
		method:'get',
	});
}

/******************* 删除 *******************/
export const del = (id) => {
	return axios.request({
		url:'/sys/user/del/'+id,
		method:'get',
	});
}