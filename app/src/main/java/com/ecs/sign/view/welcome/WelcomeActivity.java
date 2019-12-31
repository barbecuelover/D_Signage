package com.ecs.sign.view.welcome;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.ecs.sign.view.main.MainActivity;
import com.ecs.sign.R;
import com.ecs.sign.base.activity.BaseActivity;
import com.ecs.sign.model.DataManager;
import com.ecs.sign.presenter.welcome.WelcomeContract;
import com.ecs.sign.presenter.welcome.WelcomePresenter;

import butterknife.BindView;


public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements WelcomeContract.WelcomeView , View.OnClickListener{

    @BindView(R.id.welcome_video_view)
    VideoView videoView;
    @BindView(R.id.jump_btn)
    Button btnSkip;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置当前窗体为全屏显示
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, flag);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new WelcomePresenter(DataManager.getInstance(getApplicationContext()));
    }

    @Override
    protected void initView() {

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        btnSkip.setOnClickListener(this);
    }


    private void  goHomePage(){
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void playWelcomeVideoAndCountDown() {

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + R.raw.welcome_ani);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);

                int duration  = mp.getDuration();
                videoView.requestFocus();
                videoView.start();

                countDownTimer =new SkipCountDownTimer(duration,1000);
                countDownTimer.start();
                btnSkip.setVisibility(View.VISIBLE);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                goHomePage();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.jump_btn) {
            goHomePage();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        if (videoView!=null){
            videoView.suspend();
        }
    }

    /**
     * 倒计时计时器
     */
    private class SkipCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public SkipCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnSkip.setText("跳过(" + millisUntilFinished / 1000 + "s)");
        }

        @Override
        public void onFinish() {
            btnSkip.setText("跳过(" + 0 + "s)");
        }
    }
}
