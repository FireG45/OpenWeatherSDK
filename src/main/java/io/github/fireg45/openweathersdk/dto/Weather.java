package io.github.fireg45.openweathersdk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Weather {
    private String main;
    private String description;
}
