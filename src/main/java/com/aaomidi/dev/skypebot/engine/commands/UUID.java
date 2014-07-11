package com.aaomidi.dev.skypebot.engine.commands;

import com.aaomidi.dev.skypebot.engine.modules.SkypeCommand;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class UUID extends SkypeCommand {

    public UUID(String name, int requiredArgs, String usage) {
        super(name, requiredArgs, usage);
    }

    @Override
    public void execute(final ChatMessage chatMessage, final String[] args) throws SkypeException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONParser jsonParser = new JSONParser();
                    String link = String.format("https://minespy.net/api/uuid/%s", args[1]);
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response;
                    if ((response = reader.readLine()) != null) {
                        Object obj = jsonParser.parse(response);
                        JSONObject jsonArray = (JSONObject) obj;
                        String message = "";
                        if (args.length <= 16) {
                            message = String.format("%s's UUID is: %s", args[1], jsonArray.get("uuid"));
                        } else {
                            message = String.format("The username of that UUID is: %s", jsonArray.get("name"));
                        }
                        chatMessage.getChat().send(message);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }).start();
    }
}
