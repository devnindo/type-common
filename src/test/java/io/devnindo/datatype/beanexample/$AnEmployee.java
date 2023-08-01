package io.devnindo.datatype.beanexample;

import io.devnindo.datatype.json.JsonObject;
import io.devnindo.datatype.schema.BeanSchema;
import io.devnindo.datatype.schema.DataDiff;
import io.devnindo.datatype.schema.SchemaField;
import io.devnindo.datatype.util.Either;
import io.devnindo.datatype.validation.ObjViolation;
import io.devnindo.datatype.validation.Violation;

import java.util.List;

public class $AnEmployee extends BeanSchema<AnEmployee> {
    public static final SchemaField<AnEmployee, Boolean> RETIRE = plainField("retire", AnEmployee::shouldRetire, Boolean.class, false);

    public static final SchemaField<AnEmployee, Gender> GENDER = enumField("gender", AnEmployee::getGender, Gender.class, false);

    public static final SchemaField<AnEmployee, List<Address>> ADDRESS_LIST = beanListField("address_list", AnEmployee::getAddressList, Address.class, false);

    public static final SchemaField<AnEmployee, APerson> MANAGER = beanField("manager", AnEmployee::getManager, APerson.class, false);

    public static final SchemaField<AnEmployee, APerson> EMPLOYER = beanField("employer", AnEmployee::getEmployer, APerson.class, false);

    public static final SchemaField<AnEmployee, Long> ID = plainField("id", AnEmployee::getId, Long.class, true);

    public static final SchemaField<AnEmployee, Integer> AGE = plainField("age", AnEmployee::getAge, Integer.class, true);

    public static final SchemaField<AnEmployee, Integer> SALARY = plainField("salary", AnEmployee::getSalary, Integer.class, false);

    @Override
    public Either<Violation, AnEmployee> apply(JsonObject data) {
        Either<Violation, Gender> genderEither = GENDER.fromJson(data);
        Either<Violation, List<Address>> addressListEither = ADDRESS_LIST.fromJson(data);
        Either<Violation, APerson> managerEither = MANAGER.fromJson(data);
        Either<Violation, APerson> employerEither = EMPLOYER.fromJson(data);
        Either<Violation, Long> idEither = ID.fromJson(data);
        Either<Violation, Integer> ageEither = AGE.fromJson(data);
        Either<Violation, Integer> salaryEither = SALARY.fromJson(data);

        ObjViolation violation = newViolation(AnEmployee.class);
        violation.check(GENDER, genderEither);
        violation.check(ADDRESS_LIST, addressListEither);
        violation.check(MANAGER, managerEither);
        violation.check(EMPLOYER, employerEither);
        violation.check(ID, idEither);
        violation.check(AGE, ageEither);
        violation.check(SALARY, salaryEither);

        if (violation.hasRequirement()) {
            return Either.left(violation);
        }
        AnEmployee bean = new AnEmployee();
        bean.gender = genderEither.right();
        bean.addressList = addressListEither.right();
        bean.manager = managerEither.right();
        bean.employer = employerEither.right();
        bean.id = idEither.right();
        bean.setAge(ageEither.right());
        bean.setSalary(salaryEither.right());
        return Either.right(bean);
    }

    @Override
    public JsonObject apply(AnEmployee bean) {
        JsonObject js = new JsonObject();
        js.put(RETIRE.name, RETIRE.toJson(bean));
        js.put(GENDER.name, GENDER.toJson(bean));
        js.put(ADDRESS_LIST.name, ADDRESS_LIST.toJson(bean));
        js.put(MANAGER.name, MANAGER.toJson(bean));
        js.put(EMPLOYER.name, EMPLOYER.toJson(bean));
        js.put(ID.name, ID.toJson(bean));
        js.put(AGE.name, AGE.toJson(bean));
        js.put(SALARY.name, SALARY.toJson(bean));
        return js;
    }

    @Override
    public DataDiff<AnEmployee> diff(AnEmployee left, AnEmployee right) {
        AnEmployee merged = new AnEmployee();
        JsonObject delta = new JsonObject();

        merged.gender = GENDER.diff(left, right, delta::put);
        merged.addressList = ADDRESS_LIST.diff(left, right, delta::put);
        merged.manager = MANAGER.diff(left, right, delta::put);
        merged.employer = EMPLOYER.diff(left, right, delta::put);
        merged.id = ID.diff(left, right, delta::put);
        merged.age = AGE.diff(left, right, delta::put);
        merged.salary = SALARY.diff(left, right, delta::put);

        return new DataDiff<>(delta, merged);
    }
}
