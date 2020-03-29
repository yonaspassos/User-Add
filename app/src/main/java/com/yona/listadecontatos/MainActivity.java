package com.yona.listadecontatos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public ListView listUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {


            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddUsers();
            }
        });

        listUsers = findViewById(R.id.listUsers);
        listUsers.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                User user = (User) adapter.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ShowUserActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

    }


    private List<User> listUsers() {
        List<User> listUsers = readFile();
        return listUsers;


    }

    public ArrayList<User> readFile() {
        checkPermissionRead();
        Gson gson = new Gson();
        FileHandler file = new FileHandler();
        String contactsFile = file.ReaderFile("usuarios.txt");
        if (contactsFile == ""){
            Toast.makeText(getApplicationContext(),"Nenhum arquivo encontrado", Toast.LENGTH_LONG).show();
        }
        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        ArrayList<User> contacts = gson.fromJson(contactsFile, listType);

        return contacts;
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


    @Override
    protected void onResume() {
        super.onResume();
        List<User> contacts = listUsers();

        if (contacts != null){
            ArrayAdapter<User> adapter = new ArrayAdapter<>(
                    this, R.layout.contact_item,
                    contacts
            );
            listUsers.setAdapter(adapter);
        }

    }

    private void showAddUsers() {
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }
}
