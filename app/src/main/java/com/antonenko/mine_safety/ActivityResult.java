package com.antonenko.mine_safety;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.antonenko.mine_safety.Helpers.User;
import com.antonenko.mine_safety.databinding.ActivityResultBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityResult extends AppCompatActivity {
    private ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setTitle(getResources().getText(R.string.test_result));

        if (!getIntent().getBooleanExtra("viewBtn", true)){
            binding.btnConfirm.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        binding.countRight.setText(getResources().getText(R.string.countRight).toString() + getIntent().getIntExtra("countRight", -100));
        binding.PIB.setText(getResources().getText(R.string.PIB).toString() + getIntent().getStringExtra("PIB"));
        binding.countWrong.setText(getResources().getText(R.string.countWrong).toString() + getIntent().getIntExtra("countWrong", -99));
        binding.countAll.setText(getResources().getText(R.string.countAll).toString() + getIntent().getIntExtra("countAll", -98));
        binding.countTime.setText(getResources().getText(R.string.countTime).toString() + getIntent().getStringExtra("countTime"));

        binding.btnConfirm.setOnClickListener(this::onSubmitClick);
    }

    @Override
    protected void onDestroy() {

        User user = new User(getIntent().getStringExtra("PIB"), getIntent().getStringExtra("countTime"), getIntent().getIntExtra("countRight", -100), getIntent().getIntExtra("countWrong", -99), getIntent().getIntExtra("countAll", -98));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(user.getPIB()).setValue(user);
        super.onDestroy();
    }

    private void onSubmitClick(View view) {
        User user = new User(getIntent().getStringExtra("PIB"), getIntent().getStringExtra("countTime"), getIntent().getIntExtra("countRight", -100), getIntent().getIntExtra("countWrong", -99), getIntent().getIntExtra("countAll", -98));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(user.getPIB()).setValue(user);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityResult.this);
        builder.setTitle(R.string.text_test);
        builder.setMessage(R.string.text_test_submit_success);
        builder.setIcon(R.drawable.ic_check);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), authorization.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (!getIntent().getBooleanExtra("viewBtn", true)){
            super.onBackPressed();
            return;
        }else
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), getText(R.string.exit_message), Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}