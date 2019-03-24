package com.example.gwu_explorer

import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast

//import kotlinx.android.synthetic.main.activity_alerts.*

class Alerts : AppCompatActivity(){

    private val alertManager: AlertManager = AlertManager()
    private val alertList: MutableList<AlertItem> = mutableListOf()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        //val alerts = generateFakeAlerts()
        //recyclerView.adapter = AlertsAdapter(alerts)

        //val intent: Intent = intent
        //val location: String = intent.getStringExtra("location")


        if (savedInstanceState != null) {
            val previousAlerts: List<AlertItem> = savedInstanceState.getSerializable("ALERTS") as List<AlertItem>
            alertList.addAll(previousAlerts)

            recyclerView.adapter = AlertsAdapter(alertList)
        }
        else {
            //first time activity launch

            // Get the intent which was used to launch this activity and retrieve data from it

            //val intent: Intent = intent
            //val l: String = intent.getStringExtra("LinesAffected")
            //val d: String = intent.getStringExtra("Description")

            //title = getString(R.string.alerts_title, location.getAddressLine(0))

                    alertManager.retrieveAlerts(
                        key = getString(R.string.wmata_key),
                        //LinesAffected = l,
                        //Description = d,
                        successCallback = { alerts ->
                            runOnUiThread {
                                alertList.clear()
                                alertList.addAll(alerts)
                                // Create the adapter and assign it to the RecyclerView
                                recyclerView.adapter = AlertsAdapter(alerts)
                            }
                        },
                        errorCallback = {
                            runOnUiThread {
                                // Runs if we have an error
                                Toast.makeText(this@Alerts, "Error retrieving Alerts", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
        }

    }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)

            //outState.putInt("SOME_INT", 5)
            //outState.putBoolean("SOME_BOOL", true)
            outState.putSerializable("ALERTS", ArrayList(alertList))
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