package com.example.worklog.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, ObjectMapperResolver.class})
public abstract class AbstractRestDocsTests {

    protected RestDocumentationResultHandler resultHandler;
    protected RestDocumentationContextProvider contextProvider;
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(
            RestDocumentationContextProvider contextProvider,
            ObjectMapper objectMapper
    ) {

        this.contextProvider = contextProvider;
        this.objectMapper = objectMapper;
    }

    protected MockMvc mockMvc(Object controller) {
        return MockMvcBuilders.standaloneSetup(controller)
                .apply(MockMvcRestDocumentation.documentationConfiguration(contextProvider))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(resultHandler)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    protected static OperationRequestPreprocessor documentRequest() {
        return Preprocessors.preprocessRequest(Preprocessors.prettyPrint());
    }

    protected static OperationResponsePreprocessor documentResponse() {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
    }
}