package org.ivangeevo.im_movens.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.ivangeevo.im_movens.datagen.provider.ImMovensLangProvider;
import org.ivangeevo.im_movens.datagen.provider.ImMovensSoundsProvider;

public class ImMovensDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ImMovensLangProvider::new);
        pack.addProvider(ImMovensSoundsProvider::new);
    }
}