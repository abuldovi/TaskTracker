package abuldovi.apps.bugtracker.states;

public enum StatusState {
    NOTSTARTED("Not started"),
    START("Start"),
    INPROGRESS("In progress"),
    FINISHED("Finished"),
    CANCELED("Canceled"),
    ;

    public final String label;
    StatusState(String label) {
        this.label = label;
    }
}
