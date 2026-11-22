package ziyue.filters;

import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ZiYueCommentary
 * @since 1.0.0
 */
public class FiltersApi
{
	public static final String MOD_ID = "filters";
	public static final Logger LOGGER = LogManager.getLogger("Filters API");
	public static final Identifier ICON_CHECK = Identifier.fromNamespaceAndPath(FiltersApi.MOD_ID, "check");
	public static final Identifier ICON_CROSS = Identifier.fromNamespaceAndPath(FiltersApi.MOD_ID, "cross");
	public static final Identifier ICON_UP = Identifier.fromNamespaceAndPath(FiltersApi.MOD_ID, "up");
	public static final Identifier ICON_DOWN = Identifier.fromNamespaceAndPath(FiltersApi.MOD_ID, "down");
	public static final Identifier ICON_WRENCH = Identifier.fromNamespaceAndPath(FiltersApi.MOD_ID, "wrench");
}
