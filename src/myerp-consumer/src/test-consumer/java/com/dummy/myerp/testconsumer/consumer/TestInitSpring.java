package com.dummy.myerp.testconsumer.consumer;

import org.junit.Assert;
import org.junit.Test;


/**
 * Classe de test de l'initialisation du contexte Spring
 */
public class TestInitSpring extends ConsumerTestCase {

    /**
     * Constructeur.
     */
    TestInitSpring() {
        super();
    }


    /**
     * Teste l'initialisation du contexte Spring
     */
    @Test
    void testInit() {
        SpringRegistry.init();
        Assert.assertNotNull(SpringRegistry.getDaoProxy());
    }
}
