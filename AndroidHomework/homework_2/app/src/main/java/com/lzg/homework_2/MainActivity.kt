package com.lzg.homework_2

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 加载并显示布局文件 res/layout/activity_main.xml
        //setContentView(R.layout.activity_main)
        //setContentView(R.layout.table_layout)
        setContentView(R.layout.img_constraint_latout)
    }
}
