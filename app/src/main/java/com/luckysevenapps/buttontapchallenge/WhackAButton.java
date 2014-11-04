package com.luckysevenapps.buttontapchallenge;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;


public class WhackAButton extends Activity implements  View.OnClickListener {
    private String SAVED_GAME_FILES = "SAVED_GAME_FILES";
    SharedPreferences gameFiles;
    SharedPreferences.Editor gameEditor;

    boolean gameSounds;

    private String selectedMode;
    private String selectedTheme;

    private String timeLeftSec;
    private int timerCountDownSoundTick = 0;
    int buttonScorePointSound = 0;
    private int timesUpBuzzerSound = 0;

    SoundPool  timerCountDownTick, buttonScorePoint, timesUpBuzzer;

    MediaPlayer mp;

    Random randomTimer = new Random();

    Handler handlerGameTimer = new Handler();
    Handler handlerFinishGame = new Handler();

    Runnable runnableGameTimer, runnableFinishGame;

    CountDownTimer gameTimer;

    private long countdownPeriod;

    TextView textViewTimeLeft, textViewTimeLeftCounter, textViewGameScore, textViewGameScoreCounter,
            textViewTimeIsUp;

    LinearLayout HUDLayout;

    ImageButton button1, button2, button3, button4, button5, button6, button7, button8
            , button9, button10, button11, button12, button13, button14, button15, button16;

    ImageButton[] buttons;

    int points = 0;

    int min = 1000;
    int max = 1500;

    Random rng1 = new Random();
    Random rng2 = new Random();
    Random rng3 = new Random();
    Random rng4 = new Random();
    Random rng5 = new Random();
    Random rng6 = new Random();
    Random rng7 = new Random();
    Random rng8 = new Random();
    Random rng9 = new Random();
    Random rng10 = new Random();
    Random rng11 = new Random();
    Random rng12 = new Random();
    Random rng13 = new Random();
    Random rng14 = new Random();
    Random rng15 = new Random();
    Random rng16 = new Random();

    Handler h1 = new Handler();
    Handler h2 = new Handler();
    Handler h3 = new Handler();
    Handler h4 = new Handler();
    Handler h5 = new Handler();
    Handler h6 = new Handler();
    Handler h7 = new Handler();
    Handler h8 = new Handler();
    Handler h9 = new Handler();
    Handler h10 = new Handler();
    Handler h11 = new Handler();
    Handler h12 = new Handler();
    Handler h13 = new Handler();
    Handler h14 = new Handler();
    Handler h15 = new Handler();
    Handler h16 = new Handler();

    Runnable r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16
            ,r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setThemeLayout();
        setContentView(R.layout.game_activity_whack_a_button);

        loadData();

        points = 0;
        countdownPeriod = 26000;

        setUpSounds();
        setupWidgets();
        themeCheck();
        createCountDownTimer();

        gameTimer.start();

        setupOnClickListeners();

        setupRunnable();
        startHandlers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
        stopAllRunnables();
        if(gameSounds) {
            releaseSoundResources();
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
        stopAllRunnables();
        if(gameSounds) {
            releaseSoundResources();
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettings();
        stopAllRunnables();
        if (gameSounds) {
            releaseSoundResources();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
        stopAllRunnables();
        if(gameSounds) {
            releaseSoundResources();
        }
        intentSettingsCheck();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButtonScoreButton01:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button1.setVisibility(View.INVISIBLE);
                h1.removeCallbacks(r1);
                h1.removeCallbacks(r2);
                h1.postDelayed(r1, rng1.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton02:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button2.setVisibility(View.INVISIBLE);
                h2.removeCallbacks(r3);
                h2.removeCallbacks(r4);
                h2.postDelayed(r3, rng2.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton03:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button3.setVisibility(View.INVISIBLE);
                h3.removeCallbacks(r5);
                h3.removeCallbacks(r6);
                h3.postDelayed(r5, rng3.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton04:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button4.setVisibility(View.INVISIBLE);
                h4.removeCallbacks(r7);
                h4.removeCallbacks(r8);
                h4.postDelayed(r7, rng4.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton05:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button5.setVisibility(View.INVISIBLE);
                h5.removeCallbacks(r9);
                h5.removeCallbacks(r10);
                h5.postDelayed(r9, rng5.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton06:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button6.setVisibility(View.INVISIBLE);
                h6.removeCallbacks(r11);
                h6.removeCallbacks(r12);
                h6.postDelayed(r11, rng6.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton07:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button7.setVisibility(View.INVISIBLE);
                h7.removeCallbacks(r13);
                h7.removeCallbacks(r14);
                h7.postDelayed(r13, rng7.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton08:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button8.setVisibility(View.INVISIBLE);
                h8.removeCallbacks(r15);
                h8.removeCallbacks(r16);
                h8.postDelayed(r15, rng8.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton09:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button9.setVisibility(View.INVISIBLE);
                h9.removeCallbacks(r17);
                h9.removeCallbacks(r18);
                h9.postDelayed(r17, rng9.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton10:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button10.setVisibility(View.INVISIBLE);
                h10.removeCallbacks(r19);
                h10.removeCallbacks(r20);
                h10.postDelayed(r19, rng10.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton11:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button11.setVisibility(View.INVISIBLE);
                h11.removeCallbacks(r21);
                h11.removeCallbacks(r22);
                h11.postDelayed(r21, rng11.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton12:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button12.setVisibility(View.INVISIBLE);
                h12.removeCallbacks(r23);
                h12.removeCallbacks(r24);
                h12.postDelayed(r23, rng12.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton13:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button13.setVisibility(View.INVISIBLE);
                h13.removeCallbacks(r25);
                h13.removeCallbacks(r26);
                h13.postDelayed(r25, rng13.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton14:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button14.setVisibility(View.INVISIBLE);
                h14.removeCallbacks(r27);
                h14.removeCallbacks(r28);
                h14.postDelayed(r27, rng14.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton15:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button15.setVisibility(View.INVISIBLE);
                h15.removeCallbacks(r29);
                h15.removeCallbacks(r30);
                h15.postDelayed(r29, rng15.nextInt(max - min + 1) + min);
                break;

            case R.id.imageButtonScoreButton16:
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                points++;
                textViewGameScoreCounter.setText(String.valueOf(points));
                button16.setVisibility(View.INVISIBLE);
                h16.removeCallbacks(r31);
                h16.removeCallbacks(r32);
                h16.postDelayed(r31, rng16.nextInt(max - min + 1) + min);
                break;
        }
    }

    public void setupRunnable() {
        runnableGameTimer = new Runnable() {
            @Override
            public void run() {
                saveSettings();
                intentSettingsCheck();
                if (gameSounds) {
                    releaseSoundResources();
                }
                finish();
            }

        };

        runnableFinishGame = new Runnable() {
            @Override
            public void run() {
                if (gameSounds) {
                    timesUpBuzzer.play(timesUpBuzzerSound, 1, 1, 0, 0, 1);
                    mp.release();
                }
                stopHandlerRunnables();
                textViewTimeIsUp.setVisibility(View.VISIBLE);
                textViewTimeLeft.setVisibility(View.GONE);
                textViewTimeLeftCounter.setVisibility(View.GONE);
                textViewGameScore.setVisibility(View.GONE);
                textViewGameScoreCounter.setVisibility(View.GONE);
                HUDLayout.setVisibility(View.GONE);
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setVisibility(View.GONE);
                }
                timerCountDownTick.release();
                buttonScorePoint.release();
            }
        };

        r1 = new Runnable() {
            @Override
            public void run() {
                button1.setVisibility(View.INVISIBLE);
                h1.removeCallbacks(r1);
                h1.postDelayed(r2, rng1.nextInt(max - min + 1) + min);
            }
        };

        r2 = new Runnable() {
            @Override
            public void run() {
                button1.setVisibility(View.VISIBLE);
                h1.removeCallbacks(r2);
                h1.postDelayed(r1, rng1.nextInt(max - min + 1) + min);
            }
        };

        r3 = new Runnable() {
            @Override
            public void run() {
                button2.setVisibility(View.INVISIBLE);
                h2.removeCallbacks(r3);
                h2.postDelayed(r4, rng2.nextInt(max - min + 1) + min);
            }
        };

        r4 = new Runnable() {
            @Override
            public void run() {
                button2.setVisibility(View.VISIBLE);
                h2.removeCallbacks(r4);
                h2.postDelayed(r3, rng2.nextInt(1250));
            }
        };

        r5 = new Runnable() {
            @Override
            public void run() {
                button3.setVisibility(View.INVISIBLE);
                h3.removeCallbacks(r5);
                h3.postDelayed(r6, rng3.nextInt(max - min + 1) + min);
            }
        };

        r6 = new Runnable() {
            @Override
            public void run() {
                button3.setVisibility(View.VISIBLE);
                h3.removeCallbacks(r6);
                h3.postDelayed(r5, rng3.nextInt(max - min + 1) + min);
            }
        };

        r7 = new Runnable() {
            @Override
            public void run() {
                button4.setVisibility(View.INVISIBLE);
                h4.removeCallbacks(r7);
                h4.postDelayed(r8, rng4.nextInt(max - min + 1) + min);
            }
        };

        r8 = new Runnable() {
            @Override
            public void run() {
                button4.setVisibility(View.INVISIBLE);
                h4.removeCallbacks(r8);
                h4.postDelayed(r7, rng4.nextInt(max - min + 1) + min);
            }
        };

        r9 = new Runnable() {
            @Override
            public void run() {
                button5.setVisibility(View.INVISIBLE);
                h5.removeCallbacks(r9);
                h5.postDelayed(r10, rng5.nextInt(max - min + 1) + min);
            }
        };

        r10 = new Runnable() {
            @Override
            public void run() {
                button5.setVisibility(View.VISIBLE);
                h5.removeCallbacks(r10);
                h5.postDelayed(r9, rng5.nextInt(max - min + 1) + min);
            }
        };

        r11 = new Runnable() {
            @Override
            public void run() {
                button6.setVisibility(View.INVISIBLE);
                h6.removeCallbacks(r11);
                h6.postDelayed(r12, rng6.nextInt(max - min + 1) + min);
            }
        };

        r12 = new Runnable() {
            @Override
            public void run() {
                button6.setVisibility(View.VISIBLE);
                h6.removeCallbacks(r12);
                h6.postDelayed(r11, rng6.nextInt(max - min + 1) + min);
            }
        };

        r13 = new Runnable() {
            @Override
            public void run() {
                button7.setVisibility(View.INVISIBLE);
                h7.removeCallbacks(r13);
                h7.postDelayed(r14, rng7.nextInt(max - min + 1) + min);
            }
        };

        r14 = new Runnable() {
            @Override
            public void run() {
                button7.setVisibility(View.VISIBLE);
                h7.removeCallbacks(r14);
                h7.postDelayed(r13, rng7.nextInt(max - min + 1) + min);
            }
        };

        r15 = new Runnable() {
            @Override
            public void run() {
                button8.setVisibility(View.INVISIBLE);
                h8.removeCallbacks(r15);
                h8.postDelayed(r16, rng8.nextInt(max - min + 1) + min);
            }
        };

        r16 = new Runnable() {
            @Override
            public void run() {
                button8.setVisibility(View.VISIBLE);
                h8.removeCallbacks(r16);
                h8.postDelayed(r15, rng8.nextInt(max - min + 1) + min);
            }
        };

        r17 = new Runnable() {
            @Override
            public void run() {
                button9.setVisibility(View.INVISIBLE);
                h9.removeCallbacks(r17);
                h9.postDelayed(r18, rng9.nextInt(max - min + 1) + min);
            }
        };

        r18 = new Runnable() {
            @Override
            public void run() {
                button9.setVisibility(View.VISIBLE);
                h9.removeCallbacks(r18);
                h9.postDelayed(r17, rng9.nextInt(max - min + 1) + min);
            }
        };

        r19 = new Runnable() {
            @Override
            public void run() {
                button10.setVisibility(View.INVISIBLE);
                h10.removeCallbacks(r19);
                h10.postDelayed(r20, rng10.nextInt(max - min + 1) + min);
            }
        };

        r20 = new Runnable() {
            @Override
            public void run() {
                button10.setVisibility(View.VISIBLE);
                h10.removeCallbacks(r20);
                h10.postDelayed(r19, rng10.nextInt(1250));
            }
        };

        r21 = new Runnable() {
            @Override
            public void run() {
                button11.setVisibility(View.INVISIBLE);
                h11.removeCallbacks(r21);
                h11.postDelayed(r22, rng11.nextInt(max - min + 1) + min);
            }
        };

        r22 = new Runnable() {
            @Override
            public void run() {
                button11.setVisibility(View.VISIBLE);
                h11.removeCallbacks(r22);
                h11.postDelayed(r21, rng11.nextInt(max - min + 1) + min);
            }
        };

        r23 = new Runnable() {
            @Override
            public void run() {
                button12.setVisibility(View.INVISIBLE);
                h12.removeCallbacks(r23);
                h12.postDelayed(r24, rng12.nextInt(max - min + 1) + min);
            }
        };

        r24 = new Runnable() {
            @Override
            public void run() {
                button12.setVisibility(View.INVISIBLE);
                h12.removeCallbacks(r24);
                h12.postDelayed(r23, rng12.nextInt(max - min + 1) + min);
            }
        };

        r25 = new Runnable() {
            @Override
            public void run() {
                button13.setVisibility(View.INVISIBLE);
                h13.removeCallbacks(r25);
                h13.postDelayed(r26, rng13.nextInt(max - min + 1) + min);
            }
        };

        r26 = new Runnable() {
            @Override
            public void run() {
                button13.setVisibility(View.VISIBLE);
                h13.removeCallbacks(r26);
                h13.postDelayed(r25, rng13.nextInt(max - min + 1) + min);
            }
        };

        r27 = new Runnable() {
            @Override
            public void run() {
                button14.setVisibility(View.INVISIBLE);
                h14.removeCallbacks(r27);
                h14.postDelayed(r28, rng14.nextInt(max - min + 1) + min);
            }
        };

        r28 = new Runnable() {
            @Override
            public void run() {
                button14.setVisibility(View.VISIBLE);
                h14.removeCallbacks(r28);
                h14.postDelayed(r27, rng14.nextInt(max - min + 1) + min);
            }
        };

        r29 = new Runnable() {
            @Override
            public void run() {
                button15.setVisibility(View.INVISIBLE);
                h15.removeCallbacks(r29);
                h15.postDelayed(r30, rng15.nextInt(max - min + 1) + min);
            }
        };

        r30 = new Runnable() {
            @Override
            public void run() {
                button15.setVisibility(View.VISIBLE);
                h15.removeCallbacks(r30);
                h15.postDelayed(r29, rng15.nextInt(max - min + 1) + min);
            }
        };

        r31 = new Runnable() {
            @Override
            public void run() {
                button16.setVisibility(View.INVISIBLE);
                h16.removeCallbacks(r31);
                h16.postDelayed(r32, rng16.nextInt(max - min + 1) + min);
            }
        };

        r32 = new Runnable() {
            @Override
            public void run() {
                button16.setVisibility(View.VISIBLE);
                h16.removeCallbacks(r32);
                h16.postDelayed(r31, rng16.nextInt(max - min + 1) + min);
            }
        };
    }

    public void startHandlers() {
        h1.postDelayed(r1, rng1.nextInt(max - min + 1) + min);
        h2.postDelayed(r3, rng2.nextInt(max - min + 1) + min);
        h3.postDelayed(r5, rng3.nextInt(max - min + 1) + min);
        h4.postDelayed(r7, rng4.nextInt(max - min + 1) + min);
        h5.postDelayed(r9, rng5.nextInt(max - min + 1) + min);
        h6.postDelayed(r11, rng6.nextInt(max - min + 1) + min);
        h7.postDelayed(r13, rng7.nextInt(max - min + 1) + min);
        h8.postDelayed(r15, rng8.nextInt(max - min + 1) + min);
        h9.postDelayed(r17, rng9.nextInt(max - min + 1) + min);
        h10.postDelayed(r19, rng10.nextInt(max - min + 1) + min);
        h11.postDelayed(r21, rng11.nextInt(max - min + 1) + min);
        h12.postDelayed(r23, rng12.nextInt(max - min + 1) + min);
        h13.postDelayed(r25, rng13.nextInt(max - min + 1) + min);
        h14.postDelayed(r27, rng14.nextInt(max - min + 1) + min);
        h15.postDelayed(r29, rng15.nextInt(max - min + 1) + min);
        h16.postDelayed(r31, rng16.nextInt(max - min + 1) + min);
    }

    public void setupOnClickListeners() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button10.setOnClickListener(this);
        button11.setOnClickListener(this);
        button12.setOnClickListener(this);
        button13.setOnClickListener(this);
        button14.setOnClickListener(this);
        button15.setOnClickListener(this);
        button16.setOnClickListener(this);
    }

    public void setupWidgets() {
        textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft);
        textViewTimeLeftCounter = (TextView) findViewById(R.id.textViewTimeLeftCounter);
        textViewGameScore = (TextView) findViewById(R.id.textViewGameScore);
        textViewGameScoreCounter = (TextView) findViewById(R.id.textViewGameScoreCounter);
        textViewTimeIsUp = (TextView) findViewById(R.id.textViewTimeIsUp);
        HUDLayout = (LinearLayout) findViewById(R.id.layout_game_top);

        button1 = (ImageButton) findViewById(R.id.imageButtonScoreButton01);
        button2 = (ImageButton) findViewById(R.id.imageButtonScoreButton02);
        button3 = (ImageButton) findViewById(R.id.imageButtonScoreButton03);
        button4 = (ImageButton) findViewById(R.id.imageButtonScoreButton04);
        button5 = (ImageButton) findViewById(R.id.imageButtonScoreButton05);
        button6 = (ImageButton) findViewById(R.id.imageButtonScoreButton06);
        button7 = (ImageButton) findViewById(R.id.imageButtonScoreButton07);
        button8 = (ImageButton) findViewById(R.id.imageButtonScoreButton08);
        button9 = (ImageButton) findViewById(R.id.imageButtonScoreButton09);
        button10 = (ImageButton) findViewById(R.id.imageButtonScoreButton10);
        button11 = (ImageButton) findViewById(R.id.imageButtonScoreButton11);
        button12 = (ImageButton) findViewById(R.id.imageButtonScoreButton12);
        button13 = (ImageButton) findViewById(R.id.imageButtonScoreButton13);
        button14 = (ImageButton) findViewById(R.id.imageButtonScoreButton14);
        button15 = (ImageButton) findViewById(R.id.imageButtonScoreButton15);
        button16 = (ImageButton) findViewById(R.id.imageButtonScoreButton16);

        buttons = new ImageButton[]{
                button1, button2, button3, button4, button5, button6, button7, button8
                , button9, button10, button11, button12, button13, button14, button15, button16
        };
    }

    private void setUpSounds() {
        timerCountDownTick = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        timerCountDownSoundTick = timerCountDownTick.load(this, R.raw.time_tick, 1);

        buttonScorePoint = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        buttonScorePointSound = buttonScorePoint.load(this, R.raw.scorepoint, 1);

        timesUpBuzzer = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        timesUpBuzzerSound = timesUpBuzzer.load(this, R.raw.times_up_buzzer, 1);

        if (gameSounds) {
            mp = MediaPlayer.create(WhackAButton.this, R.raw.game_loop_01);
            mp.setLooping(true);
            mp.setVolume(.35F, .35F);
            mp.start();
        }

    }

    private void releaseSoundResources() {
        timerCountDownTick.release();
        buttonScorePoint.release();
        timesUpBuzzer.release();
        mp.release();
    }

    private void setThemeLayout() {
        if ("Default".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            setTheme(R.style.DefaultTheme);
            selectedTheme = "Default";
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            setTheme(R.style.TmntTheme);
            selectedTheme = "Tmnt";
        }
    }

    private void themeCheck() {
        if ("Default".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_game_screen_layout);
            viewGroup.setBackgroundResource(R.drawable.buttontapchallengebackgroundfaded);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.layout_game_top);
            viewGroupTwo.setBackgroundResource(R.drawable.default_score_frame_styles);
            for (ImageButton button : buttons) {
                button.setBackgroundResource(R.drawable.classic_button_change_onclick);
            }
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "tmnt.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_game_screen_layout);
            viewGroup.setBackgroundResource(R.drawable.green_hex_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.layout_game_top);
            viewGroupTwo.setBackgroundResource(R.drawable.tmnt_score_frame_styles);
            setFont(viewGroup, typeface);

            for (ImageButton button : buttons) {
                button.setBackgroundResource(R.drawable.tmnt_button_change_onclick);
            }
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "batman.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_game_screen_layout);
            viewGroup.setBackgroundResource(R.drawable.batman_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.layout_game_top);
            viewGroupTwo.setBackgroundResource(R.drawable.batman_score_frame_styles);
            setFont(viewGroup, typeface);
            for (ImageButton button : buttons) {
                button.setBackgroundResource(R.drawable.batman_button_change_onclick);
            }
        }
    }

    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof Button /*etc.*/) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v, font);
            }
        }
    }

    private void saveSettings() {
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        gameEditor = gameFiles.edit();
        gameEditor.putString("savedSelectedMode", selectedMode);
        gameEditor.putString("savedSelectedTheme", selectedTheme);
        gameEditor.putString("savedWhackAButtonGameScore", textViewGameScoreCounter.getText().toString());
        gameEditor.commit();
    }

    private void loadData() {
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        selectedMode = gameFiles.getString("savedSelectedMode", "Whack A Button");
        gameSounds = gameFiles.getBoolean("gameSounds", true);
        selectedTheme = gameFiles.getString("savedSelectedTheme", "Default");
    }

    private void intentSettingsCheck() {
        Intent mainMenuIntent = new Intent("com.luckysevenapps.buttontapchallenge.MAINMENU");
        mainMenuIntent.putExtra("Theme", selectedTheme);
        finish();
        startActivity(mainMenuIntent);
    }

    private void stopAllRunnables() {
        gameTimer.cancel();

        if (handlerGameTimer != null) {
            handlerGameTimer.removeCallbacks(runnableFinishGame);
        }

        if (handlerFinishGame != null) {
            handlerFinishGame.removeCallbacks(runnableGameTimer);
        }
    }

    private void stopHandlerRunnables() {
        if (h1 != null) {
            h1.removeCallbacks(r1);
            h1.removeCallbacks(r2);
        }

        if (h2 != null) {
            h2.removeCallbacks(r3);
            h2.removeCallbacks(r4);
        }

        if (h3 != null) {
            h3.removeCallbacks(r5);
            h3.removeCallbacks(r6);
        }

        if (h4 != null) {
            h4.removeCallbacks(r7);
            h4.removeCallbacks(r8);
        }

        if (h5 != null) {
            h5.removeCallbacks(r9);
            h5.removeCallbacks(r10);
        }

        if (h6 != null) {
            h6.removeCallbacks(r11);
            h6.removeCallbacks(r12);
        }

        if (h7 != null) {
            h7.removeCallbacks(r13);
            h7.removeCallbacks(r14);
        }

        if (h8 != null) {
            h8.removeCallbacks(r15);
            h8.removeCallbacks(r16);
        }

        if (h9 != null) {
            h9.removeCallbacks(r17);
            h9.removeCallbacks(r18);
        }

        if (h10 != null) {
            h10.removeCallbacks(r19);
            h10.removeCallbacks(r20);
        }

        if (h11 != null) {
            h11.removeCallbacks(r21);
            h11.removeCallbacks(r22);
        }

        if (h12 != null) {
            h12.removeCallbacks(r23);
            h12.removeCallbacks(r24);
        }

        if (h13 != null) {
            h13.removeCallbacks(r25);
            h13.removeCallbacks(r26);
        }

        if (h14 != null) {
            h14.removeCallbacks(r27);
            h14.removeCallbacks(r28);
        }

        if (h15 != null) {
            h15.removeCallbacks(r29);
            h15.removeCallbacks(r30);
        }

        if (h16 != null) {
            h16.removeCallbacks(r31);
            h16.removeCallbacks(r32);
        }
    }

    private void createCountDownTimer() {
        gameTimer = new CountDownTimer(countdownPeriod + 5000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftSec = String.valueOf(millisUntilFinished / 1000);
                textViewTimeLeftCounter.setText(timeLeftSec);
                countdownPeriod = millisUntilFinished;

                if (millisUntilFinished < 10000) {
                    if (gameSounds) {
                        timerCountDownTick.play(timerCountDownSoundTick, 1, 1, 0, 0, 1);
                    }
                }

                if (millisUntilFinished < 2000) {
                    handlerGameTimer.postDelayed(runnableFinishGame, 2000);
                }
            }

            public void onFinish() {
                handlerFinishGame.postDelayed(runnableGameTimer, 2000);
            }
        };
    }
}