/************************* 公共函数文件(当前文件不会被打包) *************************/
/**
 * 搜索关键词（默认值）及数据拼接
 * @param data  待搜索的数据对象
 * @returns {string}
 */
const handleSearchData = (data) => {
    let searchWords = '';
    if (data){
        for (let key in data){
            if (data[key] != ''){
                if (Array.isArray(data[key])) {
                    let flag = '';
                    for (let k in data[key]){
                        if(!data[key][k]){
                            flag = true;
                            break;
                        }
                    }
                    if (flag)continue;
                }
                searchWords += '&' + key + '=' + data[key];
            }
        }
    }
    return searchWords;
};

/**
 * 批量修改对象直
 * @param data
 * @param val
 * @constructor
 */
const ObjectforIn = (data,val) => {
    for (var key in data){
        data[key] = val;
    }
};