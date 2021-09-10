package com.telepathy.labs.application.plan.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telepathy.labs.application.plan.bean.InputData;
import com.telepathy.labs.application.plan.bean.Output;
import com.telepathy.labs.application.plan.constants.PSConstants;
import com.telepathy.labs.application.plan.service.IPlanService;
import com.telepathy.labs.application.plan.utils.CommonUtils;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Find the best plan
 * To calculate the minimum price plan from user input services
 *
 */
@RestController
@RequestMapping(path = PSConstants.PS_REST_API)
public class PlanController {

	private static final Logger LOG = LoggerFactory.getLogger(PlanController.class);

	@Autowired
	IPlanService planService;

	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 500, message = "Internal Server Error"),
			@ApiResponse(code = 400, message = "Bad Request"), })
	@PostMapping("getPrice")
	public Output toFindBestPricePlan(@RequestBody List<InputData> input, @RequestParam("services") List<String> values) {
		Output output = new Output(PSConstants.DEFAULT_VALUE, PSConstants.EMPTY);
		try {
			LOG.info("input file successfully fetched");
			// check input is vaild 
			if (!CommonUtils.isValidInput(input)) {
				LOG.info("input price is not valid data, The price plan should positive");
				return output;
			}
			// check input service with available services
			int getServiceCount = planService.checkAvailableServices(input, values);
			if (values.size() != getServiceCount) {
				LOG.info("service not available");
				return output;
			}

			InputData result = planService.calculateMinimumPrice(input, values);
			output.setPlanNames(result.getPlanName());
			output.setPrice(result.getPrice());
			return output;
		} catch (Exception e) {
			LOG.info("Error Message : " + e.getMessage());
		}
		return output;
	}
	
}
