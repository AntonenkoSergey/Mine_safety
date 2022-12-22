package com.antonenko.mine_safety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.antonenko.mine_safety.Helpers.TestData;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class authorization extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        setTitle(getResources().getText(R.string.text_auth));
        EditText login = (EditText) findViewById(R.id.AuthFieldLogin);
        Button btnAuth = (Button) findViewById(R.id.btnAuth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().equals("")){
                    Snackbar.make(login.getRootView(), getText(R.string.error_PIB), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (login.getText().toString().contains(".") ||
                    login.getText().toString().contains("#") ||
                    login.getText().toString().contains("$") ||
                    login.getText().toString().contains("[") ||
                    login.getText().toString().contains("]") ||
                    login.getText().toString().contains("@")
                ){
                    Snackbar.make(login.getRootView(), "ПІБ не може містисти у собі '.', '#', '$', '@', '[', або ']'", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (login.getText().toString().equals("ADMIN")){
                    Intent intent = new Intent(getBaseContext(), ActivityAdmin.class);
                    intent.putExtra("PIB", login.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("test").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            TestData test = snapshot.getValue(TestData.class);
                            if (test!=null){
                                Intent intent = new Intent(getBaseContext(), ActivityTest.class);
                                intent.putExtra("PIB", login.getText().toString());
                                startActivity(intent);
                                finish();
                            }else{
                                Snackbar.make(login.getRootView(), R.string.text_test_not_ready, Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
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