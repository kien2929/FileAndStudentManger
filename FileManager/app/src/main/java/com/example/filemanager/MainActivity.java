package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<File> items;
    private String path;
    private String rootPath;
    private TextView tvPath;
    private FileAdapter adapter;
    private boolean isFile = true;
    private AlertDialog.Builder confirmDelete, confirmRename, inputNewName;
    private EditText input;
    private String newName, oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        createConfirmAlertBuilder();

        // init
        path = Environment.getExternalStorageDirectory().toString();
        rootPath = Environment.getExternalStorageDirectory().toString();
        tvPath.setText(path);

        items = getListFile(rootPath);
        adapter = new FileAdapter(items);
        listView.setAdapter(adapter);
        setOnClickListView();
        setLongClickListView();
        registerForContextMenu(listView);
    }


    private void findView(){
        listView = findViewById(R.id.lv_file);
        tvPath = findViewById(R.id.tv_path);
    }

    @Override
    public void onBackPressed() {
        if(path.compareTo(rootPath) == 0){
            super.onBackPressed();
        }
        else {
            File file = new File(path);
            path = file.getParent();
            items = getListFile(path);
            adapter.updateData(items);
            tvPath.setText(path);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub


        if(isFile){
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.file_context_menu, menu);
        }
        else {
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.folder_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getTitle().toString()){
            case "Rename File":
            case "Rename Folder":
                showRenameAlertDialog();
                break;
            case "Delete File":
            case "Delete Folder":
                AlertDialog confirmDeleteFile = confirmDelete.create();
                confirmDeleteFile.show();
                break;
            case "Copy File":
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getTitle().toString()){
            case "Create Folder":
                showCreateFolderAlertDialog();
                break;
            case "Create File":
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<File> getListFile(String path){
        List<File> res = new ArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++)
            res.add(new File(path + "/" + files[i].getName()));
        return res;
    }

    private void setOnClickListView(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();
                String folder = items.get(position).getName();
                path = path + "/" + folder;
                File file = new File(path);

                if (file.isDirectory()) {
                    items = getListFile(path);
                    adapter.updateData(items);
                    tvPath.setText(path);
                } else {
                    if (folder.toLowerCase().contains(".bmp")
                            || folder.toLowerCase().contains(".jpg")
                            || folder.toLowerCase().contains(".png")) {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri,"image/*");
                        startActivity(intent);
                    }
                    if(folder.toLowerCase().contains(".txt")){
                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri, "text/plain");
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void setLongClickListView(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String folder = items.get(position).getName();
                File file = new File(path + "/" + folder);
                oldName = folder;

                if (file.isDirectory()) {
                   isFile = false;
                } else {
                   isFile = true;
                }
                return false;
            }
        });
    }

    private void createConfirmAlertBuilder(){
        // set up confirm Delete builder
        confirmDelete = new AlertDialog.Builder(this);
        confirmDelete.setTitle("Delete");
        confirmDelete.setCancelable(true);
        confirmDelete.setMessage("Do you want to delete Folder?");
        confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File oldFile = new File(path , oldName);
                if(oldFile.delete()){
                    Toast.makeText(getApplicationContext(), "delete successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "delete failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // set up confirm Rename builder
        confirmRename = new AlertDialog.Builder(this);
        confirmRename.setTitle("Rename");
        confirmRename.setCancelable(true);
        confirmRename.setMessage("Do you want to rename Folder?");
        confirmRename.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File oldFile = new File(path , oldName);
                File newFile = new File(path , newName);

                if(oldFile.renameTo(newFile)){
                    Toast.makeText(getApplicationContext(), "Rename file success", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Rename file failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmRename.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    private void showRenameAlertDialog(){
        // set up input new name
        input = new EditText(this);
        inputNewName = new AlertDialog.Builder(this);
        inputNewName.setTitle("Rename");
        inputNewName.setCancelable(true);
        inputNewName.setMessage("Enter new name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        inputNewName.setView(input);

        // Set up the buttons
        inputNewName.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), input.getText().toString(), Toast.LENGTH_SHORT).show();
                newName = input.getText().toString();
                AlertDialog confirmRenameFile = confirmRename.create();
                confirmRenameFile.show();
            }
        });
        inputNewName.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        inputNewName.show();
    }

    private void showCreateFolderAlertDialog(){
        // set up input new name
        input = new EditText(this);
        final AlertDialog.Builder inputNewFolder = new AlertDialog.Builder(this);
        inputNewFolder.setTitle("Create Folder");
        inputNewFolder.setCancelable(true);
        inputNewFolder.setMessage("Enter name new folder:");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        inputNewFolder.setView(input);

        // Set up the buttons
        inputNewFolder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newFolder = input.getText().toString();
                File directory  = new File(path, newFolder);
                if(directory.mkdirs()){
                    Toast.makeText(getApplicationContext(), "make dir successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "make dir failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        inputNewFolder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        inputNewFolder.show();
    }
}
