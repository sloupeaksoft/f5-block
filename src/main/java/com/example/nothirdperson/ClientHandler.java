package com.example.nothirdperson;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Блокирует смену перспективы (F5) и принудительно держит вид от 1-го лица.
 */
@Mod.EventBusSubscriber(modid = NoThirdPerson.MODID, value = Side.CLIENT)
public class ClientHandler {

    // Срабатывает ДО того, как ванильный код обработает нажатие F5 —
    // "съедаем" нажатие, и перспектива вообще не переключается.
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        consumePerspectiveKey();
    }

    // На случай, если смену вида переназначили на кнопку мыши.
    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseInputEvent event) {
        consumePerspectiveKey();
    }

    // Страховка: каждый тик возвращаем вид от 1-го лица.
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        forceFirstPerson();
    }

    // Страховка перед каждым кадром.
    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            forceFirstPerson();
        }
    }

    private static void consumePerspectiveKey() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings == null) return;
        KeyBinding key = mc.gameSettings.keyBindTogglePerspective;
        while (key.isPressed()) {
            // сбрасываем все накопленные нажатия
        }
        forceFirstPerson();
    }

    private static void forceFirstPerson() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings != null && mc.gameSettings.thirdPersonView != 0) {
            mc.gameSettings.thirdPersonView = 0;
            if (mc.renderGlobal != null) {
                mc.renderGlobal.setDisplayListEntitiesDirty();
            }
        }
    }
}
