package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testconsumer.consumer.ConsumerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class ComptabiliteDaoImplTes extends ConsumerTestCase {

    private static ComptabiliteDaoImpl dao;
    private static EcritureComptable vEcritureComptable;
    private static Date vCurrentDate;
    private static Integer vCurrentYear;

    @BeforeClass
    static void initAll() {
        dao = new ComptabiliteDaoImpl();
        vCurrentDate = new Date();
        vCurrentYear = LocalDateTime.ofInstant(vCurrentDate.toInstant(), ZoneId.systemDefault()).toLocalDate().getYear();
    }

    @Before
    void init() {
        vEcritureComptable = new EcritureComptable();
    }

    @After
    static void tearDownAll() {
        vEcritureComptable = null;
    }


    // ==================== CompteComptable - GET ====================

    @Test
    void getListCompteComptable() {
        List<CompteComptable> vList = dao.getListCompteComptable();
        assertEquals(7, vList.size());
    }


    // ==================== JournalComptable - GET ====================

    @Test
    void getListJournalComptable() {
        List<JournalComptable> vList = dao.getListJournalComptable();
        assertEquals(4, vList.size());
    }


    // ==================== EcritureComptable - GET ====================

    @Test
    void getListEcritureComptable() {
        List<EcritureComptable> vList = dao.getListEcritureComptable();
        assertEquals(5, vList.size());
    }

    @Test
    void getEcritureComptable() throws NotFoundException {
        EcritureComptable vEcritureComptable = dao.getEcritureComptable(-3);
        assertEquals("BQ-2016/00003", vEcritureComptable.getReference());

    }

    @Test
    void getEcritureComptableByRef() throws NotFoundException {
        EcritureComptable vEcritureComptable = dao.getEcritureComptableByRef("BQ-2016/00003");
        assertEquals("BQ", vEcritureComptable.getJournal().getCode());
        String vEcritureYear = new SimpleDateFormat("yyyy").format(vEcritureComptable.getDate());
        assertEquals("2016", vEcritureYear);
        assertEquals(-3, vEcritureComptable.getId().intValue());

    }

    @Test
    void loadListLigneEcriture() {
        vEcritureComptable.setId(-5);
        dao.loadListLigneEcriture(vEcritureComptable);
        assertEquals(2, vEcritureComptable.getListLigneEcriture().size());
    }


    // ==================== EcritureComptable - INSERT ====================

    @Test
    void insertEcritureComptable() throws FunctionalException {
        vEcritureComptable.setJournal(new JournalComptable("OD", "Opérations Diverses"));
        vEcritureComptable.setReference("OD-" + vCurrentYear + "/00200");
        vEcritureComptable.setDate(vCurrentDate);
        vEcritureComptable.setLibelle("Sandwichs");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                "Club saumon", new BigDecimal(10),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4456),
                "TVA 20%", new BigDecimal(2),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                "Facture S110001", null,
                new BigDecimal(12)));

        dao.insertEcritureComptable(vEcritureComptable);
    }


    // ==================== EcritureComptable - UPDATE ====================

    @Test
    void updateEcritureComptable() throws FunctionalException {
        vEcritureComptable.setId(-4);
        vEcritureComptable.setJournal(new JournalComptable("OD", "Opérations Diverses"));
        vEcritureComptable.setReference("OD-" + vCurrentYear + "/00200");
        vEcritureComptable.setDate(vCurrentDate);
        vEcritureComptable.setLibelle("Sandwichs");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                "Club saumon", new BigDecimal(10),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4456),
                "TVA 20%", new BigDecimal(2),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                "Facture S110001", null,
                new BigDecimal(12)));

        dao.updateEcritureComptable(vEcritureComptable);
    }


    // ==================== EcritureComptable - DELETE ====================

    @Test
    void deleteEcritureComptable() {
        dao.deleteEcritureComptable(1);
    }


    // ==================== SequenceEcritureComptable ====================

    @Test
    void getSequenceByCodeAndAnneeCourante() throws NotFoundException {
        SequenceEcritureComptable vRechercheSequence = new SequenceEcritureComptable();
        vRechercheSequence.setJournalCode("OD");
        vRechercheSequence.setAnnee(2016);
        SequenceEcritureComptable vExistingSequence = dao.getSequenceByCodeAndAnneeCourante(vRechercheSequence);

        if (vExistingSequence != null) {
            assertEquals("OD", vExistingSequence.getJournalCode());
            assertEquals(2016, vExistingSequence.getAnnee().intValue());
            assertEquals(88, vExistingSequence.getDerniereValeur().intValue());
        } else fail("Incorrect result size: expected 1, actual 0");

    }

    @Test
    void upsertSequenceEcritureComptable() {
        SequenceEcritureComptable vSequenceEcritureComptable = new SequenceEcritureComptable();
        vSequenceEcritureComptable.setJournalCode("VE");
        vSequenceEcritureComptable.setAnnee(1963);
        vSequenceEcritureComptable.setDerniereValeur(42);

        dao.upsertSequenceEcritureComptable(vSequenceEcritureComptable);
    }
}