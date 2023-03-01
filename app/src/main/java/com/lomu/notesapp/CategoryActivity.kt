package com.lomu.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_category.*
import java.util.*

class CategoryActivity : AppCompatActivity() {
     private var list=LinkedList<InformationNote>()
     private val sql=SqlLite(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setTextNameCategory()
        btnAddNewNote.setOnClickListener{
            codeBtnAddNote()
        }
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun codeBtnAddNote() {
        val idCategory = intent.extras!!.getString("Id", "")
        val intent = Intent(this, WriteNoteActivity::class.java)
        intent.putExtra("IdCategory", idCategory)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        adapterGrid()
    }
    private fun setTextNameCategory() {
        nameCategoryTitle.text = sql.select(intent.extras?.getString("Id","0")!!.toInt())[0].nameCategory
    }
    private fun adapterGrid() {
        list = sql.select(intent.extras!!.getString("Id", ""),true)
        Grid.adapter = GridAdapter(this,list)
    }
}