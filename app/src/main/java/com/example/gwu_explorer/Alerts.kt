package com.example.gwu_explorer

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
//import kotlinx.android.synthetic.main.activity_alerts.*

class Alerts : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val alerts = generateFakeAlerts()

        recyclerView.adapter = AlertsAdapter(alerts)

        //val intent: Intent = intent
        //val location: String = intent.getStringExtra("location")

    }
    private fun generateFakeAlerts(): List<AlertItem>{
        return listOf(
            AlertItem("RED LINE","fake error 1")
            ,AlertItem("BLUE LINE","fake error 2")
            ,AlertItem("ORANGE LINE","fake error 3")
            ,AlertItem("","fake error 4")
            ,AlertItem("","fake error 5")
            ,AlertItem("","fake error 6")
        )
    }
}