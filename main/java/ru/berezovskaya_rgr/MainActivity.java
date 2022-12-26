package ru.berezovskaya_rgr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ru.berezovskaya_rgr.list.List;
import ru.berezovskaya_rgr.list.UserFactory;
import ru.berezovskaya_rgr.list.builder.DoubleObjectBuilder;
import ru.berezovskaya_rgr.list.builder.PolarVectorObjectBuilder;
import ru.berezovskaya_rgr.list.builder.UserTypeBuilder;

public class MainActivity extends AppCompatActivity {

    public UserFactory userFactory;
    public UserTypeBuilder builder;
    public List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userFactory = new UserFactory();
        builder = new DoubleObjectBuilder();
        list = new List();

        RadioButtonInitialization();
        ButtonsInitialization();

    }

    public void RadioButtonInitialization(){

        ListView listView = findViewById(R.id.listView);
        RadioGroup rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonPolar:
                        builder = new PolarVectorObjectBuilder();
                        list = new List();

                        break;
                    case R.id.radioButtonDouble:
                        builder = new DoubleObjectBuilder();
                        list = new List();
                        break;
                }

                listAdapter();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.saveMenu:
                saveList();
                return true;
            case R.id.loadMenu:
                loadList();
                return true;
            case R.id.sortMenu:
                sortList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortList() {
        list.sort(builder.getComparator());
        listAdapter();
    }

    public void ButtonsInitialization(){
        Button insertBtn = findViewById(R.id.insertBtn);
        Button removeBtn = findViewById(R.id.RemoveBtn);
        Button insertByIdBtn = findViewById(R.id.insertByIdBtn);
        Button deleteByIdBtn = findViewById(R.id.deleteByIdBtn);

        insertBtn.setOnClickListener((view)->{
            list.add(builder.create());
            listAdapter();
        });

        removeBtn.setOnClickListener((view)->{
            if(list.size() > 0) {
                list.remove(list.size()-1);
                listAdapter();
            }
        });

        insertByIdBtn.setOnClickListener(v->{
            EditText insertByIdField = (EditText) findViewById(R.id.addEditText);
            if (insertByIdField.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "Введите индекс для вставки!", Toast.LENGTH_LONG).show();
            } else {
                if (list.get(Integer.parseInt(String.valueOf(insertByIdField.getText()))) == null) {
                    Toast.makeText(getBaseContext(), "Введите правильный индекс для вставки!", Toast.LENGTH_LONG).show();
                } else {
                    list.add(builder.create(), Integer.parseInt(String.valueOf(insertByIdField.getText())));
                    listAdapter();
                }
            }

        });

        deleteByIdBtn.setOnClickListener(v->{
            EditText deleteByIdField = (EditText) findViewById(R.id.deleteEditText);
            if (deleteByIdField.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "Введите индекс для удаления!", Toast.LENGTH_LONG).show();
            } else {
                if (list.get(Integer.parseInt(String.valueOf(deleteByIdField.getText()))) == null) {
                    Toast.makeText(getBaseContext(), "Введите правильный индекс для удаления!", Toast.LENGTH_LONG).show();
                } else {
                    list.remove(Integer.parseInt(String.valueOf(deleteByIdField.getText())));
                    listAdapter();
                }
            }

        });

    }

    private void loadList() {
        BufferedReader bufferedReader;
        try {
            Log.d("MY_TAG", builder.typeName());
            if (builder.typeName().equals("Double")) {
                bufferedReader = new BufferedReader(new InputStreamReader((openFileInput("double.txt"))));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader((openFileInput("polar.txt"))));
            }
        } catch (Exception ex) {
            Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
            return;
        }
        String line;
        try {
            line = bufferedReader.readLine();
            if (line == null) {
                Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                return;
            }
            if (!builder.typeName().equals(line)) {
                Toast.makeText(getBaseContext(), "Неправильный формат файла!", Toast.LENGTH_LONG).show();
                return;
            }
            list = new List();

            while ((line = bufferedReader.readLine()) != null) {
                try {
                    list.add(builder.createFromString(line));
                } catch (Exception ex) {
                    Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getBaseContext(), "Список успешно загружен из файла!", Toast.LENGTH_LONG).show();
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listAdapter();
    }

    private void saveList() {
        BufferedWriter bufferedWriter = null;
        try {
            Log.d("MY_TAG", builder.typeName());
            if (builder.typeName().equals("Double")) {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput("double.txt", MODE_PRIVATE))));
            } else {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput("polar.txt", MODE_PRIVATE))));
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try {
            bufferedWriter.write(builder.typeName() + "\n");
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        BufferedWriter finalBufferedWriter = bufferedWriter;
        list.forEach(el -> {
            try {
                finalBufferedWriter.write(el.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Toast.makeText(getBaseContext(), "Список успешно сохранен в файл!", Toast.LENGTH_LONG).show();
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listAdapter(){
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        adapter.clear();
        for (int i = 0; i < list.size(); i++) {
            adapter.add((list.get(i)).toString());
        }
    }
}