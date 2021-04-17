package com.maiia.pro.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ProAbstractMapper{
	
	@Autowired
	private ModelMapper modelMapper;
	
	<S, T> T map(S source, Class<T> targetClass) {
		return modelMapper.map(source, targetClass);
	}

	<S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
	    return source
	      .stream()
	      .map(element -> modelMapper.map(element, targetClass))
	      .collect(Collectors.toList());
	}
}
