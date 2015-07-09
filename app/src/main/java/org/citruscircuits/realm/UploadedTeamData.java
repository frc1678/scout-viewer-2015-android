import io.realm.RealmObject;

public class UploadedTeamData extends RealmObject {
    private String pitOrganization;
    private String programmingLanguage;
    private String pitNotes;
    private boolean canMountMechanism;
    private boolean rampable;
    private int mountingSpeed;
    private boolean mechRemove;
}
