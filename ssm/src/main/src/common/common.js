/************************* 公共函数文件(会被加到 vue 实例上) *************************/
export default {
    /**
     * 页面跳转
     * @param data 请求返回的数据对象
     * @param This vue实例
     */
    jumpUrl:(data,_this) => {
        if (data.code == 401){
            _this.$router.push('/401');
        }else {
            _this.$ls.clear();
            _this.$store.dispatch('clearLoginOut');
            _this.$router.push('/login');
        }
        _this.error(data.msg);
    },
    /**
     * 删除图片
     * @param url 请求地址
     * @param path 待删除图片路径
     * @param success 删除成功回调
     * @param error 删除失败回调
     * @param type 请求类型
     */
    deluploadImage:function(url,path,success,error,type='post') {
        this.axios.request({
            url:url,
            method:type,
            data:{path:path}
        }).then(res => {
            if(res.data.code == 1){
                return success && success();
            }else{
                return error && error();
            }
        });
    }
}