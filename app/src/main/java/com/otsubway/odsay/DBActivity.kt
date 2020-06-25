package com.otsubway.odsay

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_db.*
import java.util.*

class DBActivity : AppCompatActivity() {

    lateinit var myDBHelper : DBhelper
    var array = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db)
        init()
        getAllRecord()
    }

    private fun init() {
        val scan = Scanner(resources.openRawResource(R.raw.station_name))
        while(scan.hasNextLine()) {
            val name = scan.nextLine()
            array.add(name)
        }
        scan.close()

        var adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, array)
        stationEdit.setAdapter(adapter)

        myDBHelper = DBhelper(this)

        insertBtn.setOnClickListener {
            val userLocation = userlocationEdit.text.toString()
            val stationName = stationEdit.text.toString()
            val location = Location(userLocation, stationName)
            val result = myDBHelper.insertProduct(location)
            if(result) {
                Toast.makeText(this, "추가에 성공했습니다.", Toast.LENGTH_SHORT).show()
                getAllRecord()
            } else {
                Toast.makeText(this, "추가에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBtn.setOnClickListener {
            val userLocation = userlocationEdit.text.toString()
            val result = myDBHelper.deleteProduct(userLocation)
            if(result) {
                Toast.makeText(this, "삭제에 성공했습니다.", Toast.LENGTH_SHORT).show()
                getAllRecord()
            } else {
                Toast.makeText(this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        updateBtn.setOnClickListener {
            val userLocation = userlocationEdit.text.toString()
            val stationName = stationEdit.text.toString()
            val location = Location(userLocation, stationName)
            val result = myDBHelper.updateProduct(location)
            if(result) {
                Toast.makeText(this, "업데이트에 성공했습니다.", Toast.LENGTH_SHORT).show()
                getAllRecord()
            } else {
                Toast.makeText(this, "업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAllRecord() {
        myDBHelper.getAllRecord()
    }
}
