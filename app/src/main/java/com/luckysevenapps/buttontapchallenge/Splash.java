package com.luckysevenapps.buttontapchallenge;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by James on 7/26/2014.
 */
public class Splash extends Activity {
    private String SAVED_GAME_FILES = "SAVED_GAME_FILES";
    SharedPreferences gameFiles;
    SharedPreferences.Editor gameEditor;

    Handler handlerSplash = new Handler();

    String selectedTheme;

    //MediaPlayer splashMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        selectedTheme = gameFiles.getString("savedSelectedTheme", "Default");

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent intent = new Intent("com.luckysevenapps.buttontapchallenge.MAINMENU");
                    intent.putExtra( "Theme", selectedTheme );
                    startActivity(intent);
                    overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}