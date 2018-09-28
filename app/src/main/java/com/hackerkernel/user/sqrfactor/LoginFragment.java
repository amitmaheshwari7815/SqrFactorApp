
package com.hackerkernel.user.sqrfactor;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hackerkernel.user.sqrfactor.HomeScreen;
import com.hackerkernel.user.sqrfactor.UserClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.RECEIVER_VISIBLE_TO_INSTANT_APPS;
import static com.android.volley.VolleyLog.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String username, password;
    private Button login;
    private TextView forgot,info;
    private EditText loginEmail, loginPassword;
    private CheckBox loginRemberMe;
    private SharedPreferences.Editor editor;
    private SharedPreferences mPrefs;
    private  FirebaseDatabase database;
    private  DatabaseReference ref;
    private LoginButton facebookLoagin;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton googleLogin;
    private AccessToken accessToken;
    private SharedPreferences sp;
    private GoogleSignInClient mGoogleSignInClient;
    private Button fb_Button,Google_Button;

    private Context context;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);



        SharedPreferences sharedPref = getActivity().getSharedPreferences("PREF_NAME", getActivity().MODE_PRIVATE);
        editor = sharedPref.edit();

        mPrefs = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        database= FirebaseDatabase.getInstance();

        ref = database.getReference();
        sp = getActivity().getSharedPreferences("login",MODE_PRIVATE);

        login = (Button) rootView.findViewById(R.id.login);
        forgot = (TextView) rootView.findViewById(R.id.forgot);
        loginEmail = (EditText) rootView.findViewById(R.id.loginEmail);
        loginPassword = (EditText) rootView.findViewById(R.id.loginPassword);
        loginRemberMe = (CheckBox) rootView.findViewById(R.id.rememberMeLoginCheckBox);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            loginEmail.setText(loginPreferences.getString("username", ""));
            loginPassword.setText(loginPreferences.getString("password", ""));
            loginRemberMe.setChecked(true);
        }


        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "Token" + loginEmail.getText() + loginPassword.getText(), Toast.LENGTH_SHORT).show();
                // if(!TextUtils.isEmpty(loginEmail.getText())&&!TextUtils.isEmpty(loginPassword.getText())) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(loginEmail.getWindowToken(), 0);

                username = loginEmail.getText().toString();
                password = loginPassword.getText().toString();

                sp.edit().putBoolean("logged",true).apply();

                if (loginRemberMe.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                    doSomethingElse();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                    doSomethingElse();

                }

            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), ResetPassword.class);
                startActivity(i);

            }
        });
        fb_Button = (Button) rootView.findViewById(R.id.fb_login);
        fb_Button.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (v == fb_Button) {
                                                 facebookLoagin.performClick();
                                             }
                                         }
                                     });
        facebookLoagin = (LoginButton) rootView.findViewById(R.id.facebook_login);
        facebookLoagin.setReadPermissions(Arrays.asList(new String[]{"public_profile","email", "user_birthday"}));
        facebookLoagin.setFragment(this);
        facebookLoagin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                getProfileData();

            }

            @Override
            public void onCancel() {
//                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
//                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.hackerkernel.user.sqrfactor",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash:" + Base64.encodeToString(md.digest(),
                        Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage((FragmentActivity) getActivity(),this).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        googleLogin = rootView.findViewById(R.id.google_login);
        googleLogin.setSize(SignInButton.SIZE_STANDARD);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        signIn();
            }

        });

              return rootView;


        }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage((FragmentActivity) getActivity());
        mGoogleApiClient.disconnect();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            final String gmail_email = account.getEmail();
//            String profile_url = account.getPhotoUrl().toString();

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/sociallogin",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("Reponse", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                UserClass userClass = new UserClass(jsonObject);
                                // notification listner for like and comment
                                FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications" + userClass.getUserId());
                                FirebaseMessaging.getInstance().subscribeToTopic("chats"+userClass.getUserId());
                                //code for user status
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date();

                                IsOnline isOnline=new IsOnline("True",formatter.format(date));
                                ref.child("Status").child(userClass.getUserId()+"").child("android").setValue(isOnline);
                                IsOnline isOnline1=new IsOnline("False",formatter.format(date));

                                ref.child("Status").child(userClass.getUserId()+"").child("web").setValue(isOnline1);

                                JSONObject TokenObject = jsonObject.getJSONObject("success");
                                String Token = TokenObject.getString("token");

                                editor.putString("TOKEN", Token);
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(userClass);
                                prefsEditor.putString("MyObject", json);
                                prefsEditor.commit();
                                editor.commit();
                                Intent i = new Intent(getActivity(), HomeScreen.class);
                                getActivity().startActivity(i);
                                getActivity().finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    params.put("email", gmail_email);
                    params.put("service", "google");
                    return params;
                }

            };
            requestQueue.add(myReq);
        }

    }
    public void getProfileData() {
        try {
            accessToken = AccessToken.getCurrentAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.d(TAG, "Graph Object :" + object);
                            try {
                                String name = object.getString("name");
                                final String email = object.getString("email");
                                String profileID = object.getString("id");
                                JSONObject picture = object.getJSONObject("picture");
                                JSONObject data = picture.getJSONObject("data");
                                String url = data.getString("url");

                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/sociallogin",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.v("Reponse", response);
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    UserClass userClass = new UserClass(jsonObject);
                                                    // notification listner for like and comment
                                                    FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications" + userClass.getUserId());
                                                    FirebaseMessaging.getInstance().subscribeToTopic("chats"+userClass.getUserId());
                                                    //code for user status
                                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    Date date = new Date();

                                                    IsOnline isOnline=new IsOnline("True",formatter.format(date));
                                                    ref.child("Status").child(userClass.getUserId()+"").child("android").setValue(isOnline);
                                                    IsOnline isOnline1=new IsOnline("False",formatter.format(date));

                                                    ref.child("Status").child(userClass.getUserId()+"").child("web").setValue(isOnline1);

                                                    JSONObject TokenObject = jsonObject.getJSONObject("success");
                                                    String Token = TokenObject.getString("token");

                                                    editor.putString("TOKEN", Token);
                                                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(userClass);
                                                    prefsEditor.putString("MyObject", json);
                                                    prefsEditor.commit();
                                                    editor.commit();
                                                    Intent i = new Intent(getActivity(), HomeScreen.class);
                                                    getActivity().startActivity(i);
                                                    getActivity().finish();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
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
                                        params.put("email", email);
                                        params.put("service", "facebook");
                                        return params;
                                    }

                                };
                                requestQueue.add(myReq);

                                info.setText("Welcome ," + name);

                                Log.d(TAG, "Name :" + name);
                                Log.d(TAG,"Email"+email);
                                Log.d(TAG,"ProfileID"+profileID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,birthday,gender,email,picture");
            request.setParameters(parameters);
            request.executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doSomethingElse() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Reponse", response);
//                        MDToast.makeText(getApplicationContext(), "Check Your UserName/password field", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            UserClass userClass = new UserClass(jsonObject);
                            // notification listner for like and comment
                            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications" + userClass.getUserId());
                            FirebaseMessaging.getInstance().subscribeToTopic("chats"+userClass.getUserId());
                            //code for user status
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = new Date();

                            IsOnline isOnline=new IsOnline("True",formatter.format(date));
                            ref.child("Status").child(userClass.getUserId()+"").child("android").setValue(isOnline);
                            IsOnline isOnline1=new IsOnline("False",formatter.format(date));

                            ref.child("Status").child(userClass.getUserId()+"").child("web").setValue(isOnline1);

                            JSONObject TokenObject = jsonObject.getJSONObject("success");
                            String Token = TokenObject.getString("token");

                            editor.putString("TOKEN", Token);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(userClass);
                            prefsEditor.putString("MyObject", json);
                            prefsEditor.commit();
                            editor.commit();
                            Intent i = new Intent(getActivity(), HomeScreen.class);
                            getActivity().startActivity(i);
                            getActivity().finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("email", loginEmail.getText().toString());
                params.put("password", loginPassword.getText().toString());
                return params;
            }
        };
        requestQueue.add(myReq);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
        context = getApplicationContext();
    }
}