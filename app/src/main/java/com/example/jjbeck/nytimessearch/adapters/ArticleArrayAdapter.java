package com.example.jjbeck.nytimessearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jjbeck.nytimessearch.R;
import com.example.jjbeck.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by jjbeck on 11/15/16.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    static public class ArticleViewHolder {
        TextView  tvTitle;
        ImageView ivImage;
    }


    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ArticleViewHolder viewHolder;  // View holder cached in the tag.

        // get the data (article) for the current position
        Article article = getItem(position);

        // Determine if the view is getting re-used...
        // If no view to re-use then inflate one and setup it's ViewHolder
        // Else - just get the existing viewHolder cached on the reused view.
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
            viewHolder  = new ArticleViewHolder();
            viewHolder.tvTitle = (TextView)  convertView.findViewById(R.id.tvTitle);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            // Re-using the view - so
            // just get the view holder that it has cached
            // and make sure any previous image is cleared out.
            viewHolder = (ArticleViewHolder) convertView.getTag();
            viewHolder.ivImage.setImageResource(0);
        }

        // Setup the data into the view now.
        // Populate the data into the template view using the data object
        viewHolder.tvTitle.setText(article.getHeadline());

        String imagePath = article.getThumbnail();
        if (!TextUtils.isEmpty(imagePath)) {
            // Use the Picasso library to get the URL and load the
            // image into the Layouts ImageView.  This is a ba
            Picasso.with(getContext())
                    .load(imagePath)
                    .transform(new RoundedCornersTransformation(10, 10))
                    .error(R.drawable.oh_no)
                    .into(viewHolder.ivImage);
        }
        return convertView;
    }
}
