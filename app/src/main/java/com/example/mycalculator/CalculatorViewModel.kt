package com.example.mycalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class CalculatorOperation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE, NONE
}

class CalculatorViewModel : ViewModel() {
    var currentInput by mutableStateOf("0")
        private set

    var expression by mutableStateOf("")
        private set

    private var wasEqualsPressed by mutableStateOf(false)
    private var isNewOperation by mutableStateOf(true)
    private var lastOperator by mutableStateOf(CalculatorOperation.NONE)
    private var expressionList = mutableListOf<String>()

    fun onNumberClick(number: String) {
        if (wasEqualsPressed) {
            expressionList.clear()
            expression = ""
            wasEqualsPressed = false
        }
        if (isNewOperation) {
            currentInput = number
            isNewOperation = false
        } else {
            if (currentInput == "0" && number != ".") {
                currentInput = number
            } else {
                currentInput += number
            }
        }
        updateExpression()
    }

    fun onDecimalClick() {
        if (!currentInput.contains(".")) {
            currentInput += "."
            isNewOperation = false
        }
        updateExpression()
    }

    fun onOperatorClick(operator: CalculatorOperation) {
        if (!isNewOperation) {
            expressionList.add(currentInput)
            expressionList.add(getOperatorSymbol(operator))
        } else if (expressionList.isNotEmpty()) {
            // Replace the last operator if consecutive operators are clicked
            expressionList[expressionList.lastIndex] = getOperatorSymbol(operator)
        }
        lastOperator = operator
        isNewOperation = true
        updateExpression()
    }

    fun onEqualsClick() {
        if (!isNewOperation) {
            expressionList.add(currentInput)
        }
        calculateResult()
        if (currentInput != "Error") {
            expressionList.add(currentInput)
        }
        isNewOperation = true
        lastOperator = CalculatorOperation.NONE
        expression = ""
        wasEqualsPressed = true
    }

    fun onClearClick() {
        currentInput = "0"
        expressionList.clear()
        isNewOperation = true
        lastOperator = CalculatorOperation.NONE
        expression = ""
    }

    fun onChangeSignClick() {
        currentInput = try {
            (currentInput.toDouble() * -1).toString()
        } catch (_: NumberFormatException) {
            "Error"
        }
        updateExpression()
    }

    fun onPercentageClick() {
        currentInput = try {
            val value = currentInput.toDouble()
            (value / 100).toString()
        } catch (_: NumberFormatException) {
            "Error"
        }
        updateExpression()
    }

    private fun calculateResult() {
        if (expressionList.isEmpty()) return

        // Copy the expression list to work with
        val tempExpression = ArrayList(expressionList)
        if (!isNewOperation) {
            tempExpression.add(currentInput)
        }

        // First handle multiplication and division
        var i = 1
        while (i < tempExpression.size - 1) {
            if (tempExpression[i] in listOf("×", "÷")) {
                val result = performOperation(
                    tempExpression[i - 1].toDouble(),
                    tempExpression[i + 1].toDouble(),
                    tempExpression[i]
                )
                tempExpression[i - 1] = result.toString()
                tempExpression.removeAt(i)
                tempExpression.removeAt(i)
                i -= 1
            } else {
                i += 2
            }
        }

        // Then handle addition and subtraction
        i = 1
        while (i < tempExpression.size - 1) {
            val result = performOperation(
                tempExpression[0].toDouble(),
                tempExpression[i + 1].toDouble(),
                tempExpression[i]
            )
            tempExpression[0] = result.toString()
            tempExpression.removeAt(i)
            tempExpression.removeAt(i)
        }

        currentInput = formatResult(tempExpression[0].toDouble())
    }

    private fun performOperation(a: Double, b: Double, operator: String): Double {
        return when (operator) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> if (b != 0.0) a / b else Double.NaN
            else -> b
        }
    }

    private fun formatResult(result: Double): String {
        return if (result.isNaN() || result.isInfinite()) {
            "Error"
        } else if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }

    private fun getOperatorSymbol(operator: CalculatorOperation): String {
        return when (operator) {
            CalculatorOperation.ADD -> "+"
            CalculatorOperation.SUBTRACT -> "-"
            CalculatorOperation.MULTIPLY -> "×"
            CalculatorOperation.DIVIDE -> "÷"
            else -> ""
        }
    }

    private fun updateExpression() {
        expression = if (expressionList.isEmpty()) {
            currentInput
        } else {
            expressionList.joinToString(" ") + " " + 
            (if (!isNewOperation) currentInput else "")
        }
    }
}