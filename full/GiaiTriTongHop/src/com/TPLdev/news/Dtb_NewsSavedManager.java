package com.TPLdev.news;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class Dtb_NewsSavedManager extends SQLiteOpenHelper {
	Context context;

	public Dtb_NewsSavedManager(Context context) {
		super(context, "quanlybao", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table qlb" + "("
				+ "_id integer primary key autoincrement," + "title text,"
				+ "link text" + ")";

		db.execSQL(sql);

	}

	// update neu csdl co version cao hon 1
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists qlb");
		onCreate(db);
	}

	// tao 1 tin
	public void createNews(Dtb_OneNewsObject c) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", c.title);
		values.put("link", c.link);
		db.insert("qlb", null, values);
	}

	// lay tat ca data, tra ve 1 cai arraylist
	public ArrayList<Dtb_OneNewsObject> getAllNewsDaTa() {
		ArrayList<Dtb_OneNewsObject> alNews = new ArrayList<Dtb_OneNewsObject>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("select * from qlb", null);
		c.moveToFirst();
		do {
			int id = Integer.parseInt(c.getString(0));
			String nd = c.getString(1);
			String tg = c.getString(2);
			Dtb_OneNewsObject cv = new Dtb_OneNewsObject(id, nd, tg);
			alNews.add(cv);

		} while (c.moveToNext());
		return alNews;
	}

	// xoa tat ca du lieu dc luu
	public void removeAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("qlb", null, null);

		// dong thoi xoa het cac file html da tai ve
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/GiaiTriTongHopBao/");

		DeleteRecursive(file);
	}

	// ham dung cho removeAllData()
	public static void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory()) {
			for (File child : fileOrDirectory.listFiles()) {
				DeleteRecursive(child);
			}
		}
		fileOrDirectory.delete();
	}

	// xoa 1 tin dua vao id
	public void removeNews(Dtb_OneNewsObject c) {
		// log ra co dang http://.../.../abc.html
		// cai minh can la abc.html
		Log.d("tit", c.link);
		int indexLast = c.link.lastIndexOf("/");
		String link = c.link.substring(indexLast + 1, c.link.length());
		// log ra lan nua xem co dung dang abc.html hay k
		Log.d("tit", link);
		// xoa file html trong GiaiTriTongHopBao
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/GiaiTriTongHopBao/" + link);
		boolean deleted = file.delete();

		// xoa trong sqlite
		int id = c.id;
		SQLiteDatabase db = this.getWritableDatabase();

		db.delete("qlb", "_id=?", new String[] { id + "" });

	}

	// sua tin
	public void editNews(Dtb_OneNewsObject c) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", c.title);
		values.put("link", c.link);
		db.update("qlb", values, "_id=?", new String[] { c.id + "" });

	}

	// lay so luong tin
	public int getDataCount() {
		String countQuery = "SELECT  * FROM qlb";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}
}
