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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewHighScore extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newhighscore);
		Button saveButton = (Button)findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				SharedPreferences settings = getSharedPreferences("HighScorePrefs", 0);
				SharedPreferences.Editor editor = settings.edit();
				EditText et = (EditText) findViewById(R.id.nameEntry);
				editor.putString("Name", et.getText().toString());
				editor.putInt("HighScore", getIntent().getIntExtra("NewHighScore", 0));
				editor.commit();
				finish();
			}
		});
	}

}
