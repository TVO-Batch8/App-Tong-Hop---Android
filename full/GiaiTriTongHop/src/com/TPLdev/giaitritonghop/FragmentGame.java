package com.TPLdev.giaitritonghop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.TPLdev.game_chinhphucthuthach.ActivityHelloGame;

public class FragmentGame extends Fragment {

	private View parentView;
	// private ResideMenu resideMenu;
	private ImageView iv_game_chinhPhucThuThach, iv_game_freakingMath;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.menu_fragment_game, container,
				false);
		iv_game_chinhPhucThuThach = (ImageView) parentView
				.findViewById(R.id.iv_game_ailatrieuphu);
		iv_game_freakingMath = (ImageView) parentView
				.findViewById(R.id.iv_game_doanmausac);
		//
		iv_game_chinhPhucThuThach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), ActivityHelloGame.class);
				startActivity(i);
			}
		});
		iv_game_freakingMath.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Tạm thời chưa có...",
						Toast.LENGTH_SHORT).show();
			}
		});
		return parentView;
	}

}
