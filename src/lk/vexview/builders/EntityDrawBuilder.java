/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: EntityDrawBuilder.java@author: karlatemp@vip.qq.com: 2020/2/2 上午12:28@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.gameprofile.WrappedGameProfile;
import lk.vexview.gui.components.VexEntityDraw;
import lk.vexview.gui.components.VexPlayerDraw;
import lk.vexview.hud.VexEntityDrawShow;
import lk.vexview.hud.VexPlayerDrawShow;
import lk.vexview.hud.VexShow;
import lk.vexview.tag.TagDirection;
import lk.vexview.tag.components.VexEntityDrawTag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.UUID;

/**
 * {@link VexEntityDraw} {@link VexEntityDrawTag}{@link VexEntityDrawShow} 的快速构造器.
 *
 * <pre>{@code
 * VexEntityDraw draw = EntityDrawBuilder.builder()
 *      .of( entity )
 *      .offset( 0, 233) // 位置
 *      .build();
 * }
 * VexEntityDraw draw = EntityDrawBuilder.builder()
 *      .player(new WrappedGameProfile(
 *          PlayerUniqueId, PlayerProfileName
 *      ).getPropertyMap()
 *      .put(......) // Editing
 *      .getProfile()).build();
 * </pre>
 *
 * @param <T> 实体类型
 * @author Karlatemp
 * @since 1.0.0
 */
public class EntityDrawBuilder<T extends Entity> extends Locator {
    static {
        ReflectionUtil.register(EntityDrawBuilder.class, MethodHandles.lookup());
    }

    public static <T extends Entity> EntityDrawBuilder<T> builder() {
        return new EntityDrawBuilder<>();
    }

    protected EntityDrawBuilder() {
    }

    public static class EntityEntityDrawBuilder<T extends Entity> extends EntityDrawBuilder<T> {
        private T entity;

        @Override
        public EntityEntityDrawBuilder<T> copy(Locator newLocation) {
            return (EntityEntityDrawBuilder<T>) super.copy(newLocation);
        }

        static {
            ReflectionUtil.register(EntityEntityDrawBuilder.class, MethodHandles.lookup());
        }

        @Override
        public PlayerDrawBuilder player() {
            throw new UnsupportedOperationException("Entity entity draw builder cannot cast to PlayerDrawBuilder");
        }

        @Override
        public PlayerDrawBuilder player(String name, UUID uniqueId) {
            return super.player(name, uniqueId);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <V extends Entity> EntityDrawBuilder<V> of(V entity) {
            this.entity = (T) entity;
            return (EntityDrawBuilder<V>) this;
        }

        @Override
        public EntityDrawBuilder<T> of(EntityType type) {
            throw new UnsupportedOperationException("Entity entity draw mode not support entity type.");
        }

        @Override
        public VexEntityDraw build() {
            return new VexEntityDraw(xOffset, yOffset, scale, entity);
        }
    }

    public static class EntityTypeDrawBuilder<T extends Entity> extends EntityDrawBuilder<T> {
        private EntityType type;

        static {
            ReflectionUtil.register(EntityTypeDrawBuilder.class, MethodHandles.lookup());
        }

        @Override
        public EntityTypeDrawBuilder<T> copy(Locator newLocation) {
            return (EntityTypeDrawBuilder<T>) super.copy(newLocation);
        }

        @Override
        public <V extends Entity> EntityDrawBuilder<V> of(V entity) {
            throw new UnsupportedOperationException("Type builder not support entity.");
        }

        @Override
        public PlayerDrawBuilder player(String name, UUID uniqueId) {
            throw new UnsupportedOperationException("Entity Type draw builder not cast to player draw builder");
        }

        @Override
        public PlayerDrawBuilder player() {
            throw new UnsupportedOperationException("Entity type draw builder not cast to player builder.");
        }

        @Override
        public VexEntityDraw build() {
            Objects.requireNonNull(type, "type");
            return new VexEntityDraw(xOffset, yOffset, scale, type);
        }

        @Override
        public EntityDrawBuilder<T> of(EntityType type) {
            this.type = type;
            return this;
        }
    }

    public static class PlayerDrawBuilder extends EntityDrawBuilder<Player> {
        public static class PlayerEntityDrawBuilder extends PlayerDrawBuilder {
            private Player entity;

            static {
                ReflectionUtil.register(PlayerEntityDrawBuilder.class, MethodHandles.lookup());
            }

            @Override
            public VexPlayerDraw build() {
                Objects.requireNonNull(entity, "player");
                return new VexPlayerDraw(xOffset, yOffset, scale, entity);
            }

            @Override
            public PlayerDrawBuilder player(String name, UUID uniqueId) {
                if (uniqueId == null) return this;
                UUID check = null;
                if (entity != null) check = entity.getUniqueId();
                if (uniqueId != check) {
                    if ((entity = Bukkit.getPlayer(uniqueId)) == null) {
                        throw new RuntimeException("Player " + uniqueId + " not found.");
                    }
                }
                return this;
            }

            @Override
            public PlayerDrawBuilder player(WrappedGameProfile profile) {
                return player(null, profile.getId());
            }

            @SuppressWarnings("unchecked")
            @Override
            public <V extends Entity> EntityDrawBuilder<V> of(V entity) {
                this.entity = (Player) entity;
                return (EntityDrawBuilder<V>) this;
            }
        }

        public static class PlayerNUDrawBuilder extends PlayerDrawBuilder {
            private UUID uniqueId;
            private String name;

            static {
                ReflectionUtil.register(PlayerNUDrawBuilder.class, MethodHandles.lookup());
            }

            @Override
            public PlayerDrawBuilder player(WrappedGameProfile profile) {
                this.name = profile.getName();
                this.uniqueId = profile.getId();
                return this;
            }

            @Override
            public PlayerDrawBuilder player(String name, UUID uniqueId) {
                this.name = name;
                this.uniqueId = uniqueId;
                return this;
            }

            @Override
            public VexPlayerDraw build() {
                return new VexPlayerDraw(xOffset, yOffset, scale, uniqueId, name);
            }
        }

        public static class PlayerProfileBuilder extends PlayerDrawBuilder {
            private WrappedGameProfile profile;

            static {
                ReflectionUtil.register(PlayerProfileBuilder.class, MethodHandles.lookup());
            }

            @Override
            public PlayerDrawBuilder player(String name, UUID uniqueId) {
                return player(new WrappedGameProfile(uniqueId, name));
            }

            @Override
            public PlayerDrawBuilder player(WrappedGameProfile profile) {
                this.profile = profile;
                return this;
            }

            @Override
            public VexPlayerDraw build() {
                Objects.requireNonNull(profile, "profile");
                Objects.requireNonNull(profile.getProfile(), "profile's profile");
                return new VexPlayerDraw(xOffset, yOffset, scale, profile.getProfile());
            }
        }

        @Override
        public PlayerDrawBuilder copy(Locator newLocation) {
            return (PlayerDrawBuilder) super.copy(newLocation);
        }

        @Override
        public PlayerDrawBuilder player() {
            return this;
        }

        @Override
        public VexPlayerDraw build() {
            throw new UnsupportedOperationException("Sorry, you called player() method but you don't call of()/player(GameProfile) method.");
        }

        static {
            ReflectionUtil.register(PlayerDrawBuilder.class, MethodHandles.lookup());
        }

        @Override
        public <V extends Entity> EntityDrawBuilder<V> of(V entity) {
            return ReflectionUtil.copyTo(this, PlayerEntityDrawBuilder.class).of(entity);
        }

        @Override
        public EntityDrawBuilder<Player> of(EntityType type) {
            if (type != EntityType.PLAYER)
                throw new UnsupportedOperationException("Player Draw not support entity type.");
            return this;
        }

        @Override
        public VexShow buildHUD(String id, int time, int z, boolean isRotate) {
            return new VexPlayerDrawShow(id, build(), time, isRotate, z);
        }

        @Override
        public VexEntityDrawTag buildTag(String id, double x, double y, double z, TagDirection direction) {
            throw new UnsupportedOperationException("Tag not support on player draw.");
        }
    }

    public PlayerDrawBuilder player() {
        return ReflectionUtil.copyTo(this, PlayerDrawBuilder.class);
    }

    public PlayerDrawBuilder player(String name, UUID uniqueId) {
        return ReflectionUtil.copyTo(this, PlayerDrawBuilder.PlayerNUDrawBuilder.class).player(name, uniqueId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityDrawBuilder<T> copy(Locator newLocation) {
        return (EntityDrawBuilder<T>) super.copy(newLocation);
    }

    public PlayerDrawBuilder player(WrappedGameProfile profile) {
        return ReflectionUtil.copyTo(this, PlayerDrawBuilder.PlayerProfileBuilder.class).player(profile);
    }

    /**
     * 构造一个实体模型绘制组件
     *
     * @return 构造的组件
     */
    public VexEntityDraw build() {
        throw new UnsupportedOperationException("Sorry, but you don't call of()/player() method.");
    }

    @Override
    public EntityDrawBuilder<T> location(int xOffset, int yOffset) {
        super.location(xOffset, yOffset);
        return this;
    }

    @Override
    public EntityDrawBuilder<T> offset(int x, int y) {
        super.offset(x, y);
        return this;
    }

    public int scale = 1;

    public EntityDrawBuilder<T> scale(int scale) {
        this.scale = scale;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <V extends Entity> EntityDrawBuilder<V> of(V entity) {
        if (entity instanceof Player) {
            return (EntityDrawBuilder<V>) player().of((Player) entity);
        } else {
            return ReflectionUtil.copyTo(this, EntityEntityDrawBuilder.class).of(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public EntityDrawBuilder<T> of(EntityType type) {
        return ReflectionUtil.copyTo(this, EntityTypeDrawBuilder.class).of(type);
    }

    /**
     * 构造一个实体模型绘制HUD
     *
     * @param id       HUD ID
     * @param time     HUD持续显示时间
     * @param z        Z轴
     * @param isRotate 是否让模型旋转
     * @return 构造的HUD
     */
    public VexShow buildHUD(String id, int time, int z, boolean isRotate) {
        return new VexEntityDrawShow(id, build(), time, isRotate, z);
    }

    /**
     * 构造实体自定义模型绘制Tag, 不支持PlayerDraw
     *
     * @param id        Tag ID
     * @param x         X坐标偏移量
     * @param y         Y坐标偏移量
     * @param z         Z坐标偏移量
     * @param direction Tag显示方向
     * @return 构造的Tag
     */
    public VexEntityDrawTag buildTag(String id, double x, double y, double z, TagDirection direction) {
        return new VexEntityDrawTag(x, y, z, direction, id, build());
    }
}
