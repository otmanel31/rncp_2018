package com.otmanel.bonjourBatch.beans;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Airport, Airport> {

	@Override
	public Airport process(Airport item) throws Exception {
		System.out.println(item.getAirport());
		return item;
	}

}
