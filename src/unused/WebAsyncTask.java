/**
 * 
 */
package unused;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import com.khmelenko.lab.currency.R;

/**
 * @author Dmytro Khmelenko
 * 
 */
public class WebAsyncTask extends AsyncTask<Void, Void, Void> {

	private AsyncTaskObserver iObserver;

	/**
	 * Constructor
	 * 
	 * @param aObserver
	 *            Observer
	 */
	public WebAsyncTask(AsyncTaskObserver aObserver) {
		iObserver = aObserver;
	}

	@Override
	protected Void doInBackground(Void... aParams) {
		postData();
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Void aResult) {
		super.onPostExecute(aResult);
	}
	
    public void postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://resources.finance.ua/ua/public/currency-cash.xml");
        HttpPost httppost = new HttpPost("http://cashexchange.com.ua/XmlApi.ashx");
        httppost.setHeader("Connection", "keep-alive");
        
        try {
            // Execute HTTP Post Request
            HttpResponse response2 = httpclient.execute(httppost);
            HttpEntity entity = response2.getEntity();
            
            String resp = response2.toString();
            String xml = EntityUtils.toString(entity);
            
            
        } catch (ClientProtocolException e) {
        	iObserver.loadingFailed(1);
            // TODO Auto-generated catch block
        } catch (IOException e) {
        	iObserver.loadingFailed(2);
        	String ex = e.toString();
            // TODO Auto-generated catch block
        }
    } 


}
