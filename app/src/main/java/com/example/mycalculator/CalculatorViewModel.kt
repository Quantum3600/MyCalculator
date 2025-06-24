package com.example.mycalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.toString

// Enum to represent calculator operations

enum class CalculatorOperation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE, NONE
}

class CalculatorViewModel : ViewModel() {
    var currentInput by mutableStateOf("0")
        private set

    var expression by mutableStateOf("")
        private set

    private var firstOperand by mutableStateOf<Double?>(null)
    private var currentOperator by mutableStateOf(CalculatorOperation.NONE)
    private var isNewOperation by mutableStateOf(true)

    fun onNumberClick(number: String) {
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
        if (firstOperand == null) {
            firstOperand = currentInput.toDoubleOrNull()
        } else if (!isNewOperation) {
            performCalculation()
        }
        currentOperator = operator
        isNewOperation = true
        updateExpression()
    }

    fun onEqualsClick() {
        performCalculation()
        currentOperator = CalculatorOperation.NONE
        isNewOperation = true
        expression = ""
    }

    fun onClearClick() {
        currentInput = "0"
        firstOperand = null
        currentOperator = CalculatorOperation.NONE
        isNewOperation = true
        expression = ""
    }

    fun onChangeSignClick() {
        currentInput = try {
            (currentInput.toDouble() * -1).toString()
        } catch (_: NumberFormatException) {
            "Error"
        }
    }

    fun onPercentageClick() {
        currentInput = try {
            val value = currentInput.toDouble()
            (value / 100).toString()
        } catch (_: NumberFormatException) {
            "Error"
        }
    }

    private fun performCalculation() {
        val secondOperand = currentInput.toDoubleOrNull()
        if (firstOperand != null && secondOperand != null && currentOperator != CalculatorOperation.NONE) {
            val result = when (currentOperator) {
                CalculatorOperation.ADD -> firstOperand!! + secondOperand
                CalculatorOperation.SUBTRACT -> firstOperand!! - secondOperand
                CalculatorOperation.MULTIPLY -> firstOperand!! * secondOperand
                CalculatorOperation.DIVIDE -> {
                    if (secondOperand != 0.0) firstOperand!! / secondOperand else Double.NaN
                }
                else -> secondOperand
            }

            currentInput = if (result.isNaN() || result.isInfinite()) {
                "Error"
            } else {
                if(result == result.toLong().toDouble()) {
                    result.toLong().toString()
                } else {
                    result.toString()
                }
            }
            firstOperand = currentInput.toDoubleOrNull()
        } else if (firstOperand == null && secondOperand != null && currentOperator == CalculatorOperation.NONE) {
            currentInput = currentInput
        }
    }

    private fun updateExpression() {
        val op = when (currentOperator) {
            CalculatorOperation.ADD -> "+"
            CalculatorOperation.SUBTRACT -> "-"
            CalculatorOperation.MULTIPLY -> "×"
            CalculatorOperation.DIVIDE -> "÷"
            else -> ""
        }
        expression = if (firstOperand != null && currentOperator != CalculatorOperation.NONE && !isNewOperation) {
            "${firstOperand!!.toString().removeSuffix(".0")} $op $currentInput"
        } else if (firstOperand != null && currentOperator != CalculatorOperation.NONE) {
            "${firstOperand!!.toString().removeSuffix(".0")} $op"
        } else {
            currentInput
        }
    }
}