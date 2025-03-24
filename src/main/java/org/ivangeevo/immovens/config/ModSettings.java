package org.ivangeevo.immovens.config;

public class ModSettings
{
    protected boolean disableFOVScaling = false;
    protected boolean painSounds = false;

    public boolean isFOVScalingDisabled() {
        return disableFOVScaling;
    }

    public boolean hasPainSounds() {
        return painSounds;
    }
}