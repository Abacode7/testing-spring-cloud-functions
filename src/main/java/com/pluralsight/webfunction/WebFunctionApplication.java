package com.pluralsight.webfunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class WebFunctionApplication {
	List<TollStation> tollStations;

	public WebFunctionApplication() {
		tollStations = new ArrayList<>();
		tollStations.add(new TollStation("111", 124.1f, 2));
		tollStations.add(new TollStation("112", 1364.0f, 4));
		tollStations.add(new TollStation("113", 117.9f, 3));
	}

	@Bean
	public Function<String, TollStation> retrieveStation(){
		return value -> {
			System.out.println("Received request for station - " + value);
			return tollStations.stream()
			.filter(station -> value.equals(station.getStationId()))
			.findAny()
			.orElse(null);
			
		};
	}

	@Bean
	public Consumer<TollRecord> processTollRecord(){
		return value -> {
			System.out.println("received toll record with license plate number - " + value.getLicensePlate());
		};
	}

	@Bean
	public Function<TollRecord, Mono<Void>> processTollRecordReactive(){
		return value -> {
			System.out.println("received toll record with license plate number - " + value.getLicensePlate());
			return Mono.empty();
		};
	}

	@Bean
	public Consumer<Flux<TollRecord>> processListOfTollRecordsReactive(){
		return value -> {
			value.subscribe(tollRecord -> System.out.println("received toll record with license - " + tollRecord.getLicensePlate()));
		};
	}

	@Bean
	public Supplier<Flux<TollStation>> getTollStations(){
		return () -> {
			return Flux.fromIterable(tollStations);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(WebFunctionApplication.class, args);
	}
}
