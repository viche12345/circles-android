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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Circle extends View {
	
	private int x,y,r = 0;
	private Paint mPaint;
	private Random gen = new Random();

	public Circle(Context context, int x, int y, int r, int color) {
		super(context);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(color);
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public Circle(Context context, int x, int y, int r) {
		super(context);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.rgb(gen.nextInt(255), gen.nextInt(255), gen.nextInt(255)));
		this.x = x;
		this.y = y;
		this.r = r;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(x, y, r, mPaint);
	}
	
}
