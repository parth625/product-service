package com.ecommerce.product_service.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequestDto {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String sortBy = "id";
    private Integer sortOrder = 1; // 1 = ASC, 2 = DESC
}
