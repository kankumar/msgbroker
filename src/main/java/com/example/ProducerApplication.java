package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


interface MessageChannels{

	@Output
	MessageChannel output();
}

@EnableBinding(MessageChannels.class)		// spring cloud stream expects interface binding to the channel
@RestController
@SpringBootApplication
public class ProducerApplication {

	private final MessageChannels channels;

	@Autowired
	public ProducerApplication( MessageChannels channel) {
		this.channels = channel;
	}


	@GetMapping(value = "/event/{name}")
	void event(@PathVariable String name){
		Message<String> message = MessageBuilder.withPayload(name).build();
		this.channels.output().send(message);

	}

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}
}
