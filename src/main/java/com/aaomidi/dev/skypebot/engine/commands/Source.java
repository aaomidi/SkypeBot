package com.aaomidi.dev.skypebot.engine.commands;

import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.skype.ChatMessage;
import com.skype.SkypeException;

/**
 * @author aaomidi
 */
public class Source extends SkypeCommand {

    public Source(String name, int requiredArgs, String usage) {
        super(name, requiredArgs, usage);
    }

    @Override
    public void execute(ChatMessage chatMessage, String[] args) throws SkypeException {
        String message = "My source is located at: https://github.com/aaomidi/SkypeBot";
        chatMessage.getChat().send(message);
    }
}
