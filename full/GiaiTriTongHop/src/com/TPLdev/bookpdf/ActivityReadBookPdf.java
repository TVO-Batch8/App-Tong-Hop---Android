package com.TPLdev.bookpdf;

//doc pdf do minh tao ra =))~ 
// hoi bi chuoi =))~
import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.TPLdev.giaitritonghop.R;

public class ActivityReadBookPdf extends PdfViewerActivity {

	public void onCreate(Bundle savedInstanceState) {

		// cua so khong co thanh title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// full man hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);

	}

	public int getPreviousPageImageResource() {
		return R.drawable.left_arrow;
	}

	public int getNextPageImageResource() {
		return R.drawable.right_arrow;
	}

	public int getZoomInImageResource() {
		return R.drawable.zoom_in;
	}

	public int getZoomOutImageResource() {
		return R.drawable.zoom_out;
	}

	public int getPdfPasswordLayoutResource() {
		return R.layout.pdf_file_password;
	}

	public int getPdfPageNumberResource() {
		return R.layout.dialog_pagenumber;
	}

	public int getPdfPasswordEditField() {
		return R.id.etPassword;
	}

	public int getPdfPasswordOkButton() {
		return R.id.btOK;
	}

	public int getPdfPasswordExitButton() {
		return R.id.btExit;
	}

	public int getPdfPageNumberEditField() {
		return R.id.pagenum_edit;
	}
}