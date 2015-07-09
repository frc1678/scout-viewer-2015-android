import io.realm.RealmObject;

public class UploadedTeamInMatchData extends RealmObject {
    private boolean stackedToteSet;
    private int numContainersMovedIntoAutoZone;
    private int numTotesStacked;
    private int numReconLevels;
    private int numNoodlesContributed;
    private int numReconsStacked;
    private int numHorizontalReconsPickedUp;
    private int numVerticalReconsPickedUp;
    private int numTotesPickedUpFromGround;
    private int numLitterDropped;
    private int numStacksDamaged;
    private RealmList<CoopAction> coopActions;
    private int maxFieldToteHeight;
    private int maxReconHeight;
    private int numTeleopReconsFromStep;
    private int numTotesFromHP;
    private RealmList<ReconAcquisition> reconAcquisitions;
    private int agility;
    private int stackPlacing;
    private int humanPlayerLoading;
    private boolean incapacitated;
    private boolean disabled;
    private String miscellaneousNotes;
    private int numStepReconAcquisitionsFailed;
    private int numSixStacksCapped;
}
