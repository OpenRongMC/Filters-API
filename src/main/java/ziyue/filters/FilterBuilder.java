package ziyue.filters;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

import static ziyue.filters.FiltersApi.ICON_WRENCH;

/**
 * @author ZiYueCommentary
 * @since 1.0.0
 */
public class FilterBuilder
{
	public static final HashMap<CreativeModeTab, FilterList> FILTERS = new HashMap<>();

	public static Filter registerFilter(ResourceKey<CreativeModeTab> creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
		return FilterBuilder.registerFilter(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(creativeModeTab), filterName, filterIcon);
	}

	public static Filter registerFilter(CreativeModeTab creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
		Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
		FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
		filterList.add(filter);
		FILTERS.put(creativeModeTab, filterList);
		return filter;
	}

	public static Filter registerUncategorizedItemsFilter(ResourceKey<CreativeModeTab> creativeModeTab) {
		return FilterBuilder.registerUncategorizedItemsFilter(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(creativeModeTab));
	}

	public static Filter registerUncategorizedItemsFilter(CreativeModeTab creativeModeTab) {
		Filter filter = new Filter(Component.translatable("filter.filters.uncategorized"), () -> new ItemStack(Blocks.BARRIER), new ArrayList<>());
		FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
		filterList.uncategorizedItems = filter;
		FILTERS.put(creativeModeTab, filterList);
		return filter;
	}

	public static Filter registerUncategorizedItemsFilter(CreativeModeTab creativeModeTab, Supplier<ItemStack> filterIcon) {
		Filter filter = new Filter(Component.translatable("filter.filters.uncategorized"), filterIcon, new ArrayList<>());
		FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
		filterList.uncategorizedItems = filter;
		FILTERS.put(creativeModeTab, filterList);
		return filter;
	}

	public static Filter registerUncategorizedItemsFilter(CreativeModeTab creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
		Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
		FilterList filterList = FILTERS.getOrDefault(creativeModeTab, FilterList.empty());
		filterList.uncategorizedItems = filter;
		FILTERS.put(creativeModeTab, filterList);
		return filter;
	}

	public static void setReservedButton(ResourceKey<CreativeModeTab> creativeModeTab, Component tooltip, Button.OnPress onPress) {
		FilterBuilder.setReservedButton(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(creativeModeTab), tooltip, onPress, ICON_WRENCH);
	}

	public static void setReservedButton(CreativeModeTab creativeModeTab, Component tooltip, Button.OnPress onPress) {
		FilterBuilder.setReservedButton(creativeModeTab, tooltip, onPress, ICON_WRENCH);
	}

	public static void setReservedButton(CreativeModeTab creativeModeTab, Component tooltip, Button.OnPress onPress, Identifier icon) {
		FilterList filters = FilterBuilder.FILTERS.get(creativeModeTab);
		filters.btnReservedTooltip = tooltip;
		filters.btnReservedOnPress = onPress;
		filters.btnReservedIcon = icon;
	}

	public static void filtersVisibility(ResourceKey<CreativeModeTab> creativeModeTab, boolean visible) {
		FilterBuilder.filtersVisibility(BuiltInRegistries.CREATIVE_MODE_TAB.getValue(creativeModeTab), visible);
	}

	public static void filtersVisibility(CreativeModeTab creativeModeTab, boolean visible) {
		if (FilterBuilder.FILTERS.containsKey(creativeModeTab)) {
			FilterBuilder.FILTERS.get(creativeModeTab).enabled = visible;
		}
	}

	public static boolean isItemCategorized(CreativeModeTab creativeModeTab, Item item) {
		for (Filter filter : FilterBuilder.FILTERS.get(creativeModeTab)) {
			if (filter.items.contains(item)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTabHasFilters(CreativeModeTab creativeModeTab) {
		return FilterBuilder.FILTERS.containsKey(creativeModeTab)
				&& !FilterBuilder.FILTERS.get(creativeModeTab).isEmpty()
				&& FilterBuilder.FILTERS.get(creativeModeTab).enabled;
	}
}
