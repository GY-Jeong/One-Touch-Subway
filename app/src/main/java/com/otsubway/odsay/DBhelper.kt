package com.otsubway.odsay

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_db.*

class DBhelper(val context : Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_VERSION = 1
        val DB_NAME = "stationByLocation.db"
        val TABLE_NAME = "locations"
        //속성정보
        val USERLOCATION = "userLocation"
        val STATION = "station"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists " + TABLE_NAME + "(" +
                USERLOCATION + " text primary key," +
                STATION + " text)"

        db?.execSQL(create_table)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME"
        db?.execSQL(drop_table)
        onCreate(db)
    }

    fun insertProduct(location: Location) : Boolean {
        val values = ContentValues()
        values.put(USERLOCATION, location.userLocation)
        values.put(STATION, location.station)
        val db = this.writableDatabase
        if(db.insert(TABLE_NAME, null, values) > 0) {
            val activity = context as DBActivity
            activity.userlocationEdit.setText("")
            activity.stationEdit.setText("")
            db.close()
            return true
        }
        db.close()
        return false
    }

    fun deleteProduct(userLocation : String) : Boolean {
        val strsql = "select * from " + TABLE_NAME + " where " +
                USERLOCATION + " = \'" + userLocation + "\'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(strsql, null)
        if(cursor.moveToFirst()) {
            db.delete(TABLE_NAME, "$USERLOCATION=?", arrayOf(userLocation))
            val activity = context as DBActivity
            activity.userlocationEdit.setText("")
            activity.stationEdit.setText("")
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

    fun updateProduct (location: Location) : Boolean {
        val strsql = "select * from " + TABLE_NAME + " where " +
                USERLOCATION + " = \'" + location.userLocation + "\'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(strsql, null)
        if(cursor.moveToFirst()) {
            val values = ContentValues()
            values.put(USERLOCATION, location.userLocation)
            values.put(STATION, location.station)
            db.update(TABLE_NAME, values, "$USERLOCATION=?", arrayOf(location.userLocation))
            val activity = context as DBActivity
            activity.userlocationEdit.setText("")
            activity.stationEdit.setText("")
            cursor.close()
            db.close()
            return true
        }
        cursor.close()
        db.close()
        return false
    }

//    fun findStationByUserLocation(userLocation : String) : String {
//        val strsql = "select * from " + TABLE_NAME + " where " +
//                USERLOCATION + " = \'" + userLocation + "\'"
//        val db = this.readableDatabase
//        val cursor = db.rawQuery(strsql, null)
//        if(cursor.moveToFirst()) {
//            db.
//            db.delete(TABLE_NAME, "$USERLOCATION=?", arrayOf(userLocation))
//            val activity = context as DBActivity
//            activity.userlocationEdit.setText("")
//            activity.stationEdit.setText("")
//            cursor.close()
//            db.close()
//            return true
//        }
//        cursor.close()
//        db.close()
//        return false
//    }

    fun getAllRecord() {
        val activity = context as DBActivity
        val strsql = "select * from $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(strsql, null)
        if(cursor.count != 0) {
            activity.tableLayout.visibility = View.VISIBLE
            //table layout에 행들 동적으로 생성해서 넣어주는 작업
            showRecord(cursor)
        } else {
            activity.tableLayout.visibility = View.INVISIBLE
        }
        cursor.close()
        db.close()
    }

    fun showRecord(cursor : Cursor) {
        cursor.moveToFirst()
        val count = cursor.columnCount
        val activity = context as DBActivity
        activity.tableLayout.removeAllViewsInLayout()
        //라벨 만들기, column title 만들기
        val tablerow = TableRow(activity)
        val rowParam = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT, count.toFloat())
        //행의 폭정보는 가중치로 결정
        tablerow.layoutParams = rowParam
        val viewParm = TableRow.LayoutParams(0, 120, 1f)
        for(i in 0 until count) {
            val textView = TextView(activity)
            textView.layoutParams = viewParm
            if(cursor.getColumnName(i) == "userLocation") {
                textView.text = "위치"
            } else {
                textView.text = "역"
            }
            textView.setBackgroundColor(Color.LTGRAY)
            textView.textSize = 18.0f
            textView.gravity = Gravity.CENTER
            tablerow.addView(textView)
        }
        activity.tableLayout.addView(tablerow)
        //실제 데이터 레코드 읽어오기
        do {
            val row = TableRow(activity)
            row.layoutParams = rowParam
            row.setOnClickListener {
                for(i in 0 until count) {
                    val txtView = row.getChildAt(i) as TextView
                    when(txtView.tag) {
                        0 -> activity.userlocationEdit.setText(txtView.text)
                        1 -> activity.stationEdit.setText(txtView.text)
                    }
                }
            }
            for(i in 0 until count) {
                val textView = TextView(activity)
                textView.layoutParams = viewParm
                textView.text = cursor.getString(i)
                textView.textSize = 18.0f
                textView.gravity = Gravity.CENTER
                textView.tag = i
                row.addView(textView)
            }
            activity.tableLayout.addView(row)
        } while(cursor.moveToNext())
    }

}