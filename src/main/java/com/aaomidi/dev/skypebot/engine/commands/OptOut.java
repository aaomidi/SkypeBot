package com.aaomidi.dev.skypebot.engine.commands;

import com.aaomidi.dev.skypebot.engine.CommandsManager;
import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.skype.ChatMessage;
import com.skype.SkypeException;

/**
 * @author aaomidi
 */
public class OptOut extends SkypeCommand {
    public OptOut(String name, int requiredArgs, String usage) {
        super(name, requiredArgs, usage);
    }

    @Override
    public void execute(final ChatMessage chatMessage, final String[] args) throws SkypeException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String senderID = chatMessage.getSenderId();
                    if (CommandsManager.getOptOutSet().contains(senderID)) {
                        CommandsManager.getOptOutSet().remove(senderID);
                        chatMessage.getChat().send(chatMessage.getSenderDisplayName() + " was removed from the optout list.");
                    } else {
                        CommandsManager.getOptOutSet().add(senderID);
                        chatMessage.getChat().send(chatMessage.getSenderDisplayName() + " was added to the optout list.");

                    }
                } catch (SkypeException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }
}
