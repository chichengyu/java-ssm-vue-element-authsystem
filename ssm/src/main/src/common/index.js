import common from "./common.js";
export default {
    install:function(Vue, options) {
        for (var key in common){
            Vue.prototype[key] = common[key];
        }
    }
}