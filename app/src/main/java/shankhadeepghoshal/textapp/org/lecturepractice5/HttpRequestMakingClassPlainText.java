package shankhadeepghoshal.textapp.org.lecturepractice5;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class HttpRequestMakingClassPlainText implements Callable<String> {
    private final HashMap<String,String> requestBody;
    private final String URL;
    private static final String charset = StandardCharsets.UTF_8.name();


    public HttpRequestMakingClassPlainText(HashMap<String, String> requestBody, String url) {
        this.requestBody = requestBody;
        URL = url;
    }

    private String fireHttpRequestToRESTService() throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection)
                new URL(this.URL).openConnection();

        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Accept-Charset",charset);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="+charset);

        StringBuilder queryString = new StringBuilder();
        for(Map.Entry<String,String> iterator : this.requestBody.entrySet()){
            String parameter = URLEncoder.encode(iterator.getKey(),charset);
            String value = iterator.setValue(parameter);
            queryString.append(parameter)
                    .append("=")
                    .append(value)
                    .append("&");
        }
        String actualQueryString = queryString.toString();

        try(OutputStream stream = urlConnection.getOutputStream()){
            stream.write(actualQueryString
                    .getBytes(charset));
            stream.flush();
        }

        InputStream responseStream = urlConnection.getInputStream();
        InputStream errorStream = urlConnection.getErrorStream();
        if(errorStream!=null)
            Log.e("ErrorMessage", new BufferedReader(new InputStreamReader(errorStream)).readLine());
        return new BufferedReader(new InputStreamReader(responseStream)).readLine();
    }

    @Override
    public String call() {
        try {
            return fireHttpRequestToRESTService();
        } catch (IOException e) {
            e.printStackTrace();
            return "error making request";
        }
    }
}