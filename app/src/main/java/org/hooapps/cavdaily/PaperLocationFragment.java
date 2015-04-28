package org.hooapps.cavdaily;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Location
{
    public String name;
    public String description;
    public float lat;
    public float lng;
}

public class PaperLocationFragment extends Fragment implements OnMapReadyCallback {

    List<Location> locations;
    // We don't use namespaces
    private static final String ns = null;

    public PaperLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paper_location, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(PaperLocationFragment.this);
        GoogleMap map = mapFragment.getMap();
        map.setMyLocationEnabled(true);
        android.location.Location location = map.getMyLocation();
        if (location != null) {
            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        XmlPullParser xmlParser = getResources().getXml(R.xml.papers);
        try {
            xmlParser.next();
            xmlParser.next();
            locations = readFeed(xmlParser);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        for(Location location:locations) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(location.lat, location.lng))
                    .title(location.name));
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Location> locationList = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "Document");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Placemark")) {
                locationList.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }

        return locationList;
    }

    private Location readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");

        Location location = new Location();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                location.name = readText(parser);
            } else if (name.equals("coordinates")) {
                String latlng = readText(parser);
                location.lng = Float.parseFloat(latlng.split(",")[0]);
                location.lat = Float.parseFloat(latlng.split(",")[1]);
            } else {
                skip(parser);
            }
        }

        return location;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
