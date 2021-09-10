package com.telepathy.labs.application.plan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telepathy.labs.application.plan.bean.InputData;
import com.telepathy.labs.application.plan.service.impl.PlanServiceImpl;
import com.telepathy.labs.application.plan.utils.BuildInputData;

@SpringBootTest
public class PlanServiceTest {

	@InjectMocks
	private PlanServiceImpl planServiceImpl;

	@Mock
	private BuildInputData data;

	private List<InputData> list = null;

	@BeforeEach
	public void setup() throws IOException {
		File file = new ClassPathResource("plan.json").getFile();
		String content = new String(Files.readAllBytes(file.toPath()));
		ObjectMapper mapper = new ObjectMapper();
		list = mapper.readValue(content, new TypeReference<List<InputData>>(){});
	}

	@Test
	public void calculateMimimumPriceTest() throws Exception {
		List<String> testdata = Arrays.asList("voice", "email");
		when(data.FileReader("dummyPath")).thenReturn(list);
		InputData data = planServiceImpl.calculateMinimumPrice(list, testdata);
		InputData expectedData = new InputData("PLAN1", 100, "");
		assertEquals(data, expectedData);
	}

	@Test
	public void getMultiPlanServicesTest() throws Exception {
		List<String> testdata = Arrays.asList("voice", "database");
		when(data.FileReader("dummyPath")).thenReturn(list);
		InputData data = planServiceImpl.calculateMinimumPrice(list, testdata);
		InputData expectedData = new InputData("PLAN1,PLAN4", 235, "");
		assertEquals(data, expectedData);
	}

	@Test
	public void checkAvailableServicesTest() throws Exception {
		List<String> testdata = Arrays.asList("voice", "email", "admin");
		when(data.FileReader("dummyPath")).thenReturn(list);
		int counter = planServiceImpl.checkAvailableServices(list, testdata);
		assertEquals(testdata.size(), counter);
	}

	@Test
	public void checkNotAvailableServices_Test() throws Exception {
		List<String> testdata = Arrays.asList("voice", "email", "phone");
		when(data.FileReader("dummyPath")).thenReturn(list);
		int counter = planServiceImpl.checkAvailableServices(list, testdata);
		assertNotEquals(testdata.size(), counter);
	}

}
