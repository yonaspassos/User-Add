package com.yona.listadecontatos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        ListView listUsers = findViewById(R.id.listUsers);

        List<User> users = listUsers();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(
                this, R.layout.contact_item,
                users
        );
        listUsers.setAdapter(adapter);


    }

    private List<User> listUsers() {
        ArrayList<User> listUsers = readFile();
        return listUsers;

    }
    public ArrayList<User> readFile() {
        checkPermissionRead();
        Gson gson = new Gson();
        FileHandler file = new FileHandler();
        String usersFile = file.ReaderFile("usuarios.txt");

        Type listType = new TypeToken<User>(){}.getType();
        ArrayList<User> users = gson.fromJson(usersFile, listType);

        return users;
    }

    private void checkPermissionRead() {
        Context ctx = this;
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity)ctx,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1
            );

        }
    }

}
