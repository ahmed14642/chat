package com.example.android.uni;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class HttpUpload extends AsyncTask<Void, Integer, Integer> {

    private Context context;
    private String imgPath;
    private String groupid;
    int serverResponseCode = 0;


    public HttpUpload(Context context, String imgPath,String groupid) {
        super();
        this.context = context;
        this.imgPath = imgPath;
        this.groupid = groupid;
        Log.e("uploadFilep",imgPath);
    }


    @Override
    protected Integer doInBackground(Void... params) {

        String upLoadServerUri = "http://192.168.10.121:8080/uploadimage";

        String username="ahmed14642";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(imgPath);
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File Does not exist");
            return 0;
        }
        try { // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            Log.i("uploadFile", "" + fileInputStream);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", imgPath);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);

            String phone=groupid;
            dos.writeBytes("Content-Disposition: form-data; name=\"phone\"" + lineEnd);
            //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
            //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(phone); // mobile_no is String variable
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);




            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ imgPath + "\"" + lineEnd);
            Log.e("uploadFilep", imgPath);
            dos.writeBytes(lineEnd);


            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();

            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            e.printStackTrace();

            Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
        }
return null;
    }
}
