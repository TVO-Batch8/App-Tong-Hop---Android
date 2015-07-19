package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class ActivitySetting extends Activity {
	ImageView ivBack;
	TextView tvSettingGame, tvMusic1, tvMusic2, tvFont1, tvFont2;
	CheckBox cbMusic, cbFont;
	// xml main
	RelativeLayout xmlMain;
	LinearLayout lnSetting;
	int arrPictureBackground[] = { R.drawable.bg1, R.drawable.bg2,
			R.drawable.bg3, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6,
			R.drawable.bg7, R.drawable.bg8, R.drawable.bg9, R.drawable.bg10,
			R.drawable.bg11, R.drawable.bg12, R.drawable.bg13, R.drawable.bg14,
			R.drawable.bg15, R.drawable.bg16, R.drawable.bg17, R.drawable.bg18,
			R.drawable.bg19, R.drawable.bg20, R.drawable.bg21, R.drawable.bg22,
			R.drawable.bg22, R.drawable.bg23, R.drawable.bg24, R.drawable.bg25,
			R.drawable.bg26, R.drawable.bg27, R.drawable.bg28, R.drawable.bg29,
			R.drawable.bg30, R.drawable.bg31, R.drawable.bg32, R.drawable.bg33,
			R.drawable.bg34, R.drawable.bg35, R.drawable.bg36, R.drawable.bg37,
			R.drawable.bg38, R.drawable.bg39, R.drawable.bg40, R.drawable.bg41,
			R.drawable.bg42, R.drawable.bg43, R.drawable.bg44, R.drawable.bg45,
			R.drawable.bg46 };
	MediaPlayer mMediaPlayer;
	Animation myAnimation;
	//
	FileInputStream in;
	int numberToSetMusic, numberToSetFont;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cptt_activity_setting);
		// anh xa
		ivBack = (ImageView) findViewById(R.id.ivBack);
		xmlMain = (RelativeLayout) findViewById(R.id.xmlMain);
		lnSetting = (LinearLayout) findViewById(R.id.lnSetting);
		tvSettingGame = (TextView) findViewById(R.id.tvSettingGame);
		tvMusic1 = (TextView) findViewById(R.id.tvMusic1);
		tvMusic2 = (TextView) findViewById(R.id.tvMusic2);
		tvFont1 = (TextView) findViewById(R.id.tvFont1);
		tvFont2 = (TextView) findViewById(R.id.tvFont2);
		cbFont = (CheckBox) findViewById(R.id.cbFont);
		cbMusic = (CheckBox) findViewById(R.id.cbMusic);
		//
		Random r = new Random();
		int index = r.nextInt(arrPictureBackground.length);
		
		try {
			xmlMain.setBackgroundResource(arrPictureBackground[index]);
		} catch (Exception e) {
		}

		//
		setMusic();
		setFont();
		setAnimation();
		//
		ivBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMediaPlayer.stop();
				saveMusicSetting();
				saveFontSetting();
				ActivitySetting.this.finish();
			}
		});
		//
		loadSetting();
	}

	void setMusic() {
		loadMusicSetting();
		if (numberToSetMusic == 1) {
			int music[] = { R.raw.background0, R.raw.background1,
					R.raw.background2, R.raw.background3, R.raw.background4,
					R.raw.background5, R.raw.background6, R.raw.background7 };
			Random r = new Random();
			int index = r.nextInt(music.length);
			mMediaPlayer = MediaPlayer.create(getApplicationContext(),
					music[index]);
			mMediaPlayer.start();
			mMediaPlayer.setLooping(true);
		} else {
			mMediaPlayer = MediaPlayer.create(getApplicationContext(),
					R.raw.background_no);
			mMediaPlayer.start();
		}

	}

	void setFont() {
		loadFontSetting();
		if (numberToSetFont == 1) {
			Typeface tf = Typeface.createFromAsset(
					ActivitySetting.this.getAssets(), "SEGOEUI.TTF");
			tvSettingGame.setTypeface(tf, Typeface.BOLD);
			tvMusic1.setTypeface(tf, Typeface.BOLD);
			tvMusic2.setTypeface(tf, Typeface.BOLD);
			tvFont1.setTypeface(tf, Typeface.BOLD);
			tvFont2.setTypeface(tf, Typeface.BOLD);
			cbFont.setTypeface(tf, Typeface.BOLD);
			cbMusic.setTypeface(tf, Typeface.BOLD);

		} else {
			Typeface tf = Typeface.DEFAULT;
			tvSettingGame.setTypeface(tf);
			tvMusic1.setTypeface(tf);
			tvMusic2.setTypeface(tf);
			tvFont1.setTypeface(tf);
			tvFont2.setTypeface(tf);
			cbFont.setTypeface(tf);
			cbMusic.setTypeface(tf);

		}

	}

	void setAnimation() {
		myAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.ln_cauhoi);
		lnSetting.setAnimation(myAnimation);
	}

	@Override
	public void onBackPressed() {
		// mp.stop();
		// super.onBackPressed();
	}

	void saveMusicSetting() {
		if (cbMusic.isChecked()) {
			try {
				FileOutputStream out = openFileOutput("dulieunhac.txt",
						MODE_PRIVATE);
				String x = "1";
				out.write(x.getBytes());
				out.close();
			} catch (Exception e) {

			}
		} else {
			try {
				FileOutputStream out = openFileOutput("dulieunhac.txt",
						MODE_PRIVATE);
				String x = "0";
				out.write(x.getBytes());
				out.close();
			} catch (Exception e) {

			}
		}

	}

	void saveFontSetting() {
		if (cbFont.isChecked()) {
			try {
				FileOutputStream out = openFileOutput("dulieufont.txt",
						MODE_PRIVATE);
				String x = "1";
				out.write(x.getBytes());
				out.close();
			} catch (Exception e) {

			}
		} else {
			try {
				FileOutputStream out = openFileOutput("dulieufont.txt",
						MODE_PRIVATE);
				String x = "0";
				out.write(x.getBytes());
				out.close();
			} catch (Exception e) {

			}
		}

	}

	void loadSetting() {
		loadFontSetting();
		loadMusicSetting();
		if (numberToSetMusic == 1) {
			cbMusic.setChecked(true);
		} else
			cbMusic.setChecked(false);
		if (numberToSetFont == 1) {
			cbFont.setChecked(true);
		} else
			cbFont.setChecked(false);
	}

	void loadMusicSetting() {
		try {
			in = openFileInput("dulieunhac.txt");
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			String x = new String(buffer);
			in.close();
			numberToSetMusic = Integer.parseInt(x);
		} catch (Exception e) {
		}
	}

	void loadFontSetting() {
		try {
			in = openFileInput("dulieufont.txt");
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			String x = new String(buffer);
			in.close();
			numberToSetFont = Integer.parseInt(x);
		} catch (Exception e) {
		}
	}
}
