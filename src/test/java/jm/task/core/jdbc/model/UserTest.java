package jm.task.core.jdbc.model;

import junit.framework.TestCase;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class UserTest extends TestCase {

    @Test
    public void hashCodeEqualsContractTest(){
        EqualsVerifier.simple().forClass(User.class).verify();
    }
}