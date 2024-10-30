package com.example.convertmoney

import android.icu.text.Transliterator.Position
import android.icu.util.Currency
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.temporal.TemporalAmount

class MainActivity : AppCompatActivity() {
    private lateinit var sourceAmount: EditText
    private lateinit var targetAmount: EditText
    private lateinit var sourceCurrency: Spinner
    private lateinit var targetCurrency: Spinner
    private lateinit var convertButton: Button
    private lateinit var clearButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        sourceAmount = findViewById(R.id.sourceAmount)
        targetAmount = findViewById(R.id.targetAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        targetCurrency = findViewById(R.id.targetCurrency)

        clearButton = findViewById(R.id.clearButton)

        ArrayAdapter.createFromResource(
            this,
            R.array.currency_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sourceCurrency.adapter = adapter
            targetCurrency.adapter = adapter

        }


        sourceAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performConversion()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        sourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id:Long)
            {
                performConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        targetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                performConversion()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        clearButton.setOnClickListener{
            clearFields()
        }

    }


    private fun performConversion(){
        val  sourceText = sourceAmount.text.toString()
        if(sourceText.isEmpty()){
            targetAmount.setText("")
            return
        }

        val amount = sourceText.toDouble()
        val sourceCurr = sourceCurrency.selectedItem.toString()
        val targetCurr = targetCurrency.selectedItem.toString()

        val rate = getRate(sourceCurr, targetCurr)
        val convertedAmount = amount * rate
        targetAmount.setText(String.format("%.2f", convertedAmount))
    }

    private fun getRate(source: String, target: String,): Double{
        return when("$source-$target"){
            "USD-EUR" -> 0.85
            "EUR-USD" -> 1.18
            "USD-VND" -> 25294.94
            "VND-USD" -> 0.00003953
            "EUR-VND" -> 28000.0
            "VND-EUR" -> 0.000036
            else -> 1.0
        }
    }

    private fun clearFields(){
        sourceAmount.text.clear()
        targetAmount.text.clear()
        sourceCurrency.setSelection(0)
        targetCurrency.setSelection(0)
    }
}