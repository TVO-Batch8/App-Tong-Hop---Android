package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class ActivityHightScore extends Activity {
	ImageView ivBack;
	TextView tvHightScore, tvNumOfMoney;
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
	// xml set hinh nen
	RelativeLayout xmlMain;
	LinearLayout ln_HightScore;
	MediaPlayer mMediaPlayer;
	// 2 hinh anh hien thi
	ImageView iv1, iv2;
	FileInputStream in;
	int numOfMoney, hightScore;
	Animation myAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cptt_activity_hight_score);
		// anh xa
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tvHightScore = (TextView) findViewById(R.id.tvHightScore);
		tvNumOfMoney = (TextView) findViewById(R.id.tvNumOfMoney);
		xmlMain = (RelativeLayout) findViewById(R.id.xmlMain);
		ln_HightScore = (LinearLayout) findViewById(R.id.ln_HightScore);
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		// set am nhac, picture ngau nhien, set font, set hieu ung
		setMusic();
		setPictureRandomInImageView();
		setFont();
		setAnimation();
		// load diem cao, load so tien
		loadHightScore();
		loadNumOfMoney();
		// tao ngau nhien index random de set background
		Random r = new Random();
		int index = r.nextInt(arrPictureBackground.length);
		xmlMain.setBackgroundResource(arrPictureBackground[index]);
		//
		ivBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mMediaPlayer.stop();
				ActivityHightScore.this.finish();
			}
		});
	}

	// set music
	void setMusic() {
		int music[] = { R.raw.background0, R.raw.background1,
				R.raw.background2, R.raw.background3, R.raw.background4,
				R.raw.background5, R.raw.background6, R.raw.background7 };
		Random r = new Random();
		int index = r.nextInt(music.length);
		mMediaPlayer = MediaPlayer
				.create(getApplicationContext(), music[index]);
		mMediaPlayer.start();
		mMediaPlayer.setLooping(true);
	}

	// set picture ngau nhien
	void setPictureRandomInImageView() {
		int hinh[] = { R.drawable.vui1, R.drawable.vui2, R.drawable.vui3,
				R.drawable.vui4, R.drawable.vui5, R.drawable.vui6,
				R.drawable.vui7, R.drawable.vui8, R.drawable.vui9,
				R.drawable.vui10, R.drawable.vui11, R.drawable.vui12 };
		//
		Random r1 = new Random();
		int index1 = r1.nextInt(hinh.length);
		iv1.setImageResource(hinh[index1]);
		//
		Random r2 = new Random();
		int index2 = r2.nextInt(hinh.length);
		iv2.setImageResource(hinh[index2]);
	}

	void setFont() {
		Typeface tf = Typeface.createFromAsset(
				ActivityHightScore.this.getAssets(), "SEGOEUI.TTF");
		tvHightScore.setTypeface(tf, Typeface.BOLD);
		tvNumOfMoney.setTypeface(tf, Typeface.BOLD);
	}

	void loadHightScore() {
		try {
			in = openFileInput("dulieudiem.txt");
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			String x = new String(buffer);
			in.close();
			hightScore = Integer.parseInt(x);
			tvHightScore.setText(getResources().getString(
					R.string.strCPTTDiemCao)
					+ "\n" + hightScore);
		} catch (Exception e) {
		}
	}

	void loadNumOfMoney() {
		try {
			in = openFileInput("dulieutien.txt");
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			String x = new String(buffer);
			in.close();
			numOfMoney = Integer.parseInt(x);
			tvNumOfMoney.setText(getResources().getString(
					R.string.strCPTTDiemCaoTien)
					+ "\n" + numOfMoney);
		} catch (Exception e) {
		}
	}

	void setAnimation() {
		myAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.ln_cauhoi);
		ln_HightScore.setAnimation(myAnimation);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}
}
