package com.hackerkernel.user.sqrfactor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.hackerkernel.user.sqrfactor.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class ProfileActivity extends ToolbarActivity {
    private ArrayList<ProfileClass1> profileClassList = new ArrayList<>();
    private ImageView displayImage;
    private ImageView camera;// button
    public ImageButton mRemoveButton;
    private ProfileAdapter profileAdapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    private ImageView morebtn, btn,coverImage,profileImage,profileStatusImage,profile_status_image;
    private TextView profileName,followCnt,followingCnt,portfolioCnt,bluePrintCnt;
    Button btnSubmit,editProfile;
    EditText writePost;
    Bitmap bitmap;
    private Uri uri;
    boolean flag = false;
    LinearLayoutManager layoutManager;
    TextView blueprint, portfolio, followers, following;
    private UserClass userClass;
    private static String nextPageUrl;
    private boolean isLoading=false;
    private static Context context;
    public  static final int RequestPermissionCode  = 1 ;
    final int PIC_CROP = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(R.color.White);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.profile_recycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        profileAdapter = new ProfileAdapter(profileClassList,this);
        recyclerView.setAdapter(profileAdapter);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        profile_status_image=(ImageView)findViewById(R.id.profile_status_image);
        writePost = (EditText)findViewById(R.id.profile_profile_write_post);
        writePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PostActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        editProfile = (Button)findViewById(R.id.profile_editprofile);
        coverImage = (ImageView) findViewById(R.id.profile_cover_image);
        profileImage = (ImageView) findViewById(R.id.profile_profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                        android.Manifest.permission.CAMERA))
                {

//                    Toast.makeText(ProfileActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

                } else {

                    ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{
                            Manifest.permission.CAMERA}, RequestPermissionCode);

                }
                selectImage();

            }


        });
        profileName =(TextView)findViewById(R.id.profile_profile_name);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profileImage);
        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profile_status_image);
        if(userClass.getFirst_name().equals("null"))
        {
            profileName.setText(userClass.getUser_name());
        }

        else {
            profileName.setText(userClass.getFirst_name()+"" +userClass.getLast_name());
        }


        profileStatusImage = (ImageView) findViewById(R.id.profile_status_image);
        followCnt = (TextView) findViewById(R.id.profile_followerscnt);
        followingCnt = (TextView) findViewById(R.id.profile_followingcnt);
        portfolioCnt = (TextView) findViewById(R.id.profile_portfoliocnt);
        bluePrintCnt = (TextView) findViewById(R.id.profile_blueprintcnt);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Settings.class);
//                intent.putExtra("portFolioCount",portfolioCnt.getText().toString());
//                intent.putExtra("followingCount",followingCnt.getText().toString());
//                intent.putExtra("followersCount",followCnt.getText().toString());
//                intent.putExtra("bluePrintCount",bluePrintCnt.getText().toString());
                startActivity(intent);
            }
        });



        morebtn = (ImageView)findViewById(R.id.profile_about_morebtn);
        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                pop.getMenu().add(1,1,0,"About "+userClass.getName());
                pop.getMenuInflater().inflate(R.menu.more_menu, pop.getMenu());
                pop.show();

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case 1:
                                Intent i = new Intent(getApplicationContext(), About.class);
                                startActivity(i);
                                return true;

                        }
                        return true;
                    }
                });

            }
        });

        blueprint = (TextView)findViewById(R.id.profile_blueprintClick);
        portfolio = (TextView)findViewById(R.id.profile_portfolioClick);
        followers = (TextView)findViewById(R.id.profile_followersClick);
        following = (TextView)findViewById(R.id.profile_followingClick);

        blueprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ProfileActivity.this, BlueprintActivity.class);
//                startActivity(i);
                LoadData();
            }
        });

        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, PortfolioActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowersActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, FollowingActivity.class);
                i.putExtra("UserName",userClass.getUser_name());
                startActivity(i);
            }
        });

        LoadData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isLoading=false;
                //Toast.makeText(context,"moving down",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int lastId=layoutManager.findLastVisibleItemPosition();
                if(dy>0 && lastId + 2 > layoutManager.getItemCount() && !isLoading)
                {
                    isLoading=true;
                    Log.v("rolling",layoutManager.getChildCount()+" "+layoutManager.getItemCount()+" "+layoutManager.findLastVisibleItemPosition()+" "+
                            layoutManager.findLastVisibleItemPosition());
                    LoadMoreDataFromServer();

                }
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        //LoadData();
    }

    public void LoadData(){

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        final UserClass userClass = gson.fromJson(json, UserClass.class);

        if(profileClassList!= null){
            profileClassList.clear();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/profile/detail/"+userClass.getUser_name(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
//                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            nextPageUrl = jsonObject.getString("nextPage");
                            followCnt.setText(jsonObject.getString("followerCnt"));
                            followingCnt.setText(jsonObject.getString("followingCnt"));
                            bluePrintCnt.setText(jsonObject.getString("blueprintCnt"));
                            portfolioCnt.setText(jsonObject.getString("portfolioCnt"));

                            JSONObject jsonPost = jsonObject.getJSONObject("posts");
                            if (jsonPost!=null)
                            {
                                JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    ProfileClass1 profileClass1 = new ProfileClass1(jsonArrayData.getJSONObject(i));
                                    profileClassList.add(profileClass1);
                                }
                                profileAdapter.notifyDataSetChanged();
                            }



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
                params.put("Authorization", "Bearer "+TokenClass.Token);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username",userClass.getUser_name());
                return params;
            }

        };


        requestQueue.add(myReq);


    }
    public void LoadMoreDataFromServer(){

        if(nextPageUrl!=null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest myReq = new StringRequest(Request.Method.POST, nextPageUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v("ReponseFeed", response);
//                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                nextPageUrl = jsonObject.getString("nextPage");
                                JSONObject jsonPost = jsonObject.getJSONObject("posts");
                                if (jsonPost != null) {
                                    JSONArray jsonArrayData = jsonPost.getJSONArray("data");
                                    for (int i = 0; i < jsonArrayData.length(); i++) {
                                        ProfileClass1 profileClass1 = new ProfileClass1(jsonArrayData.getJSONObject(i));
                                        profileClassList.add(profileClass1);
                                    }
                                    profileAdapter.notifyDataSetChanged();
                                }


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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", userClass.getUser_name() );
                    return params;
                }
            };


            requestQueue.add(myReq);
        }


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



    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override

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
                profileImage.setImageBitmap(bitmap);
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
//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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