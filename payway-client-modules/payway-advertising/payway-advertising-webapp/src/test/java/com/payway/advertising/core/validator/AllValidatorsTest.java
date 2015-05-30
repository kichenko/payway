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

    private final Validator agentFileExpressionValidator;
    private final Validator agentFileValidator;
    private final Validator fileNameValidator;
    private final Validator userNameValidator;
    private final Validator userPasswordValidator;

    public AllValidatorsTest() {
        fileNameValidator = new FileNameValidator();
        userNameValidator = new UserNameValidator();
        userPasswordValidator = new UserPasswordValidator();
        agentFileExpressionValidator = new AgentFileExpressionValidator();
        agentFileValidator = new AgentFileValidator(agentFileExpressionValidator);
    }

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
    public void testAgentFileExpressionValidatorAllOk() {
        String values[] = {""};
        validateString(agentFileExpressionValidator, values, true);
    }

    @Test
    public void testAgentFileExpressionValidatorAllBad() {
        String values[] = {null};
        validateString(agentFileExpressionValidator, values, false);
    }

    @Test
    public void testAgentFileValidatorAllOk() {
        DbAgentFile values[] = {new DbAgentFile("1/2/file.txt", DbFileType.Logo, new DbAgentFileOwner(), "", "digest", false, new DbConfiguration(), 0)};
        validateDbAbstractEntity(agentFileValidator, values, true);
    }

    @Test
    public void testAgentFileValidatorAllBad() {
        DbAgentFile values[] = {new DbAgentFile()};
        validateDbAbstractEntity(agentFileValidator, values, false);
    }

    @Test
    public void testFileNameValidatorAllOk() {
        String values[] = {"file-name.txt", "hello.pdf",};
        validateString(fileNameValidator, values, true);
    }

    @Test
    public void testFileNameValidatorBadAllBad() {
        String values[] = {"", "", "$file.pdf", "^{}*.exe", "   \\/*{}&,;'\"()|:<>?#$@!%"};
        validateString(fileNameValidator, values, false);
    }

    @Test
    public void testUserNameValidatorAllOk() {
        String values[] = {"sergey", "ivan-k", "marius"};
        validateString(userNameValidator, values, true);
    }

    @Test
    public void testUserNameValidatorAllBad() {
        String values[] = {"", "sergey kichenko", "$sergey", "  \\/*{}&,;'\"()|:<>?#$@!%"};
        validateString(userNameValidator, values, false);
    }

    @Test
    public void testUserPasswordValidatorAllOk() {
        String values[] = {"qwerty"};
        validateString(userPasswordValidator, values, true);
    }

    @Test
    public void testUserPasswordValidatorAllBad() {
        String values[] = {""};
        validateString(userPasswordValidator, values, false);
    }
}
