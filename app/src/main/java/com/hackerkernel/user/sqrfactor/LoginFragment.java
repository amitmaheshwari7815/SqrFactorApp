package com.hackerkernel.user.sqrfactor;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    Button login;
    TextView forgot;
    EditText loginEmail,loginPassword;

    //static interface Listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("PREF_NAME" ,getActivity().MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        login = (Button)rootView.findViewById(R.id.login);
        forgot = (TextView)rootView.findViewById(R.id.forgot);
        loginEmail=(EditText) rootView.findViewById(R.id.loginEmail);
        loginPassword=(EditText) rootView.findViewById(R.id.loginPassword);


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 Toast.makeText(getActivity().getApplicationContext(), "Token" + loginEmail.getText()+loginPassword.getText(), Toast.LENGTH_SHORT).show();
               // if(!TextUtils.isEmpty(loginEmail.getText())&&!TextUtils.isEmpty(loginPassword.getText())) {
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/login",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.v("Reponse", response);
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        JSONObject TokenObject= jsonObject.getJSONObject("success");
                                        String Token = TokenObject.getString("token");
                                        Log.v("token", Token);
                                        //Toast.makeText(getActivity().getApplicationContext(), "Token" + Token, Toast.LENGTH_SHORT).show();
                                        //SharedPreferences sharedPref = getActivity().getSharedPreferences("PREF_NAME" ,MODE_PRIVATE);
                                        //SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("TOKEN",Token);
                                        editor.commit();

//                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF_NAME",getActivity().MODE_PRIVATE);
//                                        String token = sharedPreferences.getString("TOKEN","sqr");
//                                        Log.v("Token0",token);
                                        Intent i = new Intent(getActivity(), HomeScreen.class);
                                        startActivity(i);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                   // Toast.makeText(getActivity().getApplicationContext(), "Token" + response.toString(), Toast.LENGTH_SHORT).show();

                                    //interval=parseInterval(response);
                                    // Log.v("Interval",interval+"");
                                    //callback.onSuccess(interval);
                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Accept", "application/json");
                            return params;
                        }
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email",loginEmail.getText().toString());
                            params.put("password", loginPassword.getText().toString());
                            return params;
                        }
                    };
//                    VolleySingletonClass.getInstance(getContext()).addToRequestQue(myReq);
                requestQueue.add(myReq);
//                }
//
//                else {
//                    Toast.makeText(getContext(),"Email or passwprd Empty",Toast.LENGTH_LONG).show();
//                }



            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext().getApplicationContext(), ResetPassword.class);
                startActivity(i);

            }
        });

        return rootView;

    }

}