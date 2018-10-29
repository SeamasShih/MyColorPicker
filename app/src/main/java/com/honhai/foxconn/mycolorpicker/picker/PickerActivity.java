package com.honhai.foxconn.mycolorpicker.picker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.honhai.foxconn.mycolorpicker.R;
import com.honhai.foxconn.mycolorpicker.entry.EntryActivity;

public class PickerActivity extends AppCompatActivity {

    private int color;
    private Button button;
    private ColorPickerView colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        setDataFromIntent();
        findViews();
        setListener();
        setViews();
    }

    private void setViews() {
        colorPicker.setColor(color);
    }

    private void setListener() {
        button.setOnClickListener(v -> {
            if (Utils.isFastClick())
                return;
            finish();
        });
        colorPicker.setOnClickListener(v -> {
            color = colorPicker.getColor();
        });
    }

    private void findViews() {
        button = findViewById(R.id.ok);
        colorPicker = findViewById(R.id.colorPicker);
    }

    private void setDataFromIntent() {
        Intent intent = getIntent();
        String type = intent.getStringExtra(EntryActivity.IntentParam.TYPE);
        switch (type) {
            case EntryActivity.SharedPreferencesParam.TEXT_COLOR:
                color = intent.getIntExtra(EntryActivity.IntentParam.COLOR, Color.WHITE);
                break;
            case EntryActivity.SharedPreferencesParam.BACKGROUND_COLOR:
                color = intent.getIntExtra(EntryActivity.IntentParam.COLOR, Color.BLACK);
                break;
            default:
                color = intent.getIntExtra(EntryActivity.IntentParam.COLOR, Color.RED);
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(EntryActivity.IntentParam.COLOR,color);
        setResult(RESULT_OK,intent);
        super.finish();
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
