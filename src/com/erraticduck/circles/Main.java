/**
 * Copyright 2011 Vincent Kwok

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.erraticduck.circles;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	
	private Random gen = new Random();
	private static int x,y,r,width,height;
	private static MediaPlayer wrongAnswerSound;
	private static MediaPlayer rightAnswerSound;
	private static boolean clickDisabled;
	private static boolean cheatingMode = false;
	private static boolean cheatingModeWasOn = false;
	static int level = 1;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
		.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        
        createCircleParams();
        clickDisabled = false;
        cheatingMode = false;
        cheatingModeWasOn = false;
        level = 1;
        
        final Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				finish();
			}
		});
        
        FrameLayout mainView = (FrameLayout)findViewById(R.id.main_view);
        final TextView messageText = (TextView)findViewById(R.id.messagetext);
        final View overlay = findViewById(R.id.level);
        wrongAnswerSound = MediaPlayer.create(this, R.raw.buzzer);
        rightAnswerSound = MediaPlayer.create(this, R.raw.ding);
        mainView.addView(new Circle(this,x,y,r,Color.LTGRAY));
        mainView.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View arg0, MotionEvent arg1) {
				arg0.buildDrawingCache();
				Bitmap cache = arg0.getDrawingCache();
				int color = cache.getPixel((int)arg1.getX(), (int)arg1.getY());
				if (!clickDisabled && color == Color.LTGRAY) {
					final FrameLayout v = (FrameLayout)arg0;
					int clickedX = (int) arg1.getX();
					int clickedY = (int) arg1.getY();
					if (Math.sqrt( ((clickedX-x)*(clickedX-x)) + ((clickedY-y)*(clickedY-y)) ) <= r) {
						Log.d("HAHAHA","CLICKED!!");
						clickDisabled = true;
						arg0.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
						rightAnswerSound.start();
						level++;
						
						Animation pushDownInAnim = AnimationUtils.loadAnimation(
								Main.this, R.anim.push_down_in);
						if (!cheatingMode) {
							overlay.startAnimation(pushDownInAnim);
							overlay.setVisibility(View.VISIBLE);
						}
						resetButton.setVisibility(View.GONE);
						messageText.setText("Level " + level);
						
						rightAnswerSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							
							public void onCompletion(MediaPlayer mp) {
								Animation pushDownOutAnim = AnimationUtils.loadAnimation(
										Main.this, R.anim.push_down_out);
								if (!cheatingMode) {
									overlay.startAnimation(pushDownOutAnim);
									overlay.setVisibility(View.GONE);
								}
								createCircleParams();
								v.addView(new Circle(Main.this,x,y,r,Color.LTGRAY));
								clickDisabled = false;
							}
						});
					}
					else {
						clickDisabled = true;
						try {
							wrongAnswerSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

								public void onCompletion(MediaPlayer mp) {
									Animation pushDownInAnim = AnimationUtils.loadAnimation(
											Main.this, R.anim.push_down_in);
									overlay.startAnimation(pushDownInAnim);
									overlay.setVisibility(View.VISIBLE);
									resetButton.setVisibility(View.VISIBLE);
									messageText.setText("YOU LOSE");
									SharedPreferences settings = getSharedPreferences("HighScorePrefs", 0);
									int highScore = settings.getInt("HighScore", 0);
									if (level-1 > highScore && !cheatingModeWasOn) {
										Intent i = new Intent(Main.this, com.erraticduck.circles.NewHighScore.class);
										i.putExtra("NewHighScore", level-1);
										startActivity(i);
									}
								}
							});
							wrongAnswerSound.start();
							v.addView(new Circle(Main.this,x,y,r,Color.RED));
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
					}
					arg0.destroyDrawingCache();
				}
				return false;
			}
		});
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.clear();
    	menu.add(Menu.NONE,-9999,Menu.NONE,"Turn Cheating Mode " + (cheatingMode ? "OFF" : "ON"));
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == -9999) {
			cheatingMode = !cheatingMode;
			cheatingModeWasOn = true;
			Toast.makeText(this, "Cheating mode is " + (cheatingMode ? "ON!" : "OFF!"), Toast.LENGTH_SHORT).show();
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void createCircleParams() {
    	int newX = gen.nextInt(width);
    	int newY = gen.nextInt(height);
    	x = newX;
        y = newY;
        r = gen.nextInt(20) + 15;
    }
}