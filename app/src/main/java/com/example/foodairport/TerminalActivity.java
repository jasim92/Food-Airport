package com.example.foodairport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

public class TerminalActivity extends AppCompatActivity {

    ImageView getStart;
    RadioGroup radioGroup;

    Dialog dialog;
    GoogleProgressBar bar;

    String terminal;
    LinearLayout linearLayout;
    Animation top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_terminal);

        getStart = findViewById(R.id.get_start);
        linearLayout = findViewById(R.id.main_loading_layout);
        radioGroup = findViewById(R.id.radioGroup);
        top = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bheanim);
        linearLayout.setAnimation(top);

        bar = findViewById(R.id.progress_bar3);
        bar.setVisibility(View.GONE);
        statusBarColor();

        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(TerminalActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                bar.setVisibility(View.VISIBLE);
                dialog.show();

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId==-1)
                    Toast.makeText(TerminalActivity.this, "No Terminal has been selected",Toast.LENGTH_SHORT).show();
                else
                {
                    RadioButton radioButton = radioGroup.findViewById(selectedId);
                    terminal = (String) radioButton.getText();
                    cacheTerminal();
                    Intent intent = new Intent(TerminalActivity.this, MainActivity.class);
//                    intent.putExtra("terminal", terminal);
                    startActivity(intent);

                }

                dialog.dismiss();

            }
        });
    }

    public void cacheTerminal()
    {
        SharedPreferences sp = getSharedPreferences("terminal",MODE_PRIVATE); //create file in private mode
        SharedPreferences.Editor editor = sp.edit(); //because we are going to edit this file
        editor.putString("terminal",terminal); //saving into file under a key
        editor.commit(); //to save the information
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bar.setVisibility(View.GONE);
        dialog.dismiss();
    }

    public void statusBarColor()
    {
        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.orange)); //status bar or the time bar at the top (see example image1)

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.actionBarBackground)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }
    }
}