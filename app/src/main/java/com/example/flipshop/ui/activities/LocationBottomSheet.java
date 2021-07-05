package com.example.flipshop.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.flipshop.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Locale;

public class LocationBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    LocationManager lm;
    double lati;
    double longi;
    String adress, pincode;
    Geocoder gc;

    ProgressDialog progressDialog;

    public LocationBottomSheet() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.locationbottomsheet, container, false);
        Button b1 = v.findViewById(R.id.apply);
        Button b2 = v.findViewById(R.id.gmaplocation);
        EditText ed = v.findViewById(R.id.pincode);
        gc = new Geocoder(getContext(), Locale.getDefault());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching the location...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ed.getText().toString().isEmpty()) {
                    mListener.onButtonClicked(ed.getText().toString());
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Please enter a valid address..", Toast.LENGTH_SHORT).show();
                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                getLocation();

                dismiss();
            }
        });
        return v;
    }

    private void getLocation() {
        int permissioncheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissioncheck1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissioncheck == PackageManager.PERMISSION_GRANTED && permissioncheck1 == PackageManager.PERMISSION_GRANTED) {
            lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // to get current one.,for every onesecond it will update.

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lati = location.getLatitude();
                    longi = location.getLongitude();

                    try {

                        List<Address> adr = gc.getFromLocation(lati, longi, 1);
                        // List<Address> adr = gc.getFromLocation(508243,1);
                        adress = adr.get(0).getAddressLine(0);
                        String statename = adr.get(0).getAdminArea();
                        String countryname = adr.get(0).getCountryName();
                        pincode = adr.get(0).getPostalCode();
                        String Code = adr.get(0).getCountryCode();
                        String k = adr.get(0).getLocality();
                        Log.d("MMMM", pincode);
                        mListener.onButtonClicked("DELIVER TO: " + adress);
                        progressDialog.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lm.removeUpdates(this);


                }


                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {
                    // when gps is on it will be called.
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // when gps is disabled it will be called
                }

            });

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 120);
        }

    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) getParentFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 120:
                int permissioncheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                int permissioncheck1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissioncheck == PackageManager.PERMISSION_GRANTED && permissioncheck1 == PackageManager.PERMISSION_GRANTED) {
                    lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    // to get current one.,for every onesecond it will update.

                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            lati = location.getLatitude();
                            longi = location.getLongitude();

                            try {

                                List<Address> adr = gc.getFromLocation(lati, longi, 1);
                                // List<Address> adr = gc.getFromLocation(508243,1);
                                adress = adr.get(0).getAddressLine(0);
                                String statename = adr.get(0).getAdminArea();
                                String countryname = adr.get(0).getCountryName();
                                pincode = adr.get(0).getPostalCode();
                                String Code = adr.get(0).getCountryCode();
                                String k = adr.get(0).getLocality();
                                //  Toast.makeText(getActivity(),pincode,Toast.LENGTH_LONG).show();
                                mListener.onButtonClicked( adress);
                                //  dismiss();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            lm.removeUpdates(this);


                        }


                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            // when gps is on it will be called.
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            // when gps is disabled it will be called
                        }

                    });

                }
                break;
        }
    }
}
