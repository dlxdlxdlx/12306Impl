package com.dallxy.ticketService.controller;

import com.dallxy.common.result.Result;
import com.dallxy.common.result.Results;
import com.dallxy.ticketService.dto.resp.RegionStationQueryRespDTO;
import com.dallxy.ticketService.dto.resp.StationQueryRespDTO;
import com.dallxy.ticketService.service.RegionStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegionStationController {
    private final RegionStationService regionStationService;


    @GetMapping("/api/ticket-service/region-station/query")
    public Result<List<RegionStationQueryRespDTO>> listRegionStationQuery(String regionId){
        throw new UnsupportedOperationException("Not implemented");
    }

    @GetMapping("/api/ticket-service/station/all")
    public Result<List<StationQueryRespDTO>>listAllStation(){
        return Results.success(regionStationService.listAllStation());
    }
}
