package com.lomu.notesapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_chose.view.*
import kotlinx.android.synthetic.main.item_in_recycler.view.*
import java.util.*

data class Information(val id:String,val nameCategory:String,val numberNoteInCategory:String)

class AdapterRecycler(private val list: LinkedList<Information>,private val context: Context): RecyclerView.Adapter<AdapterRecycler.Holder>() {
    private var dialog:AlertDialog.Builder?=null
    private var createDialog:AlertDialog?=null
    private var view:View?=null
    private var database=SqlLite(context)
    class Holder(view: View):RecyclerView.ViewHolder(view){
         var nameCategory:TextView?=null
         var numberNote:TextView?=null
         var btnDelete:ImageButton?=null
         var viewRecycler:ConstraintLayout?=null
        init {
            nameCategory=view.nameCategory
            numberNote=view.numberNoteCategory
            btnDelete=view.btnDeleteItemInRecycler
            viewRecycler=view.viewRecycler
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_in_recycler,parent,false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item= list[position]
        holder.nameCategory?.text=item.nameCategory
        holder.numberNote?.text=item.numberNoteInCategory
        holder.viewRecycler?.setOnClickListener{
            val intent=Intent(context,CategoryActivity::class.java)
            intent.putExtra("Id",item.id)
            context.startActivity(intent)
        }
        holder.btnDelete?.setOnClickListener {
            dialogDelete(item, position)
        }
    }

    private fun dialogDelete(item: Information, position: Int) {
        createDialog()
        setTextToChangeNameBtn()
        view!!.btnAddPassword.setOnClickListener {
            codeDeleteFolderBeforeExecute(item, position)
        }
        view!!.btnDeletePassword.setOnClickListener {
            codeBtnDeleteNotesBeforeExecute(item, position)
        }
        view!!.btnCancelBtnPass.setOnClickListener {
            createDialog!!.dismiss()
        }
        createDialog!!.show()
    }

    private fun codeBtnDeleteNotesBeforeExecute(
        item: Information,
        position: Int
    ) {
        createDialog!!.dismiss()
        dialog = AlertDialog.Builder(context)
        dialog!!.setMessage("${context.resources.getString(R.string.Are_you_sure_you_want_to_delete_all_notes_in)} ${item.nameCategory}${context.resources.getString(R.string.qu)}")
        dialog!!.setPositiveButton(context.resources.getString(R.string.Yes)) { _, _ ->
            codeBtnDeleteNotes(item, position)
        }
        dialog!!.setNegativeButton(context.resources.getString(R.string.No)) { _, _ -> }
        createDialog=dialog!!.create()
        createDialog!!.show()
    }

    private fun codeBtnDeleteNotes(item: Information, position: Int) {
        database.delete(item.id, 2)
        database.update(item.id,0,false)
        list[position] = Information(item.id, item.nameCategory, "0")
        notifyItemRangeChanged(position, list.size)
    }

    private fun codeDeleteFolderBeforeExecute(
        item: Information,
        position: Int
    ) {
        createDialog!!.dismiss()
        dialog = AlertDialog.Builder(context)
        dialog!!.setMessage("${context.resources.getString(R.string.Are_you_sure_you_want_to_delete)} ${item.nameCategory}${context.resources.getString(R.string.qu)}")
        dialog!!.setPositiveButton(context.resources.getString(R.string.Yes)) { _, _ ->
            codeDeleteFolder(item, position)
        }
        dialog!!.setNegativeButton(context.resources.getString(R.string.No)) { _, _ -> }
        createDialog=dialog!!.create()
        createDialog!!.show()
    }

    private fun codeDeleteFolder(item: Information, position: Int) {
        database.delete(item.id, 2)
        database.delete(item.id, 3)
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }
    private fun setTextToChangeNameBtn() {
        view!!.btnAddPassword.text = context.resources.getString(R.string.Delete_Folder)
        view!!.btnDeletePassword.text = context.resources.getString(R.string.Delete_All_Notes)
    }

    private fun createDialog() {
        dialog = AlertDialog.Builder(context)
        view = LayoutInflater.from(context).inflate(R.layout.dialog_chose, null)
        dialog!!.setView(view)
        createDialog = dialog!!.create()
    }

    override fun getItemCount()=list.size
}