package com.TPLdev.news;

//show ra dialog la hinh dong va 2 nut yeu cau mo wifi hoac dong thong bao
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.TPLdev.giaitritonghop.R;

public class Activity_Dialog_Noconnect extends Activity implements
		OnClickListener {
	//wv show anh dong
	WebView wv;
	Button btOpenWifi, btClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// full man hinh
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu_fragment_news_dialog_noconnect);
		//anh xa
		wv = (WebView) findViewById(R.id.wv);
		btOpenWifi = (Button) findViewById(R.id.btOpenWifi);
		btClose = (Button) findViewById(R.id.btClose);

		btOpenWifi.setOnClickListener(this);
		btClose.setOnClickListener(this);

		wv.setBackgroundColor(Color.TRANSPARENT);
		wv.loadUrl("file:///android_asset/no_connection.gif");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btOpenWifi:
			startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
			break;
		case R.id.btClose:
			this.finish();
			break;
		default:
			break;
		}
	}
}
