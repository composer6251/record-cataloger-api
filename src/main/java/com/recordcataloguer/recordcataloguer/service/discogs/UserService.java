package com.recordcataloguer.recordcataloguer.service.discogs;

import com.google.cloud.vision.v1.EntityAnnotation;
import com.recordcataloguer.recordcataloguer.client.discogs.DiscogsClient;
import com.recordcataloguer.recordcataloguer.constants.DiscogsConstants;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsTokens;
import com.recordcataloguer.recordcataloguer.constants.auth.discogs.DiscogsUserCredentials;
import com.recordcataloguer.recordcataloguer.database.hibernate.HibernateUtil;
//import com.recordcataloguer.recordcataloguer.database.mongodb.MongoDbClient;
import com.recordcataloguer.recordcataloguer.dto.discogs.request.DiscogsSearchAlbumRequest;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Album;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.DiscogsSearchResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.Listing;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.PriceSuggestionResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.Release;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.collectionapi.UserCollectionByFolderResponse;
import com.recordcataloguer.recordcataloguer.dto.discogs.response.marketplaceapi.DiscogsUserInventoryResponse;
import com.recordcataloguer.recordcataloguer.dto.mongodb.UserDTO;
import com.recordcataloguer.recordcataloguer.entity.AlbumEntity;
import com.recordcataloguer.recordcataloguer.util.discogs.DiscogsServiceHelper;
import com.recordcataloguer.recordcataloguer.util.discogs.auth.DiscogsAuthHelper;
import com.recordcataloguer.recordcataloguer.util.discogs.validators.DiscogsSearchResultValidator;
import com.recordcataloguer.recordcataloguer.util.string.StringHelper;
import feign.FeignException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.recordcataloguer.recordcataloguer.util.regex.CatalogNumberRegex.CAT_NO_SIX_AND_GREATER;

/***
 * Service for handling user login and userDTO db calls
 */
@Service
@Slf4j
public class UserService {

    /**************************USER LOOKUPS*************************/


    /**************************USER INSERTS*************************/
    public void insertUserIntoDb(String userName, String oAuthVerifier, String oAuthToken, String oAuthTokenSecret) {

//        return MongoDbClient.insertUser(buildUserDTO(userName, oAuthVerifier, oAuthToken, oAuthTokenSecret));

    }

    /**************************USER UTILS*************************/
    private UserDTO buildUserDTO(String userName, String oAuthVerifier, String oAuthToken, String oAuthTokenSecret) {

        UserDTO user = UserDTO.builder()
                .userName(userName)
                .verifierToken(oAuthVerifier)
                .oAuthToken(oAuthToken)
                .oAuthTokenSecret(oAuthTokenSecret)
                .build();



        return user;
    }
}
