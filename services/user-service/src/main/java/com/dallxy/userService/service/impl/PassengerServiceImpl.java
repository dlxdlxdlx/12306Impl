package com.dallxy.userService.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dallxy.cache.LocalCache;
import com.dallxy.cache.RemoteCache;
import com.dallxy.common.exception.ClientException;
import com.dallxy.common.exception.ServiceException;
import com.dallxy.userService.dao.PassengerDao;
import com.dallxy.userService.dto.req.PassengerRemoveReqDTO;
import com.dallxy.userService.dto.req.PassengerReqDTO;
import com.dallxy.userService.dto.resp.PassengerActualRespDTO;
import com.dallxy.userService.dto.resp.PassengerRespDTO;
import com.dallxy.userService.mapper.PassengerMapper;
import com.dallxy.userService.service.PassengerService;
import com.dallxy.user.constant.VerifyStatusEnum;
import com.dallxy.user.core.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.dallxy.userService.common.constant.CacheKeyConstant.USER_PASSENGER_LIST;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerMapper passengerMapper;
    private final LocalCache localCache;
    private final RemoteCache remoteCache;

    @Override
    public List<PassengerRespDTO> listPassengerQueryByUsername(String username) {
        String actualUserPassengerListStr = getActualUserPassengerListStr(username);
        return Optional.ofNullable(actualUserPassengerListStr)
                .map(each -> JSON.parseArray(each, PassengerDao.class))
                .map(each -> BeanUtil.copyToList(each, PassengerRespDTO.class))
                .orElse(List.of());
    }

    private String getActualUserPassengerListStr(String username) {
        String key = USER_PASSENGER_LIST + username;
        return localCache.get(key, String.class, v ->
                remoteCache.get(key, k -> {
                    LambdaQueryWrapper<PassengerDao> queryWrapper = Wrappers.lambdaQuery(PassengerDao.class).eq(PassengerDao::getUsername, username);
                    List<PassengerDao> passengerList = passengerMapper.selectList(queryWrapper);
                    return CollUtil.isNotEmpty(passengerList) ? JSON.toJSONString(passengerList) : null;
                }, 1L, TimeUnit.DAYS));
    }

    @Override
    public List<PassengerActualRespDTO> listPassengerQueryByIds(String username, List<Long> ids) {
        String actualUserPassengerListStr = getActualUserPassengerListStr(username);

        return Optional.ofNullable(actualUserPassengerListStr)
                .map(each -> JSON.parseArray(each, PassengerDao.class))
                .map(each -> each.stream()
                        .filter(passenger -> ids.contains(passenger.getId()))
                        .map(each2 -> BeanUtil.copyProperties(each2, PassengerActualRespDTO.class))
                        .toList())
                .orElse(List.of());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePassenger(PassengerReqDTO requestParam) {
        try {
            PassengerDao passengerDao = BeanUtil.copyProperties(requestParam, PassengerDao.class);
            passengerDao.setUsername(UserContext.getUsername());
            passengerDao.setCreateDate(new Date());
            passengerDao.setVerifyStatus(VerifyStatusEnum.REVIEWED.getCode());
            if (!SqlHelper.retBool(passengerMapper.insert(passengerDao))) {
                throw new ServiceException("新增乘车人失败");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
//        TODO: 删除缓存
        delPassengerCache(UserContext.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassenger(PassengerReqDTO requestParam) {
        try {
            String username = UserContext.getUsername();
            PassengerDao passengerDao = BeanUtil.copyProperties(requestParam, PassengerDao.class);
            LambdaUpdateWrapper<PassengerDao> updateWrapper = Wrappers.lambdaUpdate(PassengerDao.class)
                    .eq(PassengerDao::getUsername, username)
                    .eq(PassengerDao::getId, passengerDao.getId());
            if (!SqlHelper.retBool(passengerMapper.update(passengerDao, updateWrapper))) {
                throw new ServiceException("修改乘车人失败");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        delPassengerCache(UserContext.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePassenger(PassengerRemoveReqDTO requestParam) {
        String username = UserContext.getUsername();
        Optional.ofNullable(passengerMapper.selectPassengerByUsernameAndId(username, Long.valueOf(requestParam.getId())))
                .orElseThrow(() -> new ClientException("乘车人不存在"));
        try {
            LambdaUpdateWrapper<PassengerDao> deleteWrapper = Wrappers.lambdaUpdate(PassengerDao.class)
                    .eq(PassengerDao::getUsername, username)
                    .eq(PassengerDao::getId, requestParam.getId());
            if (!SqlHelper.retBool(passengerMapper.delete(deleteWrapper))) {
                throw new ServiceException("删除乘车人失败");
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private void delPassengerCache(String username) {
        remoteCache.invalidate(USER_PASSENGER_LIST + username);
        localCache.invalidate(USER_PASSENGER_LIST + username);
    }
}
