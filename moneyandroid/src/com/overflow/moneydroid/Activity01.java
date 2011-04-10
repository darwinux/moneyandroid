package com.overflow.moneydroid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Handler;

public class Activity01 extends Activity {
	private final String DEBUG_TAG = "MoneyDroid";
	private final static int MONEY_GET = 0;

	static ArrayList<MoneyInfo> lst = null;
	ArrayList<String> lstName = null;
	
	
	private TextView textview = null;
	private Button button = null;
	private Spinner spinner1 = null;
	private Spinner spinner2 = null;
	private EditText edittext = null;
	private ProgressDialog pDlg = null;
	
	static String sp1 = "";
	static String sp2 = "";
	private double d = 0.0;
	private ArrayAdapter<String> adapter;
	DecimalFormat di = null;//new DecimalFormat("#.00");
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textview = (TextView) this.findViewById(R.id.TextView01);
        button = (Button) this.findViewById(R.id.Button01);
        spinner1 = (Spinner) this.findViewById(R.id.Spinner01);
        spinner2 = (Spinner) this.findViewById(R.id.Spinner02);
        edittext = (EditText) this.findViewById(R.id.EditText01);
        
        lstName = new ArrayList<String>();
        
        //InputStream instream = this.getClass().getClassLoader().getResourceAsStream("moneyname.xml");
		//lst = ExchangeManager.getMoneyInfo(instream);
        InputStream instream = null;
    	try {
    		instream = this.getClass().getClassLoader().getResourceAsStream("moneyname.xml");
    		lst = ExchangeManager.getMoneyInfo(instream);
    		
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	finally {
    		if(instream!=null)
			{
				
				try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
    	}
    	
    	//lst = ExchangeManager.getMoneyInfo(instream);
    	
        for(MoneyInfo info:lst)
    	{
    	    lstName.add(info.getName());
    	}
        
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lstName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        
        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//sp1 = moneyName[arg2];
				//sp1 = em.getMoneyInfo().get(arg2).getCode();
				sp1 = lst.get(arg2).getCode();
				Log.e(DEBUG_TAG, sp1);
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//sp2 = moneyName[arg2];
				//sp2 = em.getMoneyInfo().get(arg2).getCode();
				sp2 = lst.get(arg2).getCode();
				Log.e(DEBUG_TAG, sp2);
				arg0.setVisibility(View.VISIBLE);
				
			
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        

        
        
        button.setOnClickListener(new Button.OnClickListener() {
        	
        	
			public void onClick(View v) {
				// TODO Auto-generated method stub
			/*	String httpUrl = "http://www.123cha.com/hl/?q="+edittext.getText().toString()+
				"&from="+sp1+"&to="+sp2+"&s="+
				sp1+sp2+"#symbol="+sp1+sp2+"=X;range=3m;"; */
				
				
				if(TextUtils.isEmpty(edittext.getText().toString())==true) {
					textview.setText("请输入正确的金额");
				}
				else {
					if(ExchangeManager.isNum(edittext.getText().toString())==true) {
						d = Double.parseDouble(edittext.getText().toString());
						
						
						pDlg = ProgressDialog.show(Activity01.this, "请等待", "正在为您转换中",true);
						Thread td = new Thread(new loop());
						td.start();
					}
					else {
						textview.setText("请输入正确的金额");
					}
				}
				
				
					
				
				
				
				
				
				
			}
			
        });
    }
    	public class loop implements Runnable {
    	   public void run() {
				// TODO Auto-generated method stub
				//getMoney();
    		   //Log.e("THREAD 1", Double.toString(d));
				d = ExchangeManager.toExchange(sp1, sp2, d);
				
				Message m = new Message();
				m.arg1 = Activity01.MONEY_GET;
				Activity01.this.messageListener.sendMessage(m);
				
			}
    		
    	}
  
     	
        private Handler messageListener = new Handler(){
        	public void handleMessage(Message msg) {
    			switch(msg.arg1){
    			case MONEY_GET:
    				if(!Thread.currentThread().isInterrupted()) {
    					pDlg.dismiss();
    					di = new DecimalFormat("#.00");
    					textview.setText(di.format(d));    
    							//Double.toString(d))
    				}
    				
    				
    				break;
    				
    			}
    			super.handleMessage(msg);
    		}
        };
        
       
}