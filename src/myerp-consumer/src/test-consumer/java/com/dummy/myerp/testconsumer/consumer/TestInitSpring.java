package com.dummy.myerp.testconsumer.consumer;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Classe de test de l'initialisation du contexte Spring
 */
class TestInitSpring extends ConsumerTestCase {

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
        assertNotNull(SpringRegistry.getDaoProxy());
    }
}
