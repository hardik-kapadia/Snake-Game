package org.example.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.RL.SnakeState;
import org.example.RL.SnakeStateWithPolicy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JsonMapper {

    ObjectMapper oj;
    public JsonMapper() {
        this.oj = new ObjectMapper();
    }

    public void export(Map<SnakeState,Character> policy, String filename) throws IOException {

        Set<SnakeStateWithPolicy> set = new HashSet<>();

        for(Map.Entry<SnakeState,Character> entry: policy.entrySet())
            set.add(SnakeStateWithPolicy.fromStateAndAction(entry.getKey(),entry.getValue()));

        File f = new File(filename);

        if(f.exists())
            f.delete();

        f.createNewFile();

        oj.writeValue(new FileWriter(f),set);

    }

    public Map<SnakeState,Character> readPolicy(String filename) throws IOException {

        File f = new File(filename);

        Map<SnakeState,Character> policy = new HashMap<>();

        List<SnakeStateWithPolicy> set = oj.readerForListOf(SnakeStateWithPolicy.class).readValue(f);

        for(SnakeStateWithPolicy ssp: set)
            policy.put(ssp.state(),ssp.action());

        return policy;
    }


}
