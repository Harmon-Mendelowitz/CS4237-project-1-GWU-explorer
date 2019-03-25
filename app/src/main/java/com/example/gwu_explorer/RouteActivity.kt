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

    //private val locationManager: AlertManager = AlertManager()
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


        /*if (savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            val previousLocations: List<AlertItem> = savedInstanceState.getSerializable("LOCATIONS") as List<AlertItem>
            locationList.addAll(previousLocations)

            //recyclerView.adapter = AlertsAdapter(locationList)
        }
        else {
            stationManager.retrieveLocation(
                key = getString(R.string.wmata_key),
                address = add,

                //LinesAffected = l,
                //Description = d,
                successCallback = { locations ->
                    runOnUiThread {
                        locationList.clear()
                        locationList.addAll(locations)
                        //recyclerView.adapter = AlertsAdapter(locations)
                        endCode = locationList[0].content
                    }
                },
                errorCallback = {
                    runOnUiThread {
                        // Runs if we have an error
                        Toast.makeText(this@RouteActivity, "Error retrieving destinations", Toast.LENGTH_LONG).show()
                    }
                }
            )
            if(locationList.size>0) {
                endCode = locationList[0].content
            }
            else
            {
                endCode = "C02"
                //  val intent: Intent = Intent(this, LoginActivity::class.java)
                //  Toast.makeText(this@RouteActivity, "error finding nearby station", Toast.LENGTH_LONG).show()
            }
        //}
*/


        //Toast.makeText(this@RouteActivity, locationList[0].content, Toast.LENGTH_LONG).show()
        //Toast.makeText(this@RouteActivity, endCode, Toast.LENGTH_LONG).show()


        //val intent: Intent = intent
        //val location: String = intent.getStringExtra("location")


        if (savedInstanceState != null) {
            @Suppress("UNCHECKED_CAST")
            val previousStations: List<AlertItem> = savedInstanceState.getSerializable("STATIONS") as List<AlertItem>
            stationList.addAll(previousStations)

            recyclerView.adapter = AlertsAdapter(stationList)
        }
        else {
            //first time activity launch

            // Get the intent which was used to launch this activity and retrieve data from it

            //val intent: Intent = intent
            //val l: String = intent.getStringExtra("LinesAffected")
            //val d: String = intent.getStringExtra("Description")

            //title = getString(R.string.alerts_title, location.getAddressLine(0))


            stationManager.retrieveLocation(
                key = getString(R.string.wmata_key),
                address = add,

                //LinesAffected = l,
                //Description = d,
                successCallback = { locations ->
                    runOnUiThread {
                        locationList.clear()
                        locationList.addAll(locations)
                        //recyclerView.adapter = AlertsAdapter(locations)
                        endCode = locationList[0].content
                    }
                    endCode = locationList[0].content
                },
                errorCallback = {
                    runOnUiThread {
                        // Runs if we have an error
                        Toast.makeText(this@RouteActivity, "Error retrieving destinations", Toast.LENGTH_LONG).show()
                    }
                }
            )

            stationManager.retrieveStations(
                key = getString(R.string.wmata_key),
                to = endCode,
                from = startCode,
                //LinesAffected = l,
                //Description = d,
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

        //outState.putInt("SOME_INT", 5)
        //outState.putBoolean("SOME_BOOL", true)
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