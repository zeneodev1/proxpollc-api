package com.zeneo.shop.service;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    private final static String UPLOAD_ROOT = "upload-dir";

    final private ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> findOneImage(String filename){
        return Mono.fromSupplier(()->
                resourceLoader.getResource("file:"+ UPLOAD_ROOT +"/"+filename+"/raw"))
                .log("findOneImage");
    }

    public Mono<Void> createImage(Flux<FilePart> files) {
        return files.flatMap(file -> Mono.just(Paths.get(UPLOAD_ROOT, file.filename()).toFile())
                .log("createImage-pickTarget")
                .map(destfile -> {
                    try {
                        destfile.createNewFile();
                        return destfile;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).log("createImage-path")
                .flatMap(file::transferTo)
                .log("createImage-flatMap")).then()
                .log("createImage-done");
    }

    public Mono<Void> deleteImage(String filename) {
        Mono<Void> deletFile = Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return deletFile;
    }

}
