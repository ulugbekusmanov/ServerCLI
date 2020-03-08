package com.ulsa.server_client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = "GalleryActivity";
    private RecyclerView photoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Log.d(TAG,"u");
        photoRecyclerView = findViewById(R.id.recycleview);
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        new FetchItemTask().execute();
        setupAdapter();

    }

    private void setupAdapter() {
        photoRecyclerView.setAdapter(new PhotoAdapter(mItems));
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            itemView = itemView.findViewById(R.id.photo_gallery_image_veiw);

        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItem;

        public PhotoAdapter(List<GalleryItem> items) {
            mGalleryItem = items;

        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = new LayoutInflater(GalleryActivity.this) {
                @Override
                public LayoutInflater cloneInContext(Context context) {
                    return null;
                }
            };
            View v = inflater.inflate(R.layout.gallery_item, viewGroup, false);

            return new PhotoHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder photoHolder, int i) {
            GalleryItem galleryItem = mGalleryItem.get(i);
            Picasso.get().load(galleryItem.getUrl()).into(photoHolder.itemImageView);
        }

        @Override
        public int getItemCount() {
            return mGalleryItem.size();
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new FlickerFetcher().fitchItems();

        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setupAdapter();

        }
    }
}
