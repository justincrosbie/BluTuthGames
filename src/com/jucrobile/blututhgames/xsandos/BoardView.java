package com.jucrobile.blututhgames.xsandos;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jucrobile.blututhgames.BluTuthApplication;

public class BoardView extends View {

    BluTuthApplication mApplication;

	  public int[][] positions = new int[][] { 
		      { 0, 0, 0 },
		      { 0, 0, 0 },
		      { 0, 0, 0 }
		  };
	
    
	public BluTuthApplication getApplication() {
		return mApplication;
	}

	public void setApplication(BluTuthApplication mApplication) {
		this.mApplication = mApplication;
	}

	private int _color = 1;
	private boolean colorSet = false;
	private boolean myGo = true;
	  
	  public void setColor( int c ) {
	    _color = c;
	  }
	  
	  public BoardView(Context context) {
	      super(context);
	  }
	  
	  public BoardView(Context context, AttributeSet attrs) {
	      super(context,attrs);
	  }
	  
	  public BoardView(Context context, AttributeSet attrs, int defStyle) {
	      super(context,attrs,defStyle);
	  }

	  public boolean onTouchEvent( MotionEvent event ) {
		  
	    if ( event.getAction() != MotionEvent.ACTION_UP )
	      return true;

		colorSet = true;

		if ( !myGo ) {
            Toast.makeText(getContext(), "Its not your go", Toast.LENGTH_SHORT).show();
//            return true;
		}
		
		myGo = false;
		
	    int offsetX = getOffsetX();
	    int offsetY = getOffsetY();
	    int lineSize = getLineSize();
	    for( int x = 0; x < 3; x++ ) {
	      for( int y = 0; y < 3; y++ ) {
	        Rect r = new Rect( ( offsetX + ( x * lineSize ) ),
	            ( offsetY + ( y * lineSize ) ),
	            ( ( offsetX + ( x * lineSize ) ) + lineSize ),
	            ( ( offsetY + ( y * lineSize ) ) + lineSize ) );
	        if ( r.contains( (int)event.getX(), (int)event.getY() ) ) {

	    	  positions[x][y] = _color;
	          invalidate();

	          String str = "XsAndOs:" + x + "," + y;
		      mApplication.getChatService().write(str.getBytes());
	
	          return true;
	        }
	      }
	    }
	    return true;
	  }
	  
	  public void drawOpponentsPiece(String str) {
		 int x = 0;
		 int y = 0;
		 
		 if ( !colorSet ) {
			 // Opponent went first - you're the other color;
			 _color = 2;
		 }
		 
		 myGo = true;
		 
		 int opponentsColor = getOtherColor();
		  if ( str.startsWith("XsAndOs:") ) {
			  str = str.substring(8);
			  StringTokenizer t = new StringTokenizer(str, ",");
			  x = Integer.valueOf(t.nextToken());
			  y = Integer.valueOf(t.nextToken());
		  }
		  
    	  positions[x][y] = opponentsColor;
          invalidate();
	  }

	  private int getSize() {
	    return (int) ( (float) 
	    ( ( getWidth() < getHeight() ) ? getWidth() : getHeight() ) * 0.8 );
	  }

	  private int getOffsetX() {
	    return ( getWidth() / 2 ) - ( getSize( ) / 2 );
	  }
	  
	  private int getOffsetY() {
	    return ( getHeight() / 2 ) - ( getSize() / 2 );
	  }
	  
	  private int getLineSize() {
	    return ( getSize() / 3 );
	  }

	  private int getOtherColor() {
		  return _color == 1 ? 2 : 1;
	  }
	  
	  protected void onDraw(Canvas canvas) {
	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setColor(Color.BLACK);
	    canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(), paint);
	    
	    int size = getSize();
	    int offsetX = getOffsetX();
	    int offsetY = getOffsetY();
	    int lineSize = getLineSize();
	    
	    paint.setColor(Color.DKGRAY);
	    paint.setStrokeWidth( 5 );
	    for( int col = 0; col < 2; col++ ) {
	      int cx = offsetX + ( ( col + 1 ) * lineSize );
	      canvas.drawLine(cx, offsetY, cx, offsetY + size, paint);
	    }
	    for( int row = 0; row < 2; row++ ) {
	      int cy = offsetY + ( ( row + 1 ) * lineSize );
	      canvas.drawLine(offsetX, cy, offsetX + size, cy, paint);
	    }
	    int inset = (int) ( (float)lineSize * 0.1 );
	    
	    paint.setColor(Color.WHITE);
	    paint.setStyle(Paint.Style.STROKE); 
	    paint.setStrokeWidth( 10 );
	    Map<Coords, Rect> rects = new HashMap<Coords, Rect>();
	    for( int x = 0; x < 3; x++ ) {
	      for( int y = 0; y < 3; y++ ) {
	        Rect r = new Rect( ( offsetX + ( x * lineSize ) ) + inset,
	            ( offsetY + ( y * lineSize ) ) + inset,
	            ( ( offsetX + ( x * lineSize ) ) + lineSize ) - inset,
	            ( ( offsetY + ( y * lineSize ) ) + lineSize ) - inset );
	        
	        rects.put(new Coords(x, y), r);
	        
	        if ( positions[ x ][ y ] == 1 ) {
	          canvas.drawCircle( ( r.right + r.left ) / 2, 
	                  ( r.bottom + r.top ) / 2, 
	                  ( r.right - r.left ) / 2, paint);
	        }
	        if ( positions[ x ][ y ] == 2 ) {
	          canvas.drawLine( r.left, r.top, r.right, r.bottom, paint);
	          canvas.drawLine( r.left, r.bottom, r.right, r.top, paint);
	        }
	      }
	    }
	    
	    boolean iWon = false;
	    paint.setColor(Color.RED);
	    Win won = checkWinner(positions, 3, _color);
	    if ( won != null ) {
	    	iWon = true;
            Toast.makeText(getContext(), "You won! " + won.type + won.pos, Toast.LENGTH_SHORT).show();
	    }
	    if ( won == null ) {
	    	won = checkWinner(positions, 3, getOtherColor());
	    }
	    if ( won != null) {
	    	if ( !iWon ) {
	    		Toast.makeText(getContext(), "They won :( " + won.type + won.pos, Toast.LENGTH_SHORT).show();
	    	}

        	if ( won.type.compareTo(WinType.DIAG) == 0 ) {
            	
        		if ( won.pos == 0 ) {
	            	Rect r1 = rects.get(new Coords(0,0));
	            	Rect r2 = rects.get(new Coords(2,2));
	
	    	        canvas.drawLine( r1.left, r1.top, r2.right, r2.bottom, paint);
        		} else {
	            	Rect r1 = rects.get(new Coords(0,2));
	            	Rect r2 = rects.get(new Coords(2,0));
	
	    	        canvas.drawLine( r1.left, r1.bottom, r2.right, r2.top, paint);
        		}

	    	} else if ( won.type.compareTo(WinType.COL) == 0 )  {
	        	Rect r1 = rects.get(new Coords(won.pos,0));
	        	Rect r2 = rects.get(new Coords(won.pos,2));
    	        canvas.drawLine( r1.right-(r1.right-r1.left)/2, r1.top, r1.right-(r1.right-r1.left)/2, r2.bottom, paint);
	    	} else {
	        	Rect r1 = rects.get(new Coords(0, won.pos));
	        	Rect r2 = rects.get(new Coords(2, won.pos));
    	        canvas.drawLine( r1.left, r1.top-(r1.top-r1.bottom)/2, r2.right, r1.top-(r1.top-r1.bottom)/2, paint);
	    	}
            
	    }
	  }
	  
	  private class Coords {
		  public int x;
		  public int y;
		  
		  public Coords(int x, int y) {
			  this.x = x;
			  this.y = y;
		  }

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Coords other = (Coords) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		private BoardView getOuterType() {
			return BoardView.this;
		}
		  
	  }

	  public static enum WinType {ROW, COL, DIAG};
	  private class Win {
		  public int pos;
		  public WinType type; 
	  }
		private Win checkWinner(int[][] board, int size, int player) {
			// check each column
			for (int x = 0; x < size; x++) {
				int total = 0;
				int[] bits = new int[size];
				for (int y = 0; y < size; y++) {
					if (board[x][y] == player) {
						total++;
						
					}
				}
				if (total >= size) {
					Win w = new Win();
					w.type = WinType.COL;
					w.pos = x;
					return w; // they win
				}
			}

			// check each row
			for (int y = 0; y < size; y++) {
				int total = 0;
				for (int x = 0; x < size; x++) {
					if (board[x][y] == player) {
						total++;
					}
				}
				if (total >= size) {
					Win w = new Win();
					w.type = WinType.ROW;
					w.pos = y;
					return w; // they win
				}
			}

			// forward diag
			int total = 0;
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (x == y && board[x][y] == player) {
						total++;
					}
				}
			}
			if (total >= size) {
				Win w = new Win();
				w.type = WinType.DIAG;
				w.pos = 0;
				return w; // they win
			}

			// backward diag
			total = 0;
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (x + y == size - 1 && board[x][y] == player) {
						total++;
					}
				}
			}
			if (total >= size) {
				Win w = new Win();
				w.type = WinType.DIAG;
				w.pos = size;
				return w; // they win
			}

			return null; // nobody won
		}
	  
	}