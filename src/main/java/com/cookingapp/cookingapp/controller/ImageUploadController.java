package com.cookingapp.cookingapp.controller;

import com.cookingapp.cookingapp.service.ImageUploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/v1/images")
@RequiredArgsConstructor
public class ImageUploadController {

  private final ImageUploadService imageUploadService;
  @PostMapping("/upload")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      String imageUrl = imageUploadService.uploadImage(file.getName(), file.getBytes());
      return ResponseEntity.ok(imageUrl);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
    }
  }

}
