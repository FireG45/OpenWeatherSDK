package ru.github.fireg45;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.github.fireg45.mock.HttpResponseMock;
import ru.github.fireg45.openweathersdk.OpenWeatherSDK;
import ru.github.fireg45.openweathersdk.OpenWeatherSDKMode;
import ru.github.fireg45.openweathersdk.dto.WeatherInfo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AppTest {

    private final String API_KEY = "API_KEY";

    private HttpClient httpClient = HttpClient.newBuilder().build();

    final HttpResponse.BodyHandler<?> handler = HttpResponse.BodyHandlers.ofString();

    private OpenWeatherSDK openWeatherSDK;

    @Test
    void Test_singleObjectPerApiKey() {
        openWeatherSDK = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.POLLING);
        OpenWeatherSDK openWeatherSDK1 = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.POLLING);
        Assertions.assertEquals(openWeatherSDK, openWeatherSDK1);
    }

    @Test
    void Test_objectPool() {
        openWeatherSDK = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.POLLING);
        OpenWeatherSDK openWeatherSDK1 = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.POLLING);
        openWeatherSDK1.releaseSDKObject();
        openWeatherSDK1 = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.POLLING);
        Assertions.assertNotEquals(openWeatherSDK, openWeatherSDK1);
    }

    @Test
    void Test_weatherRequestOnDemand_CachedTest() throws IOException, InterruptedException, URISyntaxException {
        String city = "Kazan";
        URI uri = URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY);

        openWeatherSDK = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.POLLING);

        httpClient = Mockito.mock(HttpClient.class);

        HttpRequest request = HttpRequest.newBuilder(uri).build();

        HttpResponse.BodyHandler<?> response = HttpResponse.BodyHandlers.ofString();

        Mockito.when(httpClient.send(request, response))
                .thenReturn(new HttpResponseMock<>());


        openWeatherSDK.setHttpClient(httpClient);

        WeatherInfo w1 = openWeatherSDK.getWeatherInfo(city);
        WeatherInfo w2 = openWeatherSDK.getWeatherInfo(city);
        WeatherInfo w3 = openWeatherSDK.getWeatherInfo(city);

        Assertions.assertEquals(w1, w2);
        Assertions.assertEquals(w2, w3);
    }

    @Test
    void Test_weatherRequestPolling() throws IOException, InterruptedException {
        URI uri1 = URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "Kazan" + "&appid=" + API_KEY);
        URI uri2 = URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "Moscow" + "&appid=" + API_KEY);
        URI uri3 = URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "London" + "&appid=" + API_KEY);

        openWeatherSDK = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.POLLING);

        httpClient = Mockito.mock(HttpClient.class);

        openWeatherSDK.setHttpClient(httpClient);

        Mockito.when(httpClient.send(HttpRequest.newBuilder(uri1).build(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(new HttpResponseMock<>());
        Mockito.when(httpClient.send(HttpRequest.newBuilder(uri2).build(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(new HttpResponseMock<>());
        Mockito.when(httpClient.send(HttpRequest.newBuilder(uri3).build(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(new HttpResponseMock<>());

        WeatherInfo w1K = openWeatherSDK.getWeatherInfo("Kazan");
        WeatherInfo w1M = openWeatherSDK.getWeatherInfo("Moscow");
        WeatherInfo w1L = openWeatherSDK.getWeatherInfo("London");

        WeatherInfo w2K = openWeatherSDK.getWeatherInfo("Kazan");
        WeatherInfo w2M = openWeatherSDK.getWeatherInfo("Moscow");
        WeatherInfo w2L = openWeatherSDK.getWeatherInfo("London");

        Assertions.assertEquals(w1K, w2K);
        Assertions.assertEquals(w1M, w2M);
        Assertions.assertEquals(w1L, w2L);
    }

    @Test
    void Test_cacheCapacityTest() throws IOException, InterruptedException {

        URI[] uris = new URI[] {
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city1" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city2" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city3" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city4" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city5" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city6" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city7" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city8" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city9" + "&appid=" + API_KEY),
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "city10" + "&appid=" + API_KEY)
        };

        openWeatherSDK = OpenWeatherSDK.factory(API_KEY, OpenWeatherSDKMode.ON_DEMAND);

        httpClient = Mockito.mock(HttpClient.class);

        openWeatherSDK.setHttpClient(httpClient);

        for (URI uri : uris) {
            Mockito.when(httpClient.send(HttpRequest.newBuilder(uri).build(), HttpResponse.BodyHandlers.ofString()))
                    .thenReturn(new HttpResponseMock<>());
        }

        WeatherInfo[] weatherInfos = new WeatherInfo[uris.length];

        for (int i = 0; i < uris.length; i++) {
            weatherInfos[i] = openWeatherSDK.getWeatherInfo("city" + (i + 1));
        }

        URI newCity1URI =
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "NEW_CITY1" + "&appid=" + API_KEY);
        URI newCity2URI =
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + "NEW_CITY2" + "&appid=" + API_KEY);

        Mockito.when(httpClient.send(HttpRequest.newBuilder(newCity1URI).build(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(new HttpResponseMock<>());

        Mockito.when(httpClient.send(HttpRequest.newBuilder(newCity2URI).build(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(new HttpResponseMock<>());

        openWeatherSDK.getWeatherInfo("NEW_CITY1");
        openWeatherSDK.getWeatherInfo("NEW_CITY2");

        openWeatherSDK.getWeatherInfo("city1");

        Mockito.verify(httpClient, Mockito.times(2))
                .send(HttpRequest.newBuilder(uris[0]).build(), HttpResponse.BodyHandlers.ofString());
    }
}
