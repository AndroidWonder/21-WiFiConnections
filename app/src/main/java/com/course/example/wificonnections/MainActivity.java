/*
 * This application will scan for all WiFi networks available.
 * It scans upon opening the app and then when the refresh menu option is clicked.
 * It uses a broadcast receiver to display the results of the scan.
 * There will be 2 menu items on the Action Bar - one with text and one with an icon.
 */

package com.course.example.wificonnections;

import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
     
    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
     
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       mainText = (TextView) findViewById(R.id.mainText);
       
       getActionBar().setDisplayShowHomeEnabled(false);

        
       // Initiate wifi service manager
       Context context = getApplicationContext();
       mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

       // Check if wifi is disabled
       if (mainWifi.isWifiEnabled() == false)
            {   
                // If wifi disabled then enable it
                Toast.makeText(MainActivity.this, "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();
                 
                mainWifi.setWifiEnabled(true);
            } 
        
       // create broadcast receiver object
       receiverWifi = new WifiReceiver();
        
       // Register broadcast receiver 
       // Broacast receiver will automatically be called when scan has completed
       registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
       mainWifi.startScan();
       mainText.setText("Starting Scan...");
    }
 
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main, menu);
	    return true;
    }
 
    public boolean onOptionsItemSelected(MenuItem item) {
        mainWifi.startScan();
        mainText.setText("Starting Scan");
        return super.onOptionsItemSelected(item);
    }
 
    protected void onPause() {
    	super.onPause();
        unregisterReceiver(receiverWifi);        
    }
 
    protected void onResume() {
    	 super.onResume();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));   
    }
     
   
    //Broacast receiver inner class
    class WifiReceiver extends BroadcastReceiver {
         
        // This method call when scan has completed and results are available
        public void onReceive(Context c, Intent intent) {
             
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults(); 
            sb.append("\n        Number Of Wifi connections :"+wifiList.size()+"\n\n");
             
            for(int i = 0; i < wifiList.size(); i++){
                 
                sb.append(new Integer(i+1).toString() + ". ");
                sb.append((wifiList.get(i)).toString());
                sb.append("\n\n");
            }
             
            mainText.setText(sb);  
        }
         
    }
}