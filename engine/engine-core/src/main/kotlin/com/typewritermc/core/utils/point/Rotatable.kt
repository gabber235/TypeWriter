package com.typewritermc.core.utils.point

/**
 * Interface representing a rotatable object with yaw and pitch angles.
 */
interface Rotatable {
    /**
     * The yaw angle in degrees.
     */
    val yaw: Float

    /**
     * The pitch angle in degrees.
     */
    val pitch: Float

    /**
     * Creates a new instance with the specified yaw angle.
     *
     * @param yaw the new yaw angle
     * @return a new instance with the updated yaw angle
     */
    fun withYaw(yaw: Float): Rotatable

    /**
     * Creates a new instance with the specified pitch angle.
     *
     * @param pitch the new pitch angle
     * @return a new instance with the updated pitch angle
     */
    fun withPitch(pitch: Float): Rotatable

    /**
     * Rotates the yaw angle by the specified amount.
     *
     * @param angle the amount to rotate the yaw angle by
     * @return a new instance with the updated yaw angle
     */
    fun rotateYaw(angle: Float): Rotatable

    /**
     * Rotates the pitch angle by the specified amount.
     *
     * @param angle the amount to rotate the pitch angle by
     * @return a new instance with the updated pitch angle
     */
    fun rotatePitch(angle: Float): Rotatable

    /**
     * Rotates both the yaw and pitch angles by the specified amounts.
     *
     * @param yaw the amount to rotate the yaw angle by
     * @param pitch the amount to rotate the pitch angle by
     * @return a new instance with the updated yaw and pitch angles
     */
    fun rotate(yaw: Float, pitch: Float): Rotatable

    /**
     * Rotates to look at the specified point.
     *
     * @param point the point to look at
     * @return a new instance with the updated yaw and pitch angles
     */
    fun lookAt(point: Point): Rotatable

    /**
     * Resets the rotation to the default values (yaw = 0, pitch = 0).
     *
     * @return a new instance with the default yaw and pitch angles
     */
    fun resetRotation(): Rotatable

    /**
     * Gets the direction vector based on the current yaw and pitch angles.
     *
     * @return the direction vector
     */
    fun directionVector(): Vector
}