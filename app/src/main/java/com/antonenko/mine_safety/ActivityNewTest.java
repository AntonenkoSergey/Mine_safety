package com.antonenko.mine_safety;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.antonenko.mine_safety.Helpers.Question;
import com.antonenko.mine_safety.Helpers.TestData;
import com.antonenko.mine_safety.databinding.ActivityNewTestBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityNewTest extends AppCompatActivity {
    private ActivityNewTestBinding binding;
    public static Question edit_question;
    public static int question_number;
    public static boolean editor;

    private static final String KEY_QUESTION = "QUESTION";
    private static final String KEY_ANSONE = "ANSONE";
    private static final String KEY_ANSTWO = "ANSTWO";
    private static final String KEY_ANSTHRE = "ANSTHRE";
    private static final String KEY_ANSFOUR = "ANSFOUR";
    private static final String KEY_ANSRIGHT = "ANSRIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null){
            binding.textQuestion.setText(savedInstanceState.getString(KEY_QUESTION, ""));
            binding.textOneAnswer.setText(savedInstanceState.getString(KEY_ANSONE, ""));
            binding.textTwoAnswer.setText(savedInstanceState.getString(KEY_ANSTWO, ""));
            binding.textThreeAnswer.setText(savedInstanceState.getString(KEY_ANSTHRE, ""));
            binding.textFourAnswer.setText(savedInstanceState.getString(KEY_ANSFOUR, ""));
            binding.numberRightAnswer.setText(savedInstanceState.getString(KEY_ANSRIGHT, ""));
        }

        if (editor){
            setTitle(R.string.text_edit_q);
            binding.btnAdd.setOnClickListener(this::onBtnEditClick);
            binding.textQuestion.setText(edit_question.getQuestion());
            binding.textOneAnswer.setText(edit_question.getAnswers().get(0));
            binding.textTwoAnswer.setText(edit_question.getAnswers().get(1));
            if (edit_question.getAnswers().size()>2)  binding.textThreeAnswer.setText(edit_question.getAnswers().get(2));
            if (edit_question.getAnswers().size()>3)  binding.textFourAnswer.setText(edit_question.getAnswers().get(3));
            binding.numberRightAnswer.setText(String.valueOf(edit_question.getRight_answer()));
            binding.btnAdd.setText(R.string.text_edit);
        }else{
            setTitle(R.string.text_add_new_question);
            binding.btnAdd.setText(R.string.text_add);
            binding.btnAdd.setOnClickListener(this::onBtnAddClick);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(KEY_QUESTION, binding.textQuestion.getText().toString());
        outState.putString(KEY_ANSONE, binding.textOneAnswer.getText().toString());
        outState.putString(KEY_ANSTWO, binding.textTwoAnswer.getText().toString());
        outState.putString(KEY_ANSTHRE, binding.textThreeAnswer.getText().toString());
        outState.putString(KEY_ANSFOUR, binding.textFourAnswer.getText().toString());
        outState.putString(KEY_ANSRIGHT, binding.numberRightAnswer.getText().toString());
    }

    private boolean check(){
        if (binding.textQuestion.getText().toString().equals("")){
            Snackbar.make(binding.getRoot(), "Введіть текст запитання", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (binding.textOneAnswer.getText().toString().equals("")){
            Snackbar.make(binding.getRoot(), "Введіть перший варіант відповіді", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (binding.textTwoAnswer.getText().toString().equals("")){
            Snackbar.make(binding.getRoot(), "Введіть другий варіант відповіді", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (binding.numberRightAnswer.getText().toString().equals("")){
            Snackbar.make(binding.getRoot(), "Введіть правильний варіант відповіді", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (binding.textThreeAnswer.getText().toString().equals("") && Integer.parseInt(binding.numberRightAnswer.getText().toString()) > 2){
            Snackbar.make(binding.getRoot(), "Такого варіанту відповіді не має", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (binding.textFourAnswer.getText().toString().equals("") && Integer.parseInt(binding.numberRightAnswer.getText().toString()) > 3){
            Snackbar.make(binding.getRoot(), "Такого варіанту відповіді не має", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void onBtnEditClick(View view) {
        if (!check()) return;

        Question question = new Question();
        question.setQuestion(binding.textQuestion.getText().toString());
        question.setAnswers(new ArrayList<>());
        question.getAnswers().add(binding.textOneAnswer.getText().toString());
        question.getAnswers().add(binding.textTwoAnswer.getText().toString());
        if (!binding.textThreeAnswer.getText().toString().equals("")) question.getAnswers().add(binding.textThreeAnswer.getText().toString());
        if (!binding.textFourAnswer.getText().toString().equals("")) question.getAnswers().add(binding.textFourAnswer.getText().toString());
        question.setRight_answer(Integer.parseInt(binding.numberRightAnswer.getText().toString()));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("test").child("questions").child(String.valueOf(question_number)).setValue(question).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityNewTest.this);
                builder.setTitle(R.string.text_edit);
                builder.setMessage(getResources().getText(R.string.text_complete));
                builder.setIcon(R.drawable.ic_check);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityNewTest.this.onBackPressed();
                        finish();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void onBtnAddClick(View view) {
        if (!check()) return;

        Question question = new Question();
        question.setQuestion(binding.textQuestion.getText().toString());
        question.setAnswers(new ArrayList<>());
        question.getAnswers().add(binding.textOneAnswer.getText().toString());
        question.getAnswers().add(binding.textTwoAnswer.getText().toString());
        if (!binding.textThreeAnswer.getText().toString().equals("")) question.getAnswers().add(binding.textThreeAnswer.getText().toString());
        if (!binding.textFourAnswer.getText().toString().equals("")) question.getAnswers().add(binding.textFourAnswer.getText().toString());
        question.setRight_answer(Integer.parseInt(binding.numberRightAnswer.getText().toString()));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("test").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TestData testData = snapshot.getValue(TestData.class);
                if (testData==null){
                    testData = new TestData(new ArrayList<>());
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("test").child("questions").child(String.valueOf(testData.getQuestions().size())).setValue(question);

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityNewTest.this);
                builder.setTitle("Додавання");
                builder.setMessage("Запитання додано");
                builder.setIcon(R.drawable.ic_check);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), ActivityAddEdit.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}