package com.TPLdev.bookpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class DownloadFilePDF extends Activity {

	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	// context
	private Context c;
	// link tai, ten file se tai
	private String fileUrl, fileName;

	// kiem tra ket toi
	ConnectivityManager conMgr;// = (ConnectivityManager)
								// getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo activeNetwork;// = conMgr.getActiveNetworkInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

		// lay gia tri ra tu intent
		fileUrl = getIntent().getStringExtra("link");
		fileName = getIntent().getStringExtra("name");

		// goi asynctask
		new TaskDownloadFilePDF(fileUrl, fileName, this).execute();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog
					.setMessage("Đang chờ tín hiệu từ Server...\nChờ tí tui tải nhanh lắm, nóng nảy làm gì cho nổi mụn :)");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// khong cho huy
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	// asynctask
	class TaskDownloadFilePDF extends AsyncTask<Void, Void, Void> {

		private Context c;
		private String fileUrl, fileName;
		ProgressDialog progressDialog;
		// tmp la con so % tai file duoc bao nhieu roi
		int tmp = 0;
		// tai ve thanh cong hay ko, true la thanh cong
		// false la that bai
		private boolean downloadSuccess = false;

		@SuppressWarnings("deprecation")
		public TaskDownloadFilePDF(String fileUrl, String fileName, Context c) {
			this.fileUrl = fileUrl;
			this.fileName = fileName;
			this.c = c;
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			// thong bao
			if (downloadSuccess)
				Toast.makeText(c, "Đã tải thành công ^^!", Toast.LENGTH_SHORT)
						.show();
			else {
				Toast.makeText(c, "Tải thất bại :(!", Toast.LENGTH_SHORT)
						.show();

				Log.d("fuck", fileName);
				// xoa luon file tai ve ko thanh cong
				File file = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/GiaiTriTongHopBook/" + fileName);
				boolean deleted = file.delete();
			}
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			DownloadFilePDF.this.finish();
			// set tmp ve 0
			tmp = 0;

			super.onPostExecute(result);
		}

		@Override
		protected Void doInBackground(Void... params) {
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;
			HttpURLConnection urlConnection = null;
			
			String extStorageDirectory = Environment
					.getExternalStorageDirectory().toString();
			File folder = new File(extStorageDirectory, "GiaiTriTongHopBook");
			folder.mkdir();

			File pdfFile = new File(folder, fileName);

			try {
				pdfFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// FileDownloader.downloadFile(fileUrl, pdfFile);
			final int MEGABYTE = 1024 * 1024;

			try {

				URL url = new URL(fileUrl);
				urlConnection = (HttpURLConnection) url.openConnection();

				// time out 15s
				urlConnection.setConnectTimeout(15000);
				urlConnection.setReadTimeout(15000);

				// urlConnection.setRequestMethod("GET");
				// urlConnection.setDoOutput(true);

				urlConnection.connect();

				inputStream = urlConnection.getInputStream();
				fileOutputStream = new FileOutputStream(pdfFile);
				// tong dung luong cua file can download
				int totalSize = urlConnection.getContentLength();

				if (totalSize == -1)
					totalSize = 4000000;

				int count = 0;// file dang down dc bao nhieu
				Log.d("totalSize", "tong dung luong " + totalSize);
				byte[] buffer = new byte[MEGABYTE];
				int bufferLength = 0;
				while ((bufferLength = inputStream.read(buffer)) > 0) {

					conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					activeNetwork = conMgr.getActiveNetworkInfo();

					if (activeNetwork != null && activeNetwork.isConnected()) {
						// notify user you are online
						Log.d("fuck", "on");

						count += bufferLength;

						Log.d("value", "count - " + count);

						tmp = (int) ((count * 100) / totalSize);

						Log.d("tmp", tmp + "% ===" + count + "/" + totalSize);

						publishProgress();
						fileOutputStream.write(buffer, 0, bufferLength);
						downloadSuccess = true;
					} else {
						// notify user you are not online
						Log.d("fuck", "off");
						downloadSuccess = false;
					}

				}
				fileOutputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
				Log.d("timeout", "time out");
			}
			//time out
			catch (SocketTimeoutException e) {
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				// Log.d("timeout", "time out");
				Toast.makeText(c, "TIME OUT!", Toast.LENGTH_SHORT).show();
			}

			catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
			} finally {
				try {
					if (fileOutputStream != null)
						fileOutputStream.close();
					if (inputStream != null)
						inputStream.close();
				} catch (IOException ignored) {
				}

				if (urlConnection != null)
					urlConnection.disconnect();
			}
			return null;

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			mProgressDialog.setProgress(tmp);
			Log.d("up", tmp + "");
			// Toast.makeText(c, tmp+"", Toast.LENGTH_SHORT).show();
			super.onProgressUpdate(values);
		}
		// protected void onProgressUpdate(String... progress) {
		// mProgressDialog.setProgress(tmp);
		// }
	}

}
