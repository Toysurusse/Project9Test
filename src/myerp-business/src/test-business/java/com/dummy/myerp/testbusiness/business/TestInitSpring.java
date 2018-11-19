package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.BusinessProxyImpl;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Classe de test de l'initialisation du contexte Spring
 */
public class TestInitSpring extends BusinessTestCase {

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



    @org.junit.Test
    public void checkCCJCandEC() {
        Assert.assertTrue(manager.getListCompteComptable().size()==7);
        Assert.assertTrue(manager.getListEcritureComptable().size()==5);
        Assert.assertTrue(manager.getListJournalComptable().size()==4);
    }

    @org.junit.Test
    public void checkInsertToDelete() throws Exception {
        boolean control = false;
        manager.insertEcritureComptable(vEcritureComptable);
        List<EcritureComptable> ecritureComptableList = manager.getListEcritureComptable();
        for (EcritureComptable ec: ecritureComptableList) {
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

    @org.junit.Test
    public void addReference() throws NotFoundException, FunctionalException {
        manager.addReference(vEcritureComptable);
    }


    @org.junit.Test
    public void TestTotalCompte() throws NotFoundException, FunctionalException {
        Assert.assertTrue(manager.soldeCompteComptable(606).equals("Créditeur"));
        Assert.assertTrue(manager.soldeCompteComptable(512).equals("Débiteur"));
    }

    @org.junit.Test(expected = FunctionalException.class)
    public void testFunctionalCC() throws FunctionalException {
        manager.soldeCompteComptable(2550);
    }
}
