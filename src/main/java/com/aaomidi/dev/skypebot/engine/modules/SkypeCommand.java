package com.aaomidi.dev.skypebot.engine.modules;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class SkypeCommand {
    @Getter
    private final String name;
    @Getter
    private final int requiredArgs;
    @Getter
    private String usage;

    public abstract void execute(ChatMessage chatMessage, String[] args) throws SkypeException;
}
