package com.xinfu.qianxiaozhuang.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.xinfu.qianxiaozhuang.utils.DisplayUtil;

public class CircleView extends View {
    private Paint paint;
    private SweepGradient mSweepGradient;
    private int witdh;
    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint=new Paint();
        anim = new CircleAnim();
        //witdh=new  DisplayUtil(getContext()).dpToPixel(100f);
        //Log.e("sangxiang", "100dp= "+new DisplayUtil(getContext()).dpToPixel(100f) );
        //设置是否使用抗锯齿功能，会消耗较大资源，绘制图形速度会变慢。
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        /**
         Android在用画笔的时候有三种Style，分别是
         Paint.Style.STROKE 只绘制图形轮廓（描边）
         Paint.Style.FILL 只绘制图形内容
         Paint.Style.FILL_AND_STROKE 既绘制轮廓也绘制内容
         */
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        // 设置渐变
        //int[] mGradientColors = {Color.GREEN, Color.YELLOW, Color.RED};
        int[] mGradientColors = {Color.parseColor("#114BFF"), Color.parseColor("#114BFF"), Color.parseColor("#114BFF")};
        mSweepGradient = new SweepGradient(witdh/2-10, witdh/2-10, mGradientColors, null);
        paint.setShader(mSweepGradient);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        witdh = MeasureSpec.getSize(widthMeasureSpec);
        Log.e("sangxiang", "onMeasure: "+witdh );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画布旋转135度，围绕中心点旋转
        canvas.rotate(135,witdh/2,witdh/2);
        /**
         oval :指定圆弧的外轮廓矩形区域。
         startAngle: 圆弧起始角度，单位为度。
         sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         useCenter:  如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。关键是这个变量，下面将会详细介绍。

         */
        canvas.drawArc(new RectF(10,10,witdh-10,witdh-10),startAngle,sweepAngle,false,paint);
    }

    private float progressSweepAngle=0;//进度条圆弧扫过的角度
    private float startAngle=0;//背景圆弧的起始角度
    private float sweepAngle=270;//背景圆弧扫过的角度
    private float maxAngle=270;//进度条最大值
    private float progressNum=0;//可以更新的进度条数值
    private CircleAnim anim;
    public class CircleAnim extends Animation {

        public CircleAnim(){
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if(progressNum>sweepAngle) {
                sweepAngle = interpolatedTime * maxAngle;
                postInvalidate();
            }
        }
    }

    public void setProgressNum(int num) {
        sweepAngle=0;
        progressNum=num;
        anim.setDuration(1000);
        this.startAnimation(anim);
    }
}
