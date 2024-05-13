package org.example;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        String path = "C:\\Users\\duffy\\Desktop\\projects\\java\\test-task-mvn\\tickets.json";

        JSONParser jsonParser = new JSONParser();
        JSONArray tickets = (JSONArray) jsonParser.parse(new FileReader(path));

        Map<String, Integer> minFlightTimeMap = new HashMap<>();
        List<Integer> prices = new ArrayList<>();

        for (Object ticket : tickets) {
            JSONObject ticketJSON = (JSONObject) ticket;
            String departure = (String) ticketJSON.get("departure");
            String destination = (String) ticketJSON.get("destination");
            String carrier = (String) ticketJSON.get("carrier");
            int price = Integer.parseInt(ticketJSON.get("price").toString());
            int flightTime = Integer.parseInt(ticketJSON.get("flight_time").toString());

            if (departure.equals("Vladivostok") && destination.equals("Tel Aviv")) {
                if (!minFlightTimeMap.containsKey(carrier)) {
                    minFlightTimeMap.put(carrier, flightTime);
                }else {
                    minFlightTimeMap.put(carrier, Math.min(minFlightTimeMap.get(carrier), flightTime));
                }

                prices.add(price);
            }


        }

        System.out.println(minFlightTimeMap.get("S7 airlines"));
        System.out.println(minFlightTimeMap.get("Aeroflot"));
        System.out.println(calculateDifferenceAverageMedian(prices));
    }

    public static int calculateDifferenceAverageMedian(List<Integer> prices) {
        int sum = prices.stream().reduce(Integer::sum).get();
        int average = sum / prices.size();

        int median = 0;
        if (prices.size() % 2 == 0) {
            median = (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2;
        } else {
            median = prices.get(prices.size() / 2);
        }

        return average - median;
    }
}