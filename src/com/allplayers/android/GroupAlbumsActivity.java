package com.allplayers.android;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


public class GroupAlbumsActivity  extends ListActivity
{
	private ArrayList<AlbumData> albumList;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		String jsonResult = APCI_RestServices.getGroupAlbumsByGroupId(Globals.currentGroup.getUUID());
		AlbumsMap albums = new AlbumsMap(jsonResult);
		albumList = albums.getAlbumData();
		
		//Create a customized ArrayAdapter
		AlbumAdapter adapter = new AlbumAdapter(getApplicationContext(), R.layout.albumlistitem, albumList);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		
		if(!albumList.isEmpty())
		{
			Globals.currentAlbum = albumList.get(position);
			
			//Display the photos for the selected album
			Intent intent = new Intent(GroupAlbumsActivity.this, AlbumPhotosActivity.class);
			startActivity(intent);
		}
	}
}