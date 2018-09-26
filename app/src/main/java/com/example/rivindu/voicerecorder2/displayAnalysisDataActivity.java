package com.example.rivindu.voicerecorder2;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class displayAnalysisDataActivity extends AppCompatActivity {

    //add the sinhala dictionary here


    //initiallize meaningful,non-meaningful parameters here
        TextView wordsCaptured;
        EditText sentence, meaningfuls;
        private Button repetitiveWords;

        int meaningfulWords=0;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_analysis_data);

        wordsCaptured = (TextView) findViewById(R.id.wordscaptured);
        sentence = (EditText) findViewById(R.id.sentence);
        meaningfuls = (EditText) findViewById(R.id.meaningfuls);
        repetitiveWords =(Button) findViewById(R.id.repetitivewordviewbtn);


        Log.println(Log.ERROR,"Hello", "InSecondPage");
        //declare text
        String text = "";
        final String capture;

        //display results

        try {
            //to read the textFile
            Log.println(Log.ERROR,"Hello", "TryTryTry");
            InputStream is = getAssets().open("dictionary.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error","Error");
        }


        Intent intent = getIntent();
        capture = intent.getStringExtra("capture");
        sentence.setText(capture);


        //Getting meaningful word count and non-meaningful wordcount
        String wordlist[] = text.split(" ");
        String captureWords[] = capture.split(" ");
        Log.println(Log.ERROR,"err", String.valueOf(captureWords.length));

        for(int i=0; i<captureWords.length;i++)
        {
            for(int j=0; j<wordlist.length;j++)
            {
                if(wordlist[j].contains(captureWords[i]))
                {
                    meaningfulWords++;
                }
            }
        }
        meaningfuls.setText(String.valueOf(meaningfulWords));

        repetitiveWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(capture);
            }
        });


    }
    public void goToPage(String capturedWords)
    {
        Intent startRepetitiveWordViewIntent = new Intent(this, RepetitiveWords.class);
        startRepetitiveWordViewIntent.putExtra("repetitives",capturedWords);
        startActivity(startRepetitiveWordViewIntent);

    }

}
