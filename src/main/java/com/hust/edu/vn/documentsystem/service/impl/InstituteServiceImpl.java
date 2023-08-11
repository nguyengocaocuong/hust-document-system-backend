package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.data.model.InstituteModel;
import com.hust.edu.vn.documentsystem.entity.Institute;
import com.hust.edu.vn.documentsystem.repository.InstituteRepository;
import com.hust.edu.vn.documentsystem.service.InstituteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstituteServiceImpl implements InstituteService {
    private final InstituteRepository instituteRepository;

    public InstituteServiceImpl(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }

    @Override
    public Institute createInstitute(InstituteModel instituteModel) {
        boolean exists = instituteRepository.existsByInstitute(instituteModel.getInstitute());
        if (exists)
            return null;
        Institute institute = new Institute();
        institute.setInstitute(instituteModel.getInstitute());
        institute.setPhoneNumber(instituteModel.getPhoneNumber());
        institute.setOffice(instituteModel.getOffice());
        institute.setWebsite(instituteModel.getWebsite());
        return instituteRepository.save(institute);
    }

    @Override
    public List<Institute> getAllInstitutes() {
        return instituteRepository.findAll();
    }
}
