package com.geekbrains.messages;

public class DeleteMessage implements Message{
    private String fileName;

    public DeleteMessage(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.DELETE_FILE;
    }
}
