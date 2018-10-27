package com.github.mhdirkse.timewriter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SecurityTest {
    Logger logger = LoggerFactory.getLogger(SecurityTest.class);

    @LocalServerPort
    private int port;

    private String base;

    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        template = new TestRestTemplate(TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
        base = "http://localhost:" + port + "/";
    }

    @Test
    public void helloWithoutLoginGivesError() throws Exception {
        hello(h -> {}, HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void helloWithLoginSucceeds() throws Exception {
        String cookie = login("password");
        hello(h -> h.add(HttpHeaders.COOKIE, cookie), HttpStatus.OK);
        logout();
    }

    @Test
    public void loginWithInvalidCredentialsFails() {
        ResponseEntity<String> response = sendLogin("invalidPassword");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void whenLoginFailedThenHelloFails() {
        ResponseEntity<String> response = sendLogin("invalidPassword");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
        String cookie = extractCookie(response);
        hello(h -> h.add(HttpHeaders.COOKIE, cookie), HttpStatus.UNAUTHORIZED);
    }

    private String login(String password) {
        ResponseEntity<String> loginResponse = sendLogin(password);
        assertThat(loginResponse.getStatusCode(), equalTo(HttpStatus.OK));
        String cookieHeaderValue = extractCookie(loginResponse);
        logger.info("Cookie header value: " + cookieHeaderValue);
        return cookieHeaderValue;
    }

    private ResponseEntity<String> sendLogin(String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword(password);
        HttpEntity<LoginRequest> loginRequestEntity = new HttpEntity<>(loginRequest);
        ResponseEntity<String> loginResponse = template.postForEntity(base + "login", loginRequestEntity, String.class);
        return loginResponse;
    }

    private String extractCookie(ResponseEntity<String> loginResponse) {
        HttpHeaders headers = loginResponse.getHeaders();
        String cookieHeaderKey = HttpHeaders.SET_COOKIE;
        logger.info("Cookie header key: " + cookieHeaderKey);
        String cookieHeaderValue = headers.getFirst(cookieHeaderKey);
        return cookieHeaderValue;
    }

    private void hello(Consumer<HttpHeaders> cookieSetter, HttpStatus expected) {
        HttpHeaders headers = new HttpHeaders();
        cookieSetter.accept(headers);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> resp = template.exchange(base + "hello", HttpMethod.GET, request, String.class);
        assertThat(resp.getStatusCode(), equalTo(expected));        
    }

    private void logout() {
        ResponseEntity<String> resp = template.getForEntity(base + "logout", String.class);
        assertThat(resp.getStatusCode(), equalTo(HttpStatus.OK));
    }
}
