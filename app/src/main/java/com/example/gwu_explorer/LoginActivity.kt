package com.example.gwu_explorer

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity(){

    private lateinit var address: EditText
    private lateinit var go: Button
    private lateinit var alerts: Button
    private lateinit var remember: CheckBox
    private lateinit var choices2: MutableList<String>

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputU: String = address.text.toString().trim() // trim doesn't allow just spaces
            val enabledButton: Boolean = inputU.isNotEmpty()

            go.isEnabled = enabledButton
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d("LoginActivity", "onCreate called")

        val sharedPrefs = getSharedPreferences("locationstorage", Context.MODE_PRIVATE)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialogopen))
            .setMessage(getString(R.string.dialogdesc))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss()}
            .show()

        address = findViewById(R.id.destination)
        go = findViewById(R.id.gobutton)
        alerts = findViewById(R.id.alertsbutton)
        address.addTextChangedListener(textWatcher)
        remember = findViewById(R.id.remember)

        //Lec 4
        address.setText(sharedPrefs.getString("SAVED_ADDRESS", ""))


        go.setOnClickListener {

            if(remember.isChecked)
            {
                sharedPrefs.edit().putString("SAVED_ADDRESS",address.text.toString()).apply()
            }
            if(!remember.isChecked)
            {
                sharedPrefs.edit().putString("SAVED_ADDRESS","").apply()
            }
            /*val sendIntent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Android Tweets is a great app!")
            startActivity(sendIntent)*/


            //Toast.makeText(this, "this is a toast message", Toast.LENGTH_LONG)

            val select: String =  address.text.toString()

            //val choices = listOf("600 Maryland Ave SW, Washington, DC 20002, USA", "600 Main St, Laurel, MD, 20707, USA", "Another Fake Entry")
            val geocoder = Geocoder(this@LoginActivity)
            val choices: List<Address> = geocoder.getFromLocationName(select, 5)
            val choices2: MutableList<String> =  mutableListOf<String>()

            for(i in 0 until choices.size) {
                choices2.add(i, choices[i].getAddressLine(i))
            }

            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
            arrayAdapter.addAll(choices2)

            AlertDialog.Builder(this)
                .setTitle("Possible Matches")
                .setAdapter(arrayAdapter) { _, which ->
                    Toast.makeText(this, "You picked: ${choices2[which]}", Toast.LENGTH_SHORT).show()

                    val intent: Intent = Intent(this, RouteActivity::class.java)
                    intent.putExtra("location", choices[which])
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss()
                }

                .show()

        }

        alerts.setOnClickListener {
            val intent: Intent = Intent(this, Alerts::class.java)
            //intent.putExtra("location", "Washington D.C.")
            startActivity(intent)
        }
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
