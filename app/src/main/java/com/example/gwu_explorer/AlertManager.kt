package com.example.gwu_explorer

import android.location.Address
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class AlertManager {

    // OkHttp is a library used to make network calls
    private val okHttpClient: OkHttpClient

    // This runs extra code when TwitterManager is created (e.g. the constructor)
    init {
        val builder = OkHttpClient.Builder()

        // This sets network timeouts (in case the phone can't connect
        // to the server or the server is down)
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)

        // This causes all network traffic to be logged to the console
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        okHttpClient = builder.build()
    }


    fun retrieveAlerts(
        key: String,
        successCallback: (List<AlertItem>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {

        val request = Request.Builder()
            .url("https://api.wmata.com/Incidents.svc/json/Incidents")
            .header("api_key", key)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Similar error handling to last time
                errorCallback(Exception("Call failed"))
            }

            override fun onResponse(call: Call, response: Response) {
                // Similar success / error handling to last time
                val alerts = mutableListOf<AlertItem>()
                val responseString = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    val statuses = JSONObject(responseString).getJSONArray("Incidents")
                    for (i in 0 until statuses.length()) {
                        val curr = statuses.getJSONObject(i)
                        val Iline = curr.getString("LinesAffected")
                        val Idesc = curr.getString("Description")

                        alerts.add(
                            AlertItem(
                                line = Iline,
                                content = Idesc
                            )
                        )
                    }
                    if(statuses.length()==0)
                    {
                        alerts.add(
                            AlertItem(
                                line = "No Alerts",
                                content = ""
                            )
                        )
                    }
                    successCallback(alerts)
                }
                else
                {
                    errorCallback(Exception("Search failed"))
                }

            }
        })

    }



    fun retrieveStations(
        key: String,
        from: String,
        to: String,
        successCallback: (List<AlertItem>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {

// Building the request, passing the OAuth token as a header
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jPath?FromStationCode=$from&ToStationCode=$to")
            .header("api_key", key)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Similar error handling to last time
                errorCallback(Exception("Call failed"))
            }

            override fun onResponse(call: Call, response: Response) {
                // Similar success / error handling to last time
                val route = mutableListOf<AlertItem>()
                val responseString = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    val statuses = JSONObject(responseString).getJSONArray("Path")
                    for (i in 0 until statuses.length()) {
                        val curr = statuses.getJSONObject(i)
                        val name = curr.getString("StationName")
                        val lc = curr.getString("LineCode")

                        route.add(
                            AlertItem(
                                line = name,
                                content = lc
                            )
                        )
                    }
                    if(statuses.length()==0)
                    {
                        route.add(
                            AlertItem(
                                line = "No Route",
                                content = ""
                            )
                        )
                    }
                    successCallback(route)
                }
                else
                {
                    errorCallback(Exception("Search failed"))
                }

            }
        })

    }

    fun getS(
        key: String,
        address: Address,
        successCallback: (String) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        val lat = address.latitude
        val lon = address.longitude
        val radius = 800


// Building the request, passing the OAuth token as a header
        val request = Request.Builder()
            .url("https://api.wmata.com/Rail.svc/json/jStationEntrances?Lat=$lat&Lon=$lon&Radius=$radius")
            .header("api_key", key)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Similar error handling to last time
                errorCallback(Exception("Call failed"))
            }

            override fun onResponse(call: Call, response: Response) {
                // Similar success / error handling to last time
                val destinations = mutableListOf<AlertItem>()
                val responseString = response.body()?.string()

                if (response.isSuccessful && responseString != null) {
                    val statuses = JSONObject(responseString).getJSONArray("Entrances")
                    for (i in 0 until statuses.length()) {
                        val curr = statuses.getJSONObject(i)
                        val name = curr.getString("Name")
                        val stationCode = curr.getString("StationCode1")

                        destinations.add(
                            AlertItem(
                                line = name,
                                content = stationCode
                            )
                        )
                    }
                    if(destinations.size>0)
                        successCallback(destinations[0].content)
                    else
                        successCallback("C04")
                }
                else
                {
                    errorCallback(Exception("Search failed"))
                }

            }
        })

    }

}