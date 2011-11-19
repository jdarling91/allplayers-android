package com.allplayers.android;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageData
{
/*  ["thread_id",
	 "subject",
	 "last_updated",
	 "is_new" -> 0 or 1,
	 "message_count",
	 "last_message_sender",
	 "last_message_sender_uuid",
	 "last_message_body",
	 "uri"]*/
	
	private String thread_id;
	private String subject;
	private String is_new;
	
	public MessageData(String jsonResult)
	{
		try
		{
			JSONObject messageObject = new JSONObject(jsonResult);
			thread_id = messageObject.getString("thread_id");
			subject = messageObject.getString("subject");
			is_new = messageObject.getString("is_new");
		}
		catch(JSONException ex)
		{
			System.out.println(ex);
		}
	}
	
	public String getThreadID()
	{
		return thread_id;
	}
	
	public String getSubject()
	{
		return subject;
	}
	
	public String getNew()
	{
		return is_new;
	}
}