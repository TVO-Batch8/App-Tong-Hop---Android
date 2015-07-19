package com.TPLdev.book;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;
import com.TPLdev.lazylist.ImageLoader;
import com.TPLdev.news.Activity_Dialog_Noconnect;

public class FragmentBook extends Fragment {
	// setting sharedpreferences
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences_Book";
	// de set font cho webview, dc cai dat trong radioGroupFont
	int indexFont;
	// de set background dua vao index
	int indexBackground;
	// xmlmain de set background
	FrameLayout xmlMain;
	// view
	private View parentView;
	// cac chuyen muc tin tuc o sliding menu
	ArrayList<FragmentBook_OneItemInLvMenu> listMenu = new ArrayList<FragmentBook_OneItemInLvMenu>();
	// la listview hien thi chuyen muc len sliding menu
	ListView mListViewSlidingMenu;
	Intent mIntent;
	// log tag
	public static final String LOG_TAG = "FragmentBook";
	// mang cac chuyen muc tin tuc, luu trong string.xml
	String[] arrRSSMenu;
	// dia chi rss hien tai, moi vo mac dinh la truyen ngan tinh yeu
	String strRSS = "http://apppro.timgem.com/rss/truyen-ngan-tinh-yeu";
	// mang cac tin tuc, moi tin la oject
	ArrayList<FragmentBook_ParsedDataSet> listBookItems = new ArrayList<FragmentBook_ParsedDataSet>();
	// cac thuoc tinh cua 1 object tin tuc, trong do strLinkImage la linkImage
	String strLink, strTitle, strDes, strPubDate, strLinkImage;
	// progress dialog khi load cac tin cua 1 chuyen muc cu the
	ProgressDialog mDialog;
	// set font
	Typeface mTypeface;
	// hieu ung
	Animation mAnimation;
	// listview hien thi cac tin tuc
	ListView mListViewBook;
	// image view fresh tin moi
	ImageView ivFresh;
	// index trong arrRSSMenu, ban dau cho la 1 de khac cai mac dinh la tin tuc
	// trong ngay co index 0
	int indexRSS = 1;
	// luu giu trang thai listview
	Parcelable state;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.menu_fragment_book, container,
				false);
		// anh xa
		xmlMain = (FrameLayout) parentView.findViewById(R.id.xmlMain);
		mListViewBook = (ListView) parentView.findViewById(R.id.lv_theloai);
		ivFresh = (ImageView) parentView.findViewById(R.id.ivFresh);

		// lay mang arrRSSMenu tu trong string.xml
		arrRSSMenu = getResources().getStringArray(R.array.rssbook);
		// cai dat font va animation
		loadSettingFont();
		// hieu ung
		mAnimation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.fl_dung);

		// cai dat du lieu cho slidingmenu phan chon chuyen muc
		setUpSlidingViews();
		// lay du lieu tu rss, mac dinh moi vao la tin tuc trong ngay
		getDataFromRSS(false);

		// su kien fresh tin moi khi nhan vao ivFresh
		// load them tin moi
		ivFresh.setVisibility(View.GONE);
		ivFresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// giu lai trang thai hien tai cua mListViewNews dang o dau
				state = mListViewBook.onSaveInstanceState();
				// cap nhat strRSS moi theo index
				strRSS = arrRSSMenu[indexRSS];
				// lay du lieu tu rss moi, truyen true de cho ham
				// getDataFromRSS biet la dang goi tu ivFresh
				// de no biet restore lai state cua mListViewNews
				getDataFromRSS(true);

				Log.d("index", strLink);
				Log.d("index", indexRSS + "");
				Log.d("index", arrRSSMenu[indexRSS] + "");

				// cong index len 1 don vi
				if (indexRSS < 21)
					indexRSS++;
				else
					indexRSS = 0;
			}
		});

		return parentView;
	}

	// thiet lap cho sliding
	private void setUpSlidingViews() {
		// anh xa
		mListViewSlidingMenu = (ListView) parentView
				.findViewById(R.id.listview);

		// checkToShowLayoutOffline();

		// set data for listview
		FragmentBook_OneItemInLvMenu a1 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Truyện ngắn tình yêu");
		FragmentBook_OneItemInLvMenu a2 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Suy ngẫm");
		FragmentBook_OneItemInLvMenu a3 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Ngôn tình");
		FragmentBook_OneItemInLvMenu a4 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Kiếm hiệp");
		FragmentBook_OneItemInLvMenu a5 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Trinh thám");
		FragmentBook_OneItemInLvMenu a6 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Cảm động");
		FragmentBook_OneItemInLvMenu a7 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Quỳnh Dao");
		FragmentBook_OneItemInLvMenu a8 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Nguyễn Nhật Ánh");
		FragmentBook_OneItemInLvMenu a9 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Phan Hồn Nhiên");
		FragmentBook_OneItemInLvMenu a10 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Truyện cười");
		FragmentBook_OneItemInLvMenu a11 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Truyện cười 18+");
		FragmentBook_OneItemInLvMenu a12 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Cổ tích");
		FragmentBook_OneItemInLvMenu a13 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Truyện cổ Grimn");
		FragmentBook_OneItemInLvMenu a14 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Thư tình hay");
		FragmentBook_OneItemInLvMenu a15 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Bí quyết tán gái");
		FragmentBook_OneItemInLvMenu a16 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Truyện ma kinh dị");
		FragmentBook_OneItemInLvMenu a17 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Mẹo vặt cuộc sống");
		FragmentBook_OneItemInLvMenu a18 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Mẹo vặt y khoa");
		FragmentBook_OneItemInLvMenu a19 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Thơ");
		FragmentBook_OneItemInLvMenu a20 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Văn học");
		FragmentBook_OneItemInLvMenu a21 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Kinh phật");
		FragmentBook_OneItemInLvMenu a22 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Thể loại khác");

		listMenu.add(a1);
		listMenu.add(a2);
		listMenu.add(a3);
		listMenu.add(a4);
		listMenu.add(a5);
		listMenu.add(a6);
		listMenu.add(a7);
		listMenu.add(a8);
		listMenu.add(a9);
		listMenu.add(a10);
		listMenu.add(a11);
		listMenu.add(a12);
		listMenu.add(a13);
		listMenu.add(a14);
		listMenu.add(a15);
		listMenu.add(a16);
		listMenu.add(a17);
		listMenu.add(a18);
		listMenu.add(a19);
		listMenu.add(a20);
		listMenu.add(a21);
		listMenu.add(a22);

		// tao adapter cho sliding
		FragmentBook_MyAdapter adapter = new FragmentBook_MyAdapter(
				getActivity(), listMenu);
		mListViewSlidingMenu.setAdapter(adapter);
		// su kien khi chon vao 1 chuyen muc
		mListViewSlidingMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {

				default:
					// xoa het du lieu trong listBookItems di vi da chon 1
					// chuyen muc khac roi
					listBookItems.clear();
					// strRSS luc nay se la rss moi
					strRSS = arrRSSMenu[position];
					// lay tin tuc tu rss hien tai
					getDataFromRSS(false);
					break;
				}
			}
		});
	}

	// ham nay dung de load cac tin tuc va hien thi len mListViewNews
	// boolean fresh = true la dc goi khi nhan vao ivFresh, nguoc lai la coi nhu
	// load lan dau
	void getDataFromRSS(boolean fresh) {
		// kiem tra ket noi cua dien thoai, neu co thi moi load tin, khong co
		// thi thong bao ko co ket noi
		final ConnectivityManager conMgr = (ConnectivityManager) getActivity()
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

		if (activeNetwork != null && activeNetwork.isConnected()) {
			// notify user you are online

			try {
				// parse our XML
				// parse du lieu tin tuc trong ngay
				Log.d("connect", "connected roi ne");
				Log.d("connect", strRSS);
				parseXmlAsync p = new parseXmlAsync(strRSS, fresh);
				p.execute();
				// su kien khi nhan vao 1 book
				mListViewBook.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						strLink = listBookItems.get(position).getLink();
						strTitle = listBookItems.get(position).getTitle();

						Intent intent = new Intent(getActivity()
								.getApplicationContext(),
								Activity_ReadBook.class);
						intent.putExtra("link", strLink);
						intent.putExtra("title", strTitle);

						Log.d("test", strLink);
						Log.d("test", strTitle);

						startActivity(intent);
					}
				});
				// trang thai cua mListViewNews la up hay down
				// up la false
				// down la true
				mListViewBook.setOnScrollListener(new OnScrollListener() {
					private int mLastFirstVisibleItem;
					boolean lvState;

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// Animation mAni;
						if (mLastFirstVisibleItem < firstVisibleItem) {
							Log.d("lv", "down");
							lvState = true;
						}
						if (mLastFirstVisibleItem > firstVisibleItem) {
							Log.d("lv", "up");
							lvState = false;
						}
						mLastFirstVisibleItem = firstVisibleItem;
						if (lvState) {
							// true lv down
							ivFresh.setVisibility(View.GONE);
							// mAni =
							// AnimationUtils.loadAnimation(getActivity(),
							// R.anim.zoom_out);
							// ivFresh.setAnimation(mAni);
						} else {
							ivFresh.setVisibility(View.VISIBLE);
							// false lv up
							// mAni =
							// AnimationUtils.loadAnimation(getActivity(),
							// R.anim.zoom_out);
							// ivFresh.setAnimation(mAni);
						}
					}
				});

			} catch (Exception e) {
			}

		} else {
			// notify user you are not online
			Log.d("connect", "disconnected");

			// hien thi anh dong
			mIntent = new Intent(getActivity(), Activity_Dialog_Noconnect.class);
			startActivity(mIntent);
		}
	}

	// class dung de parse du lieu tin tuc cua chuyen muc hien tai
	private class parseXmlAsync extends AsyncTask<String, String, String> {
		// ham tao, booleam fresh= true la dc goi neu muon load them tin moi
		String currentRSS = "";
		boolean fresh;

		public parseXmlAsync(String rss, boolean fresh) {
			this.currentRSS = rss;
			this.fresh = fresh;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mDialog.setCancelable(true);
			mDialog = ProgressDialog.show(getActivity(), "VUI LÒNG CHỜ...",
					"ĐANG TẢI DỮ LIỆU, VIỆC NÀY SẼ KHÔNG QUÁ LÂU...");
		}

		@Override
		protected String doInBackground(String... strings) {

			try {

				/*
				 * You may change the value of x to try different sources of XML
				 * 
				 * @1 = XML from SD Card
				 * 
				 * @2 = XML from URL
				 * 
				 * @3 = XML from assets folder
				 */
				int x = 2;

				// initialize our input source variable
				InputSource inputSource = null;

				// XML from sdcard
				if (x == 1) {

					// make sure sample.xml is in your root SD card directory
					File xmlFile = new File(
							Environment.getExternalStorageDirectory()
									+ "/a.xml");
					FileInputStream xmlFileInputStream = new FileInputStream(
							xmlFile);
					inputSource = new InputSource(xmlFileInputStream);
				}

				// XML from URL
				else if (x == 2) {
					// specify a URL
					// make sure you are connected to the internet
					URL url = new URL(currentRSS + "");
					inputSource = new InputSource(url.openStream());
					// inputSource.setEncoding("UTF-8");
				}

				// XML from assets folder
				else if (x == 3) {
					inputSource = new InputSource(getActivity()
							.getApplicationContext().getAssets().open("a.xml"));
				}

				// instantiate SAX parser
				SAXParserFactory saxParserFactory = SAXParserFactory
						.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();

				// get the XML reader
				XMLReader xmlReader = saxParser.getXMLReader();

				// prepare and set the XML content or data handler before
				// parsing
				FragmentBook_XmlContentHandler xmlContentHandler = new FragmentBook_XmlContentHandler();
				xmlReader.setContentHandler(xmlContentHandler);

				// parse the XML input source
				xmlReader.parse(inputSource);

				// put the parsed data to a List
				List<FragmentBook_ParsedDataSet> parsedDataSet = xmlContentHandler
						.getParsedData();

				// we'll use an iterator so we can loop through the data
				Iterator<FragmentBook_ParsedDataSet> i = parsedDataSet
						.iterator();
				FragmentBook_ParsedDataSet dataItem;

				while (i.hasNext()) {

					dataItem = (FragmentBook_ParsedDataSet) i.next();
					String parentTag = dataItem.getParentTag();
					Log.v(LOG_TAG, "parentTag: " + parentTag);

					if (parentTag.equals("item")) {
						Log.v(LOG_TAG, "link: " + dataItem.getLink());
						Log.v(LOG_TAG, "title: " + dataItem.getTitle());
						Log.v(LOG_TAG, "des: " + dataItem.getDescription());
						Log.v(LOG_TAG, "pubdate: " + dataItem.getPubDate());
						strLink = dataItem.getLink();
						strTitle = dataItem.getTitle();
						strPubDate = dataItem.getPubDate();
						//
						String des_linkimage = dataItem.getDescription()
								.toString();

						int indexdau_linkimage = des_linkimage.indexOf("\"");
						int indexcuoi_linkimage = des_linkimage
								.lastIndexOf("\"");
						strLinkImage = des_linkimage.substring(
								indexdau_linkimage + 1, indexcuoi_linkimage);

						int indexdau_des = des_linkimage.indexOf("<p>");
						int indexcuoi_des = des_linkimage.indexOf("</p>");
						strDes = des_linkimage.substring(indexdau_des + 3,
								indexcuoi_des);
						//
						FragmentBook_ParsedDataSet p = new FragmentBook_ParsedDataSet(
								strTitle, strDes, strLink, strPubDate,
								strLinkImage);
						listBookItems.add(p);
					}
				}

			} catch (NullPointerException e) {

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String lenghtOfFile) {
			try {
				MyAdapter_LoadDataBook adap = new MyAdapter_LoadDataBook(
						getActivity(), listBookItems);
				adap.notifyDataSetChanged();
				mListViewBook.setAdapter(adap);

				// dismiss
				mDialog.dismiss();

				// thuc hien getData neu dc goi tu su kien nha ivFresh
				if (fresh == true) {
					mListViewBook.onRestoreInstanceState(state);
					Toast.makeText(getActivity(), "Đã cập nhật tin mới...",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}
		}
	}

	public class MyAdapter_LoadDataBook extends BaseAdapter {
		// lazy list
		public ImageLoader imageLoader;
		// context duoc truyen vao
		Context c;
		// list book da parse
		ArrayList<FragmentBook_ParsedDataSet> al;

		public MyAdapter_LoadDataBook(Context c,
				ArrayList<FragmentBook_ParsedDataSet> ds) {
			this.c = c;
			this.al = ds;
			// tao lazy list
			imageLoader = new ImageLoader(getActivity().getApplicationContext());
		}

		@Override
		public int getCount() {
			return al.size();
		}

		@Override
		public Object getItem(int position) {
			return al.get(position).getLink();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public class ViewHolder {
			public ImageView iv;
			public TextView title, des, pubdate;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder oneView;
			LayoutInflater inf = ((Activity) c).getLayoutInflater();

			if (convertView == null) {
				oneView = new ViewHolder();
				convertView = inf
						.inflate(
								R.layout.menu_fragment_news_listview_item_one_news_object,
								null);
				oneView.iv = (ImageView) convertView.findViewById(R.id.iv);
				oneView.title = (TextView) convertView.findViewById(R.id.title);
				oneView.des = (TextView) convertView
						.findViewById(R.id.description);
				oneView.pubdate = (TextView) convertView
						.findViewById(R.id.pubdate);

				convertView.setTag(oneView);
			} else
				oneView = (ViewHolder) convertView.getTag();

			// Log.d("link", listBookItems.get(position).getSummaryImg());

			// load anh lazy
			imageLoader.DisplayImage(listBookItems.get(position)
					.getSummaryImg(), oneView.iv);

			oneView.title.setText(al.get(position).getTitle());
			oneView.des.setText(al.get(position).getDescription());
			oneView.pubdate.setText(al.get(position).getPubDate());

			oneView.title.setTypeface(mTypeface, Typeface.BOLD);
			oneView.des.setTypeface(mTypeface);
			oneView.pubdate.setTypeface(mTypeface, Typeface.ITALIC);

			oneView.title.setTextColor(Color.RED);
			oneView.pubdate.setTextColor(Color.BLUE);

			/*
			 * mot_o.title.setTypeface(tf, Typeface.BOLD);
			 * mot_o.des.setTypeface(tf, Typeface.ITALIC);
			 * mot_o.pubdate.setTypeface(tf);
			 */

			// convertView.setAnimation(mAnimation);
			return convertView;
		}
	}
	
	private void loadSettingFont() {
		SharedPreferences mysettings = getActivity().getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		indexFont = mysettings.getInt("indexFont", 1);
		Log.d("indexFont", indexFont + "");
		switch (indexFont) {
		case 0:
			mTypeface = Typeface.DEFAULT;
			break;
		case 1:
			mTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"SEGOEUI.TTF");
			break;
		case 2:
			mTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"tinhyeu.ttf");
			break;
		case 3:
			mTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"thuphap.ttf");
			break;
		default:
			mTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"SEGOEUI.TTF");
			break;
		}
	}

	private void loadSettingBackground() {
		SharedPreferences mysettings = getActivity().getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		indexBackground = mysettings.getInt("indexBackground", 0);
		Log.d("indexBackground", indexBackground + "");
		switch (indexBackground) {
		case 0:
			xmlMain.setBackgroundColor(Color.WHITE);
			mListViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 1:
			xmlMain.setBackgroundColor(Color.parseColor("#ff99ff"));
			mListViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 2:
			xmlMain.setBackgroundColor(Color.parseColor("#99ff66"));
			mListViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 3:
			xmlMain.setBackgroundColor(Color.parseColor("#ccffff"));
			mListViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 4:
			xmlMain.setBackgroundResource(R.drawable.bg_main);
			mListViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 5:
			xmlMain.setBackgroundResource(R.drawable.bg_news1);
			mListViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 6:
			mListViewBook.setBackgroundColor(Color.TRANSPARENT);
			xmlMain.setBackgroundResource(R.drawable.bg_news2);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		loadSettingFont();
		loadSettingBackground();
		super.onResume();
	}
}
