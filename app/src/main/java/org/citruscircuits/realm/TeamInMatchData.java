import io.realm.RealmObject;

public class TeamInMatchData extends RealmObject {
    private Team team;
    private Match match;
    private UploadedTeamInMatchData uploadedData;
    private CalculatedTeamInMatchData calculatedData;
}
