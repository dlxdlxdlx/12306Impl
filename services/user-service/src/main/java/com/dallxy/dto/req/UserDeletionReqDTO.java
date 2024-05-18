package com.dallxy.dto.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeletionReqDTO {
    private String username;
}
