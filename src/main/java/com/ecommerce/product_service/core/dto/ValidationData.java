package com.ecommerce.product_service.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationData {
    private boolean valid = true;
    private String message;
}
