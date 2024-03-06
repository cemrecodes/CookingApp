package com.cookingapp.cookingapp.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImp {
  private static String IMAGE_DESTINATION_FOLDER = "C:/images";

  public static void downloadImage(String strImageURL){

    //get file name from image path
    String strImageName =
        strImageURL.substring( strImageURL.lastIndexOf("/") + 1 );

    log.info("Saving: " + strImageName + ", from: " + strImageURL);

    try {

      //open the stream from URL
      URL urlImage = new URL(strImageURL);
      InputStream in = urlImage.openStream();

      byte[] buffer = new byte[4096];
      int n = -1;

      OutputStream os =
          new FileOutputStream( IMAGE_DESTINATION_FOLDER + "/" + strImageName );

      //write bytes to the output stream
      while ( (n = in.read(buffer)) != -1 ){
        os.write(buffer, 0, n);
      }

      //close the stream
      os.close();

      log.info("Image has been saved.");

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
