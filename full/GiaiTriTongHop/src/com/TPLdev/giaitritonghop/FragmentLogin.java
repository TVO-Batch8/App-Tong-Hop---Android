package com.TPLdev.giaitritonghop;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

public class FragmentLogin extends Fragment {

	private View parentView;

	// facebook
	private final String PENDING_ACTION_BUNDLE_KEY = "com.TPLdev.giaitritonghop:PendingAction";
	// dung load avatar neu login thanh cong
	private ProfilePictureView avatar;
	// textview chao nguoi dung neu login facebook thanh cong
	private TextView tvGreeting;
	private PendingAction pendingAction = PendingAction.NONE;
	private CallbackManager callbackManager;
	private ProfileTracker profileTracker;

	private enum PendingAction {
		NONE, FB_SHARE_PHOTO, FB_SHARE_POST
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// facebook sdk
		FacebookSdk.sdkInitialize(getActivity());
		
		callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						Log.d("a", "success");
						handlePendingAction();
						updateUI();
					}

					@Override
					public void onCancel() {
						Log.d("a", "cancel");
						if (pendingAction != PendingAction.NONE) {
							showAlert();
							pendingAction = PendingAction.NONE;
						}
						updateUI();
					}

					@Override
					public void onError(FacebookException exception) {
						Log.d("a", "error");
						if (pendingAction != PendingAction.NONE
								&& exception instanceof FacebookAuthorizationException) {
							showAlert();
							pendingAction = PendingAction.NONE;
						}
						updateUI();
					}

					private void showAlert() {
						new AlertDialog.Builder(getActivity())
								.setTitle(R.string.cancelled)
								.setMessage(R.string.permission_not_granted)
								.setPositiveButton(R.string.ok, null).show();

						// AlertDialog myDialog;
						// myDialog = builder.create();
						// myDialog.getWindow().setType(
						// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
						// myDialog.show();
					}
				});

		// shareDialog = new ShareDialog(this);
		// shareDialog.registerCallback(callbackManager, shareCallback);

		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		/*
		 * //////////////////////////////////
		 */
		parentView = inflater.inflate(R.layout.menu_fragment_login, container,
				false);
		// iv_game_ailatrieuphu = (ImageView) parentView
		// .findViewById(R.id.iv_game_ailatrieuphu);
		// iv_game_doanmausac = (ImageView) parentView
		// .findViewById(R.id.iv_game_doanmausac);
		//
		// iv_game_ailatrieuphu.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// Intent i = new Intent(getActivity(), ActivityHelloGame.class);
		// startActivity(i);
		// }
		// });
		// iv_game_doanmausac.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// ===============================
		// facebook
		// Xu ly cho facebook
		LoginButton loginButton = (LoginButton) parentView.findViewById(R.id.login_button);
	    loginButton.setReadPermissions("user_friends");
	    // If using in a fragment
	    loginButton.setFragment(this);  
		profileTracker = new ProfileTracker() {
			@Override
			protected void onCurrentProfileChanged(Profile oldProfile,
					Profile currentProfile) {
				updateUI();
				handlePendingAction();
			}
		};
		// anh xa avatar va text chao mung
		avatar = (ProfilePictureView) parentView.findViewById(R.id.profilePicture);
		tvGreeting = (TextView) parentView.findViewById(R.id.greeting);

		return parentView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		AppEventsLogger.activateApp(getActivity());
		updateUI();
		super.onResume();
	}

	// /////////
	// xu ly cho facebook
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	public void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		profileTracker.stopTracking();
	}

	private void updateUI() {
		boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

		/*
		 * btFBSharePost.setEnabled(enableButtons || canPresentShareDialog);
		 * btFBSharePhoto.setEnabled(enableButtons ||
		 * canPresentShareDialogWithPhotos);
		 */

		Profile profile = Profile.getCurrentProfile();
		if (enableButtons && profile != null) {
			// Toast.makeText(getActivity(), "a", Toast.LENGTH_SHORT).show();
			avatar.setProfileId(profile.getId());
			tvGreeting.setText(getString(R.string.hello_user,
					profile.getFirstName()));
		} else {
			avatar.setProfileId(null);
			tvGreeting.setText(null);
		}
	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case NONE:
			break;
		case FB_SHARE_PHOTO:
			// facebookSharePhoto();
			break;
		case FB_SHARE_POST:
			// facebookSharePost();
			break;
		}
	}
}
