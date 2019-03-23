package com.xinfu.qianxiaozhuang.sqllite

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class test(var name:String){
    @Id
    var id:Long = 0
}