package org.ivangeevo.immovens.config;

public class ModSettings
{
    protected boolean doFOVScaling = true;
    protected boolean doPainSounds = false;
    protected boolean doHardcoreExhaustion = true;
    protected boolean doHungerPenalties = true;
    protected boolean doHealthPenalties = true;
    protected boolean doNaturalRegen = true;

    public boolean isHardcoreHungerEnabled() {
        return doHardcoreExhaustion;
    }

    public boolean isHungerPenaltiesEnabled() {
        return doHungerPenalties;
    }

    public boolean isHealthPenaltiesEnabled() {
        return doHealthPenalties;
    }

    public boolean isFOVScalingEnabled() {
        return doFOVScaling;
    }

    public boolean hasPainSounds() {
        return doPainSounds;
    }

    public boolean isNaturalRegenEnabled() {
        return doNaturalRegen;
    }
}