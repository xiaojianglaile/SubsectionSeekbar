package com.jym.seekbar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.jym.sandbox.seekbar.view.SubsectionSeekBar;
import com.jym.subsectionseekbar.R;


public class MainActivity extends AppCompatActivity {

    private SubsectionSeekBar sbSubsection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sbSubsection = (SubsectionSeekBar) findViewById(R.id.sbSubsection);
        sbSubsection.setSelectedIndex(2);

        sbSubsection.setOnSeekBarChangeListener(new SubsectionSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar, int selectIndex) {
                Toast.makeText(MainActivity.this, "Progress:" + seekBar.getProgress() + ";SelectIndex:" + selectIndex, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
