package com.TPLdev.bookpdf;

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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.book.FragmentBook_MyAdapter;
import com.TPLdev.book.FragmentBook_OneItemInLvMenu;
import com.TPLdev.book.FragmentBook_ParsedDataSet;
import com.TPLdev.book.FragmentBook_XmlContentHandler;
import com.TPLdev.giaitritonghop.R;
import com.TPLdev.lazylist.ImageLoader;
import com.TPLdev.news.Activity_Dialog_Noconnect;

public class FragmentBookPDF extends Fragment {
	// setting
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences_Book";
	// de set font cho webview, dc cai dat trong radioGroupFont
	int indexFont;
	// de set background dua vao index
	int indexBackground;
	// xmlmain de set background
	FrameLayout xmlMain;

	private View parentView;
	// cac chuyen muc tin tuc o sliding menu
	ArrayList<FragmentBook_OneItemInLvMenu> listMenu = new ArrayList<FragmentBook_OneItemInLvMenu>();
	// la listview hien thi chuyen muc len sliding menu
	ListView mListViewSlidingMenu;
	Intent mIntent;

	// layout chua nut nhan doc tin offline
	// LinearLayout lnOffline;
	// ImageView ivOffline;

	// log tag
	public static final String LOG_TAG = "FragmentBook";
	// mang cac chuyen muc tin tuc, luu trong string.xml
	String[] arrRSSMenu;
	// dia chi rss hien tai, moi vo mac dinh la truyen ngan tinh yeu
	String strRSS = "http://tplloi.vfun.me/rss/truyen-ngan";
	// mang cac tin tuc, moi tin la oject
	ArrayList<FragmentBook_ParsedDataSet> listBookItems = new ArrayList<FragmentBook_ParsedDataSet>();

	// mang sau khi search
	// ArrayList<FragmentBook_ParsedDataSet> listAfterFilter;
	// cac thuoc tinh cua 1 object tin tuc, trong do strLinkImage la linkImage
	String strLink, strTitle, strDes, strPubDate, strLinkImage;
	// progress dialog khi load cac tin cua 1 chuyen muc cu the
	ProgressDialog mDialog;
	// set font
	Typeface mTypeface;
	Animation mAnimation;
	// listview hien thi cac tin tuc
	GridView mGridViewBook;
	// image view fresh tin moi
	ImageView ivFresh;
	// index trong arrRSSMenu, ban dau cho la 1 de khac cai mac dinh la tin tuc
	// trong ngay co index 0
	int indexRSS = 1;

	// luu giu trang thai listview
	Parcelable state;

	// doc book pdf offline
	ImageView ivOffline;

	// serach online
	EditText etSearch;
	// adapter
	MyAdapter_LoadDataBook adap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.menu_fragment_book_pdf,
				container, false);
		// anh xa
		xmlMain = (FrameLayout) parentView.findViewById(R.id.xmlMain);
		mGridViewBook = (GridView) parentView.findViewById(R.id.lv_theloai);
		ivFresh = (ImageView) parentView.findViewById(R.id.ivFresh);
		ivOffline = (ImageView) parentView.findViewById(R.id.ivOffline);
		etSearch = (EditText) parentView.findViewById(R.id.etSearch);
		// lnOffline = (LinearLayout) parentView.findViewById(R.id.lnOffline);
		// ivOffline = (ImageView) parentView.findViewById(R.id.ivOffline);
		//
		// mAnimation = AnimationUtils.loadAnimation(getActivity()
		// .getApplicationContext(), R.anim.scale_button);
		// lnOffline.setAnimation(mAnimation);
		//
		// // nhan nut doc offline
		// ivOffline.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // chuyen sang offline
		// mIntent = new Intent(getActivity(),
		// Activity_Book_Saved_List.class);
		// startActivity(mIntent);
		// }
		// });

		// lay mang arrRSSMenu tu trong string.xml
		arrRSSMenu = getResources().getStringArray(R.array.rssbookpdf);
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
				state = mGridViewBook.onSaveInstanceState();
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
				// 25 la length cua chuyen muc tren slidingmenu
				if (indexRSS < 25)
					indexRSS++;
				else
					indexRSS = 0;
			}
		});

		// doc sach offline
		ivOffline.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),
						ActivityListBookDownloaded.class);
				startActivity(i);
			}
		});
		// su kien khi chon 1 sach tren mGridViewBook de tai ve
		mGridViewBook.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ListView Clicked item index
				int itemPosition = position;
				// ListView Clicked item value
				FragmentBook_ParsedDataSet object = (FragmentBook_ParsedDataSet) mGridViewBook
						.getItemAtPosition(itemPosition);

				// name vd Anh Yeu Em
				String name = object.getTitle().toString();
				// link tai tu drive
				String linkDownloadFromDrive = object.getDescription()
						.toString();

				// kiem tra de tai, truyen vao name + link tai
				checkToDownload(name, linkDownloadFromDrive);
			}

		});

		// tao dapter
		adap = new MyAdapter_LoadDataBook(getActivity(), listBookItems);

		// serach online
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// loc adapter
				adap.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		return parentView;
	}

	// thiet lap cho sliding
	private void setUpSlidingViews() {
		mListViewSlidingMenu = (ListView) parentView
				.findViewById(R.id.listview);

		// checkToShowLayoutOffline();

		// set data for listview
		FragmentBook_OneItemInLvMenu a1 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Bài viết");
		FragmentBook_OneItemInLvMenu a2 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Cuộc chiến Việt Nam");
		FragmentBook_OneItemInLvMenu a3 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Cổ tích");
		FragmentBook_OneItemInLvMenu a4 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Cổ văn Việt Nam");
		FragmentBook_OneItemInLvMenu a5 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Hồi ký - Tùy bút");
		FragmentBook_OneItemInLvMenu a6 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Khoa học");
		FragmentBook_OneItemInLvMenu a7 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Khoa học huyền bí");
		FragmentBook_OneItemInLvMenu a8 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Ngôn tình");
		FragmentBook_OneItemInLvMenu a9 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Kinh dị ma quái");
		FragmentBook_OneItemInLvMenu a10 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Kiếm hiệp");
		// FragmentBook_OneItemInLvMenu a11 = new FragmentBook_OneItemInLvMenu(
		// R.drawable.icon_book, "Chuyên mục", "Kịch - Kịch bản");
		FragmentBook_OneItemInLvMenu a12 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Lịch sử");
		FragmentBook_OneItemInLvMenu a13 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Nhân vật lịch sử");
		FragmentBook_OneItemInLvMenu a14 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Phiêu lưu mạo hiểm");
		FragmentBook_OneItemInLvMenu a15 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Suy ngẫm làm người");
		FragmentBook_OneItemInLvMenu a16 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Tiếu lâm");
		FragmentBook_OneItemInLvMenu a17 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Tiểu thuyết");
		FragmentBook_OneItemInLvMenu a18 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Trinh thám hình sự");
		// FragmentBook_OneItemInLvMenu a19 = new FragmentBook_OneItemInLvMenu(
		// R.drawable.icon_book, "Chuyên mục", "Triết học - Kinh tế");
		FragmentBook_OneItemInLvMenu a20 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Trung hoa");
		FragmentBook_OneItemInLvMenu a21 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Truyện dài");
		FragmentBook_OneItemInLvMenu a22 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Truyện ngắn");
		FragmentBook_OneItemInLvMenu a23 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Tuổi học trò");
		FragmentBook_OneItemInLvMenu a24 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Tình cảm xã hội");
		FragmentBook_OneItemInLvMenu a25 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Tôn giáo chính trị");
		FragmentBook_OneItemInLvMenu a26 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Tập truyện ngắn");
		FragmentBook_OneItemInLvMenu a27 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Văn học cổ điển");
		FragmentBook_OneItemInLvMenu a28 = new FragmentBook_OneItemInLvMenu(
				R.drawable.icon_book, "Chuyên mục", "Y học - Sức khỏe");

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
		// listMenu.add(a11);
		listMenu.add(a12);
		listMenu.add(a13);
		listMenu.add(a14);
		listMenu.add(a15);
		listMenu.add(a16);
		listMenu.add(a17);
		listMenu.add(a18);
		// listMenu.add(a19);
		listMenu.add(a20);
		listMenu.add(a21);
		listMenu.add(a22);
		listMenu.add(a23);
		listMenu.add(a24);
		listMenu.add(a25);
		listMenu.add(a26);
		listMenu.add(a27);
		listMenu.add(a28);

		FragmentBook_MyAdapter adapter = new FragmentBook_MyAdapter(
				getActivity(), listMenu);
		mListViewSlidingMenu.setAdapter(adapter);

		// su kien khi nhan vao chuyen muc
		mListViewSlidingMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				default:
					// xoa het du lieu trong listNewsItems di vi da chon 1
					// chuyen muc khac roi
					listBookItems.clear();
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

				// trang thai cua mListViewNews la up hay down
				// up la false
				// down la true

				mGridViewBook.setOnScrollListener(new OnScrollListener() {
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
						//
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

				adap.notifyDataSetChanged();

				mGridViewBook.setAdapter(adap);
				mDialog.dismiss();
				// thuc hien getData neu dc goi tu su kien nha ivFresh
				if (fresh == true) {
					mGridViewBook.onRestoreInstanceState(state);
					Toast.makeText(getActivity(), "Đã cập nhật tin mới...",
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
			}
		}
	}

	// class adapter tu custom, co luon chuc nang search online
	public class MyAdapter_LoadDataBook extends BaseAdapter implements
			Filterable {

		// lazy list
		public ImageLoader imageLoader;
		Context c;
		// mang chua book
		ArrayList<FragmentBook_ParsedDataSet> al;
		// mang chua book de loc
		private ArrayList<FragmentBook_ParsedDataSet> mStringFilterList;
		// gia tri can loc
		private ValueFilter valueFilter;

		public MyAdapter_LoadDataBook(Context c,
				ArrayList<FragmentBook_ParsedDataSet> ds) {
			this.c = c;
			this.al = ds;
			mStringFilterList = ds;
			// lazy list
			imageLoader = new ImageLoader(getActivity().getApplicationContext());

			getFilter();
		}

		@Override
		public int getCount() {
			return al.size();
		}

		@Override
		public Object getItem(int position) {
			// return al.get(position).getDescription();
			return al.get(position);
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
			final int tmpPosition = position;
			LayoutInflater inf = ((Activity) c).getLayoutInflater();

			if (convertView == null) {
				oneView = new ViewHolder();
				convertView = inf
						.inflate(
								R.layout.menu_fragment_book_pdf_listview_item_one_news_object,
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
			imageLoader.DisplayImage(al.get(position).getSummaryImg(),
					oneView.iv);

			oneView.title.setText(al.get(position).getTitle());
			oneView.des.setText(al.get(position).getDescription());
			oneView.pubdate.setText(al.get(position).getPubDate());

			oneView.title.setTypeface(mTypeface, Typeface.BOLD);
			oneView.des.setTypeface(mTypeface);
			oneView.pubdate.setTypeface(mTypeface, Typeface.ITALIC);

			oneView.title.setTextColor(Color.RED);
			oneView.pubdate.setTextColor(Color.BLUE);

			// convertView.startAnimation(mAnimation);
			return convertView;
		}

		@Override
		public Filter getFilter() {
			if (valueFilter == null) {
				valueFilter = new ValueFilter();
			}
			return valueFilter;
		}

		private class ValueFilter extends Filter {

			// Invoked in a worker thread to filter the data according to the
			// constraint.
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				if (constraint != null && constraint.length() > 0) {
					ArrayList<FragmentBook_ParsedDataSet> filterList = new ArrayList<FragmentBook_ParsedDataSet>();
					for (int i = 0; i < mStringFilterList.size(); i++) {
						if ((mStringFilterList.get(i).getTitle().toUpperCase())
								.contains(constraint.toString().toUpperCase())) {
							FragmentBook_ParsedDataSet objetcBook = new FragmentBook_ParsedDataSet();

							objetcBook.setDescription(mStringFilterList.get(i)
									.getDescription());
							objetcBook.setTitle(mStringFilterList.get(i)
									.getTitle());
							objetcBook.setLink(mStringFilterList.get(i).getLink());
							objetcBook.setPubDate(mStringFilterList.get(i)
									.getPubDate());
							objetcBook.setSummaryImg(mStringFilterList.get(i)
									.getSummaryImg());
							filterList.add(objetcBook);
						}
					}
					results.count = filterList.size();
					results.values = filterList;
				} else {
					results.count = mStringFilterList.size();
					results.values = mStringFilterList;
				}
				return results;
			}

			// Invoked in the UI thread to publish the filtering results in the
			// user interface.
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				al = (ArrayList<FragmentBook_ParsedDataSet>) results.values;

				// adap = new MyAdapter_LoadDataBook(getActivity(), al);
				// mGridViewBook.setAdapter(adap);
				for (int i = 0; i < al.size(); i++)
					Log.d("fil", al.get(i).getTitle() + "");

				// adap = new MyAdapter_LoadDataBook(getActivity(), al);
				// mGridViewBook.setAdapter(adap);
				notifyDataSetChanged();
			}
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
			mGridViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 1:
			xmlMain.setBackgroundColor(Color.parseColor("#ff99ff"));
			mGridViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 2:
			xmlMain.setBackgroundColor(Color.parseColor("#99ff66"));
			mGridViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 3:
			xmlMain.setBackgroundColor(Color.parseColor("#ccffff"));
			mGridViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 4:
			xmlMain.setBackgroundResource(R.drawable.bg_main);
			mGridViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 5:
			xmlMain.setBackgroundResource(R.drawable.bg_news1);
			mGridViewBook.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 6:
			mGridViewBook.setBackgroundColor(Color.TRANSPARENT);
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

	private void checkToDownload(String name, String link) {

		File directory = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/GiaiTriTongHopBook/");

		ArrayList<File> mFiles = getListFiles(directory);

		for (int i = 0; i < mFiles.size(); i++) {
			Log.d("check", mFiles.get(i).toString());
		}

		//

		// kiem tra xem mFile co ton tai tmpLink ko
		// co thi thoi
		// ko co moi cho luu
		if (mFiles.toString().contains(name)) {
			Log.d("check", "y");
			Toast.makeText(
					getActivity(),
					"Bạn đã tải sách này trước đó rồi... Không cần tải về nữa...",
					Toast.LENGTH_SHORT).show();

			// neu chua co thi se hien thi luu file pdf
		} else {
			Log.d("check", "n");

			// new DownloadFilePDF(link, name + ".pdf",
			// getActivity()).execute();

			final ConnectivityManager conMgr = (ConnectivityManager) getActivity()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
			if (activeNetwork != null && activeNetwork.isConnected()) {
				// notify user you are online

				Intent i = new Intent(getActivity(), DownloadFilePDF.class);
				i.putExtra("link", link);
				i.putExtra("name", name + ".pdf");
				startActivity(i);
				// Toast.makeText(getActivity(), link,
				// Toast.LENGTH_SHORT).show();

			} else {
				// notify user you are not online
				Log.d("net", "offline");
				Intent i = new Intent(getActivity(),
						Activity_Dialog_Noconnect.class);
				startActivity(i);
			}

		}

	}

	// lay danh sach cac file da duoc luu trong thu muc GiaiTriTongHopBook
	private ArrayList<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		try {
			File[] files = parentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					inFiles.addAll(getListFiles(file));
				} else {
					if (file.getName().endsWith(".pdf")) {
						inFiles.add(file);
					}
				}
			}
			// for (int i = 0; i < inFiles.size(); i++) {
			// Log.d("file", inFiles.get(i).toString());
			// }
		} catch (Exception e) {
		}

		return inFiles;
	}
}
