package com.example.akka.producer.proxy;

import java.util.List;

import scala.concurrent.Future;

import com.example.akka.producer.entity.Person;

public interface PersonProxy {

	public Future<List<Person>> getAllPersons();

}
