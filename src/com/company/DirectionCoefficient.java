package com.company;

public class DirectionCoefficient {
    Double x;
    Double y;
    Double z;

    public DirectionCoefficient(Double newX, Double newY, Double newZ) {
        x = newX;
        y = newY;
        z = newZ;
    }

    public static DirectionCoefficient  getRandomDirectionCoefficient(DirectionCoefficient oldDirectionCoefficient) {
        Double r = Math.random();
        Double theta = Math.acos(1 - 2 * r);

        Double R = Math.random();
        Double fi = R * 2 * Math.PI;

        Double cosinusFi = Math.cos(fi);
        Double cosinusTheta = Math.cos(theta);
        Double sinusTheta = Math.sin(theta);
        Double sinusFi = Math.sin(fi);
        Double wsp = sinusTheta / Math.sqrt(1 - oldDirectionCoefficient.z * oldDirectionCoefficient.z);

        Double x = wsp * (oldDirectionCoefficient.x * oldDirectionCoefficient.z * cosinusFi - oldDirectionCoefficient.y * sinusFi) + oldDirectionCoefficient.x * cosinusTheta;
        Double y = wsp * (oldDirectionCoefficient.y * oldDirectionCoefficient.z * cosinusFi + oldDirectionCoefficient.x * sinusFi) + oldDirectionCoefficient.y * cosinusFi;
        Double z = -sinusTheta * cosinusFi * Math.sqrt(1 - oldDirectionCoefficient.z * oldDirectionCoefficient.z) + oldDirectionCoefficient.z * cosinusFi;
        return new DirectionCoefficient(x,y,z);
    }
}
