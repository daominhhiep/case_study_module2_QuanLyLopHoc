package com.example.quanlysinhvien;

import android.content.DialogInterface;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewStudent;
    List<Student> listStudent;
    StudentAdapter adapter;
    Button buttonAdd, buttonEdit, buttonSort;
    EditText editTextId, editTextName, editTextBirth, editTextAddress, editTextGpa;
    RadioGroup groupGender;
    RadioButton checkMale, checkFemale;
    private static final String simpleFileName = "note.txt";
    int location = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listStudent = read();
        initView();
        list();
        adapter = new StudentAdapter(this, R.layout.row_student, listStudent);
        listViewStudent.setAdapter(adapter);

        onCheckGender();
        onClickButtonAdd();
        onClickButtonEdit();
        onClickButtonSort();
        onClickListView();
        onLongClickListView();
    }

    private void onCheckGender() {
        groupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.checkMale:
                        break;
                    case R.id.checkFemale:
                        break;
                }
            }
        });
    }

    private int getGender() {
        int gender = 0;
        if (checkMale.isChecked()) {
            gender = R.drawable.male;
        }
        if (checkFemale.isChecked()) {
            gender = R.drawable.female;
        }
        return gender;
    }

    private void onClickListView() {
        listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editTextId.setText(listStudent.get(i).getId());
                editTextName.setText(listStudent.get(i).getName());
                editTextBirth.setText(listStudent.get(i).getBirth());
                editTextAddress.setText(listStudent.get(i).getAddress());
                editTextGpa.setText(listStudent.get(i).getGpa());
                location = i;
            }
        });
    }

    private void onLongClickListView() {
        listViewStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                confirmDelete(i);
                return false;
            }
        });
    }

    private void confirmDelete(final int location) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông Báo");
        alertDialog.setMessage("Bạn có muốn xóa thông tin sinh viên này không?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listStudent.remove(location);
                write(listStudent);
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.notifyDataSetChanged();
            }
        });
        alertDialog.show();
    }


    private void onClickButtonSort() {
        buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuSort();
            }
        });
    }


    private void showMenuSort() {
        PopupMenu popupMenu = new PopupMenu(this, buttonSort);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sortById:
                        Collections.sort(listStudent, new SortById());
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.sortByGpa:
                        Collections.sort(listStudent, new SortByGpa());
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void onClickButtonAdd() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                String name = editTextName.getText().toString();
                String birth = editTextBirth.getText().toString();
                String address = editTextAddress.getText().toString();
                String gpa = editTextGpa.getText().toString();
                int gender = getGender();
                listStudent.add(new Student(id, name, birth, address, gpa, gender));
                write(listStudent);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void onClickButtonEdit() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                String name = editTextName.getText().toString();
                String birth = editTextBirth.getText().toString();
                String address = editTextAddress.getText().toString();
                String gpa = editTextGpa.getText().toString();
                int gender = getGender();
                listStudent.add(new Student(id, name, birth, address, gpa, gender));
                listStudent.remove(location);
                write(listStudent);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        buttonSort = (Button) findViewById(R.id.buttonSort);
        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextBirth = (EditText) findViewById(R.id.editTextBirth);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextGpa = (EditText) findViewById(R.id.editTextGpa);
        groupGender = (RadioGroup) findViewById(R.id.checkGender);
        checkMale = (RadioButton) findViewById(R.id.checkMale);
        checkFemale = (RadioButton) findViewById(R.id.checkFemale);
    }

    private void list() {
        listViewStudent = (ListView) findViewById(R.id.listViewStudent);
        listStudent = read();
    }

    private void write(List<Student> studentList) {
        try {
            FileOutputStream fileOutputStream = this.openFileOutput(simpleFileName, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(studentList);
            objectOutputStream.close();
            System.out.println("luu thanh cong");

            Toast.makeText(this,"sjfshdfjsd",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<Student> read() {
        List<Student> studentList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = this.openFileInput(simpleFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            studentList = (List<Student>) objectInputStream.readObject();
            objectInputStream.close();

            Toast.makeText(this,studentList.get(studentList.size()-1).getName() + studentList.get(studentList.size()-1).getGpa(),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return studentList;
    }
}