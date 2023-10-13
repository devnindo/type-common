package io.devnindo.datatype.beanexample;

import io.devnindo.datatype.schema.AField;
import io.devnindo.datatype.schema.DataBean;
import io.devnindo.datatype.schema.Required;

import java.util.List;

public class APerson implements DataBean {
    @Required
    Long id;

    @Required
    Integer age;
    Gender gender;
    List<Address> addressList;
    APerson employer;


    public Long getId() {
        return id;
    }


    public Integer getAge() {
        return age;
    }

    public APerson setAge(Integer age$) {
        age = age$;
        return this;
    }

    public APerson getEmployer() {
        return employer;
    }

    public Gender getGender() {
        return gender;
    }

    public List<Address> getAddressList() {
        return addressList;
    }
}
