package com.curso.ranas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txt = (TextView) findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/FunRaiser.ttf");
        txt.setTypeface(font);
    }


    public void listenerStart(View view){
        Intent intent = new Intent(this, Juego.class);
        startActivity(intent);
    }

    public void listenerOptions(View view){

        final SeekBar volControl = new SeekBar(this);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volControl.setMax(maxVol);
        volControl.setProgress(currVol);
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setIcon(R.drawable.ic_volume)
                .setView(volControl)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volControl.getProgress(), 0);
                        onResume();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        onResume();
                    }
                })
                .create()
                .show();
    }

    public void listenerExit(View view){
        finishAffinity();
    }
}
