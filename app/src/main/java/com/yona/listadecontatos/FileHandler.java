package com.yona.listadecontatos;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {


    public String ReaderFile(String path) {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), path);

        BufferedReader buffRead;
        try {
            buffRead = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        String line = "";
        StringBuilder data = new StringBuilder();

        while (true) {

            if (line == null) {
                break;
            } else {
                data.append(line);
            }
            try {
                line = buffRead.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            buffRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public void WriteFile(String path, String data) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), path);

        try {
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(file, false);

            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

