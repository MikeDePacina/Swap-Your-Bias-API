package com.swapyourbias.Swap.Your.Bias.API;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SwapYourBiasApiApplication {

	//injecting 3rd party model mapper as a bean
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(SwapYourBiasApiApplication.class, args);
	}

}
