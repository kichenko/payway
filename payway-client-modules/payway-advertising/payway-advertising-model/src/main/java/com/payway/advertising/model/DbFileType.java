/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model;

/**
 * File types
 *
 * @author Sergey Kichenko
 * @created 05.05.15 00:00
 */
public enum DbFileType {

    /**
     * Know nothing about this file
     */
    Unknown,
    /**
     * Static logo to show in the upper
     */
    Logo,
    /**
     * Static banner image on the right
     */
    Banner,
    /**
     * Static banner archive to integrate into
     */
    Archive,
    /**
     * To show in the video player on dual monitor
     */
    Clip,
    /**
     * Short video clip to show in kiosk UI
     */
    Popup
}
