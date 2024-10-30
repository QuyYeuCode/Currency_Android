package com.learn.currency

import android.os.Bundle
import android.telecom.Call
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.IOException
import java.text.DecimalFormat
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var editTextAmount: EditText
    private lateinit var textViewResult: TextView
    private val currencyList = listOf("USD", "EUR", "VND", "JPY", "GBP", "AUD", "CAD", "SGD", "CHF", "THB", "MYR", "CNY")
    private val exchangeRates = mapOf(
    "USD" to 24000.0, // 1 USD = 24,000 VND
    "EUR" to 27000.0, // 1 EUR = 27,000 VND
    "JPY" to 220.0,   // 1 JPY = 220 VND
    "GBP" to 31000.0, // 1 GBP = 31,000 VND
    "AUD" to 16000.0, // 1 AUD = 16,000 VND
    "CAD" to 18000.0, // 1 CAD = 18,000 VND
    "SGD" to 17000.0, // 1 SGD = 17,000 VND
    "CHF" to 26000.0, // 1 CHF = 26,000 VND
    "THB" to 700.0,   // 1 THB = 700 VND
    "MYR" to 5400.0,  // 1 MYR = 5,400 VND
    "CNY" to 3500.0   // 1 CNY = 3,500 VND
)
    private var conversionRate = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        spinnerFrom = findViewById(R.id.spinner_from)
        spinnerTo = findViewById(R.id.spinner_to)
        editTextAmount = findViewById(R.id.editTextAmount)
        textViewResult = findViewById(R.id.textViewResult)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fetchExchangeRate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fetchExchangeRate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        editTextAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateConversion()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    private fun fetchExchangeRate() {
    val currencyFrom = spinnerFrom.selectedItem.toString()
    val currencyTo = spinnerTo.selectedItem.toString()
    conversionRate = if (currencyTo == "VND") {
        exchangeRates[currencyFrom] ?: 1.0
    }
    else if (currencyFrom == "VND") {
        1 / (exchangeRates[currencyTo] ?: 1.0)
    }
    else {
        val rateFromVND = exchangeRates[currencyFrom] ?: 1.0
        val rateToVND = exchangeRates[currencyTo] ?: 1.0
        rateFromVND / rateToVND
    }

    calculateConversion()
}

    private fun calculateConversion() {
    val amount = editTextAmount.text.toString().toDoubleOrNull()
    if (amount != null) {
        val result = amount * conversionRate
        val decimalFormat = DecimalFormat("#.###")
        textViewResult.text = decimalFormat.format(result) + " " + spinnerTo.selectedItem.toString()
    }else{
        textViewResult.text = "Result"
    }
}
}