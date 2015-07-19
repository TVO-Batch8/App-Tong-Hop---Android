package com.TPLdev.game_chinhphucthuthach;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuestionManagerDatabase extends SQLiteOpenHelper {
	// duong dan toi database
	private static String DB_PATH = "/data/data/com.TPLdev.giaitritonghop/databases/";
	// version cua database hien tai la 1
	private static final int DATABASE_VERSION = 1;
	// ten cua database
	private static String DB_NAME = "tpl.sqlite";
	// ten cua bang cung voi cac cot
	private static final String TABLE_NAME = "table_question";
	private static final String KEY_ID = "id";
	private static final String KEY_LEVEL = "level";
	private static final String KEY_QUESTION = "question";
	private static final String KEY_CASEA = "casea";
	private static final String KEY_CASEB = "caseb";
	private static final String KEY_CASEC = "casec";
	private static final String KEY_CASED = "cased";
	// quan ly database
	private SQLiteDatabase myDataBase;
	// context
	private final Context myContext;

	// ham tao truyen vao 1 cai context
	public QuestionManagerDatabase(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		myContext = context;
	}

	// oncreate khong co gi vi database da dc tao va de trong asset roi
	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	// neu muon update thi version moi phai cao hon 1, ghi code update trong day
	// o day ko co nhu cau update nen bo qua
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// mo database len
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	// dong database lai
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	// kiem tra database co mo duoc khong, neu mo dc thi tra ve true, ko thi
	// false
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (Exception e) {
		}
		if (checkDB != null)
			checkDB.close();
		return checkDB != null ? true : false;
	}

	// copy du lieu tu database len bo nho
	private void copyDatabase() throws IOException {
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	// kiem tra database ton tai thi thoi, chua co thi goi copy
	public void createDatabase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {

		} else {
			this.getReadableDatabase();
			try {
				copyDatabase();
			} catch (IOException e) {
				throw new Error("Error");
			}
		}
	}

	// lay tat ca cac cau hoi co trong database, tra ve cursor
	public Cursor getAllQuestionData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
		return cursor;
	}

	// public ArrayList<OneQuestionObject> LayNCauNgauNhienCauHoiLevel1(int
	// socau) {
	//
	// String limit = "0," + socau;
	// ArrayList<OneQuestionObject> ds_cauhoi = new
	// ArrayList<OneQuestionObject>();
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor contro = db.query(TABLE_NAME, null, KEY_LEVEL + "=?",
	// new String[] { "1" }, null, null, "random()", limit);
	//
	// contro.moveToFirst();
	// do {
	// OneQuestionObject x = new OneQuestionObject();
	// x.id = Integer.parseInt(contro.getString(0));
	// x.level = Integer.parseInt(contro.getString(2));
	// x.question = contro.getString(1);
	// x.answer_a = contro.getString(3);
	// x.answer_b = contro.getString(4);
	// x.answer_c = contro.getString(5);
	// x.answer_d = contro.getString(6);
	// ds_cauhoi.add(x);
	// } while (contro.moveToNext());
	// return ds_cauhoi;
	// }
	//
	// public ArrayList<OneQuestionObject> LayNCauNgauNhienCauHoiLevel2(int
	// socau) {
	//
	// String limit = "0," + socau;
	// ArrayList<OneQuestionObject> ds_cauhoi = new
	// ArrayList<OneQuestionObject>();
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor contro = db.query(TABLE_NAME, null, KEY_LEVEL + "=?",
	// new String[] { "2" }, null, null, "random()", limit);
	//
	// contro.moveToFirst();
	// do {
	// OneQuestionObject x = new OneQuestionObject();
	// x.id = Integer.parseInt(contro.getString(0));
	// x.level = Integer.parseInt(contro.getString(2));
	// x.question = contro.getString(1);
	// x.answer_a = contro.getString(3);
	// x.answer_b = contro.getString(4);
	// x.answer_c = contro.getString(5);
	// x.answer_d = contro.getString(6);
	// ds_cauhoi.add(x);
	// } while (contro.moveToNext());
	// return ds_cauhoi;
	// }
	//
	// public ArrayList<OneQuestionObject> LayNCauNgauNhienCauHoiLevel3(int
	// socau) {
	//
	// String limit = "0," + socau;
	// ArrayList<OneQuestionObject> ds_cauhoi = new
	// ArrayList<OneQuestionObject>();
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor contro = db.query(TABLE_NAME, null, KEY_LEVEL + "=?",
	// new String[] { "1" }, null, null, "random()", limit);
	//
	// contro.moveToFirst();
	// do {
	// OneQuestionObject x = new OneQuestionObject();
	// x.id = Integer.parseInt(contro.getString(0));
	// x.level = Integer.parseInt(contro.getString(2));
	// x.question = contro.getString(1);
	// x.answer_a = contro.getString(3);
	// x.answer_b = contro.getString(4);
	// x.answer_c = contro.getString(5);
	// x.answer_d = contro.getString(6);
	// ds_cauhoi.add(x);
	// } while (contro.moveToNext());
	// return ds_cauhoi;
	// }

	// lay n ngau nhien cau hoi, tra ve 1 cai arraylist, n la tham so dc truyen
	// vao
	public ArrayList<OneQuestionObject> getNQuestionRandom(int number) {

		String limit = "0," + number;
		ArrayList<OneQuestionObject> al_question = new ArrayList<OneQuestionObject>();
		SQLiteDatabase db = this.getReadableDatabase();
		// lay random bao nhieu cai bang truy van random, tra ve doi tuong con
		// tro
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null,
				"random()", limit);
		// con tro dau tien
		cursor.moveToFirst();
		do {
			// tao doi tuong cau hoi roi nhet vao arraylist
			OneQuestionObject x = new OneQuestionObject();
			x.id = Integer.parseInt(cursor.getString(0));
			x.level = Integer.parseInt(cursor.getString(2));
			x.question = cursor.getString(1);
			x.answer_a = cursor.getString(3);
			x.answer_b = cursor.getString(4);
			x.answer_c = cursor.getString(5);
			x.answer_d = cursor.getString(6);
			al_question.add(x);
		} while (cursor.moveToNext());
		return al_question;
	}
}
