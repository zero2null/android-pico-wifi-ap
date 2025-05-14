package com.example.androidpicowifiap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.androidpicowifiap.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.controlFragment, R.id.loadingFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    public void connectWifi() {
        // Android level low, legacy API is available?
        // onnectToWifiLegacy(context, ssid, password)

        final NetworkSpecifier specifier =
                new WifiNetworkSpecifier.Builder()
                        .setSsid("picow_test")
                        .setWpa2Passphrase("password")
                        .build();

        final NetworkRequest request =
                new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .setNetworkSpecifier(specifier)
                        .build();

        connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            public void onAvailable(Network network) {
                connectivityManager.bindProcessToNetwork(network);
                Log.d("MyApp", "onAvailable");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.action_loadingFragment_to_controlFragment);
                    }
                });

            }

            public void onLosing(Network network, int maxMsToLive) {
                Log.d("MyApp", "onLosing");
            }

            public void onLost(Network network) {
                connectivityManager.bindProcessToNetwork(null);
                Log.d("MyApp", "onLost");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                        navController.navigate(R.id.action_controlFragment_to_loadingFragment);
                    }
                });
            }

            public void onUnavailable() {
                Log.d("MyApp", "onUnavailable");
            }

            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                Log.d("MyApp", "onCapabilitiesChanged");
            }

            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                Log.d("MyApp", "onLinkPropertiesChanged");
            }

            public void onBlockedStatusChanged(Network network, boolean blocked) {
                Log.d("MyApp", "onBlockedStatusChanged");
            }
        };

        connectivityManager.requestNetwork(request, networkCallback);

        // Release the request when done.
//                connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}