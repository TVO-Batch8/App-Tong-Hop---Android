package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;

public class ActivityHelloGame extends Activity {
	ImageView ivStart, ivSetting, ivHightScore;
	Intent mIntent;
	// dong chu ten tac gia tren man hinh hello
	TextView tvDev;
	// am nhac
	MediaPlayer mMediaPlayer;
	// hieu ung
	Animation mAnimation;
	FileInputStream in;
	// mac dinh la 1
	int numberToSetMusic = 1, numberToSetFont = 1;
	// luu so tien
	int numOfMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cptt_activity_hello_game);
		// anh xa
		tvDev = (TextView) findViewById(R.id.tvDev);
		ivStart = (ImageView) findViewById(R.id.ivStart);
		ivSetting = (ImageView) findViewById(R.id.ivSetting);
		ivHightScore = (ImageView) findViewById(R.id.ivHightScore);
		// khi nhan nut start, chuyen sang Activity_Main, ngung nhac nen hien
		// tai
		ivStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mIntent = new Intent(getApplicationContext(),
						ActivityMain.class);
				startActivity(mIntent);
				mMediaPlayer.stop();
			}
		});
		// chuyen sang activity cai dat, ngung nhac nen lai
		ivSetting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mIntent = new Intent(getApplicationContext(),
						ActivitySetting.class);
				startActivity(mIntent);
				mMediaPlayer.stop();
			}
		});
		// chuyen sang activity xem diem cao, ngung nhac nen lai
		ivHightScore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mIntent = new Intent(getApplicationContext(),
						ActivityHightScore.class);
				startActivity(mIntent);
			}
		});

		// ham set font
		setFont();
		// kiem tra thiet bi co ket noi hay ko
		checkInternet();
		// thuc hien cac hieu ung
		setAnimation();
	}

	// khi nhan back, tat nhac hien tai
	@Override
	public void onBackPressed() {
		mMediaPlayer.stop();
		super.onBackPressed();
	}

	// ham load du lieu font roi luu vao numberToSetFont, phuc vu cho ham
	// setFont ben duoi
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

	// dua vao numberToSetFont de chinh sua font
	void setFont() {
		loadFontSetting();
		if (numberToSetFont == 1) {
			Typeface tf = Typeface.createFromAsset(
					ActivityHelloGame.this.getAssets(), "SEGOEUI.TTF");
			tvDev.setTypeface(tf, Typeface.BOLD);
		} else {
			Typeface tf = Typeface.DEFAULT;
			tvDev.setTypeface(tf);
		}

	}

	// ham load du lieu music roi luu vao numberToSetMusic, phuc vu cho ham
	// setMusic ben duoi
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

	// cai dat am nhac dua vao numberToSetMusic
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

	// set hieu ung
	void setAnimation() {
		mAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.scale_button);
		// tvDev.setAnimation(mAnimation);
		ivStart.setAnimation(mAnimation);
		ivSetting.setAnimation(mAnimation);
		ivHightScore.setAnimation(mAnimation);
	}

	// chinh lai nhac, font khi resume
	@Override
	protected void onResume() {
		setMusic();
		setFont();
		super.onResume();
	}

	// ngung thi tat nhac
	@Override
	protected void onPause() {
		mMediaPlayer.pause();
		super.onPause();
	}

	// destroy tat nhac luon
	@Override
	protected void onDestroy() {
		mMediaPlayer.stop();
		super.onDestroy();
	}

	// ham kiem tra internet, neu ko co ket noi thi thong bao du nguoi dung ket
	// noi internet di,
	// neu co ket noi thi goi gam loadNumberOfMoney de biet so tien bao nhieu,
	// roi +10, sau do save lai
	void checkInternet() {
		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			// notify user you are online
			loadNumOfMoney();
			numOfMoney = numOfMoney + 10;
			saveNumOfMoney();
			Toast.makeText(
					getApplicationContext(),
					getResources().getString(R.string.strCPTTInternetOn) + " "
							+ numOfMoney, Toast.LENGTH_LONG).show();
		} else {
			// notify user you are not online
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.strCPTTInternetOff),
					Toast.LENGTH_LONG).show();
		}
	}
	//ham load tien phuc vu cho ham checkInternet
	void loadNumOfMoney() {
		try {
			in = openFileInput("dulieutien.txt");
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			String x = new String(buffer);
			in.close();
			numOfMoney = Integer.parseInt(x);
		} catch (Exception e) {
		}
	}
	//ham save tien phuc vu cho gam checkInternet
	void saveNumOfMoney() {
		try {
			FileOutputStream out = openFileOutput("dulieutien.txt",
					MODE_PRIVATE);
			String x = numOfMoney + "";
			out.write(x.getBytes());
			out.close();
		} catch (Exception e) {
		}
	}
}
