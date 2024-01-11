package com.epam.reportportal.extension.github.provider.rest;

import com.epam.reportportal.extension.github.generated.ApiClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class ApiClientCustomized extends ApiClient {

    @Override
    protected RestTemplate buildRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // This allows us to read the response more than once - Necessary for debugging.
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(restTemplate.getRequestFactory()));
        restTemplate.getMessageConverters().add(0, createMappingJacksonHttpMessageConverter());
        // disable default URL encoding
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        restTemplate.setUriTemplateHandler(uriBuilderFactory);
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
