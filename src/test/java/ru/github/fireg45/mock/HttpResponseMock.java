package ru.github.fireg45.mock;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpResponseMock<T> implements HttpResponse<T> {

    @Override
    public int statusCode() {
        return 200;
    }

    @Override
    public HttpRequest request() {
        return null;
    }

    @Override
    public Optional<HttpResponse<T>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public T body() {
        return (T) "{\"coord\":{\"lon\":49.1221,\"lat\":55.7887},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"base\":\"stations\",\"main\":{\"temp\":267.43,\"feels_like\":264.26,\"temp_min\":267.38,\"temp_max\":267.49,\"pressure\":1007,\"humidity\":98,\"sea_level\":1007,\"grnd_level\":999},\"visibility\":1191,\"wind\":{\"speed\":1.85,\"deg\":284,\"gust\":2.35},\"snow\":{\"1h\":0.1},\"clouds\":{\"all\":85},\"dt\":1709924708,\"sys\":{\"type\":2,\"id\":48937,\"country\":\"RU\",\"sunrise\":1709867797,\"sunset\":1709908329},\"timezone\":10800,\"id\":551487,\"name\":\"Kazan’\",\"cod\":200}";
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public HttpClient.Version version() {
        return null;
    }
}
