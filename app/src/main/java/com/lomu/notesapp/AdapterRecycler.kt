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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialog_chose.view.*
import kotlinx.android.synthetic.main.dialog_password.view.btnCancelPassword
import kotlinx.android.synthetic.main.dialog_password.view.btnSavePassword
import kotlinx.android.synthetic.main.dialog_password.view.inputPass
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
         var btnEdit:ImageButton?=null
         var viewRecycler:ConstraintLayout?=null
        init {
            nameCategory=view.nameCategory
            numberNote=view.numberNoteCategory
            btnDelete=view.btnDeleteItemInRecycler
            viewRecycler=view.viewRecycler
            btnEdit=view.editNameCategory
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_in_recycler,parent,false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item= list[position]
        holder.nameCategory?.text=item.nameCategory
        holder.numberNote?.text=item.numberNoteInCategory
        holder.viewRecycler?.setOnLongClickListener {
             codeLongClickOnRecycler(holder)
            true
        }
        holder.btnEdit?.setOnClickListener {
            codeBtnUpdateToRecycler(item, position)
        }
        holder.viewRecycler?.setOnClickListener{
            if(holder.btnEdit!!.isVisible){
                reset(holder)
            }else {
                intentToCategoryActivity(item)
            }
            }
        holder.btnDelete?.setOnClickListener {
            dialogDelete(item, position)
        }
    }

    private fun codeBtnUpdateToRecycler(
        item: Information,
        position: Int
    ) {
        codeCreateDialogUpdateNameFolder(item)
        view!!.btnSavePassword.setOnClickListener {
            codeBtnSaveUpdate(item, position)
        }
        view!!.btnCancelPassword.setOnClickListener {
            createDialog?.dismiss()
        }
        createDialog?.show()
    }

    private fun codeCreateDialogUpdateNameFolder(item: Information) {
        dialog = AlertDialog.Builder(context)
        view = LayoutInflater.from(context).inflate(R.layout.dialog_add_category, null)
        view!!.inputPass.setText(item.nameCategory)
        dialog?.setView(view)
        createDialog = dialog?.create()
    }

    private fun codeBtnSaveUpdate(
        item: Information,
        position: Int
    ) {
        val nameNewFolder = view!!.inputPass.text.toString().trim()
        if (nameNewFolder.isNotEmpty()) {
            if (database.select(nameNewFolder).size > 0 && nameNewFolder != item.nameCategory) {
                view!!.inputPass.error =
                    context.resources.getString(R.string.The_name_has_been_used_before)
            } else {
                database.update(item.id.toInt(), nameNewFolder)
                list[position] = Information(item.id, nameNewFolder, item.numberNoteInCategory)
                notifyItemRangeChanged(position, list.size)
                notifyDataSetChanged()
                createDialog?.dismiss()
            }
        }
    }

    private fun codeLongClickOnRecycler(holder: Holder) {
        holder.btnDelete?.visibility = View.VISIBLE
        holder.btnEdit?.visibility = View.VISIBLE
        holder.viewRecycler?.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.clickLongOnItemRecycler)
    }

    private fun intentToCategoryActivity(item: Information) {
        val intent = Intent(context, CategoryActivity::class.java)
        intent.putExtra("Id", item.id)
        context.startActivity(intent)
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
        notifyDataSetChanged()
    }

    private fun reset(holder: Holder) {
        holder.btnDelete?.visibility = View.GONE
        holder.btnEdit?.visibility = View.GONE
        holder.viewRecycler?.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.colorTypeNoteRecycler)
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