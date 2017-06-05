/*
 * Copyright (C) 2011-2017 Yanick Poirier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * More details about the licensing can be found at https://www.gnu.org/licenses/lgpl-3.0.en.html.
 */
package moka.awt;

import java.awt.Component;
import java.awt.Dimension;

/**
 *
 * @author Yanick Poirier
 */
public class ComponentUtils {

    /**
     * Retrieves the size of the widest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the widest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getWidestSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getSize().getWidth() < components[i].getSize().getWidth() ) {
                w = i;
            }
        }

        return components[w].getSize();
    }

    /**
     * Retrieves the size of the smallest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the smallest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getSmallestSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getSize().getWidth() > components[i].getSize().getWidth() ) {
                w = i;
            }
        }

        return components[w].getSize();
    }

    /**
     * Retrieves the size of the highest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the highest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getHighestSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getSize().getHeight() < components[i].getSize().getHeight() ) {
                w = i;
            }
        }

        return components[w].getSize();
    }

    /**
     * Retrieves the size of the lowest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the lowest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getLowestSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getSize().getHeight() > components[i].getSize().getHeight() ) {
                w = i;
            }
        }

        return components[w].getSize();
    }

    /**
     * Retrieves the size of the widest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the widest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getWidestPreferredSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getPreferredSize().getWidth() <
                components[i].getPreferredSize().getWidth() ) {
                w = i;
            }
        }

        return components[w].getPreferredSize();
    }

    /**
     * Retrieves the size of the smallest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the smallest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getSmallestPreferredSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getPreferredSize().getWidth() >
                components[i].getPreferredSize().getWidth() ) {
                w = i;
            }
        }

        return components[w].getPreferredSize();
    }

    /**
     * Retrieves the size of the highest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the highest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getHighestPreferredSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getPreferredSize().getHeight() <
                components[i].getPreferredSize().getHeight() ) {
                w = i;
            }
        }

        return components[w].getPreferredSize();
    }

    /**
     * Retrieves the size of the lowest {@linkplain Component} in the specified array. The
     * comparison
     * is based on the value returned by the {@linkplain Component#getSize()} method.
     *
     * @param components Array of components.
     *
     * @return a {@linkplain Dimension} object that is the size of the lowest component.
     *
     * @throws NullPointerException if {@code component} or one of its element is {@code null}.
     */
    static public Dimension getLowestPreferredSize( Component[] components ) {
        int w = 0;

        for( int i = 1; i < components.length; i++ ) {
            if( components[w].getPreferredSize().getHeight() >
                components[i].getPreferredSize().getHeight() ) {
                w = i;
            }
        }

        return components[w].getPreferredSize();
    }

    private ComponentUtils() {
    }

}
