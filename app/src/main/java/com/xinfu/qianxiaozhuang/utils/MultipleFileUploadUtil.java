package com.xinfu.qianxiaozhuang.utils;

import android.text.TextUtils;
import android.widget.Toast;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.xinfu.qianxiaozhuang.App;
import com.xinfu.qianxiaozhuang.api.Api;
import com.xinfu.qianxiaozhuang.api.BaseResult;
import com.xinfu.qianxiaozhuang.api.model.QiNiuResponseBean;
import com.xinfu.qianxiaozhuang.api.model.QiniuModel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 21/08/2017  11:03 AM
 * Created by Zhang.
 */


public class MultipleFileUploadUtil implements UpCancellationSignal, UpCompletionHandler, UpProgressHandler {

    private static final String TAG = MultipleFileUploadUtil.class.getSimpleName();
    private ExecutorService es = Executors.newCachedThreadPool();
    private Map<String, Long> mFileSizeMap = new HashMap<>();
    private Map<String, Long> mUploadFileSizeMap = new HashMap<>();
    private List<QiNiuResponseBean> mUploadedFiles = new ArrayList<>();
    private long mUploadFileSize = 0;
    private Gson mGson;
    private int mFileNum = 0;

    private boolean mCancel = false;

    private boolean mRunning = false;

    private long mTotalFileSize;
    private List<File> mFiles;
    private QiNiuUploadUtil uploadUtil;
    private List<String> mTokens;

    private UploadListener mUploadListener;

    private int mFailRetryTimes = 5;

    public MultipleFileUploadUtil(String... paths) {
        if (paths != null) {
            mFiles = new ArrayList<>(paths.length);
            mFileNum = paths.length;
            for (int i = 0; i < mFileNum; i++) {
                File f = new File(paths[i]);
                if (f.exists()) {
                    long thisFileLength = f.length();
                    mTotalFileSize += thisFileLength;
                    LogUtil.e(TAG, "MultipleFileUploadUtil: this file size :" + thisFileLength + "   total file size:" + mTotalFileSize);
                    mFiles.add(f);
                    mFileSizeMap.put(f.getAbsolutePath(), thisFileLength);
                }
            }
            mTokens = new ArrayList<>(paths.length);
            uploadUtil = QiNiuUploadUtil.getInstance();
            mGson = new Gson();
        }
    }

    public interface UploadListener {
        void onStartGetToken();

        void onStartUpload();

        void onUploadProgress(int percentage, long bytesInProgress, long totalBytes);

        void onUploadSucceed(List<QiNiuResponseBean> resp);

        void onUploadFailed();
    }

    public void setUploadListener(UploadListener listener) {
        this.mUploadListener = listener;
    }

    public void cancel() {
        this.mCancel = true;
    }

    public void start() {
        if (!mRunning) {
            es.execute(mTask);
        } else {

            Toast.makeText(App.mInstance, "无法重新启动上传，请重新实例化！！！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isCancelled() {
        return mCancel;
    }

    @Override
    public synchronized void complete(String key, ResponseInfo info, JSONObject response) {

        if (info.isOK()) {
            try {
                LogUtil.e(TAG, "complete: --->" + response.toString());
                QiNiuResponseBean b = mGson.fromJson(response.toString(), QiNiuResponseBean.class);
                mUploadedFiles.add(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mUploadedFiles.size() == mFileNum) {

                LogUtil.e(TAG, "complete: mUploadedFiles.size() == mFileNum)");

                if (mUploadListener != null) {
                    mUploadListener.onUploadSucceed(mUploadedFiles);
                }
            } else {


            }
        } else {
            LogUtil.e(TAG, "complete: failed---->>>   " + info + " key:" + key);

            //upload to sever

            if (mTokens.size() > 0&&uploadUtil!=null) {
                uploadUtil.upload(key,mTokens.remove(0),this,this,this);
            } else {
                cancel();
                if (mUploadListener != null) {
                    mUploadListener.onUploadFailed();
                }
            }
        }
//        releaseUploadListener();
    }

    private void releaseUploadListener() {
        if (mUploadListener != null) {
            mUploadListener = null;
        }
    }

    @Override
    public void progress(String key, double percent) {
        LogUtil.e(TAG, "progress: " + key + "   " + percent);
        long thisFileSize = mFileSizeMap.get(key);
        long upLoadInThisFile = (long) (thisFileSize * percent);
        mUploadFileSizeMap.put(key, upLoadInThisFile);
        refreshTotalUploadFileSize();
        if (mUploadListener != null) {
            mUploadListener.onUploadProgress(((int) ((mUploadFileSize * 100.0f) / mTotalFileSize)), mUploadFileSize, mTotalFileSize);
        }

    }

    private void refreshTotalUploadFileSize() {
        mUploadFileSize = 0;
        for (Iterator<String> it = mUploadFileSizeMap.keySet().iterator(); it.hasNext(); ) {
            String k = it.next();
            mUploadFileSize += mUploadFileSizeMap.get(k);
        }
    }

    public void upload() {
        LogUtil.e(TAG, "upload: " );
        if (mTokens.size() < mFiles.size()) {
            cancel();
            MainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.mInstance, "Token错误", Toast.LENGTH_SHORT).show();
                }
            }, 0);
//            mUploadListener.onUploadFailed();
            return;
        }
        for (int i = 0; i < mFiles.size() && !mCancel; i++) {
            LogUtil.e(TAG, "upload: " + i);
            String filePath = mFiles.get(i).getAbsolutePath();
            String token = mTokens.remove(0);
            uploadUtil.upload(filePath, token, this, this, this);
        }
    }

    public void getToken(final int size) {

        LogUtil.e(TAG, "getToken: ");
        for (int i = 0; i < size; i++) {
            LogUtil.e(TAG, "getToken: " + i);
            Api.getApiService().getQiNiuToken()
                    .observeOn(Schedulers.single())
                    .subscribe(new Observer<BaseResult<QiniuModel>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(BaseResult<QiniuModel> t) {
                           if(t!=null&&t.getData()!=null) {
                               String token = t.getData().getToken();
                               if (!TextUtils.isEmpty(token)) {
                                   try {
                                       mTokens.add(token);
                                   } catch (Exception e) {
                                       e.printStackTrace();
                                   }
                               }
                           }

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private int mRetry = 0;
    private static final int MAX_RETRY = 10;

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if (mUploadListener != null) {
                mUploadListener.onStartGetToken();
            }
            LogUtil.e(TAG, "tokens.size:" + mTokens.size() + "  fils.size:" + mFiles.size());
            while (mTokens.size() < mFiles.size() + mFailRetryTimes && mRetry++ < MAX_RETRY) {
                LogUtil.e(TAG, "run: mTokens.size() < mFiles.size() && mRetry++ < MAX_RETRY " + " mretry:" + mRetry + "  max retry" + MAX_RETRY + " tokens.size:" + mTokens.size() + "  fils.size:" + mFiles.size());
                getToken(mFiles.size() - mTokens.size() + mFailRetryTimes);
            }

            if (mTokens.size() == (mFiles.size()+mFailRetryTimes)) {
                if (mUploadListener != null) {
                    mUploadListener.onStartUpload();
                }
                upload();
            } else {
                if (mUploadListener != null) {
                    mUploadListener.onUploadFailed();
//                    releaseUploadListener();
                    cancel();
                }
            }
        }
    };


}
