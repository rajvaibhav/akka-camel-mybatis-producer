package com.example.akka.producer;

import akka.camel.javaapi.UntypedProducerActor;

public class GenericProducerActor extends UntypedProducerActor {

	private String uri;

	@Override
	public String getEndpointUri() {
		return this.uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	

	public GenericProducerActor() {
		super();
	}

	public GenericProducerActor(String uri) {
		this();
		this.uri = uri;
	}
}
