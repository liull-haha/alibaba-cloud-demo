package com.example.demoelasticsearch.dto;

import lombok.Data;

/**
 * @Author liull
 * @Date 2024/4/4 19:06
 **/
@Data
public class PersonInfoDTO {

    private Integer age;

    private String email;

    private String info;

    private Name name;


    @Data
    public static class Name{

        private String firstName;

        private String secondName;

        @Override
        public String toString() {
            return "Name{" +
                    "firstName='" + firstName + '\'' +
                    ", secondName='" + secondName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PersonInfoDTO{" +
                "age=" + age +
                ", email='" + email + '\'' +
                ", info='" + info + '\'' +
                ", name=" + name +
                '}';
    }
}
