package au.com.hellopeople.hotch.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * This class is utilized to create the http connection to the server. We are
 * only creating 1 object so that the session is maintained.
 * 
 */
public class HttpConnectionManager {

	private HttpConnectionManager() {
		super();
	}

	public static HttpClient httpClient = null;

	public static HttpClient getClient() {

		if (httpClient == null) {

			HttpParams parameters = new BasicHttpParams();
			HttpConnectionParams.setStaleCheckingEnabled(parameters, false);
			HttpConnectionParams.setConnectionTimeout(parameters, 200000);
			HttpConnectionParams.setSoTimeout(parameters, 200000);

			httpClient = new DefaultHttpClient(parameters);
			ClientConnectionManager mgr = httpClient.getConnectionManager();
			HttpParams params = httpClient.getParams();
			httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(
					params,

					mgr.getSchemeRegistry()), params);
		}

		return httpClient;

	}

}
