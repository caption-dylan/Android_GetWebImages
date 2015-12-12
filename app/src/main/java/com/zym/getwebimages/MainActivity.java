package com.zym.getwebimages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webGetImages;
    private String images="";
    private StringBuilder getImagesFun = new StringBuilder();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webGetImages = (WebView) this.findViewById(R.id.web_get_images);
        //启用支持javascript
        WebSettings settings = webGetImages.getSettings();
        settings.setJavaScriptEnabled(true);
        webGetImages.loadUrl("http://120.24.50.4:8000/Uploads/html/20150728035415141805.html");
        getImagesFun.append("javascript:");
        getImagesFun.append("function getAllImagesUrl(){");
            getImagesFun.append("var imgs = document.getElementsByTagName(\"img\");");
                //getImagesFun.append("var imgURLs=new Array(imgs.length);");
            getImagesFun.append("var imgURLs = \"\";");
            getImagesFun.append("for(var i = 0;i<imgs.length;i++){");
                //getImagesFun.append("imgs[i].setAttribute(\"index\", i);");
                getImagesFun.append("imgs[i].alt = i;");
                getImagesFun.append("imgURLs+= imgs[i].src + \";\";");  //拼接地址
                getImagesFun.append("imgs[i].onclick=function(){");  //添加事件
                    //getImagesFun.append("var index = this.attributes[\"index\"].nodeValue;");
                    getImagesFun.append("window.demo.openImage(this.src,this.alt);");  //调用 java方法进行传递参数
                getImagesFun.append("}");
            getImagesFun.append("}");
            getImagesFun.append("window.demo.clickOnAndroid(imgURLs);");  //调用 java方法进行传递参数
        getImagesFun.append("}");
        webGetImages.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webGetImages.loadUrl(getImagesFun.toString());
                view.loadUrl("javascript:getAllImagesUrl()");
            }
        });
        webGetImages.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
    }

    final class DemoJavaScriptInterface{
        @JavascriptInterface
        public void clickOnAndroid(String result) {
            images = result;
            Log.d("TAG", "结果是：" + images);
        }

        @JavascriptInterface
        public void openImage(String img,String i) {
            Log.d("TAG", "点击地址：" + img+"----i:"+i);
            String[] urls = images.split(";");
            Intent intent = new Intent(MainActivity.this,ShowWebImageActivity.class);
            intent.putExtra(ShowWebImageActivity.EXTRA_IMAGE_URLS, urls);
            intent.putExtra(ShowWebImageActivity.EXTRA_IMAGE_INDEX, Integer.parseInt(i));
            MainActivity.this.startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
