package ziyue.filters.mixin;

import net.fabricmc.fabric.api.client.creativetab.v1.FabricCreativeModeInventoryScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.filters.FilterBuilder;
import ziyue.filters.FilterList;
import ziyue.filters.FiltersApi;
import ziyue.filters.gui.IconButton;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static ziyue.filters.FiltersApi.ICON_CHECK;
import static ziyue.filters.FiltersApi.ICON_CROSS;
import static ziyue.filters.FiltersApi.ICON_DOWN;
import static ziyue.filters.FiltersApi.ICON_UP;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> implements FabricCreativeModeInventoryScreen
{
	@Shadow
	private static CreativeModeTab selectedTab;
	@Shadow
	@Final
	private Set<TagKey<Item>> visibleTags;
	@Shadow
	private float scrollOffs;
	@Unique
	private static boolean filtersAPI$itemsCategorized = false;

	public CreativeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory playerInventory, Component text) {
		super(menu, playerInventory, text);
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	private void afterInit(LocalPlayer player, FeatureFlagSet enabledFeatures, boolean operatorTabEnabled, CallbackInfo ci) {
		if (!filtersAPI$itemsCategorized) {
			AtomicInteger uncategorizedItems = new AtomicInteger(0);
			AtomicInteger uncategorizedFilters = new AtomicInteger(0);

			BuiltInRegistries.ITEM.forEach(item -> CreativeModeTabs.tabs().forEach(tab -> {
				if (FilterBuilder.isTabHasFilters(tab)) {
					FilterList filters = FilterBuilder.FILTERS.get(tab);
					if (filters.uncategorizedItems != null) {
						List<Item> items = tab.getDisplayItems().stream().map(ItemStack::getItem).toList();
						if (items.contains(item) && !FilterBuilder.isItemCategorized(tab, item)) {
							filters.uncategorizedItems.addItems(item);
							uncategorizedItems.getAndIncrement();
						}
					}
				}
			}));

			FilterBuilder.FILTERS.forEach((tabId, filterList) -> {
				if (filterList.uncategorizedItems != null && !filterList.uncategorizedItems.items.isEmpty()) {
					filterList.add(filterList.uncategorizedItems);
					uncategorizedFilters.getAndIncrement();
				}
			});

			FiltersApi.LOGGER.info("Found {} uncategorized items, added {} filters to the filter lists", uncategorizedItems.get(), uncategorizedFilters.get());
			filtersAPI$itemsCategorized = true;
		}
	}

	@Inject(at = @At("HEAD"), method = "extractRenderState")
	protected void beforeRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		FilterBuilder.FILTERS.forEach((map, filter1) -> filtersApi$showButtons(filter1, false));
		FilterBuilder.FILTERS.forEach((map, filter) -> filter.forEach(button -> button.visible = false));

		if (!FilterBuilder.isTabHasFilters(selectedTab)) {
			return;
		}
		filtersApi$updateItems();
		FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
		filter.filterIndex = Math.max(0, Math.min(filter.filterIndex, filter.size() - 4));
		filtersApi$showButtons(filter, true);
		for (int o = 0; o < filter.size(); o++) {
			if (o >= filter.filterIndex && o < filter.filterIndex + 4) {
				filter.get(o).setX(this.leftPos - 28);
				filter.get(o).setY(this.topPos + 27 * (o - filter.filterIndex) + 10);
				filter.get(o).visible = true;
			} else {
				filter.get(o).visible = false;
			}
		}
		filter.btnScrollUp.active = filter.filterIndex > 0;
		filter.btnScrollDown.active = filter.filterIndex + 4 < filter.size();
	}

	@Inject(at = @At("TAIL"), method = "extractRenderState")
	protected void afterRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (!FilterBuilder.isTabHasFilters(selectedTab)) {
			return;
		}

		FilterList filter = FilterBuilder.FILTERS.get(selectedTab);
		if (filter.btnScrollUp.isHovered()) {
			context.setTooltipForNextFrame(this.font, filter.btnScrollUp.getMessage(), mouseX, mouseY);
		}
		if (filter.btnScrollDown.isHovered()) {
			context.setTooltipForNextFrame(this.font, filter.btnScrollDown.getMessage(), mouseX, mouseY);
		}
		if (filter.btnEnableAll.isHovered()) {
			context.setTooltipForNextFrame(this.font, filter.btnEnableAll.getMessage(), mouseX, mouseY);
		}
		if (filter.btnDisableAll.isHovered()) {
			context.setTooltipForNextFrame(this.font, filter.btnDisableAll.getMessage(), mouseX, mouseY);
		}
		if (filter.btnReserved != null && filter.btnReserved.isHovered() && filter.btnReservedTooltip != null) {
			context.setTooltipForNextFrame(this.font, filter.btnReservedTooltip, mouseX, mouseY);
		}

		filter.forEach(filter1 -> {
			if (filter1.isHovered()) {
				context.setTooltipForNextFrame(this.font, filter1.getMessage(), mouseX, mouseY);
			}
		});
	}

	@Inject(at = @At("TAIL"), method = "init")
	protected void afterInit(CallbackInfo ci) {
		FilterBuilder.FILTERS.forEach((map, filter) -> {
			filter.btnScrollUp = new IconButton(this.leftPos - 22, this.topPos - 12, Component.translatable("button.filters.scroll_up").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex--, ICON_UP);
			filter.btnScrollDown = new IconButton(this.leftPos - 22, this.topPos + 119, Component.translatable("button.filters.scroll_down").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex++, ICON_DOWN);
			filter.btnEnableAll = new IconButton(this.leftPos - 50, this.topPos + 10, Component.translatable("button.filters.enable_all").withStyle(ChatFormatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = true), ICON_CHECK);
			filter.btnDisableAll = new IconButton(this.leftPos - 50, this.topPos + 32, Component.translatable("button.filters.disable_all").withStyle(ChatFormatting.WHITE), button -> FilterBuilder.FILTERS.get(selectedTab).forEach(filter1 -> filter1.enabled = false), ICON_CROSS);
			if (filter.btnReservedOnPress != null) {
				filter.btnReserved = new IconButton(this.leftPos - 50, this.topPos + 54, filter.btnReservedTooltip, filter.btnReservedOnPress, filter.btnReservedIcon);
				this.addRenderableWidget(filter.btnReserved);
			}
			this.addRenderableWidget(filter.btnScrollUp);
			this.addRenderableWidget(filter.btnScrollDown);
			this.addRenderableWidget(filter.btnEnableAll);
			this.addRenderableWidget(filter.btnDisableAll);

			filter.forEach(this::addRenderableWidget);
		});
	}

	@Unique
	protected void filtersApi$showButtons(FilterList list, boolean visible) {
		if (list.btnScrollUp == null) {
			return;
		}
		if (list.size() > 4) {
			list.btnScrollUp.visible = visible;
			list.btnScrollDown.visible = visible;
		} else {
			list.btnScrollUp.visible = false;
			list.btnScrollDown.visible = false;
		}
		if (list.btnReserved != null) {
			list.btnReserved.visible = list.btnReservedOnPress != null && visible;
		}
		list.btnEnableAll.visible = visible;
		list.btnDisableAll.visible = visible;
	}

	@Unique
	protected void filtersApi$updateItems() {
		visibleTags.clear();
		this.menu.items.clear();
		Set<Item> enabledItems = new HashSet<>();
		FilterBuilder.FILTERS.get(selectedTab).forEach(filter -> {
			if (filter.enabled) {
				enabledItems.addAll(filter.items);
			}
		});
		selectedTab.getDisplayItems().stream()
				.filter(stack -> enabledItems.contains(stack.getItem()))
				.map(ItemStack::copy)
				.forEach(this.menu.items::add);
		this.menu.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem())));
		float previousOffset = this.scrollOffs;
		this.menu.scrollTo(0.0f);
		this.scrollOffs = previousOffset;
		this.menu.scrollTo(previousOffset);
	}
}
