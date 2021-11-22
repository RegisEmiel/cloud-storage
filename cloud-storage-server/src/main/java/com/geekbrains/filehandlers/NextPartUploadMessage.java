package com.geekbrains.filehandlers;

public class NextPartUploadMessage extends FilePartMessage {
    public NextPartUploadMessage(String fileName, int allParts, int currentPart, long startByte, int lastPart) {
        super(fileName, allParts, currentPart, startByte, lastPart);
    }

    @Override
    public TypeMessage getType() {
        return TypeMessage.NEXT_PART_UPLOAD;
    }
}
