package com.geekbrains.filehandlers;

public class SendingFileMessage implements Message{
    private String fileName;
    private byte[] data;

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public SendingFileMessage(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.SENDING_FILE;
    }
}
