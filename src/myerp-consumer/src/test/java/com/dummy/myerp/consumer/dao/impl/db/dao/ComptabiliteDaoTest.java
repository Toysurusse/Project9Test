package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testconsumer.consumer.ConsumerTestCase;

import org.junit.Test;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

public class ComptabiliteDaoTest extends ConsumerTestCase{

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static ResultSethelperImp RS = ResultSethelperImp.getInstance();
    private static ComptabiliteDaoImpl dao = ComptabiliteDaoImpl.getInstance();
    private static EcritureComptable vEcritureComptable = new EcritureComptable();
    private static Date vCurrentDate = new Date();
    private static Integer vCurrentYear = LocalDateTime.ofInstant(vCurrentDate.toInstant(), ZoneId.systemDefault()).toLocalDate().getYear();


    // ==================== CompteComptable - GET ====================

    @Test
    public void getListCompteComptable() {
        List<CompteComptable> vList = dao.getListCompteComptable();
        assertEquals(7, vList.size());
    }


    // ==================== JournalComptable - GET ====================

    @Test
    public void getListJournalComptable() {
        List<JournalComptable> vList = dao.getListJournalComptable();
        assertEquals(4, vList.size());
    }


    // ==================== EcritureComptable - GET ====================

   @Test
    public void getListEcritureComptable() {
        List<EcritureComptable> vList = dao.getListEcritureComptable();
        assertEquals(4, vList.size());
    }

    @Test
    public void getEcritureComptable() throws NotFoundException {
        EcritureComptable vEcritureComptable = dao.getEcritureComptable(-3);
        assertEquals("BQ-2016/00003", vEcritureComptable.getReference());

        String exception = "EcritureComptable non trouvée : id=10";
        try {
            dao.getEcritureComptable(10);
        }
        catch (NotFoundException e) {
            Assert.assertEquals(exception, e.getMessage());
        }
    }

    @Test
    public void getEcritureComptableByRef() throws NotFoundException {
        EcritureComptable vEcritureComptable = dao.getEcritureComptableByRef("BQ-2016/00003");
        assertEquals("BQ", vEcritureComptable.getJournal().getCode());
        String vEcritureYear = new SimpleDateFormat("yyyy").format(vEcritureComptable.getDate());
        assertEquals("2016", vEcritureYear);
        assertEquals(-3, vEcritureComptable.getId().intValue());

        String exception = "EcritureComptable non trouvée : reference=BQ-2020/00003";
        try {
            dao.getEcritureComptableByRef("BQ-2020/00003");
            }
        catch (NotFoundException e) {
            assertEquals(exception, e.getMessage());
            }
    }

    @Test
    public void loadListLigneEcriture() {
        vEcritureComptable.setId(-5);
        dao.loadListLigneEcriture(vEcritureComptable);
        assertEquals(2, vEcritureComptable.getListLigneEcriture().size());
    }


    // ==================== EcritureComptable - INSERT ====================

    @Test
    public void insertEcritureComptable() throws FunctionalException {
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
    public void updateEcritureComptable() throws FunctionalException {
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
    public  void deleteEcritureComptable() {
        dao.deleteEcritureComptable(-2);
    }


    // ==================== SequenceEcritureComptable ====================

    @Test
    public void getSequenceByAnneeCourante() throws NotFoundException {
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

    // ==================== Test Resultset Helper ====================

    @Test
    public void getTestResultTest() throws SQLException {
        getDaoProxy();
        RS.IntegerTest("numero");
        RS.LongTest("numero");
        //String exception ="Le nom de colonne test n'a pas été trouvé dans ce ResultSet.";
        String exception ="The column name test was not found in this ResultSet.";
        try {
            assertNull(RS.IntegerTest("test"));
            fail();
        }
        catch (SQLException e) {
            Assert.assertEquals(exception, e.getMessage());
        }

        try {
            assertNull(RS.LongTest("test"));
            fail();
        }
        catch (SQLException e) {
            Assert.assertEquals(exception, e.getMessage());
        }
    }
}
