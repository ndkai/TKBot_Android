package com.example.iot_app.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.iot_app.R;

import com.example.iot_app.ui.base.BaseActivity;

import com.example.iot_app.ui.main.Model.Slider;

import com.example.iot_app.ui.main.adapter.ItemAdapter;
import com.example.iot_app.ui.main.adapter.MainFragment;
import com.example.iot_app.utils.LanguageUtils;
import com.example.iot_app.utils.ScreenHelper;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.shreyaspatil.material.navigationview.MaterialNavigationView;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainActivity {
    
    @BindView(R.id.viewpaper_layout)
    LinearLayout viewPPLayout;
    @BindView(R.id.about_icon)
    ImageButton aboutIcon;
    GoogleApiClient googleApiClient;


    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;

    @Inject
    MainPresenter mMainPresenter;
    private ArrayList<Slider> sliderItems;
    private boolean clickAble = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBaseActivity().getIActivityComponent().inject(this);
        mMainPresenter.onAttached(this);
         turnOnGPS();
        ButterKnife.bind(this);
        setupViews();
        replaceFragment(R.id.viewpaper_layout, new MainFragment());

    }

    private void setupViews(){
        setupViewPaper();
    }

   

    private void setupViewPaper(){


     }

      private void turnOnGPS(){
          if (googleApiClient == null) {
              googleApiClient = new GoogleApiClient.Builder(this)
                      .addApi(LocationServices.API).build();
              googleApiClient.connect();

              LocationRequest locationRequest = LocationRequest.create();
              locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
              locationRequest.setInterval(30 * 1000);
              locationRequest.setFastestInterval(5 * 1000);
              LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                      .addLocationRequest(locationRequest);

              //**************************
              builder.setAlwaysShow(true); //this is the key ingredient
              //**************************

              PendingResult<LocationSettingsResult> result =
                      LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
              result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                  @Override
                  public void onResult(LocationSettingsResult result) {
                      final Status status = result.getStatus();
                      final LocationSettingsStates state = result.getLocationSettingsStates();
                      switch (status.getStatusCode()) {
                          case LocationSettingsStatusCodes.SUCCESS:
                              // All location settings are satisfied. The client can initialize location
                              // requests here.
                              break;
                          case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                              // Location settings are not satisfied. But could be fixed by showing the user
                              // a dialog.
                              try {
                                  // Show the dialog by calling startResolutionForResult(),
                                  // and check the result in onActivityResult().
                                  status.startResolutionForResult(
                                          MainActivity.this, 1000);
                              } catch (IntentSender.SendIntentException e) {
                                  // Ignore the error.
                              }
                              break;
                          case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                              // Location settings are not satisfied. However, we have no way to fix the
                              // settings so we won't show the dialog.
                              break;
                      }
                  }
              });
          }
      }
      @OnClick(R.id.about_icon)
     public void onAboutIconClick(){
//        String content = "";
         CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                 .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                 .setHeaderView(R.layout.about_layout)
                 .addButton("   Contact us   ", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                        //url browers
                     Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://titkul.com/"));
                     startActivity(browserIntent);
                     DynamicToast.make(this, "Đang mở web công ty",R.drawable.bluetooth_icon).show();
                     dialog.dismiss();
                 })
                 .addButton("   Cancel   ", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                     // Toast.makeText(BlocklyActivity.this, "Sao bjn", Toast.LENGTH_SHORT).show();
                     dialog.dismiss();
                 });
         builder.show();
     }


    public static void open(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:

                break;

            case R.id.nav_pass:

                break;

            case R.id.nav_instruction:

                break;
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ScreenHelper.hideSystemUI(this);
        }
    }

    @Override
    public void setLanguagePref(String language) {
        mMainPresenter.setLanguagePref(language);
    }

    @Override
    public String getLanguagePref() {
        return mMainPresenter.getLanguagePref();
    }

}


