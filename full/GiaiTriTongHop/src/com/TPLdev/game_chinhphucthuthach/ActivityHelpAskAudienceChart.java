package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;
import java.util.Random;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class ActivityHelpAskAudienceChart extends Activity {
	ProgressBar progressBarA, progressBarB, progressBarC, progressBarD;
	TextView tv_a, tv_b, tv_c, tv_d, tv_survey;
	Button bt_yes;
	FileInputStream in;
	int numberToSetFont = 1;
	int indexA = 0, indexB = 0, indexC = 0, indexD = 0;// tuc la so nguoi

	// dong y voi dap an
	// a-b-c-d
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cptt_activity_help_ask_audience_chart);
		// anh xa
		progressBarA = (ProgressBar) findViewById(R.id.progressBara);
		progressBarB = (ProgressBar) findViewById(R.id.progressBarb);
		progressBarC = (ProgressBar) findViewById(R.id.progressBarc);
		progressBarD = (ProgressBar) findViewById(R.id.progressBard);
		tv_a = (TextView) findViewById(R.id.tv1);
		tv_b = (TextView) findViewById(R.id.tv2);
		tv_c = (TextView) findViewById(R.id.tv3);
		tv_d = (TextView) findViewById(R.id.tv4);
		tv_survey = (TextView) findViewById(R.id.tv_khaosat);
		bt_yes = (Button) findViewById(R.id.bt_yes);
		// tao so ngau nhien co 4 cai index
		createRandomNumber();
		
		progressBarA.setMax(100);
		progressBarA.setMax(100);
		progressBarA.setMax(100);
		progressBarA.setMax(100);
		
		progressBarA.setProgress(indexA);
		progressBarB.setProgress(indexB);
		progressBarC.setProgress(indexC);
		progressBarD.setProgress(indexD);
		
		setFont();
		bt_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer mp_Yes = MediaPlayer.create(
						getApplicationContext(), R.raw.bt_xacnhan);
				mp_Yes.start();
				ActivityHelpAskAudienceChart.this.finish();
			}
		});
	}

	void createRandomNumber() {
		//lay dap an tu intent
		String result = getIntent().getStringExtra("dapan");

		Random rd1 = new Random();
		Random rd2 = new Random();
		Random rd3 = new Random();
		if (result.equals("a")) {

			indexA = rd1.nextInt(100 - 40 + 1) + 40;
			indexB = rd2.nextInt(100 - indexA - 0 + 1) + 0;
			indexC = rd3.nextInt(100 - indexA - indexB - 0 + 1) + 0;
			indexD = 100 - indexA - indexB - indexC;
		}
		if (result.equals("b")) {

			indexB = rd1.nextInt(100 - 40 + 1) + 40;
			indexA = rd2.nextInt(100 - indexB - 0 + 1) + 0;
			indexC = rd3.nextInt(100 - indexB - indexA - 0 + 1) + 0;
			indexD = 100 - indexA - indexB - indexC;
		}
		if (result.equals("c")) {

			indexC = rd1.nextInt(100 - 40 + 1) + 40;
			indexB = rd2.nextInt(100 - indexC - 0 + 1) + 0;
			indexA = rd3.nextInt(100 - indexC - indexB - 0 + 1) + 0;
			indexD = 100 - indexA - indexB - indexC;
		}
		if (result.equals("d")) {

			indexD = rd1.nextInt(100 - 40 + 1) + 40;
			indexB = rd2.nextInt(100 - indexD - 0 + 1) + 0;
			indexC = rd3.nextInt(100 - indexD - indexB - 0 + 1) + 0;
			indexA = 100 - indexD - indexB - indexC;
		}

		tv_a.setText(indexA + "%\nA");
		tv_b.setText(indexB + "%\nB");
		tv_c.setText(indexC + "%\nC");
		tv_d.setText(indexD + "%\nD");
		// bt_yes.setText(dapan);
		// bt_yes.setText(dapan+"\n"+(indexa+indexb+indexc+indexd));
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
			tv_a.setTypeface(tf, Typeface.BOLD);
			tv_b.setTypeface(tf, Typeface.BOLD);
			tv_c.setTypeface(tf, Typeface.BOLD);
			tv_d.setTypeface(tf, Typeface.BOLD);
			tv_survey.setTypeface(tf, Typeface.BOLD);
			bt_yes.setTypeface(tf, Typeface.BOLD);
		} else {
			Typeface tf = Typeface.DEFAULT;
			tv_a.setTypeface(tf);
			tv_b.setTypeface(tf);
			tv_c.setTypeface(tf);
			tv_d.setTypeface(tf);
			tv_survey.setTypeface(tf);
			bt_yes.setTypeface(tf);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}
}
