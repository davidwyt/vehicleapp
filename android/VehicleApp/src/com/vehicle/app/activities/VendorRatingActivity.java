package com.vehicle.app.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.vehicle.app.bean.Vendor;
import com.vehicle.app.layout.ToggleButtonGroupTableLayout;
import com.vehicle.app.mgrs.SelfMgr;
import com.vehicle.app.msg.bean.CommentMessage;
import com.vehicle.app.msg.worker.IMessageCourier;
import com.vehicle.app.msg.worker.VendorCommentMessageCourier;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.ImageUtil;

import cn.edu.sjtu.vehicleapp.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VendorRatingActivity extends TemplateActivity implements OnClickListener {

	private Vendor mVendor;

	private ToggleButtonGroupTableLayout mRadioGroupProject;
	private RatingBar mRatingPrice;
	private RatingBar mRatingTech;
	private RatingBar mRatingEfficiency;
	private RatingBar mRatingRecption;
	private RatingBar mRatingEnvironment;

	private TextView mVendorName;

	private EditText mComment;
	private Button mBtnOK;
	private Button mBtnUpload;
	private Button mBtnBak;
	private View mImgContainer;

	private PopupWindow selectImgPopup;

	private BroadcastReceiver mReceiver;

	public static final String KEY_VENDORRATING_VENDOR = "com.vehicle.app.vendorrating.vendor";

	private final static int[] CHATPLUS_ICONS = { R.drawable.icon_chatplus_camera, R.drawable.icon_chatplus_gallery, };

	private static String[] CHATPLUS_FUNS = new String[2];

	private static final int REQUESTCODE_CAPTURE_IMAGE = 0x00000010;
	private static final int REQUESTCODE_BROWSE_ALBUM = 0x00000011;

	private int mImgIndex = 1;

	private ImageView mImg1;
	private ImageView mImg2;
	private ImageView mImg3;
	private ImageView mImg4;

	private boolean isUploadBtnHidden = false;

	private static final String KEY_UPLOADBTN_HIDDEN = "com.vehicle.app.vendorrating.uploadhidden";
	public static final String KEY_COMMENT_ERRMSG = "com.vehicle.app.vendorrating.errormsg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vendorrating);

		initView();

		if (null != savedInstanceState) {
			this.isUploadBtnHidden = savedInstanceState.getBoolean(KEY_UPLOADBTN_HIDDEN);
			hideUploadBtn(isUploadBtnHidden);
		} else {
			hideUploadBtn(false);
		}
	}

	private void initView() {

		CHATPLUS_FUNS[0] = this.getResources().getString(R.string.camera_zh);
		CHATPLUS_FUNS[1] = this.getResources().getString(R.string.gallery_zh);

		this.mVendorName = (TextView) this.findViewById(R.id.vendorrating_name);

		this.mRadioGroupProject = (ToggleButtonGroupTableLayout) this
				.findViewById(R.id.vendorrating_project_radiogroup);

		this.mRatingPrice = (RatingBar) this.findViewById(R.id.ratingbar_price);
		this.mRatingTech = (RatingBar) this.findViewById(R.id.ratingbar_technology);
		this.mRatingRecption = (RatingBar) this.findViewById(R.id.ratingbar_reception);
		this.mRatingEfficiency = (RatingBar) this.findViewById(R.id.ratingbar_efficiency);
		this.mRatingEnvironment = (RatingBar) this.findViewById(R.id.ratingbar_environment);

		mImgContainer = this.findViewById(R.id.vendorrating_imgs);

		this.mComment = (EditText) this.findViewById(R.id.vendorrating_et_comment);

		this.mBtnBak = (Button) this.findViewById(R.id.vendorrating_goback);
		this.mBtnBak.setOnClickListener(this);

		this.mBtnOK = (Button) this.findViewById(R.id.vendorrating_submit);

		this.mBtnOK.setOnClickListener(this);

		mBtnUpload = (Button) this.findViewById(R.id.vendorrating_uploadimg);
		mBtnUpload.setOnClickListener(this);

		createSelectImgPopup();

		mImg1 = (ImageView) this.findViewById(R.id.vendorrating_img1);
		this.mImg1.setOnClickListener(this);

		mImg2 = (ImageView) this.findViewById(R.id.vendorrating_img2);
		this.mImg2.setOnClickListener(this);

		mImg3 = (ImageView) this.findViewById(R.id.vendorrating_img3);
		this.mImg3.setOnClickListener(this);

		mImg4 = (ImageView) this.findViewById(R.id.vendorrating_img4);
		this.mImg4.setOnClickListener(this);
	}

	private void hideUploadBtn(boolean isHide) {

		if (isHide) {
			mImgContainer.setVisibility(View.VISIBLE);
			mBtnUpload.setVisibility(View.INVISIBLE);
		} else {
			mImgContainer.setVisibility(View.INVISIBLE);
			mBtnUpload.setVisibility(View.VISIBLE);
		}
	}

	@SuppressWarnings("deprecation")
	private void createSelectImgPopup() {

		View contentView = getLayoutInflater().inflate(R.layout.popwindow_selectimg, null);

		selectImgPopup = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		selectImgPopup.setFocusable(true);
		selectImgPopup.setBackgroundDrawable(new BitmapDrawable());
		selectImgPopup.setAnimationStyle(R.style.chatpluspopupwin_animation);

		GridView gridView = (GridView) contentView.findViewById(R.id.selectimg_gridView);

		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < CHATPLUS_ICONS.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("icon", CHATPLUS_ICONS[i]);
			item.put("fun", CHATPLUS_FUNS[i]);
			data.add(item);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.chatplusitem,
				new String[] { "icon", "fun" }, new int[] { R.id.chatplus_imageView, R.id.chatplus_textView });

		gridView.setAdapter(simpleAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (selectImgPopup.isShowing()) {
					selectImgPopup.dismiss();
				}

				switch (position) {
				case 0:
					captureImage();
					break;
				case 1:
					browseImage();
					break;
				}

			}
		});
	}

	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		this.startActivityForResult(intent, REQUESTCODE_CAPTURE_IMAGE);
	}

	private void browseImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");

		startActivityForResult(intent, REQUESTCODE_BROWSE_ALBUM);
	}

	private void selectPopup(int index) {
		mImgIndex = index;
		selectImgPopup.showAtLocation(this.mBtnOK, Gravity.BOTTOM, 0, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUESTCODE_CAPTURE_IMAGE:
			onCaptureImage(resultCode, data);
			break;
		case REQUESTCODE_BROWSE_ALBUM:
			onBrowseAlbum(resultCode, data);
			break;

		default:
			System.out.println("invalid requestcode :" + requestCode + " in chat activity");
		}
	}

	private void onCaptureImage(int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode) {
			return;
		}

		if (Activity.RESULT_OK == resultCode) {
			String sdState = Environment.getExternalStorageState();
			if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
				System.out.println("sd card unmount");
				return;
			}
		}

		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
				+ Environment.DIRECTORY_DCIM + File.separator + "Camera";
		String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".png";

		Bundle bundle = data.getExtras();
		if (null == bundle)
			return;

		Bitmap bitmap = (Bitmap) bundle.get("data");
		FileOutputStream fout = null;

		String filePath = path + File.separator + name;

		System.out.println("photoooooooooooo:" + filePath);

		try {
			fout = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
			setImageViewBitmap(filePath);

		} catch (FileNotFoundException e) {
			e.printStackTrace();

			// do something
		} finally {
			try {
				if (null != fout) {
					fout.flush();
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void onBrowseAlbum(int resultCode, Intent data) {

		if (Activity.RESULT_OK == resultCode) {

			Uri originalUri = data.getData();
			ContentResolver resolver = getContentResolver();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = resolver.query(originalUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);

			File file = new File(path);
			if (file.isFile() && file.exists()) {
				setImageViewBitmap(path);
				System.out.println("selected file:" + path);
			} else {
				System.out.println("file not exist:" + path);
			}
		}
	}

	private void setImageViewBitmap(String path) {
		try {
			Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(path, 128, 128);

			switch (this.mImgIndex) {
			case 1:
				this.mImg1.setImageBitmap(bitmap);
				this.mImg1.setTag(path);
				break;
			case 2:
				this.mImg2.setImageBitmap(bitmap);
				this.mImg2.setTag(path);
				break;
			case 3:
				this.mImg3.setImageBitmap(bitmap);
				this.mImg3.setTag(path);
				break;
			case 4:
				this.mImg4.setImageBitmap(bitmap);
				this.mImg4.setTag(path);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> collectImgPaths() {
		List<String> paths = new ArrayList<String>();

		collectImgPath(this.mImg1, paths);
		collectImgPath(this.mImg2, paths);
		collectImgPath(this.mImg3, paths);
		collectImgPath(this.mImg4, paths);

		return paths;
	}

	private void collectImgPath(ImageView imgView, List<String> paths) {
		if (null != imgView.getTag()) {
			String name = (String) imgView.getTag();
			paths.add(name);
		}
	}

	private void submitCommit() {

		if (null == this.mVendor) {
			return;
		}

		String comment = this.mComment.getText().toString();
		comment = comment.trim();
		/**
		 * if ((!StringUtil.isChinese(comment) && comment.length() < 10) ||
		 * (StringUtil.isChinese(comment) && StringUtil.getWordCount(comment) <
		 * 10)) { Toast.makeText(getApplicationContext(),
		 * getResources().getString(R.string.tip_vendorcommenttooshort),
		 * Toast.LENGTH_LONG).show(); return; }
		 */

		int checkId = mRadioGroupProject.getCheckedRadioButtonId();
		if (-1 == checkId) {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_selectproject),
					Toast.LENGTH_LONG).show();
			return;
		}

		int mainProjectId = 0;
		switch (checkId) {
		case R.id.rd_vehiclerepair:
			mainProjectId = 0;
			break;
		case R.id.rd_vehiclebeauty:
			mainProjectId = 1;
			break;
		case R.id.rd_vehiclemaintain:
			mainProjectId = 2;
			break;
		case R.id.rd_vehiclewash:
			mainProjectId = 3;
			break;
		}

		double price = this.mRatingPrice.getRating();
		double tech = this.mRatingTech.getRating();
		double env = this.mRatingEnvironment.getRating();
		double eff = this.mRatingEfficiency.getRating();
		double rec = this.mRatingRecption.getRating();

		CommentMessage msg = new CommentMessage();
		msg.setSource(SelfMgr.getInstance().getId());
		msg.setTarget(this.mVendor.getId());
		msg.setMainProjectId(mainProjectId);
		msg.setComment(comment);
		msg.setEfficiencyScore(eff);
		msg.setPriceScore(price);
		msg.setEnvironmentScore(env);
		msg.setReceptionScore(rec);
		msg.setTechnologyScore(tech);
		msg.setImgPaths(collectImgPaths());

		IMessageCourier courier = new VendorCommentMessageCourier(this.getApplicationContext());
		courier.dispatch(msg);
	}

	private void initData() {
		Bundle bundle = this.getIntent().getExtras();

		if (null != bundle) {

			this.mVendor = (Vendor) bundle.getSerializable(KEY_VENDORRATING_VENDOR);

			if (null != this.mVendor) {
				this.mVendorName.setText(mVendor.getName());
			}
		}
	}

	private void registerMessageReceiver() {

		mReceiver = new CommentResultReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Constants.ACTION_VENDORCOMMENT_SUCCESS);
		filter.addAction(Constants.ACTION_VENDORCOMMENT_FAILED);

		try {
			registerReceiver(mReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unregisterMessageReceiver() {
		try {
			unregisterReceiver(this.mReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		initData();
		registerMessageReceiver();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterMessageReceiver();
	}

	private void back() {
		this.finish();
		finish();
	}

	class CommentResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();

			if (Constants.ACTION_VENDORCOMMENT_SUCCESS.equals(action)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_commentsuccess),
						Toast.LENGTH_LONG).show();
			} else if (Constants.ACTION_VENDORCOMMENT_FAILED.equals(action)) {
				String msg = intent.getStringExtra(KEY_COMMENT_ERRMSG);
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.vendorrating_goback:
			back();
			break;
		case R.id.vendorrating_submit:
			submitCommit();
			break;
		case R.id.vendorrating_uploadimg:
			isUploadBtnHidden = true;
			hideUploadBtn(true);
			break;
		case R.id.vendorrating_img1:
			selectPopup(1);
			break;
		case R.id.vendorrating_img2:
			selectPopup(2);
			break;
		case R.id.vendorrating_img3:
			selectPopup(3);
			break;
		case R.id.vendorrating_img4:
			selectPopup(4);
			break;
		}
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean(KEY_UPLOADBTN_HIDDEN, this.isUploadBtnHidden);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
