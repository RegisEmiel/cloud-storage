package com.geekbrains.filehandlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileDescriptions {
    private final Path path;

    private long size;

    public String getFilename() {
        return path.getFileName().toString();
    }

    public long getSize() {
        return size;
    }

    public boolean isDirectory() {
        return Files.isDirectory(path);
    }

    public FileDescriptions(Path path) {
        this.path = path;

        try {
            size = Files.size(path);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
