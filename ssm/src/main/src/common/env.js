/************************* 变量文件(当前文件不会被打包) *************************/
const userInfo = 'userInfo';// 存个人信息的键名
const timeout = 1000;// 页面跳转时间
const pageOffset = 5;// 每页显示的条数

const project = 'xxxxxxxxxxxx系统';

const ip = 'http://localhost:8080';
// const ip = 'http://192.168.5.100:8001';

const host = 'http://192.168.5.100:8003';// 资源服务器

// const baseUrl = ip + '/api/v1';
const baseUrl = ip;
/********************* 图片管理(上传目录可以随便定义) ***********************/
// 注意：删除图片，只需要改地址即可
const delUploadImageUrl = host + '/uploads/del';// 删除已上传图片地址
const upload = ip + '/upload/';

const uploadExcelUrl = baseUrl + '/loan/excel';// excel车抵贷导入地址

const uploadIdCardUrl = host + '/upload/zn_IdCard';// 身份证上传
const uploadBankCardUrl = host + '/upload/zn_bankCard';// 银行卡图片上传

