package com.zeneo.shop.controller;

import com.zeneo.shop.model.FilePath;
import com.zeneo.shop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class UploadController {

    @Autowired
    private FileService fileService;

    @GetMapping(value= "/{filename:.+}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Mono<ResponseEntity<?>> getOneImage(@PathVariable String filename){
        return fileService.findOneImage(filename)
                .map(resource->{
                    try {
                        return ResponseEntity.ok()
                                .contentLength(resource.contentLength())
                                .body(new InputStreamResource(resource.getInputStream()));
                    } catch (IOException e) {
                        return ResponseEntity
                                .badRequest()
                                .body("Could not find "+filename+" ==>"+e.getMessage());
                    }
                });
    }

    @PostMapping("upload")
    public Flux<FilePath> createImage(@RequestPart(name="file") Flux<FilePart> files){
        return fileService.createImage(files).map(FilePath::new);
    }

    @DeleteMapping("{filename:.+}")
    public Mono<Void> deleteImage(@PathVariable String filename) {
        return fileService.deleteImage(filename);
    }

}
