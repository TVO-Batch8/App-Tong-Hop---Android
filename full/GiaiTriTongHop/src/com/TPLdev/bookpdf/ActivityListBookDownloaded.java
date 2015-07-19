package com.TPLdev.bookpdf;

//danh sach cac sach da tai ve
//sach offline
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.TPLdev.giaitritonghop.R;

public class ActivityListBookDownloaded extends Activity {
	// mang sach pdf
	String[] listBookPDF;
	// mang file GiaiTriTongHopBook
	File[] listFile;
	ListView mListView;
	// search book offline
	EditText etSearch;
	// MyAdapter adapter;
	ArrayAdapter<String> adapter;
	// setting
	public static final String PREFERENCES_FILE_NAME = "MyAppPreferences_Book";
	// de set font cho webview, dc cai dat trong radioGroupFont
	int indexFont;
	// de set background dua vao index
	// set font
	Typeface mTypeface;
	int indexBackground;
	// xmlmain de set background
	RelativeLayout xmlMain;
	// xoa het tat ca sach da tai ve
	ImageView ivRemoveAll;
	//luu tru trang thai listview
	Parcelable state;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu_fragment_book_pdf_list_book_downloaded);
		// anh xa
		xmlMain = (RelativeLayout) findViewById(R.id.xmlMain);
		mListView = (ListView) findViewById(R.id.gv);
		ivRemoveAll = (ImageView) findViewById(R.id.ivRemoveAll);
		etSearch = (EditText) findViewById(R.id.etSearch);

		// adapter = new MyAdapter(this, android.R.layout.simple_list_item_1);

		setDataForListOfBook();
		// su kien doc sach pdf khi nhan vao 1 sach da tai ve
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				
				String path = listFile[(int) id].getAbsolutePath();
				// Toast.makeText(getApplicationContext(), path,
				// Toast.LENGTH_SHORT).show();

				// co 2 cach doc pdf, chon 1 trong 2 thoi

				// c1: goi doc file pdf cua minh tu che =))~
				// openPdfIntent(path);

				// c2: goi intent cho nhanh

				// ListView Clicked item index
				int itemPosition = position;
				// ListView Clicked item value
				String itemValue = (String) mListView
						.getItemAtPosition(itemPosition) + ".pdf";

				File file = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/GiaiTriTongHopBook/" + itemValue);

				// Toast.makeText(getApplicationContext(),
				// listBookPDF[position],
				// Toast.LENGTH_SHORT).show();
				if (file.exists()) {
					Uri uri = Uri.fromFile(file);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri, "application/pdf");
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					try {
						startActivity(intent);
						// openPdfIntent(path);
					} catch (ActivityNotFoundException e) {
						// goi doc file pdf cua minh tu che =))~
						openPdfIntent(path);

						// ========================

						/*
						 * // neu ko co thi keu nguoi dung tai ve phan mem doc
						 * pdf Toast.makeText(ActivityListBookDownloaded.this,
						 * "Vui lòng tải phần mềm đọc PDF",
						 * Toast.LENGTH_SHORT).show(); try { startActivity(new
						 * Intent( Intent.ACTION_VIEW,
						 * Uri.parse("market://details?id=adobe%20reader&c=apps"
						 * ))); } catch
						 * (android.content.ActivityNotFoundException anfe) {
						 * startActivity(new Intent( Intent.ACTION_VIEW,
						 * Uri.parse(
						 * "https://play.google.com/store/search?q=adobe%20reader&c=apps"
						 * ))); }
						 */
					}
				} else {
					Toast.makeText(ActivityListBookDownloaded.this, "No file",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// nhan giu lau de xoa sach
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				//luu trang thai mListView
				state = mListView.onSaveInstanceState();
				
				// Toast.makeText(getApplicationContext(), position + "",
				// Toast.LENGTH_SHORT).show();

				// ListView Clicked item index
				int itemPosition = position;
				// ListView Clicked item value
				final String itemValue = (String) mListView
						.getItemAtPosition(itemPosition) + ".pdf";

				new AlertDialog.Builder(ActivityListBookDownloaded.this)
						.setTitle("Chú ý...")
						.setMessage(
								"Bạn có chắc chắn muốn xóa "
										+ itemValue.replace(".pdf", "")
										+ " hay không?")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										File file = new File(Environment
												.getExternalStorageDirectory()
												.getPath()
												+ "/GiaiTriTongHopBook/"
												+ itemValue);
										boolean deleted = file.delete();
										Toast.makeText(getApplicationContext(),
												"Đã xóa!", Toast.LENGTH_SHORT)
												.show();
										// goi lai set data
										setDataForListOfBook();
										
										//phuc hoi trang thai
										mListView.onRestoreInstanceState(state);
									}
								})
						.setNegativeButton(android.R.string.no,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// do nothing
									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();

				return true;
			}
		});
		ivRemoveAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				adapter.getFilter().filter(s);
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("Chú ý...")
				.setMessage(
						"Bạn muốn xóa tất cả những sách đã tải về đúng không?")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(ActivityListBookDownloaded.this,
										"Đã xóa hết!", Toast.LENGTH_SHORT)
										.show();

								File file = new File(Environment
										.getExternalStorageDirectory()
										.getPath()
										+ "/GiaiTriTongHopBook/");

								DeleteRecursive(file);
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

	// ham dung cho removeAllData()
	public void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory()) {
			for (File child : fileOrDirectory.listFiles()) {
				DeleteRecursive(child);
			}
		}
		fileOrDirectory.delete();
		// goi lai data
		setDataForListOfBook();
	}

	private void setDataForListOfBook() {
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/GiaiTriTongHopBook/");

			listFile = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return ((name.endsWith(".pdf")));
				}
			});

			if (listFile.length <= 0) {
				Toast.makeText(getApplicationContext(),
						"Chưa có sách nào được tải", Toast.LENGTH_SHORT).show();
				listBookPDF = null;
				mListView.setAdapter(null);
				return;
			}
			listBookPDF = new String[listFile.length];
			for (int i = 0; i < listFile.length; i++) {
				// lay ten nhet vao listBookPDF nhung ko luu phan mo rong .pdf
				String tmp = listFile[i].getName();
				tmp = tmp.replace(".pdf", "");
				Log.d("te", tmp);
				listBookPDF[i] = tmp;
			}
			Arrays.sort(listBookPDF);
			adapter = new ArrayAdapter<String>(
					this,
					R.layout.menu_fragment_book_pdf_listview_item_one_news_object_downloaded,
					R.id.title, listBookPDF);
			adapter.notifyDataSetChanged();
			mListView.setAdapter(adapter);
		} catch (Exception e) {

		}

	}

	// mo sach
	private void openPdfIntent(String path) {
		try {
			final Intent intent = new Intent(getApplicationContext(),
					ActivityReadBookPdf.class);
			intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, path);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
			mTypeface = Typeface.createFromAsset(getAssets(), "SEGOEUI.TTF");
			break;
		case 2:
			mTypeface = Typeface.createFromAsset(getAssets(), "tinhyeu.ttf");
			break;
		case 3:
			mTypeface = Typeface.createFromAsset(getAssets(), "thuphap.ttf");
			break;
		default:
			mTypeface = Typeface.createFromAsset(getAssets(), "SEGOEUI.TTF");
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
			mListView.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 1:
			xmlMain.setBackgroundColor(Color.parseColor("#ff99ff"));
			mListView.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 2:
			xmlMain.setBackgroundColor(Color.parseColor("#99ff66"));
			mListView.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 3:
			xmlMain.setBackgroundColor(Color.parseColor("#ccffff"));
			mListView.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 4:
			xmlMain.setBackgroundResource(R.drawable.bg_main);
			mListView.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 5:
			xmlMain.setBackgroundResource(R.drawable.bg_news1);
			mListView.setBackgroundColor(Color.TRANSPARENT);
			break;
		case 6:
			mListView.setBackgroundColor(Color.TRANSPARENT);
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
