package com.aaomidi.dev.skypebot.engine.commands;

import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.skype.ChatMessage;
import com.skype.SkypeException;

import java.util.HashMap;

/**
 * Created by Amir on 7/4/2014.
 */
public class Chat extends SkypeCommand {
    private HashMap<String, ChatterBotSession> bots;
    private ChatterBot chatterBot;

    public Chat(String name, int requiredArgs, String usage) {
        super(name, requiredArgs, usage);
        try {
            bots = new HashMap<>();
            ChatterBotFactory factory = new ChatterBotFactory();
            chatterBot = factory.create(ChatterBotType.CLEVERBOT);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void execute(ChatMessage chatMessage, String[] args) throws SkypeException {
        try {
            String senderID = chatMessage.getSenderId();
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("reset")) {
                    if (bots.containsKey(senderID)) {
                        bots.remove(senderID);
                        chatMessage.getChat().send("We reset your skype bot.");
                        return;
                    }
                }
            }
            ChatterBotSession session;
            if (bots.containsKey(senderID)) {
                session = bots.get(senderID);
            } else {
                session = chatterBot.createSession();
                bots.put(senderID, session);
            }
            boolean isFirst = true;
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                if (!isFirst) {
                    sb.append(arg);
                    sb.append(" ");
                }
                isFirst = false;
            }
            String message = "Cleverbot: " + session.think(sb.toString());
            chatMessage.getChat().send(message);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
