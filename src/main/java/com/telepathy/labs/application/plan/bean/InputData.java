package com.telepathy.labs.application.plan.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InputData implements Comparable<InputData> {
	private String planName;
	private int price;
	private String serviceName;

	@Override
	public int compareTo(InputData o) {
		return this.planName.compareTo(o.planName);
	}

}
