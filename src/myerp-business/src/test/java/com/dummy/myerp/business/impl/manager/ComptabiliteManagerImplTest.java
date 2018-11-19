package com.dummy.myerp.business.impl.manager;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.consumer.dao.ComptabiliteDaoMock;
import com.dummy.myerp.consumer.dao.DaoProxyMock;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class ComptabiliteManagerImplTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    private EcritureComptable vEcritureComptable;
    private static SimpleDateFormat dateFormat;



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ComptabiliteDao comptabiliteDao=Mockito.mock(ComptabiliteDao.class);
        DaoProxy daoProxy = new DaoProxyMock(comptabiliteDao);
        ComptabiliteDaoMock comptabiliteDaoMock=new ComptabiliteDaoMock();
        ComptabiliteManagerImpl.configure(null, daoProxy, null);
        when(daoProxy.getComptabiliteDao().getListEcritureComptable()).thenReturn(comptabiliteDaoMock.getListEcritureComptable());
        dateFormat = new SimpleDateFormat("yyyy");
    }


    @Before
    public void setUp() throws FunctionalException {
        // Doit être une EcritureComptable valide
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(22);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setReference("AC-" + dateFormat.format(vEcritureComptable.getDate()) + "/00022");
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(123)));

    }


    @Test
    public void checkEcritureComptable() throws Exception {
        manager.checkEcritureComptable(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableContext() {
        String exception = ComptabiliteManagerImpl.RG6_EXCEPTION;

        try {
            vEcritureComptable.setId(-1);
            vEcritureComptable.setReference("AC-2016/00001");
            manager.checkEcritureComptableContext(vEcritureComptable);
            fail();
        }
        catch (FunctionalException e) {
            Assert.assertEquals(exception, e.getMessage());
        }
    }


    @Test
    public void checkEcritureComptableUnit() throws Exception {
        vEcritureComptable.setReference(null);
        manager.checkEcritureComptableUnit(vEcritureComptable);

        vEcritureComptable.setReference("AC-" + dateFormat.format(vEcritureComptable.getDate()) + "/00022");
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableUnitViolation() throws Exception {
        thrown.expect(FunctionalException.class);
        thrown.expectMessage(ComptabiliteManagerImpl.VIOLATION_EXCEPTION);

        vEcritureComptable = new EcritureComptable();

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableUnitRG2() throws Exception {
        thrown.expect(FunctionalException.class);
        thrown.expectMessage(ComptabiliteManagerImpl.RG2_EXCEPTION);

        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(2), null, null, new BigDecimal(1234)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableUnitRG3() throws Exception {
        thrown.expect(FunctionalException.class);
        thrown.expectMessage(ComptabiliteManagerImpl.RG3_EXCEPTION);

        vEcritureComptable.getListLigneEcriture().clear();
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(-123), null));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableUnitRG5Code() throws Exception {
        thrown.expect(FunctionalException.class);
        thrown.expectMessage(ComptabiliteManagerImpl.RG5_CODE_EXCEPTION);

        vEcritureComptable.setReference("AV-" + dateFormat.format(vEcritureComptable.getDate()) + "/00022");

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableUnitRG5Date() throws Exception {
        thrown.expect(FunctionalException.class);
        thrown.expectMessage(ComptabiliteManagerImpl.RG5_DATE_EXCEPTION);

        vEcritureComptable.setReference("AC-1963/00022");

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

}