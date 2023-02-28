package com.lomu.notesapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_in_grid.view.*
import java.util.*


class GridAdapter(private val context: Context,private val list:LinkedList<InformationNote>):BaseAdapter(){
    override fun getCount()=list.size

    override fun getItem(p0: Int)=list[p0]

    override fun getItemId(p0: Int)=p0.toLong()

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View =run{
        val view=LayoutInflater.from(parent?.context).inflate(R.layout.item_in_grid,parent,false)
        val item=getItem(position)
        view.textTitle.text =item.title
        view.textTime.text=item.time
        setBackgroundView(item, view)
        view.setOnClickListener {
            val intent=Intent(context,ShowNoteActivity::class.java)
            intent.putExtra("IdNote",getItem(position).id)
            intent.putExtra("IdCategory",getItem(position).idCategory)
            context.startActivity(intent)
        }

        view
    }

    private fun setBackgroundView(item: InformationNote, view: View) {
        view.backgroundTintList= when (item.color.toInt()) {
            1 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectOne)
            }
            2 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectTwo)
            }
            3 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectThree)
            }
            4 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectFour)
            }
            5 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectOneFive)
            }
            6 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectSix)
            }
            7 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectSeven)
            }
            8 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectEight)
            }
            9 -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectNine)
            }
            else -> {
                ContextCompat.getColorStateList(context,R.color.colorSelectTen)
            }
        }
    }

}