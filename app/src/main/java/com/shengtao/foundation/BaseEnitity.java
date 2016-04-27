package com.shengtao.foundation;

import java.io.Serializable;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/15 18:13
 * @package： com.foudation.framework.base
 * @classname： BaseEnitity.java
 * @description： 数据模型基类，所有的数据模型都必须继承自BaseEnitity类
 */
public class BaseEnitity implements Serializable {

    public int ClassId;

    public int getClassId() {
        return ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

}
