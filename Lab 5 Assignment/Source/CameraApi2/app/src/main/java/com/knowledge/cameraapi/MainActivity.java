package com.knowledge.cameraapi;
import android.Manifest;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    final int IMAGE_GALLERY_REQUEST = 1;
    final int GPS_REQUEST = 2;
    private ImageView imgPicture;
    private Button button;
    private EditText editText;
    private ClarifaiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new ClarifaiBuilder("b119f3b6fe3046a8893918b0bf5b89e6")
                .client(new OkHttpClient())
                .buildSync();

        imgPicture = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(
                R.id.button);
        editText = (EditText) findViewById(R.id.editText);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_REQUEST);

    }

    public void onImageGalleryClicked(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();
                InputStream inStream;
                try {
                    inStream = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                    imgPicture.setImageBitmap(bitmap);
                    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    final byte[] imageBytes = outStream.toByteArray();
                    onImagePicked(imageBytes);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void onImagePicked(@NonNull final byte[] imageBytes) {

        new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
            @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
                final ConceptModel generalModel = client.getDefaultModels().generalModel();
                return generalModel.predict()
                        .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                        .executeSync();
            }

            @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                final List<ClarifaiOutput<Concept>> predictions = response.get();
                if (predictions.isEmpty()) {
                    return;
                }
                String output = predictions.get(0).data().get(0).name();
                GPSTracker g = new GPSTracker(getApplicationContext());
                Location l = g.getLocation();
                if (l != null) {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    findStores(lat, lon, output);
                }
            }
        }.execute();
    }

    private void findStores(Double longitude, Double latitude, String keyword) {
        String getURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + longitude + "," + latitude + "&" +
                "radius=500&" +
                "type=restaurant&" +
                "keyword=" + keyword + "&" +
                "key=AIzaSyAWW4efsxQuIlA0xh4u0hq9V_ICtQ8nYLY";
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(getURL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject jsonResult;
                    final JSONArray jsonArray;
                    final String result = response.body().string();
                    final String output;
                    try {
                        jsonResult = new JSONObject(result);
                        jsonArray = (JSONArray) jsonResult.get("results");

                        String tempOutput = "";
                        for (int i = 0 ; i< jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String name = (String)jsonObject.get("name");
                            String vicinity = (String)jsonObject.get("vicinity");
                            tempOutput += "name: [" + name + "]\n" + "location: [" + vicinity + "]\n\n";
                        }
                        output = tempOutput;

                        Log.d("okHttp", jsonResult.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editText.setText(output);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}