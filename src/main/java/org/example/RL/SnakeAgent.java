package org.example.RL;

import org.example.GameFrame;
import org.example.GamePanel;
import org.example.Utils.JsonMapper;

import java.util.*;

public class SnakeAgent {

    private Map<SnakeState, Map<Character,Double>> q;

    private double randomNess;

    private Random random;

    private double discount;

    private double learningRate;

    int[] scoreTracker;

    public GameFrame gf;

    public JsonMapper mapper;

    public SnakeAgent() {
        this.q= new HashMap<>();
        this.random = new Random();
        this.randomNess = 0.25;
        this.discount = 0.97;
        this.learningRate = 0.8;
        this.gf = new GameFrame(0);
        this.mapper = new JsonMapper();
    }

    public void runAll(int n) {

        this.scoreTracker = new int[100];
        int score = -1;

        for(int i=0;i<n;i++) {
            int temp = run(gf);

            if(temp > score)
                score = temp;

            if(i % (n/100) == 0){
                scoreTracker[i/(n/100)] = score;
                System.out.println((i/(n/100)) + ". Max score after "+i+" -> "+score);
            }
        }

        System.out.println("Max score: "+score);

    }

    public void runAll() {
        runAll(10);
    }

    public int run(GameFrame gf) {

        gf.play();
        GamePanel gp = gf.gp;

        SnakeState state = gp.startManualGame(100);

//        System.out.println("Starting another game");
        int latestScore = 0;

        while(true) {

            List<Character> actions = state.possibleActions();

            if(actions.isEmpty())
                break;

            // Get ideal Action
            char idealAction = getIdealAction(state);

            // Choose a randomAction
            char randomAction = actions.get(random.nextInt(actions.size()));

            // deciding whether to go with ideal action or random action
            double ideal = random.nextDouble();
            char action = ideal < randomNess ? randomAction : idealAction;

            // Act
            SnakeState nState = gp.act(action);

            // R(s')
            double nReward = nState.reward();

            // Q(s,a)
            double currentQ = getQValue(state,action);

            double sample = nReward + discount * getMaxQValue(nState);

            // Q(s,a) <-- Q(s,a) + learning_rate * (sample - Q(s,a))
            double newQ = currentQ + learningRate * (sample - currentQ);

            setQValue(state,action,newQ);

            latestScore = nState.applesEaten;

            if(!nState.running)
                break;

            state = nState;
        }
//        System.out.println("Play through finished with score: "+latestScore);
        return latestScore;

    }

    public void setQValue(SnakeState state, char action, double qv){

        if(!q.containsKey(state))
            q.put(state,new HashMap<>());

        q.get(state).put(action,qv);

        System.out.println("Setting q-Value for "+state+" and "+action+" to "+qv);

    }

    public double getQValue(SnakeState ss, char action){

        if(!q.containsKey(ss)){

            q.put(ss,new HashMap<>());
            q.get(ss).put(action,0.0);
            return 0;
        }

        Map<Character, Double> vals = q.get(ss);
        if(!vals.containsKey(action))
            vals.put(action,0.0);

        return vals.get(action);

    }

    public double getMaxQValue(SnakeState state) {

        if(!q.containsKey(state))
            return 0;

        Map<Character, Double> vals = q.get(state);

        if(vals.isEmpty())
            return 0;

        return Collections.max(vals.values());

    }

    public char getIdealAction(SnakeState state) {

        List<Character> actions = state.possibleActions();

        char randomAction = actions.get(random.nextInt(actions.size()));

        if(!q.containsKey(state) || q.get(state).isEmpty())
            return randomAction;

        Map<Character,Double> map = q.get(state);

        double max = Double.MIN_VALUE;
        char action = randomAction;

        for(Map.Entry<Character,Double> entry: map.entrySet()) {
            if(entry.getValue() > max) {
                action = entry.getKey();
                max = entry.getValue();
            }
        }

        return action;

    }

    public Map<SnakeState,Character> learnPolicy() {

        Map<SnakeState,Character> learnedPolicy = new HashMap<>();

        for(SnakeState state: q.keySet()){
            char action = getIdealAction(state);
            learnedPolicy.put(state,action);
        }

        return learnedPolicy;

    }


}
