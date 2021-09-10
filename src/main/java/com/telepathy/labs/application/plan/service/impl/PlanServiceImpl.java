package com.telepathy.labs.application.plan.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.telepathy.labs.application.plan.bean.InputData;
import com.telepathy.labs.application.plan.bean.Output;
import com.telepathy.labs.application.plan.constants.PSConstants;
import com.telepathy.labs.application.plan.service.IPlanService;
import com.telepathy.labs.application.plan.utils.CommonUtils;

/**
 * 
 * finds the combination of plans that offers all selected features at the
 * lowest price.
 *
 */
@Service
public class PlanServiceImpl implements IPlanService {

	private static final Logger LOG = LoggerFactory.getLogger(PlanServiceImpl.class);

	@Override
	public InputData calculateMinimumPrice(List<InputData> input, List<String> values) {
		InputData resultPlan = null;

		if (values == null || values.size() == 0) {
			return new InputData(PSConstants.EMPTY, PSConstants.DEFAULT_VALUE, PSConstants.EMPTY);
		}
		
		LOG.info("start calculate Minimum Price");
		resultPlan = getCombinationalPlanFeatures(input, values);
		LOG.info("End calculate Minimum Price");
		return resultPlan;
	}
	
	/**
	 * function help to find the combinational plan from user input
	 * @param input
	 * @param values
	 * @return
	 */
	public InputData getCombinationalPlanFeatures(List<InputData> input, List<String> values) {
		Set<InputData> result = null;
		Set<Set<InputData>> finalResult = new HashSet<Set<InputData>>();

		List<List<String>> combinationSearchInputs = CommonUtils.getCombinationSearch(values, values.size() - 3);
		for (List<String> comResult : combinationSearchInputs) {
			InputData dataResult = getMultiPlanServicesIfAllMatched(input, comResult);
			if (dataResult != null) {
				result = new TreeSet<InputData>();
				List<String> uniqueValues = CommonUtils.findUniqueValues(values, CommonUtils.splittedSeriveNames(dataResult.getServiceName()));
				result.add(dataResult);
				for (String singleResult : uniqueValues) {
					result.add(getSinglePlanService(input, singleResult));
				}
				finalResult.add(result);
			}
		}

		InputData minimumPriceRecord = findMinimumRecordFromList(finalResult);
		return minimumPriceRecord;
	}

	/**
	 * get multi plan services records If All Matched from user Input
	 * 
	 * @param input
	 * @param values
	 * @return Input Data
	 */
	public InputData getMultiPlanServicesIfAllMatched(List<InputData> input, List<String> values) {

		String[] serviceInput = values.stream().toArray(String[]::new);
		List<InputData> inData = input;
		InputData op = null;
		List<InputData> data = inData.stream()
				.filter(f -> Stream.of(serviceInput).allMatch(f.getServiceName()::contains))
				.collect(Collectors.toList());
		if (data.size() != 0) {
			op = data.stream().min(Comparator.comparing(InputData::getPrice)).get();
		}
		return op;
	}

	/**
	 * get single plan service record from user Input
	 * 
	 * @param input
	 * @param serviceName
	 * @return InputData
	 */
	public InputData getSinglePlanService(List<InputData> inputData, String serviceName) {
		List<InputData> data = inputData.stream().filter(f -> f.getServiceName().contains(serviceName))
				.collect(Collectors.toList());
		InputData op = data.stream().min(Comparator.comparing(InputData::getPrice)).get();
		return op;

	}
	
	/**
	 * find Minimum Record From prepared filtered List
	 * @param finalResult
	 * @return
	 */
	private InputData findMinimumRecordFromList(Set<Set<InputData>> finalResult) {
		List<Output> minmumResult = new ArrayList<>();
		int totalAmount = 0;
		String plans = "";
		for (Set<InputData> list : finalResult) {
			for (InputData output : list) {
				totalAmount += output.getPrice();
				plans += output.getPlanName() + ",";
			}
			minmumResult.add(new Output(totalAmount, plans));
			totalAmount = 0;
			plans = "";
		}
		Output op = minmumResult.stream().min(Comparator.comparing(Output::getPrice)).get();
		return DataMapptingToDto(op);
	}
	
	/**
	 * data transferring to Object
	 * @param op
	 * @return
	 */
	private InputData DataMapptingToDto(Output op) {
		InputData record = new InputData();
		record.setPrice(op.getPrice());
		record.setPlanName(op.getPlanNames().substring(0, op.getPlanNames().length() - 1));
		record.setServiceName("");
		return record;
	}

	/**
	 * Check the available services count.
	 * 
	 * @return count of services
	 * @Param List<InputData> input
	 * @Param List<String> values
	 */
	@Override
	public int checkAvailableServices(List<InputData> input, List<String> values) {
		Set<String> availableServices = new HashSet<String>();
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		for (InputData planDetails : input) {
			sb.append(planDetails.getServiceName() + ",");
		}
		String[] arr = sb.toString().split(",");

		for (String setVals : arr) {
			availableServices.add(setVals);
		}

		for (String inStr : values) {
			for (String serviceName : availableServices) {
				if (inStr.equalsIgnoreCase(serviceName)) {
					counter++;
				}
			}
		}
		return counter;
	}

}
