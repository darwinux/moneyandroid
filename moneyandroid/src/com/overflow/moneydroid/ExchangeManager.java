package com.overflow.moneydroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.util.Xml;

public class ExchangeManager {
	private final static String DEBUG_TAG = "ExchangeManager";
	
	public static ArrayList<MoneyInfo> getMoneyInfo(InputStream inStream)
    {
		ArrayList<MoneyInfo> al = null;
		MoneyInfo mi = null;
		//Context context = null;
		XmlPullParser xmlPull = Xml.newPullParser();
		try {
			xmlPull.setInput(inStream,"UTF-8");
			
			int eventCode = xmlPull.getEventType();
			
			while (eventCode != XmlPullParser.END_DOCUMENT) {
				
				switch (eventCode) {
				case XmlPullParser.START_DOCUMENT:
					al = new ArrayList<MoneyInfo>();
					break;
				case XmlPullParser.START_TAG:
					String name = xmlPull.getName();
					if(name.equalsIgnoreCase("money")) {
						mi = new MoneyInfo();
						mi.setCode(xmlPull.getAttributeValue(null, "code"));
						mi.setName(xmlPull.getAttributeValue(null,"name"));
						//Log.e("111", xmlPull.getAttributeValue(null,"name"));
					}
					break;
				case XmlPullParser.END_TAG:
					if(mi != null && xmlPull.getName().equalsIgnoreCase("money")) {
						al.add(mi);
						mi = null;
					}
					break;
					
				}
				eventCode = xmlPull.next();
			}
			
		} catch (XmlPullParserException e) {
			Log.e(DEBUG_TAG, e.toString());
		} catch (IOException e) {
			Log.e(DEBUG_TAG, e.toString());
		}
		return al;
		
		//return al;
		
		
		
		//XmlDo xmd = new XmlDo();
		//MoneyInfo[] moneyClass = new MoneyInfo[9];
		//ArrayList<MoneyInfo> li = xmd.readXml();
		
	/*	MoneyInfo CNY=new MoneyInfo();
		CNY.setName("�����");
		CNY.setCode("CNY");
		li.add(CNY);
		MoneyInfo HKD=new MoneyInfo();
		HKD.setName("�۱�");
		HKD.setCode("HKD");
		li.add(HKD);
		MoneyInfo TWD=new MoneyInfo();
		TWD.setName("̨��");
		TWD.setCode("TWD");
		li.add(TWD);
		MoneyInfo EUR=new MoneyInfo();
		EUR.setName("ŷԪ");
		EUR.setCode("EUR");
		li.add(EUR);
		MoneyInfo USD=new MoneyInfo();
		USD.setName("��Ԫ");
		USD.setCode("USD");
		li.add(USD);
		MoneyInfo GBP=new MoneyInfo();
		GBP.setName("Ӣ��");
		GBP.setCode("GBP");
		li.add(GBP);
		MoneyInfo AUD=new MoneyInfo();
		AUD.setName("��Ԫ");
		AUD.setCode("AUD");
		li.add(AUD);
		MoneyInfo KRW=new MoneyInfo();
		KRW.setName("��Ԫ");
		KRW.setCode("KRW");
		li.add(KRW);
		MoneyInfo JPY=new MoneyInfo();
		JPY.setName("��Ԫ");
		JPY.setCode("JPY");
		li.add(JPY); 
		/*moneyClass[0].setName("�����");
		moneyClass[0].setCode("CNY");
		moneyClass[1].setName("�۱�");
		moneyClass[1].setCode("HKD");
		moneyClass[2].setName("̨��");
		moneyClass[2].setCode("TWD");
		moneyClass[3].setName("ŷԪ");
		moneyClass[3].setCode("EUR");
		moneyClass[4].setName("��Ԫ");
		moneyClass[4].setCode("USD");
		moneyClass[5].setName("Ӣ��");
		moneyClass[5].setCode("GBP");
		moneyClass[6].setName("��Ԫ");
		moneyClass[6].setCode("AUD");
		moneyClass[7].setName("��Ԫ");
		moneyClass[7].setCode("KRW");
		moneyClass[8].setName("��Ԫ");
		moneyClass[8].setCode("JPY");
		li.add(moneyClass[0]);
		li.add(moneyClass[1]);
		li.add(moneyClass[2]);
		li.add(moneyClass[3]);
		li.add(moneyClass[4]);
		li.add(moneyClass[5]);
		li.add(moneyClass[6]);
		li.add(moneyClass[7]);
		li.add(moneyClass[8]);*/
		
    }

    //ת������ sourceCode��Դ���� targetCode��Ŀ����� count��Դ���ұ�ֵ ����Ŀ����ұ�ֵ
    public static double toExchange(String sourceCode,String targetCode,double count)
    {
    	final String DEBUG_TAG = "ExchangeManager";
    	String httpUrl = "http://download.finance.yahoo.com/d/quotes.html?s="+
		sourceCode+targetCode+"=X&f=l1";
		//����һ��URL����
		//double d = Double.parseDouble(edittext.getText().toString());
		
		String resultData = "";
		URL url = null;
		
	
		
		try
		{
			
			url = new URL(httpUrl); 
		}
		catch (MalformedURLException e)
		{
			Log.e(DEBUG_TAG, "MalformedURLException");
		}
		if (url != null)
		{
			
			try
			{
				// ʹ��HttpURLConnection������
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				//�õ���ȡ������(��)
				InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
				
				// Ϊ�������BufferedReader
				BufferedReader buffer = new BufferedReader(in);
				String inputLine = null;
				
				while (((inputLine = buffer.readLine()) != null))
				{
					//������ÿһ�к������һ��"\n"������
					
					resultData += inputLine + "\n";
				
					
				}		  
				//�ر�InputStreamReader
				Log.e(DEBUG_TAG,httpUrl);
				in.close();
				//�ر�http����
				urlConn.disconnect();
				//������ʾȡ�õ�����
				if ( resultData != null )
				{
					Log.e(DEBUG_TAG, resultData);
					count *= (Double.parseDouble(resultData));
					
				/*	Message m = new Message();
					m.arg1 = Activity01.MONEY_GET;
					Activity01.this.messageListener.sendMessage(m);
					Log.e(DEBUG_TAG, Double.toString(d)); */
				}
				else 
				{
					//textview.setText("��ȡ������ΪNULL");
					Log.e(DEBUG_TAG, "��ȡ������ΪNULL");
				}
			}
			catch (IOException e)
			{
				Log.e(DEBUG_TAG, "IOException");
			}
			
		}
		else
		{
			Log.e(DEBUG_TAG, "Url NULL");
		}
		//DecimalFormat di = new DecimalFormat("#.00");
		return count;
    }
    

    public static boolean isNum(String s) {
    	
    	  boolean hasDot = false;
    	  char[] scr = s.toCharArray(); 
          for (char chr : scr) 
          {
              if (!(chr >= '0' && chr <= '9'))
              {
                  if (chr == '.' && !hasDot)
                  {
                      hasDot = true;
                      continue;
                  }
                  return false;
                  
              }
          }
          return true;
    }
    
}
