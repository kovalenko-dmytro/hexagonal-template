package com.gmail.apach.hexagonaltemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(
	classes = HexagonalTemplateApplication.class,
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureHttpGraphQlTester
public abstract class AbstractControllerIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	protected MockMvc mvc;
	@Autowired
	protected ObjectMapper objectMapper;
	@Autowired
	private WebApplicationContext context;
	@Autowired
	protected HttpGraphQlTester httpGraphQlTester;

	@Override
	protected void beforeEach() {
		super.beforeEach();
		mvc = MockMvcBuilders
			.webAppContextSetup(context)
			.apply(springSecurity())
			.build();
	}
}
