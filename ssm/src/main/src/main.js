// 入口文件


// 引入包
// import Vue from 'Vue'
import Vue from 'vue'
import store from './store/index.js'
import App from './App.vue'
import router from './router/router.js'
import directives from './directives'
import common from './common/index.js'
import ElementComponent from 'element-component'

// import 'font-awesome/css/font-awesome.min.css' // https://www.thinkcmf.com/font/font_awesome/icons.html
import 'normalize.css'
import './assets/style/globalstyle.css'

Vue.use(common);
Vue.use(directives);
Vue.use(ElementComponent);

Vue.config.productionTip = false;
const vm = new Vue({
	el:'#app',
    store,
	router,
	render:c => c(App),
});
