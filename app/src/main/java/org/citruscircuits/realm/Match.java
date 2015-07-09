import io.realm.RealmObject;

public class Match extends RealmObject {
    private String match;
    private RealmList<Team> redTeams;
    private RealmList<Team> blueTeams;
    private RealmList<TeamInMatchData> teamInMatchDatas;
    private int officialRedScore;
    private int officialBlueScore;
    private CalculatedMatchData calculatedData;
}
