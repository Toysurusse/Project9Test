package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import com.dummy.myerp.testbusiness.business.SpringRegistry;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;


public class ComptabiliteManagerImplTest extends BusinessTestCase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
    private EcritureComptable vEcritureComptable;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        DaoProxyTest daoProxy= new DaoProxyTest(new ComptabiliteDaoTest());
        SpringRegistry.getTransactionManager().beginTransactionMyERP();
        SpringRegistry.getTransactionManager().commitMyERP(SpringRegistry.getTransactionManager().beginTransactionMyERP());
        SpringRegistry.getTransactionManager().rollbackMyERP(SpringRegistry.getTransactionManager().beginTransactionMyERP());
        SpringRegistry.getTransactionManager().beginTransactionMyERP();
        BusinessProxy business = BusinessProxyImpl.getInstance(daoProxy,SpringRegistry.getTransactionManager());
        ComptabiliteManagerImpl.configure( SpringRegistry.getBusinessProxy(), daoProxy, SpringRegistry.getTransactionManager());
    }


    @Before
    public void setUp() throws FunctionalException {
        // Doit être une EcritureComptable valide
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(22);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(vEcritureComptable.getDate());
        vEcritureComptable.setReference("AC-" + calendar.get(Calendar.YEAR) + "/00022");
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

        // id == null
        try {
            vEcritureComptable.setId(null);

            manager.checkEcritureComptableContext(vEcritureComptable);
            fail();
        }
        catch (FunctionalException e) {
            Assert.assertEquals(exception, e.getMessage());
        }

        // id != expected
        try {
            vEcritureComptable.setId(33);

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

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(vEcritureComptable.getDate());
        vEcritureComptable.setReference("AC-" + calendar.get(Calendar.YEAR) + "/00022");
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

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(vEcritureComptable.getDate());
        vEcritureComptable.setReference("AV-" + calendar.get(Calendar.YEAR) + "/00022");

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptableUnitRG5Date() throws Exception {
        thrown.expect(FunctionalException.class);
        thrown.expectMessage(ComptabiliteManagerImpl.RG5_DATE_EXCEPTION);

        vEcritureComptable.setReference("AC-1967/00022");

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

}