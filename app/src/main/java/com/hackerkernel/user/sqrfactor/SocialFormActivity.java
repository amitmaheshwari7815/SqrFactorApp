package com.hackerkernel.user.sqrfactor;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SocialFormActivity extends AppCompatActivity {
    private EditText userName,mobileNo;
    private Button submit,skip;
    private Spinner country;
    private String country_val=null;
    private ArrayList<CountryClass> countryClassArrayList=new ArrayList<>();
    private ArrayList<String> countryName=new ArrayList<>();
    private int countryId=1;
    private UserClass userClass;
    public static DatabaseReference ref;
    public static FirebaseDatabase database;
    private ImageView uploadProfile;
    private Bitmap bitmap;
    private Uri uri;
    public  static final int RequestPermissionCode  = 1 ;
    final int PIC_CROP = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_form);

        final SharedPreferences mPrefs = getSharedPreferences("User", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        userClass = gson.fromJson(json, UserClass.class);
        database= FirebaseDatabase.getInstance();
        ref = database.getReference();

        SharedPreferences sharedPreferences = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "sqr");
        TokenClass.Token = token;
        TokenClass tokenClass = new TokenClass(token);
        Log.v("Token1", token);

        userName = findViewById(R.id.userName_text);
        mobileNo = findViewById(R.id.user_mobile_number_text);
        country = findViewById(R.id.social_country_spinner);
        submit = findViewById(R.id.social_submit);

        uploadProfile = findViewById(R.id.upload_profile);
        uploadProfile.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (ContextCompat.checkSelfPermission(SocialFormActivity.this,
                                                         android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                                         != PackageManager.PERMISSION_GRANTED) {

                                                     ActivityCompat.requestPermissions(SocialFormActivity.this,
                                                             new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                                                             MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                                 }

                                                 if (ActivityCompat.shouldShowRequestPermissionRationale(SocialFormActivity.this,
                                                         android.Manifest.permission.CAMERA)) {

                                                     Toast.makeText(SocialFormActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                                                 } else {

                                                     ActivityCompat.requestPermissions(SocialFormActivity.this, new String[]{
                                                             Manifest.permission.CAMERA}, RequestPermissionCode);

                                                 }
                                                 selectImage();

                                             }
                                         });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitDetails();
                    }
                });

                LoadCountryFromServer();

                country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

            }

            public void LoadCountryFromServer() {
                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://archsqr.in/api/public_country",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.v("ResponseLike", s);
                                Toast.makeText(SocialFormActivity.this, "res" + s, Toast.LENGTH_LONG).show();
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


                                    ArrayAdapter<String> spin_adapter1 = new ArrayAdapter<String>(SocialFormActivity.this, android.R.layout.simple_list_item_1, countryName);
                                    country.setAdapter(spin_adapter1);

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

public void submitDetails(){
    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
    StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/updatesocialreg",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("Reponse", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        userName.setText("");
                        mobileNo.setText("");
                        Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(i);
                        finish();
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
            params.put("Authorization", "Bearer " + TokenClass.Token);
            return params;
        }

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobile_number",mobileNo.getText().toString());
            params.put("country_id",country_val);
            params.put("username",userName.getText().toString());
            return params;
        }


    };

    requestQueue.add(myReq);

    }
    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                }

                else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                uri = data.getData();
                performCrop();

//                File f = new File(Environment.getExternalStorageDirectory().toString());
//
//                for (File temp : f.listFiles()) {
//
//                    if (temp.getName().equals("temp.jpg")) {
//
//                        f = temp;
//
//                        break;
//
//                    }
//
//                }
//
//                try {
//
//
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//
//
//
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//
//                            bitmapOptions);
//                    uri= data.getData();
//                    performCrop();
//
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//
//                }
            }


            else if (requestCode == 2) {

                uri = data.getData();
                performCrop();

                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(uri,filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);


                String picturePath = c.getString(columnIndex);
                String[] fileName = picturePath.split("/");
                c.close();


            }
            else if(requestCode== PIC_CROP)
            {

                Bundle extras = data.getExtras();
                bitmap  = extras.getParcelable("data");
                uploadProfile.setImageBitmap(bitmap);
                ChangeProfile();
            }
        }

    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }
    private void performCrop(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 60);
            cropIntent.putExtra("outputY", 60);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void ChangeProfile(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest myReq = new StringRequest(Request.Method.POST,  "https://archsqr.in/api/parse/change_profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

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
                params.put("Authorization", "Bearer " + TokenClass.Token);

                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String image = getStringImage(bitmap);

                params.put("profile_image","data:image/jpeg;base64,"+image );
                return params;
            }

        };


        requestQueue.add(myReq);
    }
}
