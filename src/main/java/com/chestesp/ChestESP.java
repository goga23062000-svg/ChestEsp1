package com.chestesp;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

@Mod("chestesp")
public class ChestESP {

    private final Minecraft mc = Minecraft.getInstance();
    private static final double RANGE = 192;

    public ChestESP() {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {

        if (mc.world == null || mc.player == null) return;

        for (TileEntity tile : mc.world.loadedTileEntityList) {

            if (tile instanceof EnderChestTileEntity) continue;

            BlockPos pos = tile.getPos();
            Block block = mc.world.getBlockState(pos).getBlock();

            if (!(block instanceof ChestBlock ||
                  block instanceof BarrelBlock ||
                  block instanceof ShulkerBoxBlock))
                continue;

            if (mc.player.getDistanceSq(pos.getX(),pos.getY(),pos.getZ()) > RANGE*RANGE)
                continue;

            double x = pos.getX() - mc.getRenderManager().info.getProjectedView().x;
            double y = pos.getY() - mc.getRenderManager().info.getProjectedView().y;
            double z = pos.getZ() - mc.getRenderManager().info.getProjectedView().z;

            AxisAlignedBB box = new AxisAlignedBB(x,y,z,x+1,y+1,z+1);

            GL11.glDisable(GL11.GL_DEPTH_TEST);

            WorldRenderer.drawBoundingBox(
                    event.getMatrixStack(),
                    box,
                    0f,1f,0f,1f
            );

            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }
}
