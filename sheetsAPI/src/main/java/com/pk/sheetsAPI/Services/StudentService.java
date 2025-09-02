package com.pk.sheetsAPI.Services;

import com.pk.sheetsAPI.Data.Status;
import com.pk.sheetsAPI.Data.Subject;
import com.pk.sheetsAPI.Data.Teacher;
import com.pk.sheetsAPI.Models.Student;
import com.pk.sheetsAPI.Utils.ApplicationUtils;
import com.pk.sheetsAPI.Utils.ApplicationUtils.*;
import com.pk.sheetsAPI.Utils.GoogleSheetsUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pk.sheetsAPI.Utils.ApplicationUtils.parseDoubleOrNull;
import static com.pk.sheetsAPI.Utils.ApplicationUtils.parseIntOrNull;

@Service
public class StudentService {
    private final GoogleSheetsUtils _googleSheetsUtils;

    public StudentService(GoogleSheetsUtils googleSheetClient){
        this._googleSheetsUtils = googleSheetClient;
    }

    public List<Student> getStudents() throws Exception {
        List<List<Object>> values = _googleSheetsUtils.readRange("A2:J");
        if (values == null) return List.of();

        return values.stream()
                .map(row -> {
                    try {
                        return ApplicationUtils.fromArray(row);
                    } catch (Exception e) {
                        throw new RuntimeException("Error mapping row: " + row, e);
                    }
                })
                .collect(Collectors.toList());
    }



    public Student getStudentByName(String name) throws Exception {
        List<List<Object>> rows = _googleSheetsUtils.readRange("A2:J");
        if (rows == null) rows = List.of();

        return rows.stream()
                .filter(row -> !row.isEmpty() && row.get(0) != null)
                .filter(row -> row.get(0).toString().trim().equalsIgnoreCase(name.trim()))
                .findFirst()
                .map(row -> {
                    try {
                        return ApplicationUtils.fromArray(row);
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing student row: " + row, e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Student not found: " + name));
    }



    public String addStudent(@RequestBody Student newStudent) throws Exception {
        // make a new row
        List<Object> newStudentRow = ApplicationUtils.toArray(newStudent);
        // make a list of lists
        List<List<Object>> values = Collections.singletonList(newStudentRow);
        _googleSheetsUtils.writeRange("Sheet1!A:J", values);
        return "Student " + newStudent.getName() + " added successfully!";
    }


    public void updateStudent(@RequestBody Student updatedStudent) throws Exception {
        List<List<Object>> allRows = _googleSheetsUtils.readRange("A2:J");

        // 1. select
        List<Student> students = ApplicationUtils.select(allRows);

        // 2. find student by name
        int index = ApplicationUtils.find(allRows, updatedStudent.getName());
        if (index == -1) throw new Exception("Student not found");

        // 3. update student in memory
        allRows = ApplicationUtils.update(allRows, index, updatedStudent);

        // 4. write back to sheets
        String range = "A" + (index + 2) + ":J" + (index + 2);
        _googleSheetsUtils.updateRange(range, List.of(allRows.get(index)));


    }
}
