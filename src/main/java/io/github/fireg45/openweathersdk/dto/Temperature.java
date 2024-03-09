package io.github.fireg45.openweathersdk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Temperature {
    private double temp;
    private double feels_like;
}
