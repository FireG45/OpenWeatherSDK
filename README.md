# OpenWeatherSDK

## Introduction

OpenWeatherSDK is a Java library designed to interact with the OpenWeatherMap API for retrieving weather information. This library simplifies the process of fetching current weather data for specific cities, offering support for both on-demand and polling modes. It includes data classes to represent various weather attributes and implements caching mechanisms for efficient data management.

## Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage Example](#usage-example)

## Installation

Add the following dependency to your Maven project:

```xml
<dependency>
    <groupId>ru.github.fireg45</groupId>
    <artifactId>openweathersdk</artifactId>
    <version>1.0.0</version>
</dependency>
```
## Configuration

To configure and use the OpenWeatherSDK in your application, follow these steps:

1. Obtain an API key from [OpenWeatherMap](https://openweathermap.org/api).
2. Initialize the OpenWeatherSDK with your API key using the factory method. The factory method ensures a single instance of OpenWeatherSDK for each unique API key, enhancing efficiency in object creation and management.

```java
OpenWeatherSDK weatherSDK = OpenWeatherSDK.factory("your_api_key", OpenWeatherSDKMode.POLLING);
```

## Usage Example

```java
// Create an instance of the OpenWeatherSDK
OpenWeatherSDK weatherSDK = OpenWeatherSDK.factory("your_api_key", OpenWeatherSDKMode.POLLING);
String city = "Kazan'";

// Retrieve weather information for the specified city
WeatherInfo weatherInfo = weatherSDK.getWeatherInfo(city);

// Display the weather details
System.out.println("Weather in " + city + ": " + weatherInfo.getTemperature() + "C, " + weatherInfo.getWeather().getDescription());

// Get weather information as a JSON object
JSONObject weatherJson = weatherSDK.getWeatherAsJsonObject(city);

// Display weather information as a JSON object
System.out.println("Weather in " + city + " as JSON: " + weatherJson.toString());
```

The JSON object representing weather information can be formatted as follows:

```json
{
  "weather": {
    "main": "Clouds",
    "description": "overcast clouds"
  },
  "datetime": 1709905202,
  "visibility": 1875,
  "temperature": {
    "temp": 269.1,
    "feels_like": 265.08
  },
  "sys": {
    "sunrise": 1709867797,
    "sunset": 1709908329
  },
  "wind": {
    "speed": 2
  },
  "timezone": 10800,
  "name": "Kazan’"
}
```
