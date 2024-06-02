package com.dallxy.gatewayService.filter;


import cn.hutool.core.collection.CollUtil;
import com.dallxy.user.constant.UserConstant;
import com.dallxy.user.core.UserInfoDTO;
import com.dallxy.user.utils.JWTUtils;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.dallxy.gatewayService.filter.TokenValidateGatewayFilterFactory.TokenValidateConfig.isPathInBlackPreList;

@Component
public class TokenValidateGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenValidateGatewayFilterFactory.TokenValidateConfig> {
    public static final String DELETION_PATH = "/api/user-service/deletion";

    @Override
    public GatewayFilter apply(TokenValidateConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String requestPath = request.getPath().toString();
            if (isPathInBlackPreList(requestPath, config.getBlackPathPre())) {
                String token = request.getHeaders().getFirst("Authorization");
                // TODO 需要验证 Token 是否有效，有可能用户注销了账户，但是 Token 有效期还未过
                UserInfoDTO userInfo = JWTUtils.parseJwtToken(token);
                if (!Objects.isNull(userInfo)) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }

                ServerHttpRequest.Builder builder = exchange.getRequest().mutate().headers(httpHeaders -> {
                    httpHeaders.set(UserConstant.USER_ID_KEY, userInfo.getUserId());
                    httpHeaders.set(UserConstant.USER_NAME_KEY, userInfo.getUsername());
                    httpHeaders.set(UserConstant.REAL_NAME_KEY, URLEncoder.encode(userInfo.getRealName(), StandardCharsets.UTF_8));
                    if (Objects.equals(requestPath, DELETION_PATH)) {
                        httpHeaders.set(UserConstant.USER_TOKEN_KEY, token);
                    }
                });
                return chain.filter(exchange.mutate().request(builder.build()).build());
            }
            return chain.filter(exchange);
        };
    }

    @Data
    class TokenValidateConfig {
        private List<String> blackPathPre;

        public static Boolean isPathInBlackPreList(String requestPath, List<String> blackPathPre) {
            if (CollUtil.isEmpty(blackPathPre)){
                return false;
            }

            return blackPathPre.stream().anyMatch(requestPath::startsWith);
        }
    }
}
