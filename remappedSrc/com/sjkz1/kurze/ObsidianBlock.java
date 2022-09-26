package com.sjkz1.kurze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;

public class ObsidianBlock extends net.minecraft.world.level.block.Block implements Fallable {
    public ObsidianBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onLand(Level level, BlockPos blockPos, BlockState blockState, BlockState blockState2, FallingBlockEntity fallingBlockEntity) {
        level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OBSIDIAN.defaultBlockState()), 1, 1, 1, 1, 1, 1);
        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 20);
    }
}
