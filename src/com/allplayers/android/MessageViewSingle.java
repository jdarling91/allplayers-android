package com.allplayers.android;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.allplayers.objects.MessageData;
import com.allplayers.objects.MessageThreadData;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageViewSingle extends SherlockActivity implements ISideNavigationCallback {

    private SideNavigationView sideNavigationView;

    /** called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewsinglemessage);

        Router router = new Router(this);
        MessageThreadData messageThreadList = router.getIntentMessageThread();
        final MessageData message = router.getIntentMessage();

        String subject = messageThreadList.getSubject();
        String sender = messageThreadList.getSenderName();
        String date = messageThreadList.getDateString();
        String body = messageThreadList.getMessageBody();

        final TextView subjectText = (TextView)findViewById(R.id.subjectText);
        subjectText.setText("This is the Subject.");
        subjectText.setText(subject);

        final TextView senderText = (TextView)findViewById(R.id.senderText);
        senderText.setText("This is the Sender's Name.");
        senderText.setText("From: " + sender);

        final TextView dateText = (TextView)findViewById(R.id.dateText);
        dateText.setText("This is the Date last sent.");
        dateText.setText("" + date);

        final TextView bodyText = (TextView)findViewById(R.id.bodyText);
        bodyText.setText("This is the body text.");
        bodyText.setText(body);
        bodyText.setMovementMethod(LinkMovementMethod.getInstance());

        final Button replyButton = (Button)findViewById(R.id.replyButton);

        replyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = (new Router(MessageViewSingle.this)).getMessagReplyIntent(message);
                startActivity(intent);
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setIcon(R.drawable.menu_icon);
        actionbar.setTitle("Messages");
        actionbar.setSubtitle("Message From: " + messageThreadList.getSenderName());

        sideNavigationView = (SideNavigationView)findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);
    }

    /**
     * Creates the Action Bar Options Menu.
     * @param menu: The menu to be created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.defaultmenu, menu);

        return true;
    }

    /**
     * Listener for the Action Bar Options Menu.
     * @param item: The selected menu item.
     * TODO: Add options for:
     *          Logout
     *          Search
     *          Refresh
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

        case android.R.id.home:
            sideNavigationView.toggleMenu();

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Listener for the Side Navigation Menu.
     * @param itemId: The ID of the list item that was selected.
     */
    @Override
    public void onSideNavigationItemClick(int itemId) {

        switch (itemId) {

        case R.id.side_navigation_menu_item1:
            invokeActivity(GroupsActivity.class);
            break;

        case R.id.side_navigation_menu_item2:
            invokeActivity(MessageActivity.class);
            break;

        case R.id.side_navigation_menu_item3:
            invokeActivity(PhotosActivity.class);
            break;

        case R.id.side_navigation_menu_item4:
            invokeActivity(EventsActivity.class);
            break;

        default:
            return;
        }

        finish();
    }

    /**
     * Helper method for onSideNavigationItemClick. Starts the passed in
     * activity.
     * @param activity: The activity to be started.
     */
    private void invokeActivity(Class activity) {

        Intent intent = new Intent(this, activity);
        startActivity(intent);

        overridePendingTransition(0, 0); // Disables new activity animation.
    }
}