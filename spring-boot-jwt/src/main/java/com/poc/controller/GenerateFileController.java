package com.poc.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin()
public class GenerateFileController {

	@Value("${file.path}")
	private String filePath;

	@Value("${file.name}")
	private String fileName;

	@PostMapping(value = "/generatefile")
	public void generateFile(@RequestBody String payload) throws IOException {

		Path path = Paths.get(filePath);

		if (!Files.exists(path)) {
			Files.createDirectory(path);

		}
		Files.write(Paths.get(path.toString() + fileName), payload.getBytes());
	}

}
