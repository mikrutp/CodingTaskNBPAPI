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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    DaysCalculator daysCalculator;

    public List<String> getCurrencyExchangeRatePLNToCurrencyCode(String currencyCodeInput) {

        String currencyCode = currencyCodeInput;
        List<String> answearList = new ArrayList<String>();

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://api.nbp.pl/api/exchangerates/rates/a/");
            urlBuilder.append(currencyCode);
            urlBuilder.append("/");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();
            urlBuilder.append(daysCalculator.subtractDaysSkippingWeekends(now, 4));
            urlBuilder.append("/");
            urlBuilder.append(now);

            urlBuilder.append("/?format=json");
            String myUrlString = urlBuilder.toString();

//          Docelowy przykladowy URL: https://api.nbp.pl/api/exchangerates/rates/a/usd/2012-01-01/2012-01-31/?format=json

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
//                System.out.println(informationString);
                JSONObject object = (JSONObject) new JSONParser().parse(String.valueOf(informationString));
                JSONArray ratesRowdata = (JSONArray) object.get("rates");
                List<Double> list = new ArrayList<>();
                for (int i = 0; i < ratesRowdata.size(); i++) {
                    JSONObject index = (JSONObject) ratesRowdata.get(i);
//                    System.out.println("INDEX: " + index);
                    double data = (double) index.get("mid");
//                    System.out.println(data);
                    list.add(data);
                }
                for (int j = 0; j < list.size(); j++) {
                    System.out.println(list.get(j));
                    answearList.add(String.valueOf(list.get(j)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answearList;
    }

}
