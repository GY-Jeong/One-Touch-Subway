package com.otsubway.odsay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_db.*

class DBActivity : AppCompatActivity() {

    lateinit var myDBHelper : DBhelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db)
        init()
        getAllRecord()
    }

    private fun init() {
        myDBHelper = DBhelper(this)

        insertBtn.setOnClickListener {
            val userLocation = userlocationEdit.text.toString()
            val stationName = stationEdit.text.toString()
            val location = Location(userLocation, stationName)
            val result = myDBHelper.insertProduct(location)
            if(result) {
                Toast.makeText(this, "DB INSERT SUCCESS", Toast.LENGTH_SHORT).show()
                getAllRecord()
            } else {
                Toast.makeText(this, "DB INSERT FAILED", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBtn.setOnClickListener {
            val userLocation = userlocationEdit.text.toString()
            val result = myDBHelper.deleteProduct(userLocation)
            if(result) {
                Toast.makeText(this, "DELETE SUCCESS", Toast.LENGTH_SHORT).show()
                getAllRecord()
            } else {
                Toast.makeText(this, "DELETE FAILED", Toast.LENGTH_SHORT).show()
            }
        }

        updateBtn.setOnClickListener {
            val userLocation = userlocationEdit.text.toString()
            val stationName = stationEdit.text.toString()
            val location = Location(userLocation, stationName)
            val result = myDBHelper.updateProduct(location)
            if(result) {
                Toast.makeText(this, "UPDATE SUCCESS", Toast.LENGTH_SHORT).show()
                getAllRecord()
            } else {
                Toast.makeText(this, "UPDATE FAILED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAllRecord() {
        myDBHelper.getAllRecord()
    }
}
