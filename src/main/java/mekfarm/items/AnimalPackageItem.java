package mekfarm.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import mekfarm.MekfarmMod;
import mekfarm.common.ItemsRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.ndrei.teslacorelib.compatibility.ItemStackUtil;

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
                NBTTagCompound nbt = ItemStackUtil.isEmpty(stack) ? null : stack.getTagCompound();
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

        NBTTagCompound nbt = ItemStackUtil.isEmpty(stack) ? null : stack.getTagCompound();
        if ((nbt != null) && (nbt.getInteger("hasAnimal") == 1)) {
            tooltip.add(ChatFormatting.AQUA + "Contains Animal");
            String className = nbt.getString("animalClass");
            tooltip.add(ChatFormatting.DARK_AQUA + className.substring(className.lastIndexOf(".") + 1));
            if (nbt.hasKey("animalHealth", Constants.NBT.TAG_FLOAT)) {
                tooltip.add(ChatFormatting.BLUE + "Health: " + String.format("%.2f", nbt.getFloat("animalHealth")));
            }
        } else {
            tooltip.add(ChatFormatting.DARK_GRAY + "No Animal");
        }
    }

    public boolean hasAnimal(ItemStack stack) {
        NBTTagCompound nbt = ItemStackUtil.isEmpty(stack) ? null : stack.getTagCompound();
        return ((nbt != null) && (nbt.getInteger("hasAnimal") == 1));
    }

    public EntityAnimal unpackage(World world, ItemStack stack) {
        if (ItemStackUtil.isEmpty(stack) || (stack.getItem() != ItemsRegistry.animalPackage)
                || !hasAnimal(stack)) {
            return null;
        }

        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null) {
            return null;
        }

        NBTTagCompound animal = compound.getCompoundTag("animal");
        String animalClass = compound.getString("animalClass");
        try {
            Class cea = Class.forName(animalClass);
            Object thing = cea.getConstructor(World.class).newInstance(world);
            if (thing instanceof EntityAnimal) {
                EntityAnimal ea = (EntityAnimal) thing;
                if (animal.hasKey("Attributes", 9) && (world != null) && world.isRemote)
                {
                    // this is the reverse of what EntityLiving does... so that attributes work on client side
                    SharedMonsterAttributes.setAttributeModifiers(ea.getAttributeMap(), animal.getTagList("Attributes", 10));
                }
                ea.readEntityFromNBT(animal);

                return ea;
            }
        } catch (Throwable e) {
            MekfarmMod.logger.warn("Error creating animal '" + animalClass + "'.", e);
        }

        return null;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return this.hasAnimal(stack) ? 1 : 16;
    }
}
