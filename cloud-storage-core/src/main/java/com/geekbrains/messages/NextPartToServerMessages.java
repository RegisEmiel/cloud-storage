package com.geekbrains.messages;

public class NextPartToServerMessages extends FilePartMessage {
    public NextPartToServerMessages(String fileName, int total, int currentPart, long startByte, int lastPart) {
        super(fileName, total, currentPart, startByte, lastPart);
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.NEXT_PART_TO_SERVER;
    }
}
