package noelfranceschi.com.ftf.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import noelfranceschi.com.ftf.activities.LoginActivity;
import noelfranceschi.com.ftf.constants.Constants;

/**
 * Created by noelfranceschi on 10/13/17.
 */

public class AuthService {

    //: Vars
    private static AuthService instance = new AuthService();

    public static AuthService getInstance() {
        return instance;
    }

    public AuthService() {

    }

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    //: Register user
    public void registerUser(String email, String password, Context context, final LoginActivity.RegisterInterface listener) {

        try {

            String url = Constants.REGISTER;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            final String mRequestBody = jsonBody.toString();

            StringRequest registerRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("Volley", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse.statusCode == 409) {
                        listener.success(true);
                    }
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee){
                        VolleyLog.wtf("Unsupported Encoding", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response.statusCode == 200 || response.statusCode == 409) {
                        listener.success(true);
                    }

                    return super.parseNetworkResponse(response);
                }
            };

            Volley.newRequestQueue(context).add(registerRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //: Login user
    public void loginUser(String email, String password, Context context, final LoginActivity.LoginInterface listener) {

        try {

            String url = Constants.LOGIN;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Volley", response.toString());

                    try {
                        JSONObject account = response;
                        authToken = account.getString("token");
                        listener.success(true);

                    } catch (JSONException e) {
                        Log.v("JSON", "EXC" + e.getLocalizedMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee){
                        VolleyLog.wtf("Unsupported Encoding", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if (response.statusCode != 200) {
                        listener.success(false);
                    }

                    return super.parseNetworkResponse(response);
                }
            };

            Volley.newRequestQueue(context).add(loginRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
