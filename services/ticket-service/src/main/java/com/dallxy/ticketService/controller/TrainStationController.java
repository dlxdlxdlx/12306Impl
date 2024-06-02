package com.dallxy.ticketService.controller;

import com.dallxy.common.result.Result;
import com.dallxy.ticketService.dto.resp.TrainStationQueryRespDTO;
import com.dallxy.ticketService.service.TrainStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrainStationController {
    private final TrainStationService trainStationService;


    public Result<List<TrainStationQueryRespDTO>>listTrainStationQuery(String trainId){
        throw new UnsupportedOperationException("Not implemented");
    }

}
