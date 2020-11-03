package com.example.iot_app.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;


import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainActivity {
    
    @BindView(R.id.viewpaper_layout)
    LinearLayout viewPPLayout;
    @BindView(R.id.about_icon)
    ImageButton aboutIcon;
    @BindView(R.id.spinnerView)
    PowerSpinnerView spinnerView;
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
        setupLanguage();
        setupViewPaper();
        setupSpinner();
    }

   

    private void setupViewPaper(){


     }

     private void setupSpinner(){
         IconSpinnerAdapter adapter = new IconSpinnerAdapter(spinnerView);
         ArrayList<IconSpinnerItem> list = new   ArrayList<IconSpinnerItem>();
         list.add( new IconSpinnerItem(getDrawable(R.drawable.united_kingdom),"En"));
         list.add( new IconSpinnerItem(getDrawable(R.drawable.japan),"Ja"));
         list.add( new IconSpinnerItem(getDrawable(R.drawable.vietnam_flag),"Vn"));
         adapter.setItems(list);
         spinnerView.setSpinnerAdapter(adapter);
         switch (getLanguagePref()){
             case "vi":
                 spinnerView.selectItemByIndex(2);
                 break;
             case "en":
                 spinnerView.selectItemByIndex(0);
                 break;
             case "ja":
                 spinnerView.selectItemByIndex(1);
                 break;
         }
         spinnerView.setOnSpinnerItemSelectedListener((i, o) -> {
            switch(i){
                case 0:
                    changeLanguage("en");
                    setLanguagePref("en");
                    restartAC();
                    break;
                case 1:
                    changeLanguage("ja");
                    setLanguagePref("ja");
                    restartAC();
                    break;
                case 2:
                    changeLanguage("vi");
                    setLanguagePref("vi");
                    restartAC();
                    break;
            }
         });
         
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
                     DynamicToast.make(this, getResources().getString(R.string.open_company_website), R.drawable.bluetooth_icon).show();
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

    private void setupLanguage(){
        if(TextUtils.isEmpty(getLanguagePref()) || getLanguagePref().length() < 2){
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = Resources.getSystem().getConfiguration().getLocales().get(0);
            } else {
                //noinspection deprecation
                locale = Resources.getSystem().getConfiguration().locale;
            }

          setLanguagePref(locale.getLanguage());
        }
        else{
            if(getLanguagePref().equals("ja")){
                setLanguagePref("ja");
                changeLanguage("ja");
            }
            else if(getLanguagePref().equals("en")){
                setLanguagePref("en");
                changeLanguage("en");
            }     else if(getLanguagePref().equals("vi")){
                setLanguagePref("vi");
                changeLanguage("vi");
            }
        }

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


    private void changeLanguage(String languageToLoad){
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
    private void restartAC(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}


