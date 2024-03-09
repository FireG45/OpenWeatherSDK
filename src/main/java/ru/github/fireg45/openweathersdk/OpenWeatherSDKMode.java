/**
 * An enumeration representing the mode of operation for the OpenWeatherSDK.
 */
package ru.github.fireg45.openweathersdk;

/**
 * Enum defining the modes of operation for the OpenWeatherSDK.
 */
public enum OpenWeatherSDKMode {
    /**
     * Represents the on-demand mode where weather information is retrieved only when requested.
     */
    ON_DEMAND,

    /**
     * Represents the polling mode where weather information is retrieved for all cached cities.
     */
    POLLING
}
