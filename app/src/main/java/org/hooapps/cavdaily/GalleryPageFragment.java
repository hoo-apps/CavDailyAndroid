package org.hooapps.cavdaily;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GalleryPageFragment extends Fragment {

    public static final String ARG_MEDIA_URL = "arg_media_url";
    public static final String ARG_POSITION = "arg_position";
    public static final String ART_TOTAL_SIZE = "arg_total_size";

    public GalleryPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery_page_view, container, false);

        // Get the views from the layout
        ImageView imageView = (ImageView) rootView.findViewById(R.id.primary_image);
        TextView positionDisplayView = (TextView) rootView.findViewById(R.id.position_display);

        String mediaUrl = getArguments().getString(ARG_MEDIA_URL);
        int position = getArguments().getInt(ARG_POSITION);
        int totalSize = getArguments().getInt(ART_TOTAL_SIZE);

        // Load the data into the views
        Picasso.with(getActivity()).load(mediaUrl).placeholder(R.drawable.article_filler).into(imageView);
        positionDisplayView.setText(String.format("%d/%d", position, totalSize));

        return rootView;
    }


}
