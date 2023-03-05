/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    Copyright © 2005-2023 Open Geospatial Consortium, Inc.
 *    http://www.geoapi.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.opengis.geometry.coordinate;

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Indicates which sort of curve may be approximated by a particular B-spline.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_SplineCurveForm", specification=ISO_19107)
public final class SplineCurveForm extends CodeList<SplineCurveForm> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 7692137703533158212L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<SplineCurveForm> VALUES = new ArrayList<SplineCurveForm>(5);

    /**
     * A connected sequence of line segments represented by a 1 degree B-spline (a line string).
     */
    @UML(identifier="polylineForm", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm POLYLINE_FORM = new SplineCurveForm(
                                       "POLYLINE_FORM");

    /**
     * An arc of a circle or a complete circle.
     */
    @UML(identifier="circularArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm CIRCULAR_ARC = new SplineCurveForm(
                                       "CIRCULAR_ARC");

    /**
     * An arc of an ellipse or a complete ellipse.
     */
    @UML(identifier="ellipticalArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm ELLIPTICAL_ARC = new SplineCurveForm(
                                       "ELLIPTICAL_ARC");

    /**
     * An arc of a finite length of a parabola.
     */
    @UML(identifier="parabolicArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm PARABOLIC_ARC = new SplineCurveForm(
                                       "PARABOLIC_ARC");

    /**
     * An arc of a finite length of one branch of a hyperbola.
     */
    @UML(identifier="hyperbolicArc", obligation=CONDITIONAL, specification=ISO_19107)
    public static final SplineCurveForm HYPERBOLIC_ARC = new SplineCurveForm(
                                       "HYPERBOLIC_ARC");

    /**
     * Constructs an element of the given name. The new element is
     * automatically added to the list returned by {@link #values()}.
     *
     * @param  name  the name of the new element.
     *        This name must not be in use by another element of this type.
     */
    private SplineCurveForm(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code SplineCurveForm}s.
     *
     * @return the list of codes declared in the current JVM.
     */
    public static SplineCurveForm[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(SplineCurveForm[]::new);
        }
    }

    /**
     * Returns the list of codes of the same kind than this code list element.
     * Invoking this method is equivalent to invoking {@link #values()}, except that
     * this method can be invoked on an instance of the parent {@code CodeList} class.
     *
     * @return all code {@linkplain #values() values} for this code list.
     */
    @Override
    public SplineCurveForm[] family() {
        return values();
    }

    /**
     * Returns the spline curve form that matches the given string, or returns a
     * new one if none match it. More specifically, this methods returns the first instance for
     * which <code>{@linkplain #name() name()}.{@linkplain String#equals equals}(code)</code>
     * returns {@code true}. If no existing instance is found, then a new one is created for
     * the given name.
     *
     * @param  code  the name of the code to fetch or to create.
     * @return a code matching the given name.
     */
    public static SplineCurveForm valueOf(String code) {
        return valueOf(SplineCurveForm.class, code);
    }
}
