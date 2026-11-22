package ziyue.filters.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Button with icon.
 *
 * @author ZiYueCommentary
 * @since 1.0.0
 */
public class IconButton extends Button
{
	protected static final WidgetSprites TEXTURES = new WidgetSprites(
			Identifier.withDefaultNamespace("widget/button"),
			Identifier.withDefaultNamespace("widget/button_disabled"),
			Identifier.withDefaultNamespace("widget/button_highlighted")
	);

	protected Identifier iconResource;

	public IconButton(int x, int y, Component tooltip, OnPress onPress, Identifier iconResource) {
		super(x, y, 20, 20, tooltip, onPress, DEFAULT_NARRATION);
		this.iconResource = iconResource;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
		context.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURES.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.width, this.height);
		context.blitSprite(RenderPipelines.GUI_TEXTURED, this.iconResource, this.getX() + 2, this.getY() + 2, 16, 16);
	}
}
