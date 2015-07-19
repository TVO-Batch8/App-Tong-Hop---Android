package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class ActivityHelpChangeQuestion extends Activity {
	TextView tv;
	Button bt_yes, bt_no;
	FileInputStream in;
	int numberToSetFont = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cptt_activity_help_change_question);
		tv = (TextView) findViewById(R.id.tv_namePerson);
		bt_yes = (Button) findViewById(R.id.bt_yes);
		bt_no = (Button) findViewById(R.id.bt_no);
		tv.setText("Sẽ mất 10 tiền nếu bạn muốn đổi sang câu hỏi khác, bạn có chắc không?");
		//
		setFont();
		//nhan yes, bat nhac 1 lan
		//gui du lieu ve qua iReturn va ngung activity
		bt_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer mp_yes = MediaPlayer.create(
						getApplicationContext(), R.raw.bt_yes);
				mp_yes.start();
				Intent iReturn = new Intent();
				iReturn.putExtra("yes", "yes");
				setResult(RESULT_OK, iReturn);
				ActivityHelpChangeQuestion.this.finish();
			}
		});
		bt_no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer mp_no = MediaPlayer.create(getApplicationContext(),
						R.raw.bt_no);
				mp_no.start();
				ActivityHelpChangeQuestion.this.finish();
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
			bt_no.setTypeface(tf, Typeface.BOLD);
		} else {
			Typeface tf = Typeface.DEFAULT;
			tv.setTypeface(tf);
			bt_yes.setTypeface(tf);
			bt_no.setTypeface(tf);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}
}
