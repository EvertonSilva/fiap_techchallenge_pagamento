package br.com.edu.fiap.techchallengelanchonete.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.edu.fiap.techchallengelanchonete.TechChallengeLanchoneteApplication;

@SpringBootTest(classes = TechChallengeLanchoneteApplication.class)
@AutoConfigureMockMvc
class BaseControllerIT {
 
    @Autowired
    MockMvc mvc;

    @Test
    void deveGerarInternalError() throws Exception {
        var resposta = mvc.perform(get("/health")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        
        assertThat(resposta.getResponse().getContentAsString())
            .contains("Up and Running");
    }

}
