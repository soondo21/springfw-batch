package com.spring.batch.step;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements ItemReader<String> {

	private String[] messages = { "Chunk Message-01",
			"Chunk Message-02",
			"Chunk Message-03" };

	private int count = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {

		if (count < messages.length) {
			System.out.println("Chunk Size : 2, Count : " + String.valueOf(count+1));
			return messages[count++];
		} else {
			count = 0;
		}
		return null;
	}

}