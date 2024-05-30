package me.gabber235.typewriter.utils

import org.bukkit.util.NumberConversions.square
import org.jetbrains.annotations.Contract
import java.util.function.DoubleUnaryOperator
import kotlin.math.floor
import kotlin.math.sqrt


/**
 * Represents a 3D point.
 */
interface Point {
    /**
     * Gets the X coordinate.
     *
     * @return the X coordinate
     */
    val x: Double

    /**
     * Gets the Y coordinate.
     *
     * @return the Y coordinate
     */
    val y: Double

    /**
     * Gets the Z coordinate.
     *
     * @return the Z coordinate
     */
    val z: Double

    /**
     * Gets the floored value of the X component
     *
     * @return the block X
     */
    val blockX: Int
        get() = floor(x).toInt()

    /**
     * Gets the floored value of the X component
     *
     * @return the block X
     */
    val blockY: Int
        get() = floor(y).toInt()

    /**
     * Gets the floored value of the X component
     *
     * @return the block X
     */
    val blockZ: Int
        get() = floor(z).toInt()

    /**
     * Creates a point with a modified X coordinate based on its value.
     *
     * @param operator the operator providing the current X coordinate and returning the new
     * @return a new point
     */
    @Contract(pure = true)
    fun withX(operator: DoubleUnaryOperator): Point {
        return withX(operator.applyAsDouble(x))
    }

    /**
     * Creates a point with the specified X coordinate.
     *
     * @param x the new X coordinate
     * @return a new point
     */
    @Contract(pure = true)
    fun withX(x: Double): Point

    /**
     * Creates a point with a modified Y coordinate based on its value.
     *
     * @param operator the operator providing the current Y coordinate and returning the new
     * @return a new point
     */
    @Contract(pure = true)
    fun withY(operator: DoubleUnaryOperator): Point {
        return withY(operator.applyAsDouble(y))
    }

    /**
     * Creates a point with the specified Y coordinate.
     *
     * @param y the new Y coordinate
     * @return a new point
     */
    @Contract(pure = true)
    fun withY(y: Double): Point

    /**
     * Creates a point with a modified Z coordinate based on its value.
     *
     * @param operator the operator providing the current Z coordinate and returning the new
     * @return a new point
     */
    @Contract(pure = true)
    fun withZ(operator: DoubleUnaryOperator): Point {
        return withZ(operator.applyAsDouble(z))
    }

    /**
     * Creates a point with the specified Z coordinate.
     *
     * @param z the new Z coordinate
     * @return a new point
     */
    @Contract(pure = true)
    fun withZ(z: Double): Point

    @Contract(pure = true)
    fun add(x: Double, y: Double, z: Double): Point

    @Contract(pure = true)
    fun add(point: Point): Point

    operator fun plus(point: Point): Point

    @Contract(pure = true)
    fun add(value: Double): Point

    operator fun plus(value: Double): Point

    @Contract(pure = true)
    fun sub(x: Double, y: Double, z: Double): Point

    @Contract(pure = true)
    fun sub(point: Point): Point

    operator fun minus(point: Point): Point

    @Contract(pure = true)
    fun sub(value: Double): Point

    operator fun minus(value: Double): Point

    @Contract(pure = true)
    fun mul(x: Double, y: Double, z: Double): Point

    @Contract(pure = true)
    fun mul(point: Point): Point

    operator fun times(point: Point): Point

    @Contract(pure = true)
    fun mul(value: Double): Point

    operator fun times(value: Double): Point

    @Contract(pure = true)
    fun div(x: Double, y: Double, z: Double): Point

    @Contract(pure = true)
    operator fun div(point: Point): Point

    @Contract(pure = true)
    operator fun div(value: Double): Point

    @Contract(pure = true)
    fun distanceSquared(x: Double, y: Double, z: Double): Double {
        return square(this.x - x) + square(this.y - y) + square(this.z - z)
    }

    /**
     * Gets the squared distance between this point and another.
     *
     * @param point the other point
     * @return the squared distance
     */
    @Contract(pure = true)
    fun distanceSquared(point: Point): Double {
        return distanceSquared(point.x, point.y, point.z)
    }

    @Contract(pure = true)
    fun distance(x: Double, y: Double, z: Double): Double {
        return sqrt(distanceSquared(x, y, z))
    }

    /**
     * Gets the distance between this point and another. The value of this
     * method is not cached and uses a costly square-root function, so do not
     * repeatedly call this method to get the vector's magnitude. NaN will be
     * returned if the inner result of the sqrt() function overflows, which
     * will be caused if the distance is too long.
     *
     * @param point the other point
     * @return the distance
     */
    @Contract(pure = true)
    fun distance(point: Point): Double {
        return distance(point.x, point.y, point.z)
    }

    fun samePoint(x: Double, y: Double, z: Double): Boolean {
        return x.compareTo(this.x) == 0 && y.compareTo(this.y) == 0 && z.compareTo(this.z) == 0
    }

    /**
     * Checks it two points have similar (x/y/z).
     *
     * @param point the point to compare
     * @return true if the two positions are similar
     */
    fun samePoint(point: Point): Boolean {
        return samePoint(point.x, point.y, point.z)
    }

    val isZero: Boolean
        /**
         * Checks if the three coordinates [.x], [.y] and [.z]
         * are equal to `0`.
         *
         * @return true if the three coordinates are zero
         */
        get() = x == 0.0 && y == 0.0 && z == 0.0

    fun sameBlock(blockX: Int, blockY: Int, blockZ: Int): Boolean {
        return this.blockX == blockX && this.blockY == blockY && this.blockZ == blockZ
    }

    /**
     * Checks if two points are in the same block.
     *
     * @param point the point to compare to
     * @return true if 'this' is in the same block as `point`
     */
    fun sameBlock(point: Point): Boolean {
        return sameBlock(point.blockX, point.blockY, point.blockZ)
    }
}

