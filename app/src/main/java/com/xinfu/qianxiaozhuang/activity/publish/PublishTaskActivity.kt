package com.xinfu.qianxiaozhuang.activity.publish

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_publish_task.*
import org.jetbrains.anko.startActivity

/**
 * 发布任务
 */
class PublishTaskActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_task)
        initUI()
    }

    private fun initUI() {
        mContent.setOnClickListener {
            startActivity<PublishTaskContextEditActivity>()
        }
    }
}
