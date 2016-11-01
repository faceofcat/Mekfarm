package mekfarm.items;

import mekfarm.MekfarmMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by CF on 2016-10-30.
 */
public class AnimalPackage extends Item {
    public AnimalPackage() {
        super();

        this.setRegistryName("animal_package");
        this.setUnlocalizedName(MekfarmMod.MODID + ".animal_package");
        this.setCreativeTab(CreativeTabs.FOOD);

        this.addPropertyOverride(new ResourceLocation("hasAnimal"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                NBTTagCompound nbt = (stack == null) ? null : stack.getTagCompound();
                if ((nbt != null) && (nbt.getInteger("hasAnimal") == 1)) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public String getUnlocalizedName()
    {
        return "Mekfarm_AnimalPackage";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        NBTTagCompound nbt = (stack == null) ? null : stack.getTagCompound();
        if ((nbt != null) && (nbt.getInteger("hasAnimal") == 1)) {
            return "Mekfarm_AnimalPackage_Full";
        }
        return this.getUnlocalizedName();
    }
}
