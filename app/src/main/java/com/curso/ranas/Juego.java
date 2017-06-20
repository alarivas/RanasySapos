package com.curso.ranas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class Juego extends AppCompatActivity {

    int gray, green, brown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        gray = ContextCompat.getColor(getApplicationContext(), R.color.colorGray);
        green = ContextCompat.getColor(getApplicationContext(), R.color.colorGreen);
        brown = ContextCompat.getColor(getApplicationContext(), R.color.colorBrown);

    }

    public void listenerBatracio(View view) {
        ColorDrawable colors = (ColorDrawable) view.getBackground();
        int color = colors.getColor();
        boolean movs = false;
        ViewGroup container = (ViewGroup) findViewById(R.id.lineal);
        int index = container.indexOfChild(view);
        //RANA
        if (color == brown) {
            if (container.getChildCount() > index + 1) {
                View viewNext = container.getChildAt(index + 1);
                ColorDrawable colorsNext = (ColorDrawable) viewNext.getBackground();
                int colorNext = colorsNext.getColor();
                if (colorNext == gray) {
                    view.setBackgroundColor(gray);
                    viewNext.setBackgroundColor(brown);
                    getSound(brown);
                }
                //PUEDE SALTAR INCLUSO A UN BATRACIO DE SU MISMO TIPO!!!!
                else if (container.getChildCount() > index + 2) {
                    View viewNextNext = container.getChildAt(index + 2);
                    ColorDrawable colorsNextNext = (ColorDrawable) viewNextNext.getBackground();
                    int colorNextNext = colorsNextNext.getColor();
                    if (colorNextNext == gray) {
                        view.setBackgroundColor(gray);
                        viewNextNext.setBackgroundColor(brown);
                        getSound(brown);
                    }
                }
            }

            //SAPO
        } else if (color == green) {
            if (index > 0) {
                View viewPrev = container.getChildAt(index - 1);
                ColorDrawable colorsPrev = (ColorDrawable) viewPrev.getBackground();
                int colorPrev = colorsPrev.getColor();
                if (colorPrev == gray) {
                    view.setBackgroundColor(gray);
                    viewPrev.setBackgroundColor(green);
                    getSound(green);
                } else if (index - 1 > 0) {
                    View viewPrevPrev = container.getChildAt(index - 2);
                    ColorDrawable colorsPrevPrev = (ColorDrawable) viewPrevPrev.getBackground();
                    int colorPrevPrev = colorsPrevPrev.getColor();
                    if (colorPrevPrev == gray) {
                        view.setBackgroundColor(gray);
                        viewPrevPrev.setBackgroundColor(green);
                        getSound(green);
                    }
                }
            }
        }

        if(victoria()){
            alertVictory();
        }

        //revisa si quedan movimientos
        else{
            for (int i = 0; i < container.getChildCount(); i++){

                if(movValido(i, container)) {
                    movs = true;
                    break;
                }
            }

            if(!movs){
                alertNoMovs();
            }
        }
    }

    public boolean victoria() {
        //llamarlo luego de cada vez que se presiona un botón
        boolean ok = true;
        ViewGroup container = (ViewGroup) findViewById(R.id.lineal);
        int count = container.getChildCount();
        for (int i = 0; i < count; i++){
            View view = container.getChildAt(i);
            ColorDrawable colors = (ColorDrawable) view.getBackground();
            int color = colors.getColor();
            if(i < count/2){
                if(color!=green){
                    ok = false;
                }
            } else if ( i > count/2){
                if (color != brown){
                    ok = false;
                }
            }
            if(ok == false) break;

        }


        return ok;
    }

    private boolean movValido(int index, ViewGroup container){
        boolean state = false;
        View view = container.getChildAt(index);
        ColorDrawable colors = (ColorDrawable) view.getBackground();
        int color = colors.getColor();
        if (color == brown){
            if (container.getChildCount() > index + 1) {
                View viewNext = container.getChildAt(index + 1);
                ColorDrawable colorsNext = (ColorDrawable) viewNext.getBackground();
                int colorNext = colorsNext.getColor();
                if (colorNext == gray) {
                    state = true;
                }
                //PUEDE SALTAR A ALGUIEN DEL MISMO COLOR!!!!
                else if (container.getChildCount() > index + 2) {
                    View viewNextNext = container.getChildAt(container.indexOfChild(view) + 2);
                    ColorDrawable colorsNextNext = (ColorDrawable) viewNextNext.getBackground();
                    int colorNextNext = colorsNextNext.getColor();
                    if (colorNextNext == gray) {
                        state = true;
                    }
                }
            }
        } else if (color == green) {
            if (index > 0) {
                View viewPrev = container.getChildAt(index - 1);
                ColorDrawable colorsPrev = (ColorDrawable) viewPrev.getBackground();
                int colorPrev = colorsPrev.getColor();
                if (colorPrev == gray) {
                    state = true;
                } else if (index - 1 > 0) {
                    View viewPrevPrev = container.getChildAt(index - 2);
                    ColorDrawable colorsPrevPrev = (ColorDrawable) viewPrevPrev.getBackground();
                    int colorPrevPrev = colorsPrevPrev.getColor();
                    if (colorPrevPrev == gray) {
                        state = true;
                    }
                }
            }

        }

        return state;
    }


    private void getSound(int batracio){
        MediaPlayer mp;
        //rana
        if(batracio==brown){
            mp = MediaPlayer.create(this, R.raw.rana);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                public void onCompletion(MediaPlayer mp){
                    mp.release();
                }
            });
        }else if(batracio==green){
            mp = MediaPlayer.create(this, R.raw.sapo);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                public void onCompletion(MediaPlayer mp){
                    mp.release();
                }
            });
        }
    }

    public void alertNoMovs(){
        new AlertDialog.Builder(Juego.this)
                .setMessage("¿Qué desea hacer?")
                .setIcon(R.drawable.ic_report)
                .setCancelable(false)
                .setTitle("Sin movimientos válidos")
                .setPositiveButton("Intentar Otra Vez", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Juego.this.recreate();
                    }
                })
                .setNegativeButton("Menú Principal", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                            Intent intent = new Intent(Juego.this, MainActivity.class);
                            startActivity(intent);

                    }
                })
                .create()
                .show();
    }

    public void alertVictory(){
        new AlertDialog.Builder(Juego.this)
                .setTitle("VICTORIA")
                .setMessage("¿Qué desea hacer?")
                .setCancelable(false)
                .setPositiveButton("Jugar otra vez", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Juego.this.recreate();
                    }
                })
                .setNegativeButton("Menú Principal", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){

                        Intent intent = new Intent(Juego.this, MainActivity.class);
                        startActivity(intent);

                    }
                })
                .create()
                .show();
    }

    public void listenerRestart(View view){
        Juego.this.recreate();
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

}


