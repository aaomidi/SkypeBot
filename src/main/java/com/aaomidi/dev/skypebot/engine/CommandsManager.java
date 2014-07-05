package com.aaomidi.dev.skypebot.engine;

import com.aaomidi.dev.skypebot.SkypeBot;
import com.aaomidi.dev.skypebot.engine.commands.*;
import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class CommandsManager {
    @Getter
    private static HashMap<String, SkypeCommand> commandMap = new HashMap<>();
    @Getter
    private static Set<String> optOutSet = new HashSet<>();
    private static HashMap<String, Long> spamFilter = new HashMap<>();
    private final SkypeBot instance;

    public CommandsManager(SkypeBot instance) {
        this.instance = instance;
        this.registerCommands();
    }

    public static void runCommand(ChatMessage chatMessage) throws SkypeException {
        if (chatMessage.getContent().equals("(┛ಠ_ಠ)┛彡┻━┻")) {
            chatMessage.getChat().send("┬─┬ノ( º _ ºノ) Please respect tables!");
            return;
        }
        String message = chatMessage.getContent();
        String[] messageArray = message.split(" ");
        String sentCommand = messageArray[0].replace("!", "").toLowerCase();
        String sender = chatMessage.getSender().getId();
        if (optOutSet.contains(sender)) {
            return;
        }
        if (spamFilter.containsKey(sender)) {
            if (System.currentTimeMillis() - spamFilter.get(sender) < 5000) {
                return;
            }
        }
        if (!message.startsWith("!")) {
            return;
        }
        if (commandMap.containsKey(sentCommand)) {
            SkypeCommand command = commandMap.get(sentCommand);
            if (messageArray.length >= command.getRequiredArgs() + 1) {
                command.execute(chatMessage, messageArray);
            } else {
                chatMessage.getChat().send(command.getUsage());
            }
            spamFilter.put(sender, System.currentTimeMillis());
        }
    }

    public void registerCommand(SkypeCommand command) {
        commandMap.put(command.getName(), command);
    }

    public void registerCommands() {
        this.onCommand();
        Chat chat = new Chat("chat", 1, "The command format is: !chat [message]");
        Help help = new Help("help", 0, "The command format is: !help");
        MCPing mcPing = new MCPing("mcping", 1, "The command format is: !mcping [serverIP]");
        OptOut optOut = new OptOut("optout", 0, "The command format is: !optout");
        Source source = new Source("source", 0, "The command format is: !source");
        this.registerCommand(chat);
        this.registerCommand(help);
        this.registerCommand(mcPing);
        this.registerCommand(optOut);
        this.registerCommand(source);
    }

    public void onCommand() {
        try {
            Skype.addChatMessageListener(new ChatMessageListener() {
                @Override
                public void chatMessageReceived(ChatMessage chatMessage) throws SkypeException {
                    CommandsManager.runCommand(chatMessage);

                }

                @Override
                public void chatMessageSent(ChatMessage chatMessage) throws SkypeException {
                    CommandsManager.runCommand(chatMessage);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}