package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class ActivityToastLostMoney extends Activity {
	ImageView iv;
	TextView tv;
	Button bt_yes;
	FileInputStream in;
	int numberToSetFont = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cptt_activity_toast_lost_money);
		// anh xa
		iv = (ImageView) findViewById(R.id.imageView1);
		bt_yes = (Button) findViewById(R.id.bt_yes);
		tv = (TextView) findViewById(R.id.tv_namePerson);
		// set font
		setFont();
		// lay du lieu tu intent
		int wrongImage = Integer.parseInt(getIntent().getStringExtra("hinh"));
		String toast = getIntent().getStringExtra("thongbao");
		tv.setText(toast);
		iv.setImageResource(wrongImage);
		// bat nhac va ngung activity
		bt_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer mpYes = MediaPlayer.create(getApplicationContext(),
						R.raw.bt_xacnhan);
				mpYes.start();
				ActivityToastLostMoney.this.finish();
			}
		});
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

	void setFont() {
		loadFontSetting();
		if (numberToSetFont == 1) {
			Typeface tf = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "SEGOEUI.TTF");
			tv.setTypeface(tf, Typeface.BOLD);
			bt_yes.setTypeface(tf, Typeface.BOLD);
		} else {
			Typeface tf = Typeface.DEFAULT;
			tv.setTypeface(tf);
			bt_yes.setTypeface(tf);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}
}
