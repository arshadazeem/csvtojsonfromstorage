package com.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import domain.Settlement;

public class ReadCsv {

	public static void main(String[] args) {
		// csvToJson();
		parseCsv();
	}

	private static void parseCsv() {

		File file = new File(
				"C:\\Users\\arazeem\\source\\repos\\java-apps\\csv-to-json\\csvtojson\\src\\main\\resources\\settlements.csv");

		List<Settlement> settlements = new ArrayList<Settlement>();

		try {
			Scanner sc = new Scanner(file).useDelimiter("\\n|\\|");

			if (sc.hasNext()) {
				// skip first line with headlines
				sc.nextLine();
			}

			while (sc.hasNextLine()) {
				// SETTLEMENT DATE|CUSIP|SYMBOL|QUANTITY|DESCRIPTION|PRICE

				Settlement settlement = new Settlement();

				settlement.setSettlementDate(sc.next());
				settlement.setCusip(sc.next());
				settlement.setSymbol(sc.next());
				settlement.setQuantity(sc.nextInt());
				settlement.setDescription(sc.next());
				settlement.setPrice(sc.next());

				settlements.add(settlement);

			}

			printList(settlements);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

	private static void printList(List<Settlement> settlements) {
		for (Settlement s : settlements) {
			System.out.println(s.toString());
		}

	}

	private static void csvToJson() {
		File input = new File(
				"C:\\Users\\arazeem\\source\\repos\\java-apps\\csv-to-json\\csvtojson\\src\\main\\resources\\settlements.csv");
		try {

			CsvMapper csvMapper = new CsvMapper();
			CsvSchema schema = CsvSchema.emptySchema().withHeader().withArrayElementSeparator("|"); // CsvSchema.emptySchema().withHeader();
			MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader().forType(Settlement.class).with(schema)
					.readValues(input);
			List<Map<?, ?>> list = mappingIterator.readAll();
			System.out.println("begin");
			System.out.println(list);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("end");
			System.out.println(mapper.writeValueAsString(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
