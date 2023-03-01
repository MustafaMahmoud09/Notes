package com.lomu.notesapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_write_note.*
import kotlinx.android.synthetic.main.dialog_select_color.view.*
import java.util.*

class WriteNoteActivity : AppCompatActivity() {
    private val database=SqlLite(this)
    private var color=1
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_note)
        btnBackWrite.setOnClickListener {
            finish()
        }
        btnSaveWrite.setOnClickListener {
            codeBtnSaveWrite()
        }
        btnColor.setOnClickListener {
            codeBtnColorAndShowColors()
        }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun codeBtnColorAndShowColors() {
        val dialog = AlertDialog.Builder(this)
        val layoutDialog = LayoutInflater.from(this).inflate(R.layout.dialog_select_color, null)
        dialog.setView(layoutDialog)
        val create = dialog.create()
        create.show()
        clickBtnColors(layoutDialog, create)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun clickBtnColors(
        layoutDialog: View,
        create: AlertDialog
    ) {
        layoutDialog.btnClose.setOnClickListener {
            create.dismiss()
        }
        layoutDialog.btnOne.setOnClickListener {
            layoutDialog.btnOne.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=1
            waiting(create)
        }
        layoutDialog.btnTwo.setOnClickListener {
            layoutDialog.btnTwo.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=2
            waiting(create)
        }
        layoutDialog.btnThree.setOnClickListener {
            layoutDialog.btnThree.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=3
            waiting(create)
        }
        layoutDialog.btnFour.setOnClickListener {
            layoutDialog.btnFour.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=4
            waiting(create)
        }
        layoutDialog.btnFive.setOnClickListener {
            layoutDialog.btnFive.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=5
            waiting(create)
        }
        layoutDialog.btnSix.setOnClickListener {
            layoutDialog.btnSix.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=6
            waiting(create)
        }
        layoutDialog.btnSeven.setOnClickListener {
            layoutDialog.btnSeven.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=7
            waiting(create)
        }
        layoutDialog.btnEight.setOnClickListener {
            layoutDialog.btnEight.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
            color=8
            waiting(create)
        }
        layoutDialog.btnNine.setOnClickListener {
            layoutDialog.btnNine.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
             color=9
            waiting(create)
        }
        layoutDialog.btnTen.setOnClickListener {
            layoutDialog.btnTen.foreground = resources.getDrawable(R.drawable.ic_baseline_check_24)
             color=10
            waiting(create)
        }
    }

    private fun waiting(create: AlertDialog) {
        Handler().postDelayed(
            { create.dismiss() }, 1000
        )
    }

    override fun onStart() {
        super.onStart()
        ifComeShowActivity()
    }

    private fun ifComeShowActivity() {
        if (intent.extras?.getString("idNote", "")!!.isNotEmpty()) {
            val list = database.select(intent.extras?.getString("idNote", "0")!!,false)[0]
            color=list.color.toInt()
            editTitle.setText(list.title)
            editNote.setText(list.note)
        }
    }
    private fun codeBtnSaveWrite() {
        val title = editTitle.text.toString().trim()
        val note = editNote.text.toString().trim()
        if (title.isNotEmpty() && note.isNotEmpty()) {
            if(intent.extras?.getString("idNote", "")!!.isEmpty()){
            val idCategory = intent.extras!!.getString("IdCategory", "1")
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month =c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val hour = String.format("%02d",c.get(Calendar.HOUR_OF_DAY))
            val minute = String.format("%02d",c.get(Calendar.MINUTE))
            val second = String.format("%02d",c.get(Calendar.SECOND))

            val timeNew = "$hour:$minute:$second $day/${month + 1}/$year"
            database.insert(idCategory, title, note, timeNew,color.toString())
            database.update(idCategory, 1,true)
            editNote.setText("")
            editTitle.setText("")
        }else{
                database.update(intent.extras?.getString("idNote","0")!!,note,title,color.toString())
        }
            Handler().postDelayed(
                {finish()},200
            );
        }
    }
}