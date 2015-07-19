package com.TPLdev.news;

//hien thi listview thong bao cac tin tuc da tai offline
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;

public class Activity_News_Saved_List extends Activity {

	// co so du lieu
	Dtb_NewsSavedManager qlb;
	// mang chua cac tin offline
	ArrayList<Dtb_OneNewsObject> alNews;
	// listview hien thi tin offline
	ListView mListView;
	// nut xoa tat ca du lieu tin offline da luu
	ImageView ivDeleteAll;
	// setting
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
	// de set font cho webview, dc cai dat trong radioGroupFont
	int indexFont;
	// de set background dua vao index
	int indexBackground;
	// xmlmain de set background
	LinearLayout xmlMain;
	// font chu
	Typeface mTypeface;
	// tv tin bai cua toi
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// full man hinh
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu_fragment_news_activity_saved_list);
		// anh xa
		tv = (TextView) findViewById(R.id.tv);
		xmlMain = (LinearLayout) findViewById(R.id.xmlMain);
		ivDeleteAll = (ImageView) findViewById(R.id.ivDeleteAll);
		qlb = new Dtb_NewsSavedManager(Activity_News_Saved_List.this);
		mListView = (ListView) findViewById(R.id.mListView);
		alNews = new ArrayList<Dtb_OneNewsObject>();

		// dang ky context menu khi nhan giu lau vao 1 doi tuong tren listview
		registerForContextMenu(mListView);

		// hien thi tin offline len listview
		displayDataUpListView();

		// File directory = new File(Environment.getExternalStorageDirectory()
		// .getPath() + "/GiaiTriTongHop/");

		// su kien cho xoa het tat ca cac tin offline
		ivDeleteAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogDeleteAll();
			}
		});
		// su kien khi nhan vao 1 item cua listview
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// chuyen sang activity de doc tin offline
				Intent i = new Intent(getApplicationContext(),
						Activity_ReadNews_Offline.class);
				i.putExtra("link", alNews.get(position).link + "");
				startActivity(i);
			}
		});
	}

	private void showDialogDeleteAll() {
		new AlertDialog.Builder(this)
				.setTitle("Chú ý...")
				.setMessage(
						"Bạn muốn xóa hết tất cả những bài báo đã lưu đúng không?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
								Toast.makeText(getApplicationContext(),
										"Đã xóa hết dữ liệu",
										Toast.LENGTH_SHORT).show();
								// xoa trong sqlite
								qlb.removeAllData();
								// da xoa xong muon thay doi du lieu ngay thi
								// goi
								// displayDataUpListView
								displayDataUpListView();
								tv.setText("Tin bài đã lưu: 0");
							}
						})
				.setNegativeButton(android.R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	// context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.menu_listview, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	// sua hay xoa
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// vi tri
		int index = info.position;

		// doi tuong tin dua vao index
		Dtb_OneNewsObject oneNews = (Dtb_OneNewsObject) mListView
				.getItemAtPosition(index);

		switch (item.getItemId()) {
		// xoa di 1 tin
		case R.id.xoa_item:
			Toast.makeText(Activity_News_Saved_List.this,
					"Đã xóa thành công " + oneNews.title, Toast.LENGTH_SHORT)
					.show();

			// sqlite xoa du lieu tin nay sau do hien thi lai len listview
			qlb.removeNews(oneNews);
			displayDataUpListView();
			// hien thi lai so tin bai da luu
			int countData = qlb.getDataCount();
			tv.setText("Tin bài đã lưu: " + countData);
			break;

		// sua di tin
		case R.id.sua_item:
			// chuyen sang activity khac de sua thong tin
			Intent i = new Intent(Activity_News_Saved_List.this,
					Activity_Dialog_TypeNameToSaveNews.class);
			i.putExtra("kieu", "sua");
			i.putExtra("congviec", oneNews);
			// tan dung lai activity them de sua
			startActivityForResult(i, 333);
			break;
		}
		return super.onContextItemSelected(item);
	}

	// hien thi du lieu len listview
	public void displayDataUpListView() {
		try {
			// lay tat ca cac du lieu trong sqlite
			alNews = qlb.getAllNewsDaTa();
			mListView.setAdapter(new AdapterListNewsSaved(
					Activity_News_Saved_List.this));
		} catch (Exception e) {
			mListView.setAdapter(null);
		}

	}

	public static class ViewHolder {
		private TextView id;
		private TextView title;
		private TextView link;
	}

	class AdapterListNewsSaved extends BaseAdapter {
		Context context;

		AdapterListNewsSaved(Context c) {
			context = c;
		}

		public int getCount() {
			return alNews.size();
		}

		public Object getItem(int arg0) {
			return alNews.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder oneView;
			LayoutInflater inf = ((Activity) context).getLayoutInflater();
			if (arg1 == null) {
				oneView = new ViewHolder();
				arg1 = inf.inflate(
						R.layout.menu_fragment_news_item_list_news_saved, null);
				oneView.id = (TextView) arg1.findViewById(R.id.id);
				oneView.title = (TextView) arg1.findViewById(R.id.tv__title);
				oneView.link = (TextView) arg1.findViewById(R.id.tv__link);

				arg1.setTag(oneView);
			} else
				oneView = (ViewHolder) arg1.getTag();

			oneView.id.setText(alNews.get(arg0).id + "");
			oneView.title.setText(alNews.get(arg0).title);
			oneView.link.setText(alNews.get(arg0).link);

			oneView.title.setTypeface(mTypeface);
			oneView.link.setTypeface(mTypeface, Typeface.ITALIC);

			return arg1;
		}
	}

	// lay ket qua sau khi nhan sua 1 tin moi
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 333 && resultCode == RESULT_OK) {
			Dtb_OneNewsObject c = (Dtb_OneNewsObject) data.getExtras().get(
					"congviec");
			// sua lai thong tin sqlite
			qlb.editNews(c);
			// hien thi lai tin
			displayDataUpListView();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		// goi de lay typeface
		loadSettingFont();

		// set font cho tv Tin bai cua toi
		tv.setTypeface(mTypeface);

		int countData = qlb.getDataCount();
		tv.setText("Tin bài đã lưu: " + countData);

		// hinh nen
		loadSettingBackground();

		super.onResume();
	}

	// dung de thiet lap mTypeface dua vao index duoc luu trong
	// SharedPreferences
	private void loadSettingFont() {
		SharedPreferences mysettings = getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		indexFont = mysettings.getInt("indexFont", 1);
		Log.d("indexFont", indexFont + "");
		switch (indexFont) {
		case 0:
			mTypeface = Typeface.DEFAULT;
			break;
		case 1:
			mTypeface = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "SEGOEUI.TTF");
			break;
		case 2:
			mTypeface = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "tinhyeu.ttf");
			break;
		case 3:
			mTypeface = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "thuphap.ttf");
			break;
		default:
			mTypeface = Typeface.createFromAsset(getApplicationContext()
					.getAssets(), "SEGOEUI.TTF");
			break;
		}
	}

	private void loadSettingBackground() {
		SharedPreferences mysettings = getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		indexBackground = mysettings.getInt("indexBackground", 0);
		Log.d("indexBackground", indexBackground + "");
		switch (indexBackground) {
		case 0:
			xmlMain.setBackgroundColor(Color.WHITE);
			break;
		case 1:
			xmlMain.setBackgroundColor(Color.parseColor("#ff99ff"));
			break;
		case 2:
			xmlMain.setBackgroundColor(Color.parseColor("#99ff66"));
			break;
		case 3:
			xmlMain.setBackgroundColor(Color.parseColor("#ccffff"));
			break;
		case 4:
			xmlMain.setBackgroundResource(R.drawable.bg_main);
			break;
		case 5:
			xmlMain.setBackgroundResource(R.drawable.bg_news1);
			break;
		case 6:
			xmlMain.setBackgroundResource(R.drawable.bg_news2);
			break;
		default:
			break;
		}
	}
}
