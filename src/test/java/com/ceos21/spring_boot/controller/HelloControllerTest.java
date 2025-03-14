package com.ceos21.spring_boot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Hello Controller success test")
    public void getHello_success_test() throws Exception {
        // given
        String expectedResult = "Greeting from Spring boot";

        // when
        ResultActions result = mvc.perform(get("/").contentType(MediaType.APPLICATION_JSON));
        String responseBody = result.andReturn().getResponse().getContentAsString();
        System.out.println("성공 테스트 : " + responseBody);

        // then
        assertThat(responseBody).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("Hello Controller fail Test : URL Not FOUND")
    public void getHello_fail_test() throws Exception {
        // given
        String invalidUrl = "/invalid";

        //when
        ResultActions result = mvc.perform(MockMvcRequestBuilders.get(invalidUrl).accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isNotFound());
    }
}
