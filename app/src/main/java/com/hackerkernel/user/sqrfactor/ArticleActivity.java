package com.hackerkernel.user.sqrfactor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.akshaykale.imagepicker.ImagePickerFragment;
import com.akshaykale.imagepicker.ImagePickerListener;
import com.akshaykale.imagepicker.PhotoObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.google.gson.Gson;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;


//import io.github.angebagui.mediumtextview.MediumTextView;

import static com.hackerkernel.user.sqrfactor.BuildConfig.DEBUG;

public class ArticleActivity extends ToolbarActivity implements ImagePickerListener{

    private Toolbar toolbar;
    private Editor editor;
    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private Uri uri;
    private  String finalHtml=null;
    String cropedImage;
    ImageView mRemoveButton;
    private boolean isEdit=false;
    private WebView videoView;
    private String html;
    private int postId;
    private String imageString;
    private Button saveArticleButton,video_post_close;
    private EditText articleTitle,articleShortDescription,articleTag;
    private TextView articleSelectBannerImage,articleUserName;
    // TextInputLayout articleSelectBannerImage;
    private ImageView articleCustomBaneerImage,cropFinalImage,profileImage,selectBanerImageIcon,banner_attachment_image;
    private FrameLayout videoFrameLayout;
    private ImageButton article_insert_video;
    private String Slug=null;
    Bitmap bitmap;
    Intent CamIntent, GalIntent, CropIntent ;
    public  static final int RequestPermissionCode  = 1 ;
    private static final int PIC_CROP = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private ImagePickerFragment imagePickerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        ContinueAfterPermission();
    }


    public void ContinueAfterPermission() {

        //adding text editor
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent=getIntent();
        if(!intent.hasExtra("Fab"))
        {
            toolbar.setVisibility(View.GONE);
        }else {
            toolbar.setVisibility(View.VISIBLE);
        }

        if( intent.hasExtra("Post_Slug_ID"))
        {
            toolbar.setVisibility(View.VISIBLE);
        }


        articleTitle = findViewById(R.id.articleTitle);
        articleTitle.setFocusable(true);

        //videoView=(WebView)findViewById(R.id.articleVideoView);
        articleShortDescription = findViewById(R.id.articleShortDescription);
        articleSelectBannerImage = findViewById(R.id.articleSelectBaneerImage);
        selectBanerImageIcon=findViewById(R.id.selectBanerImageIcon);

        article_insert_video=(ImageButton)findViewById(R.id.article_insert_video);
        article_insert_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();

            }
        });
        banner_attachment_image=(ImageView)findViewById(R.id.banner_attachment_image);

        videoFrameLayout=(FrameLayout)findViewById(R.id.videoFrameLayout);
        video_post_close=(Button)findViewById(R.id.video_post_close);

        articleTag = findViewById(R.id.articleTags);
        saveArticleButton = findViewById(R.id.saveArticle);

        Intent intent1=getIntent();
        if(intent1!=null && intent1.hasExtra("Post_Slug_ID"))
        {
            Toast.makeText(this,intent1.getStringExtra("Post_Slug_ID"),Toast.LENGTH_LONG).show();
            isEdit=true;
            Slug=intent1.getStringExtra("Post_Slug_ID");
            postId=intent1.getIntExtra("Post_ID",0);
            FetchDataFromServerAndBindToViews(intent1.getStringExtra("Post_Slug_ID"));


        }
        final FrameLayout frameLayout = findViewById(R.id.article_rl);
        frameLayout.setVisibility(View.GONE);

        mRemoveButton = findViewById(R.id.article_banner_remove);
        mRemoveButton.setVisibility(View.GONE);

        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.GONE);
                cropFinalImage.setImageBitmap(null);
                cropFinalImage.setVisibility(View.GONE);
                mRemoveButton.setVisibility(View.GONE);


            }

        });

        SharedPreferences mPrefs =getSharedPreferences("User",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("MyObject", "");
        UserClass userClass = gson.fromJson(json, UserClass.class);
        profileImage=findViewById(R.id.article_profile);
        articleUserName=findViewById(R.id.article_userName);


        Glide.with(this).load("https://archsqr.in/"+userClass.getProfile())
                .into(profileImage);


        if(userClass.getFirst_name().equals("null"))
        {
            articleUserName.setText(userClass.getUser_name());
        }

        else {
            articleUserName.setText(userClass.getFirst_name()+"" +userClass.getLast_name());
        }




        cropFinalImage = findViewById(R.id.cropFinalImage);
//        cropFinalImage.setVisibility(View.GONE);



        GetTagFromServer();

        articleSelectBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                cropFinalImage.setVisibility(View.VISIBLE);
                mRemoveButton.setVisibility(View.VISIBLE);
                    CameraOpen();


            }


        });
        selectBanerImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.setVisibility(View.VISIBLE);
                cropFinalImage.setVisibility(View.VISIBLE);
                mRemoveButton.setVisibility(View.VISIBLE);

                CameraOpen();

            }

        });



        saveArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the data and send it to server

                Log.v("dataTosend",editor.getContentAsHTML());

                cropFinalImage.invalidate();
                BitmapDrawable drawable = (BitmapDrawable)cropFinalImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                String image=getStringImage(bitmap);
                Log.v("cropedImage",image);
                if(isEdit)
                {
                    SendArticleDataToServer1(image,"https://archsqr.in/api/article-edit");
                }
                else {
                    SendArticleDataToServer(image,"https://archsqr.in/api/article-parse-post");
                }

                // Toast.makeText(getApplicationContext(),editor.getContentAsHTML(),Toast.LENGTH_LONG).show();
//                if (!TextUtils.isEmpty(articleTitle.getText()) && !TextUtils.isEmpty(articleShortDescription.getText()) &&
//                        !TextUtils.isEmpty(articleTag.getText())) {
//
//                }
            }
        });


        editor = (Editor) findViewById(R.id.editor);
        editor.setFocusable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editor.setFocusable(View.NOT_FOCUSABLE);
        }

        findViewById(R.id.article_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.article_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });

        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);
        //editor.StartEditor();
        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                //Toast.makeText(ArticleActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
                Toast.makeText(ArticleActivity.this, uuid, Toast.LENGTH_LONG).show();
                uploadEditorImageToServer(uuid);


                // editor.onImageUploadFailed(uuid);
            }
        });
        editor.render();

    }

    private void CameraOpen(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        imagePickerFragment = new ImagePickerFragment();//.newInstance(mStackLevel);
        imagePickerFragment.addOnClickListener(this);
        //imagePickerFragment.setImageLoadEngine(new ILoad());
        imagePickerFragment.show(ft, "dialog");
    }


    @Override
    public void onPhotoClicked(PhotoObject photoObject) {
        Glide.with(getApplicationContext()).clear(cropFinalImage);
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(photoObject.getPath())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        Bitmap bitmapResized = Bitmap.createScaledBitmap(resource,
                                (int) (resource.getWidth() * 0.5), (int) (resource.getHeight() * 0.5), false);

                        cropFinalImage.setImageBitmap(bitmapResized);
                    }
                });

        imagePickerFragment.dismiss();
    }

    @Override
    public void onCameraClicked(Bitmap image) {
        cropFinalImage.setImageBitmap(image);
        imagePickerFragment.dismiss();
    }

    private void GetTagFromServer() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/searchtags",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray tags=jsonObject.getJSONArray("searched_tags");
                            final ArrayList<String> tagName=new ArrayList<>();
                            for(int i=0;i<tags.length();i++)
                            {
                                tagName.add(tags.getJSONObject(i).getString("name"));
                            }

                            // String[] languages = { "C","C++","Java","C#","PHP","JavaScript","jQuery","AJAX","JSON" };
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ArticleActivity.this, android.R.layout.simple_spinner_dropdown_item, tagName);
                            multiAutoCompleteTextView = findViewById(R.id.articleTags);
                            multiAutoCompleteTextView.setAdapter(adapter);
                            multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

//
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
        };

        requestQueue.add(myReq);

    }

    public void uploadEditorImageToServer(final String uuid)
    {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/upload-medium-image",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String Imgurl=jsonObject.getString("asset_image");
                            editor.onImageUploadComplete(Imgurl, uuid);
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("image","data:image/jpeg;base64,"+imageString);
                return params;
            }
        };

        requestQueue.add(myReq);


    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK&& data != null && data.getData() != null) {
            uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap,
                        (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), false);
                imageString=getStringImage(bitmap);
                editor.insertImage(bitmapResized);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            // editor.RestoreState();
        }
        else if(requestCode== editor.MAP_MARKER_REQUEST){
            editor.insertMap(data.getStringExtra("cords"));
        }


        }

    public void SendArticleDataToServer(final String image,final String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Reponse", response);
//                        Toast.makeText(getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();
                        editor.clearAllContents();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            editor.setFocusable(View.NOT_FOCUSABLE);
//                        }
                        multiAutoCompleteTextView.setText("");
                        articleTitle.setText("");
                        articleShortDescription.setText("");
                        cropFinalImage.setVisibility(View.GONE);

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                Log.v("article",articleTitle.getText().toString()+" "+multiAutoCompleteTextView.getText().toString()+" "+articleShortDescription.getText().toString()+
//                " "+image+ " "+finalHtml);

                params.put("post_type","article");
                params.put("title",articleTitle.getText().toString());
                params.put("tags",multiAutoCompleteTextView.getText().toString());
                params.put("description_short",articleShortDescription.getText().toString());
                params.put("banner_image","data:image/jpeg;base64,"+image);
                if(finalHtml!=null)
                    params.put("description",finalHtml);
                else
                    params.put("description",editor.getContentAsHTML());
                return params;
            }
        };

        requestQueue.add(myReq);
    }

    public void SendArticleDataToServer1(final String image,final String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("Reponse", response);
//                        Toast.makeText(getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();
                        editor.clearAllContents();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            editor.setFocusable(View.NOT_FOCUSABLE);
//                        }
                        multiAutoCompleteTextView.setText("");
                        articleTitle.setText("");
                        articleShortDescription.setText("");
                        cropFinalImage.setVisibility(View.GONE);

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.v("article1",articleTitle.getText().toString()+" "+multiAutoCompleteTextView.getText().toString()+" "+articleShortDescription.getText().toString()+
                        " "+finalHtml+" "+Slug +" "+image+ " ");
                params.put("id",postId+"");
                params.put("title",articleTitle.getText().toString());
                params.put("tags",multiAutoCompleteTextView.getText().toString());
                params.put("description_short",articleShortDescription.getText().toString());
                params.put("slug",Slug);
                params.put("banner_image","data:image/jpeg;base64,"+image);
                if(finalHtml!=null)
                    params.put("description",finalHtml);
                else
                    params.put("description",editor.getContentAsHTML());
                return params;
            }
        };

        requestQueue.add(myReq);
    }


    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri, "image/*");
            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 640);
            CropIntent.putExtra("outputY", 640);
            CropIntent.putExtra("aspectX", 16);
            CropIntent.putExtra("aspectY", 9);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, PIC_CROP);

        } catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {



        if(requestCode== RequestPermissionCode)

        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                Toast.makeText(ArticleActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

            }
            else {

//                Toast.makeText(ArticleActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

            }
        }

        else if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ContinueAfterPermission();
            } else {
                // Permission Denied
//                Toast.makeText(ArticleActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showPopup(){
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.post_video_link_popup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.post_video_link);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String videoLink = userInput.getText().toString();
                                showVideo(videoLink);
                                //result.setText(userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        // AlertDialog alertDialog = alertDialogBuilder.create();


    }

    private void showVideo(String videoLink)
    {
//        Toast.makeText(this,videoLink,Toast.LENGTH_LONG).show();
        final WebView myWebView = (WebView) findViewById(R.id.articleVideoView);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient());

        //String src ="src="+'"'+videoLink+'"';
        String[] stringId=videoLink.split("/");

        String id=stringId[stringId.length-1];
        Log.v("id",id);
        String src="src="+'"'+"https://www.youtube.com/embed/"+id+'"';



        html="<iframe width=\"100%\" height=\"300\" "+src+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";
        //String html1 = "<iframe width=\"100%\" height=\"600\" src=\"www.youtube.com/embed/cffcUX_aHe0\" frameborder=\"0\" allowfullscreen=\"\"></iframe>";

        myWebView.loadDataWithBaseURL("https://www.youtube.com/embed/"+id+'"', html, "text/html","UTF-8",null);
        //myWebView.loadUrl(videoLink);

        finalHtml="   <html>\n" +
                "  <head>\n" +
                "    <title>Combined</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"page1\">\n" +
                editor.getContentAsHTML() +
                "    </div>\n" +
                "    <div id=\"page2\">\n" +
                html +
                "    </div>\n" +
                "  </body>\n" +
                "</html>";

        videoFrameLayout.setVisibility(View.VISIBLE);
//
        video_post_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoFrameLayout.setVisibility(View.GONE);
            }
        });
    }

    private void FetchDataFromServerAndBindToViews(String post_slug_id) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // "https://archsqr.in/api/profile/detail/
        StringRequest myReq = new StringRequest(Request.Method.GET, "https://archsqr.in/api/post/article/edit/"+post_slug_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response",response);
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectFullPost = jsonObject.getJSONObject("articlePostEdit");
                            final ArticleEditClass articleEditClass = new ArticleEditClass(jsonObjectFullPost);
                            articleTitle.setText(articleEditClass.getTitle());
                            articleShortDescription.setText(articleEditClass.getShort_description());
                            //multiAutoCompleteTextView.setText(fullPost.get);

                            if(articleEditClass.getImage()!=null) {
                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load("https://archsqr.in/"+articleEditClass.getImage())
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                                Bitmap bitmapResized = Bitmap.createScaledBitmap(resource,
                                                        (int) (resource.getWidth() * 0.5), (int) (resource.getHeight() * 0.5), false);
                                                imageString = getStringImage(resource);
                                                editor.insertImage(bitmapResized);
                                            }
                                        });
                            }
                            //Log.v("des0",articleEditClass.getDescription());

                            setContentToView(articleEditClass.getDescription());
                            if(articleEditClass.getBanner_image()!=null)
                            {


                                Glide.with(getApplicationContext()).load("https://archsqr.in/"+articleEditClass.getBanner_image())
                                        .into(cropFinalImage);
                                cropFinalImage.setVisibility(View.VISIBLE);
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

        };
        requestQueue.add(myReq);
    }

    public void setContentToView(String content){
        List<String> p = new ArrayList<>();
        List<String> src = new ArrayList<>();
        Document doc = Jsoup.parse(content);

        Elements elements = doc.getAllElements();
        int pos=0;

        for(Element element :elements){
            Tag tag = element.tag();

            if(tag.getName().equalsIgnoreCase("a")){
                String name  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                Log.v("des1",name);
                if(name.contains("span")||name.contains("<i>")||name.contains("<b>"))
                {
                    continue;
                }
                else {
                    editor.getInputExtensions().insertEditText(pos,"",name);
                    pos++;
                }

            }

            else if(tag.getName().equalsIgnoreCase("b")){
                String title  = element.html();
                //String heading = element.select(tag.getName().toString()).text();
                Log.v("des2",title);
                if(title.contains("&nbsp")||title.contains("href")||title.equals("<br>"))
                {
                    continue;
                }

                else {
                    editor.getInputExtensions().insertEditText(pos,"",title);
                    pos++;
                    continue;
                }

            }

            else if(tag.getName().equalsIgnoreCase("p")){

                element.select("img").remove();
                String body= element.html();

                String[] parsedBody=body.split("\\.");
                StringBuilder builder = new StringBuilder();
                for(String s : parsedBody) {
                    Log.v("des3",s);
                    if(s.contains("&nbsp")||s.contains("<span")||s.contains("</span>")||s.contains("<br>"))
                    {
                        continue;
                    }
                    else
                        builder.append(s+".");
                }
                String str = builder.toString();
                if(body.contains("href")||body.equals("<br>")||body.contains("<b>"))
                {
                    continue;
                }
                else {
                    editor.getInputExtensions().insertEditText(pos,"",str);
                    pos++;
                    continue;
                }


            }
            else if (tag.getName().equalsIgnoreCase("img")){
                String url  = element.select("img").attr("src");
                Log.v("des4",url);

                Glide.with(this)
                        .asBitmap()
                        .load(url)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Bitmap bitmapResized = Bitmap.createScaledBitmap(resource,
                                        (int) (resource.getWidth() * 0.5), (int) (resource.getHeight() * 0.5), false);
                                imageString=getStringImage(resource);
                                editor.insertImage(bitmapResized);
                            }
                        });
                continue;
            }
            else if (tag.getName().equalsIgnoreCase("iframe")){
                String url  = element.select("iframe").attr("src");
                Log.v("des5",url);


                final WebView myWebView = (WebView) findViewById(R.id.articleVideoView);
                myWebView.setWebViewClient(new WebViewClient());
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.setWebChromeClient(new WebChromeClient());

                String[] stringId=url.split("/");
                String id=stringId[stringId.length-1];
                String src1="src="+'"'+"https://www.youtube.com/embed/"+id+'"';
                String html="<iframe width=\"100%\" height=\"400\" "+src1+"frameborder=\"0\" allowfullscreen=\"\"></iframe>";

                finalHtml="   <html>\n" +
                        "  <head>\n" +
                        "    <title>Combined</title>\n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "    <div id=\"page1\">\n" +
                        editor.getContentAsHTML() +
                        "    </div>\n" +
                        "    <div id=\"page2\">\n" +
                        html +
                        "    </div>\n" +
                        "  </body>\n" +
                        "</html>";

                myWebView.loadDataWithBaseURL(url,html, "text/html", "UTF-8", "");
                videoFrameLayout.setVisibility(View.VISIBLE);
//
                video_post_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        videoFrameLayout.setVisibility(View.GONE);
                    }
                });
                continue;
            }



        }
    }


}