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
    //private var ot: String? = null

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
    /*fun retrieveOAuthToken(
        successCallback: (String) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        // If the token is cached, we don't need to call the API
        if (ot != null) {
            successCallback(ot!!)
            return
        }
        val encodedKey = "@string/wmata_key"

        // Builds the OAuth request, which is comprised of:
        //   - URL: https://api.twitter.com/oauth2/token"
        //   - One header (the encoded key above)
        //   - It's a POST call
        //   - The body type is a special type (x-www-form-urlencoded). Usually, you will just see
        //     a JSON-based body.
        val request: Request = Request.Builder()
            .url("https://api.wmata.com/Incidents.svc/json/Incidents")
            .header("api_key", "$encodedKey")
            .post(
                RequestBody.create(
                    MediaType.parse("application/x-www-form-urlencoded"),
                    "grant_type=client_credentials"
                )
            )
            .build()

        // Let OkHttp handle the actual networking. It will call one of the two callbacks...
        okHttpClient.newCall(request).enqueue(object : Callback {
            /**
             * [onFailure] is called if OkHttp is has an issue making the request (for example,
             * no network connectivity).
             */
            override fun onFailure(call: Call, e: IOException) {
                // Invoke the callback passed to our [retrieveOAuthToken] function.
                errorCallback(e)
            }

            /**
             * [onResponse] is called if OkHttp is able to get any response (successful or not)
             * back from the server
             */
            override fun onResponse(call: Call, response: Response) {
                // The token would be part of the JSON response body
                val responseBody = response.body()?.string()

                // Check if the response was successful (200 code) and the body is non-null
                if (response.isSuccessful && responseBody != null) {
                    // Parse the token out of the JSON
                    val jsonObject = JSONObject(responseBody)
                    val token = jsonObject.getString("access_token")
                    ot = token

                    // Invoke the callback passed to our [retrieveOAuthToken] function.
                    successCallback(token)
                } else {
                    // Invoke the callback passed to our [retrieveOAuthToken] function.
                    errorCallback(Exception("OAuth call failed"))
                }
            }
        }
    )
    }*/

    fun retrieveAlerts(
        key: String,
        //LinesAffected: String,
        //Description: String,
        successCallback: (List<AlertItem>) -> Unit,
        errorCallback: (Exception) -> Unit
    ) {
        //val line = LinesAffected
        //val desc = Description
        //val topic = "Android"
        //val radius = "30mi"

        //val key = getString(R.string.wmata_key)
// Building the request, passing the OAuth token as a header
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

                        route.add(
                            AlertItem(
                                line = name,
                                content = ""
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



    fun retrieveLocation(
        key: String,
        address: Address,
        successCallback: (List<AlertItem>) -> Unit,
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
                    successCallback(destinations)
                }
                else
                {
                    errorCallback(Exception("Search failed"))
                }

            }
        })

    }

}