package com.poly.util;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ParamService {
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	ServletContext app;
	
	public String getString(String name, String defaultValue) {
		String nameGetRequest = request.getParameter(name);
		
		if (nameGetRequest != null) {
			return nameGetRequest;
		}
		
		return defaultValue;
	}
	
	public Integer getInt(String name, Integer defaultValue) {
		String nameGetRequest = request.getParameter(name);
		
		if (nameGetRequest != null) {
			return Integer.parseInt(nameGetRequest);
		}
		
		return defaultValue;
	}
	
	public Boolean getBoolean(String name, Boolean defaultValue) {
		String nameGetRequest = request.getParameter(name);
		
		if (nameGetRequest != null) {
			return Boolean.parseBoolean(nameGetRequest);
		}
		
		return defaultValue;
	}
	
	public Date getDate(String name, String pattern) {
		if (request.getParameter(name) != null && pattern != null) {
			try {
				// xử lý giá trị tham số
				return null;
			} catch(RuntimeException ex) {
				ex.printStackTrace();
			}			
		}
		
		return null;
	}
	
	public File save(MultipartFile file, String path) {
		File dir = new File(app.getRealPath(path));
		if (!dir.exists()) dir.mkdir();
		
		try {
			File saveFile = new File(dir, file.getOriginalFilename());
			file.transferTo(saveFile);
			return saveFile;
		} catch(Exception ex) {
			throw new RuntimeException();
		}
	}
}
