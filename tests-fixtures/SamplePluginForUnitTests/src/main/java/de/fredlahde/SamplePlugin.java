package de.fredlahde;

import software.kloud.ChromPluginSDK.IKMSPlugin;
import software.kloud.ChromPluginSDK.KMSPlugin;

@KMSPlugin(author = "Fred Lahde", version = "0.1", priority = 1)
public class SamplePlugin implements IKMSPlugin {
    @Override
    public void init() {
    }
}
