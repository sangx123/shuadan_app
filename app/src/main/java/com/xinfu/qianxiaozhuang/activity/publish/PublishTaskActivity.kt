package com.xinfu.qianxiaozhuang.activity.publish

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.RequestCodeConfig
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_publish_task.*
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity

/**
 * 发布任务
 */
class PublishTaskActivity : BaseActivity() {
    var content=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_task)
        initUI()
    }

    private fun initUI() {
        mContent.setOnClickListener {
            var intent= Intent(this@PublishTaskActivity,PublishTaskContextEditActivity::class.java)
            intent.putExtra(PublishTaskContextEditActivity.param_data_content,content)
            startActivityForResult(intent,RequestCodeConfig.request100)
        }
    }
//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCodeConfig.request100->{
                if(resultCode==RequestCodeConfig.result200){
                    if(data!=null&&!data.getStringExtra(PublishTaskContextEditActivity.param_data_content).isNullOrBlank()){
                        content=data.getStringExtra(PublishTaskContextEditActivity.param_data_content)
                    }
                }
            }
        }
    }


}
