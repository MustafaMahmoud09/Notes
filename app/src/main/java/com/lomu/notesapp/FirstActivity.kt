package com.lomu.notesapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class FirstActivity : AppCompatActivity() {
    private var sharedPreferences:SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        Handler().postDelayed({
                               sharedPreferences=getSharedPreferences("passwordApplication", MODE_PRIVATE)
                               if(sharedPreferences!!.getString("passwordApp","").toString().isEmpty()) {
                                   startActivity(Intent(this, MainActivity::class.java))
                               }else{
                                   startActivity(Intent(this,RegisterActivity::class.java))
                               }
                               finish()
                              },3000)
    }
}