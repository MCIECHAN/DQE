package com.company;

/**
 * Created by ciechan on 2016-01-18.
 */
public class DirectionCooficient {
     Double x;
     Double y;
     Double z;

    public DirectionCooficient( Double newX, Double newY, Double newZ){
        x = newX;
        y = newY;
        z = newZ;
    }

    public DirectionCooficient (DirectionCooficient oldDirectionCooficient){

        Double r = Math.random();
        Double theta = Math.acos(1 - 2 * r);

        Double R = Math.random();
        Double fi = R*2*Math.PI;

        Double cosinusfi = Math.cos(fi);
        Double cosinustheta= Math.cos(theta);
        Double sinustheta = Math.sin(theta);
        Double sinusfi = Math.sin(fi);
        Double wsp = sinustheta/Math.sqrt(1-oldDirectionCooficient.z*oldDirectionCooficient.z);

        x = wsp*(oldDirectionCooficient.x*oldDirectionCooficient.z*cosinusfi-oldDirectionCooficient.y*sinusfi)+oldDirectionCooficient.x*cosinustheta;
        y = wsp*(oldDirectionCooficient.y*oldDirectionCooficient.z*cosinusfi+oldDirectionCooficient.x*sinusfi)+oldDirectionCooficient.y*cosinusfi;
        z = -sinustheta*cosinusfi*Math.sqrt(1-oldDirectionCooficient.z*oldDirectionCooficient.z)+oldDirectionCooficient.z*cosinusfi;
    }
}
