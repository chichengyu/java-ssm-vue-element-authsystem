import types from './types.js'

const getMenuList = (routesList,nowRole) => {
	let siderList = [];
	for (let key in routesList) {
		let item = routesList[key];
		// if (item.isShowMenu === false) {
		// 	continue;
		// }
		if ((item.isMenu === false) || (item.meta && item.meta.isShowMenu === false)) {
			continue;
		}
		const sider = {
			name:'',
			icon:'',
			path:'',
		};
		if (item.meta && item.meta.icon && item.meta.title && item.path){
			sider.name = item.meta.title;
			sider.icon = item.meta.icon;
			sider.path = item.path;
			if (nowRole != 1 && !item.children && item.meta && item.meta.isShowMenu === false){
				sider.name = '';
				sider.icon = '';
				sider.path = '';
			}
		}
		let childItem = item.children;
		if (childItem) {
			if (childItem.length == 1 && childItem[0].path === 'index'){
				childItem = childItem[0];
				if (childItem.meta && childItem.meta.icon && childItem.meta.title && childItem.path) {
					if (nowRole == 1 || (childItem.meta && (childItem.meta.isShowMenu!==false)||(childItem.meta.isShowMenu===true))){
						sider.name = childItem.meta.title;
						sider.icon = childItem.meta.icon;
						sider.path = item.redirect;
					}
				}
			} else {
				let childrenSider = getMenuList(item.children,nowRole);
				if (childrenSider.length > 0){
					for(let key in childrenSider){
						childrenSider[key].path = item.path + '/' + childrenSider[key].path;
					}
					sider.children = childrenSider;
				}else {
					sider.name = '';
					sider.icon = '';
					sider.path = '';
				}
			}
		}
		sider.name && sider.icon && sider.path && siderList.push(sider);
	}
	return siderList;
};

const mutations = {
	[types.SET_TOKEN] (state,isAuthToken) {
		state.isAuthToken = isAuthToken;
	},
	[types.SET_USER] (state,userInfo) {
		if (userInfo){
			state.userInfo = userInfo;
		}else{
			state.userInfo = {};
		}
	},
	[types.SET_NEWROUTES] (state,newRoutes) {
		state.routes = newRoutes;
		state.isAuthToken = true;
	},
	[types.SET_SIDERLIST] (state,siderList) {
		if(!siderList){
			return state.siderList = siderList;
		}
		siderList = getMenuList(siderList,state.userInfo.roles);
		if (Array.isArray(siderList) && siderList.length > 0) {
			state.siderList = siderList;
		}else {
			state.siderList = [];
		}
	},
};

export default mutations
