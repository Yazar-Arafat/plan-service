package com.telepathy.labs.application.plan.service;

import java.util.List;

import com.telepathy.labs.application.plan.bean.InputData;
/**
 * Interface for Plan service
 *
 */
public interface IPlanService {

	public InputData calculateMinimumPrice(List<InputData> input, List<String> values);

	public int checkAvailableServices(List<InputData> input, List<String> values);

}
