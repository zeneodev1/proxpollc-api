package com.zeneo.shop.model;

import lombok.Data;

import java.util.Map;

@Data
public class ShippingQuote {

    private ShippingResponse response;

    @Data
    public static class ShippingResponse {
        private Integer numQuotes;
        private ShippingMode mode;
    }

    @Data
    public static class ShippingMode {
        private Map<String, Object> price;
        private Map<String, Object> transitTimes;
        private String mode;
    }

}
