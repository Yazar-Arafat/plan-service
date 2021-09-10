package com.telepathy.labs.application.plan.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telepathy.labs.application.plan.bean.InputData;

/***
 * Class will help to read the text file from user directory and build the List
 * of object data.
 *
 */
@Component
public class BuildInputData {

	private static final Logger LOG = LoggerFactory.getLogger(BuildInputData.class);
	private static Scanner read;

	/**
	 * read the text file from user directory
	 * 
	 * @param filePath
	 * @return
	 */
	public List<InputData> FileReader(String filePath) {
		try {
			File file = new File(filePath);
			read = new Scanner(file);
		} catch (IOException ioException) {
			System.err.println("Cannot open the file.");
			System.exit(1);
		}

		List<InputData> inputData = new ArrayList<InputData>();
		InputData data = null;
		String line;
		
		try {
			while (read.hasNextLine()) {
				line = read.nextLine();
				String[] arr = line.split(",", 3);
				data = new InputData();
				data.setPlanName(arr[0]);
				data.setPrice(Integer.parseInt(arr[1]));
				data.setServiceName(arr[2]);
				inputData.add(data);
			}
			read.close();
		} catch (Exception e) {
			LOG.info("Error on parsing data from text to object : " + e.getMessage());
		}
		return inputData;
	}

}
