
package com.dummy.myerp.model.bean.comptabilite;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class SequenceEcritureComptableTest {

    SequenceEcritureComptable vSequence = new SequenceEcritureComptable();

    @Test
    public void GettersTest() {
        vSequence.setJournalCode("BQ");
        Assert.assertTrue(vSequence.getJournalCode().equals("BQ"));

        vSequence.setAnnee(2018);
        Assert.assertTrue(vSequence.getAnnee().equals(2018));

        vSequence.setDerniereValeur(1);
        Assert.assertTrue(vSequence.getDerniereValeur().equals(1));

        SequenceEcritureComptable vSequence2 = new SequenceEcritureComptable("BD",2018,1);
        Assert.assertTrue(vSequence2.getJournalCode().equals("BD"));
        Assert.assertTrue(vSequence2.getAnnee().equals(2018));
        Assert.assertTrue(vSequence2.getDerniereValeur().equals(1));

        vSequence.toString();
    }
}