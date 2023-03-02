package com.lomu.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import java.util.*

data class InformationNote(val id:String,val idCategory:String,val title:String,val note:String,val time:String,val color:String)

class SqlLite(context: Context?)
    : SQLiteOpenHelper(context, "data.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table category(id INTEGER PRIMARY KEY AUTOINCREMENT,name_category TEXT UNIQUE,number_note INTEGER)")
        db?.execSQL("create table note(id INTEGER PRIMARY KEY AUTOINCREMENT,id_category INTEGER,title TEXT,note TEXT,time TEXT,color INTEGER,FOREIGN KEY(id_category) REFERENCES category(id))")
    }

    override fun onUpgrade(dp: SQLiteDatabase?, p1: Int, p2: Int) {
        dp?.execSQL("DROP TABLE IF EXISTS category")
        dp?.execSQL("DROP TABLE IF EXISTS note")
        onCreate(dp)
    }

      fun insert(name:String)=run{
          val contentValue = ContentValues()
          val database: SQLiteDatabase = this.writableDatabase
          contentValue.put("name_category",name)
          contentValue.put("number_note",0)
          val numRequest=database.insert("category",null,contentValue)
          numRequest!=-1L
      }
      fun insert(id:String,title:String,note:String,time: String,color:String)=run{
          val contentValue = ContentValues()
          val database: SQLiteDatabase = this.writableDatabase
          contentValue.put("id_category",id.toInt())
          contentValue.put("title",title)
          contentValue.put("note",note)
          contentValue.put("time",time)
          contentValue.put("color",color)
          val numRequest=database.insert("note",null,contentValue)
          numRequest!=-1L
      }

       fun select(id:Int)= run {
           val databaseRead: SQLiteDatabase =this.readableDatabase
            val list=LinkedList<Information>()
            val allItem=
                if(id==0) {
                    databaseRead.rawQuery("select * from category order by id desc", null)
                }else{
                    databaseRead.rawQuery("select * from category where id=${id}", null)
                }
            allItem.moveToFirst()
            while (!allItem.isAfterLast){
            val id=allItem.getString(0)
            val name=allItem.getString(1)
            val number=allItem.getString(2)
            list.add(Information(id,name,number))
            allItem.moveToNext()
        }
            list
     }

     fun select(id:String,check:Boolean)=run{
         val databaseRead: SQLiteDatabase =this.readableDatabase
         val list=LinkedList<InformationNote>()
         val res =  if(check) {
             databaseRead.rawQuery("select * from note where id_category=${id.toInt()} order by id desc", null)
         }else{
             databaseRead.rawQuery("select * from note where id=${id.toInt()}", null)
         }
             res.moveToFirst()
         while (!res.isAfterLast){
         list.add(InformationNote(res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)))
         res.moveToNext()
         }
         list
     }
    fun select(name:String)=run{
        val database:SQLiteDatabase=this.readableDatabase
        val list=LinkedList<Information>()
        val listItem=database.rawQuery("select * from category where name_category='${name}'",null)
        listItem.moveToFirst()
        while (!listItem.isAfterLast){
            list.add(Information(listItem.getString(0),listItem.getString(1),listItem.getString(2)))
            listItem.moveToNext()
        }
        list
    }
    fun update(id:String,increment:Int,check:Boolean){
        val contentValue = ContentValues()
        val database: SQLiteDatabase = this.writableDatabase
      if(check) {
          contentValue.put(
              "number_note",
              select(id.toInt())[0].numberNoteInCategory.toInt() + increment
          )
      }else{
          contentValue.put("number_note",0)
      }
        database.update("category", contentValue, "id=${id.toInt()}", null)
        database.close()
    }

    fun update(id:String,note:String,title:String,color: String){
        val contentValue = ContentValues()
        val database: SQLiteDatabase = this.writableDatabase
        contentValue.put("note",note)
        contentValue.put("title",title)
        contentValue.put("color",color)
        database.update("note",contentValue,"id=${id.toInt()}",null)
        database.close()
    }
   fun update(id:Int,name:String) {
   val database=this.writableDatabase
   val value=ContentValues()
   value.put("name_category",name)
   database.update("category",value,"id=$id",null)
   database.close()
   }
    fun delete(id: String?,num:Int) {
        val database: SQLiteDatabase = this.writableDatabase
        when(num) {
           1-> database.delete("note", "id=${id!!.toInt()}", null)
           2->database.delete("note","id_category=${id!!.toInt()}",null)
           3->database.delete("category","id=${id!!.toInt()}",null)
        }
        database.close()
    }

}