package com.atabek.skyparktashkent.model.SkyParkModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counter{
    private Quadrature quadrature;
    private Attractions attractions;
    private String cafe;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Quadrature{
        private Integer total;
        private String unit;
        private String info;

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attractions{
        private Integer total;
        private String info;
    }
}




