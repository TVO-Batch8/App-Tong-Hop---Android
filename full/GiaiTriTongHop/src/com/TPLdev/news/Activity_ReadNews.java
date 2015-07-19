package com.TPLdev.news;

//doc tin tuc online
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.System;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class Activity_ReadNews extends Activity implements OnClickListener {
	// setting
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
	// de set background dua vao index
	int indexBackground;
	// de set font cho webview, dc cai dat trong radioGroupFont
	int indexFont;

	// 4 nut chon font trong sliding
	RadioButton rbFont0, rbFont1, rbFont2, rbFont3;
	// ten cua font->mac dinh, mum mim, tinh yeu, thu phap
	String nameOfFont;
	// xmlMain
	RelativeLayout xmlMain;
	// link html lay tu intent
	String mLink;
	// link chinh xac tai 1 thoi diem cu the, la mLink lan dau
	// tien, sau do la link nhan tiep theo khi nhan vao webview
	// client
	String mainLink;
	// title bai bao lay ra tu intent
	String mTitle;
	// wv show tin
	private WebView mWebView;
	// hien thi truoc khi load tin
	private ProgressBar loadingProgressBar;
	private TextView txtLoading;
	// set do sang man hinh
	// Seek bar object
	private SeekBar brightbar;
	// Variable to store brightness value
	private int brightness;
	// Content resolver used as a handle to the system's settings
	private ContentResolver cResolver;
	// Window object, that will store a reference to the current window
	private Window window;

	// share, shareFB, save o trong sliding menu
	ImageView ivShare, ivShareFB, ivSave;

	// mReadCont la bien kiem tra xem co nhan link nao moi o webview
	// client hay ko, false la
	// ko co, true la co. Neu co nhan thi khi share fb de tieu de share khac
	boolean mReadCont = false;

	// bien dung cho setting background
	ImageView imageView0, imageView1, imageView2, imageView3, imageView4,
			imageView5, imageView6;

	// quan ly co so du lieu
	Dtb_NewsSavedManager qlb;

	// bien cho facebook
	// private static final String FB_PERMISSION = "publish_actions";
	private boolean canPresentShareDialog;
	private boolean canPresentShareDialogWithPhotos;
	private ShareDialog shareDialog;
	private CallbackManager callbackManager;
	private final String PENDING_ACTION_BUNDLE_KEY = "com.example.hellofacebook:PendingAction";

	private enum PendingAction {
		NONE, FB_SHARE_PHOTO, FB_SHARE_POST
	}

	private PendingAction pendingAction = PendingAction.NONE;
	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
		@Override
		public void onCancel() {
		}

		@Override
		public void onError(FacebookException error) {
			String title = getString(R.string.error);
			String alertMessage = error.getMessage();
			showResult(title, alertMessage);
		}

		@Override
		public void onSuccess(Sharer.Result result) {
			if (result.getPostId() != null) {
				String title = getString(R.string.success);
				String id = result.getPostId();
				String alertMessage = getString(
						R.string.successfully_posted_post, id);
				showResult(title, alertMessage);
			}
		}

		private void showResult(String title, String alertMessage) {
			new AlertDialog.Builder(Activity_ReadNews.this).setTitle(title)
					.setMessage(alertMessage)
					.setPositiveButton(R.string.ok, null).show();
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// full man hinh
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// facebook sdk
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		callbackManager = CallbackManager.Factory.create();

		// anh xa
		setContentView(R.layout.menu_fragment_news_read_news);
		xmlMain = (RelativeLayout) findViewById(R.id.xmltrum);
		mWebView = (WebView) findViewById(R.id.webView1);

		brightbar = (SeekBar) findViewById(R.id.brightbar);

		ivSave = (ImageView) findViewById(R.id.ivSave);
		ivShare = (ImageView) findViewById(R.id.ivShare);
		ivShareFB = (ImageView) findViewById(R.id.ivShareFB);

		imageView0 = (ImageView) findViewById(R.id.imageView0);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		imageView4 = (ImageView) findViewById(R.id.imageView4);
		imageView5 = (ImageView) findViewById(R.id.imageView5);
		imageView6 = (ImageView) findViewById(R.id.imageView6);

		rbFont0 = (RadioButton) findViewById(R.id.radioButton0);
		rbFont1 = (RadioButton) findViewById(R.id.radioButton1);
		rbFont2 = (RadioButton) findViewById(R.id.radioButton2);
		rbFont3 = (RadioButton) findViewById(R.id.radioButton3);

		// lay link ra tu intent
		mLink = getIntent().getExtras().getString("link");
		// moi lay ra thi tai thoi diem nay mainLink la mLink
		mainLink = mLink;
		mTitle = getIntent().getExtras().getString("title");

		// cai dat cho webview
		final WebSettings webSettings = mWebView.getSettings();
		Resources res = getResources();
		// size chu 10sp
		float fontSize = res.getDimension(R.dimen.txtSize);
		webSettings.setDefaultFontSize((int) fontSize);

		webSettings.setRenderPriority(RenderPriority.HIGH);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		// webSettings.setBlockNetworkImage(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setGeolocationEnabled(false);
		webSettings.setNeedInitialFocus(false);
		webSettings.setSaveFormData(false);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		mWebView.setBackgroundColor(Color.TRANSPARENT);
		mWebView.setWebViewClient(new myWebClient());
		mWebView.getSettings().setJavaScriptEnabled(true);

		// co so du lieu
		qlb = new Dtb_NewsSavedManager(Activity_ReadNews.this);

		// lay noi dung tu mLink la link dc truyen qua intent
		loadContentFromLink(mLink);

		// set do sang man hinh trong sliding menu
		setBright();

		// cac ham xu ly trong sliding menu nhu share, shareFB, save
		setFunctionShare();

		// setting background trong sliding menu
		setFunctionBackground();

		// load background da luu
		loadSettingBackground();

		// setting font trong sliding menu
		setFunctionFont();

		// load setting font da luu
		loadSettingFont();

		// facebook
		shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, shareCallback);

		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}
		// Can we present the share dialog for regular links?
		canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);

		// Can we present the share dialog for photos?
		canPresentShareDialogWithPhotos = ShareDialog
				.canShow(SharePhotoContent.class);
	}

	public class myWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// bien boolean mReadCont = true vi da mo them 1 link moi

			Log.d("url", url);
			// cho mainLink =url dung cho share fb

			Log.d("mLink", mLink);
			if (url.contains("://plus.google.com/share?url")) {
				// goi intentShare - chuc nang share
				intentShare();
			} else if (url.contains("facebook.com/sharer/sharer.php")) {
				// goi ham share qua facebook
				facebookSharePost();
			} else {
				// load trang moi can load setting font lai, vi co the nguoi
				// dung da thay doi setting font roi
				loadSettingFont();

				mReadCont = true;
				loadContentFromLink(url);
				// vi doc tiep nen mainLink luc nay la url moi
				mainLink = url;
			}
			return true;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint({ "ParserError", "ParserError" })
	private void loadContentFromLink(final String url) {

		final ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

		loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		loadingProgressBar.setVisibility(View.VISIBLE);
		txtLoading = (TextView) findViewById(R.id.textView1);

		if (activeNetwork != null && activeNetwork.isConnected()) {
			// notify user you are online
			try {
				new Thread(new Runnable() {
					public void run() {
						final String newPage;
						try {

							Document doc = Jsoup.connect(url).get();
							// div[class=content]
							Elements newsRawTag = doc
									.select("div[class=text-conent]");
							// Elements newsRawTag =
							// doc.select("div[class=content]");
							newPage = newsRawTag.html();

							runOnUiThread(new Runnable() {
								public void run() {
									try {
										HtmlCleaner cleaner = new HtmlCleaner();
										CleanerProperties props = cleaner
												.getProperties();
										TagNode tagNode = new HtmlCleaner(props)
												.clean(newPage);
										SimpleHtmlSerializer htmlSerializer = new SimpleHtmlSerializer(
												props);
										//
										String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/"
												+ nameOfFont
												+ "\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
										String pas = "<br/><br/><br/></body></html>";
										String myHtmlString = pish
												+ htmlSerializer
														.getAsString(tagNode)
												+ pas;
										mWebView.loadDataWithBaseURL(null,
												myHtmlString, "text/html",
												"charset=UTF-8", null);

										// an di
										loadingProgressBar
												.setVisibility(View.GONE);
										txtLoading.setVisibility(View.GONE);
									} catch (Exception e) {

									}
								}
							});
						} catch (Exception e) {

						}

					}
				}).start();
			} catch (Exception e) {
			}

		} else {
			// notify user you are not online
			loadingProgressBar.setVisibility(View.INVISIBLE);
			txtLoading.setVisibility(View.INVISIBLE);

			Log.d("connect", "disconnected");

			Intent i = new Intent(getApplicationContext(),
					Activity_Dialog_Noconnect.class);
			startActivity(i);
		}

	}

	//chinh do sang man hinh
	private void setBright() {

		// Get the content resolver
		cResolver = getContentResolver();

		// Get the current window
		window = getWindow();

		// Set the seekbar range between 0 and 255
		brightbar.setMax(255);

		// Set the seek bar progress to 1
		brightbar.setKeyProgressIncrement(1);

		try {
			// Get the current system brightness
			brightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			// Throw an error case it couldn't be retrieved
			Log.e("Error", "cannot access system brightness.");
			e.printStackTrace();
		}

		// Set the progress of the seek bar based on the system's brightness
		brightbar.setProgress(brightness);

		// Register OnSeekBarChangeListener, so it can actually change values
		brightbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Set the system brightness using the brightness variable value
				System.putInt(cResolver, System.SCREEN_BRIGHTNESS, brightness);
				// Get the current window attributes
				LayoutParams layoutpars = window.getAttributes();

				// Set the brightness of this window
				layoutpars.screenBrightness = brightness / (float) 255;
				// Apply attribute changes to this window
				window.setAttributes(layoutpars);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// Nothing handled here
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Set the minimal brightness level
				// if seek bar is 20 or any value below
				if (progress <= 20) {
					// Set the brightness to 20
					brightness = 20;
				} else // brightness is greater than 20
				{
					// Set brightness variable based on the progress bar
					brightness = progress;
				}

				// Calculate the brightness percentage
				// float perc = (brightness / (float) 255) * 100;

				// Set the brightness percentage
			}
		});

	}

	private void setFunctionShare() {
		ivSave.setOnClickListener(this);
		ivShare.setOnClickListener(this);
		ivShareFB.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//luu tin 
		case R.id.ivSave:

			Log.d("check", mainLink);
			File directory = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/GiaiTriTongHopBao/");

			ArrayList<File> mFiles = getListFiles(directory);

			for (int i = 0; i < mFiles.size(); i++) {
				Log.d("check", mFiles.get(i).toString());
			}

			// downloadLink co dang la
			// http://www.24h.com.vn/tin-tuc-trong-ngay/......html
			// tmpTile chi lay ra cai tieu de thoi
			int tmpIndex = mainLink.lastIndexOf("/");
			String tmpTilte = mainLink.substring(tmpIndex + 1,
					mainLink.length());
			Log.d("check", tmpTilte);
			//
			String tmpLink = "/storage/emulated/0/GiaiTriTongHopBao/"
					+ tmpTilte;
			Log.d("check", tmpLink);
			// kiem tra xem mFile co ton tai tmpLink ko
			// co thi thoi
			// ko co moi cho luu
			if (mFiles.toString().contains(tmpLink)) {
				Log.d("check", "y");
				Toast.makeText(
						getApplicationContext(),
						"Bạn đã lưu tin này trước đó rồi... Không cần tải về nữa...",
						Toast.LENGTH_SHORT).show();

				// neu chua co thi se hien thi hop thoai yeu cau nhap ten cho
				// bai bao
			} else {
				Log.d("check", "n");

				Intent i = new Intent(getApplicationContext(),
						Activity_Dialog_TypeNameToSaveNews.class);
				i.putExtra("kieu", "them");
				i.putExtra("link", mainLink);
				startActivityForResult(i, 999);
			}
			break;
		case R.id.ivShare:
			intentShare();
			break;
		case R.id.ivShareFB:
			facebookSharePost();
			break;
		case R.id.imageView0:
			saveSettingBackground(0);
			loadSettingBackground();
			break;
		case R.id.imageView1:
			saveSettingBackground(1);
			loadSettingBackground();
			break;
		case R.id.imageView2:
			saveSettingBackground(2);
			loadSettingBackground();
			break;
		case R.id.imageView3:
			saveSettingBackground(3);
			loadSettingBackground();
			break;
		case R.id.imageView4:
			saveSettingBackground(4);
			loadSettingBackground();
			break;
		case R.id.imageView5:
			saveSettingBackground(5);
			loadSettingBackground();
			break;
		case R.id.imageView6:
			saveSettingBackground(6);
			loadSettingBackground();
			break;
		case R.id.radioButton0:
			eventClickRadioButton(0);
			break;
		case R.id.radioButton1:
			eventClickRadioButton(1);
			break;
		case R.id.radioButton2:
			eventClickRadioButton(2);
			break;
		case R.id.radioButton3:
			eventClickRadioButton(3);
			break;
		default:
			break;
		}

	}

	// ham dong slider
	// @SuppressWarnings("deprecation")
	// private void closeSlidingDrawer()
	// {
	// if (slider.isOpened()) {
	// slider.close ();
	// } else {
	// super.onBackPressed();
	// }
	// }
	void intentShare() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Tin mới:\n\n\n" + mainLink;
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Bài báo này hay nè mọi người...");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Chia sẻ qua: "));

	}

	private void facebookSharePost() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				Activity_ReadNews.this);
		builder.setMessage("Bạn có muốn share bài báo này lên facebook không?");
		builder.setCancelable(true);
		builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// dau tien kiem tra xem share fb tu link mo lan dau tien hay mo
				// tiep tuc trong
				// webview client. Neu = true thi co mo => doi title bai bao
				// khac di. False thi thoi
				if (mReadCont == true)
					mTitle = "Tin mới Giải trí tổng hợp";

				Profile profile = Profile.getCurrentProfile();
				ShareLinkContent linkContent = new ShareLinkContent.Builder()
						.setContentTitle("Tin giật gân đây mọi người ơi...")
						.setContentDescription(mTitle)
						.setContentUrl(Uri.parse(mainLink + "")).build();
				Log.d("mLink", mainLink);
				if (canPresentShareDialog) {
					shareDialog.show(linkContent);
				} else if (profile != null && hasPublishPermission()) {
					ShareApi.share(linkContent, shareCallback);
				} else {
					pendingAction = PendingAction.FB_SHARE_POST;
				}
				//
			}
		});
		builder.setNegativeButton("Không",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert1 = builder.create();
		alert1.show();

	}

	private boolean hasPublishPermission() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null
				&& accessToken.getPermissions().contains("publish_actions");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		callbackManager.onActivityResult(requestCode, resultCode, data);
		// lay du lieu ve khi nhan luu tin -> chuyen sang activity nhap name
		if (requestCode == 999 && resultCode == RESULT_OK)// tu them tra ve
		{
			Dtb_OneNewsObject c = (Dtb_OneNewsObject) data.getExtras().get(
					"congviec");
			qlb.createNews(c);

			saveToFavourite(mainLink, 0);
			// dodulieu();
		}
//		else if (requestCode == 333 && resultCode == RESULT_OK) {
//			Dtb_OneNewsObject c = (Dtb_OneNewsObject) data.getExtras().get(
//					"congviec");
//			qlb.editNews(c);
//			// dodulieu();
//		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	// ham chinh background
	private void setFunctionBackground() {
		imageView0.setOnClickListener(this);
		imageView1.setOnClickListener(this);
		imageView2.setOnClickListener(this);
		imageView3.setOnClickListener(this);
		imageView4.setOnClickListener(this);
		imageView5.setOnClickListener(this);
		imageView6.setOnClickListener(this);
	}

	//luu index de set background
	private void saveSettingBackground(int indexBackground) {
		SharedPreferences settingsfile = getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		SharedPreferences.Editor myeditor = settingsfile.edit();
		myeditor.putInt("indexBackground", indexBackground);
		myeditor.commit();
	}

	private void loadSettingBackground() {
		SharedPreferences mysettings = getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		indexBackground = mysettings.getInt("indexBackground", 0);
		Log.d("indexBackground", indexBackground + "");
		switch (indexBackground) {
		case 0:
			mWebView.setBackgroundColor(Color.WHITE);
			xmlMain.setBackgroundColor(Color.WHITE);
			break;
		case 1:
			mWebView.setBackgroundColor(Color.parseColor("#ff99ff"));
			xmlMain.setBackgroundColor(Color.parseColor("#ff99ff"));
			break;
		case 2:
			mWebView.setBackgroundColor(Color.parseColor("#99ff66"));
			xmlMain.setBackgroundColor(Color.parseColor("#99ff66"));
			break;
		case 3:
			mWebView.setBackgroundColor(Color.parseColor("#ccffff"));
			xmlMain.setBackgroundColor(Color.parseColor("#ccffff"));
			break;
		case 4:
			mWebView.setBackgroundColor(Color.TRANSPARENT);
			xmlMain.setBackgroundResource(R.drawable.bg_main);
			break;
		case 5:
			mWebView.setBackgroundColor(Color.TRANSPARENT);
			xmlMain.setBackgroundResource(R.drawable.bg_news1);
			break;
		case 6:
			mWebView.setBackgroundColor(Color.TRANSPARENT);
			xmlMain.setBackgroundResource(R.drawable.bg_news2);
			break;
		default:
			break;
		}
	}

	// ham chinh font cho webview
	private void setFunctionFont() {
		// load setting font de biet coi indexFont da luu la bao nhieu de
		// setcheck cho no
		loadSettingFont();
		switch (indexFont) {
		case 0:
			rbFont0.setChecked(true);
			break;
		case 1:
			rbFont1.setChecked(true);
			break;
		case 2:
			rbFont2.setChecked(true);
			break;
		case 3:
			rbFont3.setChecked(true);
			break;
		default:
			break;
		}

		rbFont0.setOnClickListener(this);
		rbFont1.setOnClickListener(this);
		rbFont2.setOnClickListener(this);
		rbFont3.setOnClickListener(this);
	}

	//luu index de setting font
	private void saveSettingFont(int indexFont) {
		SharedPreferences settingsfile = getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		SharedPreferences.Editor myeditor = settingsfile.edit();
		myeditor.putInt("indexFont", indexFont);
		myeditor.commit();
	}

	private void loadSettingFont() {
		SharedPreferences mysettings = getSharedPreferences(
				PREFERENCES_FILE_NAME, 0);
		indexFont = mysettings.getInt("indexFont", 1);
		Log.d("indexFont", indexFont + "");
		switch (indexFont) {
		case 0:
			nameOfFont = "";
			break;
		case 1:
			nameOfFont = "SEGOEUI.TTF";
			break;
		case 2:
			nameOfFont = "tinhyeu.ttf";
			break;
		case 3:
			nameOfFont = "thuphap.ttf";
			break;
		default:
			nameOfFont = "SEGOEUI.TTF";
			break;
		}
		Log.d("nameOfFont", nameOfFont);
	}

	// xu ly su kien khi 1 radio button duoc nhan
	private void eventClickRadioButton(int i) {
		Log.d("indexFont", i + "");
		saveSettingFont(i);
		loadSettingFont();
		loadContentFromLink(mainLink);
	}

	// lay danh sach cac file da duoc luu trong thu muc GiaiTriTongHopBao
	private ArrayList<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		try {
			File[] files = parentDir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					inFiles.addAll(getListFiles(file));
				} else {
					if (file.getName().endsWith(".html")) {
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

	// xu ly save file html, kiem tra xem file co ton tai chua
	// neu chua thi moi cho luu
	// neu co roi thi thong bao da luu tin nay roi
	void saveToFavourite(String downloadlink, int choice) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			// execute this when the downloader must be fired
			DownloadFile downloadFile = new DownloadFile(this, mainLink);
			downloadFile.execute();

		} else {
			Toast.makeText(this, "Can't write to internal storage",
					Toast.LENGTH_LONG).show();
		}

	}

	// class asyntask download file offlie
	private class DownloadFile extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;
		private Context c;
		private String link;
		private String nameHTML = "";// ten cua file se luu vd abc.html

		public DownloadFile(Context c, String link) {
			this.c = c;
			this.link = link;
			Log.d("name", link);

			// tim index cua chu / cuoi cung de cat ra name
			int indexLast = link.lastIndexOf("/");

			nameHTML = link.substring(indexLast + 1, link.length());
			Log.d("name", nameHTML);
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(c);
			dialog.setMessage("Đang tải...");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				URL url = new URL(link);
				URLConnection connection = url.openConnection();
				connection.connect();

				File directory = new File(Environment
						.getExternalStorageDirectory().getPath()
						+ "/GiaiTriTongHopBao/");
				File file_path = new File(Environment
						.getExternalStorageDirectory().getPath()
						+ "/GiaiTriTongHopBao/" + nameHTML);

				if (!directory.exists())
					directory.mkdirs();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				// OutputStream output = new FileOutputStream(Environment
				// .getExternalStorageDirectory().getPath()
				//
				// + "/data/" + getPackageName() + "/webpage.html");
				//
				// +"/"+mainLink);

				OutputStream output;
				output = new FileOutputStream(file_path);

				byte data[] = new byte[1024];
				int count;
				try {
					while ((count = input.read(data)) != -1)
						output.write(data, 0, count);
				} catch (IOException e) {
					e.printStackTrace();
				}

				output.flush();
				output.close();
				input.close();

				// Toast.makeText(getApplicationContext(), "File downloaded",
				// Toast.LENGTH_SHORT).show();

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				Toast.makeText(c, "Đã tải thành công!", Toast.LENGTH_SHORT)
						.show();
			}
			super.onPostExecute(result);
		}
	}

}
