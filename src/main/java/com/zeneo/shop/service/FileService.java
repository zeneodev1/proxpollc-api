package com.zeneo.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    private final static String UPLOAD_ROOT = "files";

    final private ResourceLoader resourceLoader;

    public FileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(UPLOAD_ROOT));
        }
        catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public Mono<Resource> findOneImage(String filename){
        return Mono.fromSupplier(()->
                resourceLoader.getResource("file:"+ UPLOAD_ROOT +"/"+filename))
                .log("findOneImage");
    }

    public Flux<String> createImage(Flux<FilePart> files) {
        return files.flatMap(file -> Mono.just(Paths.get(UPLOAD_ROOT, UUID.randomUUID().toString().replaceAll("-", "") + ".png").toFile())
                .log("createImage-pickTarget")
                .map(destfile -> {
                    try {
                        destfile.createNewFile();
                        return destfile;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).log("createImage-path")
                .map(f -> {
                    log.info(f.getName());
                    file.transferTo(f).log("transfer-to").subscribe();
                    return f.getName();
                }));
    }

    public Mono<Void> deleteImage(String filename) {
        return Mono.fromRunnable(() -> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
