package com.dallxy.userService.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.userService.common.constant.CacheKeyConstant;
import com.dallxy.userService.common.constant.RedisConstant;
import com.dallxy.common.exception.ClientException;
import com.dallxy.common.exception.ServiceException;
import com.dallxy.database.utils.MybatisUtils;
import com.dallxy.userService.dao.*;
import com.dallxy.userService.dto.req.UserDeletionReqDTO;
import com.dallxy.userService.dto.req.UserLoginReqDTO;
import com.dallxy.userService.dto.req.UserRegisterReqDTO;
import com.dallxy.userService.dto.req.UserUpdateReqDTO;
import com.dallxy.userService.dto.resp.UserLoginRespDTO;
import com.dallxy.userService.dto.resp.UserQueryActualRespDTO;
import com.dallxy.userService.dto.resp.UserQueryRespDTO;
import com.dallxy.userService.dto.resp.UserRegisterRespDTO;
import com.dallxy.userService.service.UserService;
import com.dallxy.user.core.UserContext;
import com.dallxy.user.core.UserInfoDTO;
import com.dallxy.user.utils.JWTUtils;
import com.dallxy.userService.mapper.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.dallxy.userService.common.constant.RedisConstant.USER_REGISTER_REUSE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMailMapper userMailMapper;
    private final UserMapper userMapper;
    private final LocalCache localCache;
    private final RemoteCache remoteCache;
    private final RBloomFilter<String> userReigisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserPhoneMapper userPhoneMapper;
    private final UserReuseMapper userReuseMapper;
    private final UserDeletionMapper userDeletionMapper;

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
            localCache.invalidate(CacheKeyConstant.TOKEN_PREFIX + accessToken);
            remoteCache.invalidate(CacheKeyConstant.TOKEN_PREFIX + accessToken);
        }
    }

    @Override
    public Boolean hasUsername(String username) {
        if (userReigisterCachePenetrationBloomFilter.contains(username)) {
            return stringRedisTemplate.opsForSet().isMember(USER_REGISTER_REUSE + username, username);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        System.out.println("register");
        /*
         * 涉及到三张表的操作: UserMapper, UserPhoneMapper, UserReuseMapper, 以及布隆过滤器
         * 当前版本只涉及局部事务,所以暂不考虑seata
         * 是否能通过线程池让多个数据库操作并发执行减少时间开销?(同时要求整体的一致性)
         *
         * 方案1:
         * 通过自定义注解配合 信号量实现并行进行数据库的CRUD操作
         *
         * 方案2:
         * 直接加锁
         *
         * 方案3:
         * seata
         * */
        String username = requestParam.getUsername();
        RLock lock = redissonClient.getLock(RedisConstant.USER_REGISTER_LOCK + username);
        if (!lock.tryLock()) {
            throw new ClientException("用户已注册");
        }

        try {
            try {
                int inserted = userMapper.insert(BeanUtil.copyProperties(requestParam, UserDao.class));
                if (MybatisUtils.insertFailed(inserted)) {
                    throw new ServiceException("用户注册失败");
                }
            } catch (Exception e) {
                throw new ServiceException("用户注册失败");
            }

            try {
                UserPhoneDao userPhoneDO = UserPhoneDao.builder().username(username).phone(requestParam.getPhone()).build();
                int inserted = userPhoneMapper.insert(userPhoneDO);
                if (MybatisUtils.insertFailed(inserted)) {
                    throw new ServiceException("用户注册失败");
                }
            } catch (Exception e) {
                throw new ServiceException("用户注册失败");
            }

            if (StringUtils.isNotBlank(requestParam.getMail())) {
                //TODO
                UserMailDao userMailDO = UserMailDao.builder().username(username).mail(requestParam.getMail()).build();
                try {
                    if (MybatisUtils.insertFailed(userMailMapper.insert(userMailDO))) {
                        throw new ServiceException("用户注册失败");
                    }
                } catch (Exception e) {
                    throw new ServiceException("用户注册失败");
                }
            }
            userReuseMapper.delete(Wrappers.update(UserReuseDao.builder().username(username).build()));
            stringRedisTemplate.opsForSet().remove(USER_REGISTER_REUSE + username, username);
            userReigisterCachePenetrationBloomFilter.add(username);

        } finally {
            lock.unlock();
        }
        return BeanUtil.copyProperties(requestParam, UserRegisterRespDTO.class);
    }

    @Override
    public void deletion(UserDeletionReqDTO requestParam) {
        String username = ""; //TODO
        if (!StringUtils.equals(username, requestParam.getUsername()) || StringUtils.isAllEmpty(username, requestParam.getUsername())) {
            throw new ClientException("用户名不匹配");
        }

        RLock lock = redissonClient.getLock(RedisConstant.USER_DELETION_LOCK + username);
        lock.lock();
        try {
            //TODO
            UserQueryRespDTO userQueryRespDTO = new UserQueryRespDTO();
            UserDeletionDao userDeletionDao = UserDeletionDao.builder()
                    .idCard(userQueryRespDTO.getIdCard())
                    .idType(userQueryRespDTO.getIdType())
                    .build();

            userDeletionMapper.insert(userDeletionDao);

            UserDao userDao = UserDao.builder()
                    .username(username)
                    .deletionTime(System.currentTimeMillis())
                    .build();
            userMapper.deletionUser(userDao);


            UserPhoneDao userPhoneDao = UserPhoneDao.builder()
                    .phone(userQueryRespDTO.getPhone())
                    .deletionTime(System.currentTimeMillis())
                    .build();
            userPhoneMapper.deletionUser(userPhoneDao);


            if (StringUtils.isNotBlank(userQueryRespDTO.getMail())) {
                UserMailDao userMailDao = UserMailDao.builder()
                        .mail(userQueryRespDTO.getMail())
                        .deletionTime(System.currentTimeMillis())
                        .build();
                userMailMapper.deletionUser(userMailDao);
            }

//            invalidate cache
            remoteCache.invalidate(CacheKeyConstant.TOKEN_PREFIX + UserContext.getToken());
            localCache.invalidate(CacheKeyConstant.TOKEN_PREFIX + UserContext.getToken());


        } finally {
            lock.unlock();
        }


    }

    @Override
    public void update(UserUpdateReqDTO reqestParam) {
        UserQueryRespDTO userQueryRespDTO = queryUserByUsername(reqestParam.getUsername());
        LambdaQueryWrapper<UserDao> queryWrapper = Wrappers.lambdaQuery(UserDao.class)
                .eq(UserDao::getUsername, reqestParam.getUsername());
        UserDao userDao = BeanUtil.copyProperties(reqestParam, UserDao.class);
        userMapper.update(userDao, queryWrapper);
        // 如果请求参数中的邮箱不为空，并且与当前用户的邮箱不同，那么更新用户的邮箱
        if (StringUtils.isNotBlank(reqestParam.getMail()) && !Objects.equals(reqestParam.getMail(), userQueryRespDTO.getMail())) {
            LambdaQueryWrapper<UserMailDao> updateWrapper = Wrappers.lambdaQuery(UserMailDao.class)
                    .eq(UserMailDao::getMail, userQueryRespDTO.getMail());
            // TODO: 延迟删除操作,先设置del_flag
            userMailMapper.delete(updateWrapper);
            UserMailDao userMailDao = UserMailDao.builder()
                    .mail(reqestParam.getMail())
                    .username(reqestParam.getUsername())
                    .build();
            userMailMapper.insert(userMailDao);
        }
    }

    @Override
    public Integer queryUserDeletionNum(Integer idType, String idCard) {
        LambdaQueryWrapper<UserDeletionDao> queryWrappers = Wrappers.lambdaQuery(UserDeletionDao.class)
                .eq(UserDeletionDao::getIdType, idType)
                .eq(UserDeletionDao::getIdCard, idCard);
        Long deletionCount = userDeletionMapper.selectCount(queryWrappers);

        return Optional.ofNullable(deletionCount).map(Long::intValue).orElse(0);
    }

    @Override
    public UserQueryRespDTO queryUserByUsername(String username) {
        LambdaQueryWrapper<UserDao> queryWrappper = Wrappers.lambdaQuery(UserDao.class)
                .eq(UserDao::getUsername, username);


        return Optional.ofNullable(userMapper.selectOne(queryWrappper))
                .map(o -> BeanUtil.copyProperties(o, UserQueryRespDTO.class))
                .orElseThrow(() -> new ClientException("user not exist"));

    }

    @Override
    public UserQueryRespDTO queryUserByUserId(String userId) {
        LambdaQueryWrapper<UserDao> queryWrapper = Wrappers.lambdaQuery(UserDao.class)
                .eq(UserDao::getId, userId);

        return Optional.ofNullable(userMapper.selectOne(queryWrapper))
                .map(o -> BeanUtil.copyProperties(o, UserQueryRespDTO.class))
                .orElseThrow(() -> new ClientException("user not exist"));
    }

    @Override
    public UserQueryActualRespDTO queryActualUserByUsername(String username) {
        return BeanUtil.copyProperties(queryUserByUsername(username), UserQueryActualRespDTO.class);
    }
}
