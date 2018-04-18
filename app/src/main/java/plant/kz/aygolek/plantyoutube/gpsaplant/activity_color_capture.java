package plant.kz.aygolek.plantyoutube.gpsaplant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import plant.kz.aygolek.plantyoutube.R;

public class activity_color_capture extends PlantPlaecesActivity implements View.OnClickListener {

    public static final int CAMERA_REQUEST_CODE = 228;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    private Button button;

    public static final int IMAGE_GALLERY_REQUEST = 20;
    private ImageView imgPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_capture);
        button = (Button)findViewById(R.id.btnImageGallery);
                button.setOnClickListener(this);

        imgPicture = (ImageView) findViewById(R.id.imgPicture);
    }


    @Override
    public int getCurrentMenuId() {
        return R.id.capturecolor;
    }

    public void  onImageGalleryClicked(){
        Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();
        //invoke image gallery with implicit intent, parameter, I want to pick something

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnImageGallery:
                Toast.makeText(this, "button clicked", Toast.LENGTH_SHORT).show();
                Intent photoPickerIntent=new Intent(Intent.ACTION_PICK);
                //where to find that
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();
                //finally get URI
                Uri data= Uri.parse(pictureDirectoryPath);
                //set the data and type, wo und welche daten bei starten des intents sollen angezeigt werden.
                //setdata wird meistens dazu verwernde um auf etwas zu zeigen
                photoPickerIntent.setDataAndType(data,"image/*");

                startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onTakePhotoClicked(View view){
        if(checkSelfPermission(android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            invokeCamera();
        }
        else {
            //let's ask permission
            String [] permissionRequest = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            //we have heard from camera request permission, and write external storage
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            }else {
                Toast.makeText(this, R.string.cannotOpenCamera, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void invokeCamera(){
        //get a fileReference
        Uri pictureUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", createImageFile());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //tell the camera where to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        //tell the camera to request file permission
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private File createImageFile() {
        //public pictue directory
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //timestamp makes unique name
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());

        //put together diroctory and timestamp to make unique image location
        File imageFile = new File(picturesDirectory, "image"+timestamp+".jpg");


        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);

                    imgPicture.setImageBitmap(imageBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "unable to open image", Toast.LENGTH_SHORT).show();
                }

            }
            if (requestCode == CAMERA_REQUEST_CODE){
                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG ).show();
            }
        }
    }
}
