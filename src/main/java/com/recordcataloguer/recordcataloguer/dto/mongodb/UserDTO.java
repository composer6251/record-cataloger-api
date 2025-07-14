package com.recordcataloguer.recordcataloguer.dto.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    private String userName;
    private String verifierToken;
    private String oAuthToken;
    private String oAuthTokenSecret;
}
