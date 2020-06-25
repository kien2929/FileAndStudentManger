package com.example.managerstudent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;


public class StudentAdapter extends BaseAdapter {

    List<Student> items;

    public StudentAdapter(List<Student> items){
        this.items = items;
    }


    @Override
    public int getCount() {
       return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);

        TextView name = view.findViewById(R.id.tv_name);
        TextView address = view.findViewById(R.id.tv_address);
        TextView email = view.findViewById(R.id.tv_email);
        TextView date = view.findViewById(R.id.tv_date);
        TextView mssv = view.findViewById(R.id.tv_mssv);

        final  Student student = items.get(position);

        name.setText(student.getName());
        address.setText(student.getAddress());
        email.setText(student.getEmail());
        date.setText(student.getDateOfBirth());
        mssv.setText(student.getMSSV());
        return view;
    }

    // up date du lieu moi
    public void updateData(List<Student> items){
        this.items = items;
        notifyDataSetChanged();
    }
}
