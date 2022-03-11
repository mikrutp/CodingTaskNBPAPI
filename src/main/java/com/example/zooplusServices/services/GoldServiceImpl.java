package com.example.zooplusServices.services;

import com.example.zooplusServices.microServices.DaysCalculator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
public class GoldServiceImpl implements GoldService {

    @Autowired
    DaysCalculator daysCalculator;

    public String getAvarageGoldFor14Days() {
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://api.nbp.pl/api/cenyzlota/");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();
            urlBuilder.append(daysCalculator.subtractDaysSkippingWeekends(now, 13));
            urlBuilder.append("/");
            urlBuilder.append(now);
            urlBuilder.append("/?format=json");
            String myUrlString = urlBuilder.toString();
//          Docelowy przykladowy URL: https://api.nbp.pl/api/cenyzlota/2013-01-01/2013-01-31/?format=json

            URL url = new URL(myUrlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }
                scanner.close();
                System.out.println(informationString);

                JSONParser parse = new JSONParser();
                JSONArray dataObject = (JSONArray) parse.parse(String.valueOf(informationString));

                double suma = 0;
                for (int i = 0; i < dataObject.size(); i++) {
                    JSONObject dayData = (JSONObject) dataObject.get(i);
                    double whatToAdd = (Double) dayData.get("cena");
                    suma += whatToAdd;
                }
                double answear = suma / dataObject.size();
//                System.out.println(answear);
                return Double.toString(answear);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }
}
