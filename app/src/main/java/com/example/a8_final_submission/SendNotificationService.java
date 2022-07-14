package com.example.a8_final_submission;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotificationService {

    String serverKey="key=AAAASxTK1Zc:APA91bGYwZrw31HYrALDg1Z5o50EY6N4PbME-TbREE9kmTPsz-lxJ7lpVI6fo_bZUT0J5Zo0pa8c_cphzcS4W_EbueJcFUUkA4LP4NS1wuwbeeBw2PM-wxl2TqeROpFc84ClYZwdTjkI";
    String baseUrl="https://fcm.googleapis.com/fcm/send";
 //   String notificationReciverToken="fUWRBwzrTCWeRjsY9S1Aoe:APA91bGNyVlW0AHXGR6r5fOkSEva9gmFCDAALSJzoZje05xHdrriTBNdaBHvfPoZtbQ-t4Xj0fikvnEX5LR0XpUMOPq9X0_MEsLp4ir-smX9U8bM0rodj9obwcTZzNlrIHllbslzQL-d";

    public void sendNotficationImpl(Context context,String title, String body,String reciverToken) throws JSONException {

        Runnable runnable= ()->{

            try {
                JSONObject json = new JSONObject();

                json.put("to", reciverToken);
                JSONObject notification = new JSONObject();
                notification.put("title", title);
                notification.put("body", body);
                json.put("notification", notification);
                JsonObjectRequest
                        jsonObjReq
                        = new JsonObjectRequest(
                        Request.Method.POST,
                        baseUrl,
                        json,
                        (Response.Listener) response -> {
                            System.out.println("notification response:"+response);
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }

                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", serverKey);

                        return params;
                    }

                };

                Volley.newRequestQueue(context).add(jsonObjReq);

            } catch(Exception e){
                System.out.println(e);
            }
        };

        new Thread(runnable).start();



    }


}
