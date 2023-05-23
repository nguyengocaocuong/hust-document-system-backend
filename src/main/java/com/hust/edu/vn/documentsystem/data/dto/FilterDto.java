package com.hust.edu.vn.documentsystem.data.dto;

import java.io.Serializable;
import java.util.List;

public class FilterDto implements Serializable {
    private List<SubjectDto> subjectFilter;
    private List<TeacherDto> teacherFilter;
}
