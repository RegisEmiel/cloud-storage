package com.geekbrains.messages;

public class NextFilePartToClientMessages extends FilePartMessage{
    private int part;
    private long startByte;
    private int lastPartSize;
    private int total;
    private String fileName;

    public NextFilePartToClientMessages(String fileName, int total, int currentPart, long startByte, int lastPart) {
        super(fileName, total, currentPart, startByte, lastPart);
        this.fileName = fileName;
        this.total = total;
        this.part = currentPart;
        this.startByte = startByte;
        this.lastPartSize = lastPart;
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.NEXT_PART_TO_CLIENT;
    }
}
