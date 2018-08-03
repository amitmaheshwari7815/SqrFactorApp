package com.hackerkernel.user.sqrfactor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatWithAFriendActivity extends AppCompatActivity {
    static int id;
    MessageClass messageClass=null;
    String friendProfile, friendName;
    private static ArrayList<MessageClass> messageClassArrayList = new ArrayList<>();
    private ChatWithAFriendActivityAdapter chatWithAFriendActivityAdapter;
    private RecyclerView recycler;
    private LinearLayoutManager layoutManager;
    private TextView friendNametext;
    private EditText messageToSend;
    private ImageButton sendMessageButton;
    private boolean isScrolling=false;
    private int currentItems,totalItems,scrolledItems;
    private int previousTotal = 0;
    private boolean loading = false;
    boolean mLoading = false;
    int page=0;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private static final int MAX_ITEMS_PER_REQUEST = 20;
    private static final int NUMBER_OF_ITEMS = 100;
    private static final int SIMULATED_LOADING_TIME_IN_MS = 1500;


    private EndlessRecyclerOnScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_afriend);



        friendNametext = (TextView) findViewById(R.id.friendName);
        messageToSend = (EditText) findViewById(R.id.messageToSend);
        sendMessageButton = (ImageButton) findViewById(R.id.sendButton);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("FriendId");
        friendProfile = intent.getExtras().getString("FriendProfileUrl");
        friendName = intent.getExtras().getString("FriendName");
        //getActionBar().setTitle(friendName);
        //getSupportActionBar().setTitle(friendName);
        //friendNametext.setText(friendName);


        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);


        //mRecyclerView.scrollToPosition(mMessages.Count-1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recycler.setLayoutManager(layoutManager);
        //recycler.addOnScrollListener(createInfiniteScrollListener());
        //recycler.scrollToPosition(messageClassArrayList.size()-1);
        chatWithAFriendActivityAdapter = new ChatWithAFriendActivityAdapter(messageClassArrayList, this, id, friendProfile, friendName);
        recycler.setAdapter(chatWithAFriendActivityAdapter);
        //recycler.addOnScrollListener(createInfiniteScrollListener());

//        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                fetchMoreChatDataFromServer();
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                //fetchMoreChatDataFromServer();
//            }
//        };
        // Adds the scroll listener to RecyclerView

//        recycler.addOnScrollListener(scrollListener);

        //recycler.addOnScrollListener(scrollListener);


//        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState== RecyclerView.SCROLL_AXIS_VERTICAL)
//                {
//                    isScrolling=true;
//
//                }
//                if(newState==RecyclerView.SCROLL_STATE_IDLE)
//                {
//                    fetchMoreChatDataFromServer();
//                }
//            }

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if(dy>0)
//                {
//                    currentItems=layoutManager.getChildCount();
//                    totalItems= layoutManager.getItemCount();
//                    scrolledItems=layoutManager.findFirstVisibleItemPosition();
//                    if(isScrolling&&(currentItems+scrolledItems==totalItems))
//                    {
//                        isScrolling=false;
//                        fetchMoreChatDataFromServer();
//                    }
//                }
//
//            }
//        });
//




//        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                Log.v("value",dy+"");
//                int totalItem = layoutManager.getItemCount();
//                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//
//                if (!mLoading && lastVisibleItem == totalItem - 1) {
//                    mLoading = true;
//                    fetchMoreChatDataFromServer();
//                    mLoading = false;
//                }
//            }
//        });



//        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                {
//                    isScrolling=true;
//                    Toast.makeText(getApplicationContext(),"Fetching more chat",Toast.LENGTH_LONG).show();
//
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                Log.v("value",dy+" "+dx);
//                if(dy<0)
//                {
//                    currentItems=layoutManager.getChildCount();
//                    totalItems= layoutManager.getItemCount();
//                    scrolledItems=layoutManager.findFirstVisibleItemPosition();
//                    Toast.makeText(getApplicationContext(),"items"+currentItems+" "+scrolledItems+" ="+totalItems,Toast.LENGTH_LONG).show();
//                    if(isScrolling)
//                    {
//                        if(currentItems+scrolledItems==totalItems)
//                        {
//
//                            Toast.makeText(getApplicationContext(),"chat data",Toast.LENGTH_LONG).show();
//                            fetchMoreChatDataFromServer();
//                            isScrolling=false;
//                        }
//                    }
//
//
//                }
//
//            }
//        });




        //recycler.smoothScrollToPosition(recycler.getAdapter().getItemCount() - 1);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/myallMSG/getChat/" + id,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                            JSONArray jsonArrayData=jsonObjectChat.getJSONArray("data");


                            for (int i = 0; i < jsonArrayData.length()-1; i++) {
                                //Log.v("Response",response);
                                MessageClass messageClass = new MessageClass(jsonArrayData.getJSONObject(i));
                                messageClassArrayList.add(messageClass);
                            }

                            //Collections.reverse(messageClassArrayList);
                            chatWithAFriendActivityAdapter.notifyDataSetChanged();
                            //recycler.scrollToPosition(messageClassArrayList.size()-1);
                            FirebaseListner();



//                            recycler.smoothScrollToPosition(messageClassArrayList.size()-1);


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


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(myReq);



        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //
                SendMessageToServer();
                LastMessage lastMessage=new LastMessage(MessagesActivity.userId,messageToSend.getText().toString());

                MessagesActivity.ref.child(id+"").setValue(lastMessage);
                Toast.makeText(ChatWithAFriendActivity.this, "Messeage sent..", Toast.LENGTH_LONG).show();
            }
        });





    }


//    private InfiniteScrollListener createInfiniteScrollListener() {
//        return new InfiniteScrollListener(maxItemsPerRequest, layoutManager) {
//            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
//                // load your items here
//                // logic of loading items will be different depending on your specific use case
//
//                // when new items are loaded, combine old and new items, pass them to your adapter
//                // and call refreshView(...) method from InfiniteScrollListener class to refresh RecyclerView
//                refreshView(recyclerView, new MyAdapter(items), firstVisibleItemPosition);
//            }
//        }
//    }


//    @NonNull private InfiniteScrollListener createInfiniteScrollListener() {
//        return new InfiniteScrollListener(MAX_ITEMS_PER_REQUEST, layoutManager) {
//            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
//                //simulateLoading();
//
//                Toast.makeText(ChatWithAFriendActivity.this,"data from server",Toast.LENGTH_LONG).show();
//                int start = ++page * MAX_ITEMS_PER_REQUEST;
//                final boolean allItemsLoaded = start >= messageClassArrayList.size();
//                if (allItemsLoaded) {
//                    //progressBar.setVisibility(View.GONE);
//                } else {
//                    int end = start + MAX_ITEMS_PER_REQUEST;
//                    final ArrayList<MessageClass> itemsLocal = getItemsToBeLoaded(start, end);
//                    //fetchMoreChatDataFromServer();
//                    refreshView(recycler, new ChatWithAFriendActivityAdapter(itemsLocal, getApplicationContext(), id, friendProfile, friendName), firstVisibleItemPosition);
//                }
//            }
//        };
//    }
//
//    @NonNull private ArrayList<MessageClass> getItemsToBeLoaded(int start, int end) {
//        //List<String> newItems = items.subList(start, end);
//        //final List<String> oldItems = ((ChatWithAFriendActivityAdapter) recycler.getAdapter()).getItems();
//        final ArrayList<MessageClass> itemsLocal = new ArrayList<>();
//        itemsLocal.addAll(messageClassArrayList);
//        itemsLocal.addAll(messageClassArrayList);
//        return itemsLocal;
//    }
//


    public  void fetchMoreChatDataFromServer() {
        //progressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(g, "chat response", Toast.LENGTH_LONG).show();

        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/myallMSG/getChat/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("MorenewsFeedFromServer", response);
                        Toast.makeText(ChatWithAFriendActivity.this, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectChat = jsonObject.getJSONObject("chats");
                            JSONArray jsonArrayData=jsonObjectChat.getJSONArray("data");
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                //Log.v("Response",response);
                                MessageClass messageClass = new MessageClass(jsonArrayData.getJSONObject(i));
                                messageClassArrayList.add(messageClass);
                            }

                            //Collections.reverse(messageClassArrayList);
                            chatWithAFriendActivityAdapter.notifyDataSetChanged();
                           // recycler.scrollToPosition(messageClassArrayList.size()-1);




//                            recycler.smoothScrollToPosition(messageClassArrayList.size()-1);


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
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(myReq);
    }





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void FirebaseListner()
    {
        MessagesActivity.ref.child(MessagesActivity.userId+"").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                LastMessage lastMessage = dataSnapshot.getValue(LastMessage.class);
                if(lastMessage!=null && id==lastMessage.getSenderId())
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();


                    if(messageClassArrayList.size()!=0)
                    {
                        messageClass= new MessageClass(messageClassArrayList.get(messageClassArrayList.size()-1).getMessageId()+1,id,MessagesActivity.userId,lastMessage.getMessage(),"1",formatter.format(date),formatter.format(date));
//                        messageClassArrayList.remove()
                    }
                    //Collections.reverse(messageClassArrayList);
                    messageClassArrayList.add(messageClass);
                    //Collections.reverse(messageClassArrayList);
                    //recycler.smoothScrollToPosition(recycler.getAdapter().getItemCount() - 1);
                    chatWithAFriendActivityAdapter.notifyDataSetChanged();
                    //recycler.scrollToPosition(messageClassArrayList.size()-1);
                    //recycler.scrollToPosition(messageClassArrayList.size()-1);
//                    chatWithAFriendActivityAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//                        @Override
//                        public void onItemRangeInserted(int positionStart, int itemCount) {
//                            layoutManager.smoothScrollToPosition(recycler, null, chatWithAFriendActivityAdapter.getItemCount()-1);
//                        }
//                    });
//                    recycler.smoothScrollToPosition(messageClassArrayList.size()-1);



                    Toast.makeText(ChatWithAFriendActivity.this,"MessageFromServer"+lastMessage.getMessage(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    public  void SendMessageToServer()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST, "https://archsqr.in/api/chat/sendChat"
                ,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        Log.v("ReponseFeed", response);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        MessageClass messageClass=
                                new MessageClass(messageClassArrayList.get(messageClassArrayList.size()-1).getMessageId()+1,MessagesActivity.userId,id,messageToSend.getText().toString(),"1",formatter.format(date),formatter.format(date));
                        //Collections.reverse(messageClassArrayList);
                        messageClassArrayList.add(messageClass);
                        //Collections.reverse(messageClassArrayList);
                        messageToSend.setText("");
                        chatWithAFriendActivityAdapter.notifyDataSetChanged();

                       // recycler.scrollToPosition(messageClassArrayList.size()-1);


                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId",MessagesActivity.userId+"");
                params.put("friendId",id+"" );
                params.put("message",messageToSend.getText().toString() );

                return params;
            }

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
    public void loadNextDataFromApi(int offset) {

        fetchMoreChatDataFromServer();
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

}
