package info.duhovniy.maxim.movies.ui;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by maxduhovniy on 13/11/2015.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public void onStart() {
        super.onStart();

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng telAviv = new LatLng(0,0);
        mMap.addMarker(new MarkerOptions().position(telAviv).title("Tel Aviv"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(telAviv));
    }

}
