package com.example.akka.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSystem;
import akka.camel.Camel;
import akka.camel.CamelExtension;

import com.example.akka.producer.entity.Person;
import com.example.akka.producer.proxy.PersonProxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-test.xml" })
public class PersonProxyTest {

	@Autowired
	@Qualifier("personProxy")
	private PersonProxy personProxy;

	@Autowired
	private ActorSystem actorSystem;

	@Test
	public void getPersonList() {
		Future<List<Person>> personList = personProxy.getAllPersons();
		List<Person> result = null;
		Camel camel = CamelExtension.get(actorSystem);
		CamelContext camelContext = camel.context();
		try {
			result = (ArrayList) Await.result(personList,
					Duration.create(30, TimeUnit.SECONDS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(result);
		Assert.assertFalse(result.size() == 0);
	}

	/*
	 * @Test public void filterPersonList() { List<Person> personList =
	 * personProxy.filterPersonList("rajvaibhav.raj@gmail.com");
	 * Assert.assertNotNull(personList); Assert.assertFalse(personList.size() ==
	 * 0); }
	 */

}
