package io.github.squid233.wires.util;

import net.minecraft.util.math.Position;

/**
 * Mutable vector with 3 doubles.
 *
 * @author squid233
 * @since 0.2.0
 */
public final class MutableVec3d implements Position {
    private double x, y, z;

    public MutableVec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MutableVec3d setX(double x) {
        this.x = x;
        return this;
    }

    public MutableVec3d setY(double y) {
        this.y = y;
        return this;
    }

    public MutableVec3d setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }
}
