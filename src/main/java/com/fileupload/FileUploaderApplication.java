package com.fileupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.fileupload.ws.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class FileUploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploaderApplication.class, args);
		System.out.println("X");
	}

}