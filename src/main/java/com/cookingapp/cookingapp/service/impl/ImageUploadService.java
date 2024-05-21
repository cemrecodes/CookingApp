package com.cookingapp.cookingapp.service.impl;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import com.google.appengine.api.images.ServingUrlOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

  private final Storage storage;
  private final String bucketName = "cooking-app-images";

  // ImagesService imagesService = ImagesServiceFactory.getImagesService();

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

      // generate a unique image name
      // String imageName = name.replaceAll(" ", "_") + "_" + UUID.randomUUID();

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

  public String uploadImage(String name, byte[] imageData) {
    String imageName = name + "_" + UUID.randomUUID();
    BlobId blobId = BlobId.of(bucketName, imageName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
        .setContentType("image/jpeg")
        .build();
    storage.create(blobInfo, imageData);
    /*
    ServingUrlOptions options = ServingUrlOptions.Builder
        .withGoogleStorageFileName("/gs/" + bucketName + imageName +".jpeg")
        .imageSize(150)
        .crop(true)
        .secureUrl(true);
    String url = imagesService.getServingUrl(options);
    return url;
    */
    return "https://storage.googleapis.com/" + bucketName + "/" + imageName;
  }
 // todo
  /*
  public void uploadImageFromUrl() {
    try {
      URL url = new URL(IMAGE_URL);
      try (InputStream in = url.openStream()) {
        String imageName = UUID.randomUUID().toString() + ".jpg";
        BlobId blobId = BlobId.of(BUCKET_NAME, imageName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType("image/jpeg")
            .build();
        storage.create(blobInfo, in.readAllBytes());
        System.out.println("Image uploaded successfully.");
      }
    } catch (IOException e) {
      System.err.println("Error uploading image: " + e.getMessage());
    }
  }

   */

}
