package com.xinfu.qianxiaozhuang.utils;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xinfu.qianxiaozhuang.R;


/**
 * Created by FanBei on 2016/7/19.
 */
public class GlideUtil {

//    /**
//     * 小图用这个
//     *
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public static void loadImage(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                //.centerCrop()
//                //.placeholder(R.mipmap.unlink)
//                //.error(R.mipmap.ic_error)
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
//    }

    /**
     * 大图的用这个
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImageTwo(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.mipmap.ic_launcher);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                //.placeholder(R.mipmap.unlink)
                //.error(R.mipmap.unlink)
                .into(imageView);
    }

//    /**
//     * 显示缩略图
//     *
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public static void loadImageThree(Context context, String url, ImageView imageView) {
//
//        Glide.with(context)
//                .load(url)
//                .thumbnail(0.1f)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
//
//    }
//
//    /**
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public static void loadImageFour(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                //.centerCrop()
//                //.placeholder(R.mipmap.unlink)
//                //.error(R.mipmap.ic_error)
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
//    }
//
//    /**
//     *显示本地图片
//     * @param context
//     * @param resourceId
//     * @param imageView
//     */
//    public static void loadImageFourTwo(Context context,Integer resourceId, ImageView imageView) {
//        Glide.with(context)
//                .load(resourceId)
//                //.centerCrop()
//                //.placeholder(R.mipmap.unlink)
//                //.error(R.mipmap.ic_error)
//                .dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imageView);
//    }
//
//    /**
//     * 小图用这个
//     *
//     * @param context
//     * @param url
//     * @param imageView
//     */
    public static void loadImageFive(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                //.centerCrop()
                //.placeholder(R.mipmap.unlink)
                //.error(R.mipmap.ic_error)
                .into(imageView);
    }
//
////    /**
////     * 开始----->活动页
////     *
////     * @param context
////     * @param url
////     * @param imageView
////     */
////    public static void loadImageSix(Context context, String url, ImageView imageView) {
////
////        Glide.with(context).load(url).listener(new RequestListener<String, GlideDrawable>() {
////            @Override
////            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
////
////                UserEvent userEvent = new UserEvent();//AccountFragment需要数据重新刷新
////                userEvent.setAdvertisement(false);
////                EventBus.getDefault().post(userEvent);
////                return false;
////            }
////
////            @Override
////            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                //imageView.setImageDrawable(resource);
////                UserEvent userEvent = new UserEvent();//AccountFragment需要数据重新刷新
////                userEvent.setAdvertisement(true);
////                EventBus.getDefault().post(userEvent);
////                return false;
////            }
////        }).into(imageView);
////    }
//
//    /**
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public static void loadImageSeven(Context context, String url, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                //.centerCrop()
//                //.placeholder(R.mipmap.unlink)
//                //.error(R.mipmap.ic_error)
//                //.dontAnimate()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .into(imageView);
//    }
//
////    /**
////     * 圆角用这个
////     *
////     * @param context
////     * @param url
////     * @param imageView
////     */
////    public static void loadImageEight(Context context, String url, ImageView imageView) {
////        RoundCornersTransformation transformation = new RoundCornersTransformation(context, MetricTransformationUtil.dip2px(context, 6), RoundCornersTransformation.CornerType.ALL);
////        Glide.with(context)
////                .load(url)
////                .fitCenter()
////                //.placeholder(R.mipmap.unlink)
////                //.error(R.mipmap.unlink)
////                .diskCacheStrategy(DiskCacheStrategy.ALL)
////                .bitmapTransform(transformation)
////                .into(imageView);
////    }
////
//    /**
//     * 圆形的用这个
//     *
//     * @param context
//     * @param url
//     * @param imageView
//     */
//    public static void loadImageNine(Context context, String url, ImageView imageView) {
//        RequestOptions myOptions = new RequestOptions().transform(new GlideCircleTransform(context));
//        Glide.with(context)
//                .load(url)
//                .fitCenter()
//                //.placeholder(R.mipmap.unlink)
//                //.error(R.mipmap.unlink)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .apply(myOptions)
//                .into(imageView);
//    }
}
