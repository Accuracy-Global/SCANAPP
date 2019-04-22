package cheemala.business.durgacameraapp;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import cheemala.business.durgacameraapp.activity.DisplayDataActivity;
import cheemala.business.durgacameraapp.database.AppDb;
import cheemala.business.durgacameraapp.utils.Constants;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "MainActivity_";
    private ImageView imgVw;
    private ImageView camImg;
    private Button camBtn;
    private Uri curntImgUri;
    private TextRecognizer detector;
    private AppDb appDb;
    private ImageView historyImg;
    private LinearLayout scanLyot;
    private final static int CAMERA_REQUEST_CODE = 111;
    private final static int MY_PERMISSIONS_REQUEST_CAMERA_REQUEST_CODE = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateViews();
    }

    private void initiateViews() {

        imgVw = findViewById(R.id.imgVw);
        scanLyot = findViewById(R.id.scan_lyot);
        historyImg = findViewById(R.id.history_img);
        scanLyot.setBackgroundResource(R.drawable.circular_bckgrnd_stroke);
        camImg = findViewById(R.id.cam_img);
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        camBtn = findViewById(R.id.sbmt_btn);

        historyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,DisplayDataActivity.class));
            }
        });

        camBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    scanLyot.setBackgroundResource(R.drawable.circular_bckgrnd_stroke);
                    camBtn.setTextColor(getResources().getColor(R.color.dark_grey));
                    camImg.setImageResource(R.drawable.scan_cam_icon_grey);
                    checkForDynamicPermissions();
                    return true;
                }else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    scanLyot.setBackgroundResource(R.drawable.circular_bckgrnd_fill);
                    camBtn.setTextColor(getResources().getColor(R.color.white));
                    camImg.setImageResource(R.drawable.scan_cam_icon_white);
                    return true;
                }else {}
                return false;

            }
        });
        appDb = new AppDb(this);

    }

    private void checkForDynamicPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CAMERA_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            initiateCamera();
        }

    }

    private void initiateCamera() {

        /*ContentValues values1 = new ContentValues();
        values1.put(Constants.DB_COLUMN_TIME_STAMP,getCurrentTimeStamp());
        values1.put(Constants.DB_COLUMN_TEXT_VALUE,"Hi Cheemala. How are you? Hope you are fine and doing good bro!");
        appDb.addCapturedText(values1);

        ContentValues values2 = new ContentValues();
        values2.put(Constants.DB_COLUMN_TIME_STAMP,getCurrentTimeStamp());
        values2.put(Constants.DB_COLUMN_TEXT_VALUE,"Hi Cheemala. How are you? Hope you are fine and doing good bro!");
        appDb.addCapturedText(values2);


        ContentValues values3 = new ContentValues();
        values3.put(Constants.DB_COLUMN_TIME_STAMP,getCurrentTimeStamp());
        values3.put(Constants.DB_COLUMN_TEXT_VALUE,"Hi Cheemala. How are you? Hope you are fine and doing good bro! Whatsap man");
        appDb.addCapturedText(values3);

        ContentValues values4 = new ContentValues();
        values4.put(Constants.DB_COLUMN_TIME_STAMP,getCurrentTimeStamp());
        values4.put(Constants.DB_COLUMN_TEXT_VALUE,"Hi Cheemala. How are you? Hope you are fine and doing good bro! Whatsap dude");
        appDb.addCapturedText(values4);

        ContentValues values5 = new ContentValues();
        values5.put(Constants.DB_COLUMN_TIME_STAMP,getCurrentTimeStamp());
        values5.put(Constants.DB_COLUMN_TEXT_VALUE,"Hi Cheemala. How are you? Hope you are fine and doing good bro!");
        appDb.addCapturedText(values5);

        ContentValues values6 = new ContentValues();
        values6.put(Constants.DB_COLUMN_TIME_STAMP,getCurrentTimeStamp());
        values6.put(Constants.DB_COLUMN_TEXT_VALUE,"Hi Cheemala. How are you? Hope you are fine and doing good bro! What the heck is happening!");
        appDb.addCapturedText(values6);

        navToDataDisply();*/

        File myImgFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+""+ File.separator,"chaitu.jpg");
        if (!myImgFile.exists()){
            try {
                myImgFile.createNewFile();
                Log.d(""+TAG,"file created!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            curntImgUri = FileProvider.getUriForFile(this,"cheemala.business.durgacameraapp.fileprovider",myImgFile);
        }else {
            curntImgUri = Uri.fromFile(myImgFile);
        }
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,curntImgUri);
        startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

            //if (data != null){
                //Bitmap imgData = (Bitmap) data.getExtras().get("data");
                //imgVw.setImageBitmap(imgData);
                startImageCrop(curntImgUri);
            //}

        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                curntImgUri = resultUri;
                if (resultUri != null){
                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    imgVw.setImageURI(resultUri);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgVw.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    doOCR(bitmap);
                    //Toast.makeText(this,"Image Successfully Cropped!",Toast.LENGTH_LONG).show();
                }
            }
        }

       /* if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (resultUri != null){
                    startImageCrop(resultUri);
                }
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
                Uri cropResultUri = cropResult.getUri();
                if (cropResultUri != null){
                    //startImageCrop(resultUri);
                    Toast.makeText(MainActivity.this,"Image Cropped!",Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/

    }

    private void scanForText(Bitmap bitmap) {
        String blocks = "";
        String lines = "";
        String words = "";

        try {

            //Bitmap bitmap = decodeBitmapUri(this, resultUri);
            if (detector.isOperational() && bitmap != null) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = detector.detect(frame);
                for (int index = 0; index < textBlocks.size(); index++) {
                    //extract scanned text blocks here
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue() + "\n" + "\n";
                    for (Text line : tBlock.getComponents()) {
                        //extract scanned text lines here
                        lines = lines + line.getValue() + "\n";
                        for (Text element : line.getComponents()) {
                            //extract scanned text words here
                            words = words + element.getValue() + ", ";
                        }
                    }
                }
            }

            Toast.makeText(MainActivity.this,"Blocks: "+blocks,Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this,"Lines: "+lines,Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this,"Words: "+words,Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, e.toString());
        }

    }

    private void startImageCrop(Uri resultUri) {
        CropImage.activity(resultUri)
                .setActivityTitle(getResources().getString(R.string.crop_activity_titl_txt))
                .setBorderLineColor(R.color.white)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setGuidelinesColor(R.color.white)
                .setActivityMenuIconColor(R.color.white)
                .start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initiateCamera();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private Bitmap decodeBitmapUri(MainActivity mainActivity, Uri resultUri) {
        int targetW = 600;
        int targetH = 600;

        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(mainActivity.getContentResolver().openInputStream(resultUri), null, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            return BitmapFactory.decodeStream(mainActivity.getContentResolver().openInputStream(resultUri), null, bmOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void doOCR (final Bitmap bitmap) {

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (textRecognizer.isOperational()){
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textItems = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <textItems.size(); i++){
                TextBlock tb = textItems.valueAt(i);
                sb.append(tb.getValue());
                sb.append("\n");
            }

            if (!sb.toString().contentEquals("")){
                showPopUpDialog(sb.toString());
                return;
            }
            Toast.makeText(this,"Oops! Please try capturing again!",Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(this,"TextRecognizer malfunctioned!",Toast.LENGTH_LONG).show();
        }

       /* tOcr = new TOCR(this,"eng");
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, "Processing",
                    "Doing OCR...", true);
        } else {
            mProgressDialog.show();
        }
        //final ProgressDialog finalMProgressDialog = mProgressDialog;
        final ProgressDialog finalMProgressDialog = mProgressDialog;
        new Thread(new Runnable() {
            public void run() {
                final String srcText = tOcr.getOCRResult(bitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (srcText != null && !srcText.equals("")) {
                            //srcText contiene el texto reconocido
                            //Toast.makeText(this,"OCR Text: "+srcText,Toast.LENGTH_LONG).show();
                            Log.d(""+TAG,""+srcText);
                        }
                        tOcr.onDestroy();
                        finalMProgressDialog.dismiss();
                    }
                });
            }
        }).start();*/
    }

    private void navToDataDisply(String sb) {

        if (!sb.contentEquals("")){
            ContentValues values = new ContentValues();
            values.put(Constants.getDbColumnTextValue(),sb.toString());
            values.put(Constants.getDbColumnTimeStamp(),getCurrentTimeStamp());
            if (appDb.addCapturedText(values) != -1){
                startActivity(new Intent(MainActivity.this, DisplayDataActivity.class));
            }else {
                Toast.makeText(MainActivity.this,"Oops!!! Something went wrong!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getCurrentTimeStamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c.getTime());
    }

    public void showPopUpDialog(final String msg){

        final Dialog dialog = new Dialog(MainActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.pop_up_layout);
        // Set dialog title
        dialog.setTitle(R.string.pop_up_ttl);
        // set values for custom dialog components - text, image and button
        final EditText scndEdt = (EditText) dialog.findViewById(R.id.scanned_text_edt);
        scndEdt.setText(msg);
        Button acceptBtn = (Button) dialog.findViewById(R.id.accept_btn);
        Button dscrdBtn = (Button) dialog.findViewById(R.id.discard_btn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String finalMsg = scndEdt.getText().toString();
                navToDataDisply(finalMsg);
            }
        });

        dscrdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
