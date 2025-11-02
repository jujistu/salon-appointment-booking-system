package com.apb.userservice.service;

import com.apb.userservice.payload.dto.Credential;
import com.apb.userservice.payload.dto.SignUpDTO;
import com.apb.userservice.payload.dto.KeycloakUserDTO;
import com.apb.userservice.payload.dto.KeycloakRole;
import com.apb.userservice.payload.dto.TokenResponse;
import com.apb.userservice.payload.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";
    private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL+"/admin/realms/master/users";
    private static final String TOKEN_URL = KEYCLOAK_BASE_URL+"/realms/master/protocol/openid-connect/token";
    private static final String CLIENT_ID = "salon-booking-client";
    private static final String CLIENT_SECRET = "iDQ9PGeEoxvkateOmBNKVVMOhPA6U5Md";
    private static final String GRANT_TYPE = "password";
    private static final String scope = "openid profile email";
    private static final String username = "tejas";
    private static final String password = "tejas";
    private static final String clientId = "3c2f2d19-bfa7-4832-8051-cfca9d903c2a";

    private final RestTemplate restTemplate;

    public void createUser(SignUpDTO signUpDTO) throws Exception{

        String ACCESS_TOKEN = getAdminAccessToken(username, password, GRANT_TYPE, null).getAccessToken();

        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signUpDTO.getPassword());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signUpDTO.getUsername());
        userRequest.setEmail(signUpDTO.getEmail());
        userRequest.setFirstName(signUpDTO.getFullName());
        userRequest.setEnabled(true);
        List<Credential> credentials = new ArrayList<>();
        credentials.add(credential);
        userRequest.setCredentials(credentials);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<UserRequest> requestHttpEntity = new HttpEntity<>(userRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KEYCLOAK_ADMIN_API,
                HttpMethod.POST,
                requestHttpEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("User created successfully");
            KeycloakUserDTO user = fetchFirstUserByUsername(signUpDTO.getUsername(), ACCESS_TOKEN);
            KeycloakRole role = getRoleByName(clientId,ACCESS_TOKEN,signUpDTO.getRole().toString());
            List<KeycloakRole> roles = new ArrayList<>();
            roles.add(role);
            assignRoleToUser(user.getId(), clientId, roles, ACCESS_TOKEN);
        }  else {
            System.out.println("User creation failed");
            throw new Exception(response.getBody());
        }


    }

    public TokenResponse getAdminAccessToken(String username, String password, String grantType, String refreshToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", grantType);
        requestBody.add("refresh_token", refreshToken);
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                TOKEN_URL,
                HttpMethod.POST,
                requestHttpEntity,
                TokenResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody()!=null) {
            return response.getBody();
        }
        throw new Exception("Failed to obtain access token");


    }

    public KeycloakRole getRoleByName(String clientId, String token, String role) {
        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/clients/"+clientId+"/roles/"+role;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakRole> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeycloakRole.class
        );

        return response.getBody();

    }

    public KeycloakUserDTO fetchFirstUserByUsername(String username, String token) throws Exception {
        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/users?username="+username;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeycloakUserDTO[].class
        );
        KeycloakUserDTO[] users = response.getBody();
        if(users!=null && users.length>0){
            return users[0];
        }
        throw new Exception("User not found with username: "+username);
    }

    public void assignRoleToUser(String userId, String clientId, List<KeycloakRole> roles, String token) throws Exception {
        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/users/"+userId+"/role-mappings/clients/"+clientId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<List<KeycloakRole>> requestHttpEntity = new HttpEntity<>(roles,headers);


        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestHttpEntity,
                    String.class
            );
        } catch (Exception e) {
            throw new Exception("Failed to assign new role "+e.getMessage());
        }
    }

    public KeycloakUserDTO fetchUserProfileByJWT(String token) throws Exception {
        String url = KEYCLOAK_BASE_URL+"/realms/master/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(headers);


        try {
            ResponseEntity<KeycloakUserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestHttpEntity,
                    KeycloakUserDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new Exception("Failed to assign new role "+e.getMessage());
        }
    }

}
