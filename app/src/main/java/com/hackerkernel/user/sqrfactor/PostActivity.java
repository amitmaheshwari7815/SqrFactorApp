package com.hackerkernel.user.sqrfactor;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

public class PostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_top,R.anim.slide_in_top);

    }
}
