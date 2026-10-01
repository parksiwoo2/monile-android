package com.example.mobile

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal
import java.math.MathContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        savedInstanceState?.getString("result")?.let { findViewById<TextView>(R.id.resultText).text = it }
        mapOf(R.id.addButton to "+", R.id.subtractButton to "−", R.id.multiplyButton to "×", R.id.divideButton to "÷").forEach { (id, operator) ->
            findViewById<View>(id).setOnClickListener { calculate(operator) }
        }
    }

    private fun calculate(operator: String) {
        val firstInput = findViewById<EditText>(R.id.firstNumber)
        val secondInput = findViewById<EditText>(R.id.secondNumber)
        val result = findViewById<TextView>(R.id.resultText)
        val first = firstInput.text.toString().trim().toBigDecimalOrNull()
        val second = secondInput.text.toString().trim().toBigDecimalOrNull()
        firstInput.error = if (first == null) "올바른 숫자를 입력해주세요" else null
        secondInput.error = if (second == null) "올바른 숫자를 입력해주세요" else null
        if (first == null || second == null) {
            result.text = "두 숫자를 올바르게 입력해주세요"
            return
        }
        if (operator == "÷" && second.compareTo(BigDecimal.ZERO) == 0) {
            secondInput.error = "0으로 나눌 수 없습니다"
            result.text = "0으로 나눌 수 없습니다"
            return
        }
        val value = when (operator) {
            "+" -> first + second
            "−" -> first - second
            "×" -> first * second
            else -> first.divide(second, MathContext.DECIMAL64)
        }
        result.text = "${first.stripTrailingZeros().toPlainString()} $operator ${second.stripTrailingZeros().toPlainString()} = ${value.stripTrailingZeros().toPlainString()}"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("result", findViewById<TextView>(R.id.resultText).text.toString())
        super.onSaveInstanceState(outState)
    }
}
