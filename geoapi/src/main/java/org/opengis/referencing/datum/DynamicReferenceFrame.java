/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    Copyright © 2024 Open Geospatial Consortium, Inc.
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
package org.opengis.referencing.datum;

import java.time.temporal.Temporal;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Reference frame in which some of the parameters describe time evolution of defining station coordinates.
 * Example: defining station coordinates having linear velocities to account for crustal motion.
 *
 * @author  OGC Topic 2 (for abstract model and documentation)
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 * @since   3.1
 */
@UML(identifier="DynamicGeodeticReferenceFrame", specification=ISO_19111)
public interface DynamicReferenceFrame extends Datum {
    /**
     * Epoch to which the coordinates of stations defining the dynamic geodetic reference frame are referenced.
     *
     * <h4>Temporal object type</h4>
     * The type of the returned object depends on the epoch accuracy and the calendar in use.
     * For reference frames relative to the Earth, the temporal type should be {@link java.time.Year} if the epoch is
     * merely a year, {@link java.time.YearMonth} or {@link java.time.LocalDate} if a better precision is available,
     * up to {@link java.time.OffsetDateTime} or {@link java.time.Instant} for maximal precision.
     * For reference frames relative to another planet, the time measurement may use a non-Gregorian calendar.
     * In the latter case, the type of the returned temporal object is currently implementation dependent.
     *
     * @return epoch to which the coordinates of stations defining the dynamic geodetic reference frame are referenced.
     *
     * @see org.opengis.coordinate.CoordinateMetadata#getCoordinateEpoch()
     */
    @UML(identifier="frameReferenceEpoch", obligation=MANDATORY, specification=ISO_19111)
    Temporal getFrameReferenceEpoch();
}
