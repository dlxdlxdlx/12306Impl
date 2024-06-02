package com.dallxy.userService.controller;

import com.dallxy.common.result.Result;
import com.dallxy.common.result.Results;
import com.dallxy.userService.dto.req.PassengerRemoveReqDTO;
import com.dallxy.userService.dto.req.PassengerReqDTO;
import com.dallxy.userService.dto.resp.PassengerActualRespDTO;
import com.dallxy.userService.dto.resp.PassengerRespDTO;
import com.dallxy.userService.service.PassengerService;
import com.dallxy.user.core.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;


    @GetMapping("/api/user-service/passenger/query")
    public Result<List<PassengerRespDTO>> listPassengerQueryByUsername() {
        return Results.success(passengerService.listPassengerQueryByUsername(UserContext.getUsername()));
    }

    @GetMapping("/api/user-service/inner/passenger/actual/query/ids")
    public Result<List<PassengerActualRespDTO>> listPassengerQueryByIds(@RequestParam("username") String username, @RequestParam("ids") List<Long> ids) {
        return Results.success(passengerService.listPassengerQueryByIds(username, ids));
    }

    //    TODO 幂等相关操作判断
    @PostMapping("/api/user-service/passenger/save")
    public Result<Void> savePassenger(@RequestBody PassengerReqDTO requestParam) {
        passengerService.savePassenger(requestParam);
        return Results.success();
    }

    @PostMapping("/api/user-service/passenger/remove")
    public Result<Void> removePassenger(@RequestBody PassengerRemoveReqDTO requestParam) {
        passengerService.removePassenger(requestParam);
        return Results.success();
    }
}
