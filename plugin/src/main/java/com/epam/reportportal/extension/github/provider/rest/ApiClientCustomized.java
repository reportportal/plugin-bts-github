package com.epam.reportportal.extension.github.provider.rest;

import com.epam.reportportal.extension.github.generated.ApiClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class ApiClientCustomized extends ApiClient {

    @Override
    protected RestTemplate buildRestTemplate() {
        RestTemplate restTemplate = super.buildRestTemplate();
        restTemplate.getMessageConverters().add(createMappingJacksonHttpMessageConverter());

        return restTemplate;
    }

    private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(createObjectMapper());
    }

    private ObjectMapper createObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JsonNullableModule());
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
