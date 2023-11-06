package org.example;

import org.example.RL.Policy;
import org.example.RL.SnakeAgent;
import org.example.RL.SnakeState;
import org.example.Utils.JsonMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SnakeGame {
    public static void main(String[] args) throws IOException {

        Map<String, String> argMaps = new HashMap<>();
        argMaps.put("type", "play");
        argMaps.put("file", "policy.json");
        argMaps.put("n", "10000");

        for (int i = 0; i < args.length; i += 2) {
            argMaps.put(args[i].substring(1), args[i + 1]);
        }

        if (argMaps.get("type").equals("train")) {

            SnakeAgent agent = new SnakeAgent();
            agent.runAll(Integer.parseInt(argMaps.get("n")));
            JsonMapper jm = new JsonMapper();
            jm.export(agent.learnPolicy(), argMaps.get("file"));

        } else if (argMaps.get("type").equals("agent")) {

            JsonMapper jm = new JsonMapper();
            Map<SnakeState, Character> policyMap = jm.readPolicy(argMaps.get("file"));

            Policy p = new Policy(policyMap);

            AgentGameFrame agf = new AgentGameFrame(p);
            agf.play();
            agf.start();
        } else {

            GameFrame gf = new GameFrame();
            gf.play();
            gf.start();
        }
    }
}
