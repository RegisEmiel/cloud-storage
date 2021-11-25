package com.geekbrains.messages;

import java.io.Serializable;

public interface Message extends Serializable {
    public TypeMessage getType();
}
