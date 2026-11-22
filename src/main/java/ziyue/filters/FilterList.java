package ziyue.filters;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import ziyue.filters.gui.IconButton;

import java.util.ArrayList;

/**
 * Filter list for creative mode tabs.
 *
 * @author ZiYueCommentary
 * @since 1.0.0
 */
public class FilterList extends ArrayList<Filter>
{
	public IconButton btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll, btnReserved;
	public int filterIndex = 0;
	public boolean enabled = true;

	public Filter uncategorizedItems = null;
	public Button.OnPress btnReservedOnPress = null;
	public Component btnReservedTooltip = null;
	public Identifier btnReservedIcon = null;

	public static FilterList empty() {
		return new FilterList();
	}
}
