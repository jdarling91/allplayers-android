package com.allplayers.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.allplayers.android.activities.AllplayersSherlockListActivity;
import com.allplayers.objects.GroupMemberData;
import com.allplayers.rest.RestApiV1;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.google.gson.Gson;

/**
 * Interface to allow a user to select user recipients for a message.
 *
 */
public class SelectFriendContacts extends AllplayersSherlockListActivity {

    private ArrayAdapter<GroupMemberData> mAdapter;
    private ArrayList<GroupMemberData> mMembersList;
    private ArrayList<GroupMemberData> mSelectedMembers;
    private Button mLoadMoreButton;
    private ListView mListView;
    private ProgressBar mLoadingIndicator;
    private ViewGroup mFooter;

    private final int LIMIT = 15;
    private boolean mEndOfData = false;
    private int mOffset = 0;

    /**
     * Called when the activity is starting. Handles variable initialization, and sets up the
     * interface.
     * @param savedInstanceState: If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied in
     * onSaveInstanceState(Bundle). Otherwise it is null.
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the page UI
        setContentView(R.layout.selectusercontacts);

        mActionBar.setTitle("Compose Message");
        mActionBar.setSubtitle("Select Individual Recipients");

        mSideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        mSideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        mSideNavigationView.setMenuClickCallback(this);
        mSideNavigationView.setMode(Mode.LEFT);

        // Variable initialization.
        mMembersList = new ArrayList<GroupMemberData>();
        mSelectedMembers = new ArrayList<GroupMemberData>();

        // Get a handle on the ListView.
        mListView = getListView();

        // Create our adapter for the ListView.
        mAdapter = new ArrayAdapter<GroupMemberData>(this, android.R.layout.simple_list_item_multiple_choice, mMembersList);

        // Inflate and get a handle on our loading button and indicator.
        mFooter = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.load_more, null);
        mLoadMoreButton = (Button) mFooter.findViewById(R.id.load_more_button);
        mLoadingIndicator = (ProgressBar) mFooter.findViewById(R.id.loading_indicator);

        // When the load more button is clicked, show the loading indicator and load more
        // group members.
        mLoadMoreButton.setOnClickListener(new OnClickListener() {

            /**
             * Called when the button is clicked.
             * @param v: The view that was clicked.
             *
             */
            @Override
            public void onClick(View v) {
                mLoadMoreButton.setVisibility(View.GONE);
                mLoadingIndicator.setVisibility(View.VISIBLE);
                new GetUserGroupmatesTask().execute();
            }
        });

        // Add our loading button and indicator to the ListView.
        mListView.addFooterView(mFooter);

        // Set our ListView adapter.
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        setListAdapter(mAdapter);

        final Button doneButton = (Button)findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when the button is clicked.
             * @param v: The view that was clicked.
             *
             */
            @Override
            public void onClick(View v) {
                mLoadMoreButton.setVisibility(View.INVISIBLE);
                Gson gson = new Gson();
                String userData = gson.toJson(mSelectedMembers);
                Intent intent = new Intent();
                
                Log.d("User Data", userData);
                
                intent.putExtra("userData", userData);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


        // Get the first 15 groupmates.
        new GetUserGroupmatesTask().execute();
    }

    /**
     * This method will be called when an item in the list is selected.
     * @param l: The ListView where the click happened.
     * @param v: The view that was clicked within the ListView.
     * @param position: The position of the view in the list.
     * @param id: The row id of the item that was clicked.
     *
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (!mSelectedMembers.contains(mMembersList.get(position))) {
            mSelectedMembers.add(mMembersList.get(position));
        } else {
            mSelectedMembers.remove(mMembersList.get(position));
        }
    }

    /**
     * Gets a group's members.
     *
     */
    public class GetUserGroupmatesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... args) {
            return RestApiV1.getUserFriends(mOffset, LIMIT);
        }

        protected void onPostExecute(String jsonResult) {
            Log.d("jsonResult Before Replace", jsonResult);
            jsonResult = jsonResult.replaceAll("firstname", "fname");
            jsonResult = jsonResult.replaceAll("lastname", "lname");
            Log.d("jsonResult After Replace", jsonResult);
            GroupMembersMap groupMembers = new GroupMembersMap(jsonResult);

            if (groupMembers.size() == 0) {
                // If the newly pulled group members is empty, indicate the end of data.
                mEndOfData = true;
                // If the members list is also empty, there are no group members, so add
                // a blank indicator showing so.
                if (mMembersList.size() == 0) {
                    GroupMemberData blank = new GroupMemberData();
                    blank.setName("No members to display");
                    mMembersList.add(blank);
                    mAdapter.notifyDataSetChanged();
                    mListView.setEnabled(false);
                }
            } else {
                // If we pulled less than 10 new members, indicate we are at the end of data.
                if (groupMembers.size() < LIMIT) {
                    mEndOfData = true;
                }

                // Add all the new members to our list and update our ListView.
                mMembersList.addAll(groupMembers.getGroupMemberData());
                mAdapter.notifyDataSetChanged();
            }
            // If we are not at the end of data, show our load more button and increase our offset.
            if (!mEndOfData) {
                mLoadMoreButton.setVisibility(View.VISIBLE);
                mLoadingIndicator.setVisibility(View.GONE);
                mOffset += groupMembers.size();
            } else { // If we are at the end of data, remove the load more button.
                mListView.removeFooterView(mFooter);
            }
        }
    }
}