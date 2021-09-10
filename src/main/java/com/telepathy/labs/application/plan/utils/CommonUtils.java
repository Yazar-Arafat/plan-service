package com.telepathy.labs.application.plan.utils;

import java.util.ArrayList;
import java.util.List;

import com.telepathy.labs.application.plan.bean.InputData;
/**
 * Class having the common utilities for plan service
 *
 */
public class CommonUtils {

	/**
	 * function to check the valid input data
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isValidInput(List<InputData> input) {
		for (InputData data : input) {
			if (0 > data.getPrice()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * function will help to get Combination Search based on Input services
	 * @param values
	 * @param filterLevel
	 * @return
	 */
	public static List<List<String>> getCombinationSearch(List<String> values, int filterLevel) {
		List<List<String>> result = new ArrayList<List<String>>();
		if (values == null) {
			return result;
		}

		final int maxbit = 1 << values.size();
		List<List<String>> combinations = new ArrayList<List<String>>();
		// for each combination given by a (binary) number
		for (int p = 0; p < maxbit; p++) {
			final List<String> res = new ArrayList<String>();

			// evaluate if array 'a' element at index 'i' is present in combination
			for (int i = 0; i < values.size(); i++) {
				if ((1 << i & p) > 0) {
					res.add(values.get(i));
				}
			}
			if (!res.isEmpty() && res.size() > filterLevel) {
				combinations.add(res);
			}
		}
		return combinations;

	}

	/**
	 * function to helper converter from String to List<Stirng>
	 * 
	 * @param serviceName
	 * @return List<String>
	 */
	public static List<String> splittedSeriveNames(String serviceName) {
		List<String> serviceValues = new ArrayList<String>();
		String[] service = serviceName.split(",");
		for (String splittedService : service) {
			serviceValues.add(splittedService);
		}
		return serviceValues;

	}

	/**
	 * function to help compare 2 list of String and find the unique values
	 * 
	 * @param values
	 * @param splittedNames
	 * @return
	 */
	public static List<String> findUniqueValues(List<String> values, List<String> splittedNames) {
		List<String> filteredResult = new ArrayList<String>();
		for (String data : values) {
			if (!splittedNames.contains(data)) {
				filteredResult.add(data);
			}
		}
		return filteredResult;
	}

}
