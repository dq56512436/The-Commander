package com.venky.controller;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.example.controller.R;
import com.venky.media.OptionsActivity;

public class MainActivity extends Activity implements
		android.view.GestureDetector.OnGestureListener,
		android.view.GestureDetector.OnDoubleTapListener {

	public static DataOutputStream DO;
	public static String xres;
	public static String yres;
	private static final String DEBUG_TAG = "Gestures";

	private Display display;

	private int moveCount = 0;
	private int scrollCount = 0;
	private int dragFlagCount = 0;
	private int dragCount = 0;

	private int me1x = 0, me1y = 0;
	private int me2x = 0, me2y = 0;

	private int se1x = 0, se1y = 0;
	private int se2x = 0, se2y = 0;

	private int deventx = 0, deventy = 0;

	private boolean dragFlag = false;

	private GestureDetectorCompat mDetector;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		display = getWindowManager().getDefaultDisplay();

		xres = display.getWidth() + "";
		yres = display.getHeight() + "";
		
		mDetector = new GestureDetectorCompat(this, this);
		mDetector.setOnDoubleTapListener(this);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			xres = display.getWidth() + "";
			yres = display.getHeight() + "";

			Log.d("Orientation : ", "Orienation Changed to Landscape");

			if (DO != null) {
				try {
					DO.write(("orientation " + xres + " " + yres).getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			xres = display.getWidth() + "";
			yres = display.getHeight() + "";

			Log.d("Orientation : ", "Orienation Changed to Portrait");

			if (DO != null) {
				try {
					DO.write(("orientation " + xres + " " + yres).getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_options:
			Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
			float velocityX, float velocityY) {
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		if (DO != null && event.getPointerCount() == 1) {
			try {
				if (!dragFlag)
					DO.write("RightClick".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d(DEBUG_TAG, "RightClick: " + event.toString() + "PointerCount = "
				+ event.getPointerCount());
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		// For moving the mouse pointer
		if (DO != null && e2.getAction() == MotionEvent.ACTION_MOVE
				&& e2.getPointerCount() == 1) {
			String posXY = "";
			if (me1x != e1.getX() && me1y != e1.getY()) {
				me1x = (int) e1.getX();
				me1y = (int) e1.getY();

				me2x = (int) e2.getX();
				me2y = (int) e2.getY();
			} else {
				int tempx = (int) e2.getX();
				int tempy = (int) e2.getY();

				posXY += " " + (tempx - me2x);
				posXY += " " + (tempy - me2y);

				me2x = tempx;
				me2y = tempy;
			}

			moveCount++;

			try {
				if (moveCount == 3) {
					if (dragFlag)
						DO.write(("Drag" + posXY).getBytes());
					else
						DO.write(("Move" + posXY).getBytes());

					moveCount = 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(DEBUG_TAG,
					"onScroll: Move e1-> x = " + e1.getX() + " y = "
							+ e1.getY() + "\ne2-> x = " + e2.getX() + " y = "
							+ e2.getY() + "PointerCount = "
							+ e2.getPointerCount());

		}

		// For scrolling...
		if (DO != null && e2.getAction() == MotionEvent.ACTION_MOVE
				&& e2.getPointerCount() == 2) {
			String posXY = "";
			if (se1x != e1.getX() && se1y != e1.getY()) {
				se1x = (int) e1.getX();
				se1y = (int) e1.getY();

				se2x = (int) e2.getX();
				se2y = (int) e2.getY();

				int xdisp = se1x - se2x;
				int ydisp = se1y - se2y;

				try {
					if (Math.abs(xdisp) < Math.abs(ydisp))
						DO.write(("vertical" + posXY).getBytes());
					else
						DO.write(("horizontal" + posXY).getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

				int tempx = (int) e2.getX();
				int tempy = (int) e2.getY();
				posXY += " " + (tempx - se2x);
				posXY += " " + (tempy - se2y);
				se2x = tempx;
				se2y = tempy;
			}
			scrollCount++;
			try {
				if (scrollCount == 3) {
					DO.write(("Scrolled" + posXY).getBytes());
					scrollCount = 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(DEBUG_TAG,
					"onScroll: Scroll e1-> x = " + e1.getX() + " y = "
							+ e1.getY() + "\ne2-> x = " + e2.getX() + " y = "
							+ e2.getY() + "PointerCount = "
							+ e2.getPointerCount());

		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {

		Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString()
				+ "PointerCount : " + event.getPointerCount());
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		dragFlag = true;
		dragFlagCount = 0;

		Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString() + "PointerCount: "
				+ event.getPointerCount());
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event) {

		dragFlagCount++;
		String posXY = "";

		int tempx = (int) event.getX();
		int tempy = (int) event.getY();

		posXY += " " + (tempx - deventx);
		posXY += " " + (tempy - deventy);

		deventx = tempx;
		deventy = tempy;

		if (dragFlagCount >= 10) {
			Log.d(DEBUG_TAG, "drag Detected");

			dragCount++;

			try {
				if (dragCount == 3) {
					if (DO != null)
						DO.write(("Drag" + posXY).getBytes());
					dragCount = 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		if (event.getAction() == MotionEvent.ACTION_UP && dragFlagCount < 10) {
			if (DO != null) {
				try {
					DO.write("onDoubleTap".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.d(DEBUG_TAG,
				"onDoubleTapEvent: " + "Action Move x =" + event.getX()
						+ "y = " + event.getY() + "PointerCount : "
						+ event.getPointerCount());
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		dragFlag = false;
		if (DO != null && event.getPointerCount() == 1) {
			try {
				DO.write("onSingleTapConfirmed".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString()
					+ "PointerCount = " + event.getPointerCount());
		}
		return true;
	}

}


