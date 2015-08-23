package com.cdhz.cdhz_1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.hyc.androidcore.HandlerHelper;
import com.hyc.androidcore.annotation.HAFindView;
import com.hyc.androidcore.annotation.HALayout;
import com.hyc.androidcore.annotation.HASetListener;
import com.hyc.androidcore.json.JSONArray;
import com.hyc.androidcore.json.JSONObject;
import com.hyc.androidcore.net.NetHelper;
import com.hyc.androidcore.net.NetRequestListener;
import com.hyc.androidcore.util.BitmapHelper;
import com.hyc.androidcore.util.CommHelper;
import com.hyc.androidcore.util.EnvironmentHelper;
import com.hyc.androidcore.util.TimeHelper;
import com.hyc.androidcore.views.AsyncImageView;

/***
 * 用户信息
 * 
 * @author Administrator
 *
 */
@HALayout(layout = R.layout.activity_userinfo)
public class UserInfoActivity extends BaseTitleActivity implements
		NetRequestListener {

	@HASetListener(Id = R.id.item_icon, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private AsyncImageView icon;

	@HAFindView(Id = R.id.item_username)
	private TextView userName;

	@HAFindView(Id = R.id.item_mail)
	private TextView userMail;

	@HAFindView(Id = R.id.item_nickname)
	private EditText nickName;

	private int selectImgIndex = 0;

	@HASetListener(Id = R.id.item_ok, listeners = { "setOnClickListener" }, listenersClass = { OnClickListener.class })
	private View ok;

	private String picPath = "";

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.item_icon) {
			// 上传头像
			selectIcon();
		} else if (v.getId() == R.id.item_ok) {
			uploadNickName();
		}
	}

	private Dialog mDialog;

	public void showNetDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = DialogHelper.createNetConectingDialog(this, false, "请求数据中");
		mDialog.show();
	}

	public void disMissDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	@Override
	public void handleMessage(Message msg, boolean mainThread) {
		if (msg.what == APPConfig.H_UPLOADPIC) {
			if (msg.arg1 == 1) {
				disMissDialog();
				DialogHelper.showNote(this, "上传头像失败");
			} else if (msg.arg1 == 2) {
				disMissDialog();
				APPHelper.saveUserInfoPoto(picPath);
				DialogHelper.showToast(this, "修改成功！", 1);
			}
		}
	}

	private void uploadNickName() {
		String nickNameStr = nickName.getText().toString();
		if (CommHelper.checkNull(nickNameStr)) {
			DialogHelper.showToast(this, "请输入昵称！", 2);
			return;
		}
		JSONObject o = APPHelper.getUserInfo();
		if (!nickNameStr.equals(o.optString("nickName"))) {
			// 修改昵称
			showNetDialog();
			APPNet.updateUser(nickNameStr, null, null, this);
			return;
		}
		uploadIcon();
	}

	private void uploadIcon() {
		// 修改头像
		if (CommHelper.checkNull(picPath)) {
			disMissDialog();
			HandlerHelper.getInstance().sendMessage(true, 0,
					APPConfig.H_UPLOADPIC, 2, 0);
			return;
		}
		showNetDialog();
		new Thread() {

			@Override
			public void run() {
				try {
					String url = NetHelper.getInstance().getBaseUrl()
							+ "/portrait";
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					FileInputStream fis = new FileInputStream(picPath);
					int len = 0;
					byte buffer[] = new byte[1024];
					while ((len = fis.read(buffer)) > 0) {
						baos.write(buffer, 0, len);
					}
					fis.close();
					String str = Base64.encodeToString(baos.toByteArray(),
							Base64.DEFAULT);
					baos.close();
					HttpPost httpost = new HttpPost(url);
					String comment = TimeHelper.getCurrentStamp(7) + ".png";// 文件名
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("file", str));
					nvps.add(new BasicNameValuePair("fileName", comment));
					httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					httpost.addHeader("Cookie", NetHelper.getInstance()
							.getSessionId());
					DefaultHttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = null;
					response = httpclient.execute(httpost);
					HttpEntity entity = response.getEntity();
					String charset = EntityUtils.getContentCharSet(entity);
					String body = null;
					body = EntityUtils.toString(entity);
					httpclient.getConnectionManager().shutdown();
					if (body != null) {
						APPHelper.saveUserInfoPoto(picPath);
						HandlerHelper.getInstance().sendMessage(true, 0,
								APPConfig.H_UPLOADPIC, 2, 0);
					} else {
						HandlerHelper.getInstance().sendMessage(true, 0,
								APPConfig.H_UPLOADPIC, 1, 0);
					}
				} catch (Exception e) {
					HandlerHelper.getInstance().sendMessage(true, 0,
							APPConfig.H_UPLOADPIC, 1, 0);
					e.printStackTrace();
				}
			}

		}.start();

	}

	@Override
	protected void init() {
		super.init();
		setTitle("个人信息");
		setRightButton(0, View.GONE);
		JSONObject o = APPHelper.getUserInfo();
		userName.setText(o.optString("userName"));
		userMail.setText(o.optString("email"));
		nickName.setText(o.optString("nickName"));
		if (o.optString("poto").startsWith("http")) {
			icon.setImageUrl(o.optString("poto"), R.drawable.comm_zhanwei_2);
		} else {
			Bitmap map=null;
			try {
				 map = BitmapFactory.decodeStream(new FileInputStream(o
						.optString("poto")));
			} catch (Exception e) {
				map=null;
			}
			if (map != null) {
				icon.setImageBitmap(map);
			} else {
				icon.setImageResource(R.drawable.comm_zhanwei_2);
			}

		}

	}

	private void selectIcon() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("头像选择");
		builder.setSingleChoiceItems(new String[] { "相机", "相册" }, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						selectImgIndex = 0;
						if (whichButton == 0) {
							// 系统相机
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							File v_f = EnvironmentHelper
									.getAppPicFile(TimeHelper
											.getCurrentStamp(7) + ".jpg");
							if (v_f.exists()) {
								v_f.delete();
							}
							picPath = v_f.getAbsolutePath();
							Uri imageUri = Uri.fromFile(v_f);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							startActivityForResult(intent, 0);
						} else {
							// 相册
							Intent picIntent = new Intent(Intent.ACTION_PICK,
									Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(picIntent, 1);
						}
					}
				});
		builder.create().show();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (selectImgIndex == 0) {
			// 获取图片
			if (requestCode == 1 && data != null) {
				try {
					// 调用系统相册
					Uri selectedImage = data.getData();
					Cursor c = this.getContentResolver().query(selectedImage,
							null, null, null, null);
					c.moveToFirst();
					String mPhotoPath = c.getString(c
							.getColumnIndex(MediaStore.Images.Media.DATA));
					int rotation = c
							.getInt(c
									.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
					c.close();
					Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
					if (rotation != 0) {
						bitmap = BitmapHelper.rotaBitMap(bitmap, rotation);
					}
					picPath = EnvironmentHelper.getAppPicFile(
							TimeHelper.getCurrentStamp(7) + ".png")
							.getAbsolutePath();
					BitmapHelper.saveBitmap(picPath, bitmap);
					cropImage(bitmap);
				} catch (Throwable e) {
					picPath = null;
					DialogHelper.showToast(this, "没有找到图片！", 2);
				}
			} else if (requestCode == 0) {
				// 调用系统相机
				try {
					int degree = BitmapHelper.readPictureDegree(picPath);
					Bitmap bitmap = BitmapFactory.decodeFile(picPath);
					if (degree != 0) {
						bitmap = BitmapHelper.rotaBitMap(bitmap, degree);
						BitmapHelper.saveBitmap(picPath, bitmap);
					}
					
					cropImage(bitmap);
				} catch (Throwable e) {
					picPath = null;
					DialogHelper.showToast(this, "未能获取到图片！", 2);
				}
			}
		} else if (selectImgIndex == 2 && data != null) {
			Bitmap bmap = data.getParcelableExtra("data");
			BitmapHelper.saveBitmap(picPath, bmap);
			icon.setImageBitmap(bmap);
		}

	}

	private void cropImage(Bitmap bitmap) {
		if (bitmap.getWidth() > 200 || bitmap.getHeight() > 200) {
			selectImgIndex = 2;
			Intent intent = new Intent();

			intent.setAction("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(new File(picPath)), "image/*");// mUri是已经选择的图片Uri
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);// 裁剪框比例
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 200);// 输出图片大小
			intent.putExtra("outputY", 200);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 200);
		} else {
			icon.setImageBitmap(bitmap);
		}
	}

	@Override
	public void onSuccess(String url, JSONArray data, int total) {
		if (url.startsWith(APPConfig.I_updateUser)) {
			APPHelper.saveUserInfo(null, null, null, nickName.getText().toString(), null, null);
			uploadIcon();
		}
	}

	@Override
	public void onFail(String url, String code, String msg) {
		disMissDialog();
		DialogHelper.showNote(this, msg);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
