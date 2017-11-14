package com.androidlib.Utils;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadImage extends AsyncTask<String, Void, Boolean> {
    private String imageUrl;
    private File folder;
    private FileDownloadStatus fileDownloadStatus = null;
    boolean status = true;


    public DownloadImage(String id, String imageUrl,String portalName) {
        initVariables(id, imageUrl, portalName);
    }

    public DownloadImage(String id, String imageUrl, String portalName, FileDownloadStatus fileDownloadStatus) {
        this.fileDownloadStatus = fileDownloadStatus;
        initVariables(id, imageUrl, portalName);
    }

    private void initVariables(String id, String imageUrl, String portalName) {
        this.imageUrl = imageUrl;
        String folderPath = BarCodeFolderPaths.FOLDER_OMADRE;
        folder = new File(folderPath);
        if (!folder.exists())
            folder.mkdir();

        String folderWithPortalName = folderPath + File.separator + portalName;
        folder = new File(folderWithPortalName);
        if(!folder.exists())
            folder.mkdir();

        String folderWithID = folderWithPortalName + File.separator + id;
        folder = new File(folderWithID);
        if(!folder.exists())
            folder.mkdirs();
        if(!folder.exists())
            folder.mkdir();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        URL imageURL;
        File imageFile = null;
        InputStream is = null;
        FileOutputStream fos = null;
        byte[] b = new byte[1024];

        try {

            // get the input stream and pass to file output stream
            imageURL = new URL(imageUrl);
            imageFile = new File(folder.getAbsolutePath(), imageUrl.substring(imageUrl.length() - 1, imageUrl.length()) + ".png");
            if (!imageFile.exists())
                imageFile.createNewFile();
            fos = new FileOutputStream(imageFile);

            // get the input stream and pass to file output stream
            is = imageURL.openConnection().getInputStream();
            // also tried but gave same results :
            // is = imageURL.openStream();

            while (is.read(b) != -1)
                fos.write(b);

        } catch (FileNotFoundException e) {
            status = false;

        } catch (MalformedURLException e) {
            status = false;

        } catch (IOException e) {
            status = false;

        } finally {
            // close the streams
            try {
                if (fos != null)
                    fos.close();
                if (is != null)
                    is.close();
            } catch (IOException e) {
                status = false;
            }
        }

        return status;
    }

    @Override
    protected void onPostExecute(Boolean status) {
        super.onPostExecute(status);
        if (fileDownloadStatus != null) {
            if (status)
                fileDownloadStatus.onDownloadCompleted();
            else
                fileDownloadStatus.onDownloadFailure();
        }
    }

    public interface FileDownloadStatus {
        void onDownloadCompleted();

        void onDownloadFailure();
    }

    public interface BarCodeFolderPaths {
        String MAIN_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath();
        String FOLDER_OMADRE = MAIN_FOLDER + File.separator + "Omadre";

    }
}