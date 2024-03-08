package ru.github.fireg45.openweathersdk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sys {
    private long sunrise;
    private long sunset;
}
