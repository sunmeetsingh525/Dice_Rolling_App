package com.example.dice_rolling_app

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val diceOptions = resources.getStringArray(R.array.dice_options)
        val spinner: Spinner = findViewById(R.id.spinner)
        val rollButton: Button = findViewById(R.id.rollButton)
        val rollTwiceButton: Button = findViewById(R.id.rollTwiceButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diceOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        rollButton.setOnClickListener {
            val maxVal = getSelectedDiceMaxValue()
            val randomVal = Random.nextInt(1, maxVal + 1)
            resultTextView.text = randomVal.toString()
        }

        rollTwiceButton.setOnClickListener {
            val maxVal = getSelectedDiceMaxValue()
            val randomVal1 = Random.nextInt(1, maxVal + 1)
            val randomVal2 = Random.nextInt(1, maxVal + 1)
            resultTextView.text = "$randomVal1, $randomVal2"
        }
    }

    private fun getSelectedDiceMaxValue(): Int {
        val spinner: Spinner = findViewById(R.id.spinner)
        val selectedDice = spinner.selectedItem.toString()
        val customValue = sharedPreferences.getString("customDice", "")?.toIntOrNull()

        return when {
            selectedDice == "Custom" && customValue != null && customValue > 0 -> customValue
            selectedDice == "d10" -> 10
            selectedDice == "True d10" -> 9
            else -> selectedDice.substring(1).toInt()
        }
    }
}
