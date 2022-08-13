package com.example.demo.registration;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.criteria.Expression;

import javax.persistence.criteria.Selection;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String>{

	@Override
	public boolean test(String t) {
		// TODO 
		return true;
	}

	

}
