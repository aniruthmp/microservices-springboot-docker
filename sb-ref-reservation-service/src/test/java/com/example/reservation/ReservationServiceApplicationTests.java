package com.example.reservation;

import com.example.reservation.config.SwaggerConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import springfox.documentation.staticdocs.Swagger2MarkupResultHandler;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureRestDocs(outputDir = "build/asciidoc/snippets")
@SpringBootTest(classes = {ReservationServiceApplication.class, SwaggerConfig.class})
@AutoConfigureMockMvc
public class ReservationServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
	}

	@Test
	public void createSpringfoxSwaggerJson() throws Exception {
		String outputDir = System.getProperty("io.springfox.staticdocs.outputDir");
		MvcResult mvcResult = this.mockMvc.perform(get("/v2/api-docs")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();

		//Pretty Print
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(response.getContentAsString()).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		Files.createDirectories(Paths.get(outputDir));
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputDir, "swagger.json"), StandardCharsets.UTF_8)){
			writer.write(prettyJson);
		}
	}

	//Commenting out this method as springfox-staticdocs is not yet updated for ver 2.7.0
//	@Test
//	public void convertSwaggerToAsciiDoc() {
//		try {
//			this.mockMvc.perform(get("/v2/api-docs")
//                    .accept(MediaType.APPLICATION_JSON))
//                    .andDo(Swagger2MarkupResultHandler.outputDirectory("target/asciidoc/generated/").build())
//                    .andExpect(status().isOk());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
