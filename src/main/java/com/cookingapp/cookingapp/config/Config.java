package com.cookingapp.cookingapp.config;

import com.cookingapp.cookingapp.service.GeminiInterface;
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

  @Bean
  public RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Bean
  public RestClient geminiRestClient(@Value("${gemini.baseurl}") String baseUrl,
      @Value("${googleai.api.key}") String apiKey) {
    return RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeader("x-goog-api-key", apiKey)
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
}
