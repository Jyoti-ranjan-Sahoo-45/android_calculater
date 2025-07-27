package com.example.calculater;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    
    private TextView display;
    private TextView expressionDisplay;
    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewNumber = true;
    private String expression = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        display = findViewById(R.id.display);
        expressionDisplay = findViewById(R.id.expressionDisplay);
        
        // Number buttons
        setupNumberButton(R.id.btn0, "0");
        setupNumberButton(R.id.btn1, "1");
        setupNumberButton(R.id.btn2, "2");
        setupNumberButton(R.id.btn3, "3");
        setupNumberButton(R.id.btn4, "4");
        setupNumberButton(R.id.btn5, "5");
        setupNumberButton(R.id.btn6, "6");
        setupNumberButton(R.id.btn7, "7");
        setupNumberButton(R.id.btn8, "8");
        setupNumberButton(R.id.btn9, "9");
        
        // Operation buttons
        setupOperatorButton(R.id.btnPlus, "+");
        setupOperatorButton(R.id.btnMinus, "−");
        setupOperatorButton(R.id.btnMultiply, "×");
        setupOperatorButton(R.id.btnDivide, "÷");
        
        // Special buttons
        setupEqualsButton();
        setupClearButton();
        setupDotButton();
        setupPlusMinusButton();
        setupPercentButton();
    }
    
    private void setupNumberButton(int buttonId, String number) {
        MaterialButton button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonPress(v);
                if (isNewNumber) {
                    currentNumber = number;
                    isNewNumber = false;
                } else {
                    currentNumber += number;
                }
                updateDisplay();
            }
        });
    }
    
    private void setupOperatorButton(int buttonId, String op) {
        MaterialButton button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonPress(v);
                if (!currentNumber.isEmpty()) {
                    if (!operator.isEmpty()) {
                        calculate();
                    }
                    firstNumber = Double.parseDouble(currentNumber);
                    operator = op;
                    expression = formatDisplayNumber(currentNumber) + " " + op;
                    isNewNumber = true;
                    updateExpressionDisplay();
                }
            }
        });
    }
    
    private void setupEqualsButton() {
        MaterialButton button = findViewById(R.id.btnEquals);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonPress(v);
                if (!currentNumber.isEmpty() && !operator.isEmpty()) {
                    expression += " " + formatDisplayNumber(currentNumber) + " =";
                    updateExpressionDisplay();
                    calculate();
                    operator = "";
                    isNewNumber = true;
                }
            }
        });
    }
    
    private void setupClearButton() {
        MaterialButton button = findViewById(R.id.btnClear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonPress(v);
                clear();
            }
        });
    }
    
    private void setupDotButton() {
        MaterialButton button = findViewById(R.id.btnDot);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonPress(v);
                if (isNewNumber) {
                    currentNumber = "0.";
                    isNewNumber = false;
                } else if (!currentNumber.contains(".")) {
                    currentNumber += ".";
                }
                updateDisplay();
            }
        });
    }
    
    private void setupPlusMinusButton() {
        MaterialButton button = findViewById(R.id.btnPlusMinus);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonPress(v);
                if (!currentNumber.isEmpty()) {
                    if (currentNumber.startsWith("-")) {
                        currentNumber = currentNumber.substring(1);
                    } else {
                        currentNumber = "-" + currentNumber;
                    }
                    updateDisplay();
                }
            }
        });
    }
    
    private void setupPercentButton() {
        MaterialButton button = findViewById(R.id.btnPercent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateButtonPress(v);
                if (!currentNumber.isEmpty()) {
                    double number = Double.parseDouble(currentNumber);
                    number = number / 100;
                    currentNumber = formatNumber(number);
                    updateDisplay();
                }
            }
        });
    }
    
    private void calculate() {
        if (currentNumber.isEmpty()) return;
        
        double secondNumber = Double.parseDouble(currentNumber);
        double result = 0;
        
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "−":
                result = firstNumber - secondNumber;
                break;
            case "×":
                result = firstNumber * secondNumber;
                break;
            case "÷":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Error");
                    animateError();
                    return;
                }
                break;
        }
        
        currentNumber = formatNumber(result);
        updateDisplay();
        animateResult();
    }
    
    private void clear() {
        currentNumber = "";
        operator = "";
        firstNumber = 0;
        isNewNumber = true;
        expression = "";
        display.setText("0");
        expressionDisplay.setText("");
    }
    
    private void updateDisplay() {
        if (currentNumber.isEmpty()) {
            display.setText("0");
        } else {
            display.setText(formatDisplayNumber(currentNumber));
        }
    }
    
    private void updateExpressionDisplay() {
        expressionDisplay.setText(expression);
    }
    
    private String formatNumber(double number) {
        String result = String.valueOf(number);
        if (result.endsWith(".0")) {
            result = result.substring(0, result.length() - 2);
        }
        return result;
    }
    
    private String formatDisplayNumber(String number) {
        try {
            double num = Double.parseDouble(number);
            if (num == (long) num) {
                return String.format("%d", (long) num);
            } else {
                return String.format("%s", number);
            }
        } catch (NumberFormatException e) {
            return number;
        }
    }
    
    private void animateButtonPress(View button) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.98f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0.98f, 1f);
        
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(100);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }
    
    private void animateResult() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(display, "alpha", 1f, 0.7f, 1f);
        alpha.setDuration(200);
        alpha.setInterpolator(new AccelerateDecelerateInterpolator());
        alpha.start();
    }
    
    private void animateError() {
        ObjectAnimator shakeX = ObjectAnimator.ofFloat(display, "translationX", 0, -5, 5, -5, 5, 0);
        shakeX.setDuration(300);
        shakeX.setInterpolator(new AccelerateDecelerateInterpolator());
        shakeX.start();
    }
}