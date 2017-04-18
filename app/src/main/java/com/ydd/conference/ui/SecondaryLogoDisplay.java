package com.ydd.conference.ui;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;

import com.ydd.conference.R;

import java.util.List;

/**
 * Created by eric on 2016/2/23.
 */
public class SecondaryLogoDisplay extends Presentation {

    private TextView nameText;

    public SecondaryLogoDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    /**
     * 显示副屏幕的内容
     *
     * @param contents
     */
    public void load(List<String> contents) {
        this.show();
        nameText.setText(getLogoTitle(contents));
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_logo_secondary);
        nameText = (TextView) findViewById(R.id.logoTitle);
    }

    @Override
    public void setOnShowListener(OnShowListener listener) {
        super.setOnShowListener(listener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private String getLogoTitle(List<String> contents) {
        String textName = "";
        if (null != contents) {
            for (int i = 0; i < contents.size(); i++) {
                textName += contents.get(i);
                if (i < contents.size() - 1) {
                    textName += "\n";
                }
            }
        }
        return textName;
    }

}
