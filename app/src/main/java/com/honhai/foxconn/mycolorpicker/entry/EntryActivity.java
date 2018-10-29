package com.honhai.foxconn.mycolorpicker.entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.honhai.foxconn.mycolorpicker.R;
import com.honhai.foxconn.mycolorpicker.display.DisplayActivity;
import com.honhai.foxconn.mycolorpicker.picker.PickerActivity;

public class EntryActivity extends AppCompatActivity {

    private EditText editText;
    private ColorPickerEntry textColorEntry;
    private ColorPickerEntry backgroundColorEntry;
    private Button go;
    private TextView preview;
    private SharedPreferences preferences;
    private int textColor;
    private int backgroundColor;

    private final int REQUEST_CODE_TEXT = 0;
    private final int REQUEST_CODE_BACKGROUND = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        findViews();
        setListeners();
        getSharedPreferences();
        setViewsColor();
    }

    private void setListeners() {
        setEditListener();
        setColorPickerEntryListener();
        setButtonListener();
    }

    private void setButtonListener() {
        go.setOnClickListener(v -> {
            if (Utils.isFastClick())
                return;
            Intent intent = new Intent();
            intent.putExtra(IntentParam.TEXT,preview.getText().toString());
            intent.putExtra(IntentParam.TEXT_COLOR,textColor);
            intent.putExtra(IntentParam.BACKGROUND_COLOR,backgroundColor);
            intent.setClass(this,DisplayActivity.class);
            startActivity(intent);
        });
    }

    private void setColorPickerEntryListener() {
        textColorEntry.setOnClickListener(v -> {
            if (Utils.isFastClick())
                return;
            Intent intent = new Intent();
            intent.putExtra(IntentParam.TYPE,SharedPreferencesParam.TEXT_COLOR);
            intent.putExtra(IntentParam.COLOR,textColor);
            intent.setClass(this,PickerActivity.class);
            startActivityForResult(intent,REQUEST_CODE_TEXT);
        });

        backgroundColorEntry.setOnClickListener(v -> {
            if (Utils.isFastClick())
                return;
            Intent intent = new Intent();
            intent.putExtra(IntentParam.TYPE,SharedPreferencesParam.BACKGROUND_COLOR);
            intent.putExtra(IntentParam.COLOR,backgroundColor);
            intent.setClass(this,PickerActivity.class);
            startActivityForResult(intent,REQUEST_CODE_BACKGROUND);
        });
    }

    private void setEditListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                preview.setText(s);
                if (s.length() == 0)
                    preview.setText(R.string.preview);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setViewsColor() {
        textColorEntry.setBackgroundColor(textColor);
        backgroundColorEntry.setBackgroundColor(backgroundColor);
        preview.setTextColor(textColor);
        preview.setBackgroundColor(backgroundColor);
    }

    private void getSharedPreferences() {
        preferences = getSharedPreferences(SharedPreferencesParam.NAME,MODE_PRIVATE);
        textColor = preferences.getInt(SharedPreferencesParam.TEXT_COLOR,Color.WHITE);
        backgroundColor = preferences.getInt(SharedPreferencesParam.BACKGROUND_COLOR,Color.BLACK);
        editText.setText(preferences.getString(SharedPreferencesParam.TEXT,""));
    }

    private void findViews() {
        editText = findViewById(R.id.edit);
        textColorEntry = findViewById(R.id.textColor);
        backgroundColorEntry = findViewById(R.id.backgroundColor);
        go = findViewById(R.id.go);
        preview = findViewById(R.id.preview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TEXT:
                if (resultCode == RESULT_OK && data != null){
                    textColor = data.getIntExtra(IntentParam.COLOR,Color.WHITE);
                    preview.setTextColor(textColor);
                    textColorEntry.setBackgroundColor(textColor);
                }
                break;
            case REQUEST_CODE_BACKGROUND:
                if (resultCode == RESULT_OK && data != null){
                    backgroundColor = data.getIntExtra(IntentParam.COLOR,Color.BLACK);
                    preview.setBackgroundColor(backgroundColor);
                    backgroundColorEntry.setBackgroundColor(backgroundColor);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        preferences.edit()
                .putString(SharedPreferencesParam.TEXT,editText.getText().toString())
                .putInt(SharedPreferencesParam.TEXT_COLOR,textColor)
                .putInt(SharedPreferencesParam.BACKGROUND_COLOR,backgroundColor)
                .apply();
        super.onStop();
    }

    public static class SharedPreferencesParam {
        public static final String NAME = "this";
        public static final String TEXT = "text";
        public static final String TEXT_COLOR = "text_color";
        public static final String BACKGROUND_COLOR = "background_color";
    }

    public static class IntentParam {
        public static final String TYPE = "type";
        public static final String COLOR = "color";
        public static final String TEXT = "text";
        public static final String BACKGROUND_COLOR = "backgroundColor";
        public static final String TEXT_COLOR = "textColor";
    }

    private static class Utils {
        private static long lastClickTime;

        static boolean isFastClick(){
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > 1000){
                lastClickTime = currentTime;
                return false;
            }
            return true;
        }
    }
}
