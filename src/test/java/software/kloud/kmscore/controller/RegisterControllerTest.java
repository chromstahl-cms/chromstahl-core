package software.kloud.kmscore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import software.kloud.kmscore.dto.RegisterDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objm;
    @Autowired
    private WebApplicationContext ctx;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
        this.objm = new ObjectMapper();
    }

    @Test
    public void it_returns_correct_errors() throws Exception {
        var dtoIn = new RegisterDTO();
        dtoIn.seteMail("something");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objm.writeValueAsBytes(dtoIn))
        ).andDo(r -> {
            var res = r.getResponse();
            System.out.println(res.getContentAsString());
        });
    }
}
