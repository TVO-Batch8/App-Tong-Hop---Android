package com.TPLdev.news;

import java.io.File;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;

public class Activity_ReadNews_Offline extends Activity {
	// link lay ra qua intent dang http://..../abc.html
	String mLink;
	// ten cua font->mac dinh, mum mim, tinh yeu, thu phap
	String nameOfFont;
	//baseURI dung de parse jsoup offline
	String baseURI;
	// setting
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
	// de set background dua vao index
	int indexBackground;
	// de set font cho webview, dc cai dat trong radioGroupFont
	int indexFont;
	// xmlMain
	RelativeLayout xmlMain;

	private WebView mWebView;
	private ProgressBar loadingProgressBar;
	private TextView txtLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// full man hinh
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu_fragment_news_read_news_offline);

		// anh xa
		xmlMain = (RelativeLayout) findViewById(R.id.xmltrum);
		mWebView = (WebView) findViewById(R.id.webView1);

		// thong bao
		Toast.makeText(
				getApplicationContext(),
				"Vì đọc Offline nên không có load ảnh được... Bạn có thể mở Wifi để có thể hiện ảnh lên cho đẹp.",
				Toast.LENGTH_LONG).show();

		// link tu intent
		mLink = getIntent().getStringExtra("link");
		Log.d("l", mLink);

		int indexLast = mLink.lastIndexOf("/");

		baseURI = mLink.substring(0, indexLast) + "/";
		Log.d("base", baseURI);

		mLink = mLink.substring(indexLast + 1, mLink.length());
		Log.d("l", mLink);

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

		// mWebView.loadUrl("file://"
		// + Environment.getExternalStorageDirectory().getPath()
		// + "/GiaiTriTongHopBao/" //+ getPackageName() + "/"
		// + mLink);

		// lay noi dung tu mLink la link dc truyen qua intent
		loadContentFromLink(mLink);

		// load background da luu
		loadSettingBackground();

		// load setting font da luu
		loadSettingFont();

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
				Toast.makeText(getApplicationContext(),
						"Trang này được lưu Offline", Toast.LENGTH_SHORT)
						.show();
			} else if (url.contains("facebook.com/sharer/sharer.php")) {
				Toast.makeText(getApplicationContext(),
						"Trang này được lưu Offline", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Trang này được lưu Offline", Toast.LENGTH_SHORT)
						.show();
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

		loadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		loadingProgressBar.setVisibility(View.VISIBLE);
		txtLoading = (TextView) findViewById(R.id.textView1);

		try {
			new Thread(new Runnable() {
				public void run() {
					final String newPage;
					try {

						// Document doc = Jsoup.connect(url).get();
						File input = new File(Environment
								.getExternalStorageDirectory().getPath()
								+ "/GiaiTriTongHopBao/" + mLink);
						Document doc = Jsoup.parse(input, "UTF-8", baseURI);

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
									String pas = "</body></html>";
									String myHtmlString = pish
											+ htmlSerializer
													.getAsString(tagNode) + pas;
									mWebView.loadDataWithBaseURL(null,
											myHtmlString, "text/html",
											"charset=UTF-8", null);

									//
									loadingProgressBar.setVisibility(View.GONE);
									txtLoading.setVisibility(View.GONE);
								} catch (Exception e) {

								}
							}
						});
					} catch (Exception e) {
						Log.d("l", "khong doc dc ne");
					}

				}
			}).start();
		} catch (Exception e) {
		}
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
}
