package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var resultView: TextView
    private lateinit var scientificLayout: GridLayout
    private lateinit var toggleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.editTextExpression)
        resultView = findViewById(R.id.textViewResult)
        scientificLayout = findViewById(R.id.scientificLayout)
        toggleButton = findViewById(R.id.buttonToggleScientific)

        // Toggle visibility
        toggleButton.setOnClickListener {
            scientificLayout.visibility =
                if (scientificLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Basic Buttons
        val basicButtons = mapOf(
            R.id.button0 to "0", R.id.button1 to "1", R.id.button2 to "2",
            R.id.button3 to "3", R.id.button4 to "4", R.id.button5 to "5",
            R.id.button6 to "6", R.id.button7 to "7", R.id.button8 to "8",
            R.id.button9 to "9", R.id.buttonDot to ".", R.id.buttonPlus to "+",
            R.id.buttonMinus to "-", R.id.buttonMultiply to "*", R.id.buttonDivide to "/",
            R.id.buttonOpen to "(", R.id.buttonClose to ")"
        )

        basicButtons.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener { appendToInput(value) }
        }

        // Scientific Buttons
        val scientificButtons = mapOf(
            R.id.buttonSin to "sin(", R.id.buttonCos to "cos(", R.id.buttonTan to "tan(",
            R.id.buttonLog to "log(", R.id.buttonLn to "ln(", R.id.buttonSqrt to "sqrt(",
            R.id.buttonPower to "^", R.id.buttonPi to "3.1415926535", R.id.buttonFactorial to "!"
        )

        scientificButtons.forEach { (id, value) ->
            findViewById<Button>(id).setOnClickListener { appendToInput(value) }
        }

        // Clear
        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            inputField.text.clear()
            resultView.text = "0"
        }

        // Backspace
        findViewById<Button>(R.id.buttonBackspace).setOnClickListener {
            val current = inputField.text.toString()
            if (current.isNotEmpty()) {
                inputField.setText(current.dropLast(1))
                inputField.setSelection(inputField.text.length)
            }
        }

        // Evaluate expression
        findViewById<Button>(R.id.buttonEquals).setOnClickListener {
            try {
                val preprocessed = preprocessFactorial(inputField.text.toString())

                // Define ln (natural log) and log (base 10)
                val ln = object : Function("ln", 1) {
                    override fun apply(vararg args: Double): Double {
                        return Math.log(args[0])
                    }
                }

                val log = object : Function("log", 1) {
                    override fun apply(vararg args: Double): Double {
                        return Math.log10(args[0])
                    }
                }

                val expression = ExpressionBuilder(preprocessed)
                    .function(ln)
                    .function(log)
                    .build()

                val result = expression.evaluate()
                resultView.text = result.toString()
            } catch (e: Exception) {
                resultView.text = "Error"
            }
        }
    }

    private fun appendToInput(value: String) {
        val current = inputField.text.toString()
        inputField.setText(current + value)
        inputField.setSelection(inputField.text.length)
    }

    private fun preprocessFactorial(expression: String): String {
        // Replace all occurrences like 5! with their factorial result
        val regex = Regex("""(\d+)!""")
        return regex.replace(expression) {
            val number = it.groupValues[1].toInt()
            factorial(number).toString()
        }
    }

    private fun factorial(n: Int): Long {
        if (n < 0) throw IllegalArgumentException("Negative factorial not allowed")
        var result = 1L
        for (i in 2..n) {
            result *= i
        }
        return result
    }
}
