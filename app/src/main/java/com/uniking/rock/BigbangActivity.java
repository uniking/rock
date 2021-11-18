package com.uniking.rock;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import jackmego.com.jieba_android.JiebaSegmenter;

/**
 * Created by wzl on 11/17/21.
 */

public class BigbangActivity extends Activity {
    private LinearLayout wordsShow;

    ArrayList<String> filter(ArrayList<String> wordList){
        ArrayList<String> newwordList = new ArrayList<>();

        String preWord = "";
        for (String word:wordList){
            if(word.equals(" ") && preWord.equals(" ")){
                continue;
            }
            if(word.equals("\n") || word.equals("\r")){
                continue;
            }

            preWord=word;
            newwordList.add(word);
        }

        return newwordList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.divided_sentence_layout, null);
        setContentView(bottomSheetView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View bottomSheetView = getLayoutInflater().inflate(R.layout.divided_sentence_layout, null);
        setContentView(bottomSheetView);

        ArrayList<String> wordList = new ArrayList<>();
        try {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cd = cmb.getPrimaryClip();
            if (null != cd && cd.getItemCount()>=1)
            {
                String text = cd.getItemAt(0).getText().toString();
                if(text.length() > 0){
                    wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(text);
                }

                DivideCard divideCard = bottomSheetView.findViewById(R.id.divide_layout);
                divideCard.setWords(filter(wordList));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}
