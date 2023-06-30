package com.hust.edu.vn.documentsystem.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class ModelMapperUtils {
    private final ModelMapper modelMapperShallow;

    
    public ModelMapperUtils(@Qualifier("modelMapperShallow") ModelMapper modelMapperShallow) {
        this.modelMapperShallow = modelMapperShallow;
    }
    public <S, D> D mapAllProperties(S source, Type destination) {
        return modelMapperShallow.map(source, destination);
    }

    public <S, D> D mapSelectedProperties(S source, Type destination, PropertyMap<S, D> propertyMap) {
        modelMapperShallow.addMappings(propertyMap);
        return modelMapperShallow.map(source, destination);
    }

}
