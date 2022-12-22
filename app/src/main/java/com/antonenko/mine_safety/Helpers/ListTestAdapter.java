package com.antonenko.mine_safety.Helpers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antonenko.mine_safety.ActivityAddEdit;
import com.antonenko.mine_safety.ActivityNewTest;
import com.antonenko.mine_safety.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

//класс адаптер для списку історії
public class ListTestAdapter extends RecyclerView.Adapter<ListTestAdapter.ListTestViewHolder>{
    //оголошення змінних
    private List<Question> list;
    private ActivityAddEdit context;
    private final int MENU_DELETE = 1;
    //конструктори

    public ListTestAdapter(ActivityAddEdit ct, List<Question> list_of_place){
        this.context = ct;
        this.list = list_of_place;
    }

    @NonNull
    @Override
    public ListTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //створення вью для елемента списка
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_test_item, parent, false);
        return new ListTestViewHolder(view);
    }

    @Override //заповнення елемента списка даними
    public void onBindViewHolder(@NonNull ListTestViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(list.get(position).question);
        holder.btnDelete.setImageResource(R.drawable.ic_minus);
        holder.btnEdit.setImageResource(R.drawable.ic_pencil);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("test").child("questions").child(String.valueOf(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Question question = snapshot.getValue(Question.class);
                        ActivityNewTest.editor = true;
                        ActivityNewTest.question_number = position;
                        ActivityNewTest.edit_question = question;
                        Intent intent = new Intent(context, ActivityNewTest.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, R.string.text_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = builder(context, position).create();
                dialog.show();
            }
        });
    }
    //метод створення діалогового вікна видалення місця з історії
    private AlertDialog.Builder builder(Context c, int p){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.text_delete);  // заголовок
        builder.setMessage(R.string.delete_question_ask); // сообщение
        builder.setIcon(R.drawable.ic_trashcan);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("test").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TestData testData = snapshot.getValue(TestData.class);
                        testData.getQuestions().remove(p);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("test").setValue(testData);
                        list.remove(p);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, R.string.text_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setCancelable(true);

        return builder;
    }

    @Override // метод повернення кількості елементів в списку
    public int getItemCount() {
        return list.size();
    }

    //знаходження елементів інтерфейсу
    public class ListTestViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, category;
        ImageButton btnDelete;
        ImageButton btnEdit;
        ConstraintLayout constraintLayout;
        public ListTestViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.list_item_question);
            btnDelete = itemView.findViewById(R.id.listBtnDelete);
            btnEdit = itemView.findViewById(R.id.listBtnEdit);
        }
    }
}
