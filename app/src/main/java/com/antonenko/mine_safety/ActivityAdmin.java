package com.antonenko.mine_safety;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.antonenko.mine_safety.Helpers.User;
import com.antonenko.mine_safety.databinding.ActivityAdminBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityAdmin extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private ArrayList<String> listUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.text_testresult);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUsers = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    listUsers.add(Objects.requireNonNull(child.getValue(User.class)).getPIB());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityAdmin.this, android.R.layout.simple_list_item_1, listUsers);
                binding.listUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = binding.listUsers.getItemAtPosition(position);
                String PIB = (String) o;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("users").child(PIB).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Intent intent = new Intent(ActivityAdmin.this, ActivityResult.class);
                        intent.putExtra("countWrong", user.getWrong_answer());
                        intent.putExtra("countRight", user.getRight_answer());
                        intent.putExtra("countAll", user.getTotal_questions());
                        intent.putExtra("countTime", user.getTime());
                        intent.putExtra("PIB", user.getPIB());
                        intent.putExtra("viewBtn", false);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tests:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Вхід до редагування тесту");


                final EditText input = new EditText(this);

                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().equals("000100111100")){
                            Intent i = new Intent(getBaseContext(), ActivityAddEdit.class);
                            startActivity(i);
                        }else{
                            Snackbar.make(findViewById(R.id.textTitleAdmin).getRootView(), getString(R.string.error_password), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            case R.id.menu_logout:
                Intent i = new Intent(this, authorization.class);
                this.startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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