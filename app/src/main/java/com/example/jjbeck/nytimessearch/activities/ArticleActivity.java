package com.example.jjbeck.nytimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.jjbeck.nytimessearch.R;
import com.example.jjbeck.nytimessearch.models.Article;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the article's web url passed in
        //String url = getIntent().getExtras().getString(SearchActivity.INTENT_EXTRA_URL);
        Article article = (Article) getIntent().getExtras().getSerializable(SearchActivity.INTENT_EXTRA_ARTICLE);

        WebView wvArticle = (WebView) findViewById(R.id.wvArticle);

        wvArticle.setWebViewClient(new WebViewClient() {
            // Requires API 21 and currently min is API 19!
            // @Override
            // public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //    view.loadUrl(request.getUrl().toString());
            //    return true;
            //}

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        wvArticle.loadUrl(article.getWebUrl());
    }

}
