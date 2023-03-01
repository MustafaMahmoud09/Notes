package com.lomu.notesapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private var sharedPreferences:SharedPreferences?=null
    private var data=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btnRegister.setOnClickListener {
            sharedPreferences = getSharedPreferences("passwordApplication", MODE_PRIVATE)
            data=InputDataRegister.text.toString().trim()
            if(data==sharedPreferences!!.getString("passwordApp","")){
                progressBarRegister.visibility=View.VISIBLE
                Handler().postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },1000)
            }else{
                InputDataRegister.error=resources.getString(R.string.The_data_is_wrong)
            }
        }
        }
}