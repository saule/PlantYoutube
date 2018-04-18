package plant.kz.aygolek.plantyoutube.gpsaplant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import plant.kz.aygolek.plantyoutube.DAO.IOfflinePlantDAO;
import plant.kz.aygolek.plantyoutube.DAO.IPlantDAO;
import plant.kz.aygolek.plantyoutube.DAO.ISpecimenDAO;
import plant.kz.aygolek.plantyoutube.DAO.OfflinePlantDAO;
import plant.kz.aygolek.plantyoutube.DAO.OfflineSpecimenDAO;
import plant.kz.aygolek.plantyoutube.DAO.PlantDAO;
import plant.kz.aygolek.plantyoutube.DTO.PlantDTO;
import plant.kz.aygolek.plantyoutube.DTO.SpecimenDTO;
import plant.kz.aygolek.plantyoutube.R;


public class GPSAPLant extends PlantPlaecesActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GestureDetector.OnGestureListener {

    private AutoCompleteTextView autoCompleteTextPlantName;
    final static int CAMERA_REQUEST = 10;

    private TextView lblLatitudeValue;
    private TextView lblLongitudeValue;


    private FusedLocationProviderApi locationProviderApi = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private final static int MILLISECONDS_PER_SECOND = 1000;
    public final static int MINUTE = 60 * MILLISECONDS_PER_SECOND; //frequency of gsp request update
    private double longitude;
    private double latitude;
    private boolean pause = false;
    private Button btnPause;
    private AutoCompleteTextView autoComplLocation;
    private AutoCompleteTextView autoComplDescription;
    private ImageView imgSpecimenPhoto;

    private ProgressDialog plantProgresDialog;
    private long cacheID;
    private int guid;
    ISpecimenDAO specimenDAO;
    private Uri pictureUri;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //associate the layout with this activity
        setContentView(R.layout.activity_gpsaplant);

        autoCompleteTextPlantName = findViewById(R.id.autoCompleteTextPlantName);
        imgSpecimenPhoto = findViewById(R.id.imgSpecimenPhoto);
        lblLatitudeValue = findViewById(R.id.lblLatitudeValue);
        lblLongitudeValue = findViewById(R.id.lblLongitudeValue);
        autoComplLocation = findViewById(R.id.actLocation);
        autoComplDescription = findViewById(R.id.actDescription);

        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        //initialize the location request with the acuracy and frequence in whic we want gps updates
        locationRequest = new LocationRequest();
        //I will request update every minute
        locationRequest.setInterval(MINUTE);
        //I will see any other application update it all 15 second
        locationRequest.setFastestInterval(15 * MILLISECONDS_PER_SECOND);
        //wie genau soll location seit, wegen energiebedarf
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        btnPause = findViewById(R.id.btnPause);

        //getPlantNames for our autocompletetextview,
        // the result that is showing up in autocompletetext is the toString in plandto!!
        PlantSearchTask pet = new PlantSearchTask();
        pet.execute("redbud");

        //create an instance of the PlantSelected Listener
        PlantSelected ps = new PlantSelected();

        //subscribe autocompleteTextPlantName to this PlantSelected Listener
        autoCompleteTextPlantName.setOnItemClickListener(ps);
        autoCompleteTextPlantName.setOnItemSelectedListener(ps);

        specimenDAO = new OfflineSpecimenDAO(this);

        detector = new GestureDetector(this,this);
    }

    public void btnShowSavedClicked(View view) {
        // String PlantName= autoCompleteTextPlantName.getText().toString();
        //Toast.makeText(this,PlantName, Toast.LENGTH_LONG).show();
        // setContentView(R.layout.specimenshow);
        Intent specimenShowIntent = new Intent(this,
                SpecimenShowActivity.class);
        startActivity(specimenShowIntent);
        //  Log.i("Content "," Main layout ");
    }

    public void btnPauseClicked(View view) {
        // Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();
        if (!pause) {
            PauseGPS();
            pause = true;
            btnPause.setText(getString(R.string.lblGps_resume));
            Toast.makeText(this, "paused", Toast.LENGTH_SHORT).show();
        } else {
            resumeGPS();
            pause = false;
            btnPause.setText(getString(R.string.lblGps_pause));
            Toast.makeText(this, "Resumed", Toast.LENGTH_SHORT).show();
        }


    }


    public void onSaveClicked(View view) {
        //create DTO to store our specimen information
        SpecimenDTO specimen = new SpecimenDTO();

        specimen.setCache_id(cacheID);
        specimen.setGuid(guid);
        specimen.setLocation(autoComplLocation.getText().toString());
        specimen.setDescription(autoComplDescription.getText().toString());
        specimen.setLatitude(Double.toString(latitude));
        specimen.setLongitude(Double.toString(longitude));

        if (pictureUri != null) {
            specimen.setPicture_URI(pictureUri.toString());
        }


        try {
                // specimenDAO.save(specimen);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference root = database.getReference();
            DatabaseReference fooDBReference = root.child("foo");
            fooDBReference.push().setValue(specimen);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.unableToSaveSpecimen, Toast.LENGTH_LONG).show();
        }
    }

    public void btnTakePhotoClicked(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File pictureDirectory;
        pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String pictureName = getPictureName();

        File imageFile = new File(pictureDirectory, pictureName);
        pictureUri = Uri.fromFile(imageFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "PlantPlacesImage" + timestamp + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                //we aare hearing bavk from camera
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                //at this point, we have the image from camera
                imgSpecimenPhoto.setImageBitmap(cameraImage);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGPS();

    }

    private void resumeGPS() {
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PauseGPS();
    }

    private PendingResult<Status> PauseGPS() {
        return LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public int getCurrentMenuId() {
        return R.id.gpsaplant;
    }


    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
    }


    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
       // Toast.makeText(this, "location changed: latitude = "+
                //location.getLatitude()+", longitude = "+location.getLongitude(), Toast.LENGTH_LONG).show();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        lblLatitudeValue.setText(Double.toString(latitude));
        lblLongitudeValue.setText(Double.toString(longitude));


    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Intent specimenShowIntent = new Intent(this,
                SpecimenShowActivity.class);
        startActivity(specimenShowIntent);

        return true;
    }

    //interclass for threads with asynctask, so it loads data without interrupting other actions
    //parameter string, we search in plant name, progress we want to show process comlete, so also integer and list of plants for the result
    class PlantSearchTask extends AsyncTask<String, Integer, List<PlantDTO>> {

        @Override
        protected void onPostExecute(List<PlantDTO> plantDTOS) {
            super.onPostExecute(plantDTOS);
            plantProgresDialog.dismiss();

            //set adapter, how it should show results, each plant in one row ->simplelistitem 1
            ArrayAdapter<PlantDTO> plantAdapter =
                    new ArrayAdapter<PlantDTO>(GPSAPLant.this.getApplicationContext(),android.R.layout.simple_list_item_1,plantDTOS);
            //adapter takes collection of data
            autoCompleteTextPlantName.setAdapter(plantAdapter);
        }

        @Override
        protected List<PlantDTO> doInBackground(String... params) {
            publishProgress(1);

            IPlantDAO plantDAO = new PlantDAO();
            IOfflinePlantDAO offlinePLantDAO = new OfflinePlantDAO(GPSAPLant.this);

            List<PlantDTO> allPlants = new ArrayList<>();//
            int countPlants = offlinePLantDAO.countPlants();//

            int plantCounter = 0;


            if (countPlants<1000){
                try{
                    publishProgress(2);
                    allPlants= plantDAO.fetchPlants(params[0]);

                    publishProgress(3);
                    Set<Integer> localGuids = offlinePLantDAO.fetchAllGuids();

                    for (PlantDTO aPlant : allPlants) {
                        if (aPlant.getGuid()>0 && !localGuids.contains(Integer.valueOf(aPlant.getGuid()))){
                            offlinePLantDAO.insert(aPlant);
                        }

                        //update the progress
                        plantCounter++;

                        if (allPlants.size()>25)
                            if(plantCounter%(allPlants.size()/25) == 0){
                                publishProgress(plantCounter*100/allPlants.size());
                           }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try{
                        allPlants = offlinePLantDAO.fetchPlants(params[0]);

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            return allPlants;

        }

        @Override
        protected void onPreExecute() {
            //setup plantProgress dialog
            plantProgresDialog = new ProgressDialog(GPSAPLant.this);
            plantProgresDialog.setCancelable(true);
            plantProgresDialog.setProgressStyle(0);
            plantProgresDialog.setMax(100);
            plantProgresDialog.setMessage("Downloading plant names");

            //make a button
            //plantProgresDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (DialogInterface.OnClickListener) (dialog, which) ->{
              //  dialog.dismiss();
            //});

            plantProgresDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            plantProgresDialog.setProgress(values[0]);
        }
    }

    class PlantSelected implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //get selected Item
            PlantDTO plant = (PlantDTO) autoCompleteTextPlantName.getAdapter().getItem(position);
            cacheID = plant.getCacheID();
            guid = plant.getGuid();

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //get the selected item
            PlantDTO plant = (PlantDTO) autoCompleteTextPlantName.getAdapter().getItem(position);
            cacheID = plant.getCacheID();
            guid = plant.getGuid();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
