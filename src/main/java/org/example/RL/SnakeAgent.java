package org.example.RL;

import org.example.GameFrame;
import org.example.GamePanel;

import java.util.*;

public class SnakeAgent {

    private Map<SnakeState,Character> learnedPolicy;
    private Map<SnakeState, Map<Character,Double>> q;

    private double randomNess;

    private Random random;

    private double discount;

    private double learningRate;

    public SnakeAgent() {
        this.learnedPolicy = new HashMap<>();
        this.q= new HashMap<>();
        this.random = new Random();
        this.randomNess = 0.29;
        this.discount = 0.97;
        this.learningRate = 0.7;
    }

    public void runAll(int n) {

        GameFrame gf = new GameFrame(0);

        for(int i=0;i<n;i++) {
            System.out.println("Hello");
        }

    }

    public void run(GameFrame gf) throws InterruptedException {

        gf.play();
        GamePanel gp = gf.gp;

        SnakeState state = gp.startManualGame(100);

        while(true) {

            List<Character> actions = state.getPossibleActions();

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
            double nReward = nState.getRewardForState();

            // Q(s,a)
            double currentQ = getQValue(state,action);

            double sample = nReward + discount * getMaxQValue(nState);

            // Q(s,a) <-- Q(s,a) + learning_rate * (sample - Q(s,a))
            double newQ = currentQ + learningRate * (sample - currentQ);

            setQValue(state,action,newQ);

            if(!nState.running)
                break;
        }

    }

    public void setQValue(SnakeState state, char action, double qv){

        if(!q.containsKey(state))
            q.put(state,new HashMap<>());

        q.get(state).put(action,qv);

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


        List<Character> actions = state.getPossibleActions();

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

    public void learnPolicy() {

        for(SnakeState state: q.keySet()){
            char action = getIdealAction(state);
            learnedPolicy.put(state,action);
        }

        System.out.println("Policy Learned: "+learnedPolicy);

    }


}
