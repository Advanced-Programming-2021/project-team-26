package controller;

public enum Phase {
    DRAW("draw phase"),
    STANDBY("standby phase"),
    MAIN1("main phase 1"),
    BATTLE("battle phase"),
    MAIN2("main phase 2"),
    END("end phase");

    String phaseName;

    Phase(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getPhaseName() {
        return this.phaseName;
    }

    public Phase nextPhase() {
        switch (this) {
            case DRAW:
                return STANDBY;
            case STANDBY:
                return MAIN1;
            case MAIN1:
                return BATTLE;
            case BATTLE:
                return MAIN2;
            case MAIN2:
                return END;
            case END:
                return null;
        }
        return null;
    }
}
