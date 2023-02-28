package com.lomu.notesapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_category.view.btnCancelPassword
import kotlinx.android.synthetic.main.dialog_add_category.view.btnSavePassword
import kotlinx.android.synthetic.main.dialog_add_category.view.inputPass
import kotlinx.android.synthetic.main.dialog_chose.*
import kotlinx.android.synthetic.main.dialog_chose.view.*
import kotlinx.android.synthetic.main.dialog_password.*
import kotlinx.android.synthetic.main.dialog_password.view.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var list=LinkedList<Information>()
    private val database=SqlLite(this)
    private var sharedPreferences:SharedPreferences?=null
    private var edit:SharedPreferences.Editor?=null
    private var dialog:AlertDialog.Builder?=null
    private var layoutBtn:View?=null
    private var create :AlertDialog?=null
    private var pass:String=""
    private var passConfirm:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportToolBarAsActionBar()
        btnAddNewNote.setOnClickListener {
            codeBtnAddCategory()
        }
    }

    private fun supportToolBarAsActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
    }

    override fun onStart() {
        super.onStart()
        showCategoryWhenRun()
    }
    private fun codeBtnAddCategory() {
        val dialog = AlertDialog.Builder(this)
        val layout = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null)
        dialog.setView(layout)
        val create = dialog.create()
        create.show()
        layout.btnCancelPassword.setOnClickListener {
            create.dismiss()
        }
        layout.btnSavePassword.setOnClickListener {
            val nameFolder=layout.inputPass.text.toString().trim()
            if(nameFolder.isNotEmpty()) {
               val insert=database.insert(nameFolder)
                if(insert) {
                    adapter()
                    create.dismiss()
                }else{
                    layout.inputPass.error="The name has been used before"
                }
            }
        }
    }
    private fun showCategoryWhenRun() {
        adapter()
    }
    private fun adapter() {
        list = database.select(0)
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler.adapter = AdapterRecycler(list,this)
    }

    override fun onOptionsItemSelected(item: MenuItem)=when(item.itemId){
        R.id.privacyItem->{
            codePrivacy()
            true
        }
        else-> super.onOptionsItemSelected(item)
    }

    private fun codePrivacy() {
        dialog = AlertDialog.Builder(this)
        layoutBtn = layoutInflater.inflate(R.layout.dialog_chose, null)
        dialog!!.setView(layoutBtn)
        create = dialog!!.create()
        layoutBtn!!.btnAddPassword.setOnClickListener {
            codeDialogAdd()
        }
        layoutBtn!!.btnDeletePassword.setOnClickListener {
            codeDeletePassword()
        }
        layoutBtn!!.btnCancelBtnPass.setOnClickListener {
            create!!.dismiss()
        }
        create?.show()
    }

    private fun codeDeletePassword() {
        removeDialogAndCreateOrDialog()
        layoutBtn!!.btnSavePassword.text = "Delete"
        layoutBtn!!.btnSavePassword.setOnClickListener {
            readyToAddPassOrDelete()
            if (passConfirm == pass && pass.length in 4..8) {
                if (pass == sharedPreferences!!.getString("passwordApp", "")) {
                    edit!!.putString("passwordApp", "")
                    edit!!.commit()
                    create!!.dismiss()
                } else {
                    layoutBtn!!.inputPass.error = "Error Input"
                    layoutBtn!!.inputConfirmPass.error = "Error Input"
                }
            } else {
                layoutBtn!!.inputPass.error = "Error Input"
                layoutBtn!!.inputConfirmPass.error = "Error Input"
            }
        }
        layoutBtn!!.btnCancelPassword.setOnClickListener {
            create!!.dismiss()
        }
       create!!.show()
    }

    private fun codeDialogAdd() {
        removeDialogAndCreateOrDialog()
        layoutBtn!!.btnSavePassword.setOnClickListener {
            codeAddPassword()
        }
        layoutBtn!!.btnCancelPassword.setOnClickListener {
            create!!.dismiss()
        }

        create?.show()
    }

    private fun removeDialogAndCreateOrDialog() {
        create?.dismiss()
        layoutBtn = layoutInflater.inflate(R.layout.dialog_password, null)
        dialog!!.setView(layoutBtn)
        create = dialog!!.create()
    }

    private fun codeAddPassword() {
        readyToAddPassOrDelete()
        if (pass.length in 4..8 && pass == passConfirm) {
            edit!!.putString("passwordApp", pass)
            edit!!.commit()
            create!!.dismiss()
        } else {
            layoutBtn!!.inputPass.error = "Error Input"
            layoutBtn!!.inputConfirmPass.error = "Error Input"
        }
    }

    private fun readyToAddPassOrDelete() {
        sharedPreferences = getSharedPreferences("passwordApplication", MODE_PRIVATE)
        edit = sharedPreferences!!.edit()
        pass = layoutBtn!!.inputPass.text.toString().trim()
        passConfirm = layoutBtn!!.inputConfirmPass.text.toString().trim()
    }


    override fun onCreateOptionsMenu(menu: Menu?)= run {
        menuInflater.inflate(R.menu.menu_pass,menu)
        true
    }

}