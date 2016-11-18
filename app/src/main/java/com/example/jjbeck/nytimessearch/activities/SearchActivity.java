package com.example.jjbeck.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jjbeck.nytimessearch.R;
import com.example.jjbeck.nytimessearch.adapters.ArticleArrayAdapter;
import com.example.jjbeck.nytimessearch.listeners.EndlessScrollListener;
import com.example.jjbeck.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    public static String INTENT_EXTRA_URL     = "url";
    public static String INTENT_EXTRA_ARTICLE = "article";
    private String NYTimesSearchURL  = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    private String NYTimesAppId      = "b87a0671a5ae43d6921ca507e907f319";
    private String PARAM_API_KEY     = "api-key";
    private String PARAM_PAGE        = "page";       // values = any string to search for
    private String PARAM_QUERY       = "q";          // values = any string to search for
    private String PARAM_SORT        = "sort";       // values = SortNewest | SortOldest
    private String SORTVAL_NEWEST    = "newest";
    private String SORTVAL_OLDEST    = "oldest";
    private String PARAM_START_DATE  = "begin_date"; // values = ??
    private String PARAM_END_DATE    = "end_date";   // values = ??

    private EditText etQuery;
    private Button   btnSearch;
    private GridView gvResults;
    private ArrayList<Article> articles = null;
    private ArticleArrayAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();

        setupViews();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        setupSearchOnMenu(menu);
        return true;
    }

    /**
     * Set up the search menu and it's actions when creating the full menu.
     * @param menu
     */
    private void setupSearchOnMenu(Menu menu) {

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        // listener when search entered (Why not onClick?)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                // Snackbar just needs any view - so use etQuery since as good as any.
                Snackbar.make(etQuery, "Query String Entered: '" + query +"'", Snackbar.LENGTH_LONG).show();
                executeSearchForQuery(query, 0);

                // workaround to avoid issues with some emulators and keyboard devices
                // firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: JJB: Assuming this is for each keystroke - but want to verify.
                return false;
            }
        });
    }

    private void setupViews() {
        etQuery   = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button)   findViewById(R.id.btnSearch);
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles  = new ArrayList<Article>();
        adapter   = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        // hook up listener for the grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // create an intent to display the article (webUrl)
                // can't use this for content - so get from application!
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                // get the article's webUrl to display
                Article article = articles.get(position);

                // pass the article into the intent (remember to now use Serializable)
                //intent.putExtra(INTENT_EXTRA_URL, article.getWebUrl());
                //intent.putExtra(INTENT_EXTRA_ARTICLE, article); // when serializable
                intent.putExtra(INTENT_EXTRA_ARTICLE, Parcels.wrap(article));

                // launch the activity
                startActivity(intent);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                loadNextDataFromApi(page);
                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Search button clicked ...
     */
    @Deprecated
    public void onArticleSearch(View view) {
        final String query = etQuery.getText().toString();
        executeSearchForQuery(query, 0);
    }


    /**
     * Given the string to query/serach for - call New York Times and
     * @param query
     */
    private void executeSearchForQuery(final String query, int page) {

        Snackbar.make(etQuery, "Search For: '" + query + "'", Snackbar.LENGTH_SHORT).show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(PARAM_API_KEY, NYTimesAppId);
        params.put(PARAM_PAGE, page);
        params.put(PARAM_QUERY, query);

        client.get(NYTimesSearchURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Snackbar.make(etQuery, "Failed to get content for: '" + query + "'", Snackbar.LENGTH_LONG).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");

                    // Alternative way to hand the data to the adapter -
                    // this way the adapter knows that the data is changing.
                    adapter.clear();
                    adapter.addAll(Article.fromJsonArray(articleJsonResults));
                    // more manual method:
                    // articles.addAll(Article.fromJsonArray(articleJsonResults));
                    // adapter.notifyDataSetChanged();

                    // NOTE: The handle to the articles ArrayList has been handed to the adapter to manage.
                    //       However, it is still the same handle, so 'articles' is still showing the current list.
                    Log.d("DEBUG articles: ", articles.toString());
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int page) {
        Toast.makeText(this, "Getting more data for page: " + page, Toast.LENGTH_SHORT).show();
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
    }
}
