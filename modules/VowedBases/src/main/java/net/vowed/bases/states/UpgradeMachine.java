package net.vowed.bases.states;

/**
 * Created by JPaul on 8/10/2016.
 */
public class UpgradeMachine
{
    private static UpgradeMachine UPGRADE_MACHINE = new UpgradeMachine();
    
    private UpgradeState currentState;

    public UpgradeMachine()
    {
        currentState = new BeginningState();
    }

    public void execute()
    {
        currentState.execute();
    }

    public void changeState(UpgradeState state)
    {
        currentState = state;
    }

    public void upgradeTo(UpgradeState state)
    {
        currentState = state;
        execute();
    }

    public static UpgradeMachine getInstance()
    {
        if (UPGRADE_MACHINE == null)
        {
            UPGRADE_MACHINE = new UpgradeMachine();
        }

        return UPGRADE_MACHINE;
    }
}
