package ziyue.filters;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Filter for creative mode tab.
 *
 * @author ZiYueCommentary
 * @since 1.0.0
 */
public class Filter extends Button
{
	public static final Identifier TAB_SELECTED = Identifier.withDefaultNamespace("container/creative_inventory/tab_top_selected_2");
	public static final Identifier TAB_UNSELECTED = Identifier.withDefaultNamespace("container/creative_inventory/tab_top_unselected_2");

	public Supplier<ItemStack> icon;
	public final List<Item> items;
	public boolean enabled = true;

	protected Filter(Component tooltip, Supplier<ItemStack> icon, List<Item> items) {
		super(0, 0, 32, 26, tooltip, button -> ((Filter) button).enabled = !((Filter) button).enabled, DEFAULT_NARRATION);
		this.icon = icon;
		this.items = items;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
		context.blitSprite(RenderPipelines.GUI_TEXTURED, enabled ? TAB_SELECTED : TAB_UNSELECTED, this.getX(), this.getY(), this.enabled ? 32 : 28, this.height);
		context.item(icon.get(), this.getX() + 8, this.getY() + 5);
	}

	public Filter addItems(Item... items) {
		this.items.addAll(Arrays.asList(items));
		return this;
	}
}
