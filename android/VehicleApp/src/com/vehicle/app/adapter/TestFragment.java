package com.vehicle.app.adapter;

import cn.edu.sjtu.vehicleapp.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public final class TestFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";

	public static TestFragment newInstance(String content, int index) {
		TestFragment fragment = new TestFragment();

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 20; i++) {
			builder.append(content).append(" ");
		}
		builder.deleteCharAt(builder.length() - 1);
		fragment.mContent = builder.toString();
		fragment.index = index;

		return fragment;
	}

	private String mContent = "???";
	private int index = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (index % 2 == 0) {
			TextView text = new TextView(getActivity());
			text.setGravity(Gravity.CENTER);
			text.setText(mContent);
			text.setTextSize(20 * getResources().getDisplayMetrics().density);
			text.setPadding(20, 20, 20, 20);

			LinearLayout layout = new LinearLayout(getActivity());
			layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			layout.setGravity(Gravity.CENTER);
			layout.addView(text);

			return layout;
		} else {
			View parent = inflater.inflate(R.layout.layout_vendorcoupon, container, false);
			TableLayout table = (TableLayout) parent.findViewById(R.id.table_coupon);

			for (int i = 0; i < 10; i++) {

				TableRow row = new TableRow(getActivity());

				TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1, 1.5f);
				row.setLayoutParams(lp);

				TableRow.LayoutParams nameLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						1.0f);
				TextView name = new TextView(getActivity());
				name.setLayoutParams(nameLayout);
				name.setText("sssssssssssssssssssssssssssssssssssssssssssssss");
				row.addView(name);

				TextView date = new TextView(getActivity());

				TableRow.LayoutParams dateLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						0.5f);
				name.setLayoutParams(dateLayout);
				date.setText("tttttttttttt");
				row.addView(date);

				ImageView img = new ImageView(getActivity());
				TableRow.LayoutParams imgLayout = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,
						0.5f);
				name.setLayoutParams(imgLayout);

				img.setImageResource(R.drawable.icon_btn_goback);

				row.addView(img);
				row.setPadding(1, 1, 1, 1);

				table.addView(row, 2 * i);

				TableRow.LayoutParams dividerLayout = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
				View divider = new View(getActivity());
				divider.setLayoutParams(dividerLayout);
				divider.setBackgroundColor(getResources().getColor(R.color.azure));

				table.addView(divider, 2 * i + 1);
			}

			return parent;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}
}
