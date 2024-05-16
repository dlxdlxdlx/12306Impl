package com.dallxy.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dallxy.common.enums.DelEnum;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

public class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
            this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
            this.strictInsertFill(metaObject, "delFlag", Integer.class, DelEnum.NORMAL.code());
        }


        @Override
        public void updateFill(MetaObject metaObject) {
            this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        }
    }
