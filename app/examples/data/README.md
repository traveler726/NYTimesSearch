
NYTimes Search Info
-------------------
* [Search API Documentation](http://developer.nytimes.com/article_search_v2.json#/Documentation/GET/articlesearch.json)
* [NY Times API FAQ](http://developer.nytimes.com/faq#1)
* app-id: <b>b87a0671a5ae43d6921ca507e907f319</b>

NYTimes Search URL:
-------------------
* template: https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=&lt;app_id&gt;&q=&lt;query_string&gt;

NYTimes Search URL:
-------------------
* template: https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=&lt;app_id&gt;&q=&lt;query_string&gt;
* Base URL: <b>https://api.nytimes.com/svc/search/v2/articlesearch.json</b>
* query string: <b>Android</b>
* Example: <b>https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=b87a0671a5ae43d6921ca507e907f319&q=Android</b>

NYTimes Search Results:
-------------------
* nytimes.search.android.json
* Rate Limits: 
   * curl --head https://api.nytimes.com/svc/books/v3/lists/overview.json?api-key=b87a0671a5ae43d6921ca507e907f319 2>/dev/null | grep -i "X-RateLimit" > nytimes.rate.limits
   * curl --head https://api.nytimes.com/svc/books/v3/lists/overview.json?api-key=b87a0671a5ae43d6921ca507e907f319 2>/dev/null 
