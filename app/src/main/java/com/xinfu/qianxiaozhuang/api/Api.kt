package com.xinfu.qianxiaozhuang.api

import android.widget.Toast
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.error
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.xinfu.qianxiaozhuang.BuildConfig
import com.xinfu.qianxiaozhuang.SpConfig


/**
 * retrofit网络请求
 */


class Api private constructor() {

    private val DEFAULT_TIMEOUT = 60
    lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService
    private lateinit var apiImageService: ApiService
    private var httpClientBuilder: OkHttpClient.Builder
    private lateinit var mReqLogFp: File
    var mCurDateTime: String? = null //yyyy-MM-dd HH:mm:ss
    //构造方法私有
    init {
        //手动创建一个OkHttpClient并设置超时时间
        httpClientBuilder = OkHttpClient.Builder()
        //mReqLogFp = App.requestLog
        httpClientBuilder.addInterceptor(AddHeaderInterceptor())
        httpClientBuilder.addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        createApiService()
    }

    fun createApiService() {
//        此方法可以设置成Date类型
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
        retrofit = Retrofit.Builder()
//                .addConverterFactory(ShowContentConverter())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build())
                .build()
        apiService = retrofit.create(ApiService::class.java)
        apiImageService= Retrofit.Builder()
//                .addConverterFactory(ShowContentConverter())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://188.131.235.188:8081/")
                .client(httpClientBuilder.build())
                .build().create(ApiService::class.java)
    }

    //在访问EmucooRequest时创建单例
    private object SingletonHolder {
        internal val INSTANCE = Api()
    }

    //获取单例
    companion object {
        var defaultIp: String = ""
        get() {
            return BuildConfig.HOST_URL
        }
        var BASE_URL = defaultIp
            set(value) {
                if (value.isBlank()) {
                    return
                }
                if (value.toLowerCase().startsWith("http")) {
                    field = value
                    getInstance().createApiService()
                } else {
                    val pair = value.toIPPair()
                    val ip = pair.first.split(".")
                    if (ip.size == 4) {
                        for (segment in ip) {
                            val parsedSegment = segment.toIntOrNull()
                            if (parsedSegment == null) {
                                return
                            }
                            if (parsedSegment > 255) {
                                return
                            }
                        }
                        field = "http://${pair.first}:${pair.second}/"
                        getInstance().createApiService()
                    }
                }

            }
            get() {
                return defaultIp
            }

        private fun getInstance(): Api {
            return SingletonHolder.INSTANCE
        }

        @JvmStatic
        fun getApiService(): ApiService {
            return getInstance().apiService
        }
        @JvmStatic
        fun getImageApiService(): ApiService {
            return getInstance().apiImageService
        }
        fun String.toIPPair():Pair<String/*ip*/,String/*port*/>{
            var tempUrl :String
            if(this.toLowerCase().startsWith("http")){
                tempUrl = this
            }else{
                tempUrl = "http://$this"
            }
            val url = URL(tempUrl)
            return url.host to if(url.port == -1) "80" else url.port.toString()
        }

    }

    //加请求头
    class AddHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
        if (!Hawk.get<String>(SpConfig.accessToken).isNullOrBlank()) {
            builder.addHeader("userToken", Hawk.get<String>(SpConfig.accessToken))
        }
            //@Headers("Version:1", "ApiType:Android")
            builder.addHeader("Version", "1")
            builder.addHeader("ApiType", "Android")
            return chain.proceed(builder.build())
        }
    }

}