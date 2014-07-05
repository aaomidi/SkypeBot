package com.aaomidi.dev.skypebot.engine.commands;

import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.skype.ChatMessage;
import com.skype.SkypeException;

/**
 * Created by aaomidi on 7/5/2014.
 */
public class Leet extends SkypeCommand {
    public Leet(String name, int requiredArgs, String usage) {
        super(name, requiredArgs, usage);
    }

    @Override
    public void execute(ChatMessage chatMessage, String[] args) throws SkypeException {


    }
}
