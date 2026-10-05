package com.example.calculatorapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private String currentNumber = "";
    private String firstNumber = "";
    private String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display
        tvDisplay = findViewById(R.id.tvDisplay);

        // Number buttons (0 - 9)
        int[] numberButtons = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberButtons) {
            Button button = findViewById(id);
            if (button != null) {
                button.setOnClickListener(v -> {
                    Button clickedButton = (Button) v;
                    currentNumber += clickedButton.getText().toString();
                    tvDisplay.setText(currentNumber);
                });
            }
        }

        // Decimal button
        Button btnDecimal = findViewById(R.id.btnDecimal);
        if (btnDecimal != null) {
            btnDecimal.setOnClickListener(v -> {
                if (!currentNumber.contains(".")) {
                    if (currentNumber.isEmpty()) {
                        currentNumber = "0";
                    }
                    currentNumber += ".";
                    tvDisplay.setText(currentNumber);
                }
            });
        }

        // Operator buttons
        setupOperatorButton(R.id.btnAdd, "+");
        setupOperatorButton(R.id.btnSubtract, "-");
        setupOperatorButton(R.id.btnMultiply, "*");
        setupOperatorButton(R.id.btnDivide, "/");

        // Action buttons
        Button btnEquals = findViewById(R.id.btnEquals);
        if (btnEquals != null) {
            btnEquals.setOnClickListener(v -> calculateResult());
        }

        Button btnClear = findViewById(R.id.btnClear);
        if (btnClear != null) {
            btnClear.setOnClickListener(v -> clearCalculator());
        }

        Button btnBackspace = findViewById(R.id.btnBackspace);
        if (btnBackspace != null) {
            btnBackspace.setOnClickListener(v -> backspace());
        }
    }

    private void setupOperatorButton(int buttonId, String op) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> setOperator(op));
        }
    }

    // Operator set karne ka method
    private void setOperator(String selectedOperator) {
        if (currentNumber.isEmpty()) {
            return;
        }

        firstNumber = currentNumber;
        operator = selectedOperator;
        currentNumber = "";
        tvDisplay.setText(firstNumber + " " + operator);
    }

    // Calculation method
    private void calculateResult() {
        if (firstNumber.isEmpty() || currentNumber.isEmpty() || operator.isEmpty()) {
            return;
        }

        double number1 = Double.parseDouble(firstNumber);
        double number2 = Double.parseDouble(currentNumber);
        double result = 0;

        switch (operator) {
            case "+":
                result = number1 + number2;
                break;
            case "-":
                result = number1 - number2;
                break;
            case "*":
                result = number1 * number2;
                break;
            case "/":
                if (number2 == 0) {
                    tvDisplay.setText("Cannot divide by zero");
                    clearCalculatorState();
                    return;
                }
                result = number1 / number2;
                break;
        }

        String formattedResult = formatResult(result);
        tvDisplay.setText(formattedResult);
        currentNumber = formattedResult;
        firstNumber = "";
        operator = "";
    }

    // Double value ko clean format karne ke liye helper (.0 handling)
    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        }
        return String.valueOf(result);
    }

    private void clearCalculatorState() {
        currentNumber = "";
        firstNumber = "";
        operator = "";
    }

    private void clearCalculator() {
        clearCalculatorState();
        if (tvDisplay != null) {
            tvDisplay.setText("0");
        }
    }

    private void backspace() {
        if (!currentNumber.isEmpty()) {
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);
            if (tvDisplay != null) {
                tvDisplay.setText(currentNumber.isEmpty() ? "0" : currentNumber);
            }
        }
    }
}