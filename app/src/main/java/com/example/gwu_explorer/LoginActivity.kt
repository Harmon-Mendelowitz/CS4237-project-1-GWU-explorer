package com.example.gwu_explorer

import android.content.Context
import android.content.Intent
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

            /*val sendIntent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Android Tweets is a great app!")
            startActivity(sendIntent)*/


            //Toast.makeText(this, "this is a toast message", Toast.LENGTH_LONG)

            val choices = listOf("600 Maryland Ave SW, Washington, DC 20002, USA", "600 Main St, Laurel, MD, 20707, USA", "Another Fake Entry")
            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
            arrayAdapter.addAll(choices)

            AlertDialog.Builder(this)
                .setTitle("Possible Matches")
                .setAdapter(arrayAdapter) { _, which ->
                    Toast.makeText(this, "You picked: ${choices[which]}", Toast.LENGTH_SHORT).show()
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
        AlertDialog.Builder(this)
            .setTitle("Welcome")
            .setMessage("Please enter a location to travel to from Foggy Bottom. " +
                    " The Alerts button can be used to view Metro Outages. " +
                    " *Currently only works for non-transfer lines.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()}
            .show()
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
