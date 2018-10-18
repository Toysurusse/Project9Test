package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;
import java.util.Date;

import com.dummy.myerp.technical.exception.FunctionalException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;


import static org.junit.Assert.*;



public class EcritureComptableTest {

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) throws FunctionalException {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();

        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);

        return vRetour;
    }

    @Test
    public void isEquilibree() throws FunctionalException {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        assertTrue(vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "-10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "-1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        assertFalse(vEcriture.isEquilibree());

        vEcriture.setId(1);
        vEcriture.setReference("BQ-2018/00001");
        vEcriture.setJournal(new JournalComptable());
        vEcriture.setDate(new Date());

        vEcriture.getId();
        vEcriture.getReference();
        vEcriture.getLibelle();
        vEcriture.getDate();
        vEcriture.getJournal();

        LigneEcritureComptable testLigne = new LigneEcritureComptable();
        testLigne.setCompteComptable(new CompteComptable());
        testLigne.setCredit(new BigDecimal("0.12"));
        testLigne.setDebit(new BigDecimal("0.12"));
        testLigne.setLibelle("Test");

        testLigne.getDebit();
        testLigne.getCredit();
        testLigne.getCompteComptable();
        testLigne.getLibelle();

        vEcriture.toString();
        testLigne.toString();

        String exception = LigneEcritureComptable.RG7_EXCEPTION;

        // Control number of Decimal for débit
        try {
            vEcriture.getListLigneEcriture().add(this.createLigne(1, "-10.003", null));
            fail(exception);
        }
        catch (FunctionalException e) {
            assertEquals(exception, e.getMessage());
        }

        // Control number of Decimal for credit
        try {
            vEcriture.getListLigneEcriture().add(this.createLigne(1, null, "-10.003"));
            fail(exception);
        }
        catch (FunctionalException e) {
            assertEquals(exception, e.getMessage());
        }

    }

}
