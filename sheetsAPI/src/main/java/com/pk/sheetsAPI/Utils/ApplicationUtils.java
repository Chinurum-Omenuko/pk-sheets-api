package com.pk.sheetsAPI.Utils;

import com.pk.sheetsAPI.Data.Status;
import com.pk.sheetsAPI.Data.Subject;
import com.pk.sheetsAPI.Data.Teacher;
import com.pk.sheetsAPI.Models.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ApplicationUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static List<Object> toArray(Student student) {
        List<Object> row = new ArrayList<>();
        row.add(student.getName());
        row.add(student.getGrade());
        row.add(student.getAge());

        // Convert subjects list back into comma-separated string
        String subjects = student.getSubjects() != null
                ? student.getSubjects().stream()
                .map(Enum::name)             // FRENCH, MATH, CODING
                .map(s -> s.replace("_", " ")) // "MATH_ENGLISH" → "MATH ENGLISH"
                .collect(Collectors.joining(", "))
                : "";
        row.add(subjects);

        row.add(student.getTraffic());
        row.add(student.getTeacher() != null ? student.getTeacher().name() : "");
        row.add(student.getPrice());
        row.add(student.getOccurrence());
        row.add(student.getStatus() != null ? student.getStatus().name() : "");
        row.add(student.getDateStarted() != null ? sdf.format(student.getDateStarted()) : "");
        return row;
    }

    public static Status parseStatus(String value) {
        if (value == null || value.isBlank()) return null;

        switch (value.trim().toLowerCase()) {
            case "active":
                return Status.ACTIVE;
            case "not active":
                return Status.NOT_ACTIVE;
            case "pending":
                return Status.PENDING;
            default:
                return Status.REASON; // fallback
        }
    }





    public static Student fromArray(List<Object> row) throws ParseException {
        if (row == null || row.isEmpty()) {
            return null;
        }

        return new Student(
                row.get(0).toString(),                // name
                parseIntOrNull(row.size() > 1 ? row.get(1) : null),           // grade
                parseIntOrNull(row.size() > 2 ? row.get(2) : null),           // age
                row.size() > 3 && !row.get(3).toString().isBlank()
                        ? Stream.of(row.get(3).toString().split(","))
                        .map(String::trim)
                        .map(s -> s.trim().toUpperCase().replace(" ", "_"))
                        .map(Subject::valueOf)
                        .collect(Collectors.toList())
                        : List.of(),                                         // subjects
                row.size() > 4 ? row.get(4).toString() : null,               // traffic
                row.size() > 5 && !row.get(5).toString().isBlank()
                        ? Teacher.valueOf(row.get(5).toString())
                        : null,
                parseDoubleOrNull(row.size() > 6 ? row.get(6) : null),       // price
                parseIntOrNull(row.size() > 7 ? row.get(7) : null),          // occurrence
                row.size() > 8 && !row.get(8).toString().isBlank()
                        ? ApplicationUtils.parseStatus(row.get(8).toString())
                        : null,

                row.size() > 9 && !row.get(9).toString().isBlank()
                        ? sdf.parse(row.get(9).toString())
                        : null
        );
    }

    public static Integer parseIntOrNull(Object value) {
        if (value == null) return null;
        String str = value.toString().trim();
        return str.isEmpty() ? null : Integer.parseInt(str);
    }

    public static Double parseDoubleOrNull(Object value) {
        if (value == null) return null;
        String str = value.toString().trim();
        return str.isEmpty() ? null : Double.parseDouble(str);
    }

    public static List<Student> select(List<List<Object>> rows) throws ParseException {
        if (rows == null) return List.of();
        List<Student> students = new ArrayList<>();
        for (List<Object> row : rows) {
            Student s = fromArray(row);
            if (s != null) students.add(s);
        }
        return students;
    }

    /**
     * Find: locate a student by name
     */
    public static int find(List<List<Object>> rows, String name) {
        if (rows == null || name == null) return -1;
        for (int i = 0; i < rows.size(); i++) {
            List<Object> row = rows.get(i);
            if (!row.isEmpty() && row.get(0).toString().equalsIgnoreCase(name)) {
                return i; // index in rows
            }
        }
        return -1; // not found
    }

    /**
     * Update: replace a row with new student data
     */
    public static List<List<Object>> update(List<List<Object>> rows, int index, Student updatedStudent) {
        if (rows == null) rows = new ArrayList<>();
        if (index < 0 || index >= rows.size()) {
            throw new IllegalArgumentException("Invalid index for update");
        }
        rows.set(index, toArray(updatedStudent));
        return rows;
    }




}
