package handle;

import util.GreenHopperReleaseDataUtility;
import util.gadget.*;

public abstract class Handler {
    protected UserUtility userService;
    protected EpicUtility epicService;
    protected StoryUtility storyService;
    protected AssigneeUtility assigneeService;
    protected GadgetUtility gadgetService;
    protected CycleUtility cycleService;
    protected GreenHopperReleaseDataUtility greenHopperReleaseService;

    public Handler() {
        userService = UserUtility.getInstance();
        epicService = EpicUtility.getInstance();
        storyService = StoryUtility.getInstance();
        gadgetService = GadgetUtility.getInstance();
        assigneeService = AssigneeUtility.getInstance();
        cycleService = CycleUtility.getInstance();
        greenHopperReleaseService = GreenHopperReleaseDataUtility.getInstance();
    }

}
