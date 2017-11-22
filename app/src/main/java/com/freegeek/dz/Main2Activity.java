package com.freegeek.dz;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.freegeek.dz.view.Keyboard;
import com.freegeek.dz.view.RadixEditText;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    Intent intent;

    private AlertDialog radixPickerDialog;
    private TextView mTvLabelMisc;
    private RadixEditText et_misc;
    private RadixEditText mCurrentEditText;
    private Keyboard mKeyboard;
    private ClipboardManager clipboardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button bt12 = (Button) findViewById(R.id.button12);
        bt12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();

    }


    private void initView() {
        mTvLabelMisc = (TextView) findViewById(R.id.tv_label_misc);
        mKeyboard = (Keyboard) findViewById(R.id.keyboard);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        final String radix[] = {"3", "4", "5", "6", "7", "8", "9", "11", "12", "13", "14", "15"};
        radixPickerDialog = new AlertDialog.Builder(this).setItems(radix, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTvLabelMisc.setText(radix[i]);
                ((RadixEditText) findViewById(R.id.et_misc))
                        .setRadix(Integer.parseInt(radix[i]));
            }
        }).setNegativeButton(R.string.cancel, null).create();

        mTvLabelMisc.setOnClickListener(this);
        mKeyboard.setVisibility(View.GONE);
        mKeyboard.setOnKeyboardListener(new Keyboard.OnKeyboardListener() {
            @Override
            public void onKeyboardClickListener(Button button, String value) {
                if(value.equalsIgnoreCase("DEL")){
                    String data =  mCurrentEditText.getText().toString();
                    if(data.trim().length() != 0){
                        mCurrentEditText.setText(data.substring(0, data.length() - 1));
                        mCurrentEditText.setSelection(mCurrentEditText.getText().length());
                    }
                }else if(value.toUpperCase().equals("COPY")){
                    clipboardManager.setText(mCurrentEditText.getText().toString().replaceAll(" ", ""));
                    Toast.makeText(Main2Activity.this, getString(R.string.done), Toast.LENGTH_SHORT).show();
                }else if(value.equals(".")){
                    if(!mCurrentEditText.getText().toString().contains(".")) mCurrentEditText.append(value);
                }else{
                    mCurrentEditText.append(value);
                }

            }

            @Override
            public void onKeyboardLongClickListener(Button button, String value) {
                if(value.equalsIgnoreCase("DEL")){
                    mCurrentEditText.setText("");
                }
            }
        });

        for (RadixEditText radixEditText : RadixEditText.getRadixEditTexts()) {
            radixEditText.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_UP:
                            RadixEditText radixEditText = (RadixEditText) v;
                            mKeyboard.setRadix(radixEditText.getRadix());
                            mKeyboard.show();
                            mCurrentEditText = radixEditText;
                            updateColor(radixEditText.getRadix());
                            break;
                    }
                    return false;
                }
            });
        }

    }


    private void updateColor(int radix){
        int colorHint = getResources().getColor(R.color.textColorHint);
        int white = getResources().getColor(R.color.white);
        int colorPrimary = getResources().getColor(R.color.colorPrimary);
        for (RadixEditText radixEditText : RadixEditText.getRadixEditTexts()) {
            if(radixEditText.getRadix() == radix){
                int color = radix == 10 ? white : colorPrimary;
                radixEditText.setTextColor(color);
                radixEditText.setHintTextColor(color);
                continue;
            }
            if(radixEditText.getRadix() == 10){
                radixEditText.setHintTextColor(colorHint);
                radixEditText.setTextColor(colorHint);
                continue;
            }
            radixEditText.setHintTextColor(0xFFD4D4D4);
            radixEditText.setTextColor(0xFF616161);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_label_misc:
                radixPickerDialog.show();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 4 && mKeyboard.isShowing()){
            mKeyboard.hide();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
