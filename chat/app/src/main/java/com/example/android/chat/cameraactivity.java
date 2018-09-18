package com.example.android.uni;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by AHMAD BUTTAR on 1/27/2016.
 */
public class cameraactivity extends Activity {
    ImageView image;
    Activity context;
    Preview preview;
    Camera camera;
    Button exitButton;
    ImageView fotoButton;
    LinearLayout progressLayout;
    String path = "/sdcard/KutCamera/cache/images/";
    String groupid;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        Intent i = getIntent();
        groupid = i.getStringExtra("groupid");


        context = this;

        fotoButton = (ImageView) findViewById(R.id.imageView_foto);
        exitButton = (Button) findViewById(R.id.button_exit);
        image = (ImageView) findViewById(R.id.imageView_photo);
        progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

        preview = new Preview(this,
                (SurfaceView) findViewById(R.id.KutCameraFragment));
        FrameLayout frame = (FrameLayout) findViewById(R.id.preview);
        frame.addView(preview);
        preview.setKeepScreenOn(true);
        fotoButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    takeFocusedPicture();
                } catch (Exception e) {

                }
                exitButton.setClickable(false);
                fotoButton.setClickable(false);
                progressLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void deleteImageFile(String path) {
        File file = new File(path);
        if (file != null && file.exists())
            file.delete();
    }

    public int setPhotoOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO Auto-generated method stub
        if (camera == null) {
            camera = Camera.open();
            camera.startPreview();
            camera.setErrorCallback(new ErrorCallback() {
                public void onError(int error, Camera mcamera) {

                    camera.release();
                    camera = Camera.open();
                    Log.d("Camera died", "error camera");

                }
            });
        }
        if (camera != null) {
            if (Build.VERSION.SDK_INT >= 14)
                setCameraDisplayOrientation(context,
                        CameraInfo.CAMERA_FACING_BACK, camera);
            preview.setCamera(camera);
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId,
                                             android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {

            try {
                camera.takePicture(mShutterCallback, null, jpegCallback);
            } catch (Exception e) {

            }

        }
    };

    Camera.ShutterCallback mShutterCallback = new ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };

    public void takeFocusedPicture() {
        camera.autoFocus(mAutoFocusCallback);

    }

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            // Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        @SuppressWarnings("deprecation")
        public void onPictureTaken(byte[] data, Camera camera) {
            String pathto=null;
            Calendar c = Calendar.getInstance();
            try {
                //write the file
                pathto=path + c.getTime().getSeconds() + ".jpg";
                FileOutputStream fos = new FileOutputStream(pathto);
                Bitmap bm = null;

                // COnverting ByteArray to Bitmap - >Rotate and Convert back to Data
                if (data != null) {
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
                    bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        // Notice that width and height are reversed
                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                        int w = scaled.getWidth();
                        int h = scaled.getHeight();
                        // Setting post rotate to 90
                        Matrix mtx = new Matrix();

                        int CameraEyeValue = setPhotoOrientation(cameraactivity.this, 0); // CameraID = 1 : front 0:back

                            mtx.postRotate(CameraEyeValue); // CameraEyeValue is default to Display Rotation


                        bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                    } else {// LANDSCAPE MODE
                        //No need to reverse width and height
                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                        bm = scaled;
                    }
                }
                // COnverting the Die photo to Bitmap


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                fos.write(byteArray);
                //fos.write(data);
                fos.close();



            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }finally {
                Intent i = new Intent(getApplicationContext(), filterpic.class);
                Bundle bundle = new Bundle();
                bundle.putString("photoPath",pathto);
                bundle.putString("groupid",groupid);
                i.putExtras(bundle);
                startActivity(i);

            }


        }
    };

    public static Bitmap rotate(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, false);
    }


    class Preview extends ViewGroup implements SurfaceHolder.Callback {
    private final String TAG = "Preview";

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    int heightmax ;
    int widthmax ;
    Size mPreviewSize;
    List<Size> mSupportedPreviewSizes;
    Camera mCamera;

    @SuppressWarnings("deprecation")
    Preview(Context context, SurfaceView sv) {
        super(context);

        mSurfaceView = sv;
//        addView(mSurfaceView);

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPictureSizes();
            requestLayout();

            // get Camera parameters
            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // set the focus mode
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                // set Camera parameters
                mCamera.setParameters(params);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {

            mPreviewSize=maxSize();

        }
    }

    public Size maxSize(){

//    	heightmax =0;
//    	widthmax =0;
        Size sizeMax=mSupportedPreviewSizes.get(0);
        //long totalsize = heightmax*widthmax;
        //long maxsize=mSupportedPreviewSizes.get(0).height*mSupportedPreviewSizes.get(0).width;

        for(Size size:mSupportedPreviewSizes){
            if(size.height*size.width>sizeMax.width*sizeMax.height){
                sizeMax = size;

            }
        }

        return sizeMax;
//    	for(int i = 0;i<mSupportedPreviewSizes.size();i++){
//    		if(maxsize>totalsize){
//    			heightmax = mSupportedPreviewSizes.get(i).height;
//    			widthmax = mSupportedPreviewSizes.get(i).width;
//    			totalsize=maxsize;
//    		}
//    	}


    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize.width;
                previewHeight = mPreviewSize.height;
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                final int scaledChildWidth = previewWidth * height / previewHeight;
                child.layout((width - scaledChildWidth) / 2, 0,
                        (width + scaledChildWidth) / 2, height);
            } else {
                final int scaledChildHeight = previewHeight * width / previewWidth;
                child.layout(0, (height - scaledChildHeight) / 2,
                        width, (height + scaledChildHeight) / 2);
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }



    Camera.AutoFocusCallback mnAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {



        }
    };
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if(mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(mPreviewSize.width, mPreviewSize.height);
            requestLayout();

            mCamera.setParameters(parameters);
            mCamera.startPreview();

        }
    }

}
}





