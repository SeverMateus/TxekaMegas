package me.severmateus.txekamegas.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import me.severmateus.txekamegas.Database.MySQLiteHelper;
import me.severmateus.txekamegas.Fragments.HomeStatsFragment;
//import me.severmateus.txekamegas.Fragments.ItemFragment;
import me.severmateus.txekamegas.R;
import me.severmateus.txekamegas.Adapters.FragmentDrawer;



public class Home extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static Home instance;

    public Home() {
        instance = this;
    }

    private static String TAG = Home.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        db = new MySQLiteHelper(this);
        List<me.severmateus.txekamegas.Model.Settings> settings = db.getAllSettings();
        if (settings.isEmpty()) {
            Intent myIntent = new Intent(this.getBaseContext(),
                    Intro.class);
            startActivityForResult(myIntent, 0);
            finish();
        } else {

            mToolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            drawerFragment = (FragmentDrawer)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            drawerFragment.setDrawerListener(this);

            // display the first navigation drawer view on app launch
            displayView(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            Intent myIntent = new
                    Intent(getBaseContext(), Settings.class);
            startActivity(myIntent);
            return true;
        }
/*
        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeStatsFragment();
                title = "Estatisticas";
                break;
/*
            case 1:
                fragment = new Settings();
                title = "Settings";
                break;
*/
//            case 2:
//                fragment = new MessagesFragment();
//                title = getString(R.string.title_messages);
//                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }


    //Other Stuff Improvisado
    public static void setMobileDataEnabled(Context context, boolean enabled)
            throws ClassNotFoundException, IllegalAccessException,
            IllegalArgumentException, NoSuchMethodException,
            InvocationTargetException, NoSuchFieldException {
        final ConnectivityManager conman = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass
                .getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField
                .get(conman);
        final Class iConnectivityManagerClass = Class
                .forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass
                .getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}