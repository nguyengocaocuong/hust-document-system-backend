package com.hust.edu.vn.documentsystem.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hust.edu.vn.documentsystem.entity.FavoriteSubject;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.FavoriteSubjectRepository;
import com.hust.edu.vn.documentsystem.repository.SubjectRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.FavoriteSubjectService;

@Service
public class FavoriteSubjectServiceImpl implements FavoriteSubjectService {
    private final FavoriteSubjectRepository favoriteSubjectRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public FavoriteSubjectServiceImpl(FavoriteSubjectRepository favoriteSubjectRepository,
                                      SubjectRepository subjectRepository, UserRepository userRepository) {
        this.favoriteSubjectRepository = favoriteSubjectRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean favoriteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null)
            return false;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        FavoriteSubject favoriteSubject = favoriteSubjectRepository.findByUserAndSubject(user, subject);
        if (favoriteSubject != null) {
            favoriteSubjectRepository.delete(favoriteSubject);
            return true;
        }
        favoriteSubject = new FavoriteSubject();
        favoriteSubject.setSubject(subject);
        favoriteSubject.setUser(user);
        favoriteSubjectRepository.save(favoriteSubject);
        return true;
    }

    @Override
    public List<FavoriteSubject> getAllFavoriteForSubject(Long subjectId) {
        return favoriteSubjectRepository.findAllBySubjectId(subjectId);
    }

}
