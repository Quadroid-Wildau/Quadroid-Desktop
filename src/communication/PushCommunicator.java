package communication;

import java.util.ArrayList;
import java.util.List;

import model.AdvLandmark;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

import de.th_wildau.quadroid.models.GNSS;

/**
 * 
 * This class represents the communication between the desktop app and the Quadroid server.
 * 
 * @author Georg Baumgarten
 *
 */
public class PushCommunicator {
	
	//for the moment, credentials are hard coded here
	private static final String EMAIL = "admin@quadroid.com";
	private static final String PASSWORD = "timetosaygoodbye2014";
	private static final String CLIENT_ID = "364330b6fb1a04f9dcb636bbb929d10d78b223f849deca561e3ae2f978fd508d";
	private static final String CLIENT_SECRET = "53c51cea3ba9ba8ff9206185e2d90fdc954da8736d4966c3b124c010382d4fcb";
	
	private static final String BASE_URL = "http://quadroid.dev.wonderweblabs.com/";
	private static final String TOKEN_URL = BASE_URL + "oauth/token";
	private static final String LANDMARK_URL = BASE_URL + "landmark_alerts";
	
	private volatile String ACCESS_TOKEN = "";
	private volatile List<AdvLandmark> mLandmarkRequestQueue = new ArrayList<AdvLandmark>();
	private volatile boolean attemptingLogin = false;
	
	public PushCommunicator() {
		Unirest.setDefaultHeader("Accept", "application/vnd.quadroid-server-v1+json");
		login();
	}
	
	/**
	 * This method will start a login attempt on the server
	 */
	public void login() {
		getLoginTokenFromServer();
	}
	
	/**
	 * This method will upload a landmark alarm to the server
	 * @param landmark
	 */
	public synchronized void pushLandmarkAlarm(AdvLandmark landmark) {
		if (ACCESS_TOKEN.isEmpty()) {
			//login first
			if (!mLandmarkRequestQueue.contains(landmark)) {
				mLandmarkRequestQueue.add(landmark);
			}
			getLoginTokenFromServer();
		} else {
			//already logged in
			uploadLandmarkAlarm(landmark);
		}
	}
	
//*********************************************************************************************************
//	Helpers
//*********************************************************************************************************
	
	private void getLoginTokenFromServer() {
		if (attemptingLogin)
			return;
		
		Unirest.post(TOKEN_URL)
		.field("grant_type", "password")
		.field("email", EMAIL)
		.field("password", PASSWORD)
		.field("client_id", CLIENT_ID)
		.field("client_secret", CLIENT_SECRET)
		.asJsonAsync(new Callback<JsonNode>() {
			
			@Override
			public void failed(UnirestException e) {
				e.printStackTrace();
				attemptingLogin = false;
			}
			
			@Override
			public void completed(HttpResponse<JsonNode> response) {
				switch (response.getCode()) {
				case 200:
					JSONObject object = response.getBody().getObject();
					try {
						if (object.has("access_token")) {
							ACCESS_TOKEN = object.getString("access_token");
							
							attemptingLogin = false;
							
							//if there are queued landmark push requests, handle them
							if (!ACCESS_TOKEN.isEmpty() && mLandmarkRequestQueue != null) {
								System.out.println("Got access token: " + ACCESS_TOKEN);
								for (AdvLandmark lm : mLandmarkRequestQueue) {
									pushLandmarkAlarm(lm);
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					JSONObject errorObject = response.getBody().getObject();
					System.err.println(errorObject.toString());
					break;
				}
			}
			
			@Override
			public void cancelled() {
				attemptingLogin = false;
			}
		});
	}
	
	private void uploadLandmarkAlarm(final AdvLandmark landmark) {
		GNSS geoData = landmark.getMetaData().getAirplane().GeoData();
		try {
			HttpResponse<JsonNode> response = Unirest.post(LANDMARK_URL)
			.header("Authorization", "Bearer " + ACCESS_TOKEN)
			.field("landmark_alert[latitude]", String.valueOf(geoData.getLatitude()))
			.field("landmark_alert[longitude]", String.valueOf(geoData.getLongitude()))
			.field("landmark_alert[detection_date]", String.valueOf(landmark.getMetaData().getAirplane().getTime()))
			.field("landmark_alert[height]", String.valueOf(geoData.getHeight()))
			.field("landmark_alert[image]", landmark.getLandmarkPictureAsFile())
			.asJson();
			
			System.out.println("Response: " + response.getBody().getObject().toString());
		} catch (UnirestException e) {
			e.printStackTrace();
		} finally {
			landmark.getLandmarkPictureAsFile().delete();
		}
	}
}
