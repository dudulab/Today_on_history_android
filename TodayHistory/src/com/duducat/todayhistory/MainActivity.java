package com.duducat.todayhistory;

import com.duducat.activeconfig.ActiveConfig;
import com.duducat.activeconfig.ActiveConfig.AsyncGetImageHandler;
import com.duducat.activeconfig.ActiveConfig.AsyncGetTextHandler;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static String join(String[] strings, int startIndex, String separator) {
		StringBuffer sb = new StringBuffer();
		for (int i = startIndex; i < strings.length; i++) {
			if (i != startIndex)
				sb.append(separator);
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActiveConfig.register(getApplicationContext(), "5DCt8gHc", "Q44mxkCi7rGZ2n1r");

		ActiveConfig.setTextViewWithKey("date", "Try again later", (TextView) findViewById(R.id.date));
		ActiveConfig.setTextViewWithKey("lunar", "", (TextView) findViewById(R.id.lunar));
		ActiveConfig.getImageAsync("cover", new AsyncGetImageHandler() {

			@Override
			public void OnSuccess(Drawable p) {
				((ImageView) findViewById(R.id.cover)).setImageDrawable(p);
				findViewById(R.id.loading).setVisibility(View.GONE);
			}

			@Override
			public void OnFailed() {
				Log.e("TodayHistory", "Failed to get image: cover");
			}
		});

		ActiveConfig.getTextAsync("history", new AsyncGetTextHandler() {

			@Override
			public void OnSuccess(String value) {
				TextView headline = (TextView) findViewById(R.id.headline);
				TextView rest = (TextView) findViewById(R.id.rest);

				String[] lines = value.split("\n");
				headline.setText(lines[0]);
				rest.setText(join(lines, 1, "\n"));
				headline.setVisibility(View.VISIBLE);
			}

			@Override
			public void OnFailed() {
				Log.e("TodayHistory", "Failed to get text: history");
			}
		});

		((TextView) findViewById(R.id.lunar)).setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				ActiveConfig.clearCache();

				return true;
			}
		});
	}
}
