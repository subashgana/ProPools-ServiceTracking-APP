package com.servicetracker.constants;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.janmuller.android.simplecropimage.CropImage;

/** this class is used for image operation */

public  class TakePictureUtils {

  public static final int TAKE_PICTURE = 101;
  public static final int PICK_GALLERY = 201;
  public static final int CROP_FROM_CAMERA = 301;


   /** this method is used for take picture from camera */
   public static void takePicture(Activity context,  String fileName) {

       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

          try {
              Uri mImageCaptureUri = null;
              mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName+".jpg"));
              intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
              intent.putExtra("return-data", true);
              context.startActivityForResult(intent, TAKE_PICTURE);
             } catch (ActivityNotFoundException ignored) {
          }catch (Exception ex) {
              ex.printStackTrace();

          }
      }


   /** this method is used for take picture from gallery */
   public static void openGallery(Activity context) {

          Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
          photoPickerIntent.setType("image/*");
          context.startActivityForResult(photoPickerIntent, PICK_GALLERY);
      }



    /** this method is used for open crop image */
  public static void startCropImage(Activity context, String fileName) {

          Intent intent = new Intent(context, CropImage.class);
          intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp") ,fileName).getPath());
          intent.putExtra(CropImage.SCALE, true);
          intent.putExtra(CropImage.ASPECT_X, 1);
          intent.putExtra(CropImage.ASPECT_Y, 1);
          intent.putExtra(CropImage.OUTPUT_X, 200);
          intent.putExtra(CropImage.OUTPUT_Y, 200);
          context.startActivityForResult(intent, CROP_FROM_CAMERA);
      }


   /** this method is used for copy stream */

   public static void copyStream(InputStream input, OutputStream output)
              throws IOException {

          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = input.read(buffer)) != -1) {
              output.write(buffer, 0, bytesRead);
          }
      }



}
