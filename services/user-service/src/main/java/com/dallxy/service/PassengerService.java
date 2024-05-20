package com.dallxy.service;

import com.dallxy.dto.req.PassengerRemoveReqDTO;
import com.dallxy.dto.req.PassengerReqDTO;
import com.dallxy.dto.resp.PassengerActualRespDTO;
import com.dallxy.dto.resp.PassengerRespDTO;

import java.util.List;

public interface PassengerService {
    List<PassengerRespDTO> listPassengerQueryByUsername(String username);

    List<PassengerActualRespDTO> listPassengerQueryByIds(String username, List<Long> ids);

    void savePassenger(PassengerReqDTO requestParam);


    void updatePassenger(PassengerReqDTO requestParam);

    void removePassenger(PassengerRemoveReqDTO requestParam);
}
