package ExternalServices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HTTPPostClient {

    public static String send(Map<String, String> params) throws Exception {

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append("=");
            postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://external-systems.000webhostapp.com/", postData, String.class);
        URL url = new URL("https://external-systems.000webhostapp.com/");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        http.setDoOutput(true);
        http.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0; )
            sb.append((char) c);
        return sb.toString();
    }


    public static Map<String, String> makeHandshakeParams(){
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action_type", "handshake");
        return params;
    }

    public static Map<String, String> makePayParams(String cardNumber, String month, String year, String holder, String cvv, String id){
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action_type", "pay");
        params.put("card_number", cardNumber);
        params.put("month", month);
        params.put("year", year);
        params.put("holder", holder);
        params.put("ccv", cvv);
        params.put("id", id);
        return params;
    }

    public static Map<String, String> makeCancelPayParams(String transactionId){
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action_type", "cancel_pay");
        params.put("transaction_id", transactionId);
        return params;
    }

    public static Map<String, String> makeSupplyParams(String name, String address, String city, String country, String zip){
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action_type", "supply");
        params.put("name", name);
        params.put("address", address);
        params.put("city", city);
        params.put("country", country);
        params.put("zip", zip);
        return params;
    }

    public static Map<String, String> makeCancelSupplyParams(String transactionId){
        Map<String, String> params = new LinkedHashMap<>();
        params.put("action_type", "cancel_supply");
        params.put("transaction_id", transactionId);
        return params;
    }

}