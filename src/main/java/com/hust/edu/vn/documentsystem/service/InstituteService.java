package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.InstituteModel;
import com.hust.edu.vn.documentsystem.entity.Institute;

import java.util.List;

public interface InstituteService {
    Institute createInstitute(InstituteModel instituteModel);

    List<Institute> getAllInstitutes();
}
