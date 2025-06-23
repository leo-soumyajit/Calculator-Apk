package com.example.calculator

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var resultView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable fullscreen
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.editTextExpression)
        resultView = findViewById(R.id.textViewResult)

        val buttons = listOf(
            R.id.button0 to "0",
            R.id.button1 to "1",
            R.id.button2 to "2",
            R.id.button3 to "3",
            R.id.button4 to "4",
            R.id.button5 to "5",
            R.id.button6 to "6",
            R.id.button7 to "7",
            R.id.button8 to "8",
            R.id.button9 to "9",
            R.id.buttonDot to ".",
            R.id.buttonPlus to "+",
            R.id.buttonMinus to "-",
            R.id.buttonMultiply to "*",
            R.id.buttonDivide to "/",
            R.id.buttonOpen to "(",
            R.id.buttonClose to ")"
        )

        buttons.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener {
                appendToInput(value)
            }
        }

        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            inputField.setText("")
            resultView.text = "0"
        }

        findViewById<Button>(R.id.buttonBackspace).setOnClickListener {
            val current = inputField.text.toString()
            if (current.isNotEmpty()) {
                inputField.setText(current.dropLast(1))
                inputField.setSelection(inputField.text.length)
            }
        }

        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            try {
                val expression = ExpressionBuilder(inputField.text.toString()).build()
                val result = expression.evaluate()
                resultView.text = result.toString()
            } catch (e: Exception) {
                resultView.text = "Error"
            }
        }
    }

    private fun appendToInput(value: String) {
        inputField.text = Editable.Factory.getInstance()
            .newEditable(inputField.text.toString() + value)
        inputField.setSelection(inputField.text.length)
    }
}
