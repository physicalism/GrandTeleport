package dev.codex.gtaliketeleport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.LockIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

final class GtaLikeTeleportConfigScreen extends Screen {
   private static final int PANEL_COLOR = 1711276032;
   private static final int PANEL_BORDER_COLOR = 2013265919;
   private static final int DEBUG_BORDER_COLOR = -171;
   private static final int DEBUG_HANDLE_COLOR = -855638187;
   private static final int DEBUG_ITEM_BORDER_COLOR = -1996488875;
   private static final int TEXT_COLOR = -1;
   private static final int MUTED_TEXT_COLOR = -6250336;
   private static final int OUTER_PADDING_LEFT = 14;
   private static final int OUTER_PADDING_TOP = 14;
   private static final int OUTER_PADDING_RIGHT = 14;
   private static final int OUTER_PADDING_BOTTOM = 0;
   private static final int MIN_PANEL_WIDTH = 300;
   private static final int MIN_PANEL_HEIGHT = 142;
   private static final int MAX_PANEL_MARGIN = 28;
   private static final int DEBUG_TOOLBAR_MARGIN = 6;
   private static final int DEBUG_TOOLBAR_BUTTON_HEIGHT = 20;
   private static final int DEBUG_TOOLBAR_GAP = 6;
   private static final int DEBUG_TOOLBAR_ROW_STEP = 22;
   private static final int DEBUG_PANEL_TOP_MARGIN = 84;
   private static final double MAX_CONTENT_WIDTH_RATIO = 0.72;
   private static final int RESIZE_HIT_SIZE = 10;
   private static final int SNAP_DISTANCE = 5;
   private static final int GRID_SIZE = 8;
   private static final int DEBUG_GRID_COLOR = 587202559;
   private static final int DEBUG_CENTER_GUIDE_COLOR = 2007682303;
   private static final int DEBUG_PANEL_GUIDE_COLOR = 1728053247;
   private static final int DEBUG_SELECTED_GUIDE_COLOR = -1996488875;
   private static final double DEFAULT_PANEL_WIDTH_RATIO = 0.58;
   private static final double DEFAULT_LINKED_PANEL_HEIGHT_RATIO = 0.24;
   private static final double DEFAULT_UNLINKED_PANEL_HEIGHT_RATIO = 0.31;
   private static final Identifier OVERWORLD_DIMENSION_ICON = Identifier.withDefaultNamespace("textures/block/grass_block_side.png");
   private static final Identifier NETHER_DIMENSION_ICON = Identifier.withDefaultNamespace("textures/block/netherrack.png");
   private static final Identifier END_DIMENSION_ICON = Identifier.withDefaultNamespace("textures/block/end_stone.png");
   private static final String ITEM_PANEL = "panel";
   private static final String ITEM_TITLE = "title";
   private static final String ITEM_DESCRIPTION = "description";
   private static final String ITEM_STATUS_LINKED = "status_linked";
   private static final String ITEM_STATUS_UNLINKED = "status_unlinked";
   private static final String ITEM_LINKED_SLIDER = "linked_slider";
   private static final String ITEM_ZOOM_OUT_SLIDER = "zoom_out_slider";
   private static final String ITEM_ZOOM_IN_SLIDER = "zoom_in_slider";
   private static final String ITEM_LINK_BUTTON = "link_button";
   private static final String ITEM_DIMENSION_OVERWORLD = "dimension_overworld";
   private static final String ITEM_DIMENSION_NETHER = "dimension_nether";
   private static final String ITEM_DIMENSION_END = "dimension_end";
   private static final String ITEM_RESET_BUTTON = "reset_button";
   private static final String ITEM_DONE_BUTTON = "done_button";
   private static final String ITEM_PREV_PAGE_BUTTON = "prev_page_button";
   private static final String ITEM_NEXT_PAGE_BUTTON = "next_page_button";
   private static final String ITEM_TAB_GENERAL = "tab_general";
   private static final String ITEM_TAB_ZOOM_STAGE = "tab_zoom_stage";
   private static final String ITEM_TAB_ZOOM_STAGE_2 = "tab_zoom_stage_2";
   private static final String ITEM_TAB_SOUNDS = "tab_sounds";
   private static final String ITEM_TAB_OTHERS = "tab_others";
   private static final String ITEM_GENERAL_TITLE = "general_title";
   private static final String ITEM_GENERAL_DESCRIPTION = "general_description";
   private static final String ITEM_EFFECT_LABEL = "effect_label";
   private static final String ITEM_EFFECT_TOGGLE = "effect_toggle";
   private static final String ITEM_MOVEMENT_LABEL = "movement_label";
   private static final String ITEM_MOVEMENT_TOGGLE = "movement_toggle";
   private static final String ITEM_CROSS_DIMENSION_TRAVEL_LABEL = "cross_dimension_travel_label";
   private static final String ITEM_CROSS_DIMENSION_TRAVEL_TOGGLE = "cross_dimension_travel_toggle";
   private static final String ITEM_ADVANCED1_TITLE = "advanced1_title";
   private static final String ITEM_ADVANCED1_DESCRIPTION = "advanced1_description";
   private static final String ITEM_ADVANCED2_TITLE = "advanced2_title";
   private static final String ITEM_ADVANCED2_DESCRIPTION = "advanced2_description";
   private static final String ITEM_ADVANCED3_TITLE = "advanced3_title";
   private static final String ITEM_ADVANCED3_DESCRIPTION = "advanced3_description";
   private static final String ITEM_ZOOM_STAGE_GLIDE_SLIDER = "zoom_stage_glide_slider";
   private static final String ITEM_ZOOM_STAGE_GLIDE_TICKS_LABEL = "zoom_stage_glide_ticks_label";
   private static final String ITEM_ZOOM_STAGE_GLIDE_TICKS_FIELD = "zoom_stage_glide_ticks_field";
   private static final String ITEM_ZOOM_OUT_TICKS_LABEL = "zoom_out_ticks_label";
   private static final String ITEM_ZOOM_OUT_TICKS_FIELD = "zoom_out_ticks_field";
   private static final String ITEM_ZOOM_IN_TICKS_LABEL = "zoom_in_ticks_label";
   private static final String ITEM_ZOOM_IN_TICKS_FIELD = "zoom_in_ticks_field";
   private static final String ITEM_BODY_HEIGHT_SLIDER = "body_height_slider";
   private static final String ITEM_BODY_GLIDE_SLIDER = "body_glide_slider";
   private static final String ITEM_BODY_GLIDE_TICKS_LABEL = "body_glide_ticks_label";
   private static final String ITEM_BODY_GLIDE_TICKS_FIELD = "body_glide_ticks_field";
   private static final String ITEM_PLAYER_HIDE_LABEL = "player_hide_label";
   private static final String ITEM_PLAYER_HIDE_TICKS_FIELD = "player_hide_ticks_field";
   private static final String ITEM_SOUNDS_TITLE = "sounds_title";
   private static final String ITEM_SOUNDS_DESCRIPTION = "sounds_description";
   private static final String ITEM_SOUND_MODE_LABEL = "sound_mode_label";
   private static final String ITEM_SOUND_MODE_TOGGLE = "sound_mode_toggle";
   private static final String ITEM_MINECRAFT_VOLUME_SLIDER = "minecraft_volume_slider";
   private static final String ITEM_CUSTOM_VOLUME_SLIDER = "custom_volume_slider";
   private static final String ITEM_OTHERS_TITLE = "others_title";
   private static final String ITEM_OTHERS_DESCRIPTION = "others_description";
   private static final String ITEM_WARP_PLATE_LABEL = "warp_plate_label";
   private static final String ITEM_WARP_PLATE_TOGGLE = "warp_plate_toggle";
   private static final String ITEM_EXTERNAL_TELEPORT_LABEL = "external_teleport_label";
   private static final String ITEM_EXTERNAL_TELEPORT_TOGGLE = "external_teleport_toggle";
   private static final String ITEM_FALLBACK_CHUNK_FADE_LABEL = "fallback_chunk_fade_label";
   private static final String ITEM_FALLBACK_CHUNK_FADE_TOGGLE = "fallback_chunk_fade_toggle";
   private final Screen parent;
   private ConfigPage currentPage;
   private boolean linked;
   private boolean effectEnabled;
   private boolean movementAllowed;
   private boolean crossDimensionTravelEnabled;
   private boolean layoutDebugEnabled;
   private boolean layoutAspectLocked;
   private boolean layoutGridEnabled;
   private boolean layoutSnapEnabled;
   private boolean updatingValueWidgets;
   private double[] zoomOutHeights;
   private double[] zoomInHeights;
   private GtaLikeTeleportConfig.ZoomDimension selectedZoomDimension;
   private int[] zoomOutStageTicks;
   private int[] zoomInStageTicks;
   private double zoomStageGlideHeight;
   private int zoomStageGlideTicks;
   private double bodyCameraHeight;
   private double bodyGlideHeight;
   private int bodyGlideTicks;
   private int localPlayerHideTicks;
   private boolean customSoundsEnabled;
   private double minecraftSoundVolume;
   private double customSoundVolume;
   private boolean warpPlateTransitionsEnabled;
   private boolean externalTeleportTransitionsEnabled;
   private boolean fallbackChunkFadeEnabled;
   private StageHeightSlider linkedSlider;
   private StageHeightSlider zoomOutSlider;
   private StageHeightSlider zoomInSlider;
   private SingleValueSlider zoomStageGlideSlider;
   private SingleValueSlider bodyHeightSlider;
   private SingleValueSlider bodyGlideSlider;
   private SingleValueSlider minecraftSoundVolumeSlider;
   private SingleValueSlider customSoundVolumeSlider;
   private EditBox zoomStageGlideTicksEditBox;
   private EditBox zoomOutTicksEditBox;
   private EditBox zoomInTicksEditBox;
   private EditBox bodyGlideTicksEditBox;
   private EditBox playerHideTicksEditBox;
   private LockIconButton linkButton;
   private DimensionIconButton overworldDimensionButton;
   private DimensionIconButton netherDimensionButton;
   private DimensionIconButton endDimensionButton;
   private Button resetButton;
   private Button doneButton;
   private Button prevPageButton;
   private Button nextPageButton;
   private Button[] pageTabButtons;
   private Button effectToggleButton;
   private Button movementToggleButton;
   private Button crossDimensionTravelToggleButton;
   private Button soundModeToggleButton;
   private Button warpPlateToggleButton;
   private Button externalTeleportToggleButton;
   private Button fallbackChunkFadeToggleButton;
   private Button layoutDebugButton;
   private Button aspectButton;
   private Button resetLayoutButton;
   private Button resetItemSizeButton;
   private Button gridButton;
   private Button snapButton;
   private EditBox textEditBox;
   private EditBox layoutValueEditBox;
   private Button applyLayoutValueButton;
   private boolean textEditorVisible;
   private boolean layoutValueEditorVisible;
   private boolean updatingTextEditor;
   private boolean updatingLayoutValueEditor;
   private String selectedLayoutItem;
   private String editingLayoutItem;
   private LayoutEditAction layoutEditAction;
   private LayoutRect editStartRect;
   private LayoutRect editingRect;
   private double editStartMouseX;
   private double editStartMouseY;
   private int sessionLayoutBaseWidth;
   private int sessionLayoutBaseHeight;

   GtaLikeTeleportConfigScreen(Screen parent) {
      super(Component.literal("Grand Teleport Settings"));
      this.currentPage = GtaLikeTeleportConfigScreen.ConfigPage.GENERAL;
      this.pageTabButtons = new Button[0];
      this.selectedLayoutItem = "panel";
      this.layoutEditAction = GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
      this.parent = parent;
      this.selectedZoomDimension = getInitialZoomDimension();
      this.linked = GtaLikeTeleportConfig.areZoomHeightsLinked(this.selectedZoomDimension);
      this.effectEnabled = GtaLikeTeleportConfig.isEffectEnabled();
      this.movementAllowed = !GtaLikeTeleportConfig.isPlayerFreezeEnabled();
      this.crossDimensionTravelEnabled = GtaLikeTeleportConfig.isCrossDimensionTravelEnabled();
      this.layoutDebugEnabled = GtaLikeTeleportConfig.isConfigLayoutEditorButtonVisible() && GtaLikeTeleportConfig.isConfigLayoutDebugEnabled();
      this.layoutAspectLocked = GtaLikeTeleportConfig.isConfigLayoutAspectLocked();
      this.layoutGridEnabled = GtaLikeTeleportConfig.isConfigLayoutGridEnabled();
      this.layoutSnapEnabled = GtaLikeTeleportConfig.isConfigLayoutSnapEnabled();
      this.zoomOutHeights = GtaLikeTeleportConfig.getZoomOutStageHeights(this.selectedZoomDimension);
      this.zoomInHeights = GtaLikeTeleportConfig.getRawZoomInStageHeights(this.selectedZoomDimension);
      this.zoomOutStageTicks = GtaLikeTeleportConfig.getZoomOutStageTicks();
      this.zoomInStageTicks = GtaLikeTeleportConfig.getZoomInStageTicks();
      this.zoomStageGlideHeight = GtaLikeTeleportConfig.getZoomStageGlideHeight();
      this.zoomStageGlideTicks = GtaLikeTeleportConfig.getZoomStageGlideTicks();
      this.bodyCameraHeight = GtaLikeTeleportConfig.getBodyCameraHeight();
      this.bodyGlideHeight = GtaLikeTeleportConfig.getBodyGlideHeight();
      this.bodyGlideTicks = GtaLikeTeleportConfig.getBodyGlideTicks();
      this.localPlayerHideTicks = GtaLikeTeleportConfig.getLocalPlayerHideTicks();
      this.customSoundsEnabled = GtaLikeTeleportConfig.isCustomSoundsEnabled();
      this.minecraftSoundVolume = GtaLikeTeleportConfig.getMinecraftSoundVolume();
      this.customSoundVolume = GtaLikeTeleportConfig.getCustomSoundVolume();
      this.warpPlateTransitionsEnabled = GtaLikeTeleportConfig.isWarpPlateTransitionsEnabled();
      this.externalTeleportTransitionsEnabled = GtaLikeTeleportConfig.isExternalTeleportTransitionsEnabled();
      this.fallbackChunkFadeEnabled = GtaLikeTeleportConfig.isFallbackChunkFadeEnabled();
   }

   private static GtaLikeTeleportConfig.ZoomDimension getInitialZoomDimension() {
      Minecraft client = Minecraft.getInstance();
      return GtaLikeTeleportConfig.ZoomDimension.fromLevel(client.level == null ? null : client.level.dimension());
   }

   private void loadZoomHeightState() {
      this.linked = GtaLikeTeleportConfig.areZoomHeightsLinked(this.selectedZoomDimension);
      this.zoomOutHeights = GtaLikeTeleportConfig.getZoomOutStageHeights(this.selectedZoomDimension);
      this.zoomInHeights = GtaLikeTeleportConfig.getRawZoomInStageHeights(this.selectedZoomDimension);
      if (this.linked) {
         this.zoomInHeights = (double[])this.zoomOutHeights.clone();
      }

   }

   protected void init() {
      if (this.sessionLayoutBaseWidth <= 0 || this.sessionLayoutBaseHeight <= 0) {
         this.sessionLayoutBaseWidth = this.width;
         this.sessionLayoutBaseHeight = this.height;
      }

      this.resetWidgetReferences();
      LayoutRect panel = this.getContentRect();
      int y = this.getFirstSliderY(panel);
      if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.ZOOM) {
         this.initZoomWidgets();
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.GENERAL) {
         this.initGeneralWidgets();
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.ZOOM_STAGE_2) {
         this.initAdvancedTwoWidgets();
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.SOUNDS) {
         this.initSoundWidgets();
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.OTHERS) {
         this.initOthersWidgets();
      }

      this.initPageNavigationWidgets();
      this.initPageTabWidgets();
      if (GtaLikeTeleportConfig.isConfigLayoutEditorButtonVisible()) {
         this.layoutDebugButton = (Button)this.addRenderableWidget(Button.builder(this.getLayoutDebugLabel(), (button) -> {
            this.toggleLayoutDebug();
         }).bounds(panel.x + panel.width - 94, panel.y + 4, 90, 20).tooltip(Tooltip.create(Component.translatable("gtalike_teleport.config.layout_debug.tooltip"))).build());
      }

      if (this.layoutDebugEnabled) {
         this.aspectButton = (Button)this.addRenderableWidget(Button.builder(this.getAspectLabel(), (button) -> {
            this.toggleAspectLock();
         }).bounds(panel.x + 4, panel.y + 4, 90, 20).tooltip(Tooltip.create(Component.translatable("gtalike_teleport.config.layout_aspect.tooltip"))).build());
         this.resetLayoutButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("gtalike_teleport.config.layout_reset"), (button) -> {
            this.resetLayout();
         }).bounds(panel.x + 98, panel.y + 4, 98, 20).build());
         this.resetItemSizeButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("gtalike_teleport.config.layout_size_reset"), (button) -> {
            this.resetSelectedItemSize();
         }).bounds(panel.x + 200, panel.y + 4, 88, 20).tooltip(Tooltip.create(Component.translatable("gtalike_teleport.config.layout_size_reset.tooltip"))).build());
         this.gridButton = (Button)this.addRenderableWidget(Button.builder(this.getGridLabel(), (button) -> {
            this.toggleGrid();
         }).bounds(panel.x + 292, panel.y + 4, 78, 20).tooltip(Tooltip.create(Component.translatable("gtalike_teleport.config.layout_grid.tooltip"))).build());
         this.snapButton = (Button)this.addRenderableWidget(Button.builder(this.getSnapLabel(), (button) -> {
            this.toggleSnap();
         }).bounds(panel.x + 282, panel.y + 4, 78, 20).tooltip(Tooltip.create(Component.translatable("gtalike_teleport.config.layout_snap.tooltip"))).build());
         this.textEditBox = (EditBox)this.addRenderableWidget(new EditBox(this.font, panel.x, y, 240, 20, Component.translatable("gtalike_teleport.config.text_editor")));
         this.textEditBox.setMaxLength(128);
         this.textEditBox.setResponder((value) -> {
            if (!this.updatingTextEditor && this.itemSupportsText(this.selectedLayoutItem)) {
               GtaLikeTeleportConfig.setConfigText(this.selectedLayoutItem, value);
               this.applyItemText(this.selectedLayoutItem);
            }

         });
         this.layoutValueEditBox = (EditBox)this.addRenderableWidget(new EditBox(this.font, panel.x, y + 24, 270, 20, Component.translatable("gtalike_teleport.config.layout_values")));
         this.layoutValueEditBox.setMaxLength(80);
         this.applyLayoutValueButton = (Button)this.addRenderableWidget(Button.builder(Component.translatable("gtalike_teleport.config.layout_values_apply"), (button) -> {
            this.applyLayoutValuesFromEditor();
         }).bounds(panel.x + 274, y + 24, 64, 20).tooltip(Tooltip.create(Component.translatable("gtalike_teleport.config.layout_values.tooltip"))).build());
      }

      LayoutRect resetRect = this.getItemRect("reset_button");
      this.resetButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.resetCurrentPage();
      }).bounds(resetRect.x, resetRect.y, resetRect.width, resetRect.height).build());
      LayoutRect doneRect = this.getItemRect("done_button");
      this.doneButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.onClose();
      }).bounds(doneRect.x, doneRect.y, doneRect.width, doneRect.height).build());
      this.repositionWidgets();
      this.refreshTextEditor();
      this.refreshLayoutValueEditor();
   }

   private void resetWidgetReferences() {
      this.zoomOutSlider = null;
      this.zoomInSlider = null;
      this.zoomStageGlideSlider = null;
      this.bodyHeightSlider = null;
      this.bodyGlideSlider = null;
      this.minecraftSoundVolumeSlider = null;
      this.customSoundVolumeSlider = null;
      this.zoomStageGlideTicksEditBox = null;
      this.zoomOutTicksEditBox = null;
      this.zoomInTicksEditBox = null;
      this.bodyGlideTicksEditBox = null;
      this.playerHideTicksEditBox = null;
      this.linkButton = null;
      this.prevPageButton = null;
      this.nextPageButton = null;
      this.pageTabButtons = new Button[0];
      this.effectToggleButton = null;
      this.movementToggleButton = null;
      this.crossDimensionTravelToggleButton = null;
      this.soundModeToggleButton = null;
      this.warpPlateToggleButton = null;
      this.externalTeleportToggleButton = null;
      this.fallbackChunkFadeToggleButton = null;
      this.resetButton = null;
      this.doneButton = null;
      this.layoutDebugButton = null;
      this.aspectButton = null;
      this.resetLayoutButton = null;
      this.resetItemSizeButton = null;
      this.gridButton = null;
      this.snapButton = null;
      this.textEditBox = null;
      this.layoutValueEditBox = null;
      this.applyLayoutValueButton = null;
   }

   private void initZoomWidgets() {
      LayoutRect outSliderRect = this.getItemRect("zoom_out_slider");
      this.zoomOutSlider = (StageHeightSlider)this.addRenderableWidget(new StageHeightSlider(outSliderRect.x, outSliderRect.y, outSliderRect.width, this.getItemComponent("zoom_out_slider"), this.zoomOutHeights, (values) -> {
         this.zoomOutHeights = values;
         if (this.linked) {
            this.zoomInHeights = (double[])values.clone();
            if (this.zoomInSlider != null) {
               this.zoomInSlider.setValues(this.zoomInHeights);
            }
         }

         this.saveHeights();
      }));
      if (this.linked) {
         this.zoomInHeights = (double[])this.zoomOutHeights.clone();
      }

      LayoutRect inSliderRect = this.getItemRect("zoom_in_slider");
      this.zoomInSlider = (StageHeightSlider)this.addRenderableWidget(new StageHeightSlider(inSliderRect.x, inSliderRect.y, inSliderRect.width, this.getItemComponent("zoom_in_slider"), this.zoomInHeights, (values) -> {
         if (!this.linked) {
            this.zoomInHeights = values;
            this.saveHeights();
         }

      }));
      this.zoomInSlider.setEditable(!this.linked);
      LayoutRect linkRect = this.getItemRect("link_button");
      this.linkButton = (LockIconButton)this.addRenderableWidget(new LockIconButton(linkRect.x, linkRect.y, (button) -> {
         this.toggleLinked();
      }));
      GtaLikeTeleportConfig.ZoomDimension var10003 = GtaLikeTeleportConfig.ZoomDimension.OVERWORLD;
      Item var10004 = Items.GRASS_BLOCK;
      Objects.requireNonNull(var10004);
      this.overworldDimensionButton = this.addDimensionButton("dimension_overworld", var10003, safeDefaultItemStack(var10004::getDefaultInstance), Component.literal("Overworld"), OVERWORLD_DIMENSION_ICON);
      var10003 = GtaLikeTeleportConfig.ZoomDimension.NETHER;
      var10004 = Items.NETHERRACK;
      Objects.requireNonNull(var10004);
      this.netherDimensionButton = this.addDimensionButton("dimension_nether", var10003, safeDefaultItemStack(var10004::getDefaultInstance), Component.literal("The Nether"), NETHER_DIMENSION_ICON);
      var10003 = GtaLikeTeleportConfig.ZoomDimension.END;
      var10004 = Items.END_STONE;
      Objects.requireNonNull(var10004);
      this.endDimensionButton = this.addDimensionButton("dimension_end", var10003, safeDefaultItemStack(var10004::getDefaultInstance), Component.literal("The End"), END_DIMENSION_ICON);
      this.updateLinkButton();
      this.updateDimensionButtons();
   }

   private static ItemStack safeDefaultItemStack(Supplier<ItemStack> stackFactory) {
      try {
         ItemStack stack = (ItemStack)stackFactory.get();
         return stack == null ? ItemStack.EMPTY : stack;
      } catch (LinkageError | RuntimeException var2) {
         return ItemStack.EMPTY;
      }
   }

   private DimensionIconButton addDimensionButton(String item, GtaLikeTeleportConfig.ZoomDimension dimension, ItemStack stack, Component label, Identifier texture) {
      LayoutRect rect = this.getItemRect(item);
      return (DimensionIconButton)this.addRenderableWidget(new DimensionIconButton(rect.x, rect.y, rect.width, rect.height, stack, label, texture, () -> {
         this.switchZoomDimension(dimension);
      }));
   }

   private void initGeneralWidgets() {
      LayoutRect effectRect = this.getItemRect("effect_toggle");
      this.effectToggleButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.toggleEffectEnabled();
      }).bounds(effectRect.x, effectRect.y, effectRect.width, effectRect.height).build());
      LayoutRect movementRect = this.getItemRect("movement_toggle");
      this.movementToggleButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.toggleMovementAllowed();
      }).bounds(movementRect.x, movementRect.y, movementRect.width, movementRect.height).build());
      LayoutRect crossDimensionRect = this.getItemRect("cross_dimension_travel_toggle");
      this.crossDimensionTravelToggleButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.toggleCrossDimensionTravel();
      }).bounds(crossDimensionRect.x, crossDimensionRect.y, crossDimensionRect.width, crossDimensionRect.height).build());
   }

   private void initOthersWidgets() {
      LayoutRect warpPlateRect = this.getItemRect("warp_plate_toggle");
      this.warpPlateToggleButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.toggleWarpPlateTransitions();
      }).bounds(warpPlateRect.x, warpPlateRect.y, warpPlateRect.width, warpPlateRect.height).build());
      LayoutRect externalRect = this.getItemRect("external_teleport_toggle");
      this.externalTeleportToggleButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.toggleExternalTeleportTransitions();
      }).bounds(externalRect.x, externalRect.y, externalRect.width, externalRect.height).build());
      this.updateOthersButtons();
   }

   private void initSoundWidgets() {
      LayoutRect modeRect = this.getItemRect("sound_mode_toggle");
      this.soundModeToggleButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.toggleCustomSoundsEnabled();
      }).bounds(modeRect.x, modeRect.y, modeRect.width, modeRect.height).build());
      LayoutRect minecraftVolumeRect = this.getItemRect("minecraft_volume_slider");
      this.minecraftSoundVolumeSlider = (SingleValueSlider)this.addRenderableWidget(new SingleValueSlider(minecraftVolumeRect.x, minecraftVolumeRect.y, minecraftVolumeRect.width, this.getItemComponent("minecraft_volume_slider"), this.minecraftSoundVolume, GtaLikeTeleportConfig.getMinSoundVolume(), GtaLikeTeleportConfig.getMaxSoundVolume(), 0.1, false, "x", (value) -> {
         this.minecraftSoundVolume = value;
         GtaLikeTeleportConfig.setMinecraftSoundVolume(value);
      }));
      LayoutRect customVolumeRect = this.getItemRect("custom_volume_slider");
      this.customSoundVolumeSlider = (SingleValueSlider)this.addRenderableWidget(new SingleValueSlider(customVolumeRect.x, customVolumeRect.y, customVolumeRect.width, this.getItemComponent("custom_volume_slider"), this.customSoundVolume, GtaLikeTeleportConfig.getMinSoundVolume(), GtaLikeTeleportConfig.getMaxSoundVolume(), 0.1, false, "x", (value) -> {
         this.customSoundVolume = value;
         GtaLikeTeleportConfig.setCustomSoundVolume(value);
      }));
      this.updateSoundButtons();
   }

   private void initAdvancedOneWidgets() {
      LayoutRect glideRect = this.getItemRect("zoom_stage_glide_slider");
      this.zoomStageGlideSlider = (SingleValueSlider)this.addRenderableWidget(new SingleValueSlider(glideRect.x, glideRect.y, glideRect.width, this.getItemComponent("zoom_stage_glide_slider"), this.zoomStageGlideHeight, GtaLikeTeleportConfig.getMinZoomStageGlideHeight(), GtaLikeTeleportConfig.getMaxZoomStageGlideHeight(), 0.1, false, " blocks", (value) -> {
         this.zoomStageGlideHeight = value;
         GtaLikeTeleportConfig.setZoomStageGlideHeight(value);
      }));
      LayoutRect ticksRect = this.getItemRect("zoom_stage_glide_ticks_field");
      this.zoomStageGlideTicksEditBox = this.addTickEditBox(ticksRect, this.zoomStageGlideTicks, (value) -> {
         this.zoomStageGlideTicks = value;
         GtaLikeTeleportConfig.setZoomStageGlideTicks(value);
      });
   }

   private void initAdvancedTwoWidgets() {
      LayoutRect outRect = this.getItemRect("zoom_out_ticks_field");
      this.zoomOutTicksEditBox = this.addStageTicksEditBox(outRect, this.zoomOutStageTicks, (values) -> {
         this.zoomOutStageTicks = values;
         this.saveStageTicks();
      });
      LayoutRect inRect = this.getItemRect("zoom_in_ticks_field");
      this.zoomInTicksEditBox = this.addStageTicksEditBox(inRect, this.zoomInStageTicks, (values) -> {
         this.zoomInStageTicks = values;
         this.saveStageTicks();
      });
   }

   private void initAdvancedThreeWidgets() {
      LayoutRect bodyRect = this.getItemRect("body_height_slider");
      this.bodyHeightSlider = (SingleValueSlider)this.addRenderableWidget(new SingleValueSlider(bodyRect.x, bodyRect.y, bodyRect.width, this.getItemComponent("body_height_slider"), this.bodyCameraHeight, GtaLikeTeleportConfig.getMinBodyCameraHeight(), GtaLikeTeleportConfig.getMaxBodyCameraHeight(), 0.1, false, " blocks", (value) -> {
         this.bodyCameraHeight = value;
         GtaLikeTeleportConfig.setBodyCameraHeight(value);
      }));
      LayoutRect glideRect = this.getItemRect("body_glide_slider");
      this.bodyGlideSlider = (SingleValueSlider)this.addRenderableWidget(new SingleValueSlider(glideRect.x, glideRect.y, glideRect.width, this.getItemComponent("body_glide_slider"), this.bodyGlideHeight, GtaLikeTeleportConfig.getMinBodyGlideHeight(), GtaLikeTeleportConfig.getMaxBodyGlideHeight(), 0.1, false, " blocks", (value) -> {
         this.bodyGlideHeight = value;
         GtaLikeTeleportConfig.setBodyGlideHeight(value);
      }));
      LayoutRect ticksRect = this.getItemRect("body_glide_ticks_field");
      this.bodyGlideTicksEditBox = this.addTickEditBox(ticksRect, this.bodyGlideTicks, (value) -> {
         this.bodyGlideTicks = value;
         GtaLikeTeleportConfig.setBodyGlideTicks(value);
      });
      LayoutRect hideRect = this.getItemRect("player_hide_ticks_field");
      this.playerHideTicksEditBox = this.addLocalPlayerHideTicksEditBox(hideRect, this.localPlayerHideTicks, (value) -> {
         this.localPlayerHideTicks = value;
         GtaLikeTeleportConfig.setLocalPlayerHideTicks(value);
      });
   }

   private EditBox addTickEditBox(LayoutRect rect, int value, Consumer<Integer> onChanged) {
      EditBox editBox = (EditBox)this.addRenderableWidget(new ScaledEditBox(this.font, rect.x, rect.y, rect.width, rect.height, Component.literal("ticks"), this.getContentScale()));
      editBox.setMaxLength(8);
      this.setEditBoxValue(editBox, Integer.toString(value));
      editBox.setResponder((text) -> {
         if (!this.updatingValueWidgets) {
            Integer parsed = this.parseSingleTick(text);
            if (parsed != null) {
               onChanged.accept(parsed);
            }

         }
      });
      return editBox;
   }

   private EditBox addLocalPlayerHideTicksEditBox(LayoutRect rect, int value, Consumer<Integer> onChanged) {
      EditBox editBox = (EditBox)this.addRenderableWidget(new ScaledEditBox(this.font, rect.x, rect.y, rect.width, rect.height, Component.literal("hide ticks"), this.getContentScale()));
      editBox.setMaxLength(4);
      this.setEditBoxValue(editBox, Integer.toString(value));
      editBox.setResponder((text) -> {
         if (!this.updatingValueWidgets) {
            Integer parsed = this.parseLocalPlayerHideTicks(text);
            if (parsed != null) {
               onChanged.accept(parsed);
            }

         }
      });
      return editBox;
   }

   private EditBox addStageTicksEditBox(LayoutRect rect, int[] values, Consumer<int[]> onChanged) {
      EditBox editBox = (EditBox)this.addRenderableWidget(new ScaledEditBox(this.font, rect.x, rect.y, rect.width, rect.height, Component.literal("stage ticks"), this.getContentScale()));
      editBox.setMaxLength(32);
      this.setEditBoxValue(editBox, this.formatStageTicks(values));
      editBox.setResponder((text) -> {
         if (!this.updatingValueWidgets) {
            int[] parsed = this.parseStageTicks(text);
            if (parsed != null) {
               onChanged.accept(parsed);
            }

         }
      });
      return editBox;
   }

   private void setEditBoxValue(EditBox editBox, String value) {
      if (editBox != null && !value.equals(editBox.getValue())) {
         this.updatingValueWidgets = true;
         editBox.setValue(value);
         this.updatingValueWidgets = false;
      }
   }

   private Integer parseSingleTick(String text) {
      if (text != null && !text.isBlank()) {
         try {
            return GtaLikeTeleportConfig.sanitizeStageTicksValue(Integer.parseInt(text.trim()));
         } catch (NumberFormatException var3) {
            return null;
         }
      } else {
         return null;
      }
   }

   private Integer parseLocalPlayerHideTicks(String text) {
      if (text != null && !text.isBlank()) {
         try {
            return GtaLikeTeleportConfig.sanitizeLocalPlayerHideTicks(Integer.parseInt(text.trim()));
         } catch (NumberFormatException var3) {
            return null;
         }
      } else {
         return null;
      }
   }

   private int[] parseStageTicks(String text) {
      if (text != null && !text.isBlank()) {
         String normalized = text.replace('/', ' ').replace(',', ' ').replace(';', ' ');
         String[] tokens = normalized.trim().split("\\s+");
         if (tokens.length != 3) {
            return null;
         } else {
            int[] values = new int[3];

            try {
               for(int i = 0; i < values.length; ++i) {
                  values[i] = Integer.parseInt(tokens[i]);
               }
            } catch (NumberFormatException var6) {
               return null;
            }

            return GtaLikeTeleportConfig.sanitizeStageTicks(values);
         }
      } else {
         return null;
      }
   }

   private String formatStageTicks(int[] values) {
      int[] sanitized = GtaLikeTeleportConfig.sanitizeStageTicks(values);
      return sanitized[0] + " / " + sanitized[1] + " / " + sanitized[2];
   }

   private void initPageNavigationWidgets() {
      LayoutRect prevRect = this.getItemRect("prev_page_button");
      this.prevPageButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.switchPage(-1);
      }).bounds(prevRect.x, prevRect.y, prevRect.width, prevRect.height).build());
      LayoutRect nextRect = this.getItemRect("next_page_button");
      this.nextPageButton = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (button) -> {
         this.switchPage(1);
      }).bounds(nextRect.x, nextRect.y, nextRect.width, nextRect.height).build());
   }

   private void initPageTabWidgets() {
      ConfigPage[] pages = GtaLikeTeleportConfigScreen.ConfigPage.values();
      this.pageTabButtons = new Button[pages.length];

      for(int i = 0; i < pages.length; ++i) {
         ConfigPage page = pages[i];
         LayoutRect rect = this.getItemRect(page.tabItem);
         Button button = (Button)this.addRenderableWidget(Button.builder(Component.empty(), (clicked) -> {
            this.switchToPage(page);
         }).bounds(rect.x, rect.y, rect.width, rect.height).build());
         button.active = page != this.currentPage;
         this.pageTabButtons[i] = button;
      }

   }

   public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickProgress) {
      this.extractBlurredBackground(context);
      context.fill(0, 0, this.width, this.height, -1728053248);
      LayoutRect outer = toOuterRect(this.getContentRect());
      context.fill(outer.x, outer.y, outer.right(), outer.bottom(), 1711276032);
      context.outline(outer.x, outer.y, outer.width, outer.height, this.layoutDebugEnabled ? -171 : 2013265919);
   }

   public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickProgress) {
      super.extractRenderState(context, mouseX, mouseY, tickProgress);
      double contentScale = this.getContentScale();
      Iterator var7 = this.getVisibleLayoutItems().iterator();

      String item;
      while(var7.hasNext()) {
         item = (String)var7.next();
         if (this.isManualTextItem(item)) {
            LayoutRect rect = this.getItemRect(item);
            int color = this.isMutedTextItem(item) ? -6250336 : -1;
            if (this.isCenteredTextItem(item)) {
               drawScaledCenteredText(context, this.font, this.getItemComponent(item), rect.x + rect.width / 2, rect.y, color, contentScale);
            } else {
               drawScaledText(context, this.font, this.getItemComponent(item), rect.x, rect.y, color, contentScale);
            }
         }
      }

      var7 = this.getVisibleLayoutItems().iterator();

      while(var7.hasNext()) {
         item = (String)var7.next();
         if (this.isManualButtonTextItem(item)) {
            this.drawButtonText(context, item);
         }
      }

      if (this.layoutDebugEnabled) {
         LayoutRect panel = this.getContentRect();
         if (this.layoutGridEnabled) {
            this.extractLayoutGuides(context);
         }

         context.text(this.font, Component.translatable("gtalike_teleport.config.layout_debug_hint"), panel.x, panel.y + panel.height - 58, -171);
         this.extractLayoutDebugOverlays(context);
      }

   }

   private static void drawScaledText(GuiGraphicsExtractor context, Font font, Component text, int x, int y, int color, double scale) {
      if (Math.abs(scale - 1.0) < 0.005) {
         context.text(font, text, x, y, color);
      } else {
         context.pose().pushMatrix();
         context.pose().translate((float)x, (float)y);
         context.pose().scale((float)scale);
         context.text(font, text, 0, 0, color);
         context.pose().popMatrix();
      }
   }

   private static void drawScaledCenteredText(GuiGraphicsExtractor context, Font font, Component text, int centerX, int y, int color, double scale) {
      if (Math.abs(scale - 1.0) < 0.005) {
         context.centeredText(font, text, centerX, y, color);
      } else {
         int x = centerX - (int)Math.round((double)font.width(text) * scale / 2.0);
         drawScaledText(context, font, text, x, y, color, scale);
      }
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      this.normalizeValueEditorsForClick(event.x(), event.y());
      if (this.layoutDebugEnabled && this.isDebugControlAt(event.x(), event.y())) {
         return super.mouseClicked(event, doubleClick);
      } else {
         if (this.layoutDebugEnabled) {
            LayoutEditAction resizeAction = this.getResizeAction(this.selectedLayoutItem, event.x(), event.y());
            if (resizeAction != GtaLikeTeleportConfigScreen.LayoutEditAction.NONE) {
               this.beginLayoutEdit(this.selectedLayoutItem, resizeAction, event.x(), event.y());
               return true;
            }

            String hitItem = this.findLayoutItemAt(event.x(), event.y());
            if (hitItem != null) {
               this.selectLayoutItem(hitItem);
               this.beginLayoutEdit(hitItem, GtaLikeTeleportConfigScreen.LayoutEditAction.MOVE, event.x(), event.y());
               return true;
            }
         }

         return super.mouseClicked(event, doubleClick);
      }
   }

   public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
      if (this.layoutEditAction != GtaLikeTeleportConfigScreen.LayoutEditAction.NONE) {
         this.updateLayoutEdit(event.x(), event.y());
         return true;
      } else {
         return super.mouseDragged(event, dragX, dragY);
      }
   }

   public boolean mouseReleased(MouseButtonEvent event) {
      if (this.layoutEditAction != GtaLikeTeleportConfigScreen.LayoutEditAction.NONE) {
         this.finishLayoutEdit();
         return true;
      } else {
         return super.mouseReleased(event);
      }
   }

   public void onClose() {
      this.normalizeValueEditors();
      if (this.minecraft != null) {
         this.minecraft.gui.setScreen(this.parent);
      }

   }

   private void switchPage(int direction) {
      ConfigPage[] pages = GtaLikeTeleportConfigScreen.ConfigPage.values();
      int nextIndex = Math.floorMod(this.currentPage.ordinal() + direction, pages.length);
      this.switchToPage(pages[nextIndex]);
   }

   private void switchToPage(ConfigPage nextPage) {
      if (nextPage != this.currentPage) {
         this.currentPage = nextPage;
         this.layoutEditAction = GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
         this.editingLayoutItem = null;
         this.editingRect = null;
         if (!this.isVisibleLayoutItem(this.selectedLayoutItem)) {
            this.selectedLayoutItem = "panel";
         }

         this.rebuildWidgets();
      }
   }

   private void normalizeValueEditorsForClick(double mouseX, double mouseY) {
      if (this.zoomStageGlideTicksEditBox != null && this.zoomStageGlideTicksEditBox.isFocused() && !this.zoomStageGlideTicksEditBox.isMouseOver(mouseX, mouseY)) {
         this.setEditBoxValue(this.zoomStageGlideTicksEditBox, Integer.toString(this.zoomStageGlideTicks));
      }

      if (this.zoomOutTicksEditBox != null && this.zoomOutTicksEditBox.isFocused() && !this.zoomOutTicksEditBox.isMouseOver(mouseX, mouseY)) {
         this.setEditBoxValue(this.zoomOutTicksEditBox, this.formatStageTicks(this.zoomOutStageTicks));
      }

      if (this.zoomInTicksEditBox != null && this.zoomInTicksEditBox.isFocused() && !this.zoomInTicksEditBox.isMouseOver(mouseX, mouseY)) {
         this.setEditBoxValue(this.zoomInTicksEditBox, this.formatStageTicks(this.zoomInStageTicks));
      }

      if (this.bodyGlideTicksEditBox != null && this.bodyGlideTicksEditBox.isFocused() && !this.bodyGlideTicksEditBox.isMouseOver(mouseX, mouseY)) {
         this.setEditBoxValue(this.bodyGlideTicksEditBox, Integer.toString(this.bodyGlideTicks));
      }

      if (this.playerHideTicksEditBox != null && this.playerHideTicksEditBox.isFocused() && !this.playerHideTicksEditBox.isMouseOver(mouseX, mouseY)) {
         this.setEditBoxValue(this.playerHideTicksEditBox, Integer.toString(this.localPlayerHideTicks));
      }

   }

   private void normalizeValueEditors() {
      this.setEditBoxValue(this.zoomStageGlideTicksEditBox, Integer.toString(this.zoomStageGlideTicks));
      this.setEditBoxValue(this.zoomOutTicksEditBox, this.formatStageTicks(this.zoomOutStageTicks));
      this.setEditBoxValue(this.zoomInTicksEditBox, this.formatStageTicks(this.zoomInStageTicks));
      this.setEditBoxValue(this.bodyGlideTicksEditBox, Integer.toString(this.bodyGlideTicks));
      this.setEditBoxValue(this.playerHideTicksEditBox, Integer.toString(this.localPlayerHideTicks));
   }

   private void toggleEffectEnabled() {
      this.effectEnabled = !this.effectEnabled;
      GtaLikeTeleportConfig.setEffectEnabled(this.effectEnabled);
      this.updateGeneralButtons();
   }

   private void toggleMovementAllowed() {
      this.movementAllowed = !this.movementAllowed;
      GtaLikeTeleportConfig.setPlayerFreezeEnabled(!this.movementAllowed);
      this.updateGeneralButtons();
   }

   private void toggleCrossDimensionTravel() {
      this.crossDimensionTravelEnabled = !this.crossDimensionTravelEnabled;
      GtaLikeTeleportConfig.setCrossDimensionTravelEnabled(this.crossDimensionTravelEnabled);
      this.updateGeneralButtons();
   }

   private void toggleWarpPlateTransitions() {
      this.warpPlateTransitionsEnabled = !this.warpPlateTransitionsEnabled;
      GtaLikeTeleportConfig.setWarpPlateTransitionsEnabled(this.warpPlateTransitionsEnabled);
      this.updateOthersButtons();
   }

   private void toggleExternalTeleportTransitions() {
      if (!this.isExternalTeleportToggleAvailable()) {
         this.updateOthersButtons();
      } else {
         this.externalTeleportTransitionsEnabled = !this.externalTeleportTransitionsEnabled;
         GtaLikeTeleportConfig.setExternalTeleportTransitionsEnabled(this.externalTeleportTransitionsEnabled);
         this.updateOthersButtons();
      }
   }

   private void toggleFallbackChunkFade() {
      this.fallbackChunkFadeEnabled = !this.fallbackChunkFadeEnabled;
      GtaLikeTeleportConfig.setFallbackChunkFadeEnabled(this.fallbackChunkFadeEnabled);
      this.updateOthersButtons();
   }

   private boolean isExternalTeleportToggleAvailable() {
      return GtaLikeTeleportClientNetworking.isServerSideTeleportAvailable();
   }

   private void updateOthersButtons() {
      if (this.warpPlateToggleButton != null) {
         this.warpPlateToggleButton.setMessage(Component.empty());
      }

      if (this.externalTeleportToggleButton != null) {
         this.externalTeleportToggleButton.active = this.isExternalTeleportToggleAvailable();
         this.externalTeleportToggleButton.setMessage(Component.empty());
      }

   }

   private void toggleCustomSoundsEnabled() {
      this.customSoundsEnabled = !this.customSoundsEnabled;
      GtaLikeTeleportConfig.setCustomSoundsEnabled(this.customSoundsEnabled);
      this.updateSoundButtons();
   }

   private void updateScaledEditBox(EditBox editBox) {
      if (editBox instanceof ScaledEditBox scaledEditBox) {
         scaledEditBox.setTextScale(this.getContentScale());
      }

   }

   private void updateSoundButtons() {
      if (this.soundModeToggleButton != null) {
         this.soundModeToggleButton.setMessage(Component.empty());
      }

   }

   private void updateGeneralButtons() {
      if (this.effectToggleButton != null) {
         this.effectToggleButton.setMessage(Component.empty());
      }

      if (this.movementToggleButton != null) {
         this.movementToggleButton.setMessage(Component.empty());
      }

      if (this.crossDimensionTravelToggleButton != null) {
         this.crossDimensionTravelToggleButton.setMessage(Component.empty());
      }

   }

   private void resetSoundSettings() {
      this.customSoundsEnabled = false;
      this.minecraftSoundVolume = GtaLikeTeleportConfig.getDefaultMinecraftSoundVolume();
      this.customSoundVolume = GtaLikeTeleportConfig.getDefaultCustomSoundVolume();
      GtaLikeTeleportConfig.setCustomSoundsEnabled(this.customSoundsEnabled);
      GtaLikeTeleportConfig.setMinecraftSoundVolume(this.minecraftSoundVolume);
      GtaLikeTeleportConfig.setCustomSoundVolume(this.customSoundVolume);
      this.rebuildWidgets();
   }

   private void resetCurrentPage() {
      if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.ZOOM) {
         this.resetHeights();
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.GENERAL) {
         this.effectEnabled = true;
         this.movementAllowed = false;
         this.crossDimensionTravelEnabled = false;
         GtaLikeTeleportConfig.setEffectEnabled(true);
         GtaLikeTeleportConfig.setPlayerFreezeEnabled(true);
         GtaLikeTeleportConfig.setCrossDimensionTravelEnabled(false);
         this.updateGeneralButtons();
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.ZOOM_STAGE_2) {
         this.zoomOutStageTicks = GtaLikeTeleportConfig.getDefaultStageTicks();
         this.zoomInStageTicks = GtaLikeTeleportConfig.getDefaultStageTicks();
         this.saveStageTicks();
         this.rebuildWidgets();
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.SOUNDS) {
         this.resetSoundSettings();
      } else {
         if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.OTHERS) {
            this.warpPlateTransitionsEnabled = true;
            this.externalTeleportTransitionsEnabled = true;
            this.fallbackChunkFadeEnabled = true;
            GtaLikeTeleportConfig.setWarpPlateTransitionsEnabled(true);
            GtaLikeTeleportConfig.setExternalTeleportTransitionsEnabled(true);
            GtaLikeTeleportConfig.setFallbackChunkFadeEnabled(true);
            this.updateOthersButtons();
         }

      }
   }

   private void toggleLinked() {
      this.linked = !this.linked;
      if (this.linked) {
         this.zoomInHeights = (double[])this.zoomOutHeights.clone();
      }

      if (!this.isVisibleLayoutItem(this.selectedLayoutItem)) {
         this.selectedLayoutItem = "panel";
      }

      this.saveHeights();
      this.rebuildWidgets();
   }

   private void toggleLayoutDebug() {
      this.layoutDebugEnabled = !this.layoutDebugEnabled;
      GtaLikeTeleportConfig.setConfigLayoutDebugEnabled(this.layoutDebugEnabled);
      this.layoutEditAction = GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
      this.editingLayoutItem = null;
      this.editingRect = null;
      if (this.selectedLayoutItem == null) {
         this.selectedLayoutItem = "panel";
      }

      this.rebuildWidgets();
   }

   private void toggleAspectLock() {
      this.layoutAspectLocked = !this.layoutAspectLocked;
      GtaLikeTeleportConfig.setConfigLayoutAspectLocked(this.layoutAspectLocked);
      if (this.aspectButton != null) {
         this.aspectButton.setMessage(this.getAspectLabel());
      }

   }

   private void toggleGrid() {
      this.layoutGridEnabled = !this.layoutGridEnabled;
      GtaLikeTeleportConfig.setConfigLayoutGridEnabled(this.layoutGridEnabled);
      if (this.gridButton != null) {
         this.gridButton.setMessage(this.getGridLabel());
      }

   }

   private void toggleSnap() {
      this.layoutSnapEnabled = !this.layoutSnapEnabled;
      GtaLikeTeleportConfig.setConfigLayoutSnapEnabled(this.layoutSnapEnabled);
      if (this.snapButton != null) {
         this.snapButton.setMessage(this.getSnapLabel());
      }

   }

   private void resetLayout() {
      GtaLikeTeleportConfig.resetConfigLayout();
      GtaLikeTeleportConfig.resetConfigWidgetLayouts();
      this.selectedLayoutItem = "panel";
      this.editingLayoutItem = null;
      this.editingRect = null;
      this.rebuildWidgets();
   }

   private void resetSelectedItemSize() {
      if (this.selectedLayoutItem != null && this.isVisibleLayoutItem(this.selectedLayoutItem)) {
         String item = this.selectedLayoutItem;
         LayoutRect current = this.getEditableRect(item);
         LayoutRect resetRect;
         LayoutRect defaults;
         if ("panel".equals(item)) {
            defaults = this.getDefaultContentRect();
            resetRect = this.constrainContentRect(new LayoutRect(current.centerX() - defaults.width / 2, current.centerY() - defaults.height / 2, defaults.width, defaults.height));
         } else {
            defaults = this.getDefaultItemRect(item, this.getContentRect());
            resetRect = this.constrainItemRect(item, new LayoutRect(current.x, current.y, defaults.width, defaults.height));
         }

         this.saveLayoutRect(item, resetRect);
         this.editingLayoutItem = null;
         this.editingRect = null;
         this.layoutEditAction = GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
         this.repositionWidgets();
         this.refreshLayoutValueEditor();
      }
   }

   private void applyLayoutValuesFromEditor() {
      if (this.layoutValueEditBox != null && this.selectedLayoutItem != null && this.isVisibleLayoutItem(this.selectedLayoutItem)) {
         double[] values = this.parseLayoutValues(this.layoutValueEditBox.getValue());
         if (values == null) {
            this.refreshLayoutValueEditor();
         } else {
            String item = this.selectedLayoutItem;
            LayoutRect rect;
            if ("panel".equals(item)) {
               rect = this.constrainContentRect(new LayoutRect((int)Math.round(values[0] * (double)this.width), (int)Math.round(values[1] * (double)this.height), (int)Math.round(values[2] * (double)this.width), (int)Math.round(values[3] * (double)this.height)));
            } else {
               LayoutRect panel = this.getContentRect();
               rect = this.constrainItemRect(item, new LayoutRect(panel.x + (int)Math.round(values[0] * (double)panel.width), panel.y + (int)Math.round(values[1] * (double)panel.height), (int)Math.round(values[2] * (double)panel.width), (int)Math.round(values[3] * (double)panel.height)));
            }

            this.saveLayoutRect(item, rect);
            this.editingLayoutItem = null;
            this.editingRect = null;
            this.layoutEditAction = GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
            this.repositionWidgets();
            this.refreshLayoutValueEditor();
         }
      }
   }

   private double[] parseLayoutValues(String text) {
      if (text != null && !text.isBlank()) {
         double[] values = new double[]{0.0 / 0.0, 0.0 / 0.0, 0.0 / 0.0, 0.0 / 0.0};
         String normalized = text.replace(',', ' ').replace(';', ' ');
         String[] tokens = normalized.trim().split("\\s+");
         int sequentialIndex = 0;
         String[] var6 = tokens;
         int var7 = tokens.length;

         int var8;
         for(var8 = 0; var8 < var7; ++var8) {
            String token = var6[var8];
            if (!token.isBlank()) {
               int equals = token.indexOf(61);

               try {
                  if (equals > 0) {
                     String name = token.substring(0, equals).trim().toLowerCase(Locale.ROOT);
                     double value = Double.parseDouble(token.substring(equals + 1).trim());
                     if ("x".equals(name)) {
                        values[0] = value;
                     } else if ("y".equals(name)) {
                        values[1] = value;
                     } else if (!"w".equals(name) && !"width".equals(name)) {
                        if ("h".equals(name) || "height".equals(name)) {
                           values[3] = value;
                        }
                     } else {
                        values[2] = value;
                     }
                  } else if (sequentialIndex < values.length) {
                     values[sequentialIndex] = Double.parseDouble(token.trim());
                     ++sequentialIndex;
                  }
               } catch (NumberFormatException var14) {
                  return null;
               }
            }
         }

         double[] var15 = values;
         var7 = values.length;

         for(var8 = 0; var8 < var7; ++var8) {
            double value = var15[var8];
            if (!Double.isFinite(value)) {
               return null;
            }
         }

         return values;
      } else {
         return null;
      }
   }

   private void saveLayoutRect(String item, LayoutRect rect) {
      if ("panel".equals(item)) {
         if (this.width > 0 && this.height > 0) {
            GtaLikeTeleportConfig.setConfigLayout((double)rect.x / (double)this.width, (double)rect.y / (double)this.height, (double)rect.width / (double)this.width, (double)rect.height / (double)this.height, this.width, this.height);
         }

      } else {
         LayoutRect panel = this.getContentRect();
         if (panel.width > 0 && panel.height > 0) {
            GtaLikeTeleportConfig.setConfigWidgetLayout(item, (double)(rect.x - panel.x) / (double)panel.width, (double)(rect.y - panel.y) / (double)panel.height, (double)rect.width / (double)panel.width, (double)rect.height / (double)panel.height, panel.width, panel.height);
         }

      }
   }

   private void resetHeights() {
      this.zoomOutHeights = GtaLikeTeleportConfig.getDefaultStageHeights();
      this.zoomInHeights = GtaLikeTeleportConfig.getDefaultStageHeights();
      this.saveHeights();
      this.rebuildWidgets();
   }

   private void saveHeights() {
      this.zoomOutHeights = GtaLikeTeleportConfig.sanitizeStageHeights(this.zoomOutHeights);
      this.zoomInHeights = GtaLikeTeleportConfig.sanitizeStageHeights(this.zoomInHeights);
      GtaLikeTeleportConfig.setZoomStageHeights(this.selectedZoomDimension, this.linked, this.zoomOutHeights, this.zoomInHeights);
   }

   private void switchZoomDimension(GtaLikeTeleportConfig.ZoomDimension dimension) {
      if (dimension != null && dimension != this.selectedZoomDimension) {
         this.selectedZoomDimension = dimension;
         this.loadZoomHeightState();
         this.layoutEditAction = GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
         this.editingLayoutItem = null;
         this.editingRect = null;
         if (!this.isVisibleLayoutItem(this.selectedLayoutItem)) {
            this.selectedLayoutItem = "panel";
         }

         this.rebuildWidgets();
      }
   }

   private void saveStageTicks() {
      this.zoomOutStageTicks = GtaLikeTeleportConfig.sanitizeStageTicks(this.zoomOutStageTicks);
      this.zoomInStageTicks = GtaLikeTeleportConfig.sanitizeStageTicks(this.zoomInStageTicks);
      GtaLikeTeleportConfig.setZoomStageTicks(this.zoomOutStageTicks, this.zoomInStageTicks);
   }

   private void updateLinkButton() {
      if (this.linkButton != null) {
         this.linkButton.setLocked(this.linked);
         this.linkButton.setTooltip(Tooltip.create(Component.translatable(this.linked ? "gtalike_teleport.config.linked.tooltip" : "gtalike_teleport.config.unlinked.tooltip")));
      }
   }

   private void repositionDimensionButton(DimensionIconButton button, String item, GtaLikeTeleportConfig.ZoomDimension dimension) {
      if (button != null) {
         LayoutRect rect = this.getItemRect(item);
         button.setRectangle(rect.width, rect.height, rect.x, rect.y);
         button.setSelected(this.selectedZoomDimension == dimension);
      }
   }

   private void updateDimensionButtons() {
      this.repositionDimensionButton(this.overworldDimensionButton, "dimension_overworld", GtaLikeTeleportConfig.ZoomDimension.OVERWORLD);
      this.repositionDimensionButton(this.netherDimensionButton, "dimension_nether", GtaLikeTeleportConfig.ZoomDimension.NETHER);
      this.repositionDimensionButton(this.endDimensionButton, "dimension_end", GtaLikeTeleportConfig.ZoomDimension.END);
   }

   private void repositionWidgets() {
      Iterator var1 = this.getVisibleLayoutItems().iterator();

      while(var1.hasNext()) {
         String item = (String)var1.next();
         this.applyItemText(item);
      }

      LayoutRect panel;
      if (this.zoomOutSlider != null) {
         panel = this.getItemRect("zoom_out_slider");
         this.zoomOutSlider.setRectangle(panel.width, panel.height, panel.x, panel.y);
      }

      if (this.zoomInSlider != null) {
         if (this.linked) {
            this.zoomInHeights = (double[])this.zoomOutHeights.clone();
            this.zoomInSlider.setValues(this.zoomInHeights);
         }

         this.zoomInSlider.setEditable(!this.linked);
         panel = this.getItemRect("zoom_in_slider");
         this.zoomInSlider.setRectangle(panel.width, panel.height, panel.x, panel.y);
      }

      if (this.linkButton != null) {
         panel = this.getItemRect("link_button");
         this.linkButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
      }

      this.repositionDimensionButton(this.overworldDimensionButton, "dimension_overworld", GtaLikeTeleportConfig.ZoomDimension.OVERWORLD);
      this.repositionDimensionButton(this.netherDimensionButton, "dimension_nether", GtaLikeTeleportConfig.ZoomDimension.NETHER);
      this.repositionDimensionButton(this.endDimensionButton, "dimension_end", GtaLikeTeleportConfig.ZoomDimension.END);
      if (this.effectToggleButton != null) {
         panel = this.getItemRect("effect_toggle");
         this.effectToggleButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.effectToggleButton.setMessage(Component.empty());
      }

      if (this.movementToggleButton != null) {
         panel = this.getItemRect("movement_toggle");
         this.movementToggleButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.movementToggleButton.setMessage(Component.empty());
      }

      if (this.crossDimensionTravelToggleButton != null) {
         panel = this.getItemRect("cross_dimension_travel_toggle");
         this.crossDimensionTravelToggleButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.crossDimensionTravelToggleButton.setMessage(Component.empty());
      }

      if (this.soundModeToggleButton != null) {
         panel = this.getItemRect("sound_mode_toggle");
         this.soundModeToggleButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.soundModeToggleButton.setMessage(Component.empty());
      }

      if (this.warpPlateToggleButton != null) {
         panel = this.getItemRect("warp_plate_toggle");
         this.warpPlateToggleButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.warpPlateToggleButton.setMessage(Component.empty());
      }

      if (this.externalTeleportToggleButton != null) {
         panel = this.getItemRect("external_teleport_toggle");
         this.externalTeleportToggleButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.externalTeleportToggleButton.active = this.isExternalTeleportToggleAvailable();
         this.externalTeleportToggleButton.setMessage(Component.empty());
      }

      if (this.minecraftSoundVolumeSlider != null) {
         panel = this.getItemRect("minecraft_volume_slider");
         this.minecraftSoundVolumeSlider.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.minecraftSoundVolumeSlider.setValue(this.minecraftSoundVolume);
      }

      if (this.customSoundVolumeSlider != null) {
         panel = this.getItemRect("custom_volume_slider");
         this.customSoundVolumeSlider.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.customSoundVolumeSlider.setValue(this.customSoundVolume);
      }

      if (this.zoomStageGlideSlider != null) {
         panel = this.getItemRect("zoom_stage_glide_slider");
         this.zoomStageGlideSlider.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.zoomStageGlideSlider.setValue(this.zoomStageGlideHeight);
      }

      if (this.zoomStageGlideTicksEditBox != null) {
         panel = this.getItemRect("zoom_stage_glide_ticks_field");
         this.zoomStageGlideTicksEditBox.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.updateScaledEditBox(this.zoomStageGlideTicksEditBox);
         this.zoomStageGlideTicksEditBox.setEditable(!this.layoutDebugEnabled);
         if (!this.zoomStageGlideTicksEditBox.isFocused()) {
            this.setEditBoxValue(this.zoomStageGlideTicksEditBox, Integer.toString(this.zoomStageGlideTicks));
         }
      }

      if (this.zoomOutTicksEditBox != null) {
         panel = this.getItemRect("zoom_out_ticks_field");
         this.zoomOutTicksEditBox.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.updateScaledEditBox(this.zoomOutTicksEditBox);
         this.zoomOutTicksEditBox.setEditable(!this.layoutDebugEnabled);
         if (!this.zoomOutTicksEditBox.isFocused()) {
            this.setEditBoxValue(this.zoomOutTicksEditBox, this.formatStageTicks(this.zoomOutStageTicks));
         }
      }

      if (this.zoomInTicksEditBox != null) {
         panel = this.getItemRect("zoom_in_ticks_field");
         this.zoomInTicksEditBox.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.updateScaledEditBox(this.zoomInTicksEditBox);
         this.zoomInTicksEditBox.setEditable(!this.layoutDebugEnabled);
         if (!this.zoomInTicksEditBox.isFocused()) {
            this.setEditBoxValue(this.zoomInTicksEditBox, this.formatStageTicks(this.zoomInStageTicks));
         }
      }

      if (this.bodyHeightSlider != null) {
         panel = this.getItemRect("body_height_slider");
         this.bodyHeightSlider.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.bodyHeightSlider.setValue(this.bodyCameraHeight);
      }

      if (this.bodyGlideSlider != null) {
         panel = this.getItemRect("body_glide_slider");
         this.bodyGlideSlider.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.bodyGlideSlider.setValue(this.bodyGlideHeight);
      }

      if (this.bodyGlideTicksEditBox != null) {
         panel = this.getItemRect("body_glide_ticks_field");
         this.bodyGlideTicksEditBox.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.updateScaledEditBox(this.bodyGlideTicksEditBox);
         this.bodyGlideTicksEditBox.setEditable(!this.layoutDebugEnabled);
         if (!this.bodyGlideTicksEditBox.isFocused()) {
            this.setEditBoxValue(this.bodyGlideTicksEditBox, Integer.toString(this.bodyGlideTicks));
         }
      }

      if (this.playerHideTicksEditBox != null) {
         panel = this.getItemRect("player_hide_ticks_field");
         this.playerHideTicksEditBox.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.updateScaledEditBox(this.playerHideTicksEditBox);
         this.playerHideTicksEditBox.setEditable(!this.layoutDebugEnabled);
         if (!this.playerHideTicksEditBox.isFocused()) {
            this.setEditBoxValue(this.playerHideTicksEditBox, Integer.toString(this.localPlayerHideTicks));
         }
      }

      if (this.prevPageButton != null) {
         panel = this.getItemRect("prev_page_button");
         this.prevPageButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.prevPageButton.setMessage(Component.empty());
      }

      if (this.nextPageButton != null) {
         panel = this.getItemRect("next_page_button");
         this.nextPageButton.setRectangle(panel.width, panel.height, panel.x, panel.y);
         this.nextPageButton.setMessage(Component.empty());
      }

      this.repositionPageTabs();
      panel = this.getContentRect();
      if (this.layoutDebugButton != null) {
         int buttonWidth = 90;
         this.layoutDebugButton.setRectangle(buttonWidth, 20, Math.max(6, this.width - buttonWidth - 6), 6);
         this.layoutDebugButton.setMessage(this.getLayoutDebugLabel());
      }

      ToolbarCursor cursor = new ToolbarCursor(6, 6);
      int rightLimit = this.layoutDebugButton == null ? this.width - 6 : this.layoutDebugButton.getX() - 6;
      if (this.aspectButton != null) {
         cursor = this.placeToolbarButton(this.aspectButton, this.getAspectLabel(), cursor, 82, rightLimit);
      }

      if (this.resetLayoutButton != null) {
         cursor = this.placeToolbarButton(this.resetLayoutButton, Component.translatable("gtalike_teleport.config.layout_reset"), cursor, 86, rightLimit);
      }

      if (this.resetItemSizeButton != null) {
         cursor = this.placeToolbarButton(this.resetItemSizeButton, Component.translatable("gtalike_teleport.config.layout_size_reset"), cursor, 88, rightLimit);
      }

      if (this.gridButton != null) {
         cursor = this.placeToolbarButton(this.gridButton, this.getGridLabel(), cursor, 78, rightLimit);
      }

      if (this.snapButton != null) {
         cursor = this.placeToolbarButton(this.snapButton, this.getSnapLabel(), cursor, 78, rightLimit);
      }

      this.repositionLayoutValueEditor(cursor, rightLimit);
      LayoutRect rect;
      if (this.resetButton != null) {
         rect = this.getItemRect("reset_button");
         this.resetButton.setRectangle(rect.width, rect.height, rect.x, rect.y);
      }

      if (this.doneButton != null) {
         rect = this.getItemRect("done_button");
         this.doneButton.setRectangle(rect.width, rect.height, rect.x, rect.y);
      }

      this.repositionTextEditor();
   }

   private void repositionPageTabs() {
      if (this.pageTabButtons != null && this.pageTabButtons.length != 0) {
         ConfigPage[] pages = GtaLikeTeleportConfigScreen.ConfigPage.values();

         for(int i = 0; i < this.pageTabButtons.length && i < pages.length; ++i) {
            Button button = this.pageTabButtons[i];
            if (button != null) {
               LayoutRect rect = this.getItemRect(pages[i].tabItem);
               button.setRectangle(rect.width, rect.height, rect.x, rect.y);
               button.setMessage(Component.empty());
               button.active = pages[i] != this.currentPage;
            }
         }

      }
   }

   private LayoutRect[] getPageTabRects() {
      ConfigPage[] pages = GtaLikeTeleportConfigScreen.ConfigPage.values();
      LayoutRect[] rects = new LayoutRect[pages.length];
      LayoutRect outer = toOuterRect(this.getContentRect());
      int totalWidth = Math.max(pages.length, outer.width);
      double scale = this.getContentScale();
      int tabHeight = Math.max(16, (int)Math.round(20.0 * scale));
      int y = Math.max(2, outer.y - tabHeight - Math.max(2, (int)Math.round(2.0 * scale)));
      int baseWidth = Math.max(24, totalWidth / pages.length);
      int remainder = Math.max(0, totalWidth - baseWidth * pages.length);
      int x = outer.x;

      for(int i = 0; i < pages.length; ++i) {
         int width = baseWidth + (i < remainder ? 1 : 0);
         rects[i] = new LayoutRect(x, y, width, tabHeight);
         x += width;
      }

      return rects;
   }

   private LayoutRect getDefaultPageTabRect(String item) {
      ConfigPage[] pages = GtaLikeTeleportConfigScreen.ConfigPage.values();
      LayoutRect[] rects = this.getPageTabRects();

      for(int i = 0; i < pages.length && i < rects.length; ++i) {
         if (pages[i].tabItem.equals(item)) {
            return rects[i];
         }
      }

      return new LayoutRect(0, 0, 24, 20);
   }

   private boolean isPageTabItem(String item) {
      ConfigPage[] var2 = GtaLikeTeleportConfigScreen.ConfigPage.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ConfigPage page = var2[var4];
         if (page.tabItem.equals(item)) {
            return true;
         }
      }

      return false;
   }

   private ToolbarCursor placeToolbarButton(Button button, Component label, ToolbarCursor cursor, int width, int rightLimit) {
      int clampedRightLimit = Math.max(width + 6, rightLimit);
      int buttonX = cursor.x;
      int buttonY = cursor.y;
      if (buttonX + width > clampedRightLimit) {
         buttonX = 6;
         buttonY += 22;
      }

      button.setRectangle(width, 20, buttonX, buttonY);
      button.setMessage(label);
      return new ToolbarCursor(buttonX + width + 6, buttonY);
   }

   private void beginLayoutEdit(String item, LayoutEditAction action, double mouseX, double mouseY) {
      this.selectedLayoutItem = item;
      this.editingLayoutItem = item;
      this.layoutEditAction = action;
      this.editStartRect = this.getEditableRect(item);
      this.editingRect = this.editStartRect;
      this.editStartMouseX = mouseX;
      this.editStartMouseY = mouseY;
      this.refreshTextEditor();
      this.refreshLayoutValueEditor();
   }

   private void updateLayoutEdit(double mouseX, double mouseY) {
      if (this.editStartRect != null && this.editingLayoutItem != null) {
         int dx = (int)Math.round(mouseX - this.editStartMouseX);
         int dy = (int)Math.round(mouseY - this.editStartMouseY);
         int x = this.editStartRect.x;
         int y = this.editStartRect.y;
         int itemWidth = this.editStartRect.width;
         int itemHeight = this.editStartRect.height;
         if (this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.MOVE) {
            x += dx;
            y += dy;
         } else if (this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_RIGHT) {
            itemWidth += dx;
         } else if (this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_BOTTOM) {
            itemHeight += dy;
         } else if (this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_CORNER) {
            itemWidth += dx;
            itemHeight += dy;
            if (this.layoutAspectLocked && this.editStartRect.height > 0) {
               double aspect = (double)this.editStartRect.width / (double)this.editStartRect.height;
               if (Math.abs(dx) >= Math.abs(dy)) {
                  itemHeight = (int)Math.round((double)itemWidth / aspect);
               } else {
                  itemWidth = (int)Math.round((double)itemHeight * aspect);
               }
            }
         }

         LayoutRect raw = new LayoutRect(x, y, itemWidth, itemHeight);
         LayoutRect constrained = "panel".equals(this.editingLayoutItem) ? this.constrainContentRect(raw) : this.constrainItemRect(this.editingLayoutItem, raw);
         this.editingRect = this.applyLayoutSnap(this.editingLayoutItem, constrained);
         this.repositionWidgets();
         this.refreshLayoutValueEditor();
      }
   }

   private void finishLayoutEdit() {
      if (this.editingLayoutItem != null) {
         this.saveLayoutRect(this.editingLayoutItem, this.getEditableRect(this.editingLayoutItem));
      }

      this.layoutEditAction = GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
      this.editingLayoutItem = null;
      this.editStartRect = null;
      this.editingRect = null;
      this.repositionWidgets();
      this.refreshLayoutValueEditor();
   }

   private LayoutRect applyLayoutSnap(String item, LayoutRect rect) {
      if (this.layoutSnapEnabled && item != null && this.layoutEditAction != GtaLikeTeleportConfigScreen.LayoutEditAction.NONE) {
         List<Integer> horizontalTargets = this.getHorizontalSnapTargets(item);
         List<Integer> verticalTargets = this.getVerticalSnapTargets(item);
         LayoutRect snapped;
         int x;
         int y;
         if (this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.MOVE) {
            x = this.findSnapOffset(new int[]{rect.x, rect.centerX(), rect.right()}, horizontalTargets);
            y = this.findSnapOffset(new int[]{rect.y, rect.centerY(), rect.bottom()}, verticalTargets);
            snapped = new LayoutRect(rect.x + x, rect.y + y, rect.width, rect.height);
         } else {
            x = rect.x;
            y = rect.y;
            int itemWidth = rect.width;
            int itemHeight = rect.height;
            int bottomOffset;
            int heightOffset;
            if (this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_RIGHT || this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_CORNER) {
               bottomOffset = this.findSnapOffset(new int[]{rect.right()}, horizontalTargets);
               heightOffset = this.findSnapOffset(new int[]{rect.width}, this.getWidthSnapTargets(item));
               itemWidth = rect.width + this.chooseSnapOffset(bottomOffset, heightOffset);
            }

            if (this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_BOTTOM || this.layoutEditAction == GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_CORNER) {
               bottomOffset = this.findSnapOffset(new int[]{rect.bottom()}, verticalTargets);
               heightOffset = this.findSnapOffset(new int[]{rect.height}, this.getHeightSnapTargets(item));
               itemHeight = rect.height + this.chooseSnapOffset(bottomOffset, heightOffset);
            }

            snapped = new LayoutRect(x, y, itemWidth, itemHeight);
         }

         return "panel".equals(item) ? this.constrainContentRect(snapped) : this.constrainItemRect(item, snapped);
      } else {
         return rect;
      }
   }

   private int chooseSnapOffset(int edgeOffset, int sizeOffset) {
      if (edgeOffset == 0) {
         return sizeOffset;
      } else if (sizeOffset == 0) {
         return edgeOffset;
      } else {
         return Math.abs(sizeOffset) < Math.abs(edgeOffset) ? sizeOffset : edgeOffset;
      }
   }

   private List<Integer> getWidthSnapTargets(String item) {
      List<Integer> targets = new ArrayList();
      LayoutRect panel = this.getContentRect();
      targets.add(panel.width);
      targets.add(Math.max(1, panel.width / 2));
      Iterator var4 = this.getVisibleLayoutItems().iterator();

      while(var4.hasNext()) {
         String visibleItem = (String)var4.next();
         if (!visibleItem.equals(item) && !"panel".equals(visibleItem)) {
            LayoutRect rect = this.getSelectableRect(visibleItem);
            targets.add(rect.width);
         }
      }

      return targets;
   }

   private List<Integer> getHeightSnapTargets(String item) {
      List<Integer> targets = new ArrayList();
      LayoutRect panel = this.getContentRect();
      targets.add(panel.height);
      targets.add(Math.max(1, panel.height / 2));
      Iterator var4 = this.getVisibleLayoutItems().iterator();

      while(var4.hasNext()) {
         String visibleItem = (String)var4.next();
         if (!visibleItem.equals(item) && !"panel".equals(visibleItem)) {
            LayoutRect rect = this.getSelectableRect(visibleItem);
            targets.add(rect.height);
         }
      }

      return targets;
   }

   private List<Integer> getHorizontalSnapTargets(String item) {
      List<Integer> targets = new ArrayList();
      targets.add(0);
      targets.add(this.width / 2);
      targets.add(this.width);
      if (!"panel".equals(item)) {
         LayoutRect panel = this.getContentRect();
         addHorizontalTargets(targets, panel);
         Iterator var4 = this.getVisibleLayoutItems().iterator();

         while(var4.hasNext()) {
            String visibleItem = (String)var4.next();
            if (!"panel".equals(visibleItem) && !visibleItem.equals(item)) {
               addHorizontalTargets(targets, this.getSelectableRect(visibleItem));
            }
         }
      }

      return targets;
   }

   private List<Integer> getVerticalSnapTargets(String item) {
      List<Integer> targets = new ArrayList();
      targets.add(0);
      targets.add(this.height / 2);
      targets.add(this.height);
      if (!"panel".equals(item)) {
         LayoutRect panel = this.getContentRect();
         addVerticalTargets(targets, panel);
         Iterator var4 = this.getVisibleLayoutItems().iterator();

         while(var4.hasNext()) {
            String visibleItem = (String)var4.next();
            if (!"panel".equals(visibleItem) && !visibleItem.equals(item)) {
               addVerticalTargets(targets, this.getSelectableRect(visibleItem));
            }
         }
      }

      return targets;
   }

   private static void addHorizontalTargets(List<Integer> targets, LayoutRect rect) {
      targets.add(rect.x);
      targets.add(rect.centerX());
      targets.add(rect.right());
   }

   private static void addVerticalTargets(List<Integer> targets, LayoutRect rect) {
      targets.add(rect.y);
      targets.add(rect.centerY());
      targets.add(rect.bottom());
   }

   private int findSnapOffset(int[] anchors, List<Integer> targets) {
      int bestDistance = 6;
      int bestOffset = 0;
      int[] var5 = anchors;
      int var6 = anchors.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         int anchor = var5[var7];
         Iterator var9 = targets.iterator();

         int distance;
         while(var9.hasNext()) {
            int target = (Integer)var9.next();
            distance = Math.abs(target - anchor);
            if (distance < bestDistance) {
               bestDistance = distance;
               bestOffset = target - anchor;
            }
         }

         if (this.layoutGridEnabled) {
            int target = roundToGrid(anchor, 8);
            distance = Math.abs(target - anchor);
            if (distance < bestDistance) {
               bestDistance = distance;
               bestOffset = target - anchor;
            }
         }
      }

      return bestDistance <= 5 ? bestOffset : 0;
   }

   private static int roundToGrid(int value, int grid) {
      return Math.round((float)value / (float)grid) * grid;
   }

   private LayoutEditAction getResizeAction(String item, double mouseX, double mouseY) {
      if (item != null && this.isVisibleLayoutItem(item)) {
         LayoutRect rect = this.getSelectableRect(item);
         boolean nearRight = Math.abs(mouseX - (double)rect.right()) <= 10.0 && mouseY >= (double)rect.y && mouseY <= (double)rect.bottom();
         boolean nearBottom = Math.abs(mouseY - (double)rect.bottom()) <= 10.0 && mouseX >= (double)rect.x && mouseX <= (double)rect.right();
         boolean nearCorner = mouseX >= (double)(rect.right() - 20) && mouseX <= (double)(rect.right() + 10) && mouseY >= (double)(rect.bottom() - 20) && mouseY <= (double)(rect.bottom() + 10);
         if (nearCorner) {
            return GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_CORNER;
         } else if (nearRight) {
            return GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_RIGHT;
         } else {
            return nearBottom ? GtaLikeTeleportConfigScreen.LayoutEditAction.RESIZE_BOTTOM : GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
         }
      } else {
         return GtaLikeTeleportConfigScreen.LayoutEditAction.NONE;
      }
   }

   private void extractLayoutGuides(GuiGraphicsExtractor context) {
      LayoutRect panel = this.getContentRect();
      int startX = panel.x + Math.floorMod(-panel.x, 8);

      int startY;
      for(startY = startX; startY <= panel.right(); startY += 8) {
         drawVerticalGuide(context, startY, panel.y, panel.bottom(), 587202559);
      }

      startY = panel.y + Math.floorMod(-panel.y, 8);

      for(int y = startY; y <= panel.bottom(); y += 8) {
         drawHorizontalGuide(context, y, panel.x, panel.right(), 587202559);
      }

      drawVerticalGuide(context, this.width / 2, 0, this.height, 2007682303);
      drawHorizontalGuide(context, this.height / 2, 0, this.width, 2007682303);
      drawVerticalGuide(context, panel.centerX(), panel.y, panel.bottom(), 1728053247);
      drawHorizontalGuide(context, panel.centerY(), panel.x, panel.right(), 1728053247);
      if (this.selectedLayoutItem != null && this.isVisibleLayoutItem(this.selectedLayoutItem)) {
         LayoutRect selected = this.getSelectableRect(this.selectedLayoutItem);
         drawVerticalGuide(context, selected.centerX(), panel.y, panel.bottom(), -1996488875);
         drawHorizontalGuide(context, selected.centerY(), panel.x, panel.right(), -1996488875);
      }

   }

   private static void drawVerticalGuide(GuiGraphicsExtractor context, int x, int y1, int y2, int color) {
      context.fill(x, y1, x + 1, y2, color);
   }

   private static void drawHorizontalGuide(GuiGraphicsExtractor context, int y, int x1, int x2, int color) {
      context.fill(x1, y, x2, y + 1, color);
   }

   private void extractLayoutDebugOverlays(GuiGraphicsExtractor context) {
      Iterator var2 = this.getVisibleLayoutItems().iterator();

      while(var2.hasNext()) {
         String item = (String)var2.next();
         LayoutRect rect = this.getSelectableRect(item);
         boolean selected = item.equals(this.selectedLayoutItem);
         int color = selected ? -171 : -1996488875;
         context.outline(rect.x, rect.y, rect.width, rect.height, color);
      }

      if (this.selectedLayoutItem != null && this.isVisibleLayoutItem(this.selectedLayoutItem)) {
         LayoutRect rect = this.getSelectableRect(this.selectedLayoutItem);
         context.fill(rect.right() - 2, rect.y, rect.right() + 2, rect.bottom(), -855638187);
         context.fill(rect.x, rect.bottom() - 2, rect.right(), rect.bottom() + 2, -855638187);
         context.fill(rect.right() - 9, rect.bottom() - 9, rect.right() + 2, rect.bottom() + 2, -570425515);
         context.outline(rect.right() - 9, rect.bottom() - 9, 11, 11, -1);
      }

   }

   private String findLayoutItemAt(double mouseX, double mouseY) {
      List<String> items = this.getVisibleLayoutItems();

      for(int i = items.size() - 1; i >= 0; --i) {
         String item = (String)items.get(i);
         if (this.getSelectableRect(item).contains(mouseX, mouseY)) {
            return item;
         }
      }

      return null;
   }

   private void selectLayoutItem(String item) {
      if (item != null && !item.equals(this.selectedLayoutItem)) {
         this.selectedLayoutItem = item;
         this.refreshTextEditor();
         this.refreshLayoutValueEditor();
         this.repositionWidgets();
      }

   }

   private boolean isDebugControlAt(double mouseX, double mouseY) {
      return isWidgetAt(this.layoutDebugButton, mouseX, mouseY) || isWidgetAt(this.aspectButton, mouseX, mouseY) || isWidgetAt(this.resetLayoutButton, mouseX, mouseY) || isWidgetAt(this.resetItemSizeButton, mouseX, mouseY) || isWidgetAt(this.gridButton, mouseX, mouseY) || isWidgetAt(this.snapButton, mouseX, mouseY) || isWidgetAt(this.prevPageButton, mouseX, mouseY) || isWidgetAt(this.nextPageButton, mouseX, mouseY) || this.layoutValueEditorVisible && isWidgetAt(this.layoutValueEditBox, mouseX, mouseY) || this.layoutValueEditorVisible && isWidgetAt(this.applyLayoutValueButton, mouseX, mouseY) || this.textEditorVisible && isWidgetAt(this.textEditBox, mouseX, mouseY);
   }

   private boolean isPageTabAt(double mouseX, double mouseY) {
      if (this.pageTabButtons == null) {
         return false;
      } else {
         Button[] var5 = this.pageTabButtons;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Button button = var5[var7];
            if (isWidgetAt(button, mouseX, mouseY)) {
               return true;
            }
         }

         return false;
      }
   }

   private static boolean isWidgetAt(AbstractWidget widget, double mouseX, double mouseY) {
      return widget != null && widget.isMouseOver(mouseX, mouseY);
   }

   private void refreshLayoutValueEditor() {
      if (this.layoutValueEditBox != null) {
         boolean visible = this.layoutDebugEnabled && this.selectedLayoutItem != null && this.isVisibleLayoutItem(this.selectedLayoutItem);
         this.layoutValueEditorVisible = visible;
         this.layoutValueEditBox.setVisible(visible);
         this.layoutValueEditBox.setEditable(visible);
         if (!visible) {
            this.layoutValueEditBox.setFocused(false);
            if (this.applyLayoutValueButton != null) {
               this.applyLayoutValueButton.setRectangle(1, 1, -1000, -1000);
            }

         } else {
            this.updatingLayoutValueEditor = true;
            this.layoutValueEditBox.setValue(this.formatCurrentLayoutValues());
            this.updatingLayoutValueEditor = false;
         }
      }
   }

   private void repositionLayoutValueEditor(ToolbarCursor cursor, int rightLimit) {
      if (this.layoutValueEditBox != null) {
         boolean visible = this.layoutDebugEnabled && this.selectedLayoutItem != null && this.isVisibleLayoutItem(this.selectedLayoutItem);
         this.layoutValueEditorVisible = visible;
         this.layoutValueEditBox.setVisible(visible);
         this.layoutValueEditBox.setEditable(visible);
         if (!visible) {
            if (this.applyLayoutValueButton != null) {
               this.applyLayoutValueButton.setRectangle(1, 1, -1000, -1000);
            }

         } else {
            int editorX = 6;
            int editorY = cursor.y + 22;
            int applyWidth = 64;
            int availableWidth = Math.max(150, rightLimit - editorX);
            int editorWidth = Math.min(300, Math.max(140, availableWidth - applyWidth - 6));
            if (editorX + editorWidth + 6 + applyWidth > rightLimit) {
               editorWidth = Math.min(300, Math.max(140, availableWidth));
               this.layoutValueEditBox.setRectangle(editorWidth, 20, editorX, editorY);
               if (this.applyLayoutValueButton != null) {
                  this.applyLayoutValueButton.setRectangle(applyWidth, 20, editorX, editorY + 22);
               }

            } else {
               this.layoutValueEditBox.setRectangle(editorWidth, 20, editorX, editorY);
               if (this.applyLayoutValueButton != null) {
                  this.applyLayoutValueButton.setRectangle(applyWidth, 20, editorX + editorWidth + 6, editorY);
               }

            }
         }
      }
   }

   private String formatCurrentLayoutValues() {
      if (this.selectedLayoutItem != null && this.isVisibleLayoutItem(this.selectedLayoutItem)) {
         LayoutRect rect = this.getEditableRect(this.selectedLayoutItem);
         double x;
         double y;
         double w;
         double h;
         if ("panel".equals(this.selectedLayoutItem)) {
            x = this.width <= 0 ? 0.0 : (double)rect.x / (double)this.width;
            y = this.height <= 0 ? 0.0 : (double)rect.y / (double)this.height;
            w = this.width <= 0 ? 0.0 : (double)rect.width / (double)this.width;
            h = this.height <= 0 ? 0.0 : (double)rect.height / (double)this.height;
         } else {
            LayoutRect panel = this.getContentRect();
            x = panel.width <= 0 ? 0.0 : (double)(rect.x - panel.x) / (double)panel.width;
            y = panel.height <= 0 ? 0.0 : (double)(rect.y - panel.y) / (double)panel.height;
            w = panel.width <= 0 ? 0.0 : (double)rect.width / (double)panel.width;
            h = panel.height <= 0 ? 0.0 : (double)rect.height / (double)panel.height;
         }

         return String.format(Locale.ROOT, "x=%.3f y=%.3f w=%.3f h=%.3f", x, y, w, h);
      } else {
         return "x=0 y=0 w=0 h=0";
      }
   }

   private void refreshTextEditor() {
      if (this.textEditBox != null) {
         boolean visible = this.layoutDebugEnabled && this.itemSupportsText(this.selectedLayoutItem) && this.isVisibleLayoutItem(this.selectedLayoutItem);
         this.textEditorVisible = visible;
         this.textEditBox.setVisible(visible);
         this.textEditBox.setEditable(visible);
         if (!visible) {
            this.textEditBox.setFocused(false);
         } else {
            this.updatingTextEditor = true;
            this.textEditBox.setValue(this.getItemText(this.selectedLayoutItem));
            this.updatingTextEditor = false;
            this.repositionTextEditor();
         }
      }
   }

   private void repositionTextEditor() {
      if (this.textEditBox != null) {
         boolean visible = this.layoutDebugEnabled && this.itemSupportsText(this.selectedLayoutItem) && this.isVisibleLayoutItem(this.selectedLayoutItem);
         this.textEditorVisible = visible;
         this.textEditBox.setVisible(visible);
         this.textEditBox.setEditable(visible);
         if (visible) {
            LayoutRect anchor = this.getSelectableRect(this.selectedLayoutItem);
            int editorWidth = Math.min(360, Math.max(180, anchor.width + 80));
            int editorHeight = 20;
            int editorX = clamp(anchor.x, 8, Math.max(8, this.width - editorWidth - 8));
            int editorY = anchor.bottom() + 6;
            if (editorY + editorHeight > this.height - 8) {
               editorY = anchor.y - editorHeight - 6;
            }

            editorY = clamp(editorY, 8, Math.max(8, this.height - editorHeight - 8));
            this.textEditBox.setRectangle(editorWidth, editorHeight, editorX, editorY);
         }
      }
   }

   private void applyItemText(String item) {
      Component component = this.getItemComponent(item);
      if ("linked_slider".equals(item) && this.linkedSlider != null) {
         this.linkedSlider.setMessage(component);
      } else if ("zoom_out_slider".equals(item) && this.zoomOutSlider != null) {
         this.zoomOutSlider.setMessage(component);
      } else if ("zoom_in_slider".equals(item) && this.zoomInSlider != null) {
         this.zoomInSlider.setMessage(component);
      } else if ("zoom_stage_glide_slider".equals(item) && this.zoomStageGlideSlider != null) {
         this.zoomStageGlideSlider.setMessage(component);
      } else if ("body_height_slider".equals(item) && this.bodyHeightSlider != null) {
         this.bodyHeightSlider.setMessage(component);
      } else if ("body_glide_slider".equals(item) && this.bodyGlideSlider != null) {
         this.bodyGlideSlider.setMessage(component);
      } else if ("effect_toggle".equals(item) && this.effectToggleButton != null) {
         this.effectToggleButton.setMessage(Component.empty());
      } else if ("movement_toggle".equals(item) && this.movementToggleButton != null) {
         this.movementToggleButton.setMessage(Component.empty());
      } else if ("cross_dimension_travel_toggle".equals(item) && this.crossDimensionTravelToggleButton != null) {
         this.crossDimensionTravelToggleButton.setMessage(Component.empty());
      } else if ("sound_mode_toggle".equals(item) && this.soundModeToggleButton != null) {
         this.soundModeToggleButton.setMessage(Component.empty());
      } else if ("warp_plate_toggle".equals(item) && this.warpPlateToggleButton != null) {
         this.warpPlateToggleButton.setMessage(Component.empty());
      } else if ("external_teleport_toggle".equals(item) && this.externalTeleportToggleButton != null) {
         this.externalTeleportToggleButton.setMessage(Component.empty());
      } else if ("minecraft_volume_slider".equals(item) && this.minecraftSoundVolumeSlider != null) {
         this.minecraftSoundVolumeSlider.setMessage(component);
      } else if ("custom_volume_slider".equals(item) && this.customSoundVolumeSlider != null) {
         this.customSoundVolumeSlider.setMessage(component);
      } else if (this.isPageTabItem(item)) {
         this.applyPageTabText(item);
      } else if ("prev_page_button".equals(item) && this.prevPageButton != null) {
         this.prevPageButton.setMessage(Component.empty());
      } else if ("next_page_button".equals(item) && this.nextPageButton != null) {
         this.nextPageButton.setMessage(Component.empty());
      } else if ("reset_button".equals(item) && this.resetButton != null) {
         this.resetButton.setMessage(Component.empty());
      } else if ("done_button".equals(item) && this.doneButton != null) {
         this.doneButton.setMessage(Component.empty());
      }

   }

   private void applyPageTabText(String item) {
      if (this.pageTabButtons != null) {
         ConfigPage[] pages = GtaLikeTeleportConfigScreen.ConfigPage.values();

         for(int i = 0; i < pages.length && i < this.pageTabButtons.length; ++i) {
            if (pages[i].tabItem.equals(item) && this.pageTabButtons[i] != null) {
               this.pageTabButtons[i].setMessage(Component.empty());
               return;
            }
         }

      }
   }

   private boolean itemSupportsText(String item) {
      return this.isPageTabItem(item) || "title".equals(item) || "description".equals(item) || "general_title".equals(item) || "general_description".equals(item) || "status_linked".equals(item) || "status_unlinked".equals(item) || "effect_label".equals(item) || "movement_label".equals(item) || "cross_dimension_travel_label".equals(item) || "cross_dimension_travel_toggle".equals(item) || "advanced1_title".equals(item) || "advanced1_description".equals(item) || "advanced2_title".equals(item) || "advanced2_description".equals(item) || "advanced3_title".equals(item) || "advanced3_description".equals(item) || "sounds_title".equals(item) || "sounds_description".equals(item) || "sound_mode_label".equals(item) || "sound_mode_toggle".equals(item) || "others_title".equals(item) || "others_description".equals(item) || "warp_plate_label".equals(item) || "warp_plate_toggle".equals(item) || "external_teleport_label".equals(item) || "external_teleport_toggle".equals(item) || "minecraft_volume_slider".equals(item) || "custom_volume_slider".equals(item) || "zoom_stage_glide_ticks_label".equals(item) || "zoom_out_ticks_label".equals(item) || "zoom_in_ticks_label".equals(item) || "body_glide_ticks_label".equals(item) || "player_hide_label".equals(item) || "linked_slider".equals(item) || "zoom_out_slider".equals(item) || "zoom_in_slider".equals(item) || "zoom_stage_glide_slider".equals(item) || "body_height_slider".equals(item) || "body_glide_slider".equals(item) || "reset_button".equals(item) || "done_button".equals(item);
   }

   private boolean isManualTextItem(String item) {
      return "title".equals(item) || "description".equals(item) || "general_title".equals(item) || "general_description".equals(item) || "status_linked".equals(item) || "status_unlinked".equals(item) || "effect_label".equals(item) || "movement_label".equals(item) || "cross_dimension_travel_label".equals(item) || "advanced1_title".equals(item) || "advanced1_description".equals(item) || "advanced2_title".equals(item) || "advanced2_description".equals(item) || "advanced3_title".equals(item) || "advanced3_description".equals(item) || "sounds_title".equals(item) || "sounds_description".equals(item) || "sound_mode_label".equals(item) || "others_title".equals(item) || "others_description".equals(item) || "warp_plate_label".equals(item) || "external_teleport_label".equals(item) || "zoom_stage_glide_ticks_label".equals(item) || "zoom_out_ticks_label".equals(item) || "zoom_in_ticks_label".equals(item) || "body_glide_ticks_label".equals(item) || "player_hide_label".equals(item);
   }

   private boolean isCenteredTextItem(String item) {
      return "title".equals(item) || "general_title".equals(item) || "advanced1_title".equals(item) || "advanced2_title".equals(item) || "advanced3_title".equals(item) || "sounds_title".equals(item) || "others_title".equals(item);
   }

   private boolean isMutedTextItem(String item) {
      return "description".equals(item) || "general_description".equals(item) || "advanced1_description".equals(item) || "advanced2_description".equals(item) || "advanced3_description".equals(item) || "sounds_description".equals(item) || "others_description".equals(item) || "external_teleport_label".equals(item) && !this.isExternalTeleportToggleAvailable() || "status_unlinked".equals(item);
   }

   private boolean isManualButtonTextItem(String item) {
      return "effect_toggle".equals(item) || "movement_toggle".equals(item) || "cross_dimension_travel_toggle".equals(item) || "sound_mode_toggle".equals(item) || "warp_plate_toggle".equals(item) || "external_teleport_toggle".equals(item) || this.isPageTabItem(item) || "prev_page_button".equals(item) || "next_page_button".equals(item) || "reset_button".equals(item) || "done_button".equals(item);
   }

   private void drawButtonText(GuiGraphicsExtractor context, String item) {
      LayoutRect rect = this.getItemRect(item);
      Component text = this.getItemComponent(item);
      double scale = this.getButtonTextScale(rect, text);
      Objects.requireNonNull(this.font);
      int textHeight = Math.max(1, (int)Math.round(9.0 * scale));
      int y = rect.y + (rect.height - textHeight) / 2;
      drawScaledCenteredText(context, this.font, text, rect.x + rect.width / 2, y, -1, scale);
   }

   private double getButtonTextScale(LayoutRect rect, Component text) {
      double scale = Math.max(0.25, Math.min(4.0, (double)rect.height / 20.0));
      int textWidth = this.font.width(text);
      int maxTextWidth = Math.max(1, rect.width - 8);
      if (textWidth > 0 && (double)textWidth * scale > (double)maxTextWidth) {
         scale = (double)maxTextWidth / (double)textWidth;
      }

      return Math.max(0.25, Math.min(4.0, scale));
   }

   private Component getItemComponent(String item) {
      return Component.literal(this.getItemText(item));
   }

   private String getItemText(String item) {
      return this.itemSupportsText(item) && !"done_button".equals(item) ? GtaLikeTeleportConfig.getConfigText(item, this.getDefaultItemText(item)) : this.getDefaultItemText(item);
   }

   private String getDefaultItemText(String item) {
      if ("tab_general".equals(item)) {
         return "General";
      } else if ("tab_zoom_stage".equals(item)) {
         return "Zoom Stage";
      } else if ("tab_zoom_stage_2".equals(item)) {
         return "Zoom Stage 2";
      } else if ("tab_sounds".equals(item)) {
         return "Sounds";
      } else if ("tab_others".equals(item)) {
         return "Others";
      } else if ("title".equals(item)) {
         return Component.translatable("gtalike_teleport.config.title").getString();
      } else if ("description".equals(item)) {
         return Component.translatable("gtalike_teleport.config.description").getString();
      } else if ("general_title".equals(item)) {
         return "Grand Teleport General Settings";
      } else if ("general_description".equals(item)) {
         return "Adjust teleport behavior and input handling.";
      } else if ("advanced1_title".equals(item)) {
         return "Grand Teleport Advanced 1";
      } else if ("advanced1_description".equals(item)) {
         return "Tune extra glide used by zoom stages.";
      } else if ("advanced2_title".equals(item)) {
         return "Grand Teleport Zoom Stage 2";
      } else if ("advanced2_description".equals(item)) {
         return "Set tick lengths for each zoom stage.";
      } else if ("advanced3_title".equals(item)) {
         return "Grand Teleport Advanced 3";
      } else if ("advanced3_description".equals(item)) {
         return "Tune body camera height and player hiding.";
      } else if ("sounds_title".equals(item)) {
         return "Grand Teleport Sound Settings";
      } else if ("sounds_description".equals(item)) {
         return "Choose the teleport sound source and volume.";
      } else if ("sound_mode_label".equals(item)) {
         return "Teleport sound source";
      } else if ("sound_mode_toggle".equals(item)) {
         return this.customSoundsEnabled ? "Grand Teleport" : "Minecraft";
      } else if ("minecraft_volume_slider".equals(item)) {
         return "Minecraft sound volume";
      } else if ("custom_volume_slider".equals(item)) {
         return "Custom sound volume";
      } else if ("others_title".equals(item)) {
         return "Grand Teleport Other Settings";
      } else if ("others_description".equals(item)) {
         return "Control third-party and server-triggered teleport effects.";
      } else if ("warp_plate_label".equals(item)) {
         return "Waystones WarpPlate effect";
      } else if ("warp_plate_toggle".equals(item)) {
         return this.warpPlateTransitionsEnabled ? "ON" : "OFF";
      } else if ("external_teleport_label".equals(item)) {
         return "External teleport effect";
      } else if ("external_teleport_toggle".equals(item)) {
         return this.isExternalTeleportToggleAvailable() ? (this.externalTeleportTransitionsEnabled ? "ON" : "OFF") : "SERVER OFF";
      } else if ("status_linked".equals(item)) {
         return Component.translatable("gtalike_teleport.config.linked").getString();
      } else if ("status_unlinked".equals(item)) {
         return Component.translatable("gtalike_teleport.config.unlinked").getString();
      } else if ("effect_label".equals(item)) {
         return "Grand Teleport effect";
      } else if ("movement_label".equals(item)) {
         return "Allow movement during teleport";
      } else if ("cross_dimension_travel_label".equals(item)) {
         return "Cross-dimension camera travel";
      } else if ("zoom_stage_glide_slider".equals(item)) {
         return "Zoom glide height";
      } else if ("zoom_stage_glide_ticks_label".equals(item)) {
         return "Zoom glide ticks";
      } else if ("zoom_out_ticks_label".equals(item)) {
         return "Zoom-out stage ticks";
      } else if ("zoom_in_ticks_label".equals(item)) {
         return "Zoom-in stage ticks";
      } else if ("body_height_slider".equals(item)) {
         return "Body camera height";
      } else if ("body_glide_slider".equals(item)) {
         return "Body glide height";
      } else if ("body_glide_ticks_label".equals(item)) {
         return "Body glide ticks";
      } else if ("player_hide_label".equals(item)) {
         return "Hide player model";
      } else if ("effect_toggle".equals(item)) {
         return this.effectEnabled ? "ON" : "OFF";
      } else if ("movement_toggle".equals(item)) {
         return this.movementAllowed ? "ON" : "OFF";
      } else if ("cross_dimension_travel_toggle".equals(item)) {
         return this.crossDimensionTravelEnabled ? "ON" : "OFF";
      } else if ("prev_page_button".equals(item)) {
         return "<<";
      } else if ("next_page_button".equals(item)) {
         return ">>";
      } else if ("linked_slider".equals(item)) {
         return Component.translatable("gtalike_teleport.config.zoom_heights").getString();
      } else if ("zoom_out_slider".equals(item)) {
         return Component.translatable("gtalike_teleport.config.zoom_out_heights").getString();
      } else if ("zoom_in_slider".equals(item)) {
         return Component.translatable("gtalike_teleport.config.zoom_in_heights").getString();
      } else if ("reset_button".equals(item)) {
         return Component.translatable("gtalike_teleport.config.reset").getString();
      } else {
         return "done_button".equals(item) ? "Close" : item;
      }
   }

   private List<String> getVisibleLayoutItems() {
      List<String> items = new ArrayList();
      items.add("panel");
      ConfigPage[] var2 = GtaLikeTeleportConfigScreen.ConfigPage.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         ConfigPage page = var2[var4];
         items.add(page.tabItem);
      }

      if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.ZOOM) {
         items.add("title");
         items.add("description");
         items.add("prev_page_button");
         items.add("next_page_button");
         items.add("zoom_out_slider");
         items.add("zoom_in_slider");
         items.add("dimension_overworld");
         items.add("dimension_nether");
         items.add("dimension_end");
         items.add("link_button");
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.GENERAL) {
         items.add("general_title");
         items.add("general_description");
         items.add("prev_page_button");
         items.add("next_page_button");
         items.add("effect_label");
         items.add("effect_toggle");
         items.add("movement_label");
         items.add("movement_toggle");
         items.add("cross_dimension_travel_label");
         items.add("cross_dimension_travel_toggle");
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.ZOOM_STAGE_2) {
         items.add("advanced2_title");
         items.add("advanced2_description");
         items.add("prev_page_button");
         items.add("next_page_button");
         items.add("zoom_out_ticks_label");
         items.add("zoom_out_ticks_field");
         items.add("zoom_in_ticks_label");
         items.add("zoom_in_ticks_field");
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.SOUNDS) {
         items.add("sounds_title");
         items.add("sounds_description");
         items.add("prev_page_button");
         items.add("next_page_button");
         items.add("sound_mode_label");
         items.add("sound_mode_toggle");
         items.add("minecraft_volume_slider");
         items.add("custom_volume_slider");
      } else if (this.currentPage == GtaLikeTeleportConfigScreen.ConfigPage.OTHERS) {
         items.add("others_title");
         items.add("others_description");
         items.add("prev_page_button");
         items.add("next_page_button");
         items.add("warp_plate_label");
         items.add("warp_plate_toggle");
         items.add("external_teleport_label");
         items.add("external_teleport_toggle");
      }

      items.add("reset_button");
      items.add("done_button");
      return items;
   }

   private boolean isVisibleLayoutItem(String item) {
      return this.getVisibleLayoutItems().contains(item);
   }

   private String getStatusItemId() {
      return this.linked ? "status_linked" : "status_unlinked";
   }

   private LayoutRect getSelectableRect(String item) {
      return "panel".equals(item) ? toOuterRect(this.getContentRect()) : this.getItemRect(item);
   }

   private LayoutRect getEditableRect(String item) {
      return "panel".equals(item) ? this.getContentRect() : this.getItemRect(item);
   }

   private LayoutRect getItemRect(String item) {
      if (this.editingRect != null && item != null && item.equals(this.editingLayoutItem)) {
         return "panel".equals(item) ? this.getContentRect() : this.constrainItemRect(item, this.editingRect);
      } else if ("panel".equals(item)) {
         return this.getContentRect();
      } else {
         LayoutRect panel = this.getContentRect();
         if (this.shouldUseCustomWidgetLayout(item)) {
            double[] layout = GtaLikeTeleportConfig.getConfigWidgetLayout(item);
            return this.constrainCustomItemRect(item, this.getScaledItemRect(panel, layout));
         } else {
            if (isDimensionButtonItem(item)) {
               LayoutRect anchored = this.getStatusAnchoredDimensionButtonRect(item, panel);
               if (anchored != null) {
                  return this.constrainCustomItemRect(item, anchored);
               }
            }

            return this.getDefaultItemRect(item, panel);
         }
      }
   }

   private LayoutRect getScaledItemRect(LayoutRect panel, double[] layout) {
      int basePanelWidth = layout.length > 5 ? (int)Math.round(layout[4]) : 0;
      int basePanelHeight = layout.length > 5 ? (int)Math.round(layout[5]) : 0;
      if (basePanelWidth <= 0 || basePanelHeight <= 0) {
         LayoutRect storedBasePanel = this.getStoredBasePanelRect();
         if (storedBasePanel != null) {
            basePanelWidth = storedBasePanel.width;
            basePanelHeight = storedBasePanel.height;
         }
      }

      if (basePanelWidth > 0 && basePanelHeight > 0) {
         double scale = Math.min((double)panel.width / (double)basePanelWidth, (double)panel.height / (double)basePanelHeight);
         int scaledBaseWidth = (int)Math.round((double)basePanelWidth * scale);
         int scaledBaseHeight = (int)Math.round((double)basePanelHeight * scale);
         int offsetX = panel.x + (panel.width - scaledBaseWidth) / 2;
         int offsetY = panel.y + (panel.height - scaledBaseHeight) / 2;
         return new LayoutRect(offsetX + (int)Math.round(layout[0] * (double)basePanelWidth * scale), offsetY + (int)Math.round(layout[1] * (double)basePanelHeight * scale), (int)Math.round(layout[2] * (double)basePanelWidth * scale), (int)Math.round(layout[3] * (double)basePanelHeight * scale));
      } else {
         return new LayoutRect(panel.x + (int)Math.round(layout[0] * (double)panel.width), panel.y + (int)Math.round(layout[1] * (double)panel.height), (int)Math.round(layout[2] * (double)panel.width), (int)Math.round(layout[3] * (double)panel.height));
      }
   }

   private LayoutRect getStatusAnchoredDimensionButtonRect(String item, LayoutRect panel) {
      String statusItem = this.shouldUseCustomWidgetLayout("status_linked") ? "status_linked" : "status_unlinked";
      if (!this.shouldUseCustomWidgetLayout(statusItem)) {
         return null;
      } else {
         LayoutRect statusRect = this.getScaledItemRect(panel, GtaLikeTeleportConfig.getConfigWidgetLayout(statusItem));
         int size = clamp(Math.max(18, statusRect.height + 8), 16, 28);
         int gap = Math.max(2, size / 5);
         int totalWidth = size * 3 + gap * 2;
         int index = dimensionButtonIndex(item);
         int x = statusRect.centerX() - totalWidth / 2 + index * (size + gap);
         int y = statusRect.centerY() - size / 2;
         return new LayoutRect(x, y, size, size);
      }
   }

   private LayoutRect getDefaultDimensionButtonRect(String item, LayoutRect panel) {
      int size = 20;
      int gap = 4;
      int totalWidth = size * 3 + gap * 2;
      int x = panel.x + panel.width / 2 - totalWidth / 2 + dimensionButtonIndex(item) * (size + gap);
      int y = this.getBottomButtonY(panel) + 22;
      if (y + size > panel.bottom()) {
         y = this.getBottomButtonY(panel) - size - 6;
      }

      return new LayoutRect(x, y, size, size);
   }

   private static int dimensionButtonIndex(String item) {
      if ("dimension_nether".equals(item)) {
         return 1;
      } else {
         return "dimension_end".equals(item) ? 2 : 0;
      }
   }

   private static boolean isDimensionButtonItem(String item) {
      return "dimension_overworld".equals(item) || "dimension_nether".equals(item) || "dimension_end".equals(item);
   }

   private LayoutRect getDefaultItemRect(String item, LayoutRect panel) {
      int sliderWidth = Math.max(160, panel.width - 48);
      int sliderY = this.getFirstSliderY(panel);
      if (this.isPageTabItem(item)) {
         return this.getDefaultPageTabRect(item);
      } else {
         int textWidth;
         if (this.isPageTitleItem(item)) {
            textWidth = Math.max(64, this.font.width(this.getItemComponent(item)) + 10);
            return new LayoutRect(panel.x + (panel.width - textWidth) / 2, panel.y, textWidth, 10);
         } else if (this.isPageDescriptionItem(item)) {
            textWidth = Math.max(120, this.font.width(this.getItemComponent(item)) + 4);
            return new LayoutRect(panel.x + Math.max(0, (panel.width - textWidth) / 2), panel.y + 24, Math.min(panel.width, textWidth), 10);
         } else {
            double scale;
            int width;
            int height;
            if ("prev_page_button".equals(item)) {
               scale = this.getContentScale();
               width = Math.max(24, (int)Math.round(34.0 * scale));
               height = Math.max(16, (int)Math.round(20.0 * scale));
               return new LayoutRect(panel.x, panel.y - Math.max(2, height / 10), width, height);
            } else if ("next_page_button".equals(item)) {
               scale = this.getContentScale();
               width = Math.max(24, (int)Math.round(34.0 * scale));
               height = Math.max(16, (int)Math.round(20.0 * scale));
               return new LayoutRect(panel.x + panel.width - width, panel.y - Math.max(2, height / 10), width, height);
            } else if (isDimensionButtonItem(item)) {
               return this.getDefaultDimensionButtonRect(item, panel);
            } else if (!"status_linked".equals(item) && !"status_unlinked".equals(item)) {
               if (!"linked_slider".equals(item) && !"zoom_out_slider".equals(item)) {
                  if ("zoom_in_slider".equals(item)) {
                     return new LayoutRect(panel.x, sliderY + 52, sliderWidth, 44);
                  } else if ("effect_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, panel.y + 58, Math.max(120, panel.width - 170), 12);
                  } else if ("effect_toggle".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 112, panel.y + 52, 92, 20);
                  } else if ("movement_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, panel.y + 92, Math.max(120, panel.width - 170), 12);
                  } else if ("movement_toggle".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 112, panel.y + 86, 92, 20);
                  } else if ("cross_dimension_travel_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, panel.y + 126, Math.max(120, panel.width - 170), 12);
                  } else if ("cross_dimension_travel_toggle".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 112, panel.y + 120, 92, 20);
                  } else if ("sound_mode_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, panel.y + 58, Math.max(120, panel.width - 190), 12);
                  } else if ("sound_mode_toggle".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 132, panel.y + 52, 112, 20);
                  } else if ("warp_plate_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, panel.y + 58, Math.max(120, panel.width - 190), 12);
                  } else if ("warp_plate_toggle".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 132, panel.y + 52, 112, 20);
                  } else if ("external_teleport_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, panel.y + 92, Math.max(120, panel.width - 190), 12);
                  } else if ("external_teleport_toggle".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 132, panel.y + 86, 112, 20);
                  } else if ("minecraft_volume_slider".equals(item)) {
                     return new LayoutRect(panel.x, panel.y + 82, sliderWidth, 44);
                  } else if ("custom_volume_slider".equals(item)) {
                     return new LayoutRect(panel.x, panel.y + 134, sliderWidth, 44);
                  } else if ("zoom_stage_glide_slider".equals(item)) {
                     return new LayoutRect(panel.x, sliderY, sliderWidth, 44);
                  } else if ("zoom_stage_glide_ticks_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, sliderY + 58, Math.max(120, panel.width - 160), 12);
                  } else if ("zoom_stage_glide_ticks_field".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 112, sliderY + 52, 92, 20);
                  } else if ("zoom_out_ticks_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, sliderY + 14, Math.max(120, panel.width - 210), 12);
                  } else if ("zoom_out_ticks_field".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 170, sliderY + 8, 150, 20);
                  } else if ("zoom_in_ticks_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, sliderY + 58, Math.max(120, panel.width - 210), 12);
                  } else if ("zoom_in_ticks_field".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 170, sliderY + 52, 150, 20);
                  } else if ("body_height_slider".equals(item)) {
                     return new LayoutRect(panel.x, sliderY, sliderWidth, 44);
                  } else if ("body_glide_slider".equals(item)) {
                     return new LayoutRect(panel.x, sliderY + 44, sliderWidth, 44);
                  } else if ("body_glide_ticks_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, sliderY + 102, Math.max(120, panel.width - 160), 12);
                  } else if ("body_glide_ticks_field".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 112, sliderY + 96, 92, 20);
                  } else if ("player_hide_label".equals(item)) {
                     return new LayoutRect(panel.x + 20, sliderY + 132, Math.max(120, panel.width - 160), 12);
                  } else if ("player_hide_ticks_field".equals(item)) {
                     return new LayoutRect(panel.x + panel.width - 112, sliderY + 126, 92, 20);
                  } else if ("link_button".equals(item)) {
                     return new LayoutRect(panel.x + panel.width / 2 - 10, this.getBottomButtonY(panel), 20, 20);
                  } else if ("reset_button".equals(item)) {
                     return new LayoutRect(panel.x, this.getBottomButtonY(panel), 150, 20);
                  } else {
                     return "done_button".equals(item) ? new LayoutRect(panel.x + panel.width - 150, this.getBottomButtonY(panel), 150, 20) : new LayoutRect(panel.x, panel.y, 20, 20);
                  }
               } else {
                  return new LayoutRect(panel.x, sliderY, sliderWidth, 44);
               }
            } else {
               return new LayoutRect(panel.x + panel.width - 156, sliderY + 20, 150, 10);
            }
         }
      }
   }

   private LayoutRect constrainItemRect(String item, LayoutRect rect) {
      LayoutRect panel = this.getContentRect();
      int minWidth = 8;
      int minHeight = 8;
      if (!"link_button".equals(item) && !isDimensionButtonItem(item)) {
         if (!"prev_page_button".equals(item) && !"next_page_button".equals(item) && !this.isPageTabItem(item)) {
            if (!"effect_toggle".equals(item) && !"movement_toggle".equals(item) && !"cross_dimension_travel_toggle".equals(item) && !"sound_mode_toggle".equals(item) && !"warp_plate_toggle".equals(item) && !"external_teleport_toggle".equals(item)) {
               if (this.isTickFieldItem(item)) {
                  minWidth = 34;
                  minHeight = 16;
               } else if (this.isSingleValueSliderItem(item)) {
                  minWidth = 100;
                  minHeight = 44;
               } else if (!"reset_button".equals(item) && !"done_button".equals(item)) {
                  if (!"linked_slider".equals(item) && !"zoom_out_slider".equals(item) && !"zoom_in_slider".equals(item)) {
                     if (this.itemSupportsText(item)) {
                        minWidth = 20;
                        minHeight = 8;
                     }
                  } else {
                     minWidth = 120;
                     minHeight = 44;
                  }
               } else {
                  minWidth = 60;
                  minHeight = 18;
               }
            } else {
               minWidth = 44;
               minHeight = 18;
            }
         } else {
            minWidth = 24;
            minHeight = 16;
         }
      } else {
         minWidth = 16;
         minHeight = 16;
      }

      int availableWidth = Math.max(4, this.width - 16);
      int availableHeight = Math.max(4, this.height - 16);
      int effectiveMinWidth = Math.min(minWidth, availableWidth);
      int effectiveMinHeight = Math.min(minHeight, availableHeight);
      int maxWidth = Math.max(effectiveMinWidth, availableWidth);
      int maxHeight = Math.max(effectiveMinHeight, availableHeight);
      int itemWidth = clamp(rect.width, effectiveMinWidth, maxWidth);
      int itemHeight = clamp(rect.height, effectiveMinHeight, maxHeight);
      int x;
      if ("link_button".equals(item) || isDimensionButtonItem(item)) {
         x = clamp(Math.min(itemWidth, itemHeight), effectiveMinWidth, Math.min(maxWidth, maxHeight));
         itemWidth = x;
         itemHeight = x;
      }

      x = clamp(rect.x, 0, Math.max(0, this.width - itemWidth));
      int y = clamp(rect.y, 0, Math.max(0, this.height - itemHeight));
      if (!"panel".equals(item) && panel.width > 0 && panel.height > 0) {
         x = clamp(x, panel.x - panel.width, panel.x + panel.width * 2);
         y = clamp(y, panel.y - panel.height, panel.y + panel.height * 2);
      }

      return new LayoutRect(x, y, itemWidth, itemHeight);
   }

   private LayoutRect constrainCustomItemRect(String item, LayoutRect rect) {
      int minWidth = !"link_button".equals(item) && !isDimensionButtonItem(item) ? 1 : 4;
      int minHeight = !"link_button".equals(item) && !isDimensionButtonItem(item) ? 1 : 4;
      if ("prev_page_button".equals(item) || "next_page_button".equals(item) || this.isPageTabItem(item) || "effect_toggle".equals(item) || "movement_toggle".equals(item) || "cross_dimension_travel_toggle".equals(item) || "sound_mode_toggle".equals(item) || "warp_plate_toggle".equals(item) || "external_teleport_toggle".equals(item) || this.isTickFieldItem(item)) {
         minWidth = 4;
         minHeight = 4;
      }

      int maxWidth = Math.max(minWidth, Math.max(4, this.width - 16));
      int maxHeight = Math.max(minHeight, Math.max(4, this.height - 16));
      int itemWidth = clamp(rect.width, minWidth, maxWidth);
      int itemHeight = clamp(rect.height, minHeight, maxHeight);
      int x;
      if ("link_button".equals(item) || isDimensionButtonItem(item)) {
         x = clamp(Math.min(itemWidth, itemHeight), minWidth, Math.min(maxWidth, maxHeight));
         itemWidth = x;
         itemHeight = x;
      }

      x = clamp(rect.x, 0, Math.max(0, this.width - itemWidth));
      int y = clamp(rect.y, 0, Math.max(0, this.height - itemHeight));
      LayoutRect panel = this.getContentRect();
      if (panel.width > 0 && panel.height > 0) {
         x = clamp(x, panel.x - panel.width, panel.x + panel.width * 2);
         y = clamp(y, panel.y - panel.height, panel.y + panel.height * 2);
      }

      return new LayoutRect(x, y, itemWidth, itemHeight);
   }

   private boolean isPageTitleItem(String item) {
      return "title".equals(item) || "general_title".equals(item) || "advanced1_title".equals(item) || "advanced2_title".equals(item) || "advanced3_title".equals(item) || "sounds_title".equals(item) || "others_title".equals(item);
   }

   private boolean isPageDescriptionItem(String item) {
      return "description".equals(item) || "general_description".equals(item) || "advanced1_description".equals(item) || "advanced2_description".equals(item) || "advanced3_description".equals(item) || "sounds_description".equals(item) || "others_description".equals(item);
   }

   private boolean isTickFieldItem(String item) {
      return "zoom_stage_glide_ticks_field".equals(item) || "zoom_out_ticks_field".equals(item) || "zoom_in_ticks_field".equals(item) || "body_glide_ticks_field".equals(item) || "player_hide_ticks_field".equals(item);
   }

   private boolean isSingleValueSliderItem(String item) {
      return "zoom_stage_glide_slider".equals(item) || "body_height_slider".equals(item) || "body_glide_slider".equals(item) || "minecraft_volume_slider".equals(item) || "custom_volume_slider".equals(item);
   }

   private Component getLayoutDebugLabel() {
      return Component.translatable(this.layoutDebugEnabled ? "gtalike_teleport.config.layout_debug_on" : "gtalike_teleport.config.layout_debug_off");
   }

   private Component getAspectLabel() {
      return Component.translatable(this.layoutAspectLocked ? "gtalike_teleport.config.layout_aspect_locked" : "gtalike_teleport.config.layout_aspect_free");
   }

   private Component getGridLabel() {
      return Component.translatable(this.layoutGridEnabled ? "gtalike_teleport.config.layout_grid_on" : "gtalike_teleport.config.layout_grid_off");
   }

   private Component getSnapLabel() {
      return Component.translatable(this.layoutSnapEnabled ? "gtalike_teleport.config.layout_snap_on" : "gtalike_teleport.config.layout_snap_off");
   }

   private boolean shouldUseCustomConfigLayout() {
      return GtaLikeTeleportConfig.hasCustomConfigLayout();
   }

   private boolean shouldUseCustomWidgetLayout(String item) {
      return GtaLikeTeleportConfig.hasConfigWidgetLayout(item);
   }

   private double getContentScale() {
      LayoutRect basePanel = this.getStoredBasePanelRect();
      if (basePanel != null && basePanel.width > 0 && basePanel.height > 0) {
         LayoutRect panel = this.getContentRect();
         return Math.max(0.2, Math.min(4.0, Math.min((double)panel.width / (double)basePanel.width, (double)panel.height / (double)basePanel.height)));
      } else {
         return 1.0;
      }
   }

   private LayoutRect getStoredBasePanelRect() {
      if (!this.shouldUseCustomConfigLayout()) {
         return null;
      } else {
         int baseWidth = GtaLikeTeleportConfig.getConfigLayoutBaseWidth();
         int baseHeight = GtaLikeTeleportConfig.getConfigLayoutBaseHeight();
         if (baseWidth <= 0 || baseHeight <= 0) {
            baseWidth = this.sessionLayoutBaseWidth;
            baseHeight = this.sessionLayoutBaseHeight;
         }

         if (baseWidth > 0 && baseHeight > 0) {
            double[] layout = GtaLikeTeleportConfig.getConfigLayout();
            return new LayoutRect((int)Math.round(layout[0] * (double)baseWidth), (int)Math.round(layout[1] * (double)baseHeight), Math.max(1, (int)Math.round(layout[2] * (double)baseWidth)), Math.max(1, (int)Math.round(layout[3] * (double)baseHeight)));
         } else {
            return null;
         }
      }
   }

   private int getFirstSliderY(LayoutRect panel) {
      return panel.y + 42;
   }

   private int getBottomButtonY(LayoutRect panel) {
      return panel.y + panel.height - 34;
   }

   private LayoutRect getContentRect() {
      if ("panel".equals(this.editingLayoutItem) && this.editingRect != null) {
         return this.constrainContentRect(this.editingRect);
      } else if (!this.shouldUseCustomConfigLayout()) {
         return this.getDefaultContentRect();
      } else {
         double[] layout = GtaLikeTeleportConfig.getConfigLayout();
         int baseWidth = GtaLikeTeleportConfig.getConfigLayoutBaseWidth();
         int baseHeight = GtaLikeTeleportConfig.getConfigLayoutBaseHeight();
         if (baseWidth <= 0 || baseHeight <= 0) {
            baseWidth = this.sessionLayoutBaseWidth;
            baseHeight = this.sessionLayoutBaseHeight;
         }

         if (baseWidth > 0 && baseHeight > 0) {
            double scale = Math.min((double)this.width / (double)baseWidth, (double)this.height / (double)baseHeight);
            int scaledBaseWidth = (int)Math.round((double)baseWidth * scale);
            int scaledBaseHeight = (int)Math.round((double)baseHeight * scale);
            int offsetX = (this.width - scaledBaseWidth) / 2;
            int offsetY = (this.height - scaledBaseHeight) / 2;
            return this.constrainScaledContentRect(new LayoutRect(offsetX + (int)Math.round(layout[0] * (double)baseWidth * scale), offsetY + (int)Math.round(layout[1] * (double)baseHeight * scale), (int)Math.round(layout[2] * (double)baseWidth * scale), (int)Math.round(layout[3] * (double)baseHeight * scale)));
         } else {
            return this.constrainContentRect(new LayoutRect((int)Math.round(layout[0] * (double)this.width), (int)Math.round(layout[1] * (double)this.height), (int)Math.round(layout[2] * (double)this.width), (int)Math.round(layout[3] * (double)this.height)));
         }
      }
   }

   private LayoutRect getDefaultContentRect() {
      int availableWidth = this.getAvailableContentWidth();
      int minWidth = Math.min(300, availableWidth);
      int targetWidth = (int)Math.round((double)this.width * 0.58);
      int panelWidth = clamp(targetWidth, minWidth, Math.min(680, availableWidth));
      int defaultPanelHeight = 260;
      double heightRatio = 0.31;
      int targetHeight = (int)Math.round((double)this.height * heightRatio);
      int panelHeight = Math.max(defaultPanelHeight, targetHeight);
      int x = (this.width - panelWidth) / 2;
      int y = Math.max(this.getContentTopMargin(), (this.height - panelHeight) / 2);
      return this.constrainContentRect(new LayoutRect(x, y, panelWidth, panelHeight));
   }

   private LayoutRect constrainContentRect(LayoutRect rect) {
      int preferredMinHeight = 170;
      int topMargin = this.getContentTopMargin();
      int availableWidth = this.getAvailableContentWidth();
      int availableHeight = Math.max(80, this.height - topMargin - 28);
      int minWidth = Math.min(300, availableWidth);
      int minHeight = Math.min(preferredMinHeight, availableHeight);
      int panelWidth = clamp(rect.width, minWidth, availableWidth);
      int panelHeight = clamp(rect.height, minHeight, availableHeight);
      int x = clamp(rect.x, 28, Math.max(28, this.width - 28 - panelWidth));
      int y = clamp(rect.y, topMargin, Math.max(topMargin, this.height - 28 - panelHeight));
      return new LayoutRect(x, y, panelWidth, panelHeight);
   }

   private LayoutRect constrainScaledContentRect(LayoutRect rect) {
      int topMargin = this.getContentTopMargin();
      int availableWidth = this.getAvailableContentWidth();
      int availableHeight = Math.max(80, this.height - topMargin - 28);
      int panelWidth = Math.max(1, rect.width);
      int panelHeight = Math.max(1, rect.height);
      double scale = Math.min(1.0, Math.min((double)availableWidth / (double)panelWidth, (double)availableHeight / (double)panelHeight));
      if (scale < 1.0) {
         panelWidth = Math.max(1, (int)Math.round((double)panelWidth * scale));
         panelHeight = Math.max(1, (int)Math.round((double)panelHeight * scale));
      }

      int x = clamp(rect.x, 28, Math.max(28, this.width - 28 - panelWidth));
      int y = clamp(rect.y, topMargin, Math.max(topMargin, this.height - 28 - panelHeight));
      return new LayoutRect(x, y, panelWidth, panelHeight);
   }

   private int getAvailableContentWidth() {
      int screenLimited = Math.max(120, this.width - 56);
      int ratioLimited = Math.max(120, (int)Math.round((double)this.width * 0.72));
      return Math.min(screenLimited, ratioLimited);
   }

   private int getContentTopMargin() {
      return this.layoutDebugEnabled ? Math.min(84, Math.max(28, this.height / 4)) : 28;
   }

   private static LayoutRect toOuterRect(LayoutRect panel) {
      return new LayoutRect(panel.x - 14, panel.y - 14, panel.width + 14 + 14, panel.height + 14 + 0);
   }

   private static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   private static enum ConfigPage {
      GENERAL("tab_general"),
      ZOOM("tab_zoom_stage"),
      ZOOM_STAGE_2("tab_zoom_stage_2"),
      SOUNDS("tab_sounds"),
      OTHERS("tab_others");

      private final String tabItem;

      private ConfigPage(String tabItem) {
         this.tabItem = tabItem;
      }
   }

   private static enum LayoutEditAction {
      NONE,
      MOVE,
      RESIZE_RIGHT,
      RESIZE_BOTTOM,
      RESIZE_CORNER;
   }

   private static record LayoutRect(int x, int y, int width, int height) {
      private LayoutRect(int x, int y, int width, int height) {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
      }

      int right() {
         return this.x + this.width;
      }

      int bottom() {
         return this.y + this.height;
      }

      int centerX() {
         return this.x + this.width / 2;
      }

      int centerY() {
         return this.y + this.height / 2;
      }

      boolean contains(double px, double py) {
         return px >= (double)this.x && px <= (double)this.right() && py >= (double)this.y && py <= (double)this.bottom();
      }

      public int x() {
         return this.x;
      }

      public int y() {
         return this.y;
      }

      public int width() {
         return this.width;
      }

      public int height() {
         return this.height;
      }
   }

   private static final class StageHeightSlider extends AbstractWidget {
      private static final int HEIGHT = 44;
      private static final int HANDLE_WIDTH = 7;
      private static final int TRACK_MARGIN = 14;
      private static final int TRACK_Y = 29;
      private static final int TRACK_HEIGHT = 3;
      private final Consumer<double[]> onChanged;
      private double[] values;
      private int activeHandle = -1;

      StageHeightSlider(int x, int y, int width, Component label, double[] values, Consumer<double[]> onChanged) {
         super(x, y, width, 44, label);
         this.values = GtaLikeTeleportConfig.sanitizeStageHeights(values);
         this.onChanged = onChanged;
      }

      void setValues(double[] values) {
         this.values = GtaLikeTeleportConfig.sanitizeStageHeights(values);
         this.activeHandle = -1;
      }

      void setEditable(boolean editable) {
         this.active = editable;
         if (!editable) {
            this.activeHandle = -1;
         }

      }

      protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickProgress) {
         int x = this.getX();
         int y = this.getY();
         int right = x + this.getWidth();
         int bottom = y + this.getHeight();
         double scale = this.getVisualScale();
         int borderColor = !this.active ? -11184811 : (this.isHoveredOrFocused() ? -1 : -8947849);
         int textColor = this.active ? -1 : -7829368;
         int valueColor = this.active ? -2039584 : -8947849;
         int trackColor = this.active ? -8947849 : -11711155;
         context.fill(x, y, right, bottom, this.active ? -1441458923 : -2012213232);
         context.outline(x, y, this.getWidth(), this.getHeight(), borderColor);
         GtaLikeTeleportConfigScreen.drawScaledText(context, Minecraft.getInstance().font, this.getMessage(), x + scaled(8, scale), y + scaled(7, scale), textColor, scale);
         int valuesWidth = (int)Math.round((double)Minecraft.getInstance().font.width(this.formatValues()) * scale);
         GtaLikeTeleportConfigScreen.drawScaledText(context, Minecraft.getInstance().font, this.formatValues(), right - valuesWidth - scaled(8, scale), y + scaled(7, scale), valueColor, scale);
         int trackLeft = x + this.getTrackMargin();
         int trackRight = right - this.getTrackMargin();
         int trackY = y + this.getTrackY();
         int trackHeight = Math.max(1, scaled(3, scale));
         context.fill(trackLeft, trackY, trackRight, trackY + trackHeight, trackColor);
         int handleWidth = Math.max(3, scaled(7, scale));
         int handleTop = scaled(8, scale);
         int handleBottom = scaled(12, scale);

         for(int i = 0; i < this.values.length; ++i) {
            int handleX = this.valueToX(this.values[i]);
            int handleColor = !this.active ? -8947849 : (i == this.activeHandle ? -1 : -3355444);
            int handleLeft = handleX - handleWidth / 2;
            int handleRight = handleLeft + handleWidth;
            context.fill(handleLeft, trackY - handleTop, handleRight, trackY + handleBottom, handleColor);
         }

      }

      public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
         if (this.active && this.visible && this.isMouseOver(event.x(), event.y())) {
            this.activeHandle = this.nearestHandle(event.x());
            this.updateValueFromMouse(event.x());
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
         } else {
            return false;
         }
      }

      public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
         if (this.activeHandle < 0) {
            return false;
         } else {
            this.updateValueFromMouse(event.x());
            return true;
         }
      }

      public boolean mouseReleased(MouseButtonEvent event) {
         if (this.activeHandle < 0) {
            return false;
         } else {
            this.activeHandle = -1;
            return true;
         }
      }

      protected void updateWidgetNarration(NarrationElementOutput output) {
         NarratedElementType var10001 = NarratedElementType.TITLE;
         String var10002 = this.getMessage().getString();
         output.add(var10001, Component.literal(var10002 + ": " + this.formatValues().getString()));
      }

      private void updateValueFromMouse(double mouseX) {
         if (this.activeHandle >= 0) {
            double value = this.xToValue(mouseX);
            double min = this.activeHandle == 0 ? GtaLikeTeleportConfig.getMinStageHeight() : this.values[this.activeHandle - 1] + GtaLikeTeleportConfig.getMinStageGap();
            double max = this.activeHandle == this.values.length - 1 ? GtaLikeTeleportConfig.getMaxStageHeight() : this.values[this.activeHandle + 1] - GtaLikeTeleportConfig.getMinStageGap();
            this.values[this.activeHandle] = clamp(Math.rint(value), min, max);
            this.onChanged.accept((double[])this.values.clone());
         }
      }

      private int nearestHandle(double mouseX) {
         int nearest = 0;
         double nearestDistance = 1.7976931348623157E308;

         for(int i = 0; i < this.values.length; ++i) {
            double distance = Math.abs(mouseX - (double)this.valueToX(this.values[i]));
            if (distance < nearestDistance) {
               nearestDistance = distance;
               nearest = i;
            }
         }

         return nearest;
      }

      private int valueToX(double value) {
         double min = GtaLikeTeleportConfig.getMinStageHeight();
         double max = GtaLikeTeleportConfig.getMaxStageHeight();
         double progress = (value - min) / Math.max(1.0, max - min);
         int trackLeft = this.getX() + this.getTrackMargin();
         int trackWidth = this.getWidth() - this.getTrackMargin() * 2;
         return trackLeft + (int)Math.round(clamp(progress, 0.0, 1.0) * (double)trackWidth);
      }

      private double xToValue(double mouseX) {
         int trackLeft = this.getX() + this.getTrackMargin();
         int trackWidth = this.getWidth() - this.getTrackMargin() * 2;
         double progress = (mouseX - (double)trackLeft) / Math.max(1.0, (double)trackWidth);
         return GtaLikeTeleportConfig.getMinStageHeight() + clamp(progress, 0.0, 1.0) * (GtaLikeTeleportConfig.getMaxStageHeight() - GtaLikeTeleportConfig.getMinStageHeight());
      }

      private double getVisualScale() {
         return Math.max(0.45, Math.min(4.0, (double)this.getHeight() / 44.0));
      }

      private int getTrackMargin() {
         return scaled(14, this.getVisualScale());
      }

      private int getTrackY() {
         int trackY = scaled(29, this.getVisualScale());
         int min = Math.max(10, this.getHeight() / 2);
         int max = Math.max(min, this.getHeight() - 8);
         return Math.max(min, Math.min(max, trackY));
      }

      private static int scaled(int value, double scale) {
         return Math.max(1, (int)Math.round((double)value * scale));
      }

      private Component formatValues() {
         int var10000 = (int)this.values[0];
         return Component.literal("" + var10000 + " / " + (int)this.values[1] + " / " + (int)this.values[2]);
      }

      private static double clamp(double value, double min, double max) {
         return Math.max(min, Math.min(max, value));
      }

      public String toString() {
         return "StageHeightSlider{" + Arrays.toString(this.values) + "}";
      }
   }

   private static final class SingleValueSlider extends AbstractWidget {
      private static final int HEIGHT = 44;
      private static final int HANDLE_WIDTH = 7;
      private static final int TRACK_MARGIN = 14;
      private static final int TRACK_Y = 29;
      private static final int TRACK_HEIGHT = 3;
      private final Consumer<Double> onChanged;
      private final double min;
      private final double max;
      private final double step;
      private final boolean integerValue;
      private final String suffix;
      private double value;
      private boolean dragging;

      SingleValueSlider(int x, int y, int width, Component label, double value, double min, double max, double step, boolean integerValue, String suffix, Consumer<Double> onChanged) {
         super(x, y, width, 44, label);
         this.min = min;
         this.max = max;
         this.step = step;
         this.integerValue = integerValue;
         this.suffix = suffix;
         this.onChanged = onChanged;
         this.value = this.sanitize(value);
      }

      void setValue(double value) {
         this.value = this.sanitize(value);
      }

      protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickProgress) {
         int x = this.getX();
         int y = this.getY();
         int right = x + this.getWidth();
         int bottom = y + this.getHeight();
         double scale = this.getVisualScale();
         int borderColor = !this.active ? -11184811 : (this.isHoveredOrFocused() ? -1 : -8947849);
         int textColor = this.active ? -1 : -7829368;
         int valueColor = this.active ? -2039584 : -8947849;
         int trackColor = this.active ? -8947849 : -11711155;
         context.fill(x, y, right, bottom, this.active ? -1441458923 : -2012213232);
         context.outline(x, y, this.getWidth(), this.getHeight(), borderColor);
         GtaLikeTeleportConfigScreen.drawScaledText(context, Minecraft.getInstance().font, this.getMessage(), x + scaled(8, scale), y + scaled(7, scale), textColor, scale);
         Component formatted = Component.literal(this.formatValue());
         int valueWidth = (int)Math.round((double)Minecraft.getInstance().font.width(formatted) * scale);
         GtaLikeTeleportConfigScreen.drawScaledText(context, Minecraft.getInstance().font, formatted, right - valueWidth - scaled(8, scale), y + scaled(7, scale), valueColor, scale);
         int trackLeft = x + this.getTrackMargin();
         int trackRight = right - this.getTrackMargin();
         int trackY = y + this.getTrackY();
         int trackHeight = Math.max(1, scaled(3, scale));
         context.fill(trackLeft, trackY, trackRight, trackY + trackHeight, trackColor);
         int handleWidth = Math.max(3, scaled(7, scale));
         int handleX = this.valueToX(this.value);
         int handleLeft = handleX - handleWidth / 2;
         int handleRight = handleLeft + handleWidth;
         int handleColor = !this.active ? -8947849 : (this.dragging ? -1 : -3355444);
         context.fill(handleLeft, trackY - scaled(8, scale), handleRight, trackY + scaled(12, scale), handleColor);
      }

      public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
         if (this.active && this.visible && this.isMouseOver(event.x(), event.y())) {
            this.dragging = true;
            this.updateValueFromMouse(event.x());
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
         } else {
            return false;
         }
      }

      public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
         if (!this.dragging) {
            return false;
         } else {
            this.updateValueFromMouse(event.x());
            return true;
         }
      }

      public boolean mouseReleased(MouseButtonEvent event) {
         if (!this.dragging) {
            return false;
         } else {
            this.dragging = false;
            return true;
         }
      }

      protected void updateWidgetNarration(NarrationElementOutput output) {
         NarratedElementType var10001 = NarratedElementType.TITLE;
         String var10002 = this.getMessage().getString();
         output.add(var10001, Component.literal(var10002 + ": " + this.formatValue()));
      }

      private void updateValueFromMouse(double mouseX) {
         double nextValue = this.sanitize(this.xToValue(mouseX));
         if (!(Math.abs(nextValue - this.value) < 1.0E-4)) {
            this.value = nextValue;
            this.onChanged.accept(this.value);
         }
      }

      private int valueToX(double value) {
         double progress = (value - this.min) / Math.max(1.0E-4, this.max - this.min);
         int trackLeft = this.getX() + this.getTrackMargin();
         int trackWidth = this.getWidth() - this.getTrackMargin() * 2;
         return trackLeft + (int)Math.round(clamp(progress, 0.0, 1.0) * (double)trackWidth);
      }

      private double xToValue(double mouseX) {
         int trackLeft = this.getX() + this.getTrackMargin();
         int trackWidth = this.getWidth() - this.getTrackMargin() * 2;
         double progress = (mouseX - (double)trackLeft) / Math.max(1.0, (double)trackWidth);
         return this.min + clamp(progress, 0.0, 1.0) * (this.max - this.min);
      }

      private double sanitize(double rawValue) {
         double clamped = clamp(rawValue, this.min, this.max);
         if (this.step > 0.0) {
            clamped = (double)Math.round(clamped / this.step) * this.step;
         }

         if (this.integerValue) {
            clamped = (double)Math.round(clamped);
         }

         return clamp(clamped, this.min, this.max);
      }

      private String formatValue() {
         if (this.integerValue) {
            String var10000 = Integer.toString((int)Math.round(this.value));
            return var10000 + this.suffix;
         } else {
            return String.format(Locale.ROOT, "%.1f%s", this.value, this.suffix);
         }
      }

      private double getVisualScale() {
         return Math.max(0.45, Math.min(4.0, (double)this.getHeight() / 44.0));
      }

      private int getTrackMargin() {
         return scaled(14, this.getVisualScale());
      }

      private int getTrackY() {
         int trackY = scaled(29, this.getVisualScale());
         int minY = Math.max(10, this.getHeight() / 2);
         int maxY = Math.max(minY, this.getHeight() - 8);
         return Math.max(minY, Math.min(maxY, trackY));
      }

      private static int scaled(int value, double scale) {
         return Math.max(1, (int)Math.round((double)value * scale));
      }

      private static double clamp(double value, double min, double max) {
         return Math.max(min, Math.min(max, value));
      }
   }

   private static final class DimensionIconButton extends AbstractWidget {
      private final ItemStack icon;
      private final Identifier texture;
      private final Runnable onPress;
      private boolean selected;

      DimensionIconButton(int x, int y, int width, int height, ItemStack icon, Component label, Identifier texture, Runnable onPress) {
         super(x, y, width, height, label);
         this.icon = icon.copy();
         this.texture = texture;
         this.onPress = onPress;
      }

      void setSelected(boolean selected) {
         this.selected = selected;
      }

      protected void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickProgress) {
         int x = this.getX();
         int y = this.getY();
         int right = x + this.getWidth();
         int bottom = y + this.getHeight();
         int borderColor = this.selected ? -1 : (this.isHoveredOrFocused() ? -5592406 : -11184811);
         context.fill(x, y, right, bottom, this.selected ? -1440735200 : -1441787888);
         context.outline(x, y, this.getWidth(), this.getHeight(), borderColor);
         double scale = Math.max(0.5, Math.min(4.0, Math.min((double)(this.getWidth() - 4) / 16.0, (double)(this.getHeight() - 4) / 16.0)));
         int iconX = x + (int)Math.round(((double)this.getWidth() - 16.0 * scale) / 2.0);
         int iconY = y + (int)Math.round(((double)this.getHeight() - 16.0 * scale) / 2.0);
         if (!this.icon.isEmpty()) {
            context.pose().pushMatrix();
            context.pose().translate((float)iconX, (float)iconY);
            context.pose().scale((float)scale);
            context.item(this.icon, 0, 0);
            context.pose().popMatrix();
         } else {
            drawTextureIcon(context, iconX, iconY, scale, this.texture);
         }

         if (!this.selected) {
            context.fill(x + 1, y + 1, Math.max(x + 1, right - 1), Math.max(y + 1, bottom - 1), -1728053248);
         }

      }

      private static void drawTextureIcon(GuiGraphicsExtractor context, int x, int y, double scale, Identifier texture) {
         int size = Math.max(4, (int)Math.round(16.0 * scale));
         context.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0.0F, 0.0F, size, size, 16, 16, 16, 16);
      }

      public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
         if (this.active && this.visible && this.isMouseOver(event.x(), event.y())) {
            this.onPress.run();
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            return true;
         } else {
            return false;
         }
      }

      protected void updateWidgetNarration(NarrationElementOutput output) {
         output.add(NarratedElementType.TITLE, this.getMessage());
      }
   }

   private static final class ScaledEditBox extends EditBox {
      private final Font font;
      private double textScale;
      private boolean selectingText;
      private int selectionAnchor;

      ScaledEditBox(Font font, int x, int y, int width, int height, Component message, double textScale) {
         super(font, x, y, width, height, message);
         this.font = font;
         this.setTextScale(textScale);
      }

      void setTextScale(double textScale) {
         this.textScale = Math.max(0.25, Math.min(4.0, textScale));
      }

      public void extractWidgetRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float tickProgress) {
         int x = this.getX();
         int y = this.getY();
         int width = this.getWidth();
         int height = this.getHeight();
         int right = x + width;
         int bottom = y + height;
         int borderColor = this.isFocused() ? -1 : -5592406;
         int textColor = this.active ? -1 : -6250336;
         context.fill(x, y, right, bottom, -16777216);
         context.outline(x, y, width, height, borderColor);
         String rawValue = this.getValue();
         Component value = Component.literal(rawValue);
         double scale = this.getTextScale(value);
         int textHeight = this.getTextHeight(scale);
         int textX = this.getTextX(scale);
         int textY = this.getTextY(textHeight);
         this.drawSelection(context, rawValue, textX, textY, textHeight, scale);
         GtaLikeTeleportConfigScreen.drawScaledText(context, this.font, value, textX, textY, textColor, scale);
         if (this.isFocused()) {
            int cursorPosition = Math.max(0, Math.min(this.getCursorPosition(), rawValue.length()));
            int cursorX = this.getCursorX(rawValue, cursorPosition, textX, scale);
            int cursorWidth = Math.max(1, (int)Math.round(scale));
            if (cursorX < right - 3) {
               context.fill(cursorX, textY - 1, cursorX + cursorWidth, textY + textHeight + 1, textColor);
            }
         }

      }

      public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
         if (this.active && this.visible && event.button() == 0 && this.isMouseOver(event.x(), event.y())) {
            int cursor = this.getCursorPositionForMouse(event.x());
            this.setFocused(true);
            this.setCursorPosition(cursor);
            this.setHighlightPos(cursor);
            this.selectionAnchor = cursor;
            this.selectingText = true;
            return true;
         } else {
            return false;
         }
      }

      public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
         if (this.selectingText && event.button() == 0) {
            int cursor = this.getCursorPositionForMouse(event.x());
            this.setCursorPosition(cursor);
            this.setHighlightPos(this.selectionAnchor);
            return true;
         } else {
            return false;
         }
      }

      public boolean mouseReleased(MouseButtonEvent event) {
         if (this.selectingText && event.button() == 0) {
            this.selectingText = false;
            return true;
         } else {
            return false;
         }
      }

      private double getTextScale(Component text) {
         double scale = Math.max(0.45, Math.min(4.0, this.textScale));
         int textWidth = this.font.width(text);
         int maxTextWidth = Math.max(1, this.getWidth() - 8);
         if (textWidth > 0 && (double)textWidth * scale > (double)maxTextWidth) {
            scale = (double)maxTextWidth / (double)textWidth;
         }

         return Math.max(0.45, Math.min(4.0, scale));
      }

      private int getTextHeight(double scale) {
         Objects.requireNonNull(this.font);
         return Math.max(1, (int)Math.round(9.0 * scale));
      }

      private int getTextX(double scale) {
         return this.getX() + Math.max(4, (int)Math.round(4.0 * scale));
      }

      private int getTextY(int textHeight) {
         return this.getY() + (this.getHeight() - textHeight) / 2;
      }

      private int getCursorX(String value, int cursorPosition, int textX, double scale) {
         int cursor = Math.max(0, Math.min(cursorPosition, value.length()));
         return textX + (int)Math.round((double)this.font.width(value.substring(0, cursor)) * scale);
      }

      private int getCursorPositionForMouse(double mouseX) {
         String value = this.getValue();
         double scale = this.getTextScale(Component.literal(value));
         double localX = (mouseX - (double)this.getTextX(scale)) / scale;
         if (localX <= 0.0) {
            return 0;
         } else {
            for(int i = 1; i <= value.length(); ++i) {
               int previous = this.font.width(value.substring(0, i - 1));
               int current = this.font.width(value.substring(0, i));
               if (localX < (double)(previous + current) / 2.0) {
                  return i - 1;
               }
            }

            return value.length();
         }
      }

      private void drawSelection(GuiGraphicsExtractor context, String value, int textX, int textY, int textHeight, double scale) {
         String selected = this.getHighlighted();
         if (selected != null && !selected.isEmpty()) {
            int cursor = Math.max(0, Math.min(this.getCursorPosition(), value.length()));
            int selectedLength = selected.length();
            int start = cursor - selectedLength;
            int end = cursor;
            if (!this.matchesSelection(value, selected, start, cursor)) {
               start = cursor;
               end = cursor + selectedLength;
            }

            if (this.matchesSelection(value, selected, start, end)) {
               int left = this.getCursorX(value, start, textX, scale);
               int right = this.getCursorX(value, end, textX, scale);
               context.fill(Math.min(left, right), textY - 1, Math.max(left, right), textY + textHeight + 1, -1439736902);
            }
         }
      }

      private boolean matchesSelection(String value, String selected, int start, int end) {
         return start >= 0 && end <= value.length() && start <= end && value.substring(start, end).equals(selected);
      }
   }

   private static record ToolbarCursor(int x, int y) {
      private ToolbarCursor(int x, int y) {
         this.x = x;
         this.y = y;
      }

      public int x() {
         return this.x;
      }

      public int y() {
         return this.y;
      }
   }
}
