package com.dummy.myerp.technical.exception;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FunctionalExceptionTest {

    private void tryTest (Object object) throws NotFoundException{
        if(object==null){
            throw new NotFoundException(
                    "Unable to get object");
        }
    }
    private void tryFunctional (Object object) throws FunctionalException{
        if(object==null){
            throw new FunctionalException(
                    "Unable to get object");
        }
    }
    private void tryTechnical (Object object) throws TechnicalException{
        if(object==null){
            throw new TechnicalException(
                    "Unable to get object");
        }
    }


    @Test(expected = NotFoundException.class)
    public void testReadFile() throws NotFoundException, FunctionalException, TechnicalException {
        Object object=null;
        tryTest(object);
        tryFunctional(object);
        tryTechnical(object);
    }









}





