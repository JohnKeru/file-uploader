package com.fileupload.ws;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
public class FileController {

	@Autowired
	private FileStorageService fileStorageService;

	@PutMapping
	public ResponseEntity<FileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/" + fileName)
				.toUriString();

		FileResponse fileResponse = new FileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
		return ResponseEntity.ok(fileResponse);
	}

	@GetMapping("/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request)
			throws IOException {
		Resource resource = fileStorageService.loadFileAsResource(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine fileType");
		}

		if (contentType == null) 
			contentType = "application/octet-stream";
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}

}