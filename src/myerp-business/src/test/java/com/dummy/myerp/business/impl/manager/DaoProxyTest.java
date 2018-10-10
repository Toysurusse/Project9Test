package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;

public class DaoProxyTest implements DaoProxy {

    private ComptabiliteDao comptabiliteDao;


    public DaoProxyTest(ComptabiliteDao comptabiliteDao) {
        super();

        this.comptabiliteDao = comptabiliteDao;
    }


    @Override
    public ComptabiliteDao getComptabiliteDao() {
        return comptabiliteDao;
    }
}