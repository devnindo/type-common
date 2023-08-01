package io.devnindo.datatype;

import io.devnindo.datatype.beanexample.$APerson;
import io.devnindo.datatype.beanexample.Gender;
import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.validation.Violation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/***
 *  violations are meant to be sent to frontend contract
 * */
public class ViolationTest {
    @Test
    public void no_ctx_violation() {
        // violation of a constraint
        Violation violation = Violation.of("A_CONSTRAINT");
        Assertions.assertEquals(Boolean.TRUE, violation.getSingleCtx() == null);
    }

    @Test
    public void single_flat_ctx_violation() {
        // violation of a constraint
        Violation violation = Violation.withCtx("ELIGIBLE_AGE", 24);
        Assertions.assertEquals(24, violation.getSingleCtx());
    }

    @Test
    public void var_ctx_violation() {
        // violation of a constraint
        JsonObject js = new JsonObject()
                .put($APerson.GENDER, Gender.female)
                .put($APerson.AGE, 45);
        Violation violation = Violation.withCtx("PENSION_ELIGIBLE", js);
        Assertions.assertEquals(Boolean.FALSE, violation.hasSingleCtx());
        Assertions.assertEquals(Boolean.TRUE, violation.hasVarCtx());

        JsonObject ctxData = violation.getVarCtx();
        Assertions.assertTrue(ctxData.equals(js), "var-ctx should be equal clone of js");
        Assertions.assertEquals(Boolean.FALSE, ctxData == js);


    }

}
