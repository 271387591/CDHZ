package com.cdhz.cdhz_1;

import com.hyc.androidcore.cache.GlobalDataHelper;
import com.hyc.androidcore.db.DBHelper;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.log.DefalutLogger;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.ZJEncryptionUtil;

public class APPHelper {

	/***
	 * 将unicode码转换为中文
	 * 
	 * @param unicodeStr
	 * @return
	 */
	public static String toUTF8(String unicodeStr) {
		try {
			String gbkStr = new String(unicodeStr.getBytes("ISO8859-1"),
					"UTF-8");
			return gbkStr;
		} catch (Exception e) {
			return unicodeStr;
		}
	}

	public static void saveUserInfo(String userName, String pass, String email,
			String nickName, String poto, String login) {
		if (userName != null) {
			GlobalDataHelper.getInstance().put("a.userName", userName);
		}
		if (pass != null) {
			GlobalDataHelper.getInstance().put("a.pass", pass);
		}
		if (email != null) {
			GlobalDataHelper.getInstance().put("a.email", email);
		}
		if (nickName != null) {
			GlobalDataHelper.getInstance().put("a.nickName", nickName);
		}
		if (poto != null) {
			GlobalDataHelper.getInstance().put("a.poto", poto);
		}
		if (login != null) {
			GlobalDataHelper.getInstance().put("a.login", login);
		}
		try {
			DBHelper.getInstance().openDB();
			JSONObject o = getUserInfo();
			Propertity p = new Propertity("cc3e", o.toString());
			encodePropertity(p);
			DBHelper.getInstance().insertOrUpdateById(p, "cc3e");
			DBHelper.getInstance().closeDB();
		} catch (Exception e) {
			DBHelper.getInstance().closeDB();
		}
	}

	/**
	 * 
	 * @Title: encodePropertity
	 * @Description: 对propertity数据进行加密
	 * @param @param pro 参数
	 * @return void 返回类型
	 * @author huangyc
	 * @date 2014-11-4 下午4:20:07
	 */
	public static void encodePropertity(Propertity pro) {
		if (pro == null) {
			return;
		}
		try {
			byte[] v_key = ZJEncryptionUtil.get3DESKey();
			pro.setValue(CommHelper.hexEncode(ZJEncryptionUtil.encryptDESede(
					v_key, pro.getValue().getBytes("UTF-8"))));
		} catch (Exception e) {
			e.printStackTrace();
			DefalutLogger.getInstance().OnError("加密数据异常:" + e.getMessage());
		}
	}

	public static JSONObject getSaveUserInfo() {
		try {
			DBHelper.getInstance().openDB();
			Propertity p = DBHelper.getInstance().queryOne(Propertity.class,
					"cc3e");
			DBHelper.getInstance().closeDB();
			decodePropertity(p);
			return new JSONObject(p.getValue());
		} catch (Throwable e) {
			DBHelper.getInstance().closeDB();
			return null;
		}
	}

	/**
	 * 
	 * @Title: decodePropertity
	 * @Description: 对propertity数据进行解密
	 * @param @param pro 参数
	 * @return void 返回类型
	 * @throws
	 * @author huangyc
	 * @date 2014-11-4 下午4:20:31
	 */
	public static void decodePropertity(Propertity pro) {
		if (pro == null) {
			return;
		}
		try {
			byte[] v_key = ZJEncryptionUtil.get3DESKey();
			pro.setValue(new String(ZJEncryptionUtil.decryptDESede(v_key,
					CommHelper.hexDecode(pro.getValue())), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			DefalutLogger.getInstance().OnError("加密数据异常:" + e.getMessage());
		}
	}

	public static void saveUserInfoPoto(String poto) {
		GlobalDataHelper.getInstance().put("a.poto", poto);
	}

	public static void clearUserInfo() {
		GlobalDataHelper.getInstance().clear("a.userName");
		GlobalDataHelper.getInstance().clear("a.pass");
		GlobalDataHelper.getInstance().clear("a.email");
		GlobalDataHelper.getInstance().clear("a.nickName");
		GlobalDataHelper.getInstance().clear("a.poto");
		GlobalDataHelper.getInstance().clear("a.login");
	}

	public static JSONObject getUserInfo() {
		JSONObject o = new JSONObject();
		if (GlobalDataHelper.getInstance().containKey("a.userName")) {
			o.put("userName",
					GlobalDataHelper.getInstance().getString("a.userName"));
		}
		if (GlobalDataHelper.getInstance().containKey("a.pass")) {
			o.put("pass", GlobalDataHelper.getInstance().getString("a.pass"));
		}
		if (GlobalDataHelper.getInstance().containKey("a.email")) {
			o.put("email", GlobalDataHelper.getInstance().getString("a.email"));
		}
		if (GlobalDataHelper.getInstance().containKey("a.nickName")) {
			o.put("nickName",
					GlobalDataHelper.getInstance().getString("a.nickName"));
		}
		if (GlobalDataHelper.getInstance().containKey("a.poto")) {
			o.put("poto", GlobalDataHelper.getInstance().getString("a.poto"));
		}
		if (GlobalDataHelper.getInstance().containKey("a.login")) {
			o.put("login", GlobalDataHelper.getInstance().getString("a.login"));
		}
		return o;
	}

	public static boolean islogin() {
		JSONObject o = getUserInfo();
		return ("1".equals(o.optString("login")) && !CommHelper
				.checkNull(NetHelper.getInstance().getSessionId()));
	}
}
