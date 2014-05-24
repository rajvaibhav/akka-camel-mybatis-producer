package com.example.akka.producer.mapper;

import java.util.List;

import com.example.akka.producer.entity.Person;

public interface PersonMapper {
	public List<Person> selectPersons();
	public List<Person> getPerson(String email);

}
