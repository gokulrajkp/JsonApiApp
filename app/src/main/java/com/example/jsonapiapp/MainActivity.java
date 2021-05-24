package com.example.jsonapiapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jsonapiapp.room.DatabaseClient;
import com.example.jsonapiapp.room.Recipe;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView homeRv;
    AdapterList adapterList;
    ArrayList<ModelList> modelList = new ArrayList<>();
    String imageDataUrl = "https://picsum.photos/v2/list?page=1&limit=20";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeRv = findViewById(R.id.homeRv);
        adapterList = new AdapterList(this, modelList, false);

        homeRv.setHasFixedSize(true);
        homeRv.setNestedScrollingEnabled(true);
        homeRv.setLayoutManager(new LinearLayoutManager(this));
        homeRv.setAdapter(adapterList);

        try {
            ConnectivityManager coonmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = coonmgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting() ) {
                Toast.makeText(this, "from server", Toast.LENGTH_SHORT).show();
                fetchfromServer();
            } else {
                Toast.makeText(this, "from room", Toast.LENGTH_SHORT).show();

                fetchfromRoom();
            }

        }catch (Exception e){
            System.out.println("Check connectivity Exception"+e.getMessage());


        }


    }

    private void fetchfromRoom() {
        Toast.makeText(this, "room connected", Toast.LENGTH_SHORT).show();
        List<Recipe> recipeList = DatabaseClient.getInstance(MainActivity.this).getAppDatabase().recipeDao().getAllUsers();
        for (Recipe recipe: recipeList){
            ModelList repo = new ModelList(recipe.getId(),
                    recipe.getWidth(),
                    recipe.getHeight(),
                    recipe.getAuthor(),
                    recipe.getImageUrl(),
                    recipe.getDownloadUrl());
            modelList.add(repo);

            adapterList.notifyDataSetChanged();
        }
    }

    private void fetchfromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, imageDataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ModelList itemSingle = new ModelList();
                                JSONObject jsonList = jsonArray.getJSONObject(i);
                                itemSingle.setId(jsonList.getInt("id"));
                                itemSingle.setWidth(jsonList.getInt("width"));
                                itemSingle.setHeight(jsonList.getInt("height"));
                                itemSingle.setImageUrl(jsonList.getString("url"));
                                itemSingle.setAuthor(jsonList.getString("author"));
                                itemSingle.setDownloadUrl(jsonList.getString("download_url"));
                                modelList.add(itemSingle);
                                SaveTask();
                                Toast.makeText(MainActivity.this, "accesing", Toast.LENGTH_SHORT).show();
                            }
                            adapterList.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_grid, menu);
        SwitchMaterial mySwitch = menu.findItem(R.id.menuSwitchList)
                .setActionView(R.layout.item_menu_switch)
                .getActionView().findViewById(R.id.switchList);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeListViewType(isChecked);
                changeicon();
            }
        });
        return true;
    }

    private void changeListViewType(boolean showGrid){
        adapterList = new AdapterList(this, modelList, showGrid);

        if (showGrid){
            homeRv.setLayoutManager(new GridLayoutManager(this, 3));
            homeRv.setAdapter(adapterList);

        } else {
            homeRv.setLayoutManager(new LinearLayoutManager(this));
            homeRv.setAdapter(adapterList);
        }

    }
    public void changeicon(){


    }

    public void SaveTask(){
        class SaveTask extends AsyncTask<Void,Void ,Void>{
            @Override
            protected Void doInBackground( Void...voids) {
                    for (int i =0;i<modelList.size();i++){
                        Recipe recipe = new Recipe();
                        recipe.setId(modelList.get(i).getId());
                        recipe.setWidth(modelList.get(i).getWidth());
                        recipe.setHeight(modelList.get(i).getHeight());
                        recipe.setAuthor(modelList.get(i).getAuthor());
                        recipe.setImageUrl(modelList.get(i).getImageUrl());
                        recipe.setDownloadUrl(modelList.get(i).getDownloadUrl());

                        Log.d("chkauthor",":"+i+modelList.get(0).author);
                       DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().recipeDao().insert(recipe);
                    }



                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();



    }

}