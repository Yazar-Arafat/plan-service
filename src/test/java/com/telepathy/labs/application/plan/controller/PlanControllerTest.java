package com.telepathy.labs.application.plan.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telepathy.labs.application.plan.bean.InputData;
import com.telepathy.labs.application.plan.bean.Output;
import com.telepathy.labs.application.plan.service.IPlanService;
import com.telepathy.labs.application.plan.utils.BuildInputData;
import com.telepathy.labs.application.plan.utils.CommonUtils;

@SpringBootTest
public class PlanControllerTest {

	@InjectMocks
	private PlanController planController;

	@Mock
	private IPlanService planService;
	
	@Mock
	private BuildInputData data;

	private MockMvc mockMvc;
	
	private String content;
	
	private List<InputData> list = null;

	@BeforeEach
	public void setup() throws IOException {
		this.mockMvc = MockMvcBuilders.standaloneSetup(planController).build();
		File file = new ClassPathResource("plan.json").getFile();
		content = new String(Files.readAllBytes(file.toPath()));
		ObjectMapper mapper = new ObjectMapper();
		list = mapper.readValue(content, new TypeReference<List<InputData>>(){});
	}

	@Test
	public void toFindMinimumPriceAPI_Test() throws Exception {
		List<String> testdata = Arrays.asList("voice", "email");
		try (MockedStatic<CommonUtils> utils = Mockito.mockStatic(CommonUtils.class)) {
			utils.when(() -> CommonUtils.isValidInput(list)).thenReturn(true);
		}
		when(planService.checkAvailableServices(list, testdata)).thenReturn(2);
		InputData excepted = new InputData("PLAN1", 100, "");
		when(planService.calculateMinimumPrice(list, testdata)).thenReturn(excepted);
		this.mockMvc
				.perform(post("/api/plan/getPrice?services=voice,email").contentType(MediaType.APPLICATION_JSON).content(content))
				.andDo(print())
				.andExpect(jsonPath("$.price", is(100)))
				.andExpect(status().isOk());
	}

	@Test
	public void toFindMinimumPriceTest_CombinationalMatched() throws Exception {
		List<String> testdata = Arrays.asList("voice", "email", "admin");
		when(data.FileReader("dummyPath")).thenReturn(list);
		try (MockedStatic<CommonUtils> utils = Mockito.mockStatic(CommonUtils.class)) {
			utils.when(() -> CommonUtils.isValidInput(list)).thenReturn(true);
		}
		when(planService.checkAvailableServices(list, testdata)).thenReturn(3);
		InputData returnData = new InputData("PLAN1,PLAN3", 225, "");
		when(planService.calculateMinimumPrice(list, testdata)).thenReturn(returnData);
		Output opa = planController.toFindBestPricePlan(list, testdata);
		assertEquals(opa, new Output(225, "PLAN1,PLAN3"));
	}

	@Test
	public void toFindMinimumPriceTest_Matched() throws Exception {
		List<String> testdata = Arrays.asList("voice", "admin");
		when(data.FileReader("dummyPath")).thenReturn(list);
		try (MockedStatic<CommonUtils> utils = Mockito.mockStatic(CommonUtils.class)) {
			utils.when(() -> CommonUtils.isValidInput(list)).thenReturn(true);
		}
		when(planService.checkAvailableServices(list, testdata)).thenReturn(2);
		InputData returnData = new InputData("PLAN3", 125, "voice,admin");
		when(planService.calculateMinimumPrice(list, testdata)).thenReturn(returnData);
		Output opa = planController.toFindBestPricePlan(list, testdata);
		assertEquals(opa, new Output(125, "PLAN3"));
	}

	@Test
	public void toCheckAvailableServices() throws Exception {
		List<String> testdata = Arrays.asList("voice", "email", "teams");
		when(data.FileReader("dummyPath")).thenReturn(list);
		when(planService.checkAvailableServices(list, testdata)).thenReturn(0);
		Output opa = planController.toFindBestPricePlan(list, testdata);
		assertEquals(opa, new Output(0, ""));
	}

}
