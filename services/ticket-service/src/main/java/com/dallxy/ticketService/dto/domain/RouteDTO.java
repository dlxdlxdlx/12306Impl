package com.dallxy.ticketService.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private String startStation;

    private String endStation;
}
