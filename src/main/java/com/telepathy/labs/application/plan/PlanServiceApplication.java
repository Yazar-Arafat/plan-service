package com.telepathy.labs.application.plan;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.telepathy.labs.application.plan.bean.InputData;
import com.telepathy.labs.application.plan.bean.Output;
import com.telepathy.labs.application.plan.controller.PlanController;
import com.telepathy.labs.application.plan.utils.BuildInputData;

@SpringBootApplication
public class PlanServiceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(PlanServiceApplication.class, args);
		BuildInputData data = applicationContext.getBean(BuildInputData.class);
		PlanController service = applicationContext.getBean(PlanController.class);

		if (args.length != 0) {
//			// pass first command line argument- to build the input data
			List<InputData> input = data.FileReader(args[0]);
//			// pass second command line argument - to take user service request and send to API
			List<String> inputServiceList = Arrays.asList(args[1].split(","));
			Output op = service.toFindBestPricePlan(input, inputServiceList);
			System.out.println("Output:\n" + op.getPrice() + "," + op.getPlanNames());
		}
	}

}
