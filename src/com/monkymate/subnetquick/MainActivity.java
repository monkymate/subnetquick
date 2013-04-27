package com.monkymate.subnetquick;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity implements OnValueChangeListener,
		OnSeekBarChangeListener, OnClickListener {
	int a, mask = 8;
	protected TextView n, c, b, h, m, s;
	protected GridView gv, gv2, gv3, gv4;
	protected NumberPicker p1, p2, p3, p4;
	protected SeekBar maskBar;
	protected Button b1, b2, b3, b4, b5;
	protected String[] pole = new String[96], ukaz0 = new String[24],
			ukaz1 = new String[24], ukaz2 = new String[24],
			ukaz3 = new String[24];;
	protected ImageAdapter adapter, adapter2, adapter3, adapter4;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		n = (TextView) findViewById(R.id.n);
		c = (TextView) findViewById(R.id.c);
		b = (TextView) findViewById(R.id.b);
		h = (TextView) findViewById(R.id.h);
		m = (TextView) findViewById(R.id.m);
		s = (TextView) findViewById(R.id.s);
		gv = (GridView) findViewById(R.id.gv);
		gv2 = (GridView) findViewById(R.id.gv2);
		gv3 = (GridView) findViewById(R.id.gv3);
		gv4 = (GridView) findViewById(R.id.gv4);
		p1 = (NumberPicker) findViewById(R.id.p1);
		p1.setMaxValue(255);
		p1.setMinValue(0);
		p2 = (NumberPicker) findViewById(R.id.p2);
		p2.setMaxValue(255);
		p2.setMinValue(0);
		p3 = (NumberPicker) findViewById(R.id.p3);
		p3.setMaxValue(255);
		p3.setMinValue(0);
		p4 = (NumberPicker) findViewById(R.id.p4);
		p4.setMaxValue(255);
		p4.setMinValue(0);
		maskBar = (SeekBar) findViewById(R.id.maskBar);
		b1 = (Button) findViewById(R.id.b1);
		b2 = (Button) findViewById(R.id.b2);
		b3 = (Button) findViewById(R.id.b3);
		b4 = (Button) findViewById(R.id.b4);
		b5 = (Button) findViewById(R.id.b5);
		maskBar.setOnSeekBarChangeListener(this);
		p1.setOnValueChangedListener(this);
		p2.setOnValueChangedListener(this);
		p3.setOnValueChangedListener(this);
		p4.setOnValueChangedListener(this);
		b1.setOnClickListener(this);
		b2.setOnClickListener(this);
		b3.setOnClickListener(this);
		b4.setOnClickListener(this);
		b5.setOnClickListener(this);
		adapter = new ImageAdapter(this, android.R.layout.simple_list_item_1,
				ukaz0);
		adapter2 = new ImageAdapter(this, android.R.layout.simple_list_item_1,
				ukaz1);
		adapter3 = new ImageAdapter(this, android.R.layout.simple_list_item_1,
				ukaz2);
		adapter4 = new ImageAdapter(this, android.R.layout.simple_list_item_1,
				ukaz3);
		onClick(b5);
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		if (picker == p1) {
			int o = newVal;
			o >>>= 4;
			if ((o & 8) != 0) {
				if ((o & 6) != 6)
					o = (o & 4) == 0 ? 1 : 2;
				else
					o = (o & 1) == 0 ? 3 : 4;
			} else
				o = 0;
			c.setText(getString(R.string.c)
					+ Character.toString((char) ('A' + o)));
			if (o < 3) {
				b1.setEnabled(true);
				b2.setEnabled(true);
				n.setVisibility(View.VISIBLE);
				s.setVisibility(View.VISIBLE);
				b.setVisibility(View.VISIBLE);
				h.setVisibility(View.VISIBLE);
				m.setVisibility(View.VISIBLE);

			} else {
				b1.setEnabled(false);
				b2.setEnabled(false);
				n.setVisibility(View.INVISIBLE);
				s.setVisibility(View.INVISIBLE);
				b.setVisibility(View.INVISIBLE);
				h.setVisibility(View.INVISIBLE);
				m.setVisibility(View.INVISIBLE);
			}
			a = o < 3 ? 8 + 8 * o : 32;
			maskBar.setMax(a == 32 ? 0 : 30 - a);
			maskBar.setProgress(mask > a ? mask - a : 0);
		}
		netbro();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		s.setText(getString(R.string.s)
				+ String.valueOf((int) Math.pow(2, progress)));
		h.setText(getString(R.string.h)
				+ String.valueOf((int) Math.pow(2, 32 - (a + progress))
						- (a != 32 ? 2 : 0)));
		m.setText(getString(R.string.m)
				+ String.valueOf(output(adresa(a + progress))));
		netbro();
	}

	@Override
	public void onClick(View v) {
		int ko = p1.getValue();
		long lo;
		if (v == b1 | v == b2) {
			long p = (v == b2 ? 1 : -1)
					* (long) Math.pow(2, 32 - (a + maskBar.getProgress()));
			lo = p
					+ adresa(p1.getValue(), p2.getValue(), p3.getValue(),
							p4.getValue());
			lo += (lo < adresa(1, 0, 0, 0) ? adresa(223, 0, 0, 0) : 0);
			lo -= (lo > adresa(223, 255, 255, 255) ? adresa(223, 0, 0, 0) : 0);
		} else {
			if (v == b3)
				lo = ko == 10 ? adresa(10, 0, 0, 0) : adresa(10, p2.getValue(),
						p3.getValue(), p4.getValue());
			else if (v == b4) {
				int z2 = p2.getValue();
				lo = adresa(172, ko == 172 & z2++ >>> 4 == 1 ? z2 > 31 ? 16
						: z2 : 16, p3.getValue(), p4.getValue());
			} else {
				int z2 = p2.getValue();
				int z3 = p3.getValue();
				lo = adresa(192, 168, ko == 192 & z2 == 168 ? z3++ == 255 ? 0
						: z3 : 0, p4.getValue());
			}
		}
		p4.setValue((int) lo & 255);
		lo >>>= 8;
		p3.setValue((int) lo & 255);
		lo >>>= 8;
		p2.setValue((int) lo & 255);
		lo >>>= 8;
		int kn = (int) lo;
		p1.setValue(kn);
		onValueChange(p1, ko, kn);
	}

	void netbro() {
		int p = a + maskBar.getProgress();
		long lo = adresa(p1.getValue(), p2.getValue(), p3.getValue(),
				p4.getValue())
				& adresa(p);
		n.setText(getString(R.string.n) + output(lo) + "/" + String.valueOf(p));
		b.setText(getString(R.string.b)
				+ output(lo + (int) Math.pow(2, 32 - p) - 1));
		zobraz();
	}

	void zobraz() {
		long[] cislo = new long[3];
		cislo[0] = adresa(p1.getValue(), p2.getValue(), p3.getValue(),
				p4.getValue());
		if (cislo[0] < adresa(1, 0, 0, 0)) {
			Intent intent = new Intent(this, TrapActivity.class);
			startActivity(intent);
			return;
		}
		int pr = a + maskBar.getProgress();
		cislo[1] = adresa(pr);
		cislo[2] = cislo[0] & cislo[1];
		for (int poc1 = 0; poc1 < 3; poc1++) {
			for (int poc = 0; poc < 32; poc++) {
				if (((cislo[poc1] >>> (31 - poc)) & 1) == 0)
					pole[32 * poc1 + poc] = (poc < a) ? "6" : (poc1 != 0)
							&& (poc < pr) ? "6" : "7";
				else
					pole[32 * poc1 + poc] = (poc1 == 0 ? (poc < a) ? "4" : "5"
							: poc1 == 1 ? (poc < a) ? "3" : "2" : "1");
			}
		}
		for (pr = 0; pr < 32; pr++) {
			switch ((pr % 32) / 8) {
			case 0:
				ukaz0[(pr / 32) * 8 + (pr % 8)] = pole[pr];
				break;
			case 1:
				ukaz1[(pr / 32) * 8 + (pr % 8)] = pole[pr];
				break;
			case 2:
				ukaz2[(pr / 32) * 8 + (pr % 8)] = pole[pr];
				break;
			case 3:
				ukaz3[(pr / 32) * 8 + (pr % 8)] = pole[pr];
			}
		}
		if (a > 24)
			for (pr = 32; pr < 96; pr++) {
				switch ((pr % 32) / 8) {
				case 0:
					ukaz0[(pr / 32) * 8 + (pr % 8)] = "";
					break;
				case 1:
					ukaz1[(pr / 32) * 8 + (pr % 8)] = "";
					break;
				case 2:
					ukaz2[(pr / 32) * 8 + (pr % 8)] = "";
					break;
				case 3:
					ukaz3[(pr / 32) * 8 + (pr % 8)] = "";
				}
			}
		else
			for (pr = 32; pr < 96; pr++) {
				switch ((pr % 32) / 8) {
				case 0:
					ukaz0[(pr / 32) * 8 + (pr % 8)] = pole[pr];
					break;
				case 1:
					ukaz1[(pr / 32) * 8 + (pr % 8)] = pole[pr];
					break;
				case 2:
					ukaz2[(pr / 32) * 8 + (pr % 8)] = pole[pr];
					break;
				case 3:
					ukaz3[(pr / 32) * 8 + (pr % 8)] = pole[pr];
				}
			}
		gv.setAdapter(adapter);
		gv2.setAdapter(adapter2);
		gv3.setAdapter(adapter3);
		gv4.setAdapter(adapter4);
	}

	long adresa(int a1, int a2, int a3, int a4) {
		long y = a1;
		y <<= 8;
		y |= a2;
		y <<= 8;
		y |= a3;
		y <<= 8;
		y |= a4;
		return y;
	}

	long adresa(int ad) {
		long mask32 = 0;
		for (int i = 0; i < ad; i++) {
			mask32++;
			mask32 <<= 1;
		}
		return (mask32 << (31 - ad));
	}

	String output(long ip) {
		long x = ip >>> 24;
		String out = String.valueOf(x);
		for (int i = 0; i < 3; i++) {
			x = ip >>> (8 * (2 - i));
			out += getString(R.string.d) + String.valueOf(x & 255);
		}
		return out;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mask = a + maskBar.getProgress();
	}

	protected void onPause() {
		super.onPause();
		onClick(b3);
	}
}
