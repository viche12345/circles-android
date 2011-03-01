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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class StartUp extends Activity {
	
	private static Random gen = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		TextView mainLogo = (TextView)findViewById(R.id.main_logo);
		TextView author = (TextView)findViewById(R.id.author);
		Typeface chiller = Typeface.createFromAsset(getAssets(), "chiller.ttf");
		Typeface brushScript = Typeface.createFromAsset(getAssets(), "brushscript.ttf");
		mainLogo.setTypeface(chiller);
		author.setTypeface(brushScript);
		
		Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent i = new Intent(StartUp.this, com.erraticduck.circles.Main.class);
				startActivity(i);
			}
		});
		
        FrameLayout randomCirclesView = (FrameLayout)findViewById(R.id.random_circles);
        randomCirclesView.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View arg0, MotionEvent event) {
				int x = (int)event.getX();
				int y = (int)event.getY();
				int r = gen.nextInt(35) + 15;
				((FrameLayout)arg0).addView(new Circle(StartUp.this, x, y, r));
				
				return false;
			}
		});
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		TextView highScore = (TextView) findViewById(R.id.highscore);
		final SharedPreferences settings = getSharedPreferences("HighScorePrefs", 0);
		if (settings.getInt("HighScore", 0) > 0) {
			highScore.setText("High Score: " + settings.getString("Name","") + " with " + settings.getInt("HighScore", 0) + " circles");
			if (settings.getString("Name","").equalsIgnoreCase("")) highScore.setText("High Score: " + settings.getInt("HighScore", 0) + " circles");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(Menu.NONE,-9999,Menu.NONE,"Clear High Score");
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == -9999) {
			SharedPreferences settings = getSharedPreferences("HighScorePrefs", 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.remove("Name");
			editor.remove("HighScore");
			editor.commit();
			TextView highScore = (TextView) findViewById(R.id.highscore);
			highScore.setText("");
			return true;
		}
		else {
			return super.onOptionsItemSelected(item);
		}
	}

}
