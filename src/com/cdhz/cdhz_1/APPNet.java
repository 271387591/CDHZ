package com.cdhz.cdhz_1;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.net.NetRequestListener;

public class APPNet {

	// 获取年展
	public final static void getExhPlan(String id, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhPlan + "/" + id, p,
				listener);
	}

	// 获取会展服务
	public final static void getExhService(String id,
			NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhService + "/" + id,
				p, listener);
	}

	// 获取新闻资讯单条
	public final static void getNewOne(String id, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getNew + "/" + id, p,
				listener);
	}

	/***
	 * 取展馆列表，获取热门展馆
	 * 
	 * @param remen
	 * @param start
	 * @param length
	 * @param listener
	 */
	public final static void getExhPlans(int start, int length,
			NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("start", "" + start));
		params.add(new BasicNameValuePair("limit", "" + length));
		String p = URLEncodedUtils.format(params, "UTF-8");
		// JSONObject o = new JSONObject();
		// o.put("start", "" + start);
		// o.put("limit", "" + length);
		// o.put("Q_t.hot_EQ", remen ? "1" : "0");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhPlans, p, listener);
	}

	public final static void checkVerson(NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getLastVersion + "/Common",
				p, listener);

	}

	public final static void updateUser(String nickName, String gender,
			String birth, NetRequestListener listener) {
		JSONObject o = new JSONObject();
		o.put("nickName", nickName);
		o.put("gender", gender);
		o.put("birth", birth);
		NetHelper.getInstance().request(APPConfig.I_updateUser, o.toString(),
				listener);

	}

	public final static void login(String username, String password,
			NetRequestListener listener) {

		// List<BasicNameValuePair> params = new
		// LinkedList<BasicNameValuePair>();
		// params.add(new BasicNameValuePair("username", username));
		// params.add(new BasicNameValuePair("password", password));
		// String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码

		JSONObject o = new JSONObject();
		o.put("username", username);
		o.put("password", password);
		NetHelper.getInstance().request(APPConfig.I_LOGIN, o.toString(),
				listener);
	}

	/*
	 * 注册
	 */
	public final static void register(String userName, String mail,
			String nickName, String pass, NetRequestListener listener) {

		JSONObject o = new JSONObject();
		o.put("username", userName);
		o.put("password", pass);
		o.put("email", mail);
		o.put("nickName", nickName);
		// List<BasicNameValuePair> params = new
		// LinkedList<BasicNameValuePair>();
		// params.add(new BasicNameValuePair("username", userName));
		// params.add(new BasicNameValuePair("password", pass));
		// params.add(new BasicNameValuePair("email", mail));
		// params.add(new BasicNameValuePair("nickName", nickName));
		// String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_register, o.toString(),
				listener);

	}

	/***
	 * 取展馆列表，获取热门展馆
	 * 
	 * @param remen
	 * @param start
	 * @param length
	 * @param listener
	 */
	public final static void getHalls(boolean remen, int start, int length,
			NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		if (remen) {
			params.add(new BasicNameValuePair("Q_t.hot_EQ", "1"));
		} else {
			params.add(new BasicNameValuePair("Q_t.hot_EQ", "0"));
		}
		params.add(new BasicNameValuePair("start", "" + start));
		params.add(new BasicNameValuePair("limit", "" + length));
		String p = URLEncodedUtils.format(params, "UTF-8");
		// JSONObject o = new JSONObject();
		// o.put("start", "" + start);
		// o.put("limit", "" + length);
		// o.put("Q_t.hot_EQ", remen ? "1" : "0");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getHalls, p, listener);
	}

	// 获取展馆详细
	public final static void getHall(String id, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getHall + "/" + id, p,
				listener);
	}

	/***
	 * 获取会展列表
	 * 
	 * @param huizhanSort
	 * @param createSort
	 * @param tradeNameSort
	 * @param nameLike
	 * @param start
	 * @param length
	 * @param listener
	 */

	public final static void getExhs(String huizhanSort, String createSort,
			String tradeNameSort, String nameLike, int start, int length,
			NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		if (huizhanSort != null) {
			params.add(new BasicNameValuePair("Q_h.name", huizhanSort));
		}
		if (createSort != null) {
			params.add(new BasicNameValuePair("Q_exh.createDate", createSort));
		}
		if (tradeNameSort != null) {
			params.add(new BasicNameValuePair("Q_exh.tradeNames", tradeNameSort));
		}
		if (nameLike != null) {
			params.add(new BasicNameValuePair("Q_exh.name_LK", nameLike));
		}
		params.add(new BasicNameValuePair("start", "" + start));
		params.add(new BasicNameValuePair("limit", "" + length));
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhs, p, listener);
	}

	/**
	 * 获取单个展会
	 * 
	 * @param id
	 * @param listener
	 */
	public final static void getExh(String id, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExh + "/" + id, p,
				listener);
	}

	/***
	 * 获取评论
	 * 
	 * @param type
	 * @param itemId
	 * @param start
	 * @param length
	 * @param listener
	 */
	public final static void getComments(String type, String itemId, int start,
			int length, NetRequestListener listener) {
		// List<BasicNameValuePair> params = new
		// LinkedList<BasicNameValuePair>();
		// if (type != null) {
		// params.add(new BasicNameValuePair("type", type));
		// }
		// if (itemId != null) {
		// params.add(new BasicNameValuePair("itemId", itemId));
		// }
		// params.add(new BasicNameValuePair("start", "" + start));
		// params.add(new BasicNameValuePair("limit", "" + length));
		// String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码

		JSONObject o = new JSONObject();
		o.put("type", type);
		o.put("itemId", itemId);
		o.put("start", start);
		o.put("limit", length);

		NetHelper.getInstance().request(APPConfig.I_getComments, o.toString(),
				listener);
	}

	/***
	 * 发表评论
	 * 
	 * @param type
	 * @param itemId
	 * @param content
	 * @param rank
	 * @param listener
	 */
	public final static void comment(String type, String itemId,
			String content, String rank, NetRequestListener listener) {
		// List<BasicNameValuePair> params = new
		// LinkedList<BasicNameValuePair>();
		// params.add(new BasicNameValuePair("type", type));
		// params.add(new BasicNameValuePair("itemId", itemId));
		// params.add(new BasicNameValuePair("rank", rank));
		// params.add(new BasicNameValuePair("content", content));
		// String p = URLEncodedUtils.format(params, "UTF-8");
		JSONObject o = new JSONObject();
		o.put("type", type);
		o.put("itemId", itemId);
		o.put("rank", rank);
		o.put("content", content);
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_comment, o.toString(),
				listener);
	}

	/***
	 * 获取参展指南
	 * 
	 * @param id
	 * @param listener
	 */
	public final static void getExhGuide(String id, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhGuide + "/" + id, p,
				listener);
	}

	/***
	 * 获取出行指南
	 * 
	 * @param id
	 * @param listener
	 */
	public final static void getExhGuideTo(String id,
			NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhGuideTo + "/" + id,
				p, listener);
	}

	/***
	 * 获取观展攻略
	 * 
	 * @param id
	 * @param listener
	 */
	public final static void getExhTravel(String id, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhTravel + "/" + id, p,
				listener);
	}

	/***
	 * 获取展会介绍
	 * 
	 * @param id
	 * @param listener
	 */
	public final static void getExhDescription(String id,
			NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(
				APPConfig.I_getExhDescription + "/" + id, p, listener);
	}

	/***
	 * 获取主办信息
	 * 
	 * @param id
	 * @param listener
	 */
	public final static void getExhSponsor(String id,
			NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhSponsor + "/" + id,
				p, listener);
	}

	/***
	 * 获取展会资讯列表
	 * 
	 * @param huizhanId
	 * @param start
	 * @param length
	 * @param listener
	 */
	public final static void getExhNews(String huizhanId, int start,
			int length, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		if (huizhanId != null) {
			params.add(new BasicNameValuePair("Q_exhId_EQ", huizhanId));
		}
		params.add(new BasicNameValuePair("start", "" + start));
		params.add(new BasicNameValuePair("limit", "" + length));
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhNews, p, listener);
	}

	/***
	 * 获取行业资讯列表
	 * 
	 * @param huizhanID
	 * @param start
	 * @param length
	 * @param listener
	 */
	public final static void getExhTrade(String huizhanID, int start,
			int length, NetRequestListener listener) {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		if (huizhanID != null) {
			params.add(new BasicNameValuePair("exhId", huizhanID));
		}
		params.add(new BasicNameValuePair("start", "" + start));
		params.add(new BasicNameValuePair("limit", "" + length));
		String p = URLEncodedUtils.format(params, "UTF-8");
		// 对参数编码
		NetHelper.getInstance().request(APPConfig.I_getExhTrade, p, listener);
	}
}
