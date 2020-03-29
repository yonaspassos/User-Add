package com.yona.listadecontatos;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends Activity implements Validator.ValidationListener, View.OnClickListener {

    @NotEmpty(message = "Esse campo é obrigatório")
    @Pattern(regex = "(?<=\\s|^)[a-zA-Z]+(?=\\s+|$)", message = "Não utilizar caracteres especiais")
    private EditText nameUser;

    @NotEmpty(message = "Esse campo é obrigatório")
    @Email(message = "Escreva um e-mail válido")
    private EditText emailUser;

    @NotEmpty(message = "Esse campo é obrigatório")
    @Password(message = "Utilize no minimo 6 digitos")
    private EditText passwordUser;

    @NotEmpty(message = "Esse campo é obrigatório")
    @ConfirmPassword(message = "A confirmação de senha deve ser igual a senha")
    private EditText confPass;

    @NotEmpty(message = "Esse campo é obrigatório")
    @Length(min = 14, max = 14)
    private EditText cpfUser;

    private Validator validator;

    private InterstitialAd mInterstitialAd;

//    ID App = ca-app-pub-1835076353634496~3482386495
//
//    ID Anuncio = ca-app-pub-1835076353634496/8543141484

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_user);
        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private void initView() {
        nameUser = findViewById(R.id.textName);
        emailUser = findViewById(R.id.textEmail);
        passwordUser = findViewById(R.id.textPass);
        confPass = findViewById(R.id.textPass2);
        cpfUser = findViewById(R.id.textCPF);
        cpfUser.addTextChangedListener(MaskWatcher.buildCpf());
    }


    public void onClick(View v) {

        nameUser.setOnClickListener(this);
        nameUser .setText("");
        emailUser.setOnClickListener(this);
        emailUser.setText("");
        passwordUser.setOnClickListener(this);
        passwordUser.setText("");
        confPass.setOnClickListener(this);
        confPass.setText("");
        cpfUser.setOnClickListener(this);
        cpfUser.setText("");
    }

    public void buttonSave(View view) {
        validator.validate();

    }

    public void viewContacts (View view) {
        finish();
    }

    private ArrayList<User> getSavedContactsList() {
        Gson gson = new Gson();
        FileHandler file = new FileHandler();
        String contactsFile = file.ReaderFile("usuarios.txt");

        Type listType = new TypeToken<ArrayList<User>>(){}.getType();
        ArrayList<User> list = gson.fromJson(contactsFile, listType);

        if (list == null) {
            return new ArrayList<User>();
        } else {
            return list;
        }
    }

    private void checkPermission() {
        Context ctx = this;
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    (Activity)ctx,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );

        }
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "ok!", Toast.LENGTH_SHORT).show();
        checkPermission();

        User userInfo = new User(
                nameUser.getText().toString(),
                emailUser.getText().toString(),
                passwordUser.getText().toString(),
                cpfUser.getText().toString());
        Gson gson = new Gson();

        ArrayList<User> users = getSavedContactsList();
        users.add(userInfo);
        String json = gson.toJson(users);
        FileHandler file = new FileHandler();


        Context ctx = getApplicationContext();
        file.WriteFile("usuarios.txt", json);


        showAds();

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void showAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "A propagranda ainda não carregou...");
        }
    }
}
