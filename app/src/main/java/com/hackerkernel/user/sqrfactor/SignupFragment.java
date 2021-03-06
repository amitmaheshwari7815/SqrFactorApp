package com.hackerkernel.user.sqrfactor;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class SignupFragment extends Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private Spinner userTypeSpinner, userCountrySpinner;
    private String country_val=null;
    private ArrayList<CountryClass> countryClassArrayList=new ArrayList<>();
    private ArrayList<String> countryName=new ArrayList<>();
    private int countryId=1;
    private EditText firstName, lastName, companyName_text, organizationName_text, collegeName_text, userName, userEmail, userPassword, confirmPassword, userMobileNumber;
    private TextInputLayout first_name, last_name, company_name, organization_name, college_name;
    private Button completeRegistration;
    private String userType;
    private static String redirect_url;
    private SharedPreferences.Editor editor;
    private SharedPreferences mPrefs;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private boolean isVisible = true;
    private LoginButton facebookSignup;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 8001;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton googleSignup;
    private AccessToken accessToken;
    private GoogleSignInClient mGoogleSignInClient;
    private Button fb_signup,google_signup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        callbackManager = CallbackManager.Factory.create();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_signup, container, false);

        company_name = rootView.findViewById(R.id.architecture_company_name);
        college_name = rootView.findViewById(R.id.architecture_college_name);
        organization_name = rootView.findViewById(R.id.architecture_organizations_name);
        companyName_text = (EditText) rootView.findViewById(R.id.architecture_company_name_text);
        organizationName_text = (EditText) rootView.findViewById(R.id.architecture_organizations_name_text);
        collegeName_text = (EditText) rootView.findViewById(R.id.architecture_college_name_text);

        first_name = rootView.findViewById(R.id.registerFristName);
        last_name = rootView.findViewById(R.id.registerLastName);
        firstName = (EditText) rootView.findViewById(R.id.registerFristName_text);
        lastName = (EditText) rootView.findViewById(R.id.registerLastName_text);
        userName = (EditText) rootView.findViewById(R.id.registerUserName);
        userEmail = (EditText) rootView.findViewById(R.id.registerEmail);
        userPassword = (EditText) rootView.findViewById(R.id.registerUserPassword);
        userMobileNumber = (EditText) rootView.findViewById(R.id.registerUserMobileNumber);
        completeRegistration = (Button) rootView.findViewById(R.id.registerCompleteButton);
        confirmPassword = (EditText) rootView.findViewById(R.id.registerUserConfirmPassword);

        userTypeSpinner = (Spinner) rootView.findViewById(R.id.userType);
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = parent.getItemAtPosition(position).toString(); //this is your selected item
                if (userType.equals("Individual")) {

                    userType = "work_individual";
                    company_name.setVisibility(View.GONE);
                    college_name.setVisibility(View.GONE);
                    organization_name.setVisibility(View.GONE);
                    first_name.setVisibility(View.VISIBLE);
                    last_name.setVisibility(View.VISIBLE);
                    Log.v("userType2", userType);


                } else if (userType.equals("Firm/Companies (Design Service Providers)")) {
                    Toast.makeText(getActivity(), userType, Toast.LENGTH_SHORT).show();
                    userType = "work_architecture_firm_companies";
                    company_name.setVisibility(View.VISIBLE);
                    first_name.setVisibility(View.GONE);
                    last_name.setVisibility(View.GONE);
                    college_name.setVisibility(View.GONE);
                    organization_name.setVisibility(View.GONE);
                    Log.v("userType3", userType);
                } else if (userType.equals("Organisations, Companies, NGOs, Media")) {
                    Toast.makeText(getActivity(), userType, Toast.LENGTH_SHORT).show();
                    userType = "work_architecture_firm_organization";
                    organization_name.setVisibility(View.VISIBLE);
                    first_name.setVisibility(View.GONE);
                    last_name.setVisibility(View.GONE);
                    company_name.setVisibility(View.GONE);
                    college_name.setVisibility(View.GONE);
                    Log.v("userType4", userType);
                } else if (userType.equals("College/University")) {
                    Toast.makeText(getActivity(), userType, Toast.LENGTH_SHORT).show();
                    userType = "work_architecture_firm_college";
                    college_name.setVisibility(View.VISIBLE);
                    first_name.setVisibility(View.GONE);
                    last_name.setVisibility(View.GONE);
                    company_name.setVisibility(View.GONE);
                    organization_name.setVisibility(View.GONE);
                    Log.v("userType5", userType);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fb_signup = rootView.findViewById(R.id.fb_signup);
        fb_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == fb_signup){
                    facebookSignup.performClick();
                }
            }
        });
        facebookSignup = rootView.findViewById(R.id.facebook_signup);
        facebookSignup.setReadPermissions(Arrays.asList(new String[]{"public_profile", "email", "user_birthday"}));
        facebookSignup.setFragment(this);
        facebookSignup.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                socialRegisteration();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity(), "Login error", Toast.LENGTH_SHORT).show();
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

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage((FragmentActivity) getActivity(), this).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        googleSignup = rootView.findViewById(R.id.google_signup);
        googleSignup.setSize(SignInButton.SIZE_STANDARD);
        googleSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }

        });

        //Spinner spinner = (Spinner)rootView.findViewById(R.id.userType);
        // String text = spinner.getSelectedItem().toString();


        SharedPreferences sharedPref = getActivity().getSharedPreferences("PREF_NAME", getActivity().MODE_PRIVATE);
        editor = sharedPref.edit();
        mPrefs = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        userCountrySpinner = (Spinner) rootView.findViewById(R.id.registerUserCountry);
        LoadCountryFromServer();
        userCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,

                                       int position, long id) {

                if (countryName.size() > 0) {
                    //country_val = countryName.get(position);
                    country_val = position + "";
//                    if(!userData.getCountry_id().equals("null"))
//                        countrySpinner.setSelection(Integer.parseInt(userData.getCountry_id()));
                    // Toast.makeText(getApplicationContext(),countryClassArrayList.get(position).getId()+" ",Toast.LENGTH_LONG).show();

                    countryId = countryClassArrayList.get(position).getId();
                }


            }

            @Override

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });



        completeRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/register",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v("Reponse", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    UserClass userClass = new UserClass(jsonObject);
                                    // notification listner for like and comment
                                    FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications" + userClass.getUserId());
                                    FirebaseMessaging.getInstance().subscribeToTopic("chats" + userClass.getUserId());
                                    //code for user status
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = new Date();

                                    IsOnline isOnline = new IsOnline("True", formatter.format(date));
                                    ref.child("Status").child(userClass.getUserId() + "").child("android").setValue(isOnline);
                                    IsOnline isOnline1 = new IsOnline("False", formatter.format(date));

                                    ref.child("Status").child(userClass.getUserId() + "").child("web").setValue(isOnline1);

                                    JSONObject TokenObject = jsonObject.getJSONObject("success");
                                    String Token = TokenObject.getString("token");

                                    editor.putString("TOKEN", Token);
                                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(userClass);
                                    prefsEditor.putString("MyObject", json);
                                    prefsEditor.commit();
                                    editor.commit();
                                    Intent i = new Intent(getActivity(), OTP_Verify.class);
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
                        params.put("user_type", userType);
                        if (firstName.getText() != null)
                            params.put("first_name", firstName.getText().toString());
                        if (lastName.getText() != null)
                            params.put("last_name", lastName.getText().toString());
                        params.put("name", companyName_text.getText().toString() + organizationName_text.getText().toString() + companyName_text.getText().toString());
                        params.put("user_name", userName.getText().toString());
                        params.put("email", userEmail.getText().toString());
                        params.put("country_code", country_val);
                        params.put("mobile_number", userMobileNumber.getText().toString());
                        params.put("password", userPassword.getText().toString());
                        params.put("password_confirmation", confirmPassword.getText().toString());
                        return params;
                    }


                };

                requestQueue.add(myReq);

            }
        });
        return rootView;
    }
    public void LoadCountryFromServer() {
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://archsqr.in/api/public_country",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.v("ResponseLike", s);
//                        Toast.makeText(getApplicationContext(), "res" + s, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray countries = jsonObject.getJSONArray("countries");
                            countryName.clear();
                            countryClassArrayList.clear();
                            for (int i = 0; i < countries.length(); i++) {
                                CountryClass countryClass = new CountryClass(countries.getJSONObject(i));
                                countryClassArrayList.add(countryClass);
                                countryName.add(countryClass.getName());

                            }


                            ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, countryName);
                            userCountrySpinner.setAdapter(spin_adapter1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");


                return params;
            }

        };

        //Adding request to the queue
        requestQueue1.add(stringRequest);

    }

    public void socialRegisteration() {
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
                                final String name = object.getString("name");
                                final String email = object.getString("email");
                                final String profileID = object.getString("id");
                                JSONObject picture = object.getJSONObject("picture");
                                JSONObject data = picture.getJSONObject("data");
                                String url = data.getString("url");

                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/social_registration",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.v("Reponse", response);
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    UserClass userClass = new UserClass(jsonObject);
                                                    // notification listner for like and comment
                                                    FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications" + userClass.getUserId());
                                                    FirebaseMessaging.getInstance().subscribeToTopic("chats" + userClass.getUserId());
                                                    //code for user status
                                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    Date date = new Date();

                                                    IsOnline isOnline = new IsOnline("True", formatter.format(date));
                                                    ref.child("Status").child(userClass.getUserId() + "").child("android").setValue(isOnline);
                                                    IsOnline isOnline1 = new IsOnline("False", formatter.format(date));

                                                    ref.child("Status").child(userClass.getUserId() + "").child("web").setValue(isOnline1);

                                                    JSONObject TokenObject = jsonObject.getJSONObject("success");
                                                    String Token = TokenObject.getString("token");

                                                    editor.putString("TOKEN", Token);
                                                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                                    Gson gson = new Gson();
                                                    String json = gson.toJson(userClass);
                                                    prefsEditor.putString("MyObject", json);
                                                    prefsEditor.commit();
                                                    editor.commit();
                                                    Intent i = new Intent(getActivity(), SocialFormActivity.class);
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
                                        params.put("social_id",profileID);
                                        params.put("email", email);
                                        params.put("fullname",name);
                                        params.put("profile_pic","");
                                        params.put("service", "facebook");
                                        return params;
                                    }

                                };
                                requestQueue.add(myReq);

                                Log.d(TAG, "Name :" + name);
                                Log.d(TAG, "Email" + email);
                                Log.d(TAG, "ProfileID" + profileID);
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

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            final String name = account.getDisplayName();
            final String gmail_email = account.getEmail();
            final String id = account.getId();
            final String profile = String.valueOf(account.getPhotoUrl());


            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/social_registration",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("Reponse", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject user = jsonObject.getJSONObject("user");
                                JSONObject activationToken = user.getJSONObject("activation_token");
                                JSONObject userDeatil = activationToken.getJSONObject("user");
                                UserClass userClass = new UserClass(userDeatil.getString("user_name"),userDeatil.getString("first_name"),userDeatil.getString("last_name"),userDeatil.getString("last_name"),
                                        userDeatil.getInt("id"),userDeatil.getString("email"),userDeatil.getString("mobile_number"),userDeatil.getString("user_type"));
                                // notification listner for like and comment
                                FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications" + userClass.getUserId());
                                FirebaseMessaging.getInstance().subscribeToTopic("chats" + userClass.getUserId());
                                //code for user status
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date();

                                IsOnline isOnline = new IsOnline("True", formatter.format(date));
                                ref.child("Status").child(userClass.getUserId() + "").child("android").setValue(isOnline);
                                IsOnline isOnline1 = new IsOnline("False", formatter.format(date));

                                ref.child("Status").child(userClass.getUserId() + "").child("web").setValue(isOnline1);

                                JSONObject TokenObject = jsonObject.getJSONObject("success");
                                String Token = TokenObject.getString("token");

                                editor.putString("TOKEN", Token);
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(userClass);
                                prefsEditor.putString("MyObject", json);
                                prefsEditor.commit();
                                editor.commit();
                                Intent i = new Intent(getActivity(), SocialFormActivity.class);
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
                    params.put("social_id",id);
                    params.put("fullname",name);
                    params.put("email", gmail_email);
                    params.put("profile_pic",profile);
                    params.put("service", "google");
                    return params;
                }

            };
            requestQueue.add(myReq);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}



