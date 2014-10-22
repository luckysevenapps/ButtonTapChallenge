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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Random;

/**
 * Created by James on 7/26/2014.
 */
public class GameScreen extends Activity {
    private String SAVED_GAME_FILES = "SAVED_GAME_FILES";
    SharedPreferences gameFiles;
    SharedPreferences.Editor gameEditor;

    boolean gameSounds;

    String selectedMode;
    private String selectedTheme;
    private int resHelper;

    private int points;
    private String timeLeftSec;

    private int toggleButtonSoundOn = 0;
    private int toggleButtonSoundOff = 0;
    private int timerCountDownSoundTick = 0;
    private int buttonScorePointSound = 0;
    private int timesUpBuzzerSound = 0;

    SoundPool toggleButtonOn;
    SoundPool toggleButtonOff;
    SoundPool timerCountDownTick;
    SoundPool buttonScorePoint;
    SoundPool timesUpBuzzer;

    MediaPlayer mp;

    Random randomTimer = new Random();

    Handler handlerGameTimer = new Handler();
    Handler handlerFinishGame = new Handler();
    Handler handlerButtonTopLeft = new Handler();
    Handler handlerButtonTopRight = new Handler();
    Handler handlerButtonBottomLeft = new Handler();
    Handler handlerButtonBottomRight = new Handler();

    Runnable runnableGameTimer, runnableFinishGame, runnableButtonTopLeft,
            runnableButtonTopRight, runnableButtonBottomLeft, runnableButtonBottomRight;

    CountDownTimer gameTimer;

    private long countdownPeriod;

    TextView textViewTimeLeft, textViewTimeLeftCounter, textViewGameScore, textViewGameScoreCounter,
            textViewTimeIsUp;
    ImageButton imageButtonScoreButton;
    ToggleButton toggleButtonTopLeft, toggleButtonTopRight, toggleButtonBottomLeft,
            toggleButtonBottomRight;

    LinearLayout HUDLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setThemeLayout();
        setContentView(R.layout.game_screen);


        loadData();

        points = 0;
        countdownPeriod = 26000;

        setUpSounds();


        initializeWidgets();
        themeCheck();
        setupRunnables();
        setButtonOnClickListener();
        toggleButtonChangedListener();
        createCountDownTimer();

        gameTimer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
        stopAllRunnables();
        intentSettingsCheck();
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
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
        stopAllRunnables();
        if(gameSounds) {
            releaseSoundResources();
        }
        finish();
    }

    private void setUpSounds() {
        toggleButtonOn = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        toggleButtonSoundOn = toggleButtonOn.load(this, R.raw.button_on_1, 1);

        toggleButtonOff = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        toggleButtonSoundOff = toggleButtonOff.load(this, R.raw.button_off_1, 1);

        timerCountDownTick = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        timerCountDownSoundTick = timerCountDownTick.load(this, R.raw.time_tick, 1);

        buttonScorePoint = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        buttonScorePointSound = buttonScorePoint.load(this, R.raw.scorepoint, 1);

        timesUpBuzzer = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        timesUpBuzzerSound = timesUpBuzzer.load(this, R.raw.times_up_buzzer, 1);

        if(gameSounds) {
            mp = MediaPlayer.create(GameScreen.this, R.raw.game_loop_01);
            mp.setLooping(true);
            mp.setVolume(.35F, .35F);
            mp.start();
        }

    }

    private void releaseSoundResources() {
        toggleButtonOn.release();
        toggleButtonOff.release();
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
            toggleButtonTopLeft.setBackgroundResource(R.drawable.classic_button_toggle_anim);
            toggleButtonTopRight.setBackgroundResource(R.drawable.classic_button_toggle_anim);
            toggleButtonBottomLeft.setBackgroundResource(R.drawable.classic_button_toggle_anim);
            toggleButtonBottomRight.setBackgroundResource(R.drawable.classic_button_toggle_anim);
            imageButtonScoreButton.setBackgroundResource(R.drawable.classic_button_change_onclick);
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "tmnt.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_game_screen_layout);
            viewGroup.setBackgroundResource(R.drawable.green_hex_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.layout_game_top);
            viewGroupTwo.setBackgroundResource(R.drawable.tmnt_score_frame_styles);
            setFont(viewGroup, typeface);
            toggleButtonTopLeft.setBackgroundResource(R.drawable.tmnt_leo_button_toggle_anim);
            toggleButtonTopRight.setBackgroundResource(R.drawable.tmnt_raph_button_toggle_anim);
            toggleButtonBottomLeft.setBackgroundResource(R.drawable.tmnt_mike_button_toggle_anim);
            toggleButtonBottomRight.setBackgroundResource(R.drawable.tmnt_don_button_toggle_anim);
            imageButtonScoreButton.setBackgroundResource(R.drawable.tmnt_button_change_onclick);
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "batman.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_game_screen_layout);
            viewGroup.setBackgroundResource(R.drawable.batman_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.layout_game_top);
            viewGroupTwo.setBackgroundResource(R.drawable.batman_score_frame_styles);
            setFont(viewGroup, typeface);
            toggleButtonTopLeft.setBackgroundResource(R.drawable.batman_button_toggle_anim);
            toggleButtonTopRight.setBackgroundResource(R.drawable.batman_button_toggle_anim);
            toggleButtonBottomLeft.setBackgroundResource(R.drawable.batman_button_toggle_anim);
            toggleButtonBottomRight.setBackgroundResource(R.drawable.batman_button_toggle_anim);
            imageButtonScoreButton.setBackgroundResource(R.drawable.batman_button_change_onclick);
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

    @Override
    protected void onPause() {
        super.onPause();
        stopAllRunnables();
        if(gameSounds) {
            releaseSoundResources();
        }
        finish();
    }

    private void saveSettings() {
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        gameEditor = gameFiles.edit();
        gameEditor.putString("savedSelectedMode", selectedMode);
        gameEditor.putString("savedSelectedTheme", selectedTheme);
        gameEditor.putString("savedClassicModeGameScore", textViewGameScoreCounter.getText().toString());
        gameEditor.commit();
    }

    private void loadData() {
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        selectedMode = gameFiles.getString("savedSelectedMode", "classic_mode");
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

        if (handlerButtonTopLeft != null) {
            handlerButtonTopLeft.removeCallbacks(runnableButtonTopLeft);
        }

        if (handlerButtonTopRight != null) {
            handlerButtonTopRight.removeCallbacks(runnableButtonTopRight);
        }

        if (handlerButtonBottomLeft != null) {
            handlerButtonBottomLeft.removeCallbacks(runnableButtonBottomLeft);
        }

        if (handlerButtonBottomRight != null) {
            handlerButtonBottomRight.removeCallbacks(runnableButtonBottomRight);
        }
    }

    private void createCountDownTimer() {
        gameTimer = new CountDownTimer(countdownPeriod + 5000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftSec = String.valueOf(millisUntilFinished / 1000);
                textViewTimeLeftCounter.setText(timeLeftSec);
                countdownPeriod = millisUntilFinished;

                if (millisUntilFinished < 10000) {
                    if(gameSounds) {
                        timerCountDownTick.play(timerCountDownSoundTick, 1, 1, 0, 0, 1);
                    }
                }

                if(millisUntilFinished < 2000) {
                    handlerGameTimer.postDelayed(runnableFinishGame, 2000);
                }
            }

            public void onFinish() {
                handlerFinishGame.postDelayed(runnableGameTimer, 2000);
            }
        };
    }

    private void initializeWidgets() {
        textViewTimeLeft = (TextView) findViewById(R.id.textViewTimeLeft);
        textViewTimeLeftCounter = (TextView) findViewById(R.id.textViewTimeLeftCounter);
        textViewGameScore = (TextView) findViewById(R.id.textViewGameScore);
        textViewGameScoreCounter = (TextView) findViewById(R.id.textViewGameScoreCounter);
        textViewTimeIsUp = (TextView) findViewById(R.id.textViewTimeIsUp);
        imageButtonScoreButton = (ImageButton) findViewById(R.id.imageButtonScoreButton);
        toggleButtonTopLeft = (ToggleButton) findViewById(R.id.toggleButtonTopLeft);
        toggleButtonTopRight = (ToggleButton) findViewById(R.id.toggleButtonTopRight);
        toggleButtonBottomLeft = (ToggleButton) findViewById(R.id.toggleButtonBottomLeft);
        toggleButtonBottomRight = (ToggleButton) findViewById(R.id.toggleButtonBottomRight);
        HUDLayout = (LinearLayout) findViewById(R.id.layout_game_top);
    }

    private void setupRunnables() {
        runnableGameTimer = new Runnable() {
            @Override
            public void run() {
                saveSettings();
                intentSettingsCheck();
                if(gameSounds) {
                    releaseSoundResources();
                }
                finish();
            }

        };

        runnableFinishGame = new Runnable() {
            @Override
            public void run() {
                if(gameSounds) {
                    timesUpBuzzer.play(timesUpBuzzerSound, 1, 1, 0, 0, 1);
                    mp.release();
                }
                textViewTimeIsUp.setVisibility(View.VISIBLE);
                imageButtonScoreButton.setVisibility(View.GONE);
                toggleButtonTopLeft.setVisibility(View.GONE);
                toggleButtonTopRight.setVisibility(View.GONE);
                toggleButtonBottomLeft.setVisibility(View.GONE);
                toggleButtonBottomRight.setVisibility(View.GONE);
                textViewTimeLeft.setVisibility(View.GONE);
                textViewTimeLeftCounter.setVisibility(View.GONE);
                textViewGameScore.setVisibility(View.GONE);
                textViewGameScoreCounter.setVisibility(View.GONE);
                HUDLayout.setVisibility(View.GONE);
                toggleButtonOn.release();
                toggleButtonOff.release();
                timerCountDownTick.release();
                buttonScorePoint.release();
            }

        };

        runnableButtonTopLeft = new Runnable() {
            @Override
            public void run() {
                toggleButtonTopLeft.setChecked(false);
                if(gameSounds) {
                    toggleButtonOff.play(toggleButtonSoundOff, 1, 1, 0, 0, 1);
                }
                imageButtonScoreButton.setVisibility(View.GONE);
            }

        };

        runnableButtonTopRight = new Runnable() {
            @Override
            public void run() {
                toggleButtonTopRight.setChecked(false);
                if(gameSounds) {
                    toggleButtonOff.play(toggleButtonSoundOff, 1, 1, 0, 0, 1);
                }
                imageButtonScoreButton.setVisibility(View.GONE);
            }

        };

        runnableButtonBottomLeft = new Runnable() {
            @Override
            public void run() {
                toggleButtonBottomLeft.setChecked(false);
                if(gameSounds) {
                    toggleButtonOff.play(toggleButtonSoundOff, 1, 1, 0, 0, 1);
                }
                imageButtonScoreButton.setVisibility(View.GONE);
            }

        };

        runnableButtonBottomRight = new Runnable() {
            @Override
            public void run() {
                toggleButtonBottomRight.setChecked(false);
                if(gameSounds) {
                    toggleButtonOff.play(toggleButtonSoundOff, 1, 1, 0, 0, 1);
                }
                imageButtonScoreButton.setVisibility(View.GONE);
            }

        };
    }

    private void toggleButtonChangedListener() {
        toggleButtonTopLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    if (toggleButtonTopRight.isChecked() && toggleButtonBottomLeft.isChecked() && toggleButtonBottomRight.isChecked()) {
                        imageButtonScoreButton.setVisibility(View.VISIBLE);
                    }
                }
                if (!toggleButtonTopLeft.isChecked()) {
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });
        toggleButtonTopRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    if (toggleButtonTopLeft.isChecked() && toggleButtonBottomLeft.isChecked() && toggleButtonBottomRight.isChecked()) {
                        imageButtonScoreButton.setVisibility(View.VISIBLE);
                    }
                }
                if (!toggleButtonTopRight.isChecked()) {
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });
        toggleButtonBottomLeft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    if (toggleButtonTopLeft.isChecked() && toggleButtonTopRight.isChecked() && toggleButtonBottomRight.isChecked()) {
                        imageButtonScoreButton.setVisibility(View.VISIBLE);
                    }
                }
                if (!toggleButtonBottomLeft.isChecked()) {
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });
        toggleButtonBottomRight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    if (toggleButtonTopLeft.isChecked() && toggleButtonTopRight.isChecked() && toggleButtonBottomLeft.isChecked()) {
                        imageButtonScoreButton.setVisibility(View.VISIBLE);
                    }
                }
                if (!toggleButtonBottomRight.isChecked()) {
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setButtonOnClickListener() {
        imageButtonScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonTopLeft.isChecked() && toggleButtonTopRight.isChecked() &&
                        toggleButtonBottomLeft.isChecked() && toggleButtonBottomRight.isChecked()) {
                    if(gameSounds) {
                        buttonScorePoint.play(buttonScorePointSound, 1, 1, 0, 0, 1);
                    }
                    points++;
                    textViewGameScoreCounter.setText(Integer.toString(points));
                }
            }
        });

        toggleButtonTopLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (toggleButtonTopLeft.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    handlerButtonTopLeft.postDelayed(runnableButtonTopLeft, randomTimer.nextInt(6500));
                } else {
                    handlerButtonTopLeft.removeCallbacks(runnableButtonTopLeft);
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });

        toggleButtonTopRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (toggleButtonTopRight.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    handlerButtonTopRight.postDelayed(runnableButtonTopRight, randomTimer.nextInt(6500));
                } else {
                    handlerButtonTopRight.removeCallbacks(runnableButtonTopRight);
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });

        toggleButtonBottomLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (toggleButtonBottomLeft.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    handlerButtonBottomLeft.postDelayed(runnableButtonBottomLeft, randomTimer.nextInt(6500));
                } else {
                    handlerButtonBottomLeft.removeCallbacks(runnableButtonBottomLeft);
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });

        toggleButtonBottomRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (toggleButtonBottomRight.isChecked()) {
                    if(gameSounds) {
                        toggleButtonOn.play(toggleButtonSoundOn, 1, 1, 0, 0, 1);
                    }
                    handlerButtonBottomRight.postDelayed(runnableButtonBottomRight, randomTimer.nextInt(6500));
                } else {
                    handlerButtonBottomRight.removeCallbacks(runnableButtonBottomRight);
                    imageButtonScoreButton.setVisibility(View.GONE);
                }
            }
        });
    }
}