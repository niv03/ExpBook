package com.example.nivit_expbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    private int selectedPosition = -1; // Default to no selection

    public ExpenseAdapter(Context context, ArrayList<Expense> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Expense expense = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_item, parent, false);
        }
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView chargeTextView = convertView.findViewById(R.id.chargeTextView);

        // Set values for name, date, and charge TextViews
        nameTextView.setText(expense.getName());
        dateTextView.setText(expense.getMonth());
        chargeTextView.setText(String.valueOf(expense.getMonthlyCharge()));

        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged(); // Refresh the ListView to update the selected item's background
    }
}
