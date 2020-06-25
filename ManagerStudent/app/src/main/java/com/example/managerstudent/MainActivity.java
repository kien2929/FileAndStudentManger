package com.example.managerstudent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private EditText editText;
    private Button buttonSearch;
    private Button buttonInsert;

    private List<Student> items;
    private MyDataBaseHelper db;
    private StudentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        db = new MyDataBaseHelper(this);
        db.delete("20160723");
        items = db.getAllNote();
        adapter = new StudentAdapter(items);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    private void findView(){
        listView = findViewById(R.id.listview);
        editText = findViewById(R.id.edt_input);
        buttonSearch = findViewById(R.id.btn_search);
        buttonInsert = findViewById(R.id.btn_insert);

        buttonInsert.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_search){
            String data = editText.getText().toString();
            items = db.search(data);
            adapter.updateData(items);

            editText.getText().clear();
        }
        if(v.getId() == R.id.btn_insert){
            showInputAlertDialog();
        }
    }

    public void showInputAlertDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Input information");
        dialog.setCancelable(true);

        View dialogView = this.getLayoutInflater().inflate(R.layout.layout_input, null);

        final EditText name = dialogView.findViewById(R.id.edt_name);
        final EditText address = dialogView.findViewById(R.id.edt_address);
        final EditText mssv = dialogView.findViewById(R.id.edt_mssv);
        final EditText date = dialogView.findViewById(R.id.edt_date);
        final EditText email = dialogView.findViewById(R.id.edt_email);

        dialog.setView(dialogView);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), name.getText().toString(), Toast.LENGTH_SHORT).show();

                Student student = new Student(mssv.getText().toString(),
                        name.getText().toString(),
                        date.getText().toString(),
                        email.getText().toString(),
                        address.getText().toString());

                db.addStudent(student);
                items = db.getAllNote();
                adapter.updateData(items);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void showConfirmDeleteDialog(final String mssv){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm Delete");
        dialog.setMessage("Do you want to delete?");

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete(mssv);
                items = db.getAllNote();
                adapter.updateData(items);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        showConfirmDeleteDialog(items.get(position).getMSSV());
        return super.onContextItemSelected(item);
    }
}
