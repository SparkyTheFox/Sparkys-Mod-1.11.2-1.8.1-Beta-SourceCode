package mod.sparkyfox.servermod.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import io.netty.buffer.ByteBuf;
import mod.sparkyfox.servermod.entity.Gun.Projectile;
import mod.sparkyfox.servermod.item.ItemAmmo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityProjectile2 extends Entity implements IEntityAdditionalSpawnData
{
	private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, entity -> entity.canBeCollidedWith());
	
	private int shooterId;
	private EntityLivingBase shooter;
	private Projectile projectile;
	private ItemStack item = ItemStack.EMPTY;
	
	   private static final DataParameter<ItemStack> ItemStackThrown = EntityDataManager.createKey(EntityProjectile.class, DataSerializers.OPTIONAL_ITEM_STACK);
	   private static final DataParameter<Integer> Velocity = EntityDataManager.createKey(EntityProjectile.class, DataSerializers.VARINT);
	   private static final DataParameter<Integer> Size = EntityDataManager.createKey(EntityProjectile.class, DataSerializers.VARINT);
	   private static final DataParameter<Integer> Particle = EntityDataManager.createKey(EntityProjectile.class, DataSerializers.VARINT);
	
	public EntityProjectile2(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntityProjectile2(World worldIn, EntityLivingBase shooter, Projectile projectile)
    {
        this(worldIn);
        this.shooterId = shooter.getEntityId();
        this.shooter = shooter;
        this.projectile = projectile;

		Vec3d dir = shooter.getLook(0.0F);
        this.motionX = dir.xCoord * projectile.speed + shooter.motionX;
        this.motionY = dir.yCoord * projectile.speed;
        this.motionZ = dir.zCoord * projectile.speed + shooter.motionZ;
		updateHeading();

		this.setSize(projectile.size, projectile.size);
		this.setPosition(shooter.posX + dir.xCoord, shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D + dir.yCoord, shooter.posZ + dir.zCoord);

		switch(projectile.type)
		{
			case GRENADE:
				this.item = ItemAmmo.getAmmo(ItemAmmo.Type.GRENADE, 1);
				break;
			case MISSILE:
				this.item = ItemAmmo.getAmmo(ItemAmmo.Type.MISSILE, 1);
				break;
		}
    }

	public ItemStack getItem()
	{
		return item;
	}

	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		
		//if(!worldObj.isRemote)
		{
			updateHeading();

			if(projectile.type == ItemAmmo.Type.MISSILE)
			{
				for(int i = 5; i > 0; i--)
				{
					world.spawnParticle(EnumParticleTypes.CLOUD, true, this.posX - (this.motionX / i), this.posY - (this.motionY / i), this.posZ - (this.motionZ / i), 0, 0, 0);
				}
				if(world.rand.nextInt(2) == 0)
				{
					world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX, this.posY, this.posZ, 0, 0, 0);
					world.spawnParticle(EnumParticleTypes.FLAME, true, this.posX, this.posY, this.posZ, 0, 0, 0);
				}
			}

			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
	
			if (raytraceresult != null) 
			{
				vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
			}
	
			Entity entity = this.findEntityOnPath(vec3d1, vec3d);
	
			if (entity != null) 
			{
				raytraceresult = new RayTraceResult(entity);
			}
	
			if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) 
			{
				EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;
	
				if (this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer)) 
				{
					raytraceresult = null;
				}
			}
	
			if (raytraceresult != null && !world.isRemote) 
			{
				this.onHit(raytraceresult);
			}
			
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			
			this.setPosition(this.posX, this.posY, this.posZ);
			
			if(this.projectile.gravity)
			{
				this.motionY -= 0.05;
			}
			
			if(this.ticksExisted >= this.projectile.life) this.setDead();
		}
	}
	
	@Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ), ARROW_TARGETS);
        double closestDistance = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity hitEntity = list.get(i);

            if (hitEntity != this.shooter)
            {
                AxisAlignedBB axisalignedbb = hitEntity.getEntityBoundingBox().expandXyz(0.30000001192092896D);
                RayTraceResult result = axisalignedbb.calculateIntercept(start, end);

                if (result != null)
                {
                    double distanceToHit = start.squareDistanceTo(result.hitVec);

                    if (distanceToHit < closestDistance || closestDistance == 0.0D)
                    {
                        entity = hitEntity;
                        closestDistance = distanceToHit;
                    }
                }
            }
        }

        return entity;
    }
	
	protected void onHit(RayTraceResult raytraceResultIn)
    {
		Entity entity = raytraceResultIn.entityHit;

		if(entity != null)
		{
			if(entity.getEntityId() == this.shooterId) return;
			float damage = this.projectile.damage;
			if(this.projectile.damageReduceOverLife)
			{
				float percent = ((float) this.projectile.life - (float) this.ticksExisted) / (float) this.projectile.life;
				damage = this.projectile.damage * percent + this.projectile.damage / this.projectile.life;
			}
			
			switch(projectile.type)
			{
			case BASIC:
			case ADVANCED:
				entity.attackEntityFrom(DamageSource.ANVIL, damage);
				entity.hurtResistantTime = 0;
				break;
			case GRENADE:
				world.createExplosion(shooter, raytraceResultIn.hitVec.xCoord, raytraceResultIn.hitVec.yCoord, raytraceResultIn.hitVec.zCoord, 5F, true);
				break;
			}
			
			this.setDead();
			return;
		}

		if(raytraceResultIn.getBlockPos() != null)
		{
			BlockPos pos = raytraceResultIn.getBlockPos();
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if((block instanceof BlockBreakable || block instanceof BlockPane) && state.getMaterial() == Material.GLASS)
			{
				world.destroyBlock(raytraceResultIn.getBlockPos(), false);
			}
			if(!block.isReplaceable(world, raytraceResultIn.getBlockPos()))
			{
				this.setDead();
			}

			if(projectile.type == ItemAmmo.Type.GRENADE)
			{
				world.createExplosion(shooter, raytraceResultIn.hitVec.xCoord, raytraceResultIn.hitVec.yCoord, raytraceResultIn.hitVec.zCoord, 5F, true);
			}

			if(projectile.type == ItemAmmo.Type.MISSILE)
			{
				world.createExplosion(shooter, raytraceResultIn.hitVec.xCoord, raytraceResultIn.hitVec.yCoord, raytraceResultIn.hitVec.zCoord, 10F, true);
				WorldServer worldServer = (WorldServer) world;
				worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, raytraceResultIn.hitVec.xCoord, raytraceResultIn.hitVec.yCoord, raytraceResultIn.hitVec.zCoord, 0, 0.0, 0.0, 0.0, 0);
			}
		}
    }
	
	@Override
	public boolean shouldRenderInPass(int pass) 
	{
		return this.projectile.visible;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		this.projectile = new Projectile();
		this.projectile.deserializeNBT(compound.getCompoundTag("projectile"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		compound.setTag("projectile", this.projectile.serializeNBT());
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		ByteBufUtils.writeTag(buffer, this.projectile.serializeNBT());
		buffer.writeInt(this.shooterId);
		buffer.writeFloat(this.rotationYaw);
		buffer.writeFloat(this.rotationPitch);
		ByteBufUtils.writeItemStack(buffer, item);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) 
	{
		if(this.projectile == null) 
		{
			this.projectile = new Projectile();
		}
		this.projectile.deserializeNBT(ByteBufUtils.readTag(additionalData));
		this.shooterId = additionalData.readInt();
		this.rotationYaw = additionalData.readFloat();
		this.prevRotationYaw = this.rotationYaw;
		this.rotationPitch = additionalData.readFloat();
		this.prevRotationPitch = this.rotationPitch;
		this.item = ByteBufUtils.readItemStack(additionalData);
	}

	public void updateHeading()
	{
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
		this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
	}


	public Projectile getProjectile()
	{
		return projectile;
	}
	
	  public void setSpeed(int speed) {
		     this.dataManager.set(Velocity, Integer.valueOf(speed));
		  }
		  
		  public float getSpeed() {
		     return ((Integer)this.dataManager.get(Velocity)).intValue() / 10.0F;
		  }
}
