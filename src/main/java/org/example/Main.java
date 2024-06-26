package org.example;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {
        String filepath = "C:\\Users\\duffy\\Desktop\\projects\\java\\test-task-mvn\\tickets.json";

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(filepath));
        JSONArray tickets = (JSONArray) jsonObject.get("tickets");

        Map<String, Integer> minFlightTimeMap = new HashMap<>();
        List<Integer> prices = new ArrayList<>();

        for (Object ticketObj : tickets) {
            JSONObject ticket = (JSONObject) ticketObj;
            String origin = (String) ticket.get("origin");
            String destination = (String) ticket.get("destination");
            String carrier = (String) ticket.get("carrier");
            String arrivalDate = (String) ticket.get("arrival_date");
            String departureDate = (String) ticket.get("departure_date");
            int flightTime = calculateFlightTime((String) ticket.get("departure_time"), (String) ticket.get("arrival_time"),
                    departureDate, arrivalDate);
            int price = Integer.parseInt(ticket.get("price").toString());

            if (origin.equals("VVO") && destination.equals("TLV")) {
                if (!minFlightTimeMap.containsKey(carrier)) {
                    minFlightTimeMap.put(carrier, flightTime);
                }else {
                    minFlightTimeMap.put(carrier, Math.min(minFlightTimeMap.get(carrier), flightTime));
                }

                prices.add(price);
            }


        }

        System.out.print("Минимальное время полета компании TK: ");
        printMinFlightTime(minFlightTimeMap.get("TK"));
        System.out.println("-----");

        System.out.print("Минимальное время полета компании SU: ");
        printMinFlightTime(minFlightTimeMap.get("SU"));
        System.out.println("-----");

        System.out.print("Минимальное время полета компании S7: ");
        printMinFlightTime(minFlightTimeMap.get("S7"));
        System.out.println("-----");

        System.out.print("Минимальное время полета компании BA: ");
        printMinFlightTime(minFlightTimeMap.get("BA"));
        System.out.println("-----");

        System.out.print("Разница между средним значением и медианой: ");
        System.out.println(calculateDifferenceAverageMedian(prices));
    }

    public static int calculateFlightTime(String departureTime, String arrivalTime, String departureDate, String arrivalDate) throws java.text.ParseException {
        String[] depTimeParts = departureTime.split(":");
        String[] arrTimeParts = arrivalTime.split(":");

        String depTime = depTimeParts[0] + ":" + depTimeParts[1];
        String arrTime = arrTimeParts[0] + ":" + arrTimeParts[1];

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.ENGLISH);
        Date arrival = format.parse(arrivalDate + " " + arrTime);
        Date departure = format.parse(departureDate + " " + depTime);

        Instant arrivalInstant = arrival.toInstant();
        Instant departureInstant = departure.toInstant();

        Duration duration = Duration.between(departureInstant, arrivalInstant);

        return (int) duration.toMinutes();

    }

    public static void printMinFlightTime(int totalMinutes){
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        String time = String.format("%02d:%02d", hours, minutes);
        System.out.println(time);
    }


    public static int calculateDifferenceAverageMedian(List<Integer> prices) {
        int sum = prices.stream().reduce(Integer::sum).get();
        int average = sum / prices.size();

        Collections.sort(prices);

        int median = 0;
        if (prices.size() % 2 == 0) {
            median = (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2;
        } else {
            median = prices.get(prices.size() / 2);
        }

        return average - median;
    }
}