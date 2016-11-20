package mekfarm.ui;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.darkhax.tesla.lib.PowerBar;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

import java.util.List;

/**
 * Created by CF on 2016-11-18.
 */
public class EnergyUIElement extends MekUIElement {
    private TileEntity te;

    private static final int WIDTH = 18;
    private static final int HEIGHT = 54;

    public EnergyUIElement(TileEntity te, MekUIContainer container, int left, int top) {
        super(container, left, top, WIDTH, HEIGHT);

        this.te = te;
    }

    @Override
    public void renderBackground(int mouseX, int mouseY) {
        super.container.drawTexturedRect(this.left, this.top, 1, 175, this.getWidth(), this.getHeight());

        if (false && Loader.isModLoaded("tesla")) {
            this.renderTeslaBackground();
        }
        else if (this.te.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage storage = this.te.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null) {
                int power = (storage.getEnergyStored() * (this.getHeight() - 6)) / storage.getMaxEnergyStored();

                super.container.drawTexturedRect(this.left + 2, this.top + 2, 20, 177, this.getWidth() - 4, this.getHeight() - 4);
                super.container.drawTexturedRect(this.left + 3, this.top + 3 + this.getHeight() - 6 - power, 35, 178 + this.getHeight() - 6 - power, this.getWidth() - 6, power + 2);
            }
        }
    }

    @Optional.Method(modid = "tesla")
    private void renderTeslaBackground() {
        ITeslaHolder tesla = this.te.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        if (tesla != null) {
            PowerBar bar = new PowerBar(super.container, super.left + 2 + super.container.getGuiLeft(), super.top + 2 + super.container.getGuiTop(), PowerBar.BackgroundType.LIGHT);
            bar.draw(tesla);
        }
    }

    @Override
    public void renderTopLayer(int mouseX, int mouseY) {
        if (super.isMouseOver && this.te.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage storage = this.te.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null) {
                List<String> lines = Lists.newArrayList();
                lines.add(String.format("%s%,d T %sof", ChatFormatting.AQUA, storage.getEnergyStored(), ChatFormatting.DARK_GRAY));
                lines.add(String.format("%s%,d T", ChatFormatting.RESET, storage.getMaxEnergyStored()));
                super.container.drawTooltip(lines, mouseX, mouseY);
            }
        }
    }
}
