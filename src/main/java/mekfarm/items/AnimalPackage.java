package mekfarm.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import mekfarm.MekfarmMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

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
        if (this.hasAnimal(stack) == true) {
            return "Mekfarm_AnimalPackage_Full";
        }
        return this.getUnlocalizedName();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);

        NBTTagCompound nbt = (stack == null) ? null : stack.getTagCompound();
        if ((nbt != null) && (nbt.getInteger("hasAnimal") == 1)) {
            tooltip.add(ChatFormatting.AQUA + "Contains Animal");
        } else {
            tooltip.add(ChatFormatting.DARK_GRAY + "No Animal");
        }
    }

    public boolean hasAnimal(ItemStack stack) {
        NBTTagCompound nbt = (stack == null) ? null : stack.getTagCompound();
        return ((nbt != null) && (nbt.getInteger("hasAnimal") == 1));
    }
}
