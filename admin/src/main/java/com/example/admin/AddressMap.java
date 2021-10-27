package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;
    private GoogleMap mMap;
    Location location;
    Marker marker1;
    MarkerOptions markerOptions;
    LatLng latLng, start_latLang, destination_latLng;
    Address address1;
    List<Address> start_addresses;
    String phone, id, email, username, uid, branch, order_id, delivery_type, token, address, admin_current_address;
    TextView currentLocation;
    String apiKey = "Yor API key";
    //polyline object
    private List<Polyline> polylines=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        }*/

        setContentView(R.layout.activity_address_map);


        phone = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        branch = getIntent().getStringExtra("branch");
        order_id = getIntent().getStringExtra("order_id");
        delivery_type = getIntent().getStringExtra("deliver_type");
        token = getIntent().getStringExtra("token");
        address = getIntent().getStringExtra("address");

        currentLocation = findViewById(R.id.admin_current_address);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);

        //check permission
        if (ActivityCompat.checkSelfPermission(AddressMap.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // when permission granted


            setMap();
            // init();
        } else {

            ActivityCompat.requestPermissions(AddressMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            Toast.makeText(AddressMap.this, "try again", Toast.LENGTH_LONG).show();
        }
    }

    private void setMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            Geocoder geocoder = new Geocoder(AddressMap.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                start_latLang = new LatLng(location.getLatitude(), location.getLongitude());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //  Toast .makeText(home_delivery.this, Html.fromHtml(addresses.get(0).getAddressLine(0)+"  "+addresses.get(0).getFeatureName()), Toast.LENGTH_LONG).show();

                            String loc = addresses.get(0).getSubLocality() + "," + addresses.get(0).getAddressLine(0);
                            if (addresses.get(0).getAddressLine(1) != null) {
                                loc = loc + "," + addresses.get(0).getAddressLine(1);
                            }

                            admin_current_address = loc;
                            currentLocation.setText(Html.fromHtml(loc));
                            ////////////////////////////////////////////////////////////////////////////           ////////////////////////////////////////////                ///////////////////////////////////////////
                            boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                                    .getString(R.string.style_json)));

                            if (!success) {
                                Toast.makeText(AddressMap.this, "not success", Toast.LENGTH_LONG).show();
                            }

                            mMap = googleMap;
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            try {
                                addresses = geocoder.getFromLocationName(address, 1);
                                address1 = addresses.get(0);
                                if (addresses.size() > 0) {
                                    latLng = new LatLng(address1.getLatitude(), address1.getLongitude());
                                    destination_latLng = latLng;
                                    markerOptions = new MarkerOptions().position(latLng)
                                            .title(address)
                                            .draggable(false);
                                    marker1 = mMap.addMarker(markerOptions);
                                    marker1.showInfoWindow();
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));


                                } else {
                                    Toast.makeText(AddressMap.this, "addresses size less han 0", Toast.LENGTH_LONG).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Findroutes(start_latLang,destination_latLng);
                        }
                    });
                }
            }
        });


    }
    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {
        if(Start==null || End==null) {
            Toast.makeText(AddressMap.this,"Unable to get location",Toast.LENGTH_LONG).show();
        }
        else
        {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener((RoutingListener) this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(apiKey)  //also define your api key here.
                    .build();
            routing.execute();
        }
    }
    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(AddressMap.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start_latLang);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);
                polylines.add(polyline);

            }
            else {

            }

        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start_latLang,destination_latLng);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start_latLang,destination_latLng);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        setMap();
    }
}