package br.com.edu.fiap.techchallengelanchonete.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.edu.fiap.techchallengelanchonete.TechChallengeLanchoneteApplication;
import br.com.edu.fiap.techchallengelanchonete.controller.BaseController;

@SpringBootTest(classes = TechChallengeLanchoneteApplication.class)
@AutoConfigureMockMvc
class ApiExceptionHandlerIT {
    
    @Autowired
    MockMvc mvc;

    @MockBean
    BaseController baseController;

    @Test
    void deveGerarNotFound() throws Exception {
        when(baseController.healthCheck()).thenThrow(NotFoundResourceException.class);

        mvc.perform(get("/health")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void deveGerarBadRequest() throws Exception {
        when(baseController.healthCheck()).thenThrow(ApplicationException.class);

        mvc.perform(get("/health")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deveGerarInternalError() throws Exception {
        when(baseController.healthCheck()).thenThrow(RuntimeException.class);

        mvc.perform(get("/health")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

}
