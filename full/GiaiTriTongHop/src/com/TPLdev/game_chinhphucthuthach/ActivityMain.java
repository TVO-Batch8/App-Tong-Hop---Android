package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;
import com.capricorn.RayMenu;

public class ActivityMain extends Activity {
	RelativeLayout xmlMain, rlMoneyAndScore;
	LinearLayout lnQuestion, lnSupport;
	// la so cau tra loi dung trong moi lan choi
	private int mScore;
	// muon su dung quyen tro giup phai ton tien
	private int mMoney;
	// doc file kyluc
	FileInputStream in;
	private int numberToSetMusic = 1, numberToSetFont = 1;
	// la diem ky luc duoc luu xuong file du lieu
	private int hightScore;
	TextView tvQuestion, tvScore, tvNumOfMoney;
	Button btQuestionA, btQuestionB, btQuestionC, btQuestionD;
	// quan ly database
	QuestionManagerDatabase dbQuestionManager;
	ArrayList<OneQuestionObject> alQuestions = new ArrayList<OneQuestionObject>();
	String question = "", answerA = "", answerB = "", answerC = "",
			answerD = "";

	Intent intent;
	private static int REQUESTCODE_1 = 000;// doi cau hoi
	private static int REQUESTCODE_2 = 111;// goi dien thoai
	private static int REQUESTCODE_3 = 222;// hen xui
	private static int REQUESTCODE_4 = 333;// hoi y kien khan gia

	Animation mAnimation;

	MediaPlayer mMediaPlayer;
	MediaPlayer mp_toast;
	// progressbar dem gio 60s
	ProgressBar mProgressBar;
	CountDownTimer t, t_second;
	TextView tvTimeCount;
	// FrameLayout sai
	FrameLayout flWrong;
	ImageView flWrong_ivWrong;
	TextView flWrong_tvScore, flWrong_tvHightScore, flWrong_tvResult,
			flWrong_tvAddMoney, flWrong_tvLose;
	Button flWrong_btReplay, flWrong_btExit;
	// FrameLayout dung
	FrameLayout flRight;
	ImageView flRight_ivRight;
	TextView flRight_tvRight, flRight_tvShowInfo;
	// mang luu hinh anh ray menu
	private static final int[] ITEM_DRAWABLES = { R.drawable.trogiup_doicauhoi,
			R.drawable.trogiup_goi, R.drawable.trogiup_henxui,
			R.drawable.trogiup_khangia };
	// mang hinh sai
	private int arrPictureWrong[] = { R.drawable.buon1, R.drawable.buon2,
			R.drawable.buon3, R.drawable.buon4, R.drawable.buon5,
			R.drawable.buon6, R.drawable.buon7, R.drawable.buon8,
			R.drawable.buon9, R.drawable.buon10, R.drawable.buon11, };
	// mang hinh background
	private int arrPictureBackground[] = { R.drawable.bg1, R.drawable.bg2,
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.cptt_activity_main);
		// anh xa
		xmlMain = (RelativeLayout) findViewById(R.id.xml_Main);
		rlMoneyAndScore = (RelativeLayout) findViewById(R.id.rlMoneyAndScore);
		tvQuestion = (TextView) findViewById(R.id.tvQuestion);
		tvScore = (TextView) findViewById(R.id.tvScore);
		tvNumOfMoney = (TextView) findViewById(R.id.tvNumOfMoney);
		btQuestionA = (Button) findViewById(R.id.btQuestionA);
		btQuestionB = (Button) findViewById(R.id.btQuestionB);
		btQuestionC = (Button) findViewById(R.id.btQuestionC);
		btQuestionD = (Button) findViewById(R.id.btQuestionD);
		lnQuestion = (LinearLayout) findViewById(R.id.lnQuestion);
		lnSupport = (LinearLayout) findViewById(R.id.lnSupport);
		mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
		tvTimeCount = (TextView) findViewById(R.id.tvTimeCount);
		//
		flWrong = (FrameLayout) findViewById(R.id.flWrong);
		flWrong_ivWrong = (ImageView) findViewById(R.id.flWrong_ivWrong);
		flWrong_tvScore = (TextView) findViewById(R.id.flWrong_tvScore);
		flWrong_tvHightScore = (TextView) findViewById(R.id.flWrong_tvHightScore);
		flWrong_btReplay = (Button) findViewById(R.id.flWrong_btReplay);
		flWrong_btExit = (Button) findViewById(R.id.flWrong_btExit);
		flWrong_tvResult = (TextView) findViewById(R.id.flWrong_tvResult);
		flWrong_tvAddMoney = (TextView) findViewById(R.id.flWrong_addMoney);
		flWrong_tvLose = (TextView) findViewById(R.id.flWrong_tvLose);
		//
		flRight = (FrameLayout) findViewById(R.id.flRight);
		flRight_ivRight = (ImageView) findViewById(R.id.flRight_ivRight);
		flRight_tvRight = (TextView) findViewById(R.id.flRight_tvRight);
		flRight_tvShowInfo = (TextView) findViewById(R.id.flRight_tvShowInfo);
		//
		setFont();
		setMusic();
		// set hieu ung cho raymenu
		setAnimationRayMenu();
		// lan dau tien la 0 diem
		mScore = 0;
		// lan dau tien choi se cho nguoi choi 50 tien
		mMoney = 50;

		loadMoney();
		tvNumOfMoney.setText(mMoney + "");
		loadHightScore();
		// ray menu chua cac menu tro giup
		RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
		final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(ITEM_DRAWABLES[i]);
			final int position = i;
			rayMenu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (position) {
					case 0:// Doi cau hoi
						mp_toast = MediaPlayer.create(getApplicationContext(),
								R.raw.toast);
						mp_toast.start();
						intent = new Intent(getApplicationContext(),
								ActivityHelpChangeQuestion.class);
						startActivityForResult(intent, REQUESTCODE_1);

						break;
					case 1:// Goi dien thoai
						mp_toast = MediaPlayer.create(getApplicationContext(),
								R.raw.toast);
						mp_toast.start();
						intent = new Intent(getApplicationContext(),
								ActivityHelpPhoneCall.class);
						startActivityForResult(intent, REQUESTCODE_2);
						break;
					case 2:// 50:50
						mp_toast = MediaPlayer.create(getApplicationContext(),
								R.raw.toast);
						mp_toast.start();
						intent = new Intent(getApplicationContext(),
								ActivityHelpBadOrLuck.class);
						startActivityForResult(intent, REQUESTCODE_3);
						break;
					case 3:// Hoi y kien khan gia trong truong quay
						mp_toast = MediaPlayer.create(getApplicationContext(),
								R.raw.toast);
						mp_toast.start();
						intent = new Intent(getApplicationContext(),
								ActivityHelpAskAudience.class);
						startActivityForResult(intent, REQUESTCODE_4);
						break;
					}
				}
			});// Add a menu item
		}
		// khoi tao database
		dbQuestionManager = new QuestionManagerDatabase(this);
		try {
			dbQuestionManager.createDatabase();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Error...",
					Toast.LENGTH_LONG).show();
		}
		dbQuestionManager.close();
		dbQuestionManager = new QuestionManagerDatabase(this);
		// lay ra cau hoi de
		getEasyQuestion();
		// nhan vao button nao thi se kiem tra ket qua button do
		btQuestionA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkResult(btQuestionA.getText().toString());
			}
		});
		btQuestionB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkResult(btQuestionB.getText().toString());
			}
		});
		btQuestionC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkResult(btQuestionC.getText().toString());
			}
		});
		btQuestionD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkResult(btQuestionD.getText().toString());
			}
		});
	}

	// ham ngau nhien set dap an len cac Button
	void setRandomResultDisplayUpButton() {
		// mang gom co 4 button de show 4 dap an
		Button arrButton[] = { btQuestionA, btQuestionB, btQuestionC,
				btQuestionD };
		// tao ra 4 cai index khac nhau
		Random rd_tv = new Random();
		int index1, index2, index3, index4;
		index1 = rd_tv.nextInt(arrButton.length);
		do {
			index2 = rd_tv.nextInt(arrButton.length);
		} while (index2 == index1);
		do {
			index3 = rd_tv.nextInt(arrButton.length);
		} while (index3 == index1 || index3 == index2);
		do {
			index4 = rd_tv.nextInt(arrButton.length);
		} while (index4 == index1 || index4 == index2 || index4 == index3);
		// set dap an len button
		arrButton[index1].setText(answerA);
		arrButton[index2].setText(answerB);
		arrButton[index3].setText(answerC);
		arrButton[index4].setText(answerD);
	}

	// ham kiem tra ket qua xem co dung dap an ko
	// lay chuoi x co tren button dem di so sanh
	void checkResult(String x) {
		// answerA chinh la dap an cua cau hoi
		if (x.equals(answerA)) {
			t.cancel();
			t_second.cancel();
			question = "";
			// diem +1
			mScore++;
			tvScore.setText("Điểm: " + mScore);
			// Kiem tra cau a, b, c, d coi cau nao sai thi hide cau do di
			if (btQuestionA.getText().toString().equals(answerA)) {
				btQuestionA.setEnabled(false);
				btQuestionB.setVisibility(View.INVISIBLE);
				btQuestionC.setVisibility(View.INVISIBLE);
				btQuestionD.setVisibility(View.INVISIBLE);
			}
			if (btQuestionB.getText().toString().equals(answerA)) {
				btQuestionB.setEnabled(false);
				btQuestionA.setVisibility(View.INVISIBLE);
				btQuestionC.setVisibility(View.INVISIBLE);
				btQuestionD.setVisibility(View.INVISIBLE);
			}
			if (btQuestionC.getText().toString().equals(answerA)) {
				btQuestionC.setEnabled(false);
				btQuestionA.setVisibility(View.INVISIBLE);
				btQuestionB.setVisibility(View.INVISIBLE);
				btQuestionD.setVisibility(View.INVISIBLE);
			}
			if (btQuestionD.getText().toString().equals(answerA)) {
				btQuestionD.setEnabled(false);
				btQuestionA.setVisibility(View.INVISIBLE);
				btQuestionB.setVisibility(View.INVISIBLE);
				btQuestionC.setVisibility(View.INVISIBLE);
			}
			// mang hinh vui
			// dung de show hinh vui len neu tra loi dung
			int picRight[] = { R.drawable.vui1, R.drawable.vui2,
					R.drawable.vui3, R.drawable.vui4, R.drawable.vui5,
					R.drawable.vui6, R.drawable.vui7, R.drawable.vui8,
					R.drawable.vui9, R.drawable.vui10, R.drawable.vui11, };
			Random r_picRight = new Random();
			int index_picRight = r_picRight.nextInt(picRight.length);
			flRight_ivRight.setImageResource(picRight[index_picRight]);
			// hien layout right roi show len
			flRight.setVisibility(View.VISIBLE);
			setAnimationRight();
			// mo nhac tra loi dung
			MediaPlayer mp_right = MediaPlayer.create(getApplicationContext(),
					R.raw.traloidung);
			mp_right.start();
			// su kien xay ra khi nhan vo tvShowInfo
			flRight_tvShowInfo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					flRight.setVisibility(View.INVISIBLE);
					btQuestionA.setVisibility(View.VISIBLE);
					btQuestionB.setVisibility(View.VISIBLE);
					btQuestionC.setVisibility(View.VISIBLE);
					btQuestionD.setVisibility(View.VISIBLE);
					btQuestionA.setEnabled(true);
					btQuestionB.setEnabled(true);
					btQuestionC.setEnabled(true);
					btQuestionD.setEnabled(true);
					// mo nhac
					MediaPlayer mp_yes = MediaPlayer.create(
							getApplicationContext(), R.raw.bt_yes);
					mp_yes.start();
					// tiep tuc lay cau hoi
					getEasyQuestion();
				}
			});

		} else {// tra loi sai
			// set chuoi rong
			question = "";
			tvScore.setText("Tiền: " + mScore);
			// mo nhac sau
			MediaPlayer mp_sai = MediaPlayer.create(getApplicationContext(),
					R.raw.traloisai);
			mp_sai.start();
			// goi ham khi tra loi sai
			answerWrong();
		}
	}

	// ham lay cau hoi
	void getEasyQuestion() {
		Random r = new Random();
		int index = r.nextInt(arrPictureBackground.length);
		xmlMain.setBackgroundResource(arrPictureBackground[index]);
		// set hieu ung
		setAnimationQuestion();
		// ngung nhac hien tai va tao lai nhac moi
		mMediaPlayer.stop();
		setMusic();
		// enable cac button
		btQuestionA.setEnabled(true);
		btQuestionB.setEnabled(true);
		btQuestionC.setEnabled(true);
		btQuestionD.setEnabled(true);
		// truyen 1 vao chinh la lay ngau nhien 1 cau hoi tu database
		alQuestions = dbQuestionManager.getNQuestionRandom(1);
		for (OneQuestionObject oneQuestion : alQuestions) {
			question += oneQuestion.question;
			answerA = oneQuestion.answer_a;
			answerB = oneQuestion.answer_b;
			answerC = oneQuestion.answer_c;
			answerD = oneQuestion.answer_d;
		}
		// set cau hoi len tvQuestion
		tvQuestion.setText(question);
		// goi ham sat ngau nhien cac dap an len button
		setRandomResultDisplayUpButton();
		// load tien
		loadMoney();
		// bat dau tinh lai thoi gian de tra loi cau hoi
		calculatorTime();
	}

	// ham tro giup doi cau hoi
	void helpChangeQuestion() {
		// quyen tro giup nay -10 tien
		mMoney -= 10;
		// ngung thoi gian
		t.cancel();
		t_second.cancel();
		// set chuoi question rong
		question = "";
		// luu lai tien vi da -10 roi phai luu lai
		saveMoney();
		// hien thi so tien
		tvNumOfMoney.setText("Tiền: " + mMoney);
		question = "";
		// lay cau hoi moi
		getEasyQuestion();
	}

	// ham nay tao 3 mang 3 so ngau nhien khac nhau dung cho ham tro giup hen
	// xui helpBadOrLuck()
	// ben duoi
	void createArr3RandomIndex(Button bt_3[]) {
		// random
		Random rd_tv = new Random();
		int index1, index2, index3;

		index1 = rd_tv.nextInt(3);
		do {
			index2 = rd_tv.nextInt(3);
		} while (index2 == index1);
		do {
			index3 = rd_tv.nextInt(3);
		} while (index3 == index1 || index3 == index2);

		bt_3[index1].setText("");
		bt_3[index2].setText("");
		bt_3[index2].setEnabled(false);
		bt_3[index2].setEnabled(false);
	}

	// ham tro giup hen xui
	void helpBadOrLuck() {
		// neu co 1 dap an bat ky la chuoi rong thi thong bao rang
		// khong su dung duoc quyen tro giup nay hon 1 lan trong 1 cau hoi
		if (btQuestionA.getText().toString().equals("")
				|| btQuestionB.getText().toString().equals("")
				|| btQuestionC.getText().toString().equals("")
				|| btQuestionD.getText().toString().equals("")) {

			// gui du lieu 1 cai anh (int) va 1 cau thong bao den
			// ActivityToastLostMoney
			intent = new Intent(getApplicationContext(),
					ActivityToastLostMoney.class);
			intent.putExtra("hinh", arrPictureWrong[randomPicWrong()] + "");
			intent.putExtra("thongbao",
					"Không thể dùng một quyền trợ giúp này trong một câu hỏi!");
			startActivity(intent);
		}
		// thuc hien bo bot 2 dap an sai
		else {
			// gui 1 cai hinh (int) va 1 cau thong bao cho
			// ActivityToastLostMoney
			intent = new Intent(getApplicationContext(),
					ActivityToastLostMoney.class);
			intent.putExtra("hinh", arrPictureWrong[randomPicWrong()] + "");
			intent.putExtra("thongbao",
					"Đã loại đi mất 2 phương án sai... Bạn bị trừ 7 tiền!");
			startActivity(intent);
			// tru di 8 dien sau do luu lai so tien moi
			mMoney -= 7;
			saveMoney();
			// set tien len tvNumOfMoney
			tvNumOfMoney.setText("Tiền: " + mMoney);
			// chuoi a b c d la cac chuoi dap an
			String a = btQuestionA.getText().toString();
			String b = btQuestionB.getText().toString();
			String c = btQuestionC.getText().toString();
			String d = btQuestionD.getText().toString();
			// neu chuoi a la dap an
			if (a.equals(answerA)) {
				Button bt_3a[] = { btQuestionB, btQuestionC, btQuestionD };
				createArr3RandomIndex(bt_3a);
			}
			// neu chuoi b la dap an
			if (b.equals(answerA)) {
				Button bt_3b[] = { btQuestionA, btQuestionC, btQuestionD };
				createArr3RandomIndex(bt_3b);
			}
			// neu chuoi c la dap an
			if (c.equals(answerA)) {
				Button bt_3c[] = { btQuestionA, btQuestionB, btQuestionD };
				createArr3RandomIndex(bt_3c);
			}
			// neu chuoi d la dap an
			if (d.equals(answerA)) {
				Button bt_3d[] = { btQuestionA, btQuestionB, btQuestionC };
				createArr3RandomIndex(bt_3d);
			}
		}

	}

	// tro giup tham khao y kien khan gia
	void helpAskAudience() {
		// -8 tien
		mMoney -= 8;
		// luu lai tien moi
		saveMoney();
		// set tien len textview
		tvNumOfMoney.setText("TIỀN: " + mMoney);
		// cac chuoi a b c d la cac chuoi tren cac dap an
		String a = btQuestionA.getText().toString();
		String b = btQuestionB.getText().toString();
		String c = btQuestionC.getText().toString();
		String d = btQuestionD.getText().toString();
		// tao chuoi dap an
		String tmpResult = "";
		// so sanh chuoi a voi dap an, neu dung thi cho chuoi dap an la a
		if (a.equals(answerA)) {
			tmpResult = "a";
		}
		// so sanh chuoi b voi dap an, neu dung thi cho chuoi dap an la b
		if (b.equals(answerA)) {
			tmpResult = "b";
		}
		// so sanh chuoi c voi dap an, neu dung thi cho chuoi dap an la c
		if (c.equals(answerA)) {
			tmpResult = "c";
		}
		// so sanh chuoi d voi dap an, neu dung thi cho chuoi dap an la d
		if (d.equals(answerA)) {
			tmpResult = "d";
		}
		// gui dap an qua intent
		intent = new Intent(getApplicationContext(),
				ActivityHelpAskAudienceChart.class);
		intent.putExtra("dapan", tmpResult);
		startActivity(intent);
	}

	// quyen tro giup goi dien thoai cho nguoi than
	void helpCallPhone() {
		// tru di 5 tien roi luu lai so tien
		mMoney -= 5;
		saveMoney();
		tvNumOfMoney.setText("Tiền: " + mMoney);
		// cac chuoi a b c d la cac chuoi tren cac dap an
		String a = btQuestionA.getText().toString();
		String b = btQuestionB.getText().toString();
		String c = btQuestionC.getText().toString();
		String d = btQuestionD.getText().toString();
		String tmpResult = "";
		// so sanh chuoi a voi dap an, neu dung thi cho chuoi dap an la a
		if (a.equals(answerA)) {
			tmpResult = "a";
		}
		// so sanh chuoi b voi dap an, neu dung thi cho chuoi dap an la b
		if (b.equals(answerA)) {
			tmpResult = "b";
		}
		// so sanh chuoi c voi dap an, neu dung thi cho chuoi dap an la c
		if (c.equals(answerA)) {
			tmpResult = "c";
		}
		// so sanh chuoi d voi dap an, neu dung thi cho chuoi dap an la d
		if (d.equals(answerA)) {
			tmpResult = "d";
		}

		intent = new Intent(getApplicationContext(),
				ActivityHelpPhoneCallList.class);
		intent.putExtra("dapan", tmpResult);
		startActivity(intent);
	}

	// ham nay thuc thi neu nguoi choi tra loi sai dap an
	void answerWrong() {
		// ngung tinh thoi gian
		t.cancel();
		t_second.cancel();
		// set hieu ung
		setAnimationWrong();
		// luu diem cao
		saveHightScore();
		// hien layout sai ra cho nguoi ta thay
		flWrong.setVisibility(View.VISIBLE);
		rlMoneyAndScore.setVisibility(View.INVISIBLE);
		// khong cho nguoi choi nhan vao nut nua
		btQuestionA.setEnabled(false);
		btQuestionB.setEnabled(false);
		btQuestionC.setEnabled(false);
		btQuestionD.setEnabled(false);
		// cac chuoi a b c d la cac chuoi tren cac dap an
		String a = btQuestionA.getText().toString();
		String b = btQuestionB.getText().toString();
		String c = btQuestionC.getText().toString();
		String d = btQuestionD.getText().toString();

		String tmpResult = "";
		if (a.equals(answerA)) {
			tmpResult = "A/ " + a;
		}
		if (b.equals(answerA)) {
			tmpResult = "B/ " + b;
		}
		if (c.equals(answerA)) {
			tmpResult = "C/ " + c;
		}
		if (d.equals(answerA)) {
			tmpResult = "D/ " + d;
		}
		// so tien hien co se duoc cong them so tien moi (chinh la so diem ghi
		// duoc o hien tai)
		mMoney = mMoney + mScore;
		// save tien lai
		saveMoney();
		// set thong tin len layout sai
		flWrong_tvResult.setText("Đáp án:\n" + tvQuestion.getText().toString()
				+ "\n" + tmpResult);
		flWrong_tvScore.setText(tvScore.getText().toString());
		flWrong_tvHightScore.setText("Kỷ lục: " + hightScore);
		flWrong_tvAddMoney.setText("Tiền thưởng: +" + mScore);
		// su kien khi nhan nut choi lai
		flWrong_btReplay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// ngung tinh thoi gian
				t.cancel();
				t_second.cancel();
				// an cai layout sai di
				flWrong.setVisibility(View.INVISIBLE);
				// hien layout tien+diem ra
				rlMoneyAndScore.setVisibility(View.VISIBLE);
				// choi lai nen cho diem ve lai 0
				mScore = 0;
				tvScore.setText("Điểm: " + mScore);
				// bat dau load lai diem ky luc va so tien da duoc luu
				loadHightScore();
				loadMoney();
				// hien thi tien len textview
				tvNumOfMoney.setText("Tiền: " + mMoney);
				// choi nhac
				MediaPlayer mp_choilai = MediaPlayer.create(
						getApplicationContext(), R.raw.bt_yes);
				mp_choilai.start();
				// lay mot cau hoi
				getEasyQuestion();
			}
		});
		// su kien khi nhan nut thoat ra
		flWrong_btExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// khong tinh thoi gian nua
				t.cancel();
				t_second.cancel();
				// luu diem cao va luu tien
				saveHightScore();
				saveMoney();
				// ngung nhac nen lai
				mMediaPlayer.stop();
				// choi nhac khi nhan nut exit
				MediaPlayer mp_thoat = MediaPlayer.create(
						getApplicationContext(), R.raw.bt_thoat);
				mp_thoat.start();
				// thoat ra luon
				ActivityMain.this.finish();
			}
		});
		flWrong_ivWrong.setImageResource(arrPictureWrong[randomPicWrong()]);
	}

	// ham nay tra ve 1 con so random mang gia tri min la 0-1?
	// max la chieu dai cua mang arrPictureWrong
	int randomPicWrong() {
		Random r_picWrong = new Random();
		int index_picWiong = r_picWrong.nextInt(arrPictureWrong.length);
		return index_picWiong;
	}

	// xu ly khi nhan duoc du lieu tra ve
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// doi cau hoi
		if (requestCode == REQUESTCODE_1 && resultCode == RESULT_OK) {
			// neu tien >=10 thi moi dc, nguoc lai thi thong bao het tien
			if (mMoney >= 10) {
				intent = new Intent(getApplicationContext(),
						ActivityToastLostMoney.class);
				intent.putExtra("hinh", arrPictureWrong[randomPicWrong()] + "");
				intent.putExtra("thongbao",
						"Bạn đã đổi câu hỏi thành công, bạn bị trừ 10 tiền!");
				startActivity(intent);
				helpChangeQuestion();
			} else {
				// khong du tien
				toastYouHaveNotEnoghtMoney();
			}
		}

		// goi dien thoai
		if (requestCode == REQUESTCODE_2 && resultCode == RESULT_OK) {
			// neu tien >==5 thi goi ham helpCallPhone
			// khong thi thong bao ko du tien
			if (mMoney >= 5) {
				helpCallPhone();
			} else {
				toastYouHaveNotEnoghtMoney();
			}
		}

		// hen xui
		if (requestCode == REQUESTCODE_3 && resultCode == RESULT_OK) {
			if (mMoney >= 7) {
				helpBadOrLuck();
			} else {
				toastYouHaveNotEnoghtMoney();
			}
		}

		// hoi y kien moi nguoi
		if (requestCode == REQUESTCODE_4 && resultCode == RESULT_OK) {
			// String kq=data.getStringExtra("yes").toString();
			if (mMoney >= 8) {
				helpAskAudience();
			} else {
				toastYouHaveNotEnoghtMoney();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// ham thong bao ko du tien
	void toastYouHaveNotEnoghtMoney() {
		intent = new Intent(getApplicationContext(),
				ActivityToastLostMoney.class);
		intent.putExtra("hinh", arrPictureWrong[randomPicWrong()] + "");
		intent.putExtra("thongbao",
				"Không đủ tiền để dùng quyền trợ giúp. Bạn tự trả lời câu hỏi yk nha!");
		startActivity(intent);
	}

	// luu diem cao
	void saveHightScore() {
		if (mScore > hightScore) {
			try {
				FileOutputStream out = openFileOutput("dulieudiem.txt",
						MODE_PRIVATE);
				String x = mScore + "";
				out.write(x.getBytes());
				out.close();
			} catch (Exception e) {
			}
		}
	}

	// luu tien
	void saveMoney() {
		try {
			FileOutputStream out = openFileOutput("dulieutien.txt",
					MODE_PRIVATE);
			String x = mMoney + "";
			out.write(x.getBytes());
			out.close();
		} catch (Exception e) {
		}
	}

	// load diem cao
	void loadHightScore() {
		try {
			in = openFileInput("dulieudiem.txt");
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			String x = new String(buffer);
			in.close();
			hightScore = Integer.parseInt(x);
		} catch (Exception e) {
		}
	}

	// load tien
	void loadMoney() {
		try {
			in = openFileInput("dulieutien.txt");
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			String x = new String(buffer);
			in.close();
			mMoney = Integer.parseInt(x);
			tvNumOfMoney.setText("Tiền: " + mMoney);
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

	// ham cai dat font
	void setFont() {
		loadFontSetting();
		if (numberToSetFont == 1) {
			Typeface tf = Typeface.createFromAsset(
					ActivityMain.this.getAssets(), "SEGOEUI.TTF");
			tvQuestion.setTypeface(tf, Typeface.BOLD);
			btQuestionA.setTypeface(tf, Typeface.BOLD);
			btQuestionB.setTypeface(tf, Typeface.BOLD);
			btQuestionC.setTypeface(tf, Typeface.BOLD);
			btQuestionD.setTypeface(tf, Typeface.BOLD);
			tvScore.setTypeface(tf, Typeface.BOLD);
			tvNumOfMoney.setTypeface(tf, Typeface.BOLD);
			flWrong_tvLose.setTypeface(tf, Typeface.BOLD);
			flWrong_btReplay.setTypeface(tf, Typeface.BOLD);
			flWrong_btExit.setTypeface(tf, Typeface.BOLD);
			flWrong_tvScore.setTypeface(tf, Typeface.BOLD);
			flWrong_tvHightScore.setTypeface(tf, Typeface.BOLD);
			flWrong_tvAddMoney.setTypeface(tf, Typeface.BOLD);
			flWrong_tvResult.setTypeface(tf, Typeface.BOLD);
			//
			flRight_tvRight.setTypeface(tf, Typeface.BOLD);
			flRight_tvShowInfo.setTypeface(tf, Typeface.ITALIC);
			//
			tvTimeCount.setTypeface(tf, Typeface.BOLD);
		} else {
			Typeface tf = Typeface.DEFAULT;
			tvQuestion.setTypeface(tf);
			btQuestionA.setTypeface(tf);
			btQuestionB.setTypeface(tf);
			btQuestionC.setTypeface(tf);
			btQuestionD.setTypeface(tf);
			tvScore.setTypeface(tf);
			tvNumOfMoney.setTypeface(tf);
			flWrong_tvLose.setTypeface(tf);
			flWrong_btReplay.setTypeface(tf);
			flWrong_btExit.setTypeface(tf);
			flWrong_tvScore.setTypeface(tf);
			flWrong_tvHightScore.setTypeface(tf);
			flWrong_tvAddMoney.setTypeface(tf);
			flWrong_tvResult.setTypeface(tf);
			//
			flRight_tvRight.setTypeface(tf);
			flRight_tvShowInfo.setTypeface(tf);
			//
			tvTimeCount.setTypeface(tf);
		}
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

	// cai dat nhac
	void setMusic() {
		loadMusicSetting();
		if (numberToSetMusic == 1) {
			int arrMusic[] = { R.raw.background0, R.raw.background1,
					R.raw.background2, R.raw.background3, R.raw.background4,
					R.raw.background5, R.raw.background6, R.raw.background7 };

			Random r = new Random();
			int index = r.nextInt(arrMusic.length);
			mMediaPlayer = MediaPlayer.create(getApplicationContext(),
					arrMusic[index]);
			mMediaPlayer.start();
			mMediaPlayer.setLooping(true);
		} else {
			mMediaPlayer = MediaPlayer.create(getApplicationContext(),
					R.raw.background_no);
			mMediaPlayer.start();
		}

	}

	// set hieu ung
	void setAnimationQuestion() {
		mAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.ln_cauhoi);
		lnQuestion.setAnimation(mAnimation);
	}

	void setAnimationRayMenu() {
		mAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.scale_button);
		lnSupport.setAnimation(mAnimation);
	}

	void setAnimationWrong() {
		mAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fl_sai);
		flWrong.setAnimation(mAnimation);
	}

	void setAnimationRight() {
		mAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fl_dung);
		flRight.setAnimation(mAnimation);
	}

	// ham dem thoi gian cho progress bar
	void calculatorTime() {
		mProgressBar.setMax(55800);
		t = new CountDownTimer(60000, 200) {
			int dem = 0;

			@Override
			public void onTick(long millisUntilFinished) {
				dem += 200;
				mProgressBar.setProgress(dem);
			}

			@Override
			public void onFinish() {
				// het 60s ma ko tra loi thi coi nhu thua cuoc
				// reset lai tu dau
				question = "";
				MediaPlayer mpWrong = MediaPlayer.create(
						getApplicationContext(), R.raw.traloisai);
				mpWrong.start();
				answerWrong();
			}
		};
		t.start();
		// dung de set thoi gian cho textview, co the gop chung o tren nhung
		// tach ra cho dep code
		t_second = new CountDownTimer(61000, 1000) {
			int dem = 0;

			@Override
			public void onTick(long millisUntilFinished) {
				dem += 1000;
				tvTimeCount.setText(60 - (dem / 1000) + "s");
				mAnimation = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.scale_thoigian);
				tvTimeCount.setAnimation(mAnimation);
			}

			@Override
			public void onFinish() {
			}
		};
		t_second.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		mMediaPlayer.stop();
		MediaPlayer mp_exit = MediaPlayer.create(getApplicationContext(),
				R.raw.bt_thoat);
		mp_exit.start();
		question = "";
		// answerWrong();
		// super.onBackPressed();

		// hien thi dialog
		new AlertDialog.Builder(this)
				.setTitle("Thông báo nè")
				.setMessage("Bạn muôn bỏ cuộc chơi tại đây đúng không?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ActivityMain.this.finish();
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// bat su kien khi nhan nut home
	@Override
	protected void onUserLeaveHint() {
		// Toast.makeText(getApplicationContext(), "Home button pressed",
		// Toast.LENGTH_LONG).show();
		if (mMediaPlayer.isPlaying())
			mMediaPlayer.stop();
		super.onUserLeaveHint();
	}
}
