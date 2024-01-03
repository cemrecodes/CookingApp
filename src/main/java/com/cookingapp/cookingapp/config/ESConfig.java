package com.cookingapp.cookingapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.cookingapp.cookingapp.repo")
@ComponentScan(basePackages = "com.cookingapp.cookingapp")
public class ESConfig extends ElasticsearchConfiguration {

  @Value("${elasticsearch.url}")
  private String url;

  @Value("${elasticsearch.username}")
  private String username;

  @Value("${elasticsearch.password}")
  private String password;

  @Value("${elasticsearch.fingerprint}")
  private String fingerprint;

  @Override
  public ClientConfiguration clientConfiguration() {
    return ClientConfiguration.builder()
        .connectedTo(url)
        .usingSsl(fingerprint)
        .withBasicAuth(username, password)
        .build();
  }
}
