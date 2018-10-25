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
    private void tryTestVide (Object object) throws NotFoundException{
        if(object==null){
            throw new NotFoundException();
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
    private void tryFunctionalCause (Object object) throws FunctionalException{
        if(object==null){
            throw new FunctionalException(new Throwable());
        }
    }
    private void tryFunctionalMessage (Object object) throws FunctionalException{
        if(object==null){
            throw new FunctionalException(
                    "Unable to get object", new Throwable());
        }
    }

    private void tryTechnical (Object object) throws TechnicalException{
        if(object==null){
            throw new TechnicalException(
                    "Unable to get object");
        }
    }
    private void tryTechnicalC (Object object) throws TechnicalException{
        if(object==null){
            throw new TechnicalException(new Throwable());
        }
    }
    private void tryTechnicalM (Object object) throws TechnicalException{
        if(object==null){
            throw new TechnicalException(
                    "Unable to get object", new Throwable());
        }
    }



    @Test(expected = NotFoundException.class)
    public void testNotFound() throws NotFoundException {
        Object object=null;
        tryTest(object);
    }
    @Test(expected = NotFoundException.class)
    public void testNotFoundVide() throws NotFoundException {
        Object object=null;
        tryTestVide(object);
    }
    @Test(expected = NotFoundException.class)
    public void testNotFoundC() throws NotFoundException {
        Object object=null;
        tryTestCause(object);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundCM() throws NotFoundException {
        Object object=null;
        tryTestCauseMessage(object);
    }

    @Test(expected = FunctionalException.class)
    public void testFunctional() throws FunctionalException {
        Object object=null;
        tryFunctional(object);
    }
    @Test(expected = FunctionalException.class)
    public void testFunctionalC() throws FunctionalException {
        Object object=null;
        tryFunctionalCause(object);
    }
    @Test(expected = FunctionalException.class)
    public void testFunctionalCM() throws FunctionalException {
        Object object=null;
        tryFunctionalMessage(object);
    }

    @Test(expected = TechnicalException.class)
    public void testTechnical() throws TechnicalException {
        Object object=null;
        tryTechnical(object);
    }
    @Test(expected = TechnicalException.class)
    public void testTechnicalC() throws TechnicalException {
        Object object=null;
        tryTechnicalC(object);
    }
    @Test(expected = TechnicalException.class)
    public void testTechnicalM() throws TechnicalException {
        Object object=null;
        tryTechnicalM(object);
    }






}





