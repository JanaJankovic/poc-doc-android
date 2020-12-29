package feri.pora.pocket_doctor.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import feri.pora.pocket_doctor.R;

public class RequestAnalysisFragment extends Fragment {
    private Button galleryButton;
    private Button cameraButton;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CAMERA_OPEN = 2;
    private static final int REQUEST_GALLERY = 3;

    String currentPhotoPath;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_request_analysis, null);

        bindGUI(rootView);
        
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGallery();
            }
        });
        
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickCamera();
            }
        });

        return  rootView;
    }

    private void bindGUI(View view){
        galleryButton = (Button) view.findViewById(R.id.buttonGallery);
        cameraButton = (Button) view.findViewById(R.id.buttonPhoto);
    }

    private void pickCamera() {
        askForPermission();
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }


    //PERMISSION
    private void askForPermission() {
        if(ContextCompat.checkSelfPermission(getActivity().getBaseContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity().getBaseContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

    //CREATE PHOTO
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getActivity().getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //OPEN CAMERA
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getBaseContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity().getBaseContext(),
                        "com.pora.poc.doc.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_OPEN);
            }
        }
    }

    //GET PATH
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CAMERA_OPEN) {
            if (resultCode == Activity.RESULT_OK) {
                File image = new File(currentPhotoPath);

                Bundle bundle = new Bundle();
                bundle.putString("filepath", Uri.fromFile(image).getPath());

                SendRequestFragment sendRequestFragment = new SendRequestFragment();
                sendRequestFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.nav_host_fragment, sendRequestFragment).commit();

            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                Bundle bundle = new Bundle();
                bundle.putString("filepath", getPathFromURI(contentUri));

                SendRequestFragment sendRequestFragment = new SendRequestFragment();
                sendRequestFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.nav_host_fragment, sendRequestFragment).commit();

            }
        }
    }
}