package set.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class WebConnection
{
    String error="";
    interface ApiURLs{
    	final String domain= "http://localhost/";
    	final String api_auth="api/auth";
    	final String api_plist="api/plist";
    }
	public WebConnection()
    {
        // connect to the web server
    }
    /**
     * Returns the player's name if authentication verified, null if fails.
     * @param fid
     * @param key
     * @return player's name.
     */
    public synchronized String authenticate(String fid, String key, int gid){
        //FIXME: important!!!! implement gid verification
        
    	List<NameValuePair> data = new ArrayList<NameValuePair>();
	    data.add(new BasicNameValuePair("fid", fid));
	    data.add(new BasicNameValuePair("key", key));
        data.add(new BasicNameValuePair("gid", String.valueOf(gid)));
	    error="";
	    String response=doPost(ApiURLs.api_auth,null,data);
	    int n=response.indexOf(",");//Facebook does not allow "," in names. OK Response: OK,name
	    if(error!="" && ((n!=2 || response.length()<4 || response.subSequence(0,2)!="OK")))
	    	error+="Authentication Error: "+" "+response+" "+n+ " "+response.subSequence(0,2);
	    if(error!=""){
	    	System.out.println(error);
	    	return null;
	    }else
	    	return response.substring(3);
    }
	public String doPost(String url,HttpEntity data,List<NameValuePair> form_data){
        HttpPost post = new HttpPost(ApiURLs.domain+url);
        try {
        	if(form_data!=null){
        		post.setEntity(new UrlEncodedFormEntity(form_data));
        	}else{
        		if(data!=null)
        			post.setEntity(data);
        	}
		} catch (UnsupportedEncodingException e1) {
			return null;
		}
        HttpResponse resp = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            resp = httpClient.execute(post);
        } catch (ClientProtocolException e) {
            error=e.getMessage();
        } catch (IOException e) {
            error=e.getMessage();
        }
        if(resp==null)
        	return null;
        else{
        	try {
            	StringBuilder result=new StringBuilder();
            	BufferedReader reader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            	String line = null;
            	while ((line = reader.readLine()) != null){
            	  result.append(line);
            	}
				return result.toString();
			} catch (IllegalStateException e) {
	            error=e.getMessage();
			} catch (IOException e) {
	            error=e.getMessage();
			}
			return null;
        }
	}
}
