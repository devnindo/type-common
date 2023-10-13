package io.devnindo.datatype.beanexample;

import io.devnindo.datatype.schema.AField;
import io.devnindo.datatype.schema.DataBean;

import java.util.List;

public class Address implements DataBean  {
    String city;

    List<String> roadList;

    public String getCity() {
        return city;
    }

    public List<String> getRoadList() {
        return roadList;
    }
}
