package com.xinfu.qianxiaozhuang.utils;

import android.widget.Toast;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.xinfu.qianxiaozhuang.App;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 19/08/2017  12:00 PM
 * Created by Zhang.
 */

public class QiNiuUploadUtil {
    private static final String TAG = QiNiuUploadUtil.class.getSimpleName();
    private static QiNiuUploadUtil mInstatce;
    private Recorder recorder;
    private Configuration config;
    private static final FixedZone zone = new FixedZone(new String[]{"upload-z2.qiniup.com"});

    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
    private UploadManager uploadManager ;

    private File mCurFile = null;
    private long mFileSize = 0;

    private QiNiuUploadUtil(){

        try {
            recorder = new FileRecorder(App.mInstance.getFileRecorder().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGen = new KeyGenerator() {

            public String gen(String key, File file) {
                // 不必使用url_safe_base64转换，uploadManager内部会处理
                // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
                return key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
            }
        };
        config = new Configuration.Builder()
                .chunkSize(256 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(512 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(1000)           // 链接超时。默认10秒
                .useHttps(false)               // 是否使用https上传域名
                .responseTimeout(6000)          // 服务器响应超时。默认60秒
                //.zone(FixedZone.zone1)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                       // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .recorder(recorder, keyGen)
                .build();

        uploadManager = new UploadManager(config);
    }

    public static QiNiuUploadUtil getInstance() {
        if(mInstatce==null) {
            mInstatce = new QiNiuUploadUtil();
        }
        return mInstatce;
    }

    public void upload(String localPath, String token,UpCompletionHandler upCompletionHandler,UpProgressHandler upProgressHandler,UpCancellationSignal upCancellationSignal) {
        mCurFile = new File(localPath);
        if(!mCurFile.exists()){
            Toast.makeText(App.mInstance,"文件不存在，无法上传",Toast.LENGTH_SHORT).show();
            return;
        }
        mFileSize = mCurFile.length();
        uploadManager.put(localPath,UUID.randomUUID().toString(),token,upCompletionHandler,new UploadOptions(null,null,false,upProgressHandler,upCancellationSignal));
//        uploadManager.syncPut(localPath,localPath,token,new UploadOptions(null,null,false,upProgressHandler,upCancellationSignal));
//        upCompletionHandler.complete(localPath,);
    }


}
