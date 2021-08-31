package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategorClickInterface {

    //b3c590d6fbd54abbbf479769da1a185a

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles>articlesArrayList;
    private ArrayList<CategoryRVModal>categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }

         private void getCategories(){
             categoryRVModalArrayList.add(new CategoryRVModal("All","https://images.unsplash.com/photo-1560177112-fbfd5fde9566?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjN8fG5ld3NwYXBlcnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
             categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
             categoryRVModalArrayList.add(new CategoryRVModal("Science","https://images.unsplash.com/photo-1518152006812-edab29b069ac?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjB8fHNjaWVuY2V8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
             categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://images.unsplash.com/photo-1556817411-31ae72fa3ea0?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjF8fHNwb3J0c3xlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
             categoryRVModalArrayList.add(new CategoryRVModal("General","https://images.unsplash.com/photo-1512314889357-e157c22f938d?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTR8fGdlbmVyYWx8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
             categoryRVModalArrayList.add(new CategoryRVModal("Business","https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?ixid=MnwxMjA3fDB8MHxzZWFyY2h8OXx8YnVzaW5lc3N8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
             categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8ZW50ZXJ0YWlubWVudHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));
             categoryRVModalArrayList.add(new CategoryRVModal("Health","https://images.unsplash.com/photo-1538333702852-c1b7a2a93001?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NDR8fGhlYWx0aHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60"));

             categoryRVAdapter.notifyDataSetChanged();

         }

         private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL ="https://newsapi.org/v2/top-headlines/sources?category="+category+"&apiKey=b3c590d6fbd54abbbf479769da1a185a";
        String url ="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=pubishedAt&language=en&apikey=b3c590d6fbd54abbbf479769da1a185a";
        String BASE_URL ="https://newsapi.org/";

        Retrofit retrofit =new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
             Call<NewsModel> call;
             if (category.equals("All")){
                 call = retrofitAPI.getAllNews(url);
             }else {
                 call = retrofitAPI.getNewsByCategory(categoryURL);
             }

             call.enqueue(new Callback<NewsModel>() {
                 @Override
                 public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                     NewsModel newsModel = response.body();
                     loadingPB.setVisibility(View.GONE);
                     ArrayList<Articles> articles = newsModel.getArticles();
                     for (int i=0 ; i<articles.size(); i++){
                         articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));

                     }
                     newsRVAdapter.notifyDataSetChanged();
                 }

                 @Override
                 public void onFailure(Call<NewsModel> call, Throwable t) {
                     Toast.makeText(MainActivity.this,"Fail to get news",Toast.LENGTH_SHORT).show();
                 }
             });


    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}