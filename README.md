# OpenWeatherSDK

## Introduction

OpenWeatherSDK is a Java library designed to interact with the OpenWeatherMap API for retrieving weather information. This library simplifies the process of fetching current weather data for specific cities, offering support for both on-demand and polling modes. It includes data classes to represent various weather attributes and implements caching mechanisms for efficient data management.

## Contents

- [Installation](#installation)
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


## Usage Example

```java
// Create an instance of the OpenWeatherSDK
OpenWeatherSDK weatherSDK = OpenWeatherSDK.factory("your_api_key", OpenWeatherSDKMode.POLLING);
String city = "London";

// Retrieve weather information for the specified city
WeatherInfo weatherInfo = weatherSDK.getWeatherInfo(city);

// Display the weather details
System.out.println("Weather in " + city + ": " + weatherInfo.getTemperature() + "C, " + weatherInfo.getWeather().getDescription());
```
