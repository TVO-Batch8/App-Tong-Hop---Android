package com.TPLdev.giaitritonghop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.TPLdev.book.FragmentBook;
import com.TPLdev.bookpdf.FragmentBookPDF;
import com.TPLdev.news.FragmentNews;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class ActivityMenu extends FragmentActivity implements
		View.OnClickListener {

	private ResideMenu resideMenu;
	// private ActivityMenu mContext;
	private ResideMenuItem itemGame;
	// doc sach apppro.timgem
	private ResideMenuItem itemBook;
	// doc bao
	private ResideMenuItem itemNews;
	// private ResideMenuItem itemSettings;
	// sach pdf
	private ResideMenuItem itemBookPDF;
	private ResideMenuItem itemMore;
	private ResideMenuItem itemLogin;
	public static String PACKAGE_NAME;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//
		setContentView(R.layout.menu_activity_main);
		PACKAGE_NAME = getApplicationContext().getPackageName();

		// mContext = this;
		setUpMenu();
		if (savedInstanceState == null)
			changeFragment(new FragmentGame());

		// Lay key hash FB

		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
		// "com.TPLdev.giaitritonghop", PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// Log.d("KeyHash:",
		// Base64.encodeToString(md.digest(), Base64.DEFAULT));
		// }
		// } catch (NameNotFoundException e) {
		//
		// } catch (NoSuchAlgorithmException e) {
		//
		// }
	}

	private void setUpMenu() {

		// attach to current activity;
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.menu_background);
		resideMenu.attachToActivity(this);
		resideMenu.setMenuListener(menuListener);
		// valid scale factor is between 0.0f and 1.0f. leftmenu'width is
		// 150dip.
		resideMenu.setScaleValue(0.6f);

		// create menu items;
		itemGame = new ResideMenuItem(this, R.drawable.icon_game,
				getResources().getString(R.string.strGame));
		itemBook = new ResideMenuItem(this, R.drawable.icon_book,
				getResources().getString(R.string.strBook));
		itemNews = new ResideMenuItem(this, R.drawable.icon_news,
				getResources().getString(R.string.strNews));
		// itemSettings = new ResideMenuItem(this, R.drawable.icon_setting,
		// getResources().getString(R.string.strSetting));
		itemLogin = new ResideMenuItem(this, R.drawable.icon_share,
				getResources().getString(R.string.strShare));
		itemBookPDF = new ResideMenuItem(this, R.drawable.icon_bookpdf,
				getResources().getString(R.string.strBookPDF));
		itemMore = new ResideMenuItem(this, R.drawable.icon_more,
				getResources().getString(R.string.strMore));

		itemGame.setOnClickListener(this);
		itemBook.setOnClickListener(this);
		itemNews.setOnClickListener(this);
		// itemSettings.setOnClickListener(this);
		itemBookPDF.setOnClickListener(this);
		itemMore.setOnClickListener(this);
		itemLogin.setOnClickListener(this);

		resideMenu.addMenuItem(itemBookPDF, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemBook, ResideMenu.DIRECTION_LEFT);
		resideMenu.addMenuItem(itemNews, ResideMenu.DIRECTION_LEFT);
		// resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);

		resideMenu.addMenuItem(itemGame, ResideMenu.DIRECTION_RIGHT);
		resideMenu.addMenuItem(itemLogin, ResideMenu.DIRECTION_RIGHT);
		resideMenu.addMenuItem(itemMore, ResideMenu.DIRECTION_RIGHT);

		// You can disable a direction by setting ->
		// resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

		findViewById(R.id.title_bar_left_menu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
					}
				});
		findViewById(R.id.title_bar_right_menu).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
					}
				});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return resideMenu.dispatchTouchEvent(ev);
	}

	@Override
	public void onClick(View view) {

		if (view == itemGame) {
			changeFragment(new FragmentGame());
		} else if (view == itemBook) {
			changeFragment(new FragmentBook());
		} else if (view == itemNews) {
			changeFragment(new FragmentNews());
			// } else if (view == itemSettings) {
			// changeFragment(new FragmentSetting());
		} else if (view == itemBookPDF) {
			// try {
			// startActivity(new Intent(Intent.ACTION_VIEW,
			// Uri.parse("market://details?id=" + PACKAGE_NAME)));
			// } catch (android.content.ActivityNotFoundException anfe) {
			// startActivity(new Intent(
			// Intent.ACTION_VIEW,
			// Uri.parse("http://play.google.com/store/apps/details?id="
			// + PACKAGE_NAME)));
			// }

			changeFragment(new FragmentBookPDF());

		} else if (view == itemMore) {
			Intent i = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/developer?id=TPLdev"));
			startActivity(i);

		} else if (view == itemLogin) {
			// callIntentShare();
			changeFragment(new FragmentLogin());
		} else {

		}

		resideMenu.closeMenu();
	}

	private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
		@Override
		public void openMenu() {
			// Toast.makeText(mContext, "Menu is opened!",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void closeMenu() {
			// Toast.makeText(mContext, "Menu is closed!",
			// Toast.LENGTH_SHORT).show();
		}
	};

	private void changeFragment(Fragment targetFragment) {
		resideMenu.clearIgnoredViewList();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	// What good method is to access resideMenuï¼Ÿ
	public ResideMenu getResideMenu() {
		return resideMenu;
	}

	// //
	// void callIntentShare() {
	// Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	// sharingIntent.setType("text/plain");
	// String shareBody = getResources().getString(R.string.app_name) + "\n"
	// + getResources().getString(R.string.strShareContent)
	// + "\n\n\nhttp://play.google.com/store/apps/details?id="
	// + PACKAGE_NAME;
	// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
	// getResources().getString(R.string.app_name));
	// sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
	// startActivity(Intent.createChooser(sharingIntent, "Chia sẻ qua: "));
	//
	// }
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle("Chú ý...")
				.setMessage("Bạn muốn thoát ứng dụng đúng không?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								ActivityMenu.this.finish();
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

	
}
