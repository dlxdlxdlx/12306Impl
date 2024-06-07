package com.dallxy.orderService.remote;

import com.dallxy.common.result.Result;
import com.dallxy.orderService.remote.dto.UserQueryActualRespDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "12306-user-service")
public interface UserRemoteService {
    /** 根据用户名查询用户信息
     * @param username 查询用的用户名
     * @return
     */
    @GetMapping("/api/user-service/actual/query")
    Result<UserQueryActualRespDTO> queryActualUserByUsername(@RequestParam("username")@NotEmpty String username);
}
