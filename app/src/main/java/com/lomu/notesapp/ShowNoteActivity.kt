package com.lomu.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_show_note.*

class ShowNoteActivity : AppCompatActivity() {
    private val sqlLite=SqlLite(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_note)
        btnBackShow.setOnClickListener {
            finish()
        }
        btnEdit.setOnClickListener {
            codeBtnEdit()
        }
        btnDelete.setOnClickListener {
            codeBtnDelete()
        }
    }
    private fun codeBtnDelete() {
        val dialog = AlertDialog.Builder(this)
            dialog.setMessage("${resources.getString(R.string.Are_you_sure_you_want_to_delete_the_note)}${resources.getString(R.string.qu)}")
            dialog.setPositiveButton(resources.getString(R.string.Yes)) { _, _ ->
            sqlLite.delete(intent.extras?.getString("IdNote", "0"),1)
            sqlLite.update(intent.extras?.getString("IdCategory", "0")!!, -1,true)
            finish()
            }
        dialog.setNegativeButton(resources.getString(R.string.No)) { _, _ -> }
        val create = dialog.create()
        create.show()
    }
    private fun codeBtnEdit() {
        val intentEdit = Intent(this, WriteNoteActivity::class.java)
        intentEdit.putExtra("idNote", intent.extras?.getString("IdNote", "0"))
        startActivity(intentEdit)
    }
    override fun onStart() {
        super.onStart()
        setText()
    }
    private fun setText() {
        val list = sqlLite.select(intent.extras?.getString("IdNote", "0")!!, false)[0]
        textTimeShow.text = "${list.time}"
        textTitleShow.text = list.title
        textNoteShow.text = list.note
    }
    }