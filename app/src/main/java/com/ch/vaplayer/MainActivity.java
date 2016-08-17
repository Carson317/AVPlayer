package com.ch.vaplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ch.utils.VALogger;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {


    //save our header or result
    private Drawer result = null;
    private Toolbar mToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Handle Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.drawer_item_video);
        setSupportActionBar(mToolbar);

        VALogger.Info("************");
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
                        if(drawerItem != null){
                            if(drawerItem.getIdentifier() == 1){
                                showVideo();
                            }else if(drawerItem.getIdentifier() == 2){
                                showAudio();
                            }else if(drawerItem.getIdentifier() == 3){
                                showSettings();
                            }else if(drawerItem.getIdentifier() == 4){
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
    }
    private void showHelp(){
        getSupportActionBar().setTitle(R.string.drawer_item_help);
    }

    private void showSettings(){
        getSupportActionBar().setTitle(R.string.drawer_item_settings);
    }
    private void showAudio() {
        getSupportActionBar().setTitle(R.string.drawer_item_audio);
    }

    private void showVideo() {
        getSupportActionBar().setTitle(R.string.drawer_item_video);
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
        switch (id){
            case R.id.action_search:
            case R.id.action_refresh:
            case R.id.action_about:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = result.saveInstanceState(outState);
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
}
