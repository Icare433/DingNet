package selfadaptation.instrumentation;

import iot.networkentity.Gateway;
import iot.networkentity.Mote;
import iot.networkentity.NetworkEntity;
import selfadaptation.feedbackloop.GenericFeedbackLoop;

/**
 * A class representing methods for probing.
 */
public class MoteProbe {
    /**
     * A list with feedBackLoops using the probe.
     */
    private GenericFeedbackLoop genericFeedbackLoop;

    /**
     * Constructs a MoteProbe with no FeedBackLoops using it.
     */
    public MoteProbe(){

    }

    /**
     * Returns a list of GenericFeedbackLoops using the probe.
     * @return  A list of GenericFeedbackLoops using the probe.
     */
    public GenericFeedbackLoop getGenericFeedbackLoop() {
        return genericFeedbackLoop;
    }

    /**
     * Sets a GenericFeedbackLoop using the probe.
     * @param genericFeedbackLoop The FeedbackLoop to set.
     */
    public void setGenericFeedbackLoop(GenericFeedbackLoop genericFeedbackLoop){
        this.genericFeedbackLoop =genericFeedbackLoop;
    }

    /**
     * Returns the spreading factor of a given mote.
     * @param mote The mote to generate the graph of.
     * @return the spreading factor of the mote
     */
    public Integer getSpreadingFactor(NetworkEntity mote) {
        return mote.getSF();
    }

    /**
     * Triggers the feedback loop.
     * @param gateway
     * @param devEUI
     */
    public void trigger(Gateway gateway, Long devEUI){
        gateway.getEnvironment().getMotes().stream()
            .filter(m -> m.getEUI() == devEUI && getGenericFeedbackLoop().isActive())
            .findFirst()
            .ifPresent((m) -> getGenericFeedbackLoop().adapt(m, gateway));
    }

    public int getPowerSetting(Mote mote) {
        return mote.getTransmissionPower();
    }
}
