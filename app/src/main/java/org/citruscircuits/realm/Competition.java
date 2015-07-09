import io.realm.RealmObject;

public class Competition extends RealmObject {
    private String name;
    private String competitionCode;
    private RealmList<Match> matches;
    private RealmList<Team> attendingTeams;
    private CalculatedCompetitionData calculatedData;
}
