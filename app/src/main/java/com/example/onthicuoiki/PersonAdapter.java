package com.example.onthicuoiki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder> {

    private Context context;
    private List<Person> listPerson;

    public PersonAdapter(Context context, List<Person> listPerson) {
        this.context = context;
        this.listPerson = listPerson;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Person person = listPerson.get(position);
        holder.tvName.setText(person.getName());
        holder.tvMail.setText(person.getEmail());
    }

    @Override
    public int getItemCount() {
        return listPerson.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvMail;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvMail = view.findViewById(R.id.tvMail);
        }

    }
}