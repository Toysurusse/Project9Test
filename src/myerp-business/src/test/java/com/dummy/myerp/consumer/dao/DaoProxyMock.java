package com.dummy.myerp.consumer.dao;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;


public class DaoProxyMock implements DaoProxy {


    private ComptabiliteDao comptabiliteDao;


    public DaoProxyMock() {
        super();
    }


    public DaoProxyMock(ComptabiliteDao comptabiliteDao) {
        super();

        this.comptabiliteDao = comptabiliteDao;
    }


    @Override
    public ComptabiliteDao getComptabiliteDao() {
        return comptabiliteDao;
    }


    public void setComptabiliteDao(ComptabiliteDao comptabiliteDao) {
        this.comptabiliteDao = comptabiliteDao;
    }
}