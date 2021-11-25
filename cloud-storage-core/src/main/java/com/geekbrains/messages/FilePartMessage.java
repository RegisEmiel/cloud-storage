package com.geekbrains.messages;

public class FilePartMessage implements Message{
    private String fileName;
    private byte[] data;
    private int part;
    private long startByte;
    private int lastPartSize;
    private int total;

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

    public int getTotal() {
        return total;
    }

    public FilePartMessage(String fileName, int total, int currentPart, long startByte, int lastPart) {
        this.fileName = fileName;
        this.total = total;
        this.part = currentPart;
        this.startByte = startByte;
        this.lastPartSize = lastPart;
    }

    public FilePartMessage(String fileName, int total, int currentPart, long startByte, int lastPart, byte[] data) {
        this.part = currentPart;
        this.startByte = startByte;
        this.lastPartSize = lastPart;
        this.total = total;
        this.fileName = fileName;
        this.data = data;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.ACCEPT_FILE_PART;
    }
}
