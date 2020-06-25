package com.example.filemanager;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FileAdapter extends BaseAdapter {

    private List<File> items;
    private SimpleDateFormat sdf;


    public FileAdapter(List<File> items) {
        this.items = items;
        sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.CANADA);
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        TextView name = view.findViewById(R.id.tv_name);
        TextView time = view.findViewById(R.id.tv_time);
        ImageView icon = view.findViewById(R.id.imv_icon);

        final File item =  items.get(position);


        name.setText(item.getName());
        time.setText(sdf.format(item.lastModified()));
        if(item.isDirectory())
            icon.setImageResource(R.drawable.ic_folder);
        else
            icon.setImageResource(R.drawable.ic_file);

        final View finalView = view;

        return view;
    }

    // up date du lieu moi
    public void updateData(List<File> items){
        this.items = items;
        notifyDataSetChanged();
    }

}