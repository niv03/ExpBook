package com.example.nivit_expbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expense> expenses = new ArrayList<>();
    private TextView totalChargeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ListView and adapter
        listView = findViewById(R.id.listView);
        expenseAdapter = new ExpenseAdapter(this, expenses);
        listView.setAdapter(expenseAdapter);

        // Initialize total monthly charge TextView
        totalChargeTextView = findViewById(R.id.totalChargeTextView);

        // Button to add expenses
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open dialog to add a new expense
                showAddExpenseDialog();
            }
        });

        // Handle item click to view/edit expense details
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense selectedExpense = expenses.get(position);
                // Start ExpenseDetailsActivity with selectedExpense
                Intent intent = new Intent(MainActivity.this, ExpenseDetailsActivity.class);
                intent.putExtra("expense", selectedExpense);
                intent.putExtra("position", position); // Pass the position
                startActivityForResult(intent, 1); // Start Activity for result
            }
        });
    }

    // Handle the result from ExpenseDetailsActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Check if an expense was updated or deleted
            Expense updatedExpense = (Expense) data.getSerializableExtra("updatedExpense");
            int deletedPosition = data.getIntExtra("position", -1);

            if (updatedExpense != null) {
                // Update the existing expense
                expenses.set(deletedPosition, updatedExpense);
                expenseAdapter.notifyDataSetChanged();
            } else if (deletedPosition != -1) {
                // Delete the expense
                expenses.remove(deletedPosition);
                expenseAdapter.notifyDataSetChanged();
            }

            // Recalculate and update the total monthly charge
            updateTotalMonthlyCharge();
        }
    }
    private boolean isValidMonthFormat(String month) {
        // Define a regular expression pattern for yyyy-MM format
        String regexPattern = "^(19|20)\\d\\d-(0[1-9]|1[0-2])$";

        // Use the matches method to check if the input matches the pattern
        return month.matches(regexPattern);
    }

    private void showAddExpenseDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);
        dialogBuilder.setView(dialogView);

        final EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        final EditText monthEditText = dialogView.findViewById(R.id.monthEditText);
        final EditText chargeEditText = dialogView.findViewById(R.id.chargeEditText);
        final EditText commentEditText = dialogView.findViewById(R.id.commentEditText);

        dialogBuilder.setTitle("Add Expense");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Get user input
                String name = nameEditText.getText().toString().trim();
                String month = monthEditText.getText().toString().trim();
                String chargeText = chargeEditText.getText().toString().trim();
                String comment = commentEditText.getText().toString().trim();

                // Check constraints
                if (name.isEmpty() || name.length() > 15) {
                    showToast("Name must be non-empty and up to 15 characters.");
                    return;
                }

                if (!isValidMonthFormat(month)) {
                    showToast("Month must be in yyyy-MM format.");
                    return;
                }

                double charge;
                try {
                    charge = Double.parseDouble(chargeText);
                    if (charge < 0) {
                        showToast("Monthly charge must be non-negative.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showToast("Invalid monthly charge format.");
                    return;
                }

                if (comment.length() > 20) {
                    showToast("Comment must be up to 20 characters.");
                    return;
                }

                // Create a new Expense object
                Expense newExpense = new Expense(name, month, charge, comment);

                // Add the new expense to the list
                expenses.add(newExpense);
                expenseAdapter.notifyDataSetChanged();

                // Update the total monthly charge
                updateTotalMonthlyCharge();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancel the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    // Recalculate and update the total monthly charge
    private void updateTotalMonthlyCharge() {
        double totalMonthlyCharge = 0.0;
        for (Expense expense : expenses) {
            totalMonthlyCharge += expense.getMonthlyCharge();
        }
        // Update the TextView displaying the total charge
        totalChargeTextView.setText("Total Monthly Charge: $" + totalMonthlyCharge);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
