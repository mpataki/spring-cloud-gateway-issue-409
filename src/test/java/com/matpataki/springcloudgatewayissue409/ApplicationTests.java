package com.matpataki.springcloudgatewayissue409;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import java.time.Duration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ApplicationTests {

  @LocalServerPort int port = 0;

  String baseUri;
  WebTestClient webClient;

  @Rule public WireMockRule wireMockRule = new WireMockRule(8082);

  String testRoute = "/service/api/resource";

  @Before
  public void setup() {
    baseUri = "http://localhost:" + port;
    webClient =
        WebTestClient.bindToServer()
            .responseTimeout(Duration.ofSeconds(10))
            .baseUrl(baseUri)
            .build();

    stubFor(get(urlEqualTo(testRoute))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader(HttpHeaders.CONTENT_TYPE, "text/plain")));
  }

  @Test
  public void shouldGetOkFromService() {
    webClient
        .get()
        .uri(baseUri + testRoute)
        .exchange()
        .expectStatus()
        .isOk();
  }
}
