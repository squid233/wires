package io.github.squid233.wires.item;

import io.github.squid233.wires.Wires;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author squid233
 * @since 0.1.0
 */
public class SelectorItem extends Item {
    protected final String subKey;

    public SelectorItem(String subKey, Settings settings) {
        super(settings);
        this.subKey = subKey;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound sub = stack.getSubTag(subKey);
        if (sub != null) {
            tooltip.add(new TranslatableText("item.tooltip." + Wires.NAMESPACE + ".selected",
                sub.getInt("x"),
                sub.getInt("y"),
                sub.getInt("z"))
                .styled(style -> style.withColor(Formatting.GREEN)));
        }
    }
}
