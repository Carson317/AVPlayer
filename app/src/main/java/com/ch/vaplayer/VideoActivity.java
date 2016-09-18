package com.ch.vaplayer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ch.vaplayer.adapter.ListViewAdapter;
import com.ch.vaplayer.media.LoadedImage;
import com.ch.vaplayer.media.Video;
import com.ch.vaplayer.provider.AbstructProvider;
import com.ch.vaplayer.provider.VideoProvider;
import com.ch.vaplayer.utils.PermissionHelper;
import com.ch.vaplayer.utils.VALogger;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VideoActivity extends AppCompatActivity {

    private ListView mVideoListView;
    private ListViewAdapter mVideoListAdapter;
    List<Video> mListVideo;
    int videoSize;

    //save our header or result
    private Drawer result = null;
    private Toolbar mToolbar = null;

    private static final int  CODE_FOR_WRITE_PERMISSION = 1;
    private static final String WRITE_PERMISSION = "Manifest.permission.READ_EXTERNAL_STORAGE";
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MediaStore.Video.query();
        // Handle Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.drawer_item_video);
        setSupportActionBar(mToolbar);


        VALogger.Info("***************************************************************");

        //new DrawerBuilder().withActivity(this).build();
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem itemVideo = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_video).withIcon(R.drawable.ic_menu_video);
        PrimaryDrawerItem itemAudio = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_audio).withIcon(R.drawable.ic_menu_audio);
        SecondaryDrawerItem itemSettings = (SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_settings).withIcon(R.drawable.ic_menu_preferences);
        SecondaryDrawerItem itemHelp = (SecondaryDrawerItem) new SecondaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_help).withIcon(R.drawable.ic_menu_help);
        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder(this)
                .withRootView(R.id.drawer_container)
                .withToolbar(mToolbar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        itemVideo,
                        itemAudio,
                        new DividerDrawerItem(),
                        itemSettings,
                        itemHelp
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                showVideo();
                            } else if (drawerItem.getIdentifier() == 2) {
                                showAudio();
                            } else if (drawerItem.getIdentifier() == 3) {
                                showSettings();
                            } else if (drawerItem.getIdentifier() == 4) {
                                showHelp();
                            }
                        }
                        return false;
                    }
                }).withSavedInstance(savedInstanceState)
                .build();
        //set the selection to the item with the identifier 1
        //result.setSelection(1);
        //set the selection to the item with the identifier 2
        //result.setSelection(item2);
        //set the selection and also fire the `onItemClick`-listener
        //result.setSelection(1, true);

        mPermissionHelper = new PermissionHelper(this);
        boolean b = mPermissionHelper.checkPermission(WRITE_PERMISSION);
        VALogger.Debug("WRITE_PERMISSION");
        Log.d("VALogger","onCreate:checkPermission" + b);
        if(!b){
            mPermissionHelper.permissionsCheck(WRITE_PERMISSION,CODE_FOR_WRITE_PERMISSION);
        }else{
            VALogger.Debug("Has granted the persission:WRITE_PERMISSION");
        }

        setupMediaView();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        if (null != userSwitchReceiver) {
            Log.d("userSwitch", "userSwitchReceiver");
            this.registerReceiver(userSwitchReceiver, intentFilter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case CODE_FOR_WRITE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //如果请求成功，则进行相应的操作，比如我们这里打开一个新的Activity
/*                    Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                    startActivity(intent);*/

                } else {
                    //如果请求失败
                    mPermissionHelper.startAppSettings();
                }
                break;
        }
    }
    private void setupMediaView() {

        AbstructProvider provider = new VideoProvider(this);
        mListVideo = provider.getList();
        videoSize = mListVideo.size();
        VALogger.Debug("videoSize",videoSize);

        mVideoListAdapter = new ListViewAdapter(this, mListVideo);
        mVideoListView = (ListView) findViewById(R.id.lv_media);
        if ("Video".equals(getSupportActionBar().getTitle())) {
            mVideoListView.setAdapter(mVideoListAdapter);
            mVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Uri uri = Uri.parse(mListVideo.get(position).getPath());
                    VALogger.Debug(mListVideo.get(position).getPath());

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //intent.setClass(this,VideoPlayer.class);
                    String mime = mListVideo.get(position).getMimeType();
                    VALogger.Debug(mime);
                    intent.setDataAndType(uri,mime);
                    // Bundle bundle = new Bundle();
                    //bundle.putSerializable("video", mListVideo.get(position));
                    //intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
            loadImages();
        }
    }

    private void loadImages() {
        final Objects data = (Objects) getLastCustomNonConfigurationInstance();
        if (data == null) {
            new LoadImagesFromSDCard().execute();
        } else {

        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        final ListView videolist = mVideoListView;
        final int count = videolist.getChildCount();
        final LoadedImage[] list = new LoadedImage[count];

        for (int i = 0; i < count; i++) {

            final ImageView v = (ImageView) videolist.getChildAt(count);
            list[i] = new LoadedImage(
                    ((BitmapDrawable) v.getDrawable()).getBitmap());
        }

        return videolist;
    }

    private BroadcastReceiver userSwitchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.USER_SWITCHED".equals(action)) {
                Log.d("userSwitch", "onReceive::USER_SWITCHED");
            } else if ("android.intent.action.MANAGED_PROFILE_ADDED".equals(action)) {
                Log.d("userSwitch", "onReceive::MANAGED_PROFILE_ADDED");
            } else {
            }
        }
    };

    private void showHelp() {
        getSupportActionBar().setTitle(R.string.drawer_item_help);
    }

    private void showSettings() {
        getSupportActionBar().setTitle(R.string.drawer_item_settings);
    }

    private void showAudio() {
        getSupportActionBar().setTitle(R.string.drawer_item_audio);

    }

    private void showVideo() {
        getSupportActionBar().setTitle(R.string.drawer_item_video);
    }

    @Override
    protected void onResume() {
        VALogger.Info("****************************");
        super.onResume();
    }

    //在该方法内监听TRIM_MEMORY_UI_HIDDEN标志，这个标志代表了UI目前进入隐藏态，应当释放UI所用到的所有资源
    //在Activity的实例变的不可见时调用，它是在APP内部Activity之间的切换时调用的。
    //所以尽管在onStop()中释放了Activity的资源比如网络连接，注销广播接收器等等，
    //但是一般不要在该方法内释放UI资源。因为这可以使用户在返回该Activity时，UI现场可以迅速恢复。
    //因为onTrimMemory()方法添加于API 14，所以可以使用onLowMemory()来兼容老版本，它大致与TRIM_MEMORY_COMPLETE标志是等价的。
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
            case R.id.action_refresh:
            case R.id.action_about:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState = result.saveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private class LoadImagesFromSDCard extends AsyncTask<Objects, LoadedImage, Objects> {

        @Override
        protected Objects doInBackground(Objects... params) {
            Bitmap bitmap = null;
            for (int i = 0; i < videoSize; i++) {
                VALogger.Debug(mListVideo.get(i).getPath());
                bitmap = getVideoThumbnail(mListVideo.get(i).getPath(), 120, 120, MediaStore.Video.Thumbnails.MINI_KIND);
                if (bitmap != null) {
                    publishProgress(new LoadedImage(bitmap));
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(LoadedImage... values) {
            super.onProgressUpdate(values);
            addImage(values);
        }
    }

    private void addImage(LoadedImage... values) {
        for (LoadedImage image : values) {
            mVideoListAdapter.addPhoto(image);
            mVideoListAdapter.notifyDataSetChanged();
        }
    }

    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
