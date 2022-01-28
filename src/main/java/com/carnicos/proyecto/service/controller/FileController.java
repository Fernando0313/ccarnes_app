package com.carnicos.proyecto.service.controller;

import com.carnicos.proyecto.service.entity.File;
import com.carnicos.proyecto.service.service.CloudinaryService;
import com.carnicos.proyecto.service.service.FileService;
import com.carnicos.proyecto.service.util.GenericResponse;
import com.carnicos.proyecto.service.dto.Mensaje;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/file")
@CrossOrigin(origins = "*")
public class FileController {
	
    private FileService service;

    @Autowired
    CloudinaryService imgService;
    
    public FileController(FileService service) {
        this.service = service;
    }

    //listar file
    @GetMapping
    public GenericResponse list() {
        return service.list();
    }


    //buscar file por id
    @GetMapping("/{id}")
    public GenericResponse find(@PathVariable Long id) {
        return null;
    }


    //descargar el file
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileName, HttpServletRequest request) {
        return service.downloadByFileName(fileName, request);
    }

    //guardar la file
   /* @PostMapping
    public GenericResponse save(@ModelAttribute File obj) {
        return service.save(obj);
    }*/
    
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@ModelAttribute File obj)throws IOException {
        
    	MultipartFile multipartFile = obj.getFile();
    	System.out.println(obj.getNombre());
    	BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if(bi == null){
            return new ResponseEntity(new Mensaje("imagen no válida"), HttpStatus.BAD_REQUEST);
        }
        Map result = imgService.upload(multipartFile);
        File archivoFile = new File();
        
        archivoFile.setUrlFile(result.get("url").toString());
        archivoFile.setNombre(obj.getNombre());
        archivoFile.setIdurl(result.get("public_id").toString());
        archivoFile.setFileName(result.get("original_filename").toString());
        service.save(archivoFile);
        System.out.println(obj.getUrlFile());
        System.out.println(result.get("url").toString());
        System.out.println(result.get("original_filename").toString());
        /*Imagen imagen =
                new Imagen((String)result.get("original_filename"),
                        (String)result.get("url"),
                        (String)result.get("public_id"));
        imagenService.save(imagen);*/
        return new ResponseEntity(new Mensaje("imagen subida"), HttpStatus.OK);
    }
    
    
    
    
    

    public GenericResponse update(Long aLong, File obj) {
        return null;
    }
    
    public GenericResponse delete(Long aLong) {
        return null;
    }
}
