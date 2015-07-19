package com.TPLdev.book;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.TPLdev.giaitritonghop.R;

public class FragmentBook_MyAdapter extends BaseAdapter {
	Context mContext;
	ArrayList<FragmentBook_OneItemInLvMenu> alItem;

	// Typeface tf;
	// String vitri = "0";// vi tri de set font
	// String vitricolor = "0";// vi tri se set mau text
	// int color;

	public FragmentBook_MyAdapter(Context c,
			ArrayList<FragmentBook_OneItemInLvMenu> ds) {
		this.mContext = c;
		this.alItem = ds;
		// SetFont();
		// SetColor();
	}

	@Override
	public int getCount() {
		return alItem.size();
	}

	@Override
	public Object getItem(int position) {
		return alItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		// anh dai dien cua trang bao sliding drawer
		public ImageView mPicture;
		// chuyen muc
		public TextView mTit;
		// ten chuyen muc
		public TextView mMenuTit;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder oneView;
		LayoutInflater inf = ((Activity) mContext).getLayoutInflater();
		if (convertView == null) {
			oneView = new ViewHolder();
			convertView = inf.inflate(
					R.layout.menu_fragment_news_listview_item, null);
			oneView.mPicture = (ImageView) convertView
					.findViewById(R.id.mPicture);
			oneView.mTit = (TextView) convertView.findViewById(R.id.mTit);
			oneView.mMenuTit = (TextView) convertView
					.findViewById(R.id.mMenuTit);
			convertView.setTag(oneView);
		} else
			oneView = (ViewHolder) convertView.getTag();
		oneView.mPicture.setImageResource(alItem.get(position).mPicture);
		oneView.mTit.setText(alItem.get(position).mMenuTit);
		oneView.mMenuTit.setText(alItem.get(position).mTit);

		Animation hh = AnimationUtils.loadAnimation(mContext, R.anim.fl_dung);
		convertView.setAnimation(hh);
		return convertView;
	}
}
