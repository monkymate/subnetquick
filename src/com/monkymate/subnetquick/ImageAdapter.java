package com.monkymate.subnetquick;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter<String> extends ArrayAdapter<String> {

	private Context c;
	private String[] s;

	public ImageAdapter(Context context, int resource,
			String[] textViewResourceId) {
		super(context, resource, textViewResourceId);
		s = textViewResourceId;
		c = context;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(c);
			imageView.setLayoutParams(new GridView.LayoutParams(21, 24));
		} else {
			imageView = (ImageView) convertView;
		}
		if (s[position] == "0")
			imageView.setImageResource(R.drawable.i1);
		else if (s[position] == "1")
			imageView.setImageResource(R.drawable.i2);
		else if (s[position] == "2")
			imageView.setImageResource(R.drawable.m1);
		else if (s[position] == "3")
			imageView.setImageResource(R.drawable.m2);
		else if (s[position] == "4")
			imageView.setImageResource(R.drawable.v1);
		else if (s[position] == "5")
			imageView.setImageResource(R.drawable.v2);
		else if (s[position] == "6")
			imageView.setImageResource(R.drawable.n1);
		else if (s[position] == "7")
			imageView.setImageResource(R.drawable.n2);

		return imageView;
	}
}
