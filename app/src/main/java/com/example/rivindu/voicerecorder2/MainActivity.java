package com.example.rivindu.voicerecorder2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button play, stop, record, analyze;
    private MediaRecorder myAudioRecorder;
    private String outputFile;

    public static final int request_code = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        analyze = (Button) findViewById(R.id.analyze);

        if(checkPermissionFromDevice())
        {

            stop.setEnabled(false);
            play.setEnabled(false);

            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);

            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException ise) {
                        Log.d("IllegalStateExcError","SOME ERROR");
                    } catch (IOException ioe) {
                        Log.d("IOERROR","SOME ERROR");
                    }
                    record.setEnabled(false);
                    stop.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("message", "shape");
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                    record.setEnabled(true);
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();

                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // make something
                    }


                }
            });

            analyze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"si-LK");

                    if(intent.resolveActivity(getPackageManager()) != null)
                    {
                        startActivityForResult(intent,10);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Your device doesnt support !!", Toast.LENGTH_LONG).show();
                    }


                }
            });


        }
        else
        {
            requestPermissionFromDevice();
        }

    }


    private void requestPermissionFromDevice() {
        ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO},
                request_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case request_code:
            {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    Toast.makeText(getApplicationContext(),"permission granted...",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"permission denied...",Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private boolean checkPermissionFromDevice() {
        int storage_permission= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recorder_permssion=ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return storage_permission == PackageManager.PERMISSION_GRANTED && recorder_permssion == PackageManager.PERMISSION_GRANTED;
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 10:
                if(resultCode == RESULT_OK && data != null)
                {
                  ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                  Log.println(Log.ERROR,"Hello", String.valueOf(result.get(0)));

                  //got to the other activity
                    Intent startNewActivity = new Intent(this, displayAnalysisDataActivity.class);
                    startNewActivity.putExtra("capture",result.get(0));
                    startActivity(startNewActivity);
                }
                break;

        }
    }
}
