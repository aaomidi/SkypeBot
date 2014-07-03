package com.aaomidi.dev.skypebot.engine;

import com.aaomidi.dev.skypebot.SkypeBot;
import com.aaomidi.dev.skypebot.engine.commands.Help;
import com.aaomidi.dev.skypebot.engine.commands.MCPing;
import com.aaomidi.dev.skypebot.engine.commands.Source;
import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;
import lombok.Getter;

import java.util.HashMap;


public class CommandsManager {
    private final SkypeBot instance;
    @Getter
    private static HashMap<String, SkypeCommand> commandMap = new HashMap<>();
    private static HashMap<String, Long> spamFilter = new HashMap<>();

    public CommandsManager(SkypeBot instance) {
        this.instance = instance;
        this.registerCommands();
    }

    public void registerCommand(SkypeCommand command) {
        commandMap.put(command.getName(), command);
    }

    public void registerCommands() {
        this.onCommand();
        Help help = new Help("help", 0, "The command format is: !help");
        MCPing mcPing = new MCPing("mcping", 1, "The command format is: !mcping [serverIP]");
        Source source = new Source("source", 0, "The command format is: !source");
        this.registerCommand(help);
        this.registerCommand(mcPing);
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

    public static void runCommand(ChatMessage chatMessage) throws SkypeException {
        String message = chatMessage.getContent();
        String[] messageArray = message.split(" ");
        String sentCommand = messageArray[0].replace("!", "").toLowerCase();
        String sender = chatMessage.getSender().getId();
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
}