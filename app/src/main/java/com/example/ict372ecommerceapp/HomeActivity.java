package com.example.ict372ecommerceapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); //displays the XML layout

        RecyclerView rv = findViewById(R.id.rvProducts); //gets UI components by id
        rv.setLayoutManager(new LinearLayoutManager(this)); //makes the RecyclerView a vertical scrolling list

        ArrayList<Product> data = new ArrayList<>(); //creates sample product data
        data.add(new Product("Linen Chair", "$321"));
        data.add(new Product("Pearl Lamp", "$191"));
        data.add(new Product("Modern Chair", "$120"));
        data.add(new Product("Wood Table", "$450"));

        adapter = new ProductAdapter(data); //binds product data to list rows
        rv.setAdapter(adapter);

        AutoCompleteTextView etSearch = findViewById(R.id.etSearch); //gets the search input field

        // ✅ typed-ahead suggestions
        String[] suggestions = new String[]{"Linen Chair", "Pearl Lamp", "Modern Chair", "Wood Table"};
        ArrayAdapter<String> sugAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions
        );
        etSearch.setAdapter(sugAdapter); //sets the adapter for suggestions

        // ✅ Filter by input
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
        // ✅ Filter by selecting a suggestion
        etSearch.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            etSearch.dismissDropDown();         //hides the suggestions dropdown
            adapter.filter(selected);           //narrowing the list to the selected suggestion
        });
    }
}
