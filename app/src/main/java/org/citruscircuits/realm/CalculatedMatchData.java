import io.realm.RealmObject;

public class CalculatedMatchData extends RealmObject {
    private int predictedRedScore;
    private int predictedBlueScore;
    private String bestRedAutoStrategy;
    private String bestBlueAutoStrategy;
}
