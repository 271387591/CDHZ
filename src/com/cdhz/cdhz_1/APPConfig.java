package com.cdhz.cdhz_1;

public class APPConfig {

	public static final int PAGE_SIZE = 10;

	public static final String D_AD = "a";
	public static final String D_ZHANGAUNG = "b";

	public static final String USER_POSITION_geoLng = "geoLng";
	public static final String USER_POSITION_geoLat = "geoLat";

	// 页面
	public static final String A_main = "main";
	public static final String A_about = "about";
	public static final String A_zhanguandetail = "zhanguandetail";
	public static final String A_map = "map";
	public static final String A_huizhandetail = "huizhandetail";
	public static final String A_webdesc = "webdesc";
	public static final String A_zixun = "zixun";
	public static final String A_chuxin = "chuxin";
	public static final String A_share = "share";
	public static final String A_login = "login";
	public static final String A_register = "register";
	public static final String A_userinfo = "userinfo";
	public static final String A_huizhanSearch = "huizhanSearch";
	public static final String A_updateAPP = "updateAPP";
	public static final String A_nianzhanplan = "nianzhanplan";

	// //
	public static final int H_MAIN_REFRESH = 1000;
	public static final int H_HOME_REFRESH = H_MAIN_REFRESH + 1;
	public static final int H_ZHANGUAN_REFRESH = H_HOME_REFRESH + 1;
	public static final int H_HUIZHAN_REFRESH = H_ZHANGUAN_REFRESH + 1;
	public static final int H_GOTO_ALL_ZHANGUAN = H_HUIZHAN_REFRESH + 1;
	public static final int H_UPLOADPIC = H_GOTO_ALL_ZHANGUAN + 1;
	public static final int H_CHECK_DATA = H_UPLOADPIC + 1;
	public static final int H_update_getApk = H_CHECK_DATA + 1;
	public static final int H_update_app = H_update_getApk + 1;
	public static final int H_update_install = H_update_app + 1;
	public static final int H_goto_YearPlan = H_update_install + 1;
	public static final int H_goto_HuiZhan = H_goto_YearPlan + 1;
	public static final int H_MAIN_AD = H_goto_HuiZhan + 1;

	// http://120.24.228.68:8080/Enya/app/
	public static final String BASE_URL = "http://120.24.228.68/Tenant/html/app/";
//	public static final String BASE_URL = "http://192.168.1.101:8085/Tenant/html/app/";
	// 登陆
	public static final String I_LOGIN = "login";
	// 更新用户信息
	public static final String I_updateUser = "updateUser";
	// 注册
	public static final String I_register = "register";
	// 获取展馆
	public static final String I_getHalls = "getHalls";
	// 获取展馆详细
	public static final String I_getHall = "getHall";
	// 获取展会列表
	public static final String I_getExhs = "getExhs";
	// 获取单个会展
	public static final String I_getExh = "getExh";
	// 获取评论
	public static final String I_getComments = "getComments";
	// 发表评论
	public static final String I_comment = "comment";
	// 获取会展指南
	public static final String I_getExhGuide = "getExhGuide";
	// 获取出行指南
	public static final String I_getExhGuideTo = "getExhGuideTo";
	// 获取观展攻略
	public static final String I_getExhTravel = "getExhTravel";
	// 获取展会介绍
	public static final String I_getExhDescription = "getExhDescription";
	// 获取主办信息
	public static final String I_getExhSponsor = "getExhSponsor";
	// 获取展会资讯列表
	public static final String I_getExhNews = "getExhNews";
	// 获取行业资讯列表
	public static final String I_getExhTrade = "getExhTrade";
	// 获取版本
	public static final String I_getLastVersion = "getLastVersion";
	// 获取质询
	public static final String I_getExhPlans = "getExhPlans";
	// 新闻资讯列表
	public static final String I_getNews = "getNews";
	// 新闻资讯单条
	public static final String I_getNew = "getNew";
	// 会展服务列表
	public static final String I_getExhServices = "getExhServices";
	// 单条会展服务
	public static final String I_getExhService = "getExhService";
	// 获取年展详细
	public static final String I_getExhPlan = "getExhPlan";

}
