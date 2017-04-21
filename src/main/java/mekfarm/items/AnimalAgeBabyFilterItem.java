package mekfarm.items;

import mekfarm.common.ItemsRegistry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.tileentities.ElectricTileEntity;

/**
 * Created by CF on 2016-11-10.
 */
public class AnimalAgeBabyFilterItem extends BaseAnimalFilterItem {
    public AnimalAgeBabyFilterItem() {
        super("animal_age_filter_baby");
    }

    @Override
    public boolean canProcess(ElectricTileEntity machine, int entityIndex, EntityAnimal entity) {
        return ((entity != null) && entity.isChild());
    }

    @Override
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "   ", " y ", "xxx",
                'x', "dustRedstone", // Items.REDSTONE,
                'y', ItemsRegistry.animalFilter
        );
    }
}
