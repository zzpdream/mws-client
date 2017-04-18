package com.ydd.conference.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ydd.conference.R;

/**
 * Created by zhushouwen on 2017/3/24.
 */

public class ImageActivity extends BaseActivity {
    private ImageView imageView;
    private String imageUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initData();
        initView();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        ImageLoader.getInstance().displayImage(imageUrl,imageView);
    }

    private void initData() {
        imageUrl = getIntent().getStringExtra("imageUrl");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
        initView();
    }
    public static void actionStart(Context context,String imageUrl){
        Intent intent = new Intent(context,ImageActivity.class);
        intent.putExtra("imageUrl",imageUrl);
        context.startActivity(intent);
    }
}
