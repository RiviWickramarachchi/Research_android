package com.example.rivindu.voicerecorder2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class RepetitiveWords extends AppCompatActivity {

    String wordsCaptured;
    EditText repetitives;
    String endResult = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repetitive_words);

        repetitives = (EditText) findViewById(R.id.repetitives);

        Intent intent = getIntent();
        wordsCaptured = intent.getStringExtra("repetitives");

        findRepetitiveWords(wordsCaptured);



    }

    public void findRepetitiveWords(String rep)
    {
        String wordsList[] = rep.split(" ");
        Map<String,Integer> repetitiveWordCatcher = new HashMap<>();

        //repetitiveWordCatcher.put(String.valueOf(wordsList[1]),0);


        //Log.println(Log.ERROR,"err","Firstlist");
       // System.out.println(repetitiveWordCatcher.get(String.valueOf(wordsList[0])));
        //repetitiveWordCatcher.put(String.valueOf(wordsList[0]),repetitiveWordCatcher.get(String.valueOf(wordsList[0]))+1);
        //Log.println(Log.ERROR,"err","New Value");
        //System.out.println(repetitiveWordCatcher.get(String.valueOf(wordsList[0])));

        repetitiveWordCatcher.put(String.valueOf(wordsList[0]),1);

        for(int i=1; i<wordsList.length;i++)
        {
            if(repetitiveWordCatcher.containsKey(String.valueOf(wordsList[i])))
            {
                repetitiveWordCatcher.put(String.valueOf(wordsList[i]),repetitiveWordCatcher.get(String.valueOf(wordsList[i]))+1);
            }
            else
            {
                repetitiveWordCatcher.put(String.valueOf(wordsList[i]),1);
            }
        }

        Log.println(Log.ERROR,"err","New Value");
        for(String key : repetitiveWordCatcher.keySet())
        {
            System.out.println(key + " - " + repetitiveWordCatcher.get(key));
            endResult = endResult + key + " - " + repetitiveWordCatcher.get(key) + " " + "\n" + "\n";
        }
        repetitives.setText(endResult);

    }
}
