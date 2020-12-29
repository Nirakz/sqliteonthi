package com.example.onthicuoiki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper db;

    private List<Person> listPerson;
    PersonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        db = new DatabaseHelper(this);

        listPerson = new ArrayList<>();
        listPerson = db.getAll();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPersonDialog(null, -1);
            }
        });

        mAdapter = new PersonAdapter(this, listPerson);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showPersonDialog(listPerson.get(position), position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showDeleteDialog(listPerson.get(position), position);
            }
        }));

    }

    private void create(String name, String mail) {
        long id = db.create(name, mail);
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setEmail(mail);
        listPerson.add(0, person);
        mAdapter.notifyDataSetChanged();
    }

    private void update(int position, long id, String name, String mail) {
        db.update(id, name, mail);
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setEmail(mail);
        listPerson.set(position, person);
        mAdapter.notifyItemChanged(position);
    }

    private void delete(int position, long id) {
        db.delete(id);
        listPerson.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    private void showPersonDialog(final Person person, final int position) {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.dialog_person, null);
        final EditText edtName = promptsView.findViewById(R.id.edtName);
        final EditText edtEmail = promptsView.findViewById(R.id.edtEmail);
        if (person != null) {
            edtName.setText(person.getName());
            edtEmail.setText(person.getEmail());
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("XÁC NHẬN",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (person == null) {
                                    create(edtName.getText().toString(), edtEmail.getText().toString());
                                } else {
                                    update(position, person.getId(), edtName.getText().toString(), edtEmail.getText().toString());
                                }
                            }
                        })
                .setNegativeButton("HỦY BỎ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void showDeleteDialog(final Person person, final int position) {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.dialog_delete, null);
        final TextView tvName = promptsView.findViewById(R.id.tvName);
        tvName.setText("Bạn muốn xóa thông tin người dùng [" + person.getName().toUpperCase() + "] không?");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("XÁC NHẬN",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete(position, person.getId());
                            }
                        })
                .setNegativeButton("HỦY BỎ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}