package com.ydd.conference.ui;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydd.conference.R;
import com.ydd.conference.config.Constant;
import com.ydd.conference.util.SharedPreferencesUtil;
import com.ydd.conference.util.StringUtils;

/**
 * Created by eric on 2016/2/23.
 */
public class SecondaryDisplay extends Presentation {

    private final int width;
    private final int height;
    private TextView nameText;
    private View backgroundView;
    private ImageView logoImage;

    public SecondaryDisplay(Context outerContext, Display display) {
        super(outerContext, display);
        width = display.getWidth();
        height = display.getHeight();
    }


    /**
     * 显示副屏幕的内容
     *
     * @param textName
     */
    public void showName(String textName) {
        this.show();
//        textName = "上海市第十四次人大常委会\n第二十八次会议";
        //如果没有排座,有徽标内容的话，则显示徽标
        if (StringUtils.isEmpty(textName) && null != Constant.LOGO_TEXT) {
            textName = getLogText();
        }
//        nameText.setGravity(Gravity.NO_GRAVITY);

        //带职务的终端副屏显示效果
        if (StringUtils.isNotEmpty(SharedPreferencesUtil.getDuty()) && Constant.isShowDuty) {
            textName = SharedPreferencesUtil.getDuty();
        }
//        System.out.println("secondaryDisplay--" + SharedPreferencesUtil.getAskForLevel());
//        System.out.println("getVotingRights--" + SharedPreferencesUtil.getVotingRights());
//        System.out.println("getRegisterStatus--" + SharedPreferencesUtil.getRegisterStatus());
        //add by lt 请假的情况
        if (SharedPreferencesUtil.getVotingRights() == Constant.HAS_VOTE_RIGHT) {
            if (SharedPreferencesUtil.getAskForLevel() == 1) {
                if (SharedPreferencesUtil.getRegisterStatus() == Constant.STATUS_REGISTER) {
                    textName = SharedPreferencesUtil.getMemberName();
                } else {
                    textName = getLogText();
                }
            }
        }
//        System.out.println("textName:" + textName);


        //正式终端
        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
            if (textName.length() < 5) {
                nameTextsetTextSize(230);
            } else if (textName.length() == 5) {
                nameTextsetTextSize(180);
            } else if (textName.length() == 6) {
                nameTextsetTextSize(150);
            } else if (textName.length() == 7) {
                nameTextsetTextSize(130);
            } else if (textName.length() == 8) {
                nameTextsetTextSize(100);
            } else if (textName.length() == 9) {
                nameTextsetTextSize(90);
            } else if (textName.length() == 10) {
                nameTextsetTextSize(90);
            } else if (textName.length() > 10) {
                nameTextsetTextSize(70);
//                nameText.setTextSize(75);
                nameText.setGravity(Gravity.CENTER);
            }
        } else {
            if (textName.length() < 4) {
                nameTextsetTextSize(260);
            } else if (textName.length() == 4) {
                nameTextsetTextSize(230);
            } else if (textName.length() == 5) {
                nameTextsetTextSize(180);
            } else if (textName.length() == 6) {
                nameTextsetTextSize(140);
            } else if (textName.length() == 7) {
                nameTextsetTextSize(130);
            } else if (textName.length() == 8) {
                nameTextsetTextSize(110);
            } else if (textName.length() == 9) {
                nameTextsetTextSize(100);
            } else if (textName.length() == 10) {
                nameTextsetTextSize(90);
            } else if (textName.length() > 10) {
                nameTextsetTextSize(65);
//                nameText.setTextSize(70);
                nameText.setGravity(Gravity.CENTER);
            } else {
                nameTextsetTextSize(70);
            }
        }
        nameText.setText(textName);

    }

    private void nameTextsetTextSize(int i) {
        if (width == 1280 && height == 800) {
            nameText.setTextSize(i - 5);
        } else {
            nameText.setTextSize(i);
        }
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if (width == 1280 && height == 800) {
            setContentView(R.layout.activity_name_1280x800);
        } else if (width == 1366 && height == 768) {
            setContentView(R.layout.activity_name_1366_2);
        } else {
            setContentView(R.layout.activity_name);
        }
        nameText = (TextView) findViewById(R.id.nameText);
        backgroundView = findViewById(R.id.backgroundView);
        logoImage = (ImageView) findViewById(R.id.logoImage);
        if (SharedPreferencesUtil.getTerminalType().equals(Constant.Terminal_FIRST)) {
            float fact = 0.9f;
            backgroundView.setBackgroundColor(Color.rgb((int) (238 * fact), (int) (59 * fact), (int) (59 * fact)));
//            changeImage(90);
        } else {
//            logoImage.setImageResource(R.mipmap.icon_logo_new);
        }
        logoImage.setImageResource(R.mipmap.icon_logo_new_5);
       /* if (width == 1280 && height == 800) {
            logoImage.setImageResource(R.mipmap.icon_logo_big);
        } else {
            logoImage.setImageResource(R.mipmap.icon_logo_new);
        }*/
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


    private String getLogText() {
        String textName = "";
        if (null != Constant.LOGO_TEXT) {
            for (int i = 0; i < Constant.LOGO_TEXT.size(); i++) {
                textName += Constant.LOGO_TEXT.get(i);
                if (i < Constant.LOGO_TEXT.size() - 1) {
                    textName += "\n";
                }
            }
        }
        return textName;
    }

    private void changeImage(int progress) {
        if (bitmap == null) {
            Bitmap srcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo_new);
            int imgHeight = srcBitmap.getHeight();
            int imgWidth = srcBitmap.getWidth();
            bitmap = Bitmap.createBitmap(imgWidth, imgHeight,
                    Bitmap.Config.ARGB_8888);
            int brightness = progress;
            ColorMatrix cMatrix = new ColorMatrix();
            cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1,
                    0, 0, brightness,// 改变亮度
                    0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});

            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

            Canvas canvas = new Canvas(bitmap);
            // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
            canvas.drawBitmap(srcBitmap, 0, 0, paint);
        }

        logoImage.setImageBitmap(bitmap);
    }


    private static Bitmap bitmap = null;


}
