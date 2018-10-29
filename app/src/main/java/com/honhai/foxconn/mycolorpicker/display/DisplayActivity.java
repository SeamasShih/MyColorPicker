package com.honhai.foxconn.mycolorpicker.display;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.honhai.foxconn.mycolorpicker.R;
import com.honhai.foxconn.mycolorpicker.entry.EntryActivity;

public class DisplayActivity extends AppCompatActivity {

    DisplayView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViews();
        setText();
    }

    private void setText() {
        Intent intent = getIntent();
        text.setText(intent.getStringExtra(EntryActivity.IntentParam.TEXT));
        text.setTextColor(intent.getIntExtra(EntryActivity.IntentParam.TEXT_COLOR,Color.WHITE));
        text.setBackgroundColor(intent.getIntExtra(EntryActivity.IntentParam.BACKGROUND_COLOR,Color.BLACK));
    }

    private void findViews() {
        text = findViewById(R.id.display);
    }
}
