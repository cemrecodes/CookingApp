package com.cookingapp.cookingapp.config;

import com.cookingapp.cookingapp.service.GeminiInterface;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.FileInputStream;
import java.io.IOException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class Config {

  @Value("${openai.api.key}")
  private String chatGptApiKey;

  @Value("${gemini.baseurl}")
  private String geminiBaseUrl;

  @Value("${googleai.api.key}")
  private String geminiApiKey;

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Bean
  public RestClient geminiRestClient() {
    return RestClient.builder()
        .baseUrl(geminiBaseUrl)
        .defaultHeader("x-goog-api-key", geminiApiKey)
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("Accept", "application/json")
        .build();
  }

  @Bean
  public GeminiInterface geminiInterface(@Qualifier("geminiRestClient") RestClient client) {
    RestClientAdapter adapter = RestClientAdapter.create(client);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(GeminiInterface.class);
  }

  @Bean
  public RestTemplate chatGptTemplate(){
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add((request, body, execution) -> {
      request.getHeaders().add("Authorization", "Bearer " + chatGptApiKey);
      return execution.execute(request, body);
    });
    return restTemplate;
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public ServiceAccountCredentials serviceAccountCredentials() throws IOException {
    String pathToJsonKey = "src/main/resources/static/cooking-app-406112-54721ffa23c4.json";
    // Servis hesabı kimlik bilgilerini okuyun
    ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(
        new FileInputStream(pathToJsonKey)
    );

    return credentials;
  }

  @Bean
  public Storage storage() throws IOException {
    return StorageOptions.newBuilder()
        .setCredentials(serviceAccountCredentials())
        .build().getService();
  }

}
