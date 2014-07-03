package com.aaomidi.dev.skypebot.engine.commands;

import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.aaomidi.dev.skypebot.utils.URIBuilder;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MCPing extends SkypeCommand {


    public MCPing(String name, int requiredArgs, String usage) {
        super(name, requiredArgs, usage);
    }

    @Override
    public void execute(final ChatMessage chatMessage, final String[] args) throws SkypeException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONParser jsonParser = new JSONParser();
                    Map<String, String> queryMap = new HashMap<>();
                    queryMap.put("server", args[1]);
                    String link = URIBuilder.buildURI("http://minecircuit.com/v1/get/", queryMap);
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response;
                    if ((response = reader.readLine()) != null) {
                        Object obj = jsonParser.parse(response);
                        JSONObject jsonArray = (JSONObject) obj;
                        if (jsonArray.get("error") != null) {
                            chatMessage.getChat().send("Sorry, that server seems to be offline.");
                            return;
                        } else {
                            JSONObject secObj = (JSONObject) jsonArray.get("players");
                            String message = String.format("Server %s\nPlayers: %s/%s\nMOTD: %s", args[1], String.valueOf(secObj.get("online")), String.valueOf(secObj.get("max")), jsonArray.get("motd"));
                            chatMessage.getChat().send(message);
                            return;
                        }
                    }
                    chatMessage.getChat().send("Sorry, that server seems to be offline.");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }
}
