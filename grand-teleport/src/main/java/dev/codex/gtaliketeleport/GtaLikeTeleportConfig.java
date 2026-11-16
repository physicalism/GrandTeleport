package dev.codex.gtaliketeleport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

final class GtaLikeTeleportConfig {
   private static final String FILE_NAME = "grand_teleport.properties";
   private static final String LEGACY_FILE_NAME = "gtalike_teleport.properties";
   private static final String EFFECT_ENABLED_KEY = "effectEnabled";
   private static final String PLAYER_FREEZE_ENABLED_KEY = "playerFreezeEnabled";
   private static final String CROSS_DIMENSION_TRAVEL_ENABLED_KEY = "crossDimensionTravelEnabled";
   private static final String ZOOM_HEIGHTS_LINKED_KEY = "zoomHeightsLinked";
   private static final String ZOOM_OUT_STAGE_KEY_PREFIX = "zoomOutStage";
   private static final String ZOOM_IN_STAGE_KEY_PREFIX = "zoomInStage";
   private static final String NETHER_ZOOM_HEIGHTS_LINKED_KEY = "netherZoomHeightsLinked";
   private static final String NETHER_ZOOM_OUT_STAGE_KEY_PREFIX = "netherZoomOutStage";
   private static final String NETHER_ZOOM_IN_STAGE_KEY_PREFIX = "netherZoomInStage";
   private static final String END_ZOOM_HEIGHTS_LINKED_KEY = "endZoomHeightsLinked";
   private static final String END_ZOOM_OUT_STAGE_KEY_PREFIX = "endZoomOutStage";
   private static final String END_ZOOM_IN_STAGE_KEY_PREFIX = "endZoomInStage";
   private static final String ZOOM_OUT_STAGE_TICKS_KEY_PREFIX = "zoomOutStageTicks";
   private static final String ZOOM_IN_STAGE_TICKS_KEY_PREFIX = "zoomInStageTicks";
   private static final String ZOOM_STAGE_GLIDE_HEIGHT_KEY = "zoomStageGlideHeight";
   private static final String ZOOM_STAGE_GLIDE_TICKS_KEY = "zoomStageGlideTicks";
   private static final String BODY_CAMERA_HEIGHT_KEY = "bodyCameraHeight";
   private static final String BODY_GLIDE_HEIGHT_KEY = "bodyGlideHeight";
   private static final String BODY_GLIDE_TICKS_KEY = "bodyGlideTicks";
   private static final String LOCAL_PLAYER_HIDE_TICKS_KEY = "localPlayerHideTicks";
   private static final String CUSTOM_SOUNDS_ENABLED_KEY = "customSoundsEnabled";
   private static final String MINECRAFT_SOUND_VOLUME_KEY = "minecraftSoundVolume";
   private static final String CUSTOM_SOUND_VOLUME_KEY = "customSoundVolume";
   private static final String WARP_PLATE_TRANSITIONS_ENABLED_KEY = "warpPlateTransitionsEnabled";
   private static final String EXTERNAL_TELEPORT_TRANSITIONS_ENABLED_KEY = "externalTeleportTransitionsEnabled";
   private static final String FALLBACK_CHUNK_FADE_ENABLED_KEY = "fallbackChunkFadeEnabled";
   private static final String CONFIG_LAYOUT_EDITOR_BUTTON_VISIBLE_KEY = "configLayoutEditorButtonVisible";
   private static final String CONFIG_LAYOUT_DEBUG_ENABLED_KEY = "configLayoutDebugEnabled";
   private static final String CONFIG_LAYOUT_ASPECT_LOCKED_KEY = "configLayoutAspectLocked";
   private static final String CONFIG_LAYOUT_GRID_ENABLED_KEY = "configLayoutGridEnabled";
   private static final String CONFIG_LAYOUT_SNAP_ENABLED_KEY = "configLayoutSnapEnabled";
   private static final String CONFIG_LAYOUT_CUSTOM_KEY = "configLayoutCustom";
   private static final String CONFIG_LAYOUT_X_KEY = "configLayoutX";
   private static final String CONFIG_LAYOUT_Y_KEY = "configLayoutY";
   private static final String CONFIG_LAYOUT_WIDTH_KEY = "configLayoutWidth";
   private static final String CONFIG_LAYOUT_HEIGHT_KEY = "configLayoutHeight";
   private static final String CONFIG_LAYOUT_BASE_WIDTH_KEY = "configLayoutBaseWidth";
   private static final String CONFIG_LAYOUT_BASE_HEIGHT_KEY = "configLayoutBaseHeight";
   private static final String CONFIG_WIDGET_PREFIX = "configWidget.";
   private static final String CONFIG_TEXT_PREFIX = "configText.";
   private static final String DEFAULT_CONFIG_PROPERTIES = "bodyCameraHeight=6.0\nbodyGlideHeight=0.5\nbodyGlideTicks=10\nconfigLayoutAspectLocked=false\nconfigLayoutBaseHeight=353\nconfigLayoutBaseWidth=640\nconfigLayoutCustom=true\nconfigLayoutDebugEnabled=false\nconfigLayoutEditorButtonVisible=false\nconfigLayoutGridEnabled=true\nconfigLayoutHeight=0.6005665722379604\nconfigLayoutSnapEnabled=false\nconfigLayoutWidth=0.5796875\nconfigLayoutX=0.2109375\nconfigLayoutY=0.23796033994334279\nconfigText.advanced1_title=GTP Advanced Settings (1)\nconfigText.advanced2_description=Set tick lengths for each zoom stage. (1st / 2nd / 3rd)\nconfigText.advanced2_title=ZoomStage Settings (2)\nconfigText.advanced3_title=GTP Advanced Settings (3)\nconfigText.done_button=Close\nconfigText.fallback_chunk_fade_label=Vanilla/Sodium chunk-mask fade\nconfigText.general_title=General Settings\nconfigText.linked_slider=\\    camera_zoom 1st / 2nd / 3rd\nconfigText.others_title=Other Settings\nconfigText.reset_button=Reset\nconfigText.sounds_title=Sound Settings\nconfigText.title=ZoomStage Settings\nconfigText.zoom_out_ticks_label=Zoom-out stage ticks\nconfigWidget.advanced1_description.baseHeight=195\nconfigWidget.advanced1_description.baseWidth=368\nconfigWidget.advanced1_description.height=0.05128205128205128\nconfigWidget.advanced1_description.width=0.5407608695652174\nconfigWidget.advanced1_description.x=0.22826086956521738\nconfigWidget.advanced1_description.y=0.13333333333333333\nconfigWidget.advanced1_title.baseHeight=195\nconfigWidget.advanced1_title.baseWidth=368\nconfigWidget.advanced1_title.height=0.05128205128205128\nconfigWidget.advanced1_title.width=0.24456521739130435\nconfigWidget.advanced1_title.x=0.37771739130434784\nconfigWidget.advanced1_title.y=0.015384615384615385\nconfigWidget.advanced2_description.baseHeight=212\nconfigWidget.advanced2_description.baseWidth=371\nconfigWidget.advanced2_description.height=0.04716981132075472\nconfigWidget.advanced2_description.width=0.7601078167115903\nconfigWidget.advanced2_description.x=0.11859838274932614\nconfigWidget.advanced2_description.y=0.15566037735849056\nconfigWidget.advanced2_title.baseHeight=198\nconfigWidget.advanced2_title.baseWidth=368\nconfigWidget.advanced2_title.height=0.050505050505050504\nconfigWidget.advanced2_title.width=0.24456521739130435\nconfigWidget.advanced2_title.x=0.37771739130434784\nconfigWidget.advanced2_title.y=0.020202020202020204\nconfigWidget.advanced3_description.baseHeight=195\nconfigWidget.advanced3_description.baseWidth=368\nconfigWidget.advanced3_description.height=0.05128205128205128\nconfigWidget.advanced3_description.width=0.6059782608695652\nconfigWidget.advanced3_description.x=0.1956521739130435\nconfigWidget.advanced3_description.y=0.13333333333333333\nconfigWidget.advanced3_title.baseHeight=195\nconfigWidget.advanced3_title.baseWidth=368\nconfigWidget.advanced3_title.height=0.05128205128205128\nconfigWidget.advanced3_title.width=0.24456521739130435\nconfigWidget.advanced3_title.x=0.37771739130434784\nconfigWidget.advanced3_title.y=0.015384615384615385\nconfigWidget.body_glide_slider.baseHeight=195\nconfigWidget.body_glide_slider.baseWidth=368\nconfigWidget.body_glide_slider.height=0.24102564102564103\nconfigWidget.body_glide_slider.width=0.5\nconfigWidget.body_glide_slider.x=0.5\nconfigWidget.body_glide_slider.y=0.2153846153846154\nconfigWidget.body_glide_ticks_field.baseHeight=195\nconfigWidget.body_glide_ticks_field.baseWidth=368\nconfigWidget.body_glide_ticks_field.height=0.10256410256410256\nconfigWidget.body_glide_ticks_field.width=0.25\nconfigWidget.body_glide_ticks_field.x=0.7038043478260869\nconfigWidget.body_glide_ticks_field.y=0.5025641025641026\nconfigWidget.body_glide_ticks_label.baseHeight=195\nconfigWidget.body_glide_ticks_label.baseWidth=368\nconfigWidget.body_glide_ticks_label.height=0.07179487179487179\nconfigWidget.body_glide_ticks_label.width=0.5652173913043478\nconfigWidget.body_glide_ticks_label.x=0.043478260869565216\nconfigWidget.body_glide_ticks_label.y=0.5333333333333333\nconfigWidget.body_height_slider.baseHeight=195\nconfigWidget.body_height_slider.baseWidth=368\nconfigWidget.body_height_slider.height=0.24102564102564103\nconfigWidget.body_height_slider.width=0.5\nconfigWidget.body_height_slider.x=0.0\nconfigWidget.body_height_slider.y=0.2153846153846154\nconfigWidget.cross_dimension_travel_label.baseHeight=212\nconfigWidget.cross_dimension_travel_label.baseWidth=371\nconfigWidget.cross_dimension_travel_label.height=0.05660377358490566\nconfigWidget.cross_dimension_travel_label.width=0.5417789757412399\nconfigWidget.cross_dimension_travel_label.x=0.05390835579514825\nconfigWidget.cross_dimension_travel_label.y=0.6132075471698113\nconfigWidget.cross_dimension_travel_toggle.baseHeight=212\nconfigWidget.cross_dimension_travel_toggle.baseWidth=371\nconfigWidget.cross_dimension_travel_toggle.height=0.09433962264150944\nconfigWidget.cross_dimension_travel_toggle.width=0.25067385444743934\nconfigWidget.cross_dimension_travel_toggle.x=0.6954177897574124\nconfigWidget.cross_dimension_travel_toggle.y=0.589622641509434\nconfigWidget.custom_volume_slider.baseHeight=297\nconfigWidget.custom_volume_slider.baseWidth=551\nconfigWidget.custom_volume_slider.height=0.2222222222222222\nconfigWidget.custom_volume_slider.width=0.5009074410163339\nconfigWidget.custom_volume_slider.x=0.5009074410163339\nconfigWidget.custom_volume_slider.y=0.4612794612794613\nconfigWidget.description.baseHeight=212\nconfigWidget.description.baseWidth=371\nconfigWidget.description.height=0.05188679245283019\nconfigWidget.description.width=0.6738544474393531\nconfigWidget.description.x=0.16172506738544473\nconfigWidget.description.y=0.15566037735849056\nconfigWidget.dimension_end.baseHeight=198\nconfigWidget.dimension_end.baseWidth=368\nconfigWidget.dimension_end.height=0.08080808080808081\nconfigWidget.dimension_end.width=0.043478260869565216\nconfigWidget.dimension_end.x=0.5244565217391305\nconfigWidget.dimension_end.y=0.8939393939393939\nconfigWidget.dimension_nether.baseHeight=198\nconfigWidget.dimension_nether.baseWidth=368\nconfigWidget.dimension_nether.height=0.08080808080808081\nconfigWidget.dimension_nether.width=0.043478260869565216\nconfigWidget.dimension_nether.x=0.47554347826086957\nconfigWidget.dimension_nether.y=0.8939393939393939\nconfigWidget.dimension_overworld.baseHeight=198\nconfigWidget.dimension_overworld.baseWidth=368\nconfigWidget.dimension_overworld.height=0.08080808080808081\nconfigWidget.dimension_overworld.width=0.043478260869565216\nconfigWidget.dimension_overworld.x=0.4266304347826087\nconfigWidget.dimension_overworld.y=0.8939393939393939\nconfigWidget.done_button.baseHeight=198\nconfigWidget.done_button.baseWidth=368\nconfigWidget.done_button.height=0.10101010101010101\nconfigWidget.done_button.width=0.44565217391304346\nconfigWidget.done_button.x=0.5543478260869565\nconfigWidget.done_button.y=0.7929292929292929\nconfigWidget.effect_label.baseHeight=212\nconfigWidget.effect_label.baseWidth=371\nconfigWidget.effect_label.height=0.05188679245283019\nconfigWidget.effect_label.width=0.31805929919137466\nconfigWidget.effect_label.x=0.05390835579514825\nconfigWidget.effect_label.y=0.3113207547169811\nconfigWidget.effect_toggle.baseHeight=212\nconfigWidget.effect_toggle.baseWidth=371\nconfigWidget.effect_toggle.height=0.09433962264150944\nconfigWidget.effect_toggle.width=0.25067385444743934\nconfigWidget.effect_toggle.x=0.6954177897574124\nconfigWidget.effect_toggle.y=0.28773584905660377\nconfigWidget.external_teleport_label.baseHeight=198\nconfigWidget.external_teleport_label.baseWidth=368\nconfigWidget.external_teleport_label.height=0.045454545454545456\nconfigWidget.external_teleport_label.width=0.483695652173913\nconfigWidget.external_teleport_label.x=0.05434782608695652\nconfigWidget.external_teleport_label.y=0.5707070707070707\nconfigWidget.external_teleport_toggle.baseHeight=198\nconfigWidget.external_teleport_toggle.baseWidth=368\nconfigWidget.external_teleport_toggle.height=0.10101010101010101\nconfigWidget.external_teleport_toggle.width=0.25\nconfigWidget.external_teleport_toggle.x=0.6956521739130435\nconfigWidget.external_teleport_toggle.y=0.5454545454545454\nconfigWidget.fallback_chunk_fade_label.baseHeight=132\nconfigWidget.fallback_chunk_fade_label.baseWidth=246\nconfigWidget.fallback_chunk_fade_label.height=0.06060606060606061\nconfigWidget.fallback_chunk_fade_label.width=0.483739837398374\nconfigWidget.fallback_chunk_fade_label.x=0.052845528455284556\nconfigWidget.fallback_chunk_fade_label.y=0.6287878787878788\nconfigWidget.fallback_chunk_fade_toggle.baseHeight=198\nconfigWidget.fallback_chunk_fade_toggle.baseWidth=368\nconfigWidget.fallback_chunk_fade_toggle.height=0.10101010101010101\nconfigWidget.fallback_chunk_fade_toggle.width=0.25\nconfigWidget.fallback_chunk_fade_toggle.x=0.6956521739130435\nconfigWidget.fallback_chunk_fade_toggle.y=0.601010101010101\nconfigWidget.general_description.baseHeight=212\nconfigWidget.general_description.baseWidth=371\nconfigWidget.general_description.height=0.05188679245283019\nconfigWidget.general_description.width=0.31266846361185985\nconfigWidget.general_description.x=0.19137466307277629\nconfigWidget.general_description.y=0.15566037735849056\nconfigWidget.general_title.baseHeight=198\nconfigWidget.general_title.baseWidth=368\nconfigWidget.general_title.height=0.050505050505050504\nconfigWidget.general_title.width=0.3125\nconfigWidget.general_title.x=0.3451086956521739\nconfigWidget.general_title.y=0.020202020202020204\nconfigWidget.link_button.baseHeight=198\nconfigWidget.link_button.baseWidth=368\nconfigWidget.link_button.height=0.10101010101010101\nconfigWidget.link_button.width=0.05434782608695652\nconfigWidget.link_button.x=0.47282608695652173\nconfigWidget.link_button.y=0.7929292929292929\nconfigWidget.linked_slider.baseHeight=0\nconfigWidget.linked_slider.baseWidth=0\nconfigWidget.linked_slider.height=0.2571428571428571\nconfigWidget.linked_slider.width=1.0\nconfigWidget.linked_slider.x=0.0\nconfigWidget.linked_slider.y=0.3314285714285714\nconfigWidget.minecraft_volume_slider.baseHeight=297\nconfigWidget.minecraft_volume_slider.baseWidth=551\nconfigWidget.minecraft_volume_slider.height=0.2222222222222222\nconfigWidget.minecraft_volume_slider.width=0.5009074410163339\nconfigWidget.minecraft_volume_slider.x=0.0\nconfigWidget.minecraft_volume_slider.y=0.4612794612794613\nconfigWidget.movement_label.baseHeight=212\nconfigWidget.movement_label.baseWidth=371\nconfigWidget.movement_label.height=0.06132075471698113\nconfigWidget.movement_label.width=0.4366576819407008\nconfigWidget.movement_label.x=0.05390835579514825\nconfigWidget.movement_label.y=0.46226415094339623\nconfigWidget.movement_toggle.baseHeight=212\nconfigWidget.movement_toggle.baseWidth=371\nconfigWidget.movement_toggle.height=0.09433962264150944\nconfigWidget.movement_toggle.width=0.25067385444743934\nconfigWidget.movement_toggle.x=0.6954177897574124\nconfigWidget.movement_toggle.y=0.4386792452830189\nconfigWidget.others_description.baseHeight=212\nconfigWidget.others_description.baseWidth=371\nconfigWidget.others_description.height=0.04716981132075472\nconfigWidget.others_description.width=0.8382749326145552\nconfigWidget.others_description.x=0.08086253369272237\nconfigWidget.others_description.y=0.15566037735849056\nconfigWidget.others_title.baseHeight=198\nconfigWidget.others_title.baseWidth=368\nconfigWidget.others_title.height=0.050505050505050504\nconfigWidget.others_title.width=0.2826086956521739\nconfigWidget.others_title.x=0.358695652173913\nconfigWidget.others_title.y=0.020202020202020204\nconfigWidget.player_hide_label.baseHeight=195\nconfigWidget.player_hide_label.baseWidth=368\nconfigWidget.player_hide_label.height=0.06153846153846154\nconfigWidget.player_hide_label.width=0.5652173913043478\nconfigWidget.player_hide_label.x=0.043478260869565216\nconfigWidget.player_hide_label.y=0.676923076923077\nconfigWidget.player_hide_slider.baseHeight=195\nconfigWidget.player_hide_slider.baseWidth=368\nconfigWidget.player_hide_slider.height=0.22564102564102564\nconfigWidget.player_hide_slider.width=0.8695652173913043\nconfigWidget.player_hide_slider.x=0.021739130434782608\nconfigWidget.player_hide_slider.y=0.5692307692307692\nconfigWidget.player_hide_ticks_field.baseHeight=195\nconfigWidget.player_hide_ticks_field.baseWidth=368\nconfigWidget.player_hide_ticks_field.height=0.10256410256410256\nconfigWidget.player_hide_ticks_field.width=0.25\nconfigWidget.player_hide_ticks_field.x=0.7038043478260869\nconfigWidget.player_hide_ticks_field.y=0.6461538461538462\nconfigWidget.reset_button.baseHeight=198\nconfigWidget.reset_button.baseWidth=368\nconfigWidget.reset_button.height=0.10101010101010101\nconfigWidget.reset_button.width=0.44565217391304346\nconfigWidget.reset_button.x=0.0\nconfigWidget.reset_button.y=0.7929292929292929\nconfigWidget.sound_mode_label.baseHeight=198\nconfigWidget.sound_mode_label.baseWidth=368\nconfigWidget.sound_mode_label.height=0.05555555555555555\nconfigWidget.sound_mode_label.width=0.483695652173913\nconfigWidget.sound_mode_label.x=0.06521739130434782\nconfigWidget.sound_mode_label.y=0.29797979797979796\nconfigWidget.sound_mode_toggle.baseHeight=198\nconfigWidget.sound_mode_toggle.baseWidth=368\nconfigWidget.sound_mode_toggle.height=0.10101010101010101\nconfigWidget.sound_mode_toggle.width=0.30434782608695654\nconfigWidget.sound_mode_toggle.x=0.6331521739130435\nconfigWidget.sound_mode_toggle.y=0.2727272727272727\nconfigWidget.sounds_description.baseHeight=212\nconfigWidget.sounds_description.baseWidth=371\nconfigWidget.sounds_description.height=0.04716981132075472\nconfigWidget.sounds_description.width=0.6522911051212938\nconfigWidget.sounds_description.x=0.1752021563342318\nconfigWidget.sounds_description.y=0.15566037735849056\nconfigWidget.sounds_title.baseHeight=198\nconfigWidget.sounds_title.baseWidth=368\nconfigWidget.sounds_title.height=0.050505050505050504\nconfigWidget.sounds_title.width=0.28804347826086957\nconfigWidget.sounds_title.x=0.35597826086956524\nconfigWidget.sounds_title.y=0.020202020202020204\nconfigWidget.status_linked.baseHeight=195\nconfigWidget.status_linked.baseWidth=368\nconfigWidget.status_linked.height=0.041025641025641026\nconfigWidget.status_linked.width=0.08695652173913043\nconfigWidget.status_linked.x=0.45652173913043476\nconfigWidget.status_linked.y=0.9179487179487179\nconfigWidget.status_unlinked.baseHeight=195\nconfigWidget.status_unlinked.baseWidth=368\nconfigWidget.status_unlinked.height=0.041025641025641026\nconfigWidget.status_unlinked.width=0.13043478260869565\nconfigWidget.status_unlinked.x=0.43478260869565216\nconfigWidget.status_unlinked.y=0.9179487179487179\nconfigWidget.tab_general.baseHeight=212\nconfigWidget.tab_general.baseWidth=371\nconfigWidget.tab_general.height=0.09433962264150944\nconfigWidget.tab_general.width=0.2183288409703504\nconfigWidget.tab_general.x=-0.03773584905660377\nconfigWidget.tab_general.y=-0.13679245283018868\nconfigWidget.tab_others.baseHeight=212\nconfigWidget.tab_others.baseWidth=371\nconfigWidget.tab_others.height=0.09433962264150944\nconfigWidget.tab_others.width=0.2183288409703504\nconfigWidget.tab_others.x=0.8194070080862533\nconfigWidget.tab_others.y=-0.13679245283018868\nconfigWidget.tab_sounds.baseHeight=212\nconfigWidget.tab_sounds.baseWidth=371\nconfigWidget.tab_sounds.height=0.09433962264150944\nconfigWidget.tab_sounds.width=0.2183288409703504\nconfigWidget.tab_sounds.x=0.6091644204851752\nconfigWidget.tab_sounds.y=-0.13679245283018868\nconfigWidget.tab_zoom_stage.baseHeight=212\nconfigWidget.tab_zoom_stage.baseWidth=371\nconfigWidget.tab_zoom_stage.height=0.09433962264150944\nconfigWidget.tab_zoom_stage.width=0.2183288409703504\nconfigWidget.tab_zoom_stage.x=0.1778975741239892\nconfigWidget.tab_zoom_stage.y=-0.13679245283018868\nconfigWidget.tab_zoom_stage_2.baseHeight=212\nconfigWidget.tab_zoom_stage_2.baseWidth=371\nconfigWidget.tab_zoom_stage_2.height=0.09433962264150944\nconfigWidget.tab_zoom_stage_2.width=0.2183288409703504\nconfigWidget.tab_zoom_stage_2.x=0.3935309973045822\nconfigWidget.tab_zoom_stage_2.y=-0.13679245283018868\nconfigWidget.title.baseHeight=198\nconfigWidget.title.baseWidth=368\nconfigWidget.title.height=0.05555555555555555\nconfigWidget.title.width=0.13043478260869565\nconfigWidget.title.x=0.43478260869565216\nconfigWidget.title.y=0.020202020202020204\nconfigWidget.warp_plate_label.baseHeight=198\nconfigWidget.warp_plate_label.baseWidth=368\nconfigWidget.warp_plate_label.height=0.045454545454545456\nconfigWidget.warp_plate_label.width=0.483695652173913\nconfigWidget.warp_plate_label.x=0.05434782608695652\nconfigWidget.warp_plate_label.y=0.35353535353535354\nconfigWidget.warp_plate_toggle.baseHeight=198\nconfigWidget.warp_plate_toggle.baseWidth=368\nconfigWidget.warp_plate_toggle.height=0.10101010101010101\nconfigWidget.warp_plate_toggle.width=0.25\nconfigWidget.warp_plate_toggle.x=0.6956521739130435\nconfigWidget.warp_plate_toggle.y=0.3282828282828283\nconfigWidget.zoom_in_slider.baseHeight=198\nconfigWidget.zoom_in_slider.baseWidth=368\nconfigWidget.zoom_in_slider.height=0.2222222222222222\nconfigWidget.zoom_in_slider.width=1.0\nconfigWidget.zoom_in_slider.x=0.0\nconfigWidget.zoom_in_slider.y=0.5050505050505051\nconfigWidget.zoom_in_ticks_field.baseHeight=198\nconfigWidget.zoom_in_ticks_field.baseWidth=368\nconfigWidget.zoom_in_ticks_field.height=0.10101010101010101\nconfigWidget.zoom_in_ticks_field.width=0.37228260869565216\nconfigWidget.zoom_in_ticks_field.x=0.5760869565217391\nconfigWidget.zoom_in_ticks_field.y=0.5454545454545454\nconfigWidget.zoom_in_ticks_label.baseHeight=198\nconfigWidget.zoom_in_ticks_label.baseWidth=368\nconfigWidget.zoom_in_ticks_label.height=0.06060606060606061\nconfigWidget.zoom_in_ticks_label.width=0.42934782608695654\nconfigWidget.zoom_in_ticks_label.x=0.05434782608695652\nconfigWidget.zoom_in_ticks_label.y=0.5707070707070707\nconfigWidget.zoom_out_slider.baseHeight=195\nconfigWidget.zoom_out_slider.baseWidth=368\nconfigWidget.zoom_out_slider.height=0.22564102564102564\nconfigWidget.zoom_out_slider.width=1.0\nconfigWidget.zoom_out_slider.x=0.0\nconfigWidget.zoom_out_slider.y=0.23076923076923078\nconfigWidget.zoom_out_ticks_field.baseHeight=198\nconfigWidget.zoom_out_ticks_field.baseWidth=368\nconfigWidget.zoom_out_ticks_field.height=0.10101010101010101\nconfigWidget.zoom_out_ticks_field.width=0.37228260869565216\nconfigWidget.zoom_out_ticks_field.x=0.5760869565217391\nconfigWidget.zoom_out_ticks_field.y=0.3282828282828283\nconfigWidget.zoom_out_ticks_label.baseHeight=198\nconfigWidget.zoom_out_ticks_label.baseWidth=368\nconfigWidget.zoom_out_ticks_label.height=0.06060606060606061\nconfigWidget.zoom_out_ticks_label.width=0.42934782608695654\nconfigWidget.zoom_out_ticks_label.x=0.05434782608695652\nconfigWidget.zoom_out_ticks_label.y=0.35353535353535354\nconfigWidget.zoom_stage_glide_slider.baseHeight=195\nconfigWidget.zoom_stage_glide_slider.baseWidth=368\nconfigWidget.zoom_stage_glide_slider.height=0.22564102564102564\nconfigWidget.zoom_stage_glide_slider.width=1.0\nconfigWidget.zoom_stage_glide_slider.x=0.0\nconfigWidget.zoom_stage_glide_slider.y=0.26153846153846155\nconfigWidget.zoom_stage_glide_ticks_field.baseHeight=195\nconfigWidget.zoom_stage_glide_ticks_field.baseWidth=368\nconfigWidget.zoom_stage_glide_ticks_field.height=0.10256410256410256\nconfigWidget.zoom_stage_glide_ticks_field.width=0.25\nconfigWidget.zoom_stage_glide_ticks_field.x=0.7092391304347826\nconfigWidget.zoom_stage_glide_ticks_field.y=0.5897435897435898\nconfigWidget.zoom_stage_glide_ticks_label.baseHeight=195\nconfigWidget.zoom_stage_glide_ticks_label.baseWidth=368\nconfigWidget.zoom_stage_glide_ticks_label.height=0.06666666666666667\nconfigWidget.zoom_stage_glide_ticks_label.width=0.5652173913043478\nconfigWidget.zoom_stage_glide_ticks_label.x=0.03804347826086957\nconfigWidget.zoom_stage_glide_ticks_label.y=0.6153846153846154\ncrossDimensionTravelEnabled=false\ncustomSoundVolume=0.5\ncustomSoundsEnabled=false\neffectEnabled=true\nendZoomHeightsLinked=true\nendZoomInStage1=20\nendZoomInStage2=40\nendZoomInStage3=60\nendZoomOutStage1=20\nendZoomOutStage2=40\nendZoomOutStage3=60\nexternalTeleportTransitionsEnabled=true\nfallbackChunkFadeEnabled=true\nlocalPlayerHideTicks=2\nminecraftSoundVolume=0.5\nnetherZoomHeightsLinked=true\nnetherZoomInStage1=20\nnetherZoomInStage2=40\nnetherZoomInStage3=60\nnetherZoomOutStage1=20\nnetherZoomOutStage2=40\nnetherZoomOutStage3=60\nplayerFreezeEnabled=true\nwarpPlateTransitionsEnabled=true\nzoomHeightsLinked=true\nzoomInStage1=20\nzoomInStage2=40\nzoomInStage3=60\nzoomInStageTicks1=13\nzoomInStageTicks2=13\nzoomInStageTicks3=13\nzoomOutStage1=20\nzoomOutStage2=40\nzoomOutStage3=60\nzoomOutStageTicks1=13\nzoomOutStageTicks2=13\nzoomOutStageTicks3=13\nzoomStageGlideHeight=0.5\nzoomStageGlideTicks=13\n";
   private static final double[] DEFAULT_STAGE_HEIGHTS = new double[]{20.0, 40.0, 60.0};
   private static final double MIN_STAGE_HEIGHT = 8.0;
   private static final double MAX_STAGE_HEIGHT = 512.0;
   private static final double MIN_STAGE_GAP = 1.0;
   private static final int[] DEFAULT_STAGE_TICKS = new int[]{13, 13, 13};
   private static final int MIN_STAGE_TICKS = 1;
   private static final int MAX_STAGE_TICKS = 200;
   private static final double DEFAULT_ZOOM_STAGE_GLIDE_HEIGHT = 0.5;
   private static final double MIN_ZOOM_STAGE_GLIDE_HEIGHT = 0.1;
   private static final double MAX_ZOOM_STAGE_GLIDE_HEIGHT = 5.0;
   private static final int DEFAULT_ZOOM_STAGE_GLIDE_TICKS = 13;
   private static final double DEFAULT_BODY_CAMERA_HEIGHT = 6.0;
   private static final double MIN_BODY_CAMERA_HEIGHT = 0.1;
   private static final double MAX_BODY_CAMERA_HEIGHT = 10.0;
   private static final double DEFAULT_BODY_GLIDE_HEIGHT = 0.5;
   private static final double MIN_BODY_GLIDE_HEIGHT = 0.1;
   private static final double MAX_BODY_GLIDE_HEIGHT = 5.0;
   private static final int DEFAULT_BODY_GLIDE_TICKS = 10;
   private static final int MIN_LOCAL_PLAYER_HIDE_TICKS = 0;
   private static final int MAX_LOCAL_PLAYER_HIDE_TICKS = 20;
   private static final int DEFAULT_LOCAL_PLAYER_HIDE_TICKS = 2;
   private static final double DEFAULT_MINECRAFT_SOUND_VOLUME = 1.0;
   private static final double DEFAULT_CUSTOM_SOUND_VOLUME = 0.3;
   private static final double MIN_SOUND_VOLUME = 0.1;
   private static final double MAX_SOUND_VOLUME = 1.0;
   private static Path configPath;
   private static boolean effectEnabled = true;
   private static boolean playerFreezeEnabled = true;
   private static boolean crossDimensionTravelEnabled = false;
   private static boolean zoomHeightsLinked = true;
   private static double[] zoomOutStageHeights;
   private static double[] zoomInStageHeights;
   private static boolean netherZoomHeightsLinked;
   private static double[] netherZoomOutStageHeights;
   private static double[] netherZoomInStageHeights;
   private static boolean endZoomHeightsLinked;
   private static double[] endZoomOutStageHeights;
   private static double[] endZoomInStageHeights;
   private static int[] zoomOutStageTicks;
   private static int[] zoomInStageTicks;
   private static double zoomStageGlideHeight;
   private static int zoomStageGlideTicks;
   private static double bodyCameraHeight;
   private static double bodyGlideHeight;
   private static int bodyGlideTicks;
   private static int localPlayerHideTicks;
   private static boolean customSoundsEnabled;
   private static double minecraftSoundVolume;
   private static double customSoundVolume;
   private static boolean warpPlateTransitionsEnabled;
   private static boolean externalTeleportTransitionsEnabled;
   private static boolean fallbackChunkFadeEnabled;
   private static boolean configLayoutEditorButtonVisible;
   private static boolean configLayoutDebugEnabled;
   private static boolean configLayoutAspectLocked;
   private static boolean configLayoutGridEnabled;
   private static boolean configLayoutSnapEnabled;
   private static boolean configLayoutCustom;
   private static double configLayoutX;
   private static double configLayoutY;
   private static double configLayoutWidth;
   private static double configLayoutHeight;
   private static int configLayoutBaseWidth;
   private static int configLayoutBaseHeight;
   private static final Map<String, double[]> configWidgetLayouts;
   private static final Map<String, String> configTexts;

   private GtaLikeTeleportConfig() {
   }

   static void load() {
      configPath = resolveConfigPath();
      migrateLegacyConfig();
      resetToDefaults();
      if (Files.exists(configPath, new LinkOption[0])) {
         boolean rewriteConfig = false;
         Properties properties = new Properties();

         try {
            InputStream input = Files.newInputStream(configPath);

            try {
               properties.load(input);
               rewriteConfig = prepareLoadedProperties(properties);
               applyConfigProperties(properties);
               rewriteConfig = rewriteConfig || !properties.containsKey("configLayoutEditorButtonVisible");
            } catch (Throwable var6) {
               if (input != null) {
                  try {
                     input.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (input != null) {
               input.close();
            }
         } catch (IOException var7) {
            resetToDefaults();
         }

         if (rewriteConfig) {
            save();
         }

      }
   }

   static boolean isEffectEnabled() {
      return effectEnabled;
   }

   static boolean setEffectEnabled(boolean enabled) {
      effectEnabled = enabled;
      return save();
   }

   static boolean isPlayerFreezeEnabled() {
      return playerFreezeEnabled;
   }

   static boolean setPlayerFreezeEnabled(boolean enabled) {
      playerFreezeEnabled = enabled;
      return save();
   }

   static boolean isCrossDimensionTravelEnabled() {
      return crossDimensionTravelEnabled;
   }

   static boolean setCrossDimensionTravelEnabled(boolean enabled) {
      crossDimensionTravelEnabled = enabled;
      return save();
   }

   static boolean areZoomHeightsLinked() {
      return areZoomHeightsLinked(GtaLikeTeleportConfig.ZoomDimension.OVERWORLD);
   }

   static boolean areZoomHeightsLinked(ZoomDimension dimension) {
      boolean var10000;
      switch (sanitizeZoomDimension(dimension).ordinal()) {
         case 1:
            var10000 = netherZoomHeightsLinked;
            break;
         case 2:
            var10000 = endZoomHeightsLinked;
            break;
         default:
            var10000 = zoomHeightsLinked;
      }

      return var10000;
   }

   static double[] getZoomOutStageHeights() {
      return getZoomOutStageHeights(GtaLikeTeleportConfig.ZoomDimension.OVERWORLD);
   }

   static double[] getZoomOutStageHeights(ZoomDimension dimension) {
      double[] var10000;
      switch (sanitizeZoomDimension(dimension).ordinal()) {
         case 1:
            var10000 = (double[])netherZoomOutStageHeights.clone();
            break;
         case 2:
            var10000 = (double[])endZoomOutStageHeights.clone();
            break;
         default:
            var10000 = (double[])zoomOutStageHeights.clone();
      }

      return var10000;
   }

   static double[] getZoomInStageHeights() {
      return getZoomInStageHeights(GtaLikeTeleportConfig.ZoomDimension.OVERWORLD);
   }

   static double[] getZoomInStageHeights(ZoomDimension dimension) {
      ZoomDimension safeDimension = sanitizeZoomDimension(dimension);
      return areZoomHeightsLinked(safeDimension) ? getZoomOutStageHeights(safeDimension) : getRawZoomInStageHeights(safeDimension);
   }

   static double[] getRawZoomInStageHeights() {
      return getRawZoomInStageHeights(GtaLikeTeleportConfig.ZoomDimension.OVERWORLD);
   }

   static double[] getRawZoomInStageHeights(ZoomDimension dimension) {
      double[] var10000;
      switch (sanitizeZoomDimension(dimension).ordinal()) {
         case 1:
            var10000 = (double[])netherZoomInStageHeights.clone();
            break;
         case 2:
            var10000 = (double[])endZoomInStageHeights.clone();
            break;
         default:
            var10000 = (double[])zoomInStageHeights.clone();
      }

      return var10000;
   }

   static boolean setZoomStageHeights(boolean linked, double[] zoomOutHeights, double[] zoomInHeights) {
      return setZoomStageHeights(GtaLikeTeleportConfig.ZoomDimension.OVERWORLD, linked, zoomOutHeights, zoomInHeights);
   }

   static boolean setZoomStageHeights(ZoomDimension dimension, boolean linked, double[] zoomOutHeights, double[] zoomInHeights) {
      double[] sanitizedOut = sanitizeStageHeights(zoomOutHeights);
      double[] sanitizedIn = sanitizeStageHeights(linked ? sanitizedOut : zoomInHeights);
      switch (sanitizeZoomDimension(dimension).ordinal()) {
         case 1:
            netherZoomHeightsLinked = linked;
            netherZoomOutStageHeights = sanitizedOut;
            netherZoomInStageHeights = sanitizedIn;
            break;
         case 2:
            endZoomHeightsLinked = linked;
            endZoomOutStageHeights = sanitizedOut;
            endZoomInStageHeights = sanitizedIn;
            break;
         default:
            zoomHeightsLinked = linked;
            zoomOutStageHeights = sanitizedOut;
            zoomInStageHeights = sanitizedIn;
      }

      return save();
   }

   static int[] getZoomOutStageTicks() {
      return (int[])zoomOutStageTicks.clone();
   }

   static int[] getZoomInStageTicks() {
      return (int[])zoomInStageTicks.clone();
   }

   static boolean setZoomStageTicks(int[] zoomOutTicks, int[] zoomInTicks) {
      zoomOutStageTicks = sanitizeStageTicks(zoomOutTicks);
      zoomInStageTicks = sanitizeStageTicks(zoomInTicks);
      return save();
   }

   static double getZoomStageGlideHeight() {
      return zoomStageGlideHeight;
   }

   static boolean setZoomStageGlideHeight(double height) {
      zoomStageGlideHeight = sanitizeZoomStageGlideHeight(height);
      return save();
   }

   static int getZoomStageGlideTicks() {
      return zoomStageGlideTicks;
   }

   static boolean setZoomStageGlideTicks(int ticks) {
      zoomStageGlideTicks = sanitizeStageTicksValue(ticks);
      return save();
   }

   static double getBodyCameraHeight() {
      return bodyCameraHeight;
   }

   static boolean setBodyCameraHeight(double height) {
      bodyCameraHeight = sanitizeBodyCameraHeight(height);
      return save();
   }

   static double getBodyGlideHeight() {
      return bodyGlideHeight;
   }

   static boolean setBodyGlideHeight(double height) {
      bodyGlideHeight = sanitizeBodyGlideHeight(height);
      return save();
   }

   static int getBodyGlideTicks() {
      return bodyGlideTicks;
   }

   static boolean setBodyGlideTicks(int ticks) {
      bodyGlideTicks = sanitizeStageTicksValue(ticks);
      return save();
   }

   static int getLocalPlayerHideTicks() {
      return localPlayerHideTicks;
   }

   static boolean setLocalPlayerHideTicks(int ticks) {
      localPlayerHideTicks = sanitizeLocalPlayerHideTicks(ticks);
      return save();
   }

   static boolean isCustomSoundsEnabled() {
      return customSoundsEnabled;
   }

   static boolean setCustomSoundsEnabled(boolean enabled) {
      customSoundsEnabled = enabled;
      return save();
   }

   static double getMinecraftSoundVolume() {
      return minecraftSoundVolume;
   }

   static boolean setMinecraftSoundVolume(double volume) {
      minecraftSoundVolume = sanitizeSoundVolume(volume);
      return save();
   }

   static double getCustomSoundVolume() {
      return customSoundVolume;
   }

   static boolean setCustomSoundVolume(double volume) {
      customSoundVolume = sanitizeSoundVolume(volume);
      return save();
   }

   static boolean isWarpPlateTransitionsEnabled() {
      return warpPlateTransitionsEnabled;
   }

   static boolean setWarpPlateTransitionsEnabled(boolean enabled) {
      warpPlateTransitionsEnabled = enabled;
      return save();
   }

   static boolean isExternalTeleportTransitionsEnabled() {
      return externalTeleportTransitionsEnabled;
   }

   static boolean setExternalTeleportTransitionsEnabled(boolean enabled) {
      externalTeleportTransitionsEnabled = enabled;
      return save();
   }

   static boolean isFallbackChunkFadeEnabled() {
      return fallbackChunkFadeEnabled;
   }

   static boolean setFallbackChunkFadeEnabled(boolean enabled) {
      fallbackChunkFadeEnabled = enabled;
      return save();
   }

   static double[] sanitizeStageHeights(double[] values) {
      double[] source = values != null && values.length >= 3 ? values : DEFAULT_STAGE_HEIGHTS;
      double[] sanitized = new double[]{clamp(roundStageHeight(source[0]), 8.0, 510.0), 0.0, 0.0};
      sanitized[1] = clamp(roundStageHeight(source[1]), sanitized[0] + 1.0, 511.0);
      sanitized[2] = clamp(roundStageHeight(source[2]), sanitized[1] + 1.0, 512.0);
      return sanitized;
   }

   static int[] sanitizeStageTicks(int[] values) {
      int[] source = values != null && values.length >= 3 ? values : DEFAULT_STAGE_TICKS;
      int[] sanitized = new int[3];

      for(int i = 0; i < sanitized.length; ++i) {
         sanitized[i] = sanitizeStageTicksValue(source[i]);
      }

      return sanitized;
   }

   static double sanitizeZoomStageGlideHeight(double value) {
      return (double)Math.round(clamp(value, 0.1, 5.0) * 10.0) / 10.0;
   }

   static double sanitizeBodyCameraHeight(double value) {
      return (double)Math.round(clamp(value, 0.1, 10.0) * 10.0) / 10.0;
   }

   static double sanitizeBodyGlideHeight(double value) {
      return (double)Math.round(clamp(value, 0.1, 5.0) * 10.0) / 10.0;
   }

   static int sanitizeStageTicksValue(int value) {
      return clamp(value, 1, 200);
   }

   static int sanitizeLocalPlayerHideTicks(int value) {
      return clamp(value, 0, 20);
   }

   static double sanitizeSoundVolume(double value) {
      return (double)Math.round(clamp(value, 0.1, 1.0) * 10.0) / 10.0;
   }

   static double getMinStageHeight() {
      return 8.0;
   }

   static double getMaxStageHeight() {
      return 512.0;
   }

   static double getMinStageGap() {
      return 1.0;
   }

   static double[] getDefaultStageHeights() {
      return (double[])DEFAULT_STAGE_HEIGHTS.clone();
   }

   static int[] getDefaultStageTicks() {
      return (int[])DEFAULT_STAGE_TICKS.clone();
   }

   static int getMinStageTicks() {
      return 1;
   }

   static int getMaxStageTicks() {
      return 200;
   }

   static double getDefaultZoomStageGlideHeight() {
      return 0.5;
   }

   static double getMinZoomStageGlideHeight() {
      return 0.1;
   }

   static double getMaxZoomStageGlideHeight() {
      return 5.0;
   }

   static int getDefaultZoomStageGlideTicks() {
      return 13;
   }

   static double getDefaultBodyCameraHeight() {
      return 6.0;
   }

   static double getMinBodyCameraHeight() {
      return 0.1;
   }

   static double getMaxBodyCameraHeight() {
      return 10.0;
   }

   static double getDefaultBodyGlideHeight() {
      return 0.5;
   }

   static double getMinBodyGlideHeight() {
      return 0.1;
   }

   static double getMaxBodyGlideHeight() {
      return 5.0;
   }

   static int getDefaultBodyGlideTicks() {
      return 10;
   }

   static int getDefaultLocalPlayerHideTicks() {
      return 2;
   }

   static int getMinLocalPlayerHideTicks() {
      return 0;
   }

   static int getMaxLocalPlayerHideTicks() {
      return 20;
   }

   static double getDefaultMinecraftSoundVolume() {
      return 1.0;
   }

   static double getDefaultCustomSoundVolume() {
      return 0.3;
   }

   static double getMinSoundVolume() {
      return 0.1;
   }

   static double getMaxSoundVolume() {
      return 1.0;
   }

   static boolean isConfigLayoutEditorButtonVisible() {
      return configLayoutEditorButtonVisible;
   }

   static boolean setConfigLayoutEditorButtonVisible(boolean visible) {
      configLayoutEditorButtonVisible = visible;
      return save();
   }

   static boolean isConfigLayoutDebugEnabled() {
      return configLayoutDebugEnabled;
   }

   static boolean setConfigLayoutDebugEnabled(boolean enabled) {
      configLayoutDebugEnabled = enabled;
      return save();
   }

   static boolean isConfigLayoutAspectLocked() {
      return configLayoutAspectLocked;
   }

   static boolean setConfigLayoutAspectLocked(boolean locked) {
      configLayoutAspectLocked = locked;
      return save();
   }

   static boolean isConfigLayoutGridEnabled() {
      return configLayoutGridEnabled;
   }

   static boolean setConfigLayoutGridEnabled(boolean enabled) {
      configLayoutGridEnabled = enabled;
      return save();
   }

   static boolean isConfigLayoutSnapEnabled() {
      return configLayoutSnapEnabled;
   }

   static boolean setConfigLayoutSnapEnabled(boolean enabled) {
      configLayoutSnapEnabled = enabled;
      return save();
   }

   static boolean hasCustomConfigLayout() {
      return configLayoutCustom;
   }

   static double[] getConfigLayout() {
      return new double[]{configLayoutX, configLayoutY, configLayoutWidth, configLayoutHeight};
   }

   static int getConfigLayoutBaseWidth() {
      return configLayoutBaseWidth;
   }

   static int getConfigLayoutBaseHeight() {
      return configLayoutBaseHeight;
   }

   static boolean setConfigLayout(double x, double y, double width, double height) {
      return setConfigLayout(x, y, width, height, configLayoutBaseWidth, configLayoutBaseHeight);
   }

   static boolean setConfigLayout(double x, double y, double width, double height, int baseWidth, int baseHeight) {
      configLayoutCustom = true;
      configLayoutX = clamp(x, 0.0, 1.0);
      configLayoutY = clamp(y, 0.0, 1.0);
      configLayoutWidth = clamp(width, 0.0, 1.0);
      configLayoutHeight = clamp(height, 0.0, 1.0);
      configLayoutBaseWidth = Math.max(1, baseWidth);
      configLayoutBaseHeight = Math.max(1, baseHeight);
      return save();
   }

   static boolean resetConfigLayout() {
      Properties defaults = createDefaultProperties();
      configLayoutCustom = Boolean.parseBoolean(defaults.getProperty("configLayoutCustom", Boolean.toString(configLayoutCustom)));
      configLayoutX = readUnitDouble(defaults, "configLayoutX", configLayoutX);
      configLayoutY = readUnitDouble(defaults, "configLayoutY", configLayoutY);
      configLayoutWidth = readUnitDouble(defaults, "configLayoutWidth", configLayoutWidth);
      configLayoutHeight = readUnitDouble(defaults, "configLayoutHeight", configLayoutHeight);
      configLayoutBaseWidth = readPositiveInt(defaults, "configLayoutBaseWidth", configLayoutBaseWidth);
      configLayoutBaseHeight = readPositiveInt(defaults, "configLayoutBaseHeight", configLayoutBaseHeight);
      return save();
   }

   static boolean hasConfigWidgetLayout(String id) {
      return configWidgetLayouts.containsKey(id);
   }

   static double[] getConfigWidgetLayout(String id) {
      double[] values = (double[])configWidgetLayouts.get(id);
      return values == null ? new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0} : (double[])values.clone();
   }

   static boolean setConfigWidgetLayout(String id, double x, double y, double width, double height) {
      return setConfigWidgetLayout(id, x, y, width, height, 0, 0);
   }

   static boolean setConfigWidgetLayout(String id, double x, double y, double width, double height, int baseWidth, int baseHeight) {
      if (!isSafeId(id)) {
         return false;
      } else {
         configWidgetLayouts.put(id, new double[]{clamp(x, -2.0, 3.0), clamp(y, -2.0, 3.0), clamp(width, 0.01, 3.0), clamp(height, 0.01, 3.0), (double)Math.max(0, baseWidth), (double)Math.max(0, baseHeight)});
         return save();
      }
   }

   static boolean resetConfigWidgetLayout(String id) {
      if (!isSafeId(id)) {
         return false;
      } else {
         Properties defaults = createDefaultProperties();
         String prefix = "configWidget." + id;
         double x = readDouble(defaults, prefix + ".x", 0.0);
         double y = readDouble(defaults, prefix + ".y", 0.0);
         double width = readDouble(defaults, prefix + ".width", 0.0);
         double height = readDouble(defaults, prefix + ".height", 0.0);
         int baseWidth = readPositiveInt(defaults, prefix + ".baseWidth", 0);
         int baseHeight = readPositiveInt(defaults, prefix + ".baseHeight", 0);
         if (width > 0.0 && height > 0.0) {
            configWidgetLayouts.put(id, new double[]{clamp(x, -2.0, 3.0), clamp(y, -2.0, 3.0), clamp(width, 0.01, 3.0), clamp(height, 0.01, 3.0), (double)baseWidth, (double)baseHeight});
         } else {
            configWidgetLayouts.remove(id);
         }

         return save();
      }
   }

   static boolean resetConfigWidgetLayouts() {
      readWidgetLayouts(createDefaultProperties());
      return save();
   }

   static String getConfigText(String id, String fallback) {
      String value = (String)configTexts.get(id);
      return value == null ? fallback : value;
   }

   static boolean setConfigText(String id, String text) {
      if (!isSafeId(id)) {
         return false;
      } else {
         if (text != null && !text.isEmpty()) {
            configTexts.put(id, text);
         } else {
            configTexts.remove(id);
         }

         return save();
      }
   }

   static boolean resetConfigText(String id) {
      if (!isSafeId(id)) {
         return false;
      } else {
         String value = createDefaultProperties().getProperty("configText." + id);
         if (value != null && !value.isEmpty()) {
            configTexts.put(id, value);
         } else {
            configTexts.remove(id);
         }

         return save();
      }
   }

   private static void applyConfigProperties(Properties properties) {
      effectEnabled = Boolean.parseBoolean(properties.getProperty("effectEnabled", Boolean.toString(effectEnabled)));
      playerFreezeEnabled = Boolean.parseBoolean(properties.getProperty("playerFreezeEnabled", Boolean.toString(playerFreezeEnabled)));
      crossDimensionTravelEnabled = Boolean.parseBoolean(properties.getProperty("crossDimensionTravelEnabled", Boolean.toString(crossDimensionTravelEnabled)));
      zoomHeightsLinked = Boolean.parseBoolean(properties.getProperty("zoomHeightsLinked", Boolean.toString(zoomHeightsLinked)));
      zoomOutStageHeights = readStageHeights(properties, "zoomOutStage", DEFAULT_STAGE_HEIGHTS);
      zoomInStageHeights = readStageHeights(properties, "zoomInStage", DEFAULT_STAGE_HEIGHTS);
      if (zoomHeightsLinked) {
         zoomInStageHeights = (double[])zoomOutStageHeights.clone();
      }

      netherZoomHeightsLinked = Boolean.parseBoolean(properties.getProperty("netherZoomHeightsLinked", Boolean.toString(zoomHeightsLinked)));
      netherZoomOutStageHeights = readStageHeights(properties, "netherZoomOutStage", zoomOutStageHeights);
      netherZoomInStageHeights = readStageHeights(properties, "netherZoomInStage", zoomInStageHeights);
      if (netherZoomHeightsLinked) {
         netherZoomInStageHeights = (double[])netherZoomOutStageHeights.clone();
      }

      endZoomHeightsLinked = Boolean.parseBoolean(properties.getProperty("endZoomHeightsLinked", Boolean.toString(zoomHeightsLinked)));
      endZoomOutStageHeights = readStageHeights(properties, "endZoomOutStage", zoomOutStageHeights);
      endZoomInStageHeights = readStageHeights(properties, "endZoomInStage", zoomInStageHeights);
      if (endZoomHeightsLinked) {
         endZoomInStageHeights = (double[])endZoomOutStageHeights.clone();
      }

      zoomOutStageTicks = readStageTicks(properties, "zoomOutStageTicks", DEFAULT_STAGE_TICKS);
      zoomInStageTicks = readStageTicks(properties, "zoomInStageTicks", DEFAULT_STAGE_TICKS);
      zoomStageGlideHeight = readClampedDouble(properties, "zoomStageGlideHeight", zoomStageGlideHeight, 0.1, 5.0);
      zoomStageGlideTicks = readClampedInt(properties, "zoomStageGlideTicks", zoomStageGlideTicks, 1, 200);
      bodyCameraHeight = readClampedDouble(properties, "bodyCameraHeight", bodyCameraHeight, 0.1, 10.0);
      bodyGlideHeight = readClampedDouble(properties, "bodyGlideHeight", bodyGlideHeight, 0.1, 5.0);
      bodyGlideTicks = readClampedInt(properties, "bodyGlideTicks", bodyGlideTicks, 1, 200);
      localPlayerHideTicks = readClampedInt(properties, "localPlayerHideTicks", localPlayerHideTicks, 0, 20);
      customSoundsEnabled = Boolean.parseBoolean(properties.getProperty("customSoundsEnabled", Boolean.toString(customSoundsEnabled)));
      minecraftSoundVolume = readClampedDouble(properties, "minecraftSoundVolume", minecraftSoundVolume, 0.1, 1.0);
      customSoundVolume = readClampedDouble(properties, "customSoundVolume", customSoundVolume, 0.1, 1.0);
      warpPlateTransitionsEnabled = Boolean.parseBoolean(properties.getProperty("warpPlateTransitionsEnabled", Boolean.toString(warpPlateTransitionsEnabled)));
      externalTeleportTransitionsEnabled = Boolean.parseBoolean(properties.getProperty("externalTeleportTransitionsEnabled", Boolean.toString(externalTeleportTransitionsEnabled)));
      fallbackChunkFadeEnabled = Boolean.parseBoolean(properties.getProperty("fallbackChunkFadeEnabled", Boolean.toString(fallbackChunkFadeEnabled)));
      configLayoutEditorButtonVisible = Boolean.parseBoolean(properties.getProperty("configLayoutEditorButtonVisible", Boolean.toString(configLayoutEditorButtonVisible)));
      configLayoutDebugEnabled = Boolean.parseBoolean(properties.getProperty("configLayoutDebugEnabled", Boolean.toString(configLayoutDebugEnabled)));
      configLayoutAspectLocked = Boolean.parseBoolean(properties.getProperty("configLayoutAspectLocked", Boolean.toString(configLayoutAspectLocked)));
      configLayoutGridEnabled = Boolean.parseBoolean(properties.getProperty("configLayoutGridEnabled", Boolean.toString(configLayoutGridEnabled)));
      configLayoutSnapEnabled = Boolean.parseBoolean(properties.getProperty("configLayoutSnapEnabled", Boolean.toString(configLayoutSnapEnabled)));
      configLayoutCustom = Boolean.parseBoolean(properties.getProperty("configLayoutCustom", Boolean.toString(configLayoutCustom)));
      configLayoutX = readUnitDouble(properties, "configLayoutX", configLayoutX);
      configLayoutY = readUnitDouble(properties, "configLayoutY", configLayoutY);
      configLayoutWidth = readUnitDouble(properties, "configLayoutWidth", configLayoutWidth);
      configLayoutHeight = readUnitDouble(properties, "configLayoutHeight", configLayoutHeight);
      configLayoutBaseWidth = readPositiveInt(properties, "configLayoutBaseWidth", configLayoutBaseWidth);
      configLayoutBaseHeight = readPositiveInt(properties, "configLayoutBaseHeight", configLayoutBaseHeight);
      readWidgetLayouts(properties);
      readConfigTexts(properties);
   }

   private static void resetToDefaults() {
      applyConfigProperties(createDefaultProperties());
   }

   private static Properties createDefaultProperties() {
      Properties properties = new Properties();

      try {
         properties.load(new StringReader("bodyCameraHeight=6.0\nbodyGlideHeight=0.5\nbodyGlideTicks=10\nconfigLayoutAspectLocked=false\nconfigLayoutBaseHeight=353\nconfigLayoutBaseWidth=640\nconfigLayoutCustom=true\nconfigLayoutDebugEnabled=false\nconfigLayoutEditorButtonVisible=false\nconfigLayoutGridEnabled=true\nconfigLayoutHeight=0.6005665722379604\nconfigLayoutSnapEnabled=false\nconfigLayoutWidth=0.5796875\nconfigLayoutX=0.2109375\nconfigLayoutY=0.23796033994334279\nconfigText.advanced1_title=GTP Advanced Settings (1)\nconfigText.advanced2_description=Set tick lengths for each zoom stage. (1st / 2nd / 3rd)\nconfigText.advanced2_title=ZoomStage Settings (2)\nconfigText.advanced3_title=GTP Advanced Settings (3)\nconfigText.done_button=Close\nconfigText.fallback_chunk_fade_label=Vanilla/Sodium chunk-mask fade\nconfigText.general_title=General Settings\nconfigText.linked_slider=\\    camera_zoom 1st / 2nd / 3rd\nconfigText.others_title=Other Settings\nconfigText.reset_button=Reset\nconfigText.sounds_title=Sound Settings\nconfigText.title=ZoomStage Settings\nconfigText.zoom_out_ticks_label=Zoom-out stage ticks\nconfigWidget.advanced1_description.baseHeight=195\nconfigWidget.advanced1_description.baseWidth=368\nconfigWidget.advanced1_description.height=0.05128205128205128\nconfigWidget.advanced1_description.width=0.5407608695652174\nconfigWidget.advanced1_description.x=0.22826086956521738\nconfigWidget.advanced1_description.y=0.13333333333333333\nconfigWidget.advanced1_title.baseHeight=195\nconfigWidget.advanced1_title.baseWidth=368\nconfigWidget.advanced1_title.height=0.05128205128205128\nconfigWidget.advanced1_title.width=0.24456521739130435\nconfigWidget.advanced1_title.x=0.37771739130434784\nconfigWidget.advanced1_title.y=0.015384615384615385\nconfigWidget.advanced2_description.baseHeight=212\nconfigWidget.advanced2_description.baseWidth=371\nconfigWidget.advanced2_description.height=0.04716981132075472\nconfigWidget.advanced2_description.width=0.7601078167115903\nconfigWidget.advanced2_description.x=0.11859838274932614\nconfigWidget.advanced2_description.y=0.15566037735849056\nconfigWidget.advanced2_title.baseHeight=198\nconfigWidget.advanced2_title.baseWidth=368\nconfigWidget.advanced2_title.height=0.050505050505050504\nconfigWidget.advanced2_title.width=0.24456521739130435\nconfigWidget.advanced2_title.x=0.37771739130434784\nconfigWidget.advanced2_title.y=0.020202020202020204\nconfigWidget.advanced3_description.baseHeight=195\nconfigWidget.advanced3_description.baseWidth=368\nconfigWidget.advanced3_description.height=0.05128205128205128\nconfigWidget.advanced3_description.width=0.6059782608695652\nconfigWidget.advanced3_description.x=0.1956521739130435\nconfigWidget.advanced3_description.y=0.13333333333333333\nconfigWidget.advanced3_title.baseHeight=195\nconfigWidget.advanced3_title.baseWidth=368\nconfigWidget.advanced3_title.height=0.05128205128205128\nconfigWidget.advanced3_title.width=0.24456521739130435\nconfigWidget.advanced3_title.x=0.37771739130434784\nconfigWidget.advanced3_title.y=0.015384615384615385\nconfigWidget.body_glide_slider.baseHeight=195\nconfigWidget.body_glide_slider.baseWidth=368\nconfigWidget.body_glide_slider.height=0.24102564102564103\nconfigWidget.body_glide_slider.width=0.5\nconfigWidget.body_glide_slider.x=0.5\nconfigWidget.body_glide_slider.y=0.2153846153846154\nconfigWidget.body_glide_ticks_field.baseHeight=195\nconfigWidget.body_glide_ticks_field.baseWidth=368\nconfigWidget.body_glide_ticks_field.height=0.10256410256410256\nconfigWidget.body_glide_ticks_field.width=0.25\nconfigWidget.body_glide_ticks_field.x=0.7038043478260869\nconfigWidget.body_glide_ticks_field.y=0.5025641025641026\nconfigWidget.body_glide_ticks_label.baseHeight=195\nconfigWidget.body_glide_ticks_label.baseWidth=368\nconfigWidget.body_glide_ticks_label.height=0.07179487179487179\nconfigWidget.body_glide_ticks_label.width=0.5652173913043478\nconfigWidget.body_glide_ticks_label.x=0.043478260869565216\nconfigWidget.body_glide_ticks_label.y=0.5333333333333333\nconfigWidget.body_height_slider.baseHeight=195\nconfigWidget.body_height_slider.baseWidth=368\nconfigWidget.body_height_slider.height=0.24102564102564103\nconfigWidget.body_height_slider.width=0.5\nconfigWidget.body_height_slider.x=0.0\nconfigWidget.body_height_slider.y=0.2153846153846154\nconfigWidget.cross_dimension_travel_label.baseHeight=212\nconfigWidget.cross_dimension_travel_label.baseWidth=371\nconfigWidget.cross_dimension_travel_label.height=0.05660377358490566\nconfigWidget.cross_dimension_travel_label.width=0.5417789757412399\nconfigWidget.cross_dimension_travel_label.x=0.05390835579514825\nconfigWidget.cross_dimension_travel_label.y=0.6132075471698113\nconfigWidget.cross_dimension_travel_toggle.baseHeight=212\nconfigWidget.cross_dimension_travel_toggle.baseWidth=371\nconfigWidget.cross_dimension_travel_toggle.height=0.09433962264150944\nconfigWidget.cross_dimension_travel_toggle.width=0.25067385444743934\nconfigWidget.cross_dimension_travel_toggle.x=0.6954177897574124\nconfigWidget.cross_dimension_travel_toggle.y=0.589622641509434\nconfigWidget.custom_volume_slider.baseHeight=297\nconfigWidget.custom_volume_slider.baseWidth=551\nconfigWidget.custom_volume_slider.height=0.2222222222222222\nconfigWidget.custom_volume_slider.width=0.5009074410163339\nconfigWidget.custom_volume_slider.x=0.5009074410163339\nconfigWidget.custom_volume_slider.y=0.4612794612794613\nconfigWidget.description.baseHeight=212\nconfigWidget.description.baseWidth=371\nconfigWidget.description.height=0.05188679245283019\nconfigWidget.description.width=0.6738544474393531\nconfigWidget.description.x=0.16172506738544473\nconfigWidget.description.y=0.15566037735849056\nconfigWidget.dimension_end.baseHeight=198\nconfigWidget.dimension_end.baseWidth=368\nconfigWidget.dimension_end.height=0.08080808080808081\nconfigWidget.dimension_end.width=0.043478260869565216\nconfigWidget.dimension_end.x=0.5244565217391305\nconfigWidget.dimension_end.y=0.8939393939393939\nconfigWidget.dimension_nether.baseHeight=198\nconfigWidget.dimension_nether.baseWidth=368\nconfigWidget.dimension_nether.height=0.08080808080808081\nconfigWidget.dimension_nether.width=0.043478260869565216\nconfigWidget.dimension_nether.x=0.47554347826086957\nconfigWidget.dimension_nether.y=0.8939393939393939\nconfigWidget.dimension_overworld.baseHeight=198\nconfigWidget.dimension_overworld.baseWidth=368\nconfigWidget.dimension_overworld.height=0.08080808080808081\nconfigWidget.dimension_overworld.width=0.043478260869565216\nconfigWidget.dimension_overworld.x=0.4266304347826087\nconfigWidget.dimension_overworld.y=0.8939393939393939\nconfigWidget.done_button.baseHeight=198\nconfigWidget.done_button.baseWidth=368\nconfigWidget.done_button.height=0.10101010101010101\nconfigWidget.done_button.width=0.44565217391304346\nconfigWidget.done_button.x=0.5543478260869565\nconfigWidget.done_button.y=0.7929292929292929\nconfigWidget.effect_label.baseHeight=212\nconfigWidget.effect_label.baseWidth=371\nconfigWidget.effect_label.height=0.05188679245283019\nconfigWidget.effect_label.width=0.31805929919137466\nconfigWidget.effect_label.x=0.05390835579514825\nconfigWidget.effect_label.y=0.3113207547169811\nconfigWidget.effect_toggle.baseHeight=212\nconfigWidget.effect_toggle.baseWidth=371\nconfigWidget.effect_toggle.height=0.09433962264150944\nconfigWidget.effect_toggle.width=0.25067385444743934\nconfigWidget.effect_toggle.x=0.6954177897574124\nconfigWidget.effect_toggle.y=0.28773584905660377\nconfigWidget.external_teleport_label.baseHeight=198\nconfigWidget.external_teleport_label.baseWidth=368\nconfigWidget.external_teleport_label.height=0.045454545454545456\nconfigWidget.external_teleport_label.width=0.483695652173913\nconfigWidget.external_teleport_label.x=0.05434782608695652\nconfigWidget.external_teleport_label.y=0.5707070707070707\nconfigWidget.external_teleport_toggle.baseHeight=198\nconfigWidget.external_teleport_toggle.baseWidth=368\nconfigWidget.external_teleport_toggle.height=0.10101010101010101\nconfigWidget.external_teleport_toggle.width=0.25\nconfigWidget.external_teleport_toggle.x=0.6956521739130435\nconfigWidget.external_teleport_toggle.y=0.5454545454545454\nconfigWidget.fallback_chunk_fade_label.baseHeight=132\nconfigWidget.fallback_chunk_fade_label.baseWidth=246\nconfigWidget.fallback_chunk_fade_label.height=0.06060606060606061\nconfigWidget.fallback_chunk_fade_label.width=0.483739837398374\nconfigWidget.fallback_chunk_fade_label.x=0.052845528455284556\nconfigWidget.fallback_chunk_fade_label.y=0.6287878787878788\nconfigWidget.fallback_chunk_fade_toggle.baseHeight=198\nconfigWidget.fallback_chunk_fade_toggle.baseWidth=368\nconfigWidget.fallback_chunk_fade_toggle.height=0.10101010101010101\nconfigWidget.fallback_chunk_fade_toggle.width=0.25\nconfigWidget.fallback_chunk_fade_toggle.x=0.6956521739130435\nconfigWidget.fallback_chunk_fade_toggle.y=0.601010101010101\nconfigWidget.general_description.baseHeight=212\nconfigWidget.general_description.baseWidth=371\nconfigWidget.general_description.height=0.05188679245283019\nconfigWidget.general_description.width=0.31266846361185985\nconfigWidget.general_description.x=0.19137466307277629\nconfigWidget.general_description.y=0.15566037735849056\nconfigWidget.general_title.baseHeight=198\nconfigWidget.general_title.baseWidth=368\nconfigWidget.general_title.height=0.050505050505050504\nconfigWidget.general_title.width=0.3125\nconfigWidget.general_title.x=0.3451086956521739\nconfigWidget.general_title.y=0.020202020202020204\nconfigWidget.link_button.baseHeight=198\nconfigWidget.link_button.baseWidth=368\nconfigWidget.link_button.height=0.10101010101010101\nconfigWidget.link_button.width=0.05434782608695652\nconfigWidget.link_button.x=0.47282608695652173\nconfigWidget.link_button.y=0.7929292929292929\nconfigWidget.linked_slider.baseHeight=0\nconfigWidget.linked_slider.baseWidth=0\nconfigWidget.linked_slider.height=0.2571428571428571\nconfigWidget.linked_slider.width=1.0\nconfigWidget.linked_slider.x=0.0\nconfigWidget.linked_slider.y=0.3314285714285714\nconfigWidget.minecraft_volume_slider.baseHeight=297\nconfigWidget.minecraft_volume_slider.baseWidth=551\nconfigWidget.minecraft_volume_slider.height=0.2222222222222222\nconfigWidget.minecraft_volume_slider.width=0.5009074410163339\nconfigWidget.minecraft_volume_slider.x=0.0\nconfigWidget.minecraft_volume_slider.y=0.4612794612794613\nconfigWidget.movement_label.baseHeight=212\nconfigWidget.movement_label.baseWidth=371\nconfigWidget.movement_label.height=0.06132075471698113\nconfigWidget.movement_label.width=0.4366576819407008\nconfigWidget.movement_label.x=0.05390835579514825\nconfigWidget.movement_label.y=0.46226415094339623\nconfigWidget.movement_toggle.baseHeight=212\nconfigWidget.movement_toggle.baseWidth=371\nconfigWidget.movement_toggle.height=0.09433962264150944\nconfigWidget.movement_toggle.width=0.25067385444743934\nconfigWidget.movement_toggle.x=0.6954177897574124\nconfigWidget.movement_toggle.y=0.4386792452830189\nconfigWidget.others_description.baseHeight=212\nconfigWidget.others_description.baseWidth=371\nconfigWidget.others_description.height=0.04716981132075472\nconfigWidget.others_description.width=0.8382749326145552\nconfigWidget.others_description.x=0.08086253369272237\nconfigWidget.others_description.y=0.15566037735849056\nconfigWidget.others_title.baseHeight=198\nconfigWidget.others_title.baseWidth=368\nconfigWidget.others_title.height=0.050505050505050504\nconfigWidget.others_title.width=0.2826086956521739\nconfigWidget.others_title.x=0.358695652173913\nconfigWidget.others_title.y=0.020202020202020204\nconfigWidget.player_hide_label.baseHeight=195\nconfigWidget.player_hide_label.baseWidth=368\nconfigWidget.player_hide_label.height=0.06153846153846154\nconfigWidget.player_hide_label.width=0.5652173913043478\nconfigWidget.player_hide_label.x=0.043478260869565216\nconfigWidget.player_hide_label.y=0.676923076923077\nconfigWidget.player_hide_slider.baseHeight=195\nconfigWidget.player_hide_slider.baseWidth=368\nconfigWidget.player_hide_slider.height=0.22564102564102564\nconfigWidget.player_hide_slider.width=0.8695652173913043\nconfigWidget.player_hide_slider.x=0.021739130434782608\nconfigWidget.player_hide_slider.y=0.5692307692307692\nconfigWidget.player_hide_ticks_field.baseHeight=195\nconfigWidget.player_hide_ticks_field.baseWidth=368\nconfigWidget.player_hide_ticks_field.height=0.10256410256410256\nconfigWidget.player_hide_ticks_field.width=0.25\nconfigWidget.player_hide_ticks_field.x=0.7038043478260869\nconfigWidget.player_hide_ticks_field.y=0.6461538461538462\nconfigWidget.reset_button.baseHeight=198\nconfigWidget.reset_button.baseWidth=368\nconfigWidget.reset_button.height=0.10101010101010101\nconfigWidget.reset_button.width=0.44565217391304346\nconfigWidget.reset_button.x=0.0\nconfigWidget.reset_button.y=0.7929292929292929\nconfigWidget.sound_mode_label.baseHeight=198\nconfigWidget.sound_mode_label.baseWidth=368\nconfigWidget.sound_mode_label.height=0.05555555555555555\nconfigWidget.sound_mode_label.width=0.483695652173913\nconfigWidget.sound_mode_label.x=0.06521739130434782\nconfigWidget.sound_mode_label.y=0.29797979797979796\nconfigWidget.sound_mode_toggle.baseHeight=198\nconfigWidget.sound_mode_toggle.baseWidth=368\nconfigWidget.sound_mode_toggle.height=0.10101010101010101\nconfigWidget.sound_mode_toggle.width=0.30434782608695654\nconfigWidget.sound_mode_toggle.x=0.6331521739130435\nconfigWidget.sound_mode_toggle.y=0.2727272727272727\nconfigWidget.sounds_description.baseHeight=212\nconfigWidget.sounds_description.baseWidth=371\nconfigWidget.sounds_description.height=0.04716981132075472\nconfigWidget.sounds_description.width=0.6522911051212938\nconfigWidget.sounds_description.x=0.1752021563342318\nconfigWidget.sounds_description.y=0.15566037735849056\nconfigWidget.sounds_title.baseHeight=198\nconfigWidget.sounds_title.baseWidth=368\nconfigWidget.sounds_title.height=0.050505050505050504\nconfigWidget.sounds_title.width=0.28804347826086957\nconfigWidget.sounds_title.x=0.35597826086956524\nconfigWidget.sounds_title.y=0.020202020202020204\nconfigWidget.status_linked.baseHeight=195\nconfigWidget.status_linked.baseWidth=368\nconfigWidget.status_linked.height=0.041025641025641026\nconfigWidget.status_linked.width=0.08695652173913043\nconfigWidget.status_linked.x=0.45652173913043476\nconfigWidget.status_linked.y=0.9179487179487179\nconfigWidget.status_unlinked.baseHeight=195\nconfigWidget.status_unlinked.baseWidth=368\nconfigWidget.status_unlinked.height=0.041025641025641026\nconfigWidget.status_unlinked.width=0.13043478260869565\nconfigWidget.status_unlinked.x=0.43478260869565216\nconfigWidget.status_unlinked.y=0.9179487179487179\nconfigWidget.tab_general.baseHeight=212\nconfigWidget.tab_general.baseWidth=371\nconfigWidget.tab_general.height=0.09433962264150944\nconfigWidget.tab_general.width=0.2183288409703504\nconfigWidget.tab_general.x=-0.03773584905660377\nconfigWidget.tab_general.y=-0.13679245283018868\nconfigWidget.tab_others.baseHeight=212\nconfigWidget.tab_others.baseWidth=371\nconfigWidget.tab_others.height=0.09433962264150944\nconfigWidget.tab_others.width=0.2183288409703504\nconfigWidget.tab_others.x=0.8194070080862533\nconfigWidget.tab_others.y=-0.13679245283018868\nconfigWidget.tab_sounds.baseHeight=212\nconfigWidget.tab_sounds.baseWidth=371\nconfigWidget.tab_sounds.height=0.09433962264150944\nconfigWidget.tab_sounds.width=0.2183288409703504\nconfigWidget.tab_sounds.x=0.6091644204851752\nconfigWidget.tab_sounds.y=-0.13679245283018868\nconfigWidget.tab_zoom_stage.baseHeight=212\nconfigWidget.tab_zoom_stage.baseWidth=371\nconfigWidget.tab_zoom_stage.height=0.09433962264150944\nconfigWidget.tab_zoom_stage.width=0.2183288409703504\nconfigWidget.tab_zoom_stage.x=0.1778975741239892\nconfigWidget.tab_zoom_stage.y=-0.13679245283018868\nconfigWidget.tab_zoom_stage_2.baseHeight=212\nconfigWidget.tab_zoom_stage_2.baseWidth=371\nconfigWidget.tab_zoom_stage_2.height=0.09433962264150944\nconfigWidget.tab_zoom_stage_2.width=0.2183288409703504\nconfigWidget.tab_zoom_stage_2.x=0.3935309973045822\nconfigWidget.tab_zoom_stage_2.y=-0.13679245283018868\nconfigWidget.title.baseHeight=198\nconfigWidget.title.baseWidth=368\nconfigWidget.title.height=0.05555555555555555\nconfigWidget.title.width=0.13043478260869565\nconfigWidget.title.x=0.43478260869565216\nconfigWidget.title.y=0.020202020202020204\nconfigWidget.warp_plate_label.baseHeight=198\nconfigWidget.warp_plate_label.baseWidth=368\nconfigWidget.warp_plate_label.height=0.045454545454545456\nconfigWidget.warp_plate_label.width=0.483695652173913\nconfigWidget.warp_plate_label.x=0.05434782608695652\nconfigWidget.warp_plate_label.y=0.35353535353535354\nconfigWidget.warp_plate_toggle.baseHeight=198\nconfigWidget.warp_plate_toggle.baseWidth=368\nconfigWidget.warp_plate_toggle.height=0.10101010101010101\nconfigWidget.warp_plate_toggle.width=0.25\nconfigWidget.warp_plate_toggle.x=0.6956521739130435\nconfigWidget.warp_plate_toggle.y=0.3282828282828283\nconfigWidget.zoom_in_slider.baseHeight=198\nconfigWidget.zoom_in_slider.baseWidth=368\nconfigWidget.zoom_in_slider.height=0.2222222222222222\nconfigWidget.zoom_in_slider.width=1.0\nconfigWidget.zoom_in_slider.x=0.0\nconfigWidget.zoom_in_slider.y=0.5050505050505051\nconfigWidget.zoom_in_ticks_field.baseHeight=198\nconfigWidget.zoom_in_ticks_field.baseWidth=368\nconfigWidget.zoom_in_ticks_field.height=0.10101010101010101\nconfigWidget.zoom_in_ticks_field.width=0.37228260869565216\nconfigWidget.zoom_in_ticks_field.x=0.5760869565217391\nconfigWidget.zoom_in_ticks_field.y=0.5454545454545454\nconfigWidget.zoom_in_ticks_label.baseHeight=198\nconfigWidget.zoom_in_ticks_label.baseWidth=368\nconfigWidget.zoom_in_ticks_label.height=0.06060606060606061\nconfigWidget.zoom_in_ticks_label.width=0.42934782608695654\nconfigWidget.zoom_in_ticks_label.x=0.05434782608695652\nconfigWidget.zoom_in_ticks_label.y=0.5707070707070707\nconfigWidget.zoom_out_slider.baseHeight=195\nconfigWidget.zoom_out_slider.baseWidth=368\nconfigWidget.zoom_out_slider.height=0.22564102564102564\nconfigWidget.zoom_out_slider.width=1.0\nconfigWidget.zoom_out_slider.x=0.0\nconfigWidget.zoom_out_slider.y=0.23076923076923078\nconfigWidget.zoom_out_ticks_field.baseHeight=198\nconfigWidget.zoom_out_ticks_field.baseWidth=368\nconfigWidget.zoom_out_ticks_field.height=0.10101010101010101\nconfigWidget.zoom_out_ticks_field.width=0.37228260869565216\nconfigWidget.zoom_out_ticks_field.x=0.5760869565217391\nconfigWidget.zoom_out_ticks_field.y=0.3282828282828283\nconfigWidget.zoom_out_ticks_label.baseHeight=198\nconfigWidget.zoom_out_ticks_label.baseWidth=368\nconfigWidget.zoom_out_ticks_label.height=0.06060606060606061\nconfigWidget.zoom_out_ticks_label.width=0.42934782608695654\nconfigWidget.zoom_out_ticks_label.x=0.05434782608695652\nconfigWidget.zoom_out_ticks_label.y=0.35353535353535354\nconfigWidget.zoom_stage_glide_slider.baseHeight=195\nconfigWidget.zoom_stage_glide_slider.baseWidth=368\nconfigWidget.zoom_stage_glide_slider.height=0.22564102564102564\nconfigWidget.zoom_stage_glide_slider.width=1.0\nconfigWidget.zoom_stage_glide_slider.x=0.0\nconfigWidget.zoom_stage_glide_slider.y=0.26153846153846155\nconfigWidget.zoom_stage_glide_ticks_field.baseHeight=195\nconfigWidget.zoom_stage_glide_ticks_field.baseWidth=368\nconfigWidget.zoom_stage_glide_ticks_field.height=0.10256410256410256\nconfigWidget.zoom_stage_glide_ticks_field.width=0.25\nconfigWidget.zoom_stage_glide_ticks_field.x=0.7092391304347826\nconfigWidget.zoom_stage_glide_ticks_field.y=0.5897435897435898\nconfigWidget.zoom_stage_glide_ticks_label.baseHeight=195\nconfigWidget.zoom_stage_glide_ticks_label.baseWidth=368\nconfigWidget.zoom_stage_glide_ticks_label.height=0.06666666666666667\nconfigWidget.zoom_stage_glide_ticks_label.width=0.5652173913043478\nconfigWidget.zoom_stage_glide_ticks_label.x=0.03804347826086957\nconfigWidget.zoom_stage_glide_ticks_label.y=0.6153846153846154\ncrossDimensionTravelEnabled=false\ncustomSoundVolume=0.5\ncustomSoundsEnabled=false\neffectEnabled=true\nendZoomHeightsLinked=true\nendZoomInStage1=20\nendZoomInStage2=40\nendZoomInStage3=60\nendZoomOutStage1=20\nendZoomOutStage2=40\nendZoomOutStage3=60\nexternalTeleportTransitionsEnabled=true\nfallbackChunkFadeEnabled=true\nlocalPlayerHideTicks=2\nminecraftSoundVolume=0.5\nnetherZoomHeightsLinked=true\nnetherZoomInStage1=20\nnetherZoomInStage2=40\nnetherZoomInStage3=60\nnetherZoomOutStage1=20\nnetherZoomOutStage2=40\nnetherZoomOutStage3=60\nplayerFreezeEnabled=true\nwarpPlateTransitionsEnabled=true\nzoomHeightsLinked=true\nzoomInStage1=20\nzoomInStage2=40\nzoomInStage3=60\nzoomInStageTicks1=13\nzoomInStageTicks2=13\nzoomInStageTicks3=13\nzoomOutStage1=20\nzoomOutStage2=40\nzoomOutStage3=60\nzoomOutStageTicks1=13\nzoomOutStageTicks2=13\nzoomOutStageTicks3=13\nzoomStageGlideHeight=0.5\nzoomStageGlideTicks=13\n"));
      } catch (IOException var2) {
      }

      return properties;
   }

   private static double[] readStageHeights(Properties properties, String prefix, double[] defaults) {
      double[] values = (double[])defaults.clone();

      for(int i = 0; i < values.length; ++i) {
         values[i] = readDouble(properties, prefix + (i + 1), values[i]);
      }

      return sanitizeStageHeights(values);
   }

   private static int[] readStageTicks(Properties properties, String prefix, int[] defaults) {
      int[] values = (int[])defaults.clone();

      for(int i = 0; i < values.length; ++i) {
         values[i] = readClampedInt(properties, prefix + (i + 1), values[i], 1, 200);
      }

      return sanitizeStageTicks(values);
   }

   private static void readWidgetLayouts(Properties properties) {
      configWidgetLayouts.clear();
      Iterator var1 = properties.stringPropertyNames().iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         if (key.startsWith("configWidget.") && key.endsWith(".x")) {
            String id = key.substring("configWidget.".length(), key.length() - 2);
            if (isSafeId(id)) {
               String prefix = "configWidget." + id;
               double x = readDouble(properties, prefix + ".x", 0.0);
               double y = readDouble(properties, prefix + ".y", 0.0);
               double width = readDouble(properties, prefix + ".width", 0.0);
               double height = readDouble(properties, prefix + ".height", 0.0);
               int baseWidth = readPositiveInt(properties, prefix + ".baseWidth", 0);
               int baseHeight = readPositiveInt(properties, prefix + ".baseHeight", 0);
               if (width > 0.0 && height > 0.0) {
                  configWidgetLayouts.put(id, new double[]{clamp(x, -2.0, 3.0), clamp(y, -2.0, 3.0), clamp(width, 0.01, 3.0), clamp(height, 0.01, 3.0), (double)baseWidth, (double)baseHeight});
               }
            }
         }
      }

   }

   private static void readConfigTexts(Properties properties) {
      configTexts.clear();
      Iterator var1 = properties.stringPropertyNames().iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         if (key.startsWith("configText.")) {
            String id = key.substring("configText.".length());
            if (isSafeId(id)) {
               configTexts.put(id, properties.getProperty(key, ""));
            }
         }
      }

   }

   private static boolean prepareLoadedProperties(Properties properties) {
      if (!isLegacyCompactLayoutConfig(properties)) {
         return false;
      } else {
         restoreDefaultLayoutProperties(properties);
         return true;
      }
   }

   private static boolean isLegacyCompactLayoutConfig(Properties properties) {
      return !hasPropertyWithPrefix(properties, "configWidget.") && !hasPropertyWithPrefix(properties, "configText.") && (properties.containsKey("configLayoutCustom") || properties.containsKey("configLayoutBaseWidth") || properties.containsKey("configLayoutBaseHeight") || properties.containsKey("configLayoutWidth") || properties.containsKey("configLayoutHeight"));
   }

   private static boolean hasPropertyWithPrefix(Properties properties, String prefix) {
      Iterator var2 = properties.stringPropertyNames().iterator();

      String key;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         key = (String)var2.next();
      } while(!key.startsWith(prefix));

      return true;
   }

   private static void restoreDefaultLayoutProperties(Properties properties) {
      Iterator var1 = properties.stringPropertyNames().stream().filter((keyx) -> {
         return keyx.startsWith("configLayout") || keyx.startsWith("configWidget.") || keyx.startsWith("configText.");
      }).toList().iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         properties.remove(key);
      }

      Properties defaults = createDefaultProperties();
      Iterator var5 = defaults.stringPropertyNames().iterator();

      while(true) {
         String key;
         do {
            if (!var5.hasNext()) {
               return;
            }

            key = (String)var5.next();
         } while(!key.startsWith("configLayout") && !key.startsWith("configWidget.") && !key.startsWith("configText."));

         properties.setProperty(key, defaults.getProperty(key));
      }
   }

   private static double readDouble(Properties properties, String key, double fallback) {
      try {
         return Double.parseDouble(properties.getProperty(key, Double.toString(fallback)));
      } catch (NumberFormatException var5) {
         return fallback;
      }
   }

   private static double readUnitDouble(Properties properties, String key, double fallback) {
      return clamp(readDouble(properties, key, fallback), 0.0, 1.0);
   }

   private static double readClampedDouble(Properties properties, String key, double fallback, double min, double max) {
      return clamp(readDouble(properties, key, fallback), min, max);
   }

   private static int readClampedInt(Properties properties, String key, int fallback, int min, int max) {
      try {
         return clamp(Integer.parseInt(properties.getProperty(key, Integer.toString(fallback))), min, max);
      } catch (NumberFormatException var6) {
         return fallback;
      }
   }

   private static int readPositiveInt(Properties properties, String key, int fallback) {
      try {
         return Math.max(0, Integer.parseInt(properties.getProperty(key, Integer.toString(fallback))));
      } catch (NumberFormatException var4) {
         return fallback;
      }
   }

   private static double roundStageHeight(double value) {
      return Math.rint(value);
   }

   private static boolean isSafeId(String id) {
      return id != null && id.matches("[a-z0-9_]+") && id.length() <= 64;
   }

   private static double clamp(double value, double min, double max) {
      return Math.max(min, Math.min(max, value));
   }

   private static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   private static boolean save() {
      if (configPath == null) {
         configPath = resolveConfigPath();
      }

      Properties properties = new Properties();
      properties.setProperty("effectEnabled", Boolean.toString(effectEnabled));
      properties.setProperty("playerFreezeEnabled", Boolean.toString(playerFreezeEnabled));
      properties.setProperty("crossDimensionTravelEnabled", Boolean.toString(crossDimensionTravelEnabled));
      properties.setProperty("zoomHeightsLinked", Boolean.toString(zoomHeightsLinked));
      writeStageHeights(properties, "zoomOutStage", zoomOutStageHeights);
      writeStageHeights(properties, "zoomInStage", zoomInStageHeights);
      properties.setProperty("netherZoomHeightsLinked", Boolean.toString(netherZoomHeightsLinked));
      writeStageHeights(properties, "netherZoomOutStage", netherZoomOutStageHeights);
      writeStageHeights(properties, "netherZoomInStage", netherZoomInStageHeights);
      properties.setProperty("endZoomHeightsLinked", Boolean.toString(endZoomHeightsLinked));
      writeStageHeights(properties, "endZoomOutStage", endZoomOutStageHeights);
      writeStageHeights(properties, "endZoomInStage", endZoomInStageHeights);
      writeStageTicks(properties, "zoomOutStageTicks", zoomOutStageTicks);
      writeStageTicks(properties, "zoomInStageTicks", zoomInStageTicks);
      properties.setProperty("zoomStageGlideHeight", Double.toString(zoomStageGlideHeight));
      properties.setProperty("zoomStageGlideTicks", Integer.toString(zoomStageGlideTicks));
      properties.setProperty("bodyCameraHeight", Double.toString(bodyCameraHeight));
      properties.setProperty("bodyGlideHeight", Double.toString(bodyGlideHeight));
      properties.setProperty("bodyGlideTicks", Integer.toString(bodyGlideTicks));
      properties.setProperty("localPlayerHideTicks", Integer.toString(localPlayerHideTicks));
      properties.setProperty("customSoundsEnabled", Boolean.toString(customSoundsEnabled));
      properties.setProperty("minecraftSoundVolume", Double.toString(minecraftSoundVolume));
      properties.setProperty("customSoundVolume", Double.toString(customSoundVolume));
      properties.setProperty("warpPlateTransitionsEnabled", Boolean.toString(warpPlateTransitionsEnabled));
      properties.setProperty("externalTeleportTransitionsEnabled", Boolean.toString(externalTeleportTransitionsEnabled));
      properties.setProperty("fallbackChunkFadeEnabled", Boolean.toString(fallbackChunkFadeEnabled));
      properties.setProperty("configLayoutEditorButtonVisible", Boolean.toString(configLayoutEditorButtonVisible));
      properties.setProperty("configLayoutDebugEnabled", Boolean.toString(configLayoutDebugEnabled));
      properties.setProperty("configLayoutAspectLocked", Boolean.toString(configLayoutAspectLocked));
      properties.setProperty("configLayoutGridEnabled", Boolean.toString(configLayoutGridEnabled));
      properties.setProperty("configLayoutSnapEnabled", Boolean.toString(configLayoutSnapEnabled));
      properties.setProperty("configLayoutCustom", Boolean.toString(configLayoutCustom));
      properties.setProperty("configLayoutX", Double.toString(configLayoutX));
      properties.setProperty("configLayoutY", Double.toString(configLayoutY));
      properties.setProperty("configLayoutWidth", Double.toString(configLayoutWidth));
      properties.setProperty("configLayoutHeight", Double.toString(configLayoutHeight));
      properties.setProperty("configLayoutBaseWidth", Integer.toString(configLayoutBaseWidth));
      properties.setProperty("configLayoutBaseHeight", Integer.toString(configLayoutBaseHeight));
      Iterator var1 = configWidgetLayouts.entrySet().iterator();

      Map.Entry entry;
      while(var1.hasNext()) {
         entry = (Map.Entry)var1.next();
         double[] values = (double[])entry.getValue();
         String prefix = "configWidget." + (String)entry.getKey();
         properties.setProperty(prefix + ".x", Double.toString(values[0]));
         properties.setProperty(prefix + ".y", Double.toString(values[1]));
         properties.setProperty(prefix + ".width", Double.toString(values[2]));
         properties.setProperty(prefix + ".height", Double.toString(values[3]));
         if (values.length > 5) {
            properties.setProperty(prefix + ".baseWidth", Integer.toString((int)Math.round(values[4])));
            properties.setProperty(prefix + ".baseHeight", Integer.toString((int)Math.round(values[5])));
         }
      }

      var1 = configTexts.entrySet().iterator();

      while(var1.hasNext()) {
         entry = (Map.Entry)var1.next();
         properties.setProperty("configText." + (String)entry.getKey(), (String)entry.getValue());
      }

      try {
         Files.createDirectories(configPath.getParent());
         OutputStream output = Files.newOutputStream(configPath);

         try {
            properties.store(output, "Grand Teleport client settings");
         } catch (Throwable var6) {
            if (output != null) {
               try {
                  output.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (output != null) {
            output.close();
         }

         return true;
      } catch (IOException var7) {
         return false;
      }
   }

   private static Path resolveConfigPath() {
      return FabricLoader.getInstance().getConfigDir().resolve("grand_teleport.properties");
   }

   private static Path resolveLegacyConfigPath() {
      return FabricLoader.getInstance().getConfigDir().resolve("gtalike_teleport.properties");
   }

   private static void migrateLegacyConfig() {
      Path legacyPath = resolveLegacyConfigPath();
      if (!Files.exists(configPath, new LinkOption[0]) && Files.exists(legacyPath, new LinkOption[0])) {
         try {
            Files.createDirectories(configPath.getParent());
            Files.copy(legacyPath, configPath);
         } catch (IOException var2) {
            configPath = legacyPath;
         }

      }
   }

   private static ZoomDimension sanitizeZoomDimension(ZoomDimension dimension) {
      return dimension == null ? GtaLikeTeleportConfig.ZoomDimension.OVERWORLD : dimension;
   }

   private static void writeStageHeights(Properties properties, String prefix, double[] values) {
      double[] sanitized = sanitizeStageHeights(values);

      for(int i = 0; i < sanitized.length; ++i) {
         properties.setProperty(prefix + (i + 1), Integer.toString((int)sanitized[i]));
      }

   }

   private static void writeStageTicks(Properties properties, String prefix, int[] values) {
      int[] sanitized = sanitizeStageTicks(values);

      for(int i = 0; i < sanitized.length; ++i) {
         properties.setProperty(prefix + (i + 1), Integer.toString(sanitized[i]));
      }

   }

   static {
      zoomOutStageHeights = (double[])DEFAULT_STAGE_HEIGHTS.clone();
      zoomInStageHeights = (double[])DEFAULT_STAGE_HEIGHTS.clone();
      netherZoomHeightsLinked = true;
      netherZoomOutStageHeights = (double[])DEFAULT_STAGE_HEIGHTS.clone();
      netherZoomInStageHeights = (double[])DEFAULT_STAGE_HEIGHTS.clone();
      endZoomHeightsLinked = true;
      endZoomOutStageHeights = (double[])DEFAULT_STAGE_HEIGHTS.clone();
      endZoomInStageHeights = (double[])DEFAULT_STAGE_HEIGHTS.clone();
      zoomOutStageTicks = (int[])DEFAULT_STAGE_TICKS.clone();
      zoomInStageTicks = (int[])DEFAULT_STAGE_TICKS.clone();
      zoomStageGlideHeight = 0.5;
      zoomStageGlideTicks = 13;
      bodyCameraHeight = 6.0;
      bodyGlideHeight = 0.5;
      bodyGlideTicks = 10;
      localPlayerHideTicks = 2;
      customSoundsEnabled = false;
      minecraftSoundVolume = 1.0;
      customSoundVolume = 0.3;
      warpPlateTransitionsEnabled = true;
      externalTeleportTransitionsEnabled = true;
      fallbackChunkFadeEnabled = true;
      configLayoutEditorButtonVisible = false;
      configLayoutDebugEnabled = false;
      configLayoutAspectLocked = true;
      configLayoutGridEnabled = true;
      configLayoutSnapEnabled = true;
      configLayoutCustom = false;
      configLayoutX = 0.0;
      configLayoutY = 0.0;
      configLayoutWidth = 0.0;
      configLayoutHeight = 0.0;
      configLayoutBaseWidth = 0;
      configLayoutBaseHeight = 0;
      configWidgetLayouts = new HashMap();
      configTexts = new HashMap();
   }

   static enum ZoomDimension {
      OVERWORLD,
      NETHER,
      END;

      static ZoomDimension fromLevel(ResourceKey<Level> dimension) {
         if (Level.NETHER.equals(dimension)) {
            return NETHER;
         } else {
            return Level.END.equals(dimension) ? END : OVERWORLD;
         }
      }
   }
}
