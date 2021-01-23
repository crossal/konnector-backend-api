package com.konnector.backendapi.verification;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VerificationController.class)
@TestPropertySource("classpath:application-integration-test.properties")
public class VerificationWebIT {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private VerificationService verificationServiceMock;
	@MockBean
	private ModelMapper modelMapperMock;
	@MockBean
	private UserDetailsService userDetailsService;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final VerificationDTO verificationDTO = easyRandom.nextObject(VerificationDTO.class);

	@Test
	public void verifyUserEmail_withToken_returnsSuccess() throws Exception {
		mockMvc.perform(post("/api/verifications/verify").queryParam("usernameOrEmail", "username_or_email").queryParam("token", "token_value").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void verifyUserEmail_withCode_returnsSuccess() throws Exception {
		String verificationJson = objectMapper.writeValueAsString(verificationDTO);
		mockMvc.perform(post("/api/verifications/verify").contentType(MediaType.APPLICATION_JSON).content(verificationJson)).andExpect(status().isOk());
	}
}
