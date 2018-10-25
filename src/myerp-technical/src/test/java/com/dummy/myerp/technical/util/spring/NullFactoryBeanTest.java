package com.dummy.myerp.technical.util.spring;

import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.Assert;
import org.junit.Test;

public class NullFactoryBeanTest {


    @Test
    public void testBean () throws Exception {
        Class<Object> pObjectType=null;
        NullFactoryBean<Object> test = new NullFactoryBean<Object>(pObjectType);
        Assert.assertNull(test.getObject());
        Assert.assertNull(test.getObjectType());
        Assert.assertFalse(test.isSingleton());
    }
}
