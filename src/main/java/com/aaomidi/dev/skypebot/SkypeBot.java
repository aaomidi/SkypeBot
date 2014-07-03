package com.aaomidi.dev.skypebot;


import com.aaomidi.dev.skypebot.engine.CommandsManager;
import com.skype.Skype;
import lombok.Getter;

public class SkypeBot {
    private final SkypeBot skypeBot;
    @Getter
    private CommandsManager commandsManager;

    public SkypeBot() {
        skypeBot = this;
        Skype.setDaemon(false);
        commandsManager = new CommandsManager(this);
    }

    public static void main(String[] args) {
        SkypeBot skypeBot = new SkypeBot();
    }


}
