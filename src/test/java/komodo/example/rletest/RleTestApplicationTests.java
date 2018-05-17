package komodo.example.rletest;

import komodo.example.rletest.solution.RLEController;
import komodo.example.rletest.solution.RLEDecoder;
import komodo.example.rletest.solution.RLEEncoder;
import komodo.example.rletest.solution.RLEProperties;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The responsibility of the controller class is simply to marshall the JSON http request into and out of the busines
 * logic classes, and so most of the testing occurs in the unit tests for those classes, without duplication here.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {RLEEncoder.class, RLEProperties.class, RLEController.class, RLEDecoder.class})
@WebAppConfiguration
public class RleTestApplicationTests {

	@Autowired
	private RLEController controller;

	private MockMvc fixture;

	@Before
	public void init() {
		fixture= MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testEncodePut() throws Exception {
		final String content="{\"text\":\"ABCCCCCCCDEFGGGGGGGGGGH\"}";
		final String expected="AB{C;7}DEF{G;10}H";
		fixture.perform(put("/encode").contentType("application/json").content(content))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.text").value(expected))
				.andExpect(jsonPath("$.time").exists());

	}

	@Test
	public void testDecodePut() throws Exception {
		final String content="{\"text\":\"AB{C;7}DEF{G;10}H\"}";
		final String expected="ABCCCCCCCDEFGGGGGGGGGGH";
		fixture.perform(put("/decode").contentType("application/json").content(content))
				.andExpect(status().is(200))
				.andExpect(jsonPath("$.text").value(expected))
				.andExpect(jsonPath("$.time").exists());
	}

	// Not shown - negative tests, corner cases, invalid json etc due to time constraints


}
