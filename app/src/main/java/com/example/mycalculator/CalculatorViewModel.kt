package com.example.mycalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mycalculator.data.HistoryDao

enum class CalculatorOperation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

open class CalculatorViewModel(
    private val dao: HistoryDao
) : ViewModel() {

    var currentInput by mutableStateOf("0")
        private set

    var expression by mutableStateOf("")
        private set

    private var wasEqualsPressed by mutableStateOf(false)

    private var expressionList = mutableListOf<String>()

    fun onNumberClick(number: String) {
        when {
            wasEqualsPressed -> {
                // Equals pressed → start fresh
                expressionList.clear()
                currentInput = if (number == ".") "0." else number
                expressionList.add(currentInput)
                wasEqualsPressed = false
            }
            expressionList.isEmpty() -> {
                // First number
                currentInput = if (number == ".") "0." else number
                expressionList.add(currentInput)
            }
            isOperator(expressionList.last()) -> {
                // After operator
                currentInput = if (number == ".") "0." else number
                expressionList.add(currentInput)
            }
            expressionList.isNotEmpty() && expressionList.last() == "-" && (expressionList.size == 1 || isOperator(expressionList[expressionList.size -2])) -> {
                // Handle negative numbers after an operator or at the beginning
                currentInput = if (number == ".") "-0." else "-$number"
                if (expressionList.size > 1 && isOperator(expressionList[expressionList.size - 2])) {
                    expressionList[expressionList.lastIndex] = currentInput // Replace the "-" with the negative number
                } else { // Handles cases like "-" at the start
                    expressionList[expressionList.lastIndex] = currentInput
                }
            }
            else -> {
                // Continue building number
                if (number == "." && currentInput.contains(".")) return

                if (currentInput == "0" && number != ".") {
                    currentInput = number
                } else {
                    currentInput += number
                }
                expressionList[expressionList.lastIndex] = currentInput
            }
        }
        updateExpression()
    }

   fun onOperatorClick(operator: CalculatorOperation) {
       val op = getOperatorSymbol(operator)

       if (wasEqualsPressed) {
           wasEqualsPressed = false
           if (currentInput != "Error") {
               expressionList.clear()
               expressionList.add(currentInput)
           } else {
               currentInput = "0"
               expressionList.clear()
           }
       }

       // Handle first operator as minus for negative numbers
       if (expressionList.isEmpty()) {
           if (op == "-") {
               currentInput = "-"
               expressionList.add("0$currentInput")
               updateExpression()
               return
           } else {
               // If first operator is not minus, just add currentInput
               expressionList.add(currentInput)
           }
       } else if (isOperator(expressionList.last())) {
           // If previous is a minus and another operator is pressed, reset minus to 0
           if (expressionList.last() == "-" && (expressionList.size == 1 || isOperator(expressionList[expressionList.size - 2]))) {
               currentInput = "0"
               expressionList[expressionList.lastIndex] = op
               updateExpression()
               return
           }
           // Replace operator
           expressionList[expressionList.lastIndex] = op
           updateExpression()
           return
       }

       expressionList.add(op)
       updateExpression()
   }

    fun onEqualsClick() {
        if (expressionList.isEmpty()) return

        if (isOperator(expressionList.last())) {
            // If ends with operator, push current input to complete
            expressionList.add(currentInput)
        }

        val result = evaluateExpression()
        currentInput = result
        expressionList.clear()
        expression = ""
        wasEqualsPressed = true
    }

    fun onClearClick() {
        expressionList.clear()
        currentInput = "0"
        expression = ""
        wasEqualsPressed = false
    }

    fun onBackspaceClick() {
        if (currentInput == "0" || currentInput == "Error") return

        currentInput = if (currentInput.length == 1) {
            "0"
        } else if (isOperator(expressionList.last())){
            currentInput
        } else {
            currentInput.dropLast(1)
        }
        if (expressionList.isNotEmpty()) {
            if (isOperator(expressionList.last())) {
                expressionList.removeAt(expressionList.lastIndex)
            } else {
                expressionList[expressionList.lastIndex] = currentInput
            }
        }
        updateExpression()
    }

    fun onPercentageClick() {
        if (currentInput == "0" || currentInput == "Error") return

        try {
            val value = currentInput.toDouble() / 100
            currentInput = formatResult(value)
            expressionList[expressionList.lastIndex] = currentInput
        } catch (_: Exception) {
            currentInput = "Error"
        }
        updateExpression()
    }
    fun onDecimalClick() {
        when {
            wasEqualsPressed -> {
                expressionList.clear()
                currentInput = "0."
                expressionList.add(currentInput)
                wasEqualsPressed = false
            }
            expressionList.isEmpty() -> {
                currentInput = "0."
                expressionList.add(currentInput)
            }
            isOperator(expressionList.last()) -> {
                currentInput = "0."
                expressionList.add(currentInput)
            }
            else -> {
                if (!currentInput.contains(".")) {
                    currentInput += "."
                    expressionList[expressionList.lastIndex] = currentInput
                }
            }
        }
        updateExpression()
    }


    private fun evaluateExpression(): String {
        if (expressionList.isEmpty()) return "0"

        val temp = ArrayList(expressionList)

        // First pass: × and ÷
        var i = 1
        while (i < temp.size - 1) {
            if (temp[i] == "×" || temp[i] == "÷") {
                val a = temp[i - 1].toDouble()
                val b = temp[i + 1].toDouble()
                val result = when (temp[i]) {
                    "×" -> a * b
                    "÷" -> if (b != 0.0) a / b else return "Error"
                    else -> 0.0
                }
                temp[i - 1] = result.toString()
                temp.removeAt(i)
                temp.removeAt(i)
                i -= 1
            } else {
                i += 2
            }
        }

        // Second pass: + and -
        var result = temp[0].toDouble()
        i = 1
        while (i < temp.size - 1) {
            val b = temp[i + 1].toDouble()
            result = when (temp[i]) {
                "+" -> result + b
                "-" -> result - b
                else -> result
            }
            i += 2
        }

        return formatResult(result)
    }

    private fun getOperatorSymbol(op: CalculatorOperation): String {
        return when (op) {
            CalculatorOperation.ADD -> "+"
            CalculatorOperation.SUBTRACT -> "-"
            CalculatorOperation.MULTIPLY -> "×"
            CalculatorOperation.DIVIDE -> "÷"
        }
    }

    private fun isOperator(s: String): Boolean {
        return s in listOf("+", "-", "×", "÷")
    }

    private fun formatResult(result: Double): String {
        return if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }

    private fun updateExpression() {
        expression = expressionList.joinToString(" ")
    }
}
