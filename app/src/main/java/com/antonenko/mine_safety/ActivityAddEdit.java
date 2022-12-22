package com.antonenko.mine_safety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.antonenko.mine_safety.Helpers.ListTestAdapter;
import com.antonenko.mine_safety.Helpers.TestData;
import com.antonenko.mine_safety.databinding.ActivityAddEditBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ActivityAddEdit extends AppCompatActivity {
    private ActivityAddEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(R.string.text_tests);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.btnAddTest.setOnClickListener(this::onAddClick);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("test").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TestData test = (snapshot.getValue(TestData.class));
                if (test==null){
                    test = new TestData();
                    test.setQuestions(new ArrayList<>());
                }
                ListTestAdapter adapter = new ListTestAdapter(ActivityAddEdit.this, test.getQuestions());
                binding.listQuestions.setAdapter(adapter);
                binding.listQuestions.setLayoutManager(new LinearLayoutManager(ActivityAddEdit.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void onAddClick(View view) {
        ActivityNewTest.editor = false;
        Intent intent = new Intent(this, ActivityNewTest.class);
        startActivity(intent);
    }
}