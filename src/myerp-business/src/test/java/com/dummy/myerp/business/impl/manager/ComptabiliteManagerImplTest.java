package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
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
    public static void setUpBeforeClass() {
        SpringRegistry.getTransactionManager().beginTransactionMyERP();
        SpringRegistry.getTransactionManager().commitMyERP(SpringRegistry.getTransactionManager().beginTransactionMyERP());
        SpringRegistry.getTransactionManager().rollbackMyERP(SpringRegistry.getTransactionManager().beginTransactionMyERP());
        SpringRegistry.getTransactionManager().beginTransactionMyERP();
        BusinessProxy business = BusinessProxyImpl.getInstance(SpringRegistry.getDaoProxy(),SpringRegistry.getTransactionManager());
        ComptabiliteManagerImpl.configure( SpringRegistry.getBusinessProxy(), SpringRegistry.getDaoProxy(), SpringRegistry.getTransactionManager());
    }


    @Before
    public void setUp() throws FunctionalException {
        // Doit être une EcritureComptable valide
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(22);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = isoFormat.parse("2016-05-23T09:01:02");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vEcritureComptable.setDate(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(vEcritureComptable.getDate());
        vEcritureComptable.setReference("AC-" + calendar.get(Calendar.YEAR) + "/00022");
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(411), null, new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture()
                .add(new LigneEcritureComptable(new CompteComptable(512), null, null, new BigDecimal(123)));
    }



    @Test
    public void checkCCJCandEC() throws Exception {
        Assert.assertTrue(manager.getListCompteComptable().size()==7);
        Assert.assertTrue(manager.getListEcritureComptable().size()==5);
        Assert.assertTrue(manager.getListJournalComptable().size()==4);
        ;
    }

    @Test
    public void checkInsertToDelete() throws Exception {
        boolean control = false;
        manager.insertEcritureComptable(vEcritureComptable);
        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        for (EcritureComptable ec: ecritureComptableList) {
            System.out.println(ec.getId());
            if(ec.getLibelle().equals("Libelle")){
                control = true;
            }
        }
        Assert.assertTrue("L'écriture comptable n'a pas pu être insérée", control);

        control = false;
        vEcritureComptable.setLibelle("Test Libellé");
        manager.updateEcritureComptable(vEcritureComptable);
        ecritureComptableList.clear();
        ecritureComptableList = manager.getListEcritureComptable();
        for (EcritureComptable ec: ecritureComptableList) {
            if(ec.getLibelle().equals("Test Libellé")){
                control = true;
            }
        }
        Assert.assertTrue("L'écriture comptable n'a pas pu être mise à jour", control);

        control = true;
        manager.deleteEcritureComptable(vEcritureComptable.getId());
        ecritureComptableList.clear();
        ecritureComptableList = manager.getListEcritureComptable();
        for (EcritureComptable ec: ecritureComptableList) {
            if(ec.getLibelle().equals("Test Libellé")){
                control = false;
            }
        }
        Assert.assertTrue("L'écriture comptable n'a pas pu être supprimée", control);

    }

    @Test
    public void checkAddReference() throws Exception {
        manager.addReference(vEcritureComptable);
    }


    @Test
    public void checkEcritureComptable() throws Exception {
        manager.checkEcritureComptable(vEcritureComptable);
    }



    //Mocker
    @Test
    public void checkEcritureComptableContext() {
        String exception = ComptabiliteManagerImpl.RG6_EXCEPTION;

        // id == null
        try {
            vEcritureComptable.setId(null);
            vEcritureComptable.setReference("AA-2018/00001");
            manager.checkEcritureComptableContext(vEcritureComptable);
            fail();
        }
        catch (FunctionalException e) {
            Assert.assertEquals("La référence d'une écriture comptable doit être unique.", e.getMessage());
        }

        // id != expected
        try {
            vEcritureComptable.setId(-5);
            manager.checkEcritureComptableContext(vEcritureComptable);
            fail();
        }
        catch (FunctionalException e) {
            Assert.assertEquals("La référence d'une écriture comptable doit être unique.", e.getMessage());
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