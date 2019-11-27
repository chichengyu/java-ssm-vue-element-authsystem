import types from './types.js'
import {routes,routesMap} from '@/router/routes.js'
import rules from '@/router/rules.js'

const hasAccess = (route,rulesIds) => {
	return rules.some(el => {
		if (route.name == el.name && rulesIds.some(item=> el.id==item)){
			return true;
		}
		return false;
	});
};
const getAccessRoutes = (routesMap,rulesIds) => {
	let newRouter = routesMap.filter(item => {
		if (item.children && Array.isArray(item.children)) {
			 item.children = item.children.map(item => {
				(!item.meta) && (item.meta = {isShowMenu:false});
				if (hasAccess({...item},rulesIds)) {
					item.meta.isShowMenu = true;
				}else {
					item.meta.isShowMenu = false;
				}
				 return item;
			 });
			(item.children.every(item => item.meta.isShowMenu === false)) && (!(item.meta) && (item.meta = {isShowMenu:false}));
			return item.children.length;
		}
	});
	newRouter[newRouter.length] = routesMap[routesMap.length-1];
	return newRouter;
};

const actions = {
	setAuthToken ({commit},isAuthToken) {
		commit(types.SET_TOKEN,isAuthToken);
	},
	setUserInfo ({commit},userInfo){
		commit(types.SET_USER,userInfo);
	},
	clearLoginOut ({commit}) {
		commit(types.SET_USER,null);
		commit(types.SET_SIDERLIST,null);
		commit(types.SET_NEWROUTES,routes);
		commit(types.SET_TOKEN,false);
	},
	setSiderList ({commit},siderList) {
		commit(types.SET_SIDERLIST,siderList);
	},
	getUserInfo ({commit},userInfo) {
		return new Promise((resolve,reject) => {
			try {
				const roles = userInfo.roles;
				if (roles && (roles == 1 || roles.length > 0)) {
					userInfo.roles = [roles];
					commit(types.SET_USER,userInfo);
				} else {
					reject('getInfo: roles must be a non-null array !')
				}
				resolve(userInfo);
			} catch(e) {
				console.log('getUserInfo',error);
				reject(error);
			}
		});
	},
	concatRlues ({commit},userInfo) {
		return new Promise((resolve,reject) => {
			try {
				let routerList = [];
				if (userInfo.roles.includes(1)) {
			 		routerList = routesMap;
			 	} else {
			 		routerList = getAccessRoutes(routesMap,userInfo.rules);
				}
				commit(types.SET_NEWROUTES,routes.concat(routerList));
				commit(types.SET_SIDERLIST,this.getters.routes);
				resolve(this.getters.routes);
			 } catch(error) {
			 	console.log('concatRlues',error);
			 	reject(error);
			 }
		});
	}
};

export default actions