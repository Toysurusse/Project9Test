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

    private void tryTestCause (Object object) throws NotFoundException{
        if(object==null){
            throw new NotFoundException(new Throwable());
        }
    }

    private void tryTestCauseMessage (Object object) throws NotFoundException{
        if(object==null){
            throw new NotFoundException("Unable to get object", new Throwable());
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
    public void testNotFound() throws NotFoundException {
        Object object=null;
        tryTest(object);
        tryTestCause(object);
        tryTestCauseMessage(object);
    }

    @Test(expected = FunctionalException.class)
    public void testFunctional() throws FunctionalException {
        Object object=null;
        tryFunctional(object);
    }

    @Test(expected = TechnicalException.class)
    public void testTechnical() throws TechnicalException {
        Object object=null;
        tryTechnical(object);
    }






}





