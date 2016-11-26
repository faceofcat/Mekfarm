package mekfarm.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by CF on 2016-10-30.
 */
public class AnimalPackageItem extends BaseItem {
    public AnimalPackageItem() {
        super("animal_package");

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
    protected IRecipe getRecipe() {
        return new ShapedOreRecipe(new ItemStack(this, 1),
                "xyx", "yzy", "xyx",
                'x', Blocks.PLANKS,
                'y', Blocks.IRON_BARS,
                'z', Items.REDSTONE);
    }

    @Override
    public String getUnlocalizedName()
    {
        return "item.mekfarm_animal_package";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if (this.hasAnimal(stack) == true) {
            return "item.mekfarm_animal_package_full";
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
