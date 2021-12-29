package app.simpleproject.drinkwater;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import app.simpleproject.drinkwater.fragments.HomeFragment;
import app.simpleproject.drinkwater.notifications.PowerSettings;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sp;
    private static long back_pressed;
    private FrameLayout adContainerView;
    private AdView adView;
    // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
    // for tests ca-app-pub-3940256099942544/9214589741
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_DrinkWater2021);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
    //    MobileAds.setRequestConfiguration(
    //           new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        adContainerView = findViewById(R.id.ad_view_container);

        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

        PowerSettings powerSettings = new PowerSettings();
        powerSettings.openPowerSettings(this);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new OrdersPagerAdapter(this));
        viewPager2.setCurrentItem(1, false);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch (position) {
                    case 0: {
                        tab.setText(R.string.title_notifications);
                        tab.setIcon(R.drawable.ic_notifications);
                        /*
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)
                        );
                        badgeDrawable.setVisible(true);
                        */
                        break;
                    }
                    case 1: {
                        tab.setText(R.string.title_home);
                        tab.setIcon(R.drawable.ic_water);
                //        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                //        badgeDrawable.setBackgroundColor(
                //                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)
                //        );
                //        badgeDrawable.setVisible(true);
                //        badgeDrawable.setNumber(8);
                        break;
                    }
                    case 2: {
                        tab.setText(R.string.title_settings);
                        tab.setIcon(R.drawable.ic_settings);
                        /*
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)
                        );
                        badgeDrawable.setVisible(true);
                        */

                //        badgeDrawable.setNumber(100);
                //        badgeDrawable.setMaxCharacterCount(3);
                        break;
                    }
                }
            }
        }
        );
        tabLayoutMediator.attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            //    Toast.makeText(getApplicationContext(), String.valueOf(tab.getPosition()), Toast.LENGTH_SHORT).show();
                if (tab.getPosition()==1){
                    HomeFragment.changeSizeOfBox(getApplicationContext(), false);

                    homeFragment.changeWaterVolume();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);
            }
        });

    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getPortraitAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    // exit from app on double click on "Back" in 2 seconds
    // пока выключил, потому что при нажатии в диалоговых окнах не закроет окна
    /*
    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), (R.string.againToExit),
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
     */
}