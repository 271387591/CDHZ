package com.cdhz.cdhz_1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lihao1 on 8/23/15.
 */
public class WebViewUtils {
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private static  Pattern pattern=Pattern.compile(".*(.jpg|.png|.gif|.jpeg|.bmp)$");



    public static void initView(WebView webView,String url,Context context){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);



        //缓存暂时不用
        //设置 缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        //开启 database storage API 功能
//        webView.getSettings().setDatabaseEnabled(true);
//        String cacheDirPath = context.getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
//      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
//        Log.i(context.getApplicationInfo().className, "cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
//        webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
//        webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
//        webView.getSettings().setAppCacheEnabled(true);

        webView.loadUrl(url);
        startWebView(webView,context);

    }
    static private void startWebView(WebView webView, final Context context) {
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Matcher matcher = pattern.matcher(url.toLowerCase());
                if(matcher.matches()){
                    return true;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
                return false;
            }
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null && url.contains("webview")) {

                    progressDialog = new ProgressDialog(context);
                    progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
                    progressDialog.setCanceledOnTouchOutside(true);//设置在点击Dialog外是否取消Dialog进度条
                    progressDialog.setMessage("加载中...");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                view.setVisibility(View.GONE); // 隐藏加载界面

            }

        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {

                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
    }

    /**
     * 清除WebView缓存
     */
    static public void clearWebViewCache(Context context){

        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(context.getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME);

        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()+"/webviewCache");

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    static private void deleteFile(File file) {


        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }
}
