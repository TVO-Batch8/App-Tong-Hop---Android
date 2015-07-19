package com.TPLdev.news;

//fargment show tin online (quan trong nhat)
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;
import com.TPLdev.lazylist.ImageLoader;

public class FragmentNews extends Fragment {
	// setting
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
	// de set font cho webview, dc cai dat trong radioGroupFont
	int indexFont;
	// de set background dua vao index
	int indexBackground;
	// xmlmain de set background
	FrameLayout xmlMain;

	private View parentView;
	// cac chuyen muc tin tuc o sliding menu
	ArrayList<FragmentNews_OneItemInLvMenu> listMenu = new ArrayList<FragmentNews_OneItemInLvMenu>();
	// la listview hien thi chuyen muc len sliding menu
	ListView mListViewSlidingMenu;
	Intent mIntent;

	// layout chua nut nhan doc tin offline
	LinearLayout lnOffline;
	ImageView ivOffline;

	// log tag
	public static final String LOG_TAG = "FragmentNews";
	// mang cac chuyen muc tin tuc, luu trong string.xml
	String[] arrRSSMenu;
	// dia chi rss hien tai, moi vo mac dinh la tin tuc trong ngay
	String strRSS = "http://www.24h.com.vn/upload/rss/tintuctrongngay.rss";
	// mang cac tin tuc, moi tin la oject
	ArrayList<FragmentNews_ParsedDataSet> listNewsItems = new ArrayList<FragmentNews_ParsedDataSet>();
	// cac thuoc tinh cua 1 object tin tuc, trong do strLinkImage la summaryimg
	String strLink, strTitle, strDes, strPubDate, strLinkImage;
	// progress dialog khi load cac tin cua 1 chuyen muc cu the
	ProgressDialog mDialog;
	// set font
	Typeface mTypeface;
	Animation mAnimation;
	// listview hien thi cac tin tuc
	ListView mListViewNews;
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
		// anh xa
		parentView = inflater.inflate(R.layout.menu_fragment_news, container,
				false);
		xmlMain = (FrameLayout) parentView.findViewById(R.id.xmlMain);
		mListViewNews = (ListView) parentView.findViewById(R.id.lv_theloai);
		ivFresh = (ImageView) parentView.findViewById(R.id.ivFresh);
		lnOffline = (LinearLayout) parentView.findViewById(R.id.lnOffline);
		ivOffline = (ImageView) parentView.findViewById(R.id.ivOffline);

		mAnimation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.scale_button);
		lnOffline.setAnimation(mAnimation);

		// nhan nut doc offline
		ivOffline.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// chuyen sang offline
				mIntent = new Intent(getActivity(),
						Activity_News_Saved_List.class);
				startActivity(mIntent);
			}
		});

		// lay mang arrRSSMenu tu trong string.xml
		arrRSSMenu = getResources().getStringArray(R.array.rssloai);
		// cai dat font va animation
		loadSettingFont();

		mAnimation = AnimationUtils.loadAnimation(getActivity()
				.getApplicationContext(), R.anim.fl_dung);
		// cai dat du lieu cho slidingmenu phan chon chuyen muc
		setUpSlidingViews();
		// lay du lieu tu rss, mac dinh moi vao la tin tuc trong ngay
		getDataFromRSS(false);
		// su kien fresh tin moi khi nhan vao ivFresh
		ivFresh.setVisibility(View.GONE);
		ivFresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// giu lai trang thai hien tai cua mListViewNews dang o dau
				state = mListViewNews.onSaveInstanceState();
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
				if (indexRSS < 20)
					indexRSS++;
				else
					indexRSS = 0;
			}
		});

		//
		return parentView;
	}

	// thiet lap cho sliding
	private void setUpSlidingViews() {
		mListViewSlidingMenu = (ListView) parentView
				.findViewById(R.id.listview);

		// checkToShowLayoutOffline();

		// set data for listview
		FragmentNews_OneItemInLvMenu a1 = new FragmentNews_OneItemInLvMenu(
				R.drawable.icon_news, "Chuyên mục", "Tin tức trong ngày");
		FragmentNews_OneItemInLvMenu a2 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_bongda, "Chuyên mục", "Bóng đá");
		FragmentNews_OneItemInLvMenu a3 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_anninh, "Chuyên mục", "An ninh - Hình sự");
		FragmentNews_OneItemInLvMenu a4 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_thoitrang, "Chuyên mục", "Thời trang");
		FragmentNews_OneItemInLvMenu a5 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_hitech, "Chuyên mục", "Thời trang Hi-tech");
		FragmentNews_OneItemInLvMenu a6 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_batdongsan, "Chuyên mục",
				"Tài chính - Bất động sản");
		FragmentNews_OneItemInLvMenu a7 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_amthuc, "Chuyên mục", "Ẩm thực");
		FragmentNews_OneItemInLvMenu a8 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_lamdep, "Chuyên mục", "Làm đẹp");
		FragmentNews_OneItemInLvMenu a9 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_phim, "Chuyên mục", "Phim");
		FragmentNews_OneItemInLvMenu a10 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_giaoduc, "Chuyên mục", "Giáo dục - Du học");
		FragmentNews_OneItemInLvMenu a11 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_cuocsong, "Chuyên mục", "Bạn trẻ - Cuộc sống");
		FragmentNews_OneItemInLvMenu a12 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_mtv, "Chuyên mục", "Ca nhạc - MTV");
		FragmentNews_OneItemInLvMenu a13 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_thethao, "Chuyên mục", "Thể thao");
		FragmentNews_OneItemInLvMenu a14 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_phithuong, "Chuyên mục", "Phi thường - Kỳ quặc");
		FragmentNews_OneItemInLvMenu a15 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_cntt, "Chuyên mục", "Công nghệ thông tin");
		FragmentNews_OneItemInLvMenu a16 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_xe, "Chuyên mục", "Ô tô - Xe máy");
		FragmentNews_OneItemInLvMenu a17 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_thitruong, "Chuyên mục",
				"Thị trường - Tiêu dùng");
		FragmentNews_OneItemInLvMenu a18 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_dulich, "Chuyên mục", "Du lịch");
		FragmentNews_OneItemInLvMenu a19 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_suckhoe, "Chuyên mục", "Sức khỏe - Đời sống");
		FragmentNews_OneItemInLvMenu a20 = new FragmentNews_OneItemInLvMenu(
				R.drawable.news_cuoi, "Chuyên mục", "Cười 24h");

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

		FragmentNews_MyAdapter adapter = new FragmentNews_MyAdapter(
				getActivity(), listMenu);
		mListViewSlidingMenu.setAdapter(adapter);
		mListViewSlidingMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {

				default:
					// xoa het du lieu trong listNewsItems di vi da chon 1
					// chuyen muc khac roi
					listNewsItems.clear();
					// strRSS luc nay se la rss moi
					strRSS = arrRSSMenu[position];
					// Toast.makeText(getActivity(), strRSS, Toast.LENGTH_SHORT)
					// .show();
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

				mListViewNews.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						strLink = listNewsItems.get(position).getLink();
						strTitle = listNewsItems.get(position).getTitle();
						// Toast.makeText(getActivity(), strLink,
						// Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(getActivity()
								.getApplicationContext(),
								Activity_ReadNews.class);
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

				mListViewNews.setOnScrollListener(new OnScrollListener() {
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
				FragmentNews_XmlContentHandler xmlContentHandler = new FragmentNews_XmlContentHandler();
				xmlReader.setContentHandler(xmlContentHandler);

				// parse the XML input source
				xmlReader.parse(inputSource);

				// put the parsed data to a List
				List<FragmentNews_ParsedDataSet> parsedDataSet = xmlContentHandler
						.getParsedData();

				// we'll use an iterator so we can loop through the data
				Iterator<FragmentNews_ParsedDataSet> i = parsedDataSet
						.iterator();
				FragmentNews_ParsedDataSet dataItem;

				while (i.hasNext()) {

					dataItem = (FragmentNews_ParsedDataSet) i.next();
					String parentTag = dataItem.getParentTag();
					Log.v(LOG_TAG, "parentTag: " + parentTag);

					if (parentTag.equals("item")) {
						Log.v(LOG_TAG, "title: " + dataItem.getTitle());
						Log.v(LOG_TAG, "des: " + dataItem.getDescription());
						Log.v(LOG_TAG, "link: " + dataItem.getLink());
						Log.v(LOG_TAG, "pubdate: " + dataItem.getPubDate());
						Log.v(LOG_TAG,
								"summaryImg: " + dataItem.getSummaryImg());
						strLink = dataItem.getLink();
						strTitle = dataItem.getTitle();
						strPubDate = dataItem.getPubDate();
						strDes = dataItem.getDescription();
						strLinkImage = dataItem.getSummaryImg();

						//
						FragmentNews_ParsedDataSet p = new FragmentNews_ParsedDataSet(
								strTitle, strDes, strLink, strPubDate,
								strLinkImage);
						listNewsItems.add(p);
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
				MyAdapter_LoadDataNews adap = new MyAdapter_LoadDataNews(
						getActivity(), listNewsItems);
				adap.notifyDataSetChanged();
				mListViewNews.setAdapter(adap);
				mDialog.dismiss();
				// thuc hien getData neu dc goi tu su kien nha ivFresh
				if (fresh == true) {
					mListViewNews.onRestoreInstanceState(state);
					Toast.makeText(getActivity(), "Đã cập nhật tin mới...",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}
		}
	}

	public class MyAdapter_LoadDataNews extends BaseAdapter {
		// lazy list
		public ImageLoader imageLoader;
		Context c;
		ArrayList<FragmentNews_ParsedDataSet> al;

		public MyAdapter_LoadDataNews(Context c,
				ArrayList<FragmentNews_ParsedDataSet> ds) {
			this.c = c;
			this.al = ds;
			// lazy list
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

			Log.d("link", listNewsItems.get(position).getSummaryImg());

			// load anh lazy
			imageLoader.DisplayImage(listNewsItems.get(position)
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

			convertView.setAnimation(mAnimation);
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
			mListViewNews.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 1:
			xmlMain.setBackgroundColor(Color.parseColor("#ff99ff"));
			mListViewNews.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 2:
			xmlMain.setBackgroundColor(Color.parseColor("#99ff66"));
			mListViewNews.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 3:
			xmlMain.setBackgroundColor(Color.parseColor("#ccffff"));
			mListViewNews.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 4:
			xmlMain.setBackgroundResource(R.drawable.bg_main);
			mListViewNews.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 5:
			xmlMain.setBackgroundResource(R.drawable.bg_news1);
			mListViewNews.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 6:
			mListViewNews.setBackgroundColor(Color.TRANSPARENT);
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
