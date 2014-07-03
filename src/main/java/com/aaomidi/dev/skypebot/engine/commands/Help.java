package com.aaomidi.dev.skypebot.engine.commands;


import com.aaomidi.dev.skypebot.engine.CommandsManager;
import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.skype.ChatMessage;
import com.skype.SkypeException;

import java.util.HashMap;

public class Help extends SkypeCommand {
    public Help(String name, int requiredArgs, String usage) {
        super(name, requiredArgs, usage);
    }

    @Override
    public void execute(ChatMessage chatMessage, String[] args) throws SkypeException {
        HashMap<String, SkypeCommand> commands = CommandsManager.getCommandMap();
        StringBuilder sb = new StringBuilder();
        sb.append("Possible commands are: ");
        for (SkypeCommand command : commands.values()) {
            sb.append(command.getName());
            sb.append(", ");
        }
        chatMessage.getChat().send(sb.toString());
    }
}
