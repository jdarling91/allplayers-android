package com.allplayers.android;

import com.allplayers.rest.RestApiV1;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

public class MainScreen extends TabActivity {
    private Context context;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inapplayout);

        context = this.getBaseContext();

        Resources res = getResources(); // Resource object to get Drawables, this will be little icons for each one
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, GroupsActivity.class); //set as GroupsActivity.class, this will be changed to
        //whatever we end up calling that particular class

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("groups").setIndicator("Groups",
                res.getDrawable(R.drawable.ic_tab_groups)).setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, MessageActivity.class);
        spec = tabHost.newTabSpec("messages").setIndicator("Messages",
                res.getDrawable(R.drawable.ic_tab_messages)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PhotosActivity.class);
        spec = tabHost.newTabSpec("photos").setIndicator("Photos",
                res.getDrawable(R.drawable.ic_tab_photos)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, EventsActivity.class);
        spec = tabHost.newTabSpec("events").setIndicator("Events",
                res.getDrawable(R.drawable.ic_tab_events)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logoutmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
		if (itemId == R.id.logOut) {
			RestApiV1.logOut();
			LocalStorage.writePassword(context, "");
			startActivity(new Intent(MainScreen.this, Login.class));
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
    }
}