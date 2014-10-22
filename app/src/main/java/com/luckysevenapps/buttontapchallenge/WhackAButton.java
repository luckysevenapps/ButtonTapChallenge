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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

public class WhackAButton extends Activity {
    private String SAVED_GAME_FILES = "SAVED_GAME_FILES";
    SharedPreferences gameFiles;
    SharedPreferences.Editor gameEditor;

    boolean gameSounds;

    String selectedMode;
    private String selectedTheme;

    private int buttonSleepTime = 6500;

    private int buttonsOn = 0;
    private int points;
    private String timeLeftSec;

    private int toggleButtonSoundOn = 0;
    private int toggleButtonSoundOff = 0;
    private int timerCountDownSoundTick = 0;
    int buttonScorePointSound = 0;
    private int timesUpBuzzerSound = 0;

    SoundPool toggleButtonOn, toggleButtonOff, timerCountDownTick, buttonScorePoint, timesUpBuzzer;

    MediaPlayer mp;

    Random randomTimer = new Random();

    Handler handlerGameTimer = new Handler();
    Handler handlerFinishGame = new Handler();

    Handler buttonHandler01 = new Handler();
    Handler buttonHandler02 = new Handler();
    Handler buttonHandler03 = new Handler();
    Handler buttonHandler04 = new Handler();
    Handler buttonHandler05 = new Handler();
    Handler buttonHandler06 = new Handler();
    Handler buttonHandler07 = new Handler();
    Handler buttonHandler08 = new Handler();
    Handler buttonHandler09 = new Handler();
    Handler buttonHandler10 = new Handler();
    Handler buttonHandler11 = new Handler();
    Handler buttonHandler12 = new Handler();
    Handler buttonHandler13 = new Handler();
    Handler buttonHandler14 = new Handler();
    Handler buttonHandler15 = new Handler();
    Handler buttonHandler16 = new Handler();

    Runnable runnableGameTimer, runnableFinishGame;

    CountDownTimer gameTimer, gameScoreCDT;

    private long countdownPeriod;

    TextView textViewTimeLeft, textViewTimeLeftCounter, textViewGameScore, textViewGameScoreCounter,
            textViewTimeIsUp;

    LinearLayout HUDLayout;

    Button toggleButtonGame01, toggleButtonGame02, toggleButtonGame03, toggleButtonGame04
            , toggleButtonGame05, toggleButtonGame06, toggleButtonGame07, toggleButtonGame08
            , toggleButtonGame09, toggleButtonGame10, toggleButtonGame11, toggleButtonGame12
            , toggleButtonGame13, toggleButtonGame14, toggleButtonGame15, toggleButtonGame16;

    Button[] buttons;

    Runnable buttonRunnable01, buttonRunnable02, buttonRunnable03, buttonRunnable04
            , buttonRunnable05, buttonRunnable06, buttonRunnable07, buttonRunnable08
            , buttonRunnable09, buttonRunnable10, buttonRunnable11, buttonRunnable12
            , buttonRunnable13, buttonRunnable14, buttonRunnable15, buttonRunnable16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setThemeLayout();
        setContentView(R.layout.game_activity_button_madness);

        loadData();

        points = 0;
        countdownPeriod = 26000;

        setUpSounds();


        initializeWidgets();
        themeCheck();
        setupRunnables();
        setButtonOnClickListener();
        createCountDownTimer();

        gameTimer.start();
        gameScoreCDT.start();
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
            for (Button button : buttons) {
                button.setBackgroundResource(R.drawable.classic_button_toggle_anim);
            }
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "tmnt.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_game_screen_layout);
            viewGroup.setBackgroundResource(R.drawable.green_hex_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.layout_game_top);
            viewGroupTwo.setBackgroundResource(R.drawable.tmnt_score_frame_styles);
            setFont(viewGroup, typeface);

            for (Button button : buttons) {
                button.setBackgroundResource(R.drawable.tmnt_leo_button_toggle_anim);
            }
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "batman.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_game_screen_layout);
            viewGroup.setBackgroundResource(R.drawable.batman_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.layout_game_top);
            viewGroupTwo.setBackgroundResource(R.drawable.batman_score_frame_styles);
            setFont(viewGroup, typeface);
            for (Button button : buttons) {
                button.setBackgroundResource(R.drawable.batman_button_toggle_anim);
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
        gameEditor.putString("savedKeepEmGreenGameScore", textViewGameScoreCounter.getText().toString());
        gameEditor.commit();
    }

    private void loadData() {
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        selectedMode = gameFiles.getString("savedSelectedMode", "keep_em_green");
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

        if (buttonHandler01 != null) {
            buttonHandler01.removeCallbacks(buttonRunnable01);
        }

        if (buttonHandler02 != null) {
            buttonHandler02.removeCallbacks(buttonRunnable02);
        }

        if (buttonHandler03 != null) {
            buttonHandler03.removeCallbacks(buttonRunnable03);
        }

        if (buttonHandler04 != null) {
            buttonHandler04.removeCallbacks(buttonRunnable04);
        }

        if (buttonHandler05 != null) {
            buttonHandler05.removeCallbacks(buttonRunnable01);
        }

        if (buttonHandler06 != null) {
            buttonHandler06.removeCallbacks(buttonRunnable02);
        }

        if (buttonHandler07 != null) {
            buttonHandler07.removeCallbacks(buttonRunnable03);
        }

        if (buttonHandler08 != null) {
            buttonHandler08.removeCallbacks(buttonRunnable04);
        }

        if (buttonHandler09 != null) {
            buttonHandler09.removeCallbacks(buttonRunnable01);
        }

        if (buttonHandler10 != null) {
            buttonHandler10.removeCallbacks(buttonRunnable02);
        }

        if (buttonHandler11 != null) {
            buttonHandler11.removeCallbacks(buttonRunnable03);
        }

        if (buttonHandler12 != null) {
            buttonHandler12.removeCallbacks(buttonRunnable04);
        }

        if (buttonHandler13 != null) {
            buttonHandler13.removeCallbacks(buttonRunnable01);
        }

        if (buttonHandler14 != null) {
            buttonHandler14.removeCallbacks(buttonRunnable02);
        }

        if (buttonHandler15 != null) {
            buttonHandler15.removeCallbacks(buttonRunnable03);
        }

        if (buttonHandler16 != null) {
            buttonHandler16.removeCallbacks(buttonRunnable04);
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

        gameScoreCDT = new CountDownTimer(countdownPeriod + 5000, 250) {
            public void onTick(long millisUntilFinished) {
                points += buttonsOn * 4;
                textViewGameScoreCounter.setText(String.valueOf(points));
                countdownPeriod = millisUntilFinished;
            }

            public void onFinish() {
                gameScoreCDT.cancel();
            }
        };
    }

    private void initializeWidgets() {
        textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft);
        textViewTimeLeftCounter = (TextView) findViewById(R.id.textViewTimeLeftCounter);
        textViewGameScore = (TextView) findViewById(R.id.textViewGameScore);
        textViewGameScoreCounter = (TextView) findViewById(R.id.textViewGameScoreCounter);
        textViewTimeIsUp = (TextView) findViewById(R.id.textViewTimeIsUp);
        HUDLayout = (LinearLayout) findViewById(R.id.layout_game_top);

        toggleButtonGame01 = (Button) findViewById(R.id.imageButtonScoreButton01);
        toggleButtonGame02 = (Button) findViewById(R.id.imageButtonScoreButton02);
        toggleButtonGame03 = (Button) findViewById(R.id.imageButtonScoreButton03);
        toggleButtonGame04 = (Button) findViewById(R.id.imageButtonScoreButton04);
        toggleButtonGame05 = (Button) findViewById(R.id.imageButtonScoreButton05);
        toggleButtonGame06 = (Button) findViewById(R.id.imageButtonScoreButton06);
        toggleButtonGame07 = (Button) findViewById(R.id.imageButtonScoreButton07);
        toggleButtonGame08 = (Button) findViewById(R.id.imageButtonScoreButton08);
        toggleButtonGame09 = (Button) findViewById(R.id.imageButtonScoreButton09);
        toggleButtonGame10 = (Button) findViewById(R.id.imageButtonScoreButton10);
        toggleButtonGame11 = (Button) findViewById(R.id.imageButtonScoreButton11);
        toggleButtonGame12 = (Button) findViewById(R.id.imageButtonScoreButton12);
        toggleButtonGame13 = (Button) findViewById(R.id.imageButtonScoreButton13);
        toggleButtonGame14 = (Button) findViewById(R.id.imageButtonScoreButton14);
        toggleButtonGame15 = (Button) findViewById(R.id.imageButtonScoreButton15);
        toggleButtonGame16 = (Button) findViewById(R.id.imageButtonScoreButton16);

        buttons = new Button[]{
                toggleButtonGame01, toggleButtonGame02, toggleButtonGame03, toggleButtonGame04
                , toggleButtonGame05, toggleButtonGame06, toggleButtonGame07, toggleButtonGame08
                , toggleButtonGame09, toggleButtonGame10, toggleButtonGame11, toggleButtonGame12
                , toggleButtonGame13, toggleButtonGame14, toggleButtonGame15, toggleButtonGame16
        };
    }

    private void setupRunnables() {
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
                textViewTimeIsUp.setVisibility(View.VISIBLE);
                for (Button button : buttons) {
                    button.setVisibility(View.GONE);
                }
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

        buttonRunnable01 = new Runnable() {
            @Override
            public void run() {
                toggleButtonGame01.setVisibility(View.GONE);
            }
        };


        buttonRunnable02 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable03 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable04 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable05 = new Runnable() {
            @Override
            public void run() {
            }
        };


        buttonRunnable06 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable07 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable08 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable09 = new Runnable() {
            @Override
            public void run() {
            }
        };


        buttonRunnable10 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable11 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable12 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable13 = new Runnable() {
            @Override
            public void run() {
            }
        };


        buttonRunnable14 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable15 = new Runnable() {
            @Override
            public void run() {
            }
        };

        buttonRunnable16 = new Runnable() {
            @Override
            public void run() {
            }
        };
    }

    private void setButtonOnClickListener() {
        toggleButtonGame01.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(gameSounds) {
                    buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                }
                buttonHandler01.postDelayed(buttonRunnable01, randomTimer.nextInt(buttonSleepTime));
                buttonHandler01.removeCallbacks(buttonRunnable01);
            }
        });

        toggleButtonGame02.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame02.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler02.postDelayed(buttonRunnable02, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler02.removeCallbacks(buttonRunnable02);
                }
            }
        });

        toggleButtonGame03.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame03.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler03.postDelayed(buttonRunnable03, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler03.removeCallbacks(buttonRunnable03);
                }
            }
        });

        toggleButtonGame04.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame04.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler04.postDelayed(buttonRunnable04, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler04.removeCallbacks(buttonRunnable04);
                }
            }
        });

        toggleButtonGame05.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame05.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler05.postDelayed(buttonRunnable05, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler05.removeCallbacks(buttonRunnable05);
                }
            }
        });

        toggleButtonGame06.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame06.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler06.postDelayed(buttonRunnable06, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler06.removeCallbacks(buttonRunnable06);
                }
            }
        });

        toggleButtonGame07.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame07.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler07.postDelayed(buttonRunnable07, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler07.removeCallbacks(buttonRunnable07);
                }
            }
        });

        toggleButtonGame08.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame08.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler08.postDelayed(buttonRunnable08, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler08.removeCallbacks(buttonRunnable08);
                }
            }
        });

        toggleButtonGame09.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame09.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler09.postDelayed(buttonRunnable09, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler09.removeCallbacks(buttonRunnable09);
                }
            }
        });

        toggleButtonGame10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame10.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler10.postDelayed(buttonRunnable10, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler10.removeCallbacks(buttonRunnable10);
                }
            }
        });

        toggleButtonGame11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame11.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler11.postDelayed(buttonRunnable11, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler11.removeCallbacks(buttonRunnable11);
                }
            }
        });

        toggleButtonGame12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame12.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler12.postDelayed(buttonRunnable12, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler12.removeCallbacks(buttonRunnable12);
                }
            }
        });

        toggleButtonGame13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame13.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler13.postDelayed(buttonRunnable13, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler13.removeCallbacks(buttonRunnable13);
                }
            }
        });

        toggleButtonGame14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame14.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler14.postDelayed(buttonRunnable14, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler14.removeCallbacks(buttonRunnable14);
                }
            }
        });

        toggleButtonGame15.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame15.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler15.postDelayed(buttonRunnable15, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler15.removeCallbacks(buttonRunnable15);
                }
            }
        });

        toggleButtonGame16.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(toggleButtonGame16.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    buttonHandler16.postDelayed(buttonRunnable16, randomTimer.nextInt(buttonSleepTime));
                } else {
                    buttonHandler16.removeCallbacks(buttonRunnable16);
                }
            }
        });
    }
}
