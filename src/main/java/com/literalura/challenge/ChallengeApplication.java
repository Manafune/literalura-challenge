package com.literalura.challenge;

import com.literalura.challenge.Repository.AutorRepository;
import com.literalura.challenge.Repository.LibroRepository;
import com.literalura.challenge.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeApplication implements CommandLineRunner {

	@Autowired
	private Principal principal;
	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraElMenu();
	}
}
