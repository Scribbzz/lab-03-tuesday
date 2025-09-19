package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(City city); // 🔹 add new callback
    }

    private AddCityDialogListener listener;
    private City cityToEdit; // 🔹 store the city being edited

    // 🔹 Factory method for editing
    static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.content, null);

        EditText editCityName = view.findViewById(R.id.city_text);
        EditText editProvinceName = view.findViewById(R.id.province_text);

        // 🔹 Check if editing
        if (getArguments() != null) {
            cityToEdit = (City) getArguments().getSerializable("city");
            if (cityToEdit != null) {
                editCityName.setText(cityToEdit.getName());
                editProvinceName.setText(cityToEdit.getProvince());
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        if (cityToEdit == null) {
            // Adding a new city
            builder.setTitle("Add a City")
                    .setPositiveButton("Add", (dialog, which) -> {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        listener.addCity(new City(cityName, provinceName));
                    });
        } else {
            // Editing an existing city
            builder.setTitle("Edit City")
                    .setPositiveButton("Save", (dialog, which) -> {
                        cityToEdit.setName(editCityName.getText().toString());
                        cityToEdit.setProvince(editProvinceName.getText().toString());
                        listener.editCity(cityToEdit);
                    });
        }

        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }
}
