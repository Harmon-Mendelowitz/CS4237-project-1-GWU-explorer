package com.example.gwu_explorer

import android.content.Intent
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.Toast

class RouteActivity : AppCompatActivity(){

    private val locationList: MutableList<AlertItem> = mutableListOf()

    private val stationManager: AlertManager = AlertManager()
    private val stationList: MutableList<AlertItem> = mutableListOf()

    private lateinit var recyclerView: RecyclerView
    private val startCode = "C04"
    private lateinit var endCode: String

    private lateinit var share: Button
    private lateinit var drive: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val add: Address = intent.getParcelableExtra("location")

        share = findViewById(R.id.sharebutton)
        drive = findViewById(R.id.drivebutton)


        if (savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            val previousStations: List<AlertItem> = savedInstanceState.getSerializable("STATIONS") as List<AlertItem>
            stationList.addAll(previousStations)

            recyclerView.adapter = AlertsAdapter(stationList)
        }
        else {
            //first time activity launch

            //title = getString(R.string.alerts_title, location.getAddressLine(0))

            stationManager.getS(
                key = getString(R.string.wmata_key),
                address = add,

                successCallback = { locations ->
                    runOnUiThread {
                        locationList.clear()
                        endCode = locations
                    }
                    Thread.sleep(1500)
                    if(endCode.equals("C04"))
                    {
                        val intent: Intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    stationManager.retrieveStations(
                        key = getString(R.string.wmata_key),
                        to = endCode,
                        from = startCode,
                        successCallback = { stations ->
                            runOnUiThread {
                                stationList.clear()
                                stationList.addAll(stations)
                                // Create the adapter and assign it to the RecyclerView
                                recyclerView.adapter = AlertsAdapter(stations)
                            }
                        },
                        errorCallback = {
                            runOnUiThread {
                                // Runs if we have an error
                                Toast.makeText(this@RouteActivity, "Error retrieving Stations", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                },
                errorCallback = {
                    runOnUiThread {
                        // Runs if we have an error
                        Toast.makeText(this@RouteActivity, "Error retrieving destinations", Toast.LENGTH_LONG).show()
                    }
                }
            )

        }


        val dest = add.getAddressLine(0)

        share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "I am on my way")
                type = "text/plain"
            }
            startActivity(sendIntent)
        }
        drive.setOnClickListener {
            val navigationUri = Uri.parse("google.navigation:q=$dest")
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("LOCATIONS", ArrayList(locationList))
        outState.putSerializable("STATIONS", ArrayList(stationList))
    }
    override fun onStart() {
        super.onStart()
        Log.d("LoginActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LoginActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LoginActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LoginActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity", "onDestroy called")
    }
}