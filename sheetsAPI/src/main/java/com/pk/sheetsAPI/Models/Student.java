package com.pk.sheetsAPI.Models;

import com.pk.sheetsAPI.Data.Status;
import com.pk.sheetsAPI.Data.Subject;
import com.pk.sheetsAPI.Data.Teacher;

import java.util.Date;
import java.util.List;

public class Student {
    private String name;
    private Integer grade;
    private Integer age;
    private List<Subject> subjects;
    private String traffic;
    private Teacher teacher;
    private Double price;
    private Integer occurrence;
    private Status status;
    private Date dateStarted;

    public Student(String name, Integer grade, Integer age, List<Subject> subjects,
                   String traffic, Teacher teacher, Double price, Integer occurrence, Status status, Date dateStarted) {
        this.name = name;
        this.grade = grade;
        this.age = age;
        this.subjects = subjects;
        this.traffic = traffic;
        this.teacher = teacher;
        this.price = price;
        this.occurrence = occurrence;
        this.status = status;
        this.dateStarted = dateStarted;
    }

    public String getName() {
        return name;
    }

    public Integer getGrade() {
        return grade;
    }

    public Integer getAge() {
        return age;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public String getTraffic() {
        return traffic;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getOccurrence() {
        return occurrence;
    }

    public Status getStatus() {
        return status;
    }

    public Date getDateStarted() {
        return dateStarted;
    }
}
