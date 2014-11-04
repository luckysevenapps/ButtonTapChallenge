package com.luckysevenapps.buttontapchallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainMenu extends ActionBarActivity {
    private String SAVED_GAME_FILES = "SAVED_GAME_FILES";
    SharedPreferences gameFiles;
    SharedPreferences.Editor gameEditor;

    private final String classic_mode = "Classic Mode";
    private final String button_madness = "Button Madness";
    private final String whack_a_button = "Whack A Button";

    String loadClassicModeGameScore;
    String loadClassicModeHighScore;
    String loadKeepEmGreenGameScore;
    String loadKeepEmGreenHighScore;
    String loadWhackAButtonGameScore;
    String loadWhackAButtonHighScore;


    String selectedMode;
    private String selectedTheme;
    boolean gameSounds;

    private int gameMedalHelperOne;
    private int gameMedalHelperTwo;

    private int resHelper;

    Button buttonPlay, buttonSettings, buttonHowToPlay, buttonChangeTheme, buttonSettingsSave, buttonHowToPlayBack;

    ImageButton buttonScorePreview;

    CheckBox muteSounds;

    RadioGroup radioGroupGameModes;
    RadioButton radioClassic, radioKeepEmGreen, radioButtonMash;

    ToggleButton toggleButtonSwitchPreview;

    TextView textViewMainMenuGameModeDisplay, textViewMainMenuHighScoreCounter, textViewMainMenuGameScoreCounter, textViewScorePreview;

    ImageView imageViewGameMedal, imageViewNewMedal, imageViewNewHighScore, defaultPreview, tmntPreview, batmanPreview;

    PopupWindow popupWindow, settingsPopUp, howToPlayPopUp;

    Display display;
    int screenWidth;
    int screenHeight;
    int points;

    Handler handlerToggleButtonPreview = new Handler();
    Runnable runnableToggleButtonPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadData();
        setThemeLayout();

        setContentView(R.layout.main_menu);

        points = 0;

        buttonPlay = (Button) findViewById(R.id.button_main_menu_play);
        buttonSettings = (Button) findViewById(R.id.button_main_menu_settings);
        buttonHowToPlay = (Button) findViewById(R.id.button_main_menu_how_to_play);
        buttonChangeTheme = (Button) findViewById(R.id.button_change_theme);


        buttonScorePreview = (ImageButton) findViewById(R.id.image_button_score_preview);

        toggleButtonSwitchPreview = (ToggleButton) findViewById(R.id.toggle_button_switch_preview);

        imageViewGameMedal = (ImageView) findViewById(R.id.imageViewGameMedal);
        imageViewNewMedal = (ImageView) findViewById(R.id.imageViewNewMedal);
        imageViewNewHighScore = (ImageView) findViewById(R.id.imageViewNewHighScore);

        textViewMainMenuGameModeDisplay = (TextView) findViewById(R.id.textViewMainMenuGameMode);
        textViewMainMenuHighScoreCounter = (TextView) findViewById(R.id.textViewMainMenuHighScoreCounter);
        textViewMainMenuGameScoreCounter = (TextView) findViewById(R.id.textViewMainMenuGameScoreCounter);
        textViewScorePreview = (TextView) findViewById(R.id.textView_score_preview);

        buttonChangedListener();

        themeCheck();

        display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        loadScores();

        assignGameMedal();
        setOnClickListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setOnClickListeners() {
        buttonChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiatePopupWindow();
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateSettingsPopupWindow();
            }
        });

        buttonHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateHowToPlayPopupWindow();
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                saveSettings();
                if(selectedMode == classic_mode){
                    Intent intent = new Intent("com.luckysevenapps.buttontapchallenge.GAMESCREEN");
                    intent.putExtra("Theme", selectedTheme);
                    intent.putExtra("WindowTheme", resHelper);
                    startActivity(intent);
                } else if(selectedMode == button_madness){
                    Intent intent = new Intent("com.luckysevenapps.buttontapchallenge.KEEPEMGREEN");
                    intent.putExtra("Theme", selectedTheme);
                    intent.putExtra("WindowTheme", resHelper);
                    startActivity(intent);
                } else if(selectedMode == whack_a_button){
                    Intent intent = new Intent("com.luckysevenapps.buttontapchallenge.WHACKABUTTON");
                    intent.putExtra("Theme", selectedTheme);
                    intent.putExtra("WindowTheme", resHelper);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    private void initiateHowToPlayPopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) MainMenu.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View howToPlayLayout = inflater.inflate(R.layout.how_to_play, (ViewGroup) findViewById(R.id.how_to_play_layout));
            howToPlayPopUp = new PopupWindow(howToPlayLayout, screenWidth, screenHeight, true);
            howToPlayPopUp.showAtLocation(howToPlayLayout, Gravity.CENTER, 0, 0);
            buttonHowToPlayBack = (Button) howToPlayLayout.findViewById(R.id.buttonHowToPlayBack);
            setButtonThemeHowToPlay();
            setHowToPlayPopUpOnClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateSettingsPopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) MainMenu.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View settingsLayout = inflater.inflate(R.layout.settings, (ViewGroup) findViewById(R.id.settingsLayout));
            settingsLayout.setBackgroundResource(resHelper);
            settingsPopUp = new PopupWindow(settingsLayout, screenWidth, screenHeight, true);
            settingsPopUp.showAtLocation(settingsLayout, Gravity.CENTER, 0, 0);
            buttonSettingsSave = (Button) settingsLayout.findViewById(R.id.buttonSettingSave);
            muteSounds = (CheckBox) settingsLayout.findViewById(R.id.checkBoxMuteSounds);
            radioGroupGameModes = (RadioGroup) settingsLayout.findViewById(R.id.radioGroupGameModes);
            radioClassic = (RadioButton) settingsLayout.findViewById(R.id.radioButtonClassic);
            radioKeepEmGreen = (RadioButton) settingsLayout.findViewById(R.id.radioButtonKeepEmGreen);
            radioButtonMash = (RadioButton) settingsLayout.findViewById(R.id.radioButtonButtonMash);
            setButtonThemeSettings();
            if (gameSounds) {
                muteSounds.setChecked(false);
            }
            if (!gameSounds) {
                muteSounds.setChecked(true);
            }
            setSettingsPopUpOnClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiatePopupWindow() {
        try {
            LayoutInflater inflater = (LayoutInflater) MainMenu.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.theme_picker, (ViewGroup) findViewById(R.id.theme_picker_layout));
            RelativeLayout relativelayout = (RelativeLayout) layout.findViewById(R.id.scroll_view_container);
            relativelayout.setBackgroundResource(resHelper);
            popupWindow = new PopupWindow(layout, screenWidth, screenHeight, true);
            popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            defaultPreview = (ImageView) layout.findViewById(R.id.imageView_default_theme_preview);
            tmntPreview = (ImageView) layout.findViewById(R.id.imageView_tmnt_theme_preview);
            batmanPreview = (ImageView) layout.findViewById(R.id.imageView_batman_theme_preview);
            setPopUpOnClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHowToPlayPopUpOnClickListeners() {
        buttonHowToPlayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howToPlayPopUp.dismiss();
            }
        });
    }

    private void setSettingsPopUpOnClickListeners() {
        muteSounds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (muteSounds.isChecked()) {
                    gameSounds = false;
                }
                if (!muteSounds.isChecked()) {
                    gameSounds = true;
                }
            }

        });

        buttonSettingsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenu.this, "Settings Saved", Toast.LENGTH_SHORT).show();
                //Intent defaultIntent = getIntent();
                //defaultIntent.putExtra("Theme", selectedTheme);
                saveSettings();
                loadData();
                loadScores();
                assignGameMedal();
                //finish();
                //startActivity(defaultIntent);
                settingsPopUp.dismiss();
            }
        });

        radioGroupGameModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonClassic:
                        selectedMode = classic_mode;
                        textViewMainMenuGameModeDisplay.setText(selectedMode);
                        break;
                    case R.id.radioButtonKeepEmGreen:
                        selectedMode = button_madness;
                        textViewMainMenuGameModeDisplay.setText(selectedMode);
                        break;
                    case R.id.radioButtonButtonMash:
                        selectedMode = whack_a_button;
                        textViewMainMenuGameModeDisplay.setText(selectedMode);
                        break;
                }
            }
        });
    }

    private void setPopUpOnClickListeners() {
        defaultPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenu.this, "Default Theme Selected", Toast.LENGTH_SHORT).show();
                Intent defaultIntent = getIntent();
                defaultIntent.putExtra("Theme", "Default");
                finish();
                startActivity(defaultIntent);
                popupWindow.dismiss();
            }
        });

        tmntPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenu.this, "TMNT Theme Selected", Toast.LENGTH_SHORT).show();
                Intent tmntIntent = getIntent();
                tmntIntent.putExtra("Theme", "Tmnt");
                finish();
                startActivity(tmntIntent);
                popupWindow.dismiss();
            }
        });

        batmanPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenu.this, "Batman Theme Selected", Toast.LENGTH_SHORT).show();
                Intent batmanIntent = getIntent();
                batmanIntent.putExtra("Theme", "Batman");
                finish();
                startActivity(batmanIntent);
                popupWindow.dismiss();
            }
        });
    }

    private void setThemeLayout() {
        if ("Default".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            setTheme(R.style.DefaultTheme);
            selectedTheme = "Default";
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            setTheme(R.style.TmntTheme);
            selectedTheme = "Tmnt";
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            setTheme(R.style.BatmanTheme);
            selectedTheme = "Batman";
        }
    }

    private void setButtonThemeSettings() {
        if ("Default".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonSettingsSave.setBackgroundResource(R.drawable.default_button_styles);
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonSettingsSave.setBackgroundResource(R.drawable.tmnt_button_styles);
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonSettingsSave.setBackgroundResource(R.drawable.batman_button_styles);
        }
    }

    private void setButtonThemeHowToPlay() {
        if ("Default".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonHowToPlayBack.setBackgroundResource(R.drawable.default_button_styles);
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonHowToPlayBack.setBackgroundResource(R.drawable.tmnt_button_styles);
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonHowToPlayBack.setBackgroundResource(R.drawable.batman_button_styles);
        }
    }

    private void setButtonTheme() {
        if ("Default".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonPlay.setBackgroundResource(R.drawable.default_button_styles);
            buttonSettings.setBackgroundResource(R.drawable.default_button_styles);
            buttonHowToPlay.setBackgroundResource(R.drawable.default_button_styles);
            buttonChangeTheme.setBackgroundResource(R.drawable.default_button_styles);
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonPlay.setBackgroundResource(R.drawable.tmnt_button_styles);
            buttonSettings.setBackgroundResource(R.drawable.tmnt_button_styles);
            buttonHowToPlay.setBackgroundResource(R.drawable.tmnt_button_styles);
            buttonChangeTheme.setBackgroundResource(R.drawable.tmnt_button_styles);
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            buttonPlay.setBackgroundResource(R.drawable.batman_button_styles);
            buttonSettings.setBackgroundResource(R.drawable.batman_button_styles);
            buttonHowToPlay.setBackgroundResource(R.drawable.batman_button_styles);
            buttonChangeTheme.setBackgroundResource(R.drawable.batman_button_styles);
        }
    }

    private void themeCheck() {
        if ("Default".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_main_menu_layout);
            viewGroup.setBackgroundResource(R.drawable.buttontapchallengebackgroundfaded);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.linearLayout_scores_layout);
            viewGroupTwo.setBackgroundResource(R.drawable.default_score_frame_styles);
            resHelper = R.drawable.default_score_scroll_frame_styles;
            setButtonTheme();
            buttonScorePreview.setBackgroundResource(R.drawable.classic_button_change_onclick);
            toggleButtonSwitchPreview.setBackgroundResource(R.drawable.classic_button_toggle_anim);
        } else if ("Tmnt".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "tmnt.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_main_menu_layout);
            viewGroup.setBackgroundResource(R.drawable.green_hex_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.linearLayout_scores_layout);
            viewGroupTwo.setBackgroundResource(R.drawable.tmnt_score_frame_styles);
            resHelper = R.drawable.tmnt_score_scroll_frame_styles;
            setFont(viewGroup, typeface);
            setFont(viewGroupTwo, typeface);
            setButtonTheme();
            buttonScorePreview.setBackgroundResource(R.drawable.tmnt_button_change_onclick);
            toggleButtonSwitchPreview.setBackgroundResource(R.drawable.tmnt_leo_button_toggle_anim);
        } else if ("Batman".equalsIgnoreCase(getIntent().getStringExtra("Theme"))) {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "batman.ttf");
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.linearLayout_main_menu_layout);
            viewGroup.setBackgroundResource(R.drawable.green_hex_background);
            ViewGroup viewGroupTwo = (ViewGroup) findViewById(R.id.linearLayout_scores_layout);
            viewGroupTwo.setBackgroundResource(R.drawable.batman_score_frame_styles);
            resHelper = R.drawable.batman_score_scroll_frame_styles;
            setFont(viewGroup, typeface);
            setButtonTheme();
            buttonScorePreview.setBackgroundResource(R.drawable.batman_button_change_onclick);
            toggleButtonSwitchPreview.setBackgroundResource(R.drawable.batman_button_toggle_anim);
        }
    }

    public void setFont(ViewGroup group, Typeface font) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView /*|| v instanceof Button etc.*/) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup) {
                setFont((ViewGroup) v, font);
            }
        }
    }

    private void assignGameMedal() {
        if(selectedMode == classic_mode) {
            if (gameMedalHelperOne < 26) {
                imageViewGameMedal.setBackgroundResource(R.drawable.default_medal);
            } else if (gameMedalHelperOne > 25 && gameMedalHelperOne < 51) {
                imageViewGameMedal.setBackgroundResource(R.drawable.bronze_button_medal_x600);
                if (gameMedalHelperTwo < 26) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 50 && gameMedalHelperOne < 76) {
                imageViewGameMedal.setBackgroundResource(R.drawable.silver_button_medal_x600);
                if (gameMedalHelperTwo < 51) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 75 && gameMedalHelperOne < 101) {
                imageViewGameMedal.setBackgroundResource(R.drawable.gold_button_medal_x600);
                if (gameMedalHelperTwo < 76) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 100) {
                imageViewGameMedal.setBackgroundResource(R.drawable.platinum_button_medal_x600);
                if (gameMedalHelperTwo < 100) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            }
        } else if(selectedMode == button_madness) {
            if (gameMedalHelperOne < 2501) {
                imageViewGameMedal.setBackgroundResource(R.drawable.default_medal);
            } else if (gameMedalHelperOne > 2500 && gameMedalHelperOne < 4001) {
                imageViewGameMedal.setBackgroundResource(R.drawable.bronze_button_medal_x600);
                if (gameMedalHelperTwo < 2501) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 4000 && gameMedalHelperOne < 6001) {
                imageViewGameMedal.setBackgroundResource(R.drawable.silver_button_medal_x600);
                if (gameMedalHelperTwo < 4001) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 6000 && gameMedalHelperOne < 6501) {
                imageViewGameMedal.setBackgroundResource(R.drawable.gold_button_medal_x600);
                if (gameMedalHelperTwo < 6001) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 6500) {
                imageViewGameMedal.setBackgroundResource(R.drawable.platinum_button_medal_x600);
                if (gameMedalHelperTwo < 6500) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            }
        } else if(selectedMode == whack_a_button) {
            if (gameMedalHelperOne < 51) {
                imageViewGameMedal.setBackgroundResource(R.drawable.default_medal);
            } else if (gameMedalHelperOne > 50 && gameMedalHelperOne < 76) {
                imageViewGameMedal.setBackgroundResource(R.drawable.bronze_button_medal_x600);
                if (gameMedalHelperTwo < 51) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 75 && gameMedalHelperOne < 101) {
                imageViewGameMedal.setBackgroundResource(R.drawable.silver_button_medal_x600);
                if (gameMedalHelperTwo < 76) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 100 && gameMedalHelperOne < 151) {
                imageViewGameMedal.setBackgroundResource(R.drawable.gold_button_medal_x600);
                if (gameMedalHelperTwo < 101) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            } else if (gameMedalHelperOne > 150) {
                imageViewGameMedal.setBackgroundResource(R.drawable.platinum_button_medal_x600);
                if (gameMedalHelperTwo < 150) {
                    imageViewNewMedal.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void saveSettings() {
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        gameEditor = gameFiles.edit();
        gameEditor.putBoolean("gameSounds", gameSounds);
        gameEditor.putString("savedSelectedMode", selectedMode);
        gameEditor.putString("savedSelectedTheme", selectedTheme);
        gameEditor.putString("savedClassicModeGameScore", loadClassicModeGameScore);
        gameEditor.putString("savedClassicModeHighScore", loadClassicModeHighScore);
        gameEditor.putString("savedKeepEmGreenGameScore", loadKeepEmGreenGameScore);
        gameEditor.putString("savedKeepEmGreenHighScore", loadKeepEmGreenHighScore);
        gameEditor.putString("savedWhackAButtonGameScore", loadWhackAButtonGameScore);
        gameEditor.putString("savedWhackAButtonHighScore", loadWhackAButtonHighScore);
        gameEditor.commit();
    }

    private void loadData() {
        gameFiles = getSharedPreferences(SAVED_GAME_FILES, 5);
        selectedMode = gameFiles.getString("savedSelectedMode", classic_mode);
        gameSounds = gameFiles.getBoolean("gameSounds", true);
        selectedTheme = gameFiles.getString("savedSelectedTheme", "Default");
        loadClassicModeGameScore = gameFiles.getString("savedClassicModeGameScore", "0");
        loadClassicModeHighScore = gameFiles.getString("savedClassicModeHighScore", "0");
        loadKeepEmGreenGameScore = gameFiles.getString("savedKeepEmGreenGameScore", "0");
        loadKeepEmGreenHighScore = gameFiles.getString("savedKeepEmGreenHighScore", "0");
        loadWhackAButtonGameScore = gameFiles.getString("savedWhackAButtonGameScore", "0");
        loadWhackAButtonHighScore = gameFiles.getString("savedWhackAButtonHighScore", "0");
        if(selectedMode == classic_mode) {
            gameMedalHelperTwo = Integer.parseInt(loadClassicModeHighScore);
        } else if(selectedMode == button_madness) {
            gameMedalHelperTwo = Integer.parseInt(loadKeepEmGreenHighScore);
        } else if(selectedMode == whack_a_button) {
            gameMedalHelperTwo = Integer.parseInt(loadWhackAButtonHighScore);
        }
    }

    private void loadScores() {
        textViewMainMenuGameModeDisplay.setText(selectedMode);
        if(selectedMode == classic_mode) {
            textViewMainMenuGameScoreCounter.setText(loadClassicModeGameScore);
            if (Integer.parseInt(loadClassicModeHighScore) < Integer.parseInt(loadClassicModeGameScore)) {
                loadClassicModeHighScore = loadClassicModeGameScore;
                textViewMainMenuHighScoreCounter.setText(loadClassicModeHighScore);
                gameMedalHelperOne = Integer.parseInt(loadClassicModeHighScore);
                imageViewNewHighScore.setVisibility(View.VISIBLE);
            } else {
                textViewMainMenuHighScoreCounter.setText(loadClassicModeHighScore);
                gameMedalHelperOne = Integer.parseInt(loadClassicModeHighScore);
            }
        } else if(selectedMode == button_madness) {
            textViewMainMenuGameScoreCounter.setText(loadKeepEmGreenGameScore);
            if (Integer.parseInt(loadKeepEmGreenHighScore) < Integer.parseInt(loadKeepEmGreenGameScore)) {
                loadKeepEmGreenHighScore = loadKeepEmGreenGameScore;
                textViewMainMenuHighScoreCounter.setText(loadKeepEmGreenHighScore);
                gameMedalHelperOne = Integer.parseInt(loadKeepEmGreenHighScore);
                imageViewNewHighScore.setVisibility(View.VISIBLE);
            } else {
                textViewMainMenuHighScoreCounter.setText(loadKeepEmGreenHighScore);
                gameMedalHelperOne = Integer.parseInt(loadKeepEmGreenHighScore);
            }
        } else if(selectedMode == whack_a_button) {
            textViewMainMenuGameScoreCounter.setText(loadWhackAButtonGameScore);
            if (Integer.parseInt(loadWhackAButtonHighScore) < Integer.parseInt(loadWhackAButtonGameScore)) {
                loadWhackAButtonHighScore = loadWhackAButtonGameScore;
                textViewMainMenuHighScoreCounter.setText(loadWhackAButtonHighScore);
                gameMedalHelperOne = Integer.parseInt(loadWhackAButtonHighScore);
                imageViewNewHighScore.setVisibility(View.VISIBLE);
            } else {
                textViewMainMenuHighScoreCounter.setText(loadWhackAButtonHighScore);
                gameMedalHelperOne = Integer.parseInt(loadWhackAButtonHighScore);
            }
        }


    }

    private void buttonChangedListener() {
        runnableToggleButtonPreview = new Runnable() {
            @Override
            public void run() {
                toggleButtonSwitchPreview.setChecked(false);
            }
        };

        buttonScorePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButtonSwitchPreview.isChecked()) {
                    points++;
                    textViewScorePreview.setText(Integer.toString(points));
                }
            }
        });

        toggleButtonSwitchPreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (toggleButtonSwitchPreview.isChecked()) {
                    handlerToggleButtonPreview.postDelayed(runnableToggleButtonPreview, 2000);
                } else {
                    handlerToggleButtonPreview.removeCallbacks(runnableToggleButtonPreview);
                }
            }
        });
    }
}
