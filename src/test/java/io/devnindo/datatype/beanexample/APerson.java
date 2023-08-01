package io.devnindo.datatype.beanexample;

import io.devnindo.datatype.schema.AField;
import io.devnindo.datatype.schema.DataBean;

import java.util.List;

public class APerson implements DataBean {
    Long id;
    Gender gender;
    List<Address> addressList;
    Integer age;
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
