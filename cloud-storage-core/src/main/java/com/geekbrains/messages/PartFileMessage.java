package com.geekbrains.messages;

public class PartFileMessage implements Message{
    private String fileName;
    private byte[] data;
    private int part;
    private long startByte;
    private int lastPartSize;
    private int allParts;

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public int getPart() {
        return part;
    }

    public long getStartByte() {
        return startByte;
    }

    public int getLastPartSize() {
        return lastPartSize;
    }

    public int getAllParts() {
        return allParts;
    }

    public PartFileMessage(String fileName, int allParts, int currentPart, long startByte, int lastPart) {
        this.fileName = fileName;
        this.allParts = allParts;
        this.part = currentPart;
        this.startByte = startByte;
        this.lastPartSize = lastPart;
    }

    public PartFileMessage(String fileName, int allParts, int currentPart, long startByte, int lastPart, byte[] data) {
        this.part = currentPart;
        this.startByte = startByte;
        this.lastPartSize = lastPart;
        this.allParts = allParts;
        this.fileName = fileName;
        this.data = data;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.ACCEPT_FILE_PART;
    }
}
