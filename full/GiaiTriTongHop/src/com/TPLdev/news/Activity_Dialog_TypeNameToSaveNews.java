package com.TPLdev.news;

//xuat hien khi muon save 1 tin bai de doc offline
//yeu cau nhap ten de luu vao sqlite dong thoi save file html vao thu muc GiaiTriTongHopBao

//luu y rang activity nay dc dung de them 1 tin moi offline
//no cung dc dung de sua lai tin moi (sua tieu de)
//chinh vi the co them bien String kieu de phan biet xem no thuc hien chuc nang gi (them hay sua)
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.TPLdev.giaitritonghop.R;

public class Activity_Dialog_TypeNameToSaveNews extends Activity {
	// etTitle dung de nhap title cho tin can luu
	// etLink hien thi duong link (ko cho nguoi dung nhap)
	EditText etTitle, etLink;
	// nut xac nhan sau khi go title xon
	Button btOkay;
	// phan biet de biet thuc hien la them hay sua
	String kieu;
	// lay link tu intent
	String link;
	// 1 doi tuong bao
	Dtb_OneNewsObject newsObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// full man hinh
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.menu_fragment_dialog_typenametosavenews);
		// anh xa
		etTitle = (EditText) findViewById(R.id.etTitle);
		etLink = (EditText) findViewById(R.id.etLink);
		etLink.setEnabled(false);
		btOkay = (Button) findViewById(R.id.btOkay);
		// lay thong tin tu intent
		kieu = getIntent().getExtras().getString("kieu");
		link = getIntent().getExtras().getString("link");
		etLink.setText(link);

		if (kieu.equals("sua")) {
			newsObject = (Dtb_OneNewsObject) getIntent().getExtras().get(
					"congviec");
			etTitle.setText(newsObject.title);
			etLink.setText(newsObject.link);
			btOkay.setText("Sửa");
		}

		btOkay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nd = etTitle.getText().toString();
				// String tg = et_thoigian.getText().toString();
				String tg = link;
				if (kieu.equals("them"))// them
				{
					Dtb_OneNewsObject oneNews = new Dtb_OneNewsObject(nd, tg);
					Intent i = new Intent();
					i.putExtra("congviec", oneNews);
					setResult(RESULT_OK, i);
					finish();
				} else // sua
				{
					// id: cu
					// noidung, thoi gian : moi
					Dtb_OneNewsObject cv_cu = (Dtb_OneNewsObject) getIntent()
							.getSerializableExtra("congviec");
					etLink.setText(cv_cu.link + "");

					// lay thong tin tin moi vua sua
					Dtb_OneNewsObject cv_moi = new Dtb_OneNewsObject(
							newsObject.id, nd, etLink.getText().toString());
					Intent i = new Intent();
					i.putExtra("congviec", cv_moi);
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});
	}
}