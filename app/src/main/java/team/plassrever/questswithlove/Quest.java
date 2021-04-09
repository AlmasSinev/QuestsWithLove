package team.plassrever.questswithlove;

public class Quest {
    int id;
    String name, type;
    int playerAmount, cost;
    String specification;
    int difficulty, recomentAge, interval;

    public Quest(int id, String name, String type, int playerAmount, int cost, String specification, int difficulty, int recomentAge, int interval) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.playerAmount = playerAmount;
        this.cost = cost;
        this.specification = specification;
        this.difficulty = difficulty;
        this.recomentAge = recomentAge;
        this.interval = interval;
    }

    public Quest(){

    }
}
