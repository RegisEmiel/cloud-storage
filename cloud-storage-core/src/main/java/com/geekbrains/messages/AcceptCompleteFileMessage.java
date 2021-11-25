package com.geekbrains.messages;

public class AcceptCompleteFileMessage implements Message{
    private String fileName;
    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public AcceptCompleteFileMessage(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.ACCEPT_COMPLETE_FILE;
    }
}
