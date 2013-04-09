package com.spirozh.note;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class Note extends Activity {

	MediaPlayer _bell;
	int _seconds = 1200;
	
	void setSeconds(int s) {
		if (_seconds != s)
		{
	        TextView textTime = (TextView) findViewById(R.id.textTime);
			textTime.setText(timeString(s));
		}

		_seconds = s;
	}
	int getSeconds() {
		return _seconds;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        
        _bell = MediaPlayer.create(this, R.raw.bell);
        _bell.start();
        
        TextView textTime = (TextView) findViewById(R.id.textTime);
        textTime.setText(timeString(_seconds));
        
        // Gesture detection
        final GestureDetector gestureDetector = new GestureDetector(this, new TimeFlinger());
        OnTouchListener gestureListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        
        View parent = (View) textTime.getParent();
        while (parent.getParent() instanceof View) {
        	parent = (View) parent.getParent();
        }
        parent.setOnTouchListener(gestureListener);
    }
    
    private String timeString(int s) {
    	int seconds = s % 60;
    	int minutes = ((s - seconds) / 60) % 60;
    	int hours = (s - seconds - minutes * 60) / 3600;
    	
    	String fmt = "%3$02d:%2$02d:%1$02d";
    	
    	return String.format(fmt, seconds, minutes, hours);
    }
    
    
    private class TimeFlinger extends SimpleOnGestureListener
    {
    	float _density;
    	
    	TimeFlinger() {
    		DisplayMetrics dm = getResources().getDisplayMetrics();
    		_density = dm.density;
    	}
    	
    	@Override
    	public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {

    		float f = (vY * _density) / 50;
			setSeconds((int) (getSeconds() - f));
    		
    		return true;
    	}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float dX, float dY) {

			float f = (dY * _density) / 50;
			setSeconds((int) (getSeconds() + f));
			
			return true;			
		}
    	
    	
    }
    
    
}
