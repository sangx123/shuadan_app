package com.xinfu.qianxiaozhuang.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.github.lzyzsd.jsbridge.BridgeHandler
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.github.lzyzsd.jsbridge.DefaultHandler
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.widget.CommonTitleBar
import kotlinx.android.synthetic.main.activity_notice_web_view.*
import org.apache.http.util.EncodingUtils

/**
 * 网页
 */
class NoticeWebActivity : BaseActivity(), CommonTitleBar.IClickTxtBack {

    companion object {
        val param_title="title"
        val param_httpUrl="url"
        val param_postData="postData"
        var param_local_html="param_local_html"

    }
    var title:String=""
    var httpUrl:String = ""
    var postData:String = ""
    var localHtmlPath=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_web_view)
        intent.getStringExtra(param_title)?.let {
            title =it;
        }
        intent.getStringExtra(param_httpUrl)?.let {
            httpUrl =it
        }
        intent.getStringExtra(param_postData)?.let {
            postData = it//用户登录之后的登录信息
        }
        intent.getStringExtra(param_local_html)?.let {
            localHtmlPath =it;
        }
        initUI()
    }


    private fun initUI() {

        //titlebar_withdrawsetTitle(title)
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick(this@NoticeWebActivity, null)
        if(!localHtmlPath.isNullOrBlank()){
            webView.loadUrl(localHtmlPath)
            return
        }
        val settings = webView.getSettings()
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE)
        loadingData(httpUrl)

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);


        webView.setDefaultHandler(DefaultHandler())
        webView.setWebChromeClient(object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, newProgress: Int) {

                if (progressbar.getVisibility() == View.VISIBLE) {
                    progressbar.setProgress(newProgress)
                }
                if (newProgress < 100) {
                    if (progressbar.getVisibility() != View.VISIBLE) {
                        progressbar.setVisibility(View.VISIBLE)
                    }
                    //progressBar.setSecondaryProgress(newProgress - 10);
                } else {
                    if (progressbar.getVisibility() != View.GONE) {
                        progressbar.setVisibility(View.GONE)
                    }
                }
            }

            //            @Override
            //            public void onReceivedTitle(WebView view, String title) {
            //                commonTitleBar.setTitle(title);//获取网页标题
            //            }
        })
        webView.registerHandler("submitFromWeb", BridgeHandler { data, function ->
            //必须和js同名函数，注册具体执行函数，类似java实现类。
            handleFromJSMessage(data)
        })

        webView.callHandler("functionInJs", "", //分享回调JS的方法
                CallBackFunction { })

        webView.send("hello")

    }

    /**
     * 加载相应的数据链接
     *
     * @param tempUrl
     */
    private fun loadingData(tempUrl: String) {

        if (TextUtils.isEmpty(postData)) {//没有登录的情况下 加载url
            webView.loadUrl(tempUrl) // 加载url
        } else {//登录的情况下 就用户信息传递过去 加载url
            val postDataByte = EncodingUtils.getBytes(postData, "utf-8")
            webView.postUrl(tempUrl, postDataByte)
        }
    }

    private fun handleFromJSMessage(jsonData: String) {

    }


    override fun onClickTxtBackCallBack() {

        finish()

    }
}
