package models.cmu.sv.sensor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.libs.F.Function;
import play.mvc.*;

public class MessageBusHandler {
	private static String serverUrl = "http://message-bus-soc.herokuapp.com/";
	
	private String topic = "";
	private ArrayList<NameValuePair> metaData = null;
	private ArrayList<NameValuePair> properties = null;
	
	public MessageBusHandler(){
		this(null);
	}
	
	public MessageBusHandler(String topic){
		this.topic = topic;
		this.metaData = new ArrayList<NameValuePair>();
		this.properties = new ArrayList<NameValuePair>();
		
	}
	
	private JSONObject toJSON(List<NameValuePair> source){
		JSONObject json = new JSONObject();
		Iterator<NameValuePair> it = source.iterator();
		while(it.hasNext()){
			NameValuePair pair = it.next();
			try {
				json.append(pair.getName(), pair.getValue());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
		return json;
	}
	public boolean isTopicExists(String topic){
		String path = "catalog/topics";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(serverUrl + path);

		try {
			
			HttpResponse response = client.execute(get);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
			    builder.append(line).append("\n");
			}
			
			//parse Json file
			JSONArray jsonArray = new JSONArray(builder.toString());
			for(int i=0; i< jsonArray.length(); i++){
				//System.err.println(jsonArray.getJSONObject(i).getString("topic"));
				if(jsonArray.getJSONObject(i).getString("topic").equals(topic)){
					return true;
				}
			}
			return false; 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		
	}
	public boolean addTopic(String topic){
		String path = "catalog/topics";
		HttpClient client = new DefaultHttpClient();
		 HttpPost post = new HttpPost(serverUrl + path);
		 try {
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		      
		      JSONObject topicObject = new JSONObject();
		      topicObject.put("topic", topic);
		      
		      JSONObject jsonMeta = new JSONObject();
		      jsonMeta.put("field", "value");
		      jsonMeta.put("type", "Float");
		      JSONArray jsonMetaArray = new JSONArray();
		      jsonMetaArray.put(jsonMeta);
		      topicObject.put("metadata", jsonMetaArray);
		     
		      StringEntity entity = new StringEntity(topicObject.toString(), HTTP.UTF_8);
		      //System.err.println(topicObject.toString());
		      entity.setContentType("application/json");
		      
		      post.setEntity(entity);
		      
		     
		      HttpResponse response =client.execute(post); 
		      
		      //Reading Content
		      String output = "";
		      String line = "";
		      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		      while ((line = rd.readLine()) != null) {
		    	  output += line;
		      }
		      if(output.equals("")) return true;
		      
		      
		 }
		 catch(IOException e){
			 e.printStackTrace(); 
		 } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return false;
	}
	public void publish(){
		 HttpClient client = new DefaultHttpClient();
		 HttpPost post = new HttpPost(serverUrl);
		 try {
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		      nameValuePairs.add(new BasicNameValuePair("topic", topic));
		      nameValuePairs.add(new BasicNameValuePair("metaData", toJSON(metaData).toString()));
		      nameValuePairs.add(new BasicNameValuePair("properties", toJSON(properties).toString()));
		      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		      
		      client.execute(post);
		      /*
		      HttpResponse response =client.execute(post); 
		      
		      //Reading Content
		      String line = "";
		      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		      while ((line = rd.readLine()) != null) {
		    	  System.out.println(line);
		      }
		      */
		      
		 }
		 catch(IOException e){
			 
		 }
		 
		 
		 
	}
}
