package com.recordcataloguer.recordcataloguer.helpers.string;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class StringHelper {


    public static final String getSubstringParam(String url, String textBeginning, String textEnd) {
        StringBuilder dataWithEnd = new StringBuilder(url);
        dataWithEnd = dataWithEnd.append("EnD");
        String releaseId = StringUtils.substringBetween(dataWithEnd.toString(), textBeginning, "EnD");

        log.info("releaseId {} from url {}", releaseId, url);

        return releaseId;
    }

}
