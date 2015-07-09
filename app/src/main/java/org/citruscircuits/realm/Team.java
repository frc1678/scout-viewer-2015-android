import io.realm.RealmObject;

public class Team extends RealmObject {
    private int number;
    private String name;
    private int seed;
    private RealmList<TeamInMatchData> matchData;
    private CalculatedTeamData calculatedData;
    private UploadedTeamData uploadedData;
}
