
package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;

import static org.junit.Assert.*;

public class SequenceEcritureComptableTest {

    SequenceEcritureComptable vSequence = new SequenceEcritureComptable();

    @Test
    public void GettersTest() {
        vSequence.setJournalCode("BQ");
        assertTrue(vSequence.getJournalCode().equals("BQ"));

        vSequence.setAnnee(2018);
        assertTrue(vSequence.getAnnee().equals(2018));

        vSequence.setDerniereValeur(1);
        assertTrue(vSequence.getDerniereValeur().equals(1));

        SequenceEcritureComptable vSequence2 = new SequenceEcritureComptable("BD",2018,1);
        assertTrue(vSequence2.getJournalCode().equals("BD"));
        assertTrue(vSequence2.getAnnee().equals(2018));
        assertTrue(vSequence2.getDerniereValeur().equals(1));

        vSequence.toString();
    }
}