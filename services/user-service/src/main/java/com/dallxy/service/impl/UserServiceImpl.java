package com.dallxy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.common.constant.CacheKeyConstant;
import com.dallxy.common.exception.ClientException;
import com.dallxy.dao.UserDao;
import com.dallxy.dto.*;
import com.dallxy.mapper.UserMailMapper;
import com.dallxy.mapper.UserMapper;
import com.dallxy.service.UserService;
import com.dallxy.user.core.UserInfoDTO;
import com.dallxy.user.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMailMapper userMailMapper;
    private final UserMapper userMapper;
    private final LocalCache localCache;
    private final RemoteCache remoteCache;
    private final RBloomFilter<String> userReigisterCachePenetrationBloomFilter;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        String usernameOrMailOrPhone = requestParam.getUsernameOrMailOrPhone();
        String username = null;
        if (Validator.isEmail(usernameOrMailOrPhone)) {
            Optional.ofNullable(userMailMapper.getUserNameByMail(usernameOrMailOrPhone))
                    .orElseThrow(() -> new ClientException("邮箱不存在"));
        } else if (Validator.isMobile(usernameOrMailOrPhone)) {
            username = userMailMapper.getUserNameByPhone(usernameOrMailOrPhone);
        }
        username = Optional.ofNullable(username).orElse(requestParam.getUsernameOrMailOrPhone());
        UserDao userDao = userMapper.selectOne(Wrappers.lambdaQuery(UserDao.class)
                .eq(UserDao::getUsername, username)
                .eq(UserDao::getPassword, requestParam.getPassword())
                .select(UserDao::getId, UserDao::getUsername, UserDao::getRealName));
        if (Objects.isNull(userDao)) {
            throw new ClientException("账号/密码不存在");
        }
        UserInfoDTO userInfo = BeanUtil.copyProperties(userDao, UserInfoDTO.class);
        String token = JWTUtils.generateAccessToken(userInfo);
        userInfo.setAccessToken(token);
        //put data into cache
        localCache.put(CacheKeyConstant.TOKEN_PREFIX + token, JSON.toJSONString(userInfo));
        remoteCache.put(CacheKeyConstant.TOKEN_PREFIX + token, JSON.toJSONString(userInfo), 30, TimeUnit.MINUTES);
        return BeanUtil.copyProperties(userInfo, UserLoginRespDTO.class);
    }

    @Override
    public UserLoginRespDTO checkLogin(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new ClientException("token 不得为空");
        }
        return localCache.get(CacheKeyConstant.TOKEN_PREFIX + accessToken,
                UserLoginRespDTO.class,
                key -> remoteCache.getIfPresent(CacheKeyConstant.TOKEN_PREFIX + accessToken)
        );
    }

    @Override
    public void logout(String accessToken) {
        if (StringUtils.isNotBlank(accessToken)) {
            remoteCache.invalidate(CacheKeyConstant.TOKEN_PREFIX + accessToken);
        }
    }

    @Override
    public Boolean hasUsername(String username) {
//        if (userReigisterCachePenetrationBloomFilter.contains(username)) {
//            return stringRedisTemplate.opsForSet().isMember(USER_REGISTER_REUSE_SHARDING + username, username);
//        }
        return true;
    }

    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        return null;
    }

    @Override
    public void deletion(UserDeletionReqDTO requestParam) {

    }
}
