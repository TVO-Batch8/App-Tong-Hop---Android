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
import android.widget.ImageView;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class ActivityShowSuggest extends Activity {
	ImageView iv;
	TextView tv_namePerson, tv_contentSuggest;
	Button bt_yes;
	FileInputStream in;
	int numberToSetFont = 1;

	String alSuggest[] = {
			"Ban đầu tôi nghĩ đáp án có thể là câu A, nhưng...",
			"Ban đầu tôi nghĩ đáp án có thể là câu B, nhưng...",
			"Ban đầu tôi nghĩ đáp án có thể là câu C, nhưng...",
			"Ban đầu tôi nghĩ đáp án có thể là câu D, nhưng...",
			"Tôi nghĩ bạn nên gọi cho Developer, anh ta biết tất cả mọi đáp án :)",
			"Thay vì gọi cho tôi, bạn nên gọi cho EINSTEIN, ông ta rất giỏi...",
			"Sao lại gọi cho tôi, bạn nghĩ tôi biết trả lời cho câu hỏi khó như vầy á?...",
			"Bạn nên gọi cho Developer thì hay hơn",
			"Tôi đang bận lắm, sao lại gọi cho tôi vào lúc này?",
			"Hãy dùng quyền trợ giúp THAM KHẢO Ý KIẾN NGƯỜI THÂN.",
			"Câu này thật sự rất khó, bạn thử trả lời đại xem sao.",
			"Chắc chắn A là câu trả lời đúng.",
			"Chắc chắn B là câu trả lời đúng.",
			"Chắc chắn C là câu trả lời đúng.",
			"Chắc chắn D là câu trả lời đúng.",
			"Chắc chắn A là câu trả lời đúng... Tôi biết vì đã đọc câu hỏi này những 3 lần rồi...",
			"Chắc chắn B là câu trả lời đúng... Tôi biết vì đã đọc câu hỏi này những 3 lần rồi...",
			"Bó tay! Câu hỏi này thật sự quá khó...",
			"Tôi nghĩ bạn nên ngừng cuộc chơi...",
			"Câu này lạ quá, tôi mới nghe lần đầu...",
			"Tôi không biết...Bạn tự suy nghĩ đi...",
			"Hãy ngừng cuộc chơi... Biết người biết ta, trăm trận trăm thắng...",
			"Tôi không biết!", "Tôi nghĩ bạn nên dùng quyền trợ giúp khác...",
			"Tôi khuyên bạn nên ngừng cuộc chơi",
			"Câu này quá khó... Tôi chịu thua!" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cptt_activity_show_suggest);
		// anh xa
		bt_yes = (Button) findViewById(R.id.bt_yes);
		iv = (ImageView) findViewById(R.id.imageView1);
		tv_namePerson = (TextView) findViewById(R.id.tv_namePerson);
		tv_contentSuggest = (TextView) findViewById(R.id.tv_contentSuggest);
		// lay du lieu qua intent
		int image = Integer.parseInt(getIntent().getStringExtra("hinh"));
		String name = getIntent().getStringExtra("ten");
		String result = getIntent().getStringExtra("dapan");

		tv_namePerson.setText(name);
		iv.setImageResource(image);
		// set font
		setFont();
		// neu hoi developer hoac einstein thi 100% tra loi dung
		// neu khong phai thi se show suggest ngau nhien
		if (name.equals("DEVELOPER") || name.equals("EINSTEIN")) {
			tv_contentSuggest.setText("Hãy tin tôi đi, " + result
					+ " 96,69% là câu trả lời đúng!");
		} else {
			Random r = new Random();
			int index = r.nextInt(alSuggest.length);
			tv_contentSuggest.setText(alSuggest[index]);
		}
		//bat nhac va ngung
		bt_yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MediaPlayer mpYes = MediaPlayer.create(
						getApplicationContext(), R.raw.bt_yes);
				mpYes.start();
				ActivityShowSuggest.this.finish();
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
		if (numberToSetFont == 1) {
			Typeface tf = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "SEGOEUI.TTF");
			tv_namePerson.setTypeface(tf, Typeface.BOLD);
			bt_yes.setTypeface(tf, Typeface.BOLD);
			tv_contentSuggest.setTypeface(tf, Typeface.BOLD);
		} else {
			Typeface tf = Typeface.DEFAULT;
			tv_namePerson.setTypeface(tf);
			bt_yes.setTypeface(tf);
			tv_contentSuggest.setTypeface(tf);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}
}
