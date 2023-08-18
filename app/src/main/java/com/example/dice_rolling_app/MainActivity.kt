package com.example.dice_rolling_app

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        val customValueEditText: EditText = findViewById(R.id.customValueEditText)
        val rollButton: Button = findViewById(R.id.rollButton)
        val rollTwiceButton: Button = findViewById(R.id.rollTwiceButton)
        val resultTextView: TextView = findViewById(R.id.resultTextView)

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

        customValueEditText.addTextChangedListener {
            val customValue = it.toString().toIntOrNull()
            if (customValue != null && customValue > 0) {
                val editor = sharedPreferences.edit()
                editor.putString("customDice", customValue.toString())
                editor.apply()
            }
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            customValueEditText.text.clear() // Clear the custom value text when changing dice selection
            resultTextView.text = ""

            if (checkedId == R.id.radio_custom) {
                customValueEditText.visibility = View.VISIBLE
            } else {
                customValueEditText.visibility = View.GONE
            }
        }
    }

    private fun getSelectedDiceMaxValue(): Int {
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val selectedDice = selectedRadioButton.text.toString()
        val customValue = sharedPreferences.getString("customDice", "")?.toIntOrNull()

        return when {
            selectedDice == "Custom" && customValue != null && customValue > 0 -> customValue
            selectedDice == "d10" -> 10
            selectedDice == "True d10" -> 9
            else -> selectedDice.substring(1).toInt()
        }
    }
}

