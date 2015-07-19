package com.TPLdev.game_chinhphucthuthach;

import java.io.FileInputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class ActivityHelpPhoneCallList extends Activity {
	String[] arrName = { "EINSTEIN", "ACE", "CHOPPER", "DEVELOPER", "ERZA",
			"LUFFY", "MARIA", "RÔ VẨU", "RONALDO", "SANJI", "SASUKE", "ZORO" };
	int arrPic[] = { R.drawable.tga, R.drawable.tgace, R.drawable.tgchopper,
			R.drawable.tgdev, R.drawable.tgerza, R.drawable.tgluffy,
			R.drawable.tgozawa, R.drawable.tgronaldinho, R.drawable.tgronaldo,
			R.drawable.tgsanji, R.drawable.tgsasuke, R.drawable.tgzoro, };
	GridView mGridView;
	Intent i;
	String result = "";
	// ban muon goi cho ai
	TextView tvWho;
	FileInputStream in;
	int numberToSetFont = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cptt_activity_help_phone_call_list);
		tvWho = (TextView) findViewById(R.id.tv_banmuongoichoai);

		mGridView = (GridView) findViewById(R.id.gridView1);
		mGridView.setAdapter(new MyAdapter(this));

		//
		setFont();
		//
		result = getIntent().getStringExtra("dapan");
		//
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MediaPlayer mp_goi = MediaPlayer.create(
						getApplicationContext(), R.raw.bt_yes);
				mp_goi.start();
				i = new Intent(getApplicationContext(),
						ActivityShowSuggest.class);
				i.putExtra("hinh", arrPic[position] + "");
				i.putExtra("ten", arrName[position]);
				i.putExtra("dapan", result);
				startActivity(i);
				ActivityHelpPhoneCallList.this.finish();

			}

		});
	}

	public static class ViewHolder {
		public ImageView imageview;
		public TextView textview;
	}

	public class MyAdapter extends BaseAdapter {
		Context context;

		public MyAdapter(Context c) {
			this.context = c;
		}

		@Override
		public int getCount() {
			return arrPic.length;
		}

		@Override
		public Object getItem(int position) {
			return arrPic[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder oneView;
			LayoutInflater layoutinflater = ((Activity) context)
					.getLayoutInflater();
			if (convertView == null) {
				oneView = new ViewHolder();
				convertView = layoutinflater.inflate(R.layout.cptt_gridview_item,
						null);
				oneView.textview = (TextView) convertView
						.findViewById(R.id.tv_namePerson);
				oneView.imageview = (ImageView) convertView
						.findViewById(R.id.imageView1);
				convertView.setTag(oneView);
			} else
				oneView = (ViewHolder) convertView.getTag();
			
			oneView.imageview.setImageResource(arrPic[position]);
			oneView.textview.setText(arrName[position]);
			Typeface tf = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "SEGOEUI.TTF");
			oneView.textview.setTypeface(tf, Typeface.BOLD);
			
			return convertView;
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

	void setFont() {
		loadFontSetting();
		if (numberToSetFont == 1) {
			Typeface tf = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "SEGOEUI.TTF");
			tvWho.setTypeface(tf, Typeface.BOLD);
		} else {
			Typeface tf = Typeface.DEFAULT;
			tvWho.setTypeface(tf);
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}
}
