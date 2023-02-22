package com.microservices.currencyexchangeservice.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.currencyexchangeservice.model.ExchangeValue;

public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Integer>{

	ExchangeValue findByFromAndTo(String from, String to);

}
