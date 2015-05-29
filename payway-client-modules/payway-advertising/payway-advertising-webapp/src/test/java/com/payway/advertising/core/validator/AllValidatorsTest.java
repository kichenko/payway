/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.validator;

import com.payway.advertising.model.DbAbstractEntity;
import com.payway.advertising.model.DbAgentFile;
import com.payway.advertising.model.DbAgentFileOwner;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbFileType;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * AllValidatorsTest
 *
 * @author Sergey Kichenko
 * @created 29.05.15 00:00
 */
public class AllValidatorsTest {

    private void validateString(Validator validator, String[] values, boolean result) {
        for (String value : values) {
            assertEquals(validator.validate(value), result);
        }
    }

    private void validateDbAbstractEntity(Validator validator, DbAbstractEntity[] values, boolean result) {
        for (DbAbstractEntity value : values) {
            assertEquals(validator.validate(value), result);
        }
    }

    @Test
    public void testAgentFileValidatorAllOk() {
        DbAgentFile values[] = {new DbAgentFile("", DbFileType.Logo, new DbAgentFileOwner(), "", "", false, new DbConfiguration(), 0)};
        validateDbAbstractEntity(new AgentFileValidator(), values, true);
    }

    @Test
    public void testAgentFileValidatorAllBad() {
        DbAgentFile values[] = {new DbAgentFile()};
        validateDbAbstractEntity(new AgentFileValidator(), values, false);
    }

    @Test
    public void testFileNameValidatorAllOk() {
        String values[] = {"1.txt"};
        validateString(new FileNameValidator(), values, true);
    }

    @Test
    public void testFileNameValidatorBadAllBad() {
        String values[] = {""};
        validateString(new FileNameValidator(), values, false);
    }

    @Test
    public void testUserNameValidatorAllOk() {
        String values[] = {"sergey"};
        validateString(new UserNameValidator(), values, true);
    }

    @Test
    public void testUserNameValidatorAllBad() {
        String values[] = {""};
        validateString(new UserNameValidator(), values, false);
    }

    @Test
    public void testUserPasswordValidatorAllOk() {
        String values[] = {"qwerty"};
        validateString(new UserPasswordValidator(), values, true);
    }

    @Test
    public void testUserPasswordValidatorAllBad() {
        String values[] = {""};
        validateString(new UserPasswordValidator(), values, false);
    }
}
