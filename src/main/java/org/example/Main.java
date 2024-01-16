package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.models.postman.Root;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) {

        OpenDentalScrapperAPISpec scrapperAPISpec = new OpenDentalScrapperAPISpec(
            new PrepareCollectionEndpointsImpl(new PrepareApiMethodImpl()));

        Root root = PostmanConverter.convertEndpointsToPostmanModel(scrapperAPISpec.getCollectionEndpoints());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File("example.json"), root);
        } catch (IOException e) {
            log.error("Save api in file exception: {}", ExceptionUtils.getStackTrace(e));
        }
    }
}
