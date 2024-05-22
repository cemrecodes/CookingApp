package com.cookingapp.cookingapp.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

  private final Storage storage;
  private final String bucketName = "cooking-app-images";

  public String uploadImageFromUrl(Long id, String strImageURL) {
    try {
      // open the stream from URL
      URL urlImage = new URL(strImageURL);
      InputStream in = urlImage.openStream();

      // read image data into a byte array
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      byte[] data = new byte[4096];
      int nRead;
      while ((nRead = in.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      byte[] imageData = buffer.toByteArray();

      // create a BlobId for the image
      BlobId blobId = BlobId.of(bucketName, String.valueOf(id));
      BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
          .setContentType("image/jpeg")
          .build();

      // upload the image data to Google Cloud Storage
      storage.create(blobInfo, imageData);

      System.out.println("Image uploaded to Google Cloud: " + "https://storage.googleapis.com/" + bucketName + "/" + id);

      return "https://storage.googleapis.com/" + bucketName + "/" + id;

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public String uploadImage(Long id, byte[] imageData) {
    BlobId blobId = BlobId.of(bucketName, String.valueOf(id));
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
        .setContentType("image/jpeg")
        .build();
    storage.create(blobInfo, imageData);
    return "https://storage.googleapis.com/" + bucketName + "/" + id;
  }

  public String uploadImage(String fileName, byte[] imageData) {
    BlobId blobId = BlobId.of(bucketName, fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
        .setContentType("image/jpeg")
        .build();
    storage.create(blobInfo, imageData);

    return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
  }
}
