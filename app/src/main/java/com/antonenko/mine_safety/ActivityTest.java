package com.antonenko.mine_safety;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.antonenko.mine_safety.Helpers.Question;
import com.antonenko.mine_safety.Helpers.TestData;
import com.antonenko.mine_safety.databinding.ActivityTestBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityTest extends AppCompatActivity {
    private static final String KEY_C_QUESTION = "C_QUESTION";
    private static final String KEY_COUNT_RIGHT = "COUNT_RIGHT";
    private static final String KEY_COUNT_TIME = "COUNT_TIME";
    private static final String KEY_CHECKED_RADIO = "CHECKED_RADIO";
    private static final String KEY_TIMER_TIME = "TIMER_TIME";

    private int c_question = 0;
    private int count_right = 0;
    private long count_time;
    private long timer_time = 1801000;
    private String checked_answer;
    private TestData test;

    private ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (savedInstanceState != null) {
            c_question = savedInstanceState.getInt(KEY_C_QUESTION, 0);
            count_right = savedInstanceState.getInt(KEY_COUNT_RIGHT, 0);
            count_time = savedInstanceState.getLong(KEY_COUNT_TIME, 0);
            checked_answer = savedInstanceState.getString(KEY_CHECKED_RADIO, null);
        }

        setTitle(getResources().getText(R.string.text_testing));

        binding.btnNext.setOnClickListener(this::onNextClick);
        binding.btnNext.setEnabled(false);

        CountDownTimer timer = new CountDownTimer(timer_time-count_time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(getDate(millisUntilFinished, "mm:ss"));
                count_time+=1000;
            }

            @Override
            public void onFinish() {
                FinishTest();
            }
        };

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("test").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                test = (snapshot.getValue(TestData.class));
                binding.progressTest.setMax(test.getQuestions().size());
                timer.start();
                VisualizeQuestion(test.getQuestions().get(c_question));
                binding.btnNext.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_C_QUESTION, c_question);
        outState.putInt(KEY_COUNT_RIGHT, count_right);
        outState.putLong(KEY_COUNT_TIME, count_time);
        RadioButton radioButton = (RadioButton) findViewById(binding.radioGroup.getCheckedRadioButtonId());
        outState.putString(KEY_CHECKED_RADIO, (radioButton!=null)?radioButton.getText().toString():"UNKNOWN");
    }

    private long lastAns = 0;
    @SuppressLint("ResourceType")
    private void onNextClick(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getText(R.string.ChoseAnswer), Toast.LENGTH_LONG);
        if (binding.radioGroup.getCheckedRadioButtonId() == -1 || binding.radioGroup.getCheckedRadioButtonId() == lastAns){
            toast.show();
            return;
        }
        lastAns = binding.radioGroup.getCheckedRadioButtonId();
        RadioButton selected_answer = (RadioButton) findViewById(binding.radioGroup.getCheckedRadioButtonId());
        int right_answer = test.getQuestions().get(c_question).right_answer;
        if (selected_answer.getText().equals(test.getQuestions().get(c_question).answers.get(right_answer-1))){
            count_right++;
        }
        if (c_question+1 == test.getQuestions().size()-1){
            binding.btnNext.setText(R.string.test_end);
        }
        if (c_question+1 != test.getQuestions().size()){
            c_question++;
            VisualizeQuestion(test.getQuestions().get(c_question));
        }else{
            FinishTest();
        }

    }
    private void FinishTest(){
        Intent intent = new Intent(getBaseContext(), ActivityResult.class);
        intent.putExtra("countWrong", test.getQuestions().size()-count_right);
        intent.putExtra("countRight", count_right);
        intent.putExtra("countAll", test.getQuestions().size());
        intent.putExtra("countTime", getDate(count_time, "mm:ss"));
        intent.putExtra("PIB", getIntent().getStringExtra("PIB"));
        startActivity(intent);
        finish();
    }


    public  String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void VisualizeQuestion(Question question){
        binding.radioGroup.removeAllViews();
        binding.testQuestion.setText(question.question);
        binding.progressTest.setProgress(c_question);
        for (String answer : question.answers) {
            RadioButton radioBtn = new RadioButton(this);
            int id_radio = View.generateViewId();
            radioBtn.setId(id_radio);
            radioBtn.setText(answer);
            binding.radioGroup.addView(radioBtn);
            if (answer.equals(checked_answer)) binding.radioGroup.check(id_radio);
        }
    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), getText(R.string.exit_message), Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

}