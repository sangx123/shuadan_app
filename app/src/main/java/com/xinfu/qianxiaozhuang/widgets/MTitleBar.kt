package com.xinfu.qianxiaozhuang.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.xinfu.qianxiaozhuang.R
import kotlinx.android.synthetic.main.common_tool_bar.view.*

/**
 * 23/08/2017  10:22 AM
 * Created by Zhang.
 */

class MTitleBar constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private var mTitle: String = ""
    private var mLeftIconRes: Int = 0
    private var mRightIconRes: Int = 0
    private var mRightIconTwoRes: Int = 0
    private var mRightText: String = ""
    private var mRightText1: String = ""
    private var mBgRes: Int = 0
    private var mRightTextColor: Int = 0
    private var mRightText1Color: Int = 0
    private var mTitleColor: Int = 0

    private var mLeftText: String = ""
    private var mLeftTextColor: Int = 0

    fun setRedDotVisible(visible: Boolean) {
        mRedDot.visibility = if (visible) View.VISIBLE else View.GONE
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.common_tool_bar, this, true)
        val a = context.obtainStyledAttributes(attrs, R.styleable.MTitleBar)
        val n = a.indexCount
        for (i in 0 until n) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.MTitleBar_title -> {
                    mTitle = a.getString(attr)
                    if (!TextUtils.isEmpty(mTitle)) {
                        setTitle(mTitle)
                    }
                }
                R.styleable.MTitleBar_background_color -> {
                    mBgRes = a.getResourceId(attr, INVALIDATE_RESOURCE)
                    if (mBgRes != INVALIDATE_RESOURCE) {
                        setToolBarBackground(mBgRes)
                    }
                }
                R.styleable.MTitleBar_left_icon -> {
                    mLeftIconRes = a.getResourceId(attr, INVALIDATE_RESOURCE)
                    if (mLeftIconRes != INVALIDATE_RESOURCE) {
                        setLeftIcon(mLeftIconRes)
                    }
                }
                R.styleable.MTitleBar_right_icon -> {
                    mRightIconRes = a.getResourceId(attr, INVALIDATE_RESOURCE)
                    if (mRightIconRes != INVALIDATE_RESOURCE) {
                        setRightIcon(mRightIconRes)
                    }
                }
                R.styleable.MTitleBar_right_icon_two -> {
                    mRightIconTwoRes = a.getResourceId(attr, INVALIDATE_RESOURCE)
                    if (mRightIconTwoRes != INVALIDATE_RESOURCE) {
                        setRightTwoIcon(mRightIconTwoRes)
                    }
                }
                R.styleable.MTitleBar_right_text -> {
                    mRightText = a.getString(attr)
                    if (!TextUtils.isEmpty(mRightText)) {
                        setRightText(mRightText)
                    }
                }
                R.styleable.MTitleBar_right_text_color -> {
                    mRightTextColor = a.getColor(attr, resources.getColor(R.color.colorAccent))
                    setRightTextColor(mRightTextColor)
                }
                R.styleable.MTitleBar_title_color -> {
                    mTitleColor = a.getColor(attr, resources.getColor(R.color.white))
                    setTitleColor(mTitleColor)
                }
                R.styleable.MTitleBar_left_text -> {
                    mLeftText = a.getString(attr)
                    setLeftText(mLeftText)
                }
                R.styleable.MTitleBar_left_text_color -> {
                    mLeftTextColor = a.getColor(attr, Color.WHITE)
                    setLeftTextColor(mLeftTextColor)
                }
                R.styleable.MTitleBar_right_text1 -> {
                    mRightText1 = a.getString(attr)
                    if (!TextUtils.isEmpty(mRightText1)) {
                        setRightText1(mRightText1)
                    }
                }
                R.styleable.MTitleBar_right_text_color1 -> {
                    mRightText1Color = a.getColor(attr, resources.getColor(R.color.colorAccent))
                    setRightTextColor1(mRightText1Color)
                }

            }

        }
        a.recycle()
        isFocusable = true
        isFocusableInTouchMode = true
        mLeftLayout.setOnClickListener {
            (context as Activity).finish()
        }
        mIvTitleRight.setOnClickListener {
            (context as Activity).finish()
        }

    }


    fun setLineColor(color: Int) {
        mLine.setBackgroundColor(resources.getColor(color))
    }

    fun setRightTwoIcon(mRightIconTwoRes: Int) {

        //        mTvRight.setVisibility(GONE);
        mIvTitleRightSecond.setVisibility(View.VISIBLE)
        mIvTitleRightSecond.setImageResource(mRightIconTwoRes)

    }

    private fun setLeftTextColor(leftTextColor: Int) {

        mTvLeft.setTextColor(leftTextColor)

    }

    private fun setLeftText(leftText: String?) {
        mTvLeft.setVisibility(View.VISIBLE)
        mIvTitleLeft.setVisibility(View.GONE)

        mTvLeft.setText(leftText)

    }


    private fun setTitleColor(mTitleColor: Int) {
        mTvTitleContent.setTextColor(mTitleColor)
    }

    private fun setRightTextColor(rightTextColor: Int) {
        mTvRight.setTextColor(rightTextColor)
    }
    private fun setRightTextColor1(rightTextColor: Int) {
        mTvRight1.setTextColor(rightTextColor)
    }

    fun setRightText(rightText: String) {
        mIvTitleRight.setVisibility(View.GONE)
        mTvRight.setVisibility(View.VISIBLE)
        mTvRight.setText(rightText)
    }
    fun setRightText1(rightText: String) {
        mIvTitleRight.setVisibility(View.GONE)
        mTvRight1.setVisibility(View.VISIBLE)
        mTvRight1.setText(rightText)
    }

    fun setRightIcon(mRightIconRes: Int) {
        mIvTitleRight.setVisibility(View.VISIBLE)
        mTvRight.setVisibility(View.GONE)
        mIvTitleRight.setImageResource(mRightIconRes)
    }

    fun setLeftIcon(mLeftIconRes: Int) {
        mIvTitleLeft.setVisibility(View.VISIBLE)
        mTvLeft.setVisibility(View.GONE)
        mIvTitleLeft.setImageResource(mLeftIconRes)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun setTitle(title: String) {
        mTvTitleContent.setText(title)
    }

    fun setToolBarBackground(toolBarBackground: Int) {
        mToolBar.setBackgroundResource(toolBarBackground)
    }

    fun setLeftOnClickListener(listener: View.OnClickListener) {
        mIvTitleLeft.setOnClickListener(listener)
        mTvLeft.setOnClickListener(listener)
    }

    fun setRightOnClickListener(listener: View.OnClickListener) {
        mIvTitleRight.setOnClickListener(listener)
        mTvRight.setOnClickListener(listener)
    }

    fun setRightTwoOnClick(listener: View.OnClickListener) {
        mIvTitleRightSecond.setOnClickListener(listener)
        //        mTvRight.setOnClickListener(listener);
    }


    fun setRightInVisible() {
        mIvTitleRight.setVisibility(View.INVISIBLE)
        mTvRight.setVisibility(View.INVISIBLE)
    }

    fun setRightVisible() {
        mIvTitleRight.setVisibility(View.VISIBLE)
        mTvRight.setVisibility(View.VISIBLE)
        mIvTitleLeft.setOnClickListener {
            (context as Activity).finish()
        }
        mIvTitleRight.setOnClickListener {
            (context as Activity).finish()
        }
    }



    companion object {
        val INVALIDATE_RESOURCE = -1
    }
}
