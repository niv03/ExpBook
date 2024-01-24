package com.example.nivit_expbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText monthEditText;
    private EditText chargeEditText;
    private EditText commentEditText;
    private Button saveButton;
    private Button deleteButton;
    private Expense expense;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        // Initialize UI elements and retrieve expense data
        nameEditText = findViewById(R.id.nameEditText);
        monthEditText = findViewById(R.id.monthEditText);
        chargeEditText = findViewById(R.id.chargeEditText);
        commentEditText = findViewById(R.id.commentEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Retrieve expense data and position passed from MainActivity
        Intent intent = getIntent();
        expense = (Expense) intent.getSerializableExtra("expense");
        position = intent.getIntExtra("position", -1);

        // Populate UI elements with expense data
        nameEditText.setText(expense.getName());
        monthEditText.setText(expense.getMonth());
        chargeEditText.setText(String.valueOf(expense.getMonthlyCharge()));
        commentEditText.setText(expense.getComment());

        // Implement save and delete functionality
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the expense details and return to MainActivity
                String name = nameEditText.getText().toString();
                String month = monthEditText.getText().toString();
                String chargeStr = chargeEditText.getText().toString();
                String comment = commentEditText.getText().toString();

                // Validate and handle errors
                if (!isValidName(name)) {
                    showToast("Name must be up to 15 characters.");
                    return;
                }

                if (!isValidMonthFormat(month)) {
                    showToast("Invalid month format (yyyy-MM).");
                    return;
                }

                double charge;
                try {
                    charge = Double.parseDouble(chargeStr);
                    if (charge < 0 ) {
                        showToast("Monthly charge must be non-negative and in Canadian dollars.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showToast("Invalid charge value.");
                    return;
                }

                if (!isValidComment(comment)) {
                    showToast("Comment must be up to 20 characters.");
                    return;
                }

                // Update the expense
                expense.setName(name);
                expense.setMonth(month);
                expense.setMonthlyCharge(charge);
                expense.setComment(comment);

                // Return to MainActivity with the updated expense and position
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedExpense", expense);
                resultIntent.putExtra("position", position);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the expense and return to MainActivity with the position
                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    // Helper method to validate the name
    private boolean isValidName(String name) {
        return name.length() <= 15;
    }

    // Helper method to validate the month format
    private boolean isValidMonthFormat(String month) {
        // Define a regular expression pattern for yyyy-MM format
        String regexPattern = "^(19|20)\\d\\d-(0[1-9]|1[0-2])$";

        // Use the matches method to check if the input matches the pattern
        return month.matches(regexPattern);
    }


    // Helper method to validate the comment
    private boolean isValidComment(String comment) {
        return comment.length() <= 20;
    }

    // Helper method to show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
