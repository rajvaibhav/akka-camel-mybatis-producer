package com.example.akka.producer.proxy.impl;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mybatis.MyBatisComponent;
import org.springframework.beans.factory.annotation.Autowired;

import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.Camel;
import akka.camel.CamelExtension;
import akka.camel.CamelMessage;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.example.akka.producer.GenericProducerActor;
import com.example.akka.producer.entity.Person;
import com.example.akka.producer.proxy.PersonProxy;

public class PersonProxyImpl implements PersonProxy {

	@Autowired
	private ActorSystem actorSystem;
	@Autowired
	private String personProducerActorName;
	@Autowired
	private Long personProducerTimeout;
	@Autowired
	private MyBatisComponent mybatis;

	private ActorRef personProducerActor;

	private Timeout timeout;

	private CamelContext camelContext;

	public Future<List<Person>> getAllPersons() {

		final Future<Object> futureResp = Patterns.ask(personProducerActor, new Person(null, null, null, "someone@abcd.com"),
				timeout);

		return futureResp.flatMap(new Mapper<Object, Future<List<Person>>>() {
			public Future<List<Person>> apply(final Object obj) {

				Future<List<Person>> result = Futures.future(
						new Callable<List<Person>>() {

							@Override
							public List<Person> call() throws Exception {
								final CamelMessage message = (CamelMessage) obj;
								final List<Person> persons = message.getBodyAs(
										List.class, camelContext);
								return persons;
							}
						}, actorSystem.dispatcher());
				return result;
			}
		}, actorSystem.dispatcher());
	}

	public List<Person> filterPersonList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		Camel camel = CamelExtension.get(actorSystem);
		camelContext = camel.context();
		camelContext.addComponent("mybatis", mybatis);
		/*Props props = Props.create(GenericProducerActor.class,
				"mybatis:selectPersons?statementType=SelectList");*/
		Props props = Props.create(GenericProducerActor.class,
				"mybatis:getPerson?statementType=SelectList");
		personProducerActor = actorSystem.actorOf(props,
				personProducerActorName);
		timeout = new Timeout(Duration.create(personProducerTimeout, SECONDS));
	}

	public ActorSystem getActorSystem() {
		return actorSystem;
	}

	public void setActorSystem(ActorSystem actorSystem) {
		this.actorSystem = actorSystem;
	}

	public ActorRef getPersonProducerActor() {
		return personProducerActor;
	}

	public void setPersonProducerActor(ActorRef personProducerActor) {
		this.personProducerActor = personProducerActor;
	}

	public MyBatisComponent getMybatis() {
		return mybatis;
	}

	public void setMybatis(MyBatisComponent mybatis) {
		this.mybatis = mybatis;
	}
}
