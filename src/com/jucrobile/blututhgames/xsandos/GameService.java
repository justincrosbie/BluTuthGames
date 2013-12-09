package com.jucrobile.blututhgames.xsandos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;

public class GameService {
	  private static GameService _instance = new GameService();

	  public int[][] positions = new int[][] { 
	      { 0, 0, 0 },
	      { 0, 0, 0 },
	      { 0, 0, 0 }
	  };

	  public static GameService getInstance() {
	    return _instance;
	  }

	  private void updatePositions( Document doc ) {
	    for( int x = 0; x < 3; x++ ) {
	      for( int y = 0; y < 3; y++ ) {
	        positions[x][y] = 0;
	      }
	    }
	    doc.getDocumentElement().normalize();
	    NodeList items = doc.getElementsByTagName("move");
	    for (int i=0;i<items.getLength();i++){
	      Element me = (Element)items.item(i);
	      int x = Integer.parseInt( me.getAttribute("x") );
	      int y = Integer.parseInt( me.getAttribute("y") );
	      int color = Integer.parseInt( me.getAttribute("color") );
	      positions[x][y] = color;
	    }
	  }
	  
	  public void restartGame() {
		  positions = new int[][] { 
			      { 0, 0, 0 },
			      { 0, 0, 0 },
			      { 0, 0, 0 }
			  };		  
	  }

	  public void startGame( int game ) {
//	    HttpClient httpclient = new DefaultHttpClient();
//	    HttpPost httppost = new HttpPost("http://10.0.2.2/ttt/moves.php");

	    try {
	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	      nameValuePairs.add(new BasicNameValuePair("game", Integer.toString(game)));
//	      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//	      HttpResponse response = httpclient.execute(httppost);
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db = dbf.newDocumentBuilder();
//	      updatePositions( db.parse(response.getEntity().getContent()) );
	    } catch (Exception e) {
	      Log.v("ioexception", e.toString());
	    }
	  }

	  public void setPosition( int game, int x, int y, int color ) {
		  
//		HttpClient httpclient = new DefaultHttpClient();
//	    HttpPost httppost = new HttpPost("http://10.0.2.2/ttt/move.php");

	    positions[x][y] = color;

	    try {
	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	      nameValuePairs.add(new BasicNameValuePair("game", Integer.toString(game)));
	      nameValuePairs.add(new BasicNameValuePair("x", Integer.toString(x)));
	      nameValuePairs.add(new BasicNameValuePair("y", Integer.toString(y)));
	      nameValuePairs.add(new BasicNameValuePair("color", Integer.toString(color)));
//	      httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//	      HttpResponse response = httpclient.execute(httppost);
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder db = dbf.newDocumentBuilder();
//	      updatePositions( db.parse(response.getEntity().getContent()) );
	    } catch (Exception e) {
	      Log.v("ioexception", e.toString());
	    }
	  }
	}