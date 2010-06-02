package com.androchat;

//import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.basic.HttpURLConnectionRequestAdapter;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.http.HttpRequest;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.URLConnectionHttpClient;
import winterwell.jtwitter.Twitter.IHttpClient;

public class OAuthSignpostClient extends URLConnectionHttpClient implements IHttpClient {

	@Override
	protected void setAuthentication(URLConnection connection, String name, String password) 
	{
		try {
			// sign the request
	        consumer.sign(connection);
		} catch (OAuthException e) {
			throw new TwitterException(e.getMessage());
		}
	}
	
	@Override
	public String post(String uri, Map<String, String> vars,
			boolean authenticate) throws TwitterException {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(uri).openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setReadTimeout(timeout);			
			final String payload = post2_getPayload(vars);
			if (authenticate) { 
				// needed for OAuthConsumer.collectBodyParameters() not to get upset
				HttpURLConnectionRequestAdapter wrapped = new HttpURLConnectionRequestAdapter(connection) {
					@Override
					public InputStream getMessagePayload() throws IOException {
						return new StringBufferInputStream(payload);
					}
				};
				consumer.sign(wrapped);
			}
			// add the payload
			OutputStream os = connection.getOutputStream();
			os.write(payload.getBytes());
			close(os);
			// Get the response
			//processError(connection);
			String response = toString(connection.getInputStream());
			return response;
			
		} catch (IOException e) {
			throw new TwitterException(e.getMessage());
		} catch (OAuthException e) {
			throw new TwitterException(e.getMessage());
		}
	}
	
	private String consumerSecret;
	private String consumerKey;
	private String callbackUrl;
	private OAuthConsumer consumer;
	private DefaultOAuthProvider provider;
	private String accessToken;
	private String accessTokenSecret;

	/**
	 * 
	 * @param consumerKey
	 * @param consumerSecret
	 * @param callbackUrl Servlet that will get the verifier sent to it, 
	 * or "oob" for out-of-band (user copies and pastes the pin to you)
	 */
	public OAuthSignpostClient(String consumerKey, String consumerSecret, String callbackUrl) {
		assert consumerKey != null && consumerSecret != null && callbackUrl != null;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.callbackUrl = callbackUrl;
		init();
	}
	
	/**
	 * Use this if you already have an accessToken for the user.
	 * You can then go straight to using the API without having to authorise again.
	 * @param consumerKey
	 * @param consumerSecret
	 * @param accessToken
	 */
	public OAuthSignpostClient(String consumerKey, String consumerSecret, 
			String accessToken, String accessTokenSecret) 
	{
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		init();		
	}
	
	String getName() {
		return name==null? "?user" : name;
	}
	
	/**
	 * Unlike the base class {@link URLConnectionHttpClient},
	 * this does not set name by default. But you can set it for nicer
	 * error messages.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	private void init() {
		// The default consumer can't do post requests! 
		// TODO override AbstractAuthConsumer.collectBodyParameters() which would be more efficient 
		consumer = new AbstractOAuthConsumer(consumerKey, consumerSecret) {
			@Override
			protected HttpRequest wrap(final Object request) {
				if (request instanceof HttpRequest) return (HttpRequest) request;
				return new HttpURLConnectionRequestAdapter((HttpURLConnection) request);
			}			
		};
		if (accessToken!=null) {
			consumer.setTokenWithSecret(accessToken, accessTokenSecret);
		}
        provider = new DefaultOAuthProvider(
                "http://twitter.com/oauth/request_token",
                "http://twitter.com/oauth/access_token",
                "http://twitter.com/oauth/authorize");
	}
	
	
	
	/**
	 * @return url to direct the user to for authorisation.
	 */
	public String authorizeUrl() {
		try {
			String url = provider.retrieveRequestToken(consumer, callbackUrl);
			return url;
		} catch (Exception e) {
			throw new TwitterException(e.getMessage());
		}		
	}
	

	/**
	 * Set the authorisation code (aka the verifier).
	 * This is only relevant when using out-of-band instead of a callback-url.
	 * @param verifier a pin code which Twitter gives the user
	 * @throws RuntimeException Scribe throws an exception if the verifier is invalid
	 */
	public void setAuthorizationCode(String verifier) throws TwitterException {
		try {
			provider.retrieveAccessToken(consumer, verifier);
			accessToken = consumer.getToken();
			accessTokenSecret = consumer.getTokenSecret();
		} catch (Exception e) {
			throw new TwitterException(e.getMessage());
		}
	}
	
	@Override
	public boolean canAuthenticate() {
		return consumer.getToken() != null;
	}

	public String getAccessToken() {
		return accessToken;
	}
	
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
}
