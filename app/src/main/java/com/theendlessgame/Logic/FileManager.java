package com.theendlessgame.Logic;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Luis on 29/05/14.
 */
public class FileManager extends ActionBarActivity {

    public static void setActivity(Activity pSender){
        _CurrentActivity = pSender;
    }

    public void createFile(String pFileName) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(_CurrentActivity.openFileOutput(pFileName, Context.MODE_PRIVATE));
        outputStreamWriter.write(';');
        outputStreamWriter.close();
    }

    public void writeToFile(String pFileName, String pData) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(_CurrentActivity.openFileOutput(pFileName, Context.MODE_PRIVATE | Context.MODE_APPEND));
        System.out.println(pData);
        outputStreamWriter.append(pData);
        outputStreamWriter.close();
    }

    public String readFile(String pFileName) throws IOException {
        InputStream inputStream = _CurrentActivity.openFileInput(pFileName);
        String result = "";
        if ( inputStream != null ) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            result = stringBuilder.toString();
        }
        return  result;
    }

    public boolean existsOnFile(String pFileData, String pQuery){
        Pattern p = Pattern.compile(pQuery);
        Matcher m = p.matcher(pFileData);
        return m.find();
    }

    private static Activity _CurrentActivity;
}
