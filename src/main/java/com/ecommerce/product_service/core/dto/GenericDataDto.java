package com.ecommerce.product_service.core.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GenericDataDto {
    private int responseCode = HttpStatus.OK.value();
    private String responseMessage = HttpStatus.OK.getReasonPhrase();
    private Object data;
    private List<?> dataList = new ArrayList<>();
    private long totalRecords = 0;
    private long pageRecords = 0;
    private int currentPageNumber = 1;
    private long totalPages = 1;
}
