package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class CompteComptableTest {

    private static CompteComptable vCompte = new CompteComptable();
    private static List<CompteComptable> vList = new ArrayList<>(0);

    @Test
    public void getByNumero() {
        assertNull(CompteComptable.getByNumero(vList, 501));
        vCompte.setNumero(401);
        vCompte.setLibelle("Fournisseurs");
        vList = new ArrayList<>(0);
        vList.add(vCompte);
        vList.add(new CompteComptable(411, "Clients"));
        assertEquals(CompteComptable.getByNumero(vList, 401), vCompte);
        assertNull(CompteComptable.getByNumero(vList, 501));
    }

    @Test
    public void GettersTest() {
        vCompte.setLibelle("pLibelle");
        assertTrue(vCompte.getLibelle().equals("pLibelle"));

        vCompte.setNumero(12);
        assertTrue(vCompte.getNumero().equals(12));
    }
}
