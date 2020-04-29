package com.gallery.plugin;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gallery.plugin.galleryplugin.R;

import org.json.JSONObject;

public class GalleryMainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int REQUEST_WRITE_PERMISSION = 411;
    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.inflateMenu(R.menu.my_navigation_items);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        addBadgeView();
        requestPermission();
    }
    private void addBadgeView() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);

        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView, false);

        itemView.addView(notificationBadge);
    }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED
      && requestCode == REQUEST_WRITE_PERMISSION && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
      loadFragment(new PictureFragment());
    }
  }
  private void requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_WRITE_PERMISSION);
    } else {
      loadFragment(new PictureFragment());
    }
  }


  private boolean loadFragment(Fragment fragment) {
    if (fragment != null) {
      getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.main_frame, fragment)
        .commit();
      return true;
    }
    return false;
  }

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
      Fragment fragment = null;
      JSONObject object = new JSONObject();
      switch (menuItem.getTitle().toString()) {

        case "Pictures":
              fragment = new PictureFragment();
              break;
        case "Videos":
              fragment = new VideosFragment();
              break;

        case "Live":
            try {
                object.put("data", "request_live");
            }catch (Exception e)
            {
                //
            }
            GalleryPlugin.returnResponse(object);
            finish();
            break;
        case "Pending":
            try {
                object.put("data", "request_pending");
            }catch (Exception e)
            {
                //
            }
          case "F!eekTok":
              try {
                  object.put("data", "request_fleektok");
              }catch (Exception e)
              {
                  //
              }
            GalleryPlugin.returnResponse(object);
            finish();
            break;
      }

      return loadFragment(fragment);
    }
  };
}
